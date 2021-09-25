package com.freevisiontech.cameralib.impl.Camera1;

import android.app.Activity;
import android.graphics.Rect;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.support.p001v4.util.SparseArrayCompat;
import android.util.Log;
import com.freevisiontech.cameralib.AspectRatio;
import com.freevisiontech.cameralib.Constants;
import com.freevisiontech.cameralib.FVCamera1Manager;
import com.freevisiontech.cameralib.Size;
import com.freevisiontech.cameralib.SizeMap;
import com.freevisiontech.cameralib.utils.CameraUtils;
import com.freevisiontech.cameralib.view.camera1.CameraView;
import com.freevisiontech.fvmobile.model.resolver.CompanyIdentifierResolver;
import com.freevisiontech.tracking.FVTrackManager;
import com.umeng.analytics.C0015a;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.atomic.AtomicBoolean;

public class CameraManager implements Camera.PreviewCallback {
    private static final SparseArrayCompat<String> FLASH_MODES = new SparseArrayCompat<>();
    private static final int INVALID_CAMERA_ID = -1;
    private final int BASE = 1;
    private final String TAG = "CameraManager";
    private Activity context;
    private FVCamera1Manager fvCameraManager = null;
    private final AtomicBoolean isPictureCaptureInProgress = new AtomicBoolean(false);
    long laststart = 0;
    private AspectRatio mAspectRatio = Camera1Constants.DEFAULT_ASPECT_RATIO;
    private boolean mAutoFocus = true;
    private Size mBackPreviewSize;
    private Camera mCamera = null;
    private int mCameraId = -1;
    private int mCameraMode = 0;
    private int mFacing = 0;
    private int mFlash = 3;
    private Size mFrontPreviewSize;
    private boolean mIsRecording = false;
    private MediaRecorder mMediaRecorder;
    private SizeMap mPictureSizes = new SizeMap();
    private CameraView mPreview = null;
    private SizeMap mPreviewSizes = new SizeMap();
    /* access modifiers changed from: private */
    public boolean mShowingPreview = false;
    private SizeMap mVideoSizes = new SizeMap();

    public interface RecordCallBack {
        void onRecordEnd();

        void onRecordError(String str);

        void onRecordStarted();
    }

    public interface TakePictureCallback {
        void onPictureTaken(byte[] bArr, Camera camera);
    }

    static {
        FLASH_MODES.put(0, Constants.FOCUS_MODE_OFF);
        FLASH_MODES.put(1, "on");
        FLASH_MODES.put(2, "torch");
        FLASH_MODES.put(3, com.freevisiontech.fvmobile.utility.Constants.SCENE_MODE_AUTO);
        FLASH_MODES.put(4, "red-eye");
    }

    public CameraManager(Activity ctx, FVCamera1Manager parent) {
        this.context = ctx;
        this.fvCameraManager = parent;
        if (Build.VERSION.SDK_INT < 23) {
            this.mBackPreviewSize = Camera1Constants.DefaultBackPreviewSize4Low;
            this.mFrontPreviewSize = Camera1Constants.DefaultFrontPreviewSize4Low;
            return;
        }
        this.mBackPreviewSize = Camera1Constants.DefaultBackPreviewSize4High;
        this.mFrontPreviewSize = Camera1Constants.DefaultFrontPreviewSize4High;
    }

    public void onPreviewFrame(byte[] data, Camera camera) {
        this.laststart = System.currentTimeMillis();
        FVTrackManager tracker = this.fvCameraManager.getTrackManager();
        if (tracker != null && tracker.isIstrack()) {
            tracker.update(data);
        }
    }

    public Camera.Parameters cameraParameters() {
        if (!isCameraOpened()) {
            return null;
        }
        return this.mCamera.getParameters();
    }

    public Camera.CameraInfo cameraInfo() {
        Camera.CameraInfo cm = new Camera.CameraInfo();
        Camera.getCameraInfo(this.mCameraId, cm);
        return cm;
    }

    public int getCameramode() {
        return this.mCameraMode;
    }

    public void changeCamreMode(int newCameraMode) {
        if (newCameraMode != this.mCameraMode) {
            this.mCameraMode = newCameraMode;
        }
    }

    public void setPerviewView(CameraView view) {
        this.mPreview = view;
    }

    public boolean startPreview() {
        if (!isCameraOpened()) {
            return false;
        }
        this.mCamera.startPreview();
        this.mShowingPreview = true;
        return true;
    }

    public boolean isShowingPreview() {
        return this.mShowingPreview;
    }

    public boolean stopPreview() {
        if (!isCameraOpened()) {
            return false;
        }
        if (!this.mShowingPreview) {
            return true;
        }
        this.mCamera.stopPreview();
        this.mShowingPreview = false;
        return true;
    }

    public boolean startCamera(boolean isinit, int facing, Size res) {
        if (!openCamera(isinit, facing, res)) {
            return false;
        }
        if (this.mFacing == 0) {
            this.mBackPreviewSize = res;
        } else if (this.mFacing == 1) {
            this.mFrontPreviewSize = res;
        }
        setFPS(30);
        startPreview();
        return true;
    }

    public void stopCamera() {
        stopPreview();
        releaseCamera();
    }

