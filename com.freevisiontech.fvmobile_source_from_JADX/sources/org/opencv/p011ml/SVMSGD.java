package org.opencv.p011ml;

import org.opencv.core.Mat;
import org.opencv.core.TermCriteria;

/* renamed from: org.opencv.ml.SVMSGD */
public class SVMSGD extends StatModel {
    public static final int ASGD = 1;
    public static final int HARD_MARGIN = 1;
    public static final int SGD = 0;
    public static final int SOFT_MARGIN = 0;

    private static native long create_0();

    private static native void delete(long j);

    private static native float getInitialStepSize_0(long j);

    private static native float getMarginRegularization_0(long j);

    private static native int getMarginType_0(long j);

    private static native float getShift_0(long j);

    private static native float getStepDecreasingPower_0(long j);

    private static native int getSvmsgdType_0(long j);

    private static native double[] getTermCriteria_0(long j);

    private static native long getWeights_0(long j);

    private static native void setInitialStepSize_0(long j, float f);

    private static native void setMarginRegularization_0(long j, float f);

    private static native void setMarginType_0(long j, int i);

    private static native void setOptimalParameters_0(long j, int i, int i2);

    private static native void setOptimalParameters_1(long j);

    private static native void setStepDecreasingPower_0(long j, float f);

    private static native void setSvmsgdType_0(long j, int i);

    private static native void setTermCriteria_0(long j, int i, int i2, double d);

    protected SVMSGD(long addr) {
        super(addr);
    }

    public Mat getWeights() {
        return new Mat(getWeights_0(this.nativeObj));
    }

    public static SVMSGD create() {
        return new SVMSGD(create_0());
    }

    public TermCriteria getTermCriteria() {
        return new TermCriteria(getTermCriteria_0(this.nativeObj));
    }

    public float getInitialStepSize() {
        return getInitialStepSize_0(this.nativeObj);
    }

    public float getMarginRegularization() {
        return getMarginRegularization_0(this.nativeObj);
    }

    public float getShift() {
        return getShift_0(this.nativeObj);
    }

    public float getStepDecreasingPower() {
        return getStepDecreasingPower_0(this.nativeObj);
    }

    public int getMarginType() {
        return getMarginType_0(this.nativeObj);
    }

    public int getSvmsgdType() {
        return getSvmsgdType_0(this.nativeObj);
    }

    public void setInitialStepSize(float InitialStepSize) {
        setInitialStepSize_0(this.nativeObj, InitialStepSize);
    }

    public void setMarginRegularization(float marginRegularization) {
        setMarginRegularization_0(this.nativeObj, marginRegularization);
    }

    public void setMarginType(int marginType) {
        setMarginType_0(this.nativeObj, marginType);
    }

    public void setOptimalParameters(int svmsgdType, int marginType) {
        setOptimalParameters_0(this.nativeObj, svmsgdType, marginType);
    }

    public void setOptimalParameters() {
        setOptimalParameters_1(this.nativeObj);
    }

    public void setStepDecreasingPower(float stepDecreasingPower) {
        setStepDecreasingPower_0(this.nativeObj, stepDecreasingPower);
    }

    public void setSvmsgdType(int svmsgdType) {
        setSvmsgdType_0(this.nativeObj, svmsgdType);
    }

    public void setTermCriteria(TermCriteria val) {
        setTermCriteria_0(this.nativeObj, val.type, val.maxCount, val.epsilon);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
