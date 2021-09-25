package com.daimajia.easing.circ;

import com.daimajia.easing.BaseEasingMethod;

public class CircEaseOut extends BaseEasingMethod {
    public CircEaseOut(float duration) {
        super(duration);
    }

    public Float calculate(float t, float b, float c, float d) {
        float t2 = (t / d) - 1.0f;
        return Float.valueOf((((float) Math.sqrt((double) (1.0f - (t2 * t2)))) * c) + b);
    }
}
