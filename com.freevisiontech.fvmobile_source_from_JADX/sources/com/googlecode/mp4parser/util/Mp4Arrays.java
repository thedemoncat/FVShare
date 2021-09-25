package com.googlecode.mp4parser.util;

public final class Mp4Arrays {
    private Mp4Arrays() {
    }

    public static long[] copyOfAndAppend(long[] original, long... toAppend) {
        if (original == null) {
            original = new long[0];
        }
        if (toAppend == null) {
            toAppend = new long[0];
        }
        long[] copy = new long[(original.length + toAppend.length)];
        System.arraycopy(original, 0, copy, 0, original.length);
        System.arraycopy(toAppend, 0, copy, original.length, toAppend.length);
        return copy;
    }

    public static int[] copyOfAndAppend(int[] original, int... toAppend) {
        if (original == null) {
            original = new int[0];
        }
        if (toAppend == null) {
            toAppend = new int[0];
        }
        int[] copy = new int[(original.length + toAppend.length)];
        System.arraycopy(original, 0, copy, 0, original.length);
        System.arraycopy(toAppend, 0, copy, original.length, toAppend.length);
        return copy;
    }

    public static double[] copyOfAndAppend(double[] original, double... toAppend) {
        if (original == null) {
            original = new double[0];
        }
        if (toAppend == null) {
            toAppend = new double[0];
        }
        double[] copy = new double[(original.length + toAppend.length)];
        System.arraycopy(original, 0, copy, 0, original.length);
        System.arraycopy(toAppend, 0, copy, original.length, toAppend.length);
        return copy;
    }
}
