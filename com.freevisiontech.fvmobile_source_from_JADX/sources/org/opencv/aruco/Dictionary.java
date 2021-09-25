package org.opencv.aruco;

import org.opencv.core.Mat;

public class Dictionary {
    protected final long nativeObj;

    private static native long create_0(int i, int i2);

    private static native long create_from_0(int i, int i2, long j);

    private static native void delete(long j);

    private static native void drawMarker_0(long j, int i, int i2, long j2, int i3);

    private static native void drawMarker_1(long j, int i, int i2, long j2);

    private static native long get_0(int i);

    private static native long get_bytesList_0(long j);

    private static native int get_markerSize_0(long j);

    private static native int get_maxCorrectionBits_0(long j);

    protected Dictionary(long addr) {
        this.nativeObj = addr;
    }

    public static Dictionary create_from(int nMarkers, int markerSize, Dictionary baseDictionary) {
        return new Dictionary(create_from_0(nMarkers, markerSize, baseDictionary.nativeObj));
    }

    public static Dictionary create(int nMarkers, int markerSize) {
        return new Dictionary(create_0(nMarkers, markerSize));
    }

    public static Dictionary get(int dict) {
        return new Dictionary(get_0(dict));
    }

    public void drawMarker(int id, int sidePixels, Mat _img, int borderBits) {
        drawMarker_0(this.nativeObj, id, sidePixels, _img.nativeObj, borderBits);
    }

    public void drawMarker(int id, int sidePixels, Mat _img) {
        drawMarker_1(this.nativeObj, id, sidePixels, _img.nativeObj);
    }

    public Mat get_bytesList() {
        return new Mat(get_bytesList_0(this.nativeObj));
    }

    public int get_markerSize() {
        return get_markerSize_0(this.nativeObj);
    }

    public int get_maxCorrectionBits() {
        return get_maxCorrectionBits_0(this.nativeObj);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
