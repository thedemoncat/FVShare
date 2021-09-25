package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.util.Log;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Util;
import java.io.OutputStream;

public class BitmapEncoder implements ResourceEncoder<Bitmap> {
    private static final int DEFAULT_COMPRESSION_QUALITY = 90;
    private static final String TAG = "BitmapEncoder";
    private Bitmap.CompressFormat compressFormat;
    private int quality;

    public BitmapEncoder() {
        this((Bitmap.CompressFormat) null, 90);
    }

    public BitmapEncoder(Bitmap.CompressFormat compressFormat2, int quality2) {
        this.compressFormat = compressFormat2;
        this.quality = quality2;
    }

    public boolean encode(Resource<Bitmap> resource, OutputStream os) {
        Bitmap bitmap = resource.get();
        long start = LogTime.getLogTime();
        Bitmap.CompressFormat format = getFormat(bitmap);
        bitmap.compress(format, this.quality, os);
        if (!Log.isLoggable(TAG, 2)) {
            return true;
        }
        Log.v(TAG, "Compressed with type: " + format + " of size " + Util.getBitmapByteSize(bitmap) + " in " + LogTime.getElapsedMillis(start));
        return true;
    }

    public String getId() {
        return "BitmapEncoder.com.bumptech.glide.load.resource.bitmap";
    }

    private Bitmap.CompressFormat getFormat(Bitmap bitmap) {
        if (this.compressFormat != null) {
            return this.compressFormat;
        }
        if (bitmap.hasAlpha()) {
            return Bitmap.CompressFormat.PNG;
        }
        return Bitmap.CompressFormat.JPEG;
    }
}
