package android.support.design.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.p003v7.appcompat.C0777R;

class ThemeUtils {
    private static final int[] APPCOMPAT_CHECK_ATTRS = {C0777R.attr.colorPrimary};

    ThemeUtils() {
    }

    static void checkAppCompatTheme(Context context) {
        boolean failed = false;
        TypedArray a = context.obtainStyledAttributes(APPCOMPAT_CHECK_ATTRS);
        if (!a.hasValue(0)) {
            failed = true;
        }
        a.recycle();
        if (failed) {
            throw new IllegalArgumentException("You need to use a Theme.AppCompat theme (or descendant) with the design library.");
        }
    }
}
