package org.opencv.android;

import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import java.util.Arrays;
import java.util.concurrent.Semaphore;

@TargetApi(21)
public class Camera2Renderer extends CameraGLRendererBase {
    protected final String LOGTAG = "Camera2Renderer";
    /* access modifiers changed from: private */
    public Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    /* access modifiers changed from: private */
    public CameraDevice mCameraDevice;
    private String mCameraID;
    /* access modifiers changed from: private */
    public Semaphore mCameraOpenCloseLock = new Semaphore(1);
    /* access modifiers changed from: private */
    public CameraCaptureSession mCaptureSession;
    /* access modifiers changed from: private */
    public CaptureRequest.Builder mPreviewRequestBuilder;
    private Size mPreviewSize = new Size(-1, -1);
    private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {
        public void onOpened(CameraDevice cameraDevice) {
            CameraDevice unused = Camera2Renderer.this.mCameraDevice = cameraDevice;
            Camera2Renderer.this.mCameraOpenCloseLock.release();
            Camera2Renderer.this.createCameraPreviewSession();
        }

        public void onDisconnected(CameraDevice cameraDevice) {
            cameraDevice.close();
            CameraDevice unused = Camera2Renderer.this.mCameraDevice = null;
            Camera2Renderer.this.mCameraOpenCloseLock.release();
        }

        public void onError(CameraDevice cameraDevice, int error) {
            cameraDevice.close();
            CameraDevice unused = Camera2Renderer.this.mCameraDevice = null;
            Camera2Renderer.this.mCameraOpenCloseLock.release();
        }
    };

    Camera2Renderer(CameraGLSurfaceView view) {
        super(view);
    }

    /* access modifiers changed from: protected */
    public void doStart() {
        Log.d("Camera2Renderer", "doStart");
        startBackgroundThread();
        super.doStart();
    }

    /* access modifiers changed from: protected */
    public void doStop() {
        Log.d("Camera2Renderer", "doStop");
        super.doStop();
        stopBackgroundThread();
    }

