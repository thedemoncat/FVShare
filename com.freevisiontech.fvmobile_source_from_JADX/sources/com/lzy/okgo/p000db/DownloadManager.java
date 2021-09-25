package com.lzy.okgo.p000db;

import android.content.ContentValues;
import android.database.Cursor;
import com.lzy.okgo.model.Progress;
import java.util.List;

/* renamed from: com.lzy.okgo.db.DownloadManager */
public class DownloadManager extends BaseDao<Progress> {
    private DownloadManager() {
        super(new DBHelper());
    }

    public static DownloadManager getInstance() {
        return DownloadManagerHolder.instance;
    }

    /* renamed from: com.lzy.okgo.db.DownloadManager$DownloadManagerHolder */
    private static class DownloadManagerHolder {
        /* access modifiers changed from: private */
        public static final DownloadManager instance = new DownloadManager();

        private DownloadManagerHolder() {
        }
    }

    public Progress parseCursorToBean(Cursor cursor) {
        return Progress.parseCursorToBean(cursor);
    }

    public ContentValues getContentValues(Progress progress) {
        return Progress.buildContentValues(progress);
    }

    public String getTableName() {
        return "download";
    }

    public void unInit() {
    }

    public Progress get(String tag) {
        return (Progress) queryOne("tag=?", new String[]{tag});
    }

    public void delete(String taskKey) {
        delete("tag=?", new String[]{taskKey});
    }

    public boolean update(Progress progress) {
        return update(progress, "tag=?", new String[]{progress.tag});
    }

    public boolean update(ContentValues contentValues, String tag) {
        return update(contentValues, "tag=?", new String[]{tag});
    }

    public List<Progress> getAll() {
        return query((String[]) null, (String) null, (String[]) null, (String) null, (String) null, "date ASC", (String) null);
    }

    public List<Progress> getFinished() {
        return query((String[]) null, "status=?", new String[]{"5"}, (String) null, (String) null, "date ASC", (String) null);
    }

    public List<Progress> getDownloading() {
        return query((String[]) null, "status not in(?)", new String[]{"5"}, (String) null, (String) null, "date ASC", (String) null);
    }

    public boolean clear() {
        return deleteAll();
    }
}
