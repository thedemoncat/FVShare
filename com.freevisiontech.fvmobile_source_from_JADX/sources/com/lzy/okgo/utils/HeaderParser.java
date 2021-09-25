package com.lzy.okgo.utils;

import android.text.TextUtils;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.request.Request;
import java.util.Locale;
import java.util.StringTokenizer;
import okhttp3.Headers;

public class HeaderParser {
    public static <T> CacheEntity<T> createCacheEntity(Headers responseHeaders, T data, CacheMode cacheMode, String cacheKey) {
        long localExpire = 0;
        if (cacheMode == CacheMode.DEFAULT) {
            long date = HttpHeaders.getDate(responseHeaders.get(HttpHeaders.HEAD_KEY_DATE));
            long expires = HttpHeaders.getExpiration(responseHeaders.get(HttpHeaders.HEAD_KEY_EXPIRES));
            String cacheControl = HttpHeaders.getCacheControl(responseHeaders.get(HttpHeaders.HEAD_KEY_CACHE_CONTROL), responseHeaders.get(HttpHeaders.HEAD_KEY_PRAGMA));
            if (TextUtils.isEmpty(cacheControl) && expires <= 0) {
                return null;
            }
            long maxAge = 0;
            if (!TextUtils.isEmpty(cacheControl)) {
                StringTokenizer stringTokenizer = new StringTokenizer(cacheControl, ",");
                while (stringTokenizer.hasMoreTokens()) {
                    String token = stringTokenizer.nextToken().trim().toLowerCase(Locale.getDefault());
                    if (token.equals("no-cache") || token.equals("no-store")) {
                        return null;
                    }
                    if (token.startsWith("max-age=")) {
                        try {
                            maxAge = Long.parseLong(token.substring(8));
                            if (maxAge <= 0) {
                                return null;
                            }
                        } catch (Exception e) {
                            OkLogger.printStackTrace(e);
                        }
                    }
                }
            }
            long now = System.currentTimeMillis();
            if (date > 0) {
                now = date;
            }
            if (maxAge > 0) {
                localExpire = now + (1000 * maxAge);
            } else if (expires >= 0) {
                localExpire = expires;
            }
        } else {
            localExpire = System.currentTimeMillis();
        }
        HttpHeaders headers = new HttpHeaders();
        for (String headerName : responseHeaders.names()) {
            headers.put(headerName, responseHeaders.get(headerName));
        }
        CacheEntity<T> cacheEntity = new CacheEntity<>();
        cacheEntity.setKey(cacheKey);
        cacheEntity.setData(data);
        cacheEntity.setLocalExpire(localExpire);
        cacheEntity.setResponseHeaders(headers);
        return cacheEntity;
    }

    public static <T> void addCacheHeaders(Request request, CacheEntity<T> cacheEntity, CacheMode cacheMode) {
        HttpHeaders responseHeaders;
        if (cacheEntity != null && cacheMode == CacheMode.DEFAULT && (responseHeaders = cacheEntity.getResponseHeaders()) != null) {
            String eTag = responseHeaders.get(HttpHeaders.HEAD_KEY_E_TAG);
            if (eTag != null) {
                request.headers(HttpHeaders.HEAD_KEY_IF_NONE_MATCH, eTag);
            }
            long lastModified = HttpHeaders.getLastModified(responseHeaders.get(HttpHeaders.HEAD_KEY_LAST_MODIFIED));
            if (lastModified > 0) {
                request.headers(HttpHeaders.HEAD_KEY_IF_MODIFIED_SINCE, HttpHeaders.formatMillisToGMT(lastModified));
            }
        }
    }
}
