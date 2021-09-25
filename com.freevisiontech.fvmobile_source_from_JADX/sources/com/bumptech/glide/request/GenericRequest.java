package com.bumptech.glide.request;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.provider.LoadProvider;
import com.bumptech.glide.request.animation.GlideAnimationFactory;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Util;
import java.util.Queue;

public final class GenericRequest<A, T, Z, R> implements Request, SizeReadyCallback, ResourceCallback {
    private static final Queue<GenericRequest<?, ?, ?, ?>> REQUEST_POOL = Util.createQueue(0);
    private static final String TAG = "GenericRequest";
    private static final double TO_MEGABYTE = 9.5367431640625E-7d;
    private GlideAnimationFactory<R> animationFactory;
    private Context context;
    private DiskCacheStrategy diskCacheStrategy;
    private Engine engine;
    private Drawable errorDrawable;
    private int errorResourceId;
    private Drawable fallbackDrawable;
    private int fallbackResourceId;
    private boolean isMemoryCacheable;
    private LoadProvider<A, T, Z, R> loadProvider;
    private Engine.LoadStatus loadStatus;
    private boolean loadedFromMemoryCache;
    private A model;
    private int overrideHeight;
    private int overrideWidth;
    private Drawable placeholderDrawable;
    private int placeholderResourceId;
    private Priority priority;
    private RequestCoordinator requestCoordinator;
    private RequestListener<? super A, R> requestListener;
    private Resource<?> resource;
    private Key signature;
    private float sizeMultiplier;
    private long startTime;
    private Status status;
    private final String tag = String.valueOf(hashCode());
    private Target<R> target;
    private Class<R> transcodeClass;
    private Transformation<Z> transformation;

    private enum Status {
        PENDING,
        RUNNING,
        WAITING_FOR_SIZE,
        COMPLETE,
        FAILED,
        CANCELLED,
        CLEARED,
        PAUSED
    }

    public static <A, T, Z, R> GenericRequest<A, T, Z, R> obtain(LoadProvider<A, T, Z, R> loadProvider2, A model2, Key signature2, Context context2, Priority priority2, Target<R> target2, float sizeMultiplier2, Drawable placeholderDrawable2, int placeholderResourceId2, Drawable errorDrawable2, int errorResourceId2, Drawable fallbackDrawable2, int fallbackResourceId2, RequestListener<? super A, R> requestListener2, RequestCoordinator requestCoordinator2, Engine engine2, Transformation<Z> transformation2, Class<R> transcodeClass2, boolean isMemoryCacheable2, GlideAnimationFactory<R> animationFactory2, int overrideWidth2, int overrideHeight2, DiskCacheStrategy diskCacheStrategy2) {
        GenericRequest<A, T, Z, R> request = REQUEST_POOL.poll();
        if (request == null) {
            request = new GenericRequest<>();
        }
        request.init(loadProvider2, model2, signature2, context2, priority2, target2, sizeMultiplier2, placeholderDrawable2, placeholderResourceId2, errorDrawable2, errorResourceId2, fallbackDrawable2, fallbackResourceId2, requestListener2, requestCoordinator2, engine2, transformation2, transcodeClass2, isMemoryCacheable2, animationFactory2, overrideWidth2, overrideHeight2, diskCacheStrategy2);
        return request;
    }

    private GenericRequest() {
    }

    public void recycle() {
        this.loadProvider = null;
        this.model = null;
        this.context = null;
        this.target = null;
        this.placeholderDrawable = null;
        this.errorDrawable = null;
        this.fallbackDrawable = null;
        this.requestListener = null;
        this.requestCoordinator = null;
        this.transformation = null;
        this.animationFactory = null;
        this.loadedFromMemoryCache = false;
        this.loadStatus = null;
        REQUEST_POOL.offer(this);
    }

