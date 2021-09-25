package com.freevisiontech.fvmobile.widget.scale;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utility.Util;
import com.p007ny.ijk.upplayer.BuildConfig;

public class HorizontalScaleScrollViewWbTwo extends BaseScaleViewWbTwo {
    private int mEndColor = getResources().getColor(C0853R.color.white);
    private int mStartColor = getResources().getColor(C0853R.color.color_shutter_iso_color);

    public HorizontalScaleScrollViewWbTwo(Context context) {
        super(context);
    }

    public HorizontalScaleScrollViewWbTwo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalScaleScrollViewWbTwo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public HorizontalScaleScrollViewWbTwo(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /* access modifiers changed from: protected */
    public void initVar() {
        this.mRectWidth = (this.mMax - this.mMin) * this.mScaleMargin;
        this.mRectHeight = this.mScaleHeight * 8;
        this.mScaleMaxHeight = this.mScaleHeight * 2;
        Log.e("-----------", "------------  mRectWidth: " + this.mRectWidth + "  ------ mRectHeight: " + this.mRectHeight);
        setLayoutParams(new ViewGroup.MarginLayoutParams(this.mRectWidth, this.mRectHeight));
        CameraUtils.setScaleMargin(this.mScaleMargin);
        CameraUtils.setMidCountScale(this.mMidCountScale);
        CameraUtils.setRectHeight(this.mRectHeight);
        CameraUtils.setScaleMaxHeight(this.mScaleMaxHeight);
        CameraUtils.setScaleHeight(this.mScaleHeight);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(this.mRectHeight, Integer.MIN_VALUE));
        this.mScaleScrollViewRange = getMeasuredWidth();
        this.mTempScale = ((this.mScaleScrollViewRange / this.mScaleMargin) / 2) + this.mMin;
        this.mMidCountScale = ((this.mScaleScrollViewRange / this.mScaleMargin) / 2) + this.mMin;
        CameraUtils.setScaleMargin(this.mScaleMargin);
        CameraUtils.setMidCountScale(this.mMidCountScale);
        CameraUtils.setRectHeight(this.mRectHeight);
        CameraUtils.setScaleMaxHeight(this.mScaleMaxHeight);
        CameraUtils.setScaleHeight(this.mScaleHeight);
    }

    /* access modifiers changed from: protected */
    public void onDrawLine(Canvas canvas, Paint paint) {
        canvas.drawLine(0.0f, (float) this.mRectHeight, (float) this.mRectWidth, (float) this.mRectHeight, paint);
    }

