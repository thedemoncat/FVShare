package org.xutils.http.request;

import android.annotation.TargetApi;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import com.lzy.okgo.model.HttpHeaders;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TimeZone;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import org.xutils.cache.DiskCacheEntity;
import org.xutils.cache.LruDiskCache;
import org.xutils.common.util.IOUtil;
import org.xutils.common.util.KeyValue;
import org.xutils.common.util.LogUtil;
import org.xutils.http.BaseParams;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.http.body.ProgressBody;
import org.xutils.http.body.RequestBody;
import org.xutils.http.cookie.DbCookieStore;
import org.xutils.p019ex.HttpException;

public class HttpRequest extends UriRequest {
    private static final CookieManager COOKIE_MANAGER = new CookieManager(DbCookieStore.INSTANCE, CookiePolicy.ACCEPT_ALL);
    private String cacheKey = null;
    private HttpURLConnection connection = null;
    private InputStream inputStream = null;
    private boolean isLoading = false;
    private int responseCode = 0;

    HttpRequest(RequestParams params, Type loadType) throws Throwable {
        super(params, loadType);
    }

    /* access modifiers changed from: protected */
    public String buildQueryUrl(RequestParams params) {
        String uri = params.getUri();
        StringBuilder queryBuilder = new StringBuilder(uri);
        if (!uri.contains("?")) {
            queryBuilder.append("?");
        } else if (!uri.endsWith("?")) {
            queryBuilder.append("&");
        }
        List<KeyValue> queryParams = params.getQueryStringParams();
        if (queryParams != null) {
            for (KeyValue kv : queryParams) {
                String name = kv.key;
                String value = kv.getValueStr();
                if (!TextUtils.isEmpty(name) && value != null) {
                    queryBuilder.append(Uri.encode(name, params.getCharset())).append("=").append(Uri.encode(value, params.getCharset())).append("&");
                }
            }
        }
        if (queryBuilder.charAt(queryBuilder.length() - 1) == '&') {
            queryBuilder.deleteCharAt(queryBuilder.length() - 1);
        }
        if (queryBuilder.charAt(queryBuilder.length() - 1) == '?') {
            queryBuilder.deleteCharAt(queryBuilder.length() - 1);
        }
        return queryBuilder.toString();
    }

    public String getRequestUri() {
        URL url;
        String result = this.queryUrl;
        if (this.connection == null || (url = this.connection.getURL()) == null) {
            return result;
        }
        return url.toString();
    }

