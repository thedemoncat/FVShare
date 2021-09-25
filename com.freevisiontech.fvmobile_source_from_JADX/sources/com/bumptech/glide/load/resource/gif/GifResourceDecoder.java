package com.bumptech.glide.load.resource.gif;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import com.bumptech.glide.Glide;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.gifdecoder.GifHeader;
import com.bumptech.glide.gifdecoder.GifHeaderParser;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.UnitTransformation;
import com.bumptech.glide.util.Util;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;

public class GifResourceDecoder implements ResourceDecoder<InputStream, GifDrawable> {
    private static final GifDecoderPool DECODER_POOL = new GifDecoderPool();
    private static final GifHeaderParserPool PARSER_POOL = new GifHeaderParserPool();
    private static final String TAG = "GifResourceDecoder";
    private final BitmapPool bitmapPool;
    private final Context context;
    private final GifDecoderPool decoderPool;
    private final GifHeaderParserPool parserPool;
    private final GifBitmapProvider provider;

    public GifResourceDecoder(Context context2) {
        this(context2, Glide.get(context2).getBitmapPool());
    }

    public GifResourceDecoder(Context context2, BitmapPool bitmapPool2) {
        this(context2, bitmapPool2, PARSER_POOL, DECODER_POOL);
    }

    GifResourceDecoder(Context context2, BitmapPool bitmapPool2, GifHeaderParserPool parserPool2, GifDecoderPool decoderPool2) {
        this.context = context2;
        this.bitmapPool = bitmapPool2;
        this.decoderPool = decoderPool2;
        this.provider = new GifBitmapProvider(bitmapPool2);
        this.parserPool = parserPool2;
    }

    public GifDrawableResource decode(InputStream source, int width, int height) {
        byte[] data = inputStreamToBytes(source);
        GifHeaderParser parser = this.parserPool.obtain(data);
        GifDecoder decoder = this.decoderPool.obtain(this.provider);
        try {
            return decode(data, width, height, parser, decoder);
        } finally {
            this.parserPool.release(parser);
            this.decoderPool.release(decoder);
        }
    }

    private GifDrawableResource decode(byte[] data, int width, int height, GifHeaderParser parser, GifDecoder decoder) {
        Bitmap firstFrame;
        GifHeader header = parser.parseHeader();
        if (header.getNumFrames() <= 0 || header.getStatus() != 0 || (firstFrame = decodeFirstFrame(decoder, header, data)) == null) {
            return null;
        }
        return new GifDrawableResource(new GifDrawable(this.context, this.provider, this.bitmapPool, UnitTransformation.get(), width, height, header, data, firstFrame));
    }

    private Bitmap decodeFirstFrame(GifDecoder decoder, GifHeader header, byte[] data) {
        decoder.setData(header, data);
        decoder.advance();
        return decoder.getNextFrame();
    }

    public String getId() {
        return "";
    }

    private static byte[] inputStreamToBytes(InputStream is) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream(16384);
        try {
            byte[] data = new byte[16384];
            while (true) {
                int nRead = is.read(data);
                if (nRead == -1) {
                    break;
                }
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
        } catch (IOException e) {
            Log.w(TAG, "Error reading data from stream", e);
        }
        return buffer.toByteArray();
    }

    static class GifDecoderPool {
        private final Queue<GifDecoder> pool = Util.createQueue(0);

        GifDecoderPool() {
        }

        public synchronized GifDecoder obtain(GifDecoder.BitmapProvider bitmapProvider) {
            GifDecoder result;
            result = this.pool.poll();
            if (result == null) {
                result = new GifDecoder(bitmapProvider);
            }
            return result;
        }

        public synchronized void release(GifDecoder decoder) {
            decoder.clear();
            this.pool.offer(decoder);
        }
    }

    static class GifHeaderParserPool {
        private final Queue<GifHeaderParser> pool = Util.createQueue(0);

        GifHeaderParserPool() {
        }

        public synchronized GifHeaderParser obtain(byte[] data) {
            GifHeaderParser result;
            result = this.pool.poll();
            if (result == null) {
                result = new GifHeaderParser();
            }
            return result.setData(data);
        }

        public synchronized void release(GifHeaderParser parser) {
            parser.clear();
            this.pool.offer(parser);
        }
    }
}
