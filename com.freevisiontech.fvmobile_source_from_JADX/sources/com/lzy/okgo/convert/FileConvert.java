package com.lzy.okgo.convert;

import android.os.Environment;
import android.text.TextUtils;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.utils.HttpUtils;
import com.lzy.okgo.utils.IOUtils;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class FileConvert implements Converter<File> {
    public static final String DM_TARGET_FOLDER = (File.separator + "download" + File.separator);
    /* access modifiers changed from: private */
    public Callback<File> callback;
    private String fileName;
    private String folder;

    public FileConvert() {
        this((String) null);
    }

    public FileConvert(String fileName2) {
        this(Environment.getExternalStorageDirectory() + DM_TARGET_FOLDER, fileName2);
    }

    public FileConvert(String folder2, String fileName2) {
        this.folder = folder2;
        this.fileName = fileName2;
    }

    public void setCallback(Callback<File> callback2) {
        this.callback = callback2;
    }

    public File convertResponse(Response response) throws Throwable {
        String url = response.request().url().toString();
        if (TextUtils.isEmpty(this.folder)) {
            this.folder = Environment.getExternalStorageDirectory() + DM_TARGET_FOLDER;
        }
        if (TextUtils.isEmpty(this.fileName)) {
            this.fileName = HttpUtils.getNetFileName(response, url);
        }
        File dir = new File(this.folder);
        IOUtils.createFolder(dir);
        File file = new File(dir, this.fileName);
        IOUtils.delFileOrFolder(file);
        InputStream bodyStream = null;
        byte[] buffer = new byte[8192];
        FileOutputStream fileOutputStream = null;
        try {
            ResponseBody body = response.body();
            if (body == null) {
                IOUtils.closeQuietly((Closeable) null);
                IOUtils.closeQuietly((Closeable) null);
                return null;
            }
            bodyStream = body.byteStream();
            Progress progress = new Progress();
            progress.totalSize = body.contentLength();
            progress.fileName = this.fileName;
            progress.filePath = file.getAbsolutePath();
            progress.status = 2;
            progress.url = url;
            progress.tag = url;
            FileOutputStream fileOutputStream2 = new FileOutputStream(file);
            while (true) {
                try {
                    int len = bodyStream.read(buffer);
                    if (len != -1) {
                        fileOutputStream2.write(buffer, 0, len);
                        if (this.callback != null) {
                            Progress.changeProgress(progress, (long) len, new Progress.Action() {
                                public void call(Progress progress) {
                                    FileConvert.this.onProgress(progress);
                                }
                            });
                        }
                    } else {
                        fileOutputStream2.flush();
                        IOUtils.closeQuietly(bodyStream);
                        IOUtils.closeQuietly(fileOutputStream2);
                        FileOutputStream fileOutputStream3 = fileOutputStream2;
                        return file;
                    }
                } catch (Throwable th) {
                    th = th;
                    fileOutputStream = fileOutputStream2;
                    IOUtils.closeQuietly(bodyStream);
                    IOUtils.closeQuietly(fileOutputStream);
                    throw th;
                }
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    /* access modifiers changed from: private */
    public void onProgress(final Progress progress) {
        HttpUtils.runOnUiThread(new Runnable() {
            public void run() {
                FileConvert.this.callback.downloadProgress(progress);
            }
        });
    }
}
