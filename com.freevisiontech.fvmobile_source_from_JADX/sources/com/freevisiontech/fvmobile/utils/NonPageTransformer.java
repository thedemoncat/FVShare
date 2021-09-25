package com.freevisiontech.fvmobile.utils;

import android.support.p001v4.view.ViewPager;
import android.view.View;

public class NonPageTransformer implements ViewPager.PageTransformer {
    public static final ViewPager.PageTransformer INSTANCE = new NonPageTransformer();

    public void transformPage(View page, float position) {
        page.setScaleX(0.999f);
    }
}
