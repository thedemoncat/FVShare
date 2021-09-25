package org.opencv.core;

import java.util.Arrays;
import java.util.List;

public class MatOfRect extends Mat {
    private static final int _channels = 4;
    private static final int _depth = 4;

    public MatOfRect() {
    }

    protected MatOfRect(long addr) {
        super(addr);
        if (!empty() && checkVector(4, 4) < 0) {
            throw new IllegalArgumentException("Incompatible Mat");
        }
    }

    public static MatOfRect fromNativeAddr(long addr) {
        return new MatOfRect(addr);
    }

    public MatOfRect(Mat m) {
        super(m, Range.all());
        if (!empty() && checkVector(4, 4) < 0) {
            throw new IllegalArgumentException("Incompatible Mat");
        }
    }

    public MatOfRect(Rect... a) {
        fromArray(a);
    }

    public void alloc(int elemNumber) {
        if (elemNumber > 0) {
            super.create(elemNumber, 1, CvType.makeType(4, 4));
        }
    }

    public void fromArray(Rect... a) {
        if (a != null && a.length != 0) {
            int num = a.length;
            alloc(num);
            int[] buff = new int[(num * 4)];
            for (int i = 0; i < num; i++) {
                Rect r = a[i];
                buff[(i * 4) + 0] = r.f1130x;
                buff[(i * 4) + 1] = r.f1131y;
                buff[(i * 4) + 2] = r.width;
                buff[(i * 4) + 3] = r.height;
            }
            put(0, 0, buff);
        }
    }

    public Rect[] toArray() {
        int num = (int) total();
        Rect[] a = new Rect[num];
        if (num != 0) {
            int[] buff = new int[(num * 4)];
            get(0, 0, buff);
            for (int i = 0; i < num; i++) {
                a[i] = new Rect(buff[i * 4], buff[(i * 4) + 1], buff[(i * 4) + 2], buff[(i * 4) + 3]);
            }
        }
        return a;
    }

    public void fromList(List<Rect> lr) {
        fromArray((Rect[]) lr.toArray(new Rect[0]));
    }

    public List<Rect> toList() {
        return Arrays.asList(toArray());
    }
}
