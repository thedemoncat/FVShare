package com.daimajia.easing.sine;

import com.daimajia.easing.BaseEasingMethod;

public class SineEaseOut extends BaseEasingMethod {
    public SineEaseOut(float duration) {
        super(duration);
    }

    public Float calculate(float t, float b, float c, float d) {
        return Float.valueOf((((float) Math.sin(((double) (t / d)) * 1.5707963267948966d)) * c) + b);
    }
}
