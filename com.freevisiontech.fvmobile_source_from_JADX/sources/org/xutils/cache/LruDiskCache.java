package org.xutils.cache;

import android.text.TextUtils;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;
import org.xutils.C2090x;
import org.xutils.DbManager;
import org.xutils.common.task.PriorityExecutor;
import org.xutils.common.util.FileUtil;
import org.xutils.common.util.IOUtil;
import org.xutils.common.util.LogUtil;
import org.xutils.common.util.MD5;
import org.xutils.common.util.ProcessLock;
import org.xutils.config.DbConfigs;
import org.xutils.p018db.sqlite.WhereBuilder;
import org.xutils.p019ex.DbException;
import org.xutils.p019ex.FileLockedException;

public final class LruDiskCache {
    private static final String CACHE_DIR_NAME = "xUtils_cache";
    private static final HashMap<String, LruDiskCache> DISK_CACHE_MAP = new HashMap<>(5);
    private static final int LIMIT_COUNT = 5000;
    private static final long LIMIT_SIZE = 104857600;
    private static final int LOCK_WAIT = 3000;
    private static final String TEMP_FILE_SUFFIX = ".tmp";
    private static final long TRIM_TIME_SPAN = 1000;
    /* access modifiers changed from: private */
    public boolean available = false;
    /* access modifiers changed from: private */
    public final DbManager cacheDb = C2090x.getDb(DbConfigs.HTTP.getConfig());
    /* access modifiers changed from: private */
    public File cacheDir;
    /* access modifiers changed from: private */
    public long diskCacheSize = LIMIT_SIZE;
    /* access modifiers changed from: private */
    public long lastTrimTime = 0;
    private final Executor trimExecutor = new PriorityExecutor(1, true);

    public static synchronized LruDiskCache getDiskCache(String dirName) {
        LruDiskCache cache;
        synchronized (LruDiskCache.class) {
            if (TextUtils.isEmpty(dirName)) {
                dirName = CACHE_DIR_NAME;
            }
            cache = DISK_CACHE_MAP.get(dirName);
            if (cache == null) {
                cache = new LruDiskCache(dirName);
                DISK_CACHE_MAP.put(dirName, cache);
            }
        }
        return cache;
    }

    private LruDiskCache(String dirName) {
        this.cacheDir = FileUtil.getCacheDir(dirName);
        if (this.cacheDir != null && (this.cacheDir.exists() || this.cacheDir.mkdirs())) {
            this.available = true;
        }
        deleteNoIndexFiles();
    }

    public LruDiskCache setMaxSize(long maxSize) {
        if (maxSize > 0) {
            long diskFreeSize = FileUtil.getDiskAvailableSize();
            if (diskFreeSize > maxSize) {
                this.diskCacheSize = maxSize;
            } else {
                this.diskCacheSize = diskFreeSize;
            }
        }
        return this;
    }

    public DiskCacheEntity get(String key) {
        if (!this.available || TextUtils.isEmpty(key)) {
            return null;
        }
        DiskCacheEntity result = null;
        try {
            result = this.cacheDb.selector(DiskCacheEntity.class).where("key", "=", key).findFirst();
        } catch (Throwable ex) {
            LogUtil.m1565e(ex.getMessage(), ex);
        }
        if (result == null) {
            return result;
        }
        if (result.getExpires() < System.currentTimeMillis()) {
            return null;
        }
        final DiskCacheEntity finalResult = result;
        this.trimExecutor.execute(new Runnable() {
            public void run() {
                finalResult.setHits(finalResult.getHits() + 1);
                finalResult.setLastAccess(System.currentTimeMillis());
                try {
                    LruDiskCache.this.cacheDb.update(finalResult, "hits", "lastAccess");
                } catch (Throwable ex) {
                    LogUtil.m1565e(ex.getMessage(), ex);
                }
            }
        });
        return result;
    }

    public void put(DiskCacheEntity entity) {
        if (this.available && entity != null && !TextUtils.isEmpty(entity.getTextContent()) && entity.getExpires() >= System.currentTimeMillis()) {
            try {
                this.cacheDb.replace(entity);
            } catch (DbException ex) {
                LogUtil.m1565e(ex.getMessage(), ex);
            }
            trimSize();
        }
    }

