package com.daimajia.easing.expo;

import com.daimajia.easing.BaseEasingMethod;

public class ExpoEaseOut extends BaseEasingMethod {
    public ExpoEaseOut(float duration) {
        super(duration);
    }

    public Float calculate(float t, float b, float c, float d) {
        return Float.valueOf(t == d ? b + c : (((-((float) Math.pow(2.0d, (double) ((-10.0f * t) / d)))) + 1.0f) * c) + b);
    }
}
