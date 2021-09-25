package com.yalantis.ucrop.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.yalantis.ucrop.C1654R;
import com.yalantis.ucrop.callback.OverlayViewChangeListener;
import com.yalantis.ucrop.util.RectUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class OverlayView extends View {
    public static final boolean DEFAULT_CIRCLE_DIMMED_LAYER = false;
    public static final int DEFAULT_CROP_GRID_COLUMN_COUNT = 2;
    public static final int DEFAULT_CROP_GRID_ROW_COUNT = 2;
    public static final int DEFAULT_FREESTYLE_CROP_MODE = 0;
    public static final boolean DEFAULT_SHOW_CROP_FRAME = true;
    public static final boolean DEFAULT_SHOW_CROP_GRID = true;
    public static final int FREESTYLE_CROP_MODE_DISABLE = 0;
    public static final int FREESTYLE_CROP_MODE_ENABLE = 1;
    public static final int FREESTYLE_CROP_MODE_ENABLE_WITH_PASS_THROUGH = 2;
    private OverlayViewChangeListener mCallback;
    private boolean mCircleDimmedLayer;
    private Path mCircularPath;
    private Paint mCropFrameCornersPaint;
    private Paint mCropFramePaint;
    protected float[] mCropGridCenter;
    private int mCropGridColumnCount;
    protected float[] mCropGridCorners;
    private Paint mCropGridPaint;
    private int mCropGridRowCount;
    private int mCropRectCornerTouchAreaLineLength;
    private int mCropRectMinSize;
    private final RectF mCropViewRect;
    private int mCurrentTouchCornerIndex;
    private int mDimmedColor;
    private Paint mDimmedStrokePaint;
    private int mFreestyleCropMode;
    private float[] mGridPoints;
    private float mPreviousTouchX;
    private float mPreviousTouchY;
    private boolean mShouldSetupCropBounds;
    private boolean mShowCropFrame;
    private boolean mShowCropGrid;
    private float mTargetAspectRatio;
    private final RectF mTempRect;
    protected int mThisHeight;
    protected int mThisWidth;
    private int mTouchPointThreshold;

    @Retention(RetentionPolicy.SOURCE)
    public @interface FreestyleMode {
    }

    public OverlayView(Context context) {
        this(context, (AttributeSet) null);
    }

    public OverlayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OverlayView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mCropViewRect = new RectF();
        this.mTempRect = new RectF();
        this.mGridPoints = null;
        this.mCircularPath = new Path();
        this.mDimmedStrokePaint = new Paint(1);
        this.mCropGridPaint = new Paint(1);
        this.mCropFramePaint = new Paint(1);
        this.mCropFrameCornersPaint = new Paint(1);
        this.mFreestyleCropMode = 0;
        this.mPreviousTouchX = -1.0f;
        this.mPreviousTouchY = -1.0f;
        this.mCurrentTouchCornerIndex = -1;
        this.mTouchPointThreshold = getResources().getDimensionPixelSize(C1654R.dimen.ucrop_default_crop_rect_corner_touch_threshold);
        this.mCropRectMinSize = getResources().getDimensionPixelSize(C1654R.dimen.ucrop_default_crop_rect_min_size);
        this.mCropRectCornerTouchAreaLineLength = getResources().getDimensionPixelSize(C1654R.dimen.ucrop_default_crop_rect_corner_touch_area_line_length);
        init();
    }

    public OverlayViewChangeListener getOverlayViewChangeListener() {
        return this.mCallback;
    }

    public void setOverlayViewChangeListener(OverlayViewChangeListener callback) {
        this.mCallback = callback;
    }

    @NonNull
    public RectF getCropViewRect() {
        return this.mCropViewRect;
    }

    @Deprecated
    public boolean isFreestyleCropEnabled() {
        return this.mFreestyleCropMode == 1;
    }

    @Deprecated
    public void setFreestyleCropEnabled(boolean freestyleCropEnabled) {
        this.mFreestyleCropMode = freestyleCropEnabled ? 1 : 0;
    }

    public int getFreestyleCropMode() {
        return this.mFreestyleCropMode;
    }

    public void setFreestyleCropMode(int mFreestyleCropMode2) {
        this.mFreestyleCropMode = mFreestyleCropMode2;
        postInvalidate();
    }

    public void setCircleDimmedLayer(boolean circleDimmedLayer) {
        this.mCircleDimmedLayer = circleDimmedLayer;
    }

    public void setCropGridRowCount(@IntRange(from = 0) int cropGridRowCount) {
        this.mCropGridRowCount = cropGridRowCount;
        this.mGridPoints = null;
    }

    public void setCropGridColumnCount(@IntRange(from = 0) int cropGridColumnCount) {
        this.mCropGridColumnCount = cropGridColumnCount;
        this.mGridPoints = null;
    }

    public void setShowCropFrame(boolean showCropFrame) {
        this.mShowCropFrame = showCropFrame;
    }

    public void setShowCropGrid(boolean showCropGrid) {
        this.mShowCropGrid = showCropGrid;
    }

    public void setDimmedColor(@ColorInt int dimmedColor) {
        this.mDimmedColor = dimmedColor;
    }

    public void setCropFrameStrokeWidth(@IntRange(from = 0) int width) {
        this.mCropFramePaint.setStrokeWidth((float) width);
    }

    public void setCropGridStrokeWidth(@IntRange(from = 0) int width) {
        this.mCropGridPaint.setStrokeWidth((float) width);
    }

    public void setCropFrameColor(@ColorInt int color) {
        this.mCropFramePaint.setColor(color);
    }

    public void setCropGridColor(@ColorInt int color) {
        this.mCropGridPaint.setColor(color);
    }

    public void setTargetAspectRatio(float targetAspectRatio) {
        this.mTargetAspectRatio = targetAspectRatio;
        if (this.mThisWidth > 0) {
            setupCropBounds();
            postInvalidate();
            return;
        }
        this.mShouldSetupCropBounds = true;
    }

    public void setupCropBounds() {
        int height = (int) (((float) this.mThisWidth) / this.mTargetAspectRatio);
        if (height > this.mThisHeight) {
            int width = (int) (((float) this.mThisHeight) * this.mTargetAspectRatio);
            int halfDiff = (this.mThisWidth - width) / 2;
            this.mCropViewRect.set((float) (getPaddingLeft() + halfDiff), (float) getPaddingTop(), (float) (getPaddingLeft() + width + halfDiff), (float) (getPaddingTop() + this.mThisHeight));
        } else {
            int halfDiff2 = (this.mThisHeight - height) / 2;
            this.mCropViewRect.set((float) getPaddingLeft(), (float) (getPaddingTop() + halfDiff2), (float) (getPaddingLeft() + this.mThisWidth), (float) (getPaddingTop() + height + halfDiff2));
        }
        if (this.mCallback != null) {
            this.mCallback.onCropRectUpdated(this.mCropViewRect);
        }
        updateGridPoints();
    }

    private void updateGridPoints() {
        this.mCropGridCorners = RectUtils.getCornersFromRect(this.mCropViewRect);
        this.mCropGridCenter = RectUtils.getCenterFromRect(this.mCropViewRect);
        this.mGridPoints = null;
        this.mCircularPath.reset();
        this.mCircularPath.addCircle(this.mCropViewRect.centerX(), this.mCropViewRect.centerY(), Math.min(this.mCropViewRect.width(), this.mCropViewRect.height()) / 2.0f, Path.Direction.CW);
    }

    /* access modifiers changed from: protected */
    public void init() {
        if (Build.VERSION.SDK_INT < 18 && Build.VERSION.SDK_INT >= 11) {
            setLayerType(1, (Paint) null);
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            int left2 = getPaddingLeft();
            int top2 = getPaddingTop();
            int right2 = getWidth() - getPaddingRight();
            int bottom2 = getHeight() - getPaddingBottom();
            this.mThisWidth = right2 - left2;
            this.mThisHeight = bottom2 - top2;
            if (this.mShouldSetupCropBounds) {
                this.mShouldSetupCropBounds = false;
                setTargetAspectRatio(this.mTargetAspectRatio);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawDimmedLayer(canvas);
        drawCropGrid(canvas);
    }

    public boolean onTouchEvent(MotionEvent event) {
        boolean shouldHandle = true;
        if (this.mCropViewRect.isEmpty() || this.mFreestyleCropMode == 0) {
            return false;
        }
        float x = event.getX();
        float y = event.getY();
        if ((event.getAction() & 255) == 0) {
            this.mCurrentTouchCornerIndex = getCurrentTouchIndex(x, y);
            if (this.mCurrentTouchCornerIndex == -1) {
                shouldHandle = false;
            }
            if (!shouldHandle) {
                this.mPreviousTouchX = -1.0f;
                this.mPreviousTouchY = -1.0f;
                return shouldHandle;
            } else if (this.mPreviousTouchX >= 0.0f) {
                return shouldHandle;
            } else {
                this.mPreviousTouchX = x;
                this.mPreviousTouchY = y;
                return shouldHandle;
            }
        } else if ((event.getAction() & 255) == 2 && event.getPointerCount() == 1 && this.mCurrentTouchCornerIndex != -1) {
            float x2 = Math.min(Math.max(x, (float) getPaddingLeft()), (float) (getWidth() - getPaddingRight()));
            float y2 = Math.min(Math.max(y, (float) getPaddingTop()), (float) (getHeight() - getPaddingBottom()));
            updateCropViewRect(x2, y2);
            this.mPreviousTouchX = x2;
            this.mPreviousTouchY = y2;
            return true;
        } else {
            if ((event.getAction() & 255) == 1) {
                this.mPreviousTouchX = -1.0f;
                this.mPreviousTouchY = -1.0f;
                this.mCurrentTouchCornerIndex = -1;
                if (this.mCallback != null) {
                    this.mCallback.onCropRectUpdated(this.mCropViewRect);
                }
            }
            return false;
        }
    }

    private void updateCropViewRect(float touchX, float touchY) {
        boolean changeHeight;
        boolean changeWidth;
        float f;
        float f2;
        float f3;
        float f4;
        this.mTempRect.set(this.mCropViewRect);
        switch (this.mCurrentTouchCornerIndex) {
            case 0:
                this.mTempRect.set(touchX, touchY, this.mCropViewRect.right, this.mCropViewRect.bottom);
                break;
            case 1:
                this.mTempRect.set(this.mCropViewRect.left, touchY, touchX, this.mCropViewRect.bottom);
                break;
            case 2:
                this.mTempRect.set(this.mCropViewRect.left, this.mCropViewRect.top, touchX, touchY);
                break;
            case 3:
                this.mTempRect.set(touchX, this.mCropViewRect.top, this.mCropViewRect.right, touchY);
                break;
            case 4:
                this.mTempRect.offset(touchX - this.mPreviousTouchX, touchY - this.mPreviousTouchY);
                if (this.mTempRect.left > ((float) getLeft()) && this.mTempRect.top > ((float) getTop()) && this.mTempRect.right < ((float) getRight()) && this.mTempRect.bottom < ((float) getBottom())) {
                    this.mCropViewRect.set(this.mTempRect);
                    updateGridPoints();
                    postInvalidate();
                    return;
                }
                return;
        }
        if (this.mTempRect.height() >= ((float) this.mCropRectMinSize)) {
            changeHeight = true;
        } else {
            changeHeight = false;
        }
        if (this.mTempRect.width() >= ((float) this.mCropRectMinSize)) {
            changeWidth = true;
        } else {
            changeWidth = false;
        }
        RectF rectF = this.mCropViewRect;
        if (changeWidth) {
            f = this.mTempRect.left;
        } else {
            f = this.mCropViewRect.left;
        }
        if (changeHeight) {
            f2 = this.mTempRect.top;
        } else {
            f2 = this.mCropViewRect.top;
        }
        if (changeWidth) {
            f3 = this.mTempRect.right;
        } else {
            f3 = this.mCropViewRect.right;
        }
        if (changeHeight) {
            f4 = this.mTempRect.bottom;
        } else {
            f4 = this.mCropViewRect.bottom;
        }
        rectF.set(f, f2, f3, f4);
        if (changeHeight || changeWidth) {
            updateGridPoints();
            postInvalidate();
        }
    }

    private int getCurrentTouchIndex(float touchX, float touchY) {
        int closestPointIndex = -1;
        double closestPointDistance = (double) this.mTouchPointThreshold;
        for (int i = 0; i < 8; i += 2) {
            double distanceToCorner = Math.sqrt(Math.pow((double) (touchX - this.mCropGridCorners[i]), 2.0d) + Math.pow((double) (touchY - this.mCropGridCorners[i + 1]), 2.0d));
            if (distanceToCorner < closestPointDistance) {
                closestPointDistance = distanceToCorner;
                closestPointIndex = i / 2;
            }
        }
        if (this.mFreestyleCropMode != 1 || closestPointIndex >= 0 || !this.mCropViewRect.contains(touchX, touchY)) {
            return closestPointIndex;
        }
        return 4;
    }

    /* access modifiers changed from: protected */
    public void drawDimmedLayer(@NonNull Canvas canvas) {
        canvas.save();
        if (this.mCircleDimmedLayer) {
            canvas.clipPath(this.mCircularPath, Region.Op.DIFFERENCE);
        } else {
            canvas.clipRect(this.mCropViewRect, Region.Op.DIFFERENCE);
        }
        canvas.drawColor(this.mDimmedColor);
        canvas.restore();
        if (this.mCircleDimmedLayer) {
            canvas.drawCircle(this.mCropViewRect.centerX(), this.mCropViewRect.centerY(), Math.min(this.mCropViewRect.width(), this.mCropViewRect.height()) / 2.0f, this.mDimmedStrokePaint);
        }
    }

    /* access modifiers changed from: protected */
    public void drawCropGrid(@NonNull Canvas canvas) {
        if (this.mShowCropGrid) {
            if (this.mGridPoints == null && !this.mCropViewRect.isEmpty()) {
                this.mGridPoints = new float[((this.mCropGridRowCount * 4) + (this.mCropGridColumnCount * 4))];
                int index = 0;
                for (int i = 0; i < this.mCropGridRowCount; i++) {
                    int index2 = index + 1;
                    this.mGridPoints[index] = this.mCropViewRect.left;
                    int index3 = index2 + 1;
                    this.mGridPoints[index2] = (this.mCropViewRect.height() * ((((float) i) + 1.0f) / ((float) (this.mCropGridRowCount + 1)))) + this.mCropViewRect.top;
                    int index4 = index3 + 1;
                    this.mGridPoints[index3] = this.mCropViewRect.right;
                    index = index4 + 1;
                    this.mGridPoints[index4] = (this.mCropViewRect.height() * ((((float) i) + 1.0f) / ((float) (this.mCropGridRowCount + 1)))) + this.mCropViewRect.top;
                }
                for (int i2 = 0; i2 < this.mCropGridColumnCount; i2++) {
                    int index5 = index + 1;
                    this.mGridPoints[index] = (this.mCropViewRect.width() * ((((float) i2) + 1.0f) / ((float) (this.mCropGridColumnCount + 1)))) + this.mCropViewRect.left;
                    int index6 = index5 + 1;
                    this.mGridPoints[index5] = this.mCropViewRect.top;
                    int index7 = index6 + 1;
                    this.mGridPoints[index6] = (this.mCropViewRect.width() * ((((float) i2) + 1.0f) / ((float) (this.mCropGridColumnCount + 1)))) + this.mCropViewRect.left;
                    index = index7 + 1;
                    this.mGridPoints[index7] = this.mCropViewRect.bottom;
                }
            }
            if (this.mGridPoints != null) {
                canvas.drawLines(this.mGridPoints, this.mCropGridPaint);
            }
        }
        if (this.mShowCropFrame) {
            canvas.drawRect(this.mCropViewRect, this.mCropFramePaint);
        }
        if (this.mFreestyleCropMode != 0) {
            canvas.save();
            this.mTempRect.set(this.mCropViewRect);
            this.mTempRect.inset((float) this.mCropRectCornerTouchAreaLineLength, (float) (-this.mCropRectCornerTouchAreaLineLength));
            canvas.clipRect(this.mTempRect, Region.Op.DIFFERENCE);
            this.mTempRect.set(this.mCropViewRect);
            this.mTempRect.inset((float) (-this.mCropRectCornerTouchAreaLineLength), (float) this.mCropRectCornerTouchAreaLineLength);
            canvas.clipRect(this.mTempRect, Region.Op.DIFFERENCE);
            canvas.drawRect(this.mCropViewRect, this.mCropFrameCornersPaint);
            canvas.restore();
        }
    }

    /* access modifiers changed from: protected */
    public void processStyledAttributes(@NonNull TypedArray a) {
        this.mCircleDimmedLayer = a.getBoolean(C1654R.styleable.ucrop_UCropView_ucrop_circle_dimmed_layer, false);
        this.mDimmedColor = a.getColor(C1654R.styleable.ucrop_UCropView_ucrop_dimmed_color, getResources().getColor(C1654R.color.ucrop_color_default_dimmed));
        this.mDimmedStrokePaint.setColor(this.mDimmedColor);
        this.mDimmedStrokePaint.setStyle(Paint.Style.STROKE);
        this.mDimmedStrokePaint.setStrokeWidth(1.0f);
        initCropFrameStyle(a);
        this.mShowCropFrame = a.getBoolean(C1654R.styleable.ucrop_UCropView_ucrop_show_frame, true);
        initCropGridStyle(a);
        this.mShowCropGrid = a.getBoolean(C1654R.styleable.ucrop_UCropView_ucrop_show_grid, true);
    }

    private void initCropFrameStyle(@NonNull TypedArray a) {
        int cropFrameStrokeSize = a.getDimensionPixelSize(C1654R.styleable.ucrop_UCropView_ucrop_frame_stroke_size, getResources().getDimensionPixelSize(C1654R.dimen.ucrop_default_crop_frame_stoke_width));
        int cropFrameColor = a.getColor(C1654R.styleable.ucrop_UCropView_ucrop_frame_color, getResources().getColor(C1654R.color.ucrop_color_default_crop_frame));
        this.mCropFramePaint.setStrokeWidth((float) cropFrameStrokeSize);
        this.mCropFramePaint.setColor(cropFrameColor);
        this.mCropFramePaint.setStyle(Paint.Style.STROKE);
        this.mCropFrameCornersPaint.setStrokeWidth((float) (cropFrameStrokeSize * 3));
        this.mCropFrameCornersPaint.setColor(cropFrameColor);
        this.mCropFrameCornersPaint.setStyle(Paint.Style.STROKE);
    }

    private void initCropGridStyle(@NonNull TypedArray a) {
        int cropGridStrokeSize = a.getDimensionPixelSize(C1654R.styleable.ucrop_UCropView_ucrop_grid_stroke_size, getResources().getDimensionPixelSize(C1654R.dimen.ucrop_default_crop_grid_stoke_width));
        int cropGridColor = a.getColor(C1654R.styleable.ucrop_UCropView_ucrop_grid_color, getResources().getColor(C1654R.color.ucrop_color_default_crop_grid));
        this.mCropGridPaint.setStrokeWidth((float) cropGridStrokeSize);
        this.mCropGridPaint.setColor(cropGridColor);
        this.mCropGridRowCount = a.getInt(C1654R.styleable.ucrop_UCropView_ucrop_grid_row_count, 2);
        this.mCropGridColumnCount = a.getInt(C1654R.styleable.ucrop_UCropView_ucrop_grid_column_count, 2);
    }
}
