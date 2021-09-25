package org.xutils.http.body;

import android.text.TextUtils;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.xutils.common.Callback;
import org.xutils.common.util.IOUtil;
import org.xutils.common.util.KeyValue;
import org.xutils.http.ProgressHandler;

public class MultipartBody implements ProgressBody {
    private static byte[] BOUNDARY_PREFIX_BYTES = "--------7da3d81520810".getBytes();
    private static byte[] END_BYTES = "\r\n".getBytes();
    private static byte[] TWO_DASHES_BYTES = "--".getBytes();
    private byte[] boundaryPostfixBytes;
    private ProgressHandler callBackHandler;
    private String charset = "UTF-8";
    private String contentType;
    private long current = 0;
    private List<KeyValue> multipartParams;
    private long total = 0;

    public MultipartBody(List<KeyValue> multipartParams2, String charset2) {
        if (!TextUtils.isEmpty(charset2)) {
            this.charset = charset2;
        }
        this.multipartParams = multipartParams2;
        generateContentType();
        CounterOutputStream counter = new CounterOutputStream();
        try {
            writeTo(counter);
            this.total = counter.total.get();
        } catch (IOException e) {
            this.total = -1;
        }
    }

    public void setProgressHandler(ProgressHandler progressHandler) {
        this.callBackHandler = progressHandler;
    }

    private void generateContentType() {
        String boundaryPostfix = Double.toHexString(Math.random() * 65535.0d);
        this.boundaryPostfixBytes = boundaryPostfix.getBytes();
        this.contentType = "multipart/form-data; boundary=" + new String(BOUNDARY_PREFIX_BYTES) + boundaryPostfix;
    }

    public long getContentLength() {
        return this.total;
    }

    public void setContentType(String subType) {
        this.contentType = "multipart/" + subType + this.contentType.substring(this.contentType.indexOf(";"));
    }

    public String getContentType() {
        return this.contentType;
    }

    public void writeTo(OutputStream out) throws IOException {
        if (this.callBackHandler == null || this.callBackHandler.updateProgress(this.total, this.current, true)) {
            for (KeyValue kv : this.multipartParams) {
                String name = kv.key;
                Object value = kv.value;
                if (!TextUtils.isEmpty(name) && value != null) {
                    writeEntry(out, name, value);
                }
            }
            writeLine(out, TWO_DASHES_BYTES, BOUNDARY_PREFIX_BYTES, this.boundaryPostfixBytes, TWO_DASHES_BYTES);
            out.flush();
            if (this.callBackHandler != null) {
                this.callBackHandler.updateProgress(this.total, this.total, true);
                return;
            }
            return;
        }
        throw new Callback.CancelledException("upload stopped!");
    }

    private void writeEntry(OutputStream out, String name, Object value) throws IOException {
        byte[] content;
        writeLine(out, TWO_DASHES_BYTES, BOUNDARY_PREFIX_BYTES, this.boundaryPostfixBytes);
        String fileName = "";
        String contentType2 = null;
        if (value instanceof BodyItemWrapper) {
            BodyItemWrapper wrapper = (BodyItemWrapper) value;
            value = wrapper.getValue();
            fileName = wrapper.getFileName();
            contentType2 = wrapper.getContentType();
        }
        if (value instanceof File) {
            File file = (File) value;
            if (TextUtils.isEmpty(fileName)) {
                fileName = file.getName();
            }
            if (TextUtils.isEmpty(contentType2)) {
                contentType2 = FileBody.getFileContentType(file);
            }
            writeLine(out, buildContentDisposition(name, fileName, this.charset));
            writeLine(out, buildContentType(value, contentType2, this.charset));
            writeLine(out, new byte[0][]);
            writeFile(out, file);
            writeLine(out, new byte[0][]);
            return;
        }
        writeLine(out, buildContentDisposition(name, fileName, this.charset));
        writeLine(out, buildContentType(value, contentType2, this.charset));
        writeLine(out, new byte[0][]);
        if (value instanceof InputStream) {
            writeStreamAndCloseIn(out, (InputStream) value);
            writeLine(out, new byte[0][]);
            return;
        }
        if (value instanceof byte[]) {
            content = (byte[]) value;
        } else {
            content = String.valueOf(value).getBytes(this.charset);
        }
        writeLine(out, content);
        this.current += (long) content.length;
        if (this.callBackHandler != null && !this.callBackHandler.updateProgress(this.total, this.current, false)) {
            throw new Callback.CancelledException("upload stopped!");
        }
    }

    private void writeLine(OutputStream out, byte[]... bs) throws IOException {
        if (bs != null) {
            for (byte[] b : bs) {
                out.write(b);
            }
        }
        out.write(END_BYTES);
    }

    private void writeFile(OutputStream out, File file) throws IOException {
        if (out instanceof CounterOutputStream) {
            ((CounterOutputStream) out).addFile(file);
        } else {
            writeStreamAndCloseIn(out, new FileInputStream(file));
        }
    }

    private void writeStreamAndCloseIn(OutputStream out, InputStream in) throws IOException {
        if (out instanceof CounterOutputStream) {
            ((CounterOutputStream) out).addStream(in);
            return;
        }
        try {
            byte[] buf = new byte[1024];
            while (true) {
                int len = in.read(buf);
                if (len >= 0) {
                    out.write(buf, 0, len);
                    this.current += (long) len;
                    if (this.callBackHandler != null && !this.callBackHandler.updateProgress(this.total, this.current, false)) {
                        throw new Callback.CancelledException("upload stopped!");
                    }
                } else {
                    return;
                }
            }
        } finally {
            IOUtil.closeQuietly((Closeable) in);
        }
    }

    private static byte[] buildContentDisposition(String name, String fileName, String charset2) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder("Content-Disposition: form-data");
        result.append("; name=\"").append(name.replace("\"", "\\\"")).append("\"");
        if (!TextUtils.isEmpty(fileName)) {
            result.append("; filename=\"").append(fileName.replace("\"", "\\\"")).append("\"");
        }
        return result.toString().getBytes(charset2);
    }

    private static byte[] buildContentType(Object value, String contentType2, String charset2) throws UnsupportedEncodingException {
        String contentType3;
        StringBuilder result = new StringBuilder("Content-Type: ");
        if (!TextUtils.isEmpty(contentType2)) {
            contentType3 = contentType2.replaceFirst("\\/jpg$", "/jpeg");
        } else if (value instanceof String) {
            contentType3 = "text/plain; charset=" + charset2;
        } else {
            contentType3 = "application/octet-stream";
        }
        result.append(contentType3);
        return result.toString().getBytes(charset2);
    }

    private class CounterOutputStream extends OutputStream {
        final AtomicLong total = new AtomicLong(0);

        public CounterOutputStream() {
        }

        public void addFile(File file) {
            if (this.total.get() != -1) {
                this.total.addAndGet(file.length());
            }
        }

        public void addStream(InputStream inputStream) {
            if (this.total.get() != -1) {
                long length = InputStreamBody.getInputStreamLength(inputStream);
                if (length > 0) {
                    this.total.addAndGet(length);
                } else {
                    this.total.set(-1);
                }
            }
        }

        public void write(int oneByte) throws IOException {
            if (this.total.get() != -1) {
                this.total.incrementAndGet();
            }
        }

        public void write(byte[] buffer) throws IOException {
            if (this.total.get() != -1) {
                this.total.addAndGet((long) buffer.length);
            }
        }

        public void write(byte[] buffer, int offset, int count) throws IOException {
            if (this.total.get() != -1) {
                this.total.addAndGet((long) count);
            }
        }
    }
}
