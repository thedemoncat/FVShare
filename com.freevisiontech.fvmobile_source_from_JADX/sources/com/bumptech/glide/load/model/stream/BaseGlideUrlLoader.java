package com.bumptech.glide.load.model.stream;

import android.content.Context;
import android.text.TextUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;
import com.bumptech.glide.load.model.ModelCache;
import com.bumptech.glide.load.model.ModelLoader;
import java.io.InputStream;

public abstract class BaseGlideUrlLoader<T> implements StreamModelLoader<T> {
    private final ModelLoader<GlideUrl, InputStream> concreteLoader;
    private final ModelCache<T, GlideUrl> modelCache;

    /* access modifiers changed from: protected */
    public abstract String getUrl(T t, int i, int i2);

    public BaseGlideUrlLoader(Context context) {
        this(context, (ModelCache) null);
    }

    public BaseGlideUrlLoader(Context context, ModelCache<T, GlideUrl> modelCache2) {
        this((ModelLoader<GlideUrl, InputStream>) Glide.buildModelLoader(GlideUrl.class, InputStream.class, context), modelCache2);
    }

    public BaseGlideUrlLoader(ModelLoader<GlideUrl, InputStream> concreteLoader2) {
        this(concreteLoader2, (ModelCache) null);
    }

    public BaseGlideUrlLoader(ModelLoader<GlideUrl, InputStream> concreteLoader2, ModelCache<T, GlideUrl> modelCache2) {
        this.concreteLoader = concreteLoader2;
        this.modelCache = modelCache2;
    }

    public DataFetcher<InputStream> getResourceFetcher(T model, int width, int height) {
        GlideUrl result = null;
        if (this.modelCache != null) {
            result = this.modelCache.get(model, width, height);
        }
        if (result == null) {
            String stringURL = getUrl(model, width, height);
            if (TextUtils.isEmpty(stringURL)) {
                return null;
            }
            result = new GlideUrl(stringURL, getHeaders(model, width, height));
            if (this.modelCache != null) {
                this.modelCache.put(model, width, height, result);
            }
        }
        return this.concreteLoader.getResourceFetcher(result, width, height);
    }

    /* access modifiers changed from: protected */
    public Headers getHeaders(T t, int width, int height) {
        return Headers.DEFAULT;
    }
}
