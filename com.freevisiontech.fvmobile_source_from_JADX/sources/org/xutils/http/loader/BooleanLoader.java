package org.xutils.http.loader;

import java.io.InputStream;
import org.xutils.cache.DiskCacheEntity;
import org.xutils.http.request.UriRequest;

class BooleanLoader extends Loader<Boolean> {
    BooleanLoader() {
    }

    public Loader<Boolean> newInstance() {
        return new BooleanLoader();
    }

    public Boolean load(InputStream in) throws Throwable {
        return false;
    }

    public Boolean load(UriRequest request) throws Throwable {
        request.sendRequest();
        return Boolean.valueOf(request.getResponseCode() < 300);
    }

    public Boolean loadFromCache(DiskCacheEntity cacheEntity) throws Throwable {
        return null;
    }

    public void save2Cache(UriRequest request) {
    }
}
