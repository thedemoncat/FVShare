package com.daimajia.androidanimations.library.flippers;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import com.daimajia.androidanimations.library.BaseViewAnimator;

public class FlipInYAnimator extends BaseViewAnimator {
    public void prepare(View target) {
        getAnimatorAgent().playTogether(new Animator[]{ObjectAnimator.ofFloat(target, "rotationY", new float[]{90.0f, -15.0f, 15.0f, 0.0f}), ObjectAnimator.ofFloat(target, "alpha", new float[]{0.25f, 0.5f, 0.75f, 1.0f})});
    }
}
