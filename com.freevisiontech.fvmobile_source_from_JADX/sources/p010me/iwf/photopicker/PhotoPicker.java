package p010me.iwf.photopicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.p001v4.app.Fragment;
import java.util.ArrayList;
import p010me.iwf.photopicker.utils.PermissionsUtils;

/* renamed from: me.iwf.photopicker.PhotoPicker */
public class PhotoPicker {
    public static final int DEFAULT_COLUMN_NUMBER = 3;
    public static final int DEFAULT_MAX_COUNT = 9;
    public static final String EXTRA_GRID_COLUMN = "column";
    public static final String EXTRA_MAX_COUNT = "MAX_COUNT";
    public static final String EXTRA_ORIGINAL_PHOTOS = "ORIGINAL_PHOTOS";
    public static final String EXTRA_PREVIEW_ENABLED = "PREVIEW_ENABLED";
    public static final String EXTRA_SHOW_CAMERA = "SHOW_CAMERA";
    public static final String EXTRA_SHOW_GIF = "SHOW_GIF";
    public static final String KEY_SELECTED_PHOTOS = "SELECTED_PHOTOS";
    public static final int REQUEST_CODE = 233;

    public static PhotoPickerBuilder builder() {
        return new PhotoPickerBuilder();
    }

    /* renamed from: me.iwf.photopicker.PhotoPicker$PhotoPickerBuilder */
    public static class PhotoPickerBuilder {
        private Intent mPickerIntent = new Intent();
        private Bundle mPickerOptionsBundle = new Bundle();

        public void start(@NonNull Activity activity, int requestCode) {
            if (PermissionsUtils.checkReadStoragePermission(activity)) {
                activity.startActivityForResult(getIntent(activity), requestCode);
            }
        }

        public void start(@NonNull Context context, @NonNull Fragment fragment, int requestCode) {
            if (PermissionsUtils.checkReadStoragePermission(fragment.getActivity())) {
                fragment.startActivityForResult(getIntent(context), requestCode);
            }
        }

        public void start(@NonNull Context context, @NonNull Fragment fragment) {
            if (PermissionsUtils.checkReadStoragePermission(fragment.getActivity())) {
                fragment.startActivityForResult(getIntent(context), 233);
            }
        }

        public Intent getIntent(@NonNull Context context) {
            this.mPickerIntent.setClass(context, PhotoPickerActivity.class);
            this.mPickerIntent.putExtras(this.mPickerOptionsBundle);
            return this.mPickerIntent;
        }

        public void start(@NonNull Activity activity) {
            start(activity, 233);
        }

        public PhotoPickerBuilder setPhotoCount(int photoCount) {
            this.mPickerOptionsBundle.putInt(PhotoPicker.EXTRA_MAX_COUNT, photoCount);
            return this;
        }

        public PhotoPickerBuilder setGridColumnCount(int columnCount) {
            this.mPickerOptionsBundle.putInt(PhotoPicker.EXTRA_GRID_COLUMN, columnCount);
            return this;
        }

        public PhotoPickerBuilder setShowGif(boolean showGif) {
            this.mPickerOptionsBundle.putBoolean(PhotoPicker.EXTRA_SHOW_GIF, showGif);
            return this;
        }

        public PhotoPickerBuilder setShowCamera(boolean showCamera) {
            this.mPickerOptionsBundle.putBoolean(PhotoPicker.EXTRA_SHOW_CAMERA, showCamera);
            return this;
        }

        public PhotoPickerBuilder setSelected(ArrayList<String> imagesUri) {
            this.mPickerOptionsBundle.putStringArrayList(PhotoPicker.EXTRA_ORIGINAL_PHOTOS, imagesUri);
            return this;
        }

        public PhotoPickerBuilder setPreviewEnabled(boolean previewEnabled) {
            this.mPickerOptionsBundle.putBoolean(PhotoPicker.EXTRA_PREVIEW_ENABLED, previewEnabled);
            return this;
        }
    }
}
