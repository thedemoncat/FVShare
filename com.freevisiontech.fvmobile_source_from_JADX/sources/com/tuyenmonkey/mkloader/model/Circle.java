package com.tuyenmonkey.mkloader.model;

import android.graphics.Canvas;
import android.graphics.PointF;

public class Circle extends GraphicObject {
    private PointF center = new PointF();
    private float radius;

    public void setRadius(float radius2) {
        this.radius = radius2;
    }

    public void setCenter(float x, float y) {
        this.center.set(x, y);
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(this.center.x, this.center.y, this.radius, this.paint);
    }
}
