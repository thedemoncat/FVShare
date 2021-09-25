package p010me.iwf.photopicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.p001v4.app.Fragment;
import java.util.ArrayList;

/* renamed from: me.iwf.photopicker.PhotoPreview */
public class PhotoPreview {
    public static final String EXTRA_CURRENT_ITEM = "current_item";
    public static final String EXTRA_PHOTOS = "photos";
    public static final String EXTRA_SHOW_DELETE = "show_delete";
    public static final int REQUEST_CODE = 666;

    public static PhotoPreviewBuilder builder() {
        return new PhotoPreviewBuilder();
    }

    /* renamed from: me.iwf.photopicker.PhotoPreview$PhotoPreviewBuilder */
    public static class PhotoPreviewBuilder {
        private Intent mPreviewIntent = new Intent();
        private Bundle mPreviewOptionsBundle = new Bundle();

        public void start(@NonNull Activity activity, int requestCode) {
            activity.startActivityForResult(getIntent(activity), requestCode);
        }

        public void start(@NonNull Context context, @NonNull Fragment fragment, int requestCode) {
            fragment.startActivityForResult(getIntent(context), requestCode);
        }

        public void start(@NonNull Context context, @NonNull Fragment fragment) {
            fragment.startActivityForResult(getIntent(context), PhotoPreview.REQUEST_CODE);
        }

        public void start(@NonNull Activity activity) {
            start(activity, (int) PhotoPreview.REQUEST_CODE);
        }

        public Intent getIntent(@NonNull Context context) {
            this.mPreviewIntent.setClass(context, PhotoPagerActivity.class);
            this.mPreviewIntent.putExtras(this.mPreviewOptionsBundle);
            return this.mPreviewIntent;
        }

        public PhotoPreviewBuilder setPhotos(ArrayList<String> photoPaths) {
            this.mPreviewOptionsBundle.putStringArrayList(PhotoPreview.EXTRA_PHOTOS, photoPaths);
            return this;
        }

        public PhotoPreviewBuilder setCurrentItem(int currentItem) {
            this.mPreviewOptionsBundle.putInt(PhotoPreview.EXTRA_CURRENT_ITEM, currentItem);
            return this;
        }

        public PhotoPreviewBuilder setShowDeleteButton(boolean showDeleteButton) {
            this.mPreviewOptionsBundle.putBoolean(PhotoPreview.EXTRA_SHOW_DELETE, showDeleteButton);
            return this;
        }
    }
}
