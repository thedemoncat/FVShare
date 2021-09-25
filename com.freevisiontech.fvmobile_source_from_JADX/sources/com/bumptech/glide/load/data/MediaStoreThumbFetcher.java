package com.bumptech.glide.load.data;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.resource.bitmap.ImageHeaderParser;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MediaStoreThumbFetcher implements DataFetcher<InputStream> {
    private static final ThumbnailStreamOpenerFactory DEFAULT_FACTORY = new ThumbnailStreamOpenerFactory();
    private static final int MINI_HEIGHT = 384;
    private static final int MINI_WIDTH = 512;
    private static final String TAG = "MediaStoreThumbFetcher";
    private final Context context;
    private final DataFetcher<InputStream> defaultFetcher;
    private final ThumbnailStreamOpenerFactory factory;
    private final int height;
    private InputStream inputStream;
    private final Uri mediaStoreUri;
    private final int width;

    interface ThumbnailQuery {
        Cursor queryPath(Context context, Uri uri);
    }

    public MediaStoreThumbFetcher(Context context2, Uri mediaStoreUri2, DataFetcher<InputStream> defaultFetcher2, int width2, int height2) {
        this(context2, mediaStoreUri2, defaultFetcher2, width2, height2, DEFAULT_FACTORY);
    }

    MediaStoreThumbFetcher(Context context2, Uri mediaStoreUri2, DataFetcher<InputStream> defaultFetcher2, int width2, int height2, ThumbnailStreamOpenerFactory factory2) {
        this.context = context2;
        this.mediaStoreUri = mediaStoreUri2;
        this.defaultFetcher = defaultFetcher2;
        this.width = width2;
        this.height = height2;
        this.factory = factory2;
    }

    public InputStream loadData(Priority priority) throws Exception {
        ThumbnailStreamOpener fetcher = this.factory.build(this.mediaStoreUri, this.width, this.height);
        if (fetcher != null) {
            this.inputStream = openThumbInputStream(fetcher);
        }
        if (this.inputStream == null) {
            this.inputStream = this.defaultFetcher.loadData(priority);
        }
        return this.inputStream;
    }

    private InputStream openThumbInputStream(ThumbnailStreamOpener fetcher) {
        InputStream result;
        try {
            result = fetcher.open(this.context, this.mediaStoreUri);
        } catch (FileNotFoundException e) {
            if (Log.isLoggable(TAG, 3)) {
                Log.d(TAG, "Failed to find thumbnail file", e);
            }
            result = null;
        }
        int orientation = -1;
        if (result != null) {
            orientation = fetcher.getOrientation(this.context, this.mediaStoreUri);
        }
        if (orientation != -1) {
            return new ExifOrientationStream(result, orientation);
        }
        return result;
    }

    public void cleanup() {
        if (this.inputStream != null) {
            try {
                this.inputStream.close();
            } catch (IOException e) {
            }
        }
        this.defaultFetcher.cleanup();
    }

    public String getId() {
        return this.mediaStoreUri.toString();
    }

    public void cancel() {
    }

    /* access modifiers changed from: private */
    public static boolean isMediaStoreUri(Uri uri) {
        return uri != null && "content".equals(uri.getScheme()) && "media".equals(uri.getAuthority());
    }

    /* access modifiers changed from: private */
    public static boolean isMediaStoreVideo(Uri uri) {
        return isMediaStoreUri(uri) && uri.getPathSegments().contains("video");
    }

    static class FileService {
        FileService() {
        }

        public boolean exists(File file) {
            return file.exists();
        }

        public long length(File file) {
            return file.length();
        }

        public File get(String path) {
            return new File(path);
        }
    }

    static class ThumbnailStreamOpener {
        private static final FileService DEFAULT_SERVICE = new FileService();
        private ThumbnailQuery query;
        private final FileService service;

        public ThumbnailStreamOpener(ThumbnailQuery query2) {
            this(DEFAULT_SERVICE, query2);
        }

        public ThumbnailStreamOpener(FileService service2, ThumbnailQuery query2) {
            this.service = service2;
            this.query = query2;
        }

        public int getOrientation(Context context, Uri uri) {
            int orientation = -1;
            InputStream is = null;
            try {
                is = context.getContentResolver().openInputStream(uri);
                orientation = new ImageHeaderParser(is).getOrientation();
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                    }
                }
            } catch (IOException e2) {
                if (Log.isLoggable(MediaStoreThumbFetcher.TAG, 3)) {
                    Log.d(MediaStoreThumbFetcher.TAG, "Failed to open uri: " + uri, e2);
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e3) {
                    }
                }
            } catch (Throwable th) {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e4) {
                    }
                }
                throw th;
            }
            return orientation;
        }

        public InputStream open(Context context, Uri uri) throws FileNotFoundException {
            Uri thumbnailUri = null;
            Cursor cursor = this.query.queryPath(context, uri);
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        thumbnailUri = parseThumbUri(cursor);
                    }
                } catch (Throwable th) {
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            }
            if (cursor != null) {
                cursor.close();
            }
            if (thumbnailUri != null) {
                return context.getContentResolver().openInputStream(thumbnailUri);
            }
            return null;
        }

        private Uri parseThumbUri(Cursor cursor) {
            String path = cursor.getString(0);
            if (TextUtils.isEmpty(path)) {
                return null;
            }
            File file = this.service.get(path);
            if (!this.service.exists(file) || this.service.length(file) <= 0) {
                return null;
            }
            return Uri.fromFile(file);
        }
    }

    static class ImageThumbnailQuery implements ThumbnailQuery {
        private static final String[] PATH_PROJECTION = {"_data"};
        private static final String PATH_SELECTION = "kind = 1 AND image_id = ?";

        ImageThumbnailQuery() {
        }

        public Cursor queryPath(Context context, Uri uri) {
            String imageId = uri.getLastPathSegment();
            return context.getContentResolver().query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, PATH_PROJECTION, PATH_SELECTION, new String[]{imageId}, (String) null);
        }
    }

    static class VideoThumbnailQuery implements ThumbnailQuery {
        private static final String[] PATH_PROJECTION = {"_data"};
        private static final String PATH_SELECTION = "kind = 1 AND video_id = ?";

        VideoThumbnailQuery() {
        }

        public Cursor queryPath(Context context, Uri uri) {
            String videoId = uri.getLastPathSegment();
            return context.getContentResolver().query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, PATH_PROJECTION, PATH_SELECTION, new String[]{videoId}, (String) null);
        }
    }

    static class ThumbnailStreamOpenerFactory {
        ThumbnailStreamOpenerFactory() {
        }

        public ThumbnailStreamOpener build(Uri uri, int width, int height) {
            if (!MediaStoreThumbFetcher.isMediaStoreUri(uri) || width > 512 || height > MediaStoreThumbFetcher.MINI_HEIGHT) {
                return null;
            }
            if (MediaStoreThumbFetcher.isMediaStoreVideo(uri)) {
                return new ThumbnailStreamOpener(new VideoThumbnailQuery());
            }
            return new ThumbnailStreamOpener(new ImageThumbnailQuery());
        }
    }
}
