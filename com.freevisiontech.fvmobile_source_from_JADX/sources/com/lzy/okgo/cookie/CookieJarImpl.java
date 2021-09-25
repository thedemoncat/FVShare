package com.lzy.okgo.cookie;

import com.lzy.okgo.cookie.store.CookieStore;
import java.util.List;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class CookieJarImpl implements CookieJar {
    private CookieStore cookieStore;

    public CookieJarImpl(CookieStore cookieStore2) {
        if (cookieStore2 == null) {
            throw new IllegalArgumentException("cookieStore can not be null!");
        }
        this.cookieStore = cookieStore2;
    }

    public synchronized void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        this.cookieStore.saveCookie(url, cookies);
    }

    public synchronized List<Cookie> loadForRequest(HttpUrl url) {
        return this.cookieStore.loadCookie(url);
    }

    public CookieStore getCookieStore() {
        return this.cookieStore;
    }
}
