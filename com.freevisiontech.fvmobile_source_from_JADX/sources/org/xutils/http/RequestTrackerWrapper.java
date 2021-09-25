package org.xutils.http;

import org.xutils.common.util.LogUtil;
import org.xutils.http.app.RequestTracker;
import org.xutils.http.request.UriRequest;

final class RequestTrackerWrapper implements RequestTracker {
    private final RequestTracker base;

    public RequestTrackerWrapper(RequestTracker base2) {
        this.base = base2;
    }

    public void onWaiting(RequestParams params) {
        try {
            this.base.onWaiting(params);
        } catch (Throwable ex) {
            LogUtil.m1565e(ex.getMessage(), ex);
        }
    }

    public void onStart(RequestParams params) {
        try {
            this.base.onStart(params);
        } catch (Throwable ex) {
            LogUtil.m1565e(ex.getMessage(), ex);
        }
    }

    public void onRequestCreated(UriRequest request) {
        try {
            this.base.onRequestCreated(request);
        } catch (Throwable ex) {
            LogUtil.m1565e(ex.getMessage(), ex);
        }
    }

    public void onCache(UriRequest request, Object result) {
        try {
            this.base.onCache(request, result);
        } catch (Throwable ex) {
            LogUtil.m1565e(ex.getMessage(), ex);
        }
    }

    public void onSuccess(UriRequest request, Object result) {
        try {
            this.base.onSuccess(request, result);
        } catch (Throwable ex) {
            LogUtil.m1565e(ex.getMessage(), ex);
        }
    }

    public void onCancelled(UriRequest request) {
        try {
            this.base.onCancelled(request);
        } catch (Throwable ex) {
            LogUtil.m1565e(ex.getMessage(), ex);
        }
    }

    public void onError(UriRequest request, Throwable ex, boolean isCallbackError) {
        try {
            this.base.onError(request, ex, isCallbackError);
        } catch (Throwable exOnError) {
            LogUtil.m1565e(exOnError.getMessage(), exOnError);
        }
    }

    public void onFinished(UriRequest request) {
        try {
            this.base.onFinished(request);
        } catch (Throwable ex) {
            LogUtil.m1565e(ex.getMessage(), ex);
        }
    }
}
