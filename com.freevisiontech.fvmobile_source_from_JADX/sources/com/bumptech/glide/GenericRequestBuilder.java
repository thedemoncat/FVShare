package com.bumptech.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import android.widget.ImageView;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.UnitTransformation;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.manager.Lifecycle;
import com.bumptech.glide.manager.RequestTracker;
import com.bumptech.glide.provider.ChildLoadProvider;
import com.bumptech.glide.provider.LoadProvider;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.GenericRequest;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestCoordinator;
import com.bumptech.glide.request.RequestFutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.ThumbnailRequestCoordinator;
import com.bumptech.glide.request.animation.GlideAnimationFactory;
import com.bumptech.glide.request.animation.NoAnimation;
import com.bumptech.glide.request.animation.ViewAnimationFactory;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;
import com.bumptech.glide.request.animation.ViewPropertyAnimationFactory;
import com.bumptech.glide.request.target.PreloadTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.EmptySignature;
import com.bumptech.glide.util.Util;
import java.io.File;

public class GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> implements Cloneable {
    private GlideAnimationFactory<TranscodeType> animationFactory;
    protected final Context context;
    private DiskCacheStrategy diskCacheStrategy;
    private int errorId;
    private Drawable errorPlaceholder;
    private Drawable fallbackDrawable;
    private int fallbackResource;
    protected final Glide glide;
    private boolean isCacheable;
    private boolean isModelSet;
    private boolean isThumbnailBuilt;
    private boolean isTransformationSet;
    protected final Lifecycle lifecycle;
    private ChildLoadProvider<ModelType, DataType, ResourceType, TranscodeType> loadProvider;
    private ModelType model;
    protected final Class<ModelType> modelClass;
    private int overrideHeight;
    private int overrideWidth;
    private Drawable placeholderDrawable;
    private int placeholderId;
    private Priority priority;
    private RequestListener<? super ModelType, TranscodeType> requestListener;
    protected final RequestTracker requestTracker;
    private Key signature;
    private Float sizeMultiplier;
    private Float thumbSizeMultiplier;
    private GenericRequestBuilder<?, ?, ?, TranscodeType> thumbnailRequestBuilder;
    protected final Class<TranscodeType> transcodeClass;
    private Transformation<ResourceType> transformation;

    GenericRequestBuilder(LoadProvider<ModelType, DataType, ResourceType, TranscodeType> loadProvider2, Class<TranscodeType> transcodeClass2, GenericRequestBuilder<ModelType, ?, ?, ?> other) {
        this(other.context, other.modelClass, loadProvider2, transcodeClass2, other.glide, other.requestTracker, other.lifecycle);
        this.model = other.model;
        this.isModelSet = other.isModelSet;
        this.signature = other.signature;
        this.diskCacheStrategy = other.diskCacheStrategy;
        this.isCacheable = other.isCacheable;
    }

