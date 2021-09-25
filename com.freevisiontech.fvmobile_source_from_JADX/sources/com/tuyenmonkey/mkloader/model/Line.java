package com.tuyenmonkey.mkloader.model;

import android.graphics.Canvas;
import android.graphics.PointF;

public class Line extends GraphicObject {
    private PointF point1;
    private PointF point2;

    public void setPoint1(PointF point12) {
        this.point1 = point12;
    }

    public void setPoint2(PointF point22) {
        this.point2 = point22;
    }

    public PointF getPoint1() {
        return this.point1;
    }

    public PointF getPoint2() {
        return this.point2;
    }

    public void draw(Canvas canvas) {
        canvas.drawLine(this.point1.x, this.point1.y, this.point2.x, this.point2.y, this.paint);
    }
}
