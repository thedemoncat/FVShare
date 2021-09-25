package org.opencv.p011ml;

import org.opencv.core.Mat;
import org.opencv.core.TermCriteria;

/* renamed from: org.opencv.ml.LogisticRegression */
public class LogisticRegression extends StatModel {
    public static final int BATCH = 0;
    public static final int MINI_BATCH = 1;
    public static final int REG_DISABLE = -1;
    public static final int REG_L1 = 0;
    public static final int REG_L2 = 1;

    private static native long create_0();

    private static native void delete(long j);

    private static native int getIterations_0(long j);

    private static native double getLearningRate_0(long j);

    private static native int getMiniBatchSize_0(long j);

    private static native int getRegularization_0(long j);

    private static native double[] getTermCriteria_0(long j);

    private static native int getTrainMethod_0(long j);

    private static native long get_learnt_thetas_0(long j);

    private static native float predict_0(long j, long j2, long j3, int i);

    private static native float predict_1(long j, long j2);

    private static native void setIterations_0(long j, int i);

    private static native void setLearningRate_0(long j, double d);

    private static native void setMiniBatchSize_0(long j, int i);

    private static native void setRegularization_0(long j, int i);

    private static native void setTermCriteria_0(long j, int i, int i2, double d);

    private static native void setTrainMethod_0(long j, int i);

    protected LogisticRegression(long addr) {
        super(addr);
    }

    public Mat get_learnt_thetas() {
        return new Mat(get_learnt_thetas_0(this.nativeObj));
    }

    public static LogisticRegression create() {
        return new LogisticRegression(create_0());
    }

    public TermCriteria getTermCriteria() {
        return new TermCriteria(getTermCriteria_0(this.nativeObj));
    }

    public double getLearningRate() {
        return getLearningRate_0(this.nativeObj);
    }

    public float predict(Mat samples, Mat results, int flags) {
        return predict_0(this.nativeObj, samples.nativeObj, results.nativeObj, flags);
    }

    public float predict(Mat samples) {
        return predict_1(this.nativeObj, samples.nativeObj);
    }

    public int getIterations() {
        return getIterations_0(this.nativeObj);
    }

    public int getMiniBatchSize() {
        return getMiniBatchSize_0(this.nativeObj);
    }

    public int getRegularization() {
        return getRegularization_0(this.nativeObj);
    }

    public int getTrainMethod() {
        return getTrainMethod_0(this.nativeObj);
    }

    public void setIterations(int val) {
        setIterations_0(this.nativeObj, val);
    }

    public void setLearningRate(double val) {
        setLearningRate_0(this.nativeObj, val);
    }

    public void setMiniBatchSize(int val) {
        setMiniBatchSize_0(this.nativeObj, val);
    }

    public void setRegularization(int val) {
        setRegularization_0(this.nativeObj, val);
    }

    public void setTermCriteria(TermCriteria val) {
        setTermCriteria_0(this.nativeObj, val.type, val.maxCount, val.epsilon);
    }

    public void setTrainMethod(int val) {
        setTrainMethod_0(this.nativeObj, val);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
