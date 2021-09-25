package org.opencv.core;

public class TermCriteria {
    public static final int COUNT = 1;
    public static final int EPS = 2;
    public static final int MAX_ITER = 1;
    public double epsilon;
    public int maxCount;
    public int type;

    public TermCriteria(int type2, int maxCount2, double epsilon2) {
        this.type = type2;
        this.maxCount = maxCount2;
        this.epsilon = epsilon2;
    }

    public TermCriteria() {
        this(0, 0, 0.0d);
    }

    public TermCriteria(double[] vals) {
        set(vals);
    }

    public void set(double[] vals) {
        double d;
        int i = 0;
        if (vals != null) {
            this.type = vals.length > 0 ? (int) vals[0] : 0;
            if (vals.length > 1) {
                i = (int) vals[1];
            }
            this.maxCount = i;
            if (vals.length > 2) {
                d = vals[2];
            } else {
                d = 0.0d;
            }
            this.epsilon = d;
            return;
        }
        this.type = 0;
        this.maxCount = 0;
        this.epsilon = 0.0d;
    }

    public TermCriteria clone() {
        return new TermCriteria(this.type, this.maxCount, this.epsilon);
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits((double) this.type);
        int result = ((int) ((temp >>> 32) ^ temp)) + 31;
        long temp2 = Double.doubleToLongBits((double) this.maxCount);
        int result2 = (result * 31) + ((int) ((temp2 >>> 32) ^ temp2));
        long temp3 = Double.doubleToLongBits(this.epsilon);
        return (result2 * 31) + ((int) ((temp3 >>> 32) ^ temp3));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TermCriteria)) {
            return false;
        }
        TermCriteria it = (TermCriteria) obj;
        if (this.type == it.type && this.maxCount == it.maxCount && this.epsilon == it.epsilon) {
            return true;
        }
        return false;
    }

    public String toString() {
        return "{ type: " + this.type + ", maxCount: " + this.maxCount + ", epsilon: " + this.epsilon + "}";
    }
}
