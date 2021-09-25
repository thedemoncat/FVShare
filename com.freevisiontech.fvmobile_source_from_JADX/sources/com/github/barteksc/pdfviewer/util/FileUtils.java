package com.github.barteksc.pdfviewer.util;

import android.content.Context;
import java.io.File;
import java.io.IOException;

public class FileUtils {
    private FileUtils() {
    }

    public static File fileFromAsset(Context context, String assetName) throws IOException {
        File outFile = new File(context.getCacheDir(), assetName + "-pdfview.pdf");
        if (assetName.contains("/")) {
            outFile.getParentFile().mkdirs();
        }
        copy(context.getAssets().open(assetName), outFile);
        return outFile;
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x001b A[SYNTHETIC, Splitter:B:12:0x001b] */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0020  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void copy(java.io.InputStream r5, java.io.File r6) throws java.io.IOException {
        /*
            r1 = 0
            java.io.FileOutputStream r2 = new java.io.FileOutputStream     // Catch:{ all -> 0x003d }
            r2.<init>(r6)     // Catch:{ all -> 0x003d }
            r3 = 0
            r4 = 1024(0x400, float:1.435E-42)
            byte[] r0 = new byte[r4]     // Catch:{ all -> 0x0017 }
        L_0x000b:
            int r3 = r5.read(r0)     // Catch:{ all -> 0x0017 }
            r4 = -1
            if (r3 == r4) goto L_0x0024
            r4 = 0
            r2.write(r0, r4, r3)     // Catch:{ all -> 0x0017 }
            goto L_0x000b
        L_0x0017:
            r4 = move-exception
            r1 = r2
        L_0x0019:
            if (r5 == 0) goto L_0x001e
            r5.close()     // Catch:{ all -> 0x0036 }
        L_0x001e:
            if (r1 == 0) goto L_0x0023
            r1.close()
        L_0x0023:
            throw r4
        L_0x0024:
            if (r5 == 0) goto L_0x0029
            r5.close()     // Catch:{ all -> 0x002f }
        L_0x0029:
            if (r2 == 0) goto L_0x002e
            r2.close()
        L_0x002e:
            return
        L_0x002f:
            r4 = move-exception
            if (r2 == 0) goto L_0x0035
            r2.close()
        L_0x0035:
            throw r4
        L_0x0036:
            r4 = move-exception
            if (r1 == 0) goto L_0x003c
            r1.close()
        L_0x003c:
            throw r4
        L_0x003d:
            r4 = move-exception
            goto L_0x0019
        */
        throw new UnsupportedOperationException("Method not decompiled: com.github.barteksc.pdfviewer.util.FileUtils.copy(java.io.InputStream, java.io.File):void");
    }
}
