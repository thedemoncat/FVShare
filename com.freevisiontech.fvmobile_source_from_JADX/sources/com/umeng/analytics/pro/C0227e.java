package com.umeng.analytics.pro;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.sqlite.SQLiteDatabase;
import java.io.File;

/* renamed from: com.umeng.analytics.pro.e */
/* compiled from: UMCCPathDatabaseContext */
public class C0227e extends ContextWrapper {

    /* renamed from: a */
    private String f737a;

    public C0227e(Context context, String str) {
        super(context);
        this.f737a = str;
    }

    public SQLiteDatabase openOrCreateDatabase(String str, int i, SQLiteDatabase.CursorFactory cursorFactory) {
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(str).getAbsolutePath(), cursorFactory);
    }

    public File getDatabasePath(String str) {
        File file = new File(this.f737a + str);
        if (!file.getParentFile().exists() && !file.getParentFile().isDirectory()) {
            file.getParentFile().mkdirs();
        }
        return file;
    }
}
