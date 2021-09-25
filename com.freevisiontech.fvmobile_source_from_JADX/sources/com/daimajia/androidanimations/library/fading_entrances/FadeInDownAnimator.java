package com.daimajia.androidanimations.library.fading_entrances;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import com.daimajia.androidanimations.library.BaseViewAnimator;

public class FadeInDownAnimator extends BaseViewAnimator {
    public void prepare(View target) {
        getAnimatorAgent().playTogether(new Animator[]{ObjectAnimator.ofFloat(target, "alpha", new float[]{0.0f, 1.0f}), ObjectAnimator.ofFloat(target, "translationY", new float[]{(float) ((-target.getHeight()) / 4), 0.0f})});
    }
}
