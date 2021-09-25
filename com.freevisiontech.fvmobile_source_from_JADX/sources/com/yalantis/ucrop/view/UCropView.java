package com.yalantis.ucrop.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import com.yalantis.ucrop.C1654R;
import com.yalantis.ucrop.callback.CropBoundsChangeListener;
import com.yalantis.ucrop.callback.OverlayViewChangeListener;

public class UCropView extends FrameLayout {
    /* access modifiers changed from: private */
    public GestureCropImageView mGestureCropImageView;
    /* access modifiers changed from: private */
    public final OverlayView mViewOverlay;

    public UCropView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UCropView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(C1654R.layout.ucrop_view, this, true);
        this.mGestureCropImageView = (GestureCropImageView) findViewById(C1654R.C1656id.image_view_crop);
        this.mViewOverlay = (OverlayView) findViewById(C1654R.C1656id.view_overlay);
        TypedArray a = context.obtainStyledAttributes(attrs, C1654R.styleable.ucrop_UCropView);
        this.mViewOverlay.processStyledAttributes(a);
        this.mGestureCropImageView.processStyledAttributes(a);
        a.recycle();
        setListenersToViews();
    }

    private void setListenersToViews() {
        this.mGestureCropImageView.setCropBoundsChangeListener(new CropBoundsChangeListener() {
            public void onCropAspectRatioChanged(float cropRatio) {
                UCropView.this.mViewOverlay.setTargetAspectRatio(cropRatio);
            }
        });
        this.mViewOverlay.setOverlayViewChangeListener(new OverlayViewChangeListener() {
            public void onCropRectUpdated(RectF cropRect) {
                UCropView.this.mGestureCropImageView.setCropRect(cropRect);
            }
        });
    }

    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @NonNull
    public GestureCropImageView getCropImageView() {
        return this.mGestureCropImageView;
    }

    @NonNull
    public OverlayView getOverlayView() {
        return this.mViewOverlay;
    }

    public void resetCropImageView() {
        removeView(this.mGestureCropImageView);
        this.mGestureCropImageView = new GestureCropImageView(getContext());
        setListenersToViews();
        this.mGestureCropImageView.setCropRect(getOverlayView().getCropViewRect());
        addView(this.mGestureCropImageView, 0);
    }
}
