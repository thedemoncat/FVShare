package org.xutils.http.loader;

import android.text.TextUtils;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import org.xutils.cache.DiskCacheEntity;
import org.xutils.common.util.IOUtil;
import org.xutils.common.util.ParameterizedTypeUtil;
import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpResponse;
import org.xutils.http.app.InputStreamResponseParser;
import org.xutils.http.app.ResponseParser;
import org.xutils.http.request.UriRequest;

class ObjectLoader extends Loader<Object> {
    private String charset = "UTF-8";
    private final Class<?> objectClass;
    private final Type objectType;
    private final ResponseParser parser;
    private String resultStr = null;

    public ObjectLoader(Type objectType2) {
        Class<?> itemClass;
        this.objectType = objectType2;
        if (objectType2 instanceof ParameterizedType) {
            this.objectClass = (Class) ((ParameterizedType) objectType2).getRawType();
        } else if (objectType2 instanceof TypeVariable) {
            throw new IllegalArgumentException("not support callback type " + objectType2.toString());
        } else {
            this.objectClass = (Class) objectType2;
        }
        if (List.class.equals(this.objectClass)) {
            Type itemType = ParameterizedTypeUtil.getParameterizedType(this.objectType, List.class, 0);
            if (itemType instanceof ParameterizedType) {
                itemClass = (Class) ((ParameterizedType) itemType).getRawType();
            } else if (itemType instanceof TypeVariable) {
                throw new IllegalArgumentException("not support callback type " + itemType.toString());
            } else {
                itemClass = (Class) itemType;
            }
            HttpResponse response = (HttpResponse) itemClass.getAnnotation(HttpResponse.class);
            if (response != null) {
                try {
                    this.parser = (ResponseParser) response.parser().newInstance();
                } catch (Throwable ex) {
                    throw new RuntimeException("create parser error", ex);
                }
            } else {
                throw new IllegalArgumentException("not found @HttpResponse from " + itemType);
            }
        } else {
            HttpResponse response2 = (HttpResponse) this.objectClass.getAnnotation(HttpResponse.class);
            if (response2 != null) {
                try {
                    this.parser = (ResponseParser) response2.parser().newInstance();
                } catch (Throwable ex2) {
                    throw new RuntimeException("create parser error", ex2);
                }
            } else {
                throw new IllegalArgumentException("not found @HttpResponse from " + this.objectType);
            }
        }
    }

    public Loader<Object> newInstance() {
        throw new IllegalAccessError("use constructor create ObjectLoader.");
    }

    public void setParams(RequestParams params) {
        if (params != null) {
            String charset2 = params.getCharset();
            if (!TextUtils.isEmpty(charset2)) {
                this.charset = charset2;
            }
        }
    }

    public Object load(InputStream in) throws Throwable {
        if (this.parser instanceof InputStreamResponseParser) {
            return ((InputStreamResponseParser) this.parser).parse(this.objectType, this.objectClass, in);
        }
        this.resultStr = IOUtil.readStr(in, this.charset);
        return this.parser.parse(this.objectType, this.objectClass, this.resultStr);
    }

    /* JADX INFO: finally extract failed */
    public Object load(UriRequest request) throws Throwable {
        try {
            request.sendRequest();
            this.parser.checkResponse(request);
            return load(request.getInputStream());
        } catch (Throwable th) {
            this.parser.checkResponse(request);
            throw th;
        }
    }

    public Object loadFromCache(DiskCacheEntity cacheEntity) throws Throwable {
        if (cacheEntity != null) {
            String text = cacheEntity.getTextContent();
            if (!TextUtils.isEmpty(text)) {
                return this.parser.parse(this.objectType, this.objectClass, text);
            }
        }
        return null;
    }

    public void save2Cache(UriRequest request) {
        saveStringCache(request, this.resultStr);
    }
}
