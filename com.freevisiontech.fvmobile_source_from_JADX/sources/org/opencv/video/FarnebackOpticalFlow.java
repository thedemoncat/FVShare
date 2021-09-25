package org.opencv.video;

public class FarnebackOpticalFlow extends DenseOpticalFlow {
    private static native long create_0(int i, double d, boolean z, int i2, int i3, int i4, double d2, int i5);

    private static native long create_1();

    private static native void delete(long j);

    private static native boolean getFastPyramids_0(long j);

    private static native int getFlags_0(long j);

    private static native int getNumIters_0(long j);

    private static native int getNumLevels_0(long j);

    private static native int getPolyN_0(long j);

    private static native double getPolySigma_0(long j);

    private static native double getPyrScale_0(long j);

    private static native int getWinSize_0(long j);

    private static native void setFastPyramids_0(long j, boolean z);

    private static native void setFlags_0(long j, int i);

    private static native void setNumIters_0(long j, int i);

    private static native void setNumLevels_0(long j, int i);

    private static native void setPolyN_0(long j, int i);

    private static native void setPolySigma_0(long j, double d);

    private static native void setPyrScale_0(long j, double d);

    private static native void setWinSize_0(long j, int i);

    protected FarnebackOpticalFlow(long addr) {
        super(addr);
    }

    public static FarnebackOpticalFlow create(int numLevels, double pyrScale, boolean fastPyramids, int winSize, int numIters, int polyN, double polySigma, int flags) {
        return new FarnebackOpticalFlow(create_0(numLevels, pyrScale, fastPyramids, winSize, numIters, polyN, polySigma, flags));
    }

    public static FarnebackOpticalFlow create() {
        return new FarnebackOpticalFlow(create_1());
    }

    public boolean getFastPyramids() {
        return getFastPyramids_0(this.nativeObj);
    }

    public double getPolySigma() {
        return getPolySigma_0(this.nativeObj);
    }

    public double getPyrScale() {
        return getPyrScale_0(this.nativeObj);
    }

    public int getFlags() {
        return getFlags_0(this.nativeObj);
    }

    public int getNumIters() {
        return getNumIters_0(this.nativeObj);
    }

    public int getNumLevels() {
        return getNumLevels_0(this.nativeObj);
    }

    public int getPolyN() {
        return getPolyN_0(this.nativeObj);
    }

    public int getWinSize() {
        return getWinSize_0(this.nativeObj);
    }

    public void setFastPyramids(boolean fastPyramids) {
        setFastPyramids_0(this.nativeObj, fastPyramids);
    }

    public void setFlags(int flags) {
        setFlags_0(this.nativeObj, flags);
    }

    public void setNumIters(int numIters) {
        setNumIters_0(this.nativeObj, numIters);
    }

    public void setNumLevels(int numLevels) {
        setNumLevels_0(this.nativeObj, numLevels);
    }

    public void setPolyN(int polyN) {
        setPolyN_0(this.nativeObj, polyN);
    }

    public void setPolySigma(double polySigma) {
        setPolySigma_0(this.nativeObj, polySigma);
    }

    public void setPyrScale(double pyrScale) {
        setPyrScale_0(this.nativeObj, pyrScale);
    }

    public void setWinSize(int winSize) {
        setWinSize_0(this.nativeObj, winSize);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
