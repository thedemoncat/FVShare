package com.daimajia.easing.expo;

import com.daimajia.easing.BaseEasingMethod;

public class ExpoEaseIn extends BaseEasingMethod {
    public ExpoEaseIn(float duration) {
        super(duration);
    }

    public Float calculate(float t, float b, float c, float d) {
        if (t != 0.0f) {
            b += ((float) Math.pow(2.0d, (double) (10.0f * ((t / d) - 1.0f)))) * c;
        }
        return Float.valueOf(b);
    }
}
