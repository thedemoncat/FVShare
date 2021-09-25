package com.tuyenmonkey.mkloader.model;

import android.graphics.Canvas;
import android.graphics.RectF;

public class Arc extends GraphicObject {
    private RectF oval;
    private float startAngle;
    private float sweepAngle;
    private boolean useCenter;

    public void setOval(RectF oval2) {
        this.oval = oval2;
    }

    public void setStartAngle(float startAngle2) {
        this.startAngle = startAngle2;
    }

    public void setSweepAngle(float sweepAngle2) {
        this.sweepAngle = sweepAngle2;
    }

    public void setUseCenter(boolean useCenter2) {
        this.useCenter = useCenter2;
    }

    public float getStartAngle() {
        return this.startAngle;
    }

    public void draw(Canvas canvas) {
        canvas.drawArc(this.oval, this.startAngle, this.sweepAngle, this.useCenter, this.paint);
    }
}
