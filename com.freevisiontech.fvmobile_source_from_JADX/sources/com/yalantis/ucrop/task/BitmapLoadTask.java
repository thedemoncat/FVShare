package com.yalantis.ucrop.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.p001v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import com.yalantis.ucrop.callback.BitmapLoadCallback;
import com.yalantis.ucrop.model.ExifInfo;
import com.yalantis.ucrop.util.BitmapLoadUtils;
import com.yalantis.ucrop.util.FileUtils;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSource;
import okio.Okio;
import okio.Sink;

public class BitmapLoadTask extends AsyncTask<Void, Void, BitmapWorkerResult> {
    private static final String TAG = "BitmapWorkerTask";
    private final BitmapLoadCallback mBitmapLoadCallback;
    private final Context mContext;
    private Uri mInputUri;
    private Uri mOutputUri;
    private final int mRequiredHeight;
    private final int mRequiredWidth;

    public static class BitmapWorkerResult {
        Bitmap mBitmapResult;
        Exception mBitmapWorkerException;
        ExifInfo mExifInfo;

        public BitmapWorkerResult(@NonNull Bitmap bitmapResult, @NonNull ExifInfo exifInfo) {
            this.mBitmapResult = bitmapResult;
            this.mExifInfo = exifInfo;
        }

        public BitmapWorkerResult(@NonNull Exception bitmapWorkerException) {
            this.mBitmapWorkerException = bitmapWorkerException;
        }
    }

    public BitmapLoadTask(@NonNull Context context, @NonNull Uri inputUri, @Nullable Uri outputUri, int requiredWidth, int requiredHeight, BitmapLoadCallback loadCallback) {
        this.mContext = context;
        this.mInputUri = inputUri;
        this.mOutputUri = outputUri;
        this.mRequiredWidth = requiredWidth;
        this.mRequiredHeight = requiredHeight;
        this.mBitmapLoadCallback = loadCallback;
    }

