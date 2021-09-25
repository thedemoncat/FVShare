package com.freevisiontech.cameralib;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.DngCreator;
import android.hardware.camera2.params.ColorSpaceTransform;
import android.hardware.camera2.params.RggbChannelVector;
import android.location.Location;
import android.media.Image;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Range;
import android.view.Surface;
import com.freevisiontech.cameralib.impl.Camera2.Camera2Constants;
import com.freevisiontech.cameralib.impl.Camera2.Camera2Manager;
import com.freevisiontech.cameralib.impl.Camera2.CameraParameters;
import com.freevisiontech.cameralib.utils.CameraUtils;
import com.freevisiontech.cameralib.view.camera2.CameraTextureView;
import com.freevisiontech.cameralib.view.camera2.CameraView;
import com.freevisiontech.filterlib.FilterType;
import com.freevisiontech.tracking.FVTrackManagerEx;
import com.freevisiontech.tracking.FVTrackObserver;
import com.freevisiontech.utils.ScreenOrientationUtil;
import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageFilter;

public class FVCamera2Manager implements Camera2Manager.PreviewCallback {
    private static final String TAG = "FVCamera2Manager";
    private boolean istrack = false;
    /* access modifiers changed from: private */
    public boolean isvideostabopened = false;
    FVCamereManagerCallback mCallback = null;
    private CameraView mCameraView = null;
    private Camera2Manager mCameramanager;
    private int mCamereManagerMode = 1;
    private Activity mContext;
    /* access modifiers changed from: private */
    public FVTrackManagerEx trackManager = null;

    public void onPreviewFrame(byte[] data) {
        CameraUtils.LogV("HTrack_onPreFrame", "FVCamera2Manager onPreviewFrame " + data.length);
        long tb = System.currentTimeMillis();
        FVTrackManagerEx tracker = getTrackManager();
        if (tracker != null && tracker.isIstrack()) {
            tracker.update(data);
        }
        CameraUtils.LogV("HTrack", "track duration=" + (System.currentTimeMillis() - tb));
    }

    public FVCamera2Manager(Activity context, CameraView cameraView, CameraParameters parameters, FVCamereManagerCallback callback) {
        this.mContext = context;
        this.mCameraView = cameraView;
        this.mCallback = callback;
        this.mCameramanager = new Camera2Manager(this.mContext, this, parameters);
        this.mCameramanager.setCameraStatusListener(new Camera2Manager.CameraStatusListener() {
            public void onOpened(@NonNull CameraDevice cameraDevice) {
                if (FVCamera2Manager.this.mCallback != null) {
                    FVCamera2Manager.this.mCallback.onCameraStarted();
                }
            }

            public void onDisconnected(@NonNull CameraDevice cameraDevice) {
                if (FVCamera2Manager.this.mCallback != null) {
                    FVCamera2Manager.this.mCallback.onCameraDisconnected();
                }
            }

            public void onError(@NonNull CameraDevice cameraDevice, int error) {
                if (FVCamera2Manager.this.mCallback != null) {
                    FVCamera2Manager.this.mCallback.onCameraError(error);
                }
            }

            public void onClosed(@NonNull CameraDevice camera) {
                if (FVCamera2Manager.this.mCallback != null) {
                    FVCamera2Manager.this.mCallback.onCameraClosed();
                }
            }

            public void onSessionClosed(@NonNull CameraCaptureSession session) {
            }

            public void onSessionActive(@NonNull CameraCaptureSession session) {
            }

            public void onSessionSurfacePrepared(@NonNull CameraCaptureSession session, @NonNull Surface surface) {
            }
        });
        this.mCameramanager.setPerviewView(this.mCameraView);
        this.mCameraView.addCameraTextureView();
    }

    public boolean destroy() {
        CameraUtils.LogV(TAG, "----destory---------");
        if (this.mCameramanager.getCamera() != null) {
            stopMediaRecord();
            stopTrack();
        }
        return this.mCameramanager.destroy();
    }

    public void setPreviewCallback(Camera2Manager.PreviewCallback previewCallback) {
        this.mCameramanager.setPreviewCallback(previewCallback);
    }

    public void setCameraStatesListener(Camera2Manager.CameraStatesListener cameraStatesListener) {
        this.mCameramanager.setCameraStatesListener(cameraStatesListener);
    }

    public CameraDevice getCamera() {
        return this.mCameramanager.getCamera();
    }

    public CameraTextureView getCameraTextureView() {
        return this.mCameraView.getCameraTextureView();
    }

    public boolean isCameraOpened() {
        return this.mCameramanager.isCameraOpened();
    }

    public boolean setupCamera(int viewWidth, int viewHeight) {
        CameraUtils.LogV(TAG, "----setupCamera---------");
        return setupCamera(this.mCameramanager.getFacing(), viewWidth, viewHeight);
    }

    public boolean setupCamera(int face, int viewWidth, int viewHeight) {
        return this.mCameramanager.startCamera(face, viewWidth, viewHeight);
    }

    public boolean stopCamera() {
        if (this.mCameramanager.getCamera() == null) {
            return false;
        }
        stopMediaRecord();
        stopTrack();
        return this.mCameramanager.stopCamera();
    }

    public boolean isSupportManualMode() {
        return this.mCameramanager.isSupportManualMode();
    }

    public void setFacing(int facing) {
        if (!this.mCameramanager.setFacing(facing)) {
        }
    }

