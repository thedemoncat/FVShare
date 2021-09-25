package com.freevisiontech.fvmobile.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.percent.PercentLayoutHelper;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class PercentLinearLayout extends LinearLayout {
    private PercentLayoutHelper mPercentLayoutHelper = new PercentLayoutHelper(this);

    public PercentLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.mPercentLayoutHelper.adjustChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (this.mPercentLayoutHelper.handleMeasuredStateTooSmall()) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.mPercentLayoutHelper.restoreOriginalParams();
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    public static class LayoutParams extends LinearLayout.LayoutParams implements PercentLayoutHelper.PercentLayoutParams {
        private PercentLayoutHelper.PercentLayoutInfo mPercentLayoutInfo;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            this.mPercentLayoutInfo = PercentLayoutHelper.getPercentLayoutInfo(c, attrs);
        }

        public PercentLayoutHelper.PercentLayoutInfo getPercentLayoutInfo() {
            return this.mPercentLayoutInfo;
        }

        /* access modifiers changed from: protected */
        public void setBaseAttributes(TypedArray a, int widthAttr, int heightAttr) {
            PercentLayoutHelper.fetchWidthAndHeight(this, a, widthAttr, heightAttr);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
        }
    }
}
