package com.bumptech.glide.load.resource.bitmap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;

public class GlideBitmapDrawable extends GlideDrawable {
    private boolean applyGravity;
    private final Rect destRect;
    private int height;
    private boolean mutated;
    private BitmapState state;
    private int width;

    public GlideBitmapDrawable(Resources res, Bitmap bitmap) {
        this(res, new BitmapState(bitmap));
    }

    GlideBitmapDrawable(Resources res, BitmapState state2) {
        int targetDensity;
        this.destRect = new Rect();
        if (state2 == null) {
            throw new NullPointerException("BitmapState must not be null");
        }
        this.state = state2;
        if (res != null) {
            int density = res.getDisplayMetrics().densityDpi;
            if (density == 0) {
                targetDensity = 160;
            } else {
                targetDensity = density;
            }
            state2.targetDensity = targetDensity;
        } else {
            targetDensity = state2.targetDensity;
        }
        this.width = state2.bitmap.getScaledWidth(targetDensity);
        this.height = state2.bitmap.getScaledHeight(targetDensity);
    }

    public int getIntrinsicWidth() {
        return this.width;
    }

    public int getIntrinsicHeight() {
        return this.height;
    }

    public boolean isAnimated() {
        return false;
    }

    public void setLoopCount(int loopCount) {
    }

    public void start() {
    }

    public void stop() {
    }

    public boolean isRunning() {
        return false;
    }

    /* access modifiers changed from: protected */
    public void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        this.applyGravity = true;
    }

    public Drawable.ConstantState getConstantState() {
        return this.state;
    }

    public void draw(Canvas canvas) {
        if (this.applyGravity) {
            Gravity.apply(119, this.width, this.height, getBounds(), this.destRect);
            this.applyGravity = false;
        }
        canvas.drawBitmap(this.state.bitmap, (Rect) null, this.destRect, this.state.paint);
    }

    public void setAlpha(int alpha) {
        if (this.state.paint.getAlpha() != alpha) {
            this.state.setAlpha(alpha);
            invalidateSelf();
        }
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.state.setColorFilter(colorFilter);
        invalidateSelf();
    }

    public int getOpacity() {
        Bitmap bm = this.state.bitmap;
        return (bm == null || bm.hasAlpha() || this.state.paint.getAlpha() < 255) ? -3 : -1;
    }

    public Drawable mutate() {
        if (!this.mutated && super.mutate() == this) {
            this.state = new BitmapState(this.state);
            this.mutated = true;
        }
        return this;
    }

    public Bitmap getBitmap() {
        return this.state.bitmap;
    }

    static class BitmapState extends Drawable.ConstantState {
        private static final Paint DEFAULT_PAINT = new Paint(6);
        private static final int DEFAULT_PAINT_FLAGS = 6;
        private static final int GRAVITY = 119;
        final Bitmap bitmap;
        Paint paint;
        int targetDensity;

        public BitmapState(Bitmap bitmap2) {
            this.paint = DEFAULT_PAINT;
            this.bitmap = bitmap2;
        }

        BitmapState(BitmapState other) {
            this(other.bitmap);
            this.targetDensity = other.targetDensity;
        }

        /* access modifiers changed from: package-private */
        public void setColorFilter(ColorFilter colorFilter) {
            mutatePaint();
            this.paint.setColorFilter(colorFilter);
        }

        /* access modifiers changed from: package-private */
        public void setAlpha(int alpha) {
            mutatePaint();
            this.paint.setAlpha(alpha);
        }

        /* access modifiers changed from: package-private */
        public void mutatePaint() {
            if (DEFAULT_PAINT == this.paint) {
                this.paint = new Paint(6);
            }
        }

        public Drawable newDrawable() {
            return new GlideBitmapDrawable((Resources) null, this);
        }

        public Drawable newDrawable(Resources res) {
            return new GlideBitmapDrawable(res, this);
        }

        public int getChangingConfigurations() {
            return 0;
        }
    }
}
