package com.bumptech.glide.load.engine.cache;

import android.util.Log;
import com.bumptech.glide.disklrucache.DiskLruCache;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.cache.DiskCache;
import java.io.File;
import java.io.IOException;

public class DiskLruCacheWrapper implements DiskCache {
    private static final int APP_VERSION = 1;
    private static final String TAG = "DiskLruCacheWrapper";
    private static final int VALUE_COUNT = 1;
    private static DiskLruCacheWrapper wrapper = null;
    private final File directory;
    private DiskLruCache diskLruCache;
    private final int maxSize;
    private final SafeKeyGenerator safeKeyGenerator;
    private final DiskCacheWriteLocker writeLocker = new DiskCacheWriteLocker();

    public static synchronized DiskCache get(File directory2, int maxSize2) {
        DiskLruCacheWrapper diskLruCacheWrapper;
        synchronized (DiskLruCacheWrapper.class) {
            if (wrapper == null) {
                wrapper = new DiskLruCacheWrapper(directory2, maxSize2);
            }
            diskLruCacheWrapper = wrapper;
        }
        return diskLruCacheWrapper;
    }

    protected DiskLruCacheWrapper(File directory2, int maxSize2) {
        this.directory = directory2;
        this.maxSize = maxSize2;
        this.safeKeyGenerator = new SafeKeyGenerator();
    }

    private synchronized DiskLruCache getDiskCache() throws IOException {
        if (this.diskLruCache == null) {
            this.diskLruCache = DiskLruCache.open(this.directory, 1, 1, (long) this.maxSize);
        }
        return this.diskLruCache;
    }

    private synchronized void resetDiskCache() {
        this.diskLruCache = null;
    }

    public File get(Key key) {
        try {
            DiskLruCache.Value value = getDiskCache().get(this.safeKeyGenerator.getSafeKey(key));
            if (value != null) {
                return value.getFile(0);
            }
            return null;
        } catch (IOException e) {
            if (!Log.isLoggable(TAG, 5)) {
                return null;
            }
            Log.w(TAG, "Unable to get from disk cache", e);
            return null;
        }
    }

    public void put(Key key, DiskCache.Writer writer) {
        DiskLruCache.Editor editor;
        String safeKey = this.safeKeyGenerator.getSafeKey(key);
        this.writeLocker.acquire(key);
        try {
            editor = getDiskCache().edit(safeKey);
            if (editor != null) {
                if (writer.write(editor.getFile(0))) {
                    editor.commit();
                }
                editor.abortUnlessCommitted();
            }
            this.writeLocker.release(key);
        } catch (IOException e) {
            try {
                if (Log.isLoggable(TAG, 5)) {
                    Log.w(TAG, "Unable to put to disk cache", e);
                }
            } finally {
                this.writeLocker.release(key);
            }
        } catch (Throwable th) {
            editor.abortUnlessCommitted();
            throw th;
        }
    }

    public void delete(Key key) {
        try {
            getDiskCache().remove(this.safeKeyGenerator.getSafeKey(key));
        } catch (IOException e) {
            if (Log.isLoggable(TAG, 5)) {
                Log.w(TAG, "Unable to delete from disk cache", e);
            }
        }
    }

    public synchronized void clear() {
        try {
            getDiskCache().delete();
            resetDiskCache();
        } catch (IOException e) {
            if (Log.isLoggable(TAG, 5)) {
                Log.w(TAG, "Unable to clear disk cache", e);
            }
        }
    }
}
