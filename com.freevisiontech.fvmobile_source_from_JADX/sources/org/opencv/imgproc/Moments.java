package org.opencv.imgproc;

public class Moments {
    public double m00;
    public double m01;
    public double m02;
    public double m03;
    public double m10;
    public double m11;
    public double m12;
    public double m20;
    public double m21;
    public double m30;
    public double mu02;
    public double mu03;
    public double mu11;
    public double mu12;
    public double mu20;
    public double mu21;
    public double mu30;
    public double nu02;
    public double nu03;
    public double nu11;
    public double nu12;
    public double nu20;
    public double nu21;
    public double nu30;

    public Moments(double m002, double m102, double m012, double m202, double m112, double m022, double m302, double m212, double m122, double m032) {
        this.m00 = m002;
        this.m10 = m102;
        this.m01 = m012;
        this.m20 = m202;
        this.m11 = m112;
        this.m02 = m022;
        this.m30 = m302;
        this.m21 = m212;
        this.m12 = m122;
        this.m03 = m032;
        completeState();
    }

    public Moments() {
        this(0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d);
    }

    public Moments(double[] vals) {
        set(vals);
    }

    public void set(double[] vals) {
        double d;
        double d2;
        double d3;
        double d4;
        double d5;
        double d6;
        double d7;
        double d8;
        double d9 = 0.0d;
        if (vals != null) {
            this.m00 = vals.length > 0 ? vals[0] : 0.0d;
            if (vals.length > 1) {
                d = vals[1];
            } else {
                d = 0.0d;
            }
            this.m10 = d;
            if (vals.length > 2) {
                d2 = vals[2];
            } else {
                d2 = 0.0d;
            }
            this.m01 = d2;
            if (vals.length > 3) {
                d3 = vals[3];
            } else {
                d3 = 0.0d;
            }
            this.m20 = d3;
            if (vals.length > 4) {
                d4 = vals[4];
            } else {
                d4 = 0.0d;
            }
            this.m11 = d4;
            if (vals.length > 5) {
                d5 = vals[5];
            } else {
                d5 = 0.0d;
            }
            this.m02 = d5;
            if (vals.length > 6) {
                d6 = vals[6];
            } else {
                d6 = 0.0d;
            }
            this.m30 = d6;
            if (vals.length > 7) {
                d7 = vals[7];
            } else {
                d7 = 0.0d;
            }
            this.m21 = d7;
            if (vals.length > 8) {
                d8 = vals[8];
            } else {
                d8 = 0.0d;
            }
            this.m12 = d8;
            if (vals.length > 9) {
                d9 = vals[9];
            }
            this.m03 = d9;
            completeState();
            return;
        }
        this.m00 = 0.0d;
        this.m10 = 0.0d;
        this.m01 = 0.0d;
        this.m20 = 0.0d;
        this.m11 = 0.0d;
        this.m02 = 0.0d;
        this.m30 = 0.0d;
        this.m21 = 0.0d;
        this.m12 = 0.0d;
        this.m03 = 0.0d;
        this.mu20 = 0.0d;
        this.mu11 = 0.0d;
        this.mu02 = 0.0d;
        this.mu30 = 0.0d;
        this.mu21 = 0.0d;
        this.mu12 = 0.0d;
        this.mu03 = 0.0d;
        this.nu20 = 0.0d;
        this.nu11 = 0.0d;
        this.nu02 = 0.0d;
        this.nu30 = 0.0d;
        this.nu21 = 0.0d;
        this.nu12 = 0.0d;
        this.nu03 = 0.0d;
    }

    public String toString() {
        return "Moments [ \nm00=" + this.m00 + ", \nm10=" + this.m10 + ", m01=" + this.m01 + ", \nm20=" + this.m20 + ", m11=" + this.m11 + ", m02=" + this.m02 + ", \nm30=" + this.m30 + ", m21=" + this.m21 + ", m12=" + this.m12 + ", m03=" + this.m03 + ", \nmu20=" + this.mu20 + ", mu11=" + this.mu11 + ", mu02=" + this.mu02 + ", \nmu30=" + this.mu30 + ", mu21=" + this.mu21 + ", mu12=" + this.mu12 + ", mu03=" + this.mu03 + ", \nnu20=" + this.nu20 + ", nu11=" + this.nu11 + ", nu02=" + this.nu02 + ", \nnu30=" + this.nu30 + ", nu21=" + this.nu21 + ", nu12=" + this.nu12 + ", nu03=" + this.nu03 + ", \n]";
    }

