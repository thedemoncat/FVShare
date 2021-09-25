package com.bumptech.glide.load.resource.gif;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.gifdecoder.GifHeader;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifFrameLoader;

public class GifDrawable extends GlideDrawable implements GifFrameLoader.FrameCallback {
    private boolean applyGravity;
    private final GifDecoder decoder;
    private final Rect destRect;
    private final GifFrameLoader frameLoader;
    private boolean isRecycled;
    private boolean isRunning;
    private boolean isStarted;
    private boolean isVisible;
    private int loopCount;
    private int maxLoopCount;
    private final Paint paint;
    private final GifState state;

    public GifDrawable(Context context, GifDecoder.BitmapProvider bitmapProvider, BitmapPool bitmapPool, Transformation<Bitmap> frameTransformation, int targetFrameWidth, int targetFrameHeight, GifHeader gifHeader, byte[] data, Bitmap firstFrame) {
        this(new GifState(gifHeader, data, context, frameTransformation, targetFrameWidth, targetFrameHeight, bitmapProvider, bitmapPool, firstFrame));
    }

    public GifDrawable(GifDrawable other, Bitmap firstFrame, Transformation<Bitmap> frameTransformation) {
        this(new GifState(other.state.gifHeader, other.state.data, other.state.context, frameTransformation, other.state.targetWidth, other.state.targetHeight, other.state.bitmapProvider, other.state.bitmapPool, firstFrame));
    }

    GifDrawable(GifState state2) {
        this.destRect = new Rect();
        this.isVisible = true;
        this.maxLoopCount = -1;
        if (state2 == null) {
            throw new NullPointerException("GifState must not be null");
        }
        this.state = state2;
        this.decoder = new GifDecoder(state2.bitmapProvider);
        this.paint = new Paint();
        this.decoder.setData(state2.gifHeader, state2.data);
        this.frameLoader = new GifFrameLoader(state2.context, this, this.decoder, state2.targetWidth, state2.targetHeight);
        this.frameLoader.setFrameTransformation(state2.frameTransformation);
    }

    GifDrawable(GifDecoder decoder2, GifFrameLoader frameLoader2, Bitmap firstFrame, BitmapPool bitmapPool, Paint paint2) {
        this.destRect = new Rect();
        this.isVisible = true;
        this.maxLoopCount = -1;
        this.decoder = decoder2;
        this.frameLoader = frameLoader2;
        this.state = new GifState((GifState) null);
        this.paint = paint2;
        this.state.bitmapPool = bitmapPool;
        this.state.firstFrame = firstFrame;
    }

    public Bitmap getFirstFrame() {
        return this.state.firstFrame;
    }

    public void setFrameTransformation(Transformation<Bitmap> frameTransformation, Bitmap firstFrame) {
        if (firstFrame == null) {
            throw new NullPointerException("The first frame of the GIF must not be null");
        } else if (frameTransformation == null) {
            throw new NullPointerException("The frame transformation must not be null");
        } else {
            this.state.frameTransformation = frameTransformation;
            this.state.firstFrame = firstFrame;
            this.frameLoader.setFrameTransformation(frameTransformation);
        }
    }

    public GifDecoder getDecoder() {
        return this.decoder;
    }

    public Transformation<Bitmap> getFrameTransformation() {
        return this.state.frameTransformation;
    }

    public byte[] getData() {
        return this.state.data;
    }

    public int getFrameCount() {
        return this.decoder.getFrameCount();
    }

    private void resetLoopCount() {
        this.loopCount = 0;
    }

    public void start() {
        this.isStarted = true;
        resetLoopCount();
        if (this.isVisible) {
            startRunning();
        }
    }

    public void stop() {
        this.isStarted = false;
        stopRunning();
        if (Build.VERSION.SDK_INT < 11) {
            reset();
        }
    }

    private void reset() {
        this.frameLoader.clear();
        invalidateSelf();
    }

    private void startRunning() {
        if (this.decoder.getFrameCount() == 1) {
            invalidateSelf();
        } else if (!this.isRunning) {
            this.isRunning = true;
            this.frameLoader.start();
            invalidateSelf();
        }
    }

    private void stopRunning() {
        this.isRunning = false;
        this.frameLoader.stop();
    }

    public boolean setVisible(boolean visible, boolean restart) {
        this.isVisible = visible;
        if (!visible) {
            stopRunning();
        } else if (this.isStarted) {
            startRunning();
        }
        return super.setVisible(visible, restart);
    }

