package org.xutils.image;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.widget.ImageView;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;
import org.xutils.C2090x;
import org.xutils.cache.LruCache;
import org.xutils.cache.LruDiskCache;
import org.xutils.common.Callback;
import org.xutils.common.task.Priority;
import org.xutils.common.task.PriorityExecutor;
import org.xutils.common.util.IOUtil;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.p019ex.FileLockedException;

final class ImageLoader implements Callback.PrepareCallback<File, Drawable>, Callback.CacheCallback<Drawable>, Callback.ProgressCallback<Drawable>, Callback.TypedCallback<Drawable>, Callback.Cancelable {
    private static final String DISK_CACHE_DIR_NAME = "xUtils_img";
    private static final Executor EXECUTOR = new PriorityExecutor(10, false);
    private static final HashMap<String, FakeImageView> FAKE_IMG_MAP = new HashMap<>();
    private static final LruCache<MemCacheKey, Drawable> MEM_CACHE = new LruCache<MemCacheKey, Drawable>(4194304) {
        private boolean deepClear = false;

        /* access modifiers changed from: protected */
        public int sizeOf(MemCacheKey key, Drawable value) {
            if (value instanceof BitmapDrawable) {
                Bitmap bitmap = ((BitmapDrawable) value).getBitmap();
                if (bitmap == null) {
                    return 0;
                }
                return bitmap.getByteCount();
            } else if (value instanceof GifDrawable) {
                return ((GifDrawable) value).getByteCount();
            } else {
                return super.sizeOf(key, value);
            }
        }

        public void trimToSize(int maxSize) {
            if (maxSize < 0) {
                this.deepClear = true;
            }
            super.trimToSize(maxSize);
            this.deepClear = false;
        }

        /* access modifiers changed from: protected */
        public void entryRemoved(boolean evicted, MemCacheKey key, Drawable oldValue, Drawable newValue) {
            super.entryRemoved(evicted, key, oldValue, newValue);
            if (evicted && this.deepClear && (oldValue instanceof ReusableDrawable)) {
                ((ReusableDrawable) oldValue).setMemCacheKey((MemCacheKey) null);
            }
        }
    };
    private static final int MEM_CACHE_MIN_SIZE = 4194304;
    private static final AtomicLong SEQ_SEEK = new AtomicLong(0);
    private static final Type loadType = File.class;
    private Callback.CacheCallback<Drawable> cacheCallback;
    /* access modifiers changed from: private */
    public Callback.CommonCallback<Drawable> callback;
    private Callback.Cancelable cancelable;
    private volatile boolean cancelled = false;
    private boolean hasCache = false;
    /* access modifiers changed from: private */
    public MemCacheKey key;
    /* access modifiers changed from: private */
    public ImageOptions options;
    private Callback.PrepareCallback<File, Drawable> prepareCallback;
    private Callback.ProgressCallback<Drawable> progressCallback;
    private final long seq = SEQ_SEEK.incrementAndGet();
    private volatile boolean stopped = false;
    /* access modifiers changed from: private */
    public WeakReference<ImageView> viewRef;

    static {
        int cacheSize = (1048576 * ((ActivityManager) C2090x.app().getSystemService("activity")).getMemoryClass()) / 8;
        if (cacheSize < 4194304) {
            cacheSize = 4194304;
        }
        MEM_CACHE.resize(cacheSize);
    }

    private ImageLoader() {
    }

    static void clearMemCache() {
        MEM_CACHE.evictAll();
    }

    static void clearCacheFiles() {
        LruDiskCache.getDiskCache(DISK_CACHE_DIR_NAME).clearCacheFiles();
    }

    static Callback.Cancelable doLoadDrawable(String url, ImageOptions options2, Callback.CommonCallback<Drawable> callback2) {
        FakeImageView fakeImageView;
        if (TextUtils.isEmpty(url)) {
            postArgsException((ImageView) null, options2, "url is null", callback2);
            return null;
        }
        synchronized (FAKE_IMG_MAP) {
            fakeImageView = FAKE_IMG_MAP.get(url);
            if (fakeImageView == null) {
                fakeImageView = new FakeImageView();
            }
        }
        return doBind(fakeImageView, url, options2, callback2);
    }

