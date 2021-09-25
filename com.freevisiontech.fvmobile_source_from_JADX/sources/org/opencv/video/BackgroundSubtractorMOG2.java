package org.opencv.video;

public class BackgroundSubtractorMOG2 extends BackgroundSubtractor {
    private static native void delete(long j);

    private static native double getBackgroundRatio_0(long j);

    private static native double getComplexityReductionThreshold_0(long j);

    private static native boolean getDetectShadows_0(long j);

    private static native int getHistory_0(long j);

    private static native int getNMixtures_0(long j);

    private static native double getShadowThreshold_0(long j);

    private static native int getShadowValue_0(long j);

    private static native double getVarInit_0(long j);

    private static native double getVarMax_0(long j);

    private static native double getVarMin_0(long j);

    private static native double getVarThresholdGen_0(long j);

    private static native double getVarThreshold_0(long j);

    private static native void setBackgroundRatio_0(long j, double d);

    private static native void setComplexityReductionThreshold_0(long j, double d);

    private static native void setDetectShadows_0(long j, boolean z);

    private static native void setHistory_0(long j, int i);

    private static native void setNMixtures_0(long j, int i);

    private static native void setShadowThreshold_0(long j, double d);

    private static native void setShadowValue_0(long j, int i);

    private static native void setVarInit_0(long j, double d);

    private static native void setVarMax_0(long j, double d);

    private static native void setVarMin_0(long j, double d);

    private static native void setVarThresholdGen_0(long j, double d);

    private static native void setVarThreshold_0(long j, double d);

    protected BackgroundSubtractorMOG2(long addr) {
        super(addr);
    }

    public boolean getDetectShadows() {
        return getDetectShadows_0(this.nativeObj);
    }

    public double getBackgroundRatio() {
        return getBackgroundRatio_0(this.nativeObj);
    }

    public double getComplexityReductionThreshold() {
        return getComplexityReductionThreshold_0(this.nativeObj);
    }

    public double getShadowThreshold() {
        return getShadowThreshold_0(this.nativeObj);
    }

    public double getVarInit() {
        return getVarInit_0(this.nativeObj);
    }

    public double getVarMax() {
        return getVarMax_0(this.nativeObj);
    }

    public double getVarMin() {
        return getVarMin_0(this.nativeObj);
    }

    public double getVarThreshold() {
        return getVarThreshold_0(this.nativeObj);
    }

    public double getVarThresholdGen() {
        return getVarThresholdGen_0(this.nativeObj);
    }

    public int getHistory() {
        return getHistory_0(this.nativeObj);
    }

    public int getNMixtures() {
        return getNMixtures_0(this.nativeObj);
    }

    public int getShadowValue() {
        return getShadowValue_0(this.nativeObj);
    }

    public void setBackgroundRatio(double ratio) {
        setBackgroundRatio_0(this.nativeObj, ratio);
    }

    public void setComplexityReductionThreshold(double ct) {
        setComplexityReductionThreshold_0(this.nativeObj, ct);
    }

    public void setDetectShadows(boolean detectShadows) {
        setDetectShadows_0(this.nativeObj, detectShadows);
    }

    public void setHistory(int history) {
        setHistory_0(this.nativeObj, history);
    }

    public void setNMixtures(int nmixtures) {
        setNMixtures_0(this.nativeObj, nmixtures);
    }

    public void setShadowThreshold(double threshold) {
        setShadowThreshold_0(this.nativeObj, threshold);
    }

    public void setShadowValue(int value) {
        setShadowValue_0(this.nativeObj, value);
    }

    public void setVarInit(double varInit) {
        setVarInit_0(this.nativeObj, varInit);
    }

    public void setVarMax(double varMax) {
        setVarMax_0(this.nativeObj, varMax);
    }

    public void setVarMin(double varMin) {
        setVarMin_0(this.nativeObj, varMin);
    }

    public void setVarThreshold(double varThreshold) {
        setVarThreshold_0(this.nativeObj, varThreshold);
    }

    public void setVarThresholdGen(double varThresholdGen) {
        setVarThresholdGen_0(this.nativeObj, varThresholdGen);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
