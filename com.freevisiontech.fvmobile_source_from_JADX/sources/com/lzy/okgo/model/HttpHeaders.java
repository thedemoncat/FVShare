package com.lzy.okgo.model;

import android.os.Build;
import android.text.TextUtils;
import com.google.android.vending.expansion.downloader.Constants;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.utils.OkLogger;
import com.p007ny.ijk.upplayer.BuildConfig;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import org.json.JSONException;
import org.json.JSONObject;

public class HttpHeaders implements Serializable {
    public static final String FORMAT_HTTP_DATA = "EEE, dd MMM y HH:mm:ss 'GMT'";
    public static final TimeZone GMT_TIME_ZONE = TimeZone.getTimeZone("GMT");
    public static final String HEAD_KEY_ACCEPT = "Accept";
    public static final String HEAD_KEY_ACCEPT_ENCODING = "Accept-Encoding";
    public static final String HEAD_KEY_ACCEPT_LANGUAGE = "Accept-Language";
    public static final String HEAD_KEY_CACHE_CONTROL = "Cache-Control";
    public static final String HEAD_KEY_CONNECTION = "Connection";
    public static final String HEAD_KEY_CONTENT_DISPOSITION = "Content-Disposition";
    public static final String HEAD_KEY_CONTENT_ENCODING = "Content-Encoding";
    public static final String HEAD_KEY_CONTENT_LENGTH = "Content-Length";
    public static final String HEAD_KEY_CONTENT_RANGE = "Content-Range";
    public static final String HEAD_KEY_CONTENT_TYPE = "Content-Type";
    public static final String HEAD_KEY_COOKIE = "Cookie";
    public static final String HEAD_KEY_COOKIE2 = "Cookie2";
    public static final String HEAD_KEY_DATE = "Date";
    public static final String HEAD_KEY_EXPIRES = "Expires";
    public static final String HEAD_KEY_E_TAG = "ETag";
    public static final String HEAD_KEY_IF_MODIFIED_SINCE = "If-Modified-Since";
    public static final String HEAD_KEY_IF_NONE_MATCH = "If-None-Match";
    public static final String HEAD_KEY_LAST_MODIFIED = "Last-Modified";
    public static final String HEAD_KEY_LOCATION = "Location";
    public static final String HEAD_KEY_PRAGMA = "Pragma";
    public static final String HEAD_KEY_RANGE = "Range";
    public static final String HEAD_KEY_RESPONSE_CODE = "ResponseCode";
    public static final String HEAD_KEY_RESPONSE_MESSAGE = "ResponseMessage";
    public static final String HEAD_KEY_SET_COOKIE = "Set-Cookie";
    public static final String HEAD_KEY_SET_COOKIE2 = "Set-Cookie2";
    public static final String HEAD_KEY_USER_AGENT = "User-Agent";
    public static final String HEAD_VALUE_ACCEPT_ENCODING = "gzip, deflate";
    public static final String HEAD_VALUE_CONNECTION_CLOSE = "close";
    public static final String HEAD_VALUE_CONNECTION_KEEP_ALIVE = "keep-alive";
    private static String acceptLanguage = null;
    private static final long serialVersionUID = 8458647755751403873L;
    private static String userAgent;
    public LinkedHashMap<String, String> headersMap;

    private void init() {
        this.headersMap = new LinkedHashMap<>();
    }

    public HttpHeaders() {
        init();
    }

    public HttpHeaders(String key, String value) {
        init();
        put(key, value);
    }

    public void put(String key, String value) {
        if (key != null && value != null) {
            this.headersMap.put(key, value);
        }
    }

    public void put(HttpHeaders headers) {
        if (headers != null && headers.headersMap != null && !headers.headersMap.isEmpty()) {
            this.headersMap.putAll(headers.headersMap);
        }
    }

    public String get(String key) {
        return this.headersMap.get(key);
    }

    public String remove(String key) {
        return (String) this.headersMap.remove(key);
    }

    public void clear() {
        this.headersMap.clear();
    }

    public Set<String> getNames() {
        return this.headersMap.keySet();
    }

