package com.daimajia.easing.bounce;

import com.daimajia.easing.BaseEasingMethod;

public class BounceEaseInOut extends BaseEasingMethod {
    private BounceEaseIn mBounceEaseIn;
    private BounceEaseOut mBounceEaseOut;

    public BounceEaseInOut(float duration) {
        super(duration);
        this.mBounceEaseIn = new BounceEaseIn(duration);
        this.mBounceEaseOut = new BounceEaseOut(duration);
    }

    public Float calculate(float t, float b, float c, float d) {
        if (t < d / 2.0f) {
            return Float.valueOf((this.mBounceEaseIn.calculate(2.0f * t, 0.0f, c, d).floatValue() * 0.5f) + b);
        }
        return Float.valueOf((this.mBounceEaseOut.calculate((2.0f * t) - d, 0.0f, c, d).floatValue() * 0.5f) + (c * 0.5f) + b);
    }
}
