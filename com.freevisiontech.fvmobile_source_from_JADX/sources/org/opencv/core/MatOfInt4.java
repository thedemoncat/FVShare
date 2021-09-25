package org.opencv.core;

import java.util.Arrays;
import java.util.List;

public class MatOfInt4 extends Mat {
    private static final int _channels = 4;
    private static final int _depth = 4;

    public MatOfInt4() {
    }

    protected MatOfInt4(long addr) {
        super(addr);
        if (!empty() && checkVector(4, 4) < 0) {
            throw new IllegalArgumentException("Incompatible Mat");
        }
    }

    public static MatOfInt4 fromNativeAddr(long addr) {
        return new MatOfInt4(addr);
    }

    public MatOfInt4(Mat m) {
        super(m, Range.all());
        if (!empty() && checkVector(4, 4) < 0) {
            throw new IllegalArgumentException("Incompatible Mat");
        }
    }

    public MatOfInt4(int... a) {
        fromArray(a);
    }

    public void alloc(int elemNumber) {
        if (elemNumber > 0) {
            super.create(elemNumber, 1, CvType.makeType(4, 4));
        }
    }

    public void fromArray(int... a) {
        if (a != null && a.length != 0) {
            alloc(a.length / 4);
            put(0, 0, a);
        }
    }

    public int[] toArray() {
        int num = checkVector(4, 4);
        if (num < 0) {
            throw new RuntimeException("Native Mat has unexpected type or size: " + toString());
        }
        int[] a = new int[(num * 4)];
        if (num != 0) {
            get(0, 0, a);
        }
        return a;
    }

    public void fromList(List<Integer> lb) {
        if (lb != null && lb.size() != 0) {
            Integer[] ab = (Integer[]) lb.toArray(new Integer[0]);
            int[] a = new int[ab.length];
            for (int i = 0; i < ab.length; i++) {
                a[i] = ab[i].intValue();
            }
            fromArray(a);
        }
    }

    public List<Integer> toList() {
        int[] a = toArray();
        Integer[] ab = new Integer[a.length];
        for (int i = 0; i < a.length; i++) {
            ab[i] = Integer.valueOf(a[i]);
        }
        return Arrays.asList(ab);
    }
}
