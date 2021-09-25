package com.freevisiontech.fvmobile.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import com.android.vending.expansion.zipfile.APKExpansionSupport;
import com.android.vending.expansion.zipfile.ZipResourceFile;
import com.google.android.vending.expansion.downloader.impl.DownloadInfo;
import com.google.android.vending.expansion.downloader.impl.DownloadsDB;
import java.io.IOException;
import java.io.InputStream;

public class LocalResourceManager {
    public static InputStream getLocalResourceStream(Context context, String path) {
        DownloadInfo[] infos;
        if (isFileExistOnAssets(context, path)) {
            try {
                return context.getAssets().open(path);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            ZipResourceFile expansionFile = null;
            int mainVersion = 0;
            int pathVersion = 0;
            try {
                DownloadsDB db = DownloadsDB.getDB(context);
                if (!(db == null || (infos = db.getDownloads()) == null)) {
                    for (DownloadInfo downloadInfo : infos) {
                        String filename = downloadInfo.mFileName;
                        if ("main".equals(filename.split("\\.")[0])) {
                            mainVersion = Integer.parseInt(filename.split("\\.")[1].split("\\.")[0]);
                        } else {
                            pathVersion = Integer.parseInt(filename.split("\\.")[1].split("\\.")[0]);
                        }
                    }
                    expansionFile = APKExpansionSupport.getAPKExpansionZipFile(context, mainVersion, pathVersion);
                }
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            if (expansionFile == null) {
                return null;
            }
            try {
                return expansionFile.getInputStream("assets/" + path);
            } catch (IOException e3) {
                e3.printStackTrace();
                return null;
            }
        }
    }

    public static AssetFileDescriptor getLocalMedia(Context context, String path) {
        AssetFileDescriptor assetFileDescriptor = null;
        if (isFileExistOnAssets(context, path)) {
            try {
                assetFileDescriptor = context.getAssets().openFd(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            ZipResourceFile expansionFile = null;
            int mainVersion = 0;
            int pathVersion = 0;
            try {
                DownloadsDB db = DownloadsDB.getDB(context);
                if (db != null) {
                    DownloadInfo[] infos = db.getDownloads();
                    if (infos == null) {
                        return null;
                    }
                    for (DownloadInfo downloadInfo : infos) {
                        String filename = downloadInfo.mFileName;
                        if ("main".equals(filename.split("\\.")[0])) {
                            mainVersion = Integer.parseInt(filename.split("\\.")[1].split("\\.")[0]);
                        } else {
                            pathVersion = Integer.parseInt(filename.split("\\.")[1].split("\\.")[0]);
                        }
                    }
                    expansionFile = APKExpansionSupport.getAPKExpansionZipFile(context, mainVersion, pathVersion);
                }
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            if (expansionFile != null) {
                assetFileDescriptor = expansionFile.getAssetFileDescriptor("assets/" + path);
            }
        }
        return assetFileDescriptor;
    }

    public static boolean expansionFilesDelivered(Context context) {
        int mainVersion = 0;
        int pathVersion = 0;
        try {
            DownloadsDB db = DownloadsDB.getDB(context);
            if (db != null) {
                DownloadInfo[] infos = db.getDownloads();
                if (infos == null) {
                    return false;
                }
                for (DownloadInfo downloadInfo : infos) {
                    String filename = downloadInfo.mFileName;
                    if ("main".equals(filename.split("\\.")[0])) {
                        mainVersion = Integer.parseInt(filename.split("\\.")[1].split("\\.")[0]);
                    } else {
                        pathVersion = Integer.parseInt(filename.split("\\.")[1].split("\\.")[0]);
                    }
                }
                if (APKExpansionSupport.getAPKExpansionZipFile(context, mainVersion, pathVersion) != null) {
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isAssetsEmpty(Context context) {
        try {
            String[] names = context.getAssets().list("");
            if (names == null || names.length <= 0) {
                return false;
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean isFileExistOnAssets(Context context, String path) {
        try {
            String[] names = context.getAssets().list("");
            for (String equals : names) {
                if (equals.equals(path.trim())) {
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
