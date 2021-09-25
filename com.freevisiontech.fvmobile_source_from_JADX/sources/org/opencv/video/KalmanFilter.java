package org.opencv.video;

import org.opencv.core.Mat;

public class KalmanFilter {
    protected final long nativeObj;

    private static native long KalmanFilter_0(int i, int i2, int i3, int i4);

    private static native long KalmanFilter_1(int i, int i2);

    private static native long KalmanFilter_2();

    private static native long correct_0(long j, long j2);

    private static native void delete(long j);

    private static native long get_controlMatrix_0(long j);

    private static native long get_errorCovPost_0(long j);

    private static native long get_errorCovPre_0(long j);

    private static native long get_gain_0(long j);

    private static native long get_measurementMatrix_0(long j);

    private static native long get_measurementNoiseCov_0(long j);

    private static native long get_processNoiseCov_0(long j);

    private static native long get_statePost_0(long j);

    private static native long get_statePre_0(long j);

    private static native long get_transitionMatrix_0(long j);

    private static native long predict_0(long j, long j2);

    private static native long predict_1(long j);

    private static native void set_controlMatrix_0(long j, long j2);

    private static native void set_errorCovPost_0(long j, long j2);

    private static native void set_errorCovPre_0(long j, long j2);

    private static native void set_gain_0(long j, long j2);

    private static native void set_measurementMatrix_0(long j, long j2);

    private static native void set_measurementNoiseCov_0(long j, long j2);

    private static native void set_processNoiseCov_0(long j, long j2);

    private static native void set_statePost_0(long j, long j2);

    private static native void set_statePre_0(long j, long j2);

    private static native void set_transitionMatrix_0(long j, long j2);

    protected KalmanFilter(long addr) {
        this.nativeObj = addr;
    }

    public KalmanFilter(int dynamParams, int measureParams, int controlParams, int type) {
        this.nativeObj = KalmanFilter_0(dynamParams, measureParams, controlParams, type);
    }

    public KalmanFilter(int dynamParams, int measureParams) {
        this.nativeObj = KalmanFilter_1(dynamParams, measureParams);
    }

    public KalmanFilter() {
        this.nativeObj = KalmanFilter_2();
    }

    public Mat correct(Mat measurement) {
        return new Mat(correct_0(this.nativeObj, measurement.nativeObj));
    }

    public Mat predict(Mat control) {
        return new Mat(predict_0(this.nativeObj, control.nativeObj));
    }

    public Mat predict() {
        return new Mat(predict_1(this.nativeObj));
    }

    public Mat get_statePre() {
        return new Mat(get_statePre_0(this.nativeObj));
    }

    public void set_statePre(Mat statePre) {
        set_statePre_0(this.nativeObj, statePre.nativeObj);
    }

    public Mat get_statePost() {
        return new Mat(get_statePost_0(this.nativeObj));
    }

    public void set_statePost(Mat statePost) {
        set_statePost_0(this.nativeObj, statePost.nativeObj);
    }

    public Mat get_transitionMatrix() {
        return new Mat(get_transitionMatrix_0(this.nativeObj));
    }

    public void set_transitionMatrix(Mat transitionMatrix) {
        set_transitionMatrix_0(this.nativeObj, transitionMatrix.nativeObj);
    }

    public Mat get_controlMatrix() {
        return new Mat(get_controlMatrix_0(this.nativeObj));
    }

    public void set_controlMatrix(Mat controlMatrix) {
        set_controlMatrix_0(this.nativeObj, controlMatrix.nativeObj);
    }

    public Mat get_measurementMatrix() {
        return new Mat(get_measurementMatrix_0(this.nativeObj));
    }

    public void set_measurementMatrix(Mat measurementMatrix) {
        set_measurementMatrix_0(this.nativeObj, measurementMatrix.nativeObj);
    }

    public Mat get_processNoiseCov() {
        return new Mat(get_processNoiseCov_0(this.nativeObj));
    }

    public void set_processNoiseCov(Mat processNoiseCov) {
        set_processNoiseCov_0(this.nativeObj, processNoiseCov.nativeObj);
    }

    public Mat get_measurementNoiseCov() {
        return new Mat(get_measurementNoiseCov_0(this.nativeObj));
    }

    public void set_measurementNoiseCov(Mat measurementNoiseCov) {
        set_measurementNoiseCov_0(this.nativeObj, measurementNoiseCov.nativeObj);
    }

    public Mat get_errorCovPre() {
        return new Mat(get_errorCovPre_0(this.nativeObj));
    }

    public void set_errorCovPre(Mat errorCovPre) {
        set_errorCovPre_0(this.nativeObj, errorCovPre.nativeObj);
    }

    public Mat get_gain() {
        return new Mat(get_gain_0(this.nativeObj));
    }

    public void set_gain(Mat gain) {
        set_gain_0(this.nativeObj, gain.nativeObj);
    }

    public Mat get_errorCovPost() {
        return new Mat(get_errorCovPost_0(this.nativeObj));
    }

    public void set_errorCovPost(Mat errorCovPost) {
        set_errorCovPost_0(this.nativeObj, errorCovPost.nativeObj);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
