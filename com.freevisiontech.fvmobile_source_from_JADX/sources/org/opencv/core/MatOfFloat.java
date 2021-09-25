package org.opencv.core;

import java.util.Arrays;
import java.util.List;

public class MatOfFloat extends Mat {
    private static final int _channels = 1;
    private static final int _depth = 5;

    public MatOfFloat() {
    }

    protected MatOfFloat(long addr) {
        super(addr);
        if (!empty() && checkVector(1, 5) < 0) {
            throw new IllegalArgumentException("Incompatible Mat");
        }
    }

    public static MatOfFloat fromNativeAddr(long addr) {
        return new MatOfFloat(addr);
    }

    public MatOfFloat(Mat m) {
        super(m, Range.all());
        if (!empty() && checkVector(1, 5) < 0) {
            throw new IllegalArgumentException("Incompatible Mat");
        }
    }

    public MatOfFloat(float... a) {
        fromArray(a);
    }

    public void alloc(int elemNumber) {
        if (elemNumber > 0) {
            super.create(elemNumber, 1, CvType.makeType(5, 1));
        }
    }

    public void fromArray(float... a) {
        if (a != null && a.length != 0) {
            alloc(a.length / 1);
            put(0, 0, a);
        }
    }

    public float[] toArray() {
        int num = checkVector(1, 5);
        if (num < 0) {
            throw new RuntimeException("Native Mat has unexpected type or size: " + toString());
        }
        float[] a = new float[(num * 1)];
        if (num != 0) {
            get(0, 0, a);
        }
        return a;
    }

    public void fromList(List<Float> lb) {
        if (lb != null && lb.size() != 0) {
            Float[] ab = (Float[]) lb.toArray(new Float[0]);
            float[] a = new float[ab.length];
            for (int i = 0; i < ab.length; i++) {
                a[i] = ab[i].floatValue();
            }
            fromArray(a);
        }
    }

    public List<Float> toList() {
        float[] a = toArray();
        Float[] ab = new Float[a.length];
        for (int i = 0; i < a.length; i++) {
            ab[i] = Float.valueOf(a[i]);
        }
        return Arrays.asList(ab);
    }
}
