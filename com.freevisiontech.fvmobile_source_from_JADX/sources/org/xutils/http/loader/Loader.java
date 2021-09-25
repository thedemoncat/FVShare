package org.xutils.http.loader;

import android.text.TextUtils;
import java.io.InputStream;
import java.util.Date;
import org.xutils.cache.DiskCacheEntity;
import org.xutils.cache.LruDiskCache;
import org.xutils.http.ProgressHandler;
import org.xutils.http.RequestParams;
import org.xutils.http.request.UriRequest;

public abstract class Loader<T> {
    protected RequestParams params;
    protected ProgressHandler progressHandler;

    public abstract T load(InputStream inputStream) throws Throwable;

    public abstract T load(UriRequest uriRequest) throws Throwable;

    public abstract T loadFromCache(DiskCacheEntity diskCacheEntity) throws Throwable;

    public abstract Loader<T> newInstance();

    public abstract void save2Cache(UriRequest uriRequest);

    public void setParams(RequestParams params2) {
        this.params = params2;
    }

    public void setProgressHandler(ProgressHandler callbackHandler) {
        this.progressHandler = callbackHandler;
    }

    /* access modifiers changed from: protected */
    public void saveStringCache(UriRequest request, String resultStr) {
        if (!TextUtils.isEmpty(resultStr)) {
            DiskCacheEntity entity = new DiskCacheEntity();
            entity.setKey(request.getCacheKey());
            entity.setLastAccess(System.currentTimeMillis());
            entity.setEtag(request.getETag());
            entity.setExpires(request.getExpiration());
            entity.setLastModify(new Date(request.getLastModified()));
            entity.setTextContent(resultStr);
            LruDiskCache.getDiskCache(request.getParams().getCacheDirName()).put(entity);
        }
    }
}
