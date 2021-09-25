package com.freevisiontech.fvmobile.widget.scale;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.model.resolver.CompanyIdentifierResolver;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utility.Util;

public class HorizontalScaleScrollViewIsoTwo extends BaseScaleViewIsoTwo {
    private int mEndColor = getResources().getColor(C0853R.color.white);
    private int mStartColor = getResources().getColor(C0853R.color.color_shutter_iso_color);
    boolean touchScale = true;
    int touchScaleMoveNums = 0;

    public HorizontalScaleScrollViewIsoTwo(Context context) {
        super(context);
    }

    public HorizontalScaleScrollViewIsoTwo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalScaleScrollViewIsoTwo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public HorizontalScaleScrollViewIsoTwo(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
        if ("SM-N9500".equals(Util.getSystemModel())) {
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
                    canvas.drawText("AUTO", (float) (this.mScaleMargin * i), (float) ((this.mRectHeight - this.mScaleMaxHeight) - 20), paint);
                } else {
                    paint.setColor(this.mEndColor);
                    canvas.drawText(String.valueOf(getDrawScaleISOPaint((int) k2)), (float) (this.mScaleMargin * i), (float) ((this.mRectHeight - this.mScaleMaxHeight) - 20), paint);
                }
                k += 10;
            }
        }
    }

    public static int getDrawScaleISOPaint(int k2) {
        int i = k2;
        return Integer.parseInt(CameraUtils.getScaleIsoList().get((k2 / 10) - 1));
    }

    public void setColor(int startColor, int endColor) {
        this.mStartColor = startColor;
        this.mEndColor = endColor;
        invalidate();
    }

    /* access modifiers changed from: protected */
    public void onDrawPointer(Canvas canvas, Paint paint) {
        paint.setColor(this.mEndColor);
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
                    canvas.drawText("AUTO", (float) (this.mCountScale * this.mScaleMargin), (float) ((this.mRectHeight - this.mScaleMaxHeight) - 20), paint);
                } else {
                    canvas.drawText(String.valueOf(getDrawScaleISOPaint((int) k2)), (float) (this.mCountScale * this.mScaleMargin), (float) ((this.mRectHeight - this.mScaleMaxHeight) - 20), paint);
                }
                paint.setStyle(Paint.Style.FILL);
                canvas.drawCircle((float) ((this.mScaleMargin * countScale) + finalX), (float) (this.mRectHeight - 20), 4.0f, paint);
            }
        }
    }

    public void scrollToScale(int val) {
        if (val >= this.mMin && val <= this.mMax) {
            smoothScrollBy((val - this.mCountScale) * this.mScaleMargin, 0);
        }
    }

    public void scrollToScaleFirst(int val) {
        Log.e("-----------", "--------  8888  mMidCountScale ISO ----  : " + this.mMidCountScale);
        smoothScrollBy((val - this.mCountScale) * this.mScaleMargin, 0);
    }

    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        switch (event.getAction()) {
            case 0:
                if (this.mScroller != null && !this.mScroller.isFinished()) {
                    this.mScroller.abortAnimation();
                }
                this.mScrollLastX = x;
                Log.e("------------", "------- x x x   x x x  ----- " + x);
                CameraUtils.setScaleScrollTouchStateISO(true);
                int dataX2 = this.mScrollLastX - x;
                if (this.mCountScale - this.mTempScale < 0) {
                    if (this.mCountScale > this.mMin || dataX2 > 0) {
                        this.touchScale = true;
                        this.touchScaleMoveNums = 0;
                    } else {
                        this.touchScale = false;
                        this.touchScaleMoveNums = 1;
                        Log.e("------------", "--------   ------");
                    }
                } else if (this.mCountScale - this.mTempScale > 0) {
                    if (this.mCountScale < this.mMax || dataX2 < 0) {
                        this.touchScale = true;
                        this.touchScaleMoveNums = 0;
                    } else {
                        this.touchScale = false;
                        this.touchScaleMoveNums = 1;
                    }
                }
                Log.e("-------------", "---------3 down -----" + this.touchScaleMoveNums);
                return true;
            case 1:
                CameraUtils.setScaleScrollTouchStateISO(true);
                if (this.mCountScale < this.mMin) {
                    this.mCountScale = this.mMin;
                }
                if (this.mCountScale > this.mMax) {
                    this.mCountScale = this.mMax;
                }
                if (this.mCountScale < 0) {
                    this.mCountScale = 0;
                } else if (this.mCountScale > 220) {
                    this.mCountScale = CompanyIdentifierResolver.PROCTER_GAMBLE;
                }
                Log.e("-----------", "---------  指针位置   ------ " + ((this.mCountScale - this.mMidCountScale) * this.mScaleMargin) + "  " + (this.mCountScale - this.mMidCountScale) + "     中心位置:" + this.mMidCountScale + "   现在的位置:" + this.mCountScale);
                if (this.mCountScale % 10 < 5) {
                    this.mCountScale -= this.mCountScale % 10;
                } else {
                    this.mCountScale += 10 - (this.mCountScale % 10);
                }
                this.mScroller.setFinalX((this.mCountScale - this.mMidCountScale) * this.mScaleMargin);
                postInvalidate();
                Log.e("-------------", "---------3 up -----" + this.touchScaleMoveNums);
                this.touchScale = true;
                this.touchScaleMoveNums = 0;
                return true;
            case 2:
                CameraUtils.setScaleScrollTouchStateISO(false);
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
                Log.e("-------------", "---------3 move -----" + this.touchScaleMoveNums);
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }
}
