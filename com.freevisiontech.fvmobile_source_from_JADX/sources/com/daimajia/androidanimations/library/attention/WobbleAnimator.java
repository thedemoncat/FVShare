package com.daimajia.androidanimations.library.attention;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import com.daimajia.androidanimations.library.BaseViewAnimator;

public class WobbleAnimator extends BaseViewAnimator {
    public void prepare(View target) {
        float one = (float) (((double) ((float) target.getWidth())) / 100.0d);
        getAnimatorAgent().playTogether(new Animator[]{ObjectAnimator.ofFloat(target, "translationX", new float[]{0.0f * one, -25.0f * one, 20.0f * one, -15.0f * one, 10.0f * one, -5.0f * one, 0.0f * one, 0.0f}), ObjectAnimator.ofFloat(target, "rotation", new float[]{0.0f, -5.0f, 3.0f, -3.0f, 2.0f, -1.0f, 0.0f})});
    }
}
