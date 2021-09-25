package com.freevisiontech.fvmobile.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView {
    private int downX;
    private int downY;
    /* access modifiers changed from: private */
    public Handler handler;
    /* access modifiers changed from: private */
    public int lastScrollY;
    private int mTouchSlop;
    /* access modifiers changed from: private */
    public OnScrollListener onScrollListener;

    public interface OnScrollListener {
        void onScroll(int i);
    }

    public MyScrollView(Context context) {
        this(context, (AttributeSet) null);
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.handler = new Handler() {
            public void handleMessage(Message msg) {
                int scrollY = MyScrollView.this.getScrollY();
                if (MyScrollView.this.lastScrollY != scrollY) {
                    int unused = MyScrollView.this.lastScrollY = scrollY;
                    MyScrollView.this.handler.sendMessageDelayed(MyScrollView.this.handler.obtainMessage(), 5);
                }
                if (MyScrollView.this.onScrollListener != null) {
                    MyScrollView.this.onScrollListener.onScroll(scrollY);
                }
            }
        };
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public void setOnScrollListener(OnScrollListener onScrollListener2) {
        this.onScrollListener = onScrollListener2;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (this.onScrollListener != null) {
            OnScrollListener onScrollListener2 = this.onScrollListener;
            int scrollY = getScrollY();
            this.lastScrollY = scrollY;
            onScrollListener2.onScroll(scrollY);
        }
        switch (ev.getAction()) {
            case 1:
                this.handler.sendMessageDelayed(this.handler.obtainMessage(), 5);
                break;
        }
        return super.onTouchEvent(ev);
    }

    public boolean onInterceptTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case 0:
                this.downX = (int) e.getRawX();
                this.downY = (int) e.getRawY();
                break;
            case 2:
                if (Math.abs(((int) e.getRawY()) - this.downY) > this.mTouchSlop) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(e);
    }
}
