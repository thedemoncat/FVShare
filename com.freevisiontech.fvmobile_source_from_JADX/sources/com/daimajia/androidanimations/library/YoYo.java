package com.daimajia.androidanimations.library;

import android.animation.Animator;
import android.support.p001v4.view.ViewCompat;
import android.view.View;
import android.view.animation.Interpolator;
import java.util.ArrayList;
import java.util.List;

public class YoYo {
    public static final float CENTER_PIVOT = Float.MAX_VALUE;
    private static final long DURATION = 1000;
    public static final int INFINITE = -1;
    private static final long NO_DELAY = 0;
    /* access modifiers changed from: private */
    public BaseViewAnimator animator;
    private List<Animator.AnimatorListener> callbacks;
    private long delay;
    private long duration;
    private Interpolator interpolator;
    private float pivotX;
    private float pivotY;
    /* access modifiers changed from: private */
    public boolean repeat;
    /* access modifiers changed from: private */
    public long repeatTimes;
    private View target;

    public interface AnimatorCallback {
        void call(Animator animator);
    }

    private YoYo(AnimationComposer animationComposer) {
        this.animator = animationComposer.animator;
        this.duration = animationComposer.duration;
        this.delay = animationComposer.delay;
        this.repeat = animationComposer.repeat;
        this.repeatTimes = animationComposer.repeatTimes;
        this.interpolator = animationComposer.interpolator;
        this.pivotX = animationComposer.pivotX;
        this.pivotY = animationComposer.pivotY;
        this.callbacks = animationComposer.callbacks;
        this.target = animationComposer.target;
    }

    public static AnimationComposer with(Techniques techniques) {
        return new AnimationComposer(techniques);
    }

    public static AnimationComposer with(BaseViewAnimator animator2) {
        return new AnimationComposer(animator2);
    }

    private static class EmptyAnimatorListener implements Animator.AnimatorListener {
        private EmptyAnimatorListener() {
        }

        public void onAnimationStart(Animator animation) {
        }

        public void onAnimationEnd(Animator animation) {
        }

        public void onAnimationCancel(Animator animation) {
        }

        public void onAnimationRepeat(Animator animation) {
        }
    }

    public static final class AnimationComposer {
        /* access modifiers changed from: private */
        public BaseViewAnimator animator;
        /* access modifiers changed from: private */
        public List<Animator.AnimatorListener> callbacks;
        /* access modifiers changed from: private */
        public long delay;
        /* access modifiers changed from: private */
        public long duration;
        /* access modifiers changed from: private */
        public Interpolator interpolator;
        /* access modifiers changed from: private */
        public float pivotX;
        /* access modifiers changed from: private */
        public float pivotY;
        /* access modifiers changed from: private */
        public boolean repeat;
        /* access modifiers changed from: private */
        public long repeatTimes;
        /* access modifiers changed from: private */
        public View target;

        private AnimationComposer(Techniques techniques) {
            this.callbacks = new ArrayList();
            this.duration = 1000;
            this.delay = 0;
            this.repeat = false;
            this.repeatTimes = 0;
            this.pivotX = Float.MAX_VALUE;
            this.pivotY = Float.MAX_VALUE;
            this.animator = techniques.getAnimator();
        }

        private AnimationComposer(BaseViewAnimator animator2) {
            this.callbacks = new ArrayList();
            this.duration = 1000;
            this.delay = 0;
            this.repeat = false;
            this.repeatTimes = 0;
            this.pivotX = Float.MAX_VALUE;
            this.pivotY = Float.MAX_VALUE;
            this.animator = animator2;
        }

        public AnimationComposer duration(long duration2) {
            this.duration = duration2;
            return this;
        }

        public AnimationComposer delay(long delay2) {
            this.delay = delay2;
            return this;
        }

        public AnimationComposer interpolate(Interpolator interpolator2) {
            this.interpolator = interpolator2;
            return this;
        }

        public AnimationComposer pivot(float pivotX2, float pivotY2) {
            this.pivotX = pivotX2;
            this.pivotY = pivotY2;
            return this;
        }

