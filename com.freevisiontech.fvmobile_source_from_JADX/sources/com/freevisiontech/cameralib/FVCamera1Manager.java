package com.freevisiontech.cameralib;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceView;
import com.freevisiontech.cameralib.experi.SlowShutter;
import com.freevisiontech.cameralib.impl.Camera1.Camera1Constants;
import com.freevisiontech.cameralib.impl.Camera1.CameraManager;
import com.freevisiontech.cameralib.utils.CameraUtils;
import com.freevisiontech.cameralib.view.camera1.CameraView;
import com.freevisiontech.filterlib.FVFilterManager;
import com.freevisiontech.filterlib.FilterType;
import com.freevisiontech.tracking.FVTrackManager;
import com.freevisiontech.tracking.FVTrackObserver;
import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImage;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageView;

public class FVCamera1Manager {
    private static final String TAG = "FVCamera1Manager";
    private boolean hastakenullphoto4largesizepictake = false;
    public FVCamereManagerCallback mCallback;
    private CameraView mCameraView;
    private CameraManager mCameramanager;
    private int mCamereManagerMode = 1;
    private Activity mContext;
    private GPUImageFilter mFilter;
    private FilterType mFilterType;
    private SlowShutter mSlowShutter = null;
    private FVTrackManager trackManager = null;

    public FVCamera1Manager(Activity context, CameraView cameraView, FVCamereManagerCallback callback) {
        this.mContext = context;
        this.mCameraView = cameraView;
        this.mCallback = callback;
        this.mCameramanager = new CameraManager(this.mContext, this);
        this.mFilterType = FilterType.NOFILTER;
        this.mFilter = new GPUImageFilter();
        this.mCameramanager.setPerviewView(this.mCameraView);
        if (this.mCamereManagerMode == 0) {
            cameraView.addGPUImageView();
        } else {
            cameraView.addCameraSurfaceView();
        }
    }

    public void setPreviewCallback(Camera.PreviewCallback cb) {
        getCamera().setPreviewCallback(cb);
    }

    public GPUImageView getGpuImageView() {
        return this.mCameraView.getGpuImageView();
    }

    public GPUImage getGPUImage() {
        GPUImageView v = getGpuImageView();
        if (v == null) {
            return null;
        }
        return v.getGPUImage();
    }

    public SurfaceView getCameraSurfaceView() {
        return this.mCameraView.getCameraSurfaceView();
    }

    public boolean isCameraOpened() {
        return this.mCameramanager.isCameraOpened();
    }

    public void destroy() {
        stopCamera();
    }

    public void switchCameraManagerMode() {
        if (this.mCamereManagerMode == 1) {
            changeCameraManagerMode(0);
        } else {
            changeCameraManagerMode(1);
        }
    }

    public int getCurrentCameraMode() {
        return this.mCamereManagerMode;
    }

    public void changeCameraManagerMode(int newCameraManagermode) {
        if (this.mCamereManagerMode != newCameraManagermode) {
            if (this.mCameramanager.isCameraOpened()) {
                this.mCameramanager.stopCamera();
            }
            this.mCamereManagerMode = newCameraManagermode;
            if (this.mCamereManagerMode == 0) {
                this.mCameraView.switchViewStyle(CameraView.ViewStyle_GPUImageView);
            } else if (this.mCamereManagerMode == 1) {
                this.mCameraView.switchViewStyle(CameraView.ViewStyle_SystemView);
            }
        }
    }

    public boolean setupCamera() {
        int facing = this.mCameramanager.getFacing();
        return setupCamera(facing, this.mCameramanager.getPreviewResolution(facing));
    }

    public boolean setupCamera(int face, Size prevsize) {
        if (!this.mCameramanager.startCamera(true, face, prevsize)) {
            return false;
        }
        this.mCameraView.bind2Camera();
        if (this.mCameraView.getCurrentViewStype() == 1 && this.mFilterType != FilterType.NOFILTER) {
            setFilter(this.mFilterType);
        }
        if (this.mCallback == null) {
            return true;
        }
        this.mCallback.onCameraStarted();
        return true;
    }

    public void stopCamera() {
        if (this.mCameramanager.getCamera() != null) {
            stopMediaRecord();
            stopTrack();
            this.mCameramanager.stopCamera();
            if (this.mCallback != null) {
                this.mCallback.onCameraClosed();
            }
        }
    }

