package com.nineoldandroids.view;

import android.os.Build;
import android.view.View;
import android.view.animation.Interpolator;
import com.nineoldandroids.animation.Animator;
import java.util.WeakHashMap;

public abstract class ViewPropertyAnimator {
    private static final WeakHashMap<View, ViewPropertyAnimator> ANIMATORS = new WeakHashMap<>(0);

    public abstract ViewPropertyAnimator alpha(float f);

    public abstract ViewPropertyAnimator alphaBy(float f);

    public abstract void cancel();

    public abstract long getDuration();

    public abstract long getStartDelay();

    public abstract ViewPropertyAnimator rotation(float f);

    public abstract ViewPropertyAnimator rotationBy(float f);

    public abstract ViewPropertyAnimator rotationX(float f);

    public abstract ViewPropertyAnimator rotationXBy(float f);

    public abstract ViewPropertyAnimator rotationY(float f);

    public abstract ViewPropertyAnimator rotationYBy(float f);

    public abstract ViewPropertyAnimator scaleX(float f);

    public abstract ViewPropertyAnimator scaleXBy(float f);

    public abstract ViewPropertyAnimator scaleY(float f);

    public abstract ViewPropertyAnimator scaleYBy(float f);

    public abstract ViewPropertyAnimator setDuration(long j);

    public abstract ViewPropertyAnimator setInterpolator(Interpolator interpolator);

    public abstract ViewPropertyAnimator setListener(Animator.AnimatorListener animatorListener);

    public abstract ViewPropertyAnimator setStartDelay(long j);

    public abstract void start();

    public abstract ViewPropertyAnimator translationX(float f);

    public abstract ViewPropertyAnimator translationXBy(float f);

    public abstract ViewPropertyAnimator translationY(float f);

    public abstract ViewPropertyAnimator translationYBy(float f);

    /* renamed from: x */
    public abstract ViewPropertyAnimator mo14703x(float f);

    public abstract ViewPropertyAnimator xBy(float f);

    /* renamed from: y */
    public abstract ViewPropertyAnimator mo14705y(float f);

    public abstract ViewPropertyAnimator yBy(float f);

    public static ViewPropertyAnimator animate(View view) {
        ViewPropertyAnimator animator = ANIMATORS.get(view);
        if (animator == null) {
            int version = Integer.valueOf(Build.VERSION.SDK).intValue();
            if (version >= 14) {
                animator = new ViewPropertyAnimatorICS(view);
            } else if (version >= 11) {
                animator = new ViewPropertyAnimatorHC(view);
            } else {
                animator = new ViewPropertyAnimatorPreHC(view);
            }
            ANIMATORS.put(view, animator);
        }
        return animator;
    }
}
