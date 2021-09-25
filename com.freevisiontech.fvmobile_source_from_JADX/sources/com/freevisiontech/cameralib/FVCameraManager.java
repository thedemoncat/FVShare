package com.freevisiontech.cameralib;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.RggbChannelVector;
import android.location.Location;
import android.os.Build;
import android.util.Range;
import com.freevisiontech.cameralib.impl.Camera2.Camera2Manager;
import com.freevisiontech.cameralib.impl.Camera2.CameraParameters;
import com.freevisiontech.cameralib.utils.CameraUtils;
import com.freevisiontech.cameralib.view.camera1.CameraView;
import com.freevisiontech.filterlib.FilterType;
import com.freevisiontech.tracking.FVTrackObserver;
import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageView;

public class FVCameraManager {
    private static final String TAG = "FVCameraManager";
    private FVCamera1Manager camera1Manager = null;
    private FVCamera2Manager camera2Manager = null;
    private int cameraManagerStyle = 0;
    private Activity mContext = null;

    public static int GetCameraLevel(Context context) {
        CameraManager manager = (CameraManager) context.getSystemService("camera");
        try {
            for (String cameraId : manager.getCameraIdList()) {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
                if (((Integer) characteristics.get(CameraCharacteristics.LENS_FACING)).intValue() == 1) {
                    return ((Integer) characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL)).intValue();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static FVCameraManager CreateCamera1Manager(Activity context, CameraView cameraView, FVCamereManagerCallback callback) {
        FVCameraManager cameraManager = new FVCameraManager(context);
        cameraView.setCameraManager(cameraManager.createCamera1Manager(context, cameraView, callback));
        cameraManager.cameraManagerStyle = 1;
        return cameraManager;
    }

    public static FVCameraManager CreateCamera2Manager(Activity context, com.freevisiontech.cameralib.view.camera2.CameraView cameraView, CameraParameters parameters, FVCamereManagerCallback callback) {
        FVCameraManager cameraManager = new FVCameraManager(context);
        cameraView.setCameraManager(cameraManager.createCamera2Manager(context, cameraView, parameters, callback));
        cameraManager.cameraManagerStyle = 2;
        return cameraManager;
    }

    public FVCameraManager(Activity ctx) {
        this.mContext = ctx;
        if (CameraUtils.isApkDebugable(ctx)) {
            CameraUtils.ShowDebugInfo = true;
        } else {
            CameraUtils.ShowDebugInfo = false;
        }
    }

    private FVCamera2Manager createCamera2Manager(Activity context, com.freevisiontech.cameralib.view.camera2.CameraView cameraView, CameraParameters parameters, FVCamereManagerCallback callback) {
        this.camera2Manager = new FVCamera2Manager(context, cameraView, parameters, callback);
        this.camera2Manager.setPreviewCallback(this.camera2Manager);
        return this.camera2Manager;
    }

    private FVCamera1Manager createCamera1Manager(Activity context, CameraView cameraView, FVCamereManagerCallback callback) {
        this.camera1Manager = new FVCamera1Manager(context, cameraView, callback);
        return this.camera1Manager;
    }

    public void setCameraStateListner(Camera2Manager.CameraStatesListener listner) {
        if (this.cameraManagerStyle != 1) {
            this.camera2Manager.setCameraStatesListener(listner);
        }
    }

    public GPUImageView getGpuImageView() {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.getGpuImageView();
        }
        return null;
    }

    public boolean setCameraView(com.freevisiontech.cameralib.view.CameraView cameraView) {
        if (this.cameraManagerStyle == 1 && this.camera1Manager != null) {
            ((CameraView) cameraView).setCameraManager(this.camera1Manager);
            return true;
        } else if (this.cameraManagerStyle != 2 || this.camera2Manager == null) {
            return false;
        } else {
            ((com.freevisiontech.cameralib.view.camera2.CameraView) cameraView).setCameraManager(this.camera2Manager);
            return true;
        }
    }

    public boolean isCameraOpened() {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.isCameraOpened();
        }
        return this.camera2Manager.isCameraOpened();
    }

    public boolean destroy() {
        if (this.cameraManagerStyle != 1) {
            return this.camera2Manager.destroy();
        }
        this.camera1Manager.destroy();
        return true;
    }

    public boolean isSupportManualMode() {
        if (this.cameraManagerStyle == 1) {
            return false;
        }
        return this.camera2Manager.isSupportManualMode();
    }

    public void switchCameraManagerMode() {
        if (this.cameraManagerStyle == 1) {
            this.camera1Manager.switchCameraManagerMode();
        } else {
            this.camera2Manager.switchCameraManagerMode();
        }
    }

    public int getCurrentCameraMode() {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.getCurrentCameraMode();
        }
        return this.camera2Manager.getCurrentCameraMode();
    }

