package com.yalantis.ucrop.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import com.yalantis.ucrop.callback.BitmapLoadCallback;
import com.yalantis.ucrop.task.BitmapLoadTask;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public class BitmapLoadUtils {
    private static final String TAG = "BitmapLoadUtils";

    public static void decodeBitmapInBackground(@NonNull Context context, @NonNull Uri uri, @Nullable Uri outputUri, int requiredWidth, int requiredHeight, BitmapLoadCallback loadCallback) {
        new BitmapLoadTask(context, uri, outputUri, requiredWidth, requiredHeight, loadCallback).execute(new Void[0]);
    }

    public static Bitmap transformBitmap(@NonNull Bitmap bitmap, @NonNull Matrix transformMatrix) {
        try {
            Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), transformMatrix, true);
            if (!bitmap.sameAs(converted)) {
                return converted;
            }
            return bitmap;
        } catch (OutOfMemoryError error) {
            Log.e(TAG, "transformBitmap: ", error);
            return bitmap;
        }
    }

    public static int calculateInSampleSize(@NonNull BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            while (true) {
                if (height / inSampleSize <= reqHeight && width / inSampleSize <= reqWidth) {
                    break;
                }
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static int getExifOrientation(@NonNull Context context, @NonNull Uri imageUri) {
        int orientation = 0;
        try {
            InputStream stream = context.getContentResolver().openInputStream(imageUri);
            if (stream == null) {
                return 0;
            }
            orientation = new ImageHeaderParser(stream).getOrientation();
            close(stream);
            return orientation;
        } catch (IOException e) {
            Log.e(TAG, "getExifOrientation: " + imageUri.toString(), e);
        }
    }

    public static int exifToDegrees(int exifOrientation) {
        switch (exifOrientation) {
            case 3:
            case 4:
                return 180;
            case 5:
            case 6:
                return 90;
            case 7:
            case 8:
                return 270;
            default:
                return 0;
        }
    }

    public static int exifToTranslation(int exifOrientation) {
        switch (exifOrientation) {
            case 2:
            case 4:
            case 5:
            case 7:
                return -1;
            default:
                return 1;
        }
    }

    public static int calculateMaxBitmapSize(@NonNull Context context) {
        int width;
        int height;
        Display display = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        Point size = new Point();
        if (Build.VERSION.SDK_INT >= 13) {
            display.getSize(size);
            width = size.x;
            height = size.y;
        } else {
            width = display.getWidth();
            height = display.getHeight();
        }
        int maxBitmapSize = (int) Math.sqrt(Math.pow((double) width, 2.0d) + Math.pow((double) height, 2.0d));
        Canvas canvas = new Canvas();
        int maxCanvasSize = Math.min(canvas.getMaximumBitmapWidth(), canvas.getMaximumBitmapHeight());
        if (maxCanvasSize > 0) {
            maxBitmapSize = Math.min(maxBitmapSize, maxCanvasSize);
        }
        int maxTextureSize = EglUtils.getMaxTextureSize();
        if (maxTextureSize > 0) {
            maxBitmapSize = Math.min(maxBitmapSize, maxTextureSize);
        }
        Log.d(TAG, "maxBitmapSize: " + maxBitmapSize);
        return maxBitmapSize;
    }

    public static void close(@Nullable Closeable c) {
        if (c != null && (c instanceof Closeable)) {
            try {
                c.close();
            } catch (IOException e) {
            }
        }
    }
}
