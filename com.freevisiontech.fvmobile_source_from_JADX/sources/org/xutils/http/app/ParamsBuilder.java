package org.xutils.http.app;

import javax.net.ssl.SSLSocketFactory;
import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpRequest;

public interface ParamsBuilder {
    String buildCacheKey(RequestParams requestParams, String[] strArr);

    void buildParams(RequestParams requestParams) throws Throwable;

    void buildSign(RequestParams requestParams, String[] strArr) throws Throwable;

    String buildUri(RequestParams requestParams, HttpRequest httpRequest) throws Throwable;

    SSLSocketFactory getSSLSocketFactory() throws Throwable;
}
