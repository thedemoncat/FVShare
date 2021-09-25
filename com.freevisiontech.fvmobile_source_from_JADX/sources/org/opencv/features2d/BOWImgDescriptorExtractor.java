package org.opencv.features2d;

import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;

public class BOWImgDescriptorExtractor {
    protected final long nativeObj;

    private static native void compute_0(long j, long j2, long j3, long j4);

    private static native void delete(long j);

    private static native int descriptorSize_0(long j);

    private static native int descriptorType_0(long j);

    private static native long getVocabulary_0(long j);

    private static native void setVocabulary_0(long j, long j2);

    protected BOWImgDescriptorExtractor(long addr) {
        this.nativeObj = addr;
    }

    public Mat getVocabulary() {
        return new Mat(getVocabulary_0(this.nativeObj));
    }

    public int descriptorSize() {
        return descriptorSize_0(this.nativeObj);
    }

    public int descriptorType() {
        return descriptorType_0(this.nativeObj);
    }

    public void compute(Mat image, MatOfKeyPoint keypoints, Mat imgDescriptor) {
        compute_0(this.nativeObj, image.nativeObj, keypoints.nativeObj, imgDescriptor.nativeObj);
    }

    public void setVocabulary(Mat vocabulary) {
        setVocabulary_0(this.nativeObj, vocabulary.nativeObj);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
