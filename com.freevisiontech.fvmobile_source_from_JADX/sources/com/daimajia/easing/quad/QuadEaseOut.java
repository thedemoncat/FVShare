package com.daimajia.easing.quad;

import com.daimajia.easing.BaseEasingMethod;

public class QuadEaseOut extends BaseEasingMethod {
    public QuadEaseOut(float duration) {
        super(duration);
    }

    public Float calculate(float t, float b, float c, float d) {
        float t2 = t / d;
        return Float.valueOf(((-c) * t2 * (t2 - 2.0f)) + b);
    }
}
