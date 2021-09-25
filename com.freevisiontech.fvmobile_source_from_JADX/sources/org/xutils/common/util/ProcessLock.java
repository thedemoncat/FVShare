package org.xutils.common.util;

import android.text.TextUtils;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.channels.FileLock;
import java.text.DecimalFormat;
import java.util.concurrent.ConcurrentHashMap;
import org.xutils.C2090x;

public final class ProcessLock implements Closeable {
    private static final DecimalFormat FORMAT = new DecimalFormat("0.##################");
    private static final String LOCK_FILE_DIR = "process_lock";
    private static final DoubleKeyValueMap<String, Integer, ProcessLock> LOCK_MAP = new DoubleKeyValueMap<>();
    private final File mFile;
    private final FileLock mFileLock;
    private final String mLockName;
    private final Closeable mStream;
    private final boolean mWriteMode;

    static {
        IOUtil.deleteFileOrDir(C2090x.app().getDir(LOCK_FILE_DIR, 0));
    }

    private ProcessLock(String lockName, File file, FileLock fileLock, Closeable stream, boolean writeMode) {
        this.mLockName = lockName;
        this.mFileLock = fileLock;
        this.mFile = file;
        this.mStream = stream;
        this.mWriteMode = writeMode;
    }

    public static ProcessLock tryLock(String lockName, boolean writeMode) {
        return tryLockInternal(lockName, customHash(lockName), writeMode);
    }

