package com.google.android.exoplayer.upstream;

import com.google.android.exoplayer.util.Assertions;
import java.io.IOException;

public final class PriorityDataSource implements DataSource {
    private final int priority;
    private final DataSource upstream;

    public PriorityDataSource(int priority2, DataSource upstream2) {
        this.priority = priority2;
        this.upstream = (DataSource) Assertions.checkNotNull(upstream2);
    }

    public long open(DataSpec dataSpec) throws IOException {
        NetworkLock.instance.proceedOrThrow(this.priority);
        return this.upstream.open(dataSpec);
    }

    public int read(byte[] buffer, int offset, int max) throws IOException {
        NetworkLock.instance.proceedOrThrow(this.priority);
        return this.upstream.read(buffer, offset, max);
    }

    public void close() throws IOException {
        this.upstream.close();
    }
}
