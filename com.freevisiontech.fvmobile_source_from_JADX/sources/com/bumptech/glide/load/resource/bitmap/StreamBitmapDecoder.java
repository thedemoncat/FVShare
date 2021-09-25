package com.bumptech.glide.load.resource.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import java.io.InputStream;

public class StreamBitmapDecoder implements ResourceDecoder<InputStream, Bitmap> {

    /* renamed from: ID */
    private static final String f1187ID = "StreamBitmapDecoder.com.bumptech.glide.load.resource.bitmap";
    private BitmapPool bitmapPool;
    private DecodeFormat decodeFormat;
    private final Downsampler downsampler;

    /* renamed from: id */
    private String f1188id;

    public StreamBitmapDecoder(Context context) {
        this(Glide.get(context).getBitmapPool());
    }

    public StreamBitmapDecoder(BitmapPool bitmapPool2) {
        this(bitmapPool2, DecodeFormat.DEFAULT);
    }

    public StreamBitmapDecoder(Context context, DecodeFormat decodeFormat2) {
        this(Glide.get(context).getBitmapPool(), decodeFormat2);
    }

    public StreamBitmapDecoder(BitmapPool bitmapPool2, DecodeFormat decodeFormat2) {
        this(Downsampler.AT_LEAST, bitmapPool2, decodeFormat2);
    }

    public StreamBitmapDecoder(Downsampler downsampler2, BitmapPool bitmapPool2, DecodeFormat decodeFormat2) {
        this.downsampler = downsampler2;
        this.bitmapPool = bitmapPool2;
        this.decodeFormat = decodeFormat2;
    }

    public Resource<Bitmap> decode(InputStream source, int width, int height) {
        return BitmapResource.obtain(this.downsampler.decode(source, this.bitmapPool, width, height, this.decodeFormat), this.bitmapPool);
    }

    public String getId() {
        if (this.f1188id == null) {
            this.f1188id = f1187ID + this.downsampler.getId() + this.decodeFormat.name();
        }
        return this.f1188id;
    }
}