    static Callback.Cancelable doLoadFile(String url, ImageOptions options2, Callback.CacheCallback<File> callback2) {
        if (TextUtils.isEmpty(url)) {
            postArgsException((ImageView) null, options2, "url is null", callback2);
            return null;
        }
        return C2090x.http().get(createRequestParams(url, options2), callback2);
    }

    static Callback.Cancelable doBind(ImageView view, String url, ImageOptions options2, Callback.CommonCallback<Drawable> callback2) {
        MemCacheKey oldKey;
        Callback.Cancelable doLoad;
        Bitmap bitmap;
        ImageOptions localOptions = options2;
        if (view == null) {
            postArgsException((ImageView) null, localOptions, "view is null", callback2);
            return null;
        } else if (TextUtils.isEmpty(url)) {
            postArgsException(view, localOptions, "url is null", callback2);
            return null;
        } else {
            if (localOptions == null) {
                localOptions = ImageOptions.DEFAULT;
            }
            localOptions.optimizeMaxSize(view);
            MemCacheKey key2 = new MemCacheKey(url, localOptions);
            Drawable oldDrawable = view.getDrawable();
            if (oldDrawable instanceof AsyncDrawable) {
                ImageLoader loader = ((AsyncDrawable) oldDrawable).getImageLoader();
                if (loader != null && !loader.stopped) {
                    if (key2.equals(loader.key)) {
                        return null;
                    }
                    loader.cancel();
                }
            } else if ((oldDrawable instanceof ReusableDrawable) && (oldKey = ((ReusableDrawable) oldDrawable).getMemCacheKey()) != null && oldKey.equals(key2)) {
                MEM_CACHE.put(key2, oldDrawable);
            }
            Drawable memDrawable = null;
            if (localOptions.isUseMemCache()) {
                memDrawable = MEM_CACHE.get(key2);
                if ((memDrawable instanceof BitmapDrawable) && ((bitmap = ((BitmapDrawable) memDrawable).getBitmap()) == null || bitmap.isRecycled())) {
                    memDrawable = null;
                }
            }
            if (memDrawable == null) {
                return new ImageLoader().doLoad(view, url, localOptions, callback2);
            }
            boolean trustMemCache = false;
            try {
                if (callback2 instanceof Callback.ProgressCallback) {
                    ((Callback.ProgressCallback) callback2).onWaiting();
                }
                view.setScaleType(localOptions.getImageScaleType());
                view.setImageDrawable(memDrawable);
                trustMemCache = true;
                if (callback2 instanceof Callback.CacheCallback) {
                    trustMemCache = ((Callback.CacheCallback) callback2).onCache(memDrawable);
                    if (!trustMemCache) {
                        Callback.Cancelable doLoad2 = new ImageLoader().doLoad(view, url, localOptions, callback2);
                        if (!trustMemCache || callback2 == null) {
                            return doLoad2;
                        }
                        try {
                            callback2.onFinished();
                            return doLoad2;
                        } catch (Throwable ignored) {
                            LogUtil.m1565e(ignored.getMessage(), ignored);
                            return doLoad2;
                        }
                    }
                } else if (callback2 != null) {
                    callback2.onSuccess(memDrawable);
                }
                if (trustMemCache && callback2 != null) {
                    try {
                        callback2.onFinished();
                    } catch (Throwable ignored2) {
                        LogUtil.m1565e(ignored2.getMessage(), ignored2);
                    }
                }
                return null;
            } catch (Throwable ignored3) {
                LogUtil.m1565e(ignored3.getMessage(), ignored3);
                return doLoad;
            }
        }
    }

