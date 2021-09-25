package org.opencv.core;

import java.util.Arrays;
import java.util.List;

public class MatOfFloat6 extends Mat {
    private static final int _channels = 6;
    private static final int _depth = 5;

    public MatOfFloat6() {
    }

    protected MatOfFloat6(long addr) {
        super(addr);
        if (!empty() && checkVector(6, 5) < 0) {
            throw new IllegalArgumentException("Incompatible Mat");
        }
    }

    public static MatOfFloat6 fromNativeAddr(long addr) {
        return new MatOfFloat6(addr);
    }

    public MatOfFloat6(Mat m) {
        super(m, Range.all());
        if (!empty() && checkVector(6, 5) < 0) {
            throw new IllegalArgumentException("Incompatible Mat");
        }
    }

    public MatOfFloat6(float... a) {
        fromArray(a);
    }

    public void alloc(int elemNumber) {
        if (elemNumber > 0) {
            super.create(elemNumber, 1, CvType.makeType(5, 6));
        }
    }

    public void fromArray(float... a) {
        if (a != null && a.length != 0) {
            alloc(a.length / 6);
            put(0, 0, a);
        }
    }

    public float[] toArray() {
        int num = checkVector(6, 5);
        if (num < 0) {
            throw new RuntimeException("Native Mat has unexpected type or size: " + toString());
        }
        float[] a = new float[(num * 6)];
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
