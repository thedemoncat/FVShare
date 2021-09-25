package com.daimajia.androidanimations.library.specials;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import com.daimajia.androidanimations.library.BaseViewAnimator;

public class RollInAnimator extends BaseViewAnimator {
    public void prepare(View target) {
        getAnimatorAgent().playTogether(new Animator[]{ObjectAnimator.ofFloat(target, "alpha", new float[]{0.0f, 1.0f}), ObjectAnimator.ofFloat(target, "translationX", new float[]{(float) (-((target.getWidth() - target.getPaddingLeft()) - target.getPaddingRight())), 0.0f}), ObjectAnimator.ofFloat(target, "rotation", new float[]{-120.0f, 0.0f})});
    }
}
