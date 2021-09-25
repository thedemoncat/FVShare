package com.bumptech.glide.load.resource.bitmap;

import android.util.Log;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class RecyclableBufferedInputStream extends FilterInputStream {
    private static final String TAG = "BufferedIs";
    private volatile byte[] buf;
    private int count;
    private int marklimit;
    private int markpos = -1;
    private int pos;

    public RecyclableBufferedInputStream(InputStream in, byte[] buffer) {
        super(in);
        if (buffer == null || buffer.length == 0) {
            throw new IllegalArgumentException("buffer is null or empty");
        }
        this.buf = buffer;
    }

    public synchronized int available() throws IOException {
        InputStream localIn;
        localIn = this.in;
        if (this.buf == null || localIn == null) {
            throw streamClosed();
        }
        return (this.count - this.pos) + localIn.available();
    }

    private static IOException streamClosed() throws IOException {
        throw new IOException("BufferedInputStream is closed");
    }

    public synchronized void fixMarkLimit() {
        this.marklimit = this.buf.length;
    }

    public void close() throws IOException {
        this.buf = null;
        InputStream localIn = this.in;
        this.in = null;
        if (localIn != null) {
            localIn.close();
        }
    }

    private int fillbuf(InputStream localIn, byte[] localBuf) throws IOException {
        int i;
        if (this.markpos == -1 || this.pos - this.markpos >= this.marklimit) {
            int result = localIn.read(localBuf);
            if (result > 0) {
                this.markpos = -1;
                this.pos = 0;
                this.count = result;
            }
            return result;
        }
        if (this.markpos == 0 && this.marklimit > localBuf.length && this.count == localBuf.length) {
            int newLength = localBuf.length * 2;
            if (newLength > this.marklimit) {
                newLength = this.marklimit;
            }
            if (Log.isLoggable(TAG, 3)) {
                Log.d(TAG, "allocate buffer of length: " + newLength);
            }
            byte[] newbuf = new byte[newLength];
            System.arraycopy(localBuf, 0, newbuf, 0, localBuf.length);
            this.buf = newbuf;
            localBuf = newbuf;
        } else if (this.markpos > 0) {
            System.arraycopy(localBuf, this.markpos, localBuf, 0, localBuf.length - this.markpos);
        }
        this.pos -= this.markpos;
        this.markpos = 0;
        this.count = 0;
        int bytesread = localIn.read(localBuf, this.pos, localBuf.length - this.pos);
        if (bytesread <= 0) {
            i = this.pos;
        } else {
            i = this.pos + bytesread;
        }
        this.count = i;
        return bytesread;
    }

    public synchronized void mark(int readlimit) {
        this.marklimit = Math.max(this.marklimit, readlimit);
        this.markpos = this.pos;
    }

    public boolean markSupported() {
        return true;
    }

    public synchronized int read() throws IOException {
        byte b = -1;
        synchronized (this) {
            byte[] localBuf = this.buf;
            InputStream localIn = this.in;
            if (localBuf == null || localIn == null) {
                throw streamClosed();
            } else if (this.pos < this.count || fillbuf(localIn, localBuf) != -1) {
                if (localBuf != this.buf && (localBuf = this.buf) == null) {
                    throw streamClosed();
                } else if (this.count - this.pos > 0) {
                    int i = this.pos;
                    this.pos = i + 1;
                    b = localBuf[i] & 255;
                }
            }
        }
        return b;
    }

    public synchronized int read(byte[] buffer, int offset, int byteCount) throws IOException {
        int required;
        int read;
        int i = -1;
        synchronized (this) {
            byte[] localBuf = this.buf;
            if (localBuf == null) {
                throw streamClosed();
            } else if (byteCount == 0) {
                i = 0;
            } else {
                InputStream localIn = this.in;
                if (localIn == null) {
                    throw streamClosed();
                }
                if (this.pos < this.count) {
                    int copylength = this.count - this.pos >= byteCount ? byteCount : this.count - this.pos;
                    System.arraycopy(localBuf, this.pos, buffer, offset, copylength);
                    this.pos += copylength;
                    if (copylength == byteCount || localIn.available() == 0) {
                        i = copylength;
                    } else {
                        offset += copylength;
                        required = byteCount - copylength;
                    }
                } else {
                    required = byteCount;
                }
                while (true) {
                    if (this.markpos == -1 && required >= localBuf.length) {
                        read = localIn.read(buffer, offset, required);
                        if (read == -1) {
                            if (required != byteCount) {
                                i = byteCount - required;
                            }
                        }
                    } else if (fillbuf(localIn, localBuf) == -1) {
                        if (required != byteCount) {
                            i = byteCount - required;
                        }
                    } else if (localBuf == this.buf || (localBuf = this.buf) != null) {
                        read = this.count - this.pos >= required ? required : this.count - this.pos;
                        System.arraycopy(localBuf, this.pos, buffer, offset, read);
                        this.pos += read;
                    } else {
                        throw streamClosed();
                    }
                    required -= read;
                    if (required == 0) {
                        i = byteCount;
                        break;
                    } else if (localIn.available() == 0) {
                        i = byteCount - required;
                        break;
                    } else {
                        offset += read;
                    }
                }
            }
        }
        return i;
    }

    public synchronized void reset() throws IOException {
        if (this.buf == null) {
            throw new IOException("Stream is closed");
        } else if (-1 == this.markpos) {
            throw new InvalidMarkException("Mark has been invalidated");
        } else {
            this.pos = this.markpos;
        }
    }

    public synchronized long skip(long byteCount) throws IOException {
        byte[] localBuf = this.buf;
        InputStream localIn = this.in;
        if (localBuf == null) {
            throw streamClosed();
        } else if (byteCount < 1) {
            byteCount = 0;
        } else if (localIn == null) {
            throw streamClosed();
        } else if (((long) (this.count - this.pos)) >= byteCount) {
            this.pos = (int) (((long) this.pos) + byteCount);
        } else {
            long read = (long) (this.count - this.pos);
            this.pos = this.count;
            if (this.markpos == -1 || byteCount > ((long) this.marklimit)) {
                byteCount = read + localIn.skip(byteCount - read);
            } else if (fillbuf(localIn, localBuf) == -1) {
                byteCount = read;
            } else if (((long) (this.count - this.pos)) >= byteCount - read) {
                this.pos = (int) (((long) this.pos) + (byteCount - read));
            } else {
                long read2 = (((long) this.count) + read) - ((long) this.pos);
                this.pos = this.count;
                byteCount = read2;
            }
        }
        return byteCount;
    }

    public static class InvalidMarkException extends RuntimeException {
        private static final long serialVersionUID = -4338378848813561757L;

        public InvalidMarkException(String detailMessage) {
            super(detailMessage);
        }
    }
}
