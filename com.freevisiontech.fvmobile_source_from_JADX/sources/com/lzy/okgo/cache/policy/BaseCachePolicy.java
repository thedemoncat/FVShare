package com.lzy.okgo.cache.policy;

import android.graphics.Bitmap;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.exception.HttpException;
import com.lzy.okgo.p000db.CacheManager;
import com.lzy.okgo.request.Request;
import com.lzy.okgo.utils.HeaderParser;
import com.lzy.okgo.utils.HttpUtils;
import java.io.IOException;
import java.net.SocketTimeoutException;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Response;

public abstract class BaseCachePolicy<T> implements CachePolicy<T> {
    protected CacheEntity<T> cacheEntity;
    protected volatile boolean canceled;
    protected volatile int currentRetryCount = 0;
    protected boolean executed;
    protected Callback<T> mCallback;
    protected Call rawCall;
    protected Request<T, ? extends Request> request;

    public BaseCachePolicy(Request<T, ? extends Request> request2) {
        this.request = request2;
    }

    public boolean onAnalysisResponse(Call call, Response response) {
        return false;
    }

    public CacheEntity<T> prepareCache() {
        if (this.request.getCacheKey() == null) {
            this.request.cacheKey(HttpUtils.createUrlFromParams(this.request.getBaseUrl(), this.request.getParams().urlParamsMap));
        }
        if (this.request.getCacheMode() == null) {
            this.request.cacheMode(CacheMode.NO_CACHE);
        }
        CacheMode cacheMode = this.request.getCacheMode();
        if (cacheMode != CacheMode.NO_CACHE) {
            this.cacheEntity = CacheManager.getInstance().get(this.request.getCacheKey());
            HeaderParser.addCacheHeaders(this.request, this.cacheEntity, cacheMode);
            if (this.cacheEntity != null && this.cacheEntity.checkExpire(cacheMode, this.request.getCacheTime(), System.currentTimeMillis())) {
                this.cacheEntity.setExpire(true);
            }
        }
        if (this.cacheEntity == null || this.cacheEntity.isExpire() || this.cacheEntity.getData() == null || this.cacheEntity.getResponseHeaders() == null) {
            this.cacheEntity = null;
        }
        return this.cacheEntity;
    }

    public synchronized Call prepareRawCall() throws Throwable {
        if (this.executed) {
            throw HttpException.COMMON("Already executed!");
        }
        this.executed = true;
        this.rawCall = this.request.getRawCall();
        if (this.canceled) {
            this.rawCall.cancel();
        }
        return this.rawCall;
    }

    /* access modifiers changed from: protected */
    public com.lzy.okgo.model.Response<T> requestNetworkSync() {
        try {
            Response response = this.rawCall.execute();
            int responseCode = response.code();
            if (responseCode == 404 || responseCode >= 500) {
                return com.lzy.okgo.model.Response.error(false, this.rawCall, response, HttpException.NET_ERROR());
            }
            T body = this.request.getConverter().convertResponse(response);
            saveCache(response.headers(), body);
            return com.lzy.okgo.model.Response.success(false, body, this.rawCall, response);
        } catch (Throwable throwable) {
            if ((throwable instanceof SocketTimeoutException) && this.currentRetryCount < this.request.getRetryCount()) {
                this.currentRetryCount++;
                this.rawCall = this.request.getRawCall();
                if (this.canceled) {
                    this.rawCall.cancel();
                } else {
                    requestNetworkSync();
                }
            }
            return com.lzy.okgo.model.Response.error(false, this.rawCall, (Response) null, throwable);
        }
    }

    /* access modifiers changed from: protected */
    public void requestNetworkAsync() {
        this.rawCall.enqueue(new okhttp3.Callback() {
            public void onFailure(Call call, IOException e) {
                if ((e instanceof SocketTimeoutException) && BaseCachePolicy.this.currentRetryCount < BaseCachePolicy.this.request.getRetryCount()) {
                    BaseCachePolicy.this.currentRetryCount++;
                    BaseCachePolicy.this.rawCall = BaseCachePolicy.this.request.getRawCall();
                    if (BaseCachePolicy.this.canceled) {
                        BaseCachePolicy.this.rawCall.cancel();
                    } else {
                        BaseCachePolicy.this.rawCall.enqueue(this);
                    }
                } else if (!call.isCanceled()) {
                    BaseCachePolicy.this.onError(com.lzy.okgo.model.Response.error(false, call, (Response) null, e));
                }
            }

            public void onResponse(Call call, Response response) throws IOException {
                int responseCode = response.code();
                if (responseCode == 404 || responseCode >= 500) {
                    BaseCachePolicy.this.onError(com.lzy.okgo.model.Response.error(false, call, response, HttpException.NET_ERROR()));
                } else if (!BaseCachePolicy.this.onAnalysisResponse(call, response)) {
                    try {
                        T body = BaseCachePolicy.this.request.getConverter().convertResponse(response);
                        BaseCachePolicy.this.saveCache(response.headers(), body);
                        BaseCachePolicy.this.onSuccess(com.lzy.okgo.model.Response.success(false, body, call, response));
                    } catch (Throwable throwable) {
                        BaseCachePolicy.this.onError(com.lzy.okgo.model.Response.error(false, call, response, throwable));
                    }
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void saveCache(Headers headers, T data) {
        if (this.request.getCacheMode() != CacheMode.NO_CACHE && !(data instanceof Bitmap)) {
            CacheEntity<T> cache = HeaderParser.createCacheEntity(headers, data, this.request.getCacheMode(), this.request.getCacheKey());
            if (cache == null) {
                CacheManager.getInstance().remove(this.request.getCacheKey());
            } else {
                CacheManager.getInstance().replace(this.request.getCacheKey(), cache);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void runOnUiThread(Runnable run) {
        OkGo.getInstance().getDelivery().post(run);
    }

    public boolean isExecuted() {
        return this.executed;
    }

    public void cancel() {
        this.canceled = true;
        if (this.rawCall != null) {
            this.rawCall.cancel();
        }
    }

    public boolean isCanceled() {
        boolean z = true;
        if (!this.canceled) {
            synchronized (this) {
                if (this.rawCall == null || !this.rawCall.isCanceled()) {
                    z = false;
                }
            }
        }
        return z;
    }
}
