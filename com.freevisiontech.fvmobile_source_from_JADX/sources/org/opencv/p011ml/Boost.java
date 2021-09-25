package org.opencv.p011ml;

/* renamed from: org.opencv.ml.Boost */
public class Boost extends DTrees {
    public static final int DISCRETE = 0;
    public static final int GENTLE = 3;
    public static final int LOGIT = 2;
    public static final int REAL = 1;

    private static native long create_0();

    private static native void delete(long j);

    private static native int getBoostType_0(long j);

    private static native int getWeakCount_0(long j);

    private static native double getWeightTrimRate_0(long j);

    private static native void setBoostType_0(long j, int i);

    private static native void setWeakCount_0(long j, int i);

    private static native void setWeightTrimRate_0(long j, double d);

    protected Boost(long addr) {
        super(addr);
    }

    public static Boost create() {
        return new Boost(create_0());
    }

    public double getWeightTrimRate() {
        return getWeightTrimRate_0(this.nativeObj);
    }

    public int getBoostType() {
        return getBoostType_0(this.nativeObj);
    }

    public int getWeakCount() {
        return getWeakCount_0(this.nativeObj);
    }

    public void setBoostType(int val) {
        setBoostType_0(this.nativeObj, val);
    }

    public void setWeakCount(int val) {
        setWeakCount_0(this.nativeObj, val);
    }

    public void setWeightTrimRate(double val) {
        setWeightTrimRate_0(this.nativeObj, val);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
