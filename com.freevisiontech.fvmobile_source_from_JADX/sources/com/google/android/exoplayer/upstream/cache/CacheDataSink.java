package com.google.android.exoplayer.upstream.cache;

import com.google.android.exoplayer.upstream.DataSink;
import com.google.android.exoplayer.upstream.DataSpec;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.Util;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public final class CacheDataSink implements DataSink {
    private final Cache cache;
    private DataSpec dataSpec;
    private long dataSpecBytesWritten;
    private File file;
    private final long maxCacheFileSize;
    private FileOutputStream outputStream;
    private long outputStreamBytesWritten;

    public static class CacheDataSinkException extends IOException {
        public CacheDataSinkException(IOException cause) {
            super(cause);
        }
    }

    public CacheDataSink(Cache cache2, long maxCacheFileSize2) {
        this.cache = (Cache) Assertions.checkNotNull(cache2);
        this.maxCacheFileSize = maxCacheFileSize2;
    }

    public DataSink open(DataSpec dataSpec2) throws CacheDataSinkException {
        Assertions.checkState(dataSpec2.length != -1);
        try {
            this.dataSpec = dataSpec2;
            this.dataSpecBytesWritten = 0;
            openNextOutputStream();
            return this;
        } catch (FileNotFoundException e) {
            throw new CacheDataSinkException(e);
        }
    }

    public void write(byte[] buffer, int offset, int length) throws CacheDataSinkException {
        int bytesWritten = 0;
        while (bytesWritten < length) {
            try {
                if (this.outputStreamBytesWritten == this.maxCacheFileSize) {
                    closeCurrentOutputStream();
                    openNextOutputStream();
                }
                int bytesToWrite = (int) Math.min((long) (length - bytesWritten), this.maxCacheFileSize - this.outputStreamBytesWritten);
                this.outputStream.write(buffer, offset + bytesWritten, bytesToWrite);
                bytesWritten += bytesToWrite;
                this.outputStreamBytesWritten += (long) bytesToWrite;
                this.dataSpecBytesWritten += (long) bytesToWrite;
            } catch (IOException e) {
                throw new CacheDataSinkException(e);
            }
        }
    }

    public void close() throws CacheDataSinkException {
        try {
            closeCurrentOutputStream();
        } catch (IOException e) {
            throw new CacheDataSinkException(e);
        }
    }

    private void openNextOutputStream() throws FileNotFoundException {
        this.file = this.cache.startFile(this.dataSpec.key, this.dataSpec.absoluteStreamPosition + this.dataSpecBytesWritten, Math.min(this.dataSpec.length - this.dataSpecBytesWritten, this.maxCacheFileSize));
        this.outputStream = new FileOutputStream(this.file);
        this.outputStreamBytesWritten = 0;
    }

    private void closeCurrentOutputStream() throws IOException {
        if (this.outputStream != null) {
            boolean success = false;
            try {
                this.outputStream.flush();
                this.outputStream.getFD().sync();
                success = true;
                if (!success) {
                    this.file.delete();
                }
            } finally {
                Util.closeQuietly((OutputStream) this.outputStream);
                if (success) {
                    this.cache.commitFile(this.file);
                } else {
                    this.file.delete();
                }
                this.outputStream = null;
                this.file = null;
            }
        }
    }
}
