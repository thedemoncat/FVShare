package com.lzy.okgo.utils;

import android.os.Build;
import android.os.StatFs;
import android.text.TextUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.Closeable;
import java.io.File;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class IOUtils {
    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                OkLogger.printStackTrace(e);
            }
        }
    }

    public static void flushQuietly(Flushable flushable) {
        if (flushable != null) {
            try {
                flushable.flush();
            } catch (Exception e) {
                OkLogger.printStackTrace(e);
            }
        }
    }

    public static InputStream toInputStream(CharSequence input) {
        return new ByteArrayInputStream(input.toString().getBytes());
    }

    public static InputStream toInputStream(CharSequence input, String encoding) throws UnsupportedEncodingException {
        return new ByteArrayInputStream(input.toString().getBytes(encoding));
    }

    public static BufferedInputStream toBufferedInputStream(InputStream inputStream) {
        return inputStream instanceof BufferedInputStream ? (BufferedInputStream) inputStream : new BufferedInputStream(inputStream);
    }

    public static BufferedOutputStream toBufferedOutputStream(OutputStream outputStream) {
        return outputStream instanceof BufferedOutputStream ? (BufferedOutputStream) outputStream : new BufferedOutputStream(outputStream);
    }

    public static BufferedReader toBufferedReader(Reader reader) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
    }

    public static BufferedWriter toBufferedWriter(Writer writer) {
        return writer instanceof BufferedWriter ? (BufferedWriter) writer : new BufferedWriter(writer);
    }

    public static String toString(InputStream input) throws IOException {
        return new String(toByteArray(input));
    }

    public static String toString(InputStream input, String encoding) throws IOException {
        return new String(toByteArray(input), encoding);
    }

    public static String toString(Reader input) throws IOException {
        return new String(toByteArray(input));
    }

    public static String toString(Reader input, String encoding) throws IOException {
        return new String(toByteArray(input), encoding);
    }

    public static String toString(byte[] byteArray) {
        return new String(byteArray);
    }

    public static String toString(byte[] byteArray, String encoding) {
        try {
            return new String(byteArray, encoding);
        } catch (UnsupportedEncodingException e) {
            return new String(byteArray);
        }
    }

    public static byte[] toByteArray(Object input) {
        ObjectOutputStream oos;
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos2 = null;
        try {
            ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
            try {
                oos = new ObjectOutputStream(baos2);
            } catch (IOException e) {
                e = e;
                baos = baos2;
                try {
                    OkLogger.printStackTrace(e);
                    closeQuietly(oos2);
                    closeQuietly(baos);
                    return null;
                } catch (Throwable th) {
                    th = th;
                    closeQuietly(oos2);
                    closeQuietly(baos);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                baos = baos2;
                closeQuietly(oos2);
                closeQuietly(baos);
                throw th;
            }
            try {
                oos.writeObject(input);
                oos.flush();
                byte[] byteArray = baos2.toByteArray();
                closeQuietly(oos);
                closeQuietly(baos2);
                ObjectOutputStream objectOutputStream = oos;
                ByteArrayOutputStream byteArrayOutputStream = baos2;
                return byteArray;
            } catch (IOException e2) {
                e = e2;
                oos2 = oos;
                baos = baos2;
                OkLogger.printStackTrace(e);
                closeQuietly(oos2);
                closeQuietly(baos);
                return null;
            } catch (Throwable th3) {
                th = th3;
                oos2 = oos;
                baos = baos2;
                closeQuietly(oos2);
                closeQuietly(baos);
                throw th;
            }
        } catch (IOException e3) {
            e = e3;
            OkLogger.printStackTrace(e);
            closeQuietly(oos2);
            closeQuietly(baos);
            return null;
        }
    }

    public static Object toObject(byte[] input) {
        ObjectInputStream ois;
        Object obj = null;
        if (input != null) {
            ByteArrayInputStream bais = null;
            ObjectInputStream ois2 = null;
            try {
                ByteArrayInputStream bais2 = new ByteArrayInputStream(input);
                try {
                    ois = new ObjectInputStream(bais2);
                } catch (Exception e) {
                    e = e;
                    bais = bais2;
                    try {
                        OkLogger.printStackTrace(e);
                        closeQuietly(ois2);
                        closeQuietly(bais);
                        return obj;
                    } catch (Throwable th) {
                        th = th;
                        closeQuietly(ois2);
                        closeQuietly(bais);
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    bais = bais2;
                    closeQuietly(ois2);
                    closeQuietly(bais);
                    throw th;
                }
                try {
                    obj = ois.readObject();
                    closeQuietly(ois);
                    closeQuietly(bais2);
                } catch (Exception e2) {
                    e = e2;
                    ois2 = ois;
                    bais = bais2;
                    OkLogger.printStackTrace(e);
                    closeQuietly(ois2);
                    closeQuietly(bais);
                    return obj;
                } catch (Throwable th3) {
                    th = th3;
                    ois2 = ois;
                    bais = bais2;
                    closeQuietly(ois2);
                    closeQuietly(bais);
                    throw th;
                }
            } catch (Exception e3) {
                e = e3;
                OkLogger.printStackTrace(e);
                closeQuietly(ois2);
                closeQuietly(bais);
                return obj;
            }
        }
        return obj;
    }

    public static byte[] toByteArray(CharSequence input) {
        if (input == null) {
            return new byte[0];
        }
        return input.toString().getBytes();
    }

    public static byte[] toByteArray(CharSequence input, String encoding) throws UnsupportedEncodingException {
        if (input == null) {
            return new byte[0];
        }
        return input.toString().getBytes(encoding);
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        write(input, (OutputStream) output);
        output.close();
        return output.toByteArray();
    }

    public static byte[] toByteArray(Reader input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        write(input, (OutputStream) output);
        output.close();
        return output.toByteArray();
    }

    public static byte[] toByteArray(Reader input, String encoding) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        write(input, (OutputStream) output, encoding);
        output.close();
        return output.toByteArray();
    }

    public static char[] toCharArray(CharSequence input) throws IOException {
        CharArrayWriter output = new CharArrayWriter();
        write(input, (Writer) output);
        return output.toCharArray();
    }

    public static char[] toCharArray(InputStream input) throws IOException {
        CharArrayWriter output = new CharArrayWriter();
        write(input, (Writer) output);
        return output.toCharArray();
    }

    public static char[] toCharArray(InputStream input, String encoding) throws IOException {
        CharArrayWriter output = new CharArrayWriter();
        write(input, (Writer) output, encoding);
        return output.toCharArray();
    }

    public static char[] toCharArray(Reader input) throws IOException {
        CharArrayWriter output = new CharArrayWriter();
        write(input, (Writer) output);
        return output.toCharArray();
    }

    public static List<String> readLines(InputStream input, String encoding) throws IOException {
        return readLines(new InputStreamReader(input, encoding));
    }

    public static List<String> readLines(InputStream input) throws IOException {
        return readLines(new InputStreamReader(input));
    }

    public static List<String> readLines(Reader input) throws IOException {
        BufferedReader reader = toBufferedReader(input);
        List<String> list = new ArrayList<>();
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            list.add(line);
        }
        return list;
    }

    public static void write(byte[] data, OutputStream output) throws IOException {
        if (data != null) {
            output.write(data);
        }
    }

    public static void write(byte[] data, Writer output) throws IOException {
        if (data != null) {
            output.write(new String(data));
        }
    }

    public static void write(byte[] data, Writer output, String encoding) throws IOException {
        if (data != null) {
            output.write(new String(data, encoding));
        }
    }

    public static void write(char[] data, Writer output) throws IOException {
        if (data != null) {
            output.write(data);
        }
    }

    public static void write(char[] data, OutputStream output) throws IOException {
        if (data != null) {
            output.write(new String(data).getBytes());
        }
    }

    public static void write(char[] data, OutputStream output, String encoding) throws IOException {
        if (data != null) {
            output.write(new String(data).getBytes(encoding));
        }
    }

    public static void write(CharSequence data, Writer output) throws IOException {
        if (data != null) {
            output.write(data.toString());
        }
    }

    public static void write(CharSequence data, OutputStream output) throws IOException {
        if (data != null) {
            output.write(data.toString().getBytes());
        }
    }

    public static void write(CharSequence data, OutputStream output, String encoding) throws IOException {
        if (data != null) {
            output.write(data.toString().getBytes(encoding));
        }
    }

    public static void write(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[4096];
        while (true) {
            int len = inputStream.read(buffer);
            if (len != -1) {
                outputStream.write(buffer, 0, len);
            } else {
                return;
            }
        }
    }

    public static void write(Reader input, OutputStream output) throws IOException {
        Writer out = new OutputStreamWriter(output);
        write(input, out);
        out.flush();
    }

    public static void write(InputStream input, Writer output) throws IOException {
        write(new InputStreamReader(input), output);
    }

    public static void write(Reader input, OutputStream output, String encoding) throws IOException {
        Writer out = new OutputStreamWriter(output, encoding);
        write(input, out);
        out.flush();
    }

    public static void write(InputStream input, OutputStream output, String encoding) throws IOException {
        write(new InputStreamReader(input, encoding), output);
    }

    public static void write(InputStream input, Writer output, String encoding) throws IOException {
        write(new InputStreamReader(input, encoding), output);
    }

    public static void write(Reader input, Writer output) throws IOException {
        char[] buffer = new char[4096];
        while (true) {
            int len = input.read(buffer);
            if (-1 != len) {
                output.write(buffer, 0, len);
            } else {
                return;
            }
        }
    }

    public static boolean contentEquals(InputStream input1, InputStream input2) throws IOException {
        InputStream input12 = toBufferedInputStream(input1);
        InputStream input22 = toBufferedInputStream(input2);
        for (int ch = input12.read(); -1 != ch; ch = input12.read()) {
            if (ch != input22.read()) {
                return false;
            }
        }
        if (input22.read() == -1) {
            return true;
        }
        return false;
    }

    public static boolean contentEquals(Reader input1, Reader input2) throws IOException {
        Reader input12 = toBufferedReader(input1);
        Reader input22 = toBufferedReader(input2);
        for (int ch = input12.read(); -1 != ch; ch = input12.read()) {
            if (ch != input22.read()) {
                return false;
            }
        }
        if (input22.read() == -1) {
            return true;
        }
        return false;
    }

    public static boolean contentEqualsIgnoreEOL(Reader input1, Reader input2) throws IOException {
        BufferedReader br1 = toBufferedReader(input1);
        BufferedReader br2 = toBufferedReader(input2);
        String line1 = br1.readLine();
        String line2 = br2.readLine();
        while (line1 != null && line2 != null && line1.equals(line2)) {
            line1 = br1.readLine();
            line2 = br2.readLine();
        }
        return line1 != null && (line2 == null || line1.equals(line2));
    }

    public static long getDirSize(String path) {
        try {
            StatFs stat = new StatFs(path);
            if (Build.VERSION.SDK_INT >= 18) {
                return getStatFsSize(stat, "getBlockSizeLong", "getAvailableBlocksLong");
            }
            return getStatFsSize(stat, "getBlockSize", "getAvailableBlocks");
        } catch (Exception e) {
            OkLogger.printStackTrace(e);
            return 0;
        }
    }

    private static long getStatFsSize(StatFs statFs, String blockSizeMethod, String availableBlocksMethod) {
        try {
            Method getBlockSizeMethod = statFs.getClass().getMethod(blockSizeMethod, new Class[0]);
            getBlockSizeMethod.setAccessible(true);
            Method getAvailableBlocksMethod = statFs.getClass().getMethod(availableBlocksMethod, new Class[0]);
            getAvailableBlocksMethod.setAccessible(true);
            return ((Long) getBlockSizeMethod.invoke(statFs, new Object[0])).longValue() * ((Long) getAvailableBlocksMethod.invoke(statFs, new Object[0])).longValue();
        } catch (Throwable e) {
            OkLogger.printStackTrace(e);
            return 0;
        }
    }

    public static boolean canWrite(String path) {
        return new File(path).canWrite();
    }

    public static boolean canRead(String path) {
        return new File(path).canRead();
    }

    public static boolean createFolder(String folderPath) {
        if (!TextUtils.isEmpty(folderPath)) {
            return createFolder(new File(folderPath));
        }
        return false;
    }

    public static boolean createFolder(File targetFolder) {
        if (targetFolder.exists()) {
            if (targetFolder.isDirectory()) {
                return true;
            }
            targetFolder.delete();
        }
        return targetFolder.mkdirs();
    }

    public static boolean createNewFolder(String folderPath) {
        return delFileOrFolder(folderPath) && createFolder(folderPath);
    }

    public static boolean createNewFolder(File targetFolder) {
        return delFileOrFolder(targetFolder) && createFolder(targetFolder);
    }

    public static boolean createFile(String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            return createFile(new File(filePath));
        }
        return false;
    }

    public static boolean createFile(File targetFile) {
        if (targetFile.exists()) {
            if (targetFile.isFile()) {
                return true;
            }
            delFileOrFolder(targetFile);
        }
        try {
            return targetFile.createNewFile();
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean createNewFile(String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            return createNewFile(new File(filePath));
        }
        return false;
    }

    public static boolean createNewFile(File targetFile) {
        if (targetFile.exists()) {
            delFileOrFolder(targetFile);
        }
        try {
            return targetFile.createNewFile();
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean delFileOrFolder(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        return delFileOrFolder(new File(path));
    }

    public static boolean delFileOrFolder(File file) {
        if (file == null || !file.exists()) {
            return true;
        }
        if (file.isFile()) {
            file.delete();
            return true;
        } else if (!file.isDirectory()) {
            return true;
        } else {
            File[] files = file.listFiles();
            if (files != null) {
                for (File sonFile : files) {
                    delFileOrFolder(sonFile);
                }
            }
            file.delete();
            return true;
        }
    }
}