    public int getIntrinsicWidth() {
        return this.state.firstFrame.getWidth();
    }

    public int getIntrinsicHeight() {
        return this.state.firstFrame.getHeight();
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    /* access modifiers changed from: package-private */
    public void setIsRunning(boolean isRunning2) {
        this.isRunning = isRunning2;
    }

    /* access modifiers changed from: protected */
    public void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        this.applyGravity = true;
    }

    public void draw(Canvas canvas) {
        if (!this.isRecycled) {
            if (this.applyGravity) {
                Gravity.apply(119, getIntrinsicWidth(), getIntrinsicHeight(), getBounds(), this.destRect);
                this.applyGravity = false;
            }
            Bitmap currentFrame = this.frameLoader.getCurrentFrame();
            canvas.drawBitmap(currentFrame != null ? currentFrame : this.state.firstFrame, (Rect) null, this.destRect, this.paint);
        }
    }

    public void setAlpha(int i) {
        this.paint.setAlpha(i);
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.paint.setColorFilter(colorFilter);
    }

    public int getOpacity() {
        return -2;
    }

    @TargetApi(11)
    public void onFrameReady(int frameIndex) {
        if (Build.VERSION.SDK_INT < 11 || getCallback() != null) {
            invalidateSelf();
            if (frameIndex == this.decoder.getFrameCount() - 1) {
                this.loopCount++;
            }
            if (this.maxLoopCount != -1 && this.loopCount >= this.maxLoopCount) {
                stop();
                return;
            }
            return;
        }
        stop();
        reset();
    }

    public Drawable.ConstantState getConstantState() {
        return this.state;
    }

    public void recycle() {
        this.isRecycled = true;
        this.state.bitmapPool.put(this.state.firstFrame);
        this.frameLoader.clear();
        this.frameLoader.stop();
    }

    /* access modifiers changed from: package-private */
    public boolean isRecycled() {
        return this.isRecycled;
    }

    public boolean isAnimated() {
        return true;
    }

    public void setLoopCount(int loopCount2) {
        if (loopCount2 <= 0 && loopCount2 != -1 && loopCount2 != 0) {
            throw new IllegalArgumentException("Loop count must be greater than 0, or equal to GlideDrawable.LOOP_FOREVER, or equal to GlideDrawable.LOOP_INTRINSIC");
        } else if (loopCount2 == 0) {
            this.maxLoopCount = this.decoder.getLoopCount();
        } else {
            this.maxLoopCount = loopCount2;
        }
    }

    static class GifState extends Drawable.ConstantState {
        private static final int GRAVITY = 119;
        BitmapPool bitmapPool;
        GifDecoder.BitmapProvider bitmapProvider;
        Context context;
        byte[] data;
        Bitmap firstFrame;
        Transformation<Bitmap> frameTransformation;
        GifHeader gifHeader;
        int targetHeight;
        int targetWidth;

        public GifState(GifHeader header, byte[] data2, Context context2, Transformation<Bitmap> frameTransformation2, int targetWidth2, int targetHeight2, GifDecoder.BitmapProvider provider, BitmapPool bitmapPool2, Bitmap firstFrame2) {
            if (firstFrame2 == null) {
                throw new NullPointerException("The first frame of the GIF must not be null");
            }
            this.gifHeader = header;
            this.data = data2;
            this.bitmapPool = bitmapPool2;
            this.firstFrame = firstFrame2;
            this.context = context2.getApplicationContext();
            this.frameTransformation = frameTransformation2;
            this.targetWidth = targetWidth2;
            this.targetHeight = targetHeight2;
            this.bitmapProvider = provider;
        }

        public GifState(GifState original) {
            if (original != null) {
                this.gifHeader = original.gifHeader;
                this.data = original.data;
                this.context = original.context;
                this.frameTransformation = original.frameTransformation;
                this.targetWidth = original.targetWidth;
                this.targetHeight = original.targetHeight;
                this.bitmapProvider = original.bitmapProvider;
                this.bitmapPool = original.bitmapPool;
                this.firstFrame = original.firstFrame;
            }
        }

        public Drawable newDrawable(Resources res) {
            return newDrawable();
        }

        public Drawable newDrawable() {
            return new GifDrawable(this);
        }

        public int getChangingConfigurations() {
            return 0;
        }
    }
}
