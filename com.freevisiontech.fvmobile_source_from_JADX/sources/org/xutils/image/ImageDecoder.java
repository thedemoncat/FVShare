package org.xutils.image;

import android.backport.webp.WebPFactory;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Movie;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.widget.ImageView;
import com.freevisiontech.utils.ScreenOrientationUtil;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import org.xutils.C2090x;
import org.xutils.cache.DiskCacheEntity;
import org.xutils.cache.DiskCacheFile;
import org.xutils.cache.LruDiskCache;
import org.xutils.common.Callback;
import org.xutils.common.task.PriorityExecutor;
import org.xutils.common.util.IOUtil;
import org.xutils.common.util.LogUtil;

public final class ImageDecoder {
    private static final int BITMAP_DECODE_MAX_WORKER;
    private static final byte[] GIF_HEADER = {71, 73, 70};
    private static final LruDiskCache THUMB_CACHE = LruDiskCache.getDiskCache("xUtils_img_thumb");
    private static final Executor THUMB_CACHE_EXECUTOR = new PriorityExecutor(1, true);
    private static final byte[] WEBP_HEADER = {87, 69, 66, 80};
    private static final Object bitmapDecodeLock = new Object();
    private static final AtomicInteger bitmapDecodeWorker = new AtomicInteger(0);
    private static final Object gifDecodeLock = new Object();

    static {
        int i = 1;
        if (Runtime.getRuntime().availableProcessors() > 4) {
            i = 2;
        }
        BITMAP_DECODE_MAX_WORKER = i;
    }

    private ImageDecoder() {
    }

    static void clearCacheFiles() {
        THUMB_CACHE.clearCacheFiles();
    }

    static Drawable decodeFileWithLock(final File file, final ImageOptions options, Callback.Cancelable cancelable) throws IOException {
        Movie movie;
        if (file == null || !file.exists() || file.length() < 1) {
            return null;
        }
        if (cancelable != null && cancelable.isCancelled()) {
            throw new Callback.CancelledException("cancelled during decode image");
        } else if (options.isIgnoreGif() || !isGif(file)) {
            Bitmap bitmap = null;
            while (bitmapDecodeWorker.get() >= BITMAP_DECODE_MAX_WORKER && (cancelable == null || !cancelable.isCancelled())) {
                try {
                    synchronized (bitmapDecodeLock) {
                        bitmapDecodeLock.wait();
                    }
                } catch (InterruptedException e) {
                    throw new Callback.CancelledException("cancelled during decode image");
                } catch (Throwable th) {
                    bitmapDecodeWorker.decrementAndGet();
                    synchronized (bitmapDecodeLock) {
                        bitmapDecodeLock.notifyAll();
                        throw th;
                    }
                }
            }
            if (cancelable != null) {
                if (cancelable.isCancelled()) {
                    throw new Callback.CancelledException("cancelled during decode image");
                }
            }
            bitmapDecodeWorker.incrementAndGet();
            if (options.isCompress()) {
                bitmap = getThumbCache(file, options);
            }
            if (bitmap == null && (bitmap = decodeBitmap(file, options, cancelable)) != null && options.isCompress()) {
                final Bitmap finalBitmap = bitmap;
                THUMB_CACHE_EXECUTOR.execute(new Runnable() {
                    public void run() {
                        ImageDecoder.saveThumbCache(file, options, finalBitmap);
                    }
                });
            }
            bitmapDecodeWorker.decrementAndGet();
            synchronized (bitmapDecodeLock) {
                bitmapDecodeLock.notifyAll();
            }
            if (bitmap != null) {
                return new ReusableBitmapDrawable(C2090x.app().getResources(), bitmap);
            }
            return null;
        } else {
            synchronized (gifDecodeLock) {
                movie = decodeGif(file, options, cancelable);
            }
            if (movie != null) {
                return new GifDrawable(movie, (int) file.length());
            }
            return null;
        }
    }

