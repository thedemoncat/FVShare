package com.lzy.okgo.cookie.store;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.lzy.okgo.cookie.SerializableCookie;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import okhttp3.Cookie;
import okhttp3.HttpUrl;

public class SPCookieStore implements CookieStore {
    private static final String COOKIE_NAME_PREFIX = "cookie_";
    private static final String COOKIE_PREFS = "okgo_cookie";
    private final SharedPreferences cookiePrefs;
    private final Map<String, ConcurrentHashMap<String, Cookie>> cookies = new HashMap();

    public SPCookieStore(Context context) {
        Cookie decodedCookie;
        this.cookiePrefs = context.getSharedPreferences(COOKIE_PREFS, 0);
        for (Map.Entry<String, ?> entry : this.cookiePrefs.getAll().entrySet()) {
            if (entry.getValue() != null && !entry.getKey().startsWith(COOKIE_NAME_PREFIX)) {
                for (String name : TextUtils.split((String) entry.getValue(), ",")) {
                    String encodedCookie = this.cookiePrefs.getString(COOKIE_NAME_PREFIX + name, (String) null);
                    if (!(encodedCookie == null || (decodedCookie = SerializableCookie.decodeCookie(encodedCookie)) == null)) {
                        if (!this.cookies.containsKey(entry.getKey())) {
                            this.cookies.put(entry.getKey(), new ConcurrentHashMap());
                        }
                        this.cookies.get(entry.getKey()).put(name, decodedCookie);
                    }
                }
            }
        }
    }

    private String getCookieToken(Cookie cookie) {
        return cookie.name() + "@" + cookie.domain();
    }

    private static boolean isCookieExpired(Cookie cookie) {
        return cookie.expiresAt() < System.currentTimeMillis();
    }

    public synchronized void saveCookie(HttpUrl url, List<Cookie> urlCookies) {
        for (Cookie cookie : urlCookies) {
            saveCookie(url, cookie);
        }
    }

    public synchronized void saveCookie(HttpUrl url, Cookie cookie) {
        if (!this.cookies.containsKey(url.host())) {
            this.cookies.put(url.host(), new ConcurrentHashMap());
        }
        if (isCookieExpired(cookie)) {
            removeCookie(url, cookie);
        } else {
            saveCookie(url, cookie, getCookieToken(cookie));
        }
    }

    private void saveCookie(HttpUrl url, Cookie cookie, String cookieToken) {
        this.cookies.get(url.host()).put(cookieToken, cookie);
        SharedPreferences.Editor prefsWriter = this.cookiePrefs.edit();
        prefsWriter.putString(url.host(), TextUtils.join(",", this.cookies.get(url.host()).keySet()));
        prefsWriter.putString(COOKIE_NAME_PREFIX + cookieToken, SerializableCookie.encodeCookie(url.host(), cookie));
        prefsWriter.apply();
    }

    public synchronized List<Cookie> loadCookie(HttpUrl url) {
        List<Cookie> ret;
        ret = new ArrayList<>();
        if (this.cookies.containsKey(url.host())) {
            for (Cookie cookie : this.cookies.get(url.host()).values()) {
                if (isCookieExpired(cookie)) {
                    removeCookie(url, cookie);
                } else {
                    ret.add(cookie);
                }
            }
        }
        return ret;
    }

    public synchronized boolean removeCookie(HttpUrl url, Cookie cookie) {
        boolean z;
        if (!this.cookies.containsKey(url.host())) {
            z = false;
        } else {
            String cookieToken = getCookieToken(cookie);
            if (!this.cookies.get(url.host()).containsKey(cookieToken)) {
                z = false;
            } else {
                this.cookies.get(url.host()).remove(cookieToken);
                SharedPreferences.Editor prefsWriter = this.cookiePrefs.edit();
                if (this.cookiePrefs.contains(COOKIE_NAME_PREFIX + cookieToken)) {
                    prefsWriter.remove(COOKIE_NAME_PREFIX + cookieToken);
                }
                prefsWriter.putString(url.host(), TextUtils.join(",", this.cookies.get(url.host()).keySet()));
                prefsWriter.apply();
                z = true;
            }
        }
        return z;
    }

    public synchronized boolean removeCookie(HttpUrl url) {
        boolean z;
        if (!this.cookies.containsKey(url.host())) {
            z = false;
        } else {
            Set<String> cookieTokens = this.cookies.remove(url.host()).keySet();
            SharedPreferences.Editor prefsWriter = this.cookiePrefs.edit();
            for (String cookieToken : cookieTokens) {
                if (this.cookiePrefs.contains(COOKIE_NAME_PREFIX + cookieToken)) {
                    prefsWriter.remove(COOKIE_NAME_PREFIX + cookieToken);
                }
            }
            prefsWriter.remove(url.host());
            prefsWriter.apply();
            z = true;
        }
        return z;
    }

    public synchronized boolean removeAllCookie() {
        this.cookies.clear();
        SharedPreferences.Editor prefsWriter = this.cookiePrefs.edit();
        prefsWriter.clear();
        prefsWriter.apply();
        return true;
    }

    public synchronized List<Cookie> getAllCookie() {
        List<Cookie> ret;
        ret = new ArrayList<>();
        for (String key : this.cookies.keySet()) {
            ret.addAll(this.cookies.get(key).values());
        }
        return ret;
    }

    public synchronized List<Cookie> getCookie(HttpUrl url) {
        List<Cookie> ret;
        ret = new ArrayList<>();
        Map<String, Cookie> mapCookie = this.cookies.get(url.host());
        if (mapCookie != null) {
            ret.addAll(mapCookie.values());
        }
        return ret;
    }
}
