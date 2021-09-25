package com.daimajia.easing.back;

import com.daimajia.easing.BaseEasingMethod;

public class BackEaseInOut extends BaseEasingMethod {

    /* renamed from: s */
    private float f1155s;

    public BackEaseInOut(float duration) {
        super(duration);
        this.f1155s = 1.70158f;
    }

    public BackEaseInOut(float duration, float back) {
        this(duration);
        this.f1155s = back;
    }

    public Float calculate(float t, float b, float c, float d) {
        float t2 = t / (d / 2.0f);
        if (t2 < 1.0f) {
            float f = (float) (((double) this.f1155s) * 1.525d);
            this.f1155s = f;
            return Float.valueOf(((c / 2.0f) * t2 * t2 * (((f + 1.0f) * t2) - this.f1155s)) + b);
        }
        float t3 = t2 - 2.0f;
        float f2 = (float) (((double) this.f1155s) * 1.525d);
        this.f1155s = f2;
        return Float.valueOf(((c / 2.0f) * ((t3 * t3 * (((f2 + 1.0f) * t3) + this.f1155s)) + 2.0f)) + b);
    }
}
