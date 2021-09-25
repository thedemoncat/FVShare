package okhttp3.internal.http;

import com.lzy.okgo.model.HttpHeaders;
import java.io.IOException;
import java.util.List;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;
import okhttp3.internal.Version;
import okio.GzipSource;
import okio.Okio;
import okio.Source;

public final class BridgeInterceptor implements Interceptor {
    private final CookieJar cookieJar;

    public BridgeInterceptor(CookieJar cookieJar2) {
        this.cookieJar = cookieJar2;
    }

    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request userRequest = chain.request();
        Request.Builder requestBuilder = userRequest.newBuilder();
        RequestBody body = userRequest.body();
        if (body != null) {
            MediaType contentType = body.contentType();
            if (contentType != null) {
                requestBuilder.header(HttpHeaders.HEAD_KEY_CONTENT_TYPE, contentType.toString());
            }
            long contentLength = body.contentLength();
            if (contentLength != -1) {
                requestBuilder.header(HttpHeaders.HEAD_KEY_CONTENT_LENGTH, Long.toString(contentLength));
                requestBuilder.removeHeader("Transfer-Encoding");
            } else {
                requestBuilder.header("Transfer-Encoding", "chunked");
                requestBuilder.removeHeader(HttpHeaders.HEAD_KEY_CONTENT_LENGTH);
            }
        }
        if (userRequest.header("Host") == null) {
            requestBuilder.header("Host", Util.hostHeader(userRequest.url(), false));
        }
        if (userRequest.header(HttpHeaders.HEAD_KEY_CONNECTION) == null) {
            requestBuilder.header(HttpHeaders.HEAD_KEY_CONNECTION, "Keep-Alive");
        }
        boolean transparentGzip = false;
        if (userRequest.header(HttpHeaders.HEAD_KEY_ACCEPT_ENCODING) == null && userRequest.header(HttpHeaders.HEAD_KEY_RANGE) == null) {
            transparentGzip = true;
            requestBuilder.header(HttpHeaders.HEAD_KEY_ACCEPT_ENCODING, "gzip");
        }
        List<Cookie> cookies = this.cookieJar.loadForRequest(userRequest.url());
        if (!cookies.isEmpty()) {
            requestBuilder.header(HttpHeaders.HEAD_KEY_COOKIE, cookieHeader(cookies));
        }
        if (userRequest.header(HttpHeaders.HEAD_KEY_USER_AGENT) == null) {
            requestBuilder.header(HttpHeaders.HEAD_KEY_USER_AGENT, Version.userAgent());
        }
        Response networkResponse = chain.proceed(requestBuilder.build());
        HttpHeaders.receiveHeaders(this.cookieJar, userRequest.url(), networkResponse.headers());
        Response.Builder responseBuilder = networkResponse.newBuilder().request(userRequest);
        if (transparentGzip && "gzip".equalsIgnoreCase(networkResponse.header(HttpHeaders.HEAD_KEY_CONTENT_ENCODING)) && HttpHeaders.hasBody(networkResponse)) {
            GzipSource responseBody = new GzipSource(networkResponse.body().source());
            Headers strippedHeaders = networkResponse.headers().newBuilder().removeAll(HttpHeaders.HEAD_KEY_CONTENT_ENCODING).removeAll(HttpHeaders.HEAD_KEY_CONTENT_LENGTH).build();
            responseBuilder.headers(strippedHeaders);
            responseBuilder.body(new RealResponseBody(strippedHeaders, Okio.buffer((Source) responseBody)));
        }
        return responseBuilder.build();
    }

    private String cookieHeader(List<Cookie> cookies) {
        StringBuilder cookieHeader = new StringBuilder();
        int size = cookies.size();
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                cookieHeader.append("; ");
            }
            Cookie cookie = cookies.get(i);
            cookieHeader.append(cookie.name()).append('=').append(cookie.value());
        }
        return cookieHeader.toString();
    }
}
