package org.opencv.core;

public class Rect {
    public int height;
    public int width;

    /* renamed from: x */
    public int f1130x;

    /* renamed from: y */
    public int f1131y;

    public Rect(int x, int y, int width2, int height2) {
        this.f1130x = x;
        this.f1131y = y;
        this.width = width2;
        this.height = height2;
    }

    public Rect() {
        this(0, 0, 0, 0);
    }

    public Rect(Point p1, Point p2) {
        this.f1130x = (int) (p1.f1125x < p2.f1125x ? p1.f1125x : p2.f1125x);
        this.f1131y = (int) (p1.f1126y < p2.f1126y ? p1.f1126y : p2.f1126y);
        this.width = ((int) (p1.f1125x > p2.f1125x ? p1.f1125x : p2.f1125x)) - this.f1130x;
        this.height = ((int) (p1.f1126y > p2.f1126y ? p1.f1126y : p2.f1126y)) - this.f1131y;
    }

    public Rect(Point p, Size s) {
        this((int) p.f1125x, (int) p.f1126y, (int) s.width, (int) s.height);
    }

    public Rect(double[] vals) {
        set(vals);
    }

    public void set(double[] vals) {
        int i;
        int i2;
        int i3 = 0;
        if (vals != null) {
            this.f1130x = vals.length > 0 ? (int) vals[0] : 0;
            if (vals.length > 1) {
                i = (int) vals[1];
            } else {
                i = 0;
            }
            this.f1131y = i;
            if (vals.length > 2) {
                i2 = (int) vals[2];
            } else {
                i2 = 0;
            }
            this.width = i2;
            if (vals.length > 3) {
                i3 = (int) vals[3];
            }
            this.height = i3;
            return;
        }
        this.f1130x = 0;
        this.f1131y = 0;
        this.width = 0;
        this.height = 0;
    }

    public Rect clone() {
        return new Rect(this.f1130x, this.f1131y, this.width, this.height);
    }

    /* renamed from: tl */
    public Point mo13770tl() {
        return new Point((double) this.f1130x, (double) this.f1131y);
    }

    /* renamed from: br */
    public Point mo13763br() {
        return new Point((double) (this.f1130x + this.width), (double) (this.f1131y + this.height));
    }

    public Size size() {
        return new Size((double) this.width, (double) this.height);
    }

    public double area() {
        return (double) (this.width * this.height);
    }

    public boolean contains(Point p) {
        return ((double) this.f1130x) <= p.f1125x && p.f1125x < ((double) (this.f1130x + this.width)) && ((double) this.f1131y) <= p.f1126y && p.f1126y < ((double) (this.f1131y + this.height));
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits((double) this.height);
        int result = ((int) ((temp >>> 32) ^ temp)) + 31;
        long temp2 = Double.doubleToLongBits((double) this.width);
        int result2 = (result * 31) + ((int) ((temp2 >>> 32) ^ temp2));
        long temp3 = Double.doubleToLongBits((double) this.f1130x);
        int result3 = (result2 * 31) + ((int) ((temp3 >>> 32) ^ temp3));
        long temp4 = Double.doubleToLongBits((double) this.f1131y);
        return (result3 * 31) + ((int) ((temp4 >>> 32) ^ temp4));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Rect)) {
            return false;
        }
        Rect it = (Rect) obj;
        if (this.f1130x == it.f1130x && this.f1131y == it.f1131y && this.width == it.width && this.height == it.height) {
            return true;
        }
        return false;
    }

    public String toString() {
        return "{" + this.f1130x + ", " + this.f1131y + ", " + this.width + "x" + this.height + "}";
    }
}
