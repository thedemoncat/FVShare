package com.freevisiontech.fvmobile.utils;

import android.graphics.Bitmap;
import android.util.LruCache;

public class LruCacheUtils extends LruCache<String, Bitmap> {
    private static int MAXMEMONRY = ((int) (Runtime.getRuntime().maxMemory() / 1024));
    private static LruCacheUtils cacheUtils;

    private LruCacheUtils(int maxSize) {
        super(maxSize);
    }

    public static LruCacheUtils getInstance() {
        if (cacheUtils == null) {
            cacheUtils = new LruCacheUtils(MAXMEMONRY / 5);
        }
        return cacheUtils;
    }

    /* access modifiers changed from: protected */
    public int sizeOf(String key, Bitmap value) {
        return (value.getRowBytes() * value.getHeight()) / 1024;
    }

    /* access modifiers changed from: protected */
    public void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
        super.entryRemoved(evicted, key, oldValue, newValue);
    }

    public void clearCache() {
        if (cacheUtils.size() > 0) {
            cacheUtils.evictAll();
        }
    }

    public synchronized void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (cacheUtils.get(key) == null) {
            if (!isEmpty(key) && bitmap != null) {
                cacheUtils.put(key, bitmap);
            }
        }
    }

    public synchronized Bitmap getBitmapFromMemCache(String key) {
        Bitmap bm;
        if (isEmpty(key)) {
            bm = null;
        } else {
            bm = (Bitmap) cacheUtils.get(key);
            if (bm == null || bm.isRecycled()) {
                bm = null;
            }
        }
        return bm;
    }

    public synchronized void removeImageCache(String key) {
        if (!isEmpty(key)) {
            Bitmap bm = (Bitmap) cacheUtils.remove(key);
            if (bm != null && !bm.isRecycled()) {
                bm.recycle();
            }
        }
    }

    public boolean isEmpty(String... str) {
        if (str == null) {
            return true;
        }
        for (String s : str) {
            if (s == null || s.isEmpty() || s.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
