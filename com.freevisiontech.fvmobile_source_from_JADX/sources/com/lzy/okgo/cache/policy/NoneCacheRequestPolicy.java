package com.lzy.okgo.cache.policy;

import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.Request;

public class NoneCacheRequestPolicy<T> extends BaseCachePolicy<T> {
    public NoneCacheRequestPolicy(Request<T, ? extends Request> request) {
        super(request);
    }

    public void onSuccess(final Response<T> success) {
        runOnUiThread(new Runnable() {
            public void run() {
                NoneCacheRequestPolicy.this.mCallback.onSuccess(success);
                NoneCacheRequestPolicy.this.mCallback.onFinish();
            }
        });
    }

    public void onError(final Response<T> error) {
        runOnUiThread(new Runnable() {
            public void run() {
                NoneCacheRequestPolicy.this.mCallback.onError(error);
                NoneCacheRequestPolicy.this.mCallback.onFinish();
            }
        });
    }

    public Response<T> requestSync(CacheEntity<T> cacheEntity) {
        try {
            prepareRawCall();
            Response<T> response = null;
            if (cacheEntity != null) {
                response = Response.success(true, cacheEntity.getData(), this.rawCall, (okhttp3.Response) null);
            }
            if (response == null) {
                return requestNetworkSync();
            }
            return response;
        } catch (Throwable throwable) {
            return Response.error(false, this.rawCall, (okhttp3.Response) null, throwable);
        }
    }

    public void requestAsync(final CacheEntity<T> cacheEntity, Callback<T> callback) {
        this.mCallback = callback;
        runOnUiThread(new Runnable() {
            public void run() {
                NoneCacheRequestPolicy.this.mCallback.onStart(NoneCacheRequestPolicy.this.request);
                try {
                    NoneCacheRequestPolicy.this.prepareRawCall();
                    if (cacheEntity != null) {
                        NoneCacheRequestPolicy.this.mCallback.onCacheSuccess(Response.success(true, cacheEntity.getData(), NoneCacheRequestPolicy.this.rawCall, (okhttp3.Response) null));
                        NoneCacheRequestPolicy.this.mCallback.onFinish();
                        return;
                    }
                    NoneCacheRequestPolicy.this.requestNetworkAsync();
                } catch (Throwable throwable) {
                    Response.error(false, NoneCacheRequestPolicy.this.rawCall, (okhttp3.Response) null, throwable);
                }
            }
        });
    }
}
