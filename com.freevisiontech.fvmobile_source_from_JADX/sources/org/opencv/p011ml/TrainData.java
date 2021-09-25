package org.opencv.p011ml;

import org.opencv.core.Mat;

/* renamed from: org.opencv.ml.TrainData */
public class TrainData {
    protected final long nativeObj;

    private static native long create_0(long j, int i, long j2, long j3, long j4, long j5, long j6);

    private static native long create_1(long j, int i, long j2);

    private static native void delete(long j);

    private static native int getCatCount_0(long j, int i);

    private static native long getCatMap_0(long j);

    private static native long getCatOfs_0(long j);

    private static native long getClassLabels_0(long j);

    private static native long getDefaultSubstValues_0(long j);

    private static native int getLayout_0(long j);

    private static native long getMissing_0(long j);

    private static native int getNAllVars_0(long j);

    private static native int getNSamples_0(long j);

    private static native int getNTestSamples_0(long j);

    private static native int getNTrainSamples_0(long j);

    private static native int getNVars_0(long j);

    private static native long getNormCatResponses_0(long j);

    private static native int getResponseType_0(long j);

    private static native long getResponses_0(long j);

    private static native long getSampleWeights_0(long j);

    private static native void getSample_0(long j, long j2, int i, float f);

    private static native long getSamples_0(long j);

    private static native long getSubVector_0(long j, long j2);

    private static native long getTestNormCatResponses_0(long j);

    private static native long getTestResponses_0(long j);

    private static native long getTestSampleIdx_0(long j);

    private static native long getTestSampleWeights_0(long j);

    private static native long getTestSamples_0(long j);

    private static native long getTrainNormCatResponses_0(long j);

    private static native long getTrainResponses_0(long j);

    private static native long getTrainSampleIdx_0(long j);

    private static native long getTrainSampleWeights_0(long j);

    private static native long getTrainSamples_0(long j, int i, boolean z, boolean z2);

    private static native long getTrainSamples_1(long j);

    private static native void getValues_0(long j, int i, long j2, float f);

    private static native long getVarIdx_0(long j);

    private static native long getVarSymbolFlags_0(long j);

    private static native long getVarType_0(long j);

    private static native void setTrainTestSplitRatio_0(long j, double d, boolean z);

    private static native void setTrainTestSplitRatio_1(long j, double d);

    private static native void setTrainTestSplit_0(long j, int i, boolean z);

    private static native void setTrainTestSplit_1(long j, int i);

    private static native void shuffleTrainTest_0(long j);

    protected TrainData(long addr) {
        this.nativeObj = addr;
    }

    public Mat getCatMap() {
        return new Mat(getCatMap_0(this.nativeObj));
    }

    public Mat getCatOfs() {
        return new Mat(getCatOfs_0(this.nativeObj));
    }

    public Mat getClassLabels() {
        return new Mat(getClassLabels_0(this.nativeObj));
    }

    public Mat getDefaultSubstValues() {
        return new Mat(getDefaultSubstValues_0(this.nativeObj));
    }

    public Mat getMissing() {
        return new Mat(getMissing_0(this.nativeObj));
    }

    public Mat getNormCatResponses() {
        return new Mat(getNormCatResponses_0(this.nativeObj));
    }

    public Mat getResponses() {
        return new Mat(getResponses_0(this.nativeObj));
    }

    public Mat getSampleWeights() {
        return new Mat(getSampleWeights_0(this.nativeObj));
    }

    public Mat getSamples() {
        return new Mat(getSamples_0(this.nativeObj));
    }

    public static Mat getSubVector(Mat vec, Mat idx) {
        return new Mat(getSubVector_0(vec.nativeObj, idx.nativeObj));
    }

    public Mat getTestNormCatResponses() {
        return new Mat(getTestNormCatResponses_0(this.nativeObj));
    }

    public Mat getTestResponses() {
        return new Mat(getTestResponses_0(this.nativeObj));
    }

    public Mat getTestSampleIdx() {
        return new Mat(getTestSampleIdx_0(this.nativeObj));
    }

    public Mat getTestSampleWeights() {
        return new Mat(getTestSampleWeights_0(this.nativeObj));
    }

    public Mat getTestSamples() {
        return new Mat(getTestSamples_0(this.nativeObj));
    }

    public Mat getTrainNormCatResponses() {
        return new Mat(getTrainNormCatResponses_0(this.nativeObj));
    }

    public Mat getTrainResponses() {
        return new Mat(getTrainResponses_0(this.nativeObj));
    }

    public Mat getTrainSampleIdx() {
        return new Mat(getTrainSampleIdx_0(this.nativeObj));
    }

    public Mat getTrainSampleWeights() {
        return new Mat(getTrainSampleWeights_0(this.nativeObj));
    }

    public Mat getTrainSamples(int layout, boolean compressSamples, boolean compressVars) {
        return new Mat(getTrainSamples_0(this.nativeObj, layout, compressSamples, compressVars));
    }

    public Mat getTrainSamples() {
        return new Mat(getTrainSamples_1(this.nativeObj));
    }

    public Mat getVarIdx() {
        return new Mat(getVarIdx_0(this.nativeObj));
    }

    public Mat getVarSymbolFlags() {
        return new Mat(getVarSymbolFlags_0(this.nativeObj));
    }

    public Mat getVarType() {
        return new Mat(getVarType_0(this.nativeObj));
    }

    public static TrainData create(Mat samples, int layout, Mat responses, Mat varIdx, Mat sampleIdx, Mat sampleWeights, Mat varType) {
        return new TrainData(create_0(samples.nativeObj, layout, responses.nativeObj, varIdx.nativeObj, sampleIdx.nativeObj, sampleWeights.nativeObj, varType.nativeObj));
    }

    public static TrainData create(Mat samples, int layout, Mat responses) {
        return new TrainData(create_1(samples.nativeObj, layout, responses.nativeObj));
    }

    public int getCatCount(int vi) {
        return getCatCount_0(this.nativeObj, vi);
    }

    public int getLayout() {
        return getLayout_0(this.nativeObj);
    }

    public int getNAllVars() {
        return getNAllVars_0(this.nativeObj);
    }

    public int getNSamples() {
        return getNSamples_0(this.nativeObj);
    }

    public int getNTestSamples() {
        return getNTestSamples_0(this.nativeObj);
    }

    public int getNTrainSamples() {
        return getNTrainSamples_0(this.nativeObj);
    }

    public int getNVars() {
        return getNVars_0(this.nativeObj);
    }

    public int getResponseType() {
        return getResponseType_0(this.nativeObj);
    }

    public void getSample(Mat varIdx, int sidx, float buf) {
        getSample_0(this.nativeObj, varIdx.nativeObj, sidx, buf);
    }

    public void getValues(int vi, Mat sidx, float values) {
        getValues_0(this.nativeObj, vi, sidx.nativeObj, values);
    }

    public void setTrainTestSplit(int count, boolean shuffle) {
        setTrainTestSplit_0(this.nativeObj, count, shuffle);
    }

    public void setTrainTestSplit(int count) {
        setTrainTestSplit_1(this.nativeObj, count);
    }

    public void setTrainTestSplitRatio(double ratio, boolean shuffle) {
        setTrainTestSplitRatio_0(this.nativeObj, ratio, shuffle);
    }

    public void setTrainTestSplitRatio(double ratio) {
        setTrainTestSplitRatio_1(this.nativeObj, ratio);
    }

    public void shuffleTrainTest() {
        shuffleTrainTest_0(this.nativeObj);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
