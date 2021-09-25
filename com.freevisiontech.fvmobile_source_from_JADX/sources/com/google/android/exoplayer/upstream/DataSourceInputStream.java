package com.google.android.exoplayer.upstream;

import com.google.android.exoplayer.util.Assertions;
import java.io.IOException;
import java.io.InputStream;

public final class DataSourceInputStream extends InputStream {
    private boolean closed = false;
    private final DataSource dataSource;
    private final DataSpec dataSpec;
    private boolean opened = false;
    private final byte[] singleByteArray;

    public DataSourceInputStream(DataSource dataSource2, DataSpec dataSpec2) {
        this.dataSource = dataSource2;
        this.dataSpec = dataSpec2;
        this.singleByteArray = new byte[1];
    }

    public void open() throws IOException {
        checkOpened();
    }

    public int read() throws IOException {
        if (read(this.singleByteArray) == -1) {
            return -1;
        }
        return this.singleByteArray[0] & 255;
    }

    public int read(byte[] buffer) throws IOException {
        return read(buffer, 0, buffer.length);
    }

    public int read(byte[] buffer, int offset, int length) throws IOException {
        Assertions.checkState(!this.closed);
        checkOpened();
        return this.dataSource.read(buffer, offset, length);
    }

    public long skip(long byteCount) throws IOException {
        Assertions.checkState(!this.closed);
        checkOpened();
        return super.skip(byteCount);
    }

    public void close() throws IOException {
        if (!this.closed) {
            this.dataSource.close();
            this.closed = true;
        }
    }

    private void checkOpened() throws IOException {
        if (!this.opened) {
            this.dataSource.open(this.dataSpec);
            this.opened = true;
        }
    }
}
