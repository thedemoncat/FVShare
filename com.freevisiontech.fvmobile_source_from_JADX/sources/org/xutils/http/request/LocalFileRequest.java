package org.xutils.http.request;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import org.xutils.common.util.IOUtil;
import org.xutils.http.RequestParams;
import org.xutils.http.loader.FileLoader;

public class LocalFileRequest extends UriRequest {
    private InputStream inputStream;

    LocalFileRequest(RequestParams params, Type loadType) throws Throwable {
        super(params, loadType);
    }

    public void sendRequest() throws Throwable {
    }

    public boolean isLoading() {
        return true;
    }

    public String getCacheKey() {
        return null;
    }

    public Object loadResult() throws Throwable {
        if (this.loader instanceof FileLoader) {
            return getFile();
        }
        return this.loader.load((UriRequest) this);
    }

    public Object loadResultFromCache() throws Throwable {
        return null;
    }

    public void clearCacheHeader() {
    }

    public void save2Cache() {
    }

    private File getFile() {
        String filePath;
        if (this.queryUrl.startsWith("file:")) {
            filePath = this.queryUrl.substring("file:".length());
        } else {
            filePath = this.queryUrl;
        }
        return new File(filePath);
    }

    public InputStream getInputStream() throws IOException {
        if (this.inputStream == null) {
            this.inputStream = new FileInputStream(getFile());
        }
        return this.inputStream;
    }

    public void close() throws IOException {
        IOUtil.closeQuietly((Closeable) this.inputStream);
        this.inputStream = null;
    }

    public long getContentLength() {
        return getFile().length();
    }

    public int getResponseCode() throws IOException {
        return getFile().exists() ? 200 : 404;
    }

    public String getResponseMessage() throws IOException {
        return null;
    }

    public long getExpiration() {
        return -1;
    }

    public long getLastModified() {
        return getFile().lastModified();
    }

    public String getETag() {
        return null;
    }

    public String getResponseHeader(String name) {
        return null;
    }

    public Map<String, List<String>> getResponseHeaders() {
        return null;
    }

    public long getHeaderFieldDate(String name, long defaultValue) {
        return defaultValue;
    }
}