    public void setFacing(int facing) {
        setFacing(facing, this.mCameramanager.getPreviewResolution(facing));
    }

    private void setFacing(int facing, Size res) {
        if (this.mCameramanager.setFacing(facing, res)) {
            this.mCameraView.bind2Camera();
        }
    }

    public int getFacing() {
        return this.mCameramanager.getFacing();
    }

    public boolean setFilter(FilterType filterType) {
        if (this.mCameraView.getCurrentViewStype() != CameraView.ViewStyle_GPUImageView || getGpuImageView() == null) {
            return false;
        }
        this.mFilter = FVFilterManager.createGPUImageFilterForType(this.mContext, filterType);
        this.mFilterType = filterType;
        getGpuImageView().setFilter(this.mFilter);
        return true;
    }

    public GPUImageFilter getFilter() {
        return this.mFilter;
    }

    public FilterType getFilterType() {
        return this.mFilterType;
    }

    public boolean changeFilterIntensity(int intensity) {
        if (this.mFilter == null) {
            return false;
        }
        this.mFilter = FVFilterManager.adjustGPUImageFilter(this.mFilter, intensity);
        return true;
    }

    public int getCameramode() {
        return this.mCameramanager.getCameramode();
    }

    public void changeCamreMode(int newCameraMode) {
        if (this.mCameramanager.getCameramode() != newCameraMode) {
            this.mCameramanager.changeCamreMode(newCameraMode);
        }
    }

    public Set<AspectRatio> getAllPreivewSupportedAspectRatios() {
        return this.mCameramanager.getSupportedAspectRatios(9);
    }

    public boolean setAspectRatio(AspectRatio ratio) {
        SortedSet<Size> set;
        if (!this.mCameramanager.getAspectRatio().equals(ratio) && (set = this.mCameramanager.getSupportedPreviewResolutions(2, ratio)) != null && !set.isEmpty()) {
            return setPreviewResolutionEx(set.last());
        }
        return false;
    }

    public Set<AspectRatio> getPicutreModeSupportedAspectRatios() {
        return this.mCameramanager.getPicutrePreviewSupportedAspectRatios();
    }

    public Set<AspectRatio> getVideoModeSupportedAspectRatios() {
        return this.mCameramanager.getVideoPreviewSupportedAspectRatios();
    }

    public AspectRatio getAspectRatio() {
        return this.mCameramanager.getAspectRatio();
    }

    public Size getPreviewResolution() {
        return this.mCameramanager.getPreviewResolution();
    }

    public boolean setPreviewResolution(Size res) {
        if (this.mCameraView.getCurrentViewStype() == CameraView.ViewStyle_GPUImageView) {
            return setPreviewResolutionEx(res);
        }
        if (this.mCameraView.getCurrentViewStype() == CameraView.ViewStyle_SystemView) {
            return setPreviewResolution0(res);
        }
        return false;
    }

    public boolean setPreviewResolution0(Size res) {
        boolean result = true;
        if (!this.mCameramanager.isContainPreviewResolution(res)) {
            return false;
        }
        Size s = this.mCameramanager.getPreviewResolution();
        if (s.getWidth() == res.getWidth() && s.getHeight() == res.getHeight()) {
            return false;
        }
        if (!this.mCameramanager.setPreviewResolution(res)) {
            result = false;
        }
        if (result && this.mCameraView.getCurrentViewStype() == CameraView.ViewStyle_GPUImageView) {
            getGPUImage().deleteImage();
        }
        return result;
    }

    public boolean setPreviewResolutionEx(Size res) {
        boolean result = true;
        if (!this.mCameramanager.isContainPreviewResolution(res)) {
            return false;
        }
        Size s = this.mCameramanager.getPreviewResolution();
        if (s.getWidth() == res.getWidth() && s.getHeight() == res.getHeight()) {
            return false;
        }
        if (!this.mCameramanager.setPreviewResolutionEx(res)) {
            result = false;
        }
        this.mCameraView.bind2Camera();
        return result;
    }

    public SizeMap getPictureModeSupportedResolutions() {
        return this.mCameramanager.getPicturePreviewSupportedResolutions();
    }

