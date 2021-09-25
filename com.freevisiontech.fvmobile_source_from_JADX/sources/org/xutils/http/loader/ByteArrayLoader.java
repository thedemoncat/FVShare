package org.xutils.http.loader;

import java.io.InputStream;
import org.xutils.cache.DiskCacheEntity;
import org.xutils.common.util.IOUtil;
import org.xutils.http.request.UriRequest;

class ByteArrayLoader extends Loader<byte[]> {
    ByteArrayLoader() {
    }

    public Loader<byte[]> newInstance() {
        return new ByteArrayLoader();
    }

    public byte[] load(InputStream in) throws Throwable {
        return IOUtil.readBytes(in);
    }

    public byte[] load(UriRequest request) throws Throwable {
        request.sendRequest();
        return load(request.getInputStream());
    }

    public byte[] loadFromCache(DiskCacheEntity cacheEntity) throws Throwable {
        return null;
    }

    public void save2Cache(UriRequest request) {
    }
}
