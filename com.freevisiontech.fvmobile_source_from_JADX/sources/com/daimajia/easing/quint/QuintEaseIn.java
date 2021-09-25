package com.daimajia.easing.quint;

import com.daimajia.easing.BaseEasingMethod;

public class QuintEaseIn extends BaseEasingMethod {
    public QuintEaseIn(float duration) {
        super(duration);
    }

    public Float calculate(float t, float b, float c, float d) {
        float t2 = t / d;
        return Float.valueOf((c * t2 * t2 * t2 * t2 * t2) + b);
    }
}
