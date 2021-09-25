package com.yalantis.ucrop.util;

import android.support.annotation.NonNull;
import android.view.MotionEvent;

public class RotationGestureDetector {
    private static final int INVALID_POINTER_INDEX = -1;

    /* renamed from: fX */
    private float f1158fX;

    /* renamed from: fY */
    private float f1159fY;
    private float mAngle;
    private boolean mIsFirstTouch;
    private OnRotationGestureListener mListener;
    private int mPointerIndex1 = -1;
    private int mPointerIndex2 = -1;

    /* renamed from: sX */
    private float f1160sX;

    /* renamed from: sY */
    private float f1161sY;

    public interface OnRotationGestureListener {
        boolean onRotation(RotationGestureDetector rotationGestureDetector);
    }

    public RotationGestureDetector(OnRotationGestureListener listener) {
        this.mListener = listener;
    }

    public float getAngle() {
        return this.mAngle;
    }

    public boolean onTouchEvent(@NonNull MotionEvent event) {
        switch (event.getActionMasked()) {
            case 0:
                this.f1160sX = event.getX();
                this.f1161sY = event.getY();
                this.mPointerIndex1 = event.findPointerIndex(event.getPointerId(0));
                this.mAngle = 0.0f;
                this.mIsFirstTouch = true;
                break;
            case 1:
                this.mPointerIndex1 = -1;
                break;
            case 2:
                if (!(this.mPointerIndex1 == -1 || this.mPointerIndex2 == -1 || event.getPointerCount() <= this.mPointerIndex2)) {
                    float nsX = event.getX(this.mPointerIndex1);
                    float nsY = event.getY(this.mPointerIndex1);
                    float nfX = event.getX(this.mPointerIndex2);
                    float nfY = event.getY(this.mPointerIndex2);
                    if (this.mIsFirstTouch) {
                        this.mAngle = 0.0f;
                        this.mIsFirstTouch = false;
                    } else {
                        calculateAngleBetweenLines(this.f1158fX, this.f1159fY, this.f1160sX, this.f1161sY, nfX, nfY, nsX, nsY);
                    }
                    if (this.mListener != null) {
                        this.mListener.onRotation(this);
                    }
                    this.f1158fX = nfX;
                    this.f1159fY = nfY;
                    this.f1160sX = nsX;
                    this.f1161sY = nsY;
                    break;
                }
            case 5:
                this.f1158fX = event.getX();
                this.f1159fY = event.getY();
                this.mPointerIndex2 = event.findPointerIndex(event.getPointerId(event.getActionIndex()));
                this.mAngle = 0.0f;
                this.mIsFirstTouch = true;
                break;
            case 6:
                this.mPointerIndex2 = -1;
                break;
        }
        return true;
    }

    private float calculateAngleBetweenLines(float fx1, float fy1, float fx2, float fy2, float sx1, float sy1, float sx2, float sy2) {
        return calculateAngleDelta((float) Math.toDegrees((double) ((float) Math.atan2((double) (fy1 - fy2), (double) (fx1 - fx2)))), (float) Math.toDegrees((double) ((float) Math.atan2((double) (sy1 - sy2), (double) (sx1 - sx2)))));
    }

    private float calculateAngleDelta(float angleFrom, float angleTo) {
        this.mAngle = (angleTo % 360.0f) - (angleFrom % 360.0f);
        if (this.mAngle < -180.0f) {
            this.mAngle += 360.0f;
        } else if (this.mAngle > 180.0f) {
            this.mAngle -= 360.0f;
        }
        return this.mAngle;
    }

    public static class SimpleOnRotationGestureListener implements OnRotationGestureListener {
        public boolean onRotation(RotationGestureDetector rotationDetector) {
            return false;
        }
    }
}
