package com.bumptech.glide;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.file_descriptor.FileDescriptorModelLoader;
import com.bumptech.glide.load.model.stream.MediaStoreStreamLoader;
import com.bumptech.glide.load.model.stream.StreamByteArrayLoader;
import com.bumptech.glide.load.model.stream.StreamModelLoader;
import com.bumptech.glide.manager.ConnectivityMonitor;
import com.bumptech.glide.manager.ConnectivityMonitorFactory;
import com.bumptech.glide.manager.Lifecycle;
import com.bumptech.glide.manager.LifecycleListener;
import com.bumptech.glide.manager.RequestManagerTreeNode;
import com.bumptech.glide.manager.RequestTracker;
import com.bumptech.glide.signature.ApplicationVersionSignature;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.bumptech.glide.signature.StringSignature;
import com.bumptech.glide.util.Util;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

public class RequestManager implements LifecycleListener {
    /* access modifiers changed from: private */
    public final Context context;
    /* access modifiers changed from: private */
    public final Glide glide;
    /* access modifiers changed from: private */
    public final Lifecycle lifecycle;
    /* access modifiers changed from: private */
    public DefaultOptions options;
    /* access modifiers changed from: private */
    public final OptionsApplier optionsApplier;
    /* access modifiers changed from: private */
    public final RequestTracker requestTracker;
    private final RequestManagerTreeNode treeNode;

    public interface DefaultOptions {
        <T> void apply(GenericRequestBuilder<T, ?, ?, ?> genericRequestBuilder);
    }

    public RequestManager(Context context2, Lifecycle lifecycle2, RequestManagerTreeNode treeNode2) {
        this(context2, lifecycle2, treeNode2, new RequestTracker(), new ConnectivityMonitorFactory());
    }

