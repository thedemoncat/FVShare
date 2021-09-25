package org.opencv.p011ml;

import org.opencv.core.Mat;
import org.opencv.core.TermCriteria;

/* renamed from: org.opencv.ml.SVM */
public class SVM extends StatModel {

    /* renamed from: C */
    public static final int f1132C = 0;
    public static final int CHI2 = 4;
    public static final int COEF = 4;
    public static final int CUSTOM = -1;
    public static final int C_SVC = 100;
    public static final int DEGREE = 5;
    public static final int EPS_SVR = 103;
    public static final int GAMMA = 1;
    public static final int INTER = 5;
    public static final int LINEAR = 0;

    /* renamed from: NU */
    public static final int f1133NU = 3;
    public static final int NU_SVC = 101;
    public static final int NU_SVR = 104;
    public static final int ONE_CLASS = 102;

    /* renamed from: P */
    public static final int f1134P = 2;
    public static final int POLY = 1;
    public static final int RBF = 2;
    public static final int SIGMOID = 3;

    private static native long create_0();

    private static native void delete(long j);

    private static native double getC_0(long j);

    private static native long getClassWeights_0(long j);

    private static native double getCoef0_0(long j);

    private static native double getDecisionFunction_0(long j, int i, long j2, long j3);

    private static native double getDegree_0(long j);

    private static native double getGamma_0(long j);

    private static native int getKernelType_0(long j);

    private static native double getNu_0(long j);

    private static native double getP_0(long j);

    private static native long getSupportVectors_0(long j);

    private static native double[] getTermCriteria_0(long j);

    private static native int getType_0(long j);

    private static native long getUncompressedSupportVectors_0(long j);

    private static native long load_0(String str);

    private static native void setC_0(long j, double d);

    private static native void setClassWeights_0(long j, long j2);

    private static native void setCoef0_0(long j, double d);

    private static native void setDegree_0(long j, double d);

    private static native void setGamma_0(long j, double d);

    private static native void setKernel_0(long j, int i);

    private static native void setNu_0(long j, double d);

    private static native void setP_0(long j, double d);

    private static native void setTermCriteria_0(long j, int i, int i2, double d);

    private static native void setType_0(long j, int i);

    protected SVM(long addr) {
        super(addr);
    }

    public Mat getClassWeights() {
        return new Mat(getClassWeights_0(this.nativeObj));
    }

    public Mat getSupportVectors() {
        return new Mat(getSupportVectors_0(this.nativeObj));
    }

    public Mat getUncompressedSupportVectors() {
        return new Mat(getUncompressedSupportVectors_0(this.nativeObj));
    }

    public static SVM create() {
        return new SVM(create_0());
    }

    public static SVM load(String filepath) {
        return new SVM(load_0(filepath));
    }

    public TermCriteria getTermCriteria() {
        return new TermCriteria(getTermCriteria_0(this.nativeObj));
    }

    public double getC() {
        return getC_0(this.nativeObj);
    }

    public double getCoef0() {
        return getCoef0_0(this.nativeObj);
    }

    public double getDecisionFunction(int i, Mat alpha, Mat svidx) {
        return getDecisionFunction_0(this.nativeObj, i, alpha.nativeObj, svidx.nativeObj);
    }

    public double getDegree() {
        return getDegree_0(this.nativeObj);
    }

    public double getGamma() {
        return getGamma_0(this.nativeObj);
    }

    public double getNu() {
        return getNu_0(this.nativeObj);
    }

    public double getP() {
        return getP_0(this.nativeObj);
    }

    public int getKernelType() {
        return getKernelType_0(this.nativeObj);
    }

    public int getType() {
        return getType_0(this.nativeObj);
    }

    public void setC(double val) {
        setC_0(this.nativeObj, val);
    }

    public void setClassWeights(Mat val) {
        setClassWeights_0(this.nativeObj, val.nativeObj);
    }

    public void setCoef0(double val) {
        setCoef0_0(this.nativeObj, val);
    }

    public void setDegree(double val) {
        setDegree_0(this.nativeObj, val);
    }

    public void setGamma(double val) {
        setGamma_0(this.nativeObj, val);
    }

    public void setKernel(int kernelType) {
        setKernel_0(this.nativeObj, kernelType);
    }

    public void setNu(double val) {
        setNu_0(this.nativeObj, val);
    }

    public void setP(double val) {
        setP_0(this.nativeObj, val);
    }

    public void setTermCriteria(TermCriteria val) {
        setTermCriteria_0(this.nativeObj, val.type, val.maxCount, val.epsilon);
    }

    public void setType(int val) {
        setType_0(this.nativeObj, val);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
