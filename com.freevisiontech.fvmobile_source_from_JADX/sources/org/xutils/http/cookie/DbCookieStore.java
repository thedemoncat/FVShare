package org.xutils.http.cookie;

import android.text.TextUtils;
import com.lzy.okgo.cookie.SerializableCookie;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import org.xutils.DbManager;
import org.xutils.common.util.LogUtil;
import org.xutils.p018db.Selector;
import org.xutils.p018db.sqlite.WhereBuilder;
import org.xutils.p018db.table.DbModel;

public enum DbCookieStore implements CookieStore {
    INSTANCE;
    
    private static final int LIMIT_COUNT = 5000;
    private static final long TRIM_TIME_SPAN = 1000;
    /* access modifiers changed from: private */

    /* renamed from: db */
    public final DbManager f1215db;
    /* access modifiers changed from: private */
    public long lastTrimTime;
    private final Executor trimExecutor;

    public void add(URI uri, HttpCookie cookie) {
        if (cookie != null) {
            try {
                this.f1215db.replace(new CookieEntity(getEffectiveURI(uri), cookie));
            } catch (Throwable ex) {
                LogUtil.m1565e(ex.getMessage(), ex);
            }
            trimSize();
        }
    }

    public List<HttpCookie> get(URI uri) {
        if (uri == null) {
            throw new NullPointerException("uri is null");
        }
        URI uri2 = getEffectiveURI(uri);
        List<HttpCookie> rt = new ArrayList<>();
        try {
            Selector<CookieEntity> selector = this.f1215db.selector(CookieEntity.class);
            WhereBuilder where = WhereBuilder.m1577b();
            String host = uri2.getHost();
            if (!TextUtils.isEmpty(host)) {
                WhereBuilder subWhere = WhereBuilder.m1578b(SerializableCookie.DOMAIN, "=", host).mo20902or(SerializableCookie.DOMAIN, "=", "." + host);
                int firstDot = host.indexOf(".");
                int lastDot = host.lastIndexOf(".");
                if (firstDot > 0 && lastDot > firstDot) {
                    String domain = host.substring(firstDot, host.length());
                    if (!TextUtils.isEmpty(domain)) {
                        subWhere.mo20902or(SerializableCookie.DOMAIN, "=", domain);
                    }
                }
                where.and(subWhere);
            }
            String path = uri2.getPath();
            if (!TextUtils.isEmpty(path)) {
                WhereBuilder subWhere2 = WhereBuilder.m1578b("path", "=", path).mo20902or("path", "=", "/").mo20902or("path", "=", (Object) null);
                int lastSplit = path.lastIndexOf("/");
                while (lastSplit > 0) {
                    path = path.substring(0, lastSplit);
                    subWhere2.mo20902or("path", "=", path);
                    lastSplit = path.lastIndexOf("/");
                }
                where.and(subWhere2);
            }
            where.mo20902or("uri", "=", uri2.toString());
            List<CookieEntity> cookieEntityList = selector.where(where).findAll();
            if (cookieEntityList != null) {
                for (CookieEntity cookieEntity : cookieEntityList) {
                    if (!cookieEntity.isExpired()) {
                        rt.add(cookieEntity.toHttpCookie());
                    }
                }
            }
        } catch (Throwable ex) {
            LogUtil.m1565e(ex.getMessage(), ex);
        }
        return rt;
    }

    public List<HttpCookie> getCookies() {
        List<HttpCookie> rt = new ArrayList<>();
        try {
            List<CookieEntity> cookieEntityList = this.f1215db.findAll(CookieEntity.class);
            if (cookieEntityList != null) {
                for (CookieEntity cookieEntity : cookieEntityList) {
                    if (!cookieEntity.isExpired()) {
                        rt.add(cookieEntity.toHttpCookie());
                    }
                }
            }
        } catch (Throwable ex) {
            LogUtil.m1565e(ex.getMessage(), ex);
        }
        return rt;
    }

    public List<URI> getURIs() {
        String uri;
        List<URI> uris = new ArrayList<>();
        try {
            List<DbModel> uriList = this.f1215db.selector(CookieEntity.class).select("uri").findAll();
            if (uriList != null) {
                for (DbModel model : uriList) {
                    uri = model.getString("uri");
                    if (!TextUtils.isEmpty(uri)) {
                        uris.add(new URI(uri));
                    }
                }
            }
        } catch (Throwable ignored) {
            LogUtil.m1565e(ignored.getMessage(), ignored);
        }
        return uris;
    }

    public boolean remove(URI uri, HttpCookie cookie) {
        if (cookie == null) {
            return true;
        }
        try {
            WhereBuilder where = WhereBuilder.m1578b(SerializableCookie.NAME, "=", cookie.getName());
            String domain = cookie.getDomain();
            if (!TextUtils.isEmpty(domain)) {
                where.and(SerializableCookie.DOMAIN, "=", domain);
            }
            String path = cookie.getPath();
            if (!TextUtils.isEmpty(path)) {
                if (path.length() > 1 && path.endsWith("/")) {
                    path = path.substring(0, path.length() - 1);
                }
                where.and("path", "=", path);
            }
            this.f1215db.delete(CookieEntity.class, where);
            return true;
        } catch (Throwable ex) {
            LogUtil.m1565e(ex.getMessage(), ex);
            return false;
        }
    }

    public boolean removeAll() {
        try {
            this.f1215db.delete((Class<?>) CookieEntity.class);
            return true;
        } catch (Throwable ex) {
            LogUtil.m1565e(ex.getMessage(), ex);
            return true;
        }
    }

    private void trimSize() {
        this.trimExecutor.execute(new Runnable() {
            public void run() {
                List<CookieEntity> rmList;
                long current = System.currentTimeMillis();
                if (current - DbCookieStore.this.lastTrimTime >= 1000) {
                    long unused = DbCookieStore.this.lastTrimTime = current;
                    try {
                        DbCookieStore.this.f1215db.delete(CookieEntity.class, WhereBuilder.m1578b("expiry", "<", Long.valueOf(System.currentTimeMillis())).and("expiry", "!=", -1L));
                    } catch (Throwable ex) {
                        LogUtil.m1565e(ex.getMessage(), ex);
                    }
                    try {
                        int count = (int) DbCookieStore.this.f1215db.selector(CookieEntity.class).count();
                        if (count > 5010 && (rmList = DbCookieStore.this.f1215db.selector(CookieEntity.class).where("expiry", "!=", -1L).orderBy("expiry", false).limit(count - 5000).findAll()) != null) {
                            DbCookieStore.this.f1215db.delete((Object) rmList);
                        }
                    } catch (Throwable ex2) {
                        LogUtil.m1565e(ex2.getMessage(), ex2);
                    }
                }
            }
        });
    }

    private URI getEffectiveURI(URI uri) {
        try {
            return new URI("http", uri.getHost(), uri.getPath(), (String) null, (String) null);
        } catch (Throwable th) {
            return uri;
        }
    }
}
