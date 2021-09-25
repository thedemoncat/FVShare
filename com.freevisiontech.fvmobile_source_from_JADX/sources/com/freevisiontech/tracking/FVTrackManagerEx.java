package com.freevisiontech.tracking;

import android.os.Handler;
import android.os.Looper;
import android.support.p001v4.internal.view.SupportMenu;
import com.freevisiontech.cameralib.FVCamera2Manager;
import com.freevisiontech.cameralib.Size;
import com.freevisiontech.cameralib.utils.CameraUtils;
import com.freevisiontech.fvmobile.application.MyApplication;
import com.freevisiontech.fvmobile.utility.Constants;
import com.freevisiontech.fvmobile.utility.SPUtils;
import com.freevisiontech.fvmobile.utility.SharePrefConstant;
import com.freevisiontech.fvmobile.utils.BleByteUtil;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.xmisc.XMiscing;

public class FVTrackManagerEx implements Runnable {
    private static final String TAG = "FVTrackManagerEx";
    private static XMiscing xmisc = null;

    /* renamed from: YK */
    private int f1117YK = 0;
    private Point center;
    private Size dimensions;
    private boolean islosted = false;
    private boolean istrack = false;
    private FVTrackObserver listener = null;
    private FVCamera2Manager manager = null;
    /* access modifiers changed from: private */
    public ObjTracking objTracking = new ObjTracking(1);
    private Rect startboundingBox = new Rect();
    private Point startboundingPt = null;
    private Thread trackthread;
    private int tracktype = 0;
    /* access modifiers changed from: private */
    public Mat yuvatfromcamera = null;

    public FVTrackManagerEx(FVCamera2Manager parent, FVTrackObserver observer, int cameraPreviewWidth, int cameraPreviewHeight) {
        this.manager = parent;
        this.listener = observer;
        this.yuvatfromcamera = new Mat((cameraPreviewHeight / 2) + cameraPreviewHeight, cameraPreviewWidth, CvType.CV_8UC1);
        if (xmisc == null) {
            xmisc = XMiscing.getInstance();
        }
    }

    public boolean initTrack() {
        this.startboundingBox = new Rect();
        this.startboundingPt = null;
        this.tracktype = 0;
        return this.objTracking.initObjTrack();
    }

    public void startTracking(Rect rect) {
        this.tracktype = 2;
        this.startboundingBox = rect;
        this.startboundingPt = null;
        this.istrack = true;
    }

    public void startTracking(Point pnt) {
        this.tracktype = 1;
        this.startboundingPt = pnt;
        this.startboundingBox = new Rect();
        this.istrack = true;
    }

