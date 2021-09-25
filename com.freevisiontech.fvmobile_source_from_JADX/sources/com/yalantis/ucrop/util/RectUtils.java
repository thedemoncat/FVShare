package com.yalantis.ucrop.util;

import android.graphics.RectF;

public class RectUtils {
    public static float[] getCornersFromRect(RectF r) {
        return new float[]{r.left, r.top, r.right, r.top, r.right, r.bottom, r.left, r.bottom};
    }

    public static float[] getRectSidesFromCorners(float[] corners) {
        return new float[]{(float) Math.sqrt(Math.pow((double) (corners[0] - corners[2]), 2.0d) + Math.pow((double) (corners[1] - corners[3]), 2.0d)), (float) Math.sqrt(Math.pow((double) (corners[2] - corners[4]), 2.0d) + Math.pow((double) (corners[3] - corners[5]), 2.0d))};
    }

    public static float[] getCenterFromRect(RectF r) {
        return new float[]{r.centerX(), r.centerY()};
    }

    public static RectF trapToRect(float[] array) {
        RectF r = new RectF(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
        for (int i = 1; i < array.length; i += 2) {
            float x = ((float) Math.round(array[i - 1] * 10.0f)) / 10.0f;
            float y = ((float) Math.round(array[i] * 10.0f)) / 10.0f;
            r.left = x < r.left ? x : r.left;
            r.top = y < r.top ? y : r.top;
            if (x <= r.right) {
                x = r.right;
            }
            r.right = x;
            if (y <= r.bottom) {
                y = r.bottom;
            }
            r.bottom = y;
        }
        r.sort();
        return r;
    }
}
