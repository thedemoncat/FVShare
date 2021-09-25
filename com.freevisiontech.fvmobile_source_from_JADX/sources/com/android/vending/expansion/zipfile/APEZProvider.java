package com.android.vending.expansion.zipfile;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import com.android.vending.expansion.zipfile.ZipResourceFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public abstract class APEZProvider extends ContentProvider {
    public static final String[] ALL_FIELDS = {FILEID, FILENAME, ZIPFILE, MODIFICATION, CRC32, COMPRESSEDLEN, UNCOMPRESSEDLEN, COMPRESSIONTYPE};
    public static final int[] ALL_FIELDS_INT = {0, 1, 2, 3, 4, 5, 6, 7};
    public static final int COMPLEN_IDX = 5;
    public static final String COMPRESSEDLEN = "ZCOL";
    public static final String COMPRESSIONTYPE = "ZTYP";
    public static final int COMPTYPE_IDX = 7;
    public static final String CRC32 = "ZCRC";
    public static final int CRC_IDX = 4;
    public static final String FILEID = "_id";
    public static final int FILEID_IDX = 0;
    public static final String FILENAME = "ZPFN";
    public static final int FILENAME_IDX = 1;
    public static final String MODIFICATION = "ZMOD";
    public static final int MOD_IDX = 3;
    private static final String NO_FILE = "N";
    public static final int UNCOMPLEN_IDX = 6;
    public static final String UNCOMPRESSEDLEN = "ZUNL";
    public static final String ZIPFILE = "ZFIL";
    public static final int ZIPFILE_IDX = 2;
    private ZipResourceFile mAPKExtensionFile;
    private boolean mInit;

    public abstract String getAuthority();

    public int delete(Uri arg0, String arg1, String[] arg2) {
        return 0;
    }

    public String getType(Uri uri) {
        return "vnd.android.cursor.item/asset";
    }

    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    private boolean initIfNecessary() {
        int patchFileVersion;
        int mainFileVersion;
        if (!this.mInit) {
            Context ctx = getContext();
            PackageManager pm = ctx.getPackageManager();
            ProviderInfo pi = pm.resolveContentProvider(getAuthority(), 128);
            try {
                int appVersionCode = pm.getPackageInfo(ctx.getPackageName(), 0).versionCode;
                String[] resourceFiles = null;
                if (pi.metaData != null) {
                    mainFileVersion = pi.metaData.getInt("mainVersion", appVersionCode);
                    patchFileVersion = pi.metaData.getInt("patchVersion", appVersionCode);
                    String mainFileName = pi.metaData.getString("mainFilename", NO_FILE);
                    if (NO_FILE != mainFileName) {
                        String patchFileName = pi.metaData.getString("patchFilename", NO_FILE);
                        resourceFiles = NO_FILE != patchFileName ? new String[]{mainFileName, patchFileName} : new String[]{mainFileName};
                    }
                } else {
                    patchFileVersion = appVersionCode;
                    mainFileVersion = appVersionCode;
                }
                if (resourceFiles == null) {
                    try {
                        this.mAPKExtensionFile = APKExpansionSupport.getAPKExpansionZipFile(ctx, mainFileVersion, patchFileVersion);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    this.mAPKExtensionFile = APKExpansionSupport.getResourceZipFile(resourceFiles);
                }
                this.mInit = true;
                return true;
            } catch (PackageManager.NameNotFoundException e1) {
                e1.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean onCreate() {
        return true;
    }

    public AssetFileDescriptor openAssetFile(Uri uri, String mode) throws FileNotFoundException {
        initIfNecessary();
        String path = uri.getEncodedPath();
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        return this.mAPKExtensionFile.getAssetFileDescriptor(path);
    }

    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
        initIfNecessary();
        return super.applyBatch(operations);
    }

    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        initIfNecessary();
        AssetFileDescriptor af = openAssetFile(uri, mode);
        if (af != null) {
            return af.getParcelFileDescriptor();
        }
        return null;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        ZipResourceFile.ZipEntryRO[] zipEntries;
        int[] intProjection;
        initIfNecessary();
        if (this.mAPKExtensionFile == null) {
            zipEntries = new ZipResourceFile.ZipEntryRO[0];
        } else {
            zipEntries = this.mAPKExtensionFile.getAllEntries();
        }
        if (projection == null) {
            intProjection = ALL_FIELDS_INT;
            projection = ALL_FIELDS;
        } else {
            int len = projection.length;
            intProjection = new int[len];
            for (int i = 0; i < len; i++) {
                if (projection[i].equals(FILEID)) {
                    intProjection[i] = 0;
                } else if (projection[i].equals(FILENAME)) {
                    intProjection[i] = 1;
                } else if (projection[i].equals(ZIPFILE)) {
                    intProjection[i] = 2;
                } else if (projection[i].equals(MODIFICATION)) {
                    intProjection[i] = 3;
                } else if (projection[i].equals(CRC32)) {
                    intProjection[i] = 4;
                } else if (projection[i].equals(COMPRESSEDLEN)) {
                    intProjection[i] = 5;
                } else if (projection[i].equals(UNCOMPRESSEDLEN)) {
                    intProjection[i] = 6;
                } else if (projection[i].equals(COMPRESSIONTYPE)) {
                    intProjection[i] = 7;
                } else {
                    throw new RuntimeException();
                }
            }
        }
        MatrixCursor mc = new MatrixCursor(projection, zipEntries.length);
        int len2 = intProjection.length;
        for (ZipResourceFile.ZipEntryRO zer : zipEntries) {
            MatrixCursor.RowBuilder rb = mc.newRow();
            for (int i2 = 0; i2 < len2; i2++) {
                switch (intProjection[i2]) {
                    case 0:
                        rb.add(Integer.valueOf(i2));
                        break;
                    case 1:
                        rb.add(zer.mFileName);
                        break;
                    case 2:
                        rb.add(zer.getZipFileName());
                        break;
                    case 3:
                        rb.add(Long.valueOf(zer.mWhenModified));
                        break;
                    case 4:
                        rb.add(Long.valueOf(zer.mCRC32));
                        break;
                    case 5:
                        rb.add(Long.valueOf(zer.mCompressedLength));
                        break;
                    case 6:
                        rb.add(Long.valueOf(zer.mUncompressedLength));
                        break;
                    case 7:
                        rb.add(Integer.valueOf(zer.mMethod));
                        break;
                }
            }
        }
        return mc;
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
