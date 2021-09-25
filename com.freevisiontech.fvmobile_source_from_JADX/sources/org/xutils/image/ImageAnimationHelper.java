package org.xutils.image;

import android.graphics.drawable.Drawable;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import java.lang.reflect.Method;
import org.xutils.common.util.LogUtil;

public final class ImageAnimationHelper {
    private static final Method cloneMethod;

    static {
        Method method;
        try {
            method = Animation.class.getDeclaredMethod("clone", new Class[0]);
            method.setAccessible(true);
        } catch (Throwable ex) {
            method = null;
            LogUtil.m1571w(ex.getMessage(), ex);
        }
        cloneMethod = method;
    }

    private ImageAnimationHelper() {
    }

    public static void fadeInDisplay(ImageView imageView, Drawable drawable) {
        AlphaAnimation fadeAnimation = new AlphaAnimation(0.0f, 1.0f);
        fadeAnimation.setDuration(300);
        fadeAnimation.setInterpolator(new DecelerateInterpolator());
        imageView.setImageDrawable(drawable);
        imageView.startAnimation(fadeAnimation);
    }

    public static void animationDisplay(ImageView imageView, Drawable drawable, Animation animation) {
        imageView.setImageDrawable(drawable);
        if (cloneMethod == null || animation == null) {
            imageView.startAnimation(animation);
            return;
        }
        try {
            imageView.startAnimation((Animation) cloneMethod.invoke(animation, new Object[0]));
        } catch (Throwable th) {
            imageView.startAnimation(animation);
        }
    }
}
