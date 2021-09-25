package com.bumptech.glide.load.resource.gif;

import android.graphics.Bitmap;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;

public class GifDrawableTransformation implements Transformation<GifDrawable> {
    private final BitmapPool bitmapPool;
    private final Transformation<Bitmap> wrapped;

    public GifDrawableTransformation(Transformation<Bitmap> wrapped2, BitmapPool bitmapPool2) {
        this.wrapped = wrapped2;
        this.bitmapPool = bitmapPool2;
    }

    public Resource<GifDrawable> transform(Resource<GifDrawable> resource, int outWidth, int outHeight) {
        GifDrawable drawable = resource.get();
        Bitmap firstFrame = resource.get().getFirstFrame();
        Bitmap transformedFrame = this.wrapped.transform(new BitmapResource(firstFrame, this.bitmapPool), outWidth, outHeight).get();
        if (!transformedFrame.equals(firstFrame)) {
            return new GifDrawableResource(new GifDrawable(drawable, transformedFrame, this.wrapped));
        }
        return resource;
    }

    public String getId() {
        return this.wrapped.getId();
    }
}
