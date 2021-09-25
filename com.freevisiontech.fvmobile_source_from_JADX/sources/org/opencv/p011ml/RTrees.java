package org.opencv.p011ml;

import org.opencv.core.Mat;
import org.opencv.core.TermCriteria;

/* renamed from: org.opencv.ml.RTrees */
public class RTrees extends DTrees {
    private static native long create_0();

    private static native void delete(long j);

    private static native int getActiveVarCount_0(long j);

    private static native boolean getCalculateVarImportance_0(long j);

    private static native double[] getTermCriteria_0(long j);

    private static native long getVarImportance_0(long j);

    private static native void setActiveVarCount_0(long j, int i);

    private static native void setCalculateVarImportance_0(long j, boolean z);

    private static native void setTermCriteria_0(long j, int i, int i2, double d);

    protected RTrees(long addr) {
        super(addr);
    }

    public Mat getVarImportance() {
        return new Mat(getVarImportance_0(this.nativeObj));
    }

    public static RTrees create() {
        return new RTrees(create_0());
    }

    public TermCriteria getTermCriteria() {
        return new TermCriteria(getTermCriteria_0(this.nativeObj));
    }

    public boolean getCalculateVarImportance() {
        return getCalculateVarImportance_0(this.nativeObj);
    }

    public int getActiveVarCount() {
        return getActiveVarCount_0(this.nativeObj);
    }

    public void setActiveVarCount(int val) {
        setActiveVarCount_0(this.nativeObj, val);
    }

    public void setCalculateVarImportance(boolean val) {
        setCalculateVarImportance_0(this.nativeObj, val);
    }

    public void setTermCriteria(TermCriteria val) {
        setTermCriteria_0(this.nativeObj, val.type, val.maxCount, val.epsilon);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
