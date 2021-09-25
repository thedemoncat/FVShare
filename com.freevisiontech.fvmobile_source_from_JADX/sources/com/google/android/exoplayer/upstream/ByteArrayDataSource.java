package com.google.android.exoplayer.upstream;

import com.google.android.exoplayer.util.Assertions;
import java.io.IOException;

public final class ByteArrayDataSource implements DataSource {
    private final byte[] data;
    private int readPosition;
    private int remainingBytes;

    public ByteArrayDataSource(byte[] data2) {
        Assertions.checkNotNull(data2);
        Assertions.checkArgument(data2.length > 0);
        this.data = data2;
    }

    public long open(DataSpec dataSpec) throws IOException {
        this.readPosition = (int) dataSpec.position;
        this.remainingBytes = (int) (dataSpec.length == -1 ? ((long) this.data.length) - dataSpec.position : dataSpec.length);
        if (this.remainingBytes > 0 && this.readPosition + this.remainingBytes <= this.data.length) {
            return (long) this.remainingBytes;
        }
        throw new IOException("Unsatisfiable range: [" + this.readPosition + ", " + dataSpec.length + "], length: " + this.data.length);
    }

    public void close() throws IOException {
    }

    public int read(byte[] buffer, int offset, int length) throws IOException {
        if (this.remainingBytes == 0) {
            return -1;
        }
        int length2 = Math.min(length, this.remainingBytes);
        System.arraycopy(this.data, this.readPosition, buffer, offset, length2);
        this.readPosition += length2;
        this.remainingBytes -= length2;
        return length2;
    }
}
