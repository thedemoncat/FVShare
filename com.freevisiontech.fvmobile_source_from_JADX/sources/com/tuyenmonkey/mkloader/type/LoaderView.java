package com.tuyenmonkey.mkloader.type;

import android.graphics.Canvas;
import android.graphics.PointF;
import com.tuyenmonkey.mkloader.callback.InvalidateListener;

public abstract class LoaderView {
    protected PointF center;
    protected int color;
    protected int desiredHeight = 150;
    protected int desiredWidth = 150;
    protected int height;
    protected InvalidateListener invalidateListener;
    protected int width;

    public abstract void draw(Canvas canvas);

    public abstract void initializeObjects();

    public abstract void setUpAnimation();

    public void setColor(int color2) {
        this.color = color2;
    }

    public void setSize(int width2, int height2) {
        this.width = width2;
        this.height = height2;
        this.center = new PointF(((float) width2) / 2.0f, ((float) height2) / 2.0f);
    }

    public void setInvalidateListener(InvalidateListener invalidateListener2) {
        this.invalidateListener = invalidateListener2;
    }

    public int getDesiredWidth() {
        return this.desiredWidth;
    }

    public int getDesiredHeight() {
        return this.desiredHeight;
    }

    public boolean isDetached() {
        return this.invalidateListener == null;
    }

    public void onDetach() {
        if (this.invalidateListener != null) {
            this.invalidateListener = null;
        }
    }
}
