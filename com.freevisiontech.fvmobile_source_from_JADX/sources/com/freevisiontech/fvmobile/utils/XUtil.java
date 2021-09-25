package com.freevisiontech.fvmobile.utils;

import android.util.Log;
import java.util.Map;
import org.xutils.C2090x;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

public class XUtil {
    public static <T> Callback.Cancelable Get(String url, Map<String, String> map, Callback.CommonCallback<T> callback) {
        RequestParams params = new RequestParams(url);
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                params.addQueryStringParameter(entry.getKey(), entry.getValue());
            }
        }
        return C2090x.http().get(params, callback);
    }

    public static <T> Callback.Cancelable Get(String url, Callback.CommonCallback<T> callback) {
        return C2090x.http().get(new RequestParams(url), callback);
    }

    public static <T> Callback.Cancelable Post(String url, Map<String, Object> map, Callback.CommonCallback<T> callback) {
        RequestParams params = new RequestParams(url);
        if (map != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                params.addParameter(entry.getKey(), entry.getValue());
            }
        }
        Callback.Cancelable cancelable = C2090x.http().post(params, callback);
        Log.e("=======url===", "=======url====" + params);
        return cancelable;
    }

    public static <T> Callback.Cancelable Post(String url, Callback.CommonCallback<T> callback) {
        return C2090x.http().post(new RequestParams(url), callback);
    }

    public static <T> Callback.Cancelable DownLoadFile(String url, String filepath, Callback.CommonCallback<T> callback) {
        RequestParams params = new RequestParams(url);
        params.setAutoResume(true);
        params.setSaveFilePath(filepath);
        return C2090x.http().get(params, callback);
    }
}
