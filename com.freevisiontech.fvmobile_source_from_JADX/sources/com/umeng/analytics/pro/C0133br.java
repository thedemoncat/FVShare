package com.umeng.analytics.pro;

import android.content.Context;
import com.umeng.analytics.C0015a;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.util.Locale;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/* renamed from: com.umeng.analytics.pro.br */
/* compiled from: DataHelper */
public class C0133br {

    /* renamed from: a */
    private static final byte[] f499a = {10, 1, 11, 5, 4, C0217dk.f723m, 7, 9, ClosedCaptionCtrl.TAB_OFFSET_CHAN_1, 3, 1, 6, 8, 12, C0217dk.f721k, 91};

    /* renamed from: a */
    public static byte[] m755a(String str) {
        byte[] bArr = null;
        if (str != null) {
            int length = str.length();
            if (length % 2 == 0) {
                bArr = new byte[(length / 2)];
                for (int i = 0; i < length; i += 2) {
                    bArr[i / 2] = (byte) Integer.valueOf(str.substring(i, i + 2), 16).intValue();
                }
            }
        }
        return bArr;
    }

    /* renamed from: a */
    public static boolean m754a(Context context, byte[] bArr) {
        long length = (long) bArr.length;
        if (length <= C0015a.f38y) {
            return false;
        }
        C0155ca.m887a(context).mo510g();
        C0236m.m1296a(context).mo697a(length, System.currentTimeMillis(), C0015a.f35v);
        return true;
    }

    /* renamed from: a */
    public static String m753a(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < bArr.length; i++) {
            stringBuffer.append(String.format("%02X", new Object[]{Byte.valueOf(bArr[i])}));
        }
        return stringBuffer.toString().toLowerCase(Locale.US);
    }

    /* renamed from: b */
    public static byte[] m758b(byte[] bArr) {
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.reset();
            instance.update(bArr);
            return instance.digest();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* renamed from: a */
    public static byte[] m756a(byte[] bArr, byte[] bArr2) throws Exception {
        Cipher instance = Cipher.getInstance("AES/CBC/PKCS7Padding");
        instance.init(1, new SecretKeySpec(bArr2, "AES"), new IvParameterSpec(f499a));
        return instance.doFinal(bArr);
    }

    /* renamed from: b */
    public static byte[] m759b(byte[] bArr, byte[] bArr2) throws Exception {
        Cipher instance = Cipher.getInstance("AES/CBC/PKCS7Padding");
        instance.init(2, new SecretKeySpec(bArr2, "AES"), new IvParameterSpec(f499a));
        return instance.doFinal(bArr);
    }

    /* renamed from: a */
    public static int m751a(int i, String str) {
        int i2 = 0;
        if (((double) new Random().nextFloat()) < 0.001d) {
            if (str == null) {
                C0138bw.m840c("--->", "null signature..");
            }
            try {
                i2 = Integer.parseInt(str.substring(9, 11), 16);
            } catch (Exception e) {
            }
            return (i2 | 128) * 1000;
        }
        int nextInt = new Random().nextInt(i);
        if (nextInt > 255000 || nextInt < 128000) {
            return nextInt;
        }
        return 127000;
    }

    /* renamed from: a */
    public static String m752a(Throwable th) {
        if (th == null) {
            return null;
        }
        try {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            th.printStackTrace(printWriter);
            for (Throwable cause = th.getCause(); cause != null; cause = cause.getCause()) {
                cause.printStackTrace(printWriter);
            }
            String obj = stringWriter.toString();
            printWriter.close();
            stringWriter.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* renamed from: b */
    public static String m757b(String str) {
        return "http://" + str + ".umeng.com/app_logs";
    }

    /* renamed from: c */
    public static String m760c(String str) {
        byte[] bytes = str.getBytes();
        try {
            MessageDigest instance = MessageDigest.getInstance("SHA1");
            instance.update(bytes);
            return m761c(instance.digest());
        } catch (Exception e) {
            return null;
        }
    }

    /* renamed from: c */
    static String m761c(byte[] bArr) {
        String str = "";
        int i = 0;
        while (i < bArr.length) {
            String hexString = Integer.toHexString(bArr[i] & 255);
            if (hexString.length() == 1) {
                str = str + "0";
            }
            i++;
            str = str + hexString;
        }
        return str;
    }
}