        public AnimationComposer pivotX(float pivotX2) {
            this.pivotX = pivotX2;
            return this;
        }

        public AnimationComposer pivotY(float pivotY2) {
            this.pivotY = pivotY2;
            return this;
        }

        public AnimationComposer repeat(int times) {
            if (times < -1) {
                throw new RuntimeException("Can not be less than -1, -1 is infinite loop");
            }
            this.repeat = times != 0;
            this.repeatTimes = (long) times;
            return this;
        }

        public AnimationComposer withListener(Animator.AnimatorListener listener) {
            this.callbacks.add(listener);
            return this;
        }

        public AnimationComposer onStart(final AnimatorCallback callback) {
            this.callbacks.add(new EmptyAnimatorListener() {
                public void onAnimationStart(Animator animation) {
                    callback.call(animation);
                }
            });
            return this;
        }

        public AnimationComposer onEnd(final AnimatorCallback callback) {
            this.callbacks.add(new EmptyAnimatorListener() {
                public void onAnimationEnd(Animator animation) {
                    callback.call(animation);
                }
            });
            return this;
        }

        public AnimationComposer onCancel(final AnimatorCallback callback) {
            this.callbacks.add(new EmptyAnimatorListener() {
                public void onAnimationCancel(Animator animation) {
                    callback.call(animation);
                }
            });
            return this;
        }

        public AnimationComposer onRepeat(final AnimatorCallback callback) {
            this.callbacks.add(new EmptyAnimatorListener() {
                public void onAnimationRepeat(Animator animation) {
                    callback.call(animation);
                }
            });
            return this;
        }

        public YoYoString playOn(View target2) {
            this.target = target2;
            return new YoYoString(new YoYo(this).play(), this.target);
        }
    }

    public static final class YoYoString {
        private BaseViewAnimator animator;
        private View target;

        private YoYoString(BaseViewAnimator animator2, View target2) {
            this.target = target2;
            this.animator = animator2;
        }

        public boolean isStarted() {
            return this.animator.isStarted();
        }

        public boolean isRunning() {
            return this.animator.isRunning();
        }

        public void stop() {
            stop(true);
        }

        public void stop(boolean reset) {
            this.animator.cancel();
            if (reset) {
                this.animator.reset(this.target);
            }
        }
    }

    /* access modifiers changed from: private */
    public BaseViewAnimator play() {
        this.animator.setTarget(this.target);
        if (this.pivotX == Float.MAX_VALUE) {
            ViewCompat.setPivotX(this.target, ((float) this.target.getMeasuredWidth()) / 2.0f);
        } else {
            this.target.setPivotX(this.pivotX);
        }
        if (this.pivotY == Float.MAX_VALUE) {
            ViewCompat.setPivotY(this.target, ((float) this.target.getMeasuredHeight()) / 2.0f);
        } else {
            this.target.setPivotY(this.pivotY);
        }
        this.animator.setDuration(this.duration).setInterpolator(this.interpolator).setStartDelay(this.delay);
        if (this.callbacks.size() > 0) {
            for (Animator.AnimatorListener callback : this.callbacks) {
                this.animator.addAnimatorListener(callback);
            }
        }
        if (this.repeat) {
            this.animator.addAnimatorListener(new Animator.AnimatorListener() {
                private long currentTimes = 0;

                public void onAnimationStart(Animator animation) {
                    this.currentTimes++;
                }

                public void onAnimationEnd(Animator animation) {
                    if (YoYo.this.repeat) {
                        if (YoYo.this.repeatTimes == -1 || this.currentTimes < YoYo.this.repeatTimes) {
                            YoYo.this.animator.restart();
                        }
                    }
                }

                public void onAnimationCancel(Animator animation) {
                    long unused = YoYo.this.repeatTimes = 0;
                    boolean unused2 = YoYo.this.repeat = false;
                }

                public void onAnimationRepeat(Animator animation) {
                }
            });
        }
        this.animator.animate();
        return this.animator;
    }
}
