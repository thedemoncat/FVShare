package org.xutils.http.body;

import android.text.TextUtils;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import org.xutils.http.ProgressHandler;

public class InputStreamBody implements ProgressBody {
    private ProgressHandler callBackHandler;
    private InputStream content;
    private String contentType;
    private long current;
    private final long total;

    public InputStreamBody(InputStream inputStream) {
        this(inputStream, (String) null);
    }

    public InputStreamBody(InputStream inputStream, String contentType2) {
        this.current = 0;
        this.content = inputStream;
        this.contentType = contentType2;
        this.total = getInputStreamLength(inputStream);
    }

    public void setProgressHandler(ProgressHandler progressHandler) {
        this.callBackHandler = progressHandler;
    }

    public long getContentLength() {
        return this.total;
    }

    public void setContentType(String contentType2) {
        this.contentType = contentType2;
    }

    public String getContentType() {
        return TextUtils.isEmpty(this.contentType) ? "application/octet-stream" : this.contentType;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
        r9.flush();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0058, code lost:
        if (r8.callBackHandler == null) goto L_0x0064;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x005a, code lost:
        r8.callBackHandler.updateProgress(r8.total, r8.total, true);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0069, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void writeTo(java.io.OutputStream r9) throws java.io.IOException {
        /*
            r8 = this;
            r6 = 1
            org.xutils.http.ProgressHandler r1 = r8.callBackHandler
            if (r1 == 0) goto L_0x001a
            org.xutils.http.ProgressHandler r1 = r8.callBackHandler
            long r2 = r8.total
            long r4 = r8.current
            boolean r1 = r1.updateProgress(r2, r4, r6)
            if (r1 != 0) goto L_0x001a
            org.xutils.common.Callback$CancelledException r1 = new org.xutils.common.Callback$CancelledException
            java.lang.String r2 = "upload stopped!"
            r1.<init>(r2)
            throw r1
        L_0x001a:
            r1 = 1024(0x400, float:1.435E-42)
            byte[] r0 = new byte[r1]
            r7 = 0
        L_0x001f:
            java.io.InputStream r1 = r8.content     // Catch:{ all -> 0x004c }
            int r7 = r1.read(r0)     // Catch:{ all -> 0x004c }
            r1 = -1
            if (r7 == r1) goto L_0x0053
            r1 = 0
            r9.write(r0, r1, r7)     // Catch:{ all -> 0x004c }
            long r2 = r8.current     // Catch:{ all -> 0x004c }
            long r4 = (long) r7     // Catch:{ all -> 0x004c }
            long r2 = r2 + r4
            r8.current = r2     // Catch:{ all -> 0x004c }
            org.xutils.http.ProgressHandler r1 = r8.callBackHandler     // Catch:{ all -> 0x004c }
            if (r1 == 0) goto L_0x001f
            org.xutils.http.ProgressHandler r1 = r8.callBackHandler     // Catch:{ all -> 0x004c }
            long r2 = r8.total     // Catch:{ all -> 0x004c }
            long r4 = r8.current     // Catch:{ all -> 0x004c }
            r6 = 0
            boolean r1 = r1.updateProgress(r2, r4, r6)     // Catch:{ all -> 0x004c }
            if (r1 != 0) goto L_0x001f
            org.xutils.common.Callback$CancelledException r1 = new org.xutils.common.Callback$CancelledException     // Catch:{ all -> 0x004c }
            java.lang.String r2 = "upload stopped!"
            r1.<init>(r2)     // Catch:{ all -> 0x004c }
            throw r1     // Catch:{ all -> 0x004c }
        L_0x004c:
            r1 = move-exception
            java.io.InputStream r2 = r8.content
            org.xutils.common.util.IOUtil.closeQuietly((java.io.Closeable) r2)
            throw r1
        L_0x0053:
            r9.flush()     // Catch:{ all -> 0x004c }
            org.xutils.http.ProgressHandler r1 = r8.callBackHandler     // Catch:{ all -> 0x004c }
            if (r1 == 0) goto L_0x0064
            org.xutils.http.ProgressHandler r1 = r8.callBackHandler     // Catch:{ all -> 0x004c }
            long r2 = r8.total     // Catch:{ all -> 0x004c }
            long r4 = r8.total     // Catch:{ all -> 0x004c }
            r6 = 1
            r1.updateProgress(r2, r4, r6)     // Catch:{ all -> 0x004c }
        L_0x0064:
            java.io.InputStream r1 = r8.content
            org.xutils.common.util.IOUtil.closeQuietly((java.io.Closeable) r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.xutils.http.body.InputStreamBody.writeTo(java.io.OutputStream):void");
    }

    public static long getInputStreamLength(InputStream inputStream) {
        try {
            if ((inputStream instanceof FileInputStream) || (inputStream instanceof ByteArrayInputStream)) {
                return (long) inputStream.available();
            }
        } catch (Throwable th) {
        }
        return -1;
    }
}
