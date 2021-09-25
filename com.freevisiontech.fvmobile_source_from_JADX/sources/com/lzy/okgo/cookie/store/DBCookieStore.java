package com.lzy.okgo.cookie.store;

import android.content.Context;
import com.lzy.okgo.cookie.SerializableCookie;
import com.lzy.okgo.p000db.CookieManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import okhttp3.Cookie;
import okhttp3.HttpUrl;

public class DBCookieStore implements CookieStore {
    private final Map<String, ConcurrentHashMap<String, Cookie>> cookies = new HashMap();

    public DBCookieStore(Context context) {
        CookieManager.init(context);
        for (SerializableCookie serializableCookie : CookieManager.getInstance().queryAll()) {
            if (!this.cookies.containsKey(serializableCookie.host)) {
                this.cookies.put(serializableCookie.host, new ConcurrentHashMap());
            }
            Cookie cookie = serializableCookie.getCookie();
            this.cookies.get(serializableCookie.host).put(getCookieToken(cookie), cookie);
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
            this.cookies.get(url.host()).put(getCookieToken(cookie), cookie);
            CookieManager.getInstance().replace(new SerializableCookie(url.host(), cookie));
        }
    }

    public synchronized List<Cookie> loadCookie(HttpUrl url) {
        List<Cookie> ret;
        ret = new ArrayList<>();
        if (this.cookies.containsKey(url.host())) {
            for (SerializableCookie serializableCookie : CookieManager.getInstance().query("host=?", new String[]{url.host()})) {
                Cookie cookie = serializableCookie.getCookie();
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
                CookieManager.getInstance().delete("host=? and name=? and domain=?", new String[]{url.host(), cookie.name(), cookie.domain()});
                z = true;
            }
        }
        return z;
    }

    public synchronized boolean removeCookie(HttpUrl url) {
        boolean z = false;
        synchronized (this) {
            if (this.cookies.containsKey(url.host())) {
                this.cookies.remove(url.host());
                CookieManager.getInstance().delete("host=?", new String[]{url.host()});
                z = true;
            }
        }
        return z;
    }

    public synchronized boolean removeAllCookie() {
        this.cookies.clear();
        CookieManager.getInstance().deleteAll();
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
