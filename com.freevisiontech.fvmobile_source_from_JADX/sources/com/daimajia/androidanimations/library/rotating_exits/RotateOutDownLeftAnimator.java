package com.daimajia.androidanimations.library.rotating_exits;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import com.daimajia.androidanimations.library.BaseViewAnimator;

public class RotateOutDownLeftAnimator extends BaseViewAnimator {
    public void prepare(View target) {
        float x = (float) target.getPaddingLeft();
        float y = (float) (target.getHeight() - target.getPaddingBottom());
        getAnimatorAgent().playTogether(new Animator[]{ObjectAnimator.ofFloat(target, "alpha", new float[]{1.0f, 0.0f}), ObjectAnimator.ofFloat(target, "rotation", new float[]{0.0f, 90.0f}), ObjectAnimator.ofFloat(target, "pivotX", new float[]{x, x}), ObjectAnimator.ofFloat(target, "pivotY", new float[]{y, y})});
    }
}