    public SizeMap getVideoModeSupportedResolutions() {
        return this.mCameramanager.getVideoPreviewSupportedResolutions();
    }

    public SortedSet<Size> getSupportedResolutions(int mode, AspectRatio ratio) {
        return this.mCameramanager.getSupportedPreviewResolutions(mode, ratio);
    }

    public SizeMap getSupportedResolutions(int mode) {
        return this.mCameramanager.getSupportedPreviewResolutions(mode);
    }

    public void setAutoFocusMode(boolean autoFocus) {
        if (this.mCameramanager.isShowingPreview()) {
            this.mCameramanager.setAutoFocusMode(autoFocus);
        }
    }

    public boolean setFocusMode(String mode) {
        return this.mCameramanager.setFocusMode(mode);
    }

    public boolean isAutoFocus() {
        return this.mCameramanager.isAutoFocus();
    }

    public void autoFocus(Rect focusarea, final Camera.AutoFocusCallback callback) {
        if (this.mCameramanager.isShowingPreview()) {
            this.mCameramanager.autoFocus(focusarea, new Camera.AutoFocusCallback() {
                public void onAutoFocus(boolean success, Camera camera) {
                    if (callback != null) {
                        callback.onAutoFocus(success, camera);
                    }
                }
            });
        }
    }

    public void autoFocus(Camera.AutoFocusCallback callback) {
        autoFocus((Rect) null, callback);
    }

    public String getFocusMode() {
        return this.mCameramanager.getFocusMode();
    }

    public boolean isSupportFocusArea() {
        return this.mCameramanager.isSupportFocusArea();
    }

    public List<Camera.Area> getFocusArea() {
        return this.mCameramanager.getFocusArea();
    }

    public void cancelAutoFocus() {
        this.mCameramanager.cancelAutoFocus();
    }

    public float getFocalLength() {
        return this.mCameramanager.getFocalLength();
    }

    public float[] getFocusDistance() {
        return this.mCameramanager.getFocusDistance();
    }

    public List<String> getSupportedFocusMode() {
        return this.mCameramanager.getSupportedFocusMode();
    }

    public String[] getSupportedManualFocusModes() {
        return this.mCameramanager.getSupportedManualFocusModes();
    }

    public int getCurFocusScale() {
        return this.mCameramanager.getCurFocusScale();
    }

    public void setFlashMode(int flash) {
        this.mCameramanager.setFlashMode(flash);
    }

    public int getFlashMode() {
        return this.mCameramanager.getFlashMode();
    }

    public Camera getCamera() {
        return this.mCameramanager.getCamera();
    }

    public boolean isZoomSupported() {
        return this.mCameramanager.isZoomSupported();
    }

    public boolean setZoom(int degree) {
        return this.mCameramanager.setZoom(degree);
    }

    public int getMaxZoom() {
        return this.mCameramanager.getMaxZoom();
    }

    public boolean startSmoothZoom(int degree) {
        return this.mCameramanager.startSmoothZoom(degree);
    }

    public void stopSmoothZoom() {
        this.mCameramanager.stopSmoothZoom();
    }

    public String[] getSupportedISOMode() {
        return this.mCameramanager.getSupportedISOMode();
    }

    public String getISOMode() {
        return this.mCameramanager.getISOMode();
    }

    public boolean setISO(String mode) {
        return this.mCameramanager.setISO(mode);
    }

    public boolean setSceneMode(String mode) {
        return this.mCameramanager.setSceneMode(mode);
    }

    public String getSceneMode() {
        return this.mCameramanager.getSceneMode();
    }

    public List<String> getSupportedSceneModes() {
        return this.mCameramanager.getSupportedSceneModes();
    }

    public boolean setFPS(int fps) {
        return this.mCameramanager.setFPS(fps);
    }

    public int getFPS() {
        return this.mCameramanager.getFPS();
    }

    public List<Integer> getSupportedPreviewFPS() {
        return this.mCameramanager.getSupportedPreviewFPS();
    }

    public boolean isSupportPreviewFPS(int fps) {
        return this.mCameramanager.isSupportPreviewFPS(fps);
    }

    public boolean isSupportHdrMode() {
        return this.mCameramanager.isSupportHdrMode();
    }