    private Callback.Cancelable doLoad(ImageView view, String url, ImageOptions options2, Callback.CommonCallback<Drawable> callback2) {
        this.viewRef = new WeakReference<>(view);
        this.options = options2;
        this.key = new MemCacheKey(url, options2);
        this.callback = callback2;
        if (callback2 instanceof Callback.ProgressCallback) {
            this.progressCallback = (Callback.ProgressCallback) callback2;
        }
        if (callback2 instanceof Callback.PrepareCallback) {
            this.prepareCallback = (Callback.PrepareCallback) callback2;
        }
        if (callback2 instanceof Callback.CacheCallback) {
            this.cacheCallback = (Callback.CacheCallback) callback2;
        }
        if (options2.isForceLoadingDrawable()) {
            Drawable loadingDrawable = options2.getLoadingDrawable(view);
            view.setScaleType(options2.getPlaceholderScaleType());
            view.setImageDrawable(new AsyncDrawable(this, loadingDrawable));
        } else {
            view.setImageDrawable(new AsyncDrawable(this, view.getDrawable()));
        }
        RequestParams params = createRequestParams(url, options2);
        if (view instanceof FakeImageView) {
            synchronized (FAKE_IMG_MAP) {
                FAKE_IMG_MAP.put(url, (FakeImageView) view);
            }
        }
        Callback.Cancelable cancelable2 = C2090x.http().get(params, this);
        this.cancelable = cancelable2;
        return cancelable2;
    }

    public void cancel() {
        this.stopped = true;
        this.cancelled = true;
        if (this.cancelable != null) {
            this.cancelable.cancel();
        }
    }

    public boolean isCancelled() {
        return this.cancelled || !validView4Callback(false);
    }

    public void onWaiting() {
        if (this.progressCallback != null) {
            this.progressCallback.onWaiting();
        }
    }

    public void onStarted() {
        if (validView4Callback(true) && this.progressCallback != null) {
            this.progressCallback.onStarted();
        }
    }

    public void onLoading(long total, long current, boolean isDownloading) {
        if (validView4Callback(true) && this.progressCallback != null) {
            this.progressCallback.onLoading(total, current, isDownloading);
        }
    }

    public Type getLoadType() {
        return loadType;
    }

    public Drawable prepare(File rawData) {
        if (!validView4Callback(true)) {
            return null;
        }
        Drawable result = null;
        try {
            if (this.prepareCallback != null) {
                result = this.prepareCallback.prepare(rawData);
            }
            if (result == null) {
                result = ImageDecoder.decodeFileWithLock(rawData, this.options, this);
            }
            if (result == null || !(result instanceof ReusableDrawable)) {
                return result;
            }
            ((ReusableDrawable) result).setMemCacheKey(this.key);
            MEM_CACHE.put(this.key, result);
            return result;
        } catch (IOException ex) {
            IOUtil.deleteFileOrDir(rawData);
            LogUtil.m1571w(ex.getMessage(), ex);
            return null;
        }
    }

    public boolean onCache(Drawable result) {
        if (!validView4Callback(true) || result == null) {
            return false;
        }
        this.hasCache = true;
        setSuccessDrawable4Callback(result);
        if (this.cacheCallback != null) {
            return this.cacheCallback.onCache(result);
        }
        if (this.callback == null) {
            return true;
        }
        this.callback.onSuccess(result);
        return true;
    }

    public void onSuccess(Drawable result) {
        if (validView4Callback(!this.hasCache) && result != null) {
            setSuccessDrawable4Callback(result);
            if (this.callback != null) {
                this.callback.onSuccess(result);
            }
        }
    }

    public void onError(Throwable ex, boolean isOnCallback) {
        this.stopped = true;
        if (validView4Callback(false)) {
            if (ex instanceof FileLockedException) {
                LogUtil.m1562d("ImageFileLocked: " + this.key.url);
                C2090x.task().postDelayed(new Runnable() {
                    public void run() {
                        ImageLoader.doBind((ImageView) ImageLoader.this.viewRef.get(), ImageLoader.this.key.url, ImageLoader.this.options, ImageLoader.this.callback);
                    }
                }, 10);
                return;
            }
            LogUtil.m1565e(this.key.url, ex);
            setErrorDrawable4Callback();
            if (this.callback != null) {
                this.callback.onError(ex, isOnCallback);
            }
        }
    }

    public void onCancelled(Callback.CancelledException cex) {
        this.stopped = true;
        if (validView4Callback(false) && this.callback != null) {
            this.callback.onCancelled(cex);
        }
    }

    public void onFinished() {
        this.stopped = true;
        if (((ImageView) this.viewRef.get()) instanceof FakeImageView) {
            synchronized (FAKE_IMG_MAP) {
                FAKE_IMG_MAP.remove(this.key.url);
            }
        }
        if (validView4Callback(false) && this.callback != null) {
            this.callback.onFinished();
        }
    }

