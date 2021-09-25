package com.daimajia.easing.quint;

import com.daimajia.easing.BaseEasingMethod;

public class QuintEaseOut extends BaseEasingMethod {
    public QuintEaseOut(float duration) {
        super(duration);
    }

    public Float calculate(float t, float b, float c, float d) {
        float t2 = (t / d) - 1.0f;
        return Float.valueOf((((t2 * t2 * t2 * t2 * t2) + 1.0f) * c) + b);
    }
}
