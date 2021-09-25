package com.freevisiontech.fvmobile.utils;

import android.content.Context;
import android.graphics.Bitmap;
import com.freevisiontech.fvmobile.utility.Util;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PhotoEditUtils {
    public static String saveBitmapToDrawable(Bitmap bmp) {
        long currentTimeMillis = System.currentTimeMillis();
        String paintPath = Util.saveBitmapToDrawablePath();
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(new File(paintPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        return paintPath;
    }

    public static String saveBitmapToDrawableCamera(Bitmap bmp, Context context) {
        long currentTimeMillis = System.currentTimeMillis();
        String paintPath = Util.getOutputPhotoFile(context);
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(new File(paintPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        return paintPath;
    }

    public static String saveBitmapToDrawableCameraCatch(Bitmap bmp, Context context) {
        long currentTimeMillis = System.currentTimeMillis();
        String paintPath = Util.getOutputPhotoFileCatch(context);
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(new File(paintPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        return paintPath;
    }

    public static String saveBitmapToDrawableMosaicCamera(Bitmap bmp, Context context) {
        String paintPath = Util.getOutputPhotoMosaicFile(context);
        try {
            FileOutputStream fOut = new FileOutputStream(new File(paintPath));
            if (bmp != null) {
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.flush();
                fOut.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return paintPath;
    }

    public static String saveBitmapToDrawableMosaicCameraCatch(Bitmap bmp, Context context) {
        long currentTimeMillis = System.currentTimeMillis();
        String paintPath = Util.getOutputPhotoMosaicFileCatch(context);
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(new File(paintPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        return paintPath;
    }
}
