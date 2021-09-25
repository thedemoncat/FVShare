package com.tuyenmonkey.mkloader;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import com.tuyenmonkey.mkloader.callback.InvalidateListener;
import com.tuyenmonkey.mkloader.type.LoaderView;
import com.tuyenmonkey.mkloader.util.LoaderGenerator;

public class MKLoader extends View implements InvalidateListener {
    private LoaderView loaderView;

    public MKLoader(Context context) {
        super(context);
        initialize(context, (AttributeSet) null, 0);
    }

    public MKLoader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public MKLoader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, C1650R.styleable.MKLoader);
        this.loaderView = LoaderGenerator.generateLoaderView(typedArray.getInt(C1650R.styleable.MKLoader_mk_type, -1));
        this.loaderView.setColor(typedArray.getColor(C1650R.styleable.MKLoader_mk_color, Color.parseColor("#ffffff")));
        typedArray.recycle();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(resolveSize(this.loaderView.getDesiredWidth(), widthMeasureSpec), resolveSize(this.loaderView.getDesiredHeight(), heightMeasureSpec));
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.loaderView.setSize(getWidth(), getHeight());
        this.loaderView.initializeObjects();
        this.loaderView.setUpAnimation();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.loaderView.draw(canvas);
    }

    public void reDraw() {
        invalidate();
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.loaderView != null && this.loaderView.isDetached()) {
            this.loaderView.setInvalidateListener(this);
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.loaderView != null) {
            this.loaderView.onDetach();
        }
    }
}
