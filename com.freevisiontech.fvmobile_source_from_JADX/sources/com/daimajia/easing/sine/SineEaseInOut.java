package com.daimajia.easing.sine;

import com.daimajia.easing.BaseEasingMethod;

public class SineEaseInOut extends BaseEasingMethod {
    public SineEaseInOut(float duration) {
        super(duration);
    }

    public Float calculate(float t, float b, float c, float d) {
        return Float.valueOf((((-c) / 2.0f) * (((float) Math.cos((3.141592653589793d * ((double) t)) / ((double) d))) - 1.0f)) + b);
    }
}
