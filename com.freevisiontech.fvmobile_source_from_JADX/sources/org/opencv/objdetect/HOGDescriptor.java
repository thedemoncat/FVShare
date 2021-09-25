package org.opencv.objdetect;

import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfRect;
import org.opencv.core.Size;

public class HOGDescriptor {
    public static final int DEFAULT_NLEVELS = 64;
    public static final int L2Hys = 0;
    protected final long nativeObj;

    private static native long HOGDescriptor_0(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, int i, int i2, double d9, int i3, double d10, boolean z, int i4, boolean z2);

    private static native long HOGDescriptor_1(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, int i);

    private static native long HOGDescriptor_2(String str);

    private static native long HOGDescriptor_3();

    private static native boolean checkDetectorSize_0(long j);

    private static native void computeGradient_0(long j, long j2, long j3, long j4, double d, double d2, double d3, double d4);

    private static native void computeGradient_1(long j, long j2, long j3, long j4);

    private static native void compute_0(long j, long j2, long j3, double d, double d2, double d3, double d4, long j4);

    private static native void compute_1(long j, long j2, long j3);

    private static native void delete(long j);

    private static native void detectMultiScale_0(long j, long j2, long j3, long j4, double d, double d2, double d3, double d4, double d5, double d6, double d7, boolean z);

    private static native void detectMultiScale_1(long j, long j2, long j3, long j4);

    private static native void detect_0(long j, long j2, long j3, long j4, double d, double d2, double d3, double d4, double d5, long j5);

    private static native void detect_1(long j, long j2, long j3, long j4);

    private static native long getDaimlerPeopleDetector_0();

    private static native long getDefaultPeopleDetector_0();

    private static native long getDescriptorSize_0(long j);

    private static native double getWinSigma_0(long j);

    private static native double get_L2HysThreshold_0(long j);

    private static native double[] get_blockSize_0(long j);

    private static native double[] get_blockStride_0(long j);

    private static native double[] get_cellSize_0(long j);

    private static native int get_derivAperture_0(long j);

    private static native boolean get_gammaCorrection_0(long j);

    private static native int get_histogramNormType_0(long j);

    private static native int get_nbins_0(long j);

    private static native int get_nlevels_0(long j);

    private static native boolean get_signedGradient_0(long j);

    private static native long get_svmDetector_0(long j);

    private static native double get_winSigma_0(long j);

    private static native double[] get_winSize_0(long j);

    private static native boolean load_0(long j, String str, String str2);

    private static native boolean load_1(long j, String str);

    private static native void save_0(long j, String str, String str2);

    private static native void save_1(long j, String str);

    private static native void setSVMDetector_0(long j, long j2);

    protected HOGDescriptor(long addr) {
        this.nativeObj = addr;
    }

    public HOGDescriptor(Size _winSize, Size _blockSize, Size _blockStride, Size _cellSize, int _nbins, int _derivAperture, double _winSigma, int _histogramNormType, double _L2HysThreshold, boolean _gammaCorrection, int _nlevels, boolean _signedGradient) {
        this.nativeObj = HOGDescriptor_0(_winSize.width, _winSize.height, _blockSize.width, _blockSize.height, _blockStride.width, _blockStride.height, _cellSize.width, _cellSize.height, _nbins, _derivAperture, _winSigma, _histogramNormType, _L2HysThreshold, _gammaCorrection, _nlevels, _signedGradient);
    }

    public HOGDescriptor(Size _winSize, Size _blockSize, Size _blockStride, Size _cellSize, int _nbins) {
        this.nativeObj = HOGDescriptor_1(_winSize.width, _winSize.height, _blockSize.width, _blockSize.height, _blockStride.width, _blockStride.height, _cellSize.width, _cellSize.height, _nbins);
    }

    public HOGDescriptor(String filename) {
        this.nativeObj = HOGDescriptor_2(filename);
    }

    public HOGDescriptor() {
        this.nativeObj = HOGDescriptor_3();
    }

    public boolean checkDetectorSize() {
        return checkDetectorSize_0(this.nativeObj);
    }

    public boolean load(String filename, String objname) {
        return load_0(this.nativeObj, filename, objname);
    }

    public boolean load(String filename) {
        return load_1(this.nativeObj, filename);
    }

    public double getWinSigma() {
        return getWinSigma_0(this.nativeObj);
    }

