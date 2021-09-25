package org.opencv.p011ml;

import org.opencv.core.Mat;

/* renamed from: org.opencv.ml.DTrees */
public class DTrees extends StatModel {
    public static final int PREDICT_AUTO = 0;
    public static final int PREDICT_MASK = 768;
    public static final int PREDICT_MAX_VOTE = 512;
    public static final int PREDICT_SUM = 256;

    private static native long create_0();

    private static native void delete(long j);

    private static native int getCVFolds_0(long j);

    private static native int getMaxCategories_0(long j);

    private static native int getMaxDepth_0(long j);

    private static native int getMinSampleCount_0(long j);

    private static native long getPriors_0(long j);

    private static native float getRegressionAccuracy_0(long j);

    private static native boolean getTruncatePrunedTree_0(long j);

    private static native boolean getUse1SERule_0(long j);

    private static native boolean getUseSurrogates_0(long j);

    private static native void setCVFolds_0(long j, int i);

    private static native void setMaxCategories_0(long j, int i);

    private static native void setMaxDepth_0(long j, int i);

    private static native void setMinSampleCount_0(long j, int i);

    private static native void setPriors_0(long j, long j2);

    private static native void setRegressionAccuracy_0(long j, float f);

    private static native void setTruncatePrunedTree_0(long j, boolean z);

    private static native void setUse1SERule_0(long j, boolean z);

    private static native void setUseSurrogates_0(long j, boolean z);

    protected DTrees(long addr) {
        super(addr);
    }

    public Mat getPriors() {
        return new Mat(getPriors_0(this.nativeObj));
    }

    public static DTrees create() {
        return new DTrees(create_0());
    }

    public boolean getTruncatePrunedTree() {
        return getTruncatePrunedTree_0(this.nativeObj);
    }

    public boolean getUse1SERule() {
        return getUse1SERule_0(this.nativeObj);
    }

    public boolean getUseSurrogates() {
        return getUseSurrogates_0(this.nativeObj);
    }

    public float getRegressionAccuracy() {
        return getRegressionAccuracy_0(this.nativeObj);
    }

    public int getCVFolds() {
        return getCVFolds_0(this.nativeObj);
    }

    public int getMaxCategories() {
        return getMaxCategories_0(this.nativeObj);
    }

    public int getMaxDepth() {
        return getMaxDepth_0(this.nativeObj);
    }

    public int getMinSampleCount() {
        return getMinSampleCount_0(this.nativeObj);
    }

    public void setCVFolds(int val) {
        setCVFolds_0(this.nativeObj, val);
    }

    public void setMaxCategories(int val) {
        setMaxCategories_0(this.nativeObj, val);
    }

    public void setMaxDepth(int val) {
        setMaxDepth_0(this.nativeObj, val);
    }

    public void setMinSampleCount(int val) {
        setMinSampleCount_0(this.nativeObj, val);
    }

    public void setPriors(Mat val) {
        setPriors_0(this.nativeObj, val.nativeObj);
    }

    public void setRegressionAccuracy(float val) {
        setRegressionAccuracy_0(this.nativeObj, val);
    }

    public void setTruncatePrunedTree(boolean val) {
        setTruncatePrunedTree_0(this.nativeObj, val);
    }

    public void setUse1SERule(boolean val) {
        setUse1SERule_0(this.nativeObj, val);
    }

    public void setUseSurrogates(boolean val) {
        setUseSurrogates_0(this.nativeObj, val);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
