package com.bumptech.glide.disklrucache;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

class StrictLineReader implements Closeable {

    /* renamed from: CR */
    private static final byte f1172CR = 13;

    /* renamed from: LF */
    private static final byte f1173LF = 10;
    private byte[] buf;
    /* access modifiers changed from: private */
    public final Charset charset;
    private int end;

    /* renamed from: in */
    private final InputStream f1174in;
    private int pos;

    public StrictLineReader(InputStream in, Charset charset2) {
        this(in, 8192, charset2);
    }

    public StrictLineReader(InputStream in, int capacity, Charset charset2) {
        if (in == null || charset2 == null) {
            throw new NullPointerException();
        } else if (capacity < 0) {
            throw new IllegalArgumentException("capacity <= 0");
        } else if (!charset2.equals(Util.US_ASCII)) {
            throw new IllegalArgumentException("Unsupported encoding");
        } else {
            this.f1174in = in;
            this.charset = charset2;
            this.buf = new byte[capacity];
        }
    }

    public void close() throws IOException {
        synchronized (this.f1174in) {
            if (this.buf != null) {
                this.buf = null;
                this.f1174in.close();
            }
        }
    }

    public String readLine() throws IOException {
        int i;
        String res;
        synchronized (this.f1174in) {
            if (this.buf == null) {
                throw new IOException("LineReader is closed");
            }
            if (this.pos >= this.end) {
                fillBuf();
            }
            int i2 = this.pos;
            while (true) {
                if (i2 == this.end) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream((this.end - this.pos) + 80) {
                        public String toString() {
                            try {
                                return new String(this.buf, 0, (this.count <= 0 || this.buf[this.count + -1] != 13) ? this.count : this.count - 1, StrictLineReader.this.charset.name());
                            } catch (UnsupportedEncodingException e) {
                                throw new AssertionError(e);
                            }
                        }
                    };
                    loop1:
                    while (true) {
                        out.write(this.buf, this.pos, this.end - this.pos);
                        this.end = -1;
                        fillBuf();
                        i = this.pos;
                        while (true) {
                            if (i != this.end) {
                                if (this.buf[i] == 10) {
                                    break loop1;
                                }
                                i++;
                            }
                        }
                    }
                    if (i != this.pos) {
                        out.write(this.buf, this.pos, i - this.pos);
                    }
                    this.pos = i + 1;
                    res = out.toString();
                } else if (this.buf[i2] == 10) {
                    res = new String(this.buf, this.pos, ((i2 == this.pos || this.buf[i2 + -1] != 13) ? i2 : i2 - 1) - this.pos, this.charset.name());
                    this.pos = i2 + 1;
                } else {
                    i2++;
                }
            }
        }
        return res;
    }

    public boolean hasUnterminatedLine() {
        return this.end == -1;
    }

    private void fillBuf() throws IOException {
        int result = this.f1174in.read(this.buf, 0, this.buf.length);
        if (result == -1) {
            throw new EOFException();
        }
        this.pos = 0;
        this.end = result;
    }
}
