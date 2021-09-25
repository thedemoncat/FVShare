package com.yalantis.ucrop.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import com.yalantis.ucrop.callback.BitmapLoadCallback;
import com.yalantis.ucrop.model.ExifInfo;
import com.yalantis.ucrop.util.BitmapLoadUtils;
import com.yalantis.ucrop.util.FastBitmapDrawable;
import com.yalantis.ucrop.util.RectUtils;

public class TransformImageView extends ImageView {
    private static final int MATRIX_VALUES_COUNT = 9;
    private static final int RECT_CENTER_POINT_COORDS = 2;
    private static final int RECT_CORNER_POINTS_COORDS = 8;
    private static final String TAG = "TransformImageView";
    protected boolean mBitmapDecoded;
    protected boolean mBitmapLaidOut;
    protected final float[] mCurrentImageCenter;
    protected final float[] mCurrentImageCorners;
    protected Matrix mCurrentImageMatrix;
    /* access modifiers changed from: private */
    public ExifInfo mExifInfo;
    /* access modifiers changed from: private */
    public String mImageInputPath;
    /* access modifiers changed from: private */
    public String mImageOutputPath;
    private float[] mInitialImageCenter;
    private float[] mInitialImageCorners;
    private final float[] mMatrixValues;
    private int mMaxBitmapSize;
    protected int mThisHeight;
    protected int mThisWidth;
    protected TransformImageListener mTransformImageListener;

    public interface TransformImageListener {
        void onLoadComplete();

        void onLoadFailure(@NonNull Exception exc);

        void onRotate(float f);

        void onScale(float f);
    }

    public TransformImageView(Context context) {
        this(context, (AttributeSet) null);
    }

