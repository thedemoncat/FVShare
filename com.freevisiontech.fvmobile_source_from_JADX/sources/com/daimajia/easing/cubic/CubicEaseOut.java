package com.daimajia.easing.cubic;

import com.daimajia.easing.BaseEasingMethod;

public class CubicEaseOut extends BaseEasingMethod {
    public CubicEaseOut(float duration) {
        super(duration);
    }

    public Float calculate(float t, float b, float c, float d) {
        float t2 = (t / d) - 1.0f;
        return Float.valueOf((((t2 * t2 * t2) + 1.0f) * c) + b);
    }
}
