package org.xutils.common.util;

import android.database.Cursor;
import android.text.TextUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

public class IOUtil {
    private IOUtil() {
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable ignored) {
                LogUtil.m1563d(ignored.getMessage(), ignored);
            }
        }
    }

    public static void closeQuietly(Cursor cursor) {
        if (cursor != null) {
            try {
                cursor.close();
            } catch (Throwable ignored) {
                LogUtil.m1563d(ignored.getMessage(), ignored);
            }
        }
    }

    public static byte[] readBytes(InputStream in) throws IOException {
        if (!(in instanceof BufferedInputStream)) {
            in = new BufferedInputStream(in);
        }
        ByteArrayOutputStream out = null;
        try {
            ByteArrayOutputStream out2 = new ByteArrayOutputStream();
            try {
                byte[] buf = new byte[1024];
                while (true) {
                    int len = in.read(buf);
                    if (len != -1) {
                        out2.write(buf, 0, len);
                    } else {
                        byte[] byteArray = out2.toByteArray();
                        closeQuietly((Closeable) out2);
                        return byteArray;
                    }
                }
            } catch (Throwable th) {
                th = th;
                out = out2;
                closeQuietly((Closeable) out);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            closeQuietly((Closeable) out);
            throw th;
        }
    }

    public static byte[] readBytes(InputStream in, long skip, int size) throws IOException {
        if (skip > 0) {
            while (skip > 0) {
                long skipped = in.skip(skip);
                if (skipped <= 0) {
                    break;
                }
                skip -= skipped;
            }
        }
        byte[] result = new byte[size];
        for (int i = 0; i < size; i++) {
            result[i] = (byte) in.read();
        }
        return result;
    }

    public static String readStr(InputStream in) throws IOException {
        return readStr(in, "UTF-8");
    }

    public static String readStr(InputStream in, String charset) throws IOException {
        if (TextUtils.isEmpty(charset)) {
            charset = "UTF-8";
        }
        if (!(in instanceof BufferedInputStream)) {
            in = new BufferedInputStream(in);
        }
        Reader reader = new InputStreamReader(in, charset);
        StringBuilder sb = new StringBuilder();
        char[] buf = new char[1024];
        while (true) {
            int len = reader.read(buf);
            if (len < 0) {
                return sb.toString();
            }
            sb.append(buf, 0, len);
        }
    }

    public static void writeStr(OutputStream out, String str) throws IOException {
        writeStr(out, str, "UTF-8");
    }

    public static void writeStr(OutputStream out, String str, String charset) throws IOException {
        if (TextUtils.isEmpty(charset)) {
            charset = "UTF-8";
        }
        Writer writer = new OutputStreamWriter(out, charset);
        writer.write(str);
        writer.flush();
    }

    public static void copy(InputStream in, OutputStream out) throws IOException {
        if (!(in instanceof BufferedInputStream)) {
            in = new BufferedInputStream(in);
        }
        if (!(out instanceof BufferedOutputStream)) {
            out = new BufferedOutputStream(out);
        }
        byte[] buffer = new byte[1024];
        while (true) {
            int len = in.read(buffer);
            if (len != -1) {
                out.write(buffer, 0, len);
            } else {
                out.flush();
                return;
            }
        }
    }

    public static boolean deleteFileOrDir(File path) {
        if (path == null || !path.exists()) {
            return true;
        }
        if (path.isFile()) {
            return path.delete();
        }
        File[] files = path.listFiles();
        if (files != null) {
            for (File file : files) {
                deleteFileOrDir(file);
            }
        }
        return path.delete();
    }
}