    public int getFacing() {
        return this.mCameramanager.getFacing();
    }

    public AspectRatio getAspectRatio() {
        return this.mCameramanager.getAspectRatio();
    }

    public Size getPreviewResolution() {
        return this.mCameramanager.getPreviewRosolution();
    }

    public boolean isContainPreviewResolution(Size res) {
        return this.mCameramanager.isContainPreviewResolution(res);
    }

    public Size getPreviewFrameSize() {
        return this.mCameramanager.getPreviewFrameSize();
    }

    public void setFlashMode(int flash) {
        this.mCameramanager.setFlashMode(flash);
    }

    public int getFlashMode() {
        return this.mCameramanager.getFlashMode();
    }

    public boolean enableMFMode(boolean toogle) {
        if (toogle) {
            return this.mCameramanager.enableMFControl();
        }
        return this.mCameramanager.enableAFControl();
    }

    public boolean enableMFModeEx(boolean toogle) {
        if (!toogle) {
            return this.mCameramanager.enableAFControl();
        }
        float fodis = this.mCameramanager.getFocusDistance(0);
        boolean result = this.mCameramanager.enableMFControl();
        if (!result || fodis <= 0.0f) {
            return result;
        }
        this.mCameramanager.setFoucsDistance(fodis);
        return result;
    }

    public int[] getAvailableAFMode() {
        return this.mCameramanager.getAvailableAFMode();
    }

    public int getFocusMode() {
        return this.mCameramanager.getFocusMode();
    }

    public boolean setAutoFocusMode(boolean auto) {
        if (auto) {
            return setAutoFocusMode();
        }
        return setManualFocusMode();
    }

    public boolean setAutoFocusMode() {
        return this.mCameramanager.setAutoFocusMode();
    }

    public boolean setManualFocusMode() {
        return this.mCameramanager.enableMFControl();
    }

    public boolean isAutoFocus() {
        return this.mCameramanager.isAutoFocusMode();
    }

    public boolean isMaunalFocus() {
        return this.mCameramanager.isMFEnabled();
    }

    public boolean setFocusMode(int mode) {
        return this.mCameramanager.setFocusModeEx(mode);
    }

    public boolean lockFocus(final Camera2Manager.AutoFocusListener autoFocusListener) {
        if (!this.mCameramanager.isCameraOpened()) {
            return false;
        }
        this.mCameramanager.setAutoFocusListener(new Camera2Manager.AutoFocusListener() {
            public void focusLocked() {
                if (autoFocusListener != null) {
                    autoFocusListener.focusLocked();
                }
            }

            public void focusUnlocked() {
                if (autoFocusListener != null) {
                    autoFocusListener.focusUnlocked();
                }
            }
        });
        return this.mCameramanager.lockFocus();
    }

    public boolean isFocusLocked() {
        return this.mCameramanager.isFocusLocked(0);
    }

    public boolean unlockFocus(final Camera2Manager.AutoFocusListener autoFocusListener) {
        if (!this.mCameramanager.isCameraOpened()) {
            return false;
        }
        this.mCameramanager.setAutoFocusListener(new Camera2Manager.AutoFocusListener() {
            public void focusLocked() {
                if (autoFocusListener != null) {
                    autoFocusListener.focusLocked();
                }
            }

            public void focusUnlocked() {
                if (autoFocusListener != null) {
                    autoFocusListener.focusUnlocked();
                }
            }
        });
        return this.mCameramanager.unlockFocus();
    }

    public boolean isSupportAutoFocus() {
        return this.mCameramanager.isSupportAutoFocus();
    }

    public boolean autoFocus(double x, double y, boolean islockae, Camera2Manager.AutoFocusListener autoFocusListener) {
        return autoFocus(this.mCameramanager.screenPoint2FocusRegion(x, y), islockae, autoFocusListener);
    }

    private boolean autoFocus(Rect focusarea, boolean islockae, final Camera2Manager.AutoFocusListener autoFocusListener) {
        if (!this.mCameramanager.isCameraOpened()) {
            return false;
        }
        this.mCameramanager.setAutoFocusListener(new Camera2Manager.AutoFocusListener() {
            public void focusLocked() {
                if (autoFocusListener != null) {
                    autoFocusListener.focusLocked();
                }
            }

            public void focusUnlocked() {
            }
        });
        return this.mCameramanager.autoFocusLock(focusarea, islockae);
    }

    public boolean cancelAutoFocus(final Camera2Manager.AutoFocusListener autoFocusListener) {
        if (!this.mCameramanager.isCameraOpened()) {
            return false;
        }
        this.mCameramanager.setAutoFocusListener(new Camera2Manager.AutoFocusListener() {
            public void focusLocked() {
            }

            public void focusUnlocked() {
                if (autoFocusListener != null) {
                    autoFocusListener.focusUnlocked();
                }
            }
        });
        return this.mCameramanager.cancelAutoFocusLock();
    }

    public float getFocalLength() {
        return this.mCameramanager.getFocalLength(0);
    }

    public boolean isFocalSupport() {
        return this.mCameramanager.isFocalSupported();
    }

    public float[] getAvailableFocalLengths() {
        return this.mCameramanager.getAvailableFocalLengths();
    }

    public boolean setFocalLength(float focallen) {
        return this.mCameramanager.setFocalLength(focallen);
    }

    public float getFocusDistance() {
        return this.mCameramanager.getFocusDistance(0);
    }

