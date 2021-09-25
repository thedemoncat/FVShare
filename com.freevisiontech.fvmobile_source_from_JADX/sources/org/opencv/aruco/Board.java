package org.opencv.aruco;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint3f;
import org.opencv.utils.Converters;

public class Board {
    protected final long nativeObj;

    private static native long create_0(long j, long j2, long j3);

    private static native void delete(long j);

    private static native long get_dictionary_0(long j);

    private static native long get_ids_0(long j);

    private static native long get_objPoints_0(long j);

    protected Board(long addr) {
        this.nativeObj = addr;
    }

    public static Board create(List<Mat> objPoints, Dictionary dictionary, Mat ids) {
        return new Board(create_0(Converters.vector_Mat_to_Mat(objPoints).nativeObj, dictionary.nativeObj, ids.nativeObj));
    }

    public List<MatOfPoint3f> get_objPoints() {
        List<MatOfPoint3f> retVal = new ArrayList<>();
        Converters.Mat_to_vector_vector_Point3f(new Mat(get_objPoints_0(this.nativeObj)), retVal);
        return retVal;
    }

    public Dictionary get_dictionary() {
        return new Dictionary(get_dictionary_0(this.nativeObj));
    }

    public MatOfInt get_ids() {
        return MatOfInt.fromNativeAddr(get_ids_0(this.nativeObj));
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
