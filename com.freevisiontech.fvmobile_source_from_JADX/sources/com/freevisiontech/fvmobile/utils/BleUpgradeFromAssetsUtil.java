package com.freevisiontech.fvmobile.utils;

import android.content.Context;
import android.os.Environment;
import com.freevisiontech.fvmobile.application.MyApplication;
import com.freevisiontech.fvmobile.common.BleConstant;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class BleUpgradeFromAssetsUtil {
    private static final String GMUNAME = "gmu.bin";
    private static final String GMUNAME_FM210 = "gmu_fm210.bin";
    private static final String GMUNAME_FM300 = "gmu_fm300.bin";
    private static final String IMUNAME = "imu.bin";
    private static final String IMUNAME_FM210 = "imu_fm210.bin";
    private static final String IMUNAME_FM300 = "imu_fm300.bin";
    public static final String UPDATEINFONAME = "updateinfo.json";
    public static final String UPDATEINFONAME_FM210 = "updateinfo_fm210.json";
    public static final String UPDATEINFONAME_FM300 = "updateinfo_fm300.json";
    private Context ctx;
    private String mPtztype = "";

    public BleUpgradeFromAssetsUtil(Context context) {
        this.ctx = context;
        this.mPtztype = MyApplication.CURRENT_PTZ_TYPE;
    }

    public byte[] getGMUBinaryFromAssert() {
        String gmuName = "gmu.bin";
        if (this.mPtztype.equals("")) {
            gmuName = "gmu.bin";
        } else if (this.mPtztype.equals(BleConstant.FM_300)) {
            gmuName = GMUNAME_FM300;
        } else if (this.mPtztype.equals(BleConstant.FM_210)) {
            gmuName = GMUNAME_FM210;
        }
        return ReadAssertBinaryResource(gmuName);
    }

    public byte[] getIMUBinaryFromAssert() {
        String imuName = "imu.bin";
        if (this.mPtztype.equals("")) {
            imuName = "imu.bin";
        } else if (this.mPtztype.equals(BleConstant.FM_300)) {
            imuName = IMUNAME_FM300;
        } else if (this.mPtztype.equals(BleConstant.FM_210)) {
            imuName = IMUNAME_FM210;
        }
        return ReadAssertBinaryResource(imuName);
    }

    public byte[] ReadAssertBinaryResource(String strAssertFileName) {
        byte[] contents = null;
        try {
            byte[] bArr = new byte[1024];
            InputStream ims = this.ctx.getAssets().open(strAssertFileName);
            contents = getBytesFromInputStream(ims);
            ims.close();
            return contents;
        } catch (IOException e) {
            e.printStackTrace();
            return contents;
        }
    }

    public static byte[] getBytesFromInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while (true) {
            int len = is.read(buffer);
            if (len != -1) {
                bos.write(buffer, 0, len);
            } else {
                bos.flush();
                byte[] result = bos.toByteArray();
                bos.close();
                return result;
            }
        }
    }

    public byte[] getIMUBinaryFromSDCard() {
        String path = getFilePath("imu.bin");
        if (path.isEmpty()) {
            return null;
        }
        return ReadSDCardBinaryResource(path);
    }

    public byte[] getGMUBinaryFromSDCard() {
        String path = getFilePath("gmu.bin");
        if (path.isEmpty()) {
            return null;
        }
        return ReadSDCardBinaryResource(path);
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x002f A[SYNTHETIC, Splitter:B:22:0x002f] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public byte[] ReadSDCardBinaryResource(java.lang.String r7) {
        /*
            r6 = this;
            r0 = 0
            r3 = 0
            java.io.File r2 = new java.io.File     // Catch:{ IOException -> 0x001d }
            r2.<init>(r7)     // Catch:{ IOException -> 0x001d }
            java.io.FileInputStream r4 = new java.io.FileInputStream     // Catch:{ IOException -> 0x001d }
            r4.<init>(r2)     // Catch:{ IOException -> 0x001d }
            byte[] r0 = getBytesFromInputStream(r4)     // Catch:{ IOException -> 0x003b, all -> 0x0038 }
            if (r4 == 0) goto L_0x003e
            r4.close()     // Catch:{ Exception -> 0x0017 }
            r3 = r4
        L_0x0016:
            return r0
        L_0x0017:
            r1 = move-exception
            r1.printStackTrace()
            r3 = r4
            goto L_0x0016
        L_0x001d:
            r1 = move-exception
        L_0x001e:
            r1.printStackTrace()     // Catch:{ all -> 0x002c }
            if (r3 == 0) goto L_0x0016
            r3.close()     // Catch:{ Exception -> 0x0027 }
            goto L_0x0016
        L_0x0027:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0016
        L_0x002c:
            r5 = move-exception
        L_0x002d:
            if (r3 == 0) goto L_0x0032
            r3.close()     // Catch:{ Exception -> 0x0033 }
        L_0x0032:
            throw r5
        L_0x0033:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0032
        L_0x0038:
            r5 = move-exception
            r3 = r4
            goto L_0x002d
        L_0x003b:
            r1 = move-exception
            r3 = r4
            goto L_0x001e
        L_0x003e:
            r3 = r4
            goto L_0x0016
        */
        throw new UnsupportedOperationException("Method not decompiled: com.freevisiontech.fvmobile.utils.BleUpgradeFromAssetsUtil.ReadSDCardBinaryResource(java.lang.String):byte[]");
    }

    public String ReadAssertResource(String strAssertFileName) {
        try {
            return GetStringFromInputStream(this.ctx.getAssets().open(strAssertFileName));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x001e A[SYNTHETIC, Splitter:B:10:0x001e] */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0033 A[SYNTHETIC, Splitter:B:22:0x0033] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String GetStringFromInputStream(java.io.InputStream r7) {
        /*
            r6 = this;
            r0 = 0
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.io.BufferedReader r1 = new java.io.BufferedReader     // Catch:{ IOException -> 0x003e, all -> 0x0030 }
            java.io.InputStreamReader r4 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x003e, all -> 0x0030 }
            r4.<init>(r7)     // Catch:{ IOException -> 0x003e, all -> 0x0030 }
            r1.<init>(r4)     // Catch:{ IOException -> 0x003e, all -> 0x0030 }
        L_0x0010:
            java.lang.String r2 = r1.readLine()     // Catch:{ IOException -> 0x001a, all -> 0x003b }
            if (r2 == 0) goto L_0x0026
            r3.append(r2)     // Catch:{ IOException -> 0x001a, all -> 0x003b }
            goto L_0x0010
        L_0x001a:
            r4 = move-exception
            r0 = r1
        L_0x001c:
            if (r0 == 0) goto L_0x0021
            r0.close()     // Catch:{ IOException -> 0x0037 }
        L_0x0021:
            java.lang.String r4 = r3.toString()
            return r4
        L_0x0026:
            if (r1 == 0) goto L_0x0040
            r1.close()     // Catch:{ IOException -> 0x002d }
            r0 = r1
            goto L_0x0021
        L_0x002d:
            r4 = move-exception
            r0 = r1
            goto L_0x0021
        L_0x0030:
            r4 = move-exception
        L_0x0031:
            if (r0 == 0) goto L_0x0036
            r0.close()     // Catch:{ IOException -> 0x0039 }
        L_0x0036:
            throw r4
        L_0x0037:
            r4 = move-exception
            goto L_0x0021
        L_0x0039:
            r5 = move-exception
            goto L_0x0036
        L_0x003b:
            r4 = move-exception
            r0 = r1
            goto L_0x0031
        L_0x003e:
            r4 = move-exception
            goto L_0x001c
        L_0x0040:
            r0 = r1
            goto L_0x0021
        */
        throw new UnsupportedOperationException("Method not decompiled: com.freevisiontech.fvmobile.utils.BleUpgradeFromAssetsUtil.GetStringFromInputStream(java.io.InputStream):java.lang.String");
    }

    public boolean isSdCardExist() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public String getFileDir() {
        if (!isSdCardExist()) {
            return "";
        }
        String tempPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/FVMobile/download/";
        File file = new File(tempPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return tempPath;
    }

    public String getFilePath(String filename) {
        String fileDir = getFileDir();
        if (!fileDir.equals("")) {
            return fileDir + filename;
        }
        return "";
    }
}
