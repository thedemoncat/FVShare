package com.lzy.okgo.utils;

import android.text.TextUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtils {
    public static String createUrlFromParams(String url, Map<String, List<String>> params) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(url);
            if (url.indexOf(38) > 0 || url.indexOf(63) > 0) {
                sb.append("&");
            } else {
                sb.append("?");
            }
            for (Map.Entry<String, List<String>> urlParams : params.entrySet()) {
                for (String value : urlParams.getValue()) {
                    sb.append(urlParams.getKey()).append("=").append(URLEncoder.encode(value, "UTF-8")).append("&");
                }
            }
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            OkLogger.printStackTrace(e);
            return url;
        }
    }

    public static Request.Builder appendHeaders(HttpHeaders headers) {
        Request.Builder requestBuilder = new Request.Builder();
        if (!headers.headersMap.isEmpty()) {
            Headers.Builder headerBuilder = new Headers.Builder();
            try {
                for (Map.Entry<String, String> entry : headers.headersMap.entrySet()) {
                    headerBuilder.add(entry.getKey(), entry.getValue());
                }
            } catch (Exception e) {
                OkLogger.printStackTrace(e);
            }
            requestBuilder.headers(headerBuilder.build());
        }
        return requestBuilder;
    }

    public static RequestBody generateMultipartRequestBody(HttpParams params, boolean isMultipart) {
        if (!params.fileParamsMap.isEmpty() || isMultipart) {
            MultipartBody.Builder multipartBodybuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            if (!params.urlParamsMap.isEmpty()) {
                for (Map.Entry<String, List<String>> entry : params.urlParamsMap.entrySet()) {
                    for (String value : entry.getValue()) {
                        multipartBodybuilder.addFormDataPart(entry.getKey(), value);
                    }
                }
            }
            for (Map.Entry<String, List<HttpParams.FileWrapper>> entry2 : params.fileParamsMap.entrySet()) {
                for (HttpParams.FileWrapper fileWrapper : entry2.getValue()) {
                    multipartBodybuilder.addFormDataPart(entry2.getKey(), fileWrapper.fileName, RequestBody.create(fileWrapper.contentType, fileWrapper.file));
                }
            }
            return multipartBodybuilder.build();
        }
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        for (String key : params.urlParamsMap.keySet()) {
            for (String value2 : params.urlParamsMap.get(key)) {
                bodyBuilder.add(key, value2);
            }
        }
        return bodyBuilder.build();
    }

    public static String getNetFileName(Response response, String url) {
        String fileName = getHeaderFileName(response);
        if (TextUtils.isEmpty(fileName)) {
            fileName = getUrlFileName(url);
        }
        if (TextUtils.isEmpty(fileName)) {
            return "unknownfile_" + System.currentTimeMillis();
        }
        return fileName;
    }

    private static String getHeaderFileName(Response response) {
        String dispositionHeader = response.header(HttpHeaders.HEAD_KEY_CONTENT_DISPOSITION);
        if (dispositionHeader != null) {
            String dispositionHeader2 = dispositionHeader.replaceAll("\"", "");
            int indexOf = dispositionHeader2.indexOf("filename=");
            if (indexOf != -1) {
                return dispositionHeader2.substring("filename=".length() + indexOf, dispositionHeader2.length());
            }
            int indexOf2 = dispositionHeader2.indexOf("filename*=");
            if (indexOf2 != -1) {
                String fileName = dispositionHeader2.substring("filename*=".length() + indexOf2, dispositionHeader2.length());
                if (fileName.startsWith("UTF-8''")) {
                    return fileName.substring("UTF-8''".length(), fileName.length());
                }
                return fileName;
            }
        }
        return null;
    }

    private static String getUrlFileName(String url) {
        int endIndex;
        String filename = null;
        String[] strings = url.split("/");
        for (String string : strings) {
            if (string.contains("?") && (endIndex = string.indexOf("?")) != -1) {
                return string.substring(0, endIndex);
            }
        }
        if (strings.length > 0) {
            filename = strings[strings.length - 1];
        }
        return filename;
    }

    public static boolean deleteFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return true;
        }
        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        if (!file.isFile()) {
            return false;
        }
        boolean delete = file.delete();
        OkLogger.m1450e("deleteFile:" + delete + " path:" + path);
        return delete;
    }

    public static MediaType guessMimeType(String fileName) {
        String contentType = URLConnection.getFileNameMap().getContentTypeFor(fileName.replace("#", ""));
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return MediaType.parse(contentType);
    }

    public static <T> T checkNotNull(T object, String message) {
        if (object != null) {
            return object;
        }
        throw new NullPointerException(message);
    }

    public static void runOnUiThread(Runnable runnable) {
        OkGo.getInstance().getDelivery().post(runnable);
    }
}
