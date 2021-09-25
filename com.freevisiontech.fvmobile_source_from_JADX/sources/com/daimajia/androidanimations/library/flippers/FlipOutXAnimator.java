package com.daimajia.androidanimations.library.flippers;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import com.daimajia.androidanimations.library.BaseViewAnimator;

public class FlipOutXAnimator extends BaseViewAnimator {
    public void prepare(View target) {
        getAnimatorAgent().playTogether(new Animator[]{ObjectAnimator.ofFloat(target, "rotationX", new float[]{0.0f, 90.0f}), ObjectAnimator.ofFloat(target, "alpha", new float[]{1.0f, 0.0f})});
    }
}