    public final String toJSONString() {
        JSONObject jsonObject = new JSONObject();
        try {
            for (Map.Entry<String, String> entry : this.headersMap.entrySet()) {
                jsonObject.put(entry.getKey(), entry.getValue());
            }
        } catch (JSONException e) {
            OkLogger.printStackTrace(e);
        }
        return jsonObject.toString();
    }

    public static long getDate(String gmtTime) {
        try {
            return parseGMTToMillis(gmtTime);
        } catch (ParseException e) {
            return 0;
        }
    }

    public static String getDate(long milliseconds) {
        return formatMillisToGMT(milliseconds);
    }

    public static long getExpiration(String expiresTime) {
        try {
            return parseGMTToMillis(expiresTime);
        } catch (ParseException e) {
            return -1;
        }
    }

    public static long getLastModified(String lastModified) {
        try {
            return parseGMTToMillis(lastModified);
        } catch (ParseException e) {
            return 0;
        }
    }

    public static String getCacheControl(String cacheControl, String pragma) {
        if (cacheControl != null) {
            return cacheControl;
        }
        if (pragma != null) {
            return pragma;
        }
        return null;
    }

    public static void setAcceptLanguage(String language) {
        acceptLanguage = language;
    }

    public static String getAcceptLanguage() {
        if (!TextUtils.isEmpty(acceptLanguage)) {
            return acceptLanguage;
        }
        Locale locale = Locale.getDefault();
        String language = locale.getLanguage();
        String country = locale.getCountry();
        StringBuilder acceptLanguageBuilder = new StringBuilder(language);
        if (!TextUtils.isEmpty(country)) {
            acceptLanguageBuilder.append('-').append(country).append(',').append(language).append(";q=0.8");
        }
        acceptLanguage = acceptLanguageBuilder.toString();
        return acceptLanguage;
    }

    public static void setUserAgent(String agent) {
        userAgent = agent;
    }

    public static String getUserAgent() {
        if (!TextUtils.isEmpty(userAgent)) {
            return userAgent;
        }
        String webUserAgent = null;
        try {
            webUserAgent = OkGo.getInstance().getContext().getString(((Integer) Class.forName("com.android.internal.R$string").getDeclaredField("web_user_agent").get((Object) null)).intValue());
        } catch (Exception e) {
        }
        if (TextUtils.isEmpty(webUserAgent)) {
            webUserAgent = "okhttp-okgo/jeasonlzy";
        }
        Locale locale = Locale.getDefault();
        StringBuffer buffer = new StringBuffer();
        String version = Build.VERSION.RELEASE;
        if (version.length() > 0) {
            buffer.append(version);
        } else {
            buffer.append(BuildConfig.VERSION_NAME);
        }
        buffer.append("; ");
        String language = locale.getLanguage();
        if (language != null) {
            buffer.append(language.toLowerCase(locale));
            String country = locale.getCountry();
            if (!TextUtils.isEmpty(country)) {
                buffer.append(Constants.FILENAME_SEQUENCE_SEPARATOR);
                buffer.append(country.toLowerCase(locale));
            }
        } else {
            buffer.append("en");
        }
        if ("REL".equals(Build.VERSION.CODENAME)) {
            String model = Build.MODEL;
            if (model.length() > 0) {
                buffer.append("; ");
                buffer.append(model);
            }
        }
        String id = Build.ID;
        if (id.length() > 0) {
            buffer.append(" Build/");
            buffer.append(id);
        }
        userAgent = String.format(webUserAgent, new Object[]{buffer, "Mobile "});
        return userAgent;
    }

    public static long parseGMTToMillis(String gmtTime) throws ParseException {
        if (TextUtils.isEmpty(gmtTime)) {
            return 0;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_HTTP_DATA, Locale.US);
        formatter.setTimeZone(GMT_TIME_ZONE);
        return formatter.parse(gmtTime).getTime();
    }

    public static String formatMillisToGMT(long milliseconds) {
        Date date = new Date(milliseconds);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_HTTP_DATA, Locale.US);
        simpleDateFormat.setTimeZone(GMT_TIME_ZONE);
        return simpleDateFormat.format(date);
    }

    public String toString() {
        return "HttpHeaders{headersMap=" + this.headersMap + '}';
    }
}
