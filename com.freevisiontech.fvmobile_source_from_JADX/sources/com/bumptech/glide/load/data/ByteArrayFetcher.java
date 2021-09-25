package com.bumptech.glide.load.data;

import com.bumptech.glide.Priority;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ByteArrayFetcher implements DataFetcher<InputStream> {
    private final byte[] bytes;

    /* renamed from: id */
    private final String f1180id;

    public ByteArrayFetcher(byte[] bytes2, String id) {
        this.bytes = bytes2;
        this.f1180id = id;
    }

    public InputStream loadData(Priority priority) {
        return new ByteArrayInputStream(this.bytes);
    }

    public void cleanup() {
    }

    public String getId() {
        return this.f1180id;
    }

    public void cancel() {
    }
}
