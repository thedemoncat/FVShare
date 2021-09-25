package com.freevisiontech.fvmobile.utils;

import android.content.Context;
import android.media.ExifInterface;
import com.freevisiontech.fvmobile.widget.view.IntentKey;
import com.lzy.okgo.model.Progress;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import p012tv.danmaku.ijk.media.player.IjkMediaMeta;

public class GsonModelHomeUtils {
    private static Context context;

    public static JSONObject shengChengJson(List patbList, Context mContext) {
        context = mContext;
        JSONObject object = null;
        List<String> list = new ArrayList<>();
        list.addAll(patbList);
        Collections.reverse(list);
        List<String> listTest = new ArrayList<>();
        listTest.clear();
        listTest.addAll(patbList);
        List listTestNew = new ArrayList();
        for (int w = 0; w < listTest.size(); w++) {
            String aaa = getDateTimeString2Int(listTest.get(w));
            if (aaa != null) {
                listTestNew.add(aaa);
            }
        }
        listTest.clear();
        listTest.addAll(getListPaiXuTwo(listTestNew));
        list.clear();
        for (int e = 0; e < listTest.size(); e++) {
            for (int w2 = 0; w2 < patbList.size(); w2++) {
                String aaa2 = getDateTimeString2Int(patbList.get(w2).toString());
                if (aaa2 != null && aaa2.equals(listTest.get(e).toString())) {
                    list.add(patbList.get(w2).toString());
                }
            }
        }
        Collections.reverse(list);
        List listNew = new ArrayList();
        for (int w3 = 0; w3 < list.size(); w3++) {
            String ddd = list.get(w3).toString();
            if (getDateTimeString(ddd) != null) {
                listNew.add(getDateTimeString(ddd));
            }
        }
        HashSet<String> hs = new HashSet<>(listNew);
        List listDate = new ArrayList();
        listDate.addAll(hs);
        List listDateSunXu = new ArrayList();
        listDateSunXu.addAll(getListPaiXu(listDate));
        try {
            JSONArray array = new JSONArray();
            JSONObject object2 = new JSONObject();
            int x = 0;
            while (x < listDateSunXu.size()) {
                try {
                    JSONObject object3 = new JSONObject();
                    JSONArray array3 = new JSONArray();
                    object3.put("time", listDateSunXu.get(x).toString());
                    for (int u = 0; u < list.size(); u++) {
                        if (getDateTimeString(list.get(u).toString()) != null && getDateTimeString(list.get(u).toString()).equals(listDateSunXu.get(x).toString())) {
                            JSONObject stoneObject = new JSONObject();
                            stoneObject.put("path", list.get(u));
                            stoneObject.put(IjkMediaMeta.IJKM_KEY_TYPE, "0");
                            array3.put(stoneObject);
                        }
                    }
                    object3.put("content", array3);
                    array.put(object3);
                    x++;
                } catch (JSONException e2) {
                    e = e2;
                    object = object2;
                    e.printStackTrace();
                    return object;
                }
            }
            object2.put(Progress.DATE, array);
            return object2;
        } catch (JSONException e3) {
            e = e3;
            e.printStackTrace();
            return object;
        }
    }

    private static String getDateTimeString(String s) {
        return s.replaceAll(":", "").replaceAll(" ", "").replaceAll("_", "").replaceAll("\\D", "").substring(0, 8);
    }

    private static String getDateTimeString2Int(String s) {
        String aaa = Pattern.compile("[^0-9]").matcher(s).replaceAll("").trim();
        if (aaa.length() > 14) {
            return aaa.substring(0, 15);
        }
        return null;
    }

    private static String getDateTimeStringTwo(String s) {
        String dateString = null;
        try {
            ExifInterface exifInterface = new ExifInterface(IntentKey.FILE_PATH + s);
            try {
                String tagDateTime = exifInterface.getAttribute("DateTime");
                if (tagDateTime != null) {
                    dateString = tagDateTime.replaceAll(":", "").replaceAll(" ", "").replaceAll("_", "").substring(0, 8);
                }
                ExifInterface exifInterface2 = exifInterface;
            } catch (IOException e) {
                e = e;
                ExifInterface exifInterface3 = exifInterface;
                e.printStackTrace();
                return dateString;
            }
        } catch (IOException e2) {
            e = e2;
            e.printStackTrace();
            return dateString;
        }
        return dateString;
    }

    public static List<String> getListPaiXu(List listDate) {
        int[] array = new int[listDate.size()];
        for (int e = 0; e < listDate.size(); e++) {
            array[e] = Integer.valueOf(listDate.get(e).toString()).intValue();
        }
        for (int i = 0; i < array.length; i++) {
            for (int j = i + 1; j < array.length; j++) {
                if (array[i] < array[j]) {
                    int temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                }
            }
        }
        List listDatePaiXu = new ArrayList();
        listDatePaiXu.clear();
        for (int valueOf : array) {
            listDatePaiXu.add(String.valueOf(valueOf));
        }
        return listDatePaiXu;
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

    public void showProgress(String msg) {
        LoadingView mProgressDialog = new LoadingView(context);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }

    public void hideProgress() {
    }
}
