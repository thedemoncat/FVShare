package com.lzy.okgo.convert;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class BitmapConvert implements Converter<Bitmap> {
    private Bitmap.Config decodeConfig;
    private int maxHeight;
    private int maxWidth;
    private ImageView.ScaleType scaleType;

    public BitmapConvert() {
        this(1000, 1000, Bitmap.Config.ARGB_8888, ImageView.ScaleType.CENTER_INSIDE);
    }

    public BitmapConvert(int maxWidth2, int maxHeight2) {
        this(maxWidth2, maxHeight2, Bitmap.Config.ARGB_8888, ImageView.ScaleType.CENTER_INSIDE);
    }

    public BitmapConvert(int maxWidth2, int maxHeight2, Bitmap.Config decodeConfig2, ImageView.ScaleType scaleType2) {
        this.maxWidth = maxWidth2;
        this.maxHeight = maxHeight2;
        this.decodeConfig = decodeConfig2;
        this.scaleType = scaleType2;
    }

    public Bitmap convertResponse(Response response) throws Throwable {
        ResponseBody body = response.body();
        if (body == null) {
            return null;
        }
        return parse(body.bytes());
    }

    private Bitmap parse(byte[] byteArray) throws OutOfMemoryError {
        BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
        if (this.maxWidth == 0 && this.maxHeight == 0) {
            decodeOptions.inPreferredConfig = this.decodeConfig;
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, decodeOptions);
        }
        decodeOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, decodeOptions);
        int actualWidth = decodeOptions.outWidth;
        int actualHeight = decodeOptions.outHeight;
        int desiredWidth = getResizedDimension(this.maxWidth, this.maxHeight, actualWidth, actualHeight, this.scaleType);
        int desiredHeight = getResizedDimension(this.maxHeight, this.maxWidth, actualHeight, actualWidth, this.scaleType);
        decodeOptions.inJustDecodeBounds = false;
        decodeOptions.inSampleSize = findBestSampleSize(actualWidth, actualHeight, desiredWidth, desiredHeight);
        Bitmap tempBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, decodeOptions);
        if (tempBitmap == null || (tempBitmap.getWidth() <= desiredWidth && tempBitmap.getHeight() <= desiredHeight)) {
            return tempBitmap;
        }
        Bitmap bitmap = Bitmap.createScaledBitmap(tempBitmap, desiredWidth, desiredHeight, true);
        tempBitmap.recycle();
        return bitmap;
    }

    private static int getResizedDimension(int maxPrimary, int maxSecondary, int actualPrimary, int actualSecondary, ImageView.ScaleType scaleType2) {
        if (maxPrimary == 0 && maxSecondary == 0) {
            return actualPrimary;
        }
        if (scaleType2 == ImageView.ScaleType.FIT_XY) {
            if (maxPrimary != 0) {
                return maxPrimary;
            }
            return actualPrimary;
        } else if (maxPrimary == 0) {
            return (int) (((double) actualPrimary) * (((double) maxSecondary) / ((double) actualSecondary)));
        } else if (maxSecondary == 0) {
            return maxPrimary;
        } else {
            double ratio = ((double) actualSecondary) / ((double) actualPrimary);
            int resized = maxPrimary;
            if (scaleType2 == ImageView.ScaleType.CENTER_CROP) {
                if (((double) resized) * ratio < ((double) maxSecondary)) {
                    resized = (int) (((double) maxSecondary) / ratio);
                }
                return resized;
            }
            if (((double) resized) * ratio > ((double) maxSecondary)) {
                resized = (int) (((double) maxSecondary) / ratio);
            }
            return resized;
        }
    }

    private static int findBestSampleSize(int actualWidth, int actualHeight, int desiredWidth, int desiredHeight) {
        float n = 1.0f;
        while (((double) (2.0f * n)) <= Math.min(((double) actualWidth) / ((double) desiredWidth), ((double) actualHeight) / ((double) desiredHeight))) {
            n *= 2.0f;
        }
        return (int) n;
    }
}
