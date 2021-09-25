package p008jp.p009co.cyberagent.android.gpuimage;

/* renamed from: jp.co.cyberagent.android.gpuimage.GPUImageNativeLibrary */
public class GPUImageNativeLibrary {
    public static native void YUVtoARBG(byte[] bArr, int i, int i2, int[] iArr);

    public static native void YUVtoRBGA(byte[] bArr, int i, int i2, int[] iArr);

    static {
        System.loadLibrary("gpuimage-library");
    }
}