    public float getMinFocusDistance() {
        return this.mCameramanager.getMinFocusDistance();
    }

    public boolean setFocusDistance(float focusdistance) {
        return this.mCameramanager.setFoucsDistance(focusdistance);
    }

    public boolean setZoom(float degree) {
        CameraUtils.LogV("CameraZoom", "degree/max=" + degree + "/" + getMaxZoom());
        if (degree < 1.0f) {
            return false;
        }
        return this.mCameramanager.setZoom(degree * getZoomStep());
    }

    public boolean setZoomEx(float degree) {
        return this.mCameramanager.setZoomEx(degree * getZoomStep());
    }

    public float getMaxZoom() {
        return this.mCameramanager.getMaxZoom() / getZoomStep();
    }

    public float getZoomStep() {
        return this.mCameramanager.getZoomStep();
    }

    public boolean isZoomSupported() {
        if (getMaxZoom() <= 0.0f) {
            return false;
        }
        return true;
    }

    public boolean setSceneMode(int mode) {
        return this.mCameramanager.setSceneModeEx(mode);
    }

    public boolean disableSceneMode() {
        return this.mCameramanager.disableSceneMode();
    }

    public int getSceneMode() {
        return this.mCameramanager.getSceneMode();
    }

    public int[] getSupportedSceneModes() {
        return this.mCameramanager.getSupportedSceneModes();
    }

    public boolean isSupportHdrMode() {
        return this.mCameramanager.isSupportHdrMode();
    }

    public boolean setHdrMode(boolean ison) {
        return this.mCameramanager.setHdrScene(ison);
    }

    public boolean setFPSRange(Range<Integer> fpsrange) {
        return this.mCameramanager.setFPSRange(fpsrange);
    }

    public boolean setFPS(int fps) {
        return this.mCameramanager.setFps(fps);
    }

    public Range<Integer> getFPS() {
        return this.mCameramanager.getFPSRange();
    }

    public Range<Integer>[] getSupportedPreviewFPSRanges() {
        return this.mCameramanager.getSupportedPreviewFPSRanges();
    }

    public List<Range<Integer>> getRangesConstainsFps(int fps) {
        return this.mCameramanager.getRangesConstainsFps(fps);
    }

    public boolean isSupportPreviewFPS(int fps) {
        if (getRangesConstainsFps(fps).isEmpty()) {
            return false;
        }
        return true;
    }

    public int[] getAvailabelAEModes() {
        return this.mCameramanager.getAvailableAEMode();
    }

    public boolean isMEMode() {
        return this.mCameramanager.isMEMode();
    }

    public boolean isAEMode() {
        return this.mCameramanager.isAEMode();
    }

    public boolean enableMEMode(boolean toogle) {
        if (toogle) {
            return this.mCameramanager.enableMEControl();
        }
        return this.mCameramanager.enableAEControl();
    }

    public boolean enableMEModeEx(boolean toogle) {
        if (!toogle) {
            return this.mCameramanager.enableAEControl();
        }
        int ios = this.mCameramanager.getISOValue(0);
        long ext = this.mCameramanager.getExposureTime(0);
        boolean result = this.mCameramanager.enableMEControl();
        if (!result) {
            return result;
        }
        this.mCameramanager.setExposureTime(ext);
        this.mCameramanager.setISOValue(Integer.valueOf(ios));
        return result;
    }

    public boolean lockAutoExposure(boolean toogle) {
        return this.mCameramanager.lockAutoExposure(toogle);
    }

    public boolean isAutoExposureLock() {
        return this.mCameramanager.isAutoExposureLock(0);
    }

    public int getISOValue() {
        return this.mCameramanager.getISOValue(0);
    }

    public boolean setISOValue(int value) {
        return this.mCameramanager.setISOValue(Integer.valueOf(value));
    }

    public Range<Integer> getIsoRange() {
        return this.mCameramanager.getISORanger();
    }

    public int getExposureCompensation() {
        return this.mCameramanager.getExposureCompensationValue(0);
    }

    public boolean setExposureCompensation(int value) {
        Range<Integer> range = getExposureCompensationRanger();
        if (!range.contains(Integer.valueOf(value))) {
            return false;
        }
        boolean result = this.mCameramanager.setExposureCompensationValue(value);
        if (!result) {
            CameraUtils.LogV(TAG, "setExposureCompensation fail " + getExposureCompensation() + " range is from " + range.getLower() + " to " + range.getUpper() + "now is " + value);
            return result;
        }
        CameraUtils.LogV(TAG, "setExposureCompensation   " + getExposureCompensation() + " range is from " + range.getLower() + " to " + range.getUpper() + "now is " + value);
        return result;
    }

    public Range<Integer> getExposureCompensationRanger() {
        return this.mCameramanager.getExposureCompensationRanger();
    }

    public float getExposureCompensationStep() {
        return this.mCameramanager.getExposureCompensationStep();
    }

    public Range<Long> getExposureTimeRange() {
        return this.mCameramanager.getExposureTimeRange();
    }

    public boolean setExposureTime(long value) {
        return this.mCameramanager.setExposureTime(value);
    }

    public long getExposureTime() {
        return this.mCameramanager.getExposureTime(0);
    }

    public int[] getAvailableAWBModes() {
        return this.mCameramanager.getAvailableAWBModes();
    }

