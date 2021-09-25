package android.backport.webp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.ByteArrayOutputStream;

public final class WebPFactory {
    private static boolean loadSoLibError;

    private static native Bitmap nativeDecodeByteArray(byte[] bArr, BitmapFactory.Options options);

    private static native Bitmap nativeDecodeFile(String str, BitmapFactory.Options options);

    private static native byte[] nativeEncodeBitmap(Bitmap bitmap, int i);

    static {
        loadSoLibError = false;
        try {
            System.loadLibrary("webpbackport");
        } catch (Throwable th) {
            loadSoLibError = true;
        }
    }

    private WebPFactory() {
    }

    public static boolean available() {
        return !loadSoLibError;
    }

    public static Bitmap decodeByteArray(byte[] data, BitmapFactory.Options options) {
        if (available()) {
            return nativeDecodeByteArray(data, options);
        }
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }

    public static Bitmap decodeFile(String path, BitmapFactory.Options options) {
        if (available()) {
            return nativeDecodeFile(path, options);
        }
        return BitmapFactory.decodeFile(path, options);
    }

    public static byte[] encodeBitmap(Bitmap bitmap, int quality) {
        if (available()) {
            return nativeEncodeBitmap(bitmap, quality);
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.WEBP, quality, out);
        return out.toByteArray();
    }
}
