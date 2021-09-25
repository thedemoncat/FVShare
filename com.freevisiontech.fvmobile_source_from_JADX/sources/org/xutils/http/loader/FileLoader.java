package org.xutils.http.loader;

import android.text.TextUtils;
import com.google.android.vending.expansion.downloader.Constants;
import com.lzy.okgo.model.HttpHeaders;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import org.xutils.cache.DiskCacheEntity;
import org.xutils.cache.DiskCacheFile;
import org.xutils.cache.LruDiskCache;
import org.xutils.common.Callback;
import org.xutils.common.util.IOUtil;
import org.xutils.common.util.LogUtil;
import org.xutils.common.util.ProcessLock;
import org.xutils.http.RequestParams;
import org.xutils.http.request.UriRequest;
import org.xutils.p019ex.FileLockedException;
import org.xutils.p019ex.HttpException;

public class FileLoader extends Loader<File> {
    private static final int CHECK_SIZE = 512;
    private long contentLength;
    private DiskCacheFile diskCacheFile;
    private boolean isAutoRename;
    private boolean isAutoResume;
    private String responseFileName;
    private String saveFilePath;
    private String tempSaveFilePath;

    public Loader<File> newInstance() {
        return new FileLoader();
    }

    public void setParams(RequestParams params) {
        if (params != null) {
            this.params = params;
            this.isAutoResume = params.isAutoResume();
            this.isAutoRename = params.isAutoRename();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:79:0x0162, code lost:
        r13.flush();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:80:0x0169, code lost:
        if (r32.diskCacheFile == null) goto L_0x019e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:81:0x016b, code lost:
        r23 = r32.diskCacheFile.commit();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:85:0x0177, code lost:
        if (r32.progressHandler == null) goto L_0x0181;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:86:0x0179, code lost:
        r32.progressHandler.updateProgress(r6, r8, true);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:87:0x0181, code lost:
        org.xutils.common.util.IOUtil.closeQuietly((java.io.Closeable) r11);
        org.xutils.common.util.IOUtil.closeQuietly((java.io.Closeable) r13);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:88:0x018f, code lost:
        return autoRename(r23);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:92:0x0199, code lost:
        r5 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:93:0x019a, code lost:
        r12 = r13;
        r4 = r11;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:94:0x019e, code lost:
        r23 = r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.io.File load(java.io.InputStream r33) throws java.lang.Throwable {
        /*
            r32 = this;
            r23 = 0
            r4 = 0
            r12 = 0
            java.io.File r24 = new java.io.File     // Catch:{ all -> 0x0190 }
            r0 = r32
            java.lang.String r5 = r0.tempSaveFilePath     // Catch:{ all -> 0x0190 }
            r0 = r24
            r0.<init>(r5)     // Catch:{ all -> 0x0190 }
            boolean r5 = r24.isDirectory()     // Catch:{ all -> 0x008a }
            if (r5 == 0) goto L_0x0018
            org.xutils.common.util.IOUtil.deleteFileOrDir(r24)     // Catch:{ all -> 0x008a }
        L_0x0018:
            boolean r5 = r24.exists()     // Catch:{ all -> 0x008a }
            if (r5 != 0) goto L_0x0031
            java.io.File r15 = r24.getParentFile()     // Catch:{ all -> 0x008a }
            boolean r5 = r15.exists()     // Catch:{ all -> 0x008a }
            if (r5 != 0) goto L_0x002e
            boolean r5 = r15.mkdirs()     // Catch:{ all -> 0x008a }
            if (r5 == 0) goto L_0x0031
        L_0x002e:
            r24.createNewFile()     // Catch:{ all -> 0x008a }
        L_0x0031:
            long r26 = r24.length()     // Catch:{ all -> 0x008a }
            r0 = r32
            boolean r5 = r0.isAutoResume     // Catch:{ all -> 0x008a }
            if (r5 == 0) goto L_0x00a7
            r28 = 0
            int r5 = (r26 > r28 ? 1 : (r26 == r28 ? 0 : -1))
            if (r5 <= 0) goto L_0x00a7
            r20 = 0
            r28 = 512(0x200, double:2.53E-321)
            long r18 = r26 - r28
            r28 = 0
            int r5 = (r18 > r28 ? 1 : (r18 == r28 ? 0 : -1))
            if (r5 <= 0) goto L_0x00f3
            java.io.FileInputStream r21 = new java.io.FileInputStream     // Catch:{ all -> 0x00ff }
            r0 = r21
            r1 = r24
            r0.<init>(r1)     // Catch:{ all -> 0x00ff }
            r5 = 512(0x200, float:7.175E-43)
            r0 = r21
            r1 = r18
            byte[] r16 = org.xutils.common.util.IOUtil.readBytes(r0, r1, r5)     // Catch:{ all -> 0x0083 }
            r28 = 0
            r5 = 512(0x200, float:7.175E-43)
            r0 = r33
            r1 = r28
            byte[] r14 = org.xutils.common.util.IOUtil.readBytes(r0, r1, r5)     // Catch:{ all -> 0x0083 }
            r0 = r16
            boolean r5 = java.util.Arrays.equals(r14, r0)     // Catch:{ all -> 0x0083 }
            if (r5 != 0) goto L_0x0094
            org.xutils.common.util.IOUtil.closeQuietly((java.io.Closeable) r21)     // Catch:{ all -> 0x0083 }
            org.xutils.common.util.IOUtil.deleteFileOrDir(r24)     // Catch:{ all -> 0x0083 }
            java.lang.RuntimeException r5 = new java.lang.RuntimeException     // Catch:{ all -> 0x0083 }
            java.lang.String r10 = "need retry"
            r5.<init>(r10)     // Catch:{ all -> 0x0083 }
            throw r5     // Catch:{ all -> 0x0083 }
        L_0x0083:
            r5 = move-exception
            r20 = r21
        L_0x0086:
            org.xutils.common.util.IOUtil.closeQuietly((java.io.Closeable) r20)     // Catch:{ all -> 0x008a }
            throw r5     // Catch:{ all -> 0x008a }
        L_0x008a:
            r5 = move-exception
            r23 = r24
        L_0x008d:
            org.xutils.common.util.IOUtil.closeQuietly((java.io.Closeable) r4)
            org.xutils.common.util.IOUtil.closeQuietly((java.io.Closeable) r12)
            throw r5
        L_0x0094:
            r0 = r32
            long r0 = r0.contentLength     // Catch:{ all -> 0x0083 }
            r28 = r0
            r30 = 512(0x200, double:2.53E-321)
            long r28 = r28 - r30
            r0 = r28
            r2 = r32
            r2.contentLength = r0     // Catch:{ all -> 0x0083 }
            org.xutils.common.util.IOUtil.closeQuietly((java.io.Closeable) r21)     // Catch:{ all -> 0x008a }
        L_0x00a7:
            r8 = 0
            r17 = 0
            r0 = r32
            boolean r5 = r0.isAutoResume     // Catch:{ all -> 0x008a }
            if (r5 == 0) goto L_0x0101
            r8 = r26
            java.io.FileOutputStream r17 = new java.io.FileOutputStream     // Catch:{ all -> 0x008a }
            r5 = 1
            r0 = r17
            r1 = r24
            r0.<init>(r1, r5)     // Catch:{ all -> 0x008a }
        L_0x00bd:
            r0 = r32
            long r0 = r0.contentLength     // Catch:{ all -> 0x008a }
            r28 = r0
            long r6 = r28 + r8
            java.io.BufferedInputStream r11 = new java.io.BufferedInputStream     // Catch:{ all -> 0x008a }
            r0 = r33
            r11.<init>(r0)     // Catch:{ all -> 0x008a }
            java.io.BufferedOutputStream r13 = new java.io.BufferedOutputStream     // Catch:{ all -> 0x0193 }
            r0 = r17
            r13.<init>(r0)     // Catch:{ all -> 0x0193 }
            r0 = r32
            org.xutils.http.ProgressHandler r5 = r0.progressHandler     // Catch:{ all -> 0x00ed }
            if (r5 == 0) goto L_0x010b
            r0 = r32
            org.xutils.http.ProgressHandler r5 = r0.progressHandler     // Catch:{ all -> 0x00ed }
            r10 = 1
            boolean r5 = r5.updateProgress(r6, r8, r10)     // Catch:{ all -> 0x00ed }
            if (r5 != 0) goto L_0x010b
            org.xutils.common.Callback$CancelledException r5 = new org.xutils.common.Callback$CancelledException     // Catch:{ all -> 0x00ed }
            java.lang.String r10 = "download stopped!"
            r5.<init>(r10)     // Catch:{ all -> 0x00ed }
            throw r5     // Catch:{ all -> 0x00ed }
        L_0x00ed:
            r5 = move-exception
            r12 = r13
            r4 = r11
            r23 = r24
            goto L_0x008d
        L_0x00f3:
            org.xutils.common.util.IOUtil.deleteFileOrDir(r24)     // Catch:{ all -> 0x00ff }
            java.lang.RuntimeException r5 = new java.lang.RuntimeException     // Catch:{ all -> 0x00ff }
            java.lang.String r10 = "need retry"
            r5.<init>(r10)     // Catch:{ all -> 0x00ff }
            throw r5     // Catch:{ all -> 0x00ff }
        L_0x00ff:
            r5 = move-exception
            goto L_0x0086
        L_0x0101:
            java.io.FileOutputStream r17 = new java.io.FileOutputStream     // Catch:{ all -> 0x008a }
            r0 = r17
            r1 = r24
            r0.<init>(r1)     // Catch:{ all -> 0x008a }
            goto L_0x00bd
        L_0x010b:
            r5 = 4096(0x1000, float:5.74E-42)
            byte[] r0 = new byte[r5]     // Catch:{ all -> 0x00ed }
            r25 = r0
        L_0x0111:
            r0 = r25
            int r22 = r11.read(r0)     // Catch:{ all -> 0x00ed }
            r5 = -1
            r0 = r22
            if (r0 == r5) goto L_0x0162
            java.io.File r5 = r24.getParentFile()     // Catch:{ all -> 0x00ed }
            boolean r5 = r5.exists()     // Catch:{ all -> 0x00ed }
            if (r5 != 0) goto L_0x0136
            java.io.File r5 = r24.getParentFile()     // Catch:{ all -> 0x00ed }
            r5.mkdirs()     // Catch:{ all -> 0x00ed }
            java.io.IOException r5 = new java.io.IOException     // Catch:{ all -> 0x00ed }
            java.lang.String r10 = "parent be deleted!"
            r5.<init>(r10)     // Catch:{ all -> 0x00ed }
            throw r5     // Catch:{ all -> 0x00ed }
        L_0x0136:
            r5 = 0
            r0 = r25
            r1 = r22
            r13.write(r0, r5, r1)     // Catch:{ all -> 0x00ed }
            r0 = r22
            long r0 = (long) r0     // Catch:{ all -> 0x00ed }
            r28 = r0
            long r8 = r8 + r28
            r0 = r32
            org.xutils.http.ProgressHandler r5 = r0.progressHandler     // Catch:{ all -> 0x00ed }
            if (r5 == 0) goto L_0x0111
            r0 = r32
            org.xutils.http.ProgressHandler r5 = r0.progressHandler     // Catch:{ all -> 0x00ed }
            r10 = 0
            boolean r5 = r5.updateProgress(r6, r8, r10)     // Catch:{ all -> 0x00ed }
            if (r5 != 0) goto L_0x0111
            r13.flush()     // Catch:{ all -> 0x00ed }
            org.xutils.common.Callback$CancelledException r5 = new org.xutils.common.Callback$CancelledException     // Catch:{ all -> 0x00ed }
            java.lang.String r10 = "download stopped!"
            r5.<init>(r10)     // Catch:{ all -> 0x00ed }
            throw r5     // Catch:{ all -> 0x00ed }
        L_0x0162:
            r13.flush()     // Catch:{ all -> 0x00ed }
            r0 = r32
            org.xutils.cache.DiskCacheFile r5 = r0.diskCacheFile     // Catch:{ all -> 0x00ed }
            if (r5 == 0) goto L_0x019e
            r0 = r32
            org.xutils.cache.DiskCacheFile r5 = r0.diskCacheFile     // Catch:{ all -> 0x00ed }
            org.xutils.cache.DiskCacheFile r23 = r5.commit()     // Catch:{ all -> 0x00ed }
        L_0x0173:
            r0 = r32
            org.xutils.http.ProgressHandler r5 = r0.progressHandler     // Catch:{ all -> 0x0199 }
            if (r5 == 0) goto L_0x0181
            r0 = r32
            org.xutils.http.ProgressHandler r5 = r0.progressHandler     // Catch:{ all -> 0x0199 }
            r10 = 1
            r5.updateProgress(r6, r8, r10)     // Catch:{ all -> 0x0199 }
        L_0x0181:
            org.xutils.common.util.IOUtil.closeQuietly((java.io.Closeable) r11)
            org.xutils.common.util.IOUtil.closeQuietly((java.io.Closeable) r13)
            r0 = r32
            r1 = r23
            java.io.File r5 = r0.autoRename(r1)
            return r5
        L_0x0190:
            r5 = move-exception
            goto L_0x008d
        L_0x0193:
            r5 = move-exception
            r4 = r11
            r23 = r24
            goto L_0x008d
        L_0x0199:
            r5 = move-exception
            r12 = r13
            r4 = r11
            goto L_0x008d
        L_0x019e:
            r23 = r24
            goto L_0x0173
        */
        throw new UnsupportedOperationException("Method not decompiled: org.xutils.http.loader.FileLoader.load(java.io.InputStream):java.io.File");
    }

    public File load(UriRequest request) throws Throwable {
        File result;
        File result2;
        ProcessLock processLock = null;
        try {
            this.saveFilePath = this.params.getSaveFilePath();
            this.diskCacheFile = null;
            if (!TextUtils.isEmpty(this.saveFilePath)) {
                this.tempSaveFilePath = this.saveFilePath + ".tmp";
            } else if (this.progressHandler == null || this.progressHandler.updateProgress(0, 0, false)) {
                initDiskCacheFile(request);
            } else {
                throw new Callback.CancelledException("download stopped!");
            }
            if (this.progressHandler == null || this.progressHandler.updateProgress(0, 0, false)) {
                processLock = ProcessLock.tryLock(this.saveFilePath + "_lock", true);
                if (processLock == null || !processLock.isValid()) {
                    throw new FileLockedException("download exists: " + this.saveFilePath);
                }
                this.params = request.getParams();
                long range = 0;
                if (this.isAutoResume) {
                    File file = new File(this.tempSaveFilePath);
                    long fileLen = file.length();
                    if (fileLen <= 512) {
                        IOUtil.deleteFileOrDir(file);
                        range = 0;
                    } else {
                        range = fileLen - 512;
                    }
                }
                this.params.setHeader("RANGE", "bytes=" + range + Constants.FILENAME_SEQUENCE_SEPARATOR);
                if (this.progressHandler == null || this.progressHandler.updateProgress(0, 0, false)) {
                    request.sendRequest();
                    this.contentLength = request.getContentLength();
                    if (this.isAutoRename) {
                        this.responseFileName = getResponseFileName(request);
                    }
                    if (this.isAutoResume) {
                        this.isAutoResume = isSupportRange(request);
                    }
                    if (this.progressHandler == null || this.progressHandler.updateProgress(0, 0, false)) {
                        if (this.diskCacheFile != null) {
                            DiskCacheEntity entity = this.diskCacheFile.getCacheEntity();
                            entity.setLastAccess(System.currentTimeMillis());
                            entity.setEtag(request.getETag());
                            entity.setExpires(request.getExpiration());
                            entity.setLastModify(new Date(request.getLastModified()));
                        }
                        result = load(request.getInputStream());
                        return result;
                    }
                    throw new Callback.CancelledException("download stopped!");
                }
                throw new Callback.CancelledException("download stopped!");
            }
            throw new Callback.CancelledException("download stopped!");
        } catch (HttpException httpException) {
            if (httpException.getCode() == 416) {
                if (this.diskCacheFile != null) {
                    result2 = this.diskCacheFile.commit();
                } else {
                    result2 = new File(this.tempSaveFilePath);
                }
                if (result2 == null || !result2.exists()) {
                    IOUtil.deleteFileOrDir(result2);
                    throw new IllegalStateException("cache file not found" + request.getCacheKey());
                }
                if (this.isAutoRename) {
                    this.responseFileName = getResponseFileName(request);
                }
                result = autoRename(result2);
            } else {
                throw httpException;
            }
        } finally {
            IOUtil.closeQuietly((Closeable) processLock);
            IOUtil.closeQuietly((Closeable) this.diskCacheFile);
        }
    }

    private void initDiskCacheFile(UriRequest request) throws Throwable {
        DiskCacheEntity entity = new DiskCacheEntity();
        entity.setKey(request.getCacheKey());
        this.diskCacheFile = LruDiskCache.getDiskCache(this.params.getCacheDirName()).createDiskCacheFile(entity);
        if (this.diskCacheFile != null) {
            this.saveFilePath = this.diskCacheFile.getAbsolutePath();
            this.tempSaveFilePath = this.saveFilePath;
            this.isAutoRename = false;
            return;
        }
        throw new IOException("create cache file error:" + request.getCacheKey());
    }

    private File autoRename(File loadedFile) {
        if (this.isAutoRename && loadedFile.exists() && !TextUtils.isEmpty(this.responseFileName)) {
            File newFile = new File(loadedFile.getParent(), this.responseFileName);
            while (newFile.exists()) {
                newFile = new File(loadedFile.getParent(), System.currentTimeMillis() + this.responseFileName);
            }
            return loadedFile.renameTo(newFile) ? newFile : loadedFile;
        } else if (this.saveFilePath.equals(this.tempSaveFilePath)) {
            return loadedFile;
        } else {
            File newFile2 = new File(this.saveFilePath);
            if (!loadedFile.renameTo(newFile2)) {
                return loadedFile;
            }
            return newFile2;
        }
    }

    private static String getResponseFileName(UriRequest request) {
        int startIndex;
        if (request == null) {
            return null;
        }
        String disposition = request.getResponseHeader(HttpHeaders.HEAD_KEY_CONTENT_DISPOSITION);
        if (!TextUtils.isEmpty(disposition) && (startIndex = disposition.indexOf("filename=")) > 0) {
            int startIndex2 = startIndex + 9;
            int endIndex = disposition.indexOf(";", startIndex2);
            if (endIndex < 0) {
                endIndex = disposition.length();
            }
            if (endIndex > startIndex2) {
                try {
                    String name = URLDecoder.decode(disposition.substring(startIndex2, endIndex), request.getParams().getCharset());
                    if (!name.startsWith("\"") || !name.endsWith("\"")) {
                        return name;
                    }
                    return name.substring(1, name.length() - 1);
                } catch (UnsupportedEncodingException ex) {
                    LogUtil.m1565e(ex.getMessage(), ex);
                }
            }
        }
        return null;
    }

    private static boolean isSupportRange(UriRequest request) {
        if (request == null) {
            return false;
        }
        String ranges = request.getResponseHeader("Accept-Ranges");
        if (ranges != null) {
            return ranges.contains("bytes");
        }
        String ranges2 = request.getResponseHeader(HttpHeaders.HEAD_KEY_CONTENT_RANGE);
        if (ranges2 == null || !ranges2.contains("bytes")) {
            return false;
        }
        return true;
    }

    public File loadFromCache(DiskCacheEntity cacheEntity) throws Throwable {
        return LruDiskCache.getDiskCache(this.params.getCacheDirName()).getDiskCacheFile(cacheEntity.getKey());
    }

    public void save2Cache(UriRequest request) {
    }
}
