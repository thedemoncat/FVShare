package com.lzy.okgo.adapter;

import com.lzy.okgo.cache.policy.CachePolicy;
import com.lzy.okgo.cache.policy.DefaultCachePolicy;
import com.lzy.okgo.cache.policy.FirstCacheRequestPolicy;
import com.lzy.okgo.cache.policy.NoCachePolicy;
import com.lzy.okgo.cache.policy.NoneCacheRequestPolicy;
import com.lzy.okgo.cache.policy.RequestFailedCachePolicy;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.Request;
import com.lzy.okgo.utils.HttpUtils;

public class CacheCall<T> implements Call<T> {
    private CachePolicy<T> policy = null;
    private Request<T, ? extends Request> request;

    public CacheCall(Request<T, ? extends Request> request2) {
        this.request = request2;
        this.policy = preparePolicy();
    }

    public Response<T> execute() {
        return this.policy.requestSync(this.policy.prepareCache());
    }

    public void execute(Callback<T> callback) {
        HttpUtils.checkNotNull(callback, "callback == null");
        this.policy.requestAsync(this.policy.prepareCache(), callback);
    }

    private CachePolicy<T> preparePolicy() {
        switch (this.request.getCacheMode()) {
            case DEFAULT:
                this.policy = new DefaultCachePolicy(this.request);
                break;
            case NO_CACHE:
                this.policy = new NoCachePolicy(this.request);
                break;
            case IF_NONE_CACHE_REQUEST:
                this.policy = new NoneCacheRequestPolicy(this.request);
                break;
            case FIRST_CACHE_THEN_REQUEST:
                this.policy = new FirstCacheRequestPolicy(this.request);
                break;
            case REQUEST_FAILED_READ_CACHE:
                this.policy = new RequestFailedCachePolicy(this.request);
                break;
        }
        if (this.request.getCachePolicy() != null) {
            this.policy = this.request.getCachePolicy();
        }
        HttpUtils.checkNotNull(this.policy, "policy == null");
        return this.policy;
    }

    public boolean isExecuted() {
        return this.policy.isExecuted();
    }

    public void cancel() {
        this.policy.cancel();
    }

    public boolean isCanceled() {
        return this.policy.isCanceled();
    }

    public Call<T> clone() {
        return new CacheCall(this.request);
    }

    public Request getRequest() {
        return this.request;
    }
}
