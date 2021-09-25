package com.yalantis.ucrop.model;

import android.graphics.RectF;

public class ImageState {
    private RectF mCropRect;
    private float mCurrentAngle;
    private RectF mCurrentImageRect;
    private float mCurrentScale;

    public ImageState(RectF cropRect, RectF currentImageRect, float currentScale, float currentAngle) {
        this.mCropRect = cropRect;
        this.mCurrentImageRect = currentImageRect;
        this.mCurrentScale = currentScale;
        this.mCurrentAngle = currentAngle;
    }

    public RectF getCropRect() {
        return this.mCropRect;
    }

    public RectF getCurrentImageRect() {
        return this.mCurrentImageRect;
    }

    public float getCurrentScale() {
        return this.mCurrentScale;
    }

    public float getCurrentAngle() {
        return this.mCurrentAngle;
    }
}