    public DiskCacheFile getDiskCacheFile(String key) throws InterruptedException {
        DiskCacheEntity entity;
        ProcessLock processLock;
        if (!this.available || TextUtils.isEmpty(key) || (entity = get(key)) == null || !new File(entity.getPath()).exists() || (processLock = ProcessLock.tryLock(entity.getPath(), false, 3000)) == null || !processLock.isValid()) {
            return null;
        }
        DiskCacheFile result = new DiskCacheFile(entity, entity.getPath(), processLock);
        if (result.exists()) {
            return result;
        }
        try {
            this.cacheDb.delete((Object) entity);
        } catch (DbException ex) {
            LogUtil.m1565e(ex.getMessage(), ex);
        }
        return null;
    }

    public DiskCacheFile createDiskCacheFile(DiskCacheEntity entity) throws IOException {
        if (!this.available || entity == null) {
            return null;
        }
        entity.setPath(new File(this.cacheDir, MD5.md5(entity.getKey())).getAbsolutePath());
        String tempFilePath = entity.getPath() + TEMP_FILE_SUFFIX;
        ProcessLock processLock = ProcessLock.tryLock(tempFilePath, true);
        if (processLock == null || !processLock.isValid()) {
            throw new FileLockedException(entity.getPath());
        }
        DiskCacheFile result = new DiskCacheFile(entity, tempFilePath, processLock);
        if (result.getParentFile().exists()) {
            return result;
        }
        result.mkdirs();
        return result;
    }

