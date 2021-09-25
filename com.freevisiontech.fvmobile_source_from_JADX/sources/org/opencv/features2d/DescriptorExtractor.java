package org.opencv.features2d;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.utils.Converters;

public class DescriptorExtractor {
    public static final int AKAZE = 7;
    public static final int BRIEF = 4;
    public static final int BRISK = 5;
    public static final int FREAK = 6;
    private static final int OPPONENTEXTRACTOR = 1000;
    public static final int OPPONENT_AKAZE = 1007;
    public static final int OPPONENT_BRIEF = 1004;
    public static final int OPPONENT_BRISK = 1005;
    public static final int OPPONENT_FREAK = 1006;
    public static final int OPPONENT_ORB = 1003;
    public static final int OPPONENT_SIFT = 1001;
    public static final int OPPONENT_SURF = 1002;
    public static final int ORB = 3;
    public static final int SIFT = 1;
    public static final int SURF = 2;
    protected final long nativeObj;

    private static native void compute_0(long j, long j2, long j3, long j4);

    private static native void compute_1(long j, long j2, long j3, long j4);

    private static native long create_0(int i);

    private static native void delete(long j);

    private static native int descriptorSize_0(long j);

    private static native int descriptorType_0(long j);

    private static native boolean empty_0(long j);

    private static native void read_0(long j, String str);

    private static native void write_0(long j, String str);

    protected DescriptorExtractor(long addr) {
        this.nativeObj = addr;
    }

    public static DescriptorExtractor create(int extractorType) {
        return new DescriptorExtractor(create_0(extractorType));
    }

    public boolean empty() {
        return empty_0(this.nativeObj);
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
