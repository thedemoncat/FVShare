package org.xutils.http.body;

import android.text.TextUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;

public class FileBody extends InputStreamBody {
    private String contentType;
    private File file;

    public FileBody(File file2) throws IOException {
        this(file2, (String) null);
    }

    public FileBody(File file2, String contentType2) throws IOException {
        super(new FileInputStream(file2));
        this.file = file2;
        this.contentType = contentType2;
    }

    public void setContentType(String contentType2) {
        this.contentType = contentType2;
    }

    public String getContentType() {
        if (TextUtils.isEmpty(this.contentType)) {
            this.contentType = getFileContentType(this.file);
        }
        return this.contentType;
    }

    public static String getFileContentType(File file2) {
        String contentType2 = HttpURLConnection.guessContentTypeFromName(file2.getName());
        if (TextUtils.isEmpty(contentType2)) {
            return "application/octet-stream";
        }
        return contentType2.replaceFirst("\\/jpg$", "/jpeg");
    }
}
