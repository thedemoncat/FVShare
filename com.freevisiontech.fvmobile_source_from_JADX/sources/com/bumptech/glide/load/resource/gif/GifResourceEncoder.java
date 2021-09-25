package com.bumptech.glide.load.resource.gif;

import android.graphics.Bitmap;
import android.util.Log;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.gifdecoder.GifHeader;
import com.bumptech.glide.gifdecoder.GifHeaderParser;
import com.bumptech.glide.gifencoder.AnimatedGifEncoder;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.UnitTransformation;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.util.LogTime;
import java.io.IOException;
import java.io.OutputStream;

public class GifResourceEncoder implements ResourceEncoder<GifDrawable> {
    private static final Factory FACTORY = new Factory();
    private static final String TAG = "GifEncoder";
    private final BitmapPool bitmapPool;
    private final Factory factory;
    private final GifDecoder.BitmapProvider provider;

    public GifResourceEncoder(BitmapPool bitmapPool2) {
        this(bitmapPool2, FACTORY);
    }

    GifResourceEncoder(BitmapPool bitmapPool2, Factory factory2) {
        this.bitmapPool = bitmapPool2;
        this.provider = new GifBitmapProvider(bitmapPool2);
        this.factory = factory2;
    }

    /* JADX INFO: finally extract failed */
    public boolean encode(Resource<GifDrawable> resource, OutputStream os) {
        long startTime = LogTime.getLogTime();
        GifDrawable drawable = resource.get();
        Transformation<Bitmap> transformation = drawable.getFrameTransformation();
        if (transformation instanceof UnitTransformation) {
            return writeDataDirect(drawable.getData(), os);
        }
        GifDecoder decoder = decodeHeaders(drawable.getData());
        AnimatedGifEncoder encoder = this.factory.buildEncoder();
        if (!encoder.start(os)) {
            return false;
        }
        int i = 0;
        while (i < decoder.getFrameCount()) {
            Resource<Bitmap> transformedResource = getTransformedFrame(decoder.getNextFrame(), transformation, drawable);
            try {
                if (!encoder.addFrame(transformedResource.get())) {
                    transformedResource.recycle();
                    return false;
                }
                encoder.setDelay(decoder.getDelay(decoder.getCurrentFrameIndex()));
                decoder.advance();
                transformedResource.recycle();
                i++;
            } catch (Throwable th) {
                transformedResource.recycle();
                throw th;
            }
        }
        boolean finish = encoder.finish();
        if (!Log.isLoggable(TAG, 2)) {
            return finish;
        }
        Log.v(TAG, "Encoded gif with " + decoder.getFrameCount() + " frames and " + drawable.getData().length + " bytes in " + LogTime.getElapsedMillis(startTime) + " ms");
        return finish;
    }

    private boolean writeDataDirect(byte[] data, OutputStream os) {
        try {
            os.write(data);
            return true;
        } catch (IOException e) {
            if (Log.isLoggable(TAG, 3)) {
                Log.d(TAG, "Failed to write data to output stream in GifResourceEncoder", e);
            }
            return false;
        }
    }

    private GifDecoder decodeHeaders(byte[] data) {
        GifHeaderParser parser = this.factory.buildParser();
        parser.setData(data);
        GifHeader header = parser.parseHeader();
        GifDecoder decoder = this.factory.buildDecoder(this.provider);
        decoder.setData(header, data);
        decoder.advance();
        return decoder;
    }

    private Resource<Bitmap> getTransformedFrame(Bitmap currentFrame, Transformation<Bitmap> transformation, GifDrawable drawable) {
        Resource buildFrameResource = this.factory.buildFrameResource(currentFrame, this.bitmapPool);
        Resource<Bitmap> transformedResource = transformation.transform(buildFrameResource, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        if (!buildFrameResource.equals(transformedResource)) {
            buildFrameResource.recycle();
        }
        return transformedResource;
    }

    public String getId() {
        return "";
    }

    static class Factory {
        Factory() {
        }

        public GifDecoder buildDecoder(GifDecoder.BitmapProvider bitmapProvider) {
            return new GifDecoder(bitmapProvider);
        }

        public GifHeaderParser buildParser() {
            return new GifHeaderParser();
        }

        public AnimatedGifEncoder buildEncoder() {
            return new AnimatedGifEncoder();
        }

        public Resource<Bitmap> buildFrameResource(Bitmap bitmap, BitmapPool bitmapPool) {
            return new BitmapResource(bitmap, bitmapPool);
        }
    }
}