    public boolean setHdrMode(boolean ison) {
        return this.mCameramanager.setHdrScene(ison);
    }

    public int getExposureCompensation() {
        return this.mCameramanager.getExposureCompensation();
    }

    public boolean setExposureCompensation(int value) {
        int[] range = getExposureCompensationRanger();
        if (value < range[0] || value > range[1]) {
            return false;
        }
        return this.mCameramanager.setExposureCompensation(value);
    }

    public int[] getExposureCompensationRanger() {
        return this.mCameramanager.getExposureCompensationRanger();
    }

    public float getExposureValue() {
        return this.mCameramanager.getExposureValue();
    }

    public float getExposureCompensationStep() {
        return this.mCameramanager.getExposureCompensationStep();
    }

    public boolean setExposureTime(float value) {
        return this.mCameramanager.setExposureTime(value);
    }

    public boolean setHwExposure(String val) {
        return this.mCameramanager.setHwExposure(val);
    }

    public boolean setExposureValue(float value) {
        return this.mCameramanager.setExposureValue(value);
    }

    public boolean lockAutoExposure(boolean toogle) {
        return this.mCameramanager.lockAutoExposure(toogle);
    }

    public boolean isAutoExposureLock() {
        return this.mCameramanager.isAutoExposureLock();
    }

    public String getWhiteBalance() {
        return this.mCameramanager.getWhiteBalance();
    }

    public boolean setWhiteBalance(String mode) {
        return this.mCameramanager.setWhiteBalance(mode);
    }

    public boolean lockAutoWhiteBalance(boolean enable) {
        return this.mCameramanager.lockAutoWhiteBalance(enable);
    }

    public boolean isAutoWhiteBalanceLocked() {
        return this.mCameramanager.isAutoWhiteBalanceLocked();
    }

    public boolean startFaceDetection(Camera.FaceDetectionListener listener) {
        return this.mCameramanager.startFaceDetection(listener);
    }

    public String getColorEffect() {
        return this.mCameramanager.getColorEffect();
    }

    public List<String> getSupportedColorEffects() {
        return this.mCameramanager.getSupportedColorEffects();
    }

    public boolean setColorEffect(String value) {
        return this.mCameramanager.setColorEffect(value);
    }

    public boolean setPictureResolution(Size res) {
        return this.mCameramanager.setPictureResolution(res);
    }

    public Size getPictureResolution() {
        return this.mCameramanager.getPictureResolution();
    }

    public int getPictureFormat() {
        return this.mCameramanager.getPictureFormat();
    }

    public boolean setPictureFormat(int pixel_format) {
        return this.mCameramanager.setPictureFormat(pixel_format);
    }

    public SizeMap getPictureSupportedResolutions() {
        return this.mCameramanager.getPictureSupportedResolutions();
    }

    public Size getMaxSupportedPictureSize() {
        return this.mCameramanager.getMaxSupportedPictureSize();
    }

    public Size getMaxPictureSize4PreviewAspectRatio() {
        return this.mCameramanager.getMaxPictureSize4AspectRatio(this.mCameramanager.getAspectRatio());
    }

    public Size getMinSupportedPictureSize4PreviewAspectRatio() {
        return this.mCameramanager.getMinSupportedPictureSize4PreviewAspectRatio();
    }

    public Size getRecommendPictureSize() {
        if (isPictureSizeSupported(Camera1Constants.RecommendPictureSizeHigh)) {
            return Camera1Constants.RecommendPictureSizeHigh;
        }
        if (isPictureSizeSupported(Camera1Constants.RecommendPictureSizeLow)) {
            return Camera1Constants.RecommendPictureSizeLow;
        }
        return getPreviewResolution();
    }

    public boolean isPictureSizeSupported(Size size) {
        for (Size s : getPictureSupportedResolutions().allSizes()) {
            if (size.getWidth() == s.getWidth() && size.getHeight() == s.getHeight()) {
                return true;
            }
        }
        return false;
    }

    public boolean isflipHorizontal() {
        if (this.mCameramanager.getFacing() == 1) {
            return true;
        }
        return false;
    }

