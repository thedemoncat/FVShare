package com.daimajia.easing.back;

import com.daimajia.easing.BaseEasingMethod;

public class BackEaseIn extends BaseEasingMethod {

    /* renamed from: s */
    private float f1154s;

    public BackEaseIn(float duration) {
        super(duration);
        this.f1154s = 1.70158f;
    }

    public BackEaseIn(float duration, float back) {
        this(duration);
        this.f1154s = back;
    }

    public Float calculate(float t, float b, float c, float d) {
        float t2 = t / d;
        return Float.valueOf((c * t2 * t2 * (((this.f1154s + 1.0f) * t2) - this.f1154s)) + b);
    }
}
