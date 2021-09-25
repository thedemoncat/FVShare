package org.opencv.core;

import java.util.Arrays;
import java.util.List;

public class MatOfDMatch extends Mat {
    private static final int _channels = 4;
    private static final int _depth = 5;

    public MatOfDMatch() {
    }

    protected MatOfDMatch(long addr) {
        super(addr);
        if (!empty() && checkVector(4, 5) < 0) {
            throw new IllegalArgumentException("Incompatible Mat: " + toString());
        }
    }

    public static MatOfDMatch fromNativeAddr(long addr) {
        return new MatOfDMatch(addr);
    }

    public MatOfDMatch(Mat m) {
        super(m, Range.all());
        if (!empty() && checkVector(4, 5) < 0) {
            throw new IllegalArgumentException("Incompatible Mat: " + toString());
        }
    }

    public MatOfDMatch(DMatch... ap) {
        fromArray(ap);
    }

    public void alloc(int elemNumber) {
        if (elemNumber > 0) {
            super.create(elemNumber, 1, CvType.makeType(5, 4));
        }
    }

    public void fromArray(DMatch... a) {
        if (a != null && a.length != 0) {
            int num = a.length;
            alloc(num);
            float[] buff = new float[(num * 4)];
            for (int i = 0; i < num; i++) {
                DMatch m = a[i];
                buff[(i * 4) + 0] = (float) m.queryIdx;
                buff[(i * 4) + 1] = (float) m.trainIdx;
                buff[(i * 4) + 2] = (float) m.imgIdx;
                buff[(i * 4) + 3] = m.distance;
            }
            put(0, 0, buff);
        }
    }

    public DMatch[] toArray() {
        int num = (int) total();
        DMatch[] a = new DMatch[num];
        if (num != 0) {
            float[] buff = new float[(num * 4)];
            get(0, 0, buff);
            for (int i = 0; i < num; i++) {
                a[i] = new DMatch((int) buff[(i * 4) + 0], (int) buff[(i * 4) + 1], (int) buff[(i * 4) + 2], buff[(i * 4) + 3]);
            }
        }
        return a;
    }

    public void fromList(List<DMatch> ldm) {
        fromArray((DMatch[]) ldm.toArray(new DMatch[0]));
    }

    public List<DMatch> toList() {
        return Arrays.asList(toArray());
    }
}