    public List<String> getAvailableWhiteBalance() {
        List<String> list = new ArrayList<>();
        int[] modes = getAvailableAWBModes();
        for (int WhiteBalanceMode2WhiteBalanceStr : modes) {
            list.add(CameraUtils.WhiteBalanceMode2WhiteBalanceStr(WhiteBalanceMode2WhiteBalanceStr));
        }
        return list;
    }

    public int getWhiteBalanceMode() {
        return this.mCameramanager.getWhiteBalanceMode();
    }

    public String getWhiteBalance() {
        return CameraUtils.WhiteBalanceMode2WhiteBalanceStr(getWhiteBalanceMode());
    }

    public boolean setWhiteBalanceMode(int mode) {
        return this.mCameramanager.setWhiteBalanceModeEx(mode);
    }

    public boolean setWhiteBalance(String mode) {
        int wbmode = CameraUtils.WhiteBalanceStr2WhiteBalanceMode(mode);
        if (wbmode == -1) {
            return false;
        }
        return setWhiteBalanceMode(wbmode);
    }

    public boolean lockAutoWhiteBalance(boolean enable) {
        return this.mCameramanager.lockAWB(enable);
    }

    public boolean isAutoWhiteBalanceLocked() {
        return this.mCameramanager.isAWBLocked(0);
    }

    public boolean enableMWBMode(boolean toogle) {
        if (toogle) {
            return this.mCameramanager.enableMWBControl();
        }
        return this.mCameramanager.enableAWBControl();
    }

    public boolean enableMWBModeEx(boolean toogle) {
        if (!toogle) {
            return this.mCameramanager.enableAWBControl();
        }
        if (this.mCameramanager.getCameraHWLevel() == 0) {
            return false;
        }
        RggbChannelVector gain = this.mCameramanager.getWBGainValue(0);
        boolean enableMWBControl = this.mCameramanager.enableMWBControl();
        this.mCameramanager.setWBGainValueEx(gain);
        return enableMWBControl;
    }

    public int getColorCorrectionMode() {
        return this.mCameramanager.getColorCorrectionMode();
    }

    public Range<Float> getWBGainRange() {
        return this.mCameramanager.getWBGainRange();
    }

    public RggbChannelVector getWBGainValue() {
        return this.mCameramanager.getWBGainValue(0);
    }

    public int getWBGainFactor() {
        RggbChannelVector v = getWBGainValue();
        if (v == null) {
            return -1;
        }
        return CameraUtils.FactorFromWbTemperature(v);
    }

    public boolean setWBGainValue(RggbChannelVector vector) {
        return this.mCameramanager.setWBGainValueEx(vector);
    }

    public boolean setWBGainValue(int facotor) {
        if (facotor < 0 || facotor > 100) {
            return false;
        }
        return setWBGainValue(CameraUtils.WBTemperatureFromFactor(facotor));
    }

    public boolean isMWBMode() {
        return this.mCameramanager.isMWBEnabled();
    }

    public boolean isAWBMode() {
        return this.mCameramanager.isAWBEnabled();
    }

    public boolean setColorCorrectionTransform(ColorSpaceTransform matrix) {
        return this.mCameramanager.setColorCorrectionTransform(matrix);
    }

    public ColorSpaceTransform getColorCorrectionTransform() {
        return this.mCameramanager.getColorCorrectionTransform();
    }

    public String getColorEffect() {
        return null;
    }

    public List<String> getSupportedColorEffects() {
        return null;
    }

    public boolean setColorEffect(String value) {
        return false;
    }

    public SizeMap getVideoSizeMap(int face) {
        return this.mCameramanager.getVideoSizeMap(face);
    }

    public SizeMap getVideoSizeMap() {
        return this.mCameramanager.getVideoSizeMap();
    }

    public Size[] getVideoSizes(int face) {
        return this.mCameramanager.getVideoSizes(face);
    }

    public Size[] getVideoSizes() {
        return this.mCameramanager.getVideoSizes();
    }

    public boolean isSupportedVideoSize(Size size) {
        Size[] sizes = getVideoSizes();
        if (sizes == null) {
            return false;
        }
        for (int i = 0; i < sizes.length; i++) {
            if (sizes[i].getWidth() == size.getWidth() && sizes[i].getHeight() == size.getHeight()) {
                return true;
            }
        }
        return false;
    }

    public boolean isSupportedVideoSize(Size size, int face) {
        Size[] sizes = getVideoSizes(face);
        if (sizes == null) {
            return false;
        }
        for (int i = 0; i < sizes.length; i++) {
            if (sizes[i].getWidth() == size.getWidth() && sizes[i].getHeight() == size.getHeight()) {
                return true;
            }
        }
        return false;
    }

    public int getPictureFormat() {
        return this.mCameramanager.getPictureFormat();
    }

    public Size[] getRawPictureSupportedResolutions(int face) {
        return this.mCameramanager.getRawPhotoSizes(face);
    }

    public SizeMap getPictureSupportedResolutions() {
        return this.mCameramanager.getPictureSizeMap();
    }

    public SizeMap getPictureSupportedResolutions(int face) {
        return this.mCameramanager.getPictureSizeMap(face);
    }

    public Size getMaxSupportedPictureSize() {
        return getMaxSupportedPictureSize(this.mCameramanager.getFacing());
    }

    public Size getMaxSupportedPictureSize(int face) {
        Size size = this.mCameramanager.getPictureMaxSize(face, AspectRatio.m1507of(16, 9), 9999);
        return (size == null || size.getWidth() <= 1920 || size.getHeight() <= 1080) ? this.mCameramanager.getPictureMaxSize(face, (AspectRatio) null, 9999) : size;
    }

