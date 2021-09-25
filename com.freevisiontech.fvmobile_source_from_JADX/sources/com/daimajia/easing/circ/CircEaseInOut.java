package com.daimajia.easing.circ;

import com.daimajia.easing.BaseEasingMethod;

public class CircEaseInOut extends BaseEasingMethod {
    public CircEaseInOut(float duration) {
        super(duration);
    }

    public Float calculate(float t, float b, float c, float d) {
        float t2 = t / (d / 2.0f);
        if (t2 < 1.0f) {
            return Float.valueOf((((-c) / 2.0f) * (((float) Math.sqrt((double) (1.0f - (t2 * t2)))) - 1.0f)) + b);
        }
        float t3 = t2 - 2.0f;
        return Float.valueOf(((c / 2.0f) * (((float) Math.sqrt((double) (1.0f - (t3 * t3)))) + 1.0f)) + b);
    }
}
