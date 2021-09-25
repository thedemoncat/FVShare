package com.daimajia.androidanimations.library.bouncing_entrances;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import com.daimajia.androidanimations.library.BaseViewAnimator;

public class BounceInAnimator extends BaseViewAnimator {
    public void prepare(View target) {
        getAnimatorAgent().playTogether(new Animator[]{ObjectAnimator.ofFloat(target, "alpha", new float[]{0.0f, 1.0f, 1.0f, 1.0f}), ObjectAnimator.ofFloat(target, "scaleX", new float[]{0.3f, 1.05f, 0.9f, 1.0f}), ObjectAnimator.ofFloat(target, "scaleY", new float[]{0.3f, 1.05f, 0.9f, 1.0f})});
    }
}
