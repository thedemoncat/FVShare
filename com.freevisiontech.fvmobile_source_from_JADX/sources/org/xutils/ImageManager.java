package org.xutils;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import java.io.File;
import org.xutils.common.Callback;
import org.xutils.image.ImageOptions;

public interface ImageManager {
    void bind(ImageView imageView, String str);

    void bind(ImageView imageView, String str, Callback.CommonCallback<Drawable> commonCallback);

    void bind(ImageView imageView, String str, ImageOptions imageOptions);

    void bind(ImageView imageView, String str, ImageOptions imageOptions, Callback.CommonCallback<Drawable> commonCallback);

    void clearCacheFiles();

    void clearMemCache();

    Callback.Cancelable loadDrawable(String str, ImageOptions imageOptions, Callback.CommonCallback<Drawable> commonCallback);

    Callback.Cancelable loadFile(String str, ImageOptions imageOptions, Callback.CacheCallback<File> cacheCallback);
}
