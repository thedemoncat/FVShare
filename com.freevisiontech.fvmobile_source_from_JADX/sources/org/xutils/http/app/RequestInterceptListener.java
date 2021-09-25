package org.xutils.http.app;

import org.xutils.http.request.UriRequest;

public interface RequestInterceptListener {
    void afterRequest(UriRequest uriRequest) throws Throwable;

    void beforeRequest(UriRequest uriRequest) throws Throwable;
}
