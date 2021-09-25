package com.bumptech.glide.load.resource.gif;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.NullEncoder;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.UUID;

class GifFrameLoader {
    private final FrameCallback callback;
    private DelayTarget current;
    private final GifDecoder gifDecoder;
    private final Handler handler;
    private boolean isCleared;
    private boolean isLoadPending;
    private boolean isRunning;
    private GenericRequestBuilder<GifDecoder, GifDecoder, Bitmap, Bitmap> requestBuilder;

    public interface FrameCallback {
        void onFrameReady(int i);
    }

    public GifFrameLoader(Context context, FrameCallback callback2, GifDecoder gifDecoder2, int width, int height) {
        this(callback2, gifDecoder2, (Handler) null, getRequestBuilder(context, gifDecoder2, width, height, Glide.get(context).getBitmapPool()));
    }

    GifFrameLoader(FrameCallback callback2, GifDecoder gifDecoder2, Handler handler2, GenericRequestBuilder<GifDecoder, GifDecoder, Bitmap, Bitmap> requestBuilder2) {
        this.isRunning = false;
        this.isLoadPending = false;
        handler2 = handler2 == null ? new Handler(Looper.getMainLooper(), new FrameLoaderCallback()) : handler2;
        this.callback = callback2;
        this.gifDecoder = gifDecoder2;
        this.handler = handler2;
        this.requestBuilder = requestBuilder2;
    }

    public void setFrameTransformation(Transformation<Bitmap> transformation) {
        if (transformation == null) {
            throw new NullPointerException("Transformation must not be null");
        }
        this.requestBuilder = this.requestBuilder.transform(transformation);
    }

    public void start() {
        if (!this.isRunning) {
            this.isRunning = true;
            this.isCleared = false;
            loadNextFrame();
        }
    }

    public void stop() {
        this.isRunning = false;
    }

    public void clear() {
        stop();
        if (this.current != null) {
            Glide.clear((Target<?>) this.current);
            this.current = null;
        }
        this.isCleared = true;
    }

    public Bitmap getCurrentFrame() {
        if (this.current != null) {
            return this.current.getResource();
        }
        return null;
    }

    private void loadNextFrame() {
        if (this.isRunning && !this.isLoadPending) {
            this.isLoadPending = true;
            this.gifDecoder.advance();
            this.requestBuilder.signature(new FrameSignature()).into(new DelayTarget(this.handler, this.gifDecoder.getCurrentFrameIndex(), SystemClock.uptimeMillis() + ((long) this.gifDecoder.getNextDelay())));
        }
    }

    /* access modifiers changed from: package-private */
    public void onFrameReady(DelayTarget delayTarget) {
        if (this.isCleared) {
            this.handler.obtainMessage(2, delayTarget).sendToTarget();
            return;
        }
        DelayTarget previous = this.current;
        this.current = delayTarget;
        this.callback.onFrameReady(delayTarget.index);
        if (previous != null) {
            this.handler.obtainMessage(2, previous).sendToTarget();
        }
        this.isLoadPending = false;
        loadNextFrame();
    }

    private class FrameLoaderCallback implements Handler.Callback {
        public static final int MSG_CLEAR = 2;
        public static final int MSG_DELAY = 1;

        private FrameLoaderCallback() {
        }

        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                GifFrameLoader.this.onFrameReady((DelayTarget) msg.obj);
                return true;
            }
            if (msg.what == 2) {
                Glide.clear((Target<?>) (DelayTarget) msg.obj);
            }
            return false;
        }
    }

    static class DelayTarget extends SimpleTarget<Bitmap> {
        private final Handler handler;
        /* access modifiers changed from: private */
        public final int index;
        private Bitmap resource;
        private final long targetTime;

        public DelayTarget(Handler handler2, int index2, long targetTime2) {
            this.handler = handler2;
            this.index = index2;
            this.targetTime = targetTime2;
        }

        public Bitmap getResource() {
            return this.resource;
        }

        public void onResourceReady(Bitmap resource2, GlideAnimation<? super Bitmap> glideAnimation) {
            this.resource = resource2;
            this.handler.sendMessageAtTime(this.handler.obtainMessage(1, this), this.targetTime);
        }
    }

    private static GenericRequestBuilder<GifDecoder, GifDecoder, Bitmap, Bitmap> getRequestBuilder(Context context, GifDecoder gifDecoder2, int width, int height, BitmapPool bitmapPool) {
        GifFrameResourceDecoder frameResourceDecoder = new GifFrameResourceDecoder(bitmapPool);
        GifFrameModelLoader frameLoader = new GifFrameModelLoader();
        return Glide.with(context).using(frameLoader, GifDecoder.class).load(gifDecoder2).mo18167as(Bitmap.class).sourceEncoder(NullEncoder.get()).decoder(frameResourceDecoder).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).override(width, height);
    }

    static class FrameSignature implements Key {
        private final UUID uuid;

        public FrameSignature() {
            this(UUID.randomUUID());
        }

        FrameSignature(UUID uuid2) {
            this.uuid = uuid2;
        }

        public boolean equals(Object o) {
            if (o instanceof FrameSignature) {
                return ((FrameSignature) o).uuid.equals(this.uuid);
            }
            return false;
        }

        public int hashCode() {
            return this.uuid.hashCode();
        }

        public void updateDiskCacheKey(MessageDigest messageDigest) throws UnsupportedEncodingException {
            throw new UnsupportedOperationException("Not implemented");
        }
    }
}
