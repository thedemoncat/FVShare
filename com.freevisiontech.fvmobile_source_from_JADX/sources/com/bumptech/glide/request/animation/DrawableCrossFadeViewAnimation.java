package com.bumptech.glide.request.animation;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;

public class DrawableCrossFadeViewAnimation<T extends Drawable> implements GlideAnimation<T> {
    private final GlideAnimation<T> defaultAnimation;
    private final int duration;

    public DrawableCrossFadeViewAnimation(GlideAnimation<T> defaultAnimation2, int duration2) {
        this.defaultAnimation = defaultAnimation2;
        this.duration = duration2;
    }

    public boolean animate(T current, GlideAnimation.ViewAdapter adapter) {
        Drawable previous = adapter.getCurrentDrawable();
        if (previous != null) {
            TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{previous, current});
            transitionDrawable.setCrossFadeEnabled(true);
            transitionDrawable.startTransition(this.duration);
            adapter.setDrawable(transitionDrawable);
            return true;
        }
        this.defaultAnimation.animate(current, adapter);
        return false;
    }
}
