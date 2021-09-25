package com.lzy.okgo.request;

import android.text.TextUtils;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.request.BodyRequest;
import com.lzy.okgo.utils.HttpUtils;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.json.JSONArray;
import org.json.JSONObject;

public abstract class BodyRequest<T, R extends BodyRequest> extends Request<T, R> implements HasBody<R> {
    private static final long serialVersionUID = -6459175248476927501L;

    /* renamed from: bs */
    protected byte[] f1017bs;
    protected String content;
    protected transient File file;
    protected boolean isMultipart = false;
    protected boolean isSpliceUrl = false;
    protected transient MediaType mediaType;
    protected RequestBody requestBody;

    public BodyRequest(String url) {
        super(url);
    }

    public R isMultipart(boolean isMultipart2) {
        this.isMultipart = isMultipart2;
        return this;
    }

    public R isSpliceUrl(boolean isSpliceUrl2) {
        this.isSpliceUrl = isSpliceUrl2;
        return this;
    }

    public R params(String key, File file2) {
        this.params.put(key, file2);
        return this;
    }

    public R addFileParams(String key, List<File> files) {
        this.params.putFileParams(key, files);
        return this;
    }

    public R addFileWrapperParams(String key, List<HttpParams.FileWrapper> fileWrappers) {
        this.params.putFileWrapperParams(key, fileWrappers);
        return this;
    }

    public R params(String key, File file2, String fileName) {
        this.params.put(key, file2, fileName);
        return this;
    }

    public R params(String key, File file2, String fileName, MediaType contentType) {
        this.params.put(key, file2, fileName, contentType);
        return this;
    }

    public R upRequestBody(RequestBody requestBody2) {
        this.requestBody = requestBody2;
        return this;
    }

    public R upString(String string) {
        this.content = string;
        this.mediaType = HttpParams.MEDIA_TYPE_PLAIN;
        return this;
    }

    public R upString(String string, MediaType mediaType2) {
        this.content = string;
        this.mediaType = mediaType2;
        return this;
    }

    public R upJson(String json) {
        this.content = json;
        this.mediaType = HttpParams.MEDIA_TYPE_JSON;
        return this;
    }

    public R upJson(JSONObject jsonObject) {
        this.content = jsonObject.toString();
        this.mediaType = HttpParams.MEDIA_TYPE_JSON;
        return this;
    }

    public R upJson(JSONArray jsonArray) {
        this.content = jsonArray.toString();
        this.mediaType = HttpParams.MEDIA_TYPE_JSON;
        return this;
    }

    public R upBytes(byte[] bs) {
        this.f1017bs = bs;
        this.mediaType = HttpParams.MEDIA_TYPE_STREAM;
        return this;
    }

    public R upBytes(byte[] bs, MediaType mediaType2) {
        this.f1017bs = bs;
        this.mediaType = mediaType2;
        return this;
    }

    public R upFile(File file2) {
        this.file = file2;
        this.mediaType = HttpUtils.guessMimeType(file2.getName());
        return this;
    }

    public R upFile(File file2, MediaType mediaType2) {
        this.file = file2;
        this.mediaType = mediaType2;
        return this;
    }

    public RequestBody generateRequestBody() {
        if (this.requestBody != null) {
            return this.requestBody;
        }
        if (this.content != null && this.mediaType != null) {
            return RequestBody.create(this.mediaType, this.content);
        }
        if (this.f1017bs != null && this.mediaType != null) {
            return RequestBody.create(this.mediaType, this.f1017bs);
        }
        if (this.file == null || this.mediaType == null) {
            return HttpUtils.generateMultipartRequestBody(this.params, this.isMultipart);
        }
        return RequestBody.create(this.mediaType, this.file);
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(this.mediaType == null ? "" : this.mediaType.toString());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        String mediaTypeString = (String) in.readObject();
        if (!TextUtils.isEmpty(mediaTypeString)) {
            this.mediaType = MediaType.parse(mediaTypeString);
        }
    }
}
