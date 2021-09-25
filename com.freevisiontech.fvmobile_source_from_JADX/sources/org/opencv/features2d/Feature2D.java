package org.opencv.features2d;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Algorithm;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.utils.Converters;

public class Feature2D extends Algorithm {
    private static native void compute_0(long j, long j2, long j3, long j4);

    private static native void compute_1(long j, long j2, long j3, long j4);

    private static native int defaultNorm_0(long j);

    private static native void delete(long j);

    private static native int descriptorSize_0(long j);

    private static native int descriptorType_0(long j);

    private static native void detectAndCompute_0(long j, long j2, long j3, long j4, long j5, boolean z);

    private static native void detectAndCompute_1(long j, long j2, long j3, long j4, long j5);

    private static native void detect_0(long j, long j2, long j3, long j4);

    private static native void detect_1(long j, long j2, long j3);

    private static native void detect_2(long j, long j2, long j3, long j4);

    private static native void detect_3(long j, long j2, long j3);

    private static native boolean empty_0(long j);

    private static native void read_0(long j, String str);

    private static native void write_0(long j, String str);

    protected Feature2D(long addr) {
        super(addr);
    }

    public boolean empty() {
        return empty_0(this.nativeObj);
    }

    public int defaultNorm() {
        return defaultNorm_0(this.nativeObj);
    }

    public int descriptorSize() {
        return descriptorSize_0(this.nativeObj);
    }

    public int descriptorType() {
        return descriptorType_0(this.nativeObj);
    }

    public void compute(Mat image, MatOfKeyPoint keypoints, Mat descriptors) {
        compute_0(this.nativeObj, image.nativeObj, keypoints.nativeObj, descriptors.nativeObj);
    }

    public void compute(List<Mat> images, List<MatOfKeyPoint> keypoints, List<Mat> descriptors) {
        Mat images_mat = Converters.vector_Mat_to_Mat(images);
        Mat keypoints_mat = Converters.vector_vector_KeyPoint_to_Mat(keypoints, new ArrayList<>(keypoints != null ? keypoints.size() : 0));
        Mat descriptors_mat = new Mat();
        compute_1(this.nativeObj, images_mat.nativeObj, keypoints_mat.nativeObj, descriptors_mat.nativeObj);
        Converters.Mat_to_vector_vector_KeyPoint(keypoints_mat, keypoints);
        keypoints_mat.release();
        Converters.Mat_to_vector_Mat(descriptors_mat, descriptors);
        descriptors_mat.release();
    }

    public void detect(Mat image, MatOfKeyPoint keypoints, Mat mask) {
        detect_0(this.nativeObj, image.nativeObj, keypoints.nativeObj, mask.nativeObj);
    }

    public void detect(Mat image, MatOfKeyPoint keypoints) {
        detect_1(this.nativeObj, image.nativeObj, keypoints.nativeObj);
    }

    public void detect(List<Mat> images, List<MatOfKeyPoint> keypoints, List<Mat> masks) {
        Mat images_mat = Converters.vector_Mat_to_Mat(images);
        Mat keypoints_mat = new Mat();
        detect_2(this.nativeObj, images_mat.nativeObj, keypoints_mat.nativeObj, Converters.vector_Mat_to_Mat(masks).nativeObj);
        Converters.Mat_to_vector_vector_KeyPoint(keypoints_mat, keypoints);
        keypoints_mat.release();
    }

    public void detect(List<Mat> images, List<MatOfKeyPoint> keypoints) {
        Mat images_mat = Converters.vector_Mat_to_Mat(images);
        Mat keypoints_mat = new Mat();
        detect_3(this.nativeObj, images_mat.nativeObj, keypoints_mat.nativeObj);
        Converters.Mat_to_vector_vector_KeyPoint(keypoints_mat, keypoints);
        keypoints_mat.release();
    }

    public void detectAndCompute(Mat image, Mat mask, MatOfKeyPoint keypoints, Mat descriptors, boolean useProvidedKeypoints) {
        detectAndCompute_0(this.nativeObj, image.nativeObj, mask.nativeObj, keypoints.nativeObj, descriptors.nativeObj, useProvidedKeypoints);
    }

    public void detectAndCompute(Mat image, Mat mask, MatOfKeyPoint keypoints, Mat descriptors) {
        detectAndCompute_1(this.nativeObj, image.nativeObj, mask.nativeObj, keypoints.nativeObj, descriptors.nativeObj);
    }

    public void read(String fileName) {
        read_0(this.nativeObj, fileName);
    }

    public void write(String fileName) {
        write_0(this.nativeObj, fileName);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
