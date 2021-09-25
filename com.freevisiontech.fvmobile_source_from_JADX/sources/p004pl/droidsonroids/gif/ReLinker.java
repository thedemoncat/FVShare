package p004pl.droidsonroids.gif;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import java.io.Closeable;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/* renamed from: pl.droidsonroids.gif.ReLinker */
class ReLinker {
    private static final int COPY_BUFFER_SIZE = 8192;
    private static final String LIB_DIR = "lib";
    /* access modifiers changed from: private */
    public static final String MAPPED_BASE_LIB_NAME = System.mapLibraryName("pl_droidsonroids_gif");
    private static final int MAX_TRIES = 5;

    private ReLinker() {
    }

    @SuppressLint({"UnsafeDynamicallyLoadedCode"})
    static void loadLibrary(Context context) {
        synchronized (ReLinker.class) {
            System.load(unpackLibrary(context).getAbsolutePath());
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        closeSilently(r10);
        closeSilently(r8);
        setFilePermissions(r13);
     */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00d1  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.io.File unpackLibrary(android.content.Context r21) {
        /*
            java.lang.StringBuilder r18 = new java.lang.StringBuilder
            r18.<init>()
            java.lang.String r19 = MAPPED_BASE_LIB_NAME
            java.lang.StringBuilder r18 = r18.append(r19)
            java.lang.String r19 = "1.2.5-SNAPSHOT"
            java.lang.StringBuilder r18 = r18.append(r19)
            java.lang.String r14 = r18.toString()
            java.io.File r13 = new java.io.File
            java.lang.String r18 = "lib"
            r19 = 0
            r0 = r21
            r1 = r18
            r2 = r19
            java.io.File r18 = r0.getDir(r1, r2)
            r0 = r18
            r13.<init>(r0, r14)
            boolean r18 = r13.isFile()
            if (r18 == 0) goto L_0x0034
            r5 = r13
        L_0x0033:
            return r5
        L_0x0034:
            java.io.File r5 = new java.io.File
            java.io.File r18 = r21.getCacheDir()
            r0 = r18
            r5.<init>(r0, r14)
            boolean r18 = r5.isFile()
            if (r18 != 0) goto L_0x0033
            java.lang.String r18 = "pl_droidsonroids_gif_surface"
            java.lang.String r12 = java.lang.System.mapLibraryName(r18)
            pl.droidsonroids.gif.ReLinker$1 r9 = new pl.droidsonroids.gif.ReLinker$1
            r9.<init>(r12)
            clearOldLibraryFiles(r13, r9)
            clearOldLibraryFiles(r5, r9)
            android.content.pm.ApplicationInfo r4 = r21.getApplicationInfo()
            java.io.File r3 = new java.io.File
            java.lang.String r0 = r4.sourceDir
            r18 = r0
            r0 = r18
            r3.<init>(r0)
            r17 = 0
            java.util.zip.ZipFile r17 = openZipFile(r3)     // Catch:{ all -> 0x00a2 }
            r15 = 0
            r16 = r15
        L_0x006f:
            int r15 = r16 + 1
            r18 = 5
            r0 = r16
            r1 = r18
            if (r0 >= r1) goto L_0x00c2
            java.util.zip.ZipEntry r11 = findLibraryEntry(r17)     // Catch:{ all -> 0x00a2 }
            if (r11 != 0) goto L_0x00a9
            java.lang.IllegalStateException r18 = new java.lang.IllegalStateException     // Catch:{ all -> 0x00a2 }
            java.lang.StringBuilder r19 = new java.lang.StringBuilder     // Catch:{ all -> 0x00a2 }
            r19.<init>()     // Catch:{ all -> 0x00a2 }
            java.lang.String r20 = "Library "
            java.lang.StringBuilder r19 = r19.append(r20)     // Catch:{ all -> 0x00a2 }
            java.lang.String r20 = MAPPED_BASE_LIB_NAME     // Catch:{ all -> 0x00a2 }
            java.lang.StringBuilder r19 = r19.append(r20)     // Catch:{ all -> 0x00a2 }
            java.lang.String r20 = " for supported ABIs not found in APK file"
            java.lang.StringBuilder r19 = r19.append(r20)     // Catch:{ all -> 0x00a2 }
            java.lang.String r19 = r19.toString()     // Catch:{ all -> 0x00a2 }
            r18.<init>(r19)     // Catch:{ all -> 0x00a2 }
            throw r18     // Catch:{ all -> 0x00a2 }
        L_0x00a2:
            r18 = move-exception
            if (r17 == 0) goto L_0x00a8
            r17.close()     // Catch:{ IOException -> 0x00e5 }
        L_0x00a8:
            throw r18
        L_0x00a9:
            r10 = 0
            r7 = 0
            r0 = r17
            java.io.InputStream r10 = r0.getInputStream(r11)     // Catch:{ IOException -> 0x00ca, all -> 0x00db }
            java.io.FileOutputStream r8 = new java.io.FileOutputStream     // Catch:{ IOException -> 0x00ca, all -> 0x00db }
            r8.<init>(r13)     // Catch:{ IOException -> 0x00ca, all -> 0x00db }
            copy(r10, r8)     // Catch:{ IOException -> 0x00ea, all -> 0x00e7 }
            closeSilently(r10)     // Catch:{ all -> 0x00a2 }
            closeSilently(r8)     // Catch:{ all -> 0x00a2 }
            setFilePermissions(r13)     // Catch:{ all -> 0x00a2 }
        L_0x00c2:
            if (r17 == 0) goto L_0x00c7
            r17.close()     // Catch:{ IOException -> 0x00e3 }
        L_0x00c7:
            r5 = r13
            goto L_0x0033
        L_0x00ca:
            r6 = move-exception
        L_0x00cb:
            r18 = 2
            r0 = r18
            if (r15 <= r0) goto L_0x00d2
            r13 = r5
        L_0x00d2:
            closeSilently(r10)     // Catch:{ all -> 0x00a2 }
            closeSilently(r7)     // Catch:{ all -> 0x00a2 }
            r16 = r15
            goto L_0x006f
        L_0x00db:
            r18 = move-exception
        L_0x00dc:
            closeSilently(r10)     // Catch:{ all -> 0x00a2 }
            closeSilently(r7)     // Catch:{ all -> 0x00a2 }
            throw r18     // Catch:{ all -> 0x00a2 }
        L_0x00e3:
            r18 = move-exception
            goto L_0x00c7
        L_0x00e5:
            r19 = move-exception
            goto L_0x00a8
        L_0x00e7:
            r18 = move-exception
            r7 = r8
            goto L_0x00dc
        L_0x00ea:
            r6 = move-exception
            r7 = r8
            goto L_0x00cb
        */
        throw new UnsupportedOperationException("Method not decompiled: p004pl.droidsonroids.gif.ReLinker.unpackLibrary(android.content.Context):java.io.File");
    }

    private static ZipEntry findLibraryEntry(ZipFile zipFile) {
        for (String abi : getSupportedABIs()) {
            ZipEntry libraryEntry = getEntry(zipFile, abi);
            if (libraryEntry != null) {
                return libraryEntry;
            }
        }
        return null;
    }

    private static String[] getSupportedABIs() {
        if (Build.VERSION.SDK_INT >= 21) {
            return Build.SUPPORTED_ABIS;
        }
        return new String[]{Build.CPU_ABI, Build.CPU_ABI2};
    }

    private static ZipEntry getEntry(ZipFile zipFile, String abi) {
        return zipFile.getEntry("lib/" + abi + "/" + MAPPED_BASE_LIB_NAME);
    }

    private static ZipFile openZipFile(File apkFile) {
        int tries = 0;
        ZipFile zipFile = null;
        while (true) {
            int tries2 = tries;
            tries = tries2 + 1;
            if (tries2 >= 5) {
                break;
            }
            try {
                zipFile = new ZipFile(apkFile, 1);
                break;
            } catch (IOException e) {
            }
        }
        if (zipFile != null) {
            return zipFile;
        }
        throw new IllegalStateException("Could not open APK file: " + apkFile.getAbsolutePath());
    }

    private static void clearOldLibraryFiles(File outputFile, FilenameFilter filter) {
        File[] fileList = outputFile.getParentFile().listFiles(filter);
        if (fileList != null) {
            for (File file : fileList) {
                file.delete();
            }
        }
    }

    @SuppressLint({"SetWorldReadable"})
    private static void setFilePermissions(File outputFile) {
        outputFile.setReadable(true, false);
        outputFile.setExecutable(true, false);
        outputFile.setWritable(true);
    }

    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[8192];
        while (true) {
            int bytesRead = in.read(buf);
            if (bytesRead != -1) {
                out.write(buf, 0, bytesRead);
            } else {
                return;
            }
        }
    }

    private static void closeSilently(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }
}
