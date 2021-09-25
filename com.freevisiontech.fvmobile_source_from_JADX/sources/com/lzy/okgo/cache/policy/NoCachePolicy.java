package com.lzy.okgo.cache.policy;

import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.Request;

public class NoCachePolicy<T> extends BaseCachePolicy<T> {
    public NoCachePolicy(Request<T, ? extends Request> request) {
        super(request);
    }

    public void onSuccess(final Response<T> success) {
        runOnUiThread(new Runnable() {
            public void run() {
                NoCachePolicy.this.mCallback.onSuccess(success);
                NoCachePolicy.this.mCallback.onFinish();
            }
        });
    }

    public void onError(final Response<T> error) {
        runOnUiThread(new Runnable() {
            public void run() {
                NoCachePolicy.this.mCallback.onError(error);
                NoCachePolicy.this.mCallback.onFinish();
            }
        });
    }

    public Response<T> requestSync(CacheEntity<T> cacheEntity) {
        try {
            prepareRawCall();
            return requestNetworkSync();
        } catch (Throwable throwable) {
            return Response.error(false, this.rawCall, (okhttp3.Response) null, throwable);
        }
    }

    public void requestAsync(CacheEntity<T> cacheEntity, Callback<T> callback) {
        this.mCallback = callback;
        runOnUiThread(new Runnable() {
            public void run() {
                NoCachePolicy.this.mCallback.onStart(NoCachePolicy.this.request);
                try {
                    NoCachePolicy.this.prepareRawCall();
                    NoCachePolicy.this.requestNetworkAsync();
                } catch (Throwable throwable) {
                    Response.error(false, NoCachePolicy.this.rawCall, (okhttp3.Response) null, throwable);
                }
            }
        });
    }
}
