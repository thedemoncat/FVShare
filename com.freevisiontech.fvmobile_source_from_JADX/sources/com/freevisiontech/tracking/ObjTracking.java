package com.freevisiontech.tracking;

import com.freevisiontech.cameralib.utils.CameraUtils;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.xtracking.XTracker;

public class ObjTracking {
    private static String TAG = "ObjTracking";
    private int Retfaile = 1;
    private int isBeforeret = 0;
    private int mImgtype = 0;
    private Mat mRgb = new Mat();
    private Point mTrackPt = null;
    private Point mTrackPtTwo = null;
    private Rect mTrackRect = null;
    private XTracker mTracker;
    private boolean mTrackingInited = false;
    private boolean mTrackingPreinited = false;

    public ObjTracking(int imgtype) {
        this.mImgtype = imgtype;
    }

    public boolean initObjTrack() {
        this.mTracker = XTracker.create("HogKCF");
        if (this.mTracker == null) {
            return false;
        }
        return true;
    }

    public void clear() {
        this.mTrackingInited = false;
        this.mTrackingPreinited = false;
        this.mRgb.release();
        this.mTrackRect = null;
        this.mTrackPt = null;
        this.mTrackPtTwo = null;
    }

    public boolean preinit(Mat yuvImageMat, Rect boundingBox) {
        if (this.mTracker == null) {
            return false;
        }
        this.mTracker.setParam(0, 0, 1);
        if (!this.mTracker.preInit(yuvImageMat, boundingBox, new Mat())) {
            return false;
        }
        this.mTrackingPreinited = true;
        return true;
    }

    public boolean init(Mat yuvImageMat, Rect boundingBox) {
        this.mTrackRect = boundingBox.clone();
        if (this.mTracker == null) {
            return false;
        }
        this.mTracker.setParam(0, 0, 1);
        if (!this.mTracker.init(yuvImageMat, this.mTrackRect)) {
            return false;
        }
        this.mTrackingInited = true;
        makeTrackPointFromTrackRect();
        return true;
    }

    public boolean update(Mat yuvImageMat) {
        if (this.mTracker == null) {
            CameraUtils.LogV(TAG, "Tracker is NULL!");
            return false;
        } else if (!this.mTrackingInited) {
            CameraUtils.LogV(TAG, "Tracker is not inited!");
            return false;
        } else {
            long t2 = System.currentTimeMillis();
            boolean update = this.mTracker.update(yuvImageMat, this.mTrackRect);
            CameraUtils.LogV("HTrack_tkupdate", "TrackDuration_tkupdate duration =" + (System.currentTimeMillis() - t2));
            if (update) {
                makeTrackPointFromTrackRect();
                return update;
            }
            makeTrackPointFromTrackRect(0, 0);
            return update;
        }
    }

    private void makeTrackPointFromTrackRect() {
        if (this.mTrackPt == null) {
            this.mTrackPt = new Point();
        }
        if (this.mTrackPtTwo == null) {
            this.mTrackPtTwo = new Point();
        }
        if (this.mTrackRect == null) {
            this.mTrackRect = new Rect(0, 0, 0, 0);
        }
        this.mTrackPt.f1125x = (double) (this.mTrackRect.f1130x + (this.mTrackRect.width / 2));
        this.mTrackPt.f1126y = (double) (this.mTrackRect.f1131y + (this.mTrackRect.height / 2));
    }

    private void makeTrackPointFromTrackRect(int pointX, int pointY) {
        if (this.mTrackPt == null) {
            this.mTrackPt = new Point();
        }
        this.mTrackPt.f1125x = (double) pointX;
        this.mTrackPt.f1126y = (double) pointY;
    }

    public Point getTrackPt() {
        if (this.mTrackPtTwo == null) {
            this.mTrackPtTwo = new Point();
        }
        this.mTrackPtTwo = this.mTrackPt;
        makeTrackPointFromTrackRect(0, 0);
        return this.mTrackPtTwo;
    }

    public Rect getmTrackRect() {
        if (this.mTrackRect == null) {
            this.mTrackRect = new Rect(0, 0, 0, 0);
        }
        return this.mTrackRect;
    }

    public boolean isTrackInited() {
        return this.mTrackingInited;
    }

    public boolean isTrackPreinited() {
        return this.mTrackingPreinited;
    }
}
