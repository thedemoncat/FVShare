package com.bumptech.glide;

import android.os.ParcelFileDescriptor;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.model.ImageVideoModelLoader;
import com.bumptech.glide.load.model.ImageVideoWrapper;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.provider.FixedLoadProvider;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;
import java.io.File;
import java.io.InputStream;

public class DrawableTypeRequest<ModelType> extends DrawableRequestBuilder<ModelType> implements DownloadOptions {
    private final ModelLoader<ModelType, ParcelFileDescriptor> fileDescriptorModelLoader;
    private final RequestManager.OptionsApplier optionsApplier;
    private final ModelLoader<ModelType, InputStream> streamModelLoader;

    private static <A, Z, R> FixedLoadProvider<A, ImageVideoWrapper, Z, R> buildProvider(Glide glide, ModelLoader<A, InputStream> streamModelLoader2, ModelLoader<A, ParcelFileDescriptor> fileDescriptorModelLoader2, Class<Z> resourceClass, Class<R> transcodedClass, ResourceTranscoder<Z, R> transcoder) {
        if (streamModelLoader2 == null && fileDescriptorModelLoader2 == null) {
            return null;
        }
        if (transcoder == null) {
            transcoder = glide.buildTranscoder(resourceClass, transcodedClass);
        }
        return new FixedLoadProvider<>(new ImageVideoModelLoader<>(streamModelLoader2, fileDescriptorModelLoader2), transcoder, glide.buildDataProvider(ImageVideoWrapper.class, resourceClass));
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    DrawableTypeRequest(java.lang.Class<ModelType> r8, com.bumptech.glide.load.model.ModelLoader<ModelType, java.io.InputStream> r9, com.bumptech.glide.load.model.ModelLoader<ModelType, android.os.ParcelFileDescriptor> r10, android.content.Context r11, com.bumptech.glide.Glide r12, com.bumptech.glide.manager.RequestTracker r13, com.bumptech.glide.manager.Lifecycle r14, com.bumptech.glide.RequestManager.OptionsApplier r15) {
        /*
            r7 = this;
            java.lang.Class<com.bumptech.glide.load.resource.gifbitmap.GifBitmapWrapper> r3 = com.bumptech.glide.load.resource.gifbitmap.GifBitmapWrapper.class
            java.lang.Class<com.bumptech.glide.load.resource.drawable.GlideDrawable> r4 = com.bumptech.glide.load.resource.drawable.GlideDrawable.class
            r5 = 0
            r0 = r12
            r1 = r9
            r2 = r10
            com.bumptech.glide.provider.FixedLoadProvider r3 = buildProvider(r0, r1, r2, r3, r4, r5)
            r0 = r7
            r1 = r11
            r2 = r8
            r4 = r12
            r5 = r13
            r6 = r14
            r0.<init>(r1, r2, r3, r4, r5, r6)
            r7.streamModelLoader = r9
            r7.fileDescriptorModelLoader = r10
            r7.optionsApplier = r15
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.DrawableTypeRequest.<init>(java.lang.Class, com.bumptech.glide.load.model.ModelLoader, com.bumptech.glide.load.model.ModelLoader, android.content.Context, com.bumptech.glide.Glide, com.bumptech.glide.manager.RequestTracker, com.bumptech.glide.manager.Lifecycle, com.bumptech.glide.RequestManager$OptionsApplier):void");
    }

    public BitmapTypeRequest<ModelType> asBitmap() {
        return (BitmapTypeRequest) this.optionsApplier.apply(new BitmapTypeRequest(this, this.streamModelLoader, this.fileDescriptorModelLoader, this.optionsApplier));
    }

    public GifTypeRequest<ModelType> asGif() {
        return (GifTypeRequest) this.optionsApplier.apply(new GifTypeRequest(this, this.streamModelLoader, this.optionsApplier));
    }

    public <Y extends Target<File>> Y downloadOnly(Y target) {
        return getDownloadOnlyRequest().downloadOnly(target);
    }

    public FutureTarget<File> downloadOnly(int width, int height) {
        return getDownloadOnlyRequest().downloadOnly(width, height);
    }

    private GenericTranscodeRequest<ModelType, InputStream, File> getDownloadOnlyRequest() {
        return (GenericTranscodeRequest) this.optionsApplier.apply(new GenericTranscodeRequest(File.class, this, this.streamModelLoader, InputStream.class, File.class, this.optionsApplier));
    }
}
