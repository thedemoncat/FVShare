package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import com.coremedia.iso.Hex;
import com.coremedia.iso.IsoTypeWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

@Descriptor(tags = {5})
public class DecoderSpecificInfo extends BaseDescriptor {
    byte[] bytes;

    public DecoderSpecificInfo() {
        this.tag = 5;
    }

    public void parseDetail(ByteBuffer bb) throws IOException {
        this.bytes = new byte[bb.remaining()];
        bb.get(this.bytes);
    }

    public void setData(byte[] bytes2) {
        this.bytes = bytes2;
    }

    /* access modifiers changed from: package-private */
    public int getContentSize() {
        return this.bytes.length;
    }

    public ByteBuffer serialize() {
        ByteBuffer out = ByteBuffer.allocate(getSize());
        IsoTypeWriter.writeUInt8(out, this.tag);
        writeSize(out, getContentSize());
        out.put(this.bytes);
        return (ByteBuffer) out.rewind();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DecoderSpecificInfo");
        sb.append("{bytes=").append(this.bytes == null ? "null" : Hex.encodeHex(this.bytes));
        sb.append('}');
        return sb.toString();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!Arrays.equals(this.bytes, ((DecoderSpecificInfo) o).bytes)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        if (this.bytes != null) {
            return Arrays.hashCode(this.bytes);
        }
        return 0;
    }
}