    public boolean isPictureSizeSupported(Size size, int face) {
        for (Size s : this.mCameramanager.getPictureSizes(face)) {
            if (size.getWidth() == s.getWidth() && size.getHeight() == s.getHeight()) {
                return true;
            }
        }
        return false;
    }

    public Size getRecommendPictureSize() {
        return Camera2Constants.RecommendPictureSize;
    }

    public void takePhoto(int width, int height, Location location, int phototype) {
        Size size;
        int rotation = ScreenOrientationUtil.getInstance().getOrientation();
        if (width > height) {
            size = new Size(width, height);
        } else {
            if (this.mCameramanager.getFacing() == 0) {
                if (rotation == 90) {
                    rotation = 270;
                } else if (rotation == 270) {
                    rotation = 90;
                }
            }
            size = new Size(height, width);
        }
        this.mCameramanager.setTakePictureListener(new Camera2Manager.TakePictureListener() {
            public void onPictureTaken(byte[] data) {
                Bitmap bitmap = null;
                if (data != null) {
                    bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                }
                if (FVCamera2Manager.this.mCallback != null) {
                    FVCamera2Manager.this.mCallback.onPictureTaken(bitmap);
                }
            }

            public void onPictureTakenError(int reason) {
                if (FVCamera2Manager.this.mCallback != null) {
                    FVCamera2Manager.this.mCallback.onPictureTakeError(reason);
                }
            }

            public void captureStillPictureError(int reason) {
                if (FVCamera2Manager.this.mCallback != null) {
                    FVCamera2Manager.this.mCallback.onPictureTakeError(reason);
                }
            }

            public void captureStillPictureCompleted() {
                if (FVCamera2Manager.this.mCallback != null) {
                    FVCamera2Manager.this.mCallback.onPictureTakeCompleted();
                }
            }

            public void onRawPictureTaken(Image image, DngCreator dngCreator) {
                if (FVCamera2Manager.this.mCallback != null) {
                    FVCamera2Manager.this.mCallback.onRawPictureTaken(image, dngCreator);
                }
            }
        });
        this.mCameramanager.takePicture(rotation, size, location, phototype);
    }

    public boolean isMediaRecordQualitySupports(int face, int quality) {
        return this.mCameramanager.isMediaRecordQualitySupported(face, quality);
    }

    public boolean isMediaRecordQualitySupports(int quality) {
        return isMediaRecordQualitySupports(this.mCameramanager.getFacing(), quality);
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
        return getSupportedMediaRecordQuality(this.mCameramanager.getFacing());
    }

