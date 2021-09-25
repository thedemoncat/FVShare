package com.daimajia.easing.bounce;

import com.daimajia.easing.BaseEasingMethod;

public class BounceEaseIn extends BaseEasingMethod {
    private BounceEaseOut mBounceEaseOut;

    public BounceEaseIn(float duration) {
        super(duration);
        this.mBounceEaseOut = new BounceEaseOut(duration);
    }

    public Float calculate(float t, float b, float c, float d) {
        return Float.valueOf((c - this.mBounceEaseOut.calculate(d - t, 0.0f, c, d).floatValue()) + b);
    }
}
