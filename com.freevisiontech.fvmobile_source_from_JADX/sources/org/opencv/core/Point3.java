package org.opencv.core;

public class Point3 {

    /* renamed from: x */
    public double f1127x;

    /* renamed from: y */
    public double f1128y;

    /* renamed from: z */
    public double f1129z;

    public Point3(double x, double y, double z) {
        this.f1127x = x;
        this.f1128y = y;
        this.f1129z = z;
    }

    public Point3() {
        this(0.0d, 0.0d, 0.0d);
    }

    public Point3(Point p) {
        this.f1127x = p.f1125x;
        this.f1128y = p.f1126y;
        this.f1129z = 0.0d;
    }

    public Point3(double[] vals) {
        this();
        set(vals);
    }

    public void set(double[] vals) {
        double d;
        double d2 = 0.0d;
        if (vals != null) {
            this.f1127x = vals.length > 0 ? vals[0] : 0.0d;
            if (vals.length > 1) {
                d = vals[1];
            } else {
                d = 0.0d;
            }
            this.f1128y = d;
            if (vals.length > 2) {
                d2 = vals[2];
            }
            this.f1129z = d2;
            return;
        }
        this.f1127x = 0.0d;
        this.f1128y = 0.0d;
        this.f1129z = 0.0d;
    }

    public Point3 clone() {
        return new Point3(this.f1127x, this.f1128y, this.f1129z);
    }

    public double dot(Point3 p) {
        return (this.f1127x * p.f1127x) + (this.f1128y * p.f1128y) + (this.f1129z * p.f1129z);
    }

    public Point3 cross(Point3 p) {
        return new Point3((this.f1128y * p.f1129z) - (this.f1129z * p.f1128y), (this.f1129z * p.f1127x) - (this.f1127x * p.f1129z), (this.f1127x * p.f1128y) - (this.f1128y * p.f1127x));
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits(this.f1127x);
        int result = ((int) ((temp >>> 32) ^ temp)) + 31;
        long temp2 = Double.doubleToLongBits(this.f1128y);
        int result2 = (result * 31) + ((int) ((temp2 >>> 32) ^ temp2));
        long temp3 = Double.doubleToLongBits(this.f1129z);
        return (result2 * 31) + ((int) ((temp3 >>> 32) ^ temp3));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Point3)) {
            return false;
        }
        Point3 it = (Point3) obj;
        if (this.f1127x == it.f1127x && this.f1128y == it.f1128y && this.f1129z == it.f1129z) {
            return true;
        }
        return false;
    }

    public String toString() {
        return "{" + this.f1127x + ", " + this.f1128y + ", " + this.f1129z + "}";
    }
}
