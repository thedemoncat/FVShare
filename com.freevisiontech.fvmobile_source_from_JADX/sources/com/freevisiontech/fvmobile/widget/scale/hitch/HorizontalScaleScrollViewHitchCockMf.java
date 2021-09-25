package com.freevisiontech.fvmobile.widget.scale.hitch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utility.Util;

public class HorizontalScaleScrollViewHitchCockMf extends BaseScaleViewHitchCockMf {
    private int mEndColor = getResources().getColor(C0853R.color.white);
    private int mStartColor = getResources().getColor(C0853R.color.color_hitch_cock_mf_color);

    public HorizontalScaleScrollViewHitchCockMf(Context context) {
        super(context);
    }

    public HorizontalScaleScrollViewHitchCockMf(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalScaleScrollViewHitchCockMf(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public HorizontalScaleScrollViewHitchCockMf(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
        paint.setStrokeWidth(2.0f);
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
                paint.setStrokeWidth(2.0f);
                canvas.drawLine((float) (this.mScaleMargin * i), (float) (this.mRectHeight - 20), (float) (this.mScaleMargin * i), (float) ((this.mRectHeight - this.mScaleMaxHeight) - 20), paint);
                paint.setStrokeWidth(0.0f);
                paint.setColor(this.mEndColor);
                canvas.drawText(String.valueOf(((float) k) / 10.0f), (float) (this.mScaleMargin * i), (float) (((this.mRectHeight - this.mScaleMaxHeight) - 20) - 20), paint);
                k += 10;
            } else {
                paint.setStrokeWidth(2.0f);
                paint.setColor(this.mEndColor);
                canvas.drawLine((float) (this.mScaleMargin * i), (float) (((this.mRectHeight - 10) - 20) - 10), (float) (this.mScaleMargin * i), (float) (((this.mRectHeight - this.mScaleHeight) - 10) - 20), paint);
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
        paint.setStrokeWidth(2.0f);
        int countScale = (this.mScaleScrollViewRange / this.mScaleMargin) / 2;
        int finalX = this.mScroller.getFinalX();
        this.mCountScale = ((int) Math.rint(((double) finalX) / ((double) this.mScaleMargin))) + countScale + this.mMin;
        if (this.mScrollListener != null) {
            this.mScrollListener.onScaleScroll(this.mCountScale);
            float k2 = (float) this.mCountScale;
            if (this.mCountScale % 10 == 0) {
                paint.setStrokeWidth(0.0f);
                paint.setColor(this.mStartColor);
                canvas.drawText(String.valueOf(k2 / 10.0f), (float) (this.mCountScale * this.mScaleMargin), (float) (((this.mRectHeight - this.mScaleMaxHeight) - 20) - 20), paint);
            }
        }
        paint.setStrokeWidth(3.0f);
        canvas.drawLine((float) ((this.mScaleMargin * countScale) + finalX), (float) (this.mRectHeight - 20), (float) ((this.mScaleMargin * countScale) + finalX), (float) ((this.mRectHeight - this.mScaleMaxHeight) - this.mScaleHeight), paint);
        paint.setStrokeWidth(1.0f);
        Path path = new Path();
        path.moveTo((float) ((this.mScaleMargin * countScale) + finalX), (float) ((this.mRectHeight - 20) + 5));
        path.lineTo((float) (((this.mScaleMargin * countScale) + finalX) - 10), (float) ((this.mRectHeight - 20) + 5 + 10));
        path.lineTo((float) ((this.mScaleMargin * countScale) + finalX + 10), (float) ((this.mRectHeight - 20) + 5 + 10));
        paint.setStyle(Paint.Style.FILL);
        path.close();
        canvas.drawPath(path, paint);
        Path path2 = new Path();
        path2.moveTo((float) ((this.mScaleMargin * countScale) + finalX), (float) (((this.mRectHeight - this.mScaleMaxHeight) - this.mScaleHeight) - 5));
        path2.lineTo((float) (((this.mScaleMargin * countScale) + finalX) - 10), (float) ((((this.mRectHeight - this.mScaleMaxHeight) - this.mScaleHeight) - 5) - 10));
        path2.lineTo((float) ((this.mScaleMargin * countScale) + finalX + 10), (float) ((((this.mRectHeight - this.mScaleMaxHeight) - this.mScaleHeight) - 5) - 10));
        paint.setStyle(Paint.Style.FILL);
        path2.close();
        canvas.drawPath(path2, paint);
    }

    public void scrollToScale(int val) {
        if (val >= this.mMin && val <= this.mMax) {
            smoothScrollBy((val - this.mCountScale) * this.mScaleMargin, 0);
        }
    }

    public void scrollToScaleFirst(int val) {
        smoothScrollBy((val - this.mCountScale) * this.mScaleMargin, 0);
    }

    public void scrollToScaleFirstNew(int val) {
        smoothScrollBy((val - this.mCountScale) * this.mScaleMargin, 0);
    }

    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        switch (event.getAction()) {
            case 0:
                if (this.mScroller != null && !this.mScroller.isFinished()) {
                    this.mScroller.abortAnimation();
                }
                CameraUtils.setScaleScrollTouchStateMF(true);
                this.mScrollLastX = x;
                return true;
            case 1:
                CameraUtils.setScaleScrollTouchStateMF(true);
                if (this.mCountScale < this.mMin) {
                    this.mCountScale = this.mMin;
                }
                if (this.mCountScale > this.mMax) {
                    this.mCountScale = this.mMax;
                }
                int finalX = (this.mCountScale - this.mMidCountScale) * this.mScaleMargin;
                Log.e("------------", "-------- mMidCountScale  mMidCountScale  mMidCountScale  6666 -----" + this.mMidCountScale);
                Log.e("-----------", "---------  指针位置   ------ " + finalX + "  " + (this.mCountScale - this.mMidCountScale) + "     中心位置:" + this.mMidCountScale + "   现在的位置:" + this.mCountScale);
                this.mScroller.setFinalX(finalX);
                postInvalidate();
                return true;
            case 2:
                CameraUtils.setScaleScrollTouchStateMF(false);
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
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }
}