    /* access modifiers changed from: protected */
    public void onDrawScale(Canvas canvas, Paint paint) {
        paint.setStrokeWidth(2.0f);
        if ("SM-N9500".equals(Util.getSystemModel())) {
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
                float k2 = (float) k;
                if ("0.0".equals(String.valueOf(k2 / 10.0f))) {
                    paint.setColor(this.mEndColor);
                    canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), C0853R.mipmap.ic_scale_bai_unselect), (float) ((this.mScaleMargin * i) - 20), (float) ((((this.mRectHeight - this.mScaleMaxHeight) - 20) - 20) - 35), paint);
                } else if (BuildConfig.VERSION_NAME.equals(String.valueOf(k2 / 10.0f))) {
                    paint.setColor(this.mEndColor);
                    canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), C0853R.mipmap.ic_scale_ying_unselect), (float) ((this.mScaleMargin * i) - 20), (float) ((((this.mRectHeight - this.mScaleMaxHeight) - 20) - 20) - 35), paint);
                } else if (com.daimajia.easing.BuildConfig.VERSION_NAME.equals(String.valueOf(k2 / 10.0f))) {
                    paint.setColor(this.mEndColor);
                    canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), C0853R.mipmap.ic_scale_yintian_unselect), (float) ((this.mScaleMargin * i) - 20), (float) ((((this.mRectHeight - this.mScaleMaxHeight) - 20) - 20) - 35), paint);
                } else if ("3.0".equals(String.valueOf(k2 / 10.0f))) {
                    paint.setColor(this.mEndColor);
                    canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), C0853R.mipmap.ic_scale_qingtian_unselect), (float) ((this.mScaleMargin * i) - 20), (float) ((((this.mRectHeight - this.mScaleMaxHeight) - 20) - 20) - 35), paint);
                } else if ("4.0".equals(String.valueOf(k2 / 10.0f))) {
                    paint.setColor(this.mEndColor);
                    canvas.drawText("AUTO", (float) (this.mScaleMargin * i), (float) (((this.mRectHeight - this.mScaleMaxHeight) - 20) - 20), paint);
                } else {
                    paint.setColor(this.mEndColor);
                    canvas.drawText(String.valueOf((int) (k2 - 40.0f)), (float) (this.mScaleMargin * i), (float) (((this.mRectHeight - this.mScaleMaxHeight) - 20) - 20), paint);
                }
                k += 10;
            } else if (i > 40) {
                paint.setStrokeWidth(2.0f);
                paint.setColor(this.mEndColor);
                canvas.drawLine((float) (this.mScaleMargin * i), (float) (((this.mRectHeight - 10) - 20) - 10), (float) (this.mScaleMargin * i), (float) (((this.mRectHeight - this.mScaleHeight) - 10) - 20), paint);
            } else if (i % 2 == 0) {
                paint.setColor(this.mEndColor);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawCircle((float) (this.mScaleMargin * i), (float) ((((this.mRectHeight - 10) - 20) - 10) - 3), 3.0f, paint);
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
        Bitmap bit;
        Bitmap bit2;
        Bitmap bit3;
        Bitmap bit4;
        paint.setColor(this.mStartColor);
        paint.setStrokeWidth(2.0f);
        this.mCountScale = ((int) Math.rint(((double) this.mScroller.getFinalX()) / ((double) this.mScaleMargin))) + ((this.mScaleScrollViewRange / this.mScaleMargin) / 2) + this.mMin;
        if (this.mScrollListener != null) {
            this.mScrollListener.onScaleScroll(this.mCountScale);
            float k2 = (float) this.mCountScale;
            if (this.mCountScale % 10 == 0) {
                paint.setStrokeWidth(0.0f);
                paint.setColor(this.mStartColor);
                if ("0.0".equals(String.valueOf(k2 / 10.0f))) {
                    if (this.mStartColor == getResources().getColor(C0853R.color.white)) {
                        bit4 = BitmapFactory.decodeResource(getResources(), C0853R.mipmap.ic_scale_bai_unselect);
                    } else if (this.mStartColor == getResources().getColor(C0853R.color.gray_be)) {
                        bit4 = BitmapFactory.decodeResource(getResources(), C0853R.mipmap.ic_scale_bai_gray_unselect);
                    } else {
                        bit4 = BitmapFactory.decodeResource(getResources(), C0853R.mipmap.ic_scale_bai_select);
                    }
                    canvas.drawBitmap(bit4, (float) ((this.mCountScale * this.mScaleMargin) - 20), (float) ((((this.mRectHeight - this.mScaleMaxHeight) - 20) - 20) - 35), paint);
                } else if (BuildConfig.VERSION_NAME.equals(String.valueOf(k2 / 10.0f))) {
                    if (this.mStartColor == getResources().getColor(C0853R.color.white)) {
                        bit3 = BitmapFactory.decodeResource(getResources(), C0853R.mipmap.ic_scale_ying_unselect);
                    } else if (this.mStartColor == getResources().getColor(C0853R.color.gray_be)) {
                        bit3 = BitmapFactory.decodeResource(getResources(), C0853R.mipmap.ic_scale_ying_gray_unselect);
                    } else {
                        bit3 = BitmapFactory.decodeResource(getResources(), C0853R.mipmap.ic_scale_ying_select);
                    }
                    canvas.drawBitmap(bit3, (float) ((this.mCountScale * this.mScaleMargin) - 20), (float) ((((this.mRectHeight - this.mScaleMaxHeight) - 20) - 20) - 35), paint);
                } else if (com.daimajia.easing.BuildConfig.VERSION_NAME.equals(String.valueOf(k2 / 10.0f))) {
                    if (this.mStartColor == getResources().getColor(C0853R.color.white)) {
                        bit2 = BitmapFactory.decodeResource(getResources(), C0853R.mipmap.ic_scale_yintian_unselect);
                    } else if (this.mStartColor == getResources().getColor(C0853R.color.gray_be)) {
                        bit2 = BitmapFactory.decodeResource(getResources(), C0853R.mipmap.ic_scale_yintian_gray_unselect);
                    } else {
                        bit2 = BitmapFactory.decodeResource(getResources(), C0853R.mipmap.ic_scale_yintian_select);
                    }
                    canvas.drawBitmap(bit2, (float) ((this.mCountScale * this.mScaleMargin) - 20), (float) ((((this.mRectHeight - this.mScaleMaxHeight) - 20) - 20) - 35), paint);
                } else if ("3.0".equals(String.valueOf(k2 / 10.0f))) {
                    if (this.mStartColor == getResources().getColor(C0853R.color.white)) {
                        bit = BitmapFactory.decodeResource(getResources(), C0853R.mipmap.ic_scale_qingtian_unselect);
                    } else if (this.mStartColor == getResources().getColor(C0853R.color.gray_be)) {
                        bit = BitmapFactory.decodeResource(getResources(), C0853R.mipmap.ic_scale_qingtian_gray_unselect);
                    } else {
                        bit = BitmapFactory.decodeResource(getResources(), C0853R.mipmap.ic_scale_qingtian_select);
                    }
                    canvas.drawBitmap(bit, (float) ((this.mCountScale * this.mScaleMargin) - 20), (float) ((((this.mRectHeight - this.mScaleMaxHeight) - 20) - 20) - 35), paint);
                } else if ("4.0".equals(String.valueOf(k2 / 10.0f))) {
                    canvas.drawText("AUTO", (float) (this.mCountScale * this.mScaleMargin), (float) (((this.mRectHeight - this.mScaleMaxHeight) - 20) - 20), paint);
                } else {
                    canvas.drawText(String.valueOf((int) (k2 - 40.0f)), (float) (this.mCountScale * this.mScaleMargin), (float) (((this.mRectHeight - this.mScaleMaxHeight) - 20) - 20), paint);
                }
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
                if (this.mScroller != null && !this.mScroller.isFinished()) {
                    this.mScroller.abortAnimation();
                }
                CameraUtils.setScaleScrollTouchStateWB(true);
                this.mScrollLastX = x;
                return true;
            case 1:
                CameraUtils.setScaleScrollTouchStateWB(true);
                if (this.mCountScale < this.mMin) {
                    this.mCountScale = this.mMin;
                }
                if (this.mCountScale > this.mMax) {
                    this.mCountScale = this.mMax;
                }
                int finalX = (this.mCountScale - this.mMidCountScale) * this.mScaleMargin;
                Log.e("-----------", "---------  指针位置   ------ " + finalX + "  " + (this.mCountScale - this.mMidCountScale) + "     中心位置:" + this.mMidCountScale + "   现在的位置:" + this.mCountScale);
                if (this.mCountScale < 40) {
                    if (this.mCountScale % 10 < 5) {
                        this.mCountScale -= this.mCountScale % 10;
                    } else {
                        this.mCountScale += 10 - (this.mCountScale % 10);
                    }
                    this.mScroller.setFinalX((this.mCountScale - this.mMidCountScale) * this.mScaleMargin);
                    postInvalidate();
                    return true;
                }
                this.mScroller.setFinalX(finalX);
                postInvalidate();
                return true;
            case 2:
                CameraUtils.setScaleScrollTouchStateWB(false);
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
