package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import java.io.IOException;
import java.nio.ByteBuffer;

@Descriptor(tags = {6})
public class SLConfigDescriptor extends BaseDescriptor {
    int predefined;

    public SLConfigDescriptor() {
        this.tag = 6;
    }

    public int getPredefined() {
        return this.predefined;
    }

    public void setPredefined(int predefined2) {
        this.predefined = predefined2;
    }

    public void parseDetail(ByteBuffer bb) throws IOException {
        this.predefined = IsoTypeReader.readUInt8(bb);
    }

    /* access modifiers changed from: package-private */
    public int getContentSize() {
        return 1;
    }

    public ByteBuffer serialize() {
        ByteBuffer out = ByteBuffer.allocate(getSize());
        IsoTypeWriter.writeUInt8(out, 6);
        writeSize(out, getContentSize());
        IsoTypeWriter.writeUInt8(out, this.predefined);
        return out;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SLConfigDescriptor");
        sb.append("{predefined=").append(this.predefined);
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
        if (this.predefined != ((SLConfigDescriptor) o).predefined) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.predefined;
    }
}