    private void init(LoadProvider<A, T, Z, R> loadProvider2, A model2, Key signature2, Context context2, Priority priority2, Target<R> target2, float sizeMultiplier2, Drawable placeholderDrawable2, int placeholderResourceId2, Drawable errorDrawable2, int errorResourceId2, Drawable fallbackDrawable2, int fallbackResourceId2, RequestListener<? super A, R> requestListener2, RequestCoordinator requestCoordinator2, Engine engine2, Transformation<Z> transformation2, Class<R> transcodeClass2, boolean isMemoryCacheable2, GlideAnimationFactory<R> animationFactory2, int overrideWidth2, int overrideHeight2, DiskCacheStrategy diskCacheStrategy2) {
        this.loadProvider = loadProvider2;
        this.model = model2;
        this.signature = signature2;
        this.fallbackDrawable = fallbackDrawable2;
        this.fallbackResourceId = fallbackResourceId2;
        this.context = context2.getApplicationContext();
        this.priority = priority2;
        this.target = target2;
        this.sizeMultiplier = sizeMultiplier2;
        this.placeholderDrawable = placeholderDrawable2;
        this.placeholderResourceId = placeholderResourceId2;
        this.errorDrawable = errorDrawable2;
        this.errorResourceId = errorResourceId2;
        this.requestListener = requestListener2;
        this.requestCoordinator = requestCoordinator2;
        this.engine = engine2;
        this.transformation = transformation2;
        this.transcodeClass = transcodeClass2;
        this.isMemoryCacheable = isMemoryCacheable2;
        this.animationFactory = animationFactory2;
        this.overrideWidth = overrideWidth2;
        this.overrideHeight = overrideHeight2;
        this.diskCacheStrategy = diskCacheStrategy2;
        this.status = Status.PENDING;
        if (model2 != null) {
            check("ModelLoader", loadProvider2.getModelLoader(), "try .using(ModelLoader)");
            check("Transcoder", loadProvider2.getTranscoder(), "try .as*(Class).transcode(ResourceTranscoder)");
            check("Transformation", transformation2, "try .transform(UnitTransformation.get())");
            if (diskCacheStrategy2.cacheSource()) {
                check("SourceEncoder", loadProvider2.getSourceEncoder(), "try .sourceEncoder(Encoder) or .diskCacheStrategy(NONE/RESULT)");
            } else {
                check("SourceDecoder", loadProvider2.getSourceDecoder(), "try .decoder/.imageDecoder/.videoDecoder(ResourceDecoder) or .diskCacheStrategy(ALL/SOURCE)");
            }
            if (diskCacheStrategy2.cacheSource() || diskCacheStrategy2.cacheResult()) {
                check("CacheDecoder", loadProvider2.getCacheDecoder(), "try .cacheDecoder(ResouceDecoder) or .diskCacheStrategy(NONE)");
            }
            if (diskCacheStrategy2.cacheResult()) {
                check("Encoder", loadProvider2.getEncoder(), "try .encode(ResourceEncoder) or .diskCacheStrategy(NONE/SOURCE)");
            }
        }
    }

    private static void check(String name, Object object, String suggestion) {
        if (object == null) {
            StringBuilder message = new StringBuilder(name);
            message.append(" must not be null");
            if (suggestion != null) {
                message.append(", ");
                message.append(suggestion);
            }
            throw new NullPointerException(message.toString());
        }
    }

    public void begin() {
        this.startTime = LogTime.getLogTime();
        if (this.model == null) {
            onException((Exception) null);
            return;
        }
        this.status = Status.WAITING_FOR_SIZE;
        if (Util.isValidDimensions(this.overrideWidth, this.overrideHeight)) {
            onSizeReady(this.overrideWidth, this.overrideHeight);
        } else {
            this.target.getSize(this);
        }
        if (!isComplete() && !isFailed() && canNotifyStatusChanged()) {
            this.target.onLoadStarted(getPlaceholderDrawable());
        }
        if (Log.isLoggable(TAG, 2)) {
            logV("finished run method in " + LogTime.getElapsedMillis(this.startTime));
        }
    }

    /* access modifiers changed from: package-private */
    public void cancel() {
        this.status = Status.CANCELLED;
        if (this.loadStatus != null) {
            this.loadStatus.cancel();
            this.loadStatus = null;
        }
    }

