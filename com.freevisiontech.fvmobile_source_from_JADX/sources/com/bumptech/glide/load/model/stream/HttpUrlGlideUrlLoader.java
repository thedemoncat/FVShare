package com.bumptech.glide.load.model.stream;

import android.content.Context;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.HttpUrlFetcher;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelCache;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.yalantis.ucrop.view.CropImageView;
import java.io.InputStream;

public class HttpUrlGlideUrlLoader implements ModelLoader<GlideUrl, InputStream> {
    private final ModelCache<GlideUrl, GlideUrl> modelCache;

    public static class Factory implements ModelLoaderFactory<GlideUrl, InputStream> {
        private final ModelCache<GlideUrl, GlideUrl> modelCache = new ModelCache<>(CropImageView.DEFAULT_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION);

        public ModelLoader<GlideUrl, InputStream> build(Context context, GenericLoaderFactory factories) {
            return new HttpUrlGlideUrlLoader(this.modelCache);
        }

        public void teardown() {
        }
    }

    public HttpUrlGlideUrlLoader() {
        this((ModelCache<GlideUrl, GlideUrl>) null);
    }

    public HttpUrlGlideUrlLoader(ModelCache<GlideUrl, GlideUrl> modelCache2) {
        this.modelCache = modelCache2;
    }

    public DataFetcher<InputStream> getResourceFetcher(GlideUrl model, int width, int height) {
        GlideUrl url = model;
        if (this.modelCache != null && (url = this.modelCache.get(model, 0, 0)) == null) {
            this.modelCache.put(model, 0, 0, model);
            url = model;
        }
        return new HttpUrlFetcher(url);
    }
}
