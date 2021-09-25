package org.opencv.video;

import org.opencv.core.Size;
import org.opencv.core.TermCriteria;

public class SparsePyrLKOpticalFlow extends SparseOpticalFlow {
    private static native long create_0(double d, double d2, int i, int i2, int i3, double d3, int i4, double d4);

    private static native long create_1();

    private static native void delete(long j);

    private static native int getFlags_0(long j);

    private static native int getMaxLevel_0(long j);

    private static native double getMinEigThreshold_0(long j);

    private static native double[] getTermCriteria_0(long j);

    private static native double[] getWinSize_0(long j);

    private static native void setFlags_0(long j, int i);

    private static native void setMaxLevel_0(long j, int i);

    private static native void setMinEigThreshold_0(long j, double d);

    private static native void setTermCriteria_0(long j, int i, int i2, double d);

    private static native void setWinSize_0(long j, double d, double d2);

    protected SparsePyrLKOpticalFlow(long addr) {
        super(addr);
    }

    public static SparsePyrLKOpticalFlow create(Size winSize, int maxLevel, TermCriteria crit, int flags, double minEigThreshold) {
        return new SparsePyrLKOpticalFlow(create_0(winSize.width, winSize.height, maxLevel, crit.type, crit.maxCount, crit.epsilon, flags, minEigThreshold));
    }

    public static SparsePyrLKOpticalFlow create() {
        return new SparsePyrLKOpticalFlow(create_1());
    }

    public Size getWinSize() {
        return new Size(getWinSize_0(this.nativeObj));
    }

    public TermCriteria getTermCriteria() {
        return new TermCriteria(getTermCriteria_0(this.nativeObj));
    }

    public double getMinEigThreshold() {
        return getMinEigThreshold_0(this.nativeObj);
    }

    public int getFlags() {
        return getFlags_0(this.nativeObj);
    }

    public int getMaxLevel() {
        return getMaxLevel_0(this.nativeObj);
    }

    public void setFlags(int flags) {
        setFlags_0(this.nativeObj, flags);
    }

    public void setMaxLevel(int maxLevel) {
        setMaxLevel_0(this.nativeObj, maxLevel);
    }

    public void setMinEigThreshold(double minEigThreshold) {
        setMinEigThreshold_0(this.nativeObj, minEigThreshold);
    }

    public void setTermCriteria(TermCriteria crit) {
        setTermCriteria_0(this.nativeObj, crit.type, crit.maxCount, crit.epsilon);
    }

    public void setWinSize(Size winSize) {
        setWinSize_0(this.nativeObj, winSize.width, winSize.height);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
