package com.google.android.vending.expansion.downloader.impl;

import android.content.ContentResolver;
import android.content.Context;
import android.net.SSLCertificateSocketFactory;
import android.os.Looper;
import android.util.Log;
import com.lzy.okgo.cookie.SerializableCookie;
import com.lzy.okgo.model.HttpHeaders;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.RequestWrapper;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AndroidHttpClient implements HttpClient {
    public static long DEFAULT_SYNC_MIN_GZIP_BYTES = 256;
    private static final Logger LOG = LoggerFactory.getLogger(TAG);
    private static final int SOCKET_OPERATION_TIMEOUT = 60000;
    private static final String TAG = "AndroidHttpClient";
    static Class<?> sSslSessionCacheClass;
    /* access modifiers changed from: private */
    public static final HttpRequestInterceptor sThreadCheckInterceptor = new HttpRequestInterceptor() {
        public void process(HttpRequest request, HttpContext context) {
            if (Looper.myLooper() != null && Looper.myLooper() == Looper.getMainLooper()) {
                throw new RuntimeException("This thread forbids HTTP requests");
            }
        }
    };
    /* access modifiers changed from: private */
    public volatile LoggingConfiguration curlConfiguration;
    private final HttpClient delegate;
    private RuntimeException mLeakedException = new IllegalStateException("AndroidHttpClient created and never closed");

    static {
        try {
            sSslSessionCacheClass = Class.forName("android.net.SSLSessionCache");
        } catch (Exception e) {
        }
    }

    public static AndroidHttpClient newInstance(String userAgent, Context context) {
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setStaleCheckingEnabled(params, false);
        HttpConnectionParams.setConnectionTimeout(params, SOCKET_OPERATION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, SOCKET_OPERATION_TIMEOUT);
        HttpConnectionParams.setSocketBufferSize(params, 8192);
        HttpClientParams.setRedirecting(params, false);
        Object sessionCache = null;
        if (!(context == null || sSslSessionCacheClass == null)) {
            try {
                sessionCache = sSslSessionCacheClass.getConstructor(new Class[]{Context.class}).newInstance(new Object[]{context});
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e2) {
                e2.printStackTrace();
            } catch (IllegalArgumentException e3) {
                e3.printStackTrace();
            } catch (InstantiationException e4) {
                e4.printStackTrace();
            } catch (IllegalAccessException e5) {
                e5.printStackTrace();
            } catch (InvocationTargetException e6) {
                e6.printStackTrace();
            }
        }
        HttpProtocolParams.setUserAgent(params, userAgent);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        SocketFactory sslCertificateSocketFactory = null;
        if (sessionCache != null) {
            Class<SSLCertificateSocketFactory> cls = SSLCertificateSocketFactory.class;
            try {
                sslCertificateSocketFactory = (SocketFactory) cls.getDeclaredMethod("getHttpSocketFactory", new Class[]{Integer.TYPE, sSslSessionCacheClass}).invoke((Object) null, new Object[]{Integer.valueOf(SOCKET_OPERATION_TIMEOUT), sessionCache});
            } catch (SecurityException e7) {
                e7.printStackTrace();
            } catch (NoSuchMethodException e8) {
                e8.printStackTrace();
            } catch (IllegalArgumentException e9) {
                e9.printStackTrace();
            } catch (IllegalAccessException e10) {
                e10.printStackTrace();
            } catch (InvocationTargetException e11) {
                e11.printStackTrace();
            }
        }
        if (sslCertificateSocketFactory == null) {
            sslCertificateSocketFactory = SSLSocketFactory.getSocketFactory();
        }
        schemeRegistry.register(new Scheme("https", sslCertificateSocketFactory, 443));
        return new AndroidHttpClient(new ThreadSafeClientConnManager(params, schemeRegistry), params);
    }

    public static AndroidHttpClient newInstance(String userAgent) {
        return newInstance(userAgent, (Context) null);
    }

    private AndroidHttpClient(ClientConnectionManager ccm, HttpParams params) {
        this.delegate = new DefaultHttpClient(ccm, params) {
            /* access modifiers changed from: protected */
            public BasicHttpProcessor createHttpProcessor() {
                BasicHttpProcessor processor = AndroidHttpClient.super.createHttpProcessor();
                processor.addRequestInterceptor(AndroidHttpClient.sThreadCheckInterceptor);
                processor.addRequestInterceptor(new CurlLogger());
                return processor;
            }

            /* access modifiers changed from: protected */
            public HttpContext createHttpContext() {
                HttpContext context = new BasicHttpContext();
                context.setAttribute("http.authscheme-registry", getAuthSchemes());
                context.setAttribute("http.cookiespec-registry", getCookieSpecs());
                context.setAttribute("http.auth.credentials-provider", getCredentialsProvider());
                return context;
            }
        };
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        super.finalize();
        if (this.mLeakedException != null) {
            LOG.error("Leak found", (Throwable) this.mLeakedException);
            this.mLeakedException = null;
        }
    }

    public static void modifyRequestToAcceptGzipResponse(HttpRequest request) {
        request.addHeader(HttpHeaders.HEAD_KEY_ACCEPT_ENCODING, "gzip");
    }

    public static InputStream getUngzippedContent(HttpEntity entity) throws IOException {
        InputStream responseStream = entity.getContent();
        if (responseStream == null) {
            return responseStream;
        }
        Header header = entity.getContentEncoding();
        if (header == null) {
            return responseStream;
        }
        String contentEncoding = header.getValue();
        if (contentEncoding == null) {
            return responseStream;
        }
        if (contentEncoding.contains("gzip")) {
            responseStream = new GZIPInputStream(responseStream);
        }
        return responseStream;
    }

    public void close() {
        if (this.mLeakedException != null) {
            getConnectionManager().shutdown();
            this.mLeakedException = null;
        }
    }

    public HttpParams getParams() {
        return this.delegate.getParams();
    }

    public ClientConnectionManager getConnectionManager() {
        return this.delegate.getConnectionManager();
    }

    public HttpResponse execute(HttpUriRequest request) throws IOException {
        return this.delegate.execute(request);
    }

    public HttpResponse execute(HttpUriRequest request, HttpContext context) throws IOException {
        return this.delegate.execute(request, context);
    }

    public HttpResponse execute(HttpHost target, HttpRequest request) throws IOException {
        return this.delegate.execute(target, request);
    }

    public HttpResponse execute(HttpHost target, HttpRequest request, HttpContext context) throws IOException {
        return this.delegate.execute(target, request, context);
    }

    public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
        return this.delegate.execute(request, responseHandler);
    }

    public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException, ClientProtocolException {
        return this.delegate.execute(request, responseHandler, context);
    }

    public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
        return this.delegate.execute(target, request, responseHandler);
    }

    public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException, ClientProtocolException {
        return this.delegate.execute(target, request, responseHandler, context);
    }

    public static AbstractHttpEntity getCompressedEntity(byte[] data, ContentResolver resolver) throws IOException {
        if (((long) data.length) < getMinGzipSize(resolver)) {
            return new ByteArrayEntity(data);
        }
        ByteArrayOutputStream arr = new ByteArrayOutputStream();
        OutputStream zipper = new GZIPOutputStream(arr);
        zipper.write(data);
        zipper.close();
        AbstractHttpEntity entity = new ByteArrayEntity(arr.toByteArray());
        entity.setContentEncoding("gzip");
        return entity;
    }

    public static long getMinGzipSize(ContentResolver resolver) {
        return DEFAULT_SYNC_MIN_GZIP_BYTES;
    }

    private static class LoggingConfiguration {
        private final int level;
        private final String tag;

        private LoggingConfiguration(String tag2, int level2) {
            this.tag = tag2;
            this.level = level2;
        }

        /* access modifiers changed from: private */
        public boolean isLoggable() {
            return Log.isLoggable(this.tag, this.level);
        }

        /* access modifiers changed from: private */
        public void println(String message) {
            Log.println(this.level, this.tag, message);
        }
    }

    public void enableCurlLogging(String name, int level) {
        if (name == null) {
            throw new NullPointerException(SerializableCookie.NAME);
        } else if (level < 2 || level > 7) {
            throw new IllegalArgumentException("Level is out of range [2..7]");
        } else {
            this.curlConfiguration = new LoggingConfiguration(name, level);
        }
    }

    public void disableCurlLogging() {
        this.curlConfiguration = null;
    }

    private class CurlLogger implements HttpRequestInterceptor {
        private CurlLogger() {
        }

        public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
            LoggingConfiguration configuration = AndroidHttpClient.this.curlConfiguration;
            if (configuration != null && configuration.isLoggable() && (request instanceof HttpUriRequest)) {
                configuration.println(AndroidHttpClient.toCurl((HttpUriRequest) request, false));
            }
        }
    }

    /* access modifiers changed from: private */
    public static String toCurl(HttpUriRequest request, boolean logAuthToken) throws IOException {
        HttpEntity entity;
        StringBuilder builder = new StringBuilder();
        builder.append("curl ");
        for (Header header : request.getAllHeaders()) {
            if (logAuthToken || (!header.getName().equals("Authorization") && !header.getName().equals(HttpHeaders.HEAD_KEY_COOKIE))) {
                builder.append("--header \"");
                builder.append(header.toString().trim());
                builder.append("\" ");
            }
        }
        URI uri = request.getURI();
        if (request instanceof RequestWrapper) {
            HttpRequest original = ((RequestWrapper) request).getOriginal();
            if (original instanceof HttpUriRequest) {
                uri = ((HttpUriRequest) original).getURI();
            }
        }
        builder.append("\"");
        builder.append(uri);
        builder.append("\"");
        if ((request instanceof HttpEntityEnclosingRequest) && (entity = ((HttpEntityEnclosingRequest) request).getEntity()) != null && entity.isRepeatable()) {
            if (entity.getContentLength() < 1024) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                entity.writeTo(stream);
                builder.append(" --data-ascii \"").append(stream.toString()).append("\"");
            } else {
                builder.append(" [TOO MUCH DATA TO INCLUDE]");
            }
        }
        return builder.toString();
    }

    public static long parseDate(String dateString) {
        return HttpDateTime.parse(dateString);
    }
}
