package org.opencv.android;

import android.annotation.TargetApi;
import android.hardware.Camera;
import android.os.Build;
import android.util.Log;
import com.freevisiontech.fvmobile.utility.Constants;
import java.io.IOException;
import java.util.List;

@TargetApi(15)
public class CameraRenderer extends CameraGLRendererBase {
    public static final String LOGTAG = "CameraRenderer";
    private Camera mCamera;
    private boolean mPreviewStarted = false;

    CameraRenderer(CameraGLSurfaceView view) {
        super(view);
    }

    /* access modifiers changed from: protected */
    public synchronized void closeCamera() {
        Log.i(LOGTAG, "closeCamera");
        if (this.mCamera != null) {
            this.mCamera.stopPreview();
            this.mPreviewStarted = false;
            this.mCamera.release();
            this.mCamera = null;
        }
    }

    /* access modifiers changed from: protected */
    public synchronized void openCamera(int id) {
        Log.i(LOGTAG, "openCamera");
        closeCamera();
        if (id == -1) {
            Log.d(LOGTAG, "Trying to open camera with old open()");
            try {
                this.mCamera = Camera.open();
            } catch (Exception e) {
                Log.e(LOGTAG, "Camera is not available (in use or does not exist): " + e.getLocalizedMessage());
            }
            if (this.mCamera == null && Build.VERSION.SDK_INT >= 9) {
                boolean connected = false;
                int camIdx = 0;
                while (camIdx < Camera.getNumberOfCameras()) {
                    Log.d(LOGTAG, "Trying to open camera with new open(" + camIdx + ")");
                    try {
                        this.mCamera = Camera.open(camIdx);
                        connected = true;
                    } catch (RuntimeException e2) {
                        Log.e(LOGTAG, "Camera #" + camIdx + "failed to open: " + e2.getLocalizedMessage());
                    }
                    if (!connected) {
                        camIdx++;
                    }
                }
            }
        } else if (Build.VERSION.SDK_INT >= 9) {
            int localCameraIndex = this.mCameraIndex;
            if (this.mCameraIndex == 99) {
                Log.i(LOGTAG, "Trying to open BACK camera");
                Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                int camIdx2 = 0;
                while (true) {
                    if (camIdx2 >= Camera.getNumberOfCameras()) {
                        break;
                    }
                    Camera.getCameraInfo(camIdx2, cameraInfo);
                    if (cameraInfo.facing == 0) {
                        localCameraIndex = camIdx2;
                        break;
                    }
                    camIdx2++;
                }
            } else if (this.mCameraIndex == 98) {
                Log.i(LOGTAG, "Trying to open FRONT camera");
                Camera.CameraInfo cameraInfo2 = new Camera.CameraInfo();
                int camIdx3 = 0;
                while (true) {
                    if (camIdx3 >= Camera.getNumberOfCameras()) {
                        break;
                    }
                    Camera.getCameraInfo(camIdx3, cameraInfo2);
                    if (cameraInfo2.facing == 1) {
                        localCameraIndex = camIdx3;
                        break;
                    }
                    camIdx3++;
                }
            }
            if (localCameraIndex == 99) {
                Log.e(LOGTAG, "Back camera not found!");
            } else if (localCameraIndex == 98) {
                Log.e(LOGTAG, "Front camera not found!");
            } else {
                Log.d(LOGTAG, "Trying to open camera with new open(" + localCameraIndex + ")");
                try {
                    this.mCamera = Camera.open(localCameraIndex);
                } catch (RuntimeException e3) {
                    Log.e(LOGTAG, "Camera #" + localCameraIndex + "failed to open: " + e3.getLocalizedMessage());
                }
            }
        }
        if (this.mCamera == null) {
            Log.e(LOGTAG, "Error: can't open camera");
        } else {
            Camera.Parameters params = this.mCamera.getParameters();
            List<String> FocusModes = params.getSupportedFocusModes();
            if (FocusModes != null && FocusModes.contains("continuous-video")) {
                params.setFocusMode("continuous-video");
            }
            this.mCamera.setParameters(params);
            try {
                this.mCamera.setPreviewTexture(this.mSTexture);
            } catch (IOException ioe) {
                Log.e(LOGTAG, "setPreviewTexture() failed: " + ioe.getMessage());
            }
        }
        return;
    }

    public synchronized void setCameraPreviewSize(int width, int height) {
        Log.i(LOGTAG, "setCameraPreviewSize: " + width + "x" + height);
        if (this.mCamera == null) {
            Log.e(LOGTAG, "Camera isn't initialized!");
        } else {
            if (this.mMaxCameraWidth > 0 && this.mMaxCameraWidth < width) {
                width = this.mMaxCameraWidth;
            }
            if (this.mMaxCameraHeight > 0 && this.mMaxCameraHeight < height) {
                height = this.mMaxCameraHeight;
            }
            Camera.Parameters param = this.mCamera.getParameters();
            List<Camera.Size> psize = param.getSupportedPreviewSizes();
            int bestWidth = 0;
            int bestHeight = 0;
            if (psize.size() > 0) {
                float aspect = ((float) width) / ((float) height);
                for (Camera.Size size : psize) {
                    int w = size.width;
                    int h = size.height;
                    Log.d(LOGTAG, "checking camera preview size: " + w + "x" + h);
                    if (w <= width && h <= height && w >= bestWidth && h >= bestHeight && ((double) Math.abs(aspect - (((float) w) / ((float) h)))) < 0.2d) {
                        bestWidth = w;
                        bestHeight = h;
                    }
                }
                if (bestWidth <= 0 || bestHeight <= 0) {
                    bestWidth = psize.get(0).width;
                    bestHeight = psize.get(0).height;
                    Log.e(LOGTAG, "Error: best size was not selected, using " + bestWidth + " x " + bestHeight);
                } else {
                    Log.i(LOGTAG, "Selected best size: " + bestWidth + " x " + bestHeight);
                }
                if (this.mPreviewStarted) {
                    this.mCamera.stopPreview();
                    this.mPreviewStarted = false;
                }
                this.mCameraWidth = bestWidth;
                this.mCameraHeight = bestHeight;
                param.setPreviewSize(bestWidth, bestHeight);
            }
            param.set("orientation", Constants.SCENE_MODE_LANDSCAPE);
            this.mCamera.setParameters(param);
            this.mCamera.startPreview();
            this.mPreviewStarted = true;
        }
    }
}
