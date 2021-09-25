package com.bumptech.glide.load.model.stream;

import android.content.Context;
import com.bumptech.glide.load.data.ByteArrayFetcher;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import java.io.InputStream;

public class StreamByteArrayLoader implements StreamModelLoader<byte[]> {

    /* renamed from: id */
    private final String f1185id;

    public StreamByteArrayLoader() {
        this("");
    }

    @Deprecated
    public StreamByteArrayLoader(String id) {
        this.f1185id = id;
    }

    public DataFetcher<InputStream> getResourceFetcher(byte[] model, int width, int height) {
        return new ByteArrayFetcher(model, this.f1185id);
    }

    public static class Factory implements ModelLoaderFactory<byte[], InputStream> {
        public ModelLoader<byte[], InputStream> build(Context context, GenericLoaderFactory factories) {
            return new StreamByteArrayLoader();
        }

        public void teardown() {
        }
    }
}
