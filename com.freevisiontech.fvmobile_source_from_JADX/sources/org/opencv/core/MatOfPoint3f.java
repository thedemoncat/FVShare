package org.opencv.core;

import java.util.Arrays;
import java.util.List;

public class MatOfPoint3f extends Mat {
    private static final int _channels = 3;
    private static final int _depth = 5;

    public MatOfPoint3f() {
    }

    protected MatOfPoint3f(long addr) {
        super(addr);
        if (!empty() && checkVector(3, 5) < 0) {
            throw new IllegalArgumentException("Incompatible Mat");
        }
    }

    public static MatOfPoint3f fromNativeAddr(long addr) {
        return new MatOfPoint3f(addr);
    }

    public MatOfPoint3f(Mat m) {
        super(m, Range.all());
        if (!empty() && checkVector(3, 5) < 0) {
            throw new IllegalArgumentException("Incompatible Mat");
        }
    }

    public MatOfPoint3f(Point3... a) {
        fromArray(a);
    }

    public void alloc(int elemNumber) {
        if (elemNumber > 0) {
            super.create(elemNumber, 1, CvType.makeType(5, 3));
        }
    }

    public void fromArray(Point3... a) {
        if (a != null && a.length != 0) {
            int num = a.length;
            alloc(num);
            float[] buff = new float[(num * 3)];
            for (int i = 0; i < num; i++) {
                Point3 p = a[i];
                buff[(i * 3) + 0] = (float) p.f1127x;
                buff[(i * 3) + 1] = (float) p.f1128y;
                buff[(i * 3) + 2] = (float) p.f1129z;
            }
            put(0, 0, buff);
        }
    }

    public Point3[] toArray() {
        int num = (int) total();
        Point3[] ap = new Point3[num];
        if (num != 0) {
            float[] buff = new float[(num * 3)];
            get(0, 0, buff);
            for (int i = 0; i < num; i++) {
                ap[i] = new Point3((double) buff[i * 3], (double) buff[(i * 3) + 1], (double) buff[(i * 3) + 2]);
            }
        }
        return ap;
    }

    public void fromList(List<Point3> lp) {
        fromArray((Point3[]) lp.toArray(new Point3[0]));
    }

    public List<Point3> toList() {
        return Arrays.asList(toArray());
    }
}
