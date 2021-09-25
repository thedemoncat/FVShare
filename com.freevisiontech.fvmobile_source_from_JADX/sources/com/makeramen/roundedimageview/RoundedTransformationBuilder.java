package com.makeramen.roundedimageview;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.ImageView;
import com.squareup.picasso.Transformation;
import java.util.Arrays;

public final class RoundedTransformationBuilder {
    /* access modifiers changed from: private */
    public ColorStateList mBorderColor = ColorStateList.valueOf(-16777216);
    /* access modifiers changed from: private */
    public float mBorderWidth = 0.0f;
    /* access modifiers changed from: private */
    public float[] mCornerRadii = {0.0f, 0.0f, 0.0f, 0.0f};
    private final DisplayMetrics mDisplayMetrics = Resources.getSystem().getDisplayMetrics();
    /* access modifiers changed from: private */
    public boolean mOval = false;
    /* access modifiers changed from: private */
    public ImageView.ScaleType mScaleType = ImageView.ScaleType.FIT_CENTER;

    public RoundedTransformationBuilder scaleType(ImageView.ScaleType scaleType) {
        this.mScaleType = scaleType;
        return this;
    }

    public RoundedTransformationBuilder cornerRadius(float radius) {
        this.mCornerRadii[0] = radius;
        this.mCornerRadii[1] = radius;
        this.mCornerRadii[2] = radius;
        this.mCornerRadii[3] = radius;
        return this;
    }

    public RoundedTransformationBuilder cornerRadius(int corner, float radius) {
        this.mCornerRadii[corner] = radius;
        return this;
    }

    public RoundedTransformationBuilder cornerRadiusDp(float radius) {
        return cornerRadius(TypedValue.applyDimension(1, radius, this.mDisplayMetrics));
    }

    public RoundedTransformationBuilder cornerRadiusDp(int corner, float radius) {
        return cornerRadius(corner, TypedValue.applyDimension(1, radius, this.mDisplayMetrics));
    }

    public RoundedTransformationBuilder borderWidth(float width) {
        this.mBorderWidth = width;
        return this;
    }

    public RoundedTransformationBuilder borderWidthDp(float width) {
        this.mBorderWidth = TypedValue.applyDimension(1, width, this.mDisplayMetrics);
        return this;
    }

    public RoundedTransformationBuilder borderColor(int color) {
        this.mBorderColor = ColorStateList.valueOf(color);
        return this;
    }

    public RoundedTransformationBuilder borderColor(ColorStateList colors) {
        this.mBorderColor = colors;
        return this;
    }

    public RoundedTransformationBuilder oval(boolean oval) {
        this.mOval = oval;
        return this;
    }

    public Transformation build() {
        return new Transformation() {
            public Bitmap transform(Bitmap source) {
                Bitmap transformed = RoundedDrawable.fromBitmap(source).setScaleType(RoundedTransformationBuilder.this.mScaleType).setCornerRadius(RoundedTransformationBuilder.this.mCornerRadii[0], RoundedTransformationBuilder.this.mCornerRadii[1], RoundedTransformationBuilder.this.mCornerRadii[2], RoundedTransformationBuilder.this.mCornerRadii[3]).setBorderWidth(RoundedTransformationBuilder.this.mBorderWidth).setBorderColor(RoundedTransformationBuilder.this.mBorderColor).setOval(RoundedTransformationBuilder.this.mOval).toBitmap();
                if (!source.equals(transformed)) {
                    source.recycle();
                }
                return transformed;
            }

            public String key() {
                return "r:" + Arrays.toString(RoundedTransformationBuilder.this.mCornerRadii) + "b:" + RoundedTransformationBuilder.this.mBorderWidth + "c:" + RoundedTransformationBuilder.this.mBorderColor + "o:" + RoundedTransformationBuilder.this.mOval;
            }
        };
    }
}
