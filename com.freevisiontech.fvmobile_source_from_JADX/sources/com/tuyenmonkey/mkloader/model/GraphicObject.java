package com.tuyenmonkey.mkloader.model;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class GraphicObject {
    protected Paint paint = new Paint();

    public abstract void draw(Canvas canvas);

    public GraphicObject() {
        this.paint.setAntiAlias(true);
    }

    public void setColor(int color) {
        this.paint.setColor(color);
    }

    public void setAlpha(int alpha) {
        this.paint.setAlpha(alpha);
    }

    public void setWidth(float width) {
        this.paint.setStrokeWidth(width);
    }

    public void setStyle(Paint.Style style) {
        this.paint.setStyle(style);
    }
}
