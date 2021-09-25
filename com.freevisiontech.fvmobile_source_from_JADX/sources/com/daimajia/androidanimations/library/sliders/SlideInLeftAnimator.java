package com.daimajia.androidanimations.library.sliders;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewGroup;
import com.daimajia.androidanimations.library.BaseViewAnimator;

public class SlideInLeftAnimator extends BaseViewAnimator {
    public void prepare(View target) {
        int distance = ((ViewGroup) target.getParent()).getWidth() - target.getLeft();
        getAnimatorAgent().playTogether(new Animator[]{ObjectAnimator.ofFloat(target, "alpha", new float[]{0.0f, 1.0f}), ObjectAnimator.ofFloat(target, "translationX", new float[]{(float) (-distance), 0.0f})});
    }
}
