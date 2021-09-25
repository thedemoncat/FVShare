package org.xutils.http.loader;

import android.text.TextUtils;
import java.io.InputStream;
import org.xutils.cache.DiskCacheEntity;
import org.xutils.common.util.IOUtil;
import org.xutils.http.RequestParams;
import org.xutils.http.request.UriRequest;

class StringLoader extends Loader<String> {
    private String charset = "UTF-8";
    private String resultStr = null;

    StringLoader() {
    }

    public Loader<String> newInstance() {
        return new StringLoader();
    }

    public void setParams(RequestParams params) {
        if (params != null) {
            String charset2 = params.getCharset();
            if (!TextUtils.isEmpty(charset2)) {
                this.charset = charset2;
            }
        }
    }

    public String load(InputStream in) throws Throwable {
        this.resultStr = IOUtil.readStr(in, this.charset);
        return this.resultStr;
    }

    public String load(UriRequest request) throws Throwable {
        request.sendRequest();
        return load(request.getInputStream());
    }

    public String loadFromCache(DiskCacheEntity cacheEntity) throws Throwable {
        if (cacheEntity != null) {
            return cacheEntity.getTextContent();
        }
        return null;
    }

    public void save2Cache(UriRequest request) {
        saveStringCache(request, this.resultStr);
    }
}