    /* access modifiers changed from: protected */
    public void completeState() {
        double cx = 0.0d;
        double cy = 0.0d;
        double inv_m00 = 0.0d;
        if (Math.abs(this.m00) > 1.0E-8d) {
            inv_m00 = 1.0d / this.m00;
            cx = this.m10 * inv_m00;
            cy = this.m01 * inv_m00;
        }
        double mu202 = this.m20 - (this.m10 * cx);
        double mu112 = this.m11 - (this.m10 * cy);
        double mu022 = this.m02 - (this.m01 * cy);
        this.mu20 = mu202;
        this.mu11 = mu112;
        this.mu02 = mu022;
        this.mu30 = this.m30 - (((3.0d * mu202) + (this.m10 * cx)) * cx);
        double mu113 = mu112 + mu112;
        this.mu21 = (this.m21 - (((this.m01 * cx) + mu113) * cx)) - (cy * mu202);
        this.mu12 = (this.m12 - (((this.m10 * cy) + mu113) * cy)) - (cx * mu022);
        this.mu03 = this.m03 - (((3.0d * mu022) + (this.m01 * cy)) * cy);
        double s2 = inv_m00 * inv_m00;
        double s3 = s2 * Math.sqrt(Math.abs(inv_m00));
        this.nu20 = this.mu20 * s2;
        this.nu11 = this.mu11 * s2;
        this.nu02 = this.mu02 * s2;
        this.nu30 = this.mu30 * s3;
        this.nu21 = this.mu21 * s3;
        this.nu12 = this.mu12 * s3;
        this.nu03 = this.mu03 * s3;
    }

    public double get_m00() {
        return this.m00;
    }

    public double get_m10() {
        return this.m10;
    }

    public double get_m01() {
        return this.m01;
    }

    public double get_m20() {
        return this.m20;
    }

    public double get_m11() {
        return this.m11;
    }

    public double get_m02() {
        return this.m02;
    }

    public double get_m30() {
        return this.m30;
    }

    public double get_m21() {
        return this.m21;
    }

    public double get_m12() {
        return this.m12;
    }

    public double get_m03() {
        return this.m03;
    }

    public double get_mu20() {
        return this.mu20;
    }

    public double get_mu11() {
        return this.mu11;
    }

    public double get_mu02() {
        return this.mu02;
    }

    public double get_mu30() {
        return this.mu30;
    }

    public double get_mu21() {
        return this.mu21;
    }

    public double get_mu12() {
        return this.mu12;
    }

    public double get_mu03() {
        return this.mu03;
    }

    public double get_nu20() {
        return this.nu20;
    }

    public double get_nu11() {
        return this.nu11;
    }

    public double get_nu02() {
        return this.nu02;
    }

    public double get_nu30() {
        return this.nu30;
    }

    public double get_nu21() {
        return this.nu21;
    }

    public double get_nu12() {
        return this.nu12;
    }

    public double get_nu03() {
        return this.nu03;
    }

    public void set_m00(double m002) {
        this.m00 = m002;
    }

    public void set_m10(double m102) {
        this.m10 = m102;
    }

    public void set_m01(double m012) {
        this.m01 = m012;
    }

    public void set_m20(double m202) {
        this.m20 = m202;
    }

    public void set_m11(double m112) {
        this.m11 = m112;
    }

    public void set_m02(double m022) {
        this.m02 = m022;
    }

    public void set_m30(double m302) {
        this.m30 = m302;
    }

    public void set_m21(double m212) {
        this.m21 = m212;
    }

    public void set_m12(double m122) {
        this.m12 = m122;
    }

    public void set_m03(double m032) {
        this.m03 = m032;
    }

    public void set_mu20(double mu202) {
        this.mu20 = mu202;
    }

    public void set_mu11(double mu112) {
        this.mu11 = mu112;
    }

    public void set_mu02(double mu022) {
        this.mu02 = mu022;
    }

    public void set_mu30(double mu302) {
        this.mu30 = mu302;
    }

    public void set_mu21(double mu212) {
        this.mu21 = mu212;
    }

    public void set_mu12(double mu122) {
        this.mu12 = mu122;
    }

    public void set_mu03(double mu032) {
        this.mu03 = mu032;
    }

    public void set_nu20(double nu202) {
        this.nu20 = nu202;
    }

    public void set_nu11(double nu112) {
        this.nu11 = nu112;
    }

    public void set_nu02(double nu022) {
        this.nu02 = nu022;
    }

    public void set_nu30(double nu302) {
        this.nu30 = nu302;
    }

    public void set_nu21(double nu212) {
        this.nu21 = nu212;
    }

    public void set_nu12(double nu122) {
        this.nu12 = nu122;
    }

    public void set_nu03(double nu032) {
        this.nu03 = nu032;
    }
}