    public TransformImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TransformImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mCurrentImageCorners = new float[8];
        this.mCurrentImageCenter = new float[2];
        this.mMatrixValues = new float[9];
        this.mCurrentImageMatrix = new Matrix();
        this.mBitmapDecoded = false;
        this.mBitmapLaidOut = false;
        this.mMaxBitmapSize = 0;
        init();
    }

    public void setTransformImageListener(TransformImageListener transformImageListener) {
        this.mTransformImageListener = transformImageListener;
    }

    public void setScaleType(ImageView.ScaleType scaleType) {
        if (scaleType == ImageView.ScaleType.MATRIX) {
            super.setScaleType(scaleType);
        } else {
            Log.w(TAG, "Invalid ScaleType. Only ScaleType.MATRIX can be used");
        }
    }

    public void setMaxBitmapSize(int maxBitmapSize) {
        this.mMaxBitmapSize = maxBitmapSize;
    }

    public int getMaxBitmapSize() {
        if (this.mMaxBitmapSize <= 0) {
            this.mMaxBitmapSize = BitmapLoadUtils.calculateMaxBitmapSize(getContext());
        }
        return this.mMaxBitmapSize;
    }

    public void setImageBitmap(Bitmap bitmap) {
        setImageDrawable(new FastBitmapDrawable(bitmap));
    }

    public String getImageInputPath() {
        return this.mImageInputPath;
    }

    public String getImageOutputPath() {
        return this.mImageOutputPath;
    }

    public ExifInfo getExifInfo() {
        return this.mExifInfo;
    }

    public void setImageUri(@NonNull Uri imageUri, @Nullable Uri outputUri) throws Exception {
        int maxBitmapSize = getMaxBitmapSize();
        BitmapLoadUtils.decodeBitmapInBackground(getContext(), imageUri, outputUri, maxBitmapSize, maxBitmapSize, new BitmapLoadCallback() {
            public void onBitmapLoaded(@NonNull Bitmap bitmap, @NonNull ExifInfo exifInfo, @NonNull String imageInputPath, @Nullable String imageOutputPath) {
                String unused = TransformImageView.this.mImageInputPath = imageInputPath;
                String unused2 = TransformImageView.this.mImageOutputPath = imageOutputPath;
                ExifInfo unused3 = TransformImageView.this.mExifInfo = exifInfo;
                TransformImageView.this.mBitmapDecoded = true;
                TransformImageView.this.setImageBitmap(bitmap);
            }

            public void onFailure(@NonNull Exception bitmapWorkerException) {
                Log.e(TransformImageView.TAG, "onFailure: setImageUri", bitmapWorkerException);
                if (TransformImageView.this.mTransformImageListener != null) {
                    TransformImageView.this.mTransformImageListener.onLoadFailure(bitmapWorkerException);
                }
            }
        });
    }

    public float getCurrentScale() {
        return getMatrixScale(this.mCurrentImageMatrix);
    }

    public float getMatrixScale(@NonNull Matrix matrix) {
        return (float) Math.sqrt(Math.pow((double) getMatrixValue(matrix, 0), 2.0d) + Math.pow((double) getMatrixValue(matrix, 3), 2.0d));
    }

    public float getCurrentAngle() {
        return getMatrixAngle(this.mCurrentImageMatrix);
    }

    public float getMatrixAngle(@NonNull Matrix matrix) {
        return (float) (-(Math.atan2((double) getMatrixValue(matrix, 1), (double) getMatrixValue(matrix, 0)) * 57.29577951308232d));
    }

    public void setImageMatrix(Matrix matrix) {
        super.setImageMatrix(matrix);
        this.mCurrentImageMatrix.set(matrix);
        updateCurrentImagePoints();
    }

    @Nullable
    public Bitmap getViewBitmap() {
        if (getDrawable() == null || !(getDrawable() instanceof FastBitmapDrawable)) {
            return null;
        }
        return ((FastBitmapDrawable) getDrawable()).getBitmap();
    }

    public void postTranslate(float deltaX, float deltaY) {
        if (deltaX != 0.0f || deltaY != 0.0f) {
            this.mCurrentImageMatrix.postTranslate(deltaX, deltaY);
            setImageMatrix(this.mCurrentImageMatrix);
        }
    }

    public void postScale(float deltaScale, float px, float py) {
        if (deltaScale != 0.0f) {
            this.mCurrentImageMatrix.postScale(deltaScale, deltaScale, px, py);
            setImageMatrix(this.mCurrentImageMatrix);
            if (this.mTransformImageListener != null) {
                this.mTransformImageListener.onScale(getMatrixScale(this.mCurrentImageMatrix));
            }
        }
    }

    public void postRotate(float deltaAngle, float px, float py) {
        if (deltaAngle != 0.0f) {
            this.mCurrentImageMatrix.postRotate(deltaAngle, px, py);
            setImageMatrix(this.mCurrentImageMatrix);
            if (this.mTransformImageListener != null) {
                this.mTransformImageListener.onRotate(getMatrixAngle(this.mCurrentImageMatrix));
            }
        }
    }

    /* access modifiers changed from: protected */
    public void init() {
        setScaleType(ImageView.ScaleType.MATRIX);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed || (this.mBitmapDecoded && !this.mBitmapLaidOut)) {
            int left2 = getPaddingLeft();
            int top2 = getPaddingTop();
            int right2 = getWidth() - getPaddingRight();
            int bottom2 = getHeight() - getPaddingBottom();
            this.mThisWidth = right2 - left2;
            this.mThisHeight = bottom2 - top2;
            onImageLaidOut();
        }
    }

    /* access modifiers changed from: protected */
    public void onImageLaidOut() {
        Drawable drawable = getDrawable();
        if (drawable != null) {
            float w = (float) drawable.getIntrinsicWidth();
            float h = (float) drawable.getIntrinsicHeight();
            Log.d(TAG, String.format("Image size: [%d:%d]", new Object[]{Integer.valueOf((int) w), Integer.valueOf((int) h)}));
            RectF initialImageRect = new RectF(0.0f, 0.0f, w, h);
            this.mInitialImageCorners = RectUtils.getCornersFromRect(initialImageRect);
            this.mInitialImageCenter = RectUtils.getCenterFromRect(initialImageRect);
            this.mBitmapLaidOut = true;
            if (this.mTransformImageListener != null) {
                this.mTransformImageListener.onLoadComplete();
            }
        }
    }

    /* access modifiers changed from: protected */
    public float getMatrixValue(@NonNull Matrix matrix, @IntRange(from = 0, mo8779to = 9) int valueIndex) {
        matrix.getValues(this.mMatrixValues);
        return this.mMatrixValues[valueIndex];
    }

    /* access modifiers changed from: protected */
    public void printMatrix(@NonNull String logPrefix, @NonNull Matrix matrix) {
        float x = getMatrixValue(matrix, 2);
        float y = getMatrixValue(matrix, 5);
        float rScale = getMatrixScale(matrix);
        Log.d(TAG, logPrefix + ": matrix: { x: " + x + ", y: " + y + ", scale: " + rScale + ", angle: " + getMatrixAngle(matrix) + " }");
    }

    private void updateCurrentImagePoints() {
        this.mCurrentImageMatrix.mapPoints(this.mCurrentImageCorners, this.mInitialImageCorners);
        this.mCurrentImageMatrix.mapPoints(this.mCurrentImageCenter, this.mInitialImageCenter);
    }
}
