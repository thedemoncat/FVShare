package com.bumptech.glide.load.resource.bitmap;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.ImageHeaderParser;
import com.bumptech.glide.util.ByteArrayPool;
import com.bumptech.glide.util.ExceptionCatchingInputStream;
import com.bumptech.glide.util.MarkEnforcingInputStream;
import com.bumptech.glide.util.Util;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.Queue;
import java.util.Set;

public abstract class Downsampler implements BitmapDecoder<InputStream> {
    public static final Downsampler AT_LEAST = new Downsampler() {
        public /* bridge */ /* synthetic */ Bitmap decode(Object x0, BitmapPool x1, int x2, int x3, DecodeFormat x4) throws Exception {
            return Downsampler.super.decode((InputStream) x0, x1, x2, x3, x4);
        }

        /* access modifiers changed from: protected */
        public int getSampleSize(int inWidth, int inHeight, int outWidth, int outHeight) {
            return Math.min(inHeight / outHeight, inWidth / outWidth);
        }

        public String getId() {
            return "AT_LEAST.com.bumptech.glide.load.data.bitmap";
        }
    };
    public static final Downsampler AT_MOST = new Downsampler() {
        public /* bridge */ /* synthetic */ Bitmap decode(Object x0, BitmapPool x1, int x2, int x3, DecodeFormat x4) throws Exception {
            return Downsampler.super.decode((InputStream) x0, x1, x2, x3, x4);
        }

        /* access modifiers changed from: protected */
        public int getSampleSize(int inWidth, int inHeight, int outWidth, int outHeight) {
            int i = 1;
            int maxIntegerFactor = (int) Math.ceil((double) Math.max(((float) inHeight) / ((float) outHeight), ((float) inWidth) / ((float) outWidth)));
            int lesserOrEqualSampleSize = Math.max(1, Integer.highestOneBit(maxIntegerFactor));
            if (lesserOrEqualSampleSize >= maxIntegerFactor) {
                i = 0;
            }
            return lesserOrEqualSampleSize << i;
        }

        public String getId() {
            return "AT_MOST.com.bumptech.glide.load.data.bitmap";
        }
    };
    private static final int MARK_POSITION = 5242880;
    public static final Downsampler NONE = new Downsampler() {
        public /* bridge */ /* synthetic */ Bitmap decode(Object x0, BitmapPool x1, int x2, int x3, DecodeFormat x4) throws Exception {
            return Downsampler.super.decode((InputStream) x0, x1, x2, x3, x4);
        }

        /* access modifiers changed from: protected */
        public int getSampleSize(int inWidth, int inHeight, int outWidth, int outHeight) {
            return 0;
        }

        public String getId() {
            return "NONE.com.bumptech.glide.load.data.bitmap";
        }
    };
    private static final Queue<BitmapFactory.Options> OPTIONS_QUEUE = Util.createQueue(0);
    private static final String TAG = "Downsampler";
    private static final Set<ImageHeaderParser.ImageType> TYPES_THAT_USE_POOL = EnumSet.of(ImageHeaderParser.ImageType.JPEG, ImageHeaderParser.ImageType.PNG_A, ImageHeaderParser.ImageType.PNG);

    /* access modifiers changed from: protected */
    public abstract int getSampleSize(int i, int i2, int i3, int i4);

