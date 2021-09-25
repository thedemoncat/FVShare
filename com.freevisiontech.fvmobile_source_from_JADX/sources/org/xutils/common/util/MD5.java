package org.xutils.common.util;

import com.umeng.analytics.pro.C0217dk;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class MD5 {
    private static final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private MD5() {
    }

    public static String toHexString(byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        StringBuilder hex = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            hex.append(hexDigits[(b >> 4) & 15]);
            hex.append(hexDigits[b & C0217dk.f723m]);
        }
        return hex.toString();
    }

    public static String md5(File file) throws IOException {
        FileInputStream in = null;
        FileChannel ch = null;
        try {
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");
            FileInputStream in2 = new FileInputStream(file);
            try {
                ch = in2.getChannel();
                messagedigest.update(ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length()));
                byte[] encodeBytes = messagedigest.digest();
                IOUtil.closeQuietly((Closeable) in2);
                IOUtil.closeQuietly((Closeable) ch);
                return toHexString(encodeBytes);
            } catch (NoSuchAlgorithmException e) {
                neverHappened = e;
                in = in2;
                try {
                    throw new RuntimeException(neverHappened);
                } catch (Throwable th) {
                    th = th;
                    IOUtil.closeQuietly((Closeable) in);
                    IOUtil.closeQuietly((Closeable) ch);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                in = in2;
                IOUtil.closeQuietly((Closeable) in);
                IOUtil.closeQuietly((Closeable) ch);
                throw th;
            }
        } catch (NoSuchAlgorithmException e2) {
            neverHappened = e2;
            throw new RuntimeException(neverHappened);
        }
    }

    public static String md5(String string) {
        try {
            return toHexString(MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException neverHappened) {
            throw new RuntimeException(neverHappened);
        } catch (UnsupportedEncodingException neverHappened2) {
            throw new RuntimeException(neverHappened2);
        }
    }
}
