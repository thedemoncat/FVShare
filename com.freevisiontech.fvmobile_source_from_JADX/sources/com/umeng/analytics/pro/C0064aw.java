package com.umeng.analytics.pro;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/* renamed from: com.umeng.analytics.pro.aw */
/* compiled from: ObjectSerializer */
public class C0064aw {
    /* renamed from: a */
    public static String m280a(Serializable serializable) {
        if (serializable == null) {
            return "";
        }
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(serializable);
            objectOutputStream.close();
            return m281a(byteArrayOutputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* renamed from: a */
    public static Object m279a(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        try {
            return new ObjectInputStream(new ByteArrayInputStream(m282b(str))).readObject();
        } catch (Exception e) {
            return null;
        }
    }

    /* renamed from: a */
    public static String m281a(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < bArr.length; i++) {
            stringBuffer.append((char) (((bArr[i] >> 4) & 15) + 97));
            stringBuffer.append((char) ((bArr[i] & C0217dk.f723m) + 97));
        }
        return stringBuffer.toString();
    }

    /* renamed from: b */
    public static byte[] m282b(String str) {
        byte[] bArr = new byte[(str.length() / 2)];
        for (int i = 0; i < str.length(); i += 2) {
            bArr[i / 2] = (byte) ((str.charAt(i) - 'a') << 4);
            int i2 = i / 2;
            bArr[i2] = (byte) ((str.charAt(i + 1) - 'a') + bArr[i2]);
        }
        return bArr;
    }
}
