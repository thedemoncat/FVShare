package org.opencv.p011ml;

import org.opencv.core.Mat;

/* renamed from: org.opencv.ml.NormalBayesClassifier */
public class NormalBayesClassifier extends StatModel {
    private static native long create_0();

    private static native void delete(long j);

    private static native float predictProb_0(long j, long j2, long j3, long j4, int i);

    private static native float predictProb_1(long j, long j2, long j3, long j4);

    protected NormalBayesClassifier(long addr) {
        super(addr);
    }

    public static NormalBayesClassifier create() {
        return new NormalBayesClassifier(create_0());
    }

    public float predictProb(Mat inputs, Mat outputs, Mat outputProbs, int flags) {
        return predictProb_0(this.nativeObj, inputs.nativeObj, outputs.nativeObj, outputProbs.nativeObj, flags);
    }

    public float predictProb(Mat inputs, Mat outputs, Mat outputProbs) {
        return predictProb_1(this.nativeObj, inputs.nativeObj, outputs.nativeObj, outputProbs.nativeObj);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
