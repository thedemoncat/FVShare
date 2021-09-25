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
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utility.Util;
import com.p007ny.ijk.upplayer.BuildConfig;
import java.util.ArrayList;
import java.util.List;

public class HorizontalScaleScrollViewWT extends BaseScaleViewWT {
    private int mEndColor = getResources().getColor(C0853R.color.white);
    private int mStartColor = getResources().getColor(C0853R.color.color_shutter_iso_color);
    private int mStartTransColor = getResources().getColor(C0853R.color.transparent);
    private List<Long> queueTime;

    public HorizontalScaleScrollViewWT(Context context) {
        super(context);
    }

    public HorizontalScaleScrollViewWT(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalScaleScrollViewWT(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public HorizontalScaleScrollViewWT(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /* access modifiers changed from: protected */
    public void initVar() {
        this.mRectWidth = (this.mMax - this.mMin) * this.mScaleMargin;
        this.mRectHeight = this.mScaleHeight * 8;
        this.mScaleMaxHeight = this.mScaleHeight * 2;
        Log.e("-----------", "------------  mRectWidth: " + this.mRectWidth + "  ------ mRectHeight: " + this.mRectHeight);
        setLayoutParams(new ViewGroup.MarginLayoutParams(this.mRectWidth, this.mRectHeight));
        setQueueTime();
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
        int finalX = this.mScroller.getFinalX();
        int countScale = (this.mScaleScrollViewRange / this.mScaleMargin) / 2;
        int tmpCountScale = (int) Math.rint(((double) finalX) / ((double) this.mScaleMargin));
        int k = this.mMin;
        for (int i = 0; i <= this.mMax - this.mMin; i++) {
            if (i % 10 == 0) {
                paint.setStrokeWidth(2.0f);
                paint.setColor(this.mEndColor);
                canvas.drawLine((float) (this.mScaleMargin * i), (float) (this.mRectHeight - 20), (float) (this.mScaleMargin * i), (float) ((this.mRectHeight - this.mScaleMaxHeight) - 20), paint);
                paint.setStrokeWidth(0.0f);
                float k2 = (float) k;
                if ("0.0".equals(String.valueOf(k2 / 10.0f))) {
                    if (tmpCountScale + countScale == i) {
                        paint.setColor(this.mStartColor);
                    } else {
                        paint.setColor(this.mEndColor);
                    }
                    canvas.drawText(String.valueOf((10.0f + k2) / 10.0f), (float) (this.mScaleMargin * i), (float) (((this.mRectHeight - this.mScaleMaxHeight) - 20) - 20), paint);
                } else if (BuildConfig.VERSION_NAME.equals(String.valueOf(k2 / 10.0f))) {
                    if (tmpCountScale + countScale == i) {
                        paint.setColor(this.mStartColor);
                    } else {
                        paint.setColor(this.mEndColor);
                    }
                    canvas.drawText(String.valueOf((10.0f + k2) / 10.0f), (float) (this.mScaleMargin * i), (float) (((this.mRectHeight - this.mScaleMaxHeight) - 20) - 20), paint);
                } else {
                    if (tmpCountScale + countScale == i) {
                        paint.setColor(this.mStartColor);
                    } else {
                        paint.setColor(this.mEndColor);
                    }
                    canvas.drawText(String.valueOf((10.0f + k2) / 10.0f), (float) (this.mScaleMargin * i), (float) (((this.mRectHeight - this.mScaleMaxHeight) - 20) - 20), paint);
                }
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

    public void setColor(int startColor, int endColor, int endTransColor) {
        this.mStartColor = startColor;
        this.mEndColor = endColor;
        this.mStartTransColor = endTransColor;
        invalidate();
    }

    /* access modifiers changed from: protected */
    public void onDrawPointer(Canvas canvas, Paint paint) {
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
                    canvas.drawText(String.valueOf((k2 + 10.0f) / 10.0f), (float) (this.mCountScale * this.mScaleMargin), (float) (((this.mRectHeight - this.mScaleMaxHeight) - 20) - 20), paint);
                } else if (BuildConfig.VERSION_NAME.equals(String.valueOf(k2 / 10.0f))) {
                    canvas.drawText(String.valueOf((k2 + 10.0f) / 10.0f), (float) (this.mCountScale * this.mScaleMargin), (float) (((this.mRectHeight - this.mScaleMaxHeight) - 20) - 20), paint);
                } else {
                    canvas.drawText(String.valueOf((k2 + 10.0f) / 10.0f), (float) (this.mCountScale * this.mScaleMargin), (float) (((this.mRectHeight - this.mScaleMaxHeight) - 20) - 20), paint);
                }
            }
        }
        invalidate();
    }

    private void setQueueTime() {
        this.queueTime = new ArrayList();
        this.queueTime.clear();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x000b, code lost:
        r0 = (r11 - r10.mCountScale) * r10.mScaleMargin;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void scrollToScale(int r11) {
        /*
            r10 = this;
            r9 = 1
            r8 = 0
            int r3 = r10.mMin
            if (r11 < r3) goto L_0x000a
            int r3 = r10.mMax
            if (r11 <= r3) goto L_0x000b
        L_0x000a:
            return
        L_0x000b:
            int r3 = r10.mCountScale
            int r3 = r11 - r3
            int r4 = r10.mScaleMargin
            int r0 = r3 * r4
            if (r0 == 0) goto L_0x000a
            int r3 = com.freevisiontech.fvmobile.utility.CameraUtils.getCurrentPageIndex()
            if (r3 != 0) goto L_0x008d
            java.util.List<java.lang.Long> r3 = r10.queueTime
            long r4 = java.lang.System.currentTimeMillis()
            java.lang.Long r4 = java.lang.Long.valueOf(r4)
            r3.add(r8, r4)
            r2 = 0
            r1 = 0
            java.util.List<java.lang.Long> r3 = r10.queueTime
            int r3 = r3.size()
            if (r3 <= r9) goto L_0x0087
            java.util.List<java.lang.Long> r3 = r10.queueTime
            java.lang.Object r3 = r3.get(r8)
            java.lang.Long r3 = (java.lang.Long) r3
            long r4 = r3.longValue()
            java.util.List<java.lang.Long> r3 = r10.queueTime
            java.lang.Object r3 = r3.get(r9)
            java.lang.Long r3 = (java.lang.Long) r3
            long r6 = r3.longValue()
            long r4 = r4 - r6
            r6 = 1000(0x3e8, double:4.94E-321)
            int r3 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r3 <= 0) goto L_0x005a
            java.util.List<java.lang.Long> r3 = r10.queueTime
            r3.clear()
            r10.smoothScrollBy(r0, r8)
            goto L_0x000a
        L_0x005a:
            java.util.List<java.lang.Long> r3 = r10.queueTime
            java.lang.Object r3 = r3.get(r8)
            java.lang.Long r3 = (java.lang.Long) r3
            long r4 = r3.longValue()
            java.util.List<java.lang.Long> r3 = r10.queueTime
            java.lang.Object r3 = r3.get(r9)
            java.lang.Long r3 = (java.lang.Long) r3
            long r6 = r3.longValue()
            long r4 = r4 - r6
            int r2 = (int) r4
            int r1 = r2 / 4
            android.os.Handler r3 = new android.os.Handler
            r3.<init>()
            com.freevisiontech.fvmobile.widget.scale.HorizontalScaleScrollViewWT$1 r4 = new com.freevisiontech.fvmobile.widget.scale.HorizontalScaleScrollViewWT$1
            r4.<init>(r0)
            int r5 = r1 + -3
            long r6 = (long) r5
            r3.postDelayed(r4, r6)
            goto L_0x000a
        L_0x0087:
            r1 = 1
            r10.smoothScrollBy(r0, r8)
            goto L_0x000a
        L_0x008d:
            r10.smoothScrollBy(r0, r8)
            goto L_0x000a
        */
        throw new UnsupportedOperationException("Method not decompiled: com.freevisiontech.fvmobile.widget.scale.HorizontalScaleScrollViewWT.scrollToScale(int):void");
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
                CameraUtils.setScaleScrollTouchStateWT(true);
                this.mScrollLastX = x;
                return true;
            case 1:
                CameraUtils.setScaleScrollTouchStateWT(true);
                if (this.mCountScale < this.mMin) {
                    this.mCountScale = this.mMin;
                }
                if (this.mCountScale > this.mMax) {
                    this.mCountScale = this.mMax;
                }
                int max = (Util.getDrawScaleWTMax() / 10) * 10;
                if (this.mCountScale < 0) {
                    this.mCountScale = 0;
                } else if (this.mCountScale > max) {
                    this.mCountScale = max;
                }
                int finalX = (this.mCountScale - this.mMidCountScale) * this.mScaleMargin;
                Log.e("------------", "-------- mMidCountScale  mMidCountScale  mMidCountScale  6666 -----" + this.mMidCountScale);
                Log.e("-----------", "---------  指针位置   ------ " + finalX + "  " + (this.mCountScale - this.mMidCountScale) + "     中心位置:" + this.mMidCountScale + "   现在的位置:" + this.mCountScale);
                this.mScroller.setFinalX(finalX);
                postInvalidate();
                return true;
            case 2:
                CameraUtils.setScaleScrollTouchStateWT(false);
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