    public boolean openCamera(boolean isinit, int facing, Size res) {
        if (this.mCamera != null) {
            stopCamera();
        }
        int id = getCameraByFacing(facing);
        if (id == -1) {
            return false;
        }
        this.mCameraId = id;
        this.mFacing = facing;
        try {
            this.mCamera = Camera.open(id);
            Camera.Parameters parameters = cameraParameters();
            List<Camera.Size> previewsizes = parameters.getSupportedPreviewSizes();
            this.mPreviewSizes.clear();
            if (previewsizes != null) {
                for (Camera.Size size : previewsizes) {
                    this.mPreviewSizes.add(new Size(size.width, size.height));
                }
            }
            CameraUtils.ShowSizesStrings(previewsizes, "CameraManager", "Preview sizes:");
            this.mPictureSizes.clear();
            List<Camera.Size> picsizes = parameters.getSupportedPictureSizes();
            if (picsizes != null) {
                for (Camera.Size size2 : picsizes) {
                    this.mPictureSizes.add(new Size(size2.width, size2.height));
                }
            }
            CameraUtils.ShowSizesStrings(picsizes, "CameraManager", "Picture sizes:");
            List<Camera.Size> videosizes = parameters.getSupportedVideoSizes();
            this.mVideoSizes.clear();
            if (videosizes != null) {
                for (Camera.Size size3 : videosizes) {
                    this.mVideoSizes.add(new Size(size3.width, size3.height));
                }
            }
            CameraUtils.ShowSizesStrings(previewsizes, "CameraManager", "Video sizes:");
            if (isinit) {
                if (res != null && this.mPreviewSizes.contains(res)) {
                    parameters.setPreviewSize(res.getWidth(), res.getHeight());
                    this.mAspectRatio = this.mPreviewSizes.ratioBySize(res);
                } else if (res == null || !this.mPreviewSizes.isEmpty()) {
                    this.mAspectRatio = Camera1Constants.DEFAULT_ASPECT_RATIO;
                    Size res2 = setDefaultPreviewSizeFromAspectRatio(parameters);
                } else {
                    parameters.setPreviewSize(res.getWidth(), res.getHeight());
                    this.mAspectRatio = AspectRatio.m1507of(res.getWidth(), res.getHeight());
                }
                setAutoFocusInternal(this.mAutoFocus, parameters);
                setFlashInternal(this.mFlash, parameters);
                parameters.setPictureFormat(256);
                if (Build.VERSION.SDK_INT >= 14 && !Build.MODEL.equals("GT-I9100")) {
                    parameters.setRecordingHint(true);
                }
            }
            setParameter2Camera(parameters);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            this.mCamera = null;
            return false;
        }
    }

    public void releaseCamera() {
        if (this.mCamera != null) {
            this.mCamera.setPreviewCallback((Camera.PreviewCallback) null);
            this.mCamera.release();
            this.mCamera = null;
            this.mPreviewSizes.clear();
            this.mPictureSizes.clear();
            this.mVideoSizes.clear();
        }
    }

    public boolean isCameraOpened() {
        return this.mCamera != null;
    }

    public boolean setFacing(int facing, Size ressize) {
        if (this.mFacing == facing || !isCameraOpened()) {
            return false;
        }
        stopCamera();
        return startCamera(true, facing, ressize);
    }

    public int getFacing() {
        return this.mFacing;
    }

    public Set<AspectRatio> getSupportedAspectRatios(int mode) {
        if (mode == 0) {
            return getPicutrePreviewSupportedAspectRatios();
        }
        if (mode == 1) {
            return getVideoPreviewSupportedAspectRatios();
        }
        return this.mPreviewSizes.ratios();
    }

    public boolean setAspectRatio(AspectRatio ratio) {
        if (isCameraOpened() && !this.mAspectRatio.equals(ratio)) {
            if (getSupportedPreviewResolutions(2, ratio) == null) {
                Log.e("CameraManager", " Ratio is not supported");
            } else {
                if (this.mShowingPreview) {
                    this.mCamera.stopPreview();
                }
                Camera.Parameters parameters = cameraParameters();
                if (adjustCameraSizeByAspectRatio(parameters, ratio)) {
                    if (setParameter2Camera(parameters)) {
                        this.mAspectRatio = ratio;
                    }
                }
                if (this.mShowingPreview) {
                    this.mCamera.startPreview();
                    this.mShowingPreview = true;
                }
            }
        }
        return false;
    }

    public Set<AspectRatio> getPicutrePreviewSupportedAspectRatios() {
        SizeMap idealAspectRatios = this.mPreviewSizes.clone();
        for (AspectRatio aspectRatio : idealAspectRatios.ratios()) {
            if (this.mPictureSizes.sizes(aspectRatio) == null) {
                idealAspectRatios.remove(aspectRatio);
            }
        }
        return idealAspectRatios.ratios();
    }

    public Set<AspectRatio> getVideoPreviewSupportedAspectRatios() {
        SizeMap idealAspectRatios = this.mPreviewSizes.clone();
        for (AspectRatio aspectRatio : idealAspectRatios.ratios()) {
            if (this.mVideoSizes.sizes(aspectRatio) == null) {
                idealAspectRatios.remove(aspectRatio);
            }
        }
        return idealAspectRatios.ratios();
    }

    public AspectRatio getAspectRatio() {
        return this.mAspectRatio;
    }

    public AspectRatio defaultAspectRatio() {
        AspectRatio r = null;
        for (AspectRatio ratio : this.mPreviewSizes.ratios()) {
            r = ratio;
            if (ratio.equals(Camera1Constants.DEFAULT_ASPECT_RATIO)) {
                return ratio;
            }
        }
        if (r == null) {
            r = Camera1Constants.DEFAULT_ASPECT_RATIO;
        }
        return r;
    }

    public SizeMap getSupportedPreviewResolutions(int mode) {
        if (mode == 0) {
            return getPicturePreviewSupportedResolutions();
        }
        if (mode == 1) {
            return getVideoPreviewSupportedResolutions();
        }
        return this.mPreviewSizes;
    }

    public SortedSet<Size> getSupportedPreviewResolutions(int mode, AspectRatio ratio) {
        return getSupportedPreviewResolutions(mode).sizes(ratio);
    }

    public SizeMap getPicturePreviewSupportedResolutions() {
        SizeMap idealAspectRatios = this.mPreviewSizes.clone();
        for (AspectRatio aspectRatio : idealAspectRatios.ratios()) {
            if (this.mPictureSizes.sizes(aspectRatio) == null) {
                idealAspectRatios.remove(aspectRatio);
            }
        }
        return idealAspectRatios;
    }

    public SizeMap getVideoPreviewSupportedResolutions() {
        SizeMap idealAspectRatios = this.mPreviewSizes.clone();
        for (AspectRatio aspectRatio : idealAspectRatios.ratios()) {
            if (this.mVideoSizes.sizes(aspectRatio) == null) {
                idealAspectRatios.remove(aspectRatio);
            }
        }
        return idealAspectRatios;
    }

