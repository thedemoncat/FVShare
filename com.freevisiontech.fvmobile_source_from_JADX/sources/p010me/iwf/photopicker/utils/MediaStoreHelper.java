package p010me.iwf.photopicker.utils;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.p001v4.app.FragmentActivity;
import android.support.p001v4.app.LoaderManager;
import android.support.p001v4.content.Loader;
import com.android.vending.expansion.zipfile.APEZProvider;
import java.util.ArrayList;
import java.util.List;
import p010me.iwf.photopicker.C1661R;
import p010me.iwf.photopicker.PhotoPicker;
import p010me.iwf.photopicker.entity.PhotoDirectory;

/* renamed from: me.iwf.photopicker.utils.MediaStoreHelper */
public class MediaStoreHelper {
    public static final int INDEX_ALL_PHOTOS = 0;

    /* renamed from: me.iwf.photopicker.utils.MediaStoreHelper$PhotosResultCallback */
    public interface PhotosResultCallback {
        void onResultCallback(List<PhotoDirectory> list);
    }

    public static void getPhotoDirs(FragmentActivity activity, Bundle args, PhotosResultCallback resultCallback) {
        activity.getSupportLoaderManager().initLoader(0, args, new PhotoDirLoaderCallbacks(activity, resultCallback));
    }

    /* renamed from: me.iwf.photopicker.utils.MediaStoreHelper$PhotoDirLoaderCallbacks */
    private static class PhotoDirLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {
        private Context context;
        private PhotosResultCallback resultCallback;

        public PhotoDirLoaderCallbacks(Context context2, PhotosResultCallback resultCallback2) {
            this.context = context2;
            this.resultCallback = resultCallback2;
        }

        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new PhotoDirectoryLoader(this.context, args.getBoolean(PhotoPicker.EXTRA_SHOW_GIF, false));
        }

        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                List<PhotoDirectory> directories = new ArrayList<>();
                PhotoDirectory photoDirectoryAll = new PhotoDirectory();
                photoDirectoryAll.setName(this.context.getString(C1661R.string.__picker_all_image));
                photoDirectoryAll.setId("ALL");
                while (data.moveToNext()) {
                    int imageId = data.getInt(data.getColumnIndexOrThrow(APEZProvider.FILEID));
                    String bucketId = data.getString(data.getColumnIndexOrThrow("bucket_id"));
                    String name = data.getString(data.getColumnIndexOrThrow("bucket_display_name"));
                    String path = data.getString(data.getColumnIndexOrThrow("_data"));
                    if (((long) data.getInt(data.getColumnIndexOrThrow("_size"))) >= 1) {
                        PhotoDirectory photoDirectory = new PhotoDirectory();
                        photoDirectory.setId(bucketId);
                        photoDirectory.setName(name);
                        if (!directories.contains(photoDirectory)) {
                            photoDirectory.setCoverPath(path);
                            photoDirectory.addPhoto(imageId, path);
                            photoDirectory.setDateAdded(data.getLong(data.getColumnIndexOrThrow("date_added")));
                            directories.add(photoDirectory);
                        } else {
                            directories.get(directories.indexOf(photoDirectory)).addPhoto(imageId, path);
                        }
                        photoDirectoryAll.addPhoto(imageId, path);
                    }
                }
                if (photoDirectoryAll.getPhotoPaths().size() > 0) {
                    photoDirectoryAll.setCoverPath(photoDirectoryAll.getPhotoPaths().get(0));
                }
                directories.add(0, photoDirectoryAll);
                if (this.resultCallback != null) {
                    this.resultCallback.onResultCallback(directories);
                }
            }
        }

        public void onLoaderReset(Loader<Cursor> loader) {
        }
    }
}
