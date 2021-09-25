package com.googlecode.mp4parser.util;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import java.nio.ByteBuffer;

public class Matrix {
    public static final Matrix ROTATE_0 = new Matrix(1.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d);
    public static final Matrix ROTATE_180 = new Matrix(-1.0d, 0.0d, 0.0d, -1.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d);
    public static final Matrix ROTATE_270 = new Matrix(0.0d, -1.0d, 1.0d, 0.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d);
    public static final Matrix ROTATE_90 = new Matrix(0.0d, 1.0d, -1.0d, 0.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d);

    /* renamed from: a */
    double f1038a;

    /* renamed from: b */
    double f1039b;

    /* renamed from: c */
    double f1040c;

    /* renamed from: d */
    double f1041d;

    /* renamed from: tx */
    double f1042tx;

    /* renamed from: ty */
    double f1043ty;

    /* renamed from: u */
    double f1044u;

    /* renamed from: v */
    double f1045v;

    /* renamed from: w */
    double f1046w;

    public Matrix(double a, double b, double c, double d, double u, double v, double w, double tx, double ty) {
        this.f1044u = u;
        this.f1045v = v;
        this.f1046w = w;
        this.f1038a = a;
        this.f1039b = b;
        this.f1040c = c;
        this.f1041d = d;
        this.f1042tx = tx;
        this.f1043ty = ty;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Matrix matrix = (Matrix) o;
        if (Double.compare(matrix.f1038a, this.f1038a) != 0) {
            return false;
        }
        if (Double.compare(matrix.f1039b, this.f1039b) != 0) {
            return false;
        }
        if (Double.compare(matrix.f1040c, this.f1040c) != 0) {
            return false;
        }
        if (Double.compare(matrix.f1041d, this.f1041d) != 0) {
            return false;
        }
        if (Double.compare(matrix.f1042tx, this.f1042tx) != 0) {
            return false;
        }
        if (Double.compare(matrix.f1043ty, this.f1043ty) != 0) {
            return false;
        }
        if (Double.compare(matrix.f1044u, this.f1044u) != 0) {
            return false;
        }
        if (Double.compare(matrix.f1045v, this.f1045v) != 0) {
            return false;
        }
        if (Double.compare(matrix.f1046w, this.f1046w) != 0) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits(this.f1044u);
        int result = (int) ((temp >>> 32) ^ temp);
        long temp2 = Double.doubleToLongBits(this.f1045v);
        int result2 = (result * 31) + ((int) ((temp2 >>> 32) ^ temp2));
        long temp3 = Double.doubleToLongBits(this.f1046w);
        int result3 = (result2 * 31) + ((int) ((temp3 >>> 32) ^ temp3));
        long temp4 = Double.doubleToLongBits(this.f1038a);
        int result4 = (result3 * 31) + ((int) ((temp4 >>> 32) ^ temp4));
        long temp5 = Double.doubleToLongBits(this.f1039b);
        int result5 = (result4 * 31) + ((int) ((temp5 >>> 32) ^ temp5));
        long temp6 = Double.doubleToLongBits(this.f1040c);
        int result6 = (result5 * 31) + ((int) ((temp6 >>> 32) ^ temp6));
        long temp7 = Double.doubleToLongBits(this.f1041d);
        int result7 = (result6 * 31) + ((int) ((temp7 >>> 32) ^ temp7));
        long temp8 = Double.doubleToLongBits(this.f1042tx);
        int result8 = (result7 * 31) + ((int) ((temp8 >>> 32) ^ temp8));
        long temp9 = Double.doubleToLongBits(this.f1043ty);
        return (result8 * 31) + ((int) ((temp9 >>> 32) ^ temp9));
    }

    public String toString() {
        if (equals(ROTATE_0)) {
            return "Rotate 0°";
        }
        if (equals(ROTATE_90)) {
            return "Rotate 90°";
        }
        if (equals(ROTATE_180)) {
            return "Rotate 180°";
        }
        if (equals(ROTATE_270)) {
            return "Rotate 270°";
        }
        return "Matrix{u=" + this.f1044u + ", v=" + this.f1045v + ", w=" + this.f1046w + ", a=" + this.f1038a + ", b=" + this.f1039b + ", c=" + this.f1040c + ", d=" + this.f1041d + ", tx=" + this.f1042tx + ", ty=" + this.f1043ty + '}';
    }

    public static Matrix fromFileOrder(double a, double b, double u, double c, double d, double v, double tx, double ty, double w) {
        return new Matrix(a, b, c, d, u, v, w, tx, ty);
    }

    public static Matrix fromByteBuffer(ByteBuffer byteBuffer) {
        return fromFileOrder(IsoTypeReader.readFixedPoint1616(byteBuffer), IsoTypeReader.readFixedPoint1616(byteBuffer), IsoTypeReader.readFixedPoint0230(byteBuffer), IsoTypeReader.readFixedPoint1616(byteBuffer), IsoTypeReader.readFixedPoint1616(byteBuffer), IsoTypeReader.readFixedPoint0230(byteBuffer), IsoTypeReader.readFixedPoint1616(byteBuffer), IsoTypeReader.readFixedPoint1616(byteBuffer), IsoTypeReader.readFixedPoint0230(byteBuffer));
    }

    public void getContent(ByteBuffer byteBuffer) {
        IsoTypeWriter.writeFixedPoint1616(byteBuffer, this.f1038a);
        IsoTypeWriter.writeFixedPoint1616(byteBuffer, this.f1039b);
        IsoTypeWriter.writeFixedPoint0230(byteBuffer, this.f1044u);
        IsoTypeWriter.writeFixedPoint1616(byteBuffer, this.f1040c);
        IsoTypeWriter.writeFixedPoint1616(byteBuffer, this.f1041d);
        IsoTypeWriter.writeFixedPoint0230(byteBuffer, this.f1045v);
        IsoTypeWriter.writeFixedPoint1616(byteBuffer, this.f1042tx);
        IsoTypeWriter.writeFixedPoint1616(byteBuffer, this.f1043ty);
        IsoTypeWriter.writeFixedPoint0230(byteBuffer, this.f1046w);
    }
}
