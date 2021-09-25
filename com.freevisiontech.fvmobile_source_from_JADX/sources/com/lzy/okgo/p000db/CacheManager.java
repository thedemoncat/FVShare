package com.lzy.okgo.p000db;

import android.content.ContentValues;
import android.database.Cursor;
import com.lzy.okgo.cache.CacheEntity;
import java.util.List;

/* renamed from: com.lzy.okgo.db.CacheManager */
public class CacheManager extends BaseDao<CacheEntity<Object>> {
    public static CacheManager getInstance() {
        return CacheManagerHolder.instance;
    }

    /* renamed from: com.lzy.okgo.db.CacheManager$CacheManagerHolder */
    private static class CacheManagerHolder {
        /* access modifiers changed from: private */
        public static final CacheManager instance = new CacheManager();

        private CacheManagerHolder() {
        }
    }

    private CacheManager() {
        super(new DBHelper());
    }

    public CacheEntity<Object> parseCursorToBean(Cursor cursor) {
        return CacheEntity.parseCursorToBean(cursor);
    }

    public ContentValues getContentValues(CacheEntity<Object> cacheEntity) {
        return CacheEntity.getContentValues(cacheEntity);
    }

    public String getTableName() {
        return "cache";
    }

    public void unInit() {
    }

    public CacheEntity<Object> get(String key) {
        if (key == null) {
            return null;
        }
        List<CacheEntity<Object>> cacheEntities = query("key=?", new String[]{key});
        if (cacheEntities.size() > 0) {
            return cacheEntities.get(0);
        }
        return null;
    }

    public boolean remove(String key) {
        if (key == null) {
            return false;
        }
        return delete("key=?", new String[]{key});
    }

    public <T> CacheEntity<T> get(String key, Class<T> cls) {
        return get(key);
    }

    public List<CacheEntity<Object>> getAll() {
        return queryAll();
    }

    public <T> CacheEntity<T> replace(String key, CacheEntity<T> entity) {
        entity.setKey(key);
        replace(entity);
        return entity;
    }

    public boolean clear() {
        return deleteAll();
    }
}
