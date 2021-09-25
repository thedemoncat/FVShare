package org.opencv.android;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import org.opencv.BuildConfig;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class JavaCameraView extends CameraBridgeViewBase implements Camera.PreviewCallback {
    private static final int MAGIC_TEXTURE_ID = 10;
    private static final String TAG = "JavaCameraView";
    private byte[] mBuffer;
    protected Camera mCamera;
    protected JavaCameraFrame[] mCameraFrame;
    /* access modifiers changed from: private */
    public boolean mCameraFrameReady = false;
    /* access modifiers changed from: private */
    public int mChainIdx = 0;
    /* access modifiers changed from: private */
    public Mat[] mFrameChain;
    /* access modifiers changed from: private */
    public boolean mStopThread;
    private SurfaceTexture mSurfaceTexture;
    private Thread mThread;

    public static class JavaCameraSizeAccessor implements CameraBridgeViewBase.ListItemAccessor {
        public int getWidth(Object obj) {
            return ((Camera.Size) obj).width;
        }

        public int getHeight(Object obj) {
            return ((Camera.Size) obj).height;
        }
    }

    public JavaCameraView(Context context, int cameraId) {
        super(context, cameraId);
    }

    public JavaCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean initializeCamera(int r24, int r25) {
        /*
            r23 = this;
            java.lang.String r17 = "JavaCameraView"
            java.lang.String r18 = "Initialize java camera"
            android.util.Log.d(r17, r18)
            r14 = 1
            monitor-enter(r23)
            r17 = 0
            r0 = r17
            r1 = r23
            r1.mCamera = r0     // Catch:{ all -> 0x00ae }
            r0 = r23
            int r0 = r0.mCameraIndex     // Catch:{ all -> 0x00ae }
            r17 = r0
            r18 = -1
            r0 = r17
            r1 = r18
            if (r0 != r1) goto L_0x00e2
            java.lang.String r17 = "JavaCameraView"
            java.lang.String r18 = "Trying to open camera with old open()"
            android.util.Log.d(r17, r18)     // Catch:{ all -> 0x00ae }
            android.hardware.Camera r17 = android.hardware.Camera.open()     // Catch:{ Exception -> 0x008e }
            r0 = r17
            r1 = r23
            r1.mCamera = r0     // Catch:{ Exception -> 0x008e }
        L_0x0034:
            r0 = r23
            android.hardware.Camera r0 = r0.mCamera     // Catch:{ all -> 0x00ae }
            r17 = r0
            if (r17 != 0) goto L_0x0082
            int r17 = android.os.Build.VERSION.SDK_INT     // Catch:{ all -> 0x00ae }
            r18 = 9
            r0 = r17
            r1 = r18
            if (r0 < r1) goto L_0x0082
            r9 = 0
            r7 = 0
        L_0x0048:
            int r17 = android.hardware.Camera.getNumberOfCameras()     // Catch:{ all -> 0x00ae }
            r0 = r17
            if (r7 >= r0) goto L_0x0082
            java.lang.String r17 = "JavaCameraView"
            java.lang.StringBuilder r18 = new java.lang.StringBuilder     // Catch:{ all -> 0x00ae }
            r18.<init>()     // Catch:{ all -> 0x00ae }
            java.lang.String r19 = "Trying to open camera with new open("
            java.lang.StringBuilder r18 = r18.append(r19)     // Catch:{ all -> 0x00ae }
            java.lang.Integer r19 = java.lang.Integer.valueOf(r7)     // Catch:{ all -> 0x00ae }
            java.lang.StringBuilder r18 = r18.append(r19)     // Catch:{ all -> 0x00ae }
            java.lang.String r19 = ")"
            java.lang.StringBuilder r18 = r18.append(r19)     // Catch:{ all -> 0x00ae }
            java.lang.String r18 = r18.toString()     // Catch:{ all -> 0x00ae }
            android.util.Log.d(r17, r18)     // Catch:{ all -> 0x00ae }
            android.hardware.Camera r17 = android.hardware.Camera.open(r7)     // Catch:{ RuntimeException -> 0x00b1 }
            r0 = r17
            r1 = r23
            r1.mCamera = r0     // Catch:{ RuntimeException -> 0x00b1 }
            r9 = 1
        L_0x0080:
            if (r9 == 0) goto L_0x00de
        L_0x0082:
            r0 = r23
            android.hardware.Camera r0 = r0.mCamera     // Catch:{ all -> 0x00ae }
            r17 = r0
            if (r17 != 0) goto L_0x01dc
            r17 = 0
            monitor-exit(r23)     // Catch:{ all -> 0x00ae }
        L_0x008d:
            return r17
        L_0x008e:
            r10 = move-exception
            java.lang.String r17 = "JavaCameraView"
            java.lang.StringBuilder r18 = new java.lang.StringBuilder     // Catch:{ all -> 0x00ae }
            r18.<init>()     // Catch:{ all -> 0x00ae }
            java.lang.String r19 = "Camera is not available (in use or does not exist): "
            java.lang.StringBuilder r18 = r18.append(r19)     // Catch:{ all -> 0x00ae }
            java.lang.String r19 = r10.getLocalizedMessage()     // Catch:{ all -> 0x00ae }
            java.lang.StringBuilder r18 = r18.append(r19)     // Catch:{ all -> 0x00ae }
            java.lang.String r18 = r18.toString()     // Catch:{ all -> 0x00ae }
            android.util.Log.e(r17, r18)     // Catch:{ all -> 0x00ae }
            goto L_0x0034
        L_0x00ae:
            r17 = move-exception
            monitor-exit(r23)     // Catch:{ all -> 0x00ae }
            throw r17
        L_0x00b1:
            r10 = move-exception
            java.lang.String r17 = "JavaCameraView"
            java.lang.StringBuilder r18 = new java.lang.StringBuilder     // Catch:{ all -> 0x00ae }
            r18.<init>()     // Catch:{ all -> 0x00ae }
            java.lang.String r19 = "Camera #"
            java.lang.StringBuilder r18 = r18.append(r19)     // Catch:{ all -> 0x00ae }
            r0 = r18
            java.lang.StringBuilder r18 = r0.append(r7)     // Catch:{ all -> 0x00ae }
            java.lang.String r19 = "failed to open: "
            java.lang.StringBuilder r18 = r18.append(r19)     // Catch:{ all -> 0x00ae }
            java.lang.String r19 = r10.getLocalizedMessage()     // Catch:{ all -> 0x00ae }
            java.lang.StringBuilder r18 = r18.append(r19)     // Catch:{ all -> 0x00ae }
            java.lang.String r18 = r18.toString()     // Catch:{ all -> 0x00ae }
            android.util.Log.e(r17, r18)     // Catch:{ all -> 0x00ae }
            goto L_0x0080
        L_0x00de:
            int r7 = r7 + 1
            goto L_0x0048
        L_0x00e2:
            int r17 = android.os.Build.VERSION.SDK_INT     // Catch:{ all -> 0x00ae }
            r18 = 9
            r0 = r17
            r1 = r18
            if (r0 < r1) goto L_0x0082
            r0 = r23
            int r12 = r0.mCameraIndex     // Catch:{ all -> 0x00ae }
            r0 = r23
            int r0 = r0.mCameraIndex     // Catch:{ all -> 0x00ae }
            r17 = r0
            r18 = 99
            r0 = r17
            r1 = r18
            if (r0 != r1) goto L_0x0133
            java.lang.String r17 = "JavaCameraView"
            java.lang.String r18 = "Trying to open back camera"
            android.util.Log.i(r17, r18)     // Catch:{ all -> 0x00ae }
            android.hardware.Camera$CameraInfo r8 = new android.hardware.Camera$CameraInfo     // Catch:{ all -> 0x00ae }
            r8.<init>()     // Catch:{ all -> 0x00ae }
            r7 = 0
        L_0x010d:
            int r17 = android.hardware.Camera.getNumberOfCameras()     // Catch:{ all -> 0x00ae }
            r0 = r17
            if (r7 >= r0) goto L_0x011f
            android.hardware.Camera.getCameraInfo(r7, r8)     // Catch:{ all -> 0x00ae }
            int r0 = r8.facing     // Catch:{ all -> 0x00ae }
            r17 = r0
            if (r17 != 0) goto L_0x0130
            r12 = r7
        L_0x011f:
            r17 = 99
            r0 = r17
            if (r12 != r0) goto L_0x016c
            java.lang.String r17 = "JavaCameraView"
            java.lang.String r18 = "Back camera not found!"
            android.util.Log.e(r17, r18)     // Catch:{ all -> 0x00ae }
            goto L_0x0082
        L_0x0130:
            int r7 = r7 + 1
            goto L_0x010d
        L_0x0133:
            r0 = r23
            int r0 = r0.mCameraIndex     // Catch:{ all -> 0x00ae }
            r17 = r0
            r18 = 98
            r0 = r17
            r1 = r18
            if (r0 != r1) goto L_0x011f
            java.lang.String r17 = "JavaCameraView"
            java.lang.String r18 = "Trying to open front camera"
            android.util.Log.i(r17, r18)     // Catch:{ all -> 0x00ae }
            android.hardware.Camera$CameraInfo r8 = new android.hardware.Camera$CameraInfo     // Catch:{ all -> 0x00ae }
            r8.<init>()     // Catch:{ all -> 0x00ae }
            r7 = 0
        L_0x0150:
            int r17 = android.hardware.Camera.getNumberOfCameras()     // Catch:{ all -> 0x00ae }
            r0 = r17
            if (r7 >= r0) goto L_0x011f
            android.hardware.Camera.getCameraInfo(r7, r8)     // Catch:{ all -> 0x00ae }
            int r0 = r8.facing     // Catch:{ all -> 0x00ae }
            r17 = r0
            r18 = 1
            r0 = r17
            r1 = r18
            if (r0 != r1) goto L_0x0169
            r12 = r7
            goto L_0x011f
        L_0x0169:
            int r7 = r7 + 1
            goto L_0x0150
        L_0x016c:
            r17 = 98
            r0 = r17
            if (r12 != r0) goto L_0x017d
            java.lang.String r17 = "JavaCameraView"
            java.lang.String r18 = "Front camera not found!"
            android.util.Log.e(r17, r18)     // Catch:{ all -> 0x00ae }
            goto L_0x0082
        L_0x017d:
            java.lang.String r17 = "JavaCameraView"
            java.lang.StringBuilder r18 = new java.lang.StringBuilder     // Catch:{ all -> 0x00ae }
            r18.<init>()     // Catch:{ all -> 0x00ae }
            java.lang.String r19 = "Trying to open camera with new open("
            java.lang.StringBuilder r18 = r18.append(r19)     // Catch:{ all -> 0x00ae }
            java.lang.Integer r19 = java.lang.Integer.valueOf(r12)     // Catch:{ all -> 0x00ae }
            java.lang.StringBuilder r18 = r18.append(r19)     // Catch:{ all -> 0x00ae }
            java.lang.String r19 = ")"
            java.lang.StringBuilder r18 = r18.append(r19)     // Catch:{ all -> 0x00ae }
            java.lang.String r18 = r18.toString()     // Catch:{ all -> 0x00ae }
            android.util.Log.d(r17, r18)     // Catch:{ all -> 0x00ae }
            android.hardware.Camera r17 = android.hardware.Camera.open(r12)     // Catch:{ RuntimeException -> 0x01ae }
            r0 = r17
            r1 = r23
            r1.mCamera = r0     // Catch:{ RuntimeException -> 0x01ae }
            goto L_0x0082
        L_0x01ae:
            r10 = move-exception
            java.lang.String r17 = "JavaCameraView"
            java.lang.StringBuilder r18 = new java.lang.StringBuilder     // Catch:{ all -> 0x00ae }
            r18.<init>()     // Catch:{ all -> 0x00ae }
            java.lang.String r19 = "Camera #"
            java.lang.StringBuilder r18 = r18.append(r19)     // Catch:{ all -> 0x00ae }
            r0 = r18
            java.lang.StringBuilder r18 = r0.append(r12)     // Catch:{ all -> 0x00ae }
            java.lang.String r19 = "failed to open: "
            java.lang.StringBuilder r18 = r18.append(r19)     // Catch:{ all -> 0x00ae }
            java.lang.String r19 = r10.getLocalizedMessage()     // Catch:{ all -> 0x00ae }
            java.lang.StringBuilder r18 = r18.append(r19)     // Catch:{ all -> 0x00ae }
            java.lang.String r18 = r18.toString()     // Catch:{ all -> 0x00ae }
            android.util.Log.e(r17, r18)     // Catch:{ all -> 0x00ae }
            goto L_0x0082
        L_0x01dc:
            r0 = r23
            android.hardware.Camera r0 = r0.mCamera     // Catch:{ Exception -> 0x0492 }
            r17 = r0
            android.hardware.Camera$Parameters r13 = r17.getParameters()     // Catch:{ Exception -> 0x0492 }
            java.lang.String r17 = "JavaCameraView"
            java.lang.String r18 = "getSupportedPreviewSizes()"
            android.util.Log.d(r17, r18)     // Catch:{ Exception -> 0x0492 }
            java.util.List r16 = r13.getSupportedPreviewSizes()     // Catch:{ Exception -> 0x0492 }
            if (r16 == 0) goto L_0x04a4
            org.opencv.android.JavaCameraView$JavaCameraSizeAccessor r17 = new org.opencv.android.JavaCameraView$JavaCameraSizeAccessor     // Catch:{ Exception -> 0x0492 }
            r17.<init>()     // Catch:{ Exception -> 0x0492 }
            r0 = r23
            r1 = r16
            r2 = r17
            r3 = r24
            r4 = r25
            org.opencv.core.Size r11 = r0.calculateCameraFrameSize(r1, r2, r3, r4)     // Catch:{ Exception -> 0x0492 }
            r17 = 17
            r0 = r17
            r13.setPreviewFormat(r0)     // Catch:{ Exception -> 0x0492 }
            java.lang.String r17 = "JavaCameraView"
            java.lang.StringBuilder r18 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0492 }
            r18.<init>()     // Catch:{ Exception -> 0x0492 }
            java.lang.String r19 = "Set preview size to "
            java.lang.StringBuilder r18 = r18.append(r19)     // Catch:{ Exception -> 0x0492 }
            double r0 = r11.width     // Catch:{ Exception -> 0x0492 }
            r20 = r0
            r0 = r20
            int r0 = (int) r0     // Catch:{ Exception -> 0x0492 }
            r19 = r0
            java.lang.Integer r19 = java.lang.Integer.valueOf(r19)     // Catch:{ Exception -> 0x0492 }
            java.lang.StringBuilder r18 = r18.append(r19)     // Catch:{ Exception -> 0x0492 }
            java.lang.String r19 = "x"
            java.lang.StringBuilder r18 = r18.append(r19)     // Catch:{ Exception -> 0x0492 }
            double r0 = r11.height     // Catch:{ Exception -> 0x0492 }
            r20 = r0
            r0 = r20
            int r0 = (int) r0     // Catch:{ Exception -> 0x0492 }
            r19 = r0
            java.lang.Integer r19 = java.lang.Integer.valueOf(r19)     // Catch:{ Exception -> 0x0492 }
            java.lang.StringBuilder r18 = r18.append(r19)     // Catch:{ Exception -> 0x0492 }
            java.lang.String r18 = r18.toString()     // Catch:{ Exception -> 0x0492 }
            android.util.Log.d(r17, r18)     // Catch:{ Exception -> 0x0492 }
            double r0 = r11.width     // Catch:{ Exception -> 0x0492 }
            r18 = r0
            r0 = r18
            int r0 = (int) r0     // Catch:{ Exception -> 0x0492 }
            r17 = r0
            double r0 = r11.height     // Catch:{ Exception -> 0x0492 }
            r18 = r0
            r0 = r18
            int r0 = (int) r0     // Catch:{ Exception -> 0x0492 }
            r18 = r0
            r0 = r17
            r1 = r18
            r13.setPreviewSize(r0, r1)     // Catch:{ Exception -> 0x0492 }
            int r17 = android.os.Build.VERSION.SDK_INT     // Catch:{ Exception -> 0x0492 }
            r18 = 14
            r0 = r17
            r1 = r18
            if (r0 < r1) goto L_0x0283
            java.lang.String r17 = android.os.Build.MODEL     // Catch:{ Exception -> 0x0492 }
            java.lang.String r18 = "GT-I9100"
            boolean r17 = r17.equals(r18)     // Catch:{ Exception -> 0x0492 }
            if (r17 != 0) goto L_0x0283
            r17 = 1
            r0 = r17
            r13.setRecordingHint(r0)     // Catch:{ Exception -> 0x0492 }
        L_0x0283:
            java.util.List r6 = r13.getSupportedFocusModes()     // Catch:{ Exception -> 0x0492 }
            if (r6 == 0) goto L_0x029c
            java.lang.String r17 = "continuous-video"
            r0 = r17
            boolean r17 = r6.contains(r0)     // Catch:{ Exception -> 0x0492 }
            if (r17 == 0) goto L_0x029c
            java.lang.String r17 = "continuous-video"
            r0 = r17
            r13.setFocusMode(r0)     // Catch:{ Exception -> 0x0492 }
        L_0x029c:
            r0 = r23
            android.hardware.Camera r0 = r0.mCamera     // Catch:{ Exception -> 0x0492 }
            r17 = r0
            r0 = r17
            r0.setParameters(r13)     // Catch:{ Exception -> 0x0492 }
            r0 = r23
            android.hardware.Camera r0 = r0.mCamera     // Catch:{ Exception -> 0x0492 }
            r17 = r0
            android.hardware.Camera$Parameters r13 = r17.getParameters()     // Catch:{ Exception -> 0x0492 }
            android.hardware.Camera$Size r17 = r13.getPreviewSize()     // Catch:{ Exception -> 0x0492 }
            r0 = r17
            int r0 = r0.width     // Catch:{ Exception -> 0x0492 }
            r17 = r0
            r0 = r17
            r1 = r23
            r1.mFrameWidth = r0     // Catch:{ Exception -> 0x0492 }
            android.hardware.Camera$Size r17 = r13.getPreviewSize()     // Catch:{ Exception -> 0x0492 }
            r0 = r17
            int r0 = r0.height     // Catch:{ Exception -> 0x0492 }
            r17 = r0
            r0 = r17
            r1 = r23
            r1.mFrameHeight = r0     // Catch:{ Exception -> 0x0492 }
            android.view.ViewGroup$LayoutParams r17 = r23.getLayoutParams()     // Catch:{ Exception -> 0x0492 }
            r0 = r17
            int r0 = r0.width     // Catch:{ Exception -> 0x0492 }
            r17 = r0
            r18 = -1
            r0 = r17
            r1 = r18
            if (r0 != r1) goto L_0x0488
            android.view.ViewGroup$LayoutParams r17 = r23.getLayoutParams()     // Catch:{ Exception -> 0x0492 }
            r0 = r17
            int r0 = r0.height     // Catch:{ Exception -> 0x0492 }
            r17 = r0
            r18 = -1
            r0 = r17
            r1 = r18
            if (r0 != r1) goto L_0x0488
            r0 = r25
            float r0 = (float) r0     // Catch:{ Exception -> 0x0492 }
            r17 = r0
            r0 = r23
            int r0 = r0.mFrameHeight     // Catch:{ Exception -> 0x0492 }
            r18 = r0
            r0 = r18
            float r0 = (float) r0     // Catch:{ Exception -> 0x0492 }
            r18 = r0
            float r17 = r17 / r18
            r0 = r24
            float r0 = (float) r0     // Catch:{ Exception -> 0x0492 }
            r18 = r0
            r0 = r23
            int r0 = r0.mFrameWidth     // Catch:{ Exception -> 0x0492 }
            r19 = r0
            r0 = r19
            float r0 = (float) r0     // Catch:{ Exception -> 0x0492 }
            r19 = r0
            float r18 = r18 / r19
            float r17 = java.lang.Math.min(r17, r18)     // Catch:{ Exception -> 0x0492 }
            r0 = r17
            r1 = r23
            r1.mScale = r0     // Catch:{ Exception -> 0x0492 }
        L_0x0323:
            r0 = r23
            org.opencv.android.FpsMeter r0 = r0.mFpsMeter     // Catch:{ Exception -> 0x0492 }
            r17 = r0
            if (r17 == 0) goto L_0x0340
            r0 = r23
            org.opencv.android.FpsMeter r0 = r0.mFpsMeter     // Catch:{ Exception -> 0x0492 }
            r17 = r0
            r0 = r23
            int r0 = r0.mFrameWidth     // Catch:{ Exception -> 0x0492 }
            r18 = r0
            r0 = r23
            int r0 = r0.mFrameHeight     // Catch:{ Exception -> 0x0492 }
            r19 = r0
            r17.setResolution(r18, r19)     // Catch:{ Exception -> 0x0492 }
        L_0x0340:
            r0 = r23
            int r0 = r0.mFrameWidth     // Catch:{ Exception -> 0x0492 }
            r17 = r0
            r0 = r23
            int r0 = r0.mFrameHeight     // Catch:{ Exception -> 0x0492 }
            r18 = r0
            int r15 = r17 * r18
            int r17 = r13.getPreviewFormat()     // Catch:{ Exception -> 0x0492 }
            int r17 = android.graphics.ImageFormat.getBitsPerPixel(r17)     // Catch:{ Exception -> 0x0492 }
            int r17 = r17 * r15
            int r15 = r17 / 8
            byte[] r0 = new byte[r15]     // Catch:{ Exception -> 0x0492 }
            r17 = r0
            r0 = r17
            r1 = r23
            r1.mBuffer = r0     // Catch:{ Exception -> 0x0492 }
            r0 = r23
            android.hardware.Camera r0 = r0.mCamera     // Catch:{ Exception -> 0x0492 }
            r17 = r0
            r0 = r23
            byte[] r0 = r0.mBuffer     // Catch:{ Exception -> 0x0492 }
            r18 = r0
            r17.addCallbackBuffer(r18)     // Catch:{ Exception -> 0x0492 }
            r0 = r23
            android.hardware.Camera r0 = r0.mCamera     // Catch:{ Exception -> 0x0492 }
            r17 = r0
            r0 = r17
            r1 = r23
            r0.setPreviewCallbackWithBuffer(r1)     // Catch:{ Exception -> 0x0492 }
            r17 = 2
            r0 = r17
            org.opencv.core.Mat[] r0 = new org.opencv.core.Mat[r0]     // Catch:{ Exception -> 0x0492 }
            r17 = r0
            r0 = r17
            r1 = r23
            r1.mFrameChain = r0     // Catch:{ Exception -> 0x0492 }
            r0 = r23
            org.opencv.core.Mat[] r0 = r0.mFrameChain     // Catch:{ Exception -> 0x0492 }
            r17 = r0
            r18 = 0
            org.opencv.core.Mat r19 = new org.opencv.core.Mat     // Catch:{ Exception -> 0x0492 }
            r0 = r23
            int r0 = r0.mFrameHeight     // Catch:{ Exception -> 0x0492 }
            r20 = r0
            r0 = r23
            int r0 = r0.mFrameHeight     // Catch:{ Exception -> 0x0492 }
            r21 = r0
            int r21 = r21 / 2
            int r20 = r20 + r21
            r0 = r23
            int r0 = r0.mFrameWidth     // Catch:{ Exception -> 0x0492 }
            r21 = r0
            int r22 = org.opencv.core.CvType.CV_8UC1     // Catch:{ Exception -> 0x0492 }
            r19.<init>((int) r20, (int) r21, (int) r22)     // Catch:{ Exception -> 0x0492 }
            r17[r18] = r19     // Catch:{ Exception -> 0x0492 }
            r0 = r23
            org.opencv.core.Mat[] r0 = r0.mFrameChain     // Catch:{ Exception -> 0x0492 }
            r17 = r0
            r18 = 1
            org.opencv.core.Mat r19 = new org.opencv.core.Mat     // Catch:{ Exception -> 0x0492 }
            r0 = r23
            int r0 = r0.mFrameHeight     // Catch:{ Exception -> 0x0492 }
            r20 = r0
            r0 = r23
            int r0 = r0.mFrameHeight     // Catch:{ Exception -> 0x0492 }
            r21 = r0
            int r21 = r21 / 2
            int r20 = r20 + r21
            r0 = r23
            int r0 = r0.mFrameWidth     // Catch:{ Exception -> 0x0492 }
            r21 = r0
            int r22 = org.opencv.core.CvType.CV_8UC1     // Catch:{ Exception -> 0x0492 }
            r19.<init>((int) r20, (int) r21, (int) r22)     // Catch:{ Exception -> 0x0492 }
            r17[r18] = r19     // Catch:{ Exception -> 0x0492 }
            r23.AllocateCache()     // Catch:{ Exception -> 0x0492 }
            r17 = 2
            r0 = r17
            org.opencv.android.JavaCameraView$JavaCameraFrame[] r0 = new org.opencv.android.JavaCameraView.JavaCameraFrame[r0]     // Catch:{ Exception -> 0x0492 }
            r17 = r0
            r0 = r17
            r1 = r23
            r1.mCameraFrame = r0     // Catch:{ Exception -> 0x0492 }
            r0 = r23
            org.opencv.android.JavaCameraView$JavaCameraFrame[] r0 = r0.mCameraFrame     // Catch:{ Exception -> 0x0492 }
            r17 = r0
            r18 = 0
            org.opencv.android.JavaCameraView$JavaCameraFrame r19 = new org.opencv.android.JavaCameraView$JavaCameraFrame     // Catch:{ Exception -> 0x0492 }
            r0 = r23
            org.opencv.core.Mat[] r0 = r0.mFrameChain     // Catch:{ Exception -> 0x0492 }
            r20 = r0
            r21 = 0
            r20 = r20[r21]     // Catch:{ Exception -> 0x0492 }
            r0 = r23
            int r0 = r0.mFrameWidth     // Catch:{ Exception -> 0x0492 }
            r21 = r0
            r0 = r23
            int r0 = r0.mFrameHeight     // Catch:{ Exception -> 0x0492 }
            r22 = r0
            r0 = r19
            r1 = r23
            r2 = r20
            r3 = r21
            r4 = r22
            r0.<init>(r2, r3, r4)     // Catch:{ Exception -> 0x0492 }
            r17[r18] = r19     // Catch:{ Exception -> 0x0492 }
            r0 = r23
            org.opencv.android.JavaCameraView$JavaCameraFrame[] r0 = r0.mCameraFrame     // Catch:{ Exception -> 0x0492 }
            r17 = r0
            r18 = 1
            org.opencv.android.JavaCameraView$JavaCameraFrame r19 = new org.opencv.android.JavaCameraView$JavaCameraFrame     // Catch:{ Exception -> 0x0492 }
            r0 = r23
            org.opencv.core.Mat[] r0 = r0.mFrameChain     // Catch:{ Exception -> 0x0492 }
            r20 = r0
            r21 = 1
            r20 = r20[r21]     // Catch:{ Exception -> 0x0492 }
            r0 = r23
            int r0 = r0.mFrameWidth     // Catch:{ Exception -> 0x0492 }
            r21 = r0
            r0 = r23
            int r0 = r0.mFrameHeight     // Catch:{ Exception -> 0x0492 }
            r22 = r0
            r0 = r19
            r1 = r23
            r2 = r20
            r3 = r21
            r4 = r22
            r0.<init>(r2, r3, r4)     // Catch:{ Exception -> 0x0492 }
            r17[r18] = r19     // Catch:{ Exception -> 0x0492 }
            int r17 = android.os.Build.VERSION.SDK_INT     // Catch:{ Exception -> 0x0492 }
            r18 = 11
            r0 = r17
            r1 = r18
            if (r0 < r1) goto L_0x0498
            android.graphics.SurfaceTexture r17 = new android.graphics.SurfaceTexture     // Catch:{ Exception -> 0x0492 }
            r18 = 10
            r17.<init>(r18)     // Catch:{ Exception -> 0x0492 }
            r0 = r17
            r1 = r23
            r1.mSurfaceTexture = r0     // Catch:{ Exception -> 0x0492 }
            r0 = r23
            android.hardware.Camera r0 = r0.mCamera     // Catch:{ Exception -> 0x0492 }
            r17 = r0
            r0 = r23
            android.graphics.SurfaceTexture r0 = r0.mSurfaceTexture     // Catch:{ Exception -> 0x0492 }
            r18 = r0
            r17.setPreviewTexture(r18)     // Catch:{ Exception -> 0x0492 }
        L_0x0471:
            java.lang.String r17 = "JavaCameraView"
            java.lang.String r18 = "startPreview"
            android.util.Log.d(r17, r18)     // Catch:{ Exception -> 0x0492 }
            r0 = r23
            android.hardware.Camera r0 = r0.mCamera     // Catch:{ Exception -> 0x0492 }
            r17 = r0
            r17.startPreview()     // Catch:{ Exception -> 0x0492 }
        L_0x0483:
            monitor-exit(r23)     // Catch:{ all -> 0x00ae }
            r17 = r14
            goto L_0x008d
        L_0x0488:
            r17 = 0
            r0 = r17
            r1 = r23
            r1.mScale = r0     // Catch:{ Exception -> 0x0492 }
            goto L_0x0323
        L_0x0492:
            r10 = move-exception
            r14 = 0
            r10.printStackTrace()     // Catch:{ all -> 0x00ae }
            goto L_0x0483
        L_0x0498:
            r0 = r23
            android.hardware.Camera r0 = r0.mCamera     // Catch:{ Exception -> 0x0492 }
            r17 = r0
            r18 = 0
            r17.setPreviewDisplay(r18)     // Catch:{ Exception -> 0x0492 }
            goto L_0x0471
        L_0x04a4:
            r14 = 0
            goto L_0x0483
        */
        throw new UnsupportedOperationException("Method not decompiled: org.opencv.android.JavaCameraView.initializeCamera(int, int):boolean");
    }

    /* access modifiers changed from: protected */
    public void releaseCamera() {
        synchronized (this) {
            if (this.mCamera != null) {
                this.mCamera.stopPreview();
                this.mCamera.setPreviewCallback((Camera.PreviewCallback) null);
                this.mCamera.release();
            }
            this.mCamera = null;
            if (this.mFrameChain != null) {
                this.mFrameChain[0].release();
                this.mFrameChain[1].release();
            }
            if (this.mCameraFrame != null) {
                this.mCameraFrame[0].release();
                this.mCameraFrame[1].release();
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean connectCamera(int width, int height) {
        Log.d(TAG, "Connecting to camera");
        if (!initializeCamera(width, height)) {
            return false;
        }
        this.mCameraFrameReady = false;
        Log.d(TAG, "Starting processing thread");
        this.mStopThread = false;
        this.mThread = new Thread(new CameraWorker());
        this.mThread.start();
        return true;
    }

    /* access modifiers changed from: protected */
    public void disconnectCamera() {
        Log.d(TAG, "Disconnecting from camera");
        try {
            this.mStopThread = true;
            Log.d(TAG, "Notify thread");
            synchronized (this) {
                notify();
            }
            Log.d(TAG, "Wating for thread");
            if (this.mThread != null) {
                this.mThread.join();
            }
            this.mThread = null;
        } catch (InterruptedException e) {
            try {
                e.printStackTrace();
            } finally {
                this.mThread = null;
            }
        }
        releaseCamera();
        this.mCameraFrameReady = false;
    }

    public void onPreviewFrame(byte[] frame, Camera arg1) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Preview Frame received. Frame size: " + frame.length);
        }
        synchronized (this) {
            this.mFrameChain[this.mChainIdx].put(0, 0, frame);
            this.mCameraFrameReady = true;
            notify();
        }
        if (this.mCamera != null) {
            this.mCamera.addCallbackBuffer(this.mBuffer);
        }
    }

    private class JavaCameraFrame implements CameraBridgeViewBase.CvCameraViewFrame {
        private int mHeight;
        private Mat mRgba = new Mat();
        private int mWidth;
        private Mat mYuvFrameData;

        public Mat gray() {
            return this.mYuvFrameData.submat(0, this.mHeight, 0, this.mWidth);
        }

        public Mat rgba() {
            Imgproc.cvtColor(this.mYuvFrameData, this.mRgba, 96, 4);
            return this.mRgba;
        }

        public JavaCameraFrame(Mat Yuv420sp, int width, int height) {
            this.mWidth = width;
            this.mHeight = height;
            this.mYuvFrameData = Yuv420sp;
        }

        public void release() {
            this.mRgba.release();
        }
    }

    private class CameraWorker implements Runnable {
        private CameraWorker() {
        }

        public void run() {
            do {
                boolean hasFrame = false;
                synchronized (JavaCameraView.this) {
                    while (!JavaCameraView.this.mCameraFrameReady && !JavaCameraView.this.mStopThread) {
                        try {
                            JavaCameraView.this.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (JavaCameraView.this.mCameraFrameReady) {
                        int unused = JavaCameraView.this.mChainIdx = 1 - JavaCameraView.this.mChainIdx;
                        boolean unused2 = JavaCameraView.this.mCameraFrameReady = false;
                        hasFrame = true;
                    }
                }
                if (!JavaCameraView.this.mStopThread && hasFrame && !JavaCameraView.this.mFrameChain[1 - JavaCameraView.this.mChainIdx].empty()) {
                    JavaCameraView.this.deliverAndDrawFrame(JavaCameraView.this.mCameraFrame[1 - JavaCameraView.this.mChainIdx]);
                }
            } while (!JavaCameraView.this.mStopThread);
            Log.d(JavaCameraView.TAG, "Finish processing thread");
        }
    }
}
