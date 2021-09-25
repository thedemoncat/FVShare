package com.lzy.okgo.https;

import com.lzy.okgo.utils.OkLogger;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class HttpsUtils {
    public static HostnameVerifier UnSafeHostnameVerifier = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };
    public static X509TrustManager UnSafeTrustManager = new X509TrustManager() {
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    };

    public static class SSLParams {
        public SSLSocketFactory sSLSocketFactory;
        public X509TrustManager trustManager;
    }

    public static SSLParams getSslSocketFactory() {
        return getSslSocketFactoryBase((X509TrustManager) null, (InputStream) null, (String) null, new InputStream[0]);
    }

    public static SSLParams getSslSocketFactory(X509TrustManager trustManager) {
        return getSslSocketFactoryBase(trustManager, (InputStream) null, (String) null, new InputStream[0]);
    }

    public static SSLParams getSslSocketFactory(InputStream... certificates) {
        return getSslSocketFactoryBase((X509TrustManager) null, (InputStream) null, (String) null, certificates);
    }

    public static SSLParams getSslSocketFactory(InputStream bksFile, String password, InputStream... certificates) {
        return getSslSocketFactoryBase((X509TrustManager) null, bksFile, password, certificates);
    }

    public static SSLParams getSslSocketFactory(InputStream bksFile, String password, X509TrustManager trustManager) {
        return getSslSocketFactoryBase(trustManager, bksFile, password, new InputStream[0]);
    }

    private static SSLParams getSslSocketFactoryBase(X509TrustManager trustManager, InputStream bksFile, String password, InputStream... certificates) {
        X509TrustManager manager;
        SSLParams sslParams = new SSLParams();
        try {
            KeyManager[] keyManagers = prepareKeyManager(bksFile, password);
            TrustManager[] trustManagers = prepareTrustManager(certificates);
            if (trustManager != null) {
                manager = trustManager;
            } else if (trustManagers != null) {
                manager = chooseTrustManager(trustManagers);
            } else {
                manager = UnSafeTrustManager;
            }
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagers, new TrustManager[]{manager}, (SecureRandom) null);
            sslParams.sSLSocketFactory = sslContext.getSocketFactory();
            sslParams.trustManager = manager;
            return sslParams;
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        } catch (KeyManagementException e2) {
            throw new AssertionError(e2);
        }
    }

    private static KeyManager[] prepareKeyManager(InputStream bksFile, String password) {
        if (bksFile == null || password == null) {
            return null;
        }
        try {
            KeyStore clientKeyStore = KeyStore.getInstance("BKS");
            clientKeyStore.load(bksFile, password.toCharArray());
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(clientKeyStore, password.toCharArray());
            return kmf.getKeyManagers();
        } catch (Exception e) {
            OkLogger.printStackTrace(e);
            return null;
        }
    }

    private static TrustManager[] prepareTrustManager(InputStream... certificates) {
        if (certificates == null || certificates.length <= 0) {
            return null;
        }
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load((KeyStore.LoadStoreParameter) null);
            int length = certificates.length;
            int i = 0;
            int index = 0;
            while (i < length) {
                InputStream certStream = certificates[i];
                int index2 = index + 1;
                keyStore.setCertificateEntry(Integer.toString(index), certificateFactory.generateCertificate(certStream));
                if (certStream != null) {
                    try {
                        certStream.close();
                    } catch (IOException e) {
                        OkLogger.printStackTrace(e);
                    }
                }
                i++;
                index = index2;
            }
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);
            return tmf.getTrustManagers();
        } catch (Exception e2) {
            OkLogger.printStackTrace(e2);
            return null;
        }
    }

    private static X509TrustManager chooseTrustManager(TrustManager[] trustManagers) {
        for (TrustManager trustManager : trustManagers) {
            if (trustManager instanceof X509TrustManager) {
                return (X509TrustManager) trustManager;
            }
        }
        return null;
    }
}
