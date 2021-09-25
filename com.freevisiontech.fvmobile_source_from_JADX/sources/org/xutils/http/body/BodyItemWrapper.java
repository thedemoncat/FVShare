package org.xutils.http.body;

import android.text.TextUtils;

public final class BodyItemWrapper {
    private final String contentType;
    private final String fileName;
    private final Object value;

    public BodyItemWrapper(Object value2, String contentType2) {
        this(value2, contentType2, (String) null);
    }

    public BodyItemWrapper(Object value2, String contentType2, String fileName2) {
        this.value = value2;
        if (TextUtils.isEmpty(contentType2)) {
            this.contentType = "application/octet-stream";
        } else {
            this.contentType = contentType2;
        }
        this.fileName = fileName2;
    }

    public Object getValue() {
        return this.value;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getContentType() {
        return this.contentType;
    }
}