    public void takePhoto(final int width, final int height, Rect focusarea) {
        if (focusarea == null) {
            takePhoto(width, height);
        }
        autoFocus(focusarea, new Camera.AutoFocusCallback() {
            public void onAutoFocus(boolean success, Camera camera) {
                if (success) {
                    Log.e(FVCamera1Manager.TAG, "takePhoto ,auto focus ok");
                    FVCamera1Manager.this.takePhoto(width, height);
                    return;
                }
                FVCamera1Manager.this.takePhoto(width, height);
                Log.e(FVCamera1Manager.TAG, "takePhoto ,auto focus error");
            }
        });
    }

    public void takePhoto(int width, int height) {
        if (this.mFilterType == FilterType.NOFILTER || this.mCameraView.getCurrentViewStype() == CameraView.ViewStyle_SystemView) {
            int rotation = 0;
            if (width <= height) {
                if (this.mCameramanager.getFacing() == 1) {
                    rotation = 270;
                } else {
                    rotation = 90;
                }
            }
            takePhotoBySystem(rotation);
            return;
        }
        takePhotoByGpuImage();
    }

    public void takePhotoByGpuImage(final int width, final int height) {
        new Thread(new Runnable() {
            public void run() {
                Bitmap bitmap;
                try {
                    bitmap = FVCamera1Manager.this.getGpuImageView().capture(width, height);
                } catch (InterruptedException e) {
                    bitmap = null;
                }
                if (FVCamera1Manager.this.mCallback != null) {
                    FVCamera1Manager.this.mCallback.onPictureTaken(bitmap);
                }
            }
        }).start();
    }

    public void takePhotoByGpuImage() {
        new Thread(new Runnable() {
            public void run() {
                Bitmap bitmap;
                try {
                    bitmap = FVCamera1Manager.this.getGpuImageView().capture();
                } catch (InterruptedException e) {
                    bitmap = null;
                }
                if (FVCamera1Manager.this.mCallback != null) {
                    FVCamera1Manager.this.mCallback.onPictureTaken(bitmap);
                }
            }
        }).start();
    }