    private static RequestParams createRequestParams(String url, ImageOptions options2) {
        ImageOptions.ParamsBuilder paramsBuilder;
        RequestParams params = new RequestParams(url);
        params.setCacheDirName(DISK_CACHE_DIR_NAME);
        params.setConnectTimeout(8000);
        params.setPriority(Priority.BG_LOW);
        params.setExecutor(EXECUTOR);
        params.setCancelFast(true);
        params.setUseCookie(false);
        if (options2 == null || (paramsBuilder = options2.getParamsBuilder()) == null) {
            return params;
        }
        return paramsBuilder.buildParams(params, options2);
    }

    private boolean validView4Callback(boolean forceValidAsyncDrawable) {
        ImageView view = (ImageView) this.viewRef.get();
        if (view == null) {
            return false;
        }
        Drawable otherDrawable = view.getDrawable();
        if (otherDrawable instanceof AsyncDrawable) {
            ImageLoader otherLoader = ((AsyncDrawable) otherDrawable).getImageLoader();
            if (otherLoader != null) {
                if (otherLoader == this) {
                    if (view.getVisibility() == 0) {
                        return true;
                    }
                    otherLoader.cancel();
                    return false;
                } else if (this.seq > otherLoader.seq) {
                    otherLoader.cancel();
                    return true;
                } else {
                    cancel();
                    return false;
                }
            }
        } else if (forceValidAsyncDrawable) {
            cancel();
            return false;
        }
        return true;
    }

    private void setSuccessDrawable4Callback(Drawable drawable) {
        ImageView view = (ImageView) this.viewRef.get();
        if (view != null) {
            view.setScaleType(this.options.getImageScaleType());
            if (drawable instanceof GifDrawable) {
                if (view.getScaleType() == ImageView.ScaleType.CENTER) {
                    view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                }
                view.setLayerType(1, (Paint) null);
            }
            if (this.options.getAnimation() != null) {
                ImageAnimationHelper.animationDisplay(view, drawable, this.options.getAnimation());
            } else if (this.options.isFadeIn()) {
                ImageAnimationHelper.fadeInDisplay(view, drawable);
            } else {
                view.setImageDrawable(drawable);
            }
        }
    }

    private void setErrorDrawable4Callback() {
        ImageView view = (ImageView) this.viewRef.get();
        if (view != null) {
            Drawable drawable = this.options.getFailureDrawable(view);
            view.setScaleType(this.options.getPlaceholderScaleType());
            view.setImageDrawable(drawable);
        }
    }

    private static void postArgsException(final ImageView view, final ImageOptions options2, final String exMsg, final Callback.CommonCallback<?> callback2) {
        C2090x.task().autoPost(new Runnable() {
            public void run() {
                try {
                    if (callback2 instanceof Callback.ProgressCallback) {
                        ((Callback.ProgressCallback) callback2).onWaiting();
                    }
                    if (!(view == null || options2 == null)) {
                        view.setScaleType(options2.getPlaceholderScaleType());
                        view.setImageDrawable(options2.getFailureDrawable(view));
                    }
                    if (callback2 != null) {
                        callback2.onError(new IllegalArgumentException(exMsg), false);
                    }
                    if (callback2 != null) {
                        try {
                            callback2.onFinished();
                            return;
                        } catch (Throwable ignored) {
                            LogUtil.m1565e(ignored.getMessage(), ignored);
                            return;
                        }
                    } else {
                        return;
                    }
                } catch (Throwable ignored2) {
                    LogUtil.m1565e(ignored2.getMessage(), ignored2);
                    return;
                }
                if (callback2 != null) {
                    callback2.onFinished();
                }
            }
        });
    }

    @SuppressLint({"ViewConstructor"})
    private static final class FakeImageView extends ImageView {
        private Drawable drawable;

        public FakeImageView() {
            super(C2090x.app());
        }

        public void setImageDrawable(Drawable drawable2) {
            this.drawable = drawable2;
        }

        public Drawable getDrawable() {
            return this.drawable;
        }

        public void setLayerType(int layerType, Paint paint) {
        }

        public void setScaleType(ImageView.ScaleType scaleType) {
        }

        public void startAnimation(Animation animation) {
        }
    }
}
