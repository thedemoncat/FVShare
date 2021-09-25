package org.xutils.http.body;

import android.text.TextUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class StringBody implements RequestBody {
    private String charset = "UTF-8";
    private byte[] content;
    private String contentType;

    public StringBody(String str, String charset2) throws UnsupportedEncodingException {
        if (!TextUtils.isEmpty(charset2)) {
            this.charset = charset2;
        }
        this.content = str.getBytes(this.charset);
    }

    public long getContentLength() {
        return (long) this.content.length;
    }

    public void setContentType(String contentType2) {
        this.contentType = contentType2;
    }

    public String getContentType() {
        return TextUtils.isEmpty(this.contentType) ? "application/json;charset=" + this.charset : this.contentType;
    }

    public void writeTo(OutputStream out) throws IOException {
        out.write(this.content);
        out.flush();
    }
}
