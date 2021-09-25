package com.freevisiontech.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.ByteArrayOutputStream;

public class FVImageUtil {
    public static byte[] Bitmap2Bytes(Bitmap bm, Bitmap.CompressFormat format, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(format, quality, baos);
        return baos.toByteArray();
    }

    public static byte[] Bitmap2JPegBytes(Bitmap bm) {
        return Bitmap2Bytes(bm, Bitmap.CompressFormat.JPEG, 100);
    }

    public static Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        }
        return null;
    }
}
