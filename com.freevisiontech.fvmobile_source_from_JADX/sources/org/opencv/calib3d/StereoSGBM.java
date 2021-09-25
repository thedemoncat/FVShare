package org.opencv.calib3d;

public class StereoSGBM extends StereoMatcher {
    public static final int MODE_HH = 1;
    public static final int MODE_SGBM = 0;
    public static final int MODE_SGBM_3WAY = 2;

    private static native long create_0(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11);

    private static native long create_1(int i, int i2, int i3);

    private static native void delete(long j);

    private static native int getMode_0(long j);

    private static native int getP1_0(long j);

    private static native int getP2_0(long j);

    private static native int getPreFilterCap_0(long j);

    private static native int getUniquenessRatio_0(long j);

    private static native void setMode_0(long j, int i);

    private static native void setP1_0(long j, int i);

    private static native void setP2_0(long j, int i);

    private static native void setPreFilterCap_0(long j, int i);

    private static native void setUniquenessRatio_0(long j, int i);

    protected StereoSGBM(long addr) {
        super(addr);
    }

    public static StereoSGBM create(int minDisparity, int numDisparities, int blockSize, int P1, int P2, int disp12MaxDiff, int preFilterCap, int uniquenessRatio, int speckleWindowSize, int speckleRange, int mode) {
        return new StereoSGBM(create_0(minDisparity, numDisparities, blockSize, P1, P2, disp12MaxDiff, preFilterCap, uniquenessRatio, speckleWindowSize, speckleRange, mode));
    }

    public static StereoSGBM create(int minDisparity, int numDisparities, int blockSize) {
        return new StereoSGBM(create_1(minDisparity, numDisparities, blockSize));
    }

    public int getMode() {
        return getMode_0(this.nativeObj);
    }

    public int getP1() {
        return getP1_0(this.nativeObj);
    }

    public int getP2() {
        return getP2_0(this.nativeObj);
    }

    public int getPreFilterCap() {
        return getPreFilterCap_0(this.nativeObj);
    }

    public int getUniquenessRatio() {
        return getUniquenessRatio_0(this.nativeObj);
    }

    public void setMode(int mode) {
        setMode_0(this.nativeObj, mode);
    }

    public void setP1(int P1) {
        setP1_0(this.nativeObj, P1);
    }

    public void setP2(int P2) {
        setP2_0(this.nativeObj, P2);
    }

    public void setPreFilterCap(int preFilterCap) {
        setPreFilterCap_0(this.nativeObj, preFilterCap);
    }

    public void setUniquenessRatio(int uniquenessRatio) {
        setUniquenessRatio_0(this.nativeObj, uniquenessRatio);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
