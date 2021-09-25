package com.freevisiontech.utils;

import android.content.Context;
import android.content.Intent;
import android.view.OrientationEventListener;

public class ScreenOrientationUtil {
    public static final String BC_OrientationChanged = "com.freevison.Orientation";
    public static final String BC_OrientationChangedKey = "Orientation";
    public static final int Rotation_0 = 0;
    public static final int Rotation_180 = 180;
    public static final int Rotation_270 = 270;
    public static final int Rotation_90 = 90;
    private static ScreenOrientationUtil instance = new ScreenOrientationUtil();
    private Context mContext;
    private OrientationEventListener mOrEventListener;
    /* access modifiers changed from: private */
    public int mOrientation;
    /* access modifiers changed from: private */
    public ScreenOrientationListener mlistener = null;

    public interface ScreenOrientationListener {
        void onScreenOrientationChanged(int i);
    }

    public static ScreenOrientationUtil getInstance() {
        return instance;
    }

    public void start(Context context, ScreenOrientationListener listener) {
        this.mContext = context;
        this.mlistener = listener;
        if (this.mOrEventListener == null) {
            initListener();
        }
        this.mOrEventListener.enable();
    }

    public void stop() {
        if (this.mOrEventListener != null) {
            this.mOrEventListener.disable();
        }
    }

    private void initListener() {
        this.mOrEventListener = new OrientationEventListener(this.mContext) {
            public void onOrientationChanged(int rotation) {
                int orientation;
                if (rotation != -1) {
                    boolean isrefreshorientation = false;
                    switch (ScreenOrientationUtil.this.mOrientation) {
                        case 0:
                            if (rotation > 335 || rotation < 205) {
                                isrefreshorientation = true;
                                break;
                            }
                        case 90:
                            if (rotation > 65 || rotation < 295) {
                                isrefreshorientation = true;
                                break;
                            }
                        case 180:
                            if (rotation > 155 || rotation < 25) {
                                isrefreshorientation = true;
                                break;
                            }
                        case 270:
                            if (rotation > 245 || rotation < 115) {
                                isrefreshorientation = true;
                                break;
                            }
                    }
                    if (isrefreshorientation && (orientation = ScreenOrientationUtil.this.convert2Orientation(rotation)) != ScreenOrientationUtil.this.mOrientation) {
                        int unused = ScreenOrientationUtil.this.mOrientation = orientation;
                        ScreenOrientationUtil.this.sendOrientationChangedBroadCast();
                        if (ScreenOrientationUtil.this.mlistener != null) {
                            ScreenOrientationUtil.this.mlistener.onScreenOrientationChanged(ScreenOrientationUtil.this.mOrientation);
                        }
                    }
                }
            }
        };
    }

    public boolean isPortrait() {
        if (this.mOrientation == 90 || this.mOrientation == 270) {
            return true;
        }
        return false;
    }

    public boolean isLandScape() {
        if (this.mOrientation == 0 || this.mOrientation == 180) {
            return true;
        }
        return false;
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    /* access modifiers changed from: private */
    public int convert2Orientation(int rotation) {
        if ((rotation >= 0 && rotation <= 45) || rotation > 315) {
            return 90;
        }
        if (rotation > 45 && rotation <= 135) {
            return 180;
        }
        if (rotation > 135 && rotation <= 225) {
            return 270;
        }
        if (rotation <= 225 || rotation > 315) {
            return 90;
        }
        return 0;
    }

    /* access modifiers changed from: private */
    public void sendOrientationChangedBroadCast() {
        Intent intent = new Intent();
        intent.setAction(BC_OrientationChanged);
        intent.putExtra(BC_OrientationChangedKey, this.mOrientation);
        this.mContext.sendBroadcast(intent);
    }
}
