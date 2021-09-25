package com.daimajia.androidanimations.library.specials.p017in;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import com.daimajia.androidanimations.library.BaseViewAnimator;
import com.daimajia.easing.Glider;
import com.daimajia.easing.Skill;

/* renamed from: com.daimajia.androidanimations.library.specials.in.DropOutAnimator */
public class DropOutAnimator extends BaseViewAnimator {
    /* access modifiers changed from: protected */
    public void prepare(View target) {
        int distance = target.getTop() + target.getHeight();
        getAnimatorAgent().playTogether(new Animator[]{ObjectAnimator.ofFloat(target, "alpha", new float[]{0.0f, 1.0f}), Glider.glide(Skill.BounceEaseOut, (float) getDuration(), (ValueAnimator) ObjectAnimator.ofFloat(target, "translationY", new float[]{(float) (-distance), 0.0f}))});
    }
}
