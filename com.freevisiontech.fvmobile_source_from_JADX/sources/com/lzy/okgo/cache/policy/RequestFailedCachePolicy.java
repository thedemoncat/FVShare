package com.lzy.okgo.cache.policy;

import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.Request;

public class RequestFailedCachePolicy<T> extends BaseCachePolicy<T> {
    public RequestFailedCachePolicy(Request<T, ? extends Request> request) {
        super(request);
    }

    public void onSuccess(final Response<T> success) {
        runOnUiThread(new Runnable() {
            public void run() {
                RequestFailedCachePolicy.this.mCallback.onSuccess(success);
                RequestFailedCachePolicy.this.mCallback.onFinish();
            }
        });
    }

    public void onError(final Response<T> error) {
        if (this.cacheEntity != null) {
            final Response<T> cacheSuccess = Response.success(true, this.cacheEntity.getData(), error.getRawCall(), error.getRawResponse());
            runOnUiThread(new Runnable() {
                public void run() {
                    RequestFailedCachePolicy.this.mCallback.onCacheSuccess(cacheSuccess);
                    RequestFailedCachePolicy.this.mCallback.onFinish();
                }
            });
            return;
        }
        runOnUiThread(new Runnable() {
            public void run() {
                RequestFailedCachePolicy.this.mCallback.onError(error);
                RequestFailedCachePolicy.this.mCallback.onFinish();
            }
        });
    }

    public Response<T> requestSync(CacheEntity<T> cacheEntity) {
        try {
            prepareRawCall();
            Response<T> response = requestNetworkSync();
            if (response.isSuccessful() || cacheEntity == null) {
                return response;
            }
            return Response.success(true, cacheEntity.getData(), this.rawCall, response.getRawResponse());
        } catch (Throwable throwable) {
            return Response.error(false, this.rawCall, (okhttp3.Response) null, throwable);
        }
    }

    public void requestAsync(CacheEntity<T> cacheEntity, Callback<T> callback) {
        this.mCallback = callback;
        runOnUiThread(new Runnable() {
            public void run() {
                RequestFailedCachePolicy.this.mCallback.onStart(RequestFailedCachePolicy.this.request);
                try {
                    RequestFailedCachePolicy.this.prepareRawCall();
                    RequestFailedCachePolicy.this.requestNetworkAsync();
                } catch (Throwable throwable) {
                    Response.error(false, RequestFailedCachePolicy.this.rawCall, (okhttp3.Response) null, throwable);
                }
            }
        });
    }
}
