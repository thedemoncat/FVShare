package com.lzy.okgo;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.request.DeleteRequest;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.HeadRequest;
import com.lzy.okgo.request.OptionsRequest;
import com.lzy.okgo.request.PatchRequest;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okgo.request.PutRequest;
import com.lzy.okgo.request.TraceRequest;
import com.lzy.okgo.utils.HttpUtils;
import okhttp3.Call;
import okhttp3.OkHttpClient;

public class OkGo {
    public static final long DEFAULT_MILLISECONDS = 60000;
    public static long REFRESH_TIME = 300;
    private Application context;
    private CacheMode mCacheMode;
    private long mCacheTime;
    private HttpHeaders mCommonHeaders;
    private HttpParams mCommonParams;
    private Handler mDelivery;
    private int mRetryCount;
    private OkHttpClient okHttpClient;

    private OkGo() {
        this.mDelivery = new Handler(Looper.getMainLooper());
        this.mRetryCount = 3;
        this.mCacheTime = -1;
        this.mCacheMode = CacheMode.NO_CACHE;
    }

    public static OkGo getInstance() {
        return OkGoHolder.holder;
    }

    private static class OkGoHolder {
        /* access modifiers changed from: private */
        public static OkGo holder = new OkGo();

        private OkGoHolder() {
        }
    }

    public static <T> GetRequest<T> get(String url) {
        return new GetRequest<>(url);
    }

    public static <T> PostRequest<T> post(String url) {
        return new PostRequest<>(url);
    }

    public static <T> PutRequest<T> put(String url) {
        return new PutRequest<>(url);
    }

    public static <T> HeadRequest<T> head(String url) {
        return new HeadRequest<>(url);
    }

    public static <T> DeleteRequest<T> delete(String url) {
        return new DeleteRequest<>(url);
    }

    public static <T> OptionsRequest<T> options(String url) {
        return new OptionsRequest<>(url);
    }

    public static <T> PatchRequest<T> patch(String url) {
        return new PatchRequest<>(url);
    }

    public static <T> TraceRequest<T> trace(String url) {
        return new TraceRequest<>(url);
    }

    public OkGo init(Application app) {
        this.context = app;
        return this;
    }

    public Context getContext() {
        HttpUtils.checkNotNull(this.context, "please call OkGo.getInstance().init() first in application!");
        return this.context;
    }

    public Handler getDelivery() {
        return this.mDelivery;
    }

    public OkHttpClient getOkHttpClient() {
        HttpUtils.checkNotNull(this.okHttpClient, "please call OkGo.getInstance().setOkHttpClient() first in application!");
        return this.okHttpClient;
    }

    public OkGo setOkHttpClient(OkHttpClient okHttpClient2) {
        HttpUtils.checkNotNull(okHttpClient2, "okHttpClient == null");
        this.okHttpClient = okHttpClient2;
        return this;
    }

    public CookieJarImpl getCookieJar() {
        return (CookieJarImpl) this.okHttpClient.cookieJar();
    }

    public OkGo setRetryCount(int retryCount) {
        if (retryCount < 0) {
            throw new IllegalArgumentException("retryCount must > 0");
        }
        this.mRetryCount = retryCount;
        return this;
    }

    public int getRetryCount() {
        return this.mRetryCount;
    }

    public OkGo setCacheMode(CacheMode cacheMode) {
        this.mCacheMode = cacheMode;
        return this;
    }

    public CacheMode getCacheMode() {
        return this.mCacheMode;
    }

    public OkGo setCacheTime(long cacheTime) {
        if (cacheTime <= -1) {
            cacheTime = -1;
        }
        this.mCacheTime = cacheTime;
        return this;
    }

    public long getCacheTime() {
        return this.mCacheTime;
    }

    public HttpParams getCommonParams() {
        return this.mCommonParams;
    }

    public OkGo addCommonParams(HttpParams commonParams) {
        if (this.mCommonParams == null) {
            this.mCommonParams = new HttpParams();
        }
        this.mCommonParams.put(commonParams);
        return this;
    }

    public HttpHeaders getCommonHeaders() {
        return this.mCommonHeaders;
    }

    public OkGo addCommonHeaders(HttpHeaders commonHeaders) {
        if (this.mCommonHeaders == null) {
            this.mCommonHeaders = new HttpHeaders();
        }
        this.mCommonHeaders.put(commonHeaders);
        return this;
    }

    public void cancelTag(Object tag) {
        if (tag != null) {
            for (Call call : getOkHttpClient().dispatcher().queuedCalls()) {
                if (tag.equals(call.request().tag())) {
                    call.cancel();
                }
            }
            for (Call call2 : getOkHttpClient().dispatcher().runningCalls()) {
                if (tag.equals(call2.request().tag())) {
                    call2.cancel();
                }
            }
        }
    }

    public static void cancelTag(OkHttpClient client, Object tag) {
        if (client != null && tag != null) {
            for (Call call : client.dispatcher().queuedCalls()) {
                if (tag.equals(call.request().tag())) {
                    call.cancel();
                }
            }
            for (Call call2 : client.dispatcher().runningCalls()) {
                if (tag.equals(call2.request().tag())) {
                    call2.cancel();
                }
            }
        }
    }

    public void cancelAll() {
        for (Call call : getOkHttpClient().dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call2 : getOkHttpClient().dispatcher().runningCalls()) {
            call2.cancel();
        }
    }

    public static void cancelAll(OkHttpClient client) {
        if (client != null) {
            for (Call call : client.dispatcher().queuedCalls()) {
                call.cancel();
            }
            for (Call call2 : client.dispatcher().runningCalls()) {
                call2.cancel();
            }
        }
    }
}
