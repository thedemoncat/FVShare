package com.daimajia.androidanimations.library.sliders;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import com.daimajia.androidanimations.library.BaseViewAnimator;

public class SlideOutLeftAnimator extends BaseViewAnimator {
    public void prepare(View target) {
        getAnimatorAgent().playTogether(new Animator[]{ObjectAnimator.ofFloat(target, "alpha", new float[]{1.0f, 0.0f}), ObjectAnimator.ofFloat(target, "translationX", new float[]{0.0f, (float) (-target.getRight())})});
    }
}
