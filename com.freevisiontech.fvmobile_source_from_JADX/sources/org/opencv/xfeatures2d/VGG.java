package org.opencv.xfeatures2d;

import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.Feature2D;

public class VGG extends Feature2D {
    private static native void compute_0(long j, long j2, long j3, long j4);

    private static native long create_0(int i, float f, boolean z, boolean z2, float f2, boolean z3);

    private static native long create_1();

    private static native void delete(long j);

    protected VGG(long addr) {
        super(addr);
    }

    public static VGG create(int desc, float isigma, boolean img_normalize, boolean use_scale_orientation, float scale_factor, boolean dsc_normalize) {
        return new VGG(create_0(desc, isigma, img_normalize, use_scale_orientation, scale_factor, dsc_normalize));
    }

    public static VGG create() {
        return new VGG(create_1());
    }

    public void compute(Mat image, MatOfKeyPoint keypoints, Mat descriptors) {
        compute_0(this.nativeObj, image.nativeObj, keypoints.nativeObj, descriptors.nativeObj);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
