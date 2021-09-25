package org.opencv.core;

public class Point {

    /* renamed from: x */
    public double f1125x;

    /* renamed from: y */
    public double f1126y;

    public Point(double x, double y) {
        this.f1125x = x;
        this.f1126y = y;
    }

    public Point() {
        this(0.0d, 0.0d);
    }

    public Point(double[] vals) {
        this();
        set(vals);
    }

    public void set(double[] vals) {
        double d = 0.0d;
        if (vals != null) {
            this.f1125x = vals.length > 0 ? vals[0] : 0.0d;
            if (vals.length > 1) {
                d = vals[1];
            }
            this.f1126y = d;
            return;
        }
        this.f1125x = 0.0d;
        this.f1126y = 0.0d;
    }

    public Point clone() {
        return new Point(this.f1125x, this.f1126y);
    }

    public double dot(Point p) {
        return (this.f1125x * p.f1125x) + (this.f1126y * p.f1126y);
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits(this.f1125x);
        int result = ((int) ((temp >>> 32) ^ temp)) + 31;
        long temp2 = Double.doubleToLongBits(this.f1126y);
        return (result * 31) + ((int) ((temp2 >>> 32) ^ temp2));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Point)) {
            return false;
        }
        Point it = (Point) obj;
        if (this.f1125x == it.f1125x && this.f1126y == it.f1126y) {
            return true;
        }
        return false;
    }

    public boolean inside(Rect r) {
        return r.contains(this);
    }

    public String toString() {
        return "{" + this.f1125x + ", " + this.f1126y + "}";
    }
}
