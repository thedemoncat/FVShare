package com.daimajia.androidanimations.library.attention;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import com.daimajia.androidanimations.library.BaseViewAnimator;

public class ShakeAnimator extends BaseViewAnimator {
    public void prepare(View target) {
        getAnimatorAgent().playTogether(new Animator[]{ObjectAnimator.ofFloat(target, "translationX", new float[]{0.0f, 25.0f, -25.0f, 25.0f, -25.0f, 15.0f, -15.0f, 6.0f, -6.0f, 0.0f})});
    }
}
