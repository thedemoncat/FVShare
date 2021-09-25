package android.support.p003v7.widget;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

/* renamed from: android.support.v7.widget.TooltipCompat */
public class TooltipCompat {
    private static final ViewCompatImpl IMPL;

    /* renamed from: android.support.v7.widget.TooltipCompat$ViewCompatImpl */
    private interface ViewCompatImpl {
        void setTooltipText(@NonNull View view, @Nullable CharSequence charSequence);
    }

    /* renamed from: android.support.v7.widget.TooltipCompat$BaseViewCompatImpl */
    private static class BaseViewCompatImpl implements ViewCompatImpl {
        private BaseViewCompatImpl() {
        }

        public void setTooltipText(@NonNull View view, @Nullable CharSequence tooltipText) {
            TooltipCompatHandler.setTooltipText(view, tooltipText);
        }
    }

    @TargetApi(26)
    /* renamed from: android.support.v7.widget.TooltipCompat$Api26ViewCompatImpl */
    private static class Api26ViewCompatImpl implements ViewCompatImpl {
        private Api26ViewCompatImpl() {
        }

        public void setTooltipText(@NonNull View view, @Nullable CharSequence tooltipText) {
            view.setTooltipText(tooltipText);
        }
    }

    static {
        if (Build.VERSION.SDK_INT >= 26) {
            IMPL = new Api26ViewCompatImpl();
        } else {
            IMPL = new BaseViewCompatImpl();
        }
    }

    public static void setTooltipText(@NonNull View view, @Nullable CharSequence tooltipText) {
        IMPL.setTooltipText(view, tooltipText);
    }

    private TooltipCompat() {
    }
}
