package com.daimajia.androidanimations.library.zooming_entrances;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewGroup;
import com.daimajia.androidanimations.library.BaseViewAnimator;

public class ZoomInUpAnimator extends BaseViewAnimator {
    public void prepare(View target) {
        int distance = ((ViewGroup) target.getParent()).getHeight() - target.getTop();
        getAnimatorAgent().playTogether(new Animator[]{ObjectAnimator.ofFloat(target, "alpha", new float[]{0.0f, 1.0f, 1.0f}), ObjectAnimator.ofFloat(target, "scaleX", new float[]{0.1f, 0.475f, 1.0f}), ObjectAnimator.ofFloat(target, "scaleY", new float[]{0.1f, 0.475f, 1.0f}), ObjectAnimator.ofFloat(target, "translationY", new float[]{(float) distance, -60.0f, 0.0f})});
    }
}
