package org.opencv.core;

import java.util.Arrays;
import java.util.List;

public class MatOfDouble extends Mat {
    private static final int _channels = 1;
    private static final int _depth = 6;

    public MatOfDouble() {
    }

    protected MatOfDouble(long addr) {
        super(addr);
        if (!empty() && checkVector(1, 6) < 0) {
            throw new IllegalArgumentException("Incompatible Mat");
        }
    }

    public static MatOfDouble fromNativeAddr(long addr) {
        return new MatOfDouble(addr);
    }

    public MatOfDouble(Mat m) {
        super(m, Range.all());
        if (!empty() && checkVector(1, 6) < 0) {
            throw new IllegalArgumentException("Incompatible Mat");
        }
    }

    public MatOfDouble(double... a) {
        fromArray(a);
    }

    public void alloc(int elemNumber) {
        if (elemNumber > 0) {
            super.create(elemNumber, 1, CvType.makeType(6, 1));
        }
    }

    public void fromArray(double... a) {
        if (a != null && a.length != 0) {
            alloc(a.length / 1);
            put(0, 0, a);
        }
    }

    public double[] toArray() {
        int num = checkVector(1, 6);
        if (num < 0) {
            throw new RuntimeException("Native Mat has unexpected type or size: " + toString());
        }
        double[] a = new double[(num * 1)];
        if (num != 0) {
            get(0, 0, a);
        }
        return a;
    }

    public void fromList(List<Double> lb) {
        if (lb != null && lb.size() != 0) {
            Double[] ab = (Double[]) lb.toArray(new Double[0]);
            double[] a = new double[ab.length];
            for (int i = 0; i < ab.length; i++) {
                a[i] = ab[i].doubleValue();
            }
            fromArray(a);
        }
    }

    public List<Double> toList() {
        double[] a = toArray();
        Double[] ab = new Double[a.length];
        for (int i = 0; i < a.length; i++) {
            ab[i] = Double.valueOf(a[i]);
        }
        return Arrays.asList(ab);
    }
}