    public boolean setPreviewResolution(Size res) {
        if (!isCameraOpened()) {
            return false;
        }
        stopPreview();
        Camera.Parameters parameters = cameraParameters();
        parameters.setPreviewSize(res.getWidth(), res.getHeight());
        boolean parameter2Camera = setParameter2Camera(parameters);
        startPreview();
        return parameter2Camera;
    }

    public boolean setPreviewResolutionEx(Size res) {
        if (!isCameraOpened()) {
            return false;
        }
        stopCamera();
        return startCamera(true, this.mFacing, res);
    }

    public Size getPreviewResolution() {
        return getPreviewResolution(this.mFacing);
    }

    public Size getPreviewResolution(int facing) {
        if (isCameraOpened()) {
            Camera.Size s = cameraParameters().getPreviewSize();
            Size ss = new Size(s.width, s.height);
            if (this.mFacing == 0) {
                this.mBackPreviewSize = ss;
            } else if (this.mFacing == 1) {
                this.mFrontPreviewSize = ss;
            }
        }
        if (facing == 0) {
            return this.mBackPreviewSize;
        }
        if (facing == 1) {
            return this.mFrontPreviewSize;
        }
        return null;
    }

    public boolean isContainPreviewResolution(Size res) {
        if (this.mPreviewSizes.contains(res)) {
            return true;
        }
        return false;
    }

    public void setAutoFocusMode(boolean autoFocus) {
        if (isCameraOpened()) {
            Camera.Parameters parameters = cameraParameters();
            if (setAutoFocusInternal(autoFocus, parameters) && setParameter2Camera(parameters)) {
                this.mAutoFocus = autoFocus;
            }
        }
    }

