package org.xutils.image;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import java.io.File;
import org.xutils.C2090x;
import org.xutils.ImageManager;
import org.xutils.common.Callback;

public final class ImageManagerImpl implements ImageManager {
    private static volatile ImageManagerImpl instance;
    private static final Object lock = new Object();

    private ImageManagerImpl() {
    }

    public static void registerInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new ImageManagerImpl();
                }
            }
        }
        C2090x.Ext.setImageManager(instance);
    }

    public void bind(final ImageView view, final String url) {
        C2090x.task().autoPost(new Runnable() {
            public void run() {
                ImageLoader.doBind(view, url, (ImageOptions) null, (Callback.CommonCallback<Drawable>) null);
            }
        });
    }

    public void bind(final ImageView view, final String url, final ImageOptions options) {
        C2090x.task().autoPost(new Runnable() {
            public void run() {
                ImageLoader.doBind(view, url, options, (Callback.CommonCallback<Drawable>) null);
            }
        });
    }

    public void bind(final ImageView view, final String url, final Callback.CommonCallback<Drawable> callback) {
        C2090x.task().autoPost(new Runnable() {
            public void run() {
                ImageLoader.doBind(view, url, (ImageOptions) null, callback);
            }
        });
    }

    public void bind(ImageView view, String url, ImageOptions options, Callback.CommonCallback<Drawable> callback) {
        final ImageView imageView = view;
        final String str = url;
        final ImageOptions imageOptions = options;
        final Callback.CommonCallback<Drawable> commonCallback = callback;
        C2090x.task().autoPost(new Runnable() {
            public void run() {
                ImageLoader.doBind(imageView, str, imageOptions, commonCallback);
            }
        });
    }

    public Callback.Cancelable loadDrawable(String url, ImageOptions options, Callback.CommonCallback<Drawable> callback) {
        return ImageLoader.doLoadDrawable(url, options, callback);
    }

    public Callback.Cancelable loadFile(String url, ImageOptions options, Callback.CacheCallback<File> callback) {
        return ImageLoader.doLoadFile(url, options, callback);
    }

    public void clearMemCache() {
        ImageLoader.clearMemCache();
    }

    public void clearCacheFiles() {
        ImageLoader.clearCacheFiles();
        ImageDecoder.clearCacheFiles();
    }
}
