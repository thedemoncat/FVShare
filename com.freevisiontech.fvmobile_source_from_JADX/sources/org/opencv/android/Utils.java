package org.opencv.android;

import android.content.Context;
import android.graphics.Bitmap;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class Utils {
    private static native void nBitmapToMat2(Bitmap bitmap, long j, boolean z);

    private static native void nMatToBitmap2(long j, Bitmap bitmap, boolean z);

    public static String exportResource(Context context, int resourceId) {
        return exportResource(context, resourceId, "OpenCV_data");
    }

    public static String exportResource(Context context, int resourceId, String dirname) {
        String fullname = context.getResources().getString(resourceId);
        String resName = fullname.substring(fullname.lastIndexOf("/") + 1);
        try {
            InputStream is = context.getResources().openRawResource(resourceId);
            File resFile = new File(context.getDir(dirname, 0), resName);
            FileOutputStream os = new FileOutputStream(resFile);
            byte[] buffer = new byte[4096];
            while (true) {
                int bytesRead = is.read(buffer);
                if (bytesRead != -1) {
                    os.write(buffer, 0, bytesRead);
                } else {
                    is.close();
                    os.close();
                    return resFile.getAbsolutePath();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new CvException("Failed to export resource " + resName + ". Exception thrown: " + e);
        }
    }

    public static Mat loadResource(Context context, int resourceId) throws IOException {
        return loadResource(context, resourceId, -1);
    }

    public static Mat loadResource(Context context, int resourceId, int flags) throws IOException {
        InputStream is = context.getResources().openRawResource(resourceId);
        ByteArrayOutputStream os = new ByteArrayOutputStream(is.available());
        byte[] buffer = new byte[4096];
        while (true) {
            int bytesRead = is.read(buffer);
            if (bytesRead != -1) {
                os.write(buffer, 0, bytesRead);
            } else {
                is.close();
                Mat encoded = new Mat(1, os.size(), 0);
                encoded.put(0, 0, os.toByteArray());
                os.close();
                Mat decoded = Imgcodecs.imdecode(encoded, flags);
                encoded.release();
                return decoded;
            }
        }
    }

    public static void bitmapToMat(Bitmap bmp, Mat mat, boolean unPremultiplyAlpha) {
        if (bmp == null) {
            throw new IllegalArgumentException("bmp == null");
        } else if (mat == null) {
            throw new IllegalArgumentException("mat == null");
        } else {
            nBitmapToMat2(bmp, mat.nativeObj, unPremultiplyAlpha);
        }
    }

    public static void bitmapToMat(Bitmap bmp, Mat mat) {
        bitmapToMat(bmp, mat, false);
    }

    public static void matToBitmap(Mat mat, Bitmap bmp, boolean premultiplyAlpha) {
        if (mat == null) {
            throw new IllegalArgumentException("mat == null");
        } else if (bmp == null) {
            throw new IllegalArgumentException("bmp == null");
        } else {
            nMatToBitmap2(mat.nativeObj, bmp, premultiplyAlpha);
        }
    }

    public static void matToBitmap(Mat mat, Bitmap bmp) {
        matToBitmap(mat, bmp, false);
    }
}
