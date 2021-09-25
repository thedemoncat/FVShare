package com.freevisiontech.fvmobile.widget.mosaic;

public class Point {

    /* renamed from: x */
    public float f1111x;

    /* renamed from: y */
    public float f1112y;

    public Point(float x, float y) {
        this.f1111x = x;
        this.f1112y = y;
    }

    public Point clone() {
        return new Point(this.f1111x, this.f1112y);
    }
}