    public int[] getSupportedMediaRecordQuality(int face) {
        List<Integer> list = this.mCameramanager.getAvailableMediaRecordQuality(face);
        int[] a = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            a[i] = list.get(i).intValue();
        }
        return a;
    }

    public Size[] getSupportedMediaRecordSizes() {
        return getSupportedMediaRecordSizes(this.mCameramanager.getFacing());
    }

    public Size[] getSupportedMediaRecordSizes(int face) {
        Size[] sizes = this.mCameramanager.getVideoSizes(face);
        List<Size> sizeList = new ArrayList<>();
        Size s = CameraUtils.GetPreferSize(sizes, Constants.Size_Video_QUALITY_8, false, isMediaRecordQualitySupports(face, 8));
        if (s != null) {
            sizeList.add(s);
        }
        Size s2 = CameraUtils.GetPreferSize(sizes, Constants.Size_Video_QUALITY_6, false, isMediaRecordQualitySupports(face, 6));
        if (s2 != null) {
            sizeList.add(s2);
        }
        Size s3 = CameraUtils.GetPreferSize(sizes, Constants.Size_Video_QUALITY_5, false, isMediaRecordQualitySupports(face, 5));
        if (s3 != null) {
            sizeList.add(s3);
        }
        Size s4 = CameraUtils.GetPreferSize(sizes, Constants.Size_Video_QUALITY_4, false, isMediaRecordQualitySupports(face, 4));
        if (s4 != null) {
            sizeList.add(s4);
        }
        return CameraUtils.SizeList2SizeArray(sizeList);
    }

    public int[] getSupportedTimeLapseMediaRecordQuality(int face) {
        List<Integer> list = this.mCameramanager.getAvailableTimeLapseQuality(face);
        int[] a = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            a[i] = list.get(i).intValue();
        }
        return a;
    }

    public int[] getSupportedTimeLapseMediaRecordQuality() {
        return getSupportedTimeLapseMediaRecordQuality(this.mCameramanager.getFacing());
    }

    public int[] getSupportedHighSpeedMediaRecordQuality(int face) {
        List<Integer> list = this.mCameramanager.getAvailableHighSpeedMediaRecordQuality(face);
        int[] a = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            a[i] = list.get(i).intValue();
        }
        return a;
    }

    public int[] getSupportedHighSpeedMediaRecordQuality() {
        return getSupportedHighSpeedMediaRecordQuality(this.mCameramanager.getFacing());
    }

    public boolean startMediaRecordEx(FileDescriptor fd, int quality) {
        int orientation = ScreenOrientationUtil.getInstance().getOrientation();
        if (getFacing() == 0) {
            if (orientation == 90) {
                orientation = 270;
            } else if (orientation == 270) {
                orientation = 90;
            }
        }
        return startMediaRecordEx(fd, quality, orientation);
    }

    public boolean startMediaRecordEx(String videopath, int quality) {
        int orientation = ScreenOrientationUtil.getInstance().getOrientation();
        if (getFacing() == 0) {
            if (orientation == 90) {
                orientation = 270;
            } else if (orientation == 270) {
                orientation = 90;
            }
        }
        return startMediaRecordEx(videopath, quality, orientation);
    }

    public boolean startMediaRecordEx(final FileDescriptor fd, int quality, int orientation) {
        if (quality < 0 || quality > 8) {
            return false;
        }
        this.mCameramanager.setRecordListener(new Camera2Manager.RecordListener() {
            public void onRecordStarted() {
                if (FVCamera2Manager.this.mCallback != null) {
                    FVCamera2Manager.this.mCallback.onRecordStarted();
                }
            }

            public void onRecordError(String reason) {
                if (FVCamera2Manager.this.mCallback != null) {
                    FVCamera2Manager.this.mCallback.onRecordError(reason);
                }
            }

            public void onRecordEnd() {
                if (FVCamera2Manager.this.mCallback != null) {
                    FVCamera2Manager.this.mCallback.onRecordEnd(fd.toString());
                }
            }
        });
        return this.mCameramanager.startMediaRecord(fd, quality, -1.0d, orientation);
    }

    public boolean startMediaRecordEx(final String videopath, int quality, int orientation) {
        if (quality < 0 || quality > 8) {
            return false;
        }
        this.mCameramanager.setRecordListener(new Camera2Manager.RecordListener() {
            public void onRecordStarted() {
                if (FVCamera2Manager.this.mCallback != null) {
                    FVCamera2Manager.this.mCallback.onRecordStarted();
                }
            }

            public void onRecordError(String reason) {
                if (FVCamera2Manager.this.mCallback != null) {
                    FVCamera2Manager.this.mCallback.onRecordError(reason);
                }
            }

            public void onRecordEnd() {
                if (FVCamera2Manager.this.mCallback != null) {
                    FVCamera2Manager.this.mCallback.onRecordEnd(videopath);
                }
            }
        });
        return this.mCameramanager.startMediaRecord(videopath, quality, -1.0d, orientation);
    }

    public double getMediaRecordVolumeAmplitude() {
        return this.mCameramanager.getMediaRecordVolumeAmplitude();
    }

    public boolean startTimeLapseMediaRecordEx(FileDescriptor fd, int quality, double timeinterval) {
        int orientation = ScreenOrientationUtil.getInstance().getOrientation();
        if (getFacing() == 0) {
            if (orientation == 90) {
                orientation = 270;
            } else if (orientation == 270) {
                orientation = 90;
            }
        }
        return startTimeLapseMediaRecordEx(fd, quality, orientation, timeinterval);
    }

    public boolean startTimeLapseMediaRecordEx(String videopath, int quality, double timeinterval) {
        int orientation = ScreenOrientationUtil.getInstance().getOrientation();
        if (getFacing() == 0) {
            if (orientation == 90) {
                orientation = 270;
            } else if (orientation == 270) {
                orientation = 90;
            }
        }
        return startTimeLapseMediaRecordEx(videopath, quality, orientation, timeinterval);
    }

    public boolean startTimeLapseMediaRecordEx(final String videopath, int quality, int orientation, double timeinterval) {
        if (quality < 1000 || quality > 1008) {
            return false;
        }
        this.mCameramanager.setRecordListener(new Camera2Manager.RecordListener() {
            public void onRecordStarted() {
                if (FVCamera2Manager.this.mCallback != null) {
                    FVCamera2Manager.this.mCallback.onRecordStarted();
                }
            }

            public void onRecordError(String reason) {
                if (FVCamera2Manager.this.mCallback != null) {
                    FVCamera2Manager.this.mCallback.onRecordError(reason);
                }
            }

            public void onRecordEnd() {
                if (FVCamera2Manager.this.mCallback != null) {
                    FVCamera2Manager.this.mCallback.onRecordEnd(videopath);
                }
            }
        });
        return this.mCameramanager.startMediaRecord(videopath, quality, 1.0d / timeinterval, orientation);
    }

    public boolean startTimeLapseMediaRecordEx(final FileDescriptor fd, int quality, int orientation, double timeinterval) {
        if (quality < 1000 || quality > 1008) {
            return false;
        }
        this.mCameramanager.setRecordListener(new Camera2Manager.RecordListener() {
            public void onRecordStarted() {
                if (FVCamera2Manager.this.mCallback != null) {
                    FVCamera2Manager.this.mCallback.onRecordStarted();
                }
            }

            public void onRecordError(String reason) {
                if (FVCamera2Manager.this.mCallback != null) {
                    FVCamera2Manager.this.mCallback.onRecordError(reason);
                }
            }

            public void onRecordEnd() {
                if (FVCamera2Manager.this.mCallback != null) {
                    FVCamera2Manager.this.mCallback.onRecordEnd(fd.toString());
                }
            }
        });
        return this.mCameramanager.startMediaRecord(fd, quality, 1.0d / timeinterval, orientation);
    }

    public boolean startMediaRecord(String videopath, Size size, int fps) {
        int orientation = ScreenOrientationUtil.getInstance().getOrientation();
        if (getFacing() == 0) {
            if (orientation == 90) {
                orientation = 270;
            } else if (orientation == 270) {
                orientation = 90;
            }
        }
        return startMediaRecord(videopath, size, fps, orientation);
    }

    public boolean startMediaRecord(FileDescriptor fd, Size size, int fps) {
        int orientation = ScreenOrientationUtil.getInstance().getOrientation();
        if (getFacing() == 0) {
            if (orientation == 90) {
                orientation = 270;
            } else if (orientation == 270) {
                orientation = 90;
            }
        }
        return startMediaRecord(fd, size, fps, orientation);
    }

    public boolean startMediaRecord(final String videopath, Size size, int fps, int orientation) {
        this.mCameramanager.setRecordListener(new Camera2Manager.RecordListener() {
            public void onRecordStarted() {
                if (FVCamera2Manager.this.mCallback != null) {
                    FVCamera2Manager.this.mCallback.onRecordStarted();
                }
            }

            public void onRecordError(String reason) {
                if (FVCamera2Manager.this.mCallback != null) {
                    FVCamera2Manager.this.mCallback.onRecordError(reason);
                }
            }

            public void onRecordEnd() {
                if (FVCamera2Manager.this.mCallback != null) {
                    FVCamera2Manager.this.mCallback.onRecordEnd(videopath);
                }
            }
        });
        return this.mCameramanager.startMediaRecord(videopath, size, fps, orientation);
    }

    public boolean startMediaRecord(final FileDescriptor fd, Size size, int fps, int orientation) {
        this.mCameramanager.setRecordListener(new Camera2Manager.RecordListener() {
            public void onRecordStarted() {
                if (FVCamera2Manager.this.mCallback != null) {
                    FVCamera2Manager.this.mCallback.onRecordStarted();
                }
            }

            public void onRecordError(String reason) {
                if (FVCamera2Manager.this.mCallback != null) {
                    FVCamera2Manager.this.mCallback.onRecordError(reason);
                }
            }

            public void onRecordEnd() {
                if (FVCamera2Manager.this.mCallback != null) {
                    FVCamera2Manager.this.mCallback.onRecordEnd(fd.toString());
                }
            }
        });
        return this.mCameramanager.startMediaRecord(fd, size, fps, orientation);
    }

    public boolean stopMediaRecord() {
        return this.mCameramanager.stopMediaRecord();
    }

    public boolean isMediaRecording() {
        return this.mCameramanager.isMediaRecording();
    }

    public boolean isVideoStabilizationSupport() {
        return this.mCameramanager.isVideoStabilizationSupport();
    }

    public boolean isOpticalVideoStabilizationSupport() {
        return this.mCameramanager.isOpticalVideoStabilizationSupport();
    }

    public boolean isDigitalVideoStabilizationSupport() {
        return this.mCameramanager.isDigitalVideoStabilizationSupport();
    }

    public int videoStabilizationType() {
        return this.mCameramanager.videoStabilizationType();
    }

    public boolean isVideoStabilizationOpened() {
        return this.mCameramanager.isVideoStabilizationOpened();
    }

    public boolean isOpticalVideoStabilizationOpened() {
        return this.mCameramanager.isOpticalVideoStabilizationOpened();
    }

    public boolean isDigitalVideoStabilizationOpened() {
        return this.mCameramanager.isDigitalVideoStabilizationOpened();
    }

    public boolean closeVideoStabilizationMode() {
        return this.mCameramanager.setVideoStabilizationMode(0);
    }

    public boolean openVideoStabilizationMode() {
        return this.mCameramanager.setVideoStabilizationMode(1);
    }

    public boolean isSupportHighSpeedVideo() {
        int[] caps;
        if (Build.VERSION.SDK_INT < 23 || isHighSpeedExcludeProduct() || (caps = this.mCameramanager.getCameraAvailableCapability()) == null) {
            return false;
        }
        for (int i : caps) {
            if (i == 9) {
                return true;
            }
        }
        return false;
    }

    public Size[] getHighSpeedResolutions() {
        return this.mCameramanager.getHighSpeedVideoSizes();
    }

    public Range<Integer>[] getHighSpeedFpsRanges() {
        return this.mCameramanager.getHighSpeedVideoFpsRange();
    }

    public Range<Integer>[] getHighSpeedFpsRangesForResolution(Size size) {
        return this.mCameramanager.getHighSpeedFpsRangeForSize(size);
    }

    public Size[] getHighSpeedResolutionsForFpsRange(Range<Integer> range) {
        return this.mCameramanager.getHighSpeedVideoSizesForRange(range);
    }

    public Map<Size, Range<Integer>[]> getHighSpeedResolutionFpsRangesMap() {
        Size[] sizes;
        Map<Size, Range<Integer>[]> map = new HashMap<>();
        if (isSupportHighSpeedVideo() && (sizes = getHighSpeedResolutions()) != null) {
            for (Size size : sizes) {
                Range<Integer>[] ranges = getHighSpeedFpsRangesForResolution(size);
                if (ranges != null) {
                    map.put(size, ranges);
                }
            }
        }
        return map;
    }

    public Map<Range<Integer>, Size[]> getHighSpeedFpsRangeResolutionsMap() {
        Range<Integer>[] ranges;
        Map<Range<Integer>, Size[]> map = new HashMap<>();
        if (isSupportHighSpeedVideo() && (ranges = getHighSpeedFpsRanges()) != null) {
            for (Range<Integer> range : ranges) {
                Size[] sizes = getHighSpeedResolutionsForFpsRange(range);
                if (sizes != null) {
                    map.put(range, sizes);
                }
            }
        }
        return map;
    }

    private boolean isHighSpeedExcludeProduct() {
        if (Constants.HighSpeedRecord_Exclude_Product.contains(Build.PRODUCT.trim().toLowerCase())) {
            return true;
        }
        return false;
    }

    public Map<Range<Integer>, Size[]> getSlowMotionFpsResolutionMap() {
        Size[] sizes;
        Map<Range<Integer>, Size[]> smap = new HashMap<>();
        if (isSupportHighSpeedVideo()) {
            Map<Range<Integer>, Size[]> map = getHighSpeedFpsRangeResolutionsMap();
            if (!map.isEmpty()) {
                for (Range<Integer> r : map.keySet()) {
                    if (r.getLower().intValue() == r.getUpper().intValue() && (sizes = getHighSpeedResolutionsForFpsRange(r)) != null) {
                        smap.put(r, sizes);
                    }
                }
            }
        }
        return smap;
    }

    public boolean enableManualMode(boolean toogle) {
        boolean result;
        if (!toogle) {
            return enableMWBMode(false) && enableMFMode(false) && enableMEMode(false);
        }
        int ios = this.mCameramanager.getISOValue(0);
        long ext = this.mCameramanager.getExposureTime(0);
        float fodis = this.mCameramanager.getFocusDistance(0);
        RggbChannelVector gain = this.mCameramanager.getWBGainValue(0);
        if (this.mCameramanager.getCameraHWLevel() == 0) {
            result = enableMFMode(true) && enableMEMode(true);
        } else {
            result = enableMFMode(true) && enableMEMode(true) && enableMWBMode(true);
        }
        if (!result) {
            return result;
        }
        this.mCameramanager.setExposureTime(ext);
        this.mCameramanager.setISOValue(Integer.valueOf(ios));
        if (fodis > 0.0f) {
            this.mCameramanager.setFoucsDistance(fodis);
        }
        if (this.mCameramanager.getCameraHWLevel() == 0) {
            return result;
        }
        this.mCameramanager.setWBGainValueEx(gain);
        return result;
    }

    public Camera2Manager getInternal() {
        return this.mCameramanager;
    }

    public int getCameramode() {
        return this.mCameramanager.getCameramode();
    }

    public boolean changeCamreMode(int newCameraMode) {
        return this.mCameramanager.changeCamreMode(newCameraMode);
    }

    public boolean switchCameraManagerMode() {
        if (this.mCamereManagerMode == 1) {
            return changeCameraManagerMode(0);
        }
        return changeCameraManagerMode(1);
    }

    public int getCurrentCameraMode() {
        return this.mCamereManagerMode;
    }

    public boolean changeCameraManagerMode(int newCameraManagermode) {
        return false;
    }

    public boolean enablePreviewListener() {
        return this.mCameramanager.enablePreviewListener();
    }

    public boolean disablePreviewListener(boolean onlyeffecttag) {
        return this.mCameramanager.disablePreviewListener(onlyeffecttag);
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
        Size previewSize = this.mCameramanager.getPreviewFrameSize();
        this.trackManager = new FVTrackManagerEx(this, tracklistener, previewSize.getWidth(), previewSize.getHeight());
        if (!this.trackManager.initTrack()) {
            this.trackManager = null;
            return false;
        }
        this.isvideostabopened = false;
        if (isVideoStabilizationOpened()) {
            this.isvideostabopened = true;
            closeVideoStabilizationMode();
        }
        enablePreviewListener();
        if (rect != null && pnt == null) {
            this.trackManager.startTracking(FVTrackManagerEx.GraphicRect2OpenCVRect(rect));
        } else if (rect != null || pnt == null) {
            return false;
        } else {
            this.trackManager.startTracking(FVTrackManagerEx.GraphicPoint2OpenCVPoint(pnt));
        }
        return true;
    }

    public void stopTrack() {
        if (this.trackManager != null && this.trackManager.isIstrack()) {
            this.trackManager.stopTrack();
            if (!isMediaRecording()) {
                disablePreviewListener(false);
            } else {
                disablePreviewListener(true);
            }
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                public void run() {
                    FVTrackManagerEx unused = FVCamera2Manager.this.trackManager = null;
                    if (FVCamera2Manager.this.isvideostabopened) {
                        FVCamera2Manager.this.openVideoStabilizationMode();
                    }
                }
            }, 500);
        }
    }

    public FVTrackManagerEx getTrackManager() {
        return this.trackManager;
    }

    public boolean setFilter(FilterType filterType) {
        return false;
    }

    public GPUImageFilter getFilter() {
        return null;
    }

    public FilterType getFilterType() {
        return FilterType.NOFILTER;
    }

    public boolean changeFilterIntensity(int intensity) {
        return false;
    }

    public void startSlowShutter() {
    }

    public boolean stopSlowShutter() {
        return false;
    }

    public String showModestatus() {
        return "Control mode =" + this.mCameramanager.getControMode() + "  ,AF-Re =" + this.mCameramanager.getFocusMode(1) + "  ,AF-Rs =" + this.mCameramanager.getFocusMode(0) + "  ,AE=" + this.mCameramanager.getAEMode() + "  ,AWB=" + this.mCameramanager.getWhiteBalanceMode();
    }
}
