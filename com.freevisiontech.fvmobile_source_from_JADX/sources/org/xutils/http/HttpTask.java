package org.xutils.http;

import android.text.TextUtils;
import java.io.Closeable;
import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import org.xutils.C2090x;
import org.xutils.common.Callback;
import org.xutils.common.task.AbsTask;
import org.xutils.common.task.Priority;
import org.xutils.common.task.PriorityExecutor;
import org.xutils.common.util.IOUtil;
import org.xutils.common.util.ParameterizedTypeUtil;
import org.xutils.http.app.RequestInterceptListener;
import org.xutils.http.app.RequestTracker;
import org.xutils.http.request.UriRequest;
import org.xutils.http.request.UriRequestFactory;

public class HttpTask<ResultType> extends AbsTask<ResultType> implements ProgressHandler {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final PriorityExecutor CACHE_EXECUTOR = new PriorityExecutor(5, true);
    private static final HashMap<String, WeakReference<HttpTask<?>>> DOWNLOAD_TASK = new HashMap<>(1);
    private static final int FLAG_CACHE = 2;
    private static final int FLAG_PROGRESS = 3;
    private static final int FLAG_REQUEST_CREATED = 1;
    private static final PriorityExecutor HTTP_EXECUTOR = new PriorityExecutor(5, true);
    private static final int MAX_FILE_LOAD_WORKER = 3;
    /* access modifiers changed from: private */
    public static final AtomicInteger sCurrFileLoadCount = new AtomicInteger(0);
    private Callback.CacheCallback<ResultType> cacheCallback;
    private final Object cacheLock = new Object();
    private final Callback.CommonCallback<ResultType> callback;
    private final Executor executor;
    private volatile boolean hasException = false;
    private long lastUpdateTime;
    /* access modifiers changed from: private */
    public Type loadType;
    private long loadingUpdateMaxTimeSpan = 300;
    /* access modifiers changed from: private */
    public RequestParams params;
    private Callback.PrepareCallback prepareCallback;
    private Callback.ProgressCallback progressCallback;
    private Object rawResult = null;
    /* access modifiers changed from: private */
    public UriRequest request;
    /* access modifiers changed from: private */
    public RequestInterceptListener requestInterceptListener;
    private HttpTask<ResultType>.RequestWorker requestWorker;
    private RequestTracker tracker;
    private volatile Boolean trustCache = null;

    static {
        boolean z;
        if (!HttpTask.class.desiredAssertionStatus()) {
            z = true;
        } else {
            z = false;
        }
        $assertionsDisabled = z;
    }

    public HttpTask(RequestParams params2, Callback.Cancelable cancelHandler, Callback.CommonCallback<ResultType> callback2) {
        super(cancelHandler);
        if (!$assertionsDisabled && params2 == null) {
            throw new AssertionError();
        } else if ($assertionsDisabled || callback2 != null) {
            this.params = params2;
            this.callback = callback2;
            if (callback2 instanceof Callback.CacheCallback) {
                this.cacheCallback = (Callback.CacheCallback) callback2;
            }
            if (callback2 instanceof Callback.PrepareCallback) {
                this.prepareCallback = (Callback.PrepareCallback) callback2;
            }
            if (callback2 instanceof Callback.ProgressCallback) {
                this.progressCallback = (Callback.ProgressCallback) callback2;
            }
            if (callback2 instanceof RequestInterceptListener) {
                this.requestInterceptListener = (RequestInterceptListener) callback2;
            }
            RequestTracker customTracker = params2.getRequestTracker();
            if (customTracker == null) {
                if (callback2 instanceof RequestTracker) {
                    customTracker = (RequestTracker) callback2;
                } else {
                    customTracker = UriRequestFactory.getDefaultTracker();
                }
            }
            if (customTracker != null) {
                this.tracker = new RequestTrackerWrapper(customTracker);
            }
            if (params2.getExecutor() != null) {
                this.executor = params2.getExecutor();
            } else if (this.cacheCallback != null) {
                this.executor = CACHE_EXECUTOR;
            } else {
                this.executor = HTTP_EXECUTOR;
            }
        } else {
            throw new AssertionError();
        }
    }

    private void resolveLoadType() {
        Class<?> callBackType = this.callback.getClass();
        if (this.callback instanceof Callback.TypedCallback) {
            this.loadType = ((Callback.TypedCallback) this.callback).getLoadType();
        } else if (this.callback instanceof Callback.PrepareCallback) {
            this.loadType = ParameterizedTypeUtil.getParameterizedType(callBackType, Callback.PrepareCallback.class, 0);
        } else {
            this.loadType = ParameterizedTypeUtil.getParameterizedType(callBackType, Callback.CommonCallback.class, 0);
        }
    }

    /* access modifiers changed from: private */
    public UriRequest createNewRequest() throws Throwable {
        this.params.init();
        UriRequest result = UriRequestFactory.getUriRequest(this.params, this.loadType);
        result.setCallingClassLoader(this.callback.getClass().getClassLoader());
        result.setProgressHandler(this);
        this.loadingUpdateMaxTimeSpan = (long) this.params.getLoadingUpdateMaxTimeSpan();
        update(1, result);
        return result;
    }