    public void clear() {
        Util.assertMainThread();
        if (this.status != Status.CLEARED) {
            cancel();
            if (this.resource != null) {
                releaseResource(this.resource);
            }
            if (canNotifyStatusChanged()) {
                this.target.onLoadCleared(getPlaceholderDrawable());
            }
            this.status = Status.CLEARED;
        }
    }

    public boolean isPaused() {
        return this.status == Status.PAUSED;
    }

    public void pause() {
        clear();
        this.status = Status.PAUSED;
    }

    private void releaseResource(Resource resource2) {
        this.engine.release(resource2);
        this.resource = null;
    }

    public boolean isRunning() {
        return this.status == Status.RUNNING || this.status == Status.WAITING_FOR_SIZE;
    }

    public boolean isComplete() {
        return this.status == Status.COMPLETE;
    }

    public boolean isResourceSet() {
        return isComplete();
    }

    public boolean isCancelled() {
        return this.status == Status.CANCELLED || this.status == Status.CLEARED;
    }

    public boolean isFailed() {
        return this.status == Status.FAILED;
    }

    private Drawable getFallbackDrawable() {
        if (this.fallbackDrawable == null && this.fallbackResourceId > 0) {
            this.fallbackDrawable = this.context.getResources().getDrawable(this.fallbackResourceId);
        }
        return this.fallbackDrawable;
    }

    private void setErrorPlaceholder(Exception e) {
        if (canNotifyStatusChanged()) {
            Drawable error = this.model == null ? getFallbackDrawable() : null;
            if (error == null) {
                error = getErrorDrawable();
            }
            if (error == null) {
                error = getPlaceholderDrawable();
            }
            this.target.onLoadFailed(e, error);
        }
    }

    private Drawable getErrorDrawable() {
        if (this.errorDrawable == null && this.errorResourceId > 0) {
            this.errorDrawable = this.context.getResources().getDrawable(this.errorResourceId);
        }
        return this.errorDrawable;
    }

    private Drawable getPlaceholderDrawable() {
        if (this.placeholderDrawable == null && this.placeholderResourceId > 0) {
            this.placeholderDrawable = this.context.getResources().getDrawable(this.placeholderResourceId);
        }
        return this.placeholderDrawable;
    }

    public void onSizeReady(int width, int height) {
        if (Log.isLoggable(TAG, 2)) {
            logV("Got onSizeReady in " + LogTime.getElapsedMillis(this.startTime));
        }
        if (this.status == Status.WAITING_FOR_SIZE) {
            this.status = Status.RUNNING;
            int width2 = Math.round(this.sizeMultiplier * ((float) width));
            int height2 = Math.round(this.sizeMultiplier * ((float) height));
            DataFetcher<T> dataFetcher = this.loadProvider.getModelLoader().getResourceFetcher(this.model, width2, height2);
            if (dataFetcher == null) {
                onException(new Exception("Failed to load model: '" + this.model + "'"));
                return;
            }
            ResourceTranscoder<Z, R> transcoder = this.loadProvider.getTranscoder();
            if (Log.isLoggable(TAG, 2)) {
                logV("finished setup for calling load in " + LogTime.getElapsedMillis(this.startTime));
            }
            this.loadedFromMemoryCache = true;
            this.loadStatus = this.engine.load(this.signature, width2, height2, dataFetcher, this.loadProvider, this.transformation, transcoder, this.priority, this.isMemoryCacheable, this.diskCacheStrategy, this);
            this.loadedFromMemoryCache = this.resource != null;
            if (Log.isLoggable(TAG, 2)) {
                logV("finished onSizeReady in " + LogTime.getElapsedMillis(this.startTime));
            }
        }
    }

    private boolean canSetResource() {
        return this.requestCoordinator == null || this.requestCoordinator.canSetImage(this);
    }

    private boolean canNotifyStatusChanged() {
        return this.requestCoordinator == null || this.requestCoordinator.canNotifyStatusChanged(this);
    }

    private boolean isFirstReadyResource() {
        return this.requestCoordinator == null || !this.requestCoordinator.isAnyResourceSet();
    }

    private void notifyLoadSuccess() {
        if (this.requestCoordinator != null) {
            this.requestCoordinator.onRequestSuccess(this);
        }
    }

