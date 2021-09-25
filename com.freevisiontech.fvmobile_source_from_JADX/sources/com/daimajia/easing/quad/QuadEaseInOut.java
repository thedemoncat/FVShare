package com.daimajia.easing.quad;

import com.daimajia.easing.BaseEasingMethod;

public class QuadEaseInOut extends BaseEasingMethod {
    public QuadEaseInOut(float duration) {
        super(duration);
    }

    public Float calculate(float t, float b, float c, float d) {
        float t2 = t / (d / 2.0f);
        if (t2 < 1.0f) {
            return Float.valueOf(((c / 2.0f) * t2 * t2) + b);
        }
        float t3 = t2 - 1.0f;
        return Float.valueOf((((-c) / 2.0f) * (((t3 - 2.0f) * t3) - 1.0f)) + b);
    }
}
