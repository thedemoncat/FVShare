package org.opencv.features2d;

public class GFTTDetector extends Feature2D {
    private static native long create_0(int i, double d, double d2, int i2, boolean z, double d3);

    private static native long create_1();

    private static native void delete(long j);

    private static native int getBlockSize_0(long j);

    private static native boolean getHarrisDetector_0(long j);

    private static native double getK_0(long j);

    private static native int getMaxFeatures_0(long j);

    private static native double getMinDistance_0(long j);

    private static native double getQualityLevel_0(long j);

    private static native void setBlockSize_0(long j, int i);

    private static native void setHarrisDetector_0(long j, boolean z);

    private static native void setK_0(long j, double d);

    private static native void setMaxFeatures_0(long j, int i);

    private static native void setMinDistance_0(long j, double d);

    private static native void setQualityLevel_0(long j, double d);

    protected GFTTDetector(long addr) {
        super(addr);
    }

    public static GFTTDetector create(int maxCorners, double qualityLevel, double minDistance, int blockSize, boolean useHarrisDetector, double k) {
        return new GFTTDetector(create_0(maxCorners, qualityLevel, minDistance, blockSize, useHarrisDetector, k));
    }

    public static GFTTDetector create() {
        return new GFTTDetector(create_1());
    }

    public boolean getHarrisDetector() {
        return getHarrisDetector_0(this.nativeObj);
    }

    public double getK() {
        return getK_0(this.nativeObj);
    }

    public double getMinDistance() {
        return getMinDistance_0(this.nativeObj);
    }

    public double getQualityLevel() {
        return getQualityLevel_0(this.nativeObj);
    }

    public int getBlockSize() {
        return getBlockSize_0(this.nativeObj);
    }

    public int getMaxFeatures() {
        return getMaxFeatures_0(this.nativeObj);
    }

    public void setBlockSize(int blockSize) {
        setBlockSize_0(this.nativeObj, blockSize);
    }

    public void setHarrisDetector(boolean val) {
        setHarrisDetector_0(this.nativeObj, val);
    }

    public void setK(double k) {
        setK_0(this.nativeObj, k);
    }

    public void setMaxFeatures(int maxFeatures) {
        setMaxFeatures_0(this.nativeObj, maxFeatures);
    }

    public void setMinDistance(double minDistance) {
        setMinDistance_0(this.nativeObj, minDistance);
    }

    public void setQualityLevel(double qlevel) {
        setQualityLevel_0(this.nativeObj, qlevel);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
