package com.bumptech.glide.load.engine;

import android.os.Looper;
import com.bumptech.glide.load.Key;

class EngineResource<Z> implements Resource<Z> {
    private int acquired;
    private final boolean isCacheable;
    private boolean isRecycled;
    private Key key;
    private ResourceListener listener;
    private final Resource<Z> resource;

    interface ResourceListener {
        void onResourceReleased(Key key, EngineResource<?> engineResource);
    }

    EngineResource(Resource<Z> toWrap, boolean isCacheable2) {
        if (toWrap == null) {
            throw new NullPointerException("Wrapped resource must not be null");
        }
        this.resource = toWrap;
        this.isCacheable = isCacheable2;
    }

    /* access modifiers changed from: package-private */
    public void setResourceListener(Key key2, ResourceListener listener2) {
        this.key = key2;
        this.listener = listener2;
    }

    /* access modifiers changed from: package-private */
    public boolean isCacheable() {
        return this.isCacheable;
    }

    public Z get() {
        return this.resource.get();
    }

    public int getSize() {
        return this.resource.getSize();
    }

    public void recycle() {
        if (this.acquired > 0) {
            throw new IllegalStateException("Cannot recycle a resource while it is still acquired");
        } else if (this.isRecycled) {
            throw new IllegalStateException("Cannot recycle a resource that has already been recycled");
        } else {
            this.isRecycled = true;
            this.resource.recycle();
        }
    }

    /* access modifiers changed from: package-private */
    public void acquire() {
        if (this.isRecycled) {
            throw new IllegalStateException("Cannot acquire a recycled resource");
        } else if (!Looper.getMainLooper().equals(Looper.myLooper())) {
            throw new IllegalThreadStateException("Must call acquire on the main thread");
        } else {
            this.acquired++;
        }
    }

    /* access modifiers changed from: package-private */
    public void release() {
        if (this.acquired <= 0) {
            throw new IllegalStateException("Cannot release a recycled or not yet acquired resource");
        } else if (!Looper.getMainLooper().equals(Looper.myLooper())) {
            throw new IllegalThreadStateException("Must call release on the main thread");
        } else {
            int i = this.acquired - 1;
            this.acquired = i;
            if (i == 0) {
                this.listener.onResourceReleased(this.key, this);
            }
        }
    }
}