    /* access modifiers changed from: protected */
    @NonNull
    public BitmapWorkerResult doInBackground(Void... params) {
        if (this.mInputUri == null) {
            return new BitmapWorkerResult(new NullPointerException("Input Uri cannot be null"));
        }
        try {
            processInputUri();
            try {
                ParcelFileDescriptor parcelFileDescriptor = this.mContext.getContentResolver().openFileDescriptor(this.mInputUri, "r");
                if (parcelFileDescriptor == null) {
                    return new BitmapWorkerResult(new NullPointerException("ParcelFileDescriptor was null for given Uri: [" + this.mInputUri + "]"));
                }
                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFileDescriptor(fileDescriptor, (Rect) null, options);
                if (options.outWidth == -1 || options.outHeight == -1) {
                    return new BitmapWorkerResult(new IllegalArgumentException("Bounds for bitmap could not be retrieved from the Uri: [" + this.mInputUri + "]"));
                }
                options.inSampleSize = BitmapLoadUtils.calculateInSampleSize(options, this.mRequiredWidth, this.mRequiredHeight);
                options.inJustDecodeBounds = false;
                Bitmap decodeSampledBitmap = null;
                boolean decodeAttemptSuccess = false;
                while (!decodeAttemptSuccess) {
                    try {
                        decodeSampledBitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor, (Rect) null, options);
                        decodeAttemptSuccess = true;
                    } catch (OutOfMemoryError error) {
                        Log.e(TAG, "doInBackground: BitmapFactory.decodeFileDescriptor: ", error);
                        options.inSampleSize *= 2;
                    }
                }
                if (decodeSampledBitmap == null) {
                    return new BitmapWorkerResult(new IllegalArgumentException("Bitmap could not be decoded from the Uri: [" + this.mInputUri + "]"));
                }
                if (Build.VERSION.SDK_INT >= 16) {
                    BitmapLoadUtils.close(parcelFileDescriptor);
                }
                int exifOrientation = BitmapLoadUtils.getExifOrientation(this.mContext, this.mInputUri);
                int exifDegrees = BitmapLoadUtils.exifToDegrees(exifOrientation);
                int exifTranslation = BitmapLoadUtils.exifToTranslation(exifOrientation);
                ExifInfo exifInfo = new ExifInfo(exifOrientation, exifDegrees, exifTranslation);
                Matrix matrix = new Matrix();
                if (exifDegrees != 0) {
                    matrix.preRotate((float) exifDegrees);
                }
                if (exifTranslation != 1) {
                    matrix.postScale((float) exifTranslation, 1.0f);
                }
                if (!matrix.isIdentity()) {
                    return new BitmapWorkerResult(BitmapLoadUtils.transformBitmap(decodeSampledBitmap, matrix), exifInfo);
                }
                return new BitmapWorkerResult(decodeSampledBitmap, exifInfo);
            } catch (FileNotFoundException e) {
                return new BitmapWorkerResult(e);
            }
        } catch (IOException | NullPointerException e2) {
            return new BitmapWorkerResult(e2);
        }
    }

    private void processInputUri() throws NullPointerException, IOException {
        String inputUriScheme = this.mInputUri.getScheme();
        Log.d(TAG, "Uri scheme: " + inputUriScheme);
        if ("http".equals(inputUriScheme) || "https".equals(inputUriScheme)) {
            try {
                downloadFile(this.mInputUri, this.mOutputUri);
            } catch (IOException | NullPointerException e) {
                Log.e(TAG, "Downloading failed", e);
                throw e;
            }
        } else if ("content".equals(inputUriScheme)) {
            String path = getFilePath();
            if (TextUtils.isEmpty(path) || !new File(path).exists()) {
                try {
                    copyFile(this.mInputUri, this.mOutputUri);
                } catch (IOException | NullPointerException e2) {
                    Log.e(TAG, "Copying failed", e2);
                    throw e2;
                }
            } else {
                this.mInputUri = Uri.fromFile(new File(path));
            }
        } else if (!"file".equals(inputUriScheme)) {
            Log.e(TAG, "Invalid Uri scheme " + inputUriScheme);
            throw new IllegalArgumentException("Invalid Uri scheme" + inputUriScheme);
        }
    }

    private String getFilePath() {
        if (ContextCompat.checkSelfPermission(this.mContext, "android.permission.READ_EXTERNAL_STORAGE") == 0) {
            return FileUtils.getPath(this.mContext, this.mInputUri);
        }
        return null;
    }

    private void copyFile(@NonNull Uri inputUri, @Nullable Uri outputUri) throws NullPointerException, IOException {
        Log.d(TAG, "copyFile");
        if (outputUri == null) {
            throw new NullPointerException("Output Uri is null - cannot copy image");
        }
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = this.mContext.getContentResolver().openInputStream(inputUri);
            OutputStream outputStream2 = new FileOutputStream(new File(outputUri.getPath()));
            if (inputStream == null) {
                try {
                    throw new NullPointerException("InputStream for given input Uri is null");
                } catch (Throwable th) {
                    th = th;
                    outputStream = outputStream2;
                    BitmapLoadUtils.close(outputStream);
                    BitmapLoadUtils.close(inputStream);
                    this.mInputUri = this.mOutputUri;
                    throw th;
                }
            } else {
                byte[] buffer = new byte[1024];
                while (true) {
                    int length = inputStream.read(buffer);
                    if (length > 0) {
                        outputStream2.write(buffer, 0, length);
                    } else {
                        BitmapLoadUtils.close(outputStream2);
                        BitmapLoadUtils.close(inputStream);
                        this.mInputUri = this.mOutputUri;
                        return;
                    }
                }
            }
        } catch (Throwable th2) {
            th = th2;
            BitmapLoadUtils.close(outputStream);
            BitmapLoadUtils.close(inputStream);
            this.mInputUri = this.mOutputUri;
            throw th;
        }
    }

    private void downloadFile(@NonNull Uri inputUri, @Nullable Uri outputUri) throws NullPointerException, IOException {
        Log.d(TAG, "downloadFile");
        if (outputUri == null) {
            throw new NullPointerException("Output Uri is null - cannot download image");
        }
        OkHttpClient client = new OkHttpClient();
        BufferedSource source = null;
        Sink sink = null;
        Response response = null;
        try {
            response = client.newCall(new Request.Builder().url(inputUri.toString()).build()).execute();
            source = response.body().source();
            OutputStream outputStream = this.mContext.getContentResolver().openOutputStream(outputUri);
            if (outputStream != null) {
                sink = Okio.sink(outputStream);
                source.readAll(sink);
                return;
            }
            throw new NullPointerException("OutputStream for given output Uri is null");
        } finally {
            BitmapLoadUtils.close(source);
            BitmapLoadUtils.close(sink);
            if (response != null) {
                BitmapLoadUtils.close(response.body());
            }
            client.dispatcher().cancelAll();
            this.mInputUri = this.mOutputUri;
        }
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(@NonNull BitmapWorkerResult result) {
        if (result.mBitmapWorkerException == null) {
            this.mBitmapLoadCallback.onBitmapLoaded(result.mBitmapResult, result.mExifInfo, this.mInputUri.getPath(), this.mOutputUri == null ? null : this.mOutputUri.getPath());
        } else {
            this.mBitmapLoadCallback.onFailure(result.mBitmapWorkerException);
        }
    }
}
