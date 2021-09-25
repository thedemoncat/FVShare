package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import com.coremedia.iso.IsoTypeReader;
import java.io.IOException;
import java.nio.ByteBuffer;

@Descriptor(tags = {0})
public abstract class BaseDescriptor {
    static final /* synthetic */ boolean $assertionsDisabled = (!BaseDescriptor.class.desiredAssertionStatus());
    int sizeBytes;
    int sizeOfInstance;
    int tag;

    /* access modifiers changed from: package-private */
    public abstract int getContentSize();

    public abstract void parseDetail(ByteBuffer byteBuffer) throws IOException;

    public abstract ByteBuffer serialize();

    public int getTag() {
        return this.tag;
    }

    public void writeSize(ByteBuffer bb, int size) {
        int pos = bb.position();
        int i = 0;
        while (true) {
            if (size > 0 || i < this.sizeBytes) {
                i++;
                if (size > 0) {
                    bb.put((getSizeSize() + pos) - i, (byte) (size & 127));
                } else {
                    bb.put((getSizeSize() + pos) - i, Byte.MIN_VALUE);
                }
                size >>>= 7;
            } else {
                bb.position(getSizeSize() + pos);
                return;
            }
        }
    }

    public int getSizeSize() {
        int size = getContentSize();
        int i = 0;
        while (true) {
            if (size <= 0 && i >= this.sizeBytes) {
                return i;
            }
            size >>>= 7;
            i++;
        }
    }

    public int getSize() {
        return getContentSize() + getSizeSize() + 1;
    }

    public final void parse(int tag2, ByteBuffer bb) throws IOException {
        this.tag = tag2;
        int tmp = IsoTypeReader.readUInt8(bb);
        int i = 0 + 1;
        this.sizeOfInstance = tmp & 127;
        while ((tmp >>> 7) == 1) {
            tmp = IsoTypeReader.readUInt8(bb);
            i++;
            this.sizeOfInstance = (this.sizeOfInstance << 7) | (tmp & 127);
        }
        this.sizeBytes = i;
        ByteBuffer detailSource = bb.slice();
        detailSource.limit(this.sizeOfInstance);
        parseDetail(detailSource);
        if ($assertionsDisabled || detailSource.remaining() == 0) {
            bb.position(bb.position() + this.sizeOfInstance);
            return;
        }
        throw new AssertionError(String.valueOf(getClass().getSimpleName()) + " has not been fully parsed");
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BaseDescriptor");
        sb.append("{tag=").append(this.tag);
        sb.append(", sizeOfInstance=").append(this.sizeOfInstance);
        sb.append('}');
        return sb.toString();
    }
}
