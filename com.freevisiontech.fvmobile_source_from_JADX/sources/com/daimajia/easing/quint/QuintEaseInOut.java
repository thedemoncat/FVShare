package com.daimajia.easing.quint;

import com.daimajia.easing.BaseEasingMethod;

public class QuintEaseInOut extends BaseEasingMethod {
    public QuintEaseInOut(float duration) {
        super(duration);
    }

    public Float calculate(float t, float b, float c, float d) {
        float t2 = t / (d / 2.0f);
        if (t2 < 1.0f) {
            return Float.valueOf(((c / 2.0f) * t2 * t2 * t2 * t2 * t2) + b);
        }
        float t3 = t2 - 2.0f;
        return Float.valueOf(((c / 2.0f) * ((t3 * t3 * t3 * t3 * t3) + 2.0f)) + b);
    }
}
