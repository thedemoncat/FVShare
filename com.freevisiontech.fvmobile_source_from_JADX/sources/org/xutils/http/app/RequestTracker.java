package org.xutils.http.app;

import org.xutils.http.RequestParams;
import org.xutils.http.request.UriRequest;

public interface RequestTracker {
    void onCache(UriRequest uriRequest, Object obj);

    void onCancelled(UriRequest uriRequest);

    void onError(UriRequest uriRequest, Throwable th, boolean z);

    void onFinished(UriRequest uriRequest);

    void onRequestCreated(UriRequest uriRequest);

    void onStart(RequestParams requestParams);

    void onSuccess(UriRequest uriRequest, Object obj);

    void onWaiting(RequestParams requestParams);
}
