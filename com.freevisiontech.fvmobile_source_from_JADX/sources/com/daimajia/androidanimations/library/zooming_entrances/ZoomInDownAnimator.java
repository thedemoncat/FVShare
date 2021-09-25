package com.daimajia.androidanimations.library.zooming_entrances;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import com.daimajia.androidanimations.library.BaseViewAnimator;

public class ZoomInDownAnimator extends BaseViewAnimator {
    public void prepare(View target) {
        getAnimatorAgent().playTogether(new Animator[]{ObjectAnimator.ofFloat(target, "scaleX", new float[]{0.1f, 0.475f, 1.0f}), ObjectAnimator.ofFloat(target, "scaleY", new float[]{0.1f, 0.475f, 1.0f}), ObjectAnimator.ofFloat(target, "translationY", new float[]{(float) (-target.getBottom()), 60.0f, 0.0f}), ObjectAnimator.ofFloat(target, "alpha", new float[]{0.0f, 1.0f, 1.0f})});
    }
}