    public Bitmap decode(InputStream is, BitmapPool pool, int outWidth, int outHeight, DecodeFormat decodeFormat) {
        int orientation;
        ByteArrayPool byteArrayPool = ByteArrayPool.get();
        byte[] bytesForOptions = byteArrayPool.getBytes();
        byte[] bytesForStream = byteArrayPool.getBytes();
        BitmapFactory.Options options = getDefaultOptions();
        RecyclableBufferedInputStream bufferedStream = new RecyclableBufferedInputStream(is, bytesForStream);
        ExceptionCatchingInputStream exceptionStream = ExceptionCatchingInputStream.obtain(bufferedStream);
        MarkEnforcingInputStream markEnforcingInputStream = new MarkEnforcingInputStream(exceptionStream);
        try {
            exceptionStream.mark(MARK_POSITION);
            orientation = 0;
            try {
                orientation = new ImageHeaderParser(exceptionStream).getOrientation();
                exceptionStream.reset();
            } catch (IOException e) {
                if (Log.isLoggable(TAG, 5)) {
                    Log.w(TAG, "Cannot determine the image orientation from header", e);
                }
                try {
                    exceptionStream.reset();
                } catch (IOException e2) {
                    if (Log.isLoggable(TAG, 5)) {
                        Log.w(TAG, "Cannot reset the input stream", e2);
                    }
                }
            } catch (Throwable th) {
                try {
                    exceptionStream.reset();
                } catch (IOException e3) {
                    if (Log.isLoggable(TAG, 5)) {
                        Log.w(TAG, "Cannot reset the input stream", e3);
                    }
                }
                throw th;
            }
        } catch (IOException e4) {
            if (Log.isLoggable(TAG, 5)) {
                Log.w(TAG, "Cannot reset the input stream", e4);
            }
        } catch (Throwable th2) {
            byteArrayPool.releaseBytes(bytesForOptions);
            byteArrayPool.releaseBytes(bytesForStream);
            exceptionStream.release();
            releaseOptions(options);
            throw th2;
        }
        options.inTempStorage = bytesForOptions;
        int[] inDimens = getDimensions(markEnforcingInputStream, bufferedStream, options);
        int inWidth = inDimens[0];
        int inHeight = inDimens[1];
        Bitmap downsampled = downsampleWithSize(markEnforcingInputStream, bufferedStream, options, pool, inWidth, inHeight, getRoundedSampleSize(TransformationUtils.getExifOrientationDegrees(orientation), inWidth, inHeight, outWidth, outHeight), decodeFormat);
        Exception streamException = exceptionStream.getException();
        if (streamException != null) {
            throw new RuntimeException(streamException);
        }
        Bitmap rotated = null;
        if (downsampled != null) {
            rotated = TransformationUtils.rotateImageExif(downsampled, pool, orientation);
            if (!downsampled.equals(rotated) && !pool.put(downsampled)) {
                downsampled.recycle();
            }
        }
        byteArrayPool.releaseBytes(bytesForOptions);
        byteArrayPool.releaseBytes(bytesForStream);
        exceptionStream.release();
        releaseOptions(options);
        return rotated;
    }

    private int getRoundedSampleSize(int degreesToRotate, int inWidth, int inHeight, int outWidth, int outHeight) {
        int targetHeight;
        int targetWidth;
        int exactSampleSize;
        if (outHeight == Integer.MIN_VALUE) {
            targetHeight = inHeight;
        } else {
            targetHeight = outHeight;
        }
        if (outWidth == Integer.MIN_VALUE) {
            targetWidth = inWidth;
        } else {
            targetWidth = outWidth;
        }
        if (degreesToRotate == 90 || degreesToRotate == 270) {
            exactSampleSize = getSampleSize(inHeight, inWidth, targetWidth, targetHeight);
        } else {
            exactSampleSize = getSampleSize(inWidth, inHeight, targetWidth, targetHeight);
        }
        return Math.max(1, exactSampleSize == 0 ? 0 : Integer.highestOneBit(exactSampleSize));
    }

    private Bitmap downsampleWithSize(MarkEnforcingInputStream is, RecyclableBufferedInputStream bufferedStream, BitmapFactory.Options options, BitmapPool pool, int inWidth, int inHeight, int sampleSize, DecodeFormat decodeFormat) {
        Bitmap.Config config = getConfig(is, decodeFormat);
        options.inSampleSize = sampleSize;
        options.inPreferredConfig = config;
        if ((options.inSampleSize == 1 || 19 <= Build.VERSION.SDK_INT) && shouldUsePool(is)) {
            setInBitmap(options, pool.getDirty((int) Math.ceil(((double) inWidth) / ((double) sampleSize)), (int) Math.ceil(((double) inHeight) / ((double) sampleSize)), config));
        }
        return decodeStream(is, bufferedStream, options);
    }

    private static boolean shouldUsePool(InputStream is) {
        if (19 <= Build.VERSION.SDK_INT) {
            return true;
        }
        is.mark(1024);
        try {
            boolean contains = TYPES_THAT_USE_POOL.contains(new ImageHeaderParser(is).getType());
            try {
                is.reset();
                return contains;
            } catch (IOException e) {
                if (!Log.isLoggable(TAG, 5)) {
                    return contains;
                }
                Log.w(TAG, "Cannot reset the input stream", e);
                return contains;
            }
        } catch (IOException e2) {
            if (Log.isLoggable(TAG, 5)) {
                Log.w(TAG, "Cannot determine the image type from header", e2);
            }
            try {
                is.reset();
            } catch (IOException e3) {
                if (Log.isLoggable(TAG, 5)) {
                    Log.w(TAG, "Cannot reset the input stream", e3);
                }
            }
            return false;
        } catch (Throwable th) {
            try {
                is.reset();
            } catch (IOException e4) {
                if (Log.isLoggable(TAG, 5)) {
                    Log.w(TAG, "Cannot reset the input stream", e4);
                }
            }
            throw th;
        }
    }