    public void stopTrack() {
        this.istrack = false;
        this.startboundingBox = new Rect();
        this.startboundingPt = null;
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            public void run() {
                FVTrackManagerEx.this.yuvatfromcamera.release();
                FVTrackManagerEx.this.objTracking.clear();
            }
        }, 500);
    }

    public boolean update(byte[] frame) {
        boolean result;
        if (!this.istrack) {
            return false;
        }
        if (this.tracktype == 0) {
            return false;
        }
        byte2Mat(frame);
        if (this.tracktype == 1 && this.startboundingPt != null) {
            boolean rs = xmisc.cutDetect(this.yuvatfromcamera, this.startboundingPt, this.startboundingBox, true);
            CameraUtils.LogV("HTrack-XMiscing", "XMiscing 进入了  update  初次  初次  初次  初次 22222  startboundingBox:" + this.startboundingBox.toString());
            this.startboundingPt = null;
            if (!rs) {
                return false;
            }
        } else if (this.tracktype == 2 && !this.objTracking.isTrackPreinited()) {
            CameraUtils.LogV("HTrack-XMiscing", "XMiscing 进入了  图像分割  图像分割  图像分割 ");
            if (!this.objTracking.preinit(this.yuvatfromcamera, this.startboundingBox)) {
                return false;
            }
        }
        if (!this.objTracking.isTrackInited()) {
            result = this.objTracking.init(this.yuvatfromcamera, this.startboundingBox);
            if (result) {
                if (this.listener != null) {
                    this.listener.trackStarted();
                }
                startTrackingThread();
            } else if (this.listener == null) {
                return result;
            } else {
                this.listener.initTrackFailed();
                return result;
            }
        } else {
            long currentTimeMillis = System.currentTimeMillis();
            result = this.objTracking.update(this.yuvatfromcamera);
        }
        if (result) {
            if (this.listener == null) {
                return result;
            }
            long t2 = System.currentTimeMillis();
            this.listener.trackRect(videoRect2UIRect(this.objTracking.getmTrackRect()));
            CameraUtils.LogV("HTrack-Rect", "-- 画框---" + (System.currentTimeMillis() - t2));
            return result;
        } else if (this.listener == null) {
            return result;
        } else {
            this.listener.trackLost();
            return result;
        }
    }

    private void byte2Mat(byte[] frame) {
        if (frame != null) {
            this.yuvatfromcamera.put(0, 0, frame);
        }
    }

    public boolean isIstrack() {
        return this.istrack;
    }

    private void sendPoint2Gimbal() {
        if (((Integer) SPUtils.get(MyApplication.getInstance().getApplicationContext(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
            if (((int) this.center.f1125x) == 0 && ((int) this.center.f1126y) == 0) {
                if (!this.islosted) {
                    BleByteUtil.setPTZParameters((byte) 21, (byte) 0, (byte) 1);
                    this.islosted = true;
                    CameraUtils.LogV(TAG, "****Losted****");
                }
            } else if (this.islosted) {
                BleByteUtil.setPTZParameters((byte) 21, (byte) 1, (byte) 1);
                this.islosted = false;
                CameraUtils.LogV(TAG, "****Resumed****");
            } else {
                int sendX = (int) ((this.center.f1125x - ((double) (this.dimensions.getWidth() / 2))) * ((double) this.f1117YK));
                int sendY = (int) ((this.center.f1126y - ((double) (this.dimensions.getHeight() / 2))) * ((double) this.f1117YK));
                if (com.freevisiontech.fvmobile.utility.CameraUtils.getCurrentPageIndex() == 0) {
                    BleByteUtil.sendPtzFollowData(sendX, -sendY);
                } else if (com.freevisiontech.fvmobile.utility.CameraUtils.getPhoneAngle() == 90 || com.freevisiontech.fvmobile.utility.CameraUtils.getPhoneAngle() == 270) {
                    BleByteUtil.sendPtzFollowData(sendY, sendX);
                } else {
                    BleByteUtil.sendPtzFollowData(sendX, -sendY);
                }
                CameraUtils.LogV("SendPoint2Gimbal", "send:" + sendX + ";" + sendY);
            }
        } else if (((int) this.center.f1125x) == 0 && ((int) this.center.f1126y) == 0) {
            if (!this.islosted) {
                BleByteUtil.setPTZParameters((byte) 21, (byte) 0, (byte) 1);
                this.islosted = true;
                CameraUtils.LogV(TAG, "****Losted****");
            }
        } else if (this.islosted) {
            BleByteUtil.setPTZParameters((byte) 21, (byte) 1, (byte) 1);
            this.islosted = false;
            CameraUtils.LogV(TAG, "****Resumed****");
        } else {
            int sendX2 = (int) ((this.center.f1125x - ((double) (this.dimensions.getWidth() / 2))) * ((double) this.f1117YK));
            int sendY2 = (int) ((this.center.f1126y - ((double) (this.dimensions.getHeight() / 2))) * ((double) this.f1117YK));
            if (com.freevisiontech.fvmobile.utility.CameraUtils.getCurrentPageIndex() == 0) {
                BleByteUtil.sendPtzFollowData(sendX2, sendY2);
            } else if (com.freevisiontech.fvmobile.utility.CameraUtils.getPhoneAngle() == 90 || com.freevisiontech.fvmobile.utility.CameraUtils.getPhoneAngle() == 270) {
                BleByteUtil.sendPtzFollowData(-sendY2, sendX2);
            } else {
                BleByteUtil.sendPtzFollowData(sendX2, sendY2);
            }
            CameraUtils.LogV("SendPoint2Gimbal", "send:" + sendX2 + ";" + sendY2);
        }
    }

    private void sendTrackEndTag2Gimbal() {
        BleByteUtil.setPTZParameters((byte) 21, (byte) 0);
    }

    private void startTrackingThread() {
        if (this.istrack) {
            if (this.trackthread != null && this.trackthread.isAlive()) {
                this.trackthread.stop();
            }
            this.trackthread = new Thread(this);
            this.trackthread.start();
        }
    }

    private Rect videoRect2UIRect(Rect rect) {
        return rect;
    }

    private Rect uiRect2VideoRect(Rect rect) {
        return rect;
    }

    private Point adjustPoint4Gimabal(Point pt) {
        return pt;
    }

    public void run() {
        getPictrueSizeAndPercent();
        this.islosted = false;
        while (this.istrack) {
            sendPoint2Gimbal();
            if (this.istrack) {
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        if (this.listener != null) {
            this.listener.trackStopped();
        }
    }

    private void getPictrueSizeAndPercent() {
        this.center = adjustPoint4Gimabal(this.objTracking.getTrackPt());
        Size size = this.manager.getPreviewFrameSize();
        this.dimensions = size;
        this.f1117YK = (int) (((double) (SupportMenu.USER_MASK / size.getWidth())) * 0.5d);
    }

    public static android.graphics.Rect OpenCVRect2GraphicRect(Rect opencvrect) {
        return new android.graphics.Rect(opencvrect.f1130x, opencvrect.f1131y, opencvrect.f1130x + opencvrect.width, opencvrect.f1131y + opencvrect.height);
    }

    public static Rect GraphicRect2OpenCVRect(android.graphics.Rect rect) {
        return new Rect(rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);
    }

    public static Point GraphicPoint2OpenCVPoint(android.graphics.Point pnt) {
        return new Point((double) pnt.x, (double) pnt.y);
    }

    public static android.graphics.Point OpenCVPoint2GraphicPoint(Point opencvpnt) {
        return new android.graphics.Point((int) opencvpnt.f1125x, (int) opencvpnt.f1126y);
    }
}