    /* access modifiers changed from: package-private */
    public boolean cacPreviewSize(int width, int height) {
        Log.i("Camera2Renderer", "cacPreviewSize: " + width + "x" + height);
        if (this.mCameraID == null) {
            Log.e("Camera2Renderer", "Camera isn't initialized!");
            return false;
        }
        try {
            int bestWidth = 0;
            int bestHeight = 0;
            float aspect = ((float) width) / ((float) height);
            for (Size psize : ((StreamConfigurationMap) ((CameraManager) this.mView.getContext().getSystemService("camera")).getCameraCharacteristics(this.mCameraID).get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)).getOutputSizes(SurfaceTexture.class)) {
                int w = psize.getWidth();
                int h = psize.getHeight();
                Log.d("Camera2Renderer", "trying size: " + w + "x" + h);
                if (width >= w && height >= h && bestWidth <= w && bestHeight <= h && ((double) Math.abs(aspect - (((float) w) / ((float) h)))) < 0.2d) {
                    bestWidth = w;
                    bestHeight = h;
                }
            }
            Log.i("Camera2Renderer", "best size: " + bestWidth + "x" + bestHeight);
            if (bestWidth == 0 || bestHeight == 0 || (this.mPreviewSize.getWidth() == bestWidth && this.mPreviewSize.getHeight() == bestHeight)) {
                return false;
            }
            this.mPreviewSize = new Size(bestWidth, bestHeight);
            return true;
        } catch (CameraAccessException e) {
            Log.e("Camera2Renderer", "cacPreviewSize - Camera Access Exception");
            return false;
        } catch (IllegalArgumentException e2) {
            Log.e("Camera2Renderer", "cacPreviewSize - Illegal Argument Exception");
            return false;
        } catch (SecurityException e3) {
            Log.e("Camera2Renderer", "cacPreviewSize - Security Exception");
            return false;
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0085, code lost:
        r9.mCameraID = r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void openCamera(int r10) {
        /*
            r9 = this;
            r5 = 0
            java.lang.String r6 = "Camera2Renderer"
            java.lang.String r7 = "openCamera"
            android.util.Log.i(r6, r7)
            org.opencv.android.CameraGLSurfaceView r6 = r9.mView
            android.content.Context r6 = r6.getContext()
            java.lang.String r7 = "camera"
            java.lang.Object r4 = r6.getSystemService(r7)
            android.hardware.camera2.CameraManager r4 = (android.hardware.camera2.CameraManager) r4
            java.lang.String[] r0 = r4.getCameraIdList()     // Catch:{ CameraAccessException -> 0x004b, IllegalArgumentException -> 0x0088, SecurityException -> 0x00be, InterruptedException -> 0x00ca }
            int r6 = r0.length     // Catch:{ CameraAccessException -> 0x004b, IllegalArgumentException -> 0x0088, SecurityException -> 0x00be, InterruptedException -> 0x00ca }
            if (r6 != 0) goto L_0x002a
            java.lang.String r5 = "Camera2Renderer"
            java.lang.String r6 = "Error: camera isn't detected."
            android.util.Log.e(r5, r6)     // Catch:{ CameraAccessException -> 0x004b, IllegalArgumentException -> 0x0088, SecurityException -> 0x00be, InterruptedException -> 0x00ca }
        L_0x0029:
            return
        L_0x002a:
            r6 = -1
            if (r10 != r6) goto L_0x0056
            r5 = 0
            r5 = r0[r5]     // Catch:{ CameraAccessException -> 0x004b, IllegalArgumentException -> 0x0088, SecurityException -> 0x00be, InterruptedException -> 0x00ca }
            r9.mCameraID = r5     // Catch:{ CameraAccessException -> 0x004b, IllegalArgumentException -> 0x0088, SecurityException -> 0x00be, InterruptedException -> 0x00ca }
        L_0x0032:
            java.lang.String r5 = r9.mCameraID     // Catch:{ CameraAccessException -> 0x004b, IllegalArgumentException -> 0x0088, SecurityException -> 0x00be, InterruptedException -> 0x00ca }
            if (r5 == 0) goto L_0x0029
            java.util.concurrent.Semaphore r5 = r9.mCameraOpenCloseLock     // Catch:{ CameraAccessException -> 0x004b, IllegalArgumentException -> 0x0088, SecurityException -> 0x00be, InterruptedException -> 0x00ca }
            r6 = 2500(0x9c4, double:1.235E-320)
            java.util.concurrent.TimeUnit r8 = java.util.concurrent.TimeUnit.MILLISECONDS     // Catch:{ CameraAccessException -> 0x004b, IllegalArgumentException -> 0x0088, SecurityException -> 0x00be, InterruptedException -> 0x00ca }
            boolean r5 = r5.tryAcquire(r6, r8)     // Catch:{ CameraAccessException -> 0x004b, IllegalArgumentException -> 0x0088, SecurityException -> 0x00be, InterruptedException -> 0x00ca }
            if (r5 != 0) goto L_0x0097
            java.lang.RuntimeException r5 = new java.lang.RuntimeException     // Catch:{ CameraAccessException -> 0x004b, IllegalArgumentException -> 0x0088, SecurityException -> 0x00be, InterruptedException -> 0x00ca }
            java.lang.String r6 = "Time out waiting to lock camera opening."
            r5.<init>(r6)     // Catch:{ CameraAccessException -> 0x004b, IllegalArgumentException -> 0x0088, SecurityException -> 0x00be, InterruptedException -> 0x00ca }
            throw r5     // Catch:{ CameraAccessException -> 0x004b, IllegalArgumentException -> 0x0088, SecurityException -> 0x00be, InterruptedException -> 0x00ca }
        L_0x004b:
            r3 = move-exception
            java.lang.String r5 = "Camera2Renderer"
            java.lang.String r6 = "OpenCamera - Camera Access Exception"
            android.util.Log.e(r5, r6)
            goto L_0x0029
        L_0x0056:
            int r7 = r0.length     // Catch:{ CameraAccessException -> 0x004b, IllegalArgumentException -> 0x0088, SecurityException -> 0x00be, InterruptedException -> 0x00ca }
            r6 = r5
        L_0x0058:
            if (r6 >= r7) goto L_0x0032
            r1 = r0[r6]     // Catch:{ CameraAccessException -> 0x004b, IllegalArgumentException -> 0x0088, SecurityException -> 0x00be, InterruptedException -> 0x00ca }
            android.hardware.camera2.CameraCharacteristics r2 = r4.getCameraCharacteristics(r1)     // Catch:{ CameraAccessException -> 0x004b, IllegalArgumentException -> 0x0088, SecurityException -> 0x00be, InterruptedException -> 0x00ca }
            r5 = 99
            if (r10 != r5) goto L_0x0073
            android.hardware.camera2.CameraCharacteristics$Key r5 = android.hardware.camera2.CameraCharacteristics.LENS_FACING     // Catch:{ CameraAccessException -> 0x004b, IllegalArgumentException -> 0x0088, SecurityException -> 0x00be, InterruptedException -> 0x00ca }
            java.lang.Object r5 = r2.get(r5)     // Catch:{ CameraAccessException -> 0x004b, IllegalArgumentException -> 0x0088, SecurityException -> 0x00be, InterruptedException -> 0x00ca }
            java.lang.Integer r5 = (java.lang.Integer) r5     // Catch:{ CameraAccessException -> 0x004b, IllegalArgumentException -> 0x0088, SecurityException -> 0x00be, InterruptedException -> 0x00ca }
            int r5 = r5.intValue()     // Catch:{ CameraAccessException -> 0x004b, IllegalArgumentException -> 0x0088, SecurityException -> 0x00be, InterruptedException -> 0x00ca }
            r8 = 1
            if (r5 == r8) goto L_0x0085
        L_0x0073:
            r5 = 98
            if (r10 != r5) goto L_0x0093
            android.hardware.camera2.CameraCharacteristics$Key r5 = android.hardware.camera2.CameraCharacteristics.LENS_FACING     // Catch:{ CameraAccessException -> 0x004b, IllegalArgumentException -> 0x0088, SecurityException -> 0x00be, InterruptedException -> 0x00ca }
            java.lang.Object r5 = r2.get(r5)     // Catch:{ CameraAccessException -> 0x004b, IllegalArgumentException -> 0x0088, SecurityException -> 0x00be, InterruptedException -> 0x00ca }
            java.lang.Integer r5 = (java.lang.Integer) r5     // Catch:{ CameraAccessException -> 0x004b, IllegalArgumentException -> 0x0088, SecurityException -> 0x00be, InterruptedException -> 0x00ca }
            int r5 = r5.intValue()     // Catch:{ CameraAccessException -> 0x004b, IllegalArgumentException -> 0x0088, SecurityException -> 0x00be, InterruptedException -> 0x00ca }
            if (r5 != 0) goto L_0x0093
        L_0x0085:
            r9.mCameraID = r1     // Catch:{ CameraAccessException -> 0x004b, IllegalArgumentException -> 0x0088, SecurityException -> 0x00be, InterruptedException -> 0x00ca }
            goto L_0x0032
        L_0x0088:
            r3 = move-exception
            java.lang.String r5 = "Camera2Renderer"
            java.lang.String r6 = "OpenCamera - Illegal Argument Exception"
            android.util.Log.e(r5, r6)
            goto L_0x0029
        L_0x0093:
            int r5 = r6 + 1
            r6 = r5
            goto L_0x0058
        L_0x0097:
            java.lang.String r5 = "Camera2Renderer"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ CameraAccessException -> 0x004b, IllegalArgumentException -> 0x0088, SecurityException -> 0x00be, InterruptedException -> 0x00ca }
            r6.<init>()     // Catch:{ CameraAccessException -> 0x004b, IllegalArgumentException -> 0x0088, SecurityException -> 0x00be, InterruptedException -> 0x00ca }
            java.lang.String r7 = "Opening camera: "
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ CameraAccessException -> 0x004b, IllegalArgumentException -> 0x0088, SecurityException -> 0x00be, InterruptedException -> 0x00ca }
            java.lang.String r7 = r9.mCameraID     // Catch:{ CameraAccessException -> 0x004b, IllegalArgumentException -> 0x0088, SecurityException -> 0x00be, InterruptedException -> 0x00ca }
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ CameraAccessException -> 0x004b, IllegalArgumentException -> 0x0088, SecurityException -> 0x00be, InterruptedException -> 0x00ca }
            java.lang.String r6 = r6.toString()     // Catch:{ CameraAccessException -> 0x004b, IllegalArgumentException -> 0x0088, SecurityException -> 0x00be, InterruptedException -> 0x00ca }
            android.util.Log.i(r5, r6)     // Catch:{ CameraAccessException -> 0x004b, IllegalArgumentException -> 0x0088, SecurityException -> 0x00be, InterruptedException -> 0x00ca }
            java.lang.String r5 = r9.mCameraID     // Catch:{ CameraAccessException -> 0x004b, IllegalArgumentException -> 0x0088, SecurityException -> 0x00be, InterruptedException -> 0x00ca }
            android.hardware.camera2.CameraDevice$StateCallback r6 = r9.mStateCallback     // Catch:{ CameraAccessException -> 0x004b, IllegalArgumentException -> 0x0088, SecurityException -> 0x00be, InterruptedException -> 0x00ca }
            android.os.Handler r7 = r9.mBackgroundHandler     // Catch:{ CameraAccessException -> 0x004b, IllegalArgumentException -> 0x0088, SecurityException -> 0x00be, InterruptedException -> 0x00ca }
            r4.openCamera(r5, r6, r7)     // Catch:{ CameraAccessException -> 0x004b, IllegalArgumentException -> 0x0088, SecurityException -> 0x00be, InterruptedException -> 0x00ca }
            goto L_0x0029
        L_0x00be:
            r3 = move-exception
            java.lang.String r5 = "Camera2Renderer"
            java.lang.String r6 = "OpenCamera - Security Exception"
            android.util.Log.e(r5, r6)
            goto L_0x0029
        L_0x00ca:
            r3 = move-exception
            java.lang.String r5 = "Camera2Renderer"
            java.lang.String r6 = "OpenCamera - Interrupted Exception"
            android.util.Log.e(r5, r6)
            goto L_0x0029
        */
        throw new UnsupportedOperationException("Method not decompiled: org.opencv.android.Camera2Renderer.openCamera(int):void");
    }

    /* access modifiers changed from: protected */
    public void closeCamera() {
        Log.i("Camera2Renderer", "closeCamera");
        try {
            this.mCameraOpenCloseLock.acquire();
            if (this.mCaptureSession != null) {
                this.mCaptureSession.close();
                this.mCaptureSession = null;
            }
            if (this.mCameraDevice != null) {
                this.mCameraDevice.close();
                this.mCameraDevice = null;
            }
            this.mCameraOpenCloseLock.release();
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.", e);
        } catch (Throwable th) {
            this.mCameraOpenCloseLock.release();
            throw th;
        }
    }

    /* access modifiers changed from: private */
    public void createCameraPreviewSession() {
        int w = this.mPreviewSize.getWidth();
        int h = this.mPreviewSize.getHeight();
        Log.i("Camera2Renderer", "createCameraPreviewSession(" + w + "x" + h + ")");
        if (w >= 0 && h >= 0) {
            try {
                this.mCameraOpenCloseLock.acquire();
                if (this.mCameraDevice == null) {
                    this.mCameraOpenCloseLock.release();
                    Log.e("Camera2Renderer", "createCameraPreviewSession: camera isn't opened");
                } else if (this.mCaptureSession != null) {
                    this.mCameraOpenCloseLock.release();
                    Log.e("Camera2Renderer", "createCameraPreviewSession: mCaptureSession is already started");
                } else if (this.mSTexture == null) {
                    this.mCameraOpenCloseLock.release();
                    Log.e("Camera2Renderer", "createCameraPreviewSession: preview SurfaceTexture is null");
                } else {
                    this.mSTexture.setDefaultBufferSize(w, h);
                    Surface surface = new Surface(this.mSTexture);
                    this.mPreviewRequestBuilder = this.mCameraDevice.createCaptureRequest(1);
                    this.mPreviewRequestBuilder.addTarget(surface);
                    this.mCameraDevice.createCaptureSession(Arrays.asList(new Surface[]{surface}), new CameraCaptureSession.StateCallback() {
                        public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                            CameraCaptureSession unused = Camera2Renderer.this.mCaptureSession = cameraCaptureSession;
                            try {
                                Camera2Renderer.this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, 4);
                                Camera2Renderer.this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, 2);
                                Camera2Renderer.this.mCaptureSession.setRepeatingRequest(Camera2Renderer.this.mPreviewRequestBuilder.build(), (CameraCaptureSession.CaptureCallback) null, Camera2Renderer.this.mBackgroundHandler);
                                Log.i("Camera2Renderer", "CameraPreviewSession has been started");
                            } catch (CameraAccessException e) {
                                Log.e("Camera2Renderer", "createCaptureSession failed");
                            }
                            Camera2Renderer.this.mCameraOpenCloseLock.release();
                        }

                        public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                            Log.e("Camera2Renderer", "createCameraPreviewSession failed");
                            Camera2Renderer.this.mCameraOpenCloseLock.release();
                        }
                    }, this.mBackgroundHandler);
                }
            } catch (CameraAccessException e) {
                Log.e("Camera2Renderer", "createCameraPreviewSession");
            } catch (InterruptedException e2) {
                throw new RuntimeException("Interrupted while createCameraPreviewSession", e2);
            }
        }
    }

    private void startBackgroundThread() {
        Log.i("Camera2Renderer", "startBackgroundThread");
        stopBackgroundThread();
        this.mBackgroundThread = new HandlerThread("CameraBackground");
        this.mBackgroundThread.start();
        this.mBackgroundHandler = new Handler(this.mBackgroundThread.getLooper());
    }

    private void stopBackgroundThread() {
        Log.i("Camera2Renderer", "stopBackgroundThread");
        if (this.mBackgroundThread != null) {
            this.mBackgroundThread.quitSafely();
            try {
                this.mBackgroundThread.join();
                this.mBackgroundThread = null;
                this.mBackgroundHandler = null;
            } catch (InterruptedException e) {
                Log.e("Camera2Renderer", "stopBackgroundThread");
            }
        }
    }

    /* access modifiers changed from: protected */
    public void setCameraPreviewSize(int width, int height) {
        Log.i("Camera2Renderer", "setCameraPreviewSize(" + width + "x" + height + ")");
        if (this.mMaxCameraWidth > 0 && this.mMaxCameraWidth < width) {
            width = this.mMaxCameraWidth;
        }
        if (this.mMaxCameraHeight > 0 && this.mMaxCameraHeight < height) {
            height = this.mMaxCameraHeight;
        }
        try {
            this.mCameraOpenCloseLock.acquire();
            boolean needReconfig = cacPreviewSize(width, height);
            this.mCameraWidth = this.mPreviewSize.getWidth();
            this.mCameraHeight = this.mPreviewSize.getHeight();
            if (!needReconfig) {
                this.mCameraOpenCloseLock.release();
                return;
            }
            if (this.mCaptureSession != null) {
                Log.d("Camera2Renderer", "closing existing previewSession");
                this.mCaptureSession.close();
                this.mCaptureSession = null;
            }
            this.mCameraOpenCloseLock.release();
            createCameraPreviewSession();
        } catch (InterruptedException e) {
            this.mCameraOpenCloseLock.release();
            throw new RuntimeException("Interrupted while setCameraPreviewSize.", e);
        }
    }
}