    public static boolean isGif(File file) {
        FileInputStream in = null;
        try {
            FileInputStream in2 = new FileInputStream(file);
            try {
                boolean equals = Arrays.equals(GIF_HEADER, IOUtil.readBytes(in2, 0, 3));
                IOUtil.closeQuietly((Closeable) in2);
                FileInputStream fileInputStream = in2;
                return equals;
            } catch (Throwable th) {
                th = th;
                in = in2;
                IOUtil.closeQuietly((Closeable) in);
                throw th;
            }
        } catch (Throwable th2) {
            ex = th2;
            LogUtil.m1565e(ex.getMessage(), ex);
            IOUtil.closeQuietly((Closeable) in);
            return false;
        }
    }

    public static boolean isWebP(File file) {
        FileInputStream in = null;
        try {
            FileInputStream in2 = new FileInputStream(file);
            try {
                boolean equals = Arrays.equals(WEBP_HEADER, IOUtil.readBytes(in2, 8, 4));
                IOUtil.closeQuietly((Closeable) in2);
                FileInputStream fileInputStream = in2;
                return equals;
            } catch (Throwable th) {
                th = th;
                in = in2;
                IOUtil.closeQuietly((Closeable) in);
                throw th;
            }
        } catch (Throwable th2) {
            ex = th2;
            LogUtil.m1565e(ex.getMessage(), ex);
            IOUtil.closeQuietly((Closeable) in);
            return false;
        }
    }

