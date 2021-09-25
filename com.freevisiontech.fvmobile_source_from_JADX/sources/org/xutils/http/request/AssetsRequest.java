package org.xutils.http.request;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.xutils.C2090x;
import org.xutils.cache.DiskCacheEntity;
import org.xutils.cache.LruDiskCache;
import org.xutils.common.util.IOUtil;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;

public class AssetsRequest extends UriRequest {
    private long contentLength = 0;
    private InputStream inputStream;

    public AssetsRequest(RequestParams params, Type loadType) throws Throwable {
        super(params, loadType);
    }

    public void sendRequest() throws Throwable {
    }

    public boolean isLoading() {
        return true;
    }

    public String getCacheKey() {
        return this.queryUrl;
    }

    public Object loadResult() throws Throwable {
        return this.loader.load((UriRequest) this);
    }

    public Object loadResultFromCache() throws Throwable {
        Date lastModifiedDate;
        DiskCacheEntity cacheEntity = LruDiskCache.getDiskCache(this.params.getCacheDirName()).setMaxSize(this.params.getCacheSize()).get(getCacheKey());
        if (cacheEntity == null || (lastModifiedDate = cacheEntity.getLastModify()) == null || lastModifiedDate.getTime() < getAssetsLastModified()) {
            return null;
        }
        return this.loader.loadFromCache(cacheEntity);
    }

    public void clearCacheHeader() {
    }

    public InputStream getInputStream() throws IOException {
        if (this.inputStream == null && this.callingClassLoader != null) {
            this.inputStream = this.callingClassLoader.getResourceAsStream("assets/" + this.queryUrl.substring("assets://".length()));
            this.contentLength = (long) this.inputStream.available();
        }
        return this.inputStream;
    }

    public void close() throws IOException {
        IOUtil.closeQuietly((Closeable) this.inputStream);
        this.inputStream = null;
    }

    public long getContentLength() {
        try {
            getInputStream();
            return this.contentLength;
        } catch (Throwable ex) {
            LogUtil.m1565e(ex.getMessage(), ex);
            return 0;
        }
    }

    public int getResponseCode() throws IOException {
        return getInputStream() != null ? 200 : 404;
    }

    public String getResponseMessage() throws IOException {
        return null;
    }

    public long getExpiration() {
        return Long.MAX_VALUE;
    }

    public long getLastModified() {
        return getAssetsLastModified();
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

    /* access modifiers changed from: protected */
    public long getAssetsLastModified() {
        return new File(C2090x.app().getApplicationInfo().sourceDir).lastModified();
    }
}