    public long getDescriptorSize() {
        return getDescriptorSize_0(this.nativeObj);
    }

    public static MatOfFloat getDaimlerPeopleDetector() {
        return MatOfFloat.fromNativeAddr(getDaimlerPeopleDetector_0());
    }

    public static MatOfFloat getDefaultPeopleDetector() {
        return MatOfFloat.fromNativeAddr(getDefaultPeopleDetector_0());
    }

    public void compute(Mat img, MatOfFloat descriptors, Size winStride, Size padding, MatOfPoint locations) {
        compute_0(this.nativeObj, img.nativeObj, descriptors.nativeObj, winStride.width, winStride.height, padding.width, padding.height, locations.nativeObj);
    }

    public void compute(Mat img, MatOfFloat descriptors) {
        compute_1(this.nativeObj, img.nativeObj, descriptors.nativeObj);
    }

    public void computeGradient(Mat img, Mat grad, Mat angleOfs, Size paddingTL, Size paddingBR) {
        computeGradient_0(this.nativeObj, img.nativeObj, grad.nativeObj, angleOfs.nativeObj, paddingTL.width, paddingTL.height, paddingBR.width, paddingBR.height);
    }

    public void computeGradient(Mat img, Mat grad, Mat angleOfs) {
        computeGradient_1(this.nativeObj, img.nativeObj, grad.nativeObj, angleOfs.nativeObj);
    }

    public void detect(Mat img, MatOfPoint foundLocations, MatOfDouble weights, double hitThreshold, Size winStride, Size padding, MatOfPoint searchLocations) {
        detect_0(this.nativeObj, img.nativeObj, foundLocations.nativeObj, weights.nativeObj, hitThreshold, winStride.width, winStride.height, padding.width, padding.height, searchLocations.nativeObj);
    }

    public void detect(Mat img, MatOfPoint foundLocations, MatOfDouble weights) {
        detect_1(this.nativeObj, img.nativeObj, foundLocations.nativeObj, weights.nativeObj);
    }

    public void detectMultiScale(Mat img, MatOfRect foundLocations, MatOfDouble foundWeights, double hitThreshold, Size winStride, Size padding, double scale, double finalThreshold, boolean useMeanshiftGrouping) {
        detectMultiScale_0(this.nativeObj, img.nativeObj, foundLocations.nativeObj, foundWeights.nativeObj, hitThreshold, winStride.width, winStride.height, padding.width, padding.height, scale, finalThreshold, useMeanshiftGrouping);
    }

    public void detectMultiScale(Mat img, MatOfRect foundLocations, MatOfDouble foundWeights) {
        detectMultiScale_1(this.nativeObj, img.nativeObj, foundLocations.nativeObj, foundWeights.nativeObj);
    }

    public void save(String filename, String objname) {
        save_0(this.nativeObj, filename, objname);
    }

    public void save(String filename) {
        save_1(this.nativeObj, filename);
    }

    public void setSVMDetector(Mat _svmdetector) {
        setSVMDetector_0(this.nativeObj, _svmdetector.nativeObj);
    }

    public Size get_winSize() {
        return new Size(get_winSize_0(this.nativeObj));
    }

    public Size get_blockSize() {
        return new Size(get_blockSize_0(this.nativeObj));
    }

    public Size get_blockStride() {
        return new Size(get_blockStride_0(this.nativeObj));
    }

    public Size get_cellSize() {
        return new Size(get_cellSize_0(this.nativeObj));
    }

    public int get_nbins() {
        return get_nbins_0(this.nativeObj);
    }

    public int get_derivAperture() {
        return get_derivAperture_0(this.nativeObj);
    }

    public double get_winSigma() {
        return get_winSigma_0(this.nativeObj);
    }

    public int get_histogramNormType() {
        return get_histogramNormType_0(this.nativeObj);
    }

    public double get_L2HysThreshold() {
        return get_L2HysThreshold_0(this.nativeObj);
    }

    public boolean get_gammaCorrection() {
        return get_gammaCorrection_0(this.nativeObj);
    }

    public MatOfFloat get_svmDetector() {
        return MatOfFloat.fromNativeAddr(get_svmDetector_0(this.nativeObj));
    }

    public int get_nlevels() {
        return get_nlevels_0(this.nativeObj);
    }

    public boolean get_signedGradient() {
        return get_signedGradient_0(this.nativeObj);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
