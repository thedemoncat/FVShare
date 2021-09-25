package org.xutils.image;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import java.lang.ref.WeakReference;

public final class AsyncDrawable extends Drawable {
    private Drawable baseDrawable;
    private final WeakReference<ImageLoader> imageLoaderReference;

    public AsyncDrawable(ImageLoader imageLoader, Drawable drawable) {
        if (imageLoader == null) {
            throw new IllegalArgumentException("imageLoader may not be null");
        }
        this.baseDrawable = drawable;
        while (this.baseDrawable instanceof AsyncDrawable) {
            this.baseDrawable = ((AsyncDrawable) this.baseDrawable).baseDrawable;
        }
        this.imageLoaderReference = new WeakReference<>(imageLoader);
    }

    public ImageLoader getImageLoader() {
        return (ImageLoader) this.imageLoaderReference.get();
    }

    public void setBaseDrawable(Drawable baseDrawable2) {
        this.baseDrawable = baseDrawable2;
    }

    public Drawable getBaseDrawable() {
        return this.baseDrawable;
    }

    public void draw(Canvas canvas) {
        if (this.baseDrawable != null) {
            this.baseDrawable.draw(canvas);
        }
    }

    public void setAlpha(int i) {
        if (this.baseDrawable != null) {
            this.baseDrawable.setAlpha(i);
        }
    }

    public void setColorFilter(ColorFilter colorFilter) {
        if (this.baseDrawable != null) {
            this.baseDrawable.setColorFilter(colorFilter);
        }
    }

    public int getOpacity() {
        if (this.baseDrawable == null) {
            return -3;
        }
        return this.baseDrawable.getOpacity();
    }

    public void setBounds(int left, int top, int right, int bottom) {
        if (this.baseDrawable != null) {
            this.baseDrawable.setBounds(left, top, right, bottom);
        }
    }

    public void setBounds(Rect bounds) {
        if (this.baseDrawable != null) {
            this.baseDrawable.setBounds(bounds);
        }
    }

    public void setChangingConfigurations(int configs) {
        if (this.baseDrawable != null) {
            this.baseDrawable.setChangingConfigurations(configs);
        }
    }

    public int getChangingConfigurations() {
        if (this.baseDrawable == null) {
            return 0;
        }
        return this.baseDrawable.getChangingConfigurations();
    }

    public void setDither(boolean dither) {
        if (this.baseDrawable != null) {
            this.baseDrawable.setDither(dither);
        }
    }

    public void setFilterBitmap(boolean filter) {
        if (this.baseDrawable != null) {
            this.baseDrawable.setFilterBitmap(filter);
        }
    }

    public void invalidateSelf() {
        if (this.baseDrawable != null) {
            this.baseDrawable.invalidateSelf();
        }
    }

    public void scheduleSelf(Runnable what, long when) {
        if (this.baseDrawable != null) {
            this.baseDrawable.scheduleSelf(what, when);
        }
    }

    public void unscheduleSelf(Runnable what) {
        if (this.baseDrawable != null) {
            this.baseDrawable.unscheduleSelf(what);
        }
    }

    public void setColorFilter(int color, PorterDuff.Mode mode) {
        if (this.baseDrawable != null) {
            this.baseDrawable.setColorFilter(color, mode);
        }
    }

    public void clearColorFilter() {
        if (this.baseDrawable != null) {
            this.baseDrawable.clearColorFilter();
        }
    }

    public boolean isStateful() {
        return this.baseDrawable != null && this.baseDrawable.isStateful();
    }

    public boolean setState(int[] stateSet) {
        return this.baseDrawable != null && this.baseDrawable.setState(stateSet);
    }

    public int[] getState() {
        if (this.baseDrawable == null) {
            return null;
        }
        return this.baseDrawable.getState();
    }

    public Drawable getCurrent() {
        if (this.baseDrawable == null) {
            return null;
        }
        return this.baseDrawable.getCurrent();
    }

    public boolean setVisible(boolean visible, boolean restart) {
        return this.baseDrawable != null && this.baseDrawable.setVisible(visible, restart);
    }

    public Region getTransparentRegion() {
        if (this.baseDrawable == null) {
            return null;
        }
        return this.baseDrawable.getTransparentRegion();
    }

    public int getIntrinsicWidth() {
        if (this.baseDrawable == null) {
            return 0;
        }
        return this.baseDrawable.getIntrinsicWidth();
    }

    public int getIntrinsicHeight() {
        if (this.baseDrawable == null) {
            return 0;
        }
        return this.baseDrawable.getIntrinsicHeight();
    }

    public int getMinimumWidth() {
        if (this.baseDrawable == null) {
            return 0;
        }
        return this.baseDrawable.getMinimumWidth();
    }

    public int getMinimumHeight() {
        if (this.baseDrawable == null) {
            return 0;
        }
        return this.baseDrawable.getMinimumHeight();
    }

    public boolean getPadding(Rect padding) {
        return this.baseDrawable != null && this.baseDrawable.getPadding(padding);
    }

    public Drawable mutate() {
        if (this.baseDrawable == null) {
            return null;
        }
        return this.baseDrawable.mutate();
    }

    public Drawable.ConstantState getConstantState() {
        if (this.baseDrawable == null) {
            return null;
        }
        return this.baseDrawable.getConstantState();
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        super.finalize();
        ImageLoader imageLoader = getImageLoader();
        if (imageLoader != null) {
            imageLoader.cancel();
        }
    }
}
