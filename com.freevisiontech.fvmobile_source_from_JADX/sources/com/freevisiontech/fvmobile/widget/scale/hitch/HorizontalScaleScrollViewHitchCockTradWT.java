package com.freevisiontech.fvmobile.widget.scale.hitch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utility.Util;

public class HorizontalScaleScrollViewHitchCockTradWT extends BaseScaleViewHitchCockTradWT {
    private int mEndColor = getResources().getColor(C0853R.color.white);
    private int mStartColor = getResources().getColor(C0853R.color.color_hitch_cock_mf_color);
    private VelocityTracker mTracker;
    boolean touchScale = true;
    int touchScaleMoveNums = 0;

    public HorizontalScaleScrollViewHitchCockTradWT(Context context) {
        super(context);
    }

    public HorizontalScaleScrollViewHitchCockTradWT(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalScaleScrollViewHitchCockTradWT(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public HorizontalScaleScrollViewHitchCockTradWT(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /* access modifiers changed from: protected */
    public void initVar() {
        this.mRectWidth = (this.mMax - this.mMin) * this.mScaleMargin;
        this.mRectHeight = this.mScaleHeight * 8;
        this.mScaleMaxHeight = this.mScaleHeight * 2;
        Log.e("-----------", "------------  mRectWidth: " + this.mRectWidth + "  ------ mRectHeight: " + this.mRectHeight);
        setLayoutParams(new ViewGroup.MarginLayoutParams(this.mRectWidth, this.mRectHeight));
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(this.mRectHeight, Integer.MIN_VALUE));
        this.mScaleScrollViewRange = getMeasuredWidth();
        this.mTempScale = ((this.mScaleScrollViewRange / this.mScaleMargin) / 2) + this.mMin;
        this.mMidCountScale = ((this.mScaleScrollViewRange / this.mScaleMargin) / 2) + this.mMin;
    }

    /* access modifiers changed from: protected */
    public void onDrawLine(Canvas canvas, Paint paint) {
        canvas.drawLine(0.0f, (float) this.mRectHeight, (float) this.mRectWidth, (float) this.mRectHeight, paint);
    }

    /* access modifiers changed from: protected */
    public void onDrawScale(Canvas canvas, Paint paint) {
        String systemModel = Util.getSystemModel();
        String manufacturer = Build.BRAND.toLowerCase();
        if ("SM-N9500".equals(systemModel) || "oneplus".equals(manufacturer)) {
            paint.setTextSize((float) (((this.mRectHeight / 4) * 3) / 4));
        } else {
            paint.setTextSize((float) (this.mRectHeight / 4));
        }
        paint.setStyle(Paint.Style.FILL);
        int k = this.mMin;
        for (int i = 0; i <= this.mMax - this.mMin; i++) {
            if (i % 10 == 0) {
                float k2 = (float) k;
                if ("0.0".equals(String.valueOf(k2 / 10.0f))) {
                    paint.setColor(this.mEndColor);
                    canvas.drawText(String.valueOf((k2 / 10.0f) / 2.0f), (float) (this.mScaleMargin * i), (float) ((this.mRectHeight - this.mScaleMaxHeight) - 20), paint);
                } else {
                    paint.setColor(this.mEndColor);
                    canvas.drawText(String.valueOf((k2 / 10.0f) / 2.0f), (float) (this.mScaleMargin * i), (float) ((this.mRectHeight - this.mScaleMaxHeight) - 20), paint);
                }
                k += 10;
            }
        }
    }

    public void setColor(int startColor, int endColor) {
        this.mStartColor = startColor;
        this.mEndColor = endColor;
        invalidate();
    }

    /* access modifiers changed from: protected */
    public void onDrawPointer(Canvas canvas, Paint paint) {
        paint.setColor(this.mStartColor);
        int countScale = (this.mScaleScrollViewRange / this.mScaleMargin) / 2;
        int finalX = this.mScroller.getFinalX();
        this.mCountScale = ((int) Math.rint(((double) finalX) / ((double) this.mScaleMargin))) + countScale + this.mMin;
        if (this.mScrollListener != null) {
            if (this.mCountScale % 10 == 0) {
                Log.e("---------------", "--------  mCountScale 111 ------ 当是整数 ");
                this.mScrollListener.onScaleScroll(this.mCountScale);
            }
            float k2 = (float) this.mCountScale;
            if (this.mCountScale % 10 == 0) {
                paint.setColor(this.mStartColor);
                if ("0.0".equals(String.valueOf(k2 / 10.0f))) {
                    canvas.drawText(String.valueOf((k2 / 10.0f) / 2.0f), (float) (this.mCountScale * this.mScaleMargin), (float) ((this.mRectHeight - this.mScaleMaxHeight) - 20), paint);
                } else {
                    canvas.drawText(String.valueOf((k2 / 10.0f) / 2.0f), (float) (this.mCountScale * this.mScaleMargin), (float) ((this.mRectHeight - this.mScaleMaxHeight) - 20), paint);
                }
                paint.setStrokeWidth(1.0f);
                Path path = new Path();
                path.moveTo((float) ((this.mScaleMargin * countScale) + finalX), (float) (((this.mRectHeight - 20) + 5) - 20));
                path.lineTo((float) (((this.mScaleMargin * countScale) + finalX) - 10), (float) ((((this.mRectHeight - 20) + 5) + 10) - 20));
                path.lineTo((float) ((this.mScaleMargin * countScale) + finalX + 10), (float) ((((this.mRectHeight - 20) + 5) + 10) - 20));
                paint.setStyle(Paint.Style.FILL);
                path.close();
                canvas.drawPath(path, paint);
                Path path2 = new Path();
                path2.moveTo((float) ((this.mScaleMargin * countScale) + finalX), (float) ((((this.mRectHeight - this.mScaleMaxHeight) - this.mScaleHeight) - 5) - 40));
                path2.lineTo((float) (((this.mScaleMargin * countScale) + finalX) - 10), (float) (((((this.mRectHeight - this.mScaleMaxHeight) - this.mScaleHeight) - 5) - 10) - 40));
                path2.lineTo((float) ((this.mScaleMargin * countScale) + finalX + 10), (float) (((((this.mRectHeight - this.mScaleMaxHeight) - this.mScaleHeight) - 5) - 10) - 40));
                paint.setStyle(Paint.Style.FILL);
                path2.close();
                canvas.drawPath(path2, paint);
            }
        }
    }

    public void scrollToScale(int val) {
        if (val >= this.mMin && val <= this.mMax) {
            smoothScrollBy((val - this.mCountScale) * this.mScaleMargin, 0);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        switch (event.getAction()) {
            case 0:
                if (this.mTracker == null) {
                    this.mTracker = VelocityTracker.obtain();
                } else {
                    this.mTracker.clear();
                }
                this.mTracker.addMovement(event);
                if (this.mScroller != null && !this.mScroller.isFinished()) {
                    this.mScroller.abortAnimation();
                }
                this.mScrollLastX = x;
                CameraUtils.setScaleScrollTouchStateShutter(true);
                Log.e("-----------", "-------- 6666 7777 8888 9999 shutter    按下手指   true  -------" + this.touchScaleMoveNums);
                int dataX2 = this.mScrollLastX - x;
                if (this.mCountScale - this.mTempScale < 0) {
                    if (this.mCountScale > this.mMin || dataX2 > 0) {
                        this.touchScale = true;
                        this.touchScaleMoveNums = 0;
                        return true;
                    }
                    this.touchScale = false;
                    this.touchScaleMoveNums = 1;
                    Log.e("------------", "--------   ------");
                    return true;
                } else if (this.mCountScale - this.mTempScale <= 0) {
                    return true;
                } else {
                    if (this.mCountScale < this.mMax || dataX2 < 0) {
                        this.touchScale = true;
                        this.touchScaleMoveNums = 0;
                        return true;
                    }
                    this.touchScale = false;
                    this.touchScaleMoveNums = 1;
                    return true;
                }
            case 1:
                if (this.mTracker != null) {
                    this.mTracker.recycle();
                    this.mTracker = null;
                }
                CameraUtils.setScaleScrollTouchStateShutter(true);
                Log.e("-----------", "-------- 6666 7777 8888 9999 shutter    抬起手指  true  -------" + this.touchScaleMoveNums);
                if (this.mCountScale < this.mMin) {
                    this.mCountScale = this.mMin;
                }
                if (this.mCountScale > this.mMax) {
                    this.mCountScale = this.mMax;
                }
                if (this.mCountScale < 0) {
                    this.mCountScale = 0;
                } else if (this.mCountScale > 460) {
                    this.mCountScale = 460;
                }
                Log.e("-----------", "---------  指针位置   ------ " + ((this.mCountScale - this.mMidCountScale) * this.mScaleMargin) + "  " + (this.mCountScale - this.mMidCountScale) + "     中心位置:" + this.mMidCountScale + "   现在的位置:" + this.mCountScale);
                if (this.mCountScale % 10 < 5) {
                    this.mCountScale -= this.mCountScale % 10;
                } else {
                    this.mCountScale += 10 - (this.mCountScale % 10);
                }
                this.mScroller.setFinalX((this.mCountScale - this.mMidCountScale) * this.mScaleMargin);
                postInvalidate();
                this.touchScale = true;
                this.touchScaleMoveNums = 0;
                CameraUtils.setScaleScrollTouchStateShutter(true);
                return true;
            case 2:
                this.mTracker.addMovement(event);
                this.mTracker.computeCurrentVelocity(1000);
                this.mTracker.computeCurrentVelocity(1000);
                int xSpeed = (int) Math.abs(this.mTracker.getXVelocity());
                int ySpeed = (int) Math.abs(this.mTracker.getYVelocity());
                Log.e("-----------", "-------- 6666 7777 8888 9999 shutter ---- xSpeed:" + xSpeed + "  ySpeed:" + ySpeed);
                int dataX = this.mScrollLastX - x;
                if (this.mCountScale - this.mTempScale < 0) {
                    if (this.mCountScale <= this.mMin && dataX <= 0) {
                        return super.onTouchEvent(event);
                    }
                } else if (this.mCountScale - this.mTempScale > 0 && this.mCountScale >= this.mMax && dataX >= 0) {
                    return super.onTouchEvent(event);
                }
                smoothScrollBy(dataX, 0);
                this.mScrollLastX = x;
                postInvalidate();
                this.mTempScale = this.mCountScale;
                this.touchScale = false;
                this.touchScaleMoveNums++;
                CameraUtils.setScaleScrollTouchStateShutter(false);
                if (xSpeed != 0 || ySpeed == 0) {
                }
                Log.e("-----------", "-------- 6666 7777 8888 9999 shutter    移动手指   false  -------" + this.touchScaleMoveNums);
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }
}