    RequestManager(Context context2, final Lifecycle lifecycle2, RequestManagerTreeNode treeNode2, RequestTracker requestTracker2, ConnectivityMonitorFactory factory) {
        this.context = context2.getApplicationContext();
        this.lifecycle = lifecycle2;
        this.treeNode = treeNode2;
        this.requestTracker = requestTracker2;
        this.glide = Glide.get(context2);
        this.optionsApplier = new OptionsApplier();
        ConnectivityMonitor connectivityMonitor = factory.build(context2, new RequestManagerConnectivityListener(requestTracker2));
        if (Util.isOnBackgroundThread()) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    lifecycle2.addListener(RequestManager.this);
                }
            });
        } else {
            lifecycle2.addListener(this);
        }
        lifecycle2.addListener(connectivityMonitor);
    }

    public void onTrimMemory(int level) {
        this.glide.trimMemory(level);
    }

    public void onLowMemory() {
        this.glide.clearMemory();
    }

    public void setDefaultOptions(DefaultOptions options2) {
        this.options = options2;
    }

    public boolean isPaused() {
        Util.assertMainThread();
        return this.requestTracker.isPaused();
    }

    public void pauseRequests() {
        Util.assertMainThread();
        this.requestTracker.pauseRequests();
    }

    public void pauseRequestsRecursive() {
        Util.assertMainThread();
        pauseRequests();
        for (RequestManager requestManager : this.treeNode.getDescendants()) {
            requestManager.pauseRequests();
        }
    }

    public void resumeRequests() {
        Util.assertMainThread();
        this.requestTracker.resumeRequests();
    }

    public void resumeRequestsRecursive() {
        Util.assertMainThread();
        resumeRequests();
        for (RequestManager requestManager : this.treeNode.getDescendants()) {
            requestManager.resumeRequests();
        }
    }

    public void onStart() {
        resumeRequests();
    }

    public void onStop() {
        pauseRequests();
    }

    public void onDestroy() {
        this.requestTracker.clearRequests();
    }

    public <A, T> GenericModelRequest<A, T> using(ModelLoader<A, T> modelLoader, Class<T> dataClass) {
        return new GenericModelRequest<>(modelLoader, dataClass);
    }

    public <T> ImageModelRequest<T> using(StreamModelLoader<T> modelLoader) {
        return new ImageModelRequest<>(modelLoader);
    }

    public ImageModelRequest<byte[]> using(StreamByteArrayLoader modelLoader) {
        return new ImageModelRequest<>(modelLoader);
    }

    public <T> VideoModelRequest<T> using(FileDescriptorModelLoader<T> modelLoader) {
        return new VideoModelRequest<>(modelLoader);
    }

    public DrawableTypeRequest<String> load(String string) {
        return (DrawableTypeRequest) fromString().load(string);
    }

    public DrawableTypeRequest<String> fromString() {
        return loadGeneric(String.class);
    }

    public DrawableTypeRequest<Uri> load(Uri uri) {
        return (DrawableTypeRequest) fromUri().load(uri);
    }

    public DrawableTypeRequest<Uri> fromUri() {
        return loadGeneric(Uri.class);
    }

    @Deprecated
    public DrawableTypeRequest<Uri> loadFromMediaStore(Uri uri, String mimeType, long dateModified, int orientation) {
        return (DrawableTypeRequest) loadFromMediaStore(uri).signature(new MediaStoreSignature(mimeType, dateModified, orientation));
    }

    public DrawableTypeRequest<Uri> loadFromMediaStore(Uri uri) {
        return (DrawableTypeRequest) fromMediaStore().load(uri);
    }

    public DrawableTypeRequest<Uri> fromMediaStore() {
        return (DrawableTypeRequest) this.optionsApplier.apply(new DrawableTypeRequest(Uri.class, new MediaStoreStreamLoader(this.context, Glide.buildStreamModelLoader(Uri.class, this.context)), Glide.buildFileDescriptorModelLoader(Uri.class, this.context), this.context, this.glide, this.requestTracker, this.lifecycle, this.optionsApplier));
    }

    public DrawableTypeRequest<File> load(File file) {
        return (DrawableTypeRequest) fromFile().load(file);
    }

    public DrawableTypeRequest<File> fromFile() {
        return loadGeneric(File.class);
    }

    public DrawableTypeRequest<Integer> load(Integer resourceId) {
        return (DrawableTypeRequest) fromResource().load(resourceId);
    }

    public DrawableTypeRequest<Integer> fromResource() {
        return (DrawableTypeRequest) loadGeneric(Integer.class).signature(ApplicationVersionSignature.obtain(this.context));
    }

    @Deprecated
    public DrawableTypeRequest<URL> load(URL url) {
        return (DrawableTypeRequest) fromUrl().load(url);
    }

    @Deprecated
    public DrawableTypeRequest<URL> fromUrl() {
        return loadGeneric(URL.class);
    }

    @Deprecated
    public DrawableTypeRequest<byte[]> load(byte[] model, String id) {
        return (DrawableTypeRequest) load(model).signature((Key) new StringSignature(id));
    }

    public DrawableTypeRequest<byte[]> load(byte[] model) {
        return (DrawableTypeRequest) fromBytes().load(model);
    }

    public DrawableTypeRequest<byte[]> fromBytes() {
        return (DrawableTypeRequest) loadGeneric(byte[].class).signature((Key) new StringSignature(UUID.randomUUID().toString())).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true);
    }

    public <T> DrawableTypeRequest<T> load(T model) {
        return (DrawableTypeRequest) loadGeneric(getSafeClass(model)).load(model);
    }

    public <T> DrawableTypeRequest<T> from(Class<T> modelClass) {
        return loadGeneric(modelClass);
    }

    private <T> DrawableTypeRequest<T> loadGeneric(Class<T> modelClass) {
        ModelLoader<T, InputStream> streamModelLoader = Glide.buildStreamModelLoader(modelClass, this.context);
        ModelLoader<T, ParcelFileDescriptor> fileDescriptorModelLoader = Glide.buildFileDescriptorModelLoader(modelClass, this.context);
        if (modelClass != null && streamModelLoader == null && fileDescriptorModelLoader == null) {
            throw new IllegalArgumentException("Unknown type " + modelClass + ". You must provide a Model of a type for" + " which there is a registered ModelLoader, if you are using a custom model, you must first call" + " Glide#register with a ModelLoaderFactory for your custom model class");
        }
        return (DrawableTypeRequest) this.optionsApplier.apply(new DrawableTypeRequest(modelClass, streamModelLoader, fileDescriptorModelLoader, this.context, this.glide, this.requestTracker, this.lifecycle, this.optionsApplier));
    }

    /* access modifiers changed from: private */
    public static <T> Class<T> getSafeClass(T model) {
        if (model != null) {
            return model.getClass();
        }
        return null;
    }

    public final class VideoModelRequest<T> {
        private final ModelLoader<T, ParcelFileDescriptor> loader;

        VideoModelRequest(ModelLoader<T, ParcelFileDescriptor> loader2) {
            this.loader = loader2;
        }

        public DrawableTypeRequest<T> load(T model) {
            return (DrawableTypeRequest) ((DrawableTypeRequest) RequestManager.this.optionsApplier.apply(new DrawableTypeRequest(RequestManager.getSafeClass(model), (ModelLoader) null, this.loader, RequestManager.this.context, RequestManager.this.glide, RequestManager.this.requestTracker, RequestManager.this.lifecycle, RequestManager.this.optionsApplier))).load(model);
        }
    }

    public final class ImageModelRequest<T> {
        private final ModelLoader<T, InputStream> loader;

        ImageModelRequest(ModelLoader<T, InputStream> loader2) {
            this.loader = loader2;
        }

        public DrawableTypeRequest<T> from(Class<T> modelClass) {
            return (DrawableTypeRequest) RequestManager.this.optionsApplier.apply(new DrawableTypeRequest(modelClass, this.loader, (ModelLoader) null, RequestManager.this.context, RequestManager.this.glide, RequestManager.this.requestTracker, RequestManager.this.lifecycle, RequestManager.this.optionsApplier));
        }

        public DrawableTypeRequest<T> load(T model) {
            return (DrawableTypeRequest) from(RequestManager.getSafeClass(model)).load(model);
        }
    }

    public final class GenericModelRequest<A, T> {
        /* access modifiers changed from: private */
        public final Class<T> dataClass;
        /* access modifiers changed from: private */
        public final ModelLoader<A, T> modelLoader;

        GenericModelRequest(ModelLoader<A, T> modelLoader2, Class<T> dataClass2) {
            this.modelLoader = modelLoader2;
            this.dataClass = dataClass2;
        }

        public GenericModelRequest<A, T>.GenericTypeRequest from(Class<A> modelClass) {
            return new GenericTypeRequest(modelClass);
        }

        public GenericModelRequest<A, T>.GenericTypeRequest load(A model) {
            return new GenericTypeRequest(model);
        }

        public final class GenericTypeRequest {
            private final A model;
            private final Class<A> modelClass;
            private final boolean providedModel;

            GenericTypeRequest(A model2) {
                this.providedModel = true;
                this.model = model2;
                this.modelClass = RequestManager.getSafeClass(model2);
            }

            GenericTypeRequest(Class<A> modelClass2) {
                this.providedModel = false;
                this.model = null;
                this.modelClass = modelClass2;
            }

            /* renamed from: as */
            public <Z> GenericTranscodeRequest<A, T, Z> mo18167as(Class<Z> resourceClass) {
                GenericTranscodeRequest<A, T, Z> result = (GenericTranscodeRequest) RequestManager.this.optionsApplier.apply(new GenericTranscodeRequest(RequestManager.this.context, RequestManager.this.glide, this.modelClass, GenericModelRequest.this.modelLoader, GenericModelRequest.this.dataClass, resourceClass, RequestManager.this.requestTracker, RequestManager.this.lifecycle, RequestManager.this.optionsApplier));
                if (this.providedModel) {
                    result.load(this.model);
                }
                return result;
            }
        }
    }

    class OptionsApplier {
        OptionsApplier() {
        }

        public <A, X extends GenericRequestBuilder<A, ?, ?, ?>> X apply(X builder) {
            if (RequestManager.this.options != null) {
                RequestManager.this.options.apply(builder);
            }
            return builder;
        }
    }

    private static class RequestManagerConnectivityListener implements ConnectivityMonitor.ConnectivityListener {
        private final RequestTracker requestTracker;

        public RequestManagerConnectivityListener(RequestTracker requestTracker2) {
            this.requestTracker = requestTracker2;
        }

        public void onConnectivityChanged(boolean isConnected) {
            if (isConnected) {
                this.requestTracker.restartRequests();
            }
        }
    }
}
