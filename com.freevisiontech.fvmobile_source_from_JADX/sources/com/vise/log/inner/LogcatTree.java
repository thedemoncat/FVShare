package com.vise.log.inner;

import android.util.Log;

public class LogcatTree extends Tree {
    /* access modifiers changed from: protected */
    public void log(int type, String tag, String message) {
        switch (type) {
            case 2:
                Log.v(tag, message);
                return;
            case 3:
                Log.d(tag, message);
                return;
            case 4:
                Log.i(tag, message);
                return;
            case 5:
                Log.w(tag, message);
                return;
            case 6:
                Log.e(tag, message);
                return;
            case 7:
                Log.wtf(tag, message);
                return;
            default:
                return;
        }
    }
}