    public void takePhotoBySystem(int rotation) {
        this.mCameramanager.takePicture(new CameraManager.TakePictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                Bitmap bitmap = null;
                if (data != null) {
                    long ww = System.currentTimeMillis();
                    bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    CameraUtils.LogV("Huangs", "" + (System.currentTimeMillis() - ww));
                }
                if (FVCamera1Manager.this.mCallback != null) {
                    FVCamera1Manager.this.mCallback.onPictureTaken(bitmap);
                }
            }
        }, rotation);
    }

    public void takeNullPicture(Size size) {
        try {
            this.mCameramanager.takeNullPicture(size);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isMediaRecordQualitySupports(int quality) {
        if (quality == 16) {
            quality = 6;
        }
        return this.mCameramanager.isMediaRecordQualitySupports(quality);
    }

    public int getRecommendMediaRecordQuality() {
        if (isMediaRecordQualitySupports(6)) {
            return 6;
        }
        if (isMediaRecordQualitySupports(5)) {
            return 5;
        }
        if (isMediaRecordQualitySupports(4)) {
            return 4;
        }
        return 0;
    }

    public int[] getSupportedMediaRecordQuality() {
        List<Integer> list = new ArrayList<>();
        if (isMediaRecordQualitySupports(8)) {
            list.add(8);
        }
        if (isMediaRecordQualitySupports(6)) {
            list.add(6);
        }
        if (isMediaRecordQualitySupports(5)) {
            list.add(5);
        }
        if (isMediaRecordQualitySupports(4)) {
            list.add(4);
        }
        int[] a = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            a[i] = list.get(i).intValue();
        }
        return a;
    }

    public int[] getSupportedTimeLapseMediaRecordQuality() {
        List<Integer> list = new ArrayList<>();
        if (isMediaRecordQualitySupports(1008)) {
            list.add(1008);
        }
        if (isMediaRecordQualitySupports(1006)) {
            list.add(1006);
        }
        if (isMediaRecordQualitySupports(1005)) {
            list.add(1005);
        }
        if (isMediaRecordQualitySupports(1004)) {
            list.add(1004);
        }
        if (isMediaRecordQualitySupports(1001)) {
            list.add(1001);
        }
        if (isMediaRecordQualitySupports(1000)) {
            list.add(1000);
        }
        if (isMediaRecordQualitySupports(1003)) {
            list.add(1003);
        }
        int[] a = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            a[i] = list.get(i).intValue();
        }
        return a;
    }

    public int getHighestRecordQuality() {
        if (isMediaRecordQualitySupports(8)) {
            return 8;
        }
        if (isMediaRecordQualitySupports(6)) {
            return 6;
        }
        if (isMediaRecordQualitySupports(5)) {
            return 5;
        }
        if (isMediaRecordQualitySupports(4)) {
            return 4;
        }
        return 0;
    }

    public boolean startMediaRecordEx(String videopath, int quality, int orientation) {
        if (quality > 10) {
            return false;
        }
        return this.mCameramanager.startMediaRecordEx(videopath, quality, orientation, -1.0d, (CameraManager.RecordCallBack) new CameraManager.RecordCallBack() {
            public void onRecordStarted() {
                if (FVCamera1Manager.this.mCallback != null) {
                    FVCamera1Manager.this.mCallback.onRecordStarted();
                }
            }

            public void onRecordError(String reason) {
                if (FVCamera1Manager.this.mCallback != null) {
                    FVCamera1Manager.this.mCallback.onRecordError(reason);
                }
            }

            public void onRecordEnd() {
            }
        });
    }

    public boolean startMediaRecordEx(FileDescriptor fd, int quality, int orientation) {
        if (quality > 10) {
            return false;
        }
        return this.mCameramanager.startMediaRecordEx(fd, quality, orientation, -1.0d, (CameraManager.RecordCallBack) new CameraManager.RecordCallBack() {
            public void onRecordStarted() {
                if (FVCamera1Manager.this.mCallback != null) {
                    FVCamera1Manager.this.mCallback.onRecordStarted();
                }
            }

            public void onRecordError(String reason) {
                if (FVCamera1Manager.this.mCallback != null) {
                    FVCamera1Manager.this.mCallback.onRecordError(reason);
                }
            }

            public void onRecordEnd() {
            }
        });
    }

    public boolean startTimeLapseMediaRecordEx(String videopath, int quality, int orientation, double timeinterval) {
        if (quality < 1000) {
            return false;
        }
        return this.mCameramanager.startMediaRecordEx(videopath, quality, orientation, 1.0d / timeinterval, (CameraManager.RecordCallBack) new CameraManager.RecordCallBack() {
            public void onRecordStarted() {
                if (FVCamera1Manager.this.mCallback != null) {
                    FVCamera1Manager.this.mCallback.onRecordStarted();
                }
            }

            public void onRecordError(String reason) {
                if (FVCamera1Manager.this.mCallback != null) {
                    FVCamera1Manager.this.mCallback.onRecordError(reason);
                }
            }

            public void onRecordEnd() {
            }
        });
    }

    public boolean startTimeLapseMediaRecordEx(FileDescriptor fd, int quality, int orientation, double timeinterval) {
        if (quality < 1000) {
            return false;
        }
        return this.mCameramanager.startMediaRecordEx(fd, quality, orientation, 1.0d / timeinterval, (CameraManager.RecordCallBack) new CameraManager.RecordCallBack() {
            public void onRecordStarted() {
                if (FVCamera1Manager.this.mCallback != null) {
                    FVCamera1Manager.this.mCallback.onRecordStarted();
                }
            }

            public void onRecordError(String reason) {
                if (FVCamera1Manager.this.mCallback != null) {
                    FVCamera1Manager.this.mCallback.onRecordError(reason);
                }
            }

            public void onRecordEnd() {
            }
        });
    }

    public boolean startMediaRecord(String videopath, Size size, int fps, int orientation) {
        return this.mCameramanager.startMediaRecord(videopath, size, fps, orientation, (CameraManager.RecordCallBack) new CameraManager.RecordCallBack() {
            public void onRecordStarted() {
                if (FVCamera1Manager.this.mCallback != null) {
                    FVCamera1Manager.this.mCallback.onRecordStarted();
                }
            }

            public void onRecordError(String reason) {
                if (FVCamera1Manager.this.mCallback != null) {
                    FVCamera1Manager.this.mCallback.onRecordError(reason);
                }
            }

            public void onRecordEnd() {
            }
        });
    }

    public boolean startMediaRecord(FileDescriptor fd, Size size, int fps, int orientation) {
        return this.mCameramanager.startMediaRecord(fd, size, fps, orientation, (CameraManager.RecordCallBack) new CameraManager.RecordCallBack() {
            public void onRecordStarted() {
                if (FVCamera1Manager.this.mCallback != null) {
                    FVCamera1Manager.this.mCallback.onRecordStarted();
                }
            }

            public void onRecordError(String reason) {
                if (FVCamera1Manager.this.mCallback != null) {
                    FVCamera1Manager.this.mCallback.onRecordError(reason);
                }
            }

            public void onRecordEnd() {
            }
        });
    }

    public boolean startMediaRecordExt(String videopath, int quality, int orientation) {
        Size size = null;
        int fps = 30;
        switch (quality) {
            case 4:
                size = new Size(720, 480);
                break;
            case 5:
                size = new Size(1280, 720);
                break;
            case 6:
                size = new Size(1920, 1080);
                break;
            case 8:
                size = new Size(3840, 2160);
                break;
            case 16:
                size = new Size(1920, 1080);
                fps = 60;
                break;
        }
        if (size == null) {
            return false;
        }
        return this.mCameramanager.startMediaRecord(videopath, size, fps, orientation, (CameraManager.RecordCallBack) new CameraManager.RecordCallBack() {
            public void onRecordStarted() {
                if (FVCamera1Manager.this.mCallback != null) {
                    FVCamera1Manager.this.mCallback.onRecordStarted();
                }
            }

            public void onRecordError(String reason) {
                if (FVCamera1Manager.this.mCallback != null) {
                    FVCamera1Manager.this.mCallback.onRecordError(reason);
                }
            }

            public void onRecordEnd() {
            }
        });
    }

    public boolean stopMediaRecord() {
        return this.mCameramanager.stopMediaRecord(new CameraManager.RecordCallBack() {
            public void onRecordStarted() {
            }

            public void onRecordError(String reason) {
                if (FVCamera1Manager.this.mCallback != null) {
                    FVCamera1Manager.this.mCallback.onRecordError(reason);
                }
            }

            public void onRecordEnd() {
                if (FVCamera1Manager.this.mCallback != null) {
                    FVCamera1Manager.this.mCallback.onRecordEnd("");
                }
            }
        });
    }

    public boolean isMediaRecording() {
        return this.mCameramanager.isMediaRecording();
    }

    public double getMediaRecordVolumeAmplitude() {
        return this.mCameramanager.getMediaRecordVolumeAmplitude();
    }

    public boolean startTrack(Rect rect, Point pnt, FVTrackObserver tracklistener) {
        if (this.trackManager != null) {
            if (this.trackManager.isIstrack()) {
                this.trackManager.stopTrack();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.trackManager = null;
        }
        Size previewSize = getPreviewResolution();
        this.trackManager = new FVTrackManager(this, tracklistener, previewSize.getWidth(), previewSize.getHeight());
        if (!this.trackManager.initTrack()) {
            this.trackManager = null;
            return false;
        }
        this.mCameramanager.getCamera().setPreviewCallback(this.mCameramanager);
        if (rect != null && pnt == null) {
            this.trackManager.startTracking(FVTrackManager.GraphicRect2OpenCVRect(rect));
        } else if (rect != null || pnt == null) {
            return false;
        } else {
            this.trackManager.startTracking(FVTrackManager.GraphicPoint2OpenCVPoint(pnt));
        }
        return true;
    }

    public void stopTrack() {
        if (this.trackManager != null && this.trackManager.isIstrack()) {
            this.trackManager.stopTrack();
            this.mCameramanager.getCamera().setPreviewCallback((Camera.PreviewCallback) null);
        }
    }

    public FVTrackManager getTrackManager() {
        return this.trackManager;
    }

    public void startSlowShutter() {
        startSlowShutter(getPreviewResolution());
    }

    public void startSlowShutter(Size photosize) {
        this.mSlowShutter = new SlowShutter(this);
        this.mSlowShutter.startSlowShutter(1, photosize);
    }

    public void stopSlowShutter() {
        this.mSlowShutter.stopSlowShutter();
    }

    public CameraManager getInternal() {
        return this.mCameramanager;
    }
}