    GenericRequestBuilder(Context context2, Class<ModelType> modelClass2, LoadProvider<ModelType, DataType, ResourceType, TranscodeType> loadProvider2, Class<TranscodeType> transcodeClass2, Glide glide2, RequestTracker requestTracker2, Lifecycle lifecycle2) {
        ChildLoadProvider<ModelType, DataType, ResourceType, TranscodeType> childLoadProvider = null;
        this.signature = EmptySignature.obtain();
        this.sizeMultiplier = Float.valueOf(1.0f);
        this.priority = null;
        this.isCacheable = true;
        this.animationFactory = NoAnimation.getFactory();
        this.overrideHeight = -1;
        this.overrideWidth = -1;
        this.diskCacheStrategy = DiskCacheStrategy.RESULT;
        this.transformation = UnitTransformation.get();
        this.context = context2;
        this.modelClass = modelClass2;
        this.transcodeClass = transcodeClass2;
        this.glide = glide2;
        this.requestTracker = requestTracker2;
        this.lifecycle = lifecycle2;
        this.loadProvider = loadProvider2 != null ? new ChildLoadProvider<>(loadProvider2) : childLoadProvider;
        if (context2 == null) {
            throw new NullPointerException("Context can't be null");
        } else if (modelClass2 != null && loadProvider2 == null) {
            throw new NullPointerException("LoadProvider must not be null");
        }
    }

    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> thumbnail(GenericRequestBuilder<?, ?, ?, TranscodeType> thumbnailRequest) {
        if (equals(thumbnailRequest)) {
            throw new IllegalArgumentException("You cannot set a request as a thumbnail for itself. Consider using clone() on the request you are passing to thumbnail()");
        }
        this.thumbnailRequestBuilder = thumbnailRequest;
        return this;
    }

    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> thumbnail(float sizeMultiplier2) {
        if (sizeMultiplier2 < 0.0f || sizeMultiplier2 > 1.0f) {
            throw new IllegalArgumentException("sizeMultiplier must be between 0 and 1");
        }
        this.thumbSizeMultiplier = Float.valueOf(sizeMultiplier2);
        return this;
    }

    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> sizeMultiplier(float sizeMultiplier2) {
        if (sizeMultiplier2 < 0.0f || sizeMultiplier2 > 1.0f) {
            throw new IllegalArgumentException("sizeMultiplier must be between 0 and 1");
        }
        this.sizeMultiplier = Float.valueOf(sizeMultiplier2);
        return this;
    }

    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> decoder(ResourceDecoder<DataType, ResourceType> decoder) {
        if (this.loadProvider != null) {
            this.loadProvider.setSourceDecoder(decoder);
        }
        return this;
    }

    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> cacheDecoder(ResourceDecoder<File, ResourceType> cacheDecoder) {
        if (this.loadProvider != null) {
            this.loadProvider.setCacheDecoder(cacheDecoder);
        }
        return this;
    }

    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> sourceEncoder(Encoder<DataType> sourceEncoder) {
        if (this.loadProvider != null) {
            this.loadProvider.setSourceEncoder(sourceEncoder);
        }
        return this;
    }

    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> diskCacheStrategy(DiskCacheStrategy strategy) {
        this.diskCacheStrategy = strategy;
        return this;
    }

    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> encoder(ResourceEncoder<ResourceType> encoder) {
        if (this.loadProvider != null) {
            this.loadProvider.setEncoder(encoder);
        }
        return this;
    }

    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> priority(Priority priority2) {
        this.priority = priority2;
        return this;
    }

    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> transform(Transformation<ResourceType>... transformations) {
        this.isTransformationSet = true;
        if (transformations.length == 1) {
            this.transformation = transformations[0];
        } else {
            this.transformation = new MultiTransformation((Transformation<T>[]) transformations);
        }
        return this;
    }

    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> dontTransform() {
        return transform(UnitTransformation.get());
    }

    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> transcoder(ResourceTranscoder<ResourceType, TranscodeType> transcoder) {
        if (this.loadProvider != null) {
            this.loadProvider.setTranscoder(transcoder);
        }
        return this;
    }

    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> dontAnimate() {
        return animate(NoAnimation.getFactory());
    }

    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> animate(int animationId) {
        return animate(new ViewAnimationFactory(this.context, animationId));
    }

    @Deprecated
    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> animate(Animation animation) {
        return animate(new ViewAnimationFactory(animation));
    }

    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> animate(ViewPropertyAnimation.Animator animator) {
        return animate(new ViewPropertyAnimationFactory(animator));
    }

