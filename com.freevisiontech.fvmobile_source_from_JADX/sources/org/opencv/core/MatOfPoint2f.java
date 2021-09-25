package org.opencv.core;

import java.util.Arrays;
import java.util.List;

public class MatOfPoint2f extends Mat {
    private static final int _channels = 2;
    private static final int _depth = 5;

    public MatOfPoint2f() {
    }

    protected MatOfPoint2f(long addr) {
        super(addr);
        if (!empty() && checkVector(2, 5) < 0) {
            throw new IllegalArgumentException("Incompatible Mat");
        }
    }

    public static MatOfPoint2f fromNativeAddr(long addr) {
        return new MatOfPoint2f(addr);
    }

    public MatOfPoint2f(Mat m) {
        super(m, Range.all());
        if (!empty() && checkVector(2, 5) < 0) {
            throw new IllegalArgumentException("Incompatible Mat");
        }
    }

    public MatOfPoint2f(Point... a) {
        fromArray(a);
    }

    public void alloc(int elemNumber) {
        if (elemNumber > 0) {
            super.create(elemNumber, 1, CvType.makeType(5, 2));
        }
    }

    public void fromArray(Point... a) {
        if (a != null && a.length != 0) {
            int num = a.length;
            alloc(num);
            float[] buff = new float[(num * 2)];
            for (int i = 0; i < num; i++) {
                Point p = a[i];
                buff[(i * 2) + 0] = (float) p.f1125x;
                buff[(i * 2) + 1] = (float) p.f1126y;
            }
            put(0, 0, buff);
        }
    }

    public Point[] toArray() {
        int num = (int) total();
        Point[] ap = new Point[num];
        if (num != 0) {
            float[] buff = new float[(num * 2)];
            get(0, 0, buff);
            for (int i = 0; i < num; i++) {
                ap[i] = new Point((double) buff[i * 2], (double) buff[(i * 2) + 1]);
            }
        }
        return ap;
    }

    public void fromList(List<Point> lp) {
        fromArray((Point[]) lp.toArray(new Point[0]));
    }

    public List<Point> toList() {
        return Arrays.asList(toArray());
    }
}
