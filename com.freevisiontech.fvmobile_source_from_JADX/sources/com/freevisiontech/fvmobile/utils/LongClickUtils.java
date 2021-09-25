package com.freevisiontech.fvmobile.utils;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

public class LongClickUtils {
    private static final String TAG = "LongClickUtils";

    public static void setLongClick(Handler handlerLong, View longClickView, long delayMillis, View.OnLongClickListener longClickListener) {
        final Handler handler = handlerLong;
        final long j = delayMillis;
        final View.OnLongClickListener onLongClickListener = longClickListener;
        final View view = longClickView;
        longClickView.setOnTouchListener(new View.OnTouchListener() {
            private int TOUCH_MAX = 50;
            private int mLastMotionX;
            private int mLastMotionY;

            /* renamed from: r */
            private Runnable f1101r = new Runnable() {
                public void run() {
                    if (onLongClickListener != null) {
                        onLongClickListener.onLongClick(view);
                    }
                }
            };

            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                switch (event.getAction()) {
                    case 0:
                        handler.removeCallbacks(this.f1101r);
                        this.mLastMotionX = x;
                        this.mLastMotionY = y;
                        handler.postDelayed(this.f1101r, j);
                        return true;
                    case 1:
                        handler.removeCallbacks(this.f1101r);
                        return true;
                    case 2:
                        if (Math.abs(this.mLastMotionX - x) <= this.TOUCH_MAX && Math.abs(this.mLastMotionY - y) <= this.TOUCH_MAX) {
                            return true;
                        }
                        handler.removeCallbacks(this.f1101r);
                        return true;
                    default:
                        return true;
                }
            }
        });
    }
}
