package com.daimajia.easing.sine;

import com.daimajia.easing.BaseEasingMethod;

public class SineEaseIn extends BaseEasingMethod {
    public SineEaseIn(float duration) {
        super(duration);
    }

    public Float calculate(float t, float b, float c, float d) {
        return Float.valueOf(((-c) * ((float) Math.cos(((double) (t / d)) * 1.5707963267948966d))) + c + b);
    }
}
