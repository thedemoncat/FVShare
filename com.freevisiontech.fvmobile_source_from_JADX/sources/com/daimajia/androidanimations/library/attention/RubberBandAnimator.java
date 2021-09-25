package com.daimajia.androidanimations.library.attention;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import com.daimajia.androidanimations.library.BaseViewAnimator;

public class RubberBandAnimator extends BaseViewAnimator {
    public void prepare(View target) {
        getAnimatorAgent().playTogether(new Animator[]{ObjectAnimator.ofFloat(target, "scaleX", new float[]{1.0f, 1.25f, 0.75f, 1.15f, 1.0f}), ObjectAnimator.ofFloat(target, "scaleY", new float[]{1.0f, 0.75f, 1.25f, 0.85f, 1.0f})});
    }
}
