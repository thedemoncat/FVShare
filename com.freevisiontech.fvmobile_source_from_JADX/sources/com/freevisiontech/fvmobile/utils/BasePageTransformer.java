package com.freevisiontech.fvmobile.utils;

import android.annotation.TargetApi;
import android.support.p001v4.view.ViewPager;
import android.view.View;

public abstract class BasePageTransformer implements ViewPager.PageTransformer {
    public static final float DEFAULT_CENTER = 0.5f;
    protected ViewPager.PageTransformer mPageTransformer = NonPageTransformer.INSTANCE;

    /* access modifiers changed from: protected */
    public abstract void pageTransform(View view, float f);

    @TargetApi(11)
    public void transformPage(View view, float position) {
        if (this.mPageTransformer != null) {
            this.mPageTransformer.transformPage(view, position);
        }
        pageTransform(view, position);
    }
}
