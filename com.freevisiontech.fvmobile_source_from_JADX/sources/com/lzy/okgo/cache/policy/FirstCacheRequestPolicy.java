package com.lzy.okgo.cache.policy;

import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.Request;

public class FirstCacheRequestPolicy<T> extends BaseCachePolicy<T> {
    public FirstCacheRequestPolicy(Request<T, ? extends Request> request) {
        super(request);
    }

    public void onSuccess(final Response<T> success) {
        runOnUiThread(new Runnable() {
            public void run() {
                FirstCacheRequestPolicy.this.mCallback.onSuccess(success);
                FirstCacheRequestPolicy.this.mCallback.onFinish();
            }
        });
    }

    public void onError(final Response<T> error) {
        runOnUiThread(new Runnable() {
            public void run() {
                FirstCacheRequestPolicy.this.mCallback.onError(error);
                FirstCacheRequestPolicy.this.mCallback.onFinish();
            }
        });
    }

    public Response<T> requestSync(CacheEntity<T> cacheEntity) {
        try {
            prepareRawCall();
            if (cacheEntity != null) {
                Response.success(true, cacheEntity.getData(), this.rawCall, (okhttp3.Response) null);
            }
            Response<T> response = requestNetworkSync();
            if (response.isSuccessful() || cacheEntity == null) {
                return response;
            }
            return Response.success(true, cacheEntity.getData(), this.rawCall, response.getRawResponse());
        } catch (Throwable throwable) {
            return Response.error(false, this.rawCall, (okhttp3.Response) null, throwable);
        }
    }

    public void requestAsync(final CacheEntity<T> cacheEntity, Callback<T> callback) {
        this.mCallback = callback;
        runOnUiThread(new Runnable() {
            public void run() {
                FirstCacheRequestPolicy.this.mCallback.onStart(FirstCacheRequestPolicy.this.request);
                try {
                    FirstCacheRequestPolicy.this.prepareRawCall();
                    if (cacheEntity != null) {
                        FirstCacheRequestPolicy.this.mCallback.onCacheSuccess(Response.success(true, cacheEntity.getData(), FirstCacheRequestPolicy.this.rawCall, (okhttp3.Response) null));
                    }
                    FirstCacheRequestPolicy.this.requestNetworkAsync();
                } catch (Throwable throwable) {
                    Response.error(false, FirstCacheRequestPolicy.this.rawCall, (okhttp3.Response) null, throwable);
                }
            }
        });
    }
}
