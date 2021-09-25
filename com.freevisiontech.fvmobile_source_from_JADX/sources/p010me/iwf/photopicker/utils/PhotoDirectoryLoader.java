package p010me.iwf.photopicker.utils;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.p001v4.content.CursorLoader;
import com.android.vending.expansion.zipfile.APEZProvider;

/* renamed from: me.iwf.photopicker.utils.PhotoDirectoryLoader */
public class PhotoDirectoryLoader extends CursorLoader {
    final String[] IMAGE_PROJECTION = {APEZProvider.FILEID, "_data", "bucket_id", "bucket_display_name", "date_added", "_size"};

    public PhotoDirectoryLoader(Context context, boolean showGif) {
        super(context);
        setProjection(this.IMAGE_PROJECTION);
        setUri(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        setSortOrder("date_added DESC");
        setSelection("mime_type=? or mime_type=? or mime_type=? " + (showGif ? "or mime_type=?" : ""));
        setSelectionArgs(showGif ? new String[]{"image/jpeg", "image/png", "image/jpg", "image/gif"} : new String[]{"image/jpeg", "image/png", "image/jpg"});
    }

    private PhotoDirectoryLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
    }
}
