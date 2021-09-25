package com.lzy.okgo.model;

import com.lzy.okgo.utils.HttpUtils;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import okhttp3.MediaType;

public class HttpParams implements Serializable {
    public static final boolean IS_REPLACE = true;
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=utf-8");
    public static final MediaType MEDIA_TYPE_PLAIN = MediaType.parse("text/plain;charset=utf-8");
    public static final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream");
    private static final long serialVersionUID = 7369819159227055048L;
    public LinkedHashMap<String, List<FileWrapper>> fileParamsMap;
    public LinkedHashMap<String, List<String>> urlParamsMap;

    public HttpParams() {
        init();
    }

    public HttpParams(String key, String value) {
        init();
        put(key, value, true);
    }

    public HttpParams(String key, File file) {
        init();
        put(key, file);
    }

    private void init() {
        this.urlParamsMap = new LinkedHashMap<>();
        this.fileParamsMap = new LinkedHashMap<>();
    }

    public void put(HttpParams params) {
        if (params != null) {
            if (params.urlParamsMap != null && !params.urlParamsMap.isEmpty()) {
                this.urlParamsMap.putAll(params.urlParamsMap);
            }
            if (params.fileParamsMap != null && !params.fileParamsMap.isEmpty()) {
                this.fileParamsMap.putAll(params.fileParamsMap);
            }
        }
    }

    public void put(Map<String, String> params, boolean... isReplace) {
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                put(entry.getKey(), entry.getValue(), isReplace);
            }
        }
    }

    public void put(String key, String value, boolean... isReplace) {
        if (isReplace == null || isReplace.length <= 0) {
            put(key, value, true);
        } else {
            put(key, value, isReplace[0]);
        }
    }

    public void put(String key, int value, boolean... isReplace) {
        if (isReplace == null || isReplace.length <= 0) {
            put(key, String.valueOf(value), true);
        } else {
            put(key, String.valueOf(value), isReplace[0]);
        }
    }

    public void put(String key, long value, boolean... isReplace) {
        if (isReplace == null || isReplace.length <= 0) {
            put(key, String.valueOf(value), true);
        } else {
            put(key, String.valueOf(value), isReplace[0]);
        }
    }

    public void put(String key, float value, boolean... isReplace) {
        if (isReplace == null || isReplace.length <= 0) {
            put(key, String.valueOf(value), true);
        } else {
            put(key, String.valueOf(value), isReplace[0]);
        }
    }

    public void put(String key, double value, boolean... isReplace) {
        if (isReplace == null || isReplace.length <= 0) {
            put(key, String.valueOf(value), true);
        } else {
            put(key, String.valueOf(value), isReplace[0]);
        }
    }

    public void put(String key, char value, boolean... isReplace) {
        if (isReplace == null || isReplace.length <= 0) {
            put(key, String.valueOf(value), true);
        } else {
            put(key, String.valueOf(value), isReplace[0]);
        }
    }

    public void put(String key, boolean value, boolean... isReplace) {
        if (isReplace == null || isReplace.length <= 0) {
            put(key, String.valueOf(value), true);
        } else {
            put(key, String.valueOf(value), isReplace[0]);
        }
    }

    private void put(String key, String value, boolean isReplace) {
        if (key != null && value != null) {
            List<String> urlValues = this.urlParamsMap.get(key);
            if (urlValues == null) {
                urlValues = new ArrayList<>();
                this.urlParamsMap.put(key, urlValues);
            }
            if (isReplace) {
                urlValues.clear();
            }
            urlValues.add(value);
        }
    }

    public void putUrlParams(String key, List<String> values) {
        if (key != null && values != null && !values.isEmpty()) {
            for (String value : values) {
                put(key, value, false);
            }
        }
    }

    public void put(String key, File file) {
        put(key, file, file.getName());
    }

    public void put(String key, File file, String fileName) {
        put(key, file, fileName, HttpUtils.guessMimeType(fileName));
    }

    public void put(String key, FileWrapper fileWrapper) {
        if (key != null && fileWrapper != null) {
            put(key, fileWrapper.file, fileWrapper.fileName, fileWrapper.contentType);
        }
    }

    public void put(String key, File file, String fileName, MediaType contentType) {
        if (key != null) {
            List<FileWrapper> fileWrappers = this.fileParamsMap.get(key);
            if (fileWrappers == null) {
                fileWrappers = new ArrayList<>();
                this.fileParamsMap.put(key, fileWrappers);
            }
            fileWrappers.add(new FileWrapper(file, fileName, contentType));
        }
    }

    public void putFileParams(String key, List<File> files) {
        if (key != null && files != null && !files.isEmpty()) {
            for (File file : files) {
                put(key, file);
            }
        }
    }

    public void putFileWrapperParams(String key, List<FileWrapper> fileWrappers) {
        if (key != null && fileWrappers != null && !fileWrappers.isEmpty()) {
            for (FileWrapper fileWrapper : fileWrappers) {
                put(key, fileWrapper);
            }
        }
    }

    public void removeUrl(String key) {
        this.urlParamsMap.remove(key);
    }

    public void removeFile(String key) {
        this.fileParamsMap.remove(key);
    }

    public void remove(String key) {
        removeUrl(key);
        removeFile(key);
    }

    public void clear() {
        this.urlParamsMap.clear();
        this.fileParamsMap.clear();
    }

    public static class FileWrapper implements Serializable {
        private static final long serialVersionUID = -2356139899636767776L;
        public transient MediaType contentType;
        public File file;
        public String fileName;
        public long fileSize;

        public FileWrapper(File file2, String fileName2, MediaType contentType2) {
            this.file = file2;
            this.fileName = fileName2;
            this.contentType = contentType2;
            this.fileSize = file2.length();
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.defaultWriteObject();
            out.writeObject(this.contentType.toString());
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();
            this.contentType = MediaType.parse((String) in.readObject());
        }

        public String toString() {
            return "FileWrapper{file=" + this.file + ", fileName=" + this.fileName + ", contentType=" + this.contentType + ", fileSize=" + this.fileSize + "}";
        }
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : this.urlParamsMap.entrySet()) {
            if (result.length() > 0) {
                result.append("&");
            }
            result.append(entry.getKey()).append("=").append(entry.getValue());
        }
        for (Map.Entry<String, List<FileWrapper>> entry2 : this.fileParamsMap.entrySet()) {
            if (result.length() > 0) {
                result.append("&");
            }
            result.append(entry2.getKey()).append("=").append(entry2.getValue());
        }
        return result.toString();
    }
}
