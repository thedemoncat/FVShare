package com.daimajia.androidanimations.library.attention;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import com.daimajia.androidanimations.library.BaseViewAnimator;

public class PulseAnimator extends BaseViewAnimator {
    public void prepare(View target) {
        getAnimatorAgent().playTogether(new Animator[]{ObjectAnimator.ofFloat(target, "scaleY", new float[]{1.0f, 1.1f, 1.0f}), ObjectAnimator.ofFloat(target, "scaleX", new float[]{1.0f, 1.1f, 1.0f})});
    }
}
