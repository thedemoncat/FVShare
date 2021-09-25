package com.freevisiontech.fvmobile.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

public class FVDropDownPop {
    /* access modifiers changed from: private */
    public int mAnimationStyle = -1;
    /* access modifiers changed from: private */
    public boolean mClippEnable = true;
    /* access modifiers changed from: private */
    public View mContentView;
    private Context mContext;
    /* access modifiers changed from: private */
    public int mHeight;
    /* access modifiers changed from: private */
    public boolean mIgnoreCheekPress = false;
    /* access modifiers changed from: private */
    public int mInputMode = -1;
    /* access modifiers changed from: private */
    public boolean mIsFocusable = true;
    /* access modifiers changed from: private */
    public boolean mIsOutside = true;
    /* access modifiers changed from: private */
    public PopupWindow.OnDismissListener mOnDismissListener;
    /* access modifiers changed from: private */
    public View.OnTouchListener mOnTouchListener;
    private PopupWindow mPopupWindow;
    /* access modifiers changed from: private */
    public int mResLayoutId = -1;
    /* access modifiers changed from: private */
    public int mSoftInputMode = -1;
    /* access modifiers changed from: private */
    public boolean mTouchable = true;
    /* access modifiers changed from: private */
    public int mWidth;

    public FVDropDownPop(Context context) {
        this.mContext = context;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public FVDropDownPop showAsDropDown(View anchor, int xOff, int yOff) {
        if (this.mPopupWindow != null) {
            this.mPopupWindow.showAsDropDown(anchor, xOff, yOff);
        }
        return this;
    }

    public FVDropDownPop showAsDropDown(View anchor) {
        if (this.mPopupWindow != null) {
            this.mPopupWindow.showAsDropDown(anchor);
        }
        return this;
    }

    @RequiresApi(api = 19)
    public FVDropDownPop showAsDropDown(View anchor, int xOff, int yOff, int gravity) {
        if (this.mPopupWindow != null) {
            this.mPopupWindow.showAsDropDown(anchor, xOff, yOff, gravity);
        }
        return this;
    }

    public FVDropDownPop showAtLocation(View parent, int gravity, int x, int y) {
        if (this.mPopupWindow != null) {
            this.mPopupWindow.showAsDropDown(parent, gravity, x, y);
        }
        return this;
    }

    public void apply(PopupWindow popupWindow) {
        popupWindow.setClippingEnabled(this.mClippEnable);
        if (this.mIgnoreCheekPress) {
            popupWindow.setIgnoreCheekPress();
        }
        if (this.mInputMode != -1) {
            popupWindow.setInputMethodMode(this.mInputMode);
        }
        if (this.mSoftInputMode != -1) {
            popupWindow.setSoftInputMode(this.mSoftInputMode);
        }
        if (this.mOnDismissListener != null) {
            popupWindow.setOnDismissListener(this.mOnDismissListener);
        }
        if (this.mOnTouchListener != null) {
            popupWindow.setTouchInterceptor(this.mOnTouchListener);
        }
        popupWindow.setTouchable(this.mTouchable);
    }

    /* access modifiers changed from: private */
    public PopupWindow build() {
        if (this.mContentView == null) {
            this.mContentView = LayoutInflater.from(this.mContext).inflate(this.mResLayoutId, (ViewGroup) null);
        }
        if (this.mWidth == 0 || this.mHeight == 0) {
            this.mPopupWindow = new PopupWindow(this.mContentView, -2, -2);
        } else {
            this.mPopupWindow = new PopupWindow(this.mContentView, this.mWidth, this.mHeight);
        }
        if (this.mAnimationStyle != -1) {
            this.mPopupWindow.setAnimationStyle(this.mAnimationStyle);
        }
        apply(this.mPopupWindow);
        this.mPopupWindow.setFocusable(this.mIsFocusable);
        this.mPopupWindow.setBackgroundDrawable(new ColorDrawable(0));
        this.mPopupWindow.setOutsideTouchable(this.mIsOutside);
        if (this.mWidth == 0 || this.mHeight == 0) {
            this.mPopupWindow.getContentView().measure(0, 0);
            this.mWidth = this.mPopupWindow.getContentView().getMeasuredWidth();
            this.mHeight = this.mPopupWindow.getContentView().getMeasuredHeight();
        }
        this.mPopupWindow.update();
        return this.mPopupWindow;
    }

    public void dismiss() {
        if (this.mPopupWindow != null) {
            this.mPopupWindow.dismiss();
        }
    }

    public static class PopupWindowBuilder {
        private FVDropDownPop mFVDropDownPop;

        public PopupWindowBuilder(Context context) {
            this.mFVDropDownPop = new FVDropDownPop(context);
        }

        public PopupWindowBuilder size(int width, int height) {
            int unused = this.mFVDropDownPop.mWidth = width;
            int unused2 = this.mFVDropDownPop.mHeight = height;
            return this;
        }

        public PopupWindowBuilder setFocusable(boolean focusable) {
            boolean unused = this.mFVDropDownPop.mIsFocusable = focusable;
            return this;
        }

        public PopupWindowBuilder setView(int resLayoutId) {
            int unused = this.mFVDropDownPop.mResLayoutId = resLayoutId;
            View unused2 = this.mFVDropDownPop.mContentView = null;
            return this;
        }

        public PopupWindowBuilder setView(View view) {
            View unused = this.mFVDropDownPop.mContentView = view;
            int unused2 = this.mFVDropDownPop.mResLayoutId = -1;
            return this;
        }

        public PopupWindowBuilder setOutsideTouchable(boolean outsideTouchable) {
            boolean unused = this.mFVDropDownPop.mIsOutside = outsideTouchable;
            return this;
        }

        public PopupWindowBuilder setAnimationStyle(int animationStyle) {
            int unused = this.mFVDropDownPop.mAnimationStyle = animationStyle;
            return this;
        }

        public PopupWindowBuilder setClippingEnable(boolean enable) {
            boolean unused = this.mFVDropDownPop.mClippEnable = enable;
            return this;
        }

        public PopupWindowBuilder setIgnoreCheckPress(boolean ignoreCheckPress) {
            boolean unused = this.mFVDropDownPop.mIgnoreCheekPress = ignoreCheckPress;
            return this;
        }

        public PopupWindowBuilder setInputMethoudMode(int mode) {
            int unused = this.mFVDropDownPop.mInputMode = mode;
            return this;
        }

        public PopupWindowBuilder setOnDissmissListener(PopupWindow.OnDismissListener onDissmissListener) {
            PopupWindow.OnDismissListener unused = this.mFVDropDownPop.mOnDismissListener = onDissmissListener;
            return this;
        }

        public PopupWindowBuilder setSoftInputMode(int softInputMode) {
            int unused = this.mFVDropDownPop.mSoftInputMode = softInputMode;
            return this;
        }

        public PopupWindowBuilder setTouchable(boolean touchable) {
            boolean unused = this.mFVDropDownPop.mTouchable = touchable;
            return this;
        }

        public PopupWindowBuilder setTouchIntercepter(View.OnTouchListener touchIntercepter) {
            View.OnTouchListener unused = this.mFVDropDownPop.mOnTouchListener = touchIntercepter;
            return this;
        }

        public FVDropDownPop creat() {
            PopupWindow unused = this.mFVDropDownPop.build();
            return this.mFVDropDownPop;
        }
    }
}
