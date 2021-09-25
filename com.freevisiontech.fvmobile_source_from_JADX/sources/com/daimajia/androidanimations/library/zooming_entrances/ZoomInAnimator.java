package com.daimajia.androidanimations.library.zooming_entrances;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import com.daimajia.androidanimations.library.BaseViewAnimator;

public class ZoomInAnimator extends BaseViewAnimator {
    public void prepare(View target) {
        getAnimatorAgent().playTogether(new Animator[]{ObjectAnimator.ofFloat(target, "scaleX", new float[]{0.45f, 1.0f}), ObjectAnimator.ofFloat(target, "scaleY", new float[]{0.45f, 1.0f}), ObjectAnimator.ofFloat(target, "alpha", new float[]{0.0f, 1.0f})});
    }
}
