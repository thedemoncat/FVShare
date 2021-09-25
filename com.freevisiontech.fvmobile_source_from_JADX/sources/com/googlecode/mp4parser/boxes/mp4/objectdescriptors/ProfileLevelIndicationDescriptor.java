package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import java.io.IOException;
import java.nio.ByteBuffer;

@Descriptor(tags = {20})
public class ProfileLevelIndicationDescriptor extends BaseDescriptor {
    int profileLevelIndicationIndex;

    public ProfileLevelIndicationDescriptor() {
        this.tag = 20;
    }

    public void parseDetail(ByteBuffer bb) throws IOException {
        this.profileLevelIndicationIndex = IsoTypeReader.readUInt8(bb);
    }

    public ByteBuffer serialize() {
        ByteBuffer out = ByteBuffer.allocate(getSize());
        IsoTypeWriter.writeUInt8(out, 20);
        writeSize(out, getContentSize());
        IsoTypeWriter.writeUInt8(out, this.profileLevelIndicationIndex);
        return out;
    }

    public int getContentSize() {
        return 1;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ProfileLevelIndicationDescriptor");
        sb.append("{profileLevelIndicationIndex=").append(Integer.toHexString(this.profileLevelIndicationIndex));
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
        if (this.profileLevelIndicationIndex != ((ProfileLevelIndicationDescriptor) o).profileLevelIndicationIndex) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.profileLevelIndicationIndex;
    }
}