    public void onResourceReady(Resource<?> resource2) {
        if (resource2 == null) {
            onException(new Exception("Expected to receive a Resource<R> with an object of " + this.transcodeClass + " inside, but instead got null."));
            return;
        }
        Object received = resource2.get();
        if (received == null || !this.transcodeClass.isAssignableFrom(received.getClass())) {
            releaseResource(resource2);
            onException(new Exception("Expected to receive an object of " + this.transcodeClass + " but instead got " + (received != null ? received.getClass() : "") + "{" + received + "}" + " inside Resource{" + resource2 + "}." + (received != null ? "" : " To indicate failure return a null Resource object, rather than a Resource object containing null data.")));
        } else if (!canSetResource()) {
            releaseResource(resource2);
            this.status = Status.COMPLETE;
        } else {
            onResourceReady(resource2, received);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x001b, code lost:
        if (r10.requestListener.onResourceReady(r12, r10.model, r10.target, r10.loadedFromMemoryCache, r5) == false) goto L_0x001d;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void onResourceReady(com.bumptech.glide.load.engine.Resource<?> r11, R r12) {
        /*
            r10 = this;
            boolean r5 = r10.isFirstReadyResource()
            com.bumptech.glide.request.GenericRequest$Status r0 = com.bumptech.glide.request.GenericRequest.Status.COMPLETE
            r10.status = r0
            r10.resource = r11
            com.bumptech.glide.request.RequestListener<? super A, R> r0 = r10.requestListener
            if (r0 == 0) goto L_0x001d
            com.bumptech.glide.request.RequestListener<? super A, R> r0 = r10.requestListener
            A r2 = r10.model
            com.bumptech.glide.request.target.Target<R> r3 = r10.target
            boolean r4 = r10.loadedFromMemoryCache
            r1 = r12
            boolean r0 = r0.onResourceReady(r1, r2, r3, r4, r5)
            if (r0 != 0) goto L_0x002a
        L_0x001d:
            com.bumptech.glide.request.animation.GlideAnimationFactory<R> r0 = r10.animationFactory
            boolean r1 = r10.loadedFromMemoryCache
            com.bumptech.glide.request.animation.GlideAnimation r6 = r0.build(r1, r5)
            com.bumptech.glide.request.target.Target<R> r0 = r10.target
            r0.onResourceReady(r12, r6)
        L_0x002a:
            r10.notifyLoadSuccess()
            java.lang.String r0 = "GenericRequest"
            r1 = 2
            boolean r0 = android.util.Log.isLoggable(r0, r1)
            if (r0 == 0) goto L_0x0074
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Resource ready in "
            java.lang.StringBuilder r0 = r0.append(r1)
            long r2 = r10.startTime
            double r2 = com.bumptech.glide.util.LogTime.getElapsedMillis(r2)
            java.lang.StringBuilder r0 = r0.append(r2)
            java.lang.String r1 = " size: "
            java.lang.StringBuilder r0 = r0.append(r1)
            int r1 = r11.getSize()
            double r2 = (double) r1
            r8 = 4517110426252607488(0x3eb0000000000000, double:9.5367431640625E-7)
            double r2 = r2 * r8
            java.lang.StringBuilder r0 = r0.append(r2)
            java.lang.String r1 = " fromCache: "
            java.lang.StringBuilder r0 = r0.append(r1)
            boolean r1 = r10.loadedFromMemoryCache
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = r0.toString()
            r10.logV(r0)
        L_0x0074:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.request.GenericRequest.onResourceReady(com.bumptech.glide.load.engine.Resource, java.lang.Object):void");
    }

    public void onException(Exception e) {
        if (Log.isLoggable(TAG, 3)) {
            Log.d(TAG, "load failed", e);
        }
        this.status = Status.FAILED;
        if (this.requestListener == null || !this.requestListener.onException(e, this.model, this.target, isFirstReadyResource())) {
            setErrorPlaceholder(e);
        }
    }

    private void logV(String message) {
        Log.v(TAG, message + " this: " + this.tag);
    }
}
