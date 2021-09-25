package com.bumptech.glide;

import android.content.Context;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.load.resource.transcode.UnitTranscoder;
import com.bumptech.glide.manager.Lifecycle;
import com.bumptech.glide.manager.RequestTracker;
import com.bumptech.glide.provider.FixedLoadProvider;
import com.bumptech.glide.provider.LoadProvider;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;
import java.io.File;

public class GenericTranscodeRequest<ModelType, DataType, ResourceType> extends GenericRequestBuilder<ModelType, DataType, ResourceType, ResourceType> implements DownloadOptions {
    private final Class<DataType> dataClass;
    private final ModelLoader<ModelType, DataType> modelLoader;
    private final RequestManager.OptionsApplier optionsApplier;
    private final Class<ResourceType> resourceClass;

    private static <A, T, Z, R> LoadProvider<A, T, Z, R> build(Glide glide, ModelLoader<A, T> modelLoader2, Class<T> dataClass2, Class<Z> resourceClass2, ResourceTranscoder<Z, R> transcoder) {
        return new FixedLoadProvider(modelLoader2, transcoder, glide.buildDataProvider(dataClass2, resourceClass2));
    }

    GenericTranscodeRequest(Class<ResourceType> transcodeClass, GenericRequestBuilder<ModelType, ?, ?, ?> other, ModelLoader<ModelType, DataType> modelLoader2, Class<DataType> dataClass2, Class<ResourceType> resourceClass2, RequestManager.OptionsApplier optionsApplier2) {
        super(build(other.glide, modelLoader2, dataClass2, resourceClass2, UnitTranscoder.get()), transcodeClass, other);
        this.modelLoader = modelLoader2;
        this.dataClass = dataClass2;
        this.resourceClass = resourceClass2;
        this.optionsApplier = optionsApplier2;
    }

    GenericTranscodeRequest(Context context, Glide glide, Class<ModelType> modelClass, ModelLoader<ModelType, DataType> modelLoader2, Class<DataType> dataClass2, Class<ResourceType> resourceClass2, RequestTracker requestTracker, Lifecycle lifecycle, RequestManager.OptionsApplier optionsApplier2) {
        super(context, modelClass, build(glide, modelLoader2, dataClass2, resourceClass2, UnitTranscoder.get()), resourceClass2, glide, requestTracker, lifecycle);
        this.modelLoader = modelLoader2;
        this.dataClass = dataClass2;
        this.resourceClass = resourceClass2;
        this.optionsApplier = optionsApplier2;
    }

    public <TranscodeType> GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> transcode(ResourceTranscoder<ResourceType, TranscodeType> transcoder, Class<TranscodeType> transcodeClass) {
        return this.optionsApplier.apply(new GenericRequestBuilder(build(this.glide, this.modelLoader, this.dataClass, this.resourceClass, transcoder), transcodeClass, this));
    }

    public <Y extends Target<File>> Y downloadOnly(Y target) {
        return getDownloadOnlyRequest().into(target);
    }

    public FutureTarget<File> downloadOnly(int width, int height) {
        return getDownloadOnlyRequest().into(width, height);
    }

    private GenericRequestBuilder<ModelType, DataType, File, File> getDownloadOnlyRequest() {
        return this.optionsApplier.apply(new GenericRequestBuilder(new FixedLoadProvider<>(this.modelLoader, UnitTranscoder.get(), this.glide.buildDataProvider(this.dataClass, File.class)), File.class, this)).priority(Priority.LOW).diskCacheStrategy(DiskCacheStrategy.SOURCE).skipMemoryCache(true);
    }
}
