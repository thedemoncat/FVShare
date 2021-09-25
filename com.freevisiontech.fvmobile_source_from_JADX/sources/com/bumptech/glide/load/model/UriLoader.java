package com.bumptech.glide.load.model;

import android.content.Context;
import android.net.Uri;
import com.bumptech.glide.load.data.DataFetcher;

public abstract class UriLoader<T> implements ModelLoader<Uri, T> {
    private final Context context;
    private final ModelLoader<GlideUrl, T> urlLoader;

    /* access modifiers changed from: protected */
    public abstract DataFetcher<T> getAssetPathFetcher(Context context2, String str);

    /* access modifiers changed from: protected */
    public abstract DataFetcher<T> getLocalUriFetcher(Context context2, Uri uri);

    public UriLoader(Context context2, ModelLoader<GlideUrl, T> urlLoader2) {
        this.context = context2;
        this.urlLoader = urlLoader2;
    }

    public final DataFetcher<T> getResourceFetcher(Uri model, int width, int height) {
        String scheme = model.getScheme();
        if (isLocalUri(scheme)) {
            if (!AssetUriParser.isAssetUri(model)) {
                return getLocalUriFetcher(this.context, model);
            }
            return getAssetPathFetcher(this.context, AssetUriParser.toAssetPath(model));
        } else if (this.urlLoader == null) {
            return null;
        } else {
            if ("http".equals(scheme) || "https".equals(scheme)) {
                return this.urlLoader.getResourceFetcher(new GlideUrl(model.toString()), width, height);
            }
            return null;
        }
    }

    private static boolean isLocalUri(String scheme) {
        return "file".equals(scheme) || "content".equals(scheme) || "android.resource".equals(scheme);
    }
}
