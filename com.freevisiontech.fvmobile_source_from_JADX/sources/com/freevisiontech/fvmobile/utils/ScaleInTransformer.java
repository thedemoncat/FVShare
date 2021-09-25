package com.freevisiontech.fvmobile.utils;

import android.annotation.TargetApi;
import android.support.p001v4.view.ViewPager;
import android.view.View;
import com.vise.log.ViseLog;

public class ScaleInTransformer extends BasePageTransformer {
    private static final float DEFAULT_MIN_SCALE = 0.8f;
    private float mMinScale;

    public ScaleInTransformer() {
        this.mMinScale = 0.8f;
    }

    public ScaleInTransformer(float minScale) {
        this(minScale, NonPageTransformer.INSTANCE);
    }

    public ScaleInTransformer(ViewPager.PageTransformer pageTransformer) {
        this(0.8f, pageTransformer);
    }

    public ScaleInTransformer(float minScale, ViewPager.PageTransformer pageTransformer) {
        this.mMinScale = 0.8f;
        this.mMinScale = minScale;
        this.mPageTransformer = pageTransformer;
    }

    @TargetApi(11)
    public void pageTransform(View view, float position) {
        int pageWidth = view.getWidth();
        view.setPivotY((float) (view.getHeight() / 2));
        view.setPivotX((float) (pageWidth / 2));
        if (position < -1.0f) {
            view.setScaleX(this.mMinScale);
            view.setScaleY(this.mMinScale);
            view.setPivotX((float) pageWidth);
        } else if (position > 1.0f) {
            ViseLog.m1466e("pageTransform position > 1 position:" + position);
            view.setPivotX(0.0f);
            view.setScaleX(this.mMinScale);
            view.setScaleY(this.mMinScale);
        } else if (position < 0.0f) {
            float scaleFactor = ((1.0f + position) * (1.0f - this.mMinScale)) + this.mMinScale;
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
            view.setPivotX(((float) pageWidth) * (((-position) * 0.5f) + 0.5f));
            ViseLog.m1466e("pageTransform position < 0 position: " + position);
            ViseLog.m1466e("pageTransform position < 0 scaleFactor: " + scaleFactor);
            ViseLog.m1466e("pageTransform position < 0 PivotX : " + (((float) pageWidth) * (((-position) * 0.5f) + 0.5f)));
        } else {
            float scaleFactor2 = ((1.0f - position) * (1.0f - this.mMinScale)) + this.mMinScale;
            view.setScaleX(scaleFactor2);
            view.setScaleY(scaleFactor2);
            view.setPivotX(((float) pageWidth) * (1.0f - position) * 0.5f);
            ViseLog.m1466e("pageTransform position > 0 position: " + position);
            ViseLog.m1466e("pageTransform position > 0 scaleFactor: " + (((1.0f - position) * (1.0f - this.mMinScale)) + this.mMinScale));
            ViseLog.m1466e("pageTransform position > 0 PivotX : " + (((float) pageWidth) * (1.0f - position) * 0.5f));
        }
    }
}
