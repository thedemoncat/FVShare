package p010me.iwf.photopicker.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.p001v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/* renamed from: me.iwf.photopicker.utils.ImageCaptureManager */
public class ImageCaptureManager {
    private static final String CAPTURED_PHOTO_PATH_KEY = "mCurrentPhotoPath";
    public static final int REQUEST_TAKE_PHOTO = 1;
    private Context mContext;
    private String mCurrentPhotoPath;

    public ImageCaptureManager(Context mContext2) {
        this.mContext = mContext2;
    }

    private File createImageFile() throws IOException {
        String imageFileName = "JPEG_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date()) + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (storageDir.exists() || storageDir.mkdir()) {
            File image = new File(storageDir, imageFileName);
            this.mCurrentPhotoPath = image.getAbsolutePath();
            return image;
        }
        Log.e("TAG", "Throwing Errors....");
        throw new IOException();
    }

    public Intent dispatchTakePictureIntent() throws IOException {
        Uri photoFile;
        Intent takePictureIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (takePictureIntent.resolveActivity(this.mContext.getPackageManager()) != null) {
            File file = createImageFile();
            if (Build.VERSION.SDK_INT >= 24) {
                photoFile = FileProvider.getUriForFile(this.mContext.getApplicationContext(), this.mContext.getApplicationInfo().packageName + ".provider", file);
            } else {
                photoFile = Uri.fromFile(file);
            }
            if (photoFile != null) {
                takePictureIntent.putExtra("output", photoFile);
            }
        }
        return takePictureIntent;
    }

    public void galleryAddPic() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        if (!TextUtils.isEmpty(this.mCurrentPhotoPath)) {
            mediaScanIntent.setData(Uri.fromFile(new File(this.mCurrentPhotoPath)));
            this.mContext.sendBroadcast(mediaScanIntent);
        }
    }

    public String getCurrentPhotoPath() {
        return this.mCurrentPhotoPath;
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null && this.mCurrentPhotoPath != null) {
            savedInstanceState.putString(CAPTURED_PHOTO_PATH_KEY, this.mCurrentPhotoPath);
        }
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(CAPTURED_PHOTO_PATH_KEY)) {
            this.mCurrentPhotoPath = savedInstanceState.getString(CAPTURED_PHOTO_PATH_KEY);
        }
    }
}
