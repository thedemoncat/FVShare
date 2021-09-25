package com.bumptech.glide;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.load.resource.transcode.GifDrawableBytesTranscoder;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import java.io.InputStream;

public class GifTypeRequest<ModelType> extends GifRequestBuilder<ModelType> {
    private final RequestManager.OptionsApplier optionsApplier;
    private final ModelLoader<ModelType, InputStream> streamModelLoader;

    /* JADX WARNING: type inference failed for: r5v0, types: [java.lang.Class, java.lang.Class<R>] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static <A, R> com.bumptech.glide.provider.FixedLoadProvider<A, java.io.InputStream, com.bumptech.glide.load.resource.gif.GifDrawable, R> buildProvider(com.bumptech.glide.Glide r3, com.bumptech.glide.load.model.ModelLoader<A, java.io.InputStream> r4, java.lang.Class<R> r5, com.bumptech.glide.load.resource.transcode.ResourceTranscoder<com.bumptech.glide.load.resource.gif.GifDrawable, R> r6) {
        /*
            if (r4 != 0) goto L_0x0004
            r1 = 0
        L_0x0003:
            return r1
        L_0x0004:
            if (r6 != 0) goto L_0x000c
            java.lang.Class<com.bumptech.glide.load.resource.gif.GifDrawable> r1 = com.bumptech.glide.load.resource.gif.GifDrawable.class
            com.bumptech.glide.load.resource.transcode.ResourceTranscoder r6 = r3.buildTranscoder(r1, r5)
        L_0x000c:
            java.lang.Class<java.io.InputStream> r1 = java.io.InputStream.class
            java.lang.Class<com.bumptech.glide.load.resource.gif.GifDrawable> r2 = com.bumptech.glide.load.resource.gif.GifDrawable.class
            com.bumptech.glide.provider.DataLoadProvider r0 = r3.buildDataProvider(r1, r2)
            com.bumptech.glide.provider.FixedLoadProvider r1 = new com.bumptech.glide.provider.FixedLoadProvider
            r1.<init>(r4, r6, r0)
            goto L_0x0003
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.GifTypeRequest.buildProvider(com.bumptech.glide.Glide, com.bumptech.glide.load.model.ModelLoader, java.lang.Class, com.bumptech.glide.load.resource.transcode.ResourceTranscoder):com.bumptech.glide.provider.FixedLoadProvider");
    }

    GifTypeRequest(GenericRequestBuilder<ModelType, ?, ?, ?> other, ModelLoader<ModelType, InputStream> streamModelLoader2, RequestManager.OptionsApplier optionsApplier2) {
        super(buildProvider(other.glide, streamModelLoader2, GifDrawable.class, (ResourceTranscoder) null), GifDrawable.class, other);
        this.streamModelLoader = streamModelLoader2;
        this.optionsApplier = optionsApplier2;
        crossFade();
    }

    public <R> GenericRequestBuilder<ModelType, InputStream, GifDrawable, R> transcode(ResourceTranscoder<GifDrawable, R> transcoder, Class<R> transcodeClass) {
        return this.optionsApplier.apply(new GenericRequestBuilder(buildProvider(this.glide, this.streamModelLoader, transcodeClass, transcoder), transcodeClass, this));
    }

    public GenericRequestBuilder<ModelType, InputStream, GifDrawable, byte[]> toBytes() {
        return transcode(new GifDrawableBytesTranscoder(), byte[].class);
    }
}
