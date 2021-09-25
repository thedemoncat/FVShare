package com.daimajia.androidanimations.library;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.support.p001v4.view.ViewCompat;
import android.view.View;
import android.view.animation.Interpolator;

public abstract class BaseViewAnimator {
    public static final long DURATION = 1000;
    private AnimatorSet mAnimatorSet = new AnimatorSet();
    private long mDuration = 1000;

    /* access modifiers changed from: protected */
    public abstract void prepare(View view);

    public BaseViewAnimator setTarget(View target) {
        reset(target);
        prepare(target);
        return this;
    }

    public void animate() {
        start();
    }

    public void restart() {
        this.mAnimatorSet = this.mAnimatorSet.clone();
        start();
    }

    public void reset(View target) {
        ViewCompat.setAlpha(target, 1.0f);
        ViewCompat.setScaleX(target, 1.0f);
        ViewCompat.setScaleY(target, 1.0f);
        ViewCompat.setTranslationX(target, 0.0f);
        ViewCompat.setTranslationY(target, 0.0f);
        ViewCompat.setRotation(target, 0.0f);
        ViewCompat.setRotationY(target, 0.0f);
        ViewCompat.setRotationX(target, 0.0f);
    }

    public void start() {
        this.mAnimatorSet.setDuration(this.mDuration);
        this.mAnimatorSet.start();
    }

    public BaseViewAnimator setDuration(long duration) {
        this.mDuration = duration;
        return this;
    }

    public BaseViewAnimator setStartDelay(long delay) {
        getAnimatorAgent().setStartDelay(delay);
        return this;
    }

    public long getStartDelay() {
        return this.mAnimatorSet.getStartDelay();
    }

    public BaseViewAnimator addAnimatorListener(Animator.AnimatorListener l) {
        this.mAnimatorSet.addListener(l);
        return this;
    }

    public void cancel() {
        this.mAnimatorSet.cancel();
    }

    public boolean isRunning() {
        return this.mAnimatorSet.isRunning();
    }

    public boolean isStarted() {
        return this.mAnimatorSet.isStarted();
    }

    public void removeAnimatorListener(Animator.AnimatorListener l) {
        this.mAnimatorSet.removeListener(l);
    }

    public void removeAllListener() {
        this.mAnimatorSet.removeAllListeners();
    }

    public BaseViewAnimator setInterpolator(Interpolator interpolator) {
        this.mAnimatorSet.setInterpolator(interpolator);
        return this;
    }

    public long getDuration() {
        return this.mDuration;
    }

    public AnimatorSet getAnimatorAgent() {
        return this.mAnimatorSet;
    }
}