    private static Bitmap.Config getConfig(InputStream is, DecodeFormat format) {
        if (format == DecodeFormat.ALWAYS_ARGB_8888 || format == DecodeFormat.PREFER_ARGB_8888 || Build.VERSION.SDK_INT == 16) {
            return Bitmap.Config.ARGB_8888;
        }
        boolean hasAlpha = false;
        is.mark(1024);
        try {
            hasAlpha = new ImageHeaderParser(is).hasAlpha();
            try {
                is.reset();
            } catch (IOException e) {
                if (Log.isLoggable(TAG, 5)) {
                    Log.w(TAG, "Cannot reset the input stream", e);
                }
            }
        } catch (IOException e2) {
            if (Log.isLoggable(TAG, 5)) {
                Log.w(TAG, "Cannot determine whether the image has alpha or not from header for format " + format, e2);
            }
            try {
                is.reset();
            } catch (IOException e3) {
                if (Log.isLoggable(TAG, 5)) {
                    Log.w(TAG, "Cannot reset the input stream", e3);
                }
            }
        } catch (Throwable th) {
            try {
                is.reset();
            } catch (IOException e4) {
                if (Log.isLoggable(TAG, 5)) {
                    Log.w(TAG, "Cannot reset the input stream", e4);
                }
            }
            throw th;
        }
        if (hasAlpha) {
            return Bitmap.Config.ARGB_8888;
        }
        return Bitmap.Config.RGB_565;
    }

    public int[] getDimensions(MarkEnforcingInputStream is, RecyclableBufferedInputStream bufferedStream, BitmapFactory.Options options) {
        options.inJustDecodeBounds = true;
        decodeStream(is, bufferedStream, options);
        options.inJustDecodeBounds = false;
        return new int[]{options.outWidth, options.outHeight};
    }

    private static Bitmap decodeStream(MarkEnforcingInputStream is, RecyclableBufferedInputStream bufferedStream, BitmapFactory.Options options) {
        if (options.inJustDecodeBounds) {
            is.mark(MARK_POSITION);
        } else {
            bufferedStream.fixMarkLimit();
        }
        Bitmap result = BitmapFactory.decodeStream(is, (Rect) null, options);
        try {
            if (options.inJustDecodeBounds) {
                is.reset();
            }
        } catch (IOException e) {
            if (Log.isLoggable(TAG, 6)) {
                Log.e(TAG, "Exception loading inDecodeBounds=" + options.inJustDecodeBounds + " sample=" + options.inSampleSize, e);
            }
        }
        return result;
    }

    @TargetApi(11)
    private static void setInBitmap(BitmapFactory.Options options, Bitmap recycled) {
        if (11 <= Build.VERSION.SDK_INT) {
            options.inBitmap = recycled;
        }
    }

    @TargetApi(11)
    private static synchronized BitmapFactory.Options getDefaultOptions() {
        BitmapFactory.Options decodeBitmapOptions;
        synchronized (Downsampler.class) {
            synchronized (OPTIONS_QUEUE) {
                decodeBitmapOptions = OPTIONS_QUEUE.poll();
            }
            if (decodeBitmapOptions == null) {
                decodeBitmapOptions = new BitmapFactory.Options();
                resetOptions(decodeBitmapOptions);
            }
        }
        return decodeBitmapOptions;
    }

    private static void releaseOptions(BitmapFactory.Options decodeBitmapOptions) {
        resetOptions(decodeBitmapOptions);
        synchronized (OPTIONS_QUEUE) {
            OPTIONS_QUEUE.offer(decodeBitmapOptions);
        }
    }

    @TargetApi(11)
    private static void resetOptions(BitmapFactory.Options decodeBitmapOptions) {
        decodeBitmapOptions.inTempStorage = null;
        decodeBitmapOptions.inDither = false;
        decodeBitmapOptions.inScaled = false;
        decodeBitmapOptions.inSampleSize = 1;
        decodeBitmapOptions.inPreferredConfig = null;
        decodeBitmapOptions.inJustDecodeBounds = false;
        decodeBitmapOptions.outWidth = 0;
        decodeBitmapOptions.outHeight = 0;
        decodeBitmapOptions.outMimeType = null;
        if (11 <= Build.VERSION.SDK_INT) {
            decodeBitmapOptions.inBitmap = null;
            decodeBitmapOptions.inMutable = true;
        }
    }
}
