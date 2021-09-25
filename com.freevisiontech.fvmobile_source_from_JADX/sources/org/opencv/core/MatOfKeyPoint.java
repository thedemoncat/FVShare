package org.opencv.core;

import java.util.Arrays;
import java.util.List;

public class MatOfKeyPoint extends Mat {
    private static final int _channels = 7;
    private static final int _depth = 5;

    public MatOfKeyPoint() {
    }

    protected MatOfKeyPoint(long addr) {
        super(addr);
        if (!empty() && checkVector(7, 5) < 0) {
            throw new IllegalArgumentException("Incompatible Mat");
        }
    }

    public static MatOfKeyPoint fromNativeAddr(long addr) {
        return new MatOfKeyPoint(addr);
    }

    public MatOfKeyPoint(Mat m) {
        super(m, Range.all());
        if (!empty() && checkVector(7, 5) < 0) {
            throw new IllegalArgumentException("Incompatible Mat");
        }
    }

    public MatOfKeyPoint(KeyPoint... a) {
        fromArray(a);
    }

    public void alloc(int elemNumber) {
        if (elemNumber > 0) {
            super.create(elemNumber, 1, CvType.makeType(5, 7));
        }
    }

    public void fromArray(KeyPoint... a) {
        if (a != null && a.length != 0) {
            int num = a.length;
            alloc(num);
            float[] buff = new float[(num * 7)];
            for (int i = 0; i < num; i++) {
                KeyPoint kp = a[i];
                buff[(i * 7) + 0] = (float) kp.f1124pt.f1125x;
                buff[(i * 7) + 1] = (float) kp.f1124pt.f1126y;
                buff[(i * 7) + 2] = kp.size;
                buff[(i * 7) + 3] = kp.angle;
                buff[(i * 7) + 4] = kp.response;
                buff[(i * 7) + 5] = (float) kp.octave;
                buff[(i * 7) + 6] = (float) kp.class_id;
            }
            put(0, 0, buff);
        }
    }

    public KeyPoint[] toArray() {
        int num = (int) total();
        KeyPoint[] a = new KeyPoint[num];
        if (num != 0) {
            float[] buff = new float[(num * 7)];
            get(0, 0, buff);
            for (int i = 0; i < num; i++) {
                a[i] = new KeyPoint(buff[(i * 7) + 0], buff[(i * 7) + 1], buff[(i * 7) + 2], buff[(i * 7) + 3], buff[(i * 7) + 4], (int) buff[(i * 7) + 5], (int) buff[(i * 7) + 6]);
            }
        }
        return a;
    }

    public void fromList(List<KeyPoint> lkp) {
        fromArray((KeyPoint[]) lkp.toArray(new KeyPoint[0]));
    }

    public List<KeyPoint> toList() {
        return Arrays.asList(toArray());
    }
}
