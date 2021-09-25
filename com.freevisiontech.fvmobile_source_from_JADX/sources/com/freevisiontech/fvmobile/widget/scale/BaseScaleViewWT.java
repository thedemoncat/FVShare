package com.freevisiontech.fvmobile.widget.scale;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.StyleableRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Scroller;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utility.Util;

public abstract class BaseScaleViewWT extends View {
    public static final int[] ATTR = {C0853R.attr.lf_scale_view_min, C0853R.attr.lf_scale_view_max, C0853R.attr.lf_scale_view_margin, C0853R.attr.lf_scale_view_height};
    @StyleableRes
    public static final int LF_SCALE_CURRENT = 4;
    @StyleableRes
    public static final int LF_SCALE_HEIGHT = 3;
    @StyleableRes
    public static final int LF_SCALE_MARGIN = 2;
    @StyleableRes
    public static final int LF_SCALE_MAX = 1;
    @StyleableRes
    public static final int LF_SCALE_MIN = 0;
    protected int mCountScale;
    protected int mMax;
    protected int mMidCountScale;
    protected int mMin;
    protected int mRectHeight;
    protected int mRectWidth;
    protected int mScaleHeight;
    protected int mScaleMargin;
    protected int mScaleMaxHeight;
    protected int mScaleScrollViewRange;
    protected int mScrollLastX;
    protected OnScrollListener mScrollListener;
    protected Scroller mScroller;
    protected int mTempScale;

    public interface OnScrollListener {
        void onScaleScroll(int i);
    }

    /* access modifiers changed from: protected */
    public abstract void initVar();

    /* access modifiers changed from: protected */
    public abstract void onDrawLine(Canvas canvas, Paint paint);

    /* access modifiers changed from: protected */
    public abstract void onDrawPointer(Canvas canvas, Paint paint);

    /* access modifiers changed from: protected */
    public abstract void onDrawScale(Canvas canvas, Paint paint);

    public abstract void scrollToScale(int i);

    public abstract void scrollToScaleFirst(int i);

    public abstract void scrollToScaleFirstNew(int i);

    public BaseScaleViewWT(Context context) {
        super(context);
        init((AttributeSet) null);
    }

    public BaseScaleViewWT(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public BaseScaleViewWT(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(21)
    public BaseScaleViewWT(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    /* access modifiers changed from: protected */
    public void init(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, ATTR);
        this.mMin = ta.getInteger(0, 0);
        this.mMax = ta.getInteger(1, (Util.getDrawScaleWTMax() / 10) * 10);
        float displayDensity = CameraUtils.getDisplayDensity();
        this.mScaleMargin = ta.getDimensionPixelOffset(2, 20);
        this.mScaleHeight = ta.getDimensionPixelOffset(3, 20);
        ta.recycle();
        this.mScroller = new Scroller(getContext());
        initVar();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(-1);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextAlign(Paint.Align.CENTER);
        onDrawScale(canvas, paint);
        onDrawPointer(canvas, paint);
        super.onDraw(canvas);
    }

    public void setCurScale(int val) {
        if (val >= this.mMin && val <= this.mMax) {
            scrollToScale(val);
            postInvalidate();
        }
    }

    public void setCurScaleFirst(int val) {
        Log.e("------------", "-------- mMidCountScale  mMidCountScale  mMidCountScale -----" + this.mMidCountScale);
        scrollToScaleFirst(val);
        postInvalidate();
    }

    public void setCurScaleFirstNew() {
        Log.e("------------", "-------- mMidCountScale  mMidCountScale  mMidCountScale  FirstNew -----  " + this.mMidCountScale);
        scrollToScaleFirstNew(this.mMidCountScale * -1);
        postInvalidate();
    }

    public void computeScroll() {
        super.computeScroll();
        if (this.mScroller.computeScrollOffset()) {
            scrollTo(this.mScroller.getCurrX(), this.mScroller.getCurrY());
            postInvalidate();
        }
    }

    public void smoothScrollBy(int dx, int dy) {
        this.mScroller.startScroll(this.mScroller.getFinalX(), this.mScroller.getFinalY(), dx, dy);
        postInvalidate();
    }

    public void smoothScrollTo(int fx, int fy) {
        smoothScrollBy(fx - this.mScroller.getFinalX(), fy - this.mScroller.getFinalY());
    }

    public void setOnScrollListener(OnScrollListener listener) {
        this.mScrollListener = listener;
    }
}
