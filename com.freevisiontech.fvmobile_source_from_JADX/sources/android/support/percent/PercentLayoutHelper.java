package android.support.percent;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.p001v4.view.MarginLayoutParamsCompat;
import android.support.p001v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class PercentLayoutHelper {
    private static final String TAG = "PercentLayout";
    private final ViewGroup mHost;

    public interface PercentLayoutParams {
        PercentLayoutInfo getPercentLayoutInfo();
    }

    public PercentLayoutHelper(ViewGroup host) {
        this.mHost = host;
    }

    public static void fetchWidthAndHeight(ViewGroup.LayoutParams params, TypedArray array, int widthAttr, int heightAttr) {
        params.width = array.getLayoutDimension(widthAttr, 0);
        params.height = array.getLayoutDimension(heightAttr, 0);
    }

    public void adjustChildren(int widthMeasureSpec, int heightMeasureSpec) {
        if (Log.isLoggable(TAG, 3)) {
            Log.d(TAG, "adjustChildren: " + this.mHost + " widthMeasureSpec: " + View.MeasureSpec.toString(widthMeasureSpec) + " heightMeasureSpec: " + View.MeasureSpec.toString(heightMeasureSpec));
        }
        int widthHint = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightHint = View.MeasureSpec.getSize(heightMeasureSpec);
        int N = this.mHost.getChildCount();
        for (int i = 0; i < N; i++) {
            View view = this.mHost.getChildAt(i);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (Log.isLoggable(TAG, 3)) {
                Log.d(TAG, "should adjust " + view + " " + params);
            }
            if (params instanceof PercentLayoutParams) {
                PercentLayoutInfo info = ((PercentLayoutParams) params).getPercentLayoutInfo();
                if (Log.isLoggable(TAG, 3)) {
                    Log.d(TAG, "using " + info);
                }
                if (info != null) {
                    if (params instanceof ViewGroup.MarginLayoutParams) {
                        info.fillMarginLayoutParams((ViewGroup.MarginLayoutParams) params, widthHint, heightHint);
                    } else {
                        info.fillLayoutParams(params, widthHint, heightHint);
                    }
                }
            }
        }
    }

    public static PercentLayoutInfo getPercentLayoutInfo(Context context, AttributeSet attrs) {
        PercentLayoutInfo info = null;
        TypedArray array = context.obtainStyledAttributes(attrs, C0770R.styleable.PercentLayout_Layout);
        float value = array.getFraction(C0770R.styleable.PercentLayout_Layout_layout_widthPercent, 1, 1, -1.0f);
        if (value != -1.0f) {
            if (Log.isLoggable(TAG, 2)) {
                Log.v(TAG, "percent width: " + value);
            }
            if (0 == 0) {
                info = new PercentLayoutInfo();
            }
            info.widthPercent = value;
        }
        float value2 = array.getFraction(C0770R.styleable.PercentLayout_Layout_layout_heightPercent, 1, 1, -1.0f);
        if (value2 != -1.0f) {
            if (Log.isLoggable(TAG, 2)) {
                Log.v(TAG, "percent height: " + value2);
            }
            if (info == null) {
                info = new PercentLayoutInfo();
            }
            info.heightPercent = value2;
        }
        float value3 = array.getFraction(C0770R.styleable.PercentLayout_Layout_layout_marginPercent, 1, 1, -1.0f);
        if (value3 != -1.0f) {
            if (Log.isLoggable(TAG, 2)) {
                Log.v(TAG, "percent margin: " + value3);
            }
            if (info == null) {
                info = new PercentLayoutInfo();
            }
            info.leftMarginPercent = value3;
            info.topMarginPercent = value3;
            info.rightMarginPercent = value3;
            info.bottomMarginPercent = value3;
        }
        float value4 = array.getFraction(C0770R.styleable.PercentLayout_Layout_layout_marginLeftPercent, 1, 1, -1.0f);
        if (value4 != -1.0f) {
            if (Log.isLoggable(TAG, 2)) {
                Log.v(TAG, "percent left margin: " + value4);
            }
            if (info == null) {
                info = new PercentLayoutInfo();
            }
            info.leftMarginPercent = value4;
        }
        float value5 = array.getFraction(C0770R.styleable.PercentLayout_Layout_layout_marginTopPercent, 1, 1, -1.0f);
        if (value5 != -1.0f) {
            if (Log.isLoggable(TAG, 2)) {
                Log.v(TAG, "percent top margin: " + value5);
            }
            if (info == null) {
                info = new PercentLayoutInfo();
            }
            info.topMarginPercent = value5;
        }
        float value6 = array.getFraction(C0770R.styleable.PercentLayout_Layout_layout_marginRightPercent, 1, 1, -1.0f);
        if (value6 != -1.0f) {
            if (Log.isLoggable(TAG, 2)) {
                Log.v(TAG, "percent right margin: " + value6);
            }
            if (info == null) {
                info = new PercentLayoutInfo();
            }
            info.rightMarginPercent = value6;
        }
        float value7 = array.getFraction(C0770R.styleable.PercentLayout_Layout_layout_marginBottomPercent, 1, 1, -1.0f);
        if (value7 != -1.0f) {
            if (Log.isLoggable(TAG, 2)) {
                Log.v(TAG, "percent bottom margin: " + value7);
            }
            if (info == null) {
                info = new PercentLayoutInfo();
            }
            info.bottomMarginPercent = value7;
        }
        float value8 = array.getFraction(C0770R.styleable.PercentLayout_Layout_layout_marginStartPercent, 1, 1, -1.0f);
        if (value8 != -1.0f) {
            if (Log.isLoggable(TAG, 2)) {
                Log.v(TAG, "percent start margin: " + value8);
            }
            if (info == null) {
                info = new PercentLayoutInfo();
            }
            info.startMarginPercent = value8;
        }
        float value9 = array.getFraction(C0770R.styleable.PercentLayout_Layout_layout_marginEndPercent, 1, 1, -1.0f);
        if (value9 != -1.0f) {
            if (Log.isLoggable(TAG, 2)) {
                Log.v(TAG, "percent end margin: " + value9);
            }
            if (info == null) {
                info = new PercentLayoutInfo();
            }
            info.endMarginPercent = value9;
        }
        array.recycle();
        if (Log.isLoggable(TAG, 3)) {
            Log.d(TAG, "constructed: " + info);
        }
        return info;
    }

    public void restoreOriginalParams() {
        int N = this.mHost.getChildCount();
        for (int i = 0; i < N; i++) {
            View view = this.mHost.getChildAt(i);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (Log.isLoggable(TAG, 3)) {
                Log.d(TAG, "should restore " + view + " " + params);
            }
            if (params instanceof PercentLayoutParams) {
                PercentLayoutInfo info = ((PercentLayoutParams) params).getPercentLayoutInfo();
                if (Log.isLoggable(TAG, 3)) {
                    Log.d(TAG, "using " + info);
                }
                if (info != null) {
                    if (params instanceof ViewGroup.MarginLayoutParams) {
                        info.restoreMarginLayoutParams((ViewGroup.MarginLayoutParams) params);
                    } else {
                        info.restoreLayoutParams(params);
                    }
                }
            }
        }
    }

    public boolean handleMeasuredStateTooSmall() {
        PercentLayoutInfo info;
        boolean needsSecondMeasure = false;
        int N = this.mHost.getChildCount();
        for (int i = 0; i < N; i++) {
            View view = this.mHost.getChildAt(i);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (Log.isLoggable(TAG, 3)) {
                Log.d(TAG, "should handle measured state too small " + view + " " + params);
            }
            if ((params instanceof PercentLayoutParams) && (info = ((PercentLayoutParams) params).getPercentLayoutInfo()) != null) {
                if (shouldHandleMeasuredWidthTooSmall(view, info)) {
                    needsSecondMeasure = true;
                    params.width = -2;
                }
                if (shouldHandleMeasuredHeightTooSmall(view, info)) {
                    needsSecondMeasure = true;
                    params.height = -2;
                }
            }
        }
        if (Log.isLoggable(TAG, 3)) {
            Log.d(TAG, "should trigger second measure pass: " + needsSecondMeasure);
        }
        return needsSecondMeasure;
    }

    private static boolean shouldHandleMeasuredWidthTooSmall(View view, PercentLayoutInfo info) {
        return (ViewCompat.getMeasuredWidthAndState(view) & -16777216) == 16777216 && info.widthPercent >= 0.0f && info.mPreservedParams.width == -2;
    }

    private static boolean shouldHandleMeasuredHeightTooSmall(View view, PercentLayoutInfo info) {
        return (ViewCompat.getMeasuredHeightAndState(view) & -16777216) == 16777216 && info.heightPercent >= 0.0f && info.mPreservedParams.height == -2;
    }

    public static class PercentLayoutInfo {
        public float bottomMarginPercent = -1.0f;
        public float endMarginPercent = -1.0f;
        public float heightPercent = -1.0f;
        public float leftMarginPercent = -1.0f;
        final ViewGroup.MarginLayoutParams mPreservedParams = new ViewGroup.MarginLayoutParams(0, 0);
        public float rightMarginPercent = -1.0f;
        public float startMarginPercent = -1.0f;
        public float topMarginPercent = -1.0f;
        public float widthPercent = -1.0f;

        public void fillLayoutParams(ViewGroup.LayoutParams params, int widthHint, int heightHint) {
            this.mPreservedParams.width = params.width;
            this.mPreservedParams.height = params.height;
            if (this.widthPercent >= 0.0f) {
                params.width = (int) (((float) widthHint) * this.widthPercent);
            }
            if (this.heightPercent >= 0.0f) {
                params.height = (int) (((float) heightHint) * this.heightPercent);
            }
            if (Log.isLoggable(PercentLayoutHelper.TAG, 3)) {
                Log.d(PercentLayoutHelper.TAG, "after fillLayoutParams: (" + params.width + ", " + params.height + ")");
            }
        }

        public void fillMarginLayoutParams(ViewGroup.MarginLayoutParams params, int widthHint, int heightHint) {
            fillLayoutParams(params, widthHint, heightHint);
            this.mPreservedParams.leftMargin = params.leftMargin;
            this.mPreservedParams.topMargin = params.topMargin;
            this.mPreservedParams.rightMargin = params.rightMargin;
            this.mPreservedParams.bottomMargin = params.bottomMargin;
            MarginLayoutParamsCompat.setMarginStart(this.mPreservedParams, MarginLayoutParamsCompat.getMarginStart(params));
            MarginLayoutParamsCompat.setMarginEnd(this.mPreservedParams, MarginLayoutParamsCompat.getMarginEnd(params));
            if (this.leftMarginPercent >= 0.0f) {
                params.leftMargin = (int) (((float) widthHint) * this.leftMarginPercent);
            }
            if (this.topMarginPercent >= 0.0f) {
                params.topMargin = (int) (((float) heightHint) * this.topMarginPercent);
            }
            if (this.rightMarginPercent >= 0.0f) {
                params.rightMargin = (int) (((float) widthHint) * this.rightMarginPercent);
            }
            if (this.bottomMarginPercent >= 0.0f) {
                params.bottomMargin = (int) (((float) heightHint) * this.bottomMarginPercent);
            }
            if (this.startMarginPercent >= 0.0f) {
                MarginLayoutParamsCompat.setMarginStart(params, (int) (((float) widthHint) * this.startMarginPercent));
            }
            if (this.endMarginPercent >= 0.0f) {
                MarginLayoutParamsCompat.setMarginEnd(params, (int) (((float) widthHint) * this.endMarginPercent));
            }
            if (Log.isLoggable(PercentLayoutHelper.TAG, 3)) {
                Log.d(PercentLayoutHelper.TAG, "after fillMarginLayoutParams: (" + params.width + ", " + params.height + ")");
            }
        }

        public String toString() {
            return String.format("PercentLayoutInformation width: %f height %f, margins (%f, %f,  %f, %f, %f, %f)", new Object[]{Float.valueOf(this.widthPercent), Float.valueOf(this.heightPercent), Float.valueOf(this.leftMarginPercent), Float.valueOf(this.topMarginPercent), Float.valueOf(this.rightMarginPercent), Float.valueOf(this.bottomMarginPercent), Float.valueOf(this.startMarginPercent), Float.valueOf(this.endMarginPercent)});
        }

        public void restoreMarginLayoutParams(ViewGroup.MarginLayoutParams params) {
            restoreLayoutParams(params);
            params.leftMargin = this.mPreservedParams.leftMargin;
            params.topMargin = this.mPreservedParams.topMargin;
            params.rightMargin = this.mPreservedParams.rightMargin;
            params.bottomMargin = this.mPreservedParams.bottomMargin;
            MarginLayoutParamsCompat.setMarginStart(params, MarginLayoutParamsCompat.getMarginStart(this.mPreservedParams));
            MarginLayoutParamsCompat.setMarginEnd(params, MarginLayoutParamsCompat.getMarginEnd(this.mPreservedParams));
        }

        public void restoreLayoutParams(ViewGroup.LayoutParams params) {
            params.width = this.mPreservedParams.width;
            params.height = this.mPreservedParams.height;
        }
    }
}
