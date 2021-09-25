package com.freevisiontech.fvmobile.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import com.freevisiontech.fvmobile.utility.Util;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CameraPaths {
    private static String aaa = null;

    public static List<String> getImagePathFromSD(Context context) {
        List<String> imagePathList = new ArrayList<>();
        File[] files = new File(Util.getParentPath(context)).listFiles();
        if (files != null) {
            for (File file : files) {
                if (checkIsImageFile(file.getPath())) {
                    String path = file.getPath();
                    imagePathList.add(path.substring(path.indexOf("/fvmobile") + 10, path.length()));
                }
            }
        }
        return imagePathList;
    }

    @SuppressLint({"DefaultLocale"})
    private static boolean checkIsImageFile(String fName) {
        String FileEnd = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();
        if (FileEnd.equals("jpg") || FileEnd.equals("png") || FileEnd.equals("gif") || FileEnd.equals("jpeg") || FileEnd.equals("bmp")) {
            return true;
        }
        return false;
    }

    public static List<String> getVideoPathFromSD(Context context) {
        List<String> imagePathList = new ArrayList<>();
        File[] files = new File(Util.getParentPath(context)).listFiles();
        if (files != null) {
            for (File file : files) {
                if (checkIsVideoFile(file.getPath())) {
                    String path = file.getPath();
                    imagePathList.add(path.substring(path.indexOf("/fvmobile") + 10, path.length()));
                }
            }
        }
        return imagePathList;
    }

    @SuppressLint({"DefaultLocale"})
    public static boolean checkIsVideoFile(String fName) {
        if (fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase().equals("mp4")) {
            return true;
        }
        return false;
    }

    public static void CameraFiratPath(String filepath) {
        aaa = filepath;
    }

    public static String getCurrentPath() {
        return aaa;
    }
}