    public static ProcessLock tryLock(String lockName, boolean writeMode, long maxWaitTimeMillis) throws InterruptedException {
        ProcessLock lock = null;
        long expiryTime = System.currentTimeMillis() + maxWaitTimeMillis;
        String hash = customHash(lockName);
        while (System.currentTimeMillis() < expiryTime && (lock = tryLockInternal(lockName, hash, writeMode)) == null) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException iex) {
                throw iex;
            } catch (Throwable th) {
            }
        }
        return lock;
    }

    public boolean isValid() {
        return isValid(this.mFileLock);
    }

    public void release() {
        release(this.mLockName, this.mFileLock, this.mFile, this.mStream);
    }

    public void close() throws IOException {
        release();
    }

    private static boolean isValid(FileLock fileLock) {
        return fileLock != null && fileLock.isValid();
    }

    private static void release(String lockName, FileLock fileLock, File file, Closeable stream) {
        synchronized (LOCK_MAP) {
            if (fileLock != null) {
                try {
                    LOCK_MAP.remove(lockName, Integer.valueOf(fileLock.hashCode()));
                    ConcurrentHashMap<Integer, ProcessLock> locks = LOCK_MAP.get(lockName);
                    if (locks == null || locks.isEmpty()) {
                        IOUtil.deleteFileOrDir(file);
                    }
                    if (fileLock.channel().isOpen()) {
                        fileLock.release();
                    }
                    IOUtil.closeQuietly((Closeable) fileLock.channel());
                } catch (Throwable th) {
                    IOUtil.closeQuietly((Closeable) fileLock.channel());
                    throw th;
                }
            }
            IOUtil.closeQuietly(stream);
        }
    }

    private static String customHash(String str) {
        if (TextUtils.isEmpty(str)) {
            return "0";
        }
        double hash = 0.0d;
        byte[] bytes = str.getBytes();
        for (int i = 0; i < str.length(); i++) {
            hash = ((255.0d * hash) + ((double) bytes[i])) * 0.005d;
        }
        return FORMAT.format(hash);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r16v0, resolved type: java.io.FileInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r16v1, resolved type: java.io.FileInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r16v2, resolved type: java.io.FileInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r16v3, resolved type: java.io.FileInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r15v0, resolved type: java.io.FileOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r16v4, resolved type: java.io.FileInputStream} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static org.xutils.common.util.ProcessLock tryLockInternal(java.lang.String r19, java.lang.String r20, boolean r21) {
        /*
            org.xutils.common.util.DoubleKeyValueMap<java.lang.String, java.lang.Integer, org.xutils.common.util.ProcessLock> r18 = LOCK_MAP
            monitor-enter(r18)
            org.xutils.common.util.DoubleKeyValueMap<java.lang.String, java.lang.Integer, org.xutils.common.util.ProcessLock> r5 = LOCK_MAP     // Catch:{ all -> 0x0039 }
            r0 = r19
            java.util.concurrent.ConcurrentHashMap r14 = r5.get(r0)     // Catch:{ all -> 0x0039 }
            if (r14 == 0) goto L_0x004e
            boolean r5 = r14.isEmpty()     // Catch:{ all -> 0x0039 }
            if (r5 != 0) goto L_0x004e
            java.util.Set r5 = r14.entrySet()     // Catch:{ all -> 0x0039 }
            java.util.Iterator r13 = r5.iterator()     // Catch:{ all -> 0x0039 }
        L_0x001b:
            boolean r5 = r13.hasNext()     // Catch:{ all -> 0x0039 }
            if (r5 == 0) goto L_0x004e
            java.lang.Object r2 = r13.next()     // Catch:{ all -> 0x0039 }
            java.util.Map$Entry r2 = (java.util.Map.Entry) r2     // Catch:{ all -> 0x0039 }
            java.lang.Object r17 = r2.getValue()     // Catch:{ all -> 0x0039 }
            org.xutils.common.util.ProcessLock r17 = (org.xutils.common.util.ProcessLock) r17     // Catch:{ all -> 0x0039 }
            if (r17 == 0) goto L_0x004a
            boolean r5 = r17.isValid()     // Catch:{ all -> 0x0039 }
            if (r5 != 0) goto L_0x003c
            r13.remove()     // Catch:{ all -> 0x0039 }
            goto L_0x001b
        L_0x0039:
            r5 = move-exception
            monitor-exit(r18)     // Catch:{ all -> 0x0039 }
            throw r5
        L_0x003c:
            if (r21 == 0) goto L_0x0041
            r4 = 0
            monitor-exit(r18)     // Catch:{ all -> 0x0039 }
        L_0x0040:
            return r4
        L_0x0041:
            r0 = r17
            boolean r5 = r0.mWriteMode     // Catch:{ all -> 0x0039 }
            if (r5 == 0) goto L_0x001b
            r4 = 0
            monitor-exit(r18)     // Catch:{ all -> 0x0039 }
            goto L_0x0040
        L_0x004a:
            r13.remove()     // Catch:{ all -> 0x0039 }
            goto L_0x001b
        L_0x004e:
            r3 = 0
            r16 = 0
            java.io.File r10 = new java.io.File     // Catch:{ Throwable -> 0x00e8 }
            android.app.Application r5 = org.xutils.C2090x.app()     // Catch:{ Throwable -> 0x00e8 }
            java.lang.String r6 = "process_lock"
            r8 = 0
            java.io.File r5 = r5.getDir(r6, r8)     // Catch:{ Throwable -> 0x00e8 }
            r0 = r20
            r10.<init>(r5, r0)     // Catch:{ Throwable -> 0x00e8 }
            boolean r5 = r10.exists()     // Catch:{ Throwable -> 0x00e8 }
            if (r5 != 0) goto L_0x0070
            boolean r5 = r10.createNewFile()     // Catch:{ Throwable -> 0x00e8 }
            if (r5 == 0) goto L_0x00c6
        L_0x0070:
            if (r21 == 0) goto L_0x00b1
            java.io.FileOutputStream r15 = new java.io.FileOutputStream     // Catch:{ Throwable -> 0x00e8 }
            r5 = 0
            r15.<init>(r10, r5)     // Catch:{ Throwable -> 0x00e8 }
            java.nio.channels.FileChannel r3 = r15.getChannel()     // Catch:{ Throwable -> 0x00e8 }
            r16 = r15
        L_0x007e:
            if (r3 == 0) goto L_0x00ca
            r4 = 0
            r6 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            if (r21 != 0) goto L_0x00bd
            r8 = 1
        L_0x008a:
            java.nio.channels.FileLock r7 = r3.tryLock(r4, r6, r8)     // Catch:{ Throwable -> 0x00e8 }
            boolean r5 = isValid(r7)     // Catch:{ Throwable -> 0x00e8 }
            if (r5 == 0) goto L_0x00bf
            org.xutils.common.util.ProcessLock r4 = new org.xutils.common.util.ProcessLock     // Catch:{ Throwable -> 0x00e8 }
            r5 = r19
            r6 = r10
            r8 = r16
            r9 = r21
            r4.<init>(r5, r6, r7, r8, r9)     // Catch:{ Throwable -> 0x00e8 }
            org.xutils.common.util.DoubleKeyValueMap<java.lang.String, java.lang.Integer, org.xutils.common.util.ProcessLock> r5 = LOCK_MAP     // Catch:{ Throwable -> 0x00e8 }
            int r6 = r7.hashCode()     // Catch:{ Throwable -> 0x00e8 }
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)     // Catch:{ Throwable -> 0x00e8 }
            r0 = r19
            r5.put(r0, r6, r4)     // Catch:{ Throwable -> 0x00e8 }
            monitor-exit(r18)     // Catch:{ all -> 0x0039 }
            goto L_0x0040
        L_0x00b1:
            java.io.FileInputStream r12 = new java.io.FileInputStream     // Catch:{ Throwable -> 0x00e8 }
            r12.<init>(r10)     // Catch:{ Throwable -> 0x00e8 }
            java.nio.channels.FileChannel r3 = r12.getChannel()     // Catch:{ Throwable -> 0x00e8 }
            r16 = r12
            goto L_0x007e
        L_0x00bd:
            r8 = 0
            goto L_0x008a
        L_0x00bf:
            r0 = r19
            r1 = r16
            release(r0, r7, r10, r1)     // Catch:{ Throwable -> 0x00e8 }
        L_0x00c6:
            monitor-exit(r18)     // Catch:{ all -> 0x0039 }
            r4 = 0
            goto L_0x0040
        L_0x00ca:
            java.io.IOException r5 = new java.io.IOException     // Catch:{ Throwable -> 0x00e8 }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00e8 }
            r6.<init>()     // Catch:{ Throwable -> 0x00e8 }
            java.lang.String r8 = "can not get file channel:"
            java.lang.StringBuilder r6 = r6.append(r8)     // Catch:{ Throwable -> 0x00e8 }
            java.lang.String r8 = r10.getAbsolutePath()     // Catch:{ Throwable -> 0x00e8 }
            java.lang.StringBuilder r6 = r6.append(r8)     // Catch:{ Throwable -> 0x00e8 }
            java.lang.String r6 = r6.toString()     // Catch:{ Throwable -> 0x00e8 }
            r5.<init>(r6)     // Catch:{ Throwable -> 0x00e8 }
            throw r5     // Catch:{ Throwable -> 0x00e8 }
        L_0x00e8:
            r11 = move-exception
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x0039 }
            r5.<init>()     // Catch:{ all -> 0x0039 }
            java.lang.String r6 = "tryLock: "
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ all -> 0x0039 }
            r0 = r19
            java.lang.StringBuilder r5 = r5.append(r0)     // Catch:{ all -> 0x0039 }
            java.lang.String r6 = ", "
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ all -> 0x0039 }
            java.lang.String r6 = r11.getMessage()     // Catch:{ all -> 0x0039 }
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ all -> 0x0039 }
            java.lang.String r5 = r5.toString()     // Catch:{ all -> 0x0039 }
            org.xutils.common.util.LogUtil.m1562d(r5)     // Catch:{ all -> 0x0039 }
            org.xutils.common.util.IOUtil.closeQuietly((java.io.Closeable) r16)     // Catch:{ all -> 0x0039 }
            org.xutils.common.util.IOUtil.closeQuietly((java.io.Closeable) r3)     // Catch:{ all -> 0x0039 }
            goto L_0x00c6
        */
        throw new UnsupportedOperationException("Method not decompiled: org.xutils.common.util.ProcessLock.tryLockInternal(java.lang.String, java.lang.String, boolean):org.xutils.common.util.ProcessLock");
    }

    public String toString() {
        return this.mLockName + ": " + this.mFile.getName();
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        super.finalize();
        release();
    }
}
