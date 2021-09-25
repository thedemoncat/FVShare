package com.bumptech.glide.load.engine;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.EngineRunnable;
import com.bumptech.glide.request.ResourceCallback;
import com.bumptech.glide.util.Util;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

class EngineJob implements EngineRunnable.EngineRunnableManager {
    private static final EngineResourceFactory DEFAULT_FACTORY = new EngineResourceFactory();
    private static final Handler MAIN_THREAD_HANDLER = new Handler(Looper.getMainLooper(), new MainThreadCallback());
    private static final int MSG_COMPLETE = 1;
    private static final int MSG_EXCEPTION = 2;
    private final List<ResourceCallback> cbs;
    private final ExecutorService diskCacheService;
    private EngineResource<?> engineResource;
    private final EngineResourceFactory engineResourceFactory;
    private EngineRunnable engineRunnable;
    private Exception exception;
    private volatile Future<?> future;
    private boolean hasException;
    private boolean hasResource;
    private Set<ResourceCallback> ignoredCallbacks;
    private final boolean isCacheable;
    private boolean isCancelled;
    private final Key key;
    private final EngineJobListener listener;
    private Resource<?> resource;
    private final ExecutorService sourceService;

    public EngineJob(Key key2, ExecutorService diskCacheService2, ExecutorService sourceService2, boolean isCacheable2, EngineJobListener listener2) {
        this(key2, diskCacheService2, sourceService2, isCacheable2, listener2, DEFAULT_FACTORY);
    }

    public EngineJob(Key key2, ExecutorService diskCacheService2, ExecutorService sourceService2, boolean isCacheable2, EngineJobListener listener2, EngineResourceFactory engineResourceFactory2) {
        this.cbs = new ArrayList();
        this.key = key2;
        this.diskCacheService = diskCacheService2;
        this.sourceService = sourceService2;
        this.isCacheable = isCacheable2;
        this.listener = listener2;
        this.engineResourceFactory = engineResourceFactory2;
    }

    public void start(EngineRunnable engineRunnable2) {
        this.engineRunnable = engineRunnable2;
        this.future = this.diskCacheService.submit(engineRunnable2);
    }

    public void submitForSource(EngineRunnable runnable) {
        this.future = this.sourceService.submit(runnable);
    }

    public void addCallback(ResourceCallback cb) {
        Util.assertMainThread();
        if (this.hasResource) {
            cb.onResourceReady(this.engineResource);
        } else if (this.hasException) {
            cb.onException(this.exception);
        } else {
            this.cbs.add(cb);
        }
    }

    public void removeCallback(ResourceCallback cb) {
        Util.assertMainThread();
        if (this.hasResource || this.hasException) {
            addIgnoredCallback(cb);
            return;
        }
        this.cbs.remove(cb);
        if (this.cbs.isEmpty()) {
            cancel();
        }
    }

    private void addIgnoredCallback(ResourceCallback cb) {
        if (this.ignoredCallbacks == null) {
            this.ignoredCallbacks = new HashSet();
        }
        this.ignoredCallbacks.add(cb);
    }

    private boolean isInIgnoredCallbacks(ResourceCallback cb) {
        return this.ignoredCallbacks != null && this.ignoredCallbacks.contains(cb);
    }

    /* access modifiers changed from: package-private */
    public void cancel() {
        if (!this.hasException && !this.hasResource && !this.isCancelled) {
            this.engineRunnable.cancel();
            Future currentFuture = this.future;
            if (currentFuture != null) {
                currentFuture.cancel(true);
            }
            this.isCancelled = true;
            this.listener.onEngineJobCancelled(this, this.key);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isCancelled() {
        return this.isCancelled;
    }

    public void onResourceReady(Resource<?> resource2) {
        this.resource = resource2;
        MAIN_THREAD_HANDLER.obtainMessage(1, this).sendToTarget();
    }

    /* access modifiers changed from: private */
    public void handleResultOnMainThread() {
        if (this.isCancelled) {
            this.resource.recycle();
        } else if (this.cbs.isEmpty()) {
            throw new IllegalStateException("Received a resource without any callbacks to notify");
        } else {
            this.engineResource = this.engineResourceFactory.build(this.resource, this.isCacheable);
            this.hasResource = true;
            this.engineResource.acquire();
            this.listener.onEngineJobComplete(this.key, this.engineResource);
            for (ResourceCallback cb : this.cbs) {
                if (!isInIgnoredCallbacks(cb)) {
                    this.engineResource.acquire();
                    cb.onResourceReady(this.engineResource);
                }
            }
            this.engineResource.release();
        }
    }

    public void onException(Exception e) {
        this.exception = e;
        MAIN_THREAD_HANDLER.obtainMessage(2, this).sendToTarget();
    }

    /* access modifiers changed from: private */
    public void handleExceptionOnMainThread() {
        if (!this.isCancelled) {
            if (this.cbs.isEmpty()) {
                throw new IllegalStateException("Received an exception without any callbacks to notify");
            }
            this.hasException = true;
            this.listener.onEngineJobComplete(this.key, (EngineResource<?>) null);
            for (ResourceCallback cb : this.cbs) {
                if (!isInIgnoredCallbacks(cb)) {
                    cb.onException(this.exception);
                }
            }
        }
    }

    static class EngineResourceFactory {
        EngineResourceFactory() {
        }

        public <R> EngineResource<R> build(Resource<R> resource, boolean isMemoryCacheable) {
            return new EngineResource<>(resource, isMemoryCacheable);
        }
    }

    private static class MainThreadCallback implements Handler.Callback {
        private MainThreadCallback() {
        }

        public boolean handleMessage(Message message) {
            if (1 != message.what && 2 != message.what) {
                return false;
            }
            EngineJob job = (EngineJob) message.obj;
            if (1 == message.what) {
                job.handleResultOnMainThread();
                return true;
            }
            job.handleExceptionOnMainThread();
            return true;
        }
    }
}
