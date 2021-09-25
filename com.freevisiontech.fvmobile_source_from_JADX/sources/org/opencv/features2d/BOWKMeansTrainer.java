package org.opencv.features2d;

import org.opencv.core.Mat;
import org.opencv.core.TermCriteria;

public class BOWKMeansTrainer extends BOWTrainer {
    private static native long BOWKMeansTrainer_0(int i, int i2, int i3, double d, int i4, int i5);

    private static native long BOWKMeansTrainer_1(int i);

    private static native long cluster_0(long j, long j2);

    private static native long cluster_1(long j);

    private static native void delete(long j);

    protected BOWKMeansTrainer(long addr) {
        super(addr);
    }

    public BOWKMeansTrainer(int clusterCount, TermCriteria termcrit, int attempts, int flags) {
        super(BOWKMeansTrainer_0(clusterCount, termcrit.type, termcrit.maxCount, termcrit.epsilon, attempts, flags));
    }

    public BOWKMeansTrainer(int clusterCount) {
        super(BOWKMeansTrainer_1(clusterCount));
    }

    public Mat cluster(Mat descriptors) {
        return new Mat(cluster_0(this.nativeObj, descriptors.nativeObj));
    }

    public Mat cluster() {
        return new Mat(cluster_1(this.nativeObj));
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
