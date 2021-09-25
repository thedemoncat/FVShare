package com.github.barteksc.pdfviewer.util;

public class MathUtils {
    private static final double BIG_ENOUGH_CEIL = 16384.999999999996d;
    private static final double BIG_ENOUGH_FLOOR = 16384.0d;
    private static final int BIG_ENOUGH_INT = 16384;

    private MathUtils() {
    }

    public static int limit(int number, int between, int and) {
        if (number <= between) {
            return between;
        }
        if (number >= and) {
            return and;
        }
        return number;
    }

    public static float limit(float number, float between, float and) {
        if (number <= between) {
            return between;
        }
        if (number >= and) {
            return and;
        }
        return number;
    }

    public static float max(float number, float max) {
        return number > max ? max : number;
    }

    public static float min(float number, float min) {
        return number < min ? min : number;
    }

    public static int max(int number, int max) {
        return number > max ? max : number;
    }

    public static int min(int number, int min) {
        return number < min ? min : number;
    }

    public static int floor(float value) {
        return ((int) (((double) value) + BIG_ENOUGH_FLOOR)) - 16384;
    }

    public static int ceil(float value) {
        return ((int) (((double) value) + BIG_ENOUGH_CEIL)) - 16384;
    }
}
