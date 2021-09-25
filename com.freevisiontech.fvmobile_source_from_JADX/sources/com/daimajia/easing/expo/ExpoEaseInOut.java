package com.daimajia.easing.expo;

import com.daimajia.easing.BaseEasingMethod;

public class ExpoEaseInOut extends BaseEasingMethod {
    public ExpoEaseInOut(float duration) {
        super(duration);
    }

    public Float calculate(float t, float b, float c, float d) {
        if (t == 0.0f) {
            return Float.valueOf(b);
        }
        if (t == d) {
            return Float.valueOf(b + c);
        }
        float t2 = t / (d / 2.0f);
        if (t2 < 1.0f) {
            return Float.valueOf(((c / 2.0f) * ((float) Math.pow(2.0d, (double) (10.0f * (t2 - 1.0f))))) + b);
        }
        return Float.valueOf(((c / 2.0f) * ((-((float) Math.pow(2.0d, (double) (-10.0f * (t2 - 1.0f))))) + 2.0f)) + b);
    }
}
