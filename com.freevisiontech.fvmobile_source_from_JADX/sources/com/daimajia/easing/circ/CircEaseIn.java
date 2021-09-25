package com.daimajia.easing.circ;

import com.daimajia.easing.BaseEasingMethod;

public class CircEaseIn extends BaseEasingMethod {
    public CircEaseIn(float duration) {
        super(duration);
    }

    public Float calculate(float t, float b, float c, float d) {
        float t2 = t / d;
        return Float.valueOf(((-c) * (((float) Math.sqrt((double) (1.0f - (t2 * t2)))) - 1.0f)) + b);
    }
}
