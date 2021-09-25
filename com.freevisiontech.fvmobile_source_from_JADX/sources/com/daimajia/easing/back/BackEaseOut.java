package com.daimajia.easing.back;

import com.daimajia.easing.BaseEasingMethod;

public class BackEaseOut extends BaseEasingMethod {

    /* renamed from: s */
    private float f1156s;

    public BackEaseOut(float duration) {
        super(duration);
        this.f1156s = 1.70158f;
    }

    public BackEaseOut(float duration, float back) {
        this(duration);
        this.f1156s = back;
    }

    public Float calculate(float t, float b, float c, float d) {
        float t2 = (t / d) - 1.0f;
        return Float.valueOf((((t2 * t2 * (((this.f1156s + 1.0f) * t2) + this.f1156s)) + 1.0f) * c) + b);
    }
}
