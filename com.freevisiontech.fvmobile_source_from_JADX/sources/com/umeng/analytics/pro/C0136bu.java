package com.umeng.analytics.pro;

import android.text.TextUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* renamed from: com.umeng.analytics.pro.bu */
/* compiled from: HelperUtils */
public class C0136bu {

    /* renamed from: a */
    public static final String f507a = System.getProperty("line.separator");

    /* renamed from: b */
    private static final String f508b = "helper";

    /* renamed from: a */
    public static String m814a(String str, int i) {
        String str2;
        int i2;
        String str3 = "";
        try {
            if (!TextUtils.isEmpty(str)) {
                String substring = str.substring(0, str.length() < i ? str.length() : i);
                try {
                    int i3 = i;
                    String str4 = substring;
                    int length = substring.getBytes("UTF-8").length;
                    str3 = str4;
                    while (length > i) {
                        i3--;
                        if (i3 > str.length()) {
                            i2 = str.length();
                        } else {
                            i2 = i3;
                        }
                        String substring2 = str.substring(0, i2);
                        str3 = substring2;
                        length = substring2.getBytes("UTF-8").length;
                    }
                } catch (Exception e) {
                    Exception exc = e;
                    str3 = str2;
                    e = exc;
                    C0138bw.m853e((Throwable) e);
                    return str3;
                }
            }
        } catch (Exception e2) {
            e = e2;
            C0138bw.m853e((Throwable) e);
            return str3;
        }
        return str3;
    }

    /* renamed from: a */
    public static String m813a(String str) {
        if (str == null) {
            return null;
        }
        try {
            byte[] bytes = str.getBytes();
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.reset();
            instance.update(bytes);
            byte[] digest = instance.digest();
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < digest.length; i++) {
                stringBuffer.append(String.format("%02X", new Object[]{Byte.valueOf(digest[i])}));
            }
            return stringBuffer.toString();
        } catch (Exception e) {
            return str.replaceAll("[^[a-z][A-Z][0-9][.][_]]", "");
        }
    }

    /* renamed from: b */
    public static String m819b(String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(str.getBytes());
            byte[] digest = instance.digest();
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : digest) {
                stringBuffer.append(Integer.toHexString(b & 255));
            }
            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            C0138bw.m838c(f508b, "getMD5 error", (Throwable) e);
            return "";
        }
    }

    /* renamed from: a */
    public static String m811a(File file) {
        byte[] bArr = new byte[1024];
        try {
            if (!file.isFile()) {
                return "";
            }
            MessageDigest instance = MessageDigest.getInstance("MD5");
            FileInputStream fileInputStream = new FileInputStream(file);
            while (true) {
                int read = fileInputStream.read(bArr, 0, 1024);
                if (read != -1) {
                    instance.update(bArr, 0, read);
                } else {
                    fileInputStream.close();
                    return String.format("%1$032x", new Object[]{new BigInteger(1, instance.digest())});
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* renamed from: a */
    public static String m812a(InputStream inputStream) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        char[] cArr = new char[1024];
        StringWriter stringWriter = new StringWriter();
        while (true) {
            int read = inputStreamReader.read(cArr);
            if (-1 == read) {
                return stringWriter.toString();
            }
            stringWriter.write(cArr, 0, read);
        }
    }

    /* renamed from: b */
    public static byte[] m820b(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[1024];
        while (true) {
            int read = inputStream.read(bArr);
            if (-1 == read) {
                return byteArrayOutputStream.toByteArray();
            }
            byteArrayOutputStream.write(bArr, 0, read);
        }
    }

    /* renamed from: a */
    public static void m816a(File file, byte[] bArr) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        try {
            fileOutputStream.write(bArr);
            fileOutputStream.flush();
        } finally {
            m817a((OutputStream) fileOutputStream);
        }
    }

    /* renamed from: a */
    public static void m815a(File file, String str) throws IOException {
        m816a(file, str.getBytes());
    }

    /* renamed from: b */
    public static String m818b(File file) {
        FileInputStream fileInputStream;
        Throwable th;
        FileInputStream fileInputStream2;
        try {
            if (!file.exists()) {
                m821c((InputStream) null);
                return null;
            }
            fileInputStream = new FileInputStream(file);
            try {
                byte[] bArr = new byte[fileInputStream.available()];
                fileInputStream.read(bArr);
                String str = new String(bArr);
                m821c(fileInputStream);
                return str;
            } catch (Throwable th2) {
                th = th2;
                m821c(fileInputStream);
                throw th;
            }
        } catch (Throwable th3) {
            fileInputStream = null;
            th = th3;
            m821c(fileInputStream);
            throw th;
        }
    }

    /* renamed from: c */
    public static void m821c(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (Exception e) {
            }
        }
    }

    /* renamed from: a */
    public static void m817a(OutputStream outputStream) {
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (Exception e) {
            }
        }
    }
}
