package com.lzy.okgo.request;

import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.utils.HttpUtils;
import com.lzy.okgo.utils.OkLogger;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

public class ProgressRequestBody<T> extends RequestBody {
    /* access modifiers changed from: private */
    public Callback<T> callback;
    /* access modifiers changed from: private */
    public UploadInterceptor interceptor;
    private RequestBody requestBody;

    public interface UploadInterceptor {
        void uploadProgress(Progress progress);
    }

    ProgressRequestBody(RequestBody requestBody2, Callback<T> callback2) {
        this.requestBody = requestBody2;
        this.callback = callback2;
    }

    public MediaType contentType() {
        return this.requestBody.contentType();
    }

    public long contentLength() {
        try {
            return this.requestBody.contentLength();
        } catch (IOException e) {
            OkLogger.printStackTrace(e);
            return -1;
        }
    }

    public void writeTo(BufferedSink sink) throws IOException {
        BufferedSink bufferedSink = Okio.buffer((Sink) new CountingSink(sink));
        this.requestBody.writeTo(bufferedSink);
        bufferedSink.flush();
    }

    private final class CountingSink extends ForwardingSink {
        private Progress progress = new Progress();

        CountingSink(Sink delegate) {
            super(delegate);
            this.progress.totalSize = ProgressRequestBody.this.contentLength();
        }

        public void write(Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);
            Progress.changeProgress(this.progress, byteCount, new Progress.Action() {
                public void call(Progress progress) {
                    if (ProgressRequestBody.this.interceptor != null) {
                        ProgressRequestBody.this.interceptor.uploadProgress(progress);
                    } else {
                        ProgressRequestBody.this.onProgress(progress);
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void onProgress(final Progress progress) {
        HttpUtils.runOnUiThread(new Runnable() {
            public void run() {
                if (ProgressRequestBody.this.callback != null) {
                    ProgressRequestBody.this.callback.uploadProgress(progress);
                }
            }
        });
    }

    public void setInterceptor(UploadInterceptor interceptor2) {
        this.interceptor = interceptor2;
    }
}
