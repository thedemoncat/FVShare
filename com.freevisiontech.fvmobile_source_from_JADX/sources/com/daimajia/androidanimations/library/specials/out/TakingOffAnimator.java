package com.daimajia.androidanimations.library.specials.out;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import com.daimajia.androidanimations.library.BaseViewAnimator;
import com.daimajia.easing.Glider;
import com.daimajia.easing.Skill;

public class TakingOffAnimator extends BaseViewAnimator {
    /* access modifiers changed from: protected */
    public void prepare(View target) {
        getAnimatorAgent().playTogether(new Animator[]{Glider.glide(Skill.QuintEaseOut, (float) getDuration(), (ValueAnimator) ObjectAnimator.ofFloat(target, "scaleX", new float[]{1.0f, 1.5f})), Glider.glide(Skill.QuintEaseOut, (float) getDuration(), (ValueAnimator) ObjectAnimator.ofFloat(target, "scaleY", new float[]{1.0f, 1.5f})), Glider.glide(Skill.QuintEaseOut, (float) getDuration(), (ValueAnimator) ObjectAnimator.ofFloat(target, "alpha", new float[]{1.0f, 0.0f}))});
    }
}