    public void clearCacheFiles() {
        IOUtil.deleteFileOrDir(this.cacheDir);
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00a0  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x00c4  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.xutils.cache.DiskCacheFile commitDiskCacheFile(org.xutils.cache.DiskCacheFile r13) throws java.io.IOException {
        /*
            r12 = this;
            r6 = 0
            if (r13 == 0) goto L_0x0011
            long r8 = r13.length()
            r10 = 1
            int r7 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r7 >= 0) goto L_0x0011
            org.xutils.common.util.IOUtil.closeQuietly((java.io.Closeable) r13)
        L_0x0010:
            return r6
        L_0x0011:
            boolean r7 = r12.available
            if (r7 == 0) goto L_0x0010
            if (r13 == 0) goto L_0x0010
            r6 = 0
            org.xutils.cache.DiskCacheEntity r0 = r13.cacheEntity
            java.lang.String r7 = r13.getName()
            java.lang.String r8 = ".tmp"
            boolean r7 = r7.endsWith(r8)
            if (r7 == 0) goto L_0x00cb
            r5 = 0
            r1 = 0
            java.lang.String r3 = r0.getPath()     // Catch:{ InterruptedException -> 0x00b1 }
            r7 = 1
            r8 = 3000(0xbb8, double:1.482E-320)
            org.xutils.common.util.ProcessLock r5 = org.xutils.common.util.ProcessLock.tryLock(r3, r7, r8)     // Catch:{ InterruptedException -> 0x00b1 }
            if (r5 == 0) goto L_0x00ab
            boolean r7 = r5.isValid()     // Catch:{ InterruptedException -> 0x00b1 }
            if (r7 == 0) goto L_0x00ab
            org.xutils.cache.DiskCacheFile r2 = new org.xutils.cache.DiskCacheFile     // Catch:{ InterruptedException -> 0x00b1 }
            r2.<init>(r0, r3, r5)     // Catch:{ InterruptedException -> 0x00b1 }
            boolean r7 = r13.renameTo(r2)     // Catch:{ InterruptedException -> 0x0067, all -> 0x009c }
            if (r7 == 0) goto L_0x007e
            r6 = r2
            org.xutils.DbManager r7 = r12.cacheDb     // Catch:{ DbException -> 0x005e }
            r7.replace(r0)     // Catch:{ DbException -> 0x005e }
        L_0x004d:
            r12.trimSize()     // Catch:{ InterruptedException -> 0x0067, all -> 0x009c }
            if (r6 != 0) goto L_0x00b3
            r6 = r13
            org.xutils.common.util.IOUtil.closeQuietly((java.io.Closeable) r2)
            org.xutils.common.util.IOUtil.closeQuietly((java.io.Closeable) r5)
            org.xutils.common.util.IOUtil.deleteFileOrDir(r2)
            r1 = r2
            goto L_0x0010
        L_0x005e:
            r4 = move-exception
            java.lang.String r7 = r4.getMessage()     // Catch:{ InterruptedException -> 0x0067, all -> 0x009c }
            org.xutils.common.util.LogUtil.m1565e(r7, r4)     // Catch:{ InterruptedException -> 0x0067, all -> 0x009c }
            goto L_0x004d
        L_0x0067:
            r4 = move-exception
            r1 = r2
        L_0x0069:
            r6 = r13
            java.lang.String r7 = r4.getMessage()     // Catch:{ all -> 0x00ce }
            org.xutils.common.util.LogUtil.m1565e(r7, r4)     // Catch:{ all -> 0x00ce }
            if (r6 != 0) goto L_0x00bc
            r6 = r13
            org.xutils.common.util.IOUtil.closeQuietly((java.io.Closeable) r1)
            org.xutils.common.util.IOUtil.closeQuietly((java.io.Closeable) r5)
            org.xutils.common.util.IOUtil.deleteFileOrDir(r1)
            goto L_0x0010
        L_0x007e:
            java.io.IOException r7 = new java.io.IOException     // Catch:{ InterruptedException -> 0x0067, all -> 0x009c }
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ InterruptedException -> 0x0067, all -> 0x009c }
            r8.<init>()     // Catch:{ InterruptedException -> 0x0067, all -> 0x009c }
            java.lang.String r9 = "rename:"
            java.lang.StringBuilder r8 = r8.append(r9)     // Catch:{ InterruptedException -> 0x0067, all -> 0x009c }
            java.lang.String r9 = r13.getAbsolutePath()     // Catch:{ InterruptedException -> 0x0067, all -> 0x009c }
            java.lang.StringBuilder r8 = r8.append(r9)     // Catch:{ InterruptedException -> 0x0067, all -> 0x009c }
            java.lang.String r8 = r8.toString()     // Catch:{ InterruptedException -> 0x0067, all -> 0x009c }
            r7.<init>(r8)     // Catch:{ InterruptedException -> 0x0067, all -> 0x009c }
            throw r7     // Catch:{ InterruptedException -> 0x0067, all -> 0x009c }
        L_0x009c:
            r7 = move-exception
            r1 = r2
        L_0x009e:
            if (r6 != 0) goto L_0x00c4
            r6 = r13
            org.xutils.common.util.IOUtil.closeQuietly((java.io.Closeable) r1)
            org.xutils.common.util.IOUtil.closeQuietly((java.io.Closeable) r5)
            org.xutils.common.util.IOUtil.deleteFileOrDir(r1)
        L_0x00aa:
            throw r7
        L_0x00ab:
            org.xutils.ex.FileLockedException r7 = new org.xutils.ex.FileLockedException     // Catch:{ InterruptedException -> 0x00b1 }
            r7.<init>(r3)     // Catch:{ InterruptedException -> 0x00b1 }
            throw r7     // Catch:{ InterruptedException -> 0x00b1 }
        L_0x00b1:
            r4 = move-exception
            goto L_0x0069
        L_0x00b3:
            org.xutils.common.util.IOUtil.closeQuietly((java.io.Closeable) r13)
            org.xutils.common.util.IOUtil.deleteFileOrDir(r13)
            r1 = r2
            goto L_0x0010
        L_0x00bc:
            org.xutils.common.util.IOUtil.closeQuietly((java.io.Closeable) r13)
            org.xutils.common.util.IOUtil.deleteFileOrDir(r13)
            goto L_0x0010
        L_0x00c4:
            org.xutils.common.util.IOUtil.closeQuietly((java.io.Closeable) r13)
            org.xutils.common.util.IOUtil.deleteFileOrDir(r13)
            goto L_0x00aa
        L_0x00cb:
            r6 = r13
            goto L_0x0010
        L_0x00ce:
            r7 = move-exception
            goto L_0x009e
        */
        throw new UnsupportedOperationException("Method not decompiled: org.xutils.cache.LruDiskCache.commitDiskCacheFile(org.xutils.cache.DiskCacheFile):org.xutils.cache.DiskCacheFile");
    }

    private void trimSize() {
        this.trimExecutor.execute(new Runnable() {
            public void run() {
                List<DiskCacheEntity> rmList;
                if (LruDiskCache.this.available) {
                    long current = System.currentTimeMillis();
                    if (current - LruDiskCache.this.lastTrimTime >= 1000) {
                        long unused = LruDiskCache.this.lastTrimTime = current;
                        LruDiskCache.this.deleteExpiry();
                        try {
                            int count = (int) LruDiskCache.this.cacheDb.selector(DiskCacheEntity.class).count();
                            if (count > 5010 && (rmList = LruDiskCache.this.cacheDb.selector(DiskCacheEntity.class).orderBy("lastAccess").orderBy("hits").limit(count - 5000).offset(0).findAll()) != null && rmList.size() > 0) {
                                for (DiskCacheEntity entity : rmList) {
                                    String path = entity.getPath();
                                    if (!TextUtils.isEmpty(path) && LruDiskCache.this.deleteFileWithLock(path) && LruDiskCache.this.deleteFileWithLock(path + LruDiskCache.TEMP_FILE_SUFFIX)) {
                                        LruDiskCache.this.cacheDb.delete((Object) entity);
                                    }
                                }
                            }
                        } catch (DbException ex) {
                            LogUtil.m1565e(ex.getMessage(), ex);
                        }
                        while (FileUtil.getFileOrDirSize(LruDiskCache.this.cacheDir) > LruDiskCache.this.diskCacheSize) {
                            try {
                                List<DiskCacheEntity> rmList2 = LruDiskCache.this.cacheDb.selector(DiskCacheEntity.class).orderBy("lastAccess").orderBy("hits").limit(10).offset(0).findAll();
                                if (rmList2 != null && rmList2.size() > 0) {
                                    for (DiskCacheEntity entity2 : rmList2) {
                                        String path2 = entity2.getPath();
                                        if (!TextUtils.isEmpty(path2) && LruDiskCache.this.deleteFileWithLock(path2) && LruDiskCache.this.deleteFileWithLock(path2 + LruDiskCache.TEMP_FILE_SUFFIX)) {
                                            LruDiskCache.this.cacheDb.delete((Object) entity2);
                                        }
                                    }
                                }
                            } catch (DbException ex2) {
                                LogUtil.m1565e(ex2.getMessage(), ex2);
                                return;
                            }
                        }
                    }
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void deleteExpiry() {
        try {
            WhereBuilder whereBuilder = WhereBuilder.m1578b("expires", "<", Long.valueOf(System.currentTimeMillis()));
            List<DiskCacheEntity> rmList = this.cacheDb.selector(DiskCacheEntity.class).where(whereBuilder).findAll();
            this.cacheDb.delete(DiskCacheEntity.class, whereBuilder);
            if (rmList != null && rmList.size() > 0) {
                for (DiskCacheEntity entity : rmList) {
                    String path = entity.getPath();
                    if (!TextUtils.isEmpty(path)) {
                        deleteFileWithLock(path);
                    }
                }
            }
        } catch (Throwable ex) {
            LogUtil.m1565e(ex.getMessage(), ex);
        }
    }

    private void deleteNoIndexFiles() {
        this.trimExecutor.execute(new Runnable() {
            public void run() {
                if (LruDiskCache.this.available) {
                    try {
                        File[] fileList = LruDiskCache.this.cacheDir.listFiles();
                        if (fileList != null) {
                            for (File file : fileList) {
                                if (LruDiskCache.this.cacheDb.selector(DiskCacheEntity.class).where("path", "=", file.getAbsolutePath()).count() < 1) {
                                    IOUtil.deleteFileOrDir(file);
                                }
                            }
                        }
                    } catch (Throwable ex) {
                        LogUtil.m1565e(ex.getMessage(), ex);
                    }
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public boolean deleteFileWithLock(String path) {
        ProcessLock processLock = null;
        try {
            processLock = ProcessLock.tryLock(path, true);
            if (processLock != null && processLock.isValid()) {
                return IOUtil.deleteFileOrDir(new File(path));
            }
            IOUtil.closeQuietly((Closeable) processLock);
            return false;
        } finally {
            IOUtil.closeQuietly((Closeable) processLock);
        }
    }
}
