package com.bumptech.glide.load.resource.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.ParcelFileDescriptor;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import java.io.IOException;

public class FileDescriptorBitmapDecoder implements ResourceDecoder<ParcelFileDescriptor, Bitmap> {
    private final VideoBitmapDecoder bitmapDecoder;
    private final BitmapPool bitmapPool;
    private DecodeFormat decodeFormat;

    public FileDescriptorBitmapDecoder(Context context) {
        this(Glide.get(context).getBitmapPool(), DecodeFormat.DEFAULT);
    }

    public FileDescriptorBitmapDecoder(Context context, DecodeFormat decodeFormat2) {
        this(Glide.get(context).getBitmapPool(), decodeFormat2);
    }

    public FileDescriptorBitmapDecoder(BitmapPool bitmapPool2, DecodeFormat decodeFormat2) {
        this(new VideoBitmapDecoder(), bitmapPool2, decodeFormat2);
    }

    public FileDescriptorBitmapDecoder(VideoBitmapDecoder bitmapDecoder2, BitmapPool bitmapPool2, DecodeFormat decodeFormat2) {
        this.bitmapDecoder = bitmapDecoder2;
        this.bitmapPool = bitmapPool2;
        this.decodeFormat = decodeFormat2;
    }

    public Resource<Bitmap> decode(ParcelFileDescriptor source, int width, int height) throws IOException {
        return BitmapResource.obtain(this.bitmapDecoder.decode(source, this.bitmapPool, width, height, this.decodeFormat), this.bitmapPool);
    }

    public String getId() {
        return "FileDescriptorBitmapDecoder.com.bumptech.glide.load.data.bitmap";
    }
}
