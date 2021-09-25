package com.lzy.okgo.cache.policy;

import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.exception.CacheException;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.Request;
import okhttp3.Call;

public class DefaultCachePolicy<T> extends BaseCachePolicy<T> {
    public DefaultCachePolicy(Request<T, ? extends Request> request) {
        super(request);
    }

    public void onSuccess(final Response<T> success) {
        runOnUiThread(new Runnable() {
            public void run() {
                DefaultCachePolicy.this.mCallback.onSuccess(success);
                DefaultCachePolicy.this.mCallback.onFinish();
            }
        });
    }

    public void onError(final Response<T> error) {
        runOnUiThread(new Runnable() {
            public void run() {
                DefaultCachePolicy.this.mCallback.onError(error);
                DefaultCachePolicy.this.mCallback.onFinish();
            }
        });
    }

    public boolean onAnalysisResponse(Call call, okhttp3.Response response) {
        if (response.code() != 304) {
            return false;
        }
        if (this.cacheEntity == null) {
            final Response<T> error = Response.error(true, call, response, CacheException.NON_AND_304(this.request.getCacheKey()));
            runOnUiThread(new Runnable() {
                public void run() {
                    DefaultCachePolicy.this.mCallback.onError(error);
                    DefaultCachePolicy.this.mCallback.onFinish();
                }
            });
            return true;
        }
        final Response<T> success = Response.success(true, this.cacheEntity.getData(), call, response);
        runOnUiThread(new Runnable() {
            public void run() {
                DefaultCachePolicy.this.mCallback.onCacheSuccess(success);
                DefaultCachePolicy.this.mCallback.onFinish();
            }
        });
        return true;
    }

    public Response<T> requestSync(CacheEntity<T> cacheEntity) {
        try {
            prepareRawCall();
            Response<T> response = requestNetworkSync();
            if (!response.isSuccessful() || response.code() != 304) {
                return response;
            }
            if (cacheEntity == null) {
                return Response.error(true, this.rawCall, response.getRawResponse(), CacheException.NON_AND_304(this.request.getCacheKey()));
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
                DefaultCachePolicy.this.mCallback.onStart(DefaultCachePolicy.this.request);
                try {
                    DefaultCachePolicy.this.prepareRawCall();
                    DefaultCachePolicy.this.requestNetworkAsync();
                } catch (Throwable throwable) {
                    Response.error(false, DefaultCachePolicy.this.rawCall, (okhttp3.Response) null, throwable);
                }
            }
        });
    }
}
