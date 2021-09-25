package org.opencv.android;

import android.util.Log;
import org.opencv.xstitching.XStitcher;

public class LoadOpenCV {
    public static boolean LoadOpenCVSccuss = false;
    private static String TAG = "LoadOpenCV";
    public static XStitcher xStitcher = null;

    public static boolean LoadOpenCVLib() {
        if (!OpenCVLoader.initDebug()) {
            Log.e(TAG, "OpenCV library not found!");
            LoadOpenCVSccuss = false;
            return false;
        }
        if (xStitcher == null) {
            xStitcher = XStitcher.getInstance(false, false);
        }
        LoadOpenCVSccuss = true;
        return true;
    }
}