    public static Bitmap decodeBitmap(File file, ImageOptions options, Callback.Cancelable cancelable) throws IOException {
        if (file == null || !file.exists() || file.length() < 1) {
            return null;
        }
        if (options == null) {
            options = ImageOptions.DEFAULT;
        }
        if (options.getMaxWidth() <= 0 || options.getMaxHeight() <= 0) {
            options.optimizeMaxSize((ImageView) null);
        }
        if (cancelable != null) {
            try {
                if (cancelable.isCancelled()) {
                    throw new Callback.CancelledException("cancelled during decode image");
                }
            } catch (IOException ex) {
                throw ex;
            } catch (Throwable ex2) {
                LogUtil.m1565e(ex2.getMessage(), ex2);
                return null;
            }
        }
        BitmapFactory.Options bitmapOps = new BitmapFactory.Options();
        bitmapOps.inJustDecodeBounds = true;
        bitmapOps.inPurgeable = true;
        bitmapOps.inInputShareable = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), bitmapOps);
        bitmapOps.inJustDecodeBounds = false;
        bitmapOps.inPreferredConfig = options.getConfig();
        int rotateAngle = 0;
        int rawWidth = bitmapOps.outWidth;
        int rawHeight = bitmapOps.outHeight;
        int optionWith = options.getWidth();
        int optionHeight = options.getHeight();
        if (options.isAutoRotate()) {
            rotateAngle = getRotateAngle(file.getAbsolutePath());
            if ((rotateAngle / 90) % 2 == 1) {
                rawWidth = bitmapOps.outHeight;
                rawHeight = bitmapOps.outWidth;
            }
        }
        if (!options.isCrop() && optionWith > 0 && optionHeight > 0) {
            if ((rotateAngle / 90) % 2 == 1) {
                bitmapOps.outWidth = optionHeight;
                bitmapOps.outHeight = optionWith;
            } else {
                bitmapOps.outWidth = optionWith;
                bitmapOps.outHeight = optionHeight;
            }
        }
        bitmapOps.inSampleSize = calculateSampleSize(rawWidth, rawHeight, options.getMaxWidth(), options.getMaxHeight());
        if (cancelable == null || !cancelable.isCancelled()) {
            Bitmap bitmap = null;
            if (isWebP(file)) {
                bitmap = WebPFactory.decodeFile(file.getAbsolutePath(), bitmapOps);
            }
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), bitmapOps);
            }
            if (bitmap == null) {
                throw new IOException("decode image error");
            } else if (cancelable == null || !cancelable.isCancelled()) {
                if (rotateAngle != 0) {
                    bitmap = rotate(bitmap, rotateAngle, true);
                }
                if (cancelable == null || !cancelable.isCancelled()) {
                    if (options.isCrop() && optionWith > 0 && optionHeight > 0) {
                        bitmap = cut2ScaleSize(bitmap, optionWith, optionHeight, true);
                    }
                    if (bitmap == null) {
                        throw new IOException("decode image error");
                    } else if (cancelable == null || !cancelable.isCancelled()) {
                        if (options.isCircular()) {
                            bitmap = cut2Circular(bitmap, true);
                        } else if (options.getRadius() > 0) {
                            bitmap = cut2RoundCorner(bitmap, options.getRadius(), options.isSquare(), true);
                        } else if (options.isSquare()) {
                            bitmap = cut2Square(bitmap, true);
                        }
                        if (bitmap != null) {
                            return bitmap;
                        }
                        throw new IOException("decode image error");
                    } else {
                        throw new Callback.CancelledException("cancelled during decode image");
                    }
                } else {
                    throw new Callback.CancelledException("cancelled during decode image");
                }
            } else {
                throw new Callback.CancelledException("cancelled during decode image");
            }
        } else {
            throw new Callback.CancelledException("cancelled during decode image");
        }
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:32:0x0054=Splitter:B:32:0x0054, B:15:0x0028=Splitter:B:15:0x0028} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.graphics.Movie decodeGif(java.io.File r10, org.xutils.image.ImageOptions r11, org.xutils.common.Callback.Cancelable r12) throws java.io.IOException {
        /*
            r5 = 0
            if (r10 == 0) goto L_0x0013
            boolean r6 = r10.exists()
            if (r6 == 0) goto L_0x0013
            long r6 = r10.length()
            r8 = 1
            int r6 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r6 >= 0) goto L_0x0015
        L_0x0013:
            r4 = r5
        L_0x0014:
            return r4
        L_0x0015:
            r2 = 0
            if (r12 == 0) goto L_0x002e
            boolean r6 = r12.isCancelled()     // Catch:{ IOException -> 0x0027, Throwable -> 0x0053 }
            if (r6 == 0) goto L_0x002e
            org.xutils.common.Callback$CancelledException r6 = new org.xutils.common.Callback$CancelledException     // Catch:{ IOException -> 0x0027, Throwable -> 0x0053 }
            java.lang.String r7 = "cancelled during decode image"
            r6.<init>(r7)     // Catch:{ IOException -> 0x0027, Throwable -> 0x0053 }
            throw r6     // Catch:{ IOException -> 0x0027, Throwable -> 0x0053 }
        L_0x0027:
            r1 = move-exception
        L_0x0028:
            throw r1     // Catch:{ all -> 0x0029 }
        L_0x0029:
            r5 = move-exception
        L_0x002a:
            org.xutils.common.util.IOUtil.closeQuietly((java.io.Closeable) r2)
            throw r5
        L_0x002e:
            r0 = 16384(0x4000, float:2.2959E-41)
            java.io.BufferedInputStream r3 = new java.io.BufferedInputStream     // Catch:{ IOException -> 0x0027, Throwable -> 0x0053 }
            java.io.FileInputStream r6 = new java.io.FileInputStream     // Catch:{ IOException -> 0x0027, Throwable -> 0x0053 }
            r6.<init>(r10)     // Catch:{ IOException -> 0x0027, Throwable -> 0x0053 }
            r3.<init>(r6, r0)     // Catch:{ IOException -> 0x0027, Throwable -> 0x0053 }
            r3.mark(r0)     // Catch:{ IOException -> 0x004c, Throwable -> 0x0063, all -> 0x0060 }
            android.graphics.Movie r4 = android.graphics.Movie.decodeStream(r3)     // Catch:{ IOException -> 0x004c, Throwable -> 0x0063, all -> 0x0060 }
            if (r4 != 0) goto L_0x004f
            java.io.IOException r6 = new java.io.IOException     // Catch:{ IOException -> 0x004c, Throwable -> 0x0063, all -> 0x0060 }
            java.lang.String r7 = "decode image error"
            r6.<init>(r7)     // Catch:{ IOException -> 0x004c, Throwable -> 0x0063, all -> 0x0060 }
            throw r6     // Catch:{ IOException -> 0x004c, Throwable -> 0x0063, all -> 0x0060 }
        L_0x004c:
            r1 = move-exception
            r2 = r3
            goto L_0x0028
        L_0x004f:
            org.xutils.common.util.IOUtil.closeQuietly((java.io.Closeable) r3)
            goto L_0x0014
        L_0x0053:
            r1 = move-exception
        L_0x0054:
            java.lang.String r6 = r1.getMessage()     // Catch:{ all -> 0x0029 }
            org.xutils.common.util.LogUtil.m1565e(r6, r1)     // Catch:{ all -> 0x0029 }
            org.xutils.common.util.IOUtil.closeQuietly((java.io.Closeable) r2)
            r4 = r5
            goto L_0x0014
        L_0x0060:
            r5 = move-exception
            r2 = r3
            goto L_0x002a
        L_0x0063:
            r1 = move-exception
            r2 = r3
            goto L_0x0054
        */
        throw new UnsupportedOperationException("Method not decompiled: org.xutils.image.ImageDecoder.decodeGif(java.io.File, org.xutils.image.ImageOptions, org.xutils.common.Callback$Cancelable):android.graphics.Movie");
    }

    public static int calculateSampleSize(int rawWidth, int rawHeight, int maxWidth, int maxHeight) {
        int sampleSize = 1;
        if (rawWidth > maxWidth || rawHeight > maxHeight) {
            if (rawWidth > rawHeight) {
                sampleSize = Math.round(((float) rawHeight) / ((float) maxHeight));
            } else {
                sampleSize = Math.round(((float) rawWidth) / ((float) maxWidth));
            }
            if (sampleSize < 1) {
                sampleSize = 1;
            }
            while (((float) (rawWidth * rawHeight)) / ((float) (sampleSize * sampleSize)) > ((float) (maxWidth * maxHeight * 2))) {
                sampleSize++;
            }
        }
        return sampleSize;
    }

    public static Bitmap cut2Square(Bitmap source, boolean recycleSource) {
        int width = source.getWidth();
        int height = source.getHeight();
        if (width == height) {
            return source;
        }
        int squareWith = Math.min(width, height);
        Bitmap result = Bitmap.createBitmap(source, (width - squareWith) / 2, (height - squareWith) / 2, squareWith, squareWith);
        if (result == null) {
            result = source;
        } else if (recycleSource && result != source) {
            source.recycle();
        }
        return result;
    }

    public static Bitmap cut2Circular(Bitmap source, boolean recycleSource) {
        int width = source.getWidth();
        int height = source.getHeight();
        int diameter = Math.min(width, height);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap result = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888);
        if (result == null) {
            return source;
        }
        Canvas canvas = new Canvas(result);
        canvas.drawCircle((float) (diameter / 2), (float) (diameter / 2), (float) (diameter / 2), paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, (float) ((diameter - width) / 2), (float) ((diameter - height) / 2), paint);
        if (!recycleSource) {
            return result;
        }
        source.recycle();
        return result;
    }

    public static Bitmap cut2RoundCorner(Bitmap source, int radius, boolean isSquare, boolean recycleSource) {
        if (radius <= 0) {
            return source;
        }
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();
        int targetWidth = sourceWidth;
        int targetHeight = sourceHeight;
        if (isSquare) {
            targetHeight = Math.min(sourceWidth, sourceHeight);
            targetWidth = targetHeight;
        }
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap result = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);
        if (result != null) {
            Canvas canvas = new Canvas(result);
            canvas.drawRoundRect(new RectF(0.0f, 0.0f, (float) targetWidth, (float) targetHeight), (float) radius, (float) radius, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(source, (float) ((targetWidth - sourceWidth) / 2), (float) ((targetHeight - sourceHeight) / 2), paint);
            if (recycleSource) {
                source.recycle();
            }
        } else {
            result = source;
        }
        return result;
    }

    public static Bitmap cut2ScaleSize(Bitmap source, int dstWidth, int dstHeight, boolean recycleSource) {
        int l;
        int r;
        int t;
        int b;
        int width = source.getWidth();
        int height = source.getHeight();
        if (width == dstWidth && height == dstHeight) {
            return source;
        }
        Matrix m = new Matrix();
        int i = width;
        int i2 = height;
        float sx = ((float) dstWidth) / ((float) width);
        float sy = ((float) dstHeight) / ((float) height);
        if (sx > sy) {
            sy = sx;
            l = 0;
            r = width;
            t = (int) ((((float) height) - (((float) dstHeight) / sx)) / 2.0f);
            b = (int) ((((float) height) + (((float) dstHeight) / sx)) / 2.0f);
        } else {
            sx = sy;
            l = (int) ((((float) width) - (((float) dstWidth) / sx)) / 2.0f);
            r = (int) ((((float) width) + (((float) dstWidth) / sx)) / 2.0f);
            t = 0;
            b = height;
        }
        m.setScale(sx, sy);
        Bitmap result = Bitmap.createBitmap(source, l, t, r - l, b - t, m, true);
        if (result == null) {
            result = source;
        } else if (recycleSource && result != source) {
            source.recycle();
        }
        return result;
    }

    public static Bitmap rotate(Bitmap source, int angle, boolean recycleSource) {
        Bitmap result = null;
        if (angle != 0) {
            Matrix m = new Matrix();
            m.setRotate((float) angle);
            try {
                result = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), m, true);
            } catch (Throwable ex) {
                LogUtil.m1565e(ex.getMessage(), ex);
            }
        }
        if (result == null) {
            return source;
        }
        if (!recycleSource || result == source) {
            return result;
        }
        source.recycle();
        return result;
    }

    public static int getRotateAngle(String filePath) {
        try {
            switch (new ExifInterface(filePath).getAttributeInt(ScreenOrientationUtil.BC_OrientationChangedKey, 0)) {
                case 3:
                    return 180;
                case 6:
                    return 90;
                case 8:
                    return 270;
                default:
                    return 0;
            }
        } catch (Throwable ex) {
            LogUtil.m1565e(ex.getMessage(), ex);
            return 0;
        }
    }

    public static void compress(Bitmap bitmap, Bitmap.CompressFormat format, int quality, OutputStream out) throws IOException {
        if (format == Bitmap.CompressFormat.WEBP) {
            out.write(WebPFactory.encodeBitmap(bitmap, quality));
        } else {
            bitmap.compress(format, quality, out);
        }
    }

    /* access modifiers changed from: private */
    public static void saveThumbCache(File file, ImageOptions options, Bitmap thumbBitmap) {
        if (WebPFactory.available()) {
            DiskCacheEntity entity = new DiskCacheEntity();
            entity.setKey(file.getAbsolutePath() + "@" + file.lastModified() + options.toString());
            DiskCacheFile cacheFile = null;
            OutputStream out = null;
            try {
                cacheFile = THUMB_CACHE.createDiskCacheFile(entity);
                if (cacheFile != null) {
                    OutputStream out2 = new FileOutputStream(cacheFile);
                    try {
                        out2.write(WebPFactory.encodeBitmap(thumbBitmap, 80));
                        out2.flush();
                        cacheFile = cacheFile.commit();
                        out = out2;
                    } catch (Throwable th) {
                        th = th;
                        out = out2;
                        IOUtil.closeQuietly((Closeable) cacheFile);
                        IOUtil.closeQuietly((Closeable) out);
                        throw th;
                    }
                }
                IOUtil.closeQuietly((Closeable) cacheFile);
                IOUtil.closeQuietly((Closeable) out);
            } catch (Throwable th2) {
                ex = th2;
                IOUtil.deleteFileOrDir(cacheFile);
                LogUtil.m1571w(ex.getMessage(), ex);
                IOUtil.closeQuietly((Closeable) cacheFile);
                IOUtil.closeQuietly((Closeable) out);
            }
        }
    }

    private static Bitmap getThumbCache(File file, ImageOptions options) {
        Bitmap bitmap = null;
        if (WebPFactory.available()) {
            DiskCacheFile cacheFile = null;
            try {
                cacheFile = THUMB_CACHE.getDiskCacheFile(file.getAbsolutePath() + "@" + file.lastModified() + options.toString());
                if (cacheFile == null || !cacheFile.exists()) {
                    IOUtil.closeQuietly((Closeable) cacheFile);
                } else {
                    BitmapFactory.Options bitmapOps = new BitmapFactory.Options();
                    bitmapOps.inJustDecodeBounds = false;
                    bitmapOps.inPurgeable = true;
                    bitmapOps.inInputShareable = true;
                    bitmapOps.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    bitmap = WebPFactory.decodeFile(cacheFile.getAbsolutePath(), bitmapOps);
                }
            } catch (Throwable ex) {
                LogUtil.m1571w(ex.getMessage(), ex);
            } finally {
                IOUtil.closeQuietly((Closeable) cacheFile);
            }
        }
        return bitmap;
    }
}
