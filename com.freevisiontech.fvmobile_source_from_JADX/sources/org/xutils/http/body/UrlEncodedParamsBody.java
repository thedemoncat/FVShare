package org.xutils.http.body;

import android.net.Uri;
import android.text.TextUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import org.xutils.common.util.KeyValue;

public class UrlEncodedParamsBody implements RequestBody {
    private String charset = "UTF-8";
    private byte[] content;

    public UrlEncodedParamsBody(List<KeyValue> params, String charset2) throws IOException {
        if (!TextUtils.isEmpty(charset2)) {
            this.charset = charset2;
        }
        StringBuilder contentSb = new StringBuilder();
        if (params != null) {
            for (KeyValue kv : params) {
                String name = kv.key;
                String value = kv.getValueStr();
                if (!TextUtils.isEmpty(name) && value != null) {
                    if (contentSb.length() > 0) {
                        contentSb.append("&");
                    }
                    contentSb.append(Uri.encode(name, this.charset)).append("=").append(Uri.encode(value, this.charset));
                }
            }
        }
        this.content = contentSb.toString().getBytes(this.charset);
    }

    public long getContentLength() {
        return (long) this.content.length;
    }

    public void setContentType(String contentType) {
    }

    public String getContentType() {
        return "application/x-www-form-urlencoded;charset=" + this.charset;
    }

    public void writeTo(OutputStream sink) throws IOException {
        sink.write(this.content);
        sink.flush();
    }
}
