package org.xutils.http.loader;

import android.text.TextUtils;
import java.io.InputStream;
import org.json.JSONArray;
import org.xutils.cache.DiskCacheEntity;
import org.xutils.common.util.IOUtil;
import org.xutils.http.RequestParams;
import org.xutils.http.request.UriRequest;

class JSONArrayLoader extends Loader<JSONArray> {
    private String charset = "UTF-8";
    private String resultStr = null;

    JSONArrayLoader() {
    }

    public Loader<JSONArray> newInstance() {
        return new JSONArrayLoader();
    }

    public void setParams(RequestParams params) {
        if (params != null) {
            String charset2 = params.getCharset();
            if (!TextUtils.isEmpty(charset2)) {
                this.charset = charset2;
            }
        }
    }

    public JSONArray load(InputStream in) throws Throwable {
        this.resultStr = IOUtil.readStr(in, this.charset);
        return new JSONArray(this.resultStr);
    }

    public JSONArray load(UriRequest request) throws Throwable {
        request.sendRequest();
        return load(request.getInputStream());
    }

    public JSONArray loadFromCache(DiskCacheEntity cacheEntity) throws Throwable {
        if (cacheEntity != null) {
            String text = cacheEntity.getTextContent();
            if (!TextUtils.isEmpty(text)) {
                return new JSONArray(text);
            }
        }
        return null;
    }

    public void save2Cache(UriRequest request) {
        saveStringCache(request, this.resultStr);
    }
}