    public void autoFocus(Rect focusarea, Camera.AutoFocusCallback callback) {
        if (isCameraOpened()) {
            if (!isSupportFocusArea()) {
                CameraUtils.LogV("AutoFocus", "not support aera");
                this.mCamera.autoFocus(callback);
                return;
            }
            CameraUtils.LogV("AutoFocus", " support aera");
            try {
                Camera.Parameters parameters = cameraParameters();
                this.mCamera.cancelAutoFocus();
                parameters.setFocusMode(com.freevisiontech.fvmobile.utility.Constants.SCENE_MODE_AUTO);
                if (focusarea != null && parameters.getMaxNumFocusAreas() > 0) {
                    Camera.Area focusArea = new Camera.Area(focusarea, 1000);
                    List<Camera.Area> focusAreas = new ArrayList<>();
                    focusAreas.add(focusArea);
                    parameters.setFocusAreas(focusAreas);
                }
                setParameter2Camera(parameters);
                this.mCamera.autoFocus(callback);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isSupportFocusArea() {
        if (isCameraOpened() && cameraParameters().getMaxNumFocusAreas() != 0) {
            return true;
        }
        return false;
    }

    public List<Camera.Area> getFocusArea() {
        Camera.Parameters parameters = cameraParameters();
        if (parameters == null || parameters.getMaxNumFocusAreas() == 0) {
            return null;
        }
        return parameters.getFocusAreas();
    }

    public void cancelAutoFocus() {
        if (isCameraOpened()) {
            try {
                Camera.Parameters parameters = cameraParameters();
                this.mCamera.cancelAutoFocus();
                parameters.setFocusMode("continuous-picture");
                setParameter2Camera(parameters);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isAutoFocus() {
        if (isCameraOpened()) {
            return this.mAutoFocus;
        }
        return false;
    }

    public boolean isAutoFocusN() {
        if (!isCameraOpened()) {
            return false;
        }
        try {
            String focusMode = cameraParameters().getFocusMode();
            if (focusMode == null || !focusMode.contains("continuous")) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getFocusMode() {
        if (!isCameraOpened()) {
            return null;
        }
        return cameraParameters().getFocusMode();
    }

    public boolean setFocusMode(String mode) {
        if (!isCameraOpened()) {
            return false;
        }
        Camera.Parameters parameters = cameraParameters();
        parameters.setFocusMode(mode);
        return setParameter2Camera(parameters);
    }

    public float getFocalLength() {
        if (!isCameraOpened()) {
            return 0.0f;
        }
        return cameraParameters().getFocalLength();
    }

    public float[] getFocusDistance() {
        if (!isCameraOpened()) {
            return null;
        }
        float[] distances = new float[3];
        cameraParameters().getFocusDistances(distances);
        return distances;
    }

    public int getCurFocusScale() {
        if (!isCameraOpened()) {
            return 0;
        }
        return cameraParameters().getInt("cur-focus-scale");
    }

    public String[] getSupportedManualFocusModes() {
        if (!isCameraOpened()) {
            return new String[0];
        }
        String manualfocusmode = cameraParameters().get("manual-focus-modes");
        if (manualfocusmode == null || manualfocusmode.trim().isEmpty()) {
            return null;
        }
        return manualfocusmode.split(",");
    }

    public List<String> getSupportedFocusMode() {
        if (!isCameraOpened()) {
            return new ArrayList();
        }
        return cameraParameters().getSupportedFocusModes();
    }

    public void setFlashMode(int flash) {
        if (flash != this.mFlash) {
            Camera.Parameters parameters = cameraParameters();
            if (setFlashInternal(flash, parameters)) {
                setParameter2Camera(parameters);
            }
        }
    }

    public int getFlashMode() {
        return this.mFlash;
    }

    public String getFlashModeStr() {
        if (!isCameraOpened()) {
            return "";
        }
        return cameraParameters().getFlashMode();
    }

    public boolean isZoomSupported() {
        if (isCameraOpened() && cameraParameters().isZoomSupported()) {
            return true;
        }
        return false;
    }

    public boolean setZoom(int degree) {
        if (!isZoomSupported() || degree < 0 || degree > getMaxZoom()) {
            return false;
        }
        Camera.Parameters params = cameraParameters();
        params.setZoom(degree);
        return setParameter2Camera(params);
    }

    public int getMaxZoom() {
        if (!isCameraOpened()) {
            return 0;
        }
        return cameraParameters().getMaxZoom();
    }

    public boolean isSmoothZoomSupported() {
        if (isCameraOpened() && cameraParameters().isSmoothZoomSupported()) {
            return true;
        }
        return false;
    }

    public boolean startSmoothZoom(int degree) {
        if (!isSmoothZoomSupported() || degree < 0 || degree > getMaxZoom()) {
            return false;
        }
        this.mCamera.startSmoothZoom(degree);
        return true;
    }

    public void stopSmoothZoom() {
        if (isCameraOpened()) {
            this.mCamera.stopSmoothZoom();
        }
    }

    public String[] getSupportedISOMode() {
        String supportedValues;
        if (!isCameraOpened()) {
            return null;
        }
        Camera.Parameters parameters = cameraParameters();
        String flat = parameters.flatten();
        String values_keyword = null;
        if (flat.contains("iso-values")) {
            values_keyword = "iso-values";
        } else if (flat.contains("iso-mode-values")) {
            values_keyword = "iso-mode-values";
        } else if (flat.contains("iso-speed-values")) {
            values_keyword = "iso-speed-values";
        } else if (flat.contains("nv-picture-iso-values")) {
            values_keyword = "nv-picture-iso-values";
        }
        if (values_keyword == null || (supportedValues = parameters.get(values_keyword)) == null) {
            return null;
        }
        return supportedValues.split(",");
    }

    public String getISOMode() {
        if (!isCameraOpened()) {
            return "";
        }
        Camera.Parameters parameters = cameraParameters();
        String flat = parameters.flatten();
        String iso_keyword = null;
        if (flat.contains("iso-values")) {
            iso_keyword = "iso";
        } else if (flat.contains("iso-mode-values")) {
            iso_keyword = "iso";
        } else if (flat.contains("iso-speed-values")) {
            iso_keyword = "iso-speed";
        } else if (flat.contains("nv-picture-iso-values")) {
            iso_keyword = "nv-picture-iso";
        }
        if (iso_keyword == null) {
            return null;
        }
        return parameters.get(iso_keyword);
    }

    public boolean setISO(String mode) {
        if (!isCameraOpened()) {
            return false;
        }
        Camera.Parameters parameters = cameraParameters();
        String flat = parameters.flatten();
        String iso_keyword = null;
        if (flat.contains("iso-values")) {
            iso_keyword = "iso";
        } else if (flat.contains("iso-mode-values")) {
            iso_keyword = "iso";
        } else if (flat.contains("iso-speed-values")) {
            iso_keyword = "iso-speed";
        } else if (flat.contains("nv-picture-iso-values")) {
            iso_keyword = "nv-picture-iso";
        }
        if (iso_keyword == null) {
            return false;
        }
        parameters.set(iso_keyword, mode);
        return setParameter2Camera(parameters);
    }

    public String getWhiteBalance() {
        if (!isCameraOpened()) {
            return "";
        }
        return cameraParameters().getWhiteBalance();
    }

    public boolean setWhiteBalance(String mode) {
        if (!isCameraOpened()) {
            return false;
        }
        Camera.Parameters parameters = cameraParameters();
        parameters.setWhiteBalance(mode);
        return setParameter2Camera(parameters);
    }

    public boolean lockAutoWhiteBalance(boolean enable) {
        if (!isCameraOpened()) {
            return false;
        }
        Camera.Parameters parameters = cameraParameters();
        if (!parameters.isAutoWhiteBalanceLockSupported()) {
            return false;
        }
        parameters.setAutoWhiteBalanceLock(enable);
        return setParameter2Camera(parameters);
    }

    public boolean isAutoWhiteBalanceLocked() {
        if (!isCameraOpened()) {
            return false;
        }
        Camera.Parameters parameters = cameraParameters();
        if (parameters.isAutoWhiteBalanceLockSupported()) {
            return parameters.getAutoWhiteBalanceLock();
        }
        return false;
    }

    public int getExposureCompensation() {
        if (!isCameraOpened()) {
            return 0;
        }
        return cameraParameters().getExposureCompensation();
    }

    public boolean setExposureCompensation(int value) {
        if (!isCameraOpened()) {
            return false;
        }
        Camera.Parameters parameters = cameraParameters();
        parameters.setExposureCompensation(value);
        return setParameter2Camera(parameters);
    }

    public int[] getExposureCompensationRanger() {
        int[] ranger = new int[2];
        Camera.Parameters parameters = cameraParameters();
        if (parameters != null) {
            ranger[0] = parameters.getMinExposureCompensation();
            ranger[1] = parameters.getMaxExposureCompensation();
        } else {
            ranger[0] = 0;
            ranger[1] = 0;
        }
        return ranger;
    }

    public float getExposureValue() {
        if (!isCameraOpened()) {
            return 0.0f;
        }
        return ((float) getExposureCompensation()) * cameraParameters().getExposureCompensationStep();
    }

    public boolean setExposureValue(float value) {
        if (!isCameraOpened()) {
            return false;
        }
        Camera.Parameters parameters = cameraParameters();
        int vi = (int) (value / parameters.getExposureCompensationStep());
        int[] ranges = getExposureCompensationRanger();
        if (vi < ranges[0] || vi > ranges[1]) {
            return false;
        }
        parameters.setExposureCompensation(vi);
        return setParameter2Camera(parameters);
    }

    public boolean lockAutoExposure(boolean toogle) {
        if (!isCameraOpened()) {
            return false;
        }
        Camera.Parameters parameters = cameraParameters();
        if (!parameters.isAutoExposureLockSupported()) {
            return false;
        }
        parameters.setAutoExposureLock(toogle);
        return setParameter2Camera(parameters);
    }

    public boolean isAutoExposureLock() {
        if (!isCameraOpened()) {
            return false;
        }
        return cameraParameters().getAutoExposureLock();
    }

    public float getExposureCompensationStep() {
        if (!isCameraOpened()) {
            return 0.0f;
        }
        return cameraParameters().getExposureCompensationStep();
    }

    public boolean setExposureTime(float value) {
        if (!isCameraOpened()) {
            return false;
        }
        Camera.Parameters parameters = cameraParameters();
        parameters.set("hw-sensor-iso-range", String.valueOf(value));
        return setParameter2Camera(parameters);
    }

    public boolean setHwExposure(String val) {
        if (!isCameraOpened()) {
            return false;
        }
        Camera.Parameters parameters = cameraParameters();
        parameters.set("key-supernight-supported-exposure", val);
        return setParameter2Camera(parameters);
    }

    public boolean setSceneMode(String mode) {
        if (!isCameraOpened() || !isSupportScene(mode)) {
            return false;
        }
        Camera.Parameters parameters = cameraParameters();
        parameters.setSceneMode(mode);
        if (!setParameter2Camera(parameters)) {
            return false;
        }
        if (isAutoFocusN()) {
            this.mAutoFocus = true;
        } else {
            this.mAutoFocus = false;
        }
        String flashmod = getFlashModeStr();
        if (flashmod != null) {
            if (flashmod.equals(Constants.FOCUS_MODE_OFF)) {
                this.mFlash = 0;
            } else if (flashmod.equals("on")) {
                this.mFlash = 1;
            } else if (flashmod.equals("torch")) {
                this.mFlash = 2;
            } else if (flashmod.equals(com.freevisiontech.fvmobile.utility.Constants.SCENE_MODE_AUTO)) {
                this.mFlash = 3;
            } else if (flashmod.equals("red-eye")) {
                this.mFlash = 4;
            }
        }
        return true;
    }

    public String getSceneMode() {
        if (!isCameraOpened()) {
            return "";
        }
        return cameraParameters().getSceneMode();
    }

    public List<String> getSupportedSceneModes() {
        if (!isCameraOpened()) {
            return new ArrayList();
        }
        return cameraParameters().getSupportedSceneModes();
    }

    public boolean isSupportScene(String scene) {
        if (!isCameraOpened()) {
            return false;
        }
        try {
            return getSupportedSceneModes().contains(scene);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean setFPS(int fps) {
        if (!isCameraOpened()) {
            return false;
        }
        Camera.Parameters parameters = cameraParameters();
        int rfps = fps * 1000;
        parameters.setPreviewFpsRange(rfps, rfps);
        return setParameter2Camera(parameters);
    }

    public int getFPS() {
        if (!isCameraOpened()) {
            return 0;
        }
        return cameraParameters().getPreviewFrameRate();
    }

    public List<Integer> getSupportedPreviewFPS() {
        if (!isCameraOpened()) {
            return new ArrayList();
        }
        return cameraParameters().getSupportedPreviewFrameRates();
    }

    public List<int[]> getSupportPreviewFPSRange() {
        if (!isCameraOpened()) {
            return new ArrayList();
        }
        return cameraParameters().getSupportedPreviewFpsRange();
    }

    public boolean isSupportPreviewFPS(int fps) {
        if (!isCameraOpened()) {
            return false;
        }
        List<Integer> list = getSupportedPreviewFPS();
        List<int[]> supportPreviewFPSRange = getSupportPreviewFPSRange();
        if (list == null) {
            return false;
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).intValue() == fps) {
                return true;
            }
        }
        return false;
    }

    public boolean startFaceDetection(Camera.FaceDetectionListener listener) {
        if (!isCameraOpened()) {
            return false;
        }
        Camera.Parameters parameters = cameraParameters();
        if (listener != null) {
            this.mCamera.setFaceDetectionListener(listener);
        }
        if (parameters.getMaxNumDetectedFaces() == 0) {
            return false;
        }
        this.mCamera.stopPreview();
        this.mCamera.startPreview();
        this.mShowingPreview = true;
        this.mCamera.startFaceDetection();
        return true;
    }

    public String getColorEffect() {
        if (!isCameraOpened()) {
            return "";
        }
        return cameraParameters().getColorEffect();
    }

    public List<String> getSupportedColorEffects() {
        if (!isCameraOpened()) {
            return new ArrayList();
        }
        return cameraParameters().getSupportedColorEffects();
    }

    public boolean setColorEffect(String value) {
        if (!isCameraOpened()) {
            return false;
        }
        Camera.Parameters parameters = cameraParameters();
        parameters.setColorEffect(value);
        return setParameter2Camera(parameters);
    }

    public boolean setHdrScene(boolean ison) {
        if (!isCameraOpened()) {
            return false;
        }
        if (ison) {
            return setSceneMode("hdr");
        }
        return setSceneMode(com.freevisiontech.fvmobile.utility.Constants.SCENE_MODE_AUTO);
    }

    public boolean isSupportHdrMode() {
        return isSupportScene("hdr");
    }

    public boolean setPictureResolution(Size res) {
        if (!isCameraOpened() || !this.mPictureSizes.contains(res)) {
            return false;
        }
        Camera.Parameters parameters = cameraParameters();
        parameters.setPictureSize(res.getWidth(), res.getHeight());
        return setParameter2Camera(parameters);
    }

    public Size getPictureResolution() {
        if (!isCameraOpened()) {
            return null;
        }
        Camera.Size size = cameraParameters().getPictureSize();
        return new Size(size.width, size.height);
    }

    public SizeMap getPictureSupportedResolutions() {
        return this.mPictureSizes;
    }

    public SortedSet<Size> getPictureSupportedResolutions(AspectRatio ratio) {
        return getPictureSupportedResolutions().sizes(ratio);
    }

    public Size getMinSupportedPictureSize4PreviewAspectRatio() {
        Size minsize = new Size(0, 0);
        for (Size _size : getPictureSupportedResolutions(this.mAspectRatio)) {
            if (minsize.getWidth() == 0) {
                minsize = _size;
            } else if (_size.getWidth() * _size.getHeight() < minsize.getWidth() * minsize.getHeight()) {
                minsize = _size;
            }
        }
        return minsize;
    }

    public Size getMaxSupportedPictureSize() {
        SizeMap sm = getPictureSupportedResolutions();
        Size maxsize = new Size(0, 0);
        for (AspectRatio aspectRatio : sm.ratios()) {
            if (((double) (((float) aspectRatio.getX()) / ((float) aspectRatio.getY()))) >= 1.3333333333333333d) {
                for (Size _size : getPictureSupportedResolutions(aspectRatio)) {
                    if (_size.getWidth() * _size.getHeight() > maxsize.getWidth() * maxsize.getHeight()) {
                        maxsize = _size;
                    }
                }
            }
        }
        return maxsize;
    }

    public Size getMaxPictureSize4AspectRatio(AspectRatio ratio) {
        int x0 = 0;
        int y0 = 0;
        for (Size s : getPictureSupportedResolutions(ratio)) {
            int x = s.getWidth();
            int y = s.getHeight();
            if (x * y > x0 * y0) {
                x0 = x;
                y0 = y;
            }
        }
        return new Size(x0, y0);
    }

    public int getPictureFormat() {
        if (!isCameraOpened()) {
            return 0;
        }
        return cameraParameters().getPictureFormat();
    }

    public boolean setPictureFormat(int pixel_format) {
        if (!isCameraOpened()) {
            return false;
        }
        Camera.Parameters parameters = cameraParameters();
        parameters.setPictureFormat(pixel_format);
        return setParameter2Camera(parameters);
    }

    public Camera getCamera() {
        return this.mCamera;
    }

    public int getCameraRotation(int viewstyle) {
        int degrees = 0;
        switch (this.context.getWindowManager().getDefaultDisplay().getRotation()) {
            case 0:
                degrees = 0;
                break;
            case 1:
                degrees = 90;
                break;
            case 2:
                degrees = 180;
                break;
            case 3:
                degrees = 270;
                break;
        }
        if (viewstyle == CameraView.ViewStyle_GPUImageView) {
            return calcCameraRotationByGPUImageView(degrees);
        }
        if (viewstyle == CameraView.ViewStyle_SystemView) {
            return calcCameraRotationBySystemSurfaceView(degrees);
        }
        return 0;
    }

    public int calcCameraRotationBySystemSurfaceView(int rotation) {
        if (cameraInfo().facing == 1) {
            return (360 - ((cameraInfo().orientation + rotation) % C0015a.f29p)) % C0015a.f29p;
        }
        return ((cameraInfo().orientation - rotation) + C0015a.f29p) % C0015a.f29p;
    }

    public int calcCameraRotationByGPUImageView(int rotation) {
        Camera.CameraInfo info = cameraInfo();
        if (info.facing == 1) {
            return (info.orientation + rotation) % C0015a.f29p;
        }
        return ((info.orientation - rotation) + C0015a.f29p) % C0015a.f29p;
    }

    public boolean takePicture(TakePictureCallback callback, int rotation) {
        if (!isCameraOpened()) {
            return false;
        }
        if (rotation == 90 || rotation == 270) {
            if (!setCameraRotation(rotation)) {
                CameraUtils.LogV("Huangs", "rotate " + rotation + " failed");
            } else {
                CameraUtils.LogV("Huangs", "rotate " + rotation + " good");
            }
        }
        takePictureInternal(callback);
        return true;
    }

    public boolean setCameraRotation(int angle) {
        if (!isCameraOpened() || Constants.Camera_Rotation_Exclude.contains(Build.BRAND.toLowerCase()) || Constants.Camera_Rotation_Exclude.contains(Build.MANUFACTURER.toLowerCase())) {
            return false;
        }
        Camera.Parameters parameters = cameraParameters();
        parameters.setRotation(angle);
        parameters.set("rotation", angle);
        return setParameter2Camera(parameters);
    }

    public void takeNullPicture(Size size) {
    }

    private Size mediaSizeFromMediaRecordQuality(int quality) {
        switch (quality) {
            case 2:
            case 1002:
                return new Size(CompanyIdentifierResolver.PASSIF_SEMICONDUCTOR_CORP, 144);
            case 3:
            case 1003:
                return new Size(352, CompanyIdentifierResolver.PORSCHE_AG);
            case 4:
            case 1004:
                return new Size(720, 480);
            case 5:
            case 1005:
                return new Size(1280, 720);
            case 6:
            case 1006:
                return new Size(1920, 1080);
            case 7:
            case 1007:
                return new Size(320, 240);
            case 8:
            case 1008:
                return new Size(3840, 2160);
            default:
                return null;
        }
    }

    public boolean isMediaRecordQualitySupports(int quality) {
        if (!isCameraOpened()) {
            return false;
        }
        try {
            CamcorderProfile camcorderProfile = CamcorderProfile.get(getFacing(), quality);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean startMediaRecordEx(String videopath, int quality, int orientationhint, double capturerate, final RecordCallBack callBack) {
        if (isMediaRecording()) {
            return false;
        }
        try {
            this.mCamera.unlock();
            this.mMediaRecorder = new MediaRecorder();
            this.mMediaRecorder.reset();
            this.mMediaRecorder.setCamera(this.mCamera);
            this.mMediaRecorder.setVideoSource(1);
            if (capturerate > 0.0d) {
                this.mMediaRecorder.setCaptureRate(capturerate);
            } else {
                this.mMediaRecorder.setAudioSource(0);
            }
            this.mMediaRecorder.setProfile(CamcorderProfile.get(getFacing(), quality));
            this.mMediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
                public void onError(MediaRecorder mr, int what, int extra) {
                    CameraManager.this.stopMediaRecord((RecordCallBack) null);
                    if (callBack != null) {
                        callBack.onRecordError("" + what + ":" + extra);
                    }
                }
            });
            this.mMediaRecorder.setOrientationHint(orientationhint);
            this.mMediaRecorder.setOutputFile(videopath);
            this.mMediaRecorder.setMaxFileSize(0);
            this.mMediaRecorder.setMaxDuration(0);
            this.mMediaRecorder.setPreviewDisplay(this.mPreview.getSurface());
            this.mMediaRecorder.prepare();
            this.mMediaRecorder.start();
            this.mIsRecording = true;
            if (callBack != null) {
                callBack.onRecordStarted();
            }
            return true;
        } catch (Exception e) {
            this.mIsRecording = false;
            if (this.mCamera != null) {
                try {
                    this.mCamera.lock();
                    this.mCamera.reconnect();
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
            e.printStackTrace();
            if (callBack == null) {
                return false;
            }
            callBack.onRecordError(e.toString());
            return false;
        }
    }

    public boolean startMediaRecordEx(FileDescriptor fd, int quality, int orientationhint, double capturerate, final RecordCallBack callBack) {
        if (isMediaRecording()) {
            return false;
        }
        try {
            this.mCamera.unlock();
            this.mMediaRecorder = new MediaRecorder();
            this.mMediaRecorder.reset();
            this.mMediaRecorder.setCamera(this.mCamera);
            this.mMediaRecorder.setVideoSource(1);
            if (capturerate > 0.0d) {
                this.mMediaRecorder.setCaptureRate(capturerate);
            } else {
                this.mMediaRecorder.setAudioSource(0);
            }
            this.mMediaRecorder.setProfile(CamcorderProfile.get(getFacing(), quality));
            this.mMediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
                public void onError(MediaRecorder mr, int what, int extra) {
                    CameraManager.this.stopMediaRecord((RecordCallBack) null);
                    if (callBack != null) {
                        callBack.onRecordError("" + what + ":" + extra);
                    }
                }
            });
            this.mMediaRecorder.setOrientationHint(orientationhint);
            this.mMediaRecorder.setOutputFile(fd);
            this.mMediaRecorder.setMaxFileSize(0);
            this.mMediaRecorder.setMaxDuration(0);
            this.mMediaRecorder.setPreviewDisplay(this.mPreview.getSurface());
            this.mMediaRecorder.prepare();
            this.mMediaRecorder.start();
            this.mIsRecording = true;
            if (callBack != null) {
                callBack.onRecordStarted();
            }
            return true;
        } catch (Exception e) {
            this.mIsRecording = false;
            if (this.mCamera != null) {
                try {
                    this.mCamera.lock();
                    this.mCamera.reconnect();
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
            e.printStackTrace();
            if (callBack == null) {
                return false;
            }
            callBack.onRecordError(e.toString());
            return false;
        }
    }

    public boolean startMediaRecord(String videopath, Size size, int fps, int orientationhint, final RecordCallBack callBack) {
        if (isMediaRecording()) {
            return false;
        }
        this.mMediaRecorder = new MediaRecorder();
        try {
            this.mCamera.unlock();
            this.mMediaRecorder.setCamera(this.mCamera);
            this.mMediaRecorder.setAudioSource(0);
            this.mMediaRecorder.setVideoSource(1);
            this.mMediaRecorder.setOutputFormat(2);
            this.mMediaRecorder.setAudioEncoder(1);
            this.mMediaRecorder.setVideoEncoder(2);
            this.mMediaRecorder.setVideoEncodingBitRate(20971520);
            this.mMediaRecorder.setOrientationHint(orientationhint);
            this.mMediaRecorder.setVideoSize(size.getWidth(), size.getHeight());
            this.mMediaRecorder.setVideoFrameRate(fps);
            this.mMediaRecorder.setOutputFile(videopath);
            this.mMediaRecorder.setMaxFileSize(0);
            this.mMediaRecorder.setMaxDuration(0);
            this.mMediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
                public void onError(MediaRecorder mr, int what, int extra) {
                    CameraUtils.LogV("Huang", "media record error!");
                    CameraManager.this.stopMediaRecord((RecordCallBack) null);
                    if (callBack != null) {
                        callBack.onRecordError("" + what + ":" + extra);
                    }
                }
            });
            this.mMediaRecorder.setPreviewDisplay(this.mPreview.getSurface());
            this.mMediaRecorder.prepare();
            this.mMediaRecorder.start();
            this.mIsRecording = true;
            if (callBack != null) {
                callBack.onRecordStarted();
            }
            return true;
        } catch (Exception e) {
            this.mIsRecording = false;
            if (this.mCamera != null) {
                this.mCamera.lock();
            }
            e.printStackTrace();
            if (callBack == null) {
                return false;
            }
            callBack.onRecordError(e.toString());
            return false;
        }
    }

    public boolean startMediaRecord(FileDescriptor fd, Size size, int fps, int orientationhint, final RecordCallBack callBack) {
        if (isMediaRecording()) {
            return false;
        }
        this.mMediaRecorder = new MediaRecorder();
        try {
            this.mCamera.unlock();
            this.mMediaRecorder.setCamera(this.mCamera);
            this.mMediaRecorder.setAudioSource(0);
            this.mMediaRecorder.setVideoSource(1);
            this.mMediaRecorder.setOutputFormat(2);
            this.mMediaRecorder.setAudioEncoder(1);
            this.mMediaRecorder.setVideoEncoder(2);
            this.mMediaRecorder.setVideoEncodingBitRate(20971520);
            this.mMediaRecorder.setOrientationHint(orientationhint);
            this.mMediaRecorder.setVideoSize(size.getWidth(), size.getHeight());
            this.mMediaRecorder.setVideoFrameRate(fps);
            this.mMediaRecorder.setOutputFile(fd);
            this.mMediaRecorder.setMaxFileSize(0);
            this.mMediaRecorder.setMaxDuration(0);
            this.mMediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
                public void onError(MediaRecorder mr, int what, int extra) {
                    CameraUtils.LogV("Huang", "media record error!");
                    CameraManager.this.stopMediaRecord((RecordCallBack) null);
                    if (callBack != null) {
                        callBack.onRecordError("" + what + ":" + extra);
                    }
                }
            });
            this.mMediaRecorder.setPreviewDisplay(this.mPreview.getSurface());
            this.mMediaRecorder.prepare();
            this.mMediaRecorder.start();
            this.mIsRecording = true;
            if (callBack != null) {
                callBack.onRecordStarted();
            }
            return true;
        } catch (Exception e) {
            this.mIsRecording = false;
            if (this.mCamera != null) {
                this.mCamera.lock();
            }
            e.printStackTrace();
            if (callBack == null) {
                return false;
            }
            callBack.onRecordError(e.toString());
            return false;
        }
    }

    public boolean isMediaRecording() {
        return this.mIsRecording;
    }

    public boolean stopMediaRecord(RecordCallBack callBack) {
        if (this.mMediaRecorder == null) {
            return false;
        }
        boolean isexceptionhappend = false;
        StringBuffer sb = new StringBuffer();
        if (!this.mIsRecording) {
            return false;
        }
        try {
            this.mMediaRecorder.stop();
        } catch (Exception e) {
            e.printStackTrace();
            isexceptionhappend = true;
            sb.append(e.getMessage()).append(" ");
        }
        try {
            this.mMediaRecorder.reset();
        } catch (Exception e2) {
            e2.printStackTrace();
            isexceptionhappend = true;
            sb.append(e2.getMessage()).append(" ");
        }
        try {
            this.mMediaRecorder.release();
        } catch (Exception e3) {
            e3.printStackTrace();
            isexceptionhappend = true;
            sb.append(e3.getMessage()).append(" ");
        }
        this.mMediaRecorder = null;
        this.mIsRecording = false;
        if (this.mCamera != null) {
            try {
                this.mCamera.lock();
                this.mCamera.reconnect();
            } catch (IOException e4) {
                e4.printStackTrace();
                isexceptionhappend = true;
                sb.append(e4.getMessage()).append(" ");
            }
        }
        if (isexceptionhappend) {
            String errmsg = sb.toString().trim();
            if (callBack == null || errmsg.isEmpty()) {
                return false;
            }
            callBack.onRecordError(errmsg);
            return false;
        }
        if (callBack != null) {
            callBack.onRecordEnd();
        }
        return true;
    }

    public double getMediaRecordVolumeAmplitude() {
        double ratio;
        if (this.mMediaRecorder == null || !isMediaRecording()) {
            return 0.0d;
        }
        try {
            ratio = ((double) this.mMediaRecorder.getMaxAmplitude()) / 1.0d;
        } catch (Exception e) {
            ratio = 0.0d;
        }
        if (ratio > 1.0d) {
            return 20.0d * Math.log10(ratio);
        }
        return 0.0d;
    }

    private void takePictureInternal(final TakePictureCallback callback) {
        this.mShowingPreview = false;
        this.mCamera.takePicture((Camera.ShutterCallback) null, (Camera.PictureCallback) null, (Camera.PictureCallback) null, new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                if (camera != null && CameraManager.this.isCameraOpened()) {
                    try {
                        CameraManager.this.setCameraRotation(0);
                        camera.startPreview();
                        boolean unused = CameraManager.this.mShowingPreview = true;
                    } catch (Exception e) {
                        data = null;
                        e.printStackTrace();
                    }
                    if (callback != null) {
                        callback.onPictureTaken(data, camera);
                    }
                }
            }
        });
    }

    private Size setDefaultPreviewSizeFromAspectRatio(Camera.Parameters parameters) {
        SortedSet<Size> sizes = this.mPreviewSizes.sizes(this.mAspectRatio);
        if (sizes == null) {
            this.mAspectRatio = defaultAspectRatio();
            sizes = this.mPreviewSizes.sizes(this.mAspectRatio);
        }
        Size previewsize = chooseOptimalSize(sizes);
        parameters.setPreviewSize(previewsize.getWidth(), previewsize.getHeight());
        return previewsize;
    }

    private void setDefaultPictureSizeFromAspectRatio(Camera.Parameters parameters, Size previewsize) {
        Size pictureSize;
        if (this.mPictureSizes.contains(previewsize)) {
            pictureSize = previewsize;
        } else if (this.mPictureSizes.sizes(this.mAspectRatio) != null) {
            pictureSize = this.mPictureSizes.sizes(this.mAspectRatio).last();
        } else {
            return;
        }
        parameters.setPictureSize(pictureSize.getWidth(), pictureSize.getHeight());
    }

    private boolean adjustCameraSizeByAspectRatio(Camera.Parameters parameters, AspectRatio ratio) {
        SortedSet<Size> sizes = this.mPreviewSizes.sizes(ratio);
        if (sizes == null) {
            return false;
        }
        Size previewsize = chooseOptimalSize(sizes);
        Size pictureSize = this.mPictureSizes.sizes(ratio).last();
        parameters.setPreviewSize(previewsize.getWidth(), previewsize.getHeight());
        parameters.setPictureSize(pictureSize.getWidth(), pictureSize.getHeight());
        return true;
    }

    private boolean setFlashInternal(int flash, Camera.Parameters parameters) {
        if (!isCameraOpened()) {
            return false;
        }
        List<String> modes = parameters.getSupportedFlashModes();
        String mode = FLASH_MODES.get(flash);
        if (modes == null || !modes.contains(mode)) {
            return false;
        }
        parameters.setFlashMode(mode);
        this.mFlash = flash;
        return true;
    }

    private boolean setParameter2Camera(Camera.Parameters parameters) {
        try {
            this.mCamera.setParameters(parameters);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Size chooseOptimalSize(SortedSet<Size> sizes) {
        int desiredWidth;
        int desiredHeight;
        int surfaceWidth = this.mPreview.getWidth();
        int surfaceHeight = this.mPreview.getHeight();
        int displayOrientation = getCameraRotation(this.mPreview.getCurrentViewStype());
        if (displayOrientation == 90 || displayOrientation == 270) {
            desiredWidth = surfaceHeight;
            desiredHeight = surfaceWidth;
        } else {
            desiredWidth = surfaceWidth;
            desiredHeight = surfaceHeight;
        }
        Size result = null;
        for (Size size : sizes) {
            if (desiredWidth <= size.getWidth() && desiredHeight <= size.getHeight()) {
                return size;
            }
            result = size;
        }
        return result;
    }

    private boolean setAutoFocusInternal(boolean autoFocus, Camera.Parameters parameters) {
        if (!isCameraOpened()) {
            return false;
        }
        List<String> modes = cameraParameters().getSupportedFocusModes();
        if (autoFocus) {
            if (this.mCameraMode == 0 && modes.contains("continuous-picture")) {
                parameters.setFocusMode("continuous-picture");
            } else if (this.mCameraMode == 1 && modes.contains("continuous-video")) {
                parameters.setFocusMode("continuous-video");
            } else if (!modes.contains(com.freevisiontech.fvmobile.utility.Constants.SCENE_MODE_AUTO)) {
                return false;
            } else {
                parameters.setFocusMode(com.freevisiontech.fvmobile.utility.Constants.SCENE_MODE_AUTO);
            }
        } else if (modes.contains("fixed")) {
            parameters.setFocusMode("fixed");
        } else if (!modes.contains("infinity")) {
            return false;
        } else {
            parameters.setFocusMode("infinity");
        }
        return true;
    }

    private int getCameraByFacing(int facing) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        int count = Camera.getNumberOfCameras();
        for (int i = 0; i < count; i++) {
            Camera.getCameraInfo(i, info);
            if (info.facing == facing) {
                return i;
            }
        }
        return -1;
    }
}