    public void changeCameraManagerMode(int newCameraManagermode) {
        if (this.cameraManagerStyle == 1) {
            this.camera1Manager.changeCameraManagerMode(newCameraManagermode);
        } else {
            this.camera2Manager.changeCameraManagerMode(newCameraManagermode);
        }
    }

    public boolean stopCamera() {
        if (this.cameraManagerStyle != 1) {
            return this.camera2Manager.stopCamera();
        }
        this.camera1Manager.stopCamera();
        return true;
    }

    public void setFacing(int facing) {
        if (this.cameraManagerStyle == 1) {
            this.camera1Manager.setFacing(facing);
        } else {
            this.camera2Manager.setFacing(facing);
        }
    }

    public int getFacing() {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.getFacing();
        }
        return this.camera2Manager.getFacing();
    }

    public boolean setFilter(FilterType filterType) {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.setFilter(filterType);
        }
        return this.camera2Manager.setFilter(filterType);
    }

    public GPUImageFilter getFilter() {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.getFilter();
        }
        return this.camera2Manager.getFilter();
    }

    public FilterType getFilterType() {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.getFilterType();
        }
        return this.camera2Manager.getFilterType();
    }

    public boolean changeFilterIntensity(int intensity) {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.changeFilterIntensity(intensity);
        }
        return this.camera2Manager.changeFilterIntensity(intensity);
    }

    public int getCameramode() {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.getCameramode();
        }
        return this.camera2Manager.getCameramode();
    }

    public void changeCamreMode(int newCameraMode) {
        if (this.cameraManagerStyle == 1) {
            this.camera1Manager.changeCamreMode(newCameraMode);
        } else {
            this.camera2Manager.changeCamreMode(newCameraMode);
        }
    }

    public AspectRatio getAspectRatio() {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.getAspectRatio();
        }
        return this.camera2Manager.getAspectRatio();
    }

    public Size getPreviewResolution() {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.getPreviewResolution();
        }
        return this.camera2Manager.getPreviewResolution();
    }

    public Size getPreviewFrameSize() {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.getPreviewResolution();
        }
        return this.camera2Manager.getPreviewFrameSize();
    }

    public boolean setPreviewResolution(Size res) {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.setPreviewResolution(res);
        }
        return false;
    }

    public boolean setPreviewResolutionEx(Size res) {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.setPreviewResolutionEx(res);
        }
        return false;
    }

    public void setFlashMode(int flash) {
        if (this.cameraManagerStyle == 1) {
            this.camera1Manager.setFlashMode(flash);
        } else {
            this.camera2Manager.setFlashMode(flash);
        }
    }

    public int getFlashMode() {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.getFlashMode();
        }
        return this.camera2Manager.getFlashMode();
    }

    public boolean setContinuosFocusMode(int mode) {
        if (this.cameraManagerStyle != 1) {
            return this.camera2Manager.setFocusMode(mode);
        }
        if (mode == 3) {
            return this.camera1Manager.setFocusMode("continuous-video");
        }
        return this.camera1Manager.setFocusMode("continuous-picture");
    }

    public int getFocusMode() {
        if (this.cameraManagerStyle == 1) {
            return CameraUtils.FocusModeStr2FocusModeId(this.camera1Manager.getFocusMode());
        }
        return this.camera2Manager.getFocusMode();
    }

    public boolean setAutoFocusMode(boolean autoFocus) {
        if (this.cameraManagerStyle != 1) {
            return this.camera2Manager.setAutoFocusMode(autoFocus);
        }
        this.camera1Manager.setAutoFocusMode(autoFocus);
        return true;
    }

    public boolean isSupportAutoFocus() {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.isSupportFocusArea();
        }
        return this.camera2Manager.isSupportAutoFocus();
    }

    public boolean isAutoFocus() {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.isAutoFocus();
        }
        return this.camera2Manager.isAutoFocus();
    }

    public boolean isMaunalFocus() {
        if (this.cameraManagerStyle == 1) {
            return false;
        }
        return this.camera2Manager.isMaunalFocus();
    }

    public boolean lockFocus(Camera.AutoFocusCallback callback) {
        if (this.cameraManagerStyle != 1) {
            return false;
        }
        this.camera1Manager.autoFocus(callback);
        return true;
    }

    public boolean lockFocus(Camera2Manager.AutoFocusListener autoFocusListener) {
        if (this.cameraManagerStyle == 1) {
            return false;
        }
        return this.camera2Manager.lockFocus(autoFocusListener);
    }

    public boolean isFocusLocked() {
        if (this.cameraManagerStyle == 1) {
            return false;
        }
        return this.camera2Manager.isFocusLocked();
    }

    public boolean unlockFocus(Camera2Manager.AutoFocusListener autoFocusListener) {
        if (this.cameraManagerStyle == 1) {
            return false;
        }
        return this.camera2Manager.unlockFocus(autoFocusListener);
    }

    public void autoFocus(Rect focusarea, Camera.AutoFocusCallback callback) {
        if (this.cameraManagerStyle == 1) {
            this.camera1Manager.autoFocus(focusarea, callback);
        }
    }

    public boolean autoFocus(double x, double y, boolean islockae, Camera2Manager.AutoFocusListener autoFocusListener) {
        if (this.cameraManagerStyle == 1) {
            return false;
        }
        return this.camera2Manager.autoFocus(x, y, islockae, autoFocusListener);
    }

    public void cancelAutoFocus() {
        if (this.cameraManagerStyle == 1) {
            this.camera1Manager.cancelAutoFocus();
        }
    }

    public boolean cancelAutoFocus(Camera2Manager.AutoFocusListener autoFocusListener) {
        if (this.cameraManagerStyle == 1) {
            return false;
        }
        return this.camera2Manager.cancelAutoFocus(autoFocusListener);
    }

    public float getFocalLength() {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.getFocalLength();
        }
        return this.camera2Manager.getFocalLength();
    }

    public boolean isFocalSupport() {
        if (this.cameraManagerStyle == 1) {
            return false;
        }
        return this.camera2Manager.isFocalSupport();
    }

    public float[] getAvailableFocalLengths() {
        if (this.cameraManagerStyle == 1) {
            return null;
        }
        return this.camera2Manager.getAvailableFocalLengths();
    }

    public boolean setFocalLength(float focallen) {
        if (this.cameraManagerStyle == 1) {
            return false;
        }
        return this.camera2Manager.setFocalLength(focallen);
    }

    public float getFocusDistance() {
        if (this.cameraManagerStyle == 1) {
            return 0.0f;
        }
        return this.camera2Manager.getFocusDistance();
    }

    public float getMinFocusDistance() {
        if (this.cameraManagerStyle == 1) {
            return 0.0f;
        }
        return this.camera2Manager.getMinFocusDistance();
    }

    public boolean setFocusDistance(float focusdistance) {
        if (this.cameraManagerStyle == 1) {
            return false;
        }
        return this.camera2Manager.setFocusDistance(focusdistance);
    }

    public boolean isZoomSupported() {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.isZoomSupported();
        }
        return this.camera2Manager.isZoomSupported();
    }

    public boolean setZoom(float degree) {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.setZoom((int) degree);
        }
        return this.camera2Manager.setZoom(degree);
    }

    public boolean setZoomEx(float degree) {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.setZoom((int) degree);
        }
        return this.camera2Manager.setZoomEx(degree);
    }

    public float getMaxZoom() {
        if (this.cameraManagerStyle == 1) {
            return (float) this.camera1Manager.getMaxZoom();
        }
        return this.camera2Manager.getMaxZoom();
    }

    public float getZoomStep() {
        if (this.cameraManagerStyle == 1) {
            return -1.0f;
        }
        return this.camera2Manager.getZoomStep();
    }

    public boolean isSupportHdrMode() {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.isSupportHdrMode();
        }
        return this.camera2Manager.isSupportHdrMode();
    }

    public boolean setHdrMode(boolean ison) {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.setHdrMode(ison);
        }
        return this.camera2Manager.setHdrMode(ison);
    }

    public boolean setSceneMode(String mode) {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.setSceneMode(mode);
        }
        return false;
    }

    public boolean setSceneMode(int mode) {
        if (this.cameraManagerStyle == 1) {
            return false;
        }
        return this.camera2Manager.setSceneMode(mode);
    }

    public String getSceneMode() {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.getSceneMode();
        }
        return null;
    }

    public int getSceneModeId() {
        if (this.cameraManagerStyle == 1) {
            return -1;
        }
        return this.camera2Manager.getSceneMode();
    }

    public List<String> getSupportedSceneMode() {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.getSupportedSceneModes();
        }
        return null;
    }

    public int[] getSupportedSceneModeIds() {
        if (this.cameraManagerStyle == 1) {
            return null;
        }
        return this.camera2Manager.getSupportedSceneModes();
    }

    public boolean setFPS(int fps) {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.setFPS(fps);
        }
        return this.camera2Manager.setFPS(fps);
    }

    public boolean setFPSRange(Range<Integer> fpsrange) {
        if (this.cameraManagerStyle == 1) {
            return false;
        }
        return this.camera2Manager.setFPSRange(fpsrange);
    }

    public int getFPS() {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.getFPS();
        }
        return getFPSRange().getUpper().intValue();
    }

    public Range<Integer> getFPSRange() {
        if (this.cameraManagerStyle == 1) {
            return null;
        }
        return this.camera2Manager.getFPS();
    }

    public List<Integer> getSupportedPreviewFPS() {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.getSupportedPreviewFPS();
        }
        Range<Integer>[] ranges = getSupportedPreviewFPSRanges();
        List<Integer> l = new ArrayList<>();
        for (Range<Integer> upper : ranges) {
            l.add(Integer.valueOf(upper.getUpper().intValue()));
        }
        return l;
    }

    public Range<Integer>[] getSupportedPreviewFPSRanges() {
        if (this.cameraManagerStyle == 1) {
            return null;
        }
        return this.camera2Manager.getSupportedPreviewFPSRanges();
    }

    public int[] getAvailabelAEModes() {
        if (this.cameraManagerStyle == 1) {
            return null;
        }
        return this.camera2Manager.getAvailabelAEModes();
    }

    public boolean lockAutoExposure(boolean toogle) {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.lockAutoExposure(toogle);
        }
        return this.camera2Manager.lockAutoExposure(toogle);
    }

    public boolean isAutoExposureLock() {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.isAutoExposureLock();
        }
        return this.camera2Manager.isAutoExposureLock();
    }

    public int getISOValue() {
        if (this.cameraManagerStyle == 1) {
            return -1;
        }
        return this.camera2Manager.getISOValue();
    }

    public boolean setISOValue(int value) {
        if (this.cameraManagerStyle == 1) {
            return false;
        }
        return this.camera2Manager.setISOValue(value);
    }

    public Range<Integer> getIsoRange() {
        if (this.cameraManagerStyle == 1) {
            return null;
        }
        return this.camera2Manager.getIsoRange();
    }

    public int getExposureCompensation() {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.getExposureCompensation();
        }
        return this.camera2Manager.getExposureCompensation();
    }

    public boolean setExposureCompensation(int value) {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.setExposureCompensation(value);
        }
        return this.camera2Manager.setExposureCompensation(value);
    }

    public int[] getExposureCompensationRanger() {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.getExposureCompensationRanger();
        }
        Range<Integer> r = this.camera2Manager.getExposureCompensationRanger();
        int[] ar = new int[2];
        if (r != null) {
            ar[0] = r.getLower().intValue();
            ar[1] = r.getUpper().intValue();
            return ar;
        }
        ar[0] = 0;
        ar[1] = 0;
        return ar;
    }

    public Range<Integer> getExposureCompensationRanger2() {
        if (this.cameraManagerStyle != 1) {
            return this.camera2Manager.getExposureCompensationRanger();
        }
        int[] ar = this.camera1Manager.getExposureCompensationRanger();
        return new Range<>(Integer.valueOf(ar[0]), Integer.valueOf(ar[1]));
    }

    public float getExposureValue() {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.getExposureValue();
        }
        return (float) this.camera2Manager.getExposureCompensation();
    }

    public float getExposureCompensationStep() {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.getExposureCompensationStep();
        }
        return this.camera2Manager.getExposureCompensationStep();
    }

    public boolean setExposureTime(long value) {
        if (this.cameraManagerStyle == 1) {
            return false;
        }
        return this.camera2Manager.setExposureTime(value);
    }

    public long getExposureTime() {
        if (this.cameraManagerStyle == 1) {
            return 0;
        }
        return this.camera2Manager.getExposureTime();
    }

    public Range<Long> getExposureTimeRange() {
        if (this.cameraManagerStyle == 1) {
            return null;
        }
        return this.camera2Manager.getExposureTimeRange();
    }

    public int[] getAvailableAWBModes() {
        if (this.cameraManagerStyle == 1) {
            return null;
        }
        return this.camera2Manager.getAvailableAWBModes();
    }

    public List<String> getAvailableWhiteBalance() {
        if (this.cameraManagerStyle == 1) {
            return null;
        }
        return this.camera2Manager.getAvailableWhiteBalance();
    }

    public String getWhiteBalance() {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.getWhiteBalance();
        }
        return this.camera2Manager.getWhiteBalance();
    }

    public int getWhiteBalanceMode() {
        if (this.cameraManagerStyle == 1) {
            return -1;
        }
        return this.camera2Manager.getWhiteBalanceMode();
    }

    public boolean setWhiteBalance(String mode) {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.setWhiteBalance(mode);
        }
        return this.camera2Manager.setWhiteBalance(mode);
    }

    public boolean setWhiteBalanceMode(int mode) {
        if (this.cameraManagerStyle == 1) {
            return false;
        }
        return this.camera2Manager.setWhiteBalanceMode(mode);
    }

    public boolean lockAutoWhiteBalance(boolean enable) {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.lockAutoWhiteBalance(enable);
        }
        return this.camera2Manager.lockAutoWhiteBalance(enable);
    }

    public boolean isAutoWhiteBalanceLocked() {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.isAutoWhiteBalanceLocked();
        }
        return this.camera2Manager.isAutoWhiteBalanceLocked();
    }

    public int getColorCorrectionMode() {
        if (this.cameraManagerStyle == 1) {
            return -1;
        }
        return this.camera2Manager.getColorCorrectionMode();
    }

    public Range<Float> getWBGainRange() {
        if (this.cameraManagerStyle == 1) {
            return null;
        }
        return this.camera2Manager.getWBGainRange();
    }

    public RggbChannelVector getWBGainValue() {
        if (this.cameraManagerStyle == 1) {
            return null;
        }
        return this.camera2Manager.getWBGainValue();
    }

    public int getWBGainFactor() {
        if (this.cameraManagerStyle == 1) {
            return -1;
        }
        return this.camera2Manager.getWBGainFactor();
    }

    public boolean setWBGainValue(RggbChannelVector vector) {
        if (this.cameraManagerStyle == 1) {
            return false;
        }
        return this.camera2Manager.setWBGainValue(vector);
    }

    public boolean setWBGainValue(int facotor) {
        if (this.cameraManagerStyle == 1) {
            return false;
        }
        return this.camera2Manager.setWBGainValue(facotor);
    }

    public String getColorEffect() {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.getColorEffect();
        }
        return this.camera2Manager.getColorEffect();
    }

    public List<String> getSupportedColorEffects() {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.getSupportedColorEffects();
        }
        return this.camera2Manager.getSupportedColorEffects();
    }

    public boolean setColorEffect(String value) {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.setColorEffect(value);
        }
        return this.camera2Manager.setColorEffect(value);
    }

    public boolean setPictureResolution(Size res) {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.setPictureResolution(res);
        }
        return false;
    }

    public SizeMap getPictureSupportedResolutions() {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.getPictureSupportedResolutions();
        }
        return this.camera2Manager.getPictureSupportedResolutions();
    }

    public Size[] getRawPictureSupportedResolutions(int face) {
        if (this.cameraManagerStyle == 1) {
            return null;
        }
        return this.camera2Manager.getRawPictureSupportedResolutions(face);
    }

    public Size[] getRawPictureSupportedResolutions() {
        return getRawPictureSupportedResolutions(getFacing());
    }

    public Size getMaxSupportedPictureSize() {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.getMaxSupportedPictureSize();
        }
        return this.camera2Manager.getMaxSupportedPictureSize();
    }

    public Size getRecommendPictureSize() {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.getRecommendPictureSize();
        }
        return this.camera2Manager.getRecommendPictureSize();
    }

    public boolean isPictureSizeSupported(Size size) {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.isPictureSizeSupported(size);
        }
        return this.camera2Manager.isPictureSizeSupported(size, this.camera2Manager.getFacing());
    }

    public void takePhoto(int width, int height, Rect focusarea) {
        if (this.cameraManagerStyle == 1) {
            if (width > height) {
                this.camera1Manager.setPictureResolution(new Size(width, height));
            } else {
                this.camera1Manager.setPictureResolution(new Size(height, width));
            }
            this.camera1Manager.takePhoto(width, height, focusarea);
            return;
        }
        this.camera2Manager.takePhoto(width, height, (Location) null, 0);
    }

    public void takePhoto(int width, int height) {
        if (this.cameraManagerStyle == 1) {
            if (width > height) {
                this.camera1Manager.setPictureResolution(new Size(width, height));
            } else {
                this.camera1Manager.setPictureResolution(new Size(height, width));
            }
            this.camera1Manager.takePhoto(width, height);
            return;
        }
        this.camera2Manager.takePhoto(width, height, (Location) null, 0);
    }

    public void takePhoto(int width, int height, Location location) {
        if (this.cameraManagerStyle == 1) {
            if (width > height) {
                this.camera1Manager.setPictureResolution(new Size(width, height));
            } else {
                this.camera1Manager.setPictureResolution(new Size(height, width));
            }
            this.camera1Manager.takePhoto(width, height);
            return;
        }
        this.camera2Manager.takePhoto(width, height, location, 0);
    }

    public void takeRawPhoto(int width, int height, int type) {
        takeRawPhoto(width, height, (Location) null, type);
    }

    public void takeRawPhoto(int width, int height, Location location, int type) {
        if (this.cameraManagerStyle != 1) {
            this.camera2Manager.takePhoto(width, height, location, type);
        }
    }

    public boolean isMediaRecordQualitySupports(int quality) {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.isMediaRecordQualitySupports(quality);
        }
        return this.camera2Manager.isMediaRecordQualitySupports(quality);
    }

    public int getRecommendMediaRecordQuality() {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.getRecommendMediaRecordQuality();
        }
        return this.camera2Manager.getRecommendMediaRecordQuality();
    }

    public int[] getSupportedMediaRecordQuality() {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.getSupportedMediaRecordQuality();
        }
        return this.camera2Manager.getSupportedMediaRecordQuality();
    }

    public Size[] getSupportedMediaRecordSizes() {
        if (this.cameraManagerStyle == 1) {
            return null;
        }
        return this.camera2Manager.getSupportedMediaRecordSizes();
    }

    public int[] getSupportedTimeLapseMediaRecordQuality() {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.getSupportedTimeLapseMediaRecordQuality();
        }
        return this.camera2Manager.getSupportedTimeLapseMediaRecordQuality();
    }

    public int getRecommendTimeLapseMediaRecordQuality() {
        if (this.cameraManagerStyle == 1) {
            return 1001;
        }
        if (Build.MODEL.toLowerCase().equals("redmi note 5")) {
            if (this.camera2Manager.isMediaRecordQualitySupports(1008) && this.camera2Manager.isSupportedVideoSize(new Size(3840, 2160))) {
                return 1008;
            }
            if (this.camera2Manager.isMediaRecordQualitySupports(1006) && this.camera2Manager.isSupportedVideoSize(new Size(1920, 1080))) {
                return 1006;
            }
            if (this.camera2Manager.isMediaRecordQualitySupports(1005) && this.camera2Manager.isSupportedVideoSize(new Size(1280, 720))) {
                return 1005;
            }
        } else if (this.camera2Manager.isMediaRecordQualitySupports(1008) && this.camera2Manager.isSupportedVideoSize(new Size(3840, 2160))) {
            return 1001;
        } else {
            if (this.camera2Manager.isMediaRecordQualitySupports(1006) && this.camera2Manager.isSupportedVideoSize(new Size(1920, 1080))) {
                return 1001;
            }
            if (!this.camera2Manager.isMediaRecordQualitySupports(1005) || !this.camera2Manager.isSupportedVideoSize(new Size(1280, 720))) {
                return 1000;
            }
            return 1001;
        }
        return 1000;
    }

    public boolean startMediaRecordEx(String videopath, int quality, int orientation) {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.startMediaRecordEx(videopath, quality, orientation);
        }
        return this.camera2Manager.startMediaRecordEx(videopath, quality, orientation);
    }

    public boolean startMediaRecordEx(FileDescriptor fd, int quality, int orientation) {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.startMediaRecordEx(fd, quality, orientation);
        }
        return this.camera2Manager.startMediaRecordEx(fd, quality, orientation);
    }

    public boolean startMediaRecordEx(String videopath, int quality) {
        if (this.cameraManagerStyle == 1) {
            return false;
        }
        return this.camera2Manager.startMediaRecordEx(videopath, quality);
    }

    public boolean startMediaRecordEx(FileDescriptor fd, int quality) {
        if (this.cameraManagerStyle == 1) {
            return false;
        }
        return this.camera2Manager.startMediaRecordEx(fd, quality);
    }

    public boolean startMediaRecord(String videopath, Size size, int fps, int orientation) {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.startMediaRecord(videopath, size, fps, orientation);
        }
        return this.camera2Manager.startMediaRecord(videopath, size, fps, orientation);
    }

    public boolean startMediaRecord(FileDescriptor fd, Size size, int fps, int orientation) {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.startMediaRecord(fd, size, fps, orientation);
        }
        return this.camera2Manager.startMediaRecord(fd, size, fps, orientation);
    }

    public boolean startMediaRecord(String videopath, Size size, int fps) {
        if (this.cameraManagerStyle == 1) {
            return false;
        }
        return this.camera2Manager.startMediaRecord(videopath, size, fps);
    }

    public boolean startMediaRecord(FileDescriptor fd, Size size, int fps) {
        if (this.cameraManagerStyle == 1) {
            return false;
        }
        return this.camera2Manager.startMediaRecord(fd, size, fps);
    }

    public boolean startMediaRecordExt(FileDescriptor fd, Size size) {
        int quality = CameraUtils.Size2Quality(size);
        if (quality != -1) {
            return startMediaRecordEx(fd, quality);
        }
        return startMediaRecord(fd, size, 30);
    }

    public boolean startMediaRecordExt(String videopath, Size size) {
        int quality = CameraUtils.Size2Quality(size);
        if (quality != -1) {
            return startMediaRecordEx(videopath, quality);
        }
        return startMediaRecord(videopath, size, 30);
    }

    public boolean startMediaRecordExt(String videopath, Size size, int orientation, int fps) {
        int quality = CameraUtils.Size2Quality(size);
        if (quality != -1) {
            return startMediaRecordEx(videopath, quality, orientation);
        }
        return startMediaRecord(videopath, size, fps, orientation);
    }

    public boolean startMediaRecordExt(FileDescriptor fd, Size size, int orientation, int fps) {
        int quality = CameraUtils.Size2Quality(size);
        if (quality != -1) {
            return startMediaRecordEx(fd, quality, orientation);
        }
        return startMediaRecord(fd, size, fps, orientation);
    }

    public double getMediaRecordVolumeAmplitude() {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.getMediaRecordVolumeAmplitude();
        }
        return this.camera2Manager.getMediaRecordVolumeAmplitude();
    }

    public boolean startTimeLapseMediaRecordEx(String videopath, int quality, int orientation, double timeinterval) {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.startTimeLapseMediaRecordEx(videopath, quality, orientation, timeinterval);
        }
        return this.camera2Manager.startTimeLapseMediaRecordEx(videopath, quality, orientation, timeinterval);
    }

    public boolean startTimeLapseMediaRecordEx(FileDescriptor fd, int quality, int orientation, double timeinterval) {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.startTimeLapseMediaRecordEx(fd, quality, orientation, timeinterval);
        }
        return this.camera2Manager.startTimeLapseMediaRecordEx(fd, quality, orientation, timeinterval);
    }

    public boolean startTimeLapseMediaRecordEx(String videopath, int quality, double timeinterval) {
        if (this.cameraManagerStyle == 1) {
            return false;
        }
        return this.camera2Manager.startTimeLapseMediaRecordEx(videopath, quality, timeinterval);
    }

    public boolean startTimeLapseMediaRecordEx(FileDescriptor fd, int quality, double timeinterval) {
        if (this.cameraManagerStyle == 1) {
            return false;
        }
        return this.camera2Manager.startTimeLapseMediaRecordEx(fd, quality, timeinterval);
    }

    public boolean stopMediaRecord() {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.stopMediaRecord();
        }
        return this.camera2Manager.stopMediaRecord();
    }

    public boolean isMediaRecording() {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.isMediaRecording();
        }
        return this.camera2Manager.isMediaRecording();
    }

    public boolean isVideoStabilizationSupport() {
        if (this.cameraManagerStyle == 1) {
            return false;
        }
        return this.camera2Manager.isVideoStabilizationSupport();
    }

    public boolean isDigitalVideoStabilizationSupport() {
        if (this.cameraManagerStyle == 1) {
            return false;
        }
        return this.camera2Manager.isDigitalVideoStabilizationSupport();
    }

    public boolean isOpticalVideoStabilizationSupport() {
        if (this.cameraManagerStyle == 1) {
            return false;
        }
        return this.camera2Manager.isOpticalVideoStabilizationSupport();
    }

    public boolean isOpticalVideoStabilizationOpened() {
        if (this.cameraManagerStyle == 1) {
            return false;
        }
        return this.camera2Manager.isOpticalVideoStabilizationOpened();
    }

    public boolean isDigitalVideoStabilizationOpened() {
        if (this.cameraManagerStyle == 1) {
            return false;
        }
        return this.camera2Manager.isDigitalVideoStabilizationOpened();
    }

    public boolean isVideoStabilizationOpened() {
        if (this.cameraManagerStyle == 1) {
            return false;
        }
        return this.camera2Manager.isVideoStabilizationOpened();
    }

    public boolean closeVideoStabilizationMode() {
        if (this.cameraManagerStyle == 1) {
            return false;
        }
        return this.camera2Manager.closeVideoStabilizationMode();
    }

    public boolean openVideoStabilizationMode() {
        if (this.cameraManagerStyle == 1) {
            return false;
        }
        return this.camera2Manager.openVideoStabilizationMode();
    }

    public int videoStabilizationType() {
        if (this.cameraManagerStyle == 1) {
            return -1;
        }
        return this.camera2Manager.videoStabilizationType();
    }

    public boolean enableManualMode(boolean toogle) {
        if (this.cameraManagerStyle == 1) {
            return false;
        }
        return this.camera2Manager.enableManualMode(toogle);
    }

    public boolean enableMEMode(boolean toogle) {
        if (this.cameraManagerStyle == 1) {
            return false;
        }
        return this.camera2Manager.enableMEModeEx(toogle);
    }

    public boolean enableMFMode(boolean toogle) {
        if (this.cameraManagerStyle == 1) {
            return false;
        }
        return this.camera2Manager.enableMFModeEx(toogle);
    }

    public boolean enableMWBMode(boolean toogle) {
        if (this.cameraManagerStyle == 1) {
            return false;
        }
        return this.camera2Manager.enableMWBModeEx(toogle);
    }

    public boolean isManualExposure() {
        if (this.cameraManagerStyle == 1) {
            return false;
        }
        return this.camera2Manager.isMEMode();
    }

    public boolean isAutoExposure() {
        if (this.cameraManagerStyle == 1) {
            return false;
        }
        return this.camera2Manager.isAEMode();
    }

    public boolean isManualWhiteBalance() {
        if (this.cameraManagerStyle == 1) {
            return false;
        }
        return this.camera2Manager.isMWBMode();
    }

    public boolean startTrack(Rect rect, FVTrackObserver tracklistener) {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.startTrack(rect, (Point) null, tracklistener);
        }
        return this.camera2Manager.startTrack(rect, (Point) null, tracklistener);
    }

    public boolean startTrack(Point pnt, FVTrackObserver tracklistener) {
        if (this.cameraManagerStyle == 1) {
            return this.camera1Manager.startTrack((Rect) null, pnt, tracklistener);
        }
        return this.camera2Manager.startTrack((Rect) null, pnt, tracklistener);
    }

    public void stopTrack() {
        if (this.cameraManagerStyle == 1) {
            this.camera1Manager.stopTrack();
        } else {
            this.camera2Manager.stopTrack();
        }
    }

    public void startSlowShutter() {
        if (this.cameraManagerStyle == 1) {
            this.camera1Manager.startSlowShutter();
        } else {
            this.camera2Manager.startSlowShutter();
        }
    }

    public void stopSlowShutter() {
        if (this.cameraManagerStyle == 1) {
            this.camera1Manager.stopSlowShutter();
        } else {
            this.camera2Manager.stopSlowShutter();
        }
    }

    public FVCamera1Manager getCamera1Manager() {
        return this.camera1Manager;
    }

    public FVCamera2Manager getCamera2Manager() {
        return this.camera2Manager;
    }

    public int getCameraManagerType() {
        return this.cameraManagerStyle;
    }

    public String showModestatus() {
        if (this.cameraManagerStyle == 1) {
            return "Not support!";
        }
        return this.camera2Manager.showModestatus();
    }

    public boolean isSupportHighSpeedVideo() {
        if (this.cameraManagerStyle == 1) {
            return false;
        }
        return this.camera2Manager.isSupportHighSpeedVideo();
    }

    public Map<Range<Integer>, Size[]> getSlowMotionFpsResolutionMap() {
        if (this.cameraManagerStyle == 1) {
            return null;
        }
        return this.camera2Manager.getSlowMotionFpsResolutionMap();
    }

    public Map<Range<Integer>, Size[]> getHighSpeedFpsRangeResolutionsMap() {
        if (this.cameraManagerStyle == 1) {
            return null;
        }
        return this.camera2Manager.getHighSpeedFpsRangeResolutionsMap();
    }

    public Map<Size, Range<Integer>[]> getHighSpeedResolutionFpsRangesMap() {
        if (this.cameraManagerStyle == 1) {
            return null;
        }
        return this.camera2Manager.getHighSpeedResolutionFpsRangesMap();
    }

    public Size[] getHighSpeedResolutionsForFpsRange(Range<Integer> range) {
        if (this.cameraManagerStyle == 1) {
            return null;
        }
        return this.camera2Manager.getHighSpeedResolutionsForFpsRange(range);
    }

    public Range<Integer>[] getHighSpeedFpsRangesForResolution(Size size) {
        if (this.cameraManagerStyle == 1) {
            return null;
        }
        return this.camera2Manager.getHighSpeedFpsRangesForResolution(size);
    }
}
