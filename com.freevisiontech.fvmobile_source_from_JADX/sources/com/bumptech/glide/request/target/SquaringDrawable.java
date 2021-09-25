package com.bumptech.glide.request.target;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;

public class SquaringDrawable extends GlideDrawable {
    private boolean mutated;
    private State state;
    private GlideDrawable wrapped;

    public SquaringDrawable(GlideDrawable wrapped2, int side) {
        this(new State(wrapped2.getConstantState(), side), wrapped2, (Resources) null);
    }

    SquaringDrawable(State state2, GlideDrawable wrapped2, Resources res) {
        this.state = state2;
        if (wrapped2 != null) {
            this.wrapped = wrapped2;
        } else if (res != null) {
            this.wrapped = (GlideDrawable) state2.wrapped.newDrawable(res);
        } else {
            this.wrapped = (GlideDrawable) state2.wrapped.newDrawable();
        }
    }

    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        this.wrapped.setBounds(left, top, right, bottom);
    }

    public void setBounds(Rect bounds) {
        super.setBounds(bounds);
        this.wrapped.setBounds(bounds);
    }

    public void setChangingConfigurations(int configs) {
        this.wrapped.setChangingConfigurations(configs);
    }

    public int getChangingConfigurations() {
        return this.wrapped.getChangingConfigurations();
    }

    public void setDither(boolean dither) {
        this.wrapped.setDither(dither);
    }

    public void setFilterBitmap(boolean filter) {
        this.wrapped.setFilterBitmap(filter);
    }

    @TargetApi(11)
    public Drawable.Callback getCallback() {
        return this.wrapped.getCallback();
    }

    @TargetApi(19)
    public int getAlpha() {
        return this.wrapped.getAlpha();
    }

    public void setColorFilter(int color, PorterDuff.Mode mode) {
        this.wrapped.setColorFilter(color, mode);
    }

    public void clearColorFilter() {
        this.wrapped.clearColorFilter();
    }

    public Drawable getCurrent() {
        return this.wrapped.getCurrent();
    }

    public boolean setVisible(boolean visible, boolean restart) {
        return this.wrapped.setVisible(visible, restart);
    }

    public int getIntrinsicWidth() {
        return this.state.side;
    }

    public int getIntrinsicHeight() {
        return this.state.side;
    }

    public int getMinimumWidth() {
        return this.wrapped.getMinimumWidth();
    }

    public int getMinimumHeight() {
        return this.wrapped.getMinimumHeight();
    }

    public boolean getPadding(Rect padding) {
        return this.wrapped.getPadding(padding);
    }

    public void invalidateSelf() {
        super.invalidateSelf();
        this.wrapped.invalidateSelf();
    }

    public void unscheduleSelf(Runnable what) {
        super.unscheduleSelf(what);
        this.wrapped.unscheduleSelf(what);
    }

    public void scheduleSelf(Runnable what, long when) {
        super.scheduleSelf(what, when);
        this.wrapped.scheduleSelf(what, when);
    }

    public void draw(Canvas canvas) {
        this.wrapped.draw(canvas);
    }

    public void setAlpha(int i) {
        this.wrapped.setAlpha(i);
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.wrapped.setColorFilter(colorFilter);
    }

    public int getOpacity() {
        return this.wrapped.getOpacity();
    }

    public boolean isAnimated() {
        return this.wrapped.isAnimated();
    }

    public void setLoopCount(int loopCount) {
        this.wrapped.setLoopCount(loopCount);
    }

    public void start() {
        this.wrapped.start();
    }

    public void stop() {
        this.wrapped.stop();
    }

    public boolean isRunning() {
        return this.wrapped.isRunning();
    }

    public Drawable mutate() {
        if (!this.mutated && super.mutate() == this) {
            this.wrapped = (GlideDrawable) this.wrapped.mutate();
            this.state = new State(this.state);
            this.mutated = true;
        }
        return this;
    }

    public Drawable.ConstantState getConstantState() {
        return this.state;
    }

    static class State extends Drawable.ConstantState {
        /* access modifiers changed from: private */
        public final int side;
        /* access modifiers changed from: private */
        public final Drawable.ConstantState wrapped;

        State(State other) {
            this(other.wrapped, other.side);
        }

        State(Drawable.ConstantState wrapped2, int side2) {
            this.wrapped = wrapped2;
            this.side = side2;
        }

        public Drawable newDrawable() {
            return newDrawable((Resources) null);
        }

        public Drawable newDrawable(Resources res) {
            return new SquaringDrawable(this, (GlideDrawable) null, res);
        }

        public int getChangingConfigurations() {
            return 0;
        }
    }
}
