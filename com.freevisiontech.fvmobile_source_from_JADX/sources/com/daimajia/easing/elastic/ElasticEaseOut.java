package com.daimajia.easing.elastic;

import com.daimajia.easing.BaseEasingMethod;

public class ElasticEaseOut extends BaseEasingMethod {
    public ElasticEaseOut(float duration) {
        super(duration);
    }

    public Float calculate(float t, float b, float c, float d) {
        if (t == 0.0f) {
            return Float.valueOf(b);
        }
        float t2 = t / d;
        if (t2 == 1.0f) {
            return Float.valueOf(b + c);
        }
        float p = d * 0.3f;
        return Float.valueOf((((float) Math.pow(2.0d, (double) (-10.0f * t2))) * c * ((float) Math.sin((double) ((((t2 * d) - (p / 4.0f)) * 6.2831855f) / p)))) + c + b);
    }
}
