package org.opencv.features2d;

public class Params {
    protected final long nativeObj;

    private static native long Params_0();

    private static native void delete(long j);

    private static native boolean get_filterByArea_0(long j);

    private static native boolean get_filterByCircularity_0(long j);

    private static native boolean get_filterByColor_0(long j);

    private static native boolean get_filterByConvexity_0(long j);

    private static native boolean get_filterByInertia_0(long j);

    private static native float get_maxArea_0(long j);

    private static native float get_maxCircularity_0(long j);

    private static native float get_maxConvexity_0(long j);

    private static native float get_maxInertiaRatio_0(long j);

    private static native float get_maxThreshold_0(long j);

    private static native float get_minArea_0(long j);

    private static native float get_minCircularity_0(long j);

    private static native float get_minConvexity_0(long j);

    private static native float get_minDistBetweenBlobs_0(long j);

    private static native float get_minInertiaRatio_0(long j);

    private static native long get_minRepeatability_0(long j);

    private static native float get_minThreshold_0(long j);

    private static native float get_thresholdStep_0(long j);

    private static native void set_filterByArea_0(long j, boolean z);

    private static native void set_filterByCircularity_0(long j, boolean z);

    private static native void set_filterByColor_0(long j, boolean z);

    private static native void set_filterByConvexity_0(long j, boolean z);

    private static native void set_filterByInertia_0(long j, boolean z);

    private static native void set_maxArea_0(long j, float f);

    private static native void set_maxCircularity_0(long j, float f);

    private static native void set_maxConvexity_0(long j, float f);

    private static native void set_maxInertiaRatio_0(long j, float f);

    private static native void set_maxThreshold_0(long j, float f);

    private static native void set_minArea_0(long j, float f);

    private static native void set_minCircularity_0(long j, float f);

    private static native void set_minConvexity_0(long j, float f);

    private static native void set_minDistBetweenBlobs_0(long j, float f);

    private static native void set_minInertiaRatio_0(long j, float f);

    private static native void set_minRepeatability_0(long j, long j2);

    private static native void set_minThreshold_0(long j, float f);

    private static native void set_thresholdStep_0(long j, float f);

    protected Params(long addr) {
        this.nativeObj = addr;
    }

    public Params() {
        this.nativeObj = Params_0();
    }

    public float get_thresholdStep() {
        return get_thresholdStep_0(this.nativeObj);
    }

    public void set_thresholdStep(float thresholdStep) {
        set_thresholdStep_0(this.nativeObj, thresholdStep);
    }

    public float get_minThreshold() {
        return get_minThreshold_0(this.nativeObj);
    }

    public void set_minThreshold(float minThreshold) {
        set_minThreshold_0(this.nativeObj, minThreshold);
    }

    public float get_maxThreshold() {
        return get_maxThreshold_0(this.nativeObj);
    }

    public void set_maxThreshold(float maxThreshold) {
        set_maxThreshold_0(this.nativeObj, maxThreshold);
    }

    public long get_minRepeatability() {
        return get_minRepeatability_0(this.nativeObj);
    }

    public void set_minRepeatability(long minRepeatability) {
        set_minRepeatability_0(this.nativeObj, minRepeatability);
    }

    public float get_minDistBetweenBlobs() {
        return get_minDistBetweenBlobs_0(this.nativeObj);
    }

    public void set_minDistBetweenBlobs(float minDistBetweenBlobs) {
        set_minDistBetweenBlobs_0(this.nativeObj, minDistBetweenBlobs);
    }

    public boolean get_filterByColor() {
        return get_filterByColor_0(this.nativeObj);
    }

    public void set_filterByColor(boolean filterByColor) {
        set_filterByColor_0(this.nativeObj, filterByColor);
    }

    public boolean get_filterByArea() {
        return get_filterByArea_0(this.nativeObj);
    }

    public void set_filterByArea(boolean filterByArea) {
        set_filterByArea_0(this.nativeObj, filterByArea);
    }

    public float get_minArea() {
        return get_minArea_0(this.nativeObj);
    }

    public void set_minArea(float minArea) {
        set_minArea_0(this.nativeObj, minArea);
    }

    public float get_maxArea() {
        return get_maxArea_0(this.nativeObj);
    }

    public void set_maxArea(float maxArea) {
        set_maxArea_0(this.nativeObj, maxArea);
    }

    public boolean get_filterByCircularity() {
        return get_filterByCircularity_0(this.nativeObj);
    }

    public void set_filterByCircularity(boolean filterByCircularity) {
        set_filterByCircularity_0(this.nativeObj, filterByCircularity);
    }

    public float get_minCircularity() {
        return get_minCircularity_0(this.nativeObj);
    }

    public void set_minCircularity(float minCircularity) {
        set_minCircularity_0(this.nativeObj, minCircularity);
    }

    public float get_maxCircularity() {
        return get_maxCircularity_0(this.nativeObj);
    }

    public void set_maxCircularity(float maxCircularity) {
        set_maxCircularity_0(this.nativeObj, maxCircularity);
    }

    public boolean get_filterByInertia() {
        return get_filterByInertia_0(this.nativeObj);
    }

    public void set_filterByInertia(boolean filterByInertia) {
        set_filterByInertia_0(this.nativeObj, filterByInertia);
    }

    public float get_minInertiaRatio() {
        return get_minInertiaRatio_0(this.nativeObj);
    }

    public void set_minInertiaRatio(float minInertiaRatio) {
        set_minInertiaRatio_0(this.nativeObj, minInertiaRatio);
    }

    public float get_maxInertiaRatio() {
        return get_maxInertiaRatio_0(this.nativeObj);
    }

    public void set_maxInertiaRatio(float maxInertiaRatio) {
        set_maxInertiaRatio_0(this.nativeObj, maxInertiaRatio);
    }

    public boolean get_filterByConvexity() {
        return get_filterByConvexity_0(this.nativeObj);
    }

    public void set_filterByConvexity(boolean filterByConvexity) {
        set_filterByConvexity_0(this.nativeObj, filterByConvexity);
    }

    public float get_minConvexity() {
        return get_minConvexity_0(this.nativeObj);
    }

    public void set_minConvexity(float minConvexity) {
        set_minConvexity_0(this.nativeObj, minConvexity);
    }

    public float get_maxConvexity() {
        return get_maxConvexity_0(this.nativeObj);
    }

    public void set_maxConvexity(float maxConvexity) {
        set_maxConvexity_0(this.nativeObj, maxConvexity);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
