package com.freevisiontech.fvmobile.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import com.freevisiontech.fvmobile.utility.Util;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class CameraFirstPhotoPaths {
    private static String aaa = null;

    public static List<String> getImagePathFromSD(Context context) {
        String aaa2;
        List<String> list = new ArrayList<>();
        File[] files = new File(Util.getParentPath(context)).listFiles();
        if (files != null) {
            for (File file : files) {
                if (checkIsImageFile(file.getPath())) {
                    String path = file.getPath();
                    list.add(path.substring(path.indexOf("fvmobile") + 9, path.length()));
                }
            }
        }
        Collections.reverse(list);
        List<String> listTest = new ArrayList<>();
        listTest.clear();
        listTest.addAll(list);
        List listTestNew = new ArrayList();
        for (int w = 0; w < listTest.size(); w++) {
            if (listTest.get(w).toString().length() > 19 && (aaa2 = getDateTimeString2Int(listTest.get(w))) != null) {
                listTestNew.add(aaa2);
            }
        }
        listTest.clear();
        listTest.addAll(getListPaiXuTwo(listTestNew));
        Collections.reverse(listTest);
        return listTest;
    }

    public static List getListPaiXuTwo(List listDate) {
        long[] arr = new long[listDate.size()];
        for (int e = 0; e < listDate.size(); e++) {
            arr[e] = Long.valueOf(listDate.get(e).toString()).longValue();
        }
        for (int x = 0; x < arr.length - 1; x++) {
            for (int y = 0; y < (arr.length - x) - 1; y++) {
                if (arr[y] > arr[y + 1]) {
                    long temp = arr[y];
                    arr[y] = arr[y + 1];
                    arr[y + 1] = temp;
                }
            }
        }
        List listDatePaiXu = new ArrayList();
        listDatePaiXu.clear();
        for (long valueOf : arr) {
            listDatePaiXu.add(String.valueOf(valueOf));
        }
        return listDatePaiXu;
    }

    private static String getDateTimeString2Int(String s) {
        String aaa2 = Pattern.compile("[^0-9]").matcher(s).replaceAll("").trim();
        if (aaa2.length() > 16) {
            return aaa2.substring(0, 17);
        }
        return null;
    }

    @SuppressLint({"DefaultLocale"})
    private static boolean checkIsImageFile(String fName) {
        String FileEnd = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();
        if (FileEnd.equals("jpg") || FileEnd.equals("png") || FileEnd.equals("gif") || FileEnd.equals("jpeg") || FileEnd.equals("bmp") || FileEnd.equals("mp4")) {
            return true;
        }
        return false;
    }

    public static List<String> getVideoPathFromSD() {
        List<String> imagePathList = new ArrayList<>();
        File[] files = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "DCIM/fvmobile").listFiles();
        if (files != null) {
            for (File file : files) {
                if (checkIsVideoFile(file.getPath())) {
                    String path = file.getPath();
                    imagePathList.add(path.substring(path.indexOf("fvmobile") + 9, path.length()));
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
