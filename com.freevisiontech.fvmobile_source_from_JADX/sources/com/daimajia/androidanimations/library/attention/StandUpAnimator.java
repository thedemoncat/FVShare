package com.daimajia.androidanimations.library.attention;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import com.daimajia.androidanimations.library.BaseViewAnimator;

public class StandUpAnimator extends BaseViewAnimator {
    public void prepare(View target) {
        float x = (float) ((((target.getWidth() - target.getPaddingLeft()) - target.getPaddingRight()) / 2) + target.getPaddingLeft());
        float y = (float) (target.getHeight() - target.getPaddingBottom());
        getAnimatorAgent().playTogether(new Animator[]{ObjectAnimator.ofFloat(target, "pivotX", new float[]{x, x, x, x, x}), ObjectAnimator.ofFloat(target, "pivotY", new float[]{y, y, y, y, y}), ObjectAnimator.ofFloat(target, "rotationX", new float[]{55.0f, -30.0f, 15.0f, -15.0f, 0.0f})});
    }
}
