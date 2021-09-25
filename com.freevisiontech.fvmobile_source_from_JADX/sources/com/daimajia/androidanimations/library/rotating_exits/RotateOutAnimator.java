package com.daimajia.androidanimations.library.rotating_exits;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import com.daimajia.androidanimations.library.BaseViewAnimator;

public class RotateOutAnimator extends BaseViewAnimator {
    public void prepare(View target) {
        getAnimatorAgent().playTogether(new Animator[]{ObjectAnimator.ofFloat(target, "alpha", new float[]{1.0f, 0.0f}), ObjectAnimator.ofFloat(target, "rotation", new float[]{0.0f, 200.0f})});
    }
}
