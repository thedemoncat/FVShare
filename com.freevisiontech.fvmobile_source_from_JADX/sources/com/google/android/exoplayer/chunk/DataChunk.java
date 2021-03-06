package com.google.android.exoplayer.chunk;

import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DataSpec;
import java.io.IOException;
import java.util.Arrays;

public abstract class DataChunk extends Chunk {
    private static final int READ_GRANULARITY = 16384;
    private byte[] data;
    private int limit;
    private volatile boolean loadCanceled;

    /* access modifiers changed from: protected */
    public abstract void consume(byte[] bArr, int i) throws IOException;

    public DataChunk(DataSource dataSource, DataSpec dataSpec, int type, int trigger, Format format, int parentId, byte[] data2) {
        super(dataSource, dataSpec, type, trigger, format, parentId);
        this.data = data2;
    }

    public byte[] getDataHolder() {
        return this.data;
    }

    public long bytesLoaded() {
        return (long) this.limit;
    }

    public final void cancelLoad() {
        this.loadCanceled = true;
    }

    public final boolean isLoadCanceled() {
        return this.loadCanceled;
    }

    public final void load() throws IOException, InterruptedException {
        try {
            this.dataSource.open(this.dataSpec);
            this.limit = 0;
            int bytesRead = 0;
            while (bytesRead != -1 && !this.loadCanceled) {
                maybeExpandData();
                bytesRead = this.dataSource.read(this.data, this.limit, 16384);
                if (bytesRead != -1) {
                    this.limit += bytesRead;
                }
            }
            if (!this.loadCanceled) {
                consume(this.data, this.limit);
            }
        } finally {
            this.dataSource.close();
        }
    }

    private void maybeExpandData() {
        if (this.data == null) {
            this.data = new byte[16384];
        } else if (this.data.length < this.limit + 16384) {
            this.data = Arrays.copyOf(this.data, this.data.length + 16384);
        }
    }
}
