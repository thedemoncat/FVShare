package com.freevisiontech.cameralib.view.camera1;

import android.content.Context;
import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import com.freevisiontech.cameralib.FVCamera1Manager;
import com.freevisiontech.cameralib.utils.CameraUtils;
import java.io.IOException;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageView;

public class CameraView extends com.freevisiontech.cameralib.view.CameraView implements SurfaceHolder.Callback {
    private static final String TAG = "Camera1.CameraView";
    public static int ViewStyle_GPUImageView = 1;
    public static int ViewStyle_SystemView = 2;
    public static int ViewStyle_UnKnown = 0;
    AttributeSet attributeSet;
    FVCamera1Manager cameraManager = null;
    CameraSurfaceView cameraSurfaceView;
    int currentViewStype = ViewStyle_UnKnown;
    GPUImageView gpuImageView;
    private boolean hastakenullphoto4syscamerainit = false;

    public CameraView(@NonNull Context context) {
        super(context);
    }

    public CameraView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.attributeSet = attrs;
    }

    public void setCameraManager(FVCamera1Manager manager) {
        this.cameraManager = manager;
    }

    public void addGPUImageView() {
        if (this.gpuImageView == null) {
            this.gpuImageView = new GPUImageView(getContext(), this.attributeSet);
        }
        removeView(this.cameraSurfaceView);
        addView(this.gpuImageView);
        this.currentViewStype = ViewStyle_GPUImageView;
    }

    public int getCurrentViewStype() {
        return this.currentViewStype;
    }

    public AttributeSet getAttributeSet() {
        return this.attributeSet;
    }

    public GPUImageView getGpuImageView() {
        return this.gpuImageView;
    }

    public void removeGPUImageView() {
        removeView(this.gpuImageView);
    }

    public void removeSysSurfaceView() {
        removeView(this.cameraSurfaceView);
    }

    public void addCameraSurfaceView() {
        if (this.cameraSurfaceView == null) {
            this.cameraSurfaceView = new CameraSurfaceView(getContext(), this.attributeSet, this);
        }
        removeView(this.gpuImageView);
        addView(this.cameraSurfaceView);
        this.currentViewStype = ViewStyle_SystemView;
    }

    public CameraSurfaceView getCameraSurfaceView() {
        return this.cameraSurfaceView;
    }

    public Surface getSurface() {
        if (this.currentViewStype == ViewStyle_GPUImageView) {
            return this.gpuImageView.getGLSurfaceView().getHolder().getSurface();
        }
        if (this.currentViewStype == ViewStyle_SystemView) {
            return this.cameraSurfaceView.getHolder().getSurface();
        }
        return null;
    }

    public boolean bind2Camera() {
        Camera camera = this.cameraManager.getCamera();
        int rotate = this.cameraManager.getInternal().getCameraRotation(this.currentViewStype);
        boolean flipVertical = false;
        boolean flipHorizontal = this.cameraManager.isflipHorizontal();
        if (this.currentViewStype == ViewStyle_GPUImageView) {
            this.gpuImageView.getGPUImage().deleteImage();
            if (this.cameraManager.getFacing() == 1) {
                flipHorizontal = false;
                flipVertical = true;
            }
            this.gpuImageView.getGPUImage().setUpCamera(camera, rotate, flipHorizontal, flipVertical);
        } else if (this.currentViewStype == ViewStyle_SystemView) {
            try {
                camera.setDisplayOrientation(rotate);
                camera.setPreviewDisplay(getCameraSurfaceView().getHolder());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public void onResume() {
        if (this.currentViewStype == ViewStyle_GPUImageView) {
            this.cameraManager.setupCamera();
        }
    }

    public void onPause() {
        if (this.currentViewStype == ViewStyle_GPUImageView) {
            this.cameraManager.stopCamera();
        }
    }

    public void switchViewStyle(int newviewstyle) {
        if (newviewstyle != this.currentViewStype) {
            if (newviewstyle == ViewStyle_GPUImageView) {
                addGPUImageView();
                this.cameraManager.setupCamera();
            } else if (newviewstyle == ViewStyle_SystemView) {
                addCameraSurfaceView();
            }
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        CameraUtils.LogV(TAG, "surfaceCreated...");
        if (this.currentViewStype == ViewStyle_SystemView) {
            this.cameraManager.setupCamera();
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        CameraUtils.LogV(TAG, "surfaceDestroyed...");
        if (getCurrentViewStype() == ViewStyle_SystemView) {
            this.cameraManager.stopCamera();
        }
    }
}
