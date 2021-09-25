package org.xutils.http.app;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpRequest;

public class DefaultParamsBuilder implements ParamsBuilder {
    private static SSLSocketFactory trustAllSSlSocketFactory;

    public String buildUri(RequestParams params, HttpRequest httpRequest) throws Throwable {
        return httpRequest.host() + "/" + httpRequest.path();
    }

    public String buildCacheKey(RequestParams params, String[] cacheKeys) {
        String cacheKey = null;
        if (cacheKeys != null && cacheKeys.length > 0) {
            cacheKey = params.getUri() + "?";
            for (String key : cacheKeys) {
                String value = params.getStringParameter(key);
                if (value != null) {
                    cacheKey = cacheKey + key + "=" + value + "&";
                }
            }
        }
        return cacheKey;
    }

    public SSLSocketFactory getSSLSocketFactory() throws Throwable {
        return getTrustAllSSLSocketFactory();
    }

    public void buildParams(RequestParams params) throws Throwable {
    }

    public void buildSign(RequestParams params, String[] signs) throws Throwable {
    }

    public static SSLSocketFactory getTrustAllSSLSocketFactory() {
        if (trustAllSSlSocketFactory == null) {
            synchronized (DefaultParamsBuilder.class) {
                if (trustAllSSlSocketFactory == null) {
                    TrustManager[] trustAllCerts = {new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }};
                    try {
                        SSLContext sslContext = SSLContext.getInstance("TLS");
                        sslContext.init((KeyManager[]) null, trustAllCerts, (SecureRandom) null);
                        trustAllSSlSocketFactory = sslContext.getSocketFactory();
                    } catch (Throwable ex) {
                        LogUtil.m1565e(ex.getMessage(), ex);
                    }
                }
            }
        }
        return trustAllSSlSocketFactory;
    }
}
