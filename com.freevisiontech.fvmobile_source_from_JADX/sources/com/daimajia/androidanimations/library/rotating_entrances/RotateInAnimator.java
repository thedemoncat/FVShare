package com.daimajia.androidanimations.library.rotating_entrances;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import com.daimajia.androidanimations.library.BaseViewAnimator;

public class RotateInAnimator extends BaseViewAnimator {
    public void prepare(View target) {
        getAnimatorAgent().playTogether(new Animator[]{ObjectAnimator.ofFloat(target, "rotation", new float[]{-200.0f, 0.0f}), ObjectAnimator.ofFloat(target, "alpha", new float[]{0.0f, 1.0f})});
    }
}
