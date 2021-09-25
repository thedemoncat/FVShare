package com.daimajia.easing.linear;

import com.daimajia.easing.BaseEasingMethod;

public class Linear extends BaseEasingMethod {
    public Linear(float duration) {
        super(duration);
    }

    public Float calculate(float t, float b, float c, float d) {
        return Float.valueOf(((c * t) / d) + b);
    }
}
