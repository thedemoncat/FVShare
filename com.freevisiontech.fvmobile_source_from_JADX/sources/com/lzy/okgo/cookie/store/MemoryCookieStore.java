package com.lzy.okgo.cookie.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.Cookie;
import okhttp3.HttpUrl;

public class MemoryCookieStore implements CookieStore {
    private final Map<String, List<Cookie>> memoryCookies = new HashMap();

    public synchronized void saveCookie(HttpUrl url, List<Cookie> cookies) {
        List<Cookie> oldCookies = this.memoryCookies.get(url.host());
        List<Cookie> needRemove = new ArrayList<>();
        for (Cookie newCookie : cookies) {
            for (Cookie oldCookie : oldCookies) {
                if (newCookie.name().equals(oldCookie.name())) {
                    needRemove.add(oldCookie);
                }
            }
        }
        oldCookies.removeAll(needRemove);
        oldCookies.addAll(cookies);
    }

    public synchronized void saveCookie(HttpUrl url, Cookie cookie) {
        List<Cookie> cookies = this.memoryCookies.get(url.host());
        List<Cookie> needRemove = new ArrayList<>();
        for (Cookie item : cookies) {
            if (cookie.name().equals(item.name())) {
                needRemove.add(item);
            }
        }
        cookies.removeAll(needRemove);
        cookies.add(cookie);
    }

    public synchronized List<Cookie> loadCookie(HttpUrl url) {
        List<Cookie> cookies;
        cookies = this.memoryCookies.get(url.host());
        if (cookies == null) {
            cookies = new ArrayList<>();
            this.memoryCookies.put(url.host(), cookies);
        }
        return cookies;
    }

    public synchronized List<Cookie> getAllCookie() {
        List<Cookie> cookies;
        cookies = new ArrayList<>();
        for (String url : this.memoryCookies.keySet()) {
            cookies.addAll(this.memoryCookies.get(url));
        }
        return cookies;
    }

    public List<Cookie> getCookie(HttpUrl url) {
        List<Cookie> cookies = new ArrayList<>();
        List<Cookie> urlCookies = this.memoryCookies.get(url.host());
        if (urlCookies != null) {
            cookies.addAll(urlCookies);
        }
        return cookies;
    }

    public synchronized boolean removeCookie(HttpUrl url, Cookie cookie) {
        return cookie != null && this.memoryCookies.get(url.host()).remove(cookie);
    }

    public synchronized boolean removeCookie(HttpUrl url) {
        return this.memoryCookies.remove(url.host()) != null;
    }

    public synchronized boolean removeAllCookie() {
        this.memoryCookies.clear();
        return true;
    }
}
