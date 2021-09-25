package com.daimajia.easing.elastic;

import com.daimajia.easing.BaseEasingMethod;

public class ElasticEaseInOut extends BaseEasingMethod {
    public ElasticEaseInOut(float duration) {
        super(duration);
    }

    public Float calculate(float t, float b, float c, float d) {
        if (t == 0.0f) {
            return Float.valueOf(b);
        }
        float t2 = t / (d / 2.0f);
        if (t2 == 2.0f) {
            return Float.valueOf(b + c);
        }
        float p = d * 0.45000002f;
        float a = c;
        float s = p / 4.0f;
        if (t2 < 1.0f) {
            float t3 = t2 - 1.0f;
            return Float.valueOf((-0.5f * ((float) Math.pow(2.0d, (double) (10.0f * t3))) * a * ((float) Math.sin((double) ((((t3 * d) - s) * 6.2831855f) / p)))) + b);
        }
        float t4 = t2 - 1.0f;
        return Float.valueOf((((float) Math.pow(2.0d, (double) (-10.0f * t4))) * a * ((float) Math.sin((double) ((((t4 * d) - s) * 6.2831855f) / p))) * 0.5f) + c + b);
    }
}
