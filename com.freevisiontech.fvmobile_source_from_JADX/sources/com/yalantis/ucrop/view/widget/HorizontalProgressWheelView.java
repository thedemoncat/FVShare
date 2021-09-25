package com.yalantis.ucrop.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.p001v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.yalantis.ucrop.C1654R;

public class HorizontalProgressWheelView extends View {
    private final Rect mCanvasClipBounds;
    private float mLastTouchedPosition;
    private int mMiddleLineColor;
    private int mProgressLineHeight;
    private int mProgressLineMargin;
    private Paint mProgressLinePaint;
    private int mProgressLineWidth;
    private boolean mScrollStarted;
    private ScrollingListener mScrollingListener;
    private float mTotalScrollDistance;

    public interface ScrollingListener {
        void onScroll(float f, float f2);

        void onScrollEnd();

        void onScrollStart();
    }

    public HorizontalProgressWheelView(Context context) {
        this(context, (AttributeSet) null);
    }

    public HorizontalProgressWheelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalProgressWheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mCanvasClipBounds = new Rect();
        init();
    }

    @TargetApi(21)
    public HorizontalProgressWheelView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mCanvasClipBounds = new Rect();
    }

    public void setScrollingListener(ScrollingListener scrollingListener) {
        this.mScrollingListener = scrollingListener;
    }

    public void setMiddleLineColor(@ColorInt int middleLineColor) {
        this.mMiddleLineColor = middleLineColor;
        invalidate();
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case 0:
                this.mLastTouchedPosition = event.getX();
                break;
            case 1:
                if (this.mScrollingListener != null) {
                    this.mScrollStarted = false;
                    this.mScrollingListener.onScrollEnd();
                    break;
                }
                break;
            case 2:
                float distance = event.getX() - this.mLastTouchedPosition;
                if (distance != 0.0f) {
                    if (!this.mScrollStarted) {
                        this.mScrollStarted = true;
                        if (this.mScrollingListener != null) {
                            this.mScrollingListener.onScrollStart();
                        }
                    }
                    onScrollEvent(event, distance);
                    break;
                }
                break;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.getClipBounds(this.mCanvasClipBounds);
        int linesCount = this.mCanvasClipBounds.width() / (this.mProgressLineWidth + this.mProgressLineMargin);
        float deltaX = this.mTotalScrollDistance % ((float) (this.mProgressLineMargin + this.mProgressLineWidth));
        this.mProgressLinePaint.setColor(getResources().getColor(C1654R.color.ucrop_color_progress_wheel_line));
        for (int i = 0; i < linesCount; i++) {
            if (i < linesCount / 4) {
                this.mProgressLinePaint.setAlpha((int) ((((float) i) / ((float) (linesCount / 4))) * 255.0f));
            } else if (i > (linesCount * 3) / 4) {
                this.mProgressLinePaint.setAlpha((int) ((((float) (linesCount - i)) / ((float) (linesCount / 4))) * 255.0f));
            } else {
                this.mProgressLinePaint.setAlpha(255);
            }
            Canvas canvas2 = canvas;
            canvas2.drawLine(((float) ((this.mProgressLineWidth + this.mProgressLineMargin) * i)) + (-deltaX) + ((float) this.mCanvasClipBounds.left), ((float) this.mCanvasClipBounds.centerY()) - (((float) this.mProgressLineHeight) / 4.0f), ((float) ((this.mProgressLineWidth + this.mProgressLineMargin) * i)) + (-deltaX) + ((float) this.mCanvasClipBounds.left), (((float) this.mProgressLineHeight) / 4.0f) + ((float) this.mCanvasClipBounds.centerY()), this.mProgressLinePaint);
        }
        this.mProgressLinePaint.setColor(this.mMiddleLineColor);
        Canvas canvas3 = canvas;
        canvas3.drawLine((float) this.mCanvasClipBounds.centerX(), ((float) this.mCanvasClipBounds.centerY()) - (((float) this.mProgressLineHeight) / 2.0f), (float) this.mCanvasClipBounds.centerX(), (((float) this.mProgressLineHeight) / 2.0f) + ((float) this.mCanvasClipBounds.centerY()), this.mProgressLinePaint);
    }

    private void onScrollEvent(MotionEvent event, float distance) {
        this.mTotalScrollDistance -= distance;
        postInvalidate();
        this.mLastTouchedPosition = event.getX();
        if (this.mScrollingListener != null) {
            this.mScrollingListener.onScroll(-distance, this.mTotalScrollDistance);
        }
    }

    private void init() {
        this.mMiddleLineColor = ContextCompat.getColor(getContext(), C1654R.color.ucrop_color_progress_wheel_line);
        this.mProgressLineWidth = getContext().getResources().getDimensionPixelSize(C1654R.dimen.ucrop_width_horizontal_wheel_progress_line);
        this.mProgressLineHeight = getContext().getResources().getDimensionPixelSize(C1654R.dimen.ucrop_height_horizontal_wheel_progress_line);
        this.mProgressLineMargin = getContext().getResources().getDimensionPixelSize(C1654R.dimen.ucrop_margin_horizontal_wheel_progress_line);
        this.mProgressLinePaint = new Paint(1);
        this.mProgressLinePaint.setStyle(Paint.Style.STROKE);
        this.mProgressLinePaint.setStrokeWidth((float) this.mProgressLineWidth);
    }
}
