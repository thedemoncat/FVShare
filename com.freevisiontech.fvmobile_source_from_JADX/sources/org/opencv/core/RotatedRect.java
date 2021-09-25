package org.opencv.core;

public class RotatedRect {
    public double angle;
    public Point center;
    public Size size;

    public RotatedRect() {
        this.center = new Point();
        this.size = new Size();
        this.angle = 0.0d;
    }

    public RotatedRect(Point c, Size s, double a) {
        this.center = c.clone();
        this.size = s.clone();
        this.angle = a;
    }

    public RotatedRect(double[] vals) {
        this();
        set(vals);
    }

    public void set(double[] vals) {
        double d;
        double d2;
        double d3;
        double d4 = 0.0d;
        if (vals != null) {
            this.center.f1125x = vals.length > 0 ? vals[0] : 0.0d;
            Point point = this.center;
            if (vals.length > 1) {
                d = vals[1];
            } else {
                d = 0.0d;
            }
            point.f1126y = d;
            Size size2 = this.size;
            if (vals.length > 2) {
                d2 = vals[2];
            } else {
                d2 = 0.0d;
            }
            size2.width = d2;
            Size size3 = this.size;
            if (vals.length > 3) {
                d3 = vals[3];
            } else {
                d3 = 0.0d;
            }
            size3.height = d3;
            if (vals.length > 4) {
                d4 = vals[4];
            }
            this.angle = d4;
            return;
        }
        this.center.f1125x = 0.0d;
        this.center.f1125x = 0.0d;
        this.size.width = 0.0d;
        this.size.height = 0.0d;
        this.angle = 0.0d;
    }

    public void points(Point[] pt) {
        double _angle = (this.angle * 3.141592653589793d) / 180.0d;
        double b = Math.cos(_angle) * 0.5d;
        double a = Math.sin(_angle) * 0.5d;
        pt[0] = new Point((this.center.f1125x - (this.size.height * a)) - (this.size.width * b), (this.center.f1126y + (this.size.height * b)) - (this.size.width * a));
        pt[1] = new Point((this.center.f1125x + (this.size.height * a)) - (this.size.width * b), (this.center.f1126y - (this.size.height * b)) - (this.size.width * a));
        pt[2] = new Point((2.0d * this.center.f1125x) - pt[0].f1125x, (2.0d * this.center.f1126y) - pt[0].f1126y);
        pt[3] = new Point((2.0d * this.center.f1125x) - pt[1].f1125x, (2.0d * this.center.f1126y) - pt[1].f1126y);
    }

    public Rect boundingRect() {
        Point[] pt = new Point[4];
        points(pt);
        Rect r = new Rect((int) Math.floor(Math.min(Math.min(Math.min(pt[0].f1125x, pt[1].f1125x), pt[2].f1125x), pt[3].f1125x)), (int) Math.floor(Math.min(Math.min(Math.min(pt[0].f1126y, pt[1].f1126y), pt[2].f1126y), pt[3].f1126y)), (int) Math.ceil(Math.max(Math.max(Math.max(pt[0].f1125x, pt[1].f1125x), pt[2].f1125x), pt[3].f1125x)), (int) Math.ceil(Math.max(Math.max(Math.max(pt[0].f1126y, pt[1].f1126y), pt[2].f1126y), pt[3].f1126y)));
        r.width -= r.f1130x - 1;
        r.height -= r.f1131y - 1;
        return r;
    }

    public RotatedRect clone() {
        return new RotatedRect(this.center, this.size, this.angle);
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits(this.center.f1125x);
        int result = ((int) ((temp >>> 32) ^ temp)) + 31;
        long temp2 = Double.doubleToLongBits(this.center.f1126y);
        int result2 = (result * 31) + ((int) ((temp2 >>> 32) ^ temp2));
        long temp3 = Double.doubleToLongBits(this.size.width);
        int result3 = (result2 * 31) + ((int) ((temp3 >>> 32) ^ temp3));
        long temp4 = Double.doubleToLongBits(this.size.height);
        int result4 = (result3 * 31) + ((int) ((temp4 >>> 32) ^ temp4));
        long temp5 = Double.doubleToLongBits(this.angle);
        return (result4 * 31) + ((int) ((temp5 >>> 32) ^ temp5));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RotatedRect)) {
            return false;
        }
        RotatedRect it = (RotatedRect) obj;
        if (!this.center.equals(it.center) || !this.size.equals(it.size) || this.angle != it.angle) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "{ " + this.center + " " + this.size + " * " + this.angle + " }";
    }
}
