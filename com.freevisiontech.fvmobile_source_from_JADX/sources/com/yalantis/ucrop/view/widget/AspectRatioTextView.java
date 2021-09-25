package com.yalantis.ucrop.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.p001v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;
import com.yalantis.ucrop.C1654R;
import com.yalantis.ucrop.model.AspectRatio;
import java.util.Locale;

public class AspectRatioTextView extends TextView {
    private float mAspectRatio;
    private String mAspectRatioTitle;
    private float mAspectRatioX;
    private float mAspectRatioY;
    private final Rect mCanvasClipBounds;
    private Paint mDotPaint;
    private int mDotSize;

    public AspectRatioTextView(Context context) {
        this(context, (AttributeSet) null);
    }

    public AspectRatioTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AspectRatioTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mCanvasClipBounds = new Rect();
        init(context.obtainStyledAttributes(attrs, C1654R.styleable.ucrop_AspectRatioTextView));
    }

    @TargetApi(21)
    public AspectRatioTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mCanvasClipBounds = new Rect();
        init(context.obtainStyledAttributes(attrs, C1654R.styleable.ucrop_AspectRatioTextView));
    }

    public void setActiveColor(@ColorInt int activeColor) {
        applyActiveColor(activeColor);
        invalidate();
    }

    public void setAspectRatio(@NonNull AspectRatio aspectRatio) {
        this.mAspectRatioTitle = aspectRatio.getAspectRatioTitle();
        this.mAspectRatioX = aspectRatio.getAspectRatioX();
        this.mAspectRatioY = aspectRatio.getAspectRatioY();
        if (this.mAspectRatioX == 0.0f || this.mAspectRatioY == 0.0f) {
            this.mAspectRatio = 0.0f;
        } else {
            this.mAspectRatio = this.mAspectRatioX / this.mAspectRatioY;
        }
        setTitle();
    }

    public float getAspectRatio(boolean toggleRatio) {
        if (toggleRatio) {
            toggleAspectRatio();
            setTitle();
        }
        return this.mAspectRatio;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isSelected()) {
            canvas.getClipBounds(this.mCanvasClipBounds);
            canvas.drawCircle(((float) (this.mCanvasClipBounds.right - this.mCanvasClipBounds.left)) / 2.0f, (float) (this.mCanvasClipBounds.bottom - this.mDotSize), (float) (this.mDotSize / 2), this.mDotPaint);
        }
    }

    private void init(@NonNull TypedArray a) {
        setGravity(1);
        this.mAspectRatioTitle = a.getString(C1654R.styleable.ucrop_AspectRatioTextView_ucrop_artv_ratio_title);
        this.mAspectRatioX = a.getFloat(C1654R.styleable.ucrop_AspectRatioTextView_ucrop_artv_ratio_x, 0.0f);
        this.mAspectRatioY = a.getFloat(C1654R.styleable.ucrop_AspectRatioTextView_ucrop_artv_ratio_y, 0.0f);
        if (this.mAspectRatioX == 0.0f || this.mAspectRatioY == 0.0f) {
            this.mAspectRatio = 0.0f;
        } else {
            this.mAspectRatio = this.mAspectRatioX / this.mAspectRatioY;
        }
        this.mDotSize = getContext().getResources().getDimensionPixelSize(C1654R.dimen.ucrop_size_dot_scale_text_view);
        this.mDotPaint = new Paint(1);
        this.mDotPaint.setStyle(Paint.Style.FILL);
        setTitle();
        applyActiveColor(getResources().getColor(C1654R.color.ucrop_color_widget_active));
        a.recycle();
    }

    private void applyActiveColor(@ColorInt int activeColor) {
        if (this.mDotPaint != null) {
            this.mDotPaint.setColor(activeColor);
        }
        setTextColor(new ColorStateList(new int[][]{new int[]{16842913}, new int[]{0}}, new int[]{activeColor, ContextCompat.getColor(getContext(), C1654R.color.ucrop_color_widget)}));
    }

    private void toggleAspectRatio() {
        if (this.mAspectRatio != 0.0f) {
            float tempRatioW = this.mAspectRatioX;
            this.mAspectRatioX = this.mAspectRatioY;
            this.mAspectRatioY = tempRatioW;
            this.mAspectRatio = this.mAspectRatioX / this.mAspectRatioY;
        }
    }

    private void setTitle() {
        if (!TextUtils.isEmpty(this.mAspectRatioTitle)) {
            setText(this.mAspectRatioTitle);
            return;
        }
        setText(String.format(Locale.US, "", new Object[]{Integer.valueOf((int) this.mAspectRatioX), Integer.valueOf((int) this.mAspectRatioY)}));
    }
}
