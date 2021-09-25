package com.yalantis.ucrop.util;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Locale;

public class FileUtils {
    static final String TAG = "FileUtils";

    private FileUtils() {
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        try {
            Cursor cursor2 = context.getContentResolver().query(uri, new String[]{"_data"}, selection, selectionArgs, (String) null);
            if (cursor2 == null || !cursor2.moveToFirst()) {
                if (cursor2 != null) {
                    cursor2.close();
                }
                return null;
            }
            String string = cursor2.getString(cursor2.getColumnIndexOrThrow("_data"));
            if (cursor2 == null) {
                return string;
            }
            cursor2.close();
            return string;
        } catch (IllegalArgumentException ex) {
            Log.i(TAG, String.format(Locale.getDefault(), "getDataColumn: _data - [%s]", new Object[]{ex.getMessage()}));
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    @SuppressLint({"NewApi"})
    public static String getPath(Context context, Uri uri) {
        boolean isKitKat;
        if (Build.VERSION.SDK_INT >= 19) {
            isKitKat = true;
        } else {
            isKitKat = false;
        }
        if (!isKitKat || !DocumentsContract.isDocumentUri(context, uri)) {
            if ("content".equalsIgnoreCase(uri.getScheme())) {
                if (isGooglePhotosUri(uri)) {
                    return uri.getLastPathSegment();
                }
                return getDataColumn(context, uri, (String) null, (String[]) null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            } else {
                return null;
            }
        } else if (isExternalStorageDocument(uri)) {
            String[] split = DocumentsContract.getDocumentId(uri).split(":");
            if ("primary".equalsIgnoreCase(split[0])) {
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            }
            return null;
        } else if (isDownloadsDocument(uri)) {
            String id = DocumentsContract.getDocumentId(uri);
            if (TextUtils.isEmpty(id)) {
                return null;
            }
            try {
                return getDataColumn(context, ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id).longValue()), (String) null, (String[]) null);
            } catch (NumberFormatException e) {
                Log.i(TAG, e.getMessage());
                return null;
            }
        } else if (!isMediaDocument(uri)) {
            return null;
        } else {
            String[] split2 = DocumentsContract.getDocumentId(uri).split(":");
            String type = split2[0];
            Uri contentUri = null;
            if ("image".equals(type)) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(type)) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            } else if ("audio".equals(type)) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            }
            return getDataColumn(context, contentUri, "_id=?", new String[]{split2[1]});
        }
    }

    public static void copyFile(@NonNull String pathFrom, @NonNull String pathTo) throws IOException {
        if (!pathFrom.equalsIgnoreCase(pathTo)) {
            FileChannel outputChannel = null;
            FileChannel inputChannel = null;
            try {
                inputChannel = new FileInputStream(new File(pathFrom)).getChannel();
                outputChannel = new FileOutputStream(new File(pathTo)).getChannel();
                inputChannel.transferTo(0, inputChannel.size(), outputChannel);
                inputChannel.close();
            } finally {
                if (inputChannel != null) {
                    inputChannel.close();
                }
                if (outputChannel != null) {
                    outputChannel.close();
                }
            }
        }
    }
}
