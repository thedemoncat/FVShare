package com.daimajia.androidanimations.library.bouncing_entrances;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import com.daimajia.androidanimations.library.BaseViewAnimator;

public class BounceInDownAnimator extends BaseViewAnimator {
    public void prepare(View target) {
        getAnimatorAgent().playTogether(new Animator[]{ObjectAnimator.ofFloat(target, "alpha", new float[]{0.0f, 1.0f, 1.0f, 1.0f}), ObjectAnimator.ofFloat(target, "translationY", new float[]{(float) (-target.getHeight()), 30.0f, -10.0f, 0.0f})});
    }
}
