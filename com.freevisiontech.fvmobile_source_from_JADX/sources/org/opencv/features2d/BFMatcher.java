package org.opencv.features2d;

public class BFMatcher extends DescriptorMatcher {
    private static native long BFMatcher_0(int i, boolean z);

    private static native long BFMatcher_1();

    private static native long create_0(int i, boolean z);

    private static native long create_1();

    private static native void delete(long j);

    protected BFMatcher(long addr) {
        super(addr);
    }

    public BFMatcher(int normType, boolean crossCheck) {
        super(BFMatcher_0(normType, crossCheck));
    }

    public BFMatcher() {
        super(BFMatcher_1());
    }

    public static BFMatcher create(int normType, boolean crossCheck) {
        return new BFMatcher(create_0(normType, crossCheck));
    }

    public static BFMatcher create() {
        return new BFMatcher(create_1());
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