    /* access modifiers changed from: package-private */
    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> animate(GlideAnimationFactory<TranscodeType> animationFactory2) {
        if (animationFactory2 == null) {
            throw new NullPointerException("Animation factory must not be null!");
        }
        this.animationFactory = animationFactory2;
        return this;
    }

    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> placeholder(int resourceId) {
        this.placeholderId = resourceId;
        return this;
    }

    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> placeholder(Drawable drawable) {
        this.placeholderDrawable = drawable;
        return this;
    }

    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> fallback(Drawable drawable) {
        this.fallbackDrawable = drawable;
        return this;
    }

    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> fallback(int resourceId) {
        this.fallbackResource = resourceId;
        return this;
    }

    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> error(int resourceId) {
        this.errorId = resourceId;
        return this;
    }

    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> error(Drawable drawable) {
        this.errorPlaceholder = drawable;
        return this;
    }

    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> listener(RequestListener<? super ModelType, TranscodeType> requestListener2) {
        this.requestListener = requestListener2;
        return this;
    }

    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> skipMemoryCache(boolean skip) {
        this.isCacheable = !skip;
        return this;
    }

    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> override(int width, int height) {
        if (!Util.isValidDimensions(width, height)) {
            throw new IllegalArgumentException("Width and height must be Target#SIZE_ORIGINAL or > 0");
        }
        this.overrideWidth = width;
        this.overrideHeight = height;
        return this;
    }

    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> signature(Key signature2) {
        if (signature2 == null) {
            throw new NullPointerException("Signature must not be null");
        }
        this.signature = signature2;
        return this;
    }

    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> load(ModelType model2) {
        this.model = model2;
        this.isModelSet = true;
        return this;
    }

    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> clone() {
        try {
            GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> clone = (GenericRequestBuilder) super.clone();
            clone.loadProvider = this.loadProvider != null ? this.loadProvider.clone() : null;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public <Y extends Target<TranscodeType>> Y into(Y target) {
        Util.assertMainThread();
        if (target == null) {
            throw new IllegalArgumentException("You must pass in a non null Target");
        } else if (!this.isModelSet) {
            throw new IllegalArgumentException("You must first set a model (try #load())");
        } else {
            Request previous = target.getRequest();
            if (previous != null) {
                previous.clear();
                this.requestTracker.removeRequest(previous);
                previous.recycle();
            }
            Request request = buildRequest(target);
            target.setRequest(request);
            this.lifecycle.addListener(target);
            this.requestTracker.runRequest(request);
            return target;
        }
    }

    public Target<TranscodeType> into(ImageView view) {
        Util.assertMainThread();
        if (view == null) {
            throw new IllegalArgumentException("You must pass in a non null View");
        }
        if (!this.isTransformationSet && view.getScaleType() != null) {
            switch (C18732.$SwitchMap$android$widget$ImageView$ScaleType[view.getScaleType().ordinal()]) {
                case 1:
                    applyCenterCrop();
                    break;
                case 2:
                case 3:
                case 4:
                    applyFitCenter();
                    break;
            }
        }
        return into(this.glide.buildImageViewTarget(view, this.transcodeClass));
    }

    /* renamed from: com.bumptech.glide.GenericRequestBuilder$2 */
    static /* synthetic */ class C18732 {
        static final /* synthetic */ int[] $SwitchMap$android$widget$ImageView$ScaleType = new int[ImageView.ScaleType.values().length];

        static {
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.CENTER_CROP.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.FIT_CENTER.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.FIT_START.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.FIT_END.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    public FutureTarget<TranscodeType> into(int width, int height) {
        final RequestFutureTarget<ModelType, TranscodeType> target = new RequestFutureTarget<>(this.glide.getMainHandler(), width, height);
        this.glide.getMainHandler().post(new Runnable() {
            public void run() {
                if (!target.isCancelled()) {
                    GenericRequestBuilder.this.into(target);
                }
            }
        });
        return target;
    }

    public Target<TranscodeType> preload(int width, int height) {
        return into(PreloadTarget.obtain(width, height));
    }

    public Target<TranscodeType> preload() {
        return preload(Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    /* access modifiers changed from: package-private */
    public void applyCenterCrop() {
    }

    /* access modifiers changed from: package-private */
    public void applyFitCenter() {
    }

    private Priority getThumbnailPriority() {
        if (this.priority == Priority.LOW) {
            return Priority.NORMAL;
        }
        if (this.priority == Priority.NORMAL) {
            return Priority.HIGH;
        }
        return Priority.IMMEDIATE;
    }

    private Request buildRequest(Target<TranscodeType> target) {
        if (this.priority == null) {
            this.priority = Priority.NORMAL;
        }
        return buildRequestRecursive(target, (ThumbnailRequestCoordinator) null);
    }

    private Request buildRequestRecursive(Target<TranscodeType> target, ThumbnailRequestCoordinator parentCoordinator) {
        if (this.thumbnailRequestBuilder != null) {
            if (this.isThumbnailBuilt) {
                throw new IllegalStateException("You cannot use a request as both the main request and a thumbnail, consider using clone() on the request(s) passed to thumbnail()");
            }
            if (this.thumbnailRequestBuilder.animationFactory.equals(NoAnimation.getFactory())) {
                this.thumbnailRequestBuilder.animationFactory = this.animationFactory;
            }
            if (this.thumbnailRequestBuilder.priority == null) {
                this.thumbnailRequestBuilder.priority = getThumbnailPriority();
            }
            if (Util.isValidDimensions(this.overrideWidth, this.overrideHeight) && !Util.isValidDimensions(this.thumbnailRequestBuilder.overrideWidth, this.thumbnailRequestBuilder.overrideHeight)) {
                this.thumbnailRequestBuilder.override(this.overrideWidth, this.overrideHeight);
            }
            ThumbnailRequestCoordinator coordinator = new ThumbnailRequestCoordinator(parentCoordinator);
            Request fullRequest = obtainRequest(target, this.sizeMultiplier.floatValue(), this.priority, coordinator);
            this.isThumbnailBuilt = true;
            Request thumbRequest = this.thumbnailRequestBuilder.buildRequestRecursive(target, coordinator);
            this.isThumbnailBuilt = false;
            coordinator.setRequests(fullRequest, thumbRequest);
            return coordinator;
        } else if (this.thumbSizeMultiplier == null) {
            return obtainRequest(target, this.sizeMultiplier.floatValue(), this.priority, parentCoordinator);
        } else {
            ThumbnailRequestCoordinator coordinator2 = new ThumbnailRequestCoordinator(parentCoordinator);
            coordinator2.setRequests(obtainRequest(target, this.sizeMultiplier.floatValue(), this.priority, coordinator2), obtainRequest(target, this.thumbSizeMultiplier.floatValue(), getThumbnailPriority(), coordinator2));
            return coordinator2;
        }
    }

    private Request obtainRequest(Target<TranscodeType> target, float sizeMultiplier2, Priority priority2, RequestCoordinator requestCoordinator) {
        return GenericRequest.obtain(this.loadProvider, this.model, this.signature, this.context, priority2, target, sizeMultiplier2, this.placeholderDrawable, this.placeholderId, this.errorPlaceholder, this.errorId, this.fallbackDrawable, this.fallbackResource, this.requestListener, requestCoordinator, this.glide.getEngine(), this.transformation, this.transcodeClass, this.isCacheable, this.animationFactory, this.overrideWidth, this.overrideHeight, this.diskCacheStrategy);
    }
}
