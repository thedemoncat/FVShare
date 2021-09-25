package com.daimajia.androidanimations.library.specials;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import com.daimajia.androidanimations.library.BaseViewAnimator;
import com.daimajia.easing.Glider;
import com.daimajia.easing.Skill;

public class HingeAnimator extends BaseViewAnimator {
    public void prepare(View target) {
        float x = (float) target.getPaddingLeft();
        float y = (float) target.getPaddingTop();
        getAnimatorAgent().playTogether(new Animator[]{Glider.glide(Skill.SineEaseInOut, 1300.0f, (ValueAnimator) ObjectAnimator.ofFloat(target, "rotation", new float[]{0.0f, 80.0f, 60.0f, 80.0f, 60.0f, 60.0f})), ObjectAnimator.ofFloat(target, "translationY", new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 700.0f}), ObjectAnimator.ofFloat(target, "alpha", new float[]{1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f}), ObjectAnimator.ofFloat(target, "pivotX", new float[]{x, x, x, x, x, x}), ObjectAnimator.ofFloat(target, "pivotY", new float[]{y, y, y, y, y, y})});
        setDuration(1300);
    }
}
