package com.bumptech.glide.request.animation;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import com.bumptech.glide.request.animation.ViewAnimation;

public class DrawableCrossFadeFactory<T extends Drawable> implements GlideAnimationFactory<T> {
    private static final int DEFAULT_DURATION_MS = 300;
    private final ViewAnimationFactory<T> animationFactory;
    private final int duration;
    private DrawableCrossFadeViewAnimation<T> firstResourceAnimation;
    private DrawableCrossFadeViewAnimation<T> secondResourceAnimation;

    public DrawableCrossFadeFactory() {
        this(300);
    }

    public DrawableCrossFadeFactory(int duration2) {
        this(new ViewAnimationFactory((ViewAnimation.AnimationFactory) new DefaultAnimationFactory(duration2)), duration2);
    }

    public DrawableCrossFadeFactory(Context context, int defaultAnimationId, int duration2) {
        this(new ViewAnimationFactory(context, defaultAnimationId), duration2);
    }

    public DrawableCrossFadeFactory(Animation defaultAnimation, int duration2) {
        this(new ViewAnimationFactory(defaultAnimation), duration2);
    }

    DrawableCrossFadeFactory(ViewAnimationFactory<T> animationFactory2, int duration2) {
        this.animationFactory = animationFactory2;
        this.duration = duration2;
    }

    public GlideAnimation<T> build(boolean isFromMemoryCache, boolean isFirstResource) {
        if (isFromMemoryCache) {
            return NoAnimation.get();
        }
        if (isFirstResource) {
            return getFirstResourceAnimation();
        }
        return getSecondResourceAnimation();
    }

    private GlideAnimation<T> getFirstResourceAnimation() {
        if (this.firstResourceAnimation == null) {
            this.firstResourceAnimation = new DrawableCrossFadeViewAnimation<>(this.animationFactory.build(false, true), this.duration);
        }
        return this.firstResourceAnimation;
    }

    private GlideAnimation<T> getSecondResourceAnimation() {
        if (this.secondResourceAnimation == null) {
            this.secondResourceAnimation = new DrawableCrossFadeViewAnimation<>(this.animationFactory.build(false, false), this.duration);
        }
        return this.secondResourceAnimation;
    }

    private static class DefaultAnimationFactory implements ViewAnimation.AnimationFactory {
        private final int duration;

        DefaultAnimationFactory(int duration2) {
            this.duration = duration2;
        }

        public Animation build() {
            AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
            animation.setDuration((long) this.duration);
            return animation;
        }
    }
}
