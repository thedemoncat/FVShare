package com.freevisiontech.fvmobile.utility;

import android.graphics.Color;

public class ColorUtil {
    public static int computeGradientColor(int startColor, int endColor, float rate) {
        if (rate < 0.0f) {
            rate = 0.0f;
        }
        if (rate > 1.0f) {
            rate = 1.0f;
        }
        return Color.argb(Math.round(((float) Color.alpha(startColor)) + (((float) (Color.alpha(endColor) - Color.alpha(startColor))) * rate)), Math.round(((float) Color.red(startColor)) + (((float) (Color.red(endColor) - Color.red(startColor))) * rate)), Math.round(((float) Color.green(startColor)) + (((float) (Color.green(endColor) - Color.green(startColor))) * rate)), Math.round(((float) Color.blue(startColor)) + (((float) (Color.blue(endColor) - Color.blue(startColor))) * rate)));
    }
}
