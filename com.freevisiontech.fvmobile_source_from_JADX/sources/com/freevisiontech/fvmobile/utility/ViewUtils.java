package com.freevisiontech.fvmobile.utility;

import android.view.View;

public class ViewUtils {
    public static int getViewMeasuredHeight(View view) {
        calculateViewMeasure(view);
        return view.getMeasuredHeight();
    }

    public static int getViewMeasuredWidth(View view) {
        calculateViewMeasure(view);
        return view.getMeasuredWidth();
    }

    public static void calculateViewMeasure(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
    }
}
