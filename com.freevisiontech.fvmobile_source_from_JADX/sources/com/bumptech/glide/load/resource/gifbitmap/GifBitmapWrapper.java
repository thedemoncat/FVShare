package com.bumptech.glide.load.resource.gifbitmap;

import android.graphics.Bitmap;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.gif.GifDrawable;

public class GifBitmapWrapper {
    private final Resource<Bitmap> bitmapResource;
    private final Resource<GifDrawable> gifResource;

    public GifBitmapWrapper(Resource<Bitmap> bitmapResource2, Resource<GifDrawable> gifResource2) {
        if (bitmapResource2 != null && gifResource2 != null) {
            throw new IllegalArgumentException("Can only contain either a bitmap resource or a gif resource, not both");
        } else if (bitmapResource2 == null && gifResource2 == null) {
            throw new IllegalArgumentException("Must contain either a bitmap resource or a gif resource");
        } else {
            this.bitmapResource = bitmapResource2;
            this.gifResource = gifResource2;
        }
    }

    public int getSize() {
        if (this.bitmapResource != null) {
            return this.bitmapResource.getSize();
        }
        return this.gifResource.getSize();
    }

    public Resource<Bitmap> getBitmapResource() {
        return this.bitmapResource;
    }

    public Resource<GifDrawable> getGifResource() {
        return this.gifResource;
    }
}
