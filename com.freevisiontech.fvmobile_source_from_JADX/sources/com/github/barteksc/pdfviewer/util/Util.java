package com.github.barteksc.pdfviewer.util;

import android.content.Context;
import android.util.TypedValue;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Util {
    private static final int DEFAULT_BUFFER_SIZE = 4096;

    public static int getDP(Context context, int dp) {
        return (int) TypedValue.applyDimension(1, (float) dp, context.getResources().getDisplayMetrics());
    }

    public static byte[] toByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        while (true) {
            int n = inputStream.read(buffer);
            if (-1 == n) {
                return os.toByteArray();
            }
            os.write(buffer, 0, n);
        }
    }
}
