package com.daimajia.easing.bounce;

import com.daimajia.easing.BaseEasingMethod;

public class BounceEaseOut extends BaseEasingMethod {
    public BounceEaseOut(float duration) {
        super(duration);
    }

    public Float calculate(float t, float b, float c, float d) {
        float t2 = t / d;
        if (t2 < 0.36363637f) {
            return Float.valueOf((7.5625f * t2 * t2 * c) + b);
        }
        if (t2 < 0.72727275f) {
            float t3 = t2 - 0.54545456f;
            return Float.valueOf((((7.5625f * t3 * t3) + 0.75f) * c) + b);
        } else if (((double) t2) < 0.9090909090909091d) {
            float t4 = t2 - 0.8181818f;
            return Float.valueOf((((7.5625f * t4 * t4) + 0.9375f) * c) + b);
        } else {
            float t5 = t2 - 0.95454544f;
            return Float.valueOf((((7.5625f * t5 * t5) + 0.984375f) * c) + b);
        }
    }
}
