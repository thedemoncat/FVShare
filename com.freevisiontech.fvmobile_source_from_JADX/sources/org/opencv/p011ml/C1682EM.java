package org.opencv.p011ml;

import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.TermCriteria;
import org.opencv.utils.Converters;

/* renamed from: org.opencv.ml.EM */
public class C1682EM extends StatModel {
    public static final int COV_MAT_DEFAULT = 1;
    public static final int COV_MAT_DIAGONAL = 1;
    public static final int COV_MAT_GENERIC = 2;
    public static final int COV_MAT_SPHERICAL = 0;
    public static final int DEFAULT_MAX_ITERS = 100;
    public static final int DEFAULT_NCLUSTERS = 5;
    public static final int START_AUTO_STEP = 0;
    public static final int START_E_STEP = 1;
    public static final int START_M_STEP = 2;

    private static native long create_0();

    private static native void delete(long j);

    private static native int getClustersNumber_0(long j);

    private static native int getCovarianceMatrixType_0(long j);

    private static native void getCovs_0(long j, long j2);

    private static native long getMeans_0(long j);

    private static native double[] getTermCriteria_0(long j);

    private static native long getWeights_0(long j);

    private static native double[] predict2_0(long j, long j2, long j3);

    private static native void setClustersNumber_0(long j, int i);

    private static native void setCovarianceMatrixType_0(long j, int i);

    private static native void setTermCriteria_0(long j, int i, int i2, double d);

    private static native boolean trainEM_0(long j, long j2, long j3, long j4, long j5);

    private static native boolean trainEM_1(long j, long j2);

    private static native boolean trainE_0(long j, long j2, long j3, long j4, long j5, long j6, long j7, long j8);

    private static native boolean trainE_1(long j, long j2, long j3);

    private static native boolean trainM_0(long j, long j2, long j3, long j4, long j5, long j6);

    private static native boolean trainM_1(long j, long j2, long j3);

    protected C1682EM(long addr) {
        super(addr);
    }

    public Mat getMeans() {
        return new Mat(getMeans_0(this.nativeObj));
    }

    public Mat getWeights() {
        return new Mat(getWeights_0(this.nativeObj));
    }

    public static C1682EM create() {
        return new C1682EM(create_0());
    }

    public TermCriteria getTermCriteria() {
        return new TermCriteria(getTermCriteria_0(this.nativeObj));
    }

    public double[] predict2(Mat sample, Mat probs) {
        return predict2_0(this.nativeObj, sample.nativeObj, probs.nativeObj);
    }

    public boolean trainE(Mat samples, Mat means0, Mat covs0, Mat weights0, Mat logLikelihoods, Mat labels, Mat probs) {
        return trainE_0(this.nativeObj, samples.nativeObj, means0.nativeObj, covs0.nativeObj, weights0.nativeObj, logLikelihoods.nativeObj, labels.nativeObj, probs.nativeObj);
    }

    public boolean trainE(Mat samples, Mat means0) {
        return trainE_1(this.nativeObj, samples.nativeObj, means0.nativeObj);
    }

    public boolean trainEM(Mat samples, Mat logLikelihoods, Mat labels, Mat probs) {
        return trainEM_0(this.nativeObj, samples.nativeObj, logLikelihoods.nativeObj, labels.nativeObj, probs.nativeObj);
    }

    public boolean trainEM(Mat samples) {
        return trainEM_1(this.nativeObj, samples.nativeObj);
    }

    public boolean trainM(Mat samples, Mat probs0, Mat logLikelihoods, Mat labels, Mat probs) {
        return trainM_0(this.nativeObj, samples.nativeObj, probs0.nativeObj, logLikelihoods.nativeObj, labels.nativeObj, probs.nativeObj);
    }

    public boolean trainM(Mat samples, Mat probs0) {
        return trainM_1(this.nativeObj, samples.nativeObj, probs0.nativeObj);
    }

    public int getClustersNumber() {
        return getClustersNumber_0(this.nativeObj);
    }

    public int getCovarianceMatrixType() {
        return getCovarianceMatrixType_0(this.nativeObj);
    }

    public void getCovs(List<Mat> covs) {
        Mat covs_mat = new Mat();
        getCovs_0(this.nativeObj, covs_mat.nativeObj);
        Converters.Mat_to_vector_Mat(covs_mat, covs);
        covs_mat.release();
    }

    public void setClustersNumber(int val) {
        setClustersNumber_0(this.nativeObj, val);
    }

    public void setCovarianceMatrixType(int val) {
        setCovarianceMatrixType_0(this.nativeObj, val);
    }

    public void setTermCriteria(TermCriteria val) {
        setTermCriteria_0(this.nativeObj, val.type, val.maxCount, val.epsilon);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
