package com.coremedia.iso;

import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.Container;
import com.coremedia.iso.boxes.UserBox;
import com.googlecode.mp4parser.DataSource;
import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

public abstract class AbstractBoxParser implements BoxParser {
    private static Logger LOG = Logger.getLogger(AbstractBoxParser.class.getName());
    ThreadLocal<ByteBuffer> header = new ThreadLocal<ByteBuffer>() {
        /* access modifiers changed from: protected */
        public ByteBuffer initialValue() {
            return ByteBuffer.allocate(32);
        }
    };

    public abstract Box createBox(String str, byte[] bArr, String str2);

    public Box parseBox(DataSource byteChannel, Container parent) throws IOException {
        int b;
        long contentSize;
        long startPos = byteChannel.position();
        this.header.get().rewind().limit(8);
        do {
            b = byteChannel.read(this.header.get());
            if (b == 8) {
                this.header.get().rewind();
                long size = IsoTypeReader.readUInt32(this.header.get());
                if (size >= 8 || size <= 1) {
                    String type = IsoTypeReader.read4cc(this.header.get());
                    byte[] usertype = null;
                    if (size == 1) {
                        this.header.get().limit(16);
                        byteChannel.read(this.header.get());
                        this.header.get().position(8);
                        contentSize = IsoTypeReader.readUInt64(this.header.get()) - 16;
                    } else if (size == 0) {
                        contentSize = byteChannel.size() - byteChannel.position();
                        long size2 = contentSize + 8;
                    } else {
                        contentSize = size - 8;
                    }
                    if (UserBox.TYPE.equals(type)) {
                        this.header.get().limit(this.header.get().limit() + 16);
                        byteChannel.read(this.header.get());
                        usertype = new byte[16];
                        for (int i = this.header.get().position() - 16; i < this.header.get().position(); i++) {
                            usertype[i - (this.header.get().position() - 16)] = this.header.get().get(i);
                        }
                        contentSize -= 16;
                    }
                    Box box = createBox(type, usertype, parent instanceof Box ? ((Box) parent).getType() : "");
                    box.setParent(parent);
                    this.header.get().rewind();
                    box.parse(byteChannel, this.header.get(), contentSize, this);
                    return box;
                }
                LOG.severe("Plausibility check failed: size < 8 (size = " + size + "). Stop parsing!");
                return null;
            }
        } while (b >= 0);
        byteChannel.position(startPos);
        throw new EOFException();
    }
}
