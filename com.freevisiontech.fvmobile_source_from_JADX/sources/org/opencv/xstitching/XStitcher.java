package org.opencv.xstitching;

import java.util.List;
import org.opencv.core.Mat;
import org.opencv.utils.Converters;

public class XStitcher {
    public static final int BLEND_AVERAGE = 0;
    public static final int BLEND_LINEAR = 1;
    public static final int BLEND_METHODS_SIZE = 2;
    public static final int GLOBAL_ROTATION_2D_METHOD = 0;
    public static final int GLOBAL_ROTATION_3D_METHOD = 1;
    public static final int GLOBAL_ROTATION_METHODS_SIZE = 2;
    public static final int SCAN_TYPE_1x5 = 0;
    public static final int SCAN_TYPE_1x7 = 6;
    public static final int SCAN_TYPE_1x8 = 1;
    public static final int SCAN_TYPE_3x3 = 2;
    public static final int SCAN_TYPE_3x5 = 5;
    public static final int SCAN_TYPE_3x8 = 4;
    public static final int SCAN_TYPE_4x4 = 3;
    public static final int WAVE_H = 1;
    public static final int WAVE_V = 2;
    public static final int WAVE_X = 0;
    protected final long nativeObj;

    private static native boolean addImageAndMatching_0(long j, long j2, int i);

    private static native boolean addImageAndMatching_1(long j, String str, int i);

    private static native void clear_0(long j);

    private static native void delete(long j);

    private static native int getBlendMethod_0(long j);

    private static native long getInstance_0(boolean z, boolean z2);

    private static native long getInstance_1();

    private static native int getScanType_0(long j);

    private static native int getWaveCorrect_0(long j);

    private static native void setBlendMethod_0(long j, int i);

    private static native void setScanType_0(long j, int i);

    private static native void setWaveCorrect_0(long j, int i);

    private static native int stitch_0(long j, long j2, long j3);

    private static native int stitching_0(long j, long j2);

    private static native int stitching_1(long j, String str);

    protected XStitcher(long addr) {
        this.nativeObj = addr;
    }

    public static XStitcher getInstance(boolean try_use_gpu, boolean try_use_omp) {
        return new XStitcher(getInstance_0(try_use_gpu, try_use_omp));
    }

    public static XStitcher getInstance() {
        return new XStitcher(getInstance_1());
    }

    public boolean addImageAndMatching(Mat image, int idx) {
        return addImageAndMatching_0(this.nativeObj, image.nativeObj, idx);
    }

    public boolean addImageAndMatching(String inFile, int idx) {
        return addImageAndMatching_1(this.nativeObj, inFile, idx);
    }

    public int getBlendMethod() {
        return getBlendMethod_0(this.nativeObj);
    }

    public int getScanType() {
        return getScanType_0(this.nativeObj);
    }

    public int getWaveCorrect() {
        return getWaveCorrect_0(this.nativeObj);
    }

    public int stitch(List<Mat> images, Mat pano) {
        return stitch_0(this.nativeObj, Converters.vector_Mat_to_Mat(images).nativeObj, pano.nativeObj);
    }

    public int stitching(Mat pano) {
        return stitching_0(this.nativeObj, pano.nativeObj);
    }

    public int stitching(String outFile) {
        return stitching_1(this.nativeObj, outFile);
    }

    public void clear() {
        clear_0(this.nativeObj);
    }

    public void setBlendMethod(int bm) {
        setBlendMethod_0(this.nativeObj, bm);
    }

    public void setScanType(int st) {
        setScanType_0(this.nativeObj, st);
    }

    public void setWaveCorrect(int wc) {
        setWaveCorrect_0(this.nativeObj, wc);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