    private void checkDownloadTask() {
        if (File.class == this.loadType) {
            synchronized (DOWNLOAD_TASK) {
                String downloadTaskKey = this.params.getSaveFilePath();
                if (!TextUtils.isEmpty(downloadTaskKey)) {
                    WeakReference<HttpTask<?>> taskRef = DOWNLOAD_TASK.get(downloadTaskKey);
                    if (taskRef != null) {
                        HttpTask<?> task = (HttpTask) taskRef.get();
                        if (task != null) {
                            task.cancel();
                            task.closeRequestSync();
                        }
                        DOWNLOAD_TASK.remove(downloadTaskKey);
                    }
                    DOWNLOAD_TASK.put(downloadTaskKey, new WeakReference(this));
                }
                if (DOWNLOAD_TASK.size() > 3) {
                    Iterator<Map.Entry<String, WeakReference<HttpTask<?>>>> entryItr = DOWNLOAD_TASK.entrySet().iterator();
                    while (entryItr.hasNext()) {
                        WeakReference<HttpTask<?>> value = entryItr.next().getValue();
                        if (value == null || value.get() == null) {
                            entryItr.remove();
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:100:0x01b0  */
    /* JADX WARNING: Removed duplicated region for block: B:153:0x0225 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x0136 A[ExcHandler: HttpRedirectException (e org.xutils.ex.HttpRedirectException), Splitter:B:77:0x0127] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ResultType doBackground() throws java.lang.Throwable {
        /*
            r14 = this;
            r13 = 1
            r12 = 0
            r10 = 0
            boolean r9 = r14.isCancelled()
            if (r9 == 0) goto L_0x0012
            org.xutils.common.Callback$CancelledException r9 = new org.xutils.common.Callback$CancelledException
            java.lang.String r10 = "cancelled before request"
            r9.<init>(r10)
            throw r9
        L_0x0012:
            r5 = 0
            r14.resolveLoadType()
            org.xutils.http.request.UriRequest r9 = r14.createNewRequest()
            r14.request = r9
            r14.checkDownloadTask()
            r6 = 1
            r7 = 0
            r2 = 0
            org.xutils.http.RequestParams r9 = r14.params
            org.xutils.http.app.HttpRetryHandler r8 = r9.getHttpRetryHandler()
            if (r8 != 0) goto L_0x002f
            org.xutils.http.app.HttpRetryHandler r8 = new org.xutils.http.app.HttpRetryHandler
            r8.<init>()
        L_0x002f:
            org.xutils.http.RequestParams r9 = r14.params
            int r9 = r9.getMaxRetryCount()
            r8.setMaxRetryCount(r9)
            boolean r9 = r14.isCancelled()
            if (r9 == 0) goto L_0x0047
            org.xutils.common.Callback$CancelledException r9 = new org.xutils.common.Callback$CancelledException
            java.lang.String r10 = "cancelled before request"
            r9.<init>(r10)
            throw r9
        L_0x0047:
            r0 = 0
            org.xutils.common.Callback$CacheCallback<ResultType> r9 = r14.cacheCallback
            if (r9 == 0) goto L_0x00ff
            org.xutils.http.RequestParams r9 = r14.params
            org.xutils.http.HttpMethod r9 = r9.getMethod()
            boolean r9 = org.xutils.http.HttpMethod.permitsCache(r9)
            if (r9 == 0) goto L_0x00ff
            r14.clearRawResult()     // Catch:{ Throwable -> 0x0092 }
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0092 }
            r9.<init>()     // Catch:{ Throwable -> 0x0092 }
            java.lang.String r11 = "load cache: "
            java.lang.StringBuilder r9 = r9.append(r11)     // Catch:{ Throwable -> 0x0092 }
            org.xutils.http.request.UriRequest r11 = r14.request     // Catch:{ Throwable -> 0x0092 }
            java.lang.String r11 = r11.getRequestUri()     // Catch:{ Throwable -> 0x0092 }
            java.lang.StringBuilder r9 = r9.append(r11)     // Catch:{ Throwable -> 0x0092 }
            java.lang.String r9 = r9.toString()     // Catch:{ Throwable -> 0x0092 }
            org.xutils.common.util.LogUtil.m1562d(r9)     // Catch:{ Throwable -> 0x0092 }
            org.xutils.http.request.UriRequest r9 = r14.request     // Catch:{ Throwable -> 0x0092 }
            java.lang.Object r9 = r9.loadResultFromCache()     // Catch:{ Throwable -> 0x0092 }
            r14.rawResult = r9     // Catch:{ Throwable -> 0x0092 }
        L_0x0080:
            boolean r9 = r14.isCancelled()
            if (r9 == 0) goto L_0x009a
            r14.clearRawResult()
            org.xutils.common.Callback$CancelledException r9 = new org.xutils.common.Callback$CancelledException
            java.lang.String r10 = "cancelled before request"
            r9.<init>(r10)
            throw r9
        L_0x0092:
            r1 = move-exception
            java.lang.String r9 = "load disk cache error"
            org.xutils.common.util.LogUtil.m1571w(r9, r1)
            goto L_0x0080
        L_0x009a:
            java.lang.Object r9 = r14.rawResult
            if (r9 == 0) goto L_0x00ff
            org.xutils.common.Callback$PrepareCallback r9 = r14.prepareCallback
            if (r9 == 0) goto L_0x00cd
            org.xutils.common.Callback$PrepareCallback r9 = r14.prepareCallback     // Catch:{ Throwable -> 0x00bc }
            java.lang.Object r11 = r14.rawResult     // Catch:{ Throwable -> 0x00bc }
            java.lang.Object r0 = r9.prepare(r11)     // Catch:{ Throwable -> 0x00bc }
            r14.clearRawResult()
        L_0x00ad:
            boolean r9 = r14.isCancelled()
            if (r9 == 0) goto L_0x00d0
            org.xutils.common.Callback$CancelledException r9 = new org.xutils.common.Callback$CancelledException
            java.lang.String r10 = "cancelled before request"
            r9.<init>(r10)
            throw r9
        L_0x00bc:
            r1 = move-exception
            r0 = 0
            java.lang.String r9 = "prepare disk cache error"
            org.xutils.common.util.LogUtil.m1571w(r9, r1)     // Catch:{ all -> 0x00c8 }
            r14.clearRawResult()
            goto L_0x00ad
        L_0x00c8:
            r9 = move-exception
            r14.clearRawResult()
            throw r9
        L_0x00cd:
            java.lang.Object r0 = r14.rawResult
            goto L_0x00ad
        L_0x00d0:
            if (r0 == 0) goto L_0x00ff
            r9 = 2
            java.lang.Object[] r11 = new java.lang.Object[r13]
            r11[r12] = r0
            r14.update(r9, r11)
        L_0x00da:
            java.lang.Boolean r9 = r14.trustCache
            if (r9 != 0) goto L_0x00f5
            java.lang.Object r11 = r14.cacheLock
            monitor-enter(r11)
            java.lang.Object r9 = r14.cacheLock     // Catch:{ InterruptedException -> 0x00eb, Throwable -> 0x023b }
            r9.wait()     // Catch:{ InterruptedException -> 0x00eb, Throwable -> 0x023b }
        L_0x00e6:
            monitor-exit(r11)     // Catch:{ all -> 0x00e8 }
            goto L_0x00da
        L_0x00e8:
            r9 = move-exception
            monitor-exit(r11)     // Catch:{ all -> 0x00e8 }
            throw r9
        L_0x00eb:
            r3 = move-exception
            org.xutils.common.Callback$CancelledException r9 = new org.xutils.common.Callback$CancelledException     // Catch:{ all -> 0x00e8 }
            java.lang.String r10 = "cancelled before request"
            r9.<init>(r10)     // Catch:{ all -> 0x00e8 }
            throw r9     // Catch:{ all -> 0x00e8 }
        L_0x00f5:
            java.lang.Boolean r9 = r14.trustCache
            boolean r9 = r9.booleanValue()
            if (r9 == 0) goto L_0x00ff
            r9 = r10
        L_0x00fe:
            return r9
        L_0x00ff:
            java.lang.Boolean r9 = r14.trustCache
            if (r9 != 0) goto L_0x0109
            java.lang.Boolean r9 = java.lang.Boolean.valueOf(r12)
            r14.trustCache = r9
        L_0x0109:
            if (r0 != 0) goto L_0x0110
            org.xutils.http.request.UriRequest r9 = r14.request
            r9.clearCacheHeader()
        L_0x0110:
            org.xutils.common.Callback$CommonCallback<ResultType> r9 = r14.callback
            boolean r9 = r9 instanceof org.xutils.common.Callback.ProxyCacheCallback
            if (r9 == 0) goto L_0x0122
            org.xutils.common.Callback$CommonCallback<ResultType> r9 = r14.callback
            org.xutils.common.Callback$ProxyCacheCallback r9 = (org.xutils.common.Callback.ProxyCacheCallback) r9
            boolean r9 = r9.onlyCache()
            if (r9 == 0) goto L_0x0122
            r9 = r10
            goto L_0x00fe
        L_0x0122:
            r6 = 1
            r9 = r5
        L_0x0124:
            if (r6 == 0) goto L_0x0228
            r6 = 0
            boolean r11 = r14.isCancelled()     // Catch:{ HttpRedirectException -> 0x0136, Throwable -> 0x01a6 }
            if (r11 == 0) goto L_0x0156
            org.xutils.common.Callback$CancelledException r11 = new org.xutils.common.Callback$CancelledException     // Catch:{ HttpRedirectException -> 0x0136, Throwable -> 0x01a6 }
            java.lang.String r12 = "cancelled before request"
            r11.<init>(r12)     // Catch:{ HttpRedirectException -> 0x0136, Throwable -> 0x01a6 }
            throw r11     // Catch:{ HttpRedirectException -> 0x0136, Throwable -> 0x01a6 }
        L_0x0136:
            r4 = move-exception
        L_0x0137:
            r6 = 1
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r12 = "Http Redirect:"
            java.lang.StringBuilder r11 = r11.append(r12)
            org.xutils.http.RequestParams r12 = r14.params
            java.lang.String r12 = r12.getUri()
            java.lang.StringBuilder r11 = r11.append(r12)
            java.lang.String r11 = r11.toString()
            org.xutils.common.util.LogUtil.m1570w((java.lang.String) r11)
            goto L_0x0124
        L_0x0156:
            org.xutils.http.request.UriRequest r11 = r14.request     // Catch:{ HttpRedirectException -> 0x0136, Throwable -> 0x01a6 }
            r11.close()     // Catch:{ HttpRedirectException -> 0x0136, Throwable -> 0x01a6 }
            r14.clearRawResult()     // Catch:{ Throwable -> 0x0193, HttpRedirectException -> 0x0136 }
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0193, HttpRedirectException -> 0x0136 }
            r11.<init>()     // Catch:{ Throwable -> 0x0193, HttpRedirectException -> 0x0136 }
            java.lang.String r12 = "load: "
            java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ Throwable -> 0x0193, HttpRedirectException -> 0x0136 }
            org.xutils.http.request.UriRequest r12 = r14.request     // Catch:{ Throwable -> 0x0193, HttpRedirectException -> 0x0136 }
            java.lang.String r12 = r12.getRequestUri()     // Catch:{ Throwable -> 0x0193, HttpRedirectException -> 0x0136 }
            java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ Throwable -> 0x0193, HttpRedirectException -> 0x0136 }
            java.lang.String r11 = r11.toString()     // Catch:{ Throwable -> 0x0193, HttpRedirectException -> 0x0136 }
            org.xutils.common.util.LogUtil.m1562d(r11)     // Catch:{ Throwable -> 0x0193, HttpRedirectException -> 0x0136 }
            org.xutils.http.HttpTask$RequestWorker r11 = new org.xutils.http.HttpTask$RequestWorker     // Catch:{ Throwable -> 0x0193, HttpRedirectException -> 0x0136 }
            r12 = 0
            r11.<init>()     // Catch:{ Throwable -> 0x0193, HttpRedirectException -> 0x0136 }
            r14.requestWorker = r11     // Catch:{ Throwable -> 0x0193, HttpRedirectException -> 0x0136 }
            org.xutils.http.HttpTask<ResultType>$RequestWorker r11 = r14.requestWorker     // Catch:{ Throwable -> 0x0193, HttpRedirectException -> 0x0136 }
            r11.request()     // Catch:{ Throwable -> 0x0193, HttpRedirectException -> 0x0136 }
            org.xutils.http.HttpTask<ResultType>$RequestWorker r11 = r14.requestWorker     // Catch:{ Throwable -> 0x0193, HttpRedirectException -> 0x0136 }
            java.lang.Throwable r11 = r11.f1213ex     // Catch:{ Throwable -> 0x0193, HttpRedirectException -> 0x0136 }
            if (r11 == 0) goto L_0x01cd
            org.xutils.http.HttpTask<ResultType>$RequestWorker r11 = r14.requestWorker     // Catch:{ Throwable -> 0x0193, HttpRedirectException -> 0x0136 }
            java.lang.Throwable r11 = r11.f1213ex     // Catch:{ Throwable -> 0x0193, HttpRedirectException -> 0x0136 }
            throw r11     // Catch:{ Throwable -> 0x0193, HttpRedirectException -> 0x0136 }
        L_0x0193:
            r1 = move-exception
            r14.clearRawResult()     // Catch:{ HttpRedirectException -> 0x0136, Throwable -> 0x01a6 }
            boolean r11 = r14.isCancelled()     // Catch:{ HttpRedirectException -> 0x0136, Throwable -> 0x01a6 }
            if (r11 == 0) goto L_0x01e6
            org.xutils.common.Callback$CancelledException r11 = new org.xutils.common.Callback$CancelledException     // Catch:{ HttpRedirectException -> 0x0136, Throwable -> 0x01a6 }
            java.lang.String r12 = "cancelled during request"
            r11.<init>(r12)     // Catch:{ HttpRedirectException -> 0x0136, Throwable -> 0x01a6 }
            throw r11     // Catch:{ HttpRedirectException -> 0x0136, Throwable -> 0x01a6 }
        L_0x01a6:
            r1 = move-exception
        L_0x01a7:
            org.xutils.http.request.UriRequest r11 = r14.request
            int r11 = r11.getResponseCode()
            switch(r11) {
                case 204: goto L_0x0225;
                case 205: goto L_0x0225;
                case 304: goto L_0x0225;
                default: goto L_0x01b0;
            }
        L_0x01b0:
            r2 = r1
            boolean r11 = r14.isCancelled()
            if (r11 == 0) goto L_0x01c3
            boolean r11 = r2 instanceof org.xutils.common.Callback.CancelledException
            if (r11 != 0) goto L_0x01c3
            org.xutils.common.Callback$CancelledException r2 = new org.xutils.common.Callback$CancelledException
            java.lang.String r11 = "canceled by user"
            r2.<init>(r11)
        L_0x01c3:
            org.xutils.http.request.UriRequest r11 = r14.request
            int r7 = r7 + 1
            boolean r6 = r8.canRetry(r11, r2, r7)
            goto L_0x0124
        L_0x01cd:
            org.xutils.http.HttpTask<ResultType>$RequestWorker r11 = r14.requestWorker     // Catch:{ Throwable -> 0x0193, HttpRedirectException -> 0x0136 }
            java.lang.Object r11 = r11.result     // Catch:{ Throwable -> 0x0193, HttpRedirectException -> 0x0136 }
            r14.rawResult = r11     // Catch:{ Throwable -> 0x0193, HttpRedirectException -> 0x0136 }
            org.xutils.common.Callback$PrepareCallback r11 = r14.prepareCallback     // Catch:{ HttpRedirectException -> 0x0136, Throwable -> 0x01a6 }
            if (r11 == 0) goto L_0x021f
            boolean r11 = r14.isCancelled()     // Catch:{ HttpRedirectException -> 0x0136, Throwable -> 0x01a6 }
            if (r11 == 0) goto L_0x01e7
            org.xutils.common.Callback$CancelledException r11 = new org.xutils.common.Callback$CancelledException     // Catch:{ HttpRedirectException -> 0x0136, Throwable -> 0x01a6 }
            java.lang.String r12 = "cancelled before request"
            r11.<init>(r12)     // Catch:{ HttpRedirectException -> 0x0136, Throwable -> 0x01a6 }
            throw r11     // Catch:{ HttpRedirectException -> 0x0136, Throwable -> 0x01a6 }
        L_0x01e6:
            throw r1     // Catch:{ HttpRedirectException -> 0x0136, Throwable -> 0x01a6 }
        L_0x01e7:
            org.xutils.common.Callback$PrepareCallback r11 = r14.prepareCallback     // Catch:{ all -> 0x021a }
            java.lang.Object r12 = r14.rawResult     // Catch:{ all -> 0x021a }
            java.lang.Object r5 = r11.prepare(r12)     // Catch:{ all -> 0x021a }
            r14.clearRawResult()     // Catch:{ HttpRedirectException -> 0x0216, Throwable -> 0x0237 }
        L_0x01f2:
            org.xutils.common.Callback$CacheCallback<ResultType> r9 = r14.cacheCallback     // Catch:{ HttpRedirectException -> 0x0216, Throwable -> 0x0237 }
            if (r9 == 0) goto L_0x0207
            org.xutils.http.RequestParams r9 = r14.params     // Catch:{ HttpRedirectException -> 0x0216, Throwable -> 0x0237 }
            org.xutils.http.HttpMethod r9 = r9.getMethod()     // Catch:{ HttpRedirectException -> 0x0216, Throwable -> 0x0237 }
            boolean r9 = org.xutils.http.HttpMethod.permitsCache(r9)     // Catch:{ HttpRedirectException -> 0x0216, Throwable -> 0x0237 }
            if (r9 == 0) goto L_0x0207
            org.xutils.http.request.UriRequest r9 = r14.request     // Catch:{ HttpRedirectException -> 0x0216, Throwable -> 0x0237 }
            r9.save2Cache()     // Catch:{ HttpRedirectException -> 0x0216, Throwable -> 0x0237 }
        L_0x0207:
            boolean r9 = r14.isCancelled()     // Catch:{ HttpRedirectException -> 0x0216, Throwable -> 0x0237 }
            if (r9 == 0) goto L_0x0222
            org.xutils.common.Callback$CancelledException r9 = new org.xutils.common.Callback$CancelledException     // Catch:{ HttpRedirectException -> 0x0216, Throwable -> 0x0237 }
            java.lang.String r11 = "cancelled after request"
            r9.<init>(r11)     // Catch:{ HttpRedirectException -> 0x0216, Throwable -> 0x0237 }
            throw r9     // Catch:{ HttpRedirectException -> 0x0216, Throwable -> 0x0237 }
        L_0x0216:
            r4 = move-exception
            r9 = r5
            goto L_0x0137
        L_0x021a:
            r11 = move-exception
            r14.clearRawResult()     // Catch:{ HttpRedirectException -> 0x0136, Throwable -> 0x01a6 }
            throw r11     // Catch:{ HttpRedirectException -> 0x0136, Throwable -> 0x01a6 }
        L_0x021f:
            java.lang.Object r5 = r14.rawResult     // Catch:{ HttpRedirectException -> 0x0136, Throwable -> 0x01a6 }
            goto L_0x01f2
        L_0x0222:
            r9 = r5
            goto L_0x0124
        L_0x0225:
            r9 = r10
            goto L_0x00fe
        L_0x0228:
            if (r2 == 0) goto L_0x00fe
            if (r9 != 0) goto L_0x00fe
            java.lang.Boolean r10 = r14.trustCache
            boolean r10 = r10.booleanValue()
            if (r10 != 0) goto L_0x00fe
            r14.hasException = r13
            throw r2
        L_0x0237:
            r1 = move-exception
            r9 = r5
            goto L_0x01a7
        L_0x023b:
            r9 = move-exception
            goto L_0x00e6
        */
        throw new UnsupportedOperationException("Method not decompiled: org.xutils.http.HttpTask.doBackground():java.lang.Object");
    }

    /* JADX INFO: finally extract failed */
    /* access modifiers changed from: protected */
    public void onUpdate(int flag, Object... args) {
        switch (flag) {
            case 1:
                if (this.tracker != null) {
                    this.tracker.onRequestCreated(args[0]);
                    return;
                }
                return;
            case 2:
                synchronized (this.cacheLock) {
                    try {
                        ResultType result = args[0];
                        if (this.tracker != null) {
                            this.tracker.onCache(this.request, result);
                        }
                        this.trustCache = Boolean.valueOf(this.cacheCallback.onCache(result));
                        this.cacheLock.notifyAll();
                    } catch (Throwable th) {
                        this.cacheLock.notifyAll();
                        throw th;
                    }
                }
                return;
            case 3:
                if (this.progressCallback != null && args.length == 3) {
                    try {
                        this.progressCallback.onLoading(args[0].longValue(), args[1].longValue(), args[2].booleanValue());
                        return;
                    } catch (Throwable ex) {
                        this.callback.onError(ex, true);
                        return;
                    }
                } else {
                    return;
                }
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void onWaiting() {
        if (this.tracker != null) {
            this.tracker.onWaiting(this.params);
        }
        if (this.progressCallback != null) {
            this.progressCallback.onWaiting();
        }
    }

    /* access modifiers changed from: protected */
    public void onStarted() {
        if (this.tracker != null) {
            this.tracker.onStart(this.params);
        }
        if (this.progressCallback != null) {
            this.progressCallback.onStarted();
        }
    }

    /* access modifiers changed from: protected */
    public void onSuccess(ResultType result) {
        if (!this.hasException) {
            if (this.tracker != null) {
                this.tracker.onSuccess(this.request, result);
            }
            this.callback.onSuccess(result);
        }
    }

    /* access modifiers changed from: protected */
    public void onError(Throwable ex, boolean isCallbackError) {
        if (this.tracker != null) {
            this.tracker.onError(this.request, ex, isCallbackError);
        }
        this.callback.onError(ex, isCallbackError);
    }

    /* access modifiers changed from: protected */
    public void onCancelled(Callback.CancelledException cex) {
        if (this.tracker != null) {
            this.tracker.onCancelled(this.request);
        }
        this.callback.onCancelled(cex);
    }

    /* access modifiers changed from: protected */
    public void onFinished() {
        if (this.tracker != null) {
            this.tracker.onFinished(this.request);
        }
        C2090x.task().run(new Runnable() {
            public void run() {
                HttpTask.this.closeRequestSync();
            }
        });
        this.callback.onFinished();
    }

    private void clearRawResult() {
        if (this.rawResult instanceof Closeable) {
            IOUtil.closeQuietly((Closeable) this.rawResult);
        }
        this.rawResult = null;
    }

    /* access modifiers changed from: protected */
    public void cancelWorks() {
        C2090x.task().run(new Runnable() {
            public void run() {
                HttpTask.this.closeRequestSync();
            }
        });
    }

    /* access modifiers changed from: protected */
    public boolean isCancelFast() {
        return this.params.isCancelFast();
    }

    /* access modifiers changed from: private */
    public void closeRequestSync() {
        clearRawResult();
        IOUtil.closeQuietly((Closeable) this.request);
    }

    public Executor getExecutor() {
        return this.executor;
    }

    public Priority getPriority() {
        return this.params.getPriority();
    }

    public boolean updateProgress(long total, long current, boolean forceUpdateUI) {
        if (isCancelled() || isFinished()) {
            return false;
        }
        if (!(this.progressCallback == null || this.request == null || total <= 0)) {
            if (total < current) {
                total = current;
            }
            if (forceUpdateUI) {
                this.lastUpdateTime = System.currentTimeMillis();
                update(3, Long.valueOf(total), Long.valueOf(current), Boolean.valueOf(this.request.isLoading()));
            } else {
                long currTime = System.currentTimeMillis();
                if (currTime - this.lastUpdateTime >= this.loadingUpdateMaxTimeSpan) {
                    this.lastUpdateTime = currTime;
                    update(3, Long.valueOf(total), Long.valueOf(current), Boolean.valueOf(this.request.isLoading()));
                }
            }
        }
        if (isCancelled() || isFinished()) {
            return false;
        }
        return true;
    }

    public String toString() {
        return this.params.toString();
    }

    private final class RequestWorker {

        /* renamed from: ex */
        Throwable f1213ex;
        Object result;

        private RequestWorker() {
        }

        /* JADX WARNING: Code restructure failed: missing block: B:23:0x0038, code lost:
            r4 = (org.xutils.p019ex.HttpException) r3;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void request() {
            /*
                r14 = this;
                r6 = 0
                java.lang.Class<java.io.File> r10 = java.io.File.class
                org.xutils.http.HttpTask r11 = org.xutils.http.HttpTask.this     // Catch:{ Throwable -> 0x0031 }
                java.lang.reflect.Type r11 = r11.loadType     // Catch:{ Throwable -> 0x0031 }
                if (r10 != r11) goto L_0x00bb
            L_0x000b:
                java.util.concurrent.atomic.AtomicInteger r10 = org.xutils.http.HttpTask.sCurrFileLoadCount     // Catch:{ Throwable -> 0x0031 }
                int r10 = r10.get()     // Catch:{ Throwable -> 0x0031 }
                r11 = 3
                if (r10 < r11) goto L_0x00b4
                org.xutils.http.HttpTask r10 = org.xutils.http.HttpTask.this     // Catch:{ Throwable -> 0x0031 }
                boolean r10 = r10.isCancelled()     // Catch:{ Throwable -> 0x0031 }
                if (r10 != 0) goto L_0x00b4
                java.util.concurrent.atomic.AtomicInteger r11 = org.xutils.http.HttpTask.sCurrFileLoadCount     // Catch:{ Throwable -> 0x0031 }
                monitor-enter(r11)     // Catch:{ Throwable -> 0x0031 }
                java.util.concurrent.atomic.AtomicInteger r10 = org.xutils.http.HttpTask.sCurrFileLoadCount     // Catch:{ InterruptedException -> 0x00b1, Throwable -> 0x015c }
                r12 = 10
                r10.wait(r12)     // Catch:{ InterruptedException -> 0x00b1, Throwable -> 0x015c }
            L_0x002c:
                monitor-exit(r11)     // Catch:{ all -> 0x002e }
                goto L_0x000b
            L_0x002e:
                r10 = move-exception
                monitor-exit(r11)     // Catch:{ all -> 0x002e }
                throw r10     // Catch:{ Throwable -> 0x0031 }
            L_0x0031:
                r3 = move-exception
                r14.f1213ex = r3     // Catch:{ all -> 0x00e4 }
                boolean r10 = r3 instanceof org.xutils.p019ex.HttpException     // Catch:{ all -> 0x00e4 }
                if (r10 == 0) goto L_0x0092
                r0 = r3
                org.xutils.ex.HttpException r0 = (org.xutils.p019ex.HttpException) r0     // Catch:{ all -> 0x00e4 }
                r4 = r0
                int r2 = r4.getCode()     // Catch:{ all -> 0x00e4 }
                r10 = 301(0x12d, float:4.22E-43)
                if (r2 == r10) goto L_0x0048
                r10 = 302(0x12e, float:4.23E-43)
                if (r2 != r10) goto L_0x0092
            L_0x0048:
                org.xutils.http.HttpTask r10 = org.xutils.http.HttpTask.this     // Catch:{ all -> 0x00e4 }
                org.xutils.http.RequestParams r10 = r10.params     // Catch:{ all -> 0x00e4 }
                org.xutils.http.app.RedirectHandler r7 = r10.getRedirectHandler()     // Catch:{ all -> 0x00e4 }
                if (r7 == 0) goto L_0x0092
                org.xutils.http.HttpTask r10 = org.xutils.http.HttpTask.this     // Catch:{ Throwable -> 0x0151 }
                org.xutils.http.request.UriRequest r10 = r10.request     // Catch:{ Throwable -> 0x0151 }
                org.xutils.http.RequestParams r8 = r7.getRedirectParams(r10)     // Catch:{ Throwable -> 0x0151 }
                if (r8 == 0) goto L_0x0092
                org.xutils.http.HttpMethod r10 = r8.getMethod()     // Catch:{ Throwable -> 0x0151 }
                if (r10 != 0) goto L_0x0073
                org.xutils.http.HttpTask r10 = org.xutils.http.HttpTask.this     // Catch:{ Throwable -> 0x0151 }
                org.xutils.http.RequestParams r10 = r10.params     // Catch:{ Throwable -> 0x0151 }
                org.xutils.http.HttpMethod r10 = r10.getMethod()     // Catch:{ Throwable -> 0x0151 }
                r8.setMethod(r10)     // Catch:{ Throwable -> 0x0151 }
            L_0x0073:
                org.xutils.http.HttpTask r10 = org.xutils.http.HttpTask.this     // Catch:{ Throwable -> 0x0151 }
                org.xutils.http.RequestParams unused = r10.params = r8     // Catch:{ Throwable -> 0x0151 }
                org.xutils.http.HttpTask r10 = org.xutils.http.HttpTask.this     // Catch:{ Throwable -> 0x0151 }
                org.xutils.http.HttpTask r11 = org.xutils.http.HttpTask.this     // Catch:{ Throwable -> 0x0151 }
                org.xutils.http.request.UriRequest r11 = r11.createNewRequest()     // Catch:{ Throwable -> 0x0151 }
                org.xutils.http.request.UriRequest unused = r10.request = r11     // Catch:{ Throwable -> 0x0151 }
                org.xutils.ex.HttpRedirectException r10 = new org.xutils.ex.HttpRedirectException     // Catch:{ Throwable -> 0x0151 }
                java.lang.String r11 = r4.getMessage()     // Catch:{ Throwable -> 0x0151 }
                java.lang.String r12 = r4.getResult()     // Catch:{ Throwable -> 0x0151 }
                r10.<init>(r2, r11, r12)     // Catch:{ Throwable -> 0x0151 }
                r14.f1213ex = r10     // Catch:{ Throwable -> 0x0151 }
            L_0x0092:
                java.lang.Class<java.io.File> r10 = java.io.File.class
                org.xutils.http.HttpTask r11 = org.xutils.http.HttpTask.this
                java.lang.reflect.Type r11 = r11.loadType
                if (r10 != r11) goto L_0x00b0
                java.util.concurrent.atomic.AtomicInteger r11 = org.xutils.http.HttpTask.sCurrFileLoadCount
                monitor-enter(r11)
                java.util.concurrent.atomic.AtomicInteger r10 = org.xutils.http.HttpTask.sCurrFileLoadCount     // Catch:{ all -> 0x0156 }
                r10.decrementAndGet()     // Catch:{ all -> 0x0156 }
                java.util.concurrent.atomic.AtomicInteger r10 = org.xutils.http.HttpTask.sCurrFileLoadCount     // Catch:{ all -> 0x0156 }
                r10.notifyAll()     // Catch:{ all -> 0x0156 }
                monitor-exit(r11)     // Catch:{ all -> 0x0156 }
            L_0x00b0:
                return
            L_0x00b1:
                r5 = move-exception
                r6 = 1
                monitor-exit(r11)     // Catch:{ all -> 0x002e }
            L_0x00b4:
                java.util.concurrent.atomic.AtomicInteger r10 = org.xutils.http.HttpTask.sCurrFileLoadCount     // Catch:{ Throwable -> 0x0031 }
                r10.incrementAndGet()     // Catch:{ Throwable -> 0x0031 }
            L_0x00bb:
                if (r6 != 0) goto L_0x00c5
                org.xutils.http.HttpTask r10 = org.xutils.http.HttpTask.this     // Catch:{ Throwable -> 0x0031 }
                boolean r10 = r10.isCancelled()     // Catch:{ Throwable -> 0x0031 }
                if (r10 == 0) goto L_0x0108
            L_0x00c5:
                org.xutils.common.Callback$CancelledException r11 = new org.xutils.common.Callback$CancelledException     // Catch:{ Throwable -> 0x0031 }
                java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0031 }
                r10.<init>()     // Catch:{ Throwable -> 0x0031 }
                java.lang.String r12 = "cancelled before request"
                java.lang.StringBuilder r12 = r10.append(r12)     // Catch:{ Throwable -> 0x0031 }
                if (r6 == 0) goto L_0x0104
                java.lang.String r10 = "(interrupted)"
            L_0x00d8:
                java.lang.StringBuilder r10 = r12.append(r10)     // Catch:{ Throwable -> 0x0031 }
                java.lang.String r10 = r10.toString()     // Catch:{ Throwable -> 0x0031 }
                r11.<init>(r10)     // Catch:{ Throwable -> 0x0031 }
                throw r11     // Catch:{ Throwable -> 0x0031 }
            L_0x00e4:
                r10 = move-exception
                java.lang.Class<java.io.File> r11 = java.io.File.class
                org.xutils.http.HttpTask r12 = org.xutils.http.HttpTask.this
                java.lang.reflect.Type r12 = r12.loadType
                if (r11 != r12) goto L_0x0103
                java.util.concurrent.atomic.AtomicInteger r11 = org.xutils.http.HttpTask.sCurrFileLoadCount
                monitor-enter(r11)
                java.util.concurrent.atomic.AtomicInteger r12 = org.xutils.http.HttpTask.sCurrFileLoadCount     // Catch:{ all -> 0x0159 }
                r12.decrementAndGet()     // Catch:{ all -> 0x0159 }
                java.util.concurrent.atomic.AtomicInteger r12 = org.xutils.http.HttpTask.sCurrFileLoadCount     // Catch:{ all -> 0x0159 }
                r12.notifyAll()     // Catch:{ all -> 0x0159 }
                monitor-exit(r11)     // Catch:{ all -> 0x0159 }
            L_0x0103:
                throw r10
            L_0x0104:
                java.lang.String r10 = ""
                goto L_0x00d8
            L_0x0108:
                org.xutils.http.HttpTask r10 = org.xutils.http.HttpTask.this     // Catch:{ Throwable -> 0x012a }
                org.xutils.http.request.UriRequest r10 = r10.request     // Catch:{ Throwable -> 0x012a }
                org.xutils.http.HttpTask r11 = org.xutils.http.HttpTask.this     // Catch:{ Throwable -> 0x012a }
                org.xutils.http.app.RequestInterceptListener r11 = r11.requestInterceptListener     // Catch:{ Throwable -> 0x012a }
                r10.setRequestInterceptListener(r11)     // Catch:{ Throwable -> 0x012a }
                org.xutils.http.HttpTask r10 = org.xutils.http.HttpTask.this     // Catch:{ Throwable -> 0x012a }
                org.xutils.http.request.UriRequest r10 = r10.request     // Catch:{ Throwable -> 0x012a }
                java.lang.Object r10 = r10.loadResult()     // Catch:{ Throwable -> 0x012a }
                r14.result = r10     // Catch:{ Throwable -> 0x012a }
            L_0x0123:
                java.lang.Throwable r10 = r14.f1213ex     // Catch:{ Throwable -> 0x0031 }
                if (r10 == 0) goto L_0x012e
                java.lang.Throwable r10 = r14.f1213ex     // Catch:{ Throwable -> 0x0031 }
                throw r10     // Catch:{ Throwable -> 0x0031 }
            L_0x012a:
                r3 = move-exception
                r14.f1213ex = r3     // Catch:{ Throwable -> 0x0031 }
                goto L_0x0123
            L_0x012e:
                java.lang.Class<java.io.File> r10 = java.io.File.class
                org.xutils.http.HttpTask r11 = org.xutils.http.HttpTask.this
                java.lang.reflect.Type r11 = r11.loadType
                if (r10 != r11) goto L_0x00b0
                java.util.concurrent.atomic.AtomicInteger r11 = org.xutils.http.HttpTask.sCurrFileLoadCount
                monitor-enter(r11)
                java.util.concurrent.atomic.AtomicInteger r10 = org.xutils.http.HttpTask.sCurrFileLoadCount     // Catch:{ all -> 0x014e }
                r10.decrementAndGet()     // Catch:{ all -> 0x014e }
                java.util.concurrent.atomic.AtomicInteger r10 = org.xutils.http.HttpTask.sCurrFileLoadCount     // Catch:{ all -> 0x014e }
                r10.notifyAll()     // Catch:{ all -> 0x014e }
                monitor-exit(r11)     // Catch:{ all -> 0x014e }
                goto L_0x00b0
            L_0x014e:
                r10 = move-exception
                monitor-exit(r11)     // Catch:{ all -> 0x014e }
                throw r10
            L_0x0151:
                r9 = move-exception
                r14.f1213ex = r3     // Catch:{ all -> 0x00e4 }
                goto L_0x0092
            L_0x0156:
                r10 = move-exception
                monitor-exit(r11)     // Catch:{ all -> 0x0156 }
                throw r10
            L_0x0159:
                r10 = move-exception
                monitor-exit(r11)     // Catch:{ all -> 0x0159 }
                throw r10
            L_0x015c:
                r10 = move-exception
                goto L_0x002c
            */
            throw new UnsupportedOperationException("Method not decompiled: org.xutils.http.HttpTask.RequestWorker.request():void");
        }
    }
}
