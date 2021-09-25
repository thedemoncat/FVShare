package com.daimajia.easing.cubic;

import com.daimajia.easing.BaseEasingMethod;

public class CubicEaseIn extends BaseEasingMethod {
    public CubicEaseIn(float duration) {
        super(duration);
    }

    public Float calculate(float t, float b, float c, float d) {
        float t2 = t / d;
        return Float.valueOf((c * t2 * t2 * t2) + b);
    }
}
