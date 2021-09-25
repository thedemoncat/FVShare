package org.opencv.features2d;

public class FlannBasedMatcher extends DescriptorMatcher {
    private static native long FlannBasedMatcher_0();

    private static native long create_0();

    private static native void delete(long j);

    protected FlannBasedMatcher(long addr) {
        super(addr);
    }

    public FlannBasedMatcher() {
        super(FlannBasedMatcher_0());
    }

    public static FlannBasedMatcher create() {
        return new FlannBasedMatcher(create_0());
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
