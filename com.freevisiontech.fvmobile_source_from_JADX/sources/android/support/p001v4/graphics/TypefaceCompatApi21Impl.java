package android.support.p001v4.graphics;

import android.os.ParcelFileDescriptor;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import java.io.File;

@RequiresApi(21)
@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
/* renamed from: android.support.v4.graphics.TypefaceCompatApi21Impl */
class TypefaceCompatApi21Impl extends TypefaceCompatBaseImpl {
    private static final String TAG = "TypefaceCompatApi21Impl";

    TypefaceCompatApi21Impl() {
    }

    private File getFile(ParcelFileDescriptor fd) {
        try {
            String path = Os.readlink("/proc/self/fd/" + fd.getFd());
            if (OsConstants.S_ISREG(Os.stat(path).st_mode)) {
                return new File(path);
            }
            return null;
        } catch (ErrnoException e) {
            return null;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:45:0x0062, code lost:
        r6 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x0063, code lost:
        r7 = r8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x006b, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x006c, code lost:
        if (r3 != null) goto L_0x006e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x006e, code lost:
        if (r7 != null) goto L_0x0070;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:?, code lost:
        r3.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:?, code lost:
        throw r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x0074, code lost:
        r9 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x0075, code lost:
        r7.addSuppressed(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x0079, code lost:
        r3.close();
     */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x0062 A[ExcHandler: all (th java.lang.Throwable)] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.graphics.Typeface createFromFontInfo(android.content.Context r12, android.os.CancellationSignal r13, @android.support.annotation.NonNull android.support.p001v4.provider.FontsContractCompat.FontInfo[] r14, int r15) {
        /*
            r11 = this;
            int r6 = r14.length
            r7 = 1
            if (r6 >= r7) goto L_0x0006
            r6 = 0
        L_0x0005:
            return r6
        L_0x0006:
            android.support.v4.provider.FontsContractCompat$FontInfo r0 = r11.findBestInfo(r14, r15)
            android.content.ContentResolver r5 = r12.getContentResolver()
            android.net.Uri r6 = r0.getUri()     // Catch:{ IOException -> 0x0048 }
            java.lang.String r7 = "r"
            android.os.ParcelFileDescriptor r4 = r5.openFileDescriptor(r6, r7, r13)     // Catch:{ IOException -> 0x0048 }
            r8 = 0
            java.io.File r2 = r11.getFile(r4)     // Catch:{ Throwable -> 0x0050, all -> 0x0062 }
            if (r2 == 0) goto L_0x0026
            boolean r6 = r2.canRead()     // Catch:{ Throwable -> 0x0050, all -> 0x0062 }
            if (r6 != 0) goto L_0x007d
        L_0x0026:
            java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch:{ Throwable -> 0x0050, all -> 0x0062 }
            java.io.FileDescriptor r6 = r4.getFileDescriptor()     // Catch:{ Throwable -> 0x0050, all -> 0x0062 }
            r3.<init>(r6)     // Catch:{ Throwable -> 0x0050, all -> 0x0062 }
            r7 = 0
            android.graphics.Typeface r6 = super.createFromInputStream(r12, r3)     // Catch:{ Throwable -> 0x0069 }
            if (r3 == 0) goto L_0x003b
            if (r7 == 0) goto L_0x005e
            r3.close()     // Catch:{ Throwable -> 0x004b, all -> 0x0062 }
        L_0x003b:
            if (r4 == 0) goto L_0x0005
            if (r8 == 0) goto L_0x0065
            r4.close()     // Catch:{ Throwable -> 0x0043 }
            goto L_0x0005
        L_0x0043:
            r7 = move-exception
            r8.addSuppressed(r7)     // Catch:{ IOException -> 0x0048 }
            goto L_0x0005
        L_0x0048:
            r1 = move-exception
            r6 = 0
            goto L_0x0005
        L_0x004b:
            r9 = move-exception
            r7.addSuppressed(r9)     // Catch:{ Throwable -> 0x0050, all -> 0x0062 }
            goto L_0x003b
        L_0x0050:
            r6 = move-exception
            throw r6     // Catch:{ all -> 0x0052 }
        L_0x0052:
            r7 = move-exception
            r10 = r7
            r7 = r6
            r6 = r10
        L_0x0056:
            if (r4 == 0) goto L_0x005d
            if (r7 == 0) goto L_0x009a
            r4.close()     // Catch:{ Throwable -> 0x0095 }
        L_0x005d:
            throw r6     // Catch:{ IOException -> 0x0048 }
        L_0x005e:
            r3.close()     // Catch:{ Throwable -> 0x0050, all -> 0x0062 }
            goto L_0x003b
        L_0x0062:
            r6 = move-exception
            r7 = r8
            goto L_0x0056
        L_0x0065:
            r4.close()     // Catch:{ IOException -> 0x0048 }
            goto L_0x0005
        L_0x0069:
            r7 = move-exception
            throw r7     // Catch:{ all -> 0x006b }
        L_0x006b:
            r6 = move-exception
            if (r3 == 0) goto L_0x0073
            if (r7 == 0) goto L_0x0079
            r3.close()     // Catch:{ Throwable -> 0x0074, all -> 0x0062 }
        L_0x0073:
            throw r6     // Catch:{ Throwable -> 0x0050, all -> 0x0062 }
        L_0x0074:
            r9 = move-exception
            r7.addSuppressed(r9)     // Catch:{ Throwable -> 0x0050, all -> 0x0062 }
            goto L_0x0073
        L_0x0079:
            r3.close()     // Catch:{ Throwable -> 0x0050, all -> 0x0062 }
            goto L_0x0073
        L_0x007d:
            android.graphics.Typeface r6 = android.graphics.Typeface.createFromFile(r2)     // Catch:{ Throwable -> 0x0050, all -> 0x0062 }
            if (r4 == 0) goto L_0x0005
            if (r8 == 0) goto L_0x0090
            r4.close()     // Catch:{ Throwable -> 0x008a }
            goto L_0x0005
        L_0x008a:
            r7 = move-exception
            r8.addSuppressed(r7)     // Catch:{ IOException -> 0x0048 }
            goto L_0x0005
        L_0x0090:
            r4.close()     // Catch:{ IOException -> 0x0048 }
            goto L_0x0005
        L_0x0095:
            r8 = move-exception
            r7.addSuppressed(r8)     // Catch:{ IOException -> 0x0048 }
            goto L_0x005d
        L_0x009a:
            r4.close()     // Catch:{ IOException -> 0x0048 }
            goto L_0x005d
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p001v4.graphics.TypefaceCompatApi21Impl.createFromFontInfo(android.content.Context, android.os.CancellationSignal, android.support.v4.provider.FontsContractCompat$FontInfo[], int):android.graphics.Typeface");
    }
}
