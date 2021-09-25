package com.bumptech.glide.request;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.util.Util;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RequestFutureTarget<T, R> implements FutureTarget<R>, Runnable {
    private static final Waiter DEFAULT_WAITER = new Waiter();
    private final boolean assertBackgroundThread;
    private Exception exception;
    private boolean exceptionReceived;
    private final int height;
    private boolean isCancelled;
    private final Handler mainHandler;
    private Request request;
    private R resource;
    private boolean resultReceived;
    private final Waiter waiter;
    private final int width;

    public RequestFutureTarget(Handler mainHandler2, int width2, int height2) {
        this(mainHandler2, width2, height2, true, DEFAULT_WAITER);
    }

    RequestFutureTarget(Handler mainHandler2, int width2, int height2, boolean assertBackgroundThread2, Waiter waiter2) {
        this.mainHandler = mainHandler2;
        this.width = width2;
        this.height = height2;
        this.assertBackgroundThread = assertBackgroundThread2;
        this.waiter = waiter2;
    }

    public synchronized boolean cancel(boolean mayInterruptIfRunning) {
        boolean result = true;
        synchronized (this) {
            if (!this.isCancelled) {
                if (isDone()) {
                    result = false;
                }
                if (result) {
                    this.isCancelled = true;
                    if (mayInterruptIfRunning) {
                        clear();
                    }
                    this.waiter.notifyAll(this);
                }
            }
        }
        return result;
    }

    public synchronized boolean isCancelled() {
        return this.isCancelled;
    }

    public synchronized boolean isDone() {
        return this.isCancelled || this.resultReceived;
    }

    public R get() throws InterruptedException, ExecutionException {
        try {
            return doGet((Long) null);
        } catch (TimeoutException e) {
            throw new AssertionError(e);
        }
    }

    public R get(long time, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        return doGet(Long.valueOf(timeUnit.toMillis(time)));
    }

    public void getSize(SizeReadyCallback cb) {
        cb.onSizeReady(this.width, this.height);
    }

    public void setRequest(Request request2) {
        this.request = request2;
    }

    public Request getRequest() {
        return this.request;
    }

    public void onLoadCleared(Drawable placeholder) {
    }

    public void onLoadStarted(Drawable placeholder) {
    }

    public synchronized void onLoadFailed(Exception e, Drawable errorDrawable) {
        this.exceptionReceived = true;
        this.exception = e;
        this.waiter.notifyAll(this);
    }

    public synchronized void onResourceReady(R resource2, GlideAnimation<? super R> glideAnimation) {
        this.resultReceived = true;
        this.resource = resource2;
        this.waiter.notifyAll(this);
    }

    private synchronized R doGet(Long timeoutMillis) throws ExecutionException, InterruptedException, TimeoutException {
        R r;
        if (this.assertBackgroundThread) {
            Util.assertBackgroundThread();
        }
        if (this.isCancelled) {
            throw new CancellationException();
        } else if (this.exceptionReceived) {
            throw new ExecutionException(this.exception);
        } else if (this.resultReceived) {
            r = this.resource;
        } else {
            if (timeoutMillis == null) {
                this.waiter.waitForTimeout(this, 0);
            } else if (timeoutMillis.longValue() > 0) {
                this.waiter.waitForTimeout(this, timeoutMillis.longValue());
            }
            if (Thread.interrupted()) {
                throw new InterruptedException();
            } else if (this.exceptionReceived) {
                throw new ExecutionException(this.exception);
            } else if (this.isCancelled) {
                throw new CancellationException();
            } else if (!this.resultReceived) {
                throw new TimeoutException();
            } else {
                r = this.resource;
            }
        }
        return r;
    }

    public void run() {
        if (this.request != null) {
            this.request.clear();
            cancel(false);
        }
    }

    public void clear() {
        this.mainHandler.post(this);
    }

    public void onStart() {
    }

    public void onStop() {
    }

    public void onDestroy() {
    }

    static class Waiter {
        Waiter() {
        }

        public void waitForTimeout(Object toWaitOn, long timeoutMillis) throws InterruptedException {
            toWaitOn.wait(timeoutMillis);
        }

        public void notifyAll(Object toNotify) {
            toNotify.notifyAll();
        }
    }
}
