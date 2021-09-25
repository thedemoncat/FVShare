package com.bumptech.glide;

import android.graphics.Bitmap;
import android.os.ParcelFileDescriptor;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.resource.transcode.BitmapBytesTranscoder;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import java.io.InputStream;

public class BitmapTypeRequest<ModelType> extends BitmapRequestBuilder<ModelType, Bitmap> {
    private final ModelLoader<ModelType, ParcelFileDescriptor> fileDescriptorModelLoader;
    private final Glide glide;
    private final RequestManager.OptionsApplier optionsApplier;
    private final ModelLoader<ModelType, InputStream> streamModelLoader;

    /* JADX WARNING: type inference failed for: r7v0, types: [java.lang.Class, java.lang.Class<R>] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static <A, R> com.bumptech.glide.provider.FixedLoadProvider<A, com.bumptech.glide.load.model.ImageVideoWrapper, android.graphics.Bitmap, R> buildProvider(com.bumptech.glide.Glide r4, com.bumptech.glide.load.model.ModelLoader<A, java.io.InputStream> r5, com.bumptech.glide.load.model.ModelLoader<A, android.os.ParcelFileDescriptor> r6, java.lang.Class<R> r7, com.bumptech.glide.load.resource.transcode.ResourceTranscoder<android.graphics.Bitmap, R> r8) {
        /*
            if (r5 != 0) goto L_0x0006
            if (r6 != 0) goto L_0x0006
            r2 = 0
        L_0x0005:
            return r2
        L_0x0006:
            if (r8 != 0) goto L_0x000e
            java.lang.Class<android.graphics.Bitmap> r2 = android.graphics.Bitmap.class
            com.bumptech.glide.load.resource.transcode.ResourceTranscoder r8 = r4.buildTranscoder(r2, r7)
        L_0x000e:
            java.lang.Class<com.bumptech.glide.load.model.ImageVideoWrapper> r2 = com.bumptech.glide.load.model.ImageVideoWrapper.class
            java.lang.Class<android.graphics.Bitmap> r3 = android.graphics.Bitmap.class
            com.bumptech.glide.provider.DataLoadProvider r0 = r4.buildDataProvider(r2, r3)
            com.bumptech.glide.load.model.ImageVideoModelLoader r1 = new com.bumptech.glide.load.model.ImageVideoModelLoader
            r1.<init>(r5, r6)
            com.bumptech.glide.provider.FixedLoadProvider r2 = new com.bumptech.glide.provider.FixedLoadProvider
            r2.<init>(r1, r8, r0)
            goto L_0x0005
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.BitmapTypeRequest.buildProvider(com.bumptech.glide.Glide, com.bumptech.glide.load.model.ModelLoader, com.bumptech.glide.load.model.ModelLoader, java.lang.Class, com.bumptech.glide.load.resource.transcode.ResourceTranscoder):com.bumptech.glide.provider.FixedLoadProvider");
    }

    BitmapTypeRequest(GenericRequestBuilder<ModelType, ?, ?, ?> other, ModelLoader<ModelType, InputStream> streamModelLoader2, ModelLoader<ModelType, ParcelFileDescriptor> fileDescriptorModelLoader2, RequestManager.OptionsApplier optionsApplier2) {
        super(buildProvider(other.glide, streamModelLoader2, fileDescriptorModelLoader2, Bitmap.class, (ResourceTranscoder) null), Bitmap.class, other);
        this.streamModelLoader = streamModelLoader2;
        this.fileDescriptorModelLoader = fileDescriptorModelLoader2;
        this.glide = other.glide;
        this.optionsApplier = optionsApplier2;
    }

    public <R> BitmapRequestBuilder<ModelType, R> transcode(ResourceTranscoder<Bitmap, R> transcoder, Class<R> transcodeClass) {
        return (BitmapRequestBuilder) this.optionsApplier.apply(new BitmapRequestBuilder(buildProvider(this.glide, this.streamModelLoader, this.fileDescriptorModelLoader, transcodeClass, transcoder), transcodeClass, this));
    }

    public BitmapRequestBuilder<ModelType, byte[]> toBytes() {
        return transcode(new BitmapBytesTranscoder(), byte[].class);
    }

    public BitmapRequestBuilder<ModelType, byte[]> toBytes(Bitmap.CompressFormat compressFormat, int quality) {
        return transcode(new BitmapBytesTranscoder(compressFormat, quality), byte[].class);
    }
}
