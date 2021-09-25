package com.freevisiontech.cameralib.impl.Camera2;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraConstrainedHighSpeedCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.DngCreator;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.ColorSpaceTransform;
import android.hardware.camera2.params.MeteringRectangle;
import android.hardware.camera2.params.RggbChannelVector;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.location.Location;
import android.media.CamcorderProfile;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Range;
import android.util.Rational;
import android.view.Surface;
import com.freevisiontech.cameralib.AspectRatio;
import com.freevisiontech.cameralib.FVCamera2Manager;
import com.freevisiontech.cameralib.Size;
import com.freevisiontech.cameralib.SizeMap;
import com.freevisiontech.cameralib.utils.CameraUtils;
import com.freevisiontech.cameralib.view.camera2.CameraView;
import com.freevisiontech.tracking.FVTrackManagerEx;
import java.io.FileDescriptor;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Camera2Manager {
    static final /* synthetic */ boolean $assertionsDisabled = (!Camera2Manager.class.desiredAssertionStatus());
    private static final String TAG = "Camera2Manager";
    public static boolean UsingEx = true;
    private final int BASE = 1;
    /* access modifiers changed from: private */
    public Activity context;
    /* access modifiers changed from: private */
    public FVCamera2Manager fvCameraManager = null;
    private boolean isPreviewListenerEnabled = false;
    private boolean isPreviewListenerEnabledFromRecord = false;
    /* access modifiers changed from: private */
    public boolean isunderhighspeedcapture = false;
    /* access modifiers changed from: private */
    public long laststart = 0;
    private AutoFocusListener mAutoFocusListener = null;
    /* access modifiers changed from: private */
    public Handler mBackgroundHandler = null;
    private HandlerThread mBackgroundThread = null;
    /* access modifiers changed from: private */
    public Camera2States mCamera2States = new Camera2States();
    /* access modifiers changed from: private */
    public CameraDevice mCameraDevice = null;
    /* access modifiers changed from: private */
    public String mCameraId = "";
    private int mCameraMode = 0;
    /* access modifiers changed from: private */
    public Semaphore mCameraOpenCloseLock = new Semaphore(1);
    /* access modifiers changed from: private */
    public CameraParameters mCameraParameters = new CameraParameters();
    private CameraStatesListener mCameraStatesListener = null;
    /* access modifiers changed from: private */
    public CameraStatusListener mCameraStatusListener = null;
    private final CameraDevice.StateCallback mDeviceStateCallback = new CameraDevice.StateCallback() {
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            Camera2Manager.this.mCameraOpenCloseLock.release();
            CameraDevice unused = Camera2Manager.this.mCameraDevice = cameraDevice;
            if (!Camera2Manager.this.createCameraPreviewSession(false, true) && Camera2Manager.this.mCameraStatusListener != null) {
                Camera2Manager.this.mCameraStatusListener.onError(Camera2Manager.this.mCameraDevice, 10000);
            }
        }

        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            Camera2Manager.this.mCameraOpenCloseLock.release();
            cameraDevice.close();
            CameraUtils.LogV("Device", "---------onDisconnected----------");
            CameraDevice unused = Camera2Manager.this.mCameraDevice = null;
            String unused2 = Camera2Manager.this.mCameraId = "";
            if (Camera2Manager.this.mCameraStatusListener != null) {
                Camera2Manager.this.mCameraStatusListener.onDisconnected(cameraDevice);
            }
        }

        public void onError(@NonNull CameraDevice cameraDevice, int error) {
            Camera2Manager.this.mCameraOpenCloseLock.release();
            cameraDevice.close();
            CameraUtils.LogV("Device", "----------onError---------" + error);
            CameraDevice unused = Camera2Manager.this.mCameraDevice = null;
            String unused2 = Camera2Manager.this.mCameraId = "";
            if (Camera2Manager.this.mCameraStatusListener != null) {
                Camera2Manager.this.mCameraStatusListener.onError(cameraDevice, error);
            }
        }

        public void onClosed(@NonNull CameraDevice camera) {
            super.onClosed(camera);
            if (Camera2Manager.this.mCameraStatusListener != null) {
                Camera2Manager.this.mCameraStatusListener.onClosed(camera);
            }
        }
    };
    private int mFace = 1;
    private int mHwlevel = 2;
    private boolean mIsAutofocus4Takepicutre = true;
    /* access modifiers changed from: private */
    public boolean mIsMediaRecording = false;
    private int mMediaRecordBitRate = 0;
    /* access modifiers changed from: private */
    public MediaRecorder mMediaRecorder = null;
    private final ImageReader.OnImageAvailableListener mPreivewFrameListener = new ImageReader.OnImageAvailableListener() {
        public void onImageAvailable(ImageReader reader) {
            byte[] bytes;
            long cur = System.currentTimeMillis();
            Image image = reader.acquireLatestImage();
            if (image != null) {
                CameraUtils.LogV("HTrack_ImgAvai_Begin", "****onImageAvailable***********" + image.getWidth() + "X" + image.getHeight() + "; 帧间 ==" + (cur - Camera2Manager.this.laststart));
                long unused = Camera2Manager.this.laststart = cur;
                FVTrackManagerEx tracker = Camera2Manager.this.fvCameraManager.getTrackManager();
                if (tracker == null || !tracker.isIstrack()) {
                    image.close();
                } else {
                    long t0 = System.currentTimeMillis();
                    if (Camera2Manager.UsingEx) {
                        bytes = CameraUtils.ImageToByteEx(image);
                        CameraUtils.LogV("HTrack_Image2Byte", "ImageToByteEx " + (System.currentTimeMillis() - t0));
                    } else {
                        bytes = CameraUtils.ImageToByte(image);
                        CameraUtils.LogV("HTrack_Image2Byte", "ImageToByte " + (System.currentTimeMillis() - t0));
                    }
                    image.close();
                    CameraUtils.LogV("HTrack_onPreFrame", "FVCamera2Manager onPreviewFrame " + bytes.length);
                    long tb = System.currentTimeMillis();
                    tracker.update(bytes);
                    CameraUtils.LogV("HTrack", "track duration=" + (System.currentTimeMillis() - tb));
                }
                CameraUtils.LogV("HTrack_ImgAvai_End", "onImageAvailable耗时:" + (System.currentTimeMillis() - cur));
            }
        }
    };
    private CameraView mPreview = null;
    private PreviewCallback mPreviewCallback = null;
    /* access modifiers changed from: private */
    public CameraCaptureSession.CaptureCallback mPreviewCaptureRequestCallback = new CameraCaptureSession.CaptureCallback() {
        public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
            Camera2Manager.this.process(partialResult);
        }

        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
            boolean unused = Camera2Manager.this.isunderhighspeedcapture = false;
            Camera2Manager.this.process(result);
            Camera2Manager.this.doStateCallback(result);
        }
    };
    private ImageReader mPreviewFrameReader;
    /* access modifiers changed from: private */
    public CameraCaptureSession.CaptureCallback mPreviewHighSpeedCaptureRequestCallback = new CameraCaptureSession.CaptureCallback() {
        public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
            Camera2Manager.this.process(partialResult);
        }

        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
            boolean unused = Camera2Manager.this.isunderhighspeedcapture = true;
            Camera2Manager.this.process(result);
        }
    };
    /* access modifiers changed from: private */
    public CaptureRequest.Builder mPreviewRequestBuilder;
    /* access modifiers changed from: private */
    public CameraCaptureSession mPreviewSession;
    private Size mPreviewSize;
    /* access modifiers changed from: private */
    public RecordListener mRecordListener = null;
    /* access modifiers changed from: private */
    public int mState = 0;
    /* access modifiers changed from: private */
    public TakePictureListener mTakePictureListener = null;
    /* access modifiers changed from: private */
    public Semaphore mWait4focusLock = new Semaphore(1);
    private Semaphore mtakepicturelock = new Semaphore(1);
    private ImageReader rawImageReader = null;
    /* access modifiers changed from: private */
    public RawImageAvailableListener rawimageAvailableListener = new RawImageAvailableListener() {
        public void onImageAvailable(ImageReader reader) {
            CameraUtils.LogV("RawPictures", "onImageAvailable");
            if (reader == null) {
                CameraUtils.LogV("RawPictures", "onImageAvailable reader == null");
                return;
            }
            Image image = reader.acquireNextImage();
            try {
                if (Camera2Manager.this.mTakePictureListener != null) {
                    Camera2Manager.this.mTakePictureListener.onRawPictureTaken(image, new DngCreator(Camera2Manager.this.getCameraCharacteristics(), this.mCaptureResult));
                }
            } catch (Exception e) {
                CameraUtils.LogV("RawPictures", "ImageSaverHelper exception:" + e.getMessage());
                if (Camera2Manager.this.mTakePictureListener != null) {
                    Camera2Manager.this.mTakePictureListener.onPictureTakenError(10002);
                }
            } finally {
                image.close();
            }
        }
    };
    private ImageReader.OnImageAvailableListener stillimageAvailableListener = new ImageReader.OnImageAvailableListener() {
        public void onImageAvailable(ImageReader reader) {
            CameraUtils.LogV("Pictures", "onImageAvailable");
            if (reader == null) {
                CameraUtils.LogV("Pictures", "onImageAvailable reader == null");
                return;
            }
            Image image = reader.acquireNextImage();
            try {
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                CameraUtils.LogV("Pictures", "ImageSaverHelper");
                image.close();
                if (Camera2Manager.this.mTakePictureListener != null) {
                    Camera2Manager.this.mTakePictureListener.onPictureTaken(bytes);
                }
            } catch (Exception e) {
                e.printStackTrace();
                CameraUtils.LogV("Pictures", "ImageSaverHelper exception");
                if (Camera2Manager.this.mTakePictureListener != null) {
                    Camera2Manager.this.mTakePictureListener.onPictureTakenError(10002);
                }
            }
        }
    };
    private ImageReader stillimageReader = null;
    List<Integer> zoom_ratios = null;

    public interface AutoFocusListener {
        void focusLocked();

        void focusUnlocked();
    }

    public interface CameraStatesListener {
        void aelocked(Boolean bool);

        void aftrigger(Integer num);

        void awblocked(Boolean bool);

        void colorCorrectionGains(RggbChannelVector rggbChannelVector);

        void exposureCompensation(Integer num);

        void exposureTime(Long l);

        void focalLen(Float f);

        void focusDistance(Float f);

        void fpsRange(Range<Integer> range);

        void frameDuration(Long l);

        void isoValue(Integer num);

        void lenState(Integer num);

        void modes(Integer num, Integer num2, Integer num3, Integer num4, Integer num5);

        void states(Integer num, Integer num2, Integer num3, Integer num4);
    }

    public interface CameraStatusListener {
        void onClosed(@NonNull CameraDevice cameraDevice);

        void onDisconnected(@NonNull CameraDevice cameraDevice);

        void onError(@NonNull CameraDevice cameraDevice, int i);

        void onOpened(@NonNull CameraDevice cameraDevice);

        void onSessionActive(@NonNull CameraCaptureSession cameraCaptureSession);

        void onSessionClosed(@NonNull CameraCaptureSession cameraCaptureSession);

        void onSessionSurfacePrepared(@NonNull CameraCaptureSession cameraCaptureSession, @NonNull Surface surface);
    }

    public interface PreviewCallback {
        void onPreviewFrame(byte[] bArr);
    }

    public interface RecordListener {
        void onRecordEnd();

        void onRecordError(String str);

        void onRecordStarted();
    }

    public interface TakePictureListener {
        void captureStillPictureCompleted();

        void captureStillPictureError(int i);

        void onPictureTaken(byte[] bArr);

        void onPictureTakenError(int i);

        void onRawPictureTaken(Image image, DngCreator dngCreator);
    }

    public static class CompareSizesByArea implements Comparator<Size> {
        public int compare(Size lhs, Size rhs) {
            return Long.signum((((long) lhs.getWidth()) * ((long) lhs.getHeight())) - (((long) rhs.getWidth()) * ((long) rhs.getHeight())));
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v61, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v60, resolved type: android.util.Range} */
    /* access modifiers changed from: private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void doStateCallback(@android.support.annotation.NonNull android.hardware.camera2.TotalCaptureResult r19) {
        /*
            r18 = this;
            r13 = 0
            r16 = 0
            r10 = 0
            r11 = 0
            android.hardware.camera2.CaptureResult$Key r2 = android.hardware.camera2.CaptureResult.CONTROL_MODE     // Catch:{ Exception -> 0x03e6 }
            r0 = r19
            java.lang.Object r2 = r0.get(r2)     // Catch:{ Exception -> 0x03e6 }
            r0 = r2
            java.lang.Integer r0 = (java.lang.Integer) r0     // Catch:{ Exception -> 0x03e6 }
            r13 = r0
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r2 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r2 = r2.controlmode     // Catch:{ Exception -> 0x03e6 }
            if (r13 == r2) goto L_0x001f
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r2 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            r2.controlmode = r13     // Catch:{ Exception -> 0x03e6 }
        L_0x001f:
            android.hardware.camera2.CaptureResult$Key r2 = android.hardware.camera2.CaptureResult.CONTROL_AE_MODE     // Catch:{ Exception -> 0x03e6 }
            r0 = r19
            java.lang.Object r2 = r0.get(r2)     // Catch:{ Exception -> 0x03e6 }
            r0 = r2
            java.lang.Integer r0 = (java.lang.Integer) r0     // Catch:{ Exception -> 0x03e6 }
            r13 = r0
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r2 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r2 = r2.aemode     // Catch:{ Exception -> 0x03e6 }
            if (r13 == r2) goto L_0x0039
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r2 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            r2.aemode = r13     // Catch:{ Exception -> 0x03e6 }
        L_0x0039:
            android.hardware.camera2.CaptureResult$Key r2 = android.hardware.camera2.CaptureResult.CONTROL_AWB_MODE     // Catch:{ Exception -> 0x03e6 }
            r0 = r19
            java.lang.Object r2 = r0.get(r2)     // Catch:{ Exception -> 0x03e6 }
            r0 = r2
            java.lang.Integer r0 = (java.lang.Integer) r0     // Catch:{ Exception -> 0x03e6 }
            r13 = r0
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r2 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r2 = r2.awbmode     // Catch:{ Exception -> 0x03e6 }
            if (r13 == r2) goto L_0x0053
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r2 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            r2.awbmode = r13     // Catch:{ Exception -> 0x03e6 }
        L_0x0053:
            android.hardware.camera2.CaptureResult$Key r2 = android.hardware.camera2.CaptureResult.CONTROL_AF_MODE     // Catch:{ Exception -> 0x03e6 }
            r0 = r19
            java.lang.Object r2 = r0.get(r2)     // Catch:{ Exception -> 0x03e6 }
            r0 = r2
            java.lang.Integer r0 = (java.lang.Integer) r0     // Catch:{ Exception -> 0x03e6 }
            r13 = r0
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r2 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r2 = r2.afmode     // Catch:{ Exception -> 0x03e6 }
            if (r13 == r2) goto L_0x006d
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r2 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            r2.afmode = r13     // Catch:{ Exception -> 0x03e6 }
        L_0x006d:
            android.hardware.camera2.CaptureResult$Key r2 = android.hardware.camera2.CaptureResult.FLASH_MODE     // Catch:{ Exception -> 0x03e6 }
            r0 = r19
            java.lang.Object r2 = r0.get(r2)     // Catch:{ Exception -> 0x03e6 }
            r0 = r2
            java.lang.Integer r0 = (java.lang.Integer) r0     // Catch:{ Exception -> 0x03e6 }
            r13 = r0
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r2 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r2 = r2.flashmode     // Catch:{ Exception -> 0x03e6 }
            if (r13 == r2) goto L_0x0087
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r2 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            r2.flashmode = r13     // Catch:{ Exception -> 0x03e6 }
        L_0x0087:
            android.hardware.camera2.CaptureResult$Key r2 = android.hardware.camera2.CaptureResult.CONTROL_SCENE_MODE     // Catch:{ Exception -> 0x03e6 }
            r0 = r19
            java.lang.Object r2 = r0.get(r2)     // Catch:{ Exception -> 0x03e6 }
            r0 = r2
            java.lang.Integer r0 = (java.lang.Integer) r0     // Catch:{ Exception -> 0x03e6 }
            r13 = r0
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r2 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r2 = r2.scenemode     // Catch:{ Exception -> 0x03e6 }
            if (r13 == r2) goto L_0x00a1
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r2 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            r2.scenemode = r13     // Catch:{ Exception -> 0x03e6 }
        L_0x00a1:
            android.hardware.camera2.CaptureResult$Key r2 = android.hardware.camera2.CaptureResult.CONTROL_AE_EXPOSURE_COMPENSATION     // Catch:{ Exception -> 0x03e6 }
            r0 = r19
            java.lang.Object r2 = r0.get(r2)     // Catch:{ Exception -> 0x03e6 }
            r0 = r2
            java.lang.Integer r0 = (java.lang.Integer) r0     // Catch:{ Exception -> 0x03e6 }
            r13 = r0
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r2 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r2 = r2.f1074ev     // Catch:{ Exception -> 0x03e6 }
            if (r13 == r2) goto L_0x00bb
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r2 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            r2.f1074ev = r13     // Catch:{ Exception -> 0x03e6 }
        L_0x00bb:
            android.hardware.camera2.CaptureResult$Key r2 = android.hardware.camera2.CaptureResult.CONTROL_AF_STATE     // Catch:{ Exception -> 0x03e6 }
            r0 = r19
            java.lang.Object r2 = r0.get(r2)     // Catch:{ Exception -> 0x03e6 }
            r0 = r2
            java.lang.Integer r0 = (java.lang.Integer) r0     // Catch:{ Exception -> 0x03e6 }
            r13 = r0
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r2 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r2 = r2.afstate     // Catch:{ Exception -> 0x03e6 }
            if (r13 == r2) goto L_0x00d5
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r2 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            r2.afstate = r13     // Catch:{ Exception -> 0x03e6 }
        L_0x00d5:
            android.hardware.camera2.CaptureResult$Key r2 = android.hardware.camera2.CaptureResult.CONTROL_AE_STATE     // Catch:{ Exception -> 0x03e6 }
            r0 = r19
            java.lang.Object r2 = r0.get(r2)     // Catch:{ Exception -> 0x03e6 }
            r0 = r2
            java.lang.Integer r0 = (java.lang.Integer) r0     // Catch:{ Exception -> 0x03e6 }
            r13 = r0
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r2 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r2 = r2.aestate     // Catch:{ Exception -> 0x03e6 }
            if (r13 == r2) goto L_0x00ef
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r2 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            r2.aestate = r13     // Catch:{ Exception -> 0x03e6 }
        L_0x00ef:
            android.hardware.camera2.CaptureResult$Key r2 = android.hardware.camera2.CaptureResult.CONTROL_AWB_STATE     // Catch:{ Exception -> 0x03e6 }
            r0 = r19
            java.lang.Object r2 = r0.get(r2)     // Catch:{ Exception -> 0x03e6 }
            r0 = r2
            java.lang.Integer r0 = (java.lang.Integer) r0     // Catch:{ Exception -> 0x03e6 }
            r13 = r0
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r2 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r2 = r2.awbstate     // Catch:{ Exception -> 0x03e6 }
            if (r13 == r2) goto L_0x0109
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r2 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            r2.awbstate = r13     // Catch:{ Exception -> 0x03e6 }
        L_0x0109:
            android.hardware.camera2.CaptureResult$Key r2 = android.hardware.camera2.CaptureResult.FLASH_STATE     // Catch:{ Exception -> 0x03e6 }
            r0 = r19
            java.lang.Object r2 = r0.get(r2)     // Catch:{ Exception -> 0x03e6 }
            r0 = r2
            java.lang.Integer r0 = (java.lang.Integer) r0     // Catch:{ Exception -> 0x03e6 }
            r13 = r0
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r2 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r2 = r2.flashstate     // Catch:{ Exception -> 0x03e6 }
            if (r13 == r2) goto L_0x0123
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r2 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            r2.flashstate = r13     // Catch:{ Exception -> 0x03e6 }
        L_0x0123:
            android.hardware.camera2.CaptureResult$Key r2 = android.hardware.camera2.CaptureResult.LENS_STATE     // Catch:{ Exception -> 0x03e6 }
            r0 = r19
            java.lang.Object r2 = r0.get(r2)     // Catch:{ Exception -> 0x03e6 }
            r0 = r2
            java.lang.Integer r0 = (java.lang.Integer) r0     // Catch:{ Exception -> 0x03e6 }
            r13 = r0
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r2 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r2 = r2.lenstate     // Catch:{ Exception -> 0x03e6 }
            if (r13 == r2) goto L_0x013d
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r2 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            r2.lenstate = r13     // Catch:{ Exception -> 0x03e6 }
        L_0x013d:
            android.hardware.camera2.CaptureResult$Key r2 = android.hardware.camera2.CaptureResult.CONTROL_AWB_STATE     // Catch:{ Exception -> 0x03e6 }
            r0 = r19
            java.lang.Object r2 = r0.get(r2)     // Catch:{ Exception -> 0x03e6 }
            r0 = r2
            java.lang.Integer r0 = (java.lang.Integer) r0     // Catch:{ Exception -> 0x03e6 }
            r13 = r0
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r2 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r2 = r2.awbstate     // Catch:{ Exception -> 0x03e6 }
            if (r13 == r2) goto L_0x0157
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r2 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            r2.awbstate = r13     // Catch:{ Exception -> 0x03e6 }
        L_0x0157:
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r3 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            android.hardware.camera2.CaptureResult$Key r2 = android.hardware.camera2.CaptureResult.SCALER_CROP_REGION     // Catch:{ Exception -> 0x03e6 }
            r0 = r19
            java.lang.Object r2 = r0.get(r2)     // Catch:{ Exception -> 0x03e6 }
            android.graphics.Rect r2 = (android.graphics.Rect) r2     // Catch:{ Exception -> 0x03e6 }
            r3.zoom = r2     // Catch:{ Exception -> 0x03e6 }
            android.hardware.camera2.CaptureResult$Key r2 = android.hardware.camera2.CaptureResult.CONTROL_AE_LOCK     // Catch:{ Exception -> 0x03e6 }
            r0 = r19
            java.lang.Object r14 = r0.get(r2)     // Catch:{ Exception -> 0x03e6 }
            java.lang.Boolean r14 = (java.lang.Boolean) r14     // Catch:{ Exception -> 0x03e6 }
            android.hardware.camera2.CaptureResult$Key r2 = android.hardware.camera2.CaptureResult.CONTROL_AWB_LOCK     // Catch:{ Exception -> 0x03e6 }
            r0 = r19
            java.lang.Object r15 = r0.get(r2)     // Catch:{ Exception -> 0x03e6 }
            java.lang.Boolean r15 = (java.lang.Boolean) r15     // Catch:{ Exception -> 0x03e6 }
            android.hardware.camera2.CaptureResult$Key r2 = android.hardware.camera2.CaptureResult.CONTROL_AF_TRIGGER     // Catch:{ Exception -> 0x03e6 }
            r0 = r19
            java.lang.Object r8 = r0.get(r2)     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r8 = (java.lang.Integer) r8     // Catch:{ Exception -> 0x03e6 }
            android.hardware.camera2.CaptureResult$Key r2 = android.hardware.camera2.CaptureResult.CONTROL_CAPTURE_INTENT     // Catch:{ Exception -> 0x03e6 }
            r0 = r19
            java.lang.Object r12 = r0.get(r2)     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r12 = (java.lang.Integer) r12     // Catch:{ Exception -> 0x03e6 }
            r17 = 0
            r0 = r18
            int r2 = r0.mHwlevel     // Catch:{ Exception -> 0x03e6 }
            r3 = 2
            if (r2 != r3) goto L_0x0241
            java.lang.String r2 = "CameraState"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x03e6 }
            r3.<init>()     // Catch:{ Exception -> 0x03e6 }
            java.lang.String r4 = "SUPPORTED_HARDWARE_LEVEL_LEGACY  control mode = "
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r4 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r4 = r4.controlmode     // Catch:{ Exception -> 0x03e6 }
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x03e6 }
            java.lang.String r4 = " ae mode ="
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r4 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r4 = r4.aemode     // Catch:{ Exception -> 0x03e6 }
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x03e6 }
            java.lang.String r4 = " af mode ="
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r4 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r4 = r4.afmode     // Catch:{ Exception -> 0x03e6 }
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x03e6 }
            java.lang.String r4 = " awb mode = "
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r4 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r4 = r4.awbmode     // Catch:{ Exception -> 0x03e6 }
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x03e6 }
            java.lang.String r4 = ", ev="
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r4 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r4 = r4.f1074ev     // Catch:{ Exception -> 0x03e6 }
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x03e6 }
            java.lang.String r4 = " afstate="
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r4 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r4 = r4.afstate     // Catch:{ Exception -> 0x03e6 }
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x03e6 }
            java.lang.String r4 = " aestate="
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r4 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r4 = r4.aestate     // Catch:{ Exception -> 0x03e6 }
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x03e6 }
            java.lang.String r4 = " awbstate="
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r4 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r4 = r4.awbmode     // Catch:{ Exception -> 0x03e6 }
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x03e6 }
            java.lang.String r4 = " intent ="
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x03e6 }
            java.lang.StringBuilder r3 = r3.append(r12)     // Catch:{ Exception -> 0x03e6 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x03e6 }
            com.freevisiontech.cameralib.utils.CameraUtils.LogV(r2, r3)     // Catch:{ Exception -> 0x03e6 }
        L_0x023a:
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2Manager$CameraStatesListener r2 = r0.mCameraStatesListener     // Catch:{ Exception -> 0x03e6 }
            if (r2 != 0) goto L_0x03ec
        L_0x0240:
            return
        L_0x0241:
            android.hardware.camera2.CaptureResult$Key r2 = android.hardware.camera2.CaptureResult.CONTROL_AE_TARGET_FPS_RANGE     // Catch:{ Exception -> 0x03e6 }
            r0 = r19
            java.lang.Object r2 = r0.get(r2)     // Catch:{ Exception -> 0x03e6 }
            r0 = r2
            android.util.Range r0 = (android.util.Range) r0     // Catch:{ Exception -> 0x03e6 }
            r11 = r0
            r0 = r18
            boolean r2 = r0.isunderhighspeedcapture     // Catch:{ Exception -> 0x03e6 }
            if (r2 != 0) goto L_0x0263
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r3 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            android.hardware.camera2.CaptureResult$Key r2 = android.hardware.camera2.CaptureResult.CONTROL_AE_TARGET_FPS_RANGE     // Catch:{ Exception -> 0x03e6 }
            r0 = r19
            java.lang.Object r2 = r0.get(r2)     // Catch:{ Exception -> 0x03e6 }
            android.util.Range r2 = (android.util.Range) r2     // Catch:{ Exception -> 0x03e6 }
            r3.fpsranges = r2     // Catch:{ Exception -> 0x03e6 }
        L_0x0263:
            android.hardware.camera2.CaptureResult$Key r2 = android.hardware.camera2.CaptureResult.SENSOR_SENSITIVITY     // Catch:{ Exception -> 0x03e6 }
            r0 = r19
            java.lang.Object r2 = r0.get(r2)     // Catch:{ Exception -> 0x03e6 }
            r0 = r2
            java.lang.Integer r0 = (java.lang.Integer) r0     // Catch:{ Exception -> 0x03e6 }
            r13 = r0
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r2 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r2 = r2.iso     // Catch:{ Exception -> 0x03e6 }
            if (r13 == r2) goto L_0x027d
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r2 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            r2.iso = r13     // Catch:{ Exception -> 0x03e6 }
        L_0x027d:
            android.hardware.camera2.CaptureResult$Key r2 = android.hardware.camera2.CaptureResult.COLOR_CORRECTION_MODE     // Catch:{ Exception -> 0x03e6 }
            r0 = r19
            java.lang.Object r2 = r0.get(r2)     // Catch:{ Exception -> 0x03e6 }
            r0 = r2
            java.lang.Integer r0 = (java.lang.Integer) r0     // Catch:{ Exception -> 0x03e6 }
            r13 = r0
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r2 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r2 = r2.colorcorrectmode     // Catch:{ Exception -> 0x03e6 }
            if (r13 == r2) goto L_0x0297
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r2 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            r2.colorcorrectmode = r13     // Catch:{ Exception -> 0x03e6 }
        L_0x0297:
            android.hardware.camera2.CaptureResult$Key r2 = android.hardware.camera2.CaptureResult.SENSOR_FRAME_DURATION     // Catch:{ Exception -> 0x03e6 }
            r0 = r19
            java.lang.Object r2 = r0.get(r2)     // Catch:{ Exception -> 0x03e6 }
            r0 = r2
            java.lang.Long r0 = (java.lang.Long) r0     // Catch:{ Exception -> 0x03e6 }
            r16 = r0
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r2 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Long r2 = r2.frameduration     // Catch:{ Exception -> 0x03e6 }
            r0 = r16
            if (r0 == r2) goto L_0x02b6
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r2 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            r0 = r16
            r2.frameduration = r0     // Catch:{ Exception -> 0x03e6 }
        L_0x02b6:
            android.hardware.camera2.CaptureResult$Key r2 = android.hardware.camera2.CaptureResult.SENSOR_EXPOSURE_TIME     // Catch:{ Exception -> 0x03e6 }
            r0 = r19
            java.lang.Object r2 = r0.get(r2)     // Catch:{ Exception -> 0x03e6 }
            r0 = r2
            java.lang.Long r0 = (java.lang.Long) r0     // Catch:{ Exception -> 0x03e6 }
            r16 = r0
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r2 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Long r2 = r2.exposuretime     // Catch:{ Exception -> 0x03e6 }
            r0 = r16
            if (r0 == r2) goto L_0x02d5
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r2 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            r0 = r16
            r2.exposuretime = r0     // Catch:{ Exception -> 0x03e6 }
        L_0x02d5:
            android.hardware.camera2.CaptureResult$Key r2 = android.hardware.camera2.CaptureResult.LENS_FOCAL_LENGTH     // Catch:{ Exception -> 0x03e6 }
            r0 = r19
            java.lang.Object r2 = r0.get(r2)     // Catch:{ Exception -> 0x03e6 }
            r0 = r2
            java.lang.Float r0 = (java.lang.Float) r0     // Catch:{ Exception -> 0x03e6 }
            r10 = r0
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r2 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Float r2 = r2.focalLen     // Catch:{ Exception -> 0x03e6 }
            if (r10 == r2) goto L_0x02ef
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r2 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            r2.focalLen = r10     // Catch:{ Exception -> 0x03e6 }
        L_0x02ef:
            android.hardware.camera2.CaptureResult$Key r2 = android.hardware.camera2.CaptureResult.LENS_FOCUS_DISTANCE     // Catch:{ Exception -> 0x03e6 }
            r0 = r19
            java.lang.Object r2 = r0.get(r2)     // Catch:{ Exception -> 0x03e6 }
            r0 = r2
            java.lang.Float r0 = (java.lang.Float) r0     // Catch:{ Exception -> 0x03e6 }
            r10 = r0
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r2 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Float r2 = r2.focusdistance     // Catch:{ Exception -> 0x03e6 }
            if (r10 == r2) goto L_0x0309
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r2 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            r2.focusdistance = r10     // Catch:{ Exception -> 0x03e6 }
        L_0x0309:
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r3 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            android.hardware.camera2.CaptureResult$Key r2 = android.hardware.camera2.CaptureResult.COLOR_CORRECTION_GAINS     // Catch:{ Exception -> 0x03e6 }
            r0 = r19
            java.lang.Object r2 = r0.get(r2)     // Catch:{ Exception -> 0x03e6 }
            android.hardware.camera2.params.RggbChannelVector r2 = (android.hardware.camera2.params.RggbChannelVector) r2     // Catch:{ Exception -> 0x03e6 }
            r3.gain = r2     // Catch:{ Exception -> 0x03e6 }
            android.hardware.camera2.CaptureResult$Key r2 = android.hardware.camera2.CaptureResult.SENSOR_FRAME_DURATION     // Catch:{ Exception -> 0x03e6 }
            r0 = r19
            java.lang.Object r2 = r0.get(r2)     // Catch:{ Exception -> 0x03e6 }
            java.lang.Long r2 = (java.lang.Long) r2     // Catch:{ Exception -> 0x03e6 }
            long r2 = com.freevisiontech.cameralib.utils.CameraUtils.Long2long(r2)     // Catch:{ Exception -> 0x03e6 }
            java.lang.Long r17 = java.lang.Long.valueOf(r2)     // Catch:{ Exception -> 0x03e6 }
            java.lang.String r2 = "CameraState"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x03e6 }
            r3.<init>()     // Catch:{ Exception -> 0x03e6 }
            java.lang.String r4 = " control mode = "
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r4 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r4 = r4.controlmode     // Catch:{ Exception -> 0x03e6 }
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x03e6 }
            java.lang.String r4 = " ae mode ="
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r4 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r4 = r4.aemode     // Catch:{ Exception -> 0x03e6 }
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x03e6 }
            java.lang.String r4 = " af mode ="
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r4 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r4 = r4.afmode     // Catch:{ Exception -> 0x03e6 }
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x03e6 }
            java.lang.String r4 = " awb mode = "
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r4 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r4 = r4.awbmode     // Catch:{ Exception -> 0x03e6 }
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x03e6 }
            java.lang.String r4 = " iso="
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r4 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r4 = r4.iso     // Catch:{ Exception -> 0x03e6 }
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x03e6 }
            java.lang.String r4 = " exptime="
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r4 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Long r4 = r4.exposuretime     // Catch:{ Exception -> 0x03e6 }
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x03e6 }
            java.lang.String r4 = ", ev="
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r4 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r4 = r4.f1074ev     // Catch:{ Exception -> 0x03e6 }
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x03e6 }
            java.lang.String r4 = " aestate="
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r4 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r4 = r4.aestate     // Catch:{ Exception -> 0x03e6 }
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x03e6 }
            java.lang.String r4 = " focusdistance="
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r4 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Float r4 = r4.focusdistance     // Catch:{ Exception -> 0x03e6 }
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x03e6 }
            java.lang.String r4 = " afstate="
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r4 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r4 = r4.afstate     // Catch:{ Exception -> 0x03e6 }
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x03e6 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x03e6 }
            com.freevisiontech.cameralib.utils.CameraUtils.LogV(r2, r3)     // Catch:{ Exception -> 0x03e6 }
            goto L_0x023a
        L_0x03e6:
            r9 = move-exception
            r9.printStackTrace()
            goto L_0x0240
        L_0x03ec:
            r0 = r18
            int r2 = r0.mHwlevel     // Catch:{ Exception -> 0x03e6 }
            r3 = 2
            if (r2 == r3) goto L_0x0451
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2Manager$CameraStatesListener r2 = r0.mCameraStatesListener     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r3 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r3 = r3.iso     // Catch:{ Exception -> 0x03e6 }
            r2.isoValue(r3)     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2Manager$CameraStatesListener r2 = r0.mCameraStatesListener     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r3 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Long r3 = r3.exposuretime     // Catch:{ Exception -> 0x03e6 }
            r2.exposureTime(r3)     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2Manager$CameraStatesListener r2 = r0.mCameraStatesListener     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r3 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r3 = r3.f1074ev     // Catch:{ Exception -> 0x03e6 }
            r2.exposureCompensation(r3)     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2Manager$CameraStatesListener r2 = r0.mCameraStatesListener     // Catch:{ Exception -> 0x03e6 }
            r0 = r17
            r2.frameDuration(r0)     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2Manager$CameraStatesListener r2 = r0.mCameraStatesListener     // Catch:{ Exception -> 0x03e6 }
            r2.fpsRange(r11)     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2Manager$CameraStatesListener r2 = r0.mCameraStatesListener     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r3 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            android.hardware.camera2.params.RggbChannelVector r3 = r3.gain     // Catch:{ Exception -> 0x03e6 }
            r2.colorCorrectionGains(r3)     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2Manager$CameraStatesListener r2 = r0.mCameraStatesListener     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r3 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Float r3 = r3.focalLen     // Catch:{ Exception -> 0x03e6 }
            r2.focalLen(r3)     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2Manager$CameraStatesListener r2 = r0.mCameraStatesListener     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r3 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Float r3 = r3.focusdistance     // Catch:{ Exception -> 0x03e6 }
            r2.focusDistance(r3)     // Catch:{ Exception -> 0x03e6 }
        L_0x0451:
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2Manager$CameraStatesListener r2 = r0.mCameraStatesListener     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r3 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r3 = r3.afstate     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r4 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r4 = r4.aestate     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r5 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r5 = r5.awbstate     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r6 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r6 = r6.flashstate     // Catch:{ Exception -> 0x03e6 }
            r2.states(r3, r4, r5, r6)     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2Manager$CameraStatesListener r2 = r0.mCameraStatesListener     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r3 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r3 = r3.lenstate     // Catch:{ Exception -> 0x03e6 }
            r2.lenState(r3)     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2Manager$CameraStatesListener r2 = r0.mCameraStatesListener     // Catch:{ Exception -> 0x03e6 }
            r2.aelocked(r14)     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2Manager$CameraStatesListener r2 = r0.mCameraStatesListener     // Catch:{ Exception -> 0x03e6 }
            r2.awblocked(r15)     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2Manager$CameraStatesListener r2 = r0.mCameraStatesListener     // Catch:{ Exception -> 0x03e6 }
            r2.aftrigger(r8)     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2Manager$CameraStatesListener r2 = r0.mCameraStatesListener     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r3 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r3 = r3.afmode     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r4 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r4 = r4.aemode     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r5 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r5 = r5.awbmode     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r6 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r6 = r6.flashmode     // Catch:{ Exception -> 0x03e6 }
            r0 = r18
            com.freevisiontech.cameralib.impl.Camera2.Camera2States r7 = r0.mCamera2States     // Catch:{ Exception -> 0x03e6 }
            java.lang.Integer r7 = r7.controlmode     // Catch:{ Exception -> 0x03e6 }
            r2.modes(r3, r4, r5, r6, r7)     // Catch:{ Exception -> 0x03e6 }
            goto L_0x0240
        */
        throw new UnsupportedOperationException("Method not decompiled: com.freevisiontech.cameralib.impl.Camera2.Camera2Manager.doStateCallback(android.hardware.camera2.TotalCaptureResult):void");
    }

    /* access modifiers changed from: private */
    public void process(CaptureResult result) {
        switch (this.mState) {
            case 1:
                Integer afState = (Integer) result.get(CaptureResult.CONTROL_AF_STATE);
                if (afState == null) {
                    return;
                }
                if (4 == afState.intValue() || 5 == afState.intValue()) {
                    this.mWait4focusLock.release();
                    this.mState = 0;
                    CameraUtils.LogV("AutoFocus", "**********af locked*************************");
                    if (this.mAutoFocusListener != null) {
                        this.mAutoFocusListener.focusLocked();
                        return;
                    }
                    return;
                }
                return;
            case 2:
                Integer afState2 = (Integer) result.get(CaptureResult.CONTROL_AF_STATE);
                if (afState2 != null && 4 != afState2.intValue() && 5 != afState2.intValue()) {
                    this.mWait4focusLock.release();
                    this.mState = 0;
                    CameraUtils.LogV("AutoFocus", "**********af unlocked*************************");
                    if (this.mAutoFocusListener != null) {
                        this.mAutoFocusListener.focusUnlocked();
                        return;
                    }
                    return;
                }
                return;
            case 3:
                Integer aeState = (Integer) result.get(CaptureResult.CONTROL_AE_STATE);
                if (aeState == null || aeState.intValue() == 5 || aeState.intValue() == 4) {
                    this.mState = 4;
                    return;
                }
                return;
            default:
                return;
        }
    }

    private static class ImageSaverHelper implements Runnable {
        private TakePictureListener mCallback = null;
        private Image mImage = null;

        ImageSaverHelper(Image image, TakePictureListener callback) {
            this.mImage = image;
            this.mCallback = callback;
        }

        public void run() {
            if (this.mImage != null) {
                try {
                    ByteBuffer buffer = this.mImage.getPlanes()[0].getBuffer();
                    byte[] bytes = new byte[buffer.remaining()];
                    buffer.get(bytes);
                    if (this.mCallback != null) {
                        this.mCallback.onPictureTaken(bytes);
                    }
                    this.mImage.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    CameraUtils.LogV("Pictures", "ImageSaverHelper exception");
                    if (this.mCallback != null) {
                        this.mCallback.onPictureTakenError(10002);
                    }
                }
            }
        }
    }

    public Camera2Manager(Activity ctx, FVCamera2Manager parent, CameraParameters parameters) {
        this.context = ctx;
        this.fvCameraManager = parent;
        if (parameters == null) {
            this.mCameraParameters = new CameraParameters();
        } else {
            this.mCameraParameters = parameters;
        }
    }

    public boolean destroy() {
        if (!stopCamera()) {
            return false;
        }
        stopBackgroundThread();
        this.isPreviewListenerEnabled = false;
        this.isPreviewListenerEnabledFromRecord = false;
        this.stillimageReader = null;
        return true;
    }

    public void setPreviewCallback(PreviewCallback previewCallback) {
        this.mPreviewCallback = previewCallback;
    }

    public boolean enablePreviewListener() {
        if (this.isPreviewListenerEnabled) {
            return true;
        }
        if (this.isPreviewListenerEnabledFromRecord) {
            this.isPreviewListenerEnabled = true;
        }
        if (!isCameraOpened()) {
            return false;
        }
        try {
            this.mPreviewRequestBuilder.addTarget(this.mPreviewFrameReader.getSurface());
            setCaptureRequestRepeating();
            this.isPreviewListenerEnabled = true;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean disablePreviewListener(boolean onlyeffecttag) {
        if (!this.isPreviewListenerEnabled) {
            return true;
        }
        if (!isCameraOpened()) {
            return false;
        }
        if (onlyeffecttag) {
            this.isPreviewListenerEnabled = false;
            return true;
        }
        try {
            this.mPreviewRequestBuilder.removeTarget(this.mPreviewFrameReader.getSurface());
            setCaptureRequestRepeating();
            this.isPreviewListenerEnabled = false;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void startBackgroundThread() {
        stopBackgroundThread();
        this.mBackgroundThread = new HandlerThread("CameraBackground");
        this.mBackgroundThread.start();
        this.mBackgroundHandler = new Handler(this.mBackgroundThread.getLooper());
    }

    private void stopBackgroundThread() {
        if (this.mBackgroundThread != null) {
            this.mBackgroundThread.quitSafely();
            try {
                this.mBackgroundThread.join();
                this.mBackgroundThread = null;
                this.mBackgroundHandler = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean openCamera(int facing, int viewWidth, int viewHeight) {
        CameraManager manager;
        if (this.mPreview == null || !this.mPreview.getCameraTextureView().isAvailable()) {
            return false;
        }
        this.isunderhighspeedcapture = false;
        if (viewWidth == 0 && viewHeight == 0) {
            viewWidth = this.mPreview.getCameraTextureView().getWidth();
            viewHeight = this.mPreview.getCameraTextureView().getHeight();
        }
        if (!setCameraOutputs(facing, viewWidth, viewHeight) || !configureSurfaceViewTransform(viewWidth, viewHeight) || (manager = (CameraManager) this.context.getSystemService("camera")) == null) {
            return false;
        }
        try {
            if (!this.mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                return false;
            }
            manager.openCamera(this.mCameraId, this.mDeviceStateCallback, this.mBackgroundHandler);
            return true;
        } catch (SecurityException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e2) {
            e2.printStackTrace();
            return false;
        } catch (IllegalArgumentException e3) {
            e3.printStackTrace();
            return false;
        } catch (CameraAccessException e4) {
            e4.printStackTrace();
            return false;
        }
    }

    public boolean stopCamera() {
        try {
            this.mCameraOpenCloseLock.acquire();
            if (this.mPreviewSession != null) {
                this.mPreviewSession.close();
                this.mPreviewSession = null;
            }
            if (this.mCameraDevice != null) {
                this.mCameraDevice.close();
                this.mCameraDevice = null;
            }
            if (this.mPreviewFrameReader != null) {
                this.mPreviewFrameReader.close();
                this.mPreviewFrameReader = null;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            this.mCameraOpenCloseLock.release();
            this.mtakepicturelock.release();
            this.mWait4focusLock.release();
        }
    }

    private void preOpenCamera() {
        startBackgroundThread();
        this.mCamera2States.clear();
        this.stillimageReader = null;
        this.isunderhighspeedcapture = false;
        this.isPreviewListenerEnabled = false;
    }

    public boolean startCamera(int facing, int viewWidth, int viewHeight) {
        preOpenCamera();
        if (openCamera(facing, viewWidth, viewHeight)) {
            return true;
        }
        return false;
    }

    private boolean closeCameraPreviewSession(boolean isstoprepeating) {
        if (this.mPreviewSession != null) {
            if (isstoprepeating) {
                try {
                    this.mPreviewSession.stopRepeating();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                this.mPreviewSession.close();
                this.mPreviewSession = null;
            } catch (IllegalStateException e2) {
                e2.printStackTrace();
                this.mPreviewSession = null;
            }
        }
        return true;
    }

    /* access modifiers changed from: private */
    public void configPreviewCamera() {
        if (this.mCamera2States.afmode == null) {
            initPreviewCamera();
        } else {
            resumePreviewCamera();
        }
    }

    private void resumePreviewCamera() {
        if (this.mCamera2States.controlmode != null) {
            this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_MODE, this.mCamera2States.controlmode);
        }
        if (this.mCamera2States.afmode != null) {
            this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, this.mCamera2States.afmode);
        }
        if (this.mCamera2States.aemode != null) {
            this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, this.mCamera2States.aemode);
        }
        if (this.mCamera2States.awbmode != null) {
            this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AWB_MODE, this.mCamera2States.awbmode);
        }
        if (this.mCamera2States.flashmode != null) {
            this.mPreviewRequestBuilder.set(CaptureRequest.FLASH_MODE, this.mCamera2States.flashmode);
        }
        if (this.mCamera2States.awbstate != null) {
            if (isAWBLocked(0)) {
                this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AWB_LOCK, true);
            } else {
                this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AWB_LOCK, false);
            }
        }
        if (this.mCamera2States.aestate != null) {
            if (isAutoExposureLock(0)) {
                this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_LOCK, true);
            } else {
                this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_LOCK, false);
            }
        }
        if (this.mCamera2States.afstate != null && isFocusLocked(0)) {
            this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, 1);
        }
        if (this.mCamera2States.iso != null) {
            this.mPreviewRequestBuilder.set(CaptureRequest.SENSOR_SENSITIVITY, this.mCamera2States.iso);
        }
        if (this.mCamera2States.exposuretime != null) {
            this.mPreviewRequestBuilder.set(CaptureRequest.SENSOR_EXPOSURE_TIME, this.mCamera2States.exposuretime);
        }
        if (this.mCamera2States.focusdistance != null) {
            this.mPreviewRequestBuilder.set(CaptureRequest.LENS_FOCUS_DISTANCE, this.mCamera2States.focusdistance);
        }
        if (this.mCamera2States.f1074ev != null) {
            this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION, this.mCamera2States.f1074ev);
        }
        if (this.mCamera2States.zoom != null) {
            this.mPreviewRequestBuilder.set(CaptureRequest.SCALER_CROP_REGION, this.mCamera2States.zoom);
        }
        if (this.mCamera2States.opticalvideostabmode != null) {
            this.mPreviewRequestBuilder.set(CaptureRequest.LENS_OPTICAL_STABILIZATION_MODE, this.mCamera2States.opticalvideostabmode);
        }
        if (this.mCamera2States.digitalvideostabmode != null) {
            this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_VIDEO_STABILIZATION_MODE, this.mCamera2States.digitalvideostabmode);
        }
    }

    private void initPreviewCamera() {
        int face = getFacing();
        this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_MODE, Integer.valueOf(this.mCameraParameters.getControlMode(face)));
        this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, Integer.valueOf(this.mCameraParameters.getAfMode(face)));
        this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AWB_MODE, Integer.valueOf(this.mCameraParameters.getAwbMode(face)));
        this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_SCENE_MODE, Integer.valueOf(this.mCameraParameters.getSceneMode(face)));
        this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AWB_LOCK, false);
        this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_LOCK, false);
        if (isFlashSupported()) {
            switch (this.mCameraParameters.getFlashMode(face)) {
                case 0:
                    this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, 1);
                    this.mPreviewRequestBuilder.set(CaptureRequest.FLASH_MODE, 0);
                    return;
                case 1:
                    this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, 3);
                    return;
                case 2:
                    this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, 1);
                    this.mPreviewRequestBuilder.set(CaptureRequest.FLASH_MODE, 2);
                    return;
                case 3:
                    this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, 2);
                    return;
                case 4:
                    this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, 4);
                    return;
                default:
                    return;
            }
        } else {
            this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, 1);
        }
    }

    /* access modifiers changed from: private */
    public boolean createCameraPreviewSession(boolean isstoprepeating, final boolean isopencamera) {
        if (this.mCameraDevice == null || this.mPreview == null) {
            return false;
        }
        if (!this.mPreview.getCameraTextureView().isAvailable() || this.mPreviewSize == null) {
            return false;
        }
        try {
            closeCameraPreviewSession(isstoprepeating);
            SurfaceTexture texture = this.mPreview.getCameraTextureView().getSurfaceTexture();
            if ($assertionsDisabled || texture != null) {
                texture.setDefaultBufferSize(this.mPreviewSize.getWidth(), this.mPreviewSize.getHeight());
                Surface previewSurface = new Surface(texture);
                this.mPreviewRequestBuilder = this.mCameraDevice.createCaptureRequest(1);
                this.mPreviewRequestBuilder.addTarget(previewSurface);
                if (this.isPreviewListenerEnabled) {
                    CameraUtils.LogV("isPreviewListenerEnabled", "isPreviewListenerEnabled true");
                    this.mPreviewRequestBuilder.addTarget(this.mPreviewFrameReader.getSurface());
                } else {
                    CameraUtils.LogV("isPreviewListenerEnabled", "isPreviewListenerEnabled false");
                }
                this.mCameraDevice.createCaptureSession(Arrays.asList(new Surface[]{previewSurface, this.mPreviewFrameReader.getSurface()}), new CameraCaptureSession.StateCallback() {
                    /* JADX INFO: finally extract failed */
                    public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                        if (Camera2Manager.this.mCameraDevice != null) {
                            CameraCaptureSession unused = Camera2Manager.this.mPreviewSession = cameraCaptureSession;
                            try {
                                Camera2Manager.this.configPreviewCamera();
                                Camera2Manager.this.mPreviewSession.setRepeatingRequest(Camera2Manager.this.mPreviewRequestBuilder.build(), Camera2Manager.this.mPreviewCaptureRequestCallback, Camera2Manager.this.mBackgroundHandler);
                                if (Camera2Manager.this.mCameraStatusListener != null && isopencamera) {
                                    Camera2Manager.this.mCameraStatusListener.onOpened(Camera2Manager.this.mCameraDevice);
                                }
                                Camera2Manager.this.releaseTakePictureLock();
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                                Camera2Manager.this.releaseTakePictureLock();
                                if (Camera2Manager.this.mCameraStatusListener != null) {
                                    Camera2Manager.this.mCameraStatusListener.onError(Camera2Manager.this.mCameraDevice, 10000);
                                }
                            } catch (Throwable th) {
                                Camera2Manager.this.releaseTakePictureLock();
                                throw th;
                            }
                        } else if (Camera2Manager.this.mCameraStatusListener != null) {
                            Camera2Manager.this.mCameraStatusListener.onError(Camera2Manager.this.mCameraDevice, 10000);
                        }
                    }

                    public void onClosed(@NonNull CameraCaptureSession session) {
                        super.onClosed(session);
                        if (Camera2Manager.this.mCameraStatusListener != null && isopencamera) {
                            Camera2Manager.this.mCameraStatusListener.onSessionClosed(session);
                        }
                    }

                    public void onActive(@NonNull CameraCaptureSession session) {
                        super.onActive(session);
                        if (Camera2Manager.this.mCameraStatusListener != null) {
                            Camera2Manager.this.mCameraStatusListener.onSessionActive(session);
                        }
                    }

                    public void onSurfacePrepared(@NonNull CameraCaptureSession session, @NonNull Surface surface) {
                        super.onSurfacePrepared(session, surface);
                        if (Camera2Manager.this.mCameraStatusListener != null) {
                            Camera2Manager.this.mCameraStatusListener.onSessionSurfacePrepared(session, surface);
                        }
                    }

                    public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                        if (Camera2Manager.this.mCameraStatusListener != null) {
                            Camera2Manager.this.mCameraStatusListener.onError(Camera2Manager.this.mCameraDevice, 10001);
                        }
                    }
                }, (Handler) null);
                CameraUtils.LogV("LogFile", "leave createCameraPreviewSession ok");
                return true;
            }
            throw new AssertionError();
        } catch (CameraAccessException e) {
            e.printStackTrace();
            if (this.mCameraStatusListener != null) {
                this.mCameraStatusListener.onError(this.mCameraDevice, 10000);
            }
            return false;
        }
    }

    private boolean configureSurfaceViewTransform(int viewWidth, int viewHeight) {
        int rotation = this.context.getWindowManager().getDefaultDisplay().getRotation();
        CameraUtils.LogV("CameraView2", "rotation=" + rotation);
        if (rotation == 0) {
            return false;
        }
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0.0f, 0.0f, (float) viewWidth, (float) viewHeight);
        RectF bufferRect = new RectF(0.0f, 0.0f, (float) this.mPreviewSize.getHeight(), (float) this.mPreviewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (1 == rotation || 3 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(((float) viewHeight) / ((float) this.mPreviewSize.getHeight()), ((float) viewWidth) / ((float) this.mPreviewSize.getWidth()));
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate((float) ((rotation - 2) * 90), centerX, centerY);
        } else if (2 == rotation) {
            matrix.postRotate(180.0f, centerX, centerY);
        }
        this.mPreview.getCameraTextureView().setTransform(matrix);
        return true;
    }

    public boolean startPreview() {
        return createCameraPreviewSession(false, false);
    }

    public boolean stopPreview() {
        return closeCameraPreviewSession(true);
    }

    private boolean setCameraOutputs(int face, int viewWidth, int viewHeight) {
        CameraManager manager = (CameraManager) this.context.getSystemService("camera");
        try {
            String[] idlist = manager.getCameraIdList();
            int length = idlist.length;
            int i = 0;
            while (i < length) {
                String cameraId = idlist[i];
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
                Integer facing = (Integer) characteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing == null || facing.intValue() != face) {
                    i++;
                } else {
                    StreamConfigurationMap map = (StreamConfigurationMap) characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                    if (map == null) {
                        return false;
                    }
                    this.mFace = face;
                    Size[] supportedpicsizes = CameraUtils.SizeArrayFromSizeArray(map.getOutputSizes(256));
                    Size[] supportedrawsizes = CameraUtils.SizeArrayFromSizeArray(map.getOutputSizes(32));
                    Size[] supportedvideosizes = CameraUtils.SizeArrayFromSizeArray(map.getOutputSizes(MediaRecorder.class));
                    Size[] supportedpreviewsizes = CameraUtils.SizeArrayFromSizeArray(map.getOutputSizes(SurfaceTexture.class));
                    this.mCameraParameters.setSupportedPictureSizes(this.mFace, supportedpicsizes);
                    this.mCameraParameters.setSupportedRawPictureSizes(this.mFace, supportedrawsizes);
                    this.mCameraParameters.setSupportedVideoSizes(this.mFace, supportedvideosizes);
                    this.mCameraParameters.setSupportedPrevewSizes(this.mFace, supportedpreviewsizes);
                    this.mPreviewSize = CameraUtils.ChooseOptimalSize(supportedpreviewsizes, viewWidth, viewHeight);
                    this.mCameraParameters.setPrevewRatio(face, AspectRatio.m1507of(this.mPreviewSize.getWidth(), this.mPreviewSize.getHeight()));
                    this.mPreviewFrameReader = ImageReader.newInstance(this.mCameraParameters.getPreviewFrameSize(face).getWidth(), this.mCameraParameters.getPreviewFrameSize(face).getHeight(), 35, 4);
                    this.mPreviewFrameReader.setOnImageAvailableListener(this.mPreivewFrameListener, this.mBackgroundHandler);
                    this.mPreview.refreshTextureView();
                    this.mCameraId = cameraId;
                    this.mHwlevel = ((Integer) characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL)).intValue();
                    return true;
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (NullPointerException e2) {
            e2.printStackTrace();
        }
        return false;
    }

    public String cameraId(int face) {
        CameraManager manager = (CameraManager) this.context.getSystemService("camera");
        try {
            for (String cameraId : manager.getCameraIdList()) {
                if (face == ((Integer) manager.getCameraCharacteristics(cameraId).get(CameraCharacteristics.LENS_FACING)).intValue()) {
                    return cameraId;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public int cameraIdByInt(int face) {
        try {
            return Integer.parseInt(cameraId(face));
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int cameraintID() {
        try {
            return Integer.parseInt(this.mCameraId);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public String cameraID() {
        return this.mCameraId;
    }

    public void setCameraStatusListener(CameraStatusListener listener) {
        this.mCameraStatusListener = listener;
    }

    public int getSensorOrientation() {
        return ((Integer) getCameraCharacteristics().get(CameraCharacteristics.SENSOR_ORIENTATION)).intValue();
    }

    public boolean isCameraOpened() {
        if (this.mCameraDevice == null || this.mPreview == null || !this.mPreview.getCameraTextureView().isAvailable()) {
            return false;
        }
        return true;
    }

    public void setPerviewView(CameraView view) {
        this.mPreview = view;
    }

    private String getCameraByFacing(int facing) {
        try {
            CameraManager manager = (CameraManager) this.context.getSystemService("camera");
            for (String cameraId : manager.getCameraIdList()) {
                Integer face = (Integer) manager.getCameraCharacteristics(cameraId).get(CameraCharacteristics.LENS_FACING);
                if (face != null && face.intValue() == facing) {
                    return cameraId;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /* access modifiers changed from: private */
    public CameraCharacteristics getCameraCharacteristics() {
        return getCameraCharacteristics(this.mCameraId);
    }

    private CameraCharacteristics getCameraCharacteristics(String cameraid) {
        if (!isCameraOpened()) {
            return null;
        }
        try {
            return ((CameraManager) this.context.getSystemService("camera")).getCameraCharacteristics(cameraid);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private StreamConfigurationMap getStreamConfigurationMap(String cameraid) {
        try {
            return (StreamConfigurationMap) getCameraCharacteristics(cameraid).get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private CaptureRequest getPreviewCaptureRequest() {
        if (isCameraOpened() && this.mPreviewRequestBuilder != null) {
            return this.mPreviewRequestBuilder.build();
        }
        return null;
    }

    /* access modifiers changed from: private */
    public int setCaptureRequestRepeating() throws Exception {
        if (!this.isunderhighspeedcapture || Build.VERSION.SDK_INT < 23) {
            return this.mPreviewSession.setRepeatingRequest(this.mPreviewRequestBuilder.build(), this.mPreviewCaptureRequestCallback, this.mBackgroundHandler);
        }
        return this.mPreviewSession.setRepeatingBurst(((CameraConstrainedHighSpeedCaptureSession) this.mPreviewSession).createHighSpeedRequestList(this.mPreviewRequestBuilder.build()), this.mPreviewHighSpeedCaptureRequestCallback, this.mBackgroundHandler);
    }

    private <T> boolean setCaptureRequestParameter(@NonNull CaptureRequest.Key<T> key, T value) {
        try {
            this.mPreviewRequestBuilder.set(key, value);
            setCaptureRequestRepeating();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private <T> boolean setCaptureRequestParameter(@NonNull CaptureRequest.Key<T>[] keys, T[] values) {
        if (keys.length != values.length) {
            return false;
        }
        int i = 0;
        while (i < keys.length) {
            try {
                this.mPreviewRequestBuilder.set(keys[i], values[i]);
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        setCaptureRequestRepeating();
        return true;
    }

    public CameraDevice getCamera() {
        return this.mCameraDevice;
    }

    public int getCameraRotation() {
        return 0;
    }

    public boolean setCameraRotation(int angle) {
        return true;
    }

    public int getCameraHWLevel() {
        try {
            return ((Integer) getCameraCharacteristics().get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL)).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean isSupportManualMode() {
        if (getCameraHWLevel() == 2) {
            return false;
        }
        return true;
    }

    public int[] getCameraAvailableCapability() {
        try {
            getCameraCharacteristics().getAvailableCaptureRequestKeys();
            return (int[]) getCameraCharacteristics().get(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<CaptureRequest.Key<?>> getAvailableCaptureRequestKeys() {
        try {
            return getCameraCharacteristics().getAvailableCaptureRequestKeys();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isCaptureRequestKeySupported(CaptureRequest.Key<?> key) {
        for (CaptureRequest.Key<?> k : getAvailableCaptureRequestKeys()) {
            if (k == key) {
                return true;
            }
        }
        return false;
    }

    private boolean isCapabilityAvailable(int capability) {
        int[] ca = getCameraAvailableCapability();
        if (ca == null) {
            return false;
        }
        for (int i : ca) {
            if (i == capability) {
                return true;
            }
        }
        return false;
    }

    public boolean isSupportManualSensor() {
        return isCapabilityAvailable(1);
    }

    public boolean isSupportManualPostProcess() {
        return isCapabilityAvailable(3);
    }

    public boolean isSupportRaw() {
        return isCapabilityAvailable(2);
    }

    private boolean aquireTakePicturelock() {
        CameraUtils.LogV("Pictures", "aquire TakePicturelock");
        try {
            return this.mtakepicturelock.tryAcquire(1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    /* access modifiers changed from: private */
    public void releaseTakePictureLock() {
        CameraUtils.LogV("Pictures", "release TakePictureLock");
        this.mtakepicturelock.release();
    }

    public void setTakePictureListener(TakePictureListener listener) {
        this.mTakePictureListener = listener;
    }

    public boolean takePicture(int pictureOrient, Size pictureSize, Location location, int phototype) {
        if (phototype == 1 || phototype == 2) {
            return captureStillRawPicture(pictureOrient, pictureSize, location, phototype);
        }
        return captureStillPicture(pictureOrient, pictureSize, location);
    }

    private void runPrecaptureSequence() {
        try {
            this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER, 1);
            this.mState = 3;
            this.mPreviewSession.capture(this.mPreviewRequestBuilder.build(), this.mPreviewCaptureRequestCallback, this.mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void setCaptureStillRequestConfig(CaptureRequest.Builder captureStillBuilder) {
        if (this.mCamera2States.controlmode != null) {
            captureStillBuilder.set(CaptureRequest.CONTROL_MODE, this.mCamera2States.controlmode);
        }
        if (this.mCamera2States.afmode != null) {
            captureStillBuilder.set(CaptureRequest.CONTROL_AF_MODE, this.mCamera2States.afmode);
        }
        if (this.mCamera2States.aemode != null) {
            captureStillBuilder.set(CaptureRequest.CONTROL_AE_MODE, this.mCamera2States.aemode);
        }
        if (this.mCamera2States.awbmode != null) {
            captureStillBuilder.set(CaptureRequest.CONTROL_AWB_MODE, this.mCamera2States.awbmode);
        }
        if (this.mCamera2States.flashmode != null) {
            captureStillBuilder.set(CaptureRequest.FLASH_MODE, this.mCamera2States.flashmode);
        }
        if (this.mCamera2States.iso != null) {
            captureStillBuilder.set(CaptureRequest.SENSOR_SENSITIVITY, this.mCamera2States.iso);
        }
        if (this.mCamera2States.exposuretime != null) {
            captureStillBuilder.set(CaptureRequest.SENSOR_EXPOSURE_TIME, this.mCamera2States.exposuretime);
        }
        if (this.mCamera2States.focusdistance != null) {
            captureStillBuilder.set(CaptureRequest.LENS_FOCUS_DISTANCE, this.mCamera2States.focusdistance);
        }
        if (this.mCamera2States.f1074ev != null) {
            captureStillBuilder.set(CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION, this.mCamera2States.f1074ev);
        }
        if (this.mCamera2States.zoom != null) {
            CameraUtils.LogV("TakePhoto", "zoom=" + this.mCamera2States.zoom);
            captureStillBuilder.set(CaptureRequest.SCALER_CROP_REGION, this.mCamera2States.zoom);
        }
        if (this.mCamera2States.awbstate != null) {
            if (isAWBLocked(0)) {
                captureStillBuilder.set(CaptureRequest.CONTROL_AWB_LOCK, true);
            } else {
                captureStillBuilder.set(CaptureRequest.CONTROL_AWB_LOCK, false);
            }
        }
        if (this.mCamera2States.aestate != null) {
            if (isAutoExposureLock(0)) {
                captureStillBuilder.set(CaptureRequest.CONTROL_AE_LOCK, true);
            } else {
                captureStillBuilder.set(CaptureRequest.CONTROL_AE_LOCK, false);
            }
        }
        if (this.mCamera2States.afstate != null && isFocusLocked(0)) {
            captureStillBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, 1);
        }
        captureStillBuilder.set(CaptureRequest.JPEG_QUALITY, Byte.valueOf(Camera2Constants.JPEG_QUALITY));
        if (this.mCamera2States.opticalvideostabmode != null) {
            captureStillBuilder.set(CaptureRequest.LENS_OPTICAL_STABILIZATION_MODE, this.mCamera2States.opticalvideostabmode);
        }
        if (this.mCamera2States.digitalvideostabmode != null) {
            captureStillBuilder.set(CaptureRequest.CONTROL_VIDEO_STABILIZATION_MODE, this.mCamera2States.digitalvideostabmode);
        }
    }

    private boolean captureStillPicture(int pictureOrient, Size pictureSize, Location location) {
        if (!isCameraOpened() || !aquireTakePicturelock()) {
            return false;
        }
        try {
            CameraUtils.LogV("Pictures", "prepare to close preview");
            closeCameraPreviewSession(false);
            boolean rebuildimagereader = false;
            if (this.stillimageReader == null) {
                rebuildimagereader = true;
            } else if (!(this.stillimageReader.getWidth() == pictureSize.getWidth() && this.stillimageReader.getHeight() == pictureSize.getHeight())) {
                rebuildimagereader = true;
            }
            if (rebuildimagereader) {
                CameraUtils.LogV("Pictures", "rebuild ImageReader +++++++++++++");
                this.stillimageReader = ImageReader.newInstance(pictureSize.getWidth(), pictureSize.getHeight(), 256, 2);
                this.stillimageReader.setOnImageAvailableListener(this.stillimageAvailableListener, (Handler) null);
            }
            final CaptureRequest.Builder captureStillBuilder = this.mCameraDevice.createCaptureRequest(2);
            captureStillBuilder.addTarget(this.stillimageReader.getSurface());
            setCaptureStillRequestConfig(captureStillBuilder);
            if (location != null) {
                CameraUtils.LogV("HHBLocationProvider", "captureStillPicture " + location.getLatitude() + ":" + location.getLongitude());
                captureStillBuilder.set(CaptureRequest.JPEG_GPS_LOCATION, location);
            }
            captureStillBuilder.set(CaptureRequest.JPEG_ORIENTATION, Integer.valueOf(pictureOrient));
            SurfaceTexture texture = this.mPreview.getCameraTextureView().getSurfaceTexture();
            if ($assertionsDisabled || texture != null) {
                texture.setDefaultBufferSize(this.mPreviewSize.getWidth(), this.mPreviewSize.getHeight());
                final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                    public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                        super.onCaptureCompleted(session, request, result);
                        CameraUtils.LogV("Pictures", "onCaptureCompleted");
                        Camera2Manager.this.startPreview();
                        Camera2Manager.this.releaseTakePictureLock();
                        if (Camera2Manager.this.mTakePictureListener != null) {
                            Camera2Manager.this.mTakePictureListener.captureStillPictureCompleted();
                        }
                    }

                    public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
                        super.onCaptureFailed(session, request, failure);
                        CameraUtils.LogV("Pictures", "onCaptureFailed");
                        Camera2Manager.this.startPreview();
                        Camera2Manager.this.releaseTakePictureLock();
                        if (Camera2Manager.this.mTakePictureListener != null) {
                            Camera2Manager.this.mTakePictureListener.captureStillPictureError(failure.getReason());
                        }
                    }

                    public void onCaptureStarted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, long timestamp, long frameNumber) {
                        super.onCaptureStarted(session, request, timestamp, frameNumber);
                        CameraUtils.LogV("Pictures", "onCaptureStarted");
                    }

                    public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
                        super.onCaptureProgressed(session, request, partialResult);
                        CameraUtils.LogV("Pictures", "onCaptureProgressed");
                    }

                    public void onCaptureSequenceCompleted(@NonNull CameraCaptureSession session, int sequenceId, long frameNumber) {
                        super.onCaptureSequenceCompleted(session, sequenceId, frameNumber);
                        CameraUtils.LogV("Pictures", "onCaptureSequenceCompleted");
                    }

                    public void onCaptureSequenceAborted(@NonNull CameraCaptureSession session, int sequenceId) {
                        super.onCaptureSequenceAborted(session, sequenceId);
                        CameraUtils.LogV("Pictures", "onCaptureSequenceAborted");
                    }

                    public void onCaptureBufferLost(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull Surface target, long frameNumber) {
                        super.onCaptureBufferLost(session, request, target, frameNumber);
                        CameraUtils.LogV("Pictures", "onCaptureBufferLost");
                    }
                };
                this.mCameraDevice.createCaptureSession(Arrays.asList(new Surface[]{new Surface(texture), this.stillimageReader.getSurface()}), new CameraCaptureSession.StateCallback() {
                    public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                        CameraCaptureSession unused = Camera2Manager.this.mPreviewSession = cameraCaptureSession;
                        try {
                            CameraUtils.LogV("Pictures", "createCaptureSession onConfigured");
                            Camera2Manager.this.mPreviewSession.capture(captureStillBuilder.build(), captureListener, Camera2Manager.this.mBackgroundHandler);
                        } catch (CameraAccessException e) {
                            CameraUtils.LogV("Pictures", "createCaptureSession onConfigured CameraAccessException " + e.getMessage());
                            Camera2Manager.this.releaseTakePictureLock();
                            if (Camera2Manager.this.mTakePictureListener != null) {
                                Camera2Manager.this.mTakePictureListener.captureStillPictureError(10000);
                            }
                            Camera2Manager.this.startPreview();
                        }
                    }

                    public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                        CameraUtils.LogV("Pictures", "createCaptureSession onConfigureFailed");
                        Camera2Manager.this.releaseTakePictureLock();
                        if (Camera2Manager.this.mTakePictureListener != null) {
                            Camera2Manager.this.mTakePictureListener.captureStillPictureError(10001);
                        }
                    }
                }, this.mBackgroundHandler);
                return true;
            }
            throw new AssertionError();
        } catch (Exception e) {
            CameraUtils.LogV("Pictures", "createCaptureSession Exception " + e.getMessage());
            releaseTakePictureLock();
            if (this.mTakePictureListener == null) {
                return false;
            }
            this.mTakePictureListener.captureStillPictureError(10000);
            return false;
        }
    }

    private abstract class RawImageAvailableListener implements ImageReader.OnImageAvailableListener {
        public CaptureResult mCaptureResult;

        private RawImageAvailableListener() {
        }
    }

    private boolean captureStillRawPicture(int pictureOrient, Size pictureSize, Location location, int rawpicturetype) {
        CameraUtils.LogV("RawPictures", "prepare to capture Raw Picture " + pictureSize.getWidth() + "x" + pictureSize.getHeight());
        if (!isCameraOpened() || !aquireTakePicturelock()) {
            return false;
        }
        try {
            CameraUtils.LogV("RawPictures", "prepare to close preview");
            closeCameraPreviewSession(false);
            boolean rebuildRawimagereader = false;
            if (this.rawImageReader == null) {
                rebuildRawimagereader = true;
            } else if (!(this.rawImageReader.getWidth() == pictureSize.getWidth() && this.rawImageReader.getHeight() == pictureSize.getHeight())) {
                rebuildRawimagereader = true;
            }
            if (rebuildRawimagereader) {
                CameraUtils.LogV("RawPictures", "rebuild ImageReader +++++++++++++");
                this.rawImageReader = ImageReader.newInstance(pictureSize.getWidth(), pictureSize.getHeight(), 32, 2);
                this.rawImageReader.setOnImageAvailableListener(this.rawimageAvailableListener, (Handler) null);
            }
            if (rawpicturetype == 2) {
                boolean rebuildstillimagereader = false;
                if (this.stillimageReader == null) {
                    rebuildstillimagereader = true;
                } else if (!(this.stillimageReader.getWidth() == pictureSize.getWidth() && this.stillimageReader.getHeight() == pictureSize.getHeight())) {
                    rebuildstillimagereader = true;
                }
                if (rebuildstillimagereader) {
                    this.stillimageReader = ImageReader.newInstance(pictureSize.getWidth(), pictureSize.getHeight(), 256, 2);
                    this.stillimageReader.setOnImageAvailableListener(this.stillimageAvailableListener, (Handler) null);
                }
            }
            final CaptureRequest.Builder captureStillBuilder = this.mCameraDevice.createCaptureRequest(2);
            captureStillBuilder.addTarget(this.rawImageReader.getSurface());
            if (rawpicturetype == 2) {
                captureStillBuilder.addTarget(this.stillimageReader.getSurface());
            }
            setCaptureStillRequestConfig(captureStillBuilder);
            captureStillBuilder.set(CaptureRequest.JPEG_ORIENTATION, Integer.valueOf(pictureOrient));
            if (location != null) {
                CameraUtils.LogV("HHBLocationProvider", "captureStillPicture " + location.getLatitude() + ":" + location.getLongitude());
                captureStillBuilder.set(CaptureRequest.JPEG_GPS_LOCATION, location);
            }
            SurfaceTexture texture = this.mPreview.getCameraTextureView().getSurfaceTexture();
            if ($assertionsDisabled || texture != null) {
                texture.setDefaultBufferSize(this.mPreviewSize.getWidth(), this.mPreviewSize.getHeight());
                final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                    public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                        super.onCaptureCompleted(session, request, result);
                        CameraUtils.LogV("RawPictures", "onCaptureCompleted");
                        Camera2Manager.this.rawimageAvailableListener.mCaptureResult = result;
                        Camera2Manager.this.startPreview();
                        Camera2Manager.this.releaseTakePictureLock();
                        if (Camera2Manager.this.mTakePictureListener != null) {
                            Camera2Manager.this.mTakePictureListener.captureStillPictureCompleted();
                        }
                    }

                    public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
                        super.onCaptureFailed(session, request, failure);
                        CameraUtils.LogV("RawPictures", "onCaptureFailed");
                        Camera2Manager.this.startPreview();
                        Camera2Manager.this.releaseTakePictureLock();
                        if (Camera2Manager.this.mTakePictureListener != null) {
                            Camera2Manager.this.mTakePictureListener.captureStillPictureError(failure.getReason());
                        }
                    }

                    public void onCaptureStarted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, long timestamp, long frameNumber) {
                        super.onCaptureStarted(session, request, timestamp, frameNumber);
                        CameraUtils.LogV("RawPictures", "onCaptureStarted");
                    }

                    public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
                        super.onCaptureProgressed(session, request, partialResult);
                        CameraUtils.LogV("RawPictures", "onCaptureProgressed");
                    }

                    public void onCaptureSequenceCompleted(@NonNull CameraCaptureSession session, int sequenceId, long frameNumber) {
                        super.onCaptureSequenceCompleted(session, sequenceId, frameNumber);
                        CameraUtils.LogV("RawPictures", "onCaptureSequenceCompleted");
                    }

                    public void onCaptureSequenceAborted(@NonNull CameraCaptureSession session, int sequenceId) {
                        super.onCaptureSequenceAborted(session, sequenceId);
                        CameraUtils.LogV("RawPictures", "onCaptureSequenceAborted");
                    }

                    public void onCaptureBufferLost(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull Surface target, long frameNumber) {
                        super.onCaptureBufferLost(session, request, target, frameNumber);
                        CameraUtils.LogV("RawPictures", "onCaptureBufferLost");
                    }
                };
                List<Surface> surfacelist = new ArrayList<>();
                surfacelist.add(new Surface(texture));
                surfacelist.add(this.rawImageReader.getSurface());
                if (rawpicturetype == 2) {
                    surfacelist.add(this.stillimageReader.getSurface());
                }
                this.mCameraDevice.createCaptureSession(surfacelist, new CameraCaptureSession.StateCallback() {
                    public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                        CameraCaptureSession unused = Camera2Manager.this.mPreviewSession = cameraCaptureSession;
                        try {
                            CameraUtils.LogV("RawPictures", "createCaptureSession onConfigured");
                            Camera2Manager.this.mPreviewSession.capture(captureStillBuilder.build(), captureListener, Camera2Manager.this.mBackgroundHandler);
                        } catch (CameraAccessException e) {
                            CameraUtils.LogV("RawPictures", "createCaptureSession onConfigured CameraAccessException " + e.getMessage());
                            Camera2Manager.this.releaseTakePictureLock();
                            if (Camera2Manager.this.mTakePictureListener != null) {
                                Camera2Manager.this.mTakePictureListener.captureStillPictureError(10000);
                            }
                            Camera2Manager.this.startPreview();
                        }
                    }

                    public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                        CameraUtils.LogV("RawPictures", "createCaptureSession onConfigureFailed");
                        Camera2Manager.this.releaseTakePictureLock();
                        if (Camera2Manager.this.mTakePictureListener != null) {
                            Camera2Manager.this.mTakePictureListener.captureStillPictureError(10001);
                        }
                    }
                }, this.mBackgroundHandler);
                return true;
            }
            throw new AssertionError();
        } catch (Exception e) {
            CameraUtils.LogV("RawPictures", "createCaptureSession Exception " + e.getMessage());
            releaseTakePictureLock();
            if (this.mTakePictureListener != null) {
                this.mTakePictureListener.captureStillPictureError(10000);
            }
            return false;
        }
    }

    public int getControMode() {
        return getControMode(0);
    }

    public int getControMode(int modestyle) {
        if (modestyle == 0) {
            return CameraUtils.Intege2int(this.mCamera2States.controlmode);
        }
        try {
            return ((Integer) this.mPreviewRequestBuilder.get(CaptureRequest.CONTROL_MODE)).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean setControlMode(int mode) {
        return setCaptureRequestParameter(CaptureRequest.CONTROL_MODE, Integer.valueOf(mode));
    }

    public void setAutoFocusListener(AutoFocusListener listener) {
        this.mAutoFocusListener = listener;
    }

    public int[] getAvailableAFMode() {
        try {
            return (int[]) getCameraCharacteristics().get(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getFocusMode() {
        if (Build.VERSION.SDK_INT >= 21) {
            return getFocusMode(0);
        }
        return getFocusMode(1);
    }

    public int getFocusMode(int modestyle) {
        if (modestyle == 0) {
            return CameraUtils.Intege2int(this.mCamera2States.afmode);
        }
        try {
            return ((Integer) this.mPreviewRequestBuilder.get(CaptureRequest.CONTROL_AF_MODE)).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean setAutoFocusMode() {
        return enableAFControl();
    }

    public boolean setFocusMode(int mode) {
        if (getFocusMode() == mode) {
            return true;
        }
        if (mode == 0) {
            if (getControMode() != 0 && !setControlMode(0)) {
                return false;
            }
        } else if (getControMode() != 1 && !setControlMode(1)) {
            return false;
        }
        return setCaptureRequestParameter(CaptureRequest.CONTROL_AF_MODE, Integer.valueOf(mode));
    }

    public boolean setFocusModeEx(int mode) {
        if (mode != 0) {
            this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_MODE, 1);
        }
        try {
            this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, Integer.valueOf(mode));
            setCaptureRequestRepeating();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isAutoFocusMode() {
        if (getControMode() == 0 || getFocusMode() == 0) {
            return false;
        }
        return true;
    }

    public boolean enableMFControl() {
        if (isMFEnabled()) {
            return true;
        }
        this.mCameraParameters.setAfMode(getFacing(), getFocusMode());
        return setFocusModeEx(0);
    }

    public boolean enableAFControl() {
        if (isAutoFocusMode()) {
            return true;
        }
        return setFocusModeEx(this.mCameraParameters.getAfMode(getFacing()));
    }

    public boolean isMFEnabled() {
        return getFocusMode() == 0;
    }

    private boolean aquireWait4focusLock() {
        return true;
    }

    public boolean lockFocus() {
        if (!aquireWait4focusLock()) {
            return false;
        }
        this.mState = 1;
        try {
            this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, 1);
            this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER, 1);
            setCaptureRequestRepeating();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            this.mWait4focusLock.release();
            this.mState = 0;
            return false;
        }
    }

    public boolean isFocusLocked(int modestyle) {
        if (modestyle == 0 && CameraUtils.Intege2int(this.mCamera2States.afstate) == 4) {
            return true;
        }
        return false;
    }

    public boolean unlockFocus() {
        if (!aquireWait4focusLock()) {
            return false;
        }
        this.mState = 2;
        if (setCaptureRequestParameter(CaptureRequest.CONTROL_AF_TRIGGER, 2)) {
            return true;
        }
        this.mWait4focusLock.release();
        this.mState = 0;
        return false;
    }

    public int maxAutofocusRegion() {
        try {
            return ((Integer) getCameraCharacteristics().get(CameraCharacteristics.CONTROL_MAX_REGIONS_AF)).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean isSupportAutoFocus() {
        if (maxAutofocusRegion() == 0) {
            return false;
        }
        return true;
    }

    public boolean cancelAutoFocusLock() {
        if (!aquireWait4focusLock()) {
            CameraUtils.LogV("AutoFocus", "cancelAutoFocusLock:aquireWait4focusLock failed");
            return false;
        }
        int ttt = this.mCameraParameters.getAfMode(getFacing());
        boolean result = setFocusModeEx(this.mCameraParameters.getAfMode(getFacing()));
        if (result) {
            CameraUtils.LogV("AutoFocus", "suc to return to af mode " + ttt);
        } else {
            CameraUtils.LogV("AutoFocus", "failed to return to af mode " + ttt);
        }
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            public void run() {
                CameraUtils.LogV("AutoFocus", "before cancelAutoFocusLock focus mode = " + Camera2Manager.this.getFocusMode() + ":control mode = " + Camera2Manager.this.getControMode());
                int unused = Camera2Manager.this.mState = 2;
                try {
                    Camera2Manager.this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, Integer.valueOf(Camera2Manager.this.mCameraParameters.getAfMode(Camera2Manager.this.getFacing())));
                    Camera2Manager.this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, 2);
                    Camera2Manager.this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER, 2);
                    int unused2 = Camera2Manager.this.setCaptureRequestRepeating();
                } catch (Exception e) {
                    e.printStackTrace();
                    Camera2Manager.this.mWait4focusLock.release();
                    int unused3 = Camera2Manager.this.mState = 0;
                }
            }
        }, 500);
        return result;
    }

    public boolean autoFocusLock(final Rect focusarea, final boolean islockae) {
        if (!isSupportAutoFocus()) {
            CameraUtils.LogV("HHBAutoFocus", "isSupportAutoFocus false");
            return false;
        } else if (!aquireWait4focusLock()) {
            CameraUtils.LogV("HHBAutoFocus", "autoFocusLock:aquireWait4focusLock failed");
            return false;
        } else {
            this.mState = 1;
            if (this.mCamera2States.afstate.intValue() == 4 || this.mCamera2States.afstate.intValue() == 5 || this.mCamera2States.afstate.intValue() == 3 || this.mCamera2States.afstate.intValue() == 0 || this.mCamera2States.afstate.intValue() == 6) {
                CameraUtils.LogV("HHBAutoFocus", "2.before autoFocusLock CONTROL_AF_TRIGGER_CANCEL first ,af state = " + this.mCamera2States.afstate);
                try {
                    this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, 2);
                    this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER, 2);
                    setCaptureRequestRepeating();
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        public void run() {
                            CameraUtils.LogV("HHBAutoFocus", "3. after  CONTROL_AF_TRIGGER_CANCEL first,start to CONTROL_AF_TRIGGER_START " + Camera2Manager.this.mCamera2States.afstate);
                            boolean unused = Camera2Manager.this.autoFocusLockInternal(focusarea, islockae);
                        }
                    }, 500);
                    return true;
                } catch (Exception e) {
                    CameraUtils.LogV("HHBAutoFocus", "cancel trigger Exception ");
                    this.mWait4focusLock.release();
                    this.mState = 0;
                    return false;
                }
            } else {
                CameraUtils.LogV("HHBAutoFocus", "1. before autoFocusLock focus mode = " + getFocusMode() + ":control mode = " + getControMode() + "; af state =" + this.mCamera2States.afstate);
                return autoFocusLockInternal(focusarea, islockae);
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean autoFocusLockInternal(Rect focusarea, boolean islockae) {
        try {
            this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_REGIONS, new MeteringRectangle[]{new MeteringRectangle(focusarea, 1000)});
            if (islockae && maxAutoExposureRegion() > 0) {
                CameraUtils.LogV("HHBAutoFocus", "control ae region is set");
                this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_REGIONS, new MeteringRectangle[]{new MeteringRectangle(focusarea, 1000)});
            }
            if (!Camera2Constants.Exclude_List_AutoFocus_AutoMode.contains(Build.BRAND.toLowerCase())) {
                CameraUtils.LogV("HHBAutoFocus", "CONTROL_AF_MODE_AUTO is set ");
                this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, 1);
            } else {
                CameraUtils.LogV("HHBAutoFocus", "CONTROL_AF_MODE_AUTO is not set ");
            }
            this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, 1);
            this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER, 1);
            setCaptureRequestRepeating();
            return true;
        } catch (Exception e) {
            CameraUtils.LogV("HHBAutoFocus", "autoFocusLock Exception ");
            e.printStackTrace();
            this.mWait4focusLock.release();
            this.mState = 0;
            return false;
        }
    }

    public Rect screenPoint2FocusRegion(double x, double y) {
        double imgScale;
        double imgScale2;
        double horizontalOffset;
        double verticalOffset;
        try {
            int viewWidth = this.mPreview.getCameraTextureView().getWidth();
            int viewHeight = this.mPreview.getCameraTextureView().getHeight();
            int realPreviewWidth = this.mPreviewSize.getWidth();
            int realPreviewHeight = this.mPreviewSize.getHeight();
            int senseOrientation = CameraUtils.GetRotateDegree(getCameraCharacteristics(), this.context);
            if (90 == senseOrientation || 270 == senseOrientation) {
                realPreviewWidth = this.mPreviewSize.getHeight();
                realPreviewHeight = this.mPreviewSize.getWidth();
            }
            double verticalOffset2 = 0.0d;
            double horizontalOffset2 = 0.0d;
            if (realPreviewHeight * viewWidth > realPreviewWidth * viewHeight) {
                imgScale = (((double) viewWidth) * 1.0d) / ((double) realPreviewWidth);
                verticalOffset2 = (((double) realPreviewHeight) - (((double) viewHeight) / imgScale)) / 2.0d;
            } else {
                imgScale = (((double) viewHeight) * 1.0d) / ((double) realPreviewHeight);
                horizontalOffset2 = (((double) realPreviewWidth) - (((double) viewWidth) / imgScale)) / 2.0d;
            }
            double x2 = (x / imgScale) + horizontalOffset2;
            double y2 = (y / imgScale) + verticalOffset2;
            if (90 == senseOrientation) {
                double tmp = x2;
                x2 = y2;
                y2 = ((double) this.mPreviewSize.getHeight()) - tmp;
            } else if (270 == senseOrientation) {
                double tmp2 = x2;
                x2 = ((double) this.mPreviewSize.getWidth()) - y2;
                y2 = tmp2;
            }
            Rect cropRegion = (Rect) this.mPreviewRequestBuilder.get(CaptureRequest.SCALER_CROP_REGION);
            if (cropRegion == null) {
                cropRegion = (Rect) getCameraCharacteristics().get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
            }
            int cropWidth = cropRegion.width();
            int cropHeight = cropRegion.height();
            if (this.mPreviewSize.getHeight() * cropWidth > this.mPreviewSize.getWidth() * cropHeight) {
                imgScale2 = (((double) cropHeight) * 1.0d) / ((double) this.mPreviewSize.getHeight());
                verticalOffset = 0.0d;
                horizontalOffset = (((double) cropWidth) - (((double) this.mPreviewSize.getWidth()) * imgScale2)) / 2.0d;
            } else {
                imgScale2 = (((double) cropWidth) * 1.0d) / ((double) this.mPreviewSize.getWidth());
                horizontalOffset = 0.0d;
                verticalOffset = (((double) cropHeight) - (((double) this.mPreviewSize.getHeight()) * imgScale2)) / 2.0d;
            }
            double x3 = (x2 * imgScale2) + horizontalOffset + ((double) cropRegion.left);
            double y3 = (y2 * imgScale2) + verticalOffset + ((double) cropRegion.top);
            Rect rect = new Rect();
            rect.left = CameraUtils.Clamp((int) (x3 - ((0.1d / 2.0d) * ((double) cropRegion.width()))), 0, cropRegion.width());
            rect.right = CameraUtils.Clamp((int) (((0.1d / 2.0d) * ((double) cropRegion.width())) + x3), 0, cropRegion.width());
            rect.top = CameraUtils.Clamp((int) (y3 - ((0.1d / 2.0d) * ((double) cropRegion.height()))), 0, cropRegion.height());
            rect.bottom = CameraUtils.Clamp((int) (((0.1d / 2.0d) * ((double) cropRegion.height())) + y3), 0, cropRegion.height());
            return rect;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public float getMinFocusDistance() {
        try {
            return ((Float) getCameraCharacteristics().get(CameraCharacteristics.LENS_INFO_MINIMUM_FOCUS_DISTANCE)).floatValue();
        } catch (Exception e) {
            e.printStackTrace();
            return -1.0f;
        }
    }

    public float getFocusDistance(int modestyle) {
        if (modestyle == 0) {
            return CameraUtils.Float2float(this.mCamera2States.focusdistance);
        }
        try {
            return ((Float) this.mPreviewRequestBuilder.get(CaptureRequest.LENS_FOCUS_DISTANCE)).floatValue();
        } catch (Exception e) {
            e.printStackTrace();
            return -1.0f;
        }
    }

    public boolean setFoucsDistance(float focusValue) {
        return setCaptureRequestParameter(CaptureRequest.LENS_FOCUS_DISTANCE, Float.valueOf(focusValue));
    }

    public float[] getAvailableFocalLengths() {
        try {
            return (float[]) getCameraCharacteristics().get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isFocalSupported() {
        float[] a = getAvailableFocalLengths();
        if (a == null || a.length == 1) {
            return false;
        }
        return true;
    }

    public float getFocalLength(int modestyle) {
        if (modestyle == 0) {
            return CameraUtils.Float2float(this.mCamera2States.focalLen);
        }
        try {
            return ((Float) this.mPreviewRequestBuilder.get(CaptureRequest.LENS_FOCAL_LENGTH)).floatValue();
        } catch (Exception e) {
            e.printStackTrace();
            return -1.0f;
        }
    }

    public boolean setFocalLength(float focallen) {
        if (!isFocalSupported()) {
            return false;
        }
        return setCaptureRequestParameter(CaptureRequest.LENS_FOCAL_LENGTH, Float.valueOf(focallen));
    }

    private void makeZoomRatios() {
        float max_zoom = getMaxZoom();
        int n_steps = (int) ((20.0d * Math.log(((double) max_zoom) + 1.0E-11d)) / Math.log(2.0d));
        double scale_factor = Math.pow((double) max_zoom, 1.0d / ((double) n_steps));
        this.zoom_ratios = new ArrayList();
        this.zoom_ratios.add(100);
        double zoom = 1.0d;
        for (int i = 0; i < n_steps - 1; i++) {
            zoom *= scale_factor;
            this.zoom_ratios.add(Integer.valueOf((int) (100.0d * zoom)));
        }
        this.zoom_ratios.add(Integer.valueOf((int) (100.0f * max_zoom)));
    }

    public boolean setZoomEx(float value) {
        if (this.zoom_ratios == null) {
            makeZoomRatios();
        }
        float zoom = ((float) this.zoom_ratios.get(((int) value) * 10).intValue()) / 100.0f;
        Rect sensor_rect = getActiveArraySize();
        int left = sensor_rect.width() / 2;
        int top = sensor_rect.height() / 2;
        int bottom = top;
        int hwidth = (int) (((double) sensor_rect.width()) / (2.0d * ((double) zoom)));
        int hheight = (int) (((double) sensor_rect.height()) / (2.0d * ((double) zoom)));
        int top2 = top - hheight;
        return setCaptureRequestParameter(CaptureRequest.SCALER_CROP_REGION, new Rect(left - hwidth, top2, left + hwidth, bottom + hheight));
    }

    public boolean setZoom(float degree) {
        float max = getMaxZoom();
        if (max < degree) {
            return false;
        }
        Rect activeArraySize = getActiveArraySize();
        Rect zoom = new Rect();
        if (degree == 1.0f) {
            zoom.set(activeArraySize);
        } else {
            int cx = activeArraySize.centerX();
            int cy = activeArraySize.centerY();
            int hw = new Float(((float) (activeArraySize.width() >> 1)) / degree).intValue();
            int hh = new Float(((float) (activeArraySize.height() >> 1)) / degree).intValue();
            zoom = new Rect(cx - hw, cy - hh, cx + hw, cy + hh);
        }
        CameraUtils.LogV("CameraZoom", "SetZoom for degree:" + degree + "/" + max + " is:left & top=( " + zoom.left + "," + zoom.top + "),width & height=( " + zoom.width() + "," + zoom.height() + ")");
        return setCaptureRequestParameter(CaptureRequest.SCALER_CROP_REGION, zoom);
    }

    public float getZoomStep() {
        return 1.0f;
    }

    public float getMaxZoom() {
        try {
            return ((Float) getCameraCharacteristics().get(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM)).floatValue();
        } catch (Exception e) {
            e.printStackTrace();
            return -1.0f;
        }
    }

    public Rect getActiveArraySize() {
        try {
            return adjustActiveArraySize4Zoom((Rect) getCameraCharacteristics().get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Rect adjustActiveArraySize4Zoom(Rect rect) {
        if (rect == null) {
            return rect;
        }
        CameraUtils.LogV("CameraZoom1", "activeArraySize=" + rect.left + ";" + rect.top + " X " + rect.width() + ";" + rect.height());
        return (rect.top == 0 && rect.left == 0) ? rect : new Rect(0, 0, rect.width(), rect.height());
    }

    public int[] getAvailableAWBModes() {
        try {
            return (int[]) getCameraCharacteristics().get(CameraCharacteristics.CONTROL_AWB_AVAILABLE_MODES);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getWhiteBalanceMode() {
        return getWhiteBalanceMode(0);
    }

    public int getWhiteBalanceMode(int modestyle) {
        if (modestyle == 0) {
            return CameraUtils.Intege2int(this.mCamera2States.awbmode);
        }
        try {
            return ((Integer) this.mPreviewRequestBuilder.get(CaptureRequest.CONTROL_AWB_MODE)).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean setWhiteBalanceMode(int mode) {
        if (mode == 0) {
            if (getControMode() != 0 && !setControlMode(0)) {
                return false;
            }
        } else if (mode == 1 && getControMode() != 1 && !setControlMode(1)) {
            return false;
        }
        return setCaptureRequestParameter(CaptureRequest.CONTROL_AWB_MODE, Integer.valueOf(mode));
    }

    public boolean setWhiteBalanceModeEx(int mode) {
        if (mode == 0) {
            try {
                if (getControMode() != 0) {
                }
                this.mPreviewRequestBuilder.set(CaptureRequest.COLOR_CORRECTION_MODE, 0);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else if (mode == 1) {
            if (getControMode() != 1) {
                this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_MODE, 1);
            }
            this.mPreviewRequestBuilder.set(CaptureRequest.COLOR_CORRECTION_MODE, 1);
        }
        this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AWB_MODE, Integer.valueOf(mode));
        setCaptureRequestRepeating();
        return true;
    }

    public boolean lockAWB(boolean enable) {
        return setCaptureRequestParameter(CaptureRequest.CONTROL_AWB_LOCK, Boolean.valueOf(enable));
    }

    public boolean isAWBLocked(int modestyle) {
        if (modestyle != 0) {
            try {
                return ((Boolean) this.mPreviewRequestBuilder.get(CaptureRequest.CONTROL_AWB_LOCK)).booleanValue();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else if (CameraUtils.Intege2int(this.mCamera2States.awbstate) == 3) {
            return true;
        } else {
            return false;
        }
    }

    public boolean enableMWBControl() {
        if (!isMWBEnabled() && !setWhiteBalanceModeEx(0)) {
            return false;
        }
        return true;
    }

    public boolean enableAWBControl() {
        if (!isAWBEnabled() && !setWhiteBalanceModeEx(1)) {
            return false;
        }
        return true;
    }

    public boolean isMWBEnabled() {
        return getWhiteBalanceMode() == 0;
    }

    public boolean isAWBEnabled() {
        boolean ismodeauto;
        boolean iscotrlmodeon;
        if (getWhiteBalanceMode() == 1) {
            ismodeauto = true;
        } else {
            ismodeauto = false;
        }
        if (getControMode() == 1) {
            iscotrlmodeon = true;
        } else {
            iscotrlmodeon = false;
        }
        if (!ismodeauto || !iscotrlmodeon) {
            return false;
        }
        return true;
    }

    public int maxAutoWhiteBalanceRegion() {
        try {
            return ((Integer) getCameraCharacteristics().get(CameraCharacteristics.CONTROL_MAX_REGIONS_AWB)).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public Range<Float> getWBGainRange() {
        return new Range<>(new Float(1.0d), new Float(3.0d));
    }

    public RggbChannelVector getWBGainValue(int modestyle) {
        if (modestyle == 0) {
            return this.mCamera2States.gain;
        }
        try {
            return (RggbChannelVector) this.mPreviewRequestBuilder.get(CaptureRequest.COLOR_CORRECTION_GAINS);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean setWBGainValue(RggbChannelVector vector) {
        if (isMWBEnabled() && getColorCorrectionMode() == 0) {
            return setCaptureRequestParameter(CaptureRequest.COLOR_CORRECTION_GAINS, vector);
        }
        return false;
    }

    public boolean setWBGainValueEx(RggbChannelVector vector) {
        try {
            if (!isMWBEnabled()) {
                this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AWB_MODE, 0);
            }
            if (getColorCorrectionMode() != 0) {
                this.mPreviewRequestBuilder.set(CaptureRequest.COLOR_CORRECTION_MODE, 0);
            }
            this.mPreviewRequestBuilder.set(CaptureRequest.COLOR_CORRECTION_GAINS, vector);
            setCaptureRequestRepeating();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getColorCorrectionMode() {
        return getColorCorrectionMode(0);
    }

    public int getColorCorrectionMode(int modestyle) {
        if (modestyle == 0) {
            return CameraUtils.Intege2int(this.mCamera2States.colorcorrectmode);
        }
        try {
            return CameraUtils.Intege2int((Integer) this.mPreviewRequestBuilder.get(CaptureRequest.COLOR_CORRECTION_MODE));
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean setColorCorrectionMode(int colorCorrectionMode) {
        return setCaptureRequestParameter(CaptureRequest.COLOR_CORRECTION_MODE, Integer.valueOf(colorCorrectionMode));
    }

    public ColorSpaceTransform getColorCorrectionTransform() {
        try {
            return (ColorSpaceTransform) this.mPreviewRequestBuilder.get(CaptureRequest.COLOR_CORRECTION_TRANSFORM);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean setColorCorrectionTransform(ColorSpaceTransform matrix) {
        if (getColorCorrectionMode() != 0) {
            return false;
        }
        return setCaptureRequestParameter(CaptureRequest.COLOR_CORRECTION_TRANSFORM, matrix);
    }

    public int[] getAvailableAEMode() {
        try {
            return (int[]) getCameraCharacteristics().get(CameraCharacteristics.CONTROL_AE_AVAILABLE_MODES);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getAEMode(int modestyle) {
        if (modestyle == 0) {
            return CameraUtils.Intege2int(this.mCamera2States.aemode);
        }
        try {
            return ((Integer) this.mPreviewRequestBuilder.get(CaptureRequest.CONTROL_AE_MODE)).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int getAEMode() {
        return getAEMode(0);
    }

    public boolean setAEMode(int mode) {
        if (getAEMode() == mode) {
            return true;
        }
        if (mode == 0) {
            if (getControMode() != 0 && !setControlMode(0)) {
                return false;
            }
        } else if (getControMode() != 1 && !setControlMode(1)) {
            return false;
        }
        return setCaptureRequestParameter(CaptureRequest.CONTROL_AE_MODE, Integer.valueOf(mode));
    }

    public boolean setAEModeEx(int mode) {
        if (mode != 0) {
            this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_MODE, 1);
        }
        try {
            this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, Integer.valueOf(mode));
            setCaptureRequestRepeating();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isAEMode() {
        if (getAEMode() == 0 || getControMode() == 0) {
            return false;
        }
        return true;
    }

    public boolean isMEMode() {
        if (getAEMode() != 0) {
            return false;
        }
        return true;
    }

    public boolean enableMEControl() {
        if (isMEMode()) {
            return true;
        }
        this.mCameraParameters.setFlashMode(getFacing(), getFlashMode());
        return setAEModeEx(0);
    }

    public boolean enableAEControl() {
        if (isAEMode()) {
            return true;
        }
        return setFlashMode(this.mCameraParameters.getFlashMode(getFacing()));
    }

    public int maxAutoExposureRegion() {
        try {
            return ((Integer) getCameraCharacteristics().get(CameraCharacteristics.CONTROL_MAX_REGIONS_AE)).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean lockAutoExposure(boolean enable) {
        return setCaptureRequestParameter(CaptureRequest.CONTROL_AE_LOCK, Boolean.valueOf(enable));
    }

    public boolean isAutoExposureLock(int modestyle) {
        if (modestyle != 0) {
            try {
                return ((Boolean) this.mPreviewRequestBuilder.get(CaptureRequest.CONTROL_AE_LOCK)).booleanValue();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else if (CameraUtils.Intege2int(this.mCamera2States.aestate) == 3) {
            return true;
        } else {
            return false;
        }
    }

    public Range<Integer> getISORanger() {
        try {
            return (Range) getCameraCharacteristics().get(CameraCharacteristics.SENSOR_INFO_SENSITIVITY_RANGE);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean setISOValue(Integer isoValue) {
        return setCaptureRequestParameter(CaptureRequest.SENSOR_SENSITIVITY, isoValue);
    }

    public int getISOValue(int modestyle) {
        if (modestyle == 0) {
            return CameraUtils.Intege2int(this.mCamera2States.iso);
        }
        try {
            return ((Integer) this.mPreviewRequestBuilder.get(CaptureRequest.SENSOR_SENSITIVITY)).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean isFlashSupported() {
        try {
            Boolean available = (Boolean) getCameraCharacteristics().get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
            if (available == null || available == null) {
                return false;
            }
            return available.booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean setFlashMode(int flashmode) {
        boolean result = false;
        switch (flashmode) {
            case 0:
                result = setFlashModeInternal(0);
                break;
            case 1:
                result = setAEModeEx(3);
                break;
            case 2:
                result = setFlashModeInternal(2);
                break;
            case 3:
                result = setAEModeEx(2);
                break;
            case 4:
                result = setAEModeEx(4);
                break;
        }
        if (result) {
            this.mCameraParameters.setFlashMode(getFacing(), flashmode);
        }
        return result;
    }

    private boolean setFlashModeInternal(int flashmode) {
        if (!isFlashSupported()) {
            return false;
        }
        if (getAEMode() == 0 || getAEMode() == 1 || setAEModeEx(1)) {
            return setCaptureRequestParameter(CaptureRequest.FLASH_MODE, Integer.valueOf(flashmode));
        }
        return false;
    }

    public int getFlashMode() {
        switch (getAEMode()) {
            case 2:
                return 3;
            case 3:
                return 1;
            case 4:
                return 4;
            default:
                switch (getFlashModeInternal()) {
                    case 0:
                        return 0;
                    case 2:
                        return 2;
                    default:
                        return -1;
                }
        }
    }

    private int getFlashModeInternal() {
        return getFlashModeInternal(0);
    }

    private int getFlashModeInternal(int modestyle) {
        if (modestyle == 0) {
            return CameraUtils.Intege2int(this.mCamera2States.flashmode);
        }
        try {
            return ((Integer) this.mPreviewRequestBuilder.get(CaptureRequest.FLASH_MODE)).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public Range<Integer> getExposureCompensationRanger() {
        try {
            return (Range) getCameraCharacteristics().get(CameraCharacteristics.CONTROL_AE_COMPENSATION_RANGE);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Range<Integer> getExposureCompensationRangerEx() {
        Range<Integer> r = getExposureCompensationRanger();
        float stepT = getExposureCompensationStepT();
        return new Range<>(Integer.valueOf((int) (((float) r.getLower().intValue()) * stepT)), Integer.valueOf((int) (((float) r.getUpper().intValue()) * stepT)));
    }

    public float getExposureCompensationStep() {
        try {
            return ((Rational) getCameraCharacteristics().get(CameraCharacteristics.CONTROL_AE_COMPENSATION_STEP)).floatValue();
        } catch (Exception e) {
            e.printStackTrace();
            return -1.0f;
        }
    }

    private float getExposureCompensationStepT() {
        try {
            Rational step = (Rational) getCameraCharacteristics().get(CameraCharacteristics.CONTROL_AE_COMPENSATION_STEP);
            return (float) (step.getDenominator() / step.getNumerator());
        } catch (Exception e) {
            e.printStackTrace();
            return -1.0f;
        }
    }

    public int getExposureCompensationValue(int modestyle) {
        if (modestyle == 0) {
            return CameraUtils.Intege2int(this.mCamera2States.f1074ev);
        }
        try {
            return ((Integer) this.mPreviewRequestBuilder.get(CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION)).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean setExposureCompensationValue(int value) {
        if (getAEMode() != 0 || setAEModeEx(1)) {
            return setCaptureRequestParameter(CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION, Integer.valueOf(value));
        }
        return false;
    }

    public Range<Long> getExposureTimeRange() {
        try {
            return (Range) getCameraCharacteristics().get(CameraCharacteristics.SENSOR_INFO_EXPOSURE_TIME_RANGE);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public long getExposureTime(int modestyle) {
        if (modestyle == 0) {
            return CameraUtils.Long2long(this.mCamera2States.exposuretime);
        }
        try {
            return ((Long) this.mPreviewRequestBuilder.get(CaptureRequest.SENSOR_EXPOSURE_TIME)).longValue();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean setExposureTime(long value) {
        return setCaptureRequestParameter(CaptureRequest.SENSOR_EXPOSURE_TIME, Long.valueOf(value));
    }

    public int getSceneMode() {
        return getSceneMode(0);
    }

    public int getSceneMode(int modestyle) {
        if (modestyle == 0) {
            return CameraUtils.Intege2int(this.mCamera2States.scenemode);
        }
        try {
            return ((Integer) this.mPreviewRequestBuilder.get(CaptureRequest.CONTROL_SCENE_MODE)).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int[] getSupportedSceneModes() {
        try {
            return (int[]) getCameraCharacteristics().get(CameraCharacteristics.CONTROL_AVAILABLE_SCENE_MODES);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean setSceneMode(int mode) {
        if (getControMode() == 2 || setControlMode(2)) {
            return setCaptureRequestParameter(CaptureRequest.CONTROL_SCENE_MODE, Integer.valueOf(mode));
        }
        return false;
    }

    public boolean setSceneModeEx(int mode) {
        try {
            this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_MODE, 2);
            this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_SCENE_MODE, Integer.valueOf(mode));
            setCaptureRequestRepeating();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean disableSceneMode() {
        if (getSceneMode() != 2 || setControlMode(1)) {
            return setCaptureRequestParameter(CaptureRequest.CONTROL_SCENE_MODE, 0);
        }
        return false;
    }

    public boolean isSupportHdrMode() {
        int[] modes = getSupportedSceneModes();
        if (modes == null) {
            return false;
        }
        for (int i : modes) {
            if (i == 18) {
                return true;
            }
        }
        return false;
    }

    public boolean setHdrScene(boolean ison) {
        boolean hdron;
        if (!isSupportHdrMode()) {
            return false;
        }
        if (getSceneMode() == 18) {
            hdron = true;
        } else {
            hdron = false;
        }
        if (ison) {
            if (!hdron) {
                return setSceneModeEx(18);
            }
        } else if (hdron) {
            return setSceneModeEx(0);
        }
        return true;
    }

    public boolean setFacing(int facing) {
        if (getFacing() == facing) {
            return true;
        }
        if (!isCameraOpened()) {
            return false;
        }
        stopCamera();
        return startCamera(facing, 0, 0);
    }

    public int getFacing() {
        CameraCharacteristics cameraCharacteristics = getCameraCharacteristics();
        if (cameraCharacteristics != null) {
            this.mFace = ((Integer) cameraCharacteristics.get(CameraCharacteristics.LENS_FACING)).intValue();
        }
        return this.mFace;
    }

    public int[] getImageOutputFormats(int facing) {
        try {
            String cid = getCameraByFacing(facing);
            if (cid == null) {
                return null;
            }
            return getStreamConfigurationMap(cid).getOutputFormats();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getPictureFormat() {
        return this.mCameraParameters.imageFormat;
    }

    private Size[] getCaptureOutputSizes(int outputFormat, String cameraId) {
        try {
            return CameraUtils.SizeArrayFromSizeArray(getStreamConfigurationMap(cameraId).getOutputSizes(outputFormat));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private <T> Size[] getCaptureOutputSizes(Class<T> outputClass, String cameraId) {
        try {
            return CameraUtils.SizeArrayFromSizeArray(getStreamConfigurationMap(cameraId).getOutputSizes(outputClass));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Size[] getPictureSizes(int face) {
        if (this.mCameraParameters.getSupportedPictureSizes(face) == null) {
            this.mCameraParameters.setSupportedPictureSizes(face, getCaptureOutputSizes(256, getCameraByFacing(face)));
        }
        return this.mCameraParameters.getSupportedPictureSizes(face);
    }

    public SizeMap getPictureSizeMap(int face) {
        return CameraUtils.Sizes2SizeMap(getPictureSizes(face));
    }

    public Size[] getRawPhotoSizes(int face) {
        if (this.mCameraParameters.getSupportedRawPictureSizes(face) == null) {
            this.mCameraParameters.setSupportedRawPictureSizes(face, getCaptureOutputSizes(32, getCameraByFacing(face)));
        }
        return this.mCameraParameters.getSupportedRawPictureSizes(face);
    }

    public Size[] getPictureSizes() {
        return getPictureSizes(getFacing());
    }

    public SizeMap getPictureSizeMap() {
        return CameraUtils.Sizes2SizeMap(getPictureSizes());
    }

    private Size getPictureGlobalMaxSize(int face, int maxwidth) {
        Size[] sizes = getPictureSizes(face);
        if (sizes == null) {
            return null;
        }
        List<Size> sizeList = new ArrayList<>();
        for (Size s : sizes) {
            if (s.getWidth() <= maxwidth) {
                sizeList.add(s);
            }
        }
        if (!sizeList.isEmpty()) {
            return (Size) Collections.max(sizeList, new CompareSizesByArea());
        }
        return null;
    }

    private Size getPictureMaxSize4AspectRatio(AspectRatio aspectRatio, int maxwidth) {
        SortedSet<Size> ss = getPictureSizeMap().sizes(aspectRatio);
        if (ss.isEmpty()) {
            return null;
        }
        List<Size> sizeList = new ArrayList<>();
        for (Size s : ss) {
            if (s.getWidth() <= maxwidth) {
                sizeList.add(s);
            }
        }
        if (!sizeList.isEmpty()) {
            return (Size) Collections.max(sizeList, new CompareSizesByArea());
        }
        return null;
    }

    public Size getPictureMaxSize(int face, AspectRatio aspectRatio, int maxwidth) {
        if (aspectRatio == null) {
            return getPictureGlobalMaxSize(face, maxwidth);
        }
        return getPictureMaxSize4AspectRatio(aspectRatio, maxwidth);
    }

    public Size[] getPreviewSizes(int face) {
        if (this.mCameraParameters.getSupportedPreviewSizes(face) == null) {
            this.mCameraParameters.setSupportedPrevewSizes(face, getCaptureOutputSizes(SurfaceTexture.class, getCameraByFacing(face)));
        }
        return this.mCameraParameters.getSupportedPreviewSizes(face);
    }

    public Size[] getPreviewSizes() {
        return getPreviewSizes(getFacing());
    }

    public SizeMap getPreviewSizeMap(int face) {
        return CameraUtils.Sizes2SizeMap(getPreviewSizes(face));
    }

    public SizeMap getPreviewSizeMap() {
        return CameraUtils.Sizes2SizeMap(getPreviewSizes());
    }

    public Size[] getVideoSizes(int face) {
        if (this.mCameraParameters.getSupportedVideoSizes(face) == null) {
            this.mCameraParameters.setSupportedVideoSizes(face, getCaptureOutputSizes(MediaRecorder.class, getCameraByFacing(face)));
        }
        return this.mCameraParameters.getSupportedVideoSizes(face);
    }

    public Size[] getVideoSizes() {
        return getVideoSizes(getFacing());
    }

    public SizeMap getVideoSizeMap(int face) {
        return CameraUtils.Sizes2SizeMap(getVideoSizes(face));
    }

    public SizeMap getVideoSizeMap() {
        return CameraUtils.Sizes2SizeMap(getVideoSizes());
    }

    public Size[] getHighSpeedVideoSizes() {
        try {
            return CameraUtils.SizeArrayFromSizeArray(getStreamConfigurationMap(this.mCameraId).getHighSpeedVideoSizes());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Size[] getHighSpeedVideoSizesForRange(Range<Integer> range) {
        try {
            return CameraUtils.SizeArrayFromSizeArray(getStreamConfigurationMap(this.mCameraId).getHighSpeedVideoSizesFor(range));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Set<AspectRatio> getSupportedAspectRatios(int mode) {
        SizeMap map = null;
        switch (mode) {
            case 0:
                map = getPreviewSizeMap();
                break;
            case 1:
                map = getPictureSizeMap();
                break;
            case 2:
                map = getVideoSizeMap();
                break;
        }
        return map.ratios();
    }

    public Set<AspectRatio> getPicutrePreviewSupportedAspectRatios() {
        SizeMap idealAspectRatios = getPreviewSizeMap();
        SizeMap pictureSizes = getPictureSizeMap();
        for (AspectRatio aspectRatio : idealAspectRatios.ratios()) {
            if (pictureSizes.sizes(aspectRatio) == null) {
                idealAspectRatios.remove(aspectRatio);
            }
        }
        return idealAspectRatios.ratios();
    }

    public Set<AspectRatio> getVideoPreviewSupportedAspectRatios() {
        SizeMap idealAspectRatios = getPreviewSizeMap();
        SizeMap videoSizeMap = getVideoSizeMap();
        for (AspectRatio aspectRatio : idealAspectRatios.ratios()) {
            if (videoSizeMap.sizes(aspectRatio) == null) {
                idealAspectRatios.remove(aspectRatio);
            }
        }
        return idealAspectRatios.ratios();
    }

    public SizeMap getSupportedPreviewResolutions(int mode) {
        switch (mode) {
            case 1:
                return getPicturePreviewSupportedResolutions();
            case 2:
                return getVideoPreviewSupportedResolutions();
            default:
                return new SizeMap();
        }
    }

    public SizeMap getPicturePreviewSupportedResolutions() {
        SizeMap idealAspectRatios = getPreviewSizeMap();
        SizeMap pictureSizes = getPictureSizeMap();
        for (AspectRatio aspectRatio : idealAspectRatios.ratios()) {
            if (pictureSizes.sizes(aspectRatio) == null) {
                idealAspectRatios.remove(aspectRatio);
            }
        }
        return idealAspectRatios;
    }

    public SizeMap getVideoPreviewSupportedResolutions() {
        SizeMap idealAspectRatios = getPreviewSizeMap();
        SizeMap videoSizeMap = getVideoSizeMap();
        for (AspectRatio aspectRatio : idealAspectRatios.ratios()) {
            if (videoSizeMap.sizes(aspectRatio) == null) {
                idealAspectRatios.remove(aspectRatio);
            }
        }
        return idealAspectRatios;
    }

    public AspectRatio getAspectRatio() {
        return this.mCameraParameters.getPrevewRatio(this.mFace);
    }

    public SortedSet<Size> getSupportedPreviewResolutions(int mode, AspectRatio ratio) {
        return getSupportedPreviewResolutions(mode).sizes(ratio);
    }

    public Size getPreviewRosolution() {
        return this.mPreviewSize;
    }

    public boolean isContainPreviewResolution(Size res) {
        Size[] sizes = getPreviewSizes();
        if (sizes == null) {
            return false;
        }
        for (Size s : sizes) {
            if (s.getHeight() == res.getHeight() && s.getWidth() == res.getWidth()) {
                return true;
            }
        }
        return false;
    }

    public Size getPreviewFrameSize() {
        return this.mCameraParameters.getPreviewFrameSize(getFacing());
    }

    public int getMediaRecordBitRate() {
        return this.mMediaRecordBitRate;
    }

    public void setMediaRecordBitRate(int bitRate) {
        this.mMediaRecordBitRate = bitRate;
    }

    public void setRecordListener(RecordListener listener) {
        this.mRecordListener = listener;
    }

    public boolean isMediaRecordQualitySupported(int face, int quality) {
        int cameraid = cameraIdByInt(face);
        if (cameraid == -1) {
            return false;
        }
        return CamcorderProfile.hasProfile(cameraid, quality);
    }

    public List<Integer> getAvailableTimeLapseQuality(int face) {
        List<Integer> list = new ArrayList<>();
        if (isMediaRecordQualitySupported(face, 1008)) {
            list.add(1008);
        }
        if (isMediaRecordQualitySupported(face, 1006)) {
            list.add(1006);
        }
        if (isMediaRecordQualitySupported(face, 1005)) {
            list.add(1005);
        }
        if (isMediaRecordQualitySupported(face, 1004)) {
            list.add(1004);
        }
        if (isMediaRecordQualitySupported(face, 1003)) {
            list.add(1003);
        }
        if (isMediaRecordQualitySupported(face, 1002)) {
            list.add(1002);
        }
        if (isMediaRecordQualitySupported(face, 1001)) {
            list.add(1001);
        }
        if (isMediaRecordQualitySupported(face, 1000)) {
            list.add(1000);
        }
        if (isMediaRecordQualitySupported(face, 1007)) {
            list.add(1007);
        }
        return list;
    }

    public List<Integer> getAvailableMediaRecordQuality(int face) {
        List<Integer> list = new ArrayList<>();
        if (isMediaRecordQualitySupported(face, 8)) {
            list.add(8);
        }
        if (isMediaRecordQualitySupported(face, 6)) {
            list.add(6);
        }
        if (isMediaRecordQualitySupported(face, 5)) {
            list.add(5);
        }
        if (isMediaRecordQualitySupported(face, 4)) {
            list.add(4);
        }
        return list;
    }

    public List<Integer> getAvailableHighSpeedMediaRecordQuality(int face) {
        List<Integer> list = new ArrayList<>();
        if (isMediaRecordQualitySupported(face, 2005)) {
            list.add(2005);
        }
        if (isMediaRecordQualitySupported(face, 2004)) {
            list.add(2004);
        }
        if (isMediaRecordQualitySupported(face, 2003)) {
            list.add(2003);
        }
        if (isMediaRecordQualitySupported(face, 2002)) {
            list.add(2002);
        }
        if (isMediaRecordQualitySupported(face, 2001)) {
            list.add(2001);
        }
        if (isMediaRecordQualitySupported(face, 2000)) {
            list.add(2000);
        }
        return list;
    }

    private void setUpMediaRecorder(String videopath, Size vidosize, int fps, int orientationhint) throws Exception {
        CameraUtils.LogV("MediaRecord", "setUpMediaRecorder videopath=" + videopath);
        this.mMediaRecorder = new MediaRecorder();
        this.mMediaRecorder.reset();
        this.mMediaRecorder.setAudioSource(1);
        this.mMediaRecorder.setVideoSource(2);
        this.mMediaRecorder.setOutputFormat(2);
        this.mMediaRecorder.setOutputFile(videopath);
        if (this.mMediaRecordBitRate > 0) {
            this.mMediaRecorder.setVideoEncodingBitRate(this.mMediaRecordBitRate);
        } else {
            int videobitrate = CameraUtils.VideoBitRateFromVideoHeightFrame(vidosize.getHeight(), fps);
            CameraUtils.LogV("HHBMediaRecord", "video hight=" + vidosize.getHeight() + " video fps=" + fps + " video bit rate = " + videobitrate + " audio bitrate=" + Camera2Constants.AudioBitRate);
            this.mMediaRecorder.setVideoEncodingBitRate(videobitrate);
        }
        this.mMediaRecorder.setVideoFrameRate(fps);
        this.mMediaRecorder.setVideoSize(vidosize.getWidth(), vidosize.getHeight());
        this.mMediaRecorder.setVideoEncoder(2);
        this.mMediaRecorder.setAudioEncoder(3);
        this.mMediaRecorder.setAudioChannels(2);
        this.mMediaRecorder.setAudioEncodingBitRate(Camera2Constants.AudioBitRate);
        this.mMediaRecorder.setAudioSamplingRate(Camera2Constants.AudioSampleRate);
        this.mMediaRecorder.setOrientationHint(orientationhint);
        this.mMediaRecorder.setMaxFileSize(0);
        this.mMediaRecorder.setMaxDuration(0);
        this.mMediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
            public void onError(MediaRecorder mr, int what, int extra) {
                CameraUtils.LogV("Huang", "media record error!");
                if (Camera2Manager.this.mRecordListener != null) {
                    Camera2Manager.this.mRecordListener.onRecordError("" + what);
                }
            }
        });
        this.mMediaRecorder.prepare();
    }

    private void setUpMediaRecorder(FileDescriptor fd, Size vidosize, int fps, int orientationhint) throws Exception {
        CameraUtils.LogV("MediaRecord", "setUpMediaRecorder FileDescriptor=" + fd);
        this.mMediaRecorder = new MediaRecorder();
        this.mMediaRecorder.reset();
        this.mMediaRecorder.setAudioSource(1);
        this.mMediaRecorder.setVideoSource(2);
        this.mMediaRecorder.setOutputFormat(2);
        this.mMediaRecorder.setOutputFile(fd);
        if (this.mMediaRecordBitRate > 0) {
            this.mMediaRecorder.setVideoEncodingBitRate(this.mMediaRecordBitRate);
        } else {
            int videobitrate = CameraUtils.VideoBitRateFromVideoHeightFrame(vidosize.getHeight(), fps);
            CameraUtils.LogV("HHBMediaRecord", "video hight=" + vidosize.getHeight() + " video fps=" + fps + " video bit rate = " + videobitrate + " audio bitrate=" + Camera2Constants.AudioBitRate);
            this.mMediaRecorder.setVideoEncodingBitRate(videobitrate);
        }
        this.mMediaRecorder.setVideoFrameRate(fps);
        this.mMediaRecorder.setVideoSize(vidosize.getWidth(), vidosize.getHeight());
        this.mMediaRecorder.setVideoEncoder(2);
        this.mMediaRecorder.setAudioEncoder(3);
        this.mMediaRecorder.setAudioChannels(2);
        this.mMediaRecorder.setAudioEncodingBitRate(Camera2Constants.AudioBitRate);
        this.mMediaRecorder.setAudioSamplingRate(Camera2Constants.AudioSampleRate);
        this.mMediaRecorder.setOrientationHint(orientationhint);
        this.mMediaRecorder.setMaxFileSize(0);
        this.mMediaRecorder.setMaxDuration(0);
        this.mMediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
            public void onError(MediaRecorder mr, int what, int extra) {
                CameraUtils.LogV("Huang", "media record error!");
                if (Camera2Manager.this.mRecordListener != null) {
                    Camera2Manager.this.mRecordListener.onRecordError("" + what);
                }
            }
        });
        this.mMediaRecorder.prepare();
    }

    private void setUpMediaRecorderEx(String videopath, int quality, double capturerate, int orientationhint) throws Exception {
        CameraUtils.LogV("MediaRecord", "setUpMediaRecorderEx qualit=" + quality + " capture rate=" + capturerate);
        int camid = cameraintID();
        if (camid == -1) {
            throw new Exception("camera id error! ");
        } else if (!CamcorderProfile.hasProfile(camid, quality)) {
            throw new Exception("Quality is not supported ");
        } else {
            this.mMediaRecorder = new MediaRecorder();
            this.mMediaRecorder.reset();
            this.mMediaRecorder.setVideoSource(2);
            if (capturerate > 0.0d) {
                this.mMediaRecorder.setCaptureRate(capturerate);
            } else {
                this.mMediaRecorder.setAudioSource(1);
            }
            this.mMediaRecorder.setProfile(CamcorderProfile.get(camid, quality));
            if (this.mMediaRecordBitRate > 0) {
                this.mMediaRecorder.setVideoEncodingBitRate(this.mMediaRecordBitRate);
            } else {
                int videobitrate = CameraUtils.VideoBitRateFromQuality(quality);
                CameraUtils.LogV("HHBMediaRecord", "video quality=" + quality + " video bit rate = " + videobitrate);
                this.mMediaRecorder.setVideoEncodingBitRate(videobitrate);
            }
            this.mMediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
                public void onError(MediaRecorder mr, int what, int extra) {
                    CameraUtils.LogV("Huang", "media record error!");
                    if (Camera2Manager.this.mRecordListener != null) {
                        Camera2Manager.this.mRecordListener.onRecordError("" + what);
                    }
                }
            });
            this.mMediaRecorder.setOrientationHint(orientationhint);
            this.mMediaRecorder.setOutputFile(videopath);
            this.mMediaRecorder.setAudioSamplingRate(Camera2Constants.AudioSampleRate);
            this.mMediaRecorder.setMaxFileSize(0);
            this.mMediaRecorder.setMaxDuration(0);
            this.mMediaRecorder.prepare();
        }
    }

    private void setUpMediaRecorderEx(FileDescriptor fd, int quality, double capturerate, int orientationhint) throws Exception {
        CameraUtils.LogV("MediaRecord", "setUpMediaRecorderEx qualit=" + quality + " capture rate=" + capturerate + "FileDescriptor=" + fd);
        int camid = cameraintID();
        if (camid == -1) {
            throw new Exception("camera id error! ");
        } else if (!CamcorderProfile.hasProfile(camid, quality)) {
            throw new Exception("Quality is not supported ");
        } else {
            this.mMediaRecorder = new MediaRecorder();
            this.mMediaRecorder.reset();
            this.mMediaRecorder.setVideoSource(2);
            if (capturerate > 0.0d) {
                this.mMediaRecorder.setCaptureRate(capturerate);
            } else {
                this.mMediaRecorder.setAudioSource(0);
            }
            this.mMediaRecorder.setProfile(CamcorderProfile.get(camid, quality));
            if (this.mMediaRecordBitRate > 0) {
                this.mMediaRecorder.setVideoEncodingBitRate(this.mMediaRecordBitRate);
            } else {
                int videobitrate = CameraUtils.VideoBitRateFromQuality(quality);
                CameraUtils.LogV("HHBMediaRecord", "video quality=" + quality + " video bit rate = " + videobitrate);
                this.mMediaRecorder.setVideoEncodingBitRate(videobitrate);
            }
            this.mMediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
                public void onError(MediaRecorder mr, int what, int extra) {
                    CameraUtils.LogV("Huang", "media record error!");
                    if (Camera2Manager.this.mRecordListener != null) {
                        Camera2Manager.this.mRecordListener.onRecordError("" + what);
                    }
                }
            });
            this.mMediaRecorder.setOrientationHint(orientationhint);
            this.mMediaRecorder.setOutputFile(fd);
            this.mMediaRecorder.setAudioSamplingRate(Camera2Constants.AudioSampleRate);
            this.mMediaRecorder.setMaxFileSize(0);
            this.mMediaRecorder.setMaxDuration(0);
            this.mMediaRecorder.prepare();
        }
    }

    /* access modifiers changed from: private */
    public void configRecordCamera4HighSpeed(Range<Integer> range) {
        resumePreviewCamera();
        this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, range);
    }

    /* access modifiers changed from: private */
    public void configRecordCamera() {
        resumePreviewCamera();
        Range<Integer>[] ranges = getSupportedPreviewFPSRanges();
        Range<Integer> range = null;
        int i = 0;
        while (true) {
            if (i < ranges.length) {
                if (ranges[i].getUpper().intValue() == 30 && ranges[i].getLower().intValue() == 30) {
                    range = ranges[i];
                    break;
                }
                i++;
            } else {
                break;
            }
        }
        if (range != null) {
            this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, range);
        }
    }

    private boolean createMediaRecordSessionAndPlay4HighSpeed(final Range<Integer> fpsranger, Size videosize) throws Exception {
        CameraUtils.LogV("HighSpeedRecord", "high speed record:" + videosize.getWidth() + "x" + videosize.getHeight() + " fps=" + fpsranger.getLower() + ":" + fpsranger.getUpper());
        if (Build.VERSION.SDK_INT < 23) {
            return false;
        }
        SurfaceTexture texture = this.mPreview.getCameraTextureView().getSurfaceTexture();
        if ($assertionsDisabled || texture != null) {
            texture.setDefaultBufferSize(videosize.getWidth(), videosize.getHeight());
            this.mPreviewRequestBuilder = this.mCameraDevice.createCaptureRequest(3);
            List<Surface> surfacelist = new ArrayList<>();
            Surface previewSurface = new Surface(texture);
            surfacelist.add(previewSurface);
            this.mPreviewRequestBuilder.addTarget(previewSurface);
            Surface recorderSurface = this.mMediaRecorder.getSurface();
            surfacelist.add(recorderSurface);
            this.mPreviewRequestBuilder.addTarget(recorderSurface);
            this.mCameraDevice.createConstrainedHighSpeedCaptureSession(surfacelist, new CameraCaptureSession.StateCallback() {
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    CameraUtils.LogV("HighSpeedRecord", "onConfigured");
                    CameraCaptureSession unused = Camera2Manager.this.mPreviewSession = cameraCaptureSession;
                    try {
                        if (Build.VERSION.SDK_INT >= 23) {
                            Camera2Manager.this.configRecordCamera4HighSpeed(fpsranger);
                            Camera2Manager.this.mPreviewSession.setRepeatingBurst(((CameraConstrainedHighSpeedCaptureSession) Camera2Manager.this.mPreviewSession).createHighSpeedRequestList(Camera2Manager.this.mPreviewRequestBuilder.build()), Camera2Manager.this.mPreviewHighSpeedCaptureRequestCallback, Camera2Manager.this.mBackgroundHandler);
                            CameraUtils.LogV("HighSpeedRecord", "after setRepeatingBurst");
                            Camera2Manager.this.context.runOnUiThread(new Runnable() {
                                public void run() {
                                    try {
                                        CameraUtils.LogV("HighSpeedRecord", "runOnUiThread");
                                        Camera2Manager.this.mMediaRecorder.start();
                                        boolean unused = Camera2Manager.this.mIsMediaRecording = true;
                                        if (Camera2Manager.this.mRecordListener != null) {
                                            Camera2Manager.this.mRecordListener.onRecordStarted();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        if (Camera2Manager.this.mRecordListener != null) {
                                            Camera2Manager.this.mRecordListener.onRecordError(e.getMessage());
                                        }
                                    }
                                }
                            });
                        }
                    } catch (Exception e) {
                        CameraUtils.LogV("HighSpeedRecord", "CameraAccessException:" + e.getMessage());
                        e.printStackTrace();
                        if (Camera2Manager.this.mRecordListener != null) {
                            Camera2Manager.this.mRecordListener.onRecordError(e.getMessage());
                        }
                    }
                }

                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    CameraUtils.LogV("HighSpeedRecord", "onConfigureFailed");
                    if (Camera2Manager.this.mRecordListener != null) {
                        Camera2Manager.this.mRecordListener.onRecordError("create Capture Session Configure Failed");
                    }
                }
            }, this.mBackgroundHandler);
            CameraUtils.LogV("HighSpeedRecord", "createMediaRecordSessionAndPlay4HighSpeed finished");
            return true;
        }
        throw new AssertionError();
    }

    private boolean createMediaRecordSessionAndPlay(Size videosize) throws Exception {
        SurfaceTexture texture = this.mPreview.getCameraTextureView().getSurfaceTexture();
        if ($assertionsDisabled || texture != null) {
            texture.setDefaultBufferSize(this.mPreviewSize.getWidth(), this.mPreviewSize.getHeight());
            this.mPreviewRequestBuilder = this.mCameraDevice.createCaptureRequest(3);
            List<Surface> surfacelist = new ArrayList<>();
            if (!Camera2Constants.Exclude_List_EnablePreviewFrameSurfaceView.contains(Build.BRAND.toLowerCase())) {
                CameraUtils.LogV("CommonMediaRecord", "----------> not xiaomi");
                Surface previewSurface = new Surface(texture);
                surfacelist.add(previewSurface);
                this.mPreviewRequestBuilder.addTarget(previewSurface);
                Surface recorderSurface = this.mMediaRecorder.getSurface();
                surfacelist.add(recorderSurface);
                this.mPreviewRequestBuilder.addTarget(recorderSurface);
            }
            this.isPreviewListenerEnabledFromRecord = false;
            if (!Camera2Constants.Exclude_List_EnablePreviewFrameSurfaceView.contains(Build.BRAND.toLowerCase()) || this.isPreviewListenerEnabled) {
                CameraUtils.LogV("CommonMediaRecord", "add mPreviewFrameReader surface");
                surfacelist.add(this.mPreviewFrameReader.getSurface());
                this.mPreviewRequestBuilder.addTarget(this.mPreviewFrameReader.getSurface());
                this.isPreviewListenerEnabledFromRecord = true;
            }
            if (Camera2Constants.Exclude_List_EnablePreviewFrameSurfaceView.contains(Build.BRAND.toLowerCase())) {
                CameraUtils.LogV("CommonMediaRecord", "==========> xiaomi");
                Surface recorderSurface2 = this.mMediaRecorder.getSurface();
                surfacelist.add(recorderSurface2);
                this.mPreviewRequestBuilder.addTarget(recorderSurface2);
                Surface previewSurface2 = new Surface(texture);
                surfacelist.add(previewSurface2);
                this.mPreviewRequestBuilder.addTarget(previewSurface2);
            }
            this.mCameraDevice.createCaptureSession(surfacelist, new CameraCaptureSession.StateCallback() {
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    CameraUtils.LogV("CreateCaptureSession", "onConfigured");
                    CameraCaptureSession unused = Camera2Manager.this.mPreviewSession = cameraCaptureSession;
                    try {
                        Camera2Manager.this.configRecordCamera();
                        Camera2Manager.this.mPreviewSession.setRepeatingRequest(Camera2Manager.this.mPreviewRequestBuilder.build(), Camera2Manager.this.mPreviewCaptureRequestCallback, Camera2Manager.this.mBackgroundHandler);
                        Camera2Manager.this.context.runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    CameraUtils.LogV("CreateCaptureSession", "runOnUiThread");
                                    Camera2Manager.this.mMediaRecorder.start();
                                    boolean unused = Camera2Manager.this.mIsMediaRecording = true;
                                    if (Camera2Manager.this.mRecordListener != null) {
                                        Camera2Manager.this.mRecordListener.onRecordStarted();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    if (Camera2Manager.this.mRecordListener != null) {
                                        Camera2Manager.this.mRecordListener.onRecordError(e.getMessage());
                                    }
                                }
                            }
                        });
                    } catch (Exception e) {
                        CameraUtils.LogV("CreateCaptureSession", "CameraAccessException:" + e.getMessage());
                        e.printStackTrace();
                        if (Camera2Manager.this.mRecordListener != null) {
                            Camera2Manager.this.mRecordListener.onRecordError(e.getMessage());
                        }
                    }
                }

                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    CameraUtils.LogV("CreateCaptureSession", "onConfigureFailed");
                    if (Camera2Manager.this.mRecordListener != null) {
                        Camera2Manager.this.mRecordListener.onRecordError("create Capture Session Configure Failed");
                    }
                }

                public void onActive(@NonNull CameraCaptureSession session) {
                    super.onActive(session);
                    CameraUtils.LogV("CreateCaptureSession", "onActive");
                }

                public void onClosed(@NonNull CameraCaptureSession session) {
                    super.onClosed(session);
                    CameraUtils.LogV("CreateCaptureSession", "onClosed");
                }

                public void onSurfacePrepared(@NonNull CameraCaptureSession session, @NonNull Surface surface) {
                    super.onSurfacePrepared(session, surface);
                    CameraUtils.LogV("CreateCaptureSession", "onSurfacePrepared");
                }

                public void onReady(@NonNull CameraCaptureSession session) {
                    super.onReady(session);
                    CameraUtils.LogV("CreateCaptureSession", "onReady");
                }
            }, this.mBackgroundHandler);
            return true;
        }
        throw new AssertionError();
    }

    public boolean startMediaRecord(String videopath, int quality, double capturerate, int orientationhint) {
        if (isMediaRecording() || !isCameraOpened()) {
            return false;
        }
        try {
            closeCameraPreviewSession(true);
            setUpMediaRecorderEx(videopath, quality, capturerate, orientationhint);
            Size videosize = null;
            CameraUtils.SizeFps sizefps = CameraUtils.Quality2Size(quality);
            if (sizefps != null) {
                videosize = sizefps.size;
            }
            return createMediaRecordSessionAndPlay(videosize);
        } catch (Exception e) {
            CameraUtils.LogV("MediaRecord", "startMediaRecord Exception:" + e.getCause() + "/" + e.getMessage());
            e.printStackTrace();
            startPreview();
            return false;
        }
    }

    public boolean startMediaRecord(FileDescriptor fd, int quality, double capturerate, int orientationhint) {
        if (isMediaRecording() || !isCameraOpened()) {
            return false;
        }
        try {
            closeCameraPreviewSession(true);
            setUpMediaRecorderEx(fd, quality, capturerate, orientationhint);
            Size videosize = null;
            CameraUtils.SizeFps sizefps = CameraUtils.Quality2Size(quality);
            if (sizefps != null) {
                videosize = sizefps.size;
            }
            return createMediaRecordSessionAndPlay(videosize);
        } catch (Exception e) {
            CameraUtils.LogV("MediaRecord", "startMediaRecord Exception:" + e.getMessage());
            e.printStackTrace();
            startPreview();
            return false;
        }
    }

    public boolean startMediaRecord(String videopath, Size vidosize, int fps, int orientationhint) {
        if (isMediaRecording() || !isCameraOpened()) {
            return false;
        }
        try {
            closeCameraPreviewSession(true);
            setUpMediaRecorder(videopath, vidosize, fps, orientationhint);
            if (fps <= 30) {
                return createMediaRecordSessionAndPlay(vidosize);
            }
            Range<Integer> fpsranges = getHighSpeedFpsRangerForSize(vidosize, fps, true);
            if (fpsranges != null) {
                return createMediaRecordSessionAndPlay4HighSpeed(fpsranges, vidosize);
            }
            CameraUtils.LogV("HighSpeedRecord", "fpsranges is null for " + vidosize.getWidth() + "x" + vidosize.getHeight() + " ; fps =" + fps);
            startPreview();
            return false;
        } catch (Exception e) {
            CameraUtils.LogV("HighSpeedRecord", "startMediaRecord Exception:" + e.getMessage());
            e.printStackTrace();
            startPreview();
            return false;
        }
    }

    public boolean startMediaRecord(FileDescriptor fd, Size vidosize, int fps, int orientationhint) {
        if (isMediaRecording() || !isCameraOpened()) {
            return false;
        }
        try {
            closeCameraPreviewSession(true);
            setUpMediaRecorder(fd, vidosize, fps, orientationhint);
            if (fps <= 30) {
                return createMediaRecordSessionAndPlay(vidosize);
            }
            Range<Integer> fpsranges = getHighSpeedFpsRangerForSize(vidosize, fps, true);
            if (fpsranges != null) {
                return createMediaRecordSessionAndPlay4HighSpeed(fpsranges, vidosize);
            }
            CameraUtils.LogV("HighSpeedRecord", "fpsranges is null for " + vidosize.getWidth() + "x" + vidosize.getHeight() + " ; fps =" + fps);
            startPreview();
            return false;
        } catch (Exception e) {
            CameraUtils.LogV("HighSpeedRecord", "startMediaRecord Exception:" + e.getMessage());
            e.printStackTrace();
            startPreview();
            return false;
        }
    }

    public boolean isMediaRecording() {
        return this.mIsMediaRecording;
    }

    public boolean stopMediaRecord() {
        if (!this.mIsMediaRecording) {
            return false;
        }
        try {
            this.mPreviewSession.stopRepeating();
            Thread.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.isPreviewListenerEnabledFromRecord = false;
            this.mMediaRecorder.stop();
            this.mMediaRecorder.reset();
            this.mMediaRecorder.release();
            if (startPreview()) {
                this.mMediaRecorder = null;
                this.mIsMediaRecording = false;
                if (this.mRecordListener != null) {
                    this.mRecordListener.onRecordEnd();
                }
                return true;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            if (startPreview()) {
                this.mMediaRecorder = null;
                this.mIsMediaRecording = false;
                if (this.mRecordListener != null) {
                    this.mRecordListener.onRecordEnd();
                }
                return true;
            }
        } catch (Throwable th) {
            if (startPreview()) {
                this.mMediaRecorder = null;
                this.mIsMediaRecording = false;
                if (this.mRecordListener != null) {
                    this.mRecordListener.onRecordEnd();
                }
                return true;
            }
            throw th;
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

    public boolean setFPSRange(Range<Integer> range) {
        boolean result = setCaptureRequestParameter(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, range);
        if (result) {
            this.mCameraParameters.setFps(getFacing(), range.getUpper().intValue());
        }
        return result;
    }

    private Range<Integer> getRangeByCertainFps(int fps, boolean ismax) {
        List<Range<Integer>> fpss = getRangesConstainsFps(fps);
        Range<Integer> afr = null;
        for (int i = 0; i < fpss.size(); i++) {
            Range<Integer> fr = fpss.get(i);
            if (fr.getUpper().intValue() == fps) {
                if (afr == null) {
                    afr = fr;
                } else if (ismax) {
                    if (afr.getLower().intValue() < fr.getLower().intValue()) {
                        afr = fr;
                    }
                } else if (afr.getLower().intValue() > fr.getLower().intValue()) {
                    afr = fr;
                }
            }
        }
        return afr;
    }

    public boolean setFps(int fps) {
        Range<Integer> afr = getRangeByCertainFps(fps, true);
        if (afr != null) {
            return setFPSRange(afr);
        }
        return false;
    }

    public Range<Integer> getFPSRange() {
        return getFPSRange(1);
    }

    public Range<Integer> getFPSRange(int modestyle) {
        if (modestyle == 0) {
            return this.mCamera2States.fpsranges;
        }
        try {
            return (Range) this.mPreviewRequestBuilder.get(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Range<Integer>[] getSupportedPreviewFPSRanges() {
        try {
            return (Range[]) getCameraCharacteristics().get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Range<Integer>> getRangesConstainsFps(int fps) {
        Range<Integer>[] ranges = getSupportedPreviewFPSRanges();
        List<Range<Integer>> list = new ArrayList<>();
        if (ranges != null) {
            for (int i = 0; i < ranges.length; i++) {
                if (ranges[i].contains(Integer.valueOf(fps))) {
                    list.add(ranges[i]);
                }
            }
        }
        return list;
    }

    public Range<Integer> getHighSpeedFpsRangerForSize(Size size, int fps, boolean fixed) {
        Range<Integer>[] ranges = getHighSpeedFpsRangeForSize(size);
        if (ranges == null) {
            return null;
        }
        for (Range<Integer> range : ranges) {
            if (fixed) {
                if (range.getLower().intValue() == fps && range.getUpper().intValue() == fps) {
                    return range;
                }
            } else if (range.getLower().intValue() <= fps && range.getUpper().intValue() >= fps) {
                return range;
            }
        }
        return null;
    }

    public Range<Integer>[] getHighSpeedFpsRangeForSize(Size size) {
        try {
            return getStreamConfigurationMap(this.mCameraId).getHighSpeedVideoFpsRangesFor(size.toSize());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Range<Integer>[] getHighSpeedVideoFpsRange() {
        try {
            return getStreamConfigurationMap(this.mCameraId).getHighSpeedVideoFpsRanges();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public long getPreviewFrameOutputMinFrameDuration(Size size) {
        try {
            return getStreamConfigurationMap(this.mCameraId).getOutputMinFrameDuration(35, size.toSize());
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public long getPreviewOutputMinFrameDuration(Size size) {
        return getOutputMinFrameDuration(SurfaceTexture.class, this.mCameraId, size);
    }

    public long getMediaRecordOutputMinFrameDuration(Size size) {
        return getOutputMinFrameDuration(MediaRecorder.class, this.mCameraId, size);
    }

    private <T> long getOutputMinFrameDuration(Class<T> outputClass, String cameraId, Size size) {
        try {
            return getStreamConfigurationMap(cameraId).getOutputMinFrameDuration(outputClass, size.toSize());
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int[] getAvailableColorEffectMode() {
        try {
            return (int[]) getCameraCharacteristics().get(CameraCharacteristics.CONTROL_AVAILABLE_EFFECTS);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getColorEffectMode() {
        try {
            return ((Integer) this.mPreviewRequestBuilder.get(CaptureRequest.CONTROL_EFFECT_MODE)).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean setColorEffectMode(int mode) {
        if (getColorEffectMode() == mode) {
            return true;
        }
        if (getControMode() != 0 || setControlMode(1)) {
            return setCaptureRequestParameter(CaptureRequest.CONTROL_EFFECT_MODE, Integer.valueOf(mode));
        }
        return false;
    }

    public boolean setColorEffectModeEx(int mode) {
        try {
            this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_MODE, 1);
            this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_EFFECT_MODE, Integer.valueOf(mode));
            setCaptureRequestRepeating();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isVideoStabilizationSupport() {
        if (isOpticalVideoStabilizationSupport()) {
            return true;
        }
        return isDigitalVideoStabilizationSupport();
    }

    public int videoStabilizationType() {
        if (isOpticalVideoStabilizationSupport()) {
            return 0;
        }
        if (isDigitalVideoStabilizationSupport()) {
            return 1;
        }
        return -1;
    }

    public boolean isVideoStabilizationOpened() {
        if (isOpticalVideoStabilizationSupport()) {
            return isOpticalVideoStabilizationOpened();
        }
        if (isDigitalVideoStabilizationSupport()) {
            return isDigitalVideoStabilizationOpened();
        }
        return false;
    }

    public int getVideoStabilizationMode() {
        int mode = -1;
        if (isOpticalVideoStabilizationSupport()) {
            mode = getOpticalVideoStabilizationMode(0);
        } else if (isDigitalVideoStabilizationSupport()) {
            mode = getDigitalVideoStabilizationMode(0);
        }
        if (mode == 0 || mode == 0) {
            return 0;
        }
        if (mode == 1 || mode == 1) {
            return 1;
        }
        return -1;
    }

    public boolean setVideoStabilizationMode(int mode) {
        if (isOpticalVideoStabilizationSupport()) {
            if (isDigitalVideoStabilizationOpened()) {
                setDigitalVideoStabilizationMode(0);
            }
            if (mode == 1) {
                return setOpticalVideoStabilizationMode(1);
            }
            if (mode == 0) {
                return setOpticalVideoStabilizationMode(0);
            }
            return false;
        } else if (!isDigitalVideoStabilizationSupport()) {
            return false;
        } else {
            if (isOpticalVideoStabilizationOpened()) {
                setOpticalVideoStabilizationMode(0);
            }
            if (mode == 1) {
                return setDigitalVideoStabilizationMode(1);
            }
            if (mode == 0) {
                return setDigitalVideoStabilizationMode(0);
            }
            return false;
        }
    }

    public boolean isDigitalVideoStabilizationSupport() {
        int[] modes = getAvailableDigitalVideoStabilizationMode();
        if (modes == null) {
            return false;
        }
        for (int i : modes) {
            if (i == 1) {
                return true;
            }
        }
        return false;
    }

    public int[] getAvailableDigitalVideoStabilizationMode() {
        try {
            return (int[]) getCameraCharacteristics().get(CameraCharacteristics.CONTROL_AVAILABLE_VIDEO_STABILIZATION_MODES);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isDigitalVideoStabilizationOpened() {
        if (getDigitalVideoStabilizationMode(0) == 1) {
            return true;
        }
        return false;
    }

    public int getDigitalVideoStabilizationMode(int modestyle) {
        if (modestyle == 0) {
            return CameraUtils.Intege2int(this.mCamera2States.digitalvideostabmode);
        }
        try {
            return ((Integer) this.mPreviewRequestBuilder.get(CaptureRequest.CONTROL_VIDEO_STABILIZATION_MODE)).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean setDigitalVideoStabilizationMode(int mode) {
        if (getAvailableDigitalVideoStabilizationMode() == null) {
            if (mode == 0) {
                return true;
            }
            return false;
        } else if (!setCaptureRequestParameter(CaptureRequest.CONTROL_VIDEO_STABILIZATION_MODE, Integer.valueOf(mode))) {
            return false;
        } else {
            this.mCamera2States.digitalvideostabmode = Integer.valueOf(mode);
            return true;
        }
    }

    public boolean isOpticalVideoStabilizationSupport() {
        int[] modes = getAvailableOpticalVideoStabilizationMode();
        if (modes == null) {
            return false;
        }
        for (int i : modes) {
            if (i == 1) {
                return true;
            }
        }
        return false;
    }

    public int[] getAvailableOpticalVideoStabilizationMode() {
        try {
            return (int[]) getCameraCharacteristics().get(CameraCharacteristics.LENS_INFO_AVAILABLE_OPTICAL_STABILIZATION);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isOpticalVideoStabilizationOpened() {
        if (getOpticalVideoStabilizationMode(1) == 1) {
            return true;
        }
        return false;
    }

    public int getOpticalVideoStabilizationMode(int modestyle) {
        if (modestyle == 1) {
            return CameraUtils.Intege2int(this.mCamera2States.opticalvideostabmode);
        }
        try {
            return ((Integer) this.mPreviewRequestBuilder.get(CaptureRequest.LENS_OPTICAL_STABILIZATION_MODE)).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean setOpticalVideoStabilizationMode(int mode) {
        int[] modes = getAvailableOpticalVideoStabilizationMode();
        if (modes != null) {
            int i = 0;
            while (i < modes.length) {
                if (modes[i] != mode) {
                    i++;
                } else if (!setCaptureRequestParameter(CaptureRequest.LENS_OPTICAL_STABILIZATION_MODE, Integer.valueOf(mode))) {
                    return false;
                } else {
                    this.mCamera2States.opticalvideostabmode = Integer.valueOf(mode);
                    return true;
                }
            }
            return false;
        } else if (mode == 0) {
            return true;
        } else {
            return false;
        }
    }

    public int getCameramode() {
        return this.mCameraMode;
    }

    public boolean changeCamreMode(int newCameraMode) {
        if (newCameraMode != this.mCameraMode) {
            this.mCameraMode = newCameraMode;
        }
        return true;
    }

    public void setCameraStatesListener(CameraStatesListener cameraStatesListener) {
        this.mCameraStatesListener = cameraStatesListener;
    }
}