    @TargetApi(19)
    public void sendRequest() throws Throwable {
        RequestBody body;
        SSLSocketFactory sslSocketFactory;
        this.isLoading = false;
        this.responseCode = 0;
        URL url = new URL(this.queryUrl);
        Proxy proxy = this.params.getProxy();
        if (proxy != null) {
            this.connection = (HttpURLConnection) url.openConnection(proxy);
        } else {
            this.connection = (HttpURLConnection) url.openConnection();
        }
        if (Build.VERSION.SDK_INT < 19) {
            this.connection.setRequestProperty(HttpHeaders.HEAD_KEY_CONNECTION, HttpHeaders.HEAD_VALUE_CONNECTION_CLOSE);
        }
        this.connection.setReadTimeout(this.params.getConnectTimeout());
        this.connection.setConnectTimeout(this.params.getConnectTimeout());
        this.connection.setInstanceFollowRedirects(this.params.getRedirectHandler() == null);
        if ((this.connection instanceof HttpsURLConnection) && (sslSocketFactory = this.params.getSslSocketFactory()) != null) {
            ((HttpsURLConnection) this.connection).setSSLSocketFactory(sslSocketFactory);
        }
        if (this.params.isUseCookie()) {
            try {
                List<String> cookies = COOKIE_MANAGER.get(url.toURI(), new HashMap(0)).get(HttpHeaders.HEAD_KEY_COOKIE);
                if (cookies != null) {
                    this.connection.setRequestProperty(HttpHeaders.HEAD_KEY_COOKIE, TextUtils.join(";", cookies));
                }
            } catch (Throwable ex) {
                LogUtil.m1565e(ex.getMessage(), ex);
            }
        }
        List<BaseParams.Header> headers = this.params.getHeaders();
        if (headers != null) {
            for (BaseParams.Header header : headers) {
                String name = header.key;
                String value = header.getValueStr();
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(value)) {
                    if (header.setHeader) {
                        this.connection.setRequestProperty(name, value);
                    } else {
                        this.connection.addRequestProperty(name, value);
                    }
                }
            }
        }
        if (this.requestInterceptListener != null) {
            this.requestInterceptListener.beforeRequest(this);
        }
        HttpMethod method = this.params.getMethod();
        try {
            this.connection.setRequestMethod(method.toString());
        } catch (ProtocolException ex2) {
            Field methodField = HttpURLConnection.class.getDeclaredField("method");
            methodField.setAccessible(true);
            methodField.set(this.connection, method.toString());
        }
        if (HttpMethod.permitsRequestBody(method) && (body = this.params.getRequestBody()) != null) {
            if (body instanceof ProgressBody) {
                ((ProgressBody) body).setProgressHandler(this.progressHandler);
            }
            String contentType = body.getContentType();
            if (!TextUtils.isEmpty(contentType)) {
                this.connection.setRequestProperty(HttpHeaders.HEAD_KEY_CONTENT_TYPE, contentType);
            }
            long contentLength = body.getContentLength();
            if (contentLength < 0) {
                this.connection.setChunkedStreamingMode(262144);
            } else if (contentLength < 2147483647L) {
                this.connection.setFixedLengthStreamingMode((int) contentLength);
            } else if (Build.VERSION.SDK_INT >= 19) {
                this.connection.setFixedLengthStreamingMode(contentLength);
            } else {
                this.connection.setChunkedStreamingMode(262144);
            }
            this.connection.setRequestProperty(HttpHeaders.HEAD_KEY_CONTENT_LENGTH, String.valueOf(contentLength));
            this.connection.setDoOutput(true);
            body.writeTo(this.connection.getOutputStream());
        }
        if (this.params.isUseCookie()) {
            try {
                Map<String, List<String>> headers2 = this.connection.getHeaderFields();
                if (headers2 != null) {
                    COOKIE_MANAGER.put(url.toURI(), headers2);
                }
            } catch (Throwable ex3) {
                LogUtil.m1565e(ex3.getMessage(), ex3);
            }
        }
        this.responseCode = this.connection.getResponseCode();
        if (this.requestInterceptListener != null) {
            this.requestInterceptListener.afterRequest(this);
        }
        if (this.responseCode == 204 || this.responseCode == 205) {
            throw new HttpException(this.responseCode, getResponseMessage());
        } else if (this.responseCode >= 300) {
            HttpException httpException = new HttpException(this.responseCode, getResponseMessage());
            try {
                httpException.setResult(IOUtil.readStr(getInputStream(), this.params.getCharset()));
            } catch (Throwable th) {
            }
            LogUtil.m1564e(httpException.toString() + ", url: " + this.queryUrl);
            throw httpException;
        } else {
            this.isLoading = true;
            return;
        }
    }

    public boolean isLoading() {
        return this.isLoading;
    }

    public String getCacheKey() {
        if (this.cacheKey == null) {
            this.cacheKey = this.params.getCacheKey();
            if (TextUtils.isEmpty(this.cacheKey)) {
                this.cacheKey = this.params.toString();
            }
        }
        return this.cacheKey;
    }

    public Object loadResult() throws Throwable {
        this.isLoading = true;
        return super.loadResult();
    }

    public Object loadResultFromCache() throws Throwable {
        this.isLoading = true;
        DiskCacheEntity cacheEntity = LruDiskCache.getDiskCache(this.params.getCacheDirName()).setMaxSize(this.params.getCacheSize()).get(getCacheKey());
        if (cacheEntity == null) {
            return null;
        }
        if (HttpMethod.permitsCache(this.params.getMethod())) {
            Date lastModified = cacheEntity.getLastModify();
            if (lastModified.getTime() > 0) {
                this.params.setHeader(HttpHeaders.HEAD_KEY_IF_MODIFIED_SINCE, toGMTString(lastModified));
            }
            String eTag = cacheEntity.getEtag();
            if (!TextUtils.isEmpty(eTag)) {
                this.params.setHeader(HttpHeaders.HEAD_KEY_IF_NONE_MATCH, eTag);
            }
        }
        return this.loader.loadFromCache(cacheEntity);
    }

    public void clearCacheHeader() {
        this.params.setHeader(HttpHeaders.HEAD_KEY_IF_MODIFIED_SINCE, (String) null);
        this.params.setHeader(HttpHeaders.HEAD_KEY_IF_NONE_MATCH, (String) null);
    }

    public InputStream getInputStream() throws IOException {
        if (this.connection != null && this.inputStream == null) {
            this.inputStream = this.connection.getResponseCode() >= 400 ? this.connection.getErrorStream() : this.connection.getInputStream();
        }
        return this.inputStream;
    }

    public void close() throws IOException {
        if (this.inputStream != null) {
            IOUtil.closeQuietly((Closeable) this.inputStream);
            this.inputStream = null;
        }
        if (this.connection != null) {
            this.connection.disconnect();
        }
    }

    public long getContentLength() {
        long result = 0;
        if (this.connection != null) {
            try {
                result = (long) this.connection.getContentLength();
            } catch (Throwable ex) {
                LogUtil.m1565e(ex.getMessage(), ex);
            }
            if (result >= 1) {
                return result;
            }
            try {
                return (long) getInputStream().available();
            } catch (Throwable th) {
                return result;
            }
        } else {
            try {
                return (long) getInputStream().available();
            } catch (Throwable th2) {
                return 0;
            }
        }
    }

    public int getResponseCode() throws IOException {
        if (this.connection != null) {
            return this.responseCode;
        }
        if (getInputStream() != null) {
            return 200;
        }
        return 404;
    }

    public String getResponseMessage() throws IOException {
        if (this.connection != null) {
            return URLDecoder.decode(this.connection.getResponseMessage(), this.params.getCharset());
        }
        return null;
    }

    public long getExpiration() {
        if (this.connection == null) {
            return -1;
        }
        long expiration = -1;
        String cacheControl = this.connection.getHeaderField(HttpHeaders.HEAD_KEY_CACHE_CONTROL);
        if (!TextUtils.isEmpty(cacheControl)) {
            StringTokenizer tok = new StringTokenizer(cacheControl, ",");
            while (true) {
                if (!tok.hasMoreTokens()) {
                    break;
                }
                String token = tok.nextToken().trim().toLowerCase();
                if (token.startsWith("max-age")) {
                    int eqIdx = token.indexOf(61);
                    if (eqIdx > 0) {
                        try {
                            long seconds = Long.parseLong(token.substring(eqIdx + 1).trim());
                            if (seconds > 0) {
                                expiration = System.currentTimeMillis() + (1000 * seconds);
                            }
                        } catch (Throwable ex) {
                            LogUtil.m1565e(ex.getMessage(), ex);
                        }
                    }
                }
            }
        }
        if (expiration <= 0) {
            expiration = this.connection.getExpiration();
        }
        if (expiration <= 0 && this.params.getCacheMaxAge() > 0) {
            expiration = System.currentTimeMillis() + this.params.getCacheMaxAge();
        }
        if (expiration <= 0) {
            return Long.MAX_VALUE;
        }
        return expiration;
    }

    public long getLastModified() {
        return getHeaderFieldDate(HttpHeaders.HEAD_KEY_LAST_MODIFIED, System.currentTimeMillis());
    }

    public String getETag() {
        if (this.connection == null) {
            return null;
        }
        return this.connection.getHeaderField(HttpHeaders.HEAD_KEY_E_TAG);
    }

    public String getResponseHeader(String name) {
        if (this.connection == null) {
            return null;
        }
        return this.connection.getHeaderField(name);
    }

    public Map<String, List<String>> getResponseHeaders() {
        if (this.connection == null) {
            return null;
        }
        return this.connection.getHeaderFields();
    }

    public long getHeaderFieldDate(String name, long defaultValue) {
        return this.connection == null ? defaultValue : this.connection.getHeaderFieldDate(name, defaultValue);
    }

    private static String toGMTString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(HttpHeaders.FORMAT_HTTP_DATA, Locale.US);
        TimeZone gmtZone = TimeZone.getTimeZone("GMT");
        sdf.setTimeZone(gmtZone);
        new GregorianCalendar(gmtZone).setTimeInMillis(date.getTime());
        return sdf.format(date);
    }
}
