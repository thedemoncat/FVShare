package com.daimajia.androidanimations.library.sliders;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewGroup;
import com.daimajia.androidanimations.library.BaseViewAnimator;

public class SlideInUpAnimator extends BaseViewAnimator {
    public void prepare(View target) {
        int distance = ((ViewGroup) target.getParent()).getHeight() - target.getTop();
        getAnimatorAgent().playTogether(new Animator[]{ObjectAnimator.ofFloat(target, "alpha", new float[]{0.0f, 1.0f}), ObjectAnimator.ofFloat(target, "translationY", new float[]{(float) distance, 0.0f})});
    }
}
