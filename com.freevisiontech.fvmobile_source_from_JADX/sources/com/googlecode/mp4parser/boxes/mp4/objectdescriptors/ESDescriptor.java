package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Descriptor(tags = {3})
public class ESDescriptor extends BaseDescriptor {
    private static Logger log = Logger.getLogger(ESDescriptor.class.getName());
    int URLFlag;
    int URLLength = 0;
    String URLString;
    DecoderConfigDescriptor decoderConfigDescriptor;
    int dependsOnEsId;
    int esId;
    int oCREsId;
    int oCRstreamFlag;
    List<BaseDescriptor> otherDescriptors = new ArrayList();
    int remoteODFlag;
    SLConfigDescriptor slConfigDescriptor;
    int streamDependenceFlag;
    int streamPriority;

    public ESDescriptor() {
        this.tag = 3;
    }

    public void parseDetail(ByteBuffer bb) throws IOException {
        this.esId = IsoTypeReader.readUInt16(bb);
        int data = IsoTypeReader.readUInt8(bb);
        this.streamDependenceFlag = data >>> 7;
        this.URLFlag = (data >>> 6) & 1;
        this.oCRstreamFlag = (data >>> 5) & 1;
        this.streamPriority = data & 31;
        if (this.streamDependenceFlag == 1) {
            this.dependsOnEsId = IsoTypeReader.readUInt16(bb);
        }
        if (this.URLFlag == 1) {
            this.URLLength = IsoTypeReader.readUInt8(bb);
            this.URLString = IsoTypeReader.readString(bb, this.URLLength);
        }
        if (this.oCRstreamFlag == 1) {
            this.oCREsId = IsoTypeReader.readUInt16(bb);
        }
        while (bb.remaining() > 1) {
            BaseDescriptor descriptor = ObjectDescriptorFactory.createFrom(-1, bb);
            if (descriptor instanceof DecoderConfigDescriptor) {
                this.decoderConfigDescriptor = (DecoderConfigDescriptor) descriptor;
            } else if (descriptor instanceof SLConfigDescriptor) {
                this.slConfigDescriptor = (SLConfigDescriptor) descriptor;
            } else {
                this.otherDescriptors.add(descriptor);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public int getContentSize() {
        int out = 3;
        if (this.streamDependenceFlag > 0) {
            out = 3 + 2;
        }
        if (this.URLFlag > 0) {
            out += this.URLLength + 1;
        }
        if (this.oCRstreamFlag > 0) {
            out += 2;
        }
        int out2 = out + this.decoderConfigDescriptor.getSize() + this.slConfigDescriptor.getSize();
        if (this.otherDescriptors.size() <= 0) {
            return out2;
        }
        throw new RuntimeException(" Doesn't handle other descriptors yet");
    }

    public ByteBuffer serialize() {
        ByteBuffer out = ByteBuffer.wrap(new byte[getSize()]);
        IsoTypeWriter.writeUInt8(out, 3);
        writeSize(out, getContentSize());
        IsoTypeWriter.writeUInt16(out, this.esId);
        IsoTypeWriter.writeUInt8(out, (this.streamDependenceFlag << 7) | (this.URLFlag << 6) | (this.oCRstreamFlag << 5) | (this.streamPriority & 31));
        if (this.streamDependenceFlag > 0) {
            IsoTypeWriter.writeUInt16(out, this.dependsOnEsId);
        }
        if (this.URLFlag > 0) {
            IsoTypeWriter.writeUInt8(out, this.URLLength);
            IsoTypeWriter.writeUtf8String(out, this.URLString);
        }
        if (this.oCRstreamFlag > 0) {
            IsoTypeWriter.writeUInt16(out, this.oCREsId);
        }
        ByteBuffer dec = this.decoderConfigDescriptor.serialize();
        ByteBuffer sl = this.slConfigDescriptor.serialize();
        out.put(dec.array());
        out.put(sl.array());
        return out;
    }

    public DecoderConfigDescriptor getDecoderConfigDescriptor() {
        return this.decoderConfigDescriptor;
    }

    public void setDecoderConfigDescriptor(DecoderConfigDescriptor decoderConfigDescriptor2) {
        this.decoderConfigDescriptor = decoderConfigDescriptor2;
    }

    public SLConfigDescriptor getSlConfigDescriptor() {
        return this.slConfigDescriptor;
    }

    public void setSlConfigDescriptor(SLConfigDescriptor slConfigDescriptor2) {
        this.slConfigDescriptor = slConfigDescriptor2;
    }

    public List<BaseDescriptor> getOtherDescriptors() {
        return this.otherDescriptors;
    }

    public int getoCREsId() {
        return this.oCREsId;
    }

    public void setoCREsId(int oCREsId2) {
        this.oCREsId = oCREsId2;
    }

    public int getEsId() {
        return this.esId;
    }

    public void setEsId(int esId2) {
        this.esId = esId2;
    }

    public int getStreamDependenceFlag() {
        return this.streamDependenceFlag;
    }

    public void setStreamDependenceFlag(int streamDependenceFlag2) {
        this.streamDependenceFlag = streamDependenceFlag2;
    }

    public int getURLFlag() {
        return this.URLFlag;
    }

    public void setURLFlag(int URLFlag2) {
        this.URLFlag = URLFlag2;
    }

    public int getoCRstreamFlag() {
        return this.oCRstreamFlag;
    }

    public void setoCRstreamFlag(int oCRstreamFlag2) {
        this.oCRstreamFlag = oCRstreamFlag2;
    }

    public int getStreamPriority() {
        return this.streamPriority;
    }

    public void setStreamPriority(int streamPriority2) {
        this.streamPriority = streamPriority2;
    }

    public int getURLLength() {
        return this.URLLength;
    }

    public void setURLLength(int URLLength2) {
        this.URLLength = URLLength2;
    }

    public String getURLString() {
        return this.URLString;
    }

    public void setURLString(String URLString2) {
        this.URLString = URLString2;
    }

    public int getRemoteODFlag() {
        return this.remoteODFlag;
    }

    public void setRemoteODFlag(int remoteODFlag2) {
        this.remoteODFlag = remoteODFlag2;
    }

    public int getDependsOnEsId() {
        return this.dependsOnEsId;
    }

    public void setDependsOnEsId(int dependsOnEsId2) {
        this.dependsOnEsId = dependsOnEsId2;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ESDescriptor");
        sb.append("{esId=").append(this.esId);
        sb.append(", streamDependenceFlag=").append(this.streamDependenceFlag);
        sb.append(", URLFlag=").append(this.URLFlag);
        sb.append(", oCRstreamFlag=").append(this.oCRstreamFlag);
        sb.append(", streamPriority=").append(this.streamPriority);
        sb.append(", URLLength=").append(this.URLLength);
        sb.append(", URLString='").append(this.URLString).append('\'');
        sb.append(", remoteODFlag=").append(this.remoteODFlag);
        sb.append(", dependsOnEsId=").append(this.dependsOnEsId);
        sb.append(", oCREsId=").append(this.oCREsId);
        sb.append(", decoderConfigDescriptor=").append(this.decoderConfigDescriptor);
        sb.append(", slConfigDescriptor=").append(this.slConfigDescriptor);
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
        ESDescriptor that = (ESDescriptor) o;
        if (this.URLFlag != that.URLFlag) {
            return false;
        }
        if (this.URLLength != that.URLLength) {
            return false;
        }
        if (this.dependsOnEsId != that.dependsOnEsId) {
            return false;
        }
        if (this.esId != that.esId) {
            return false;
        }
        if (this.oCREsId != that.oCREsId) {
            return false;
        }
        if (this.oCRstreamFlag != that.oCRstreamFlag) {
            return false;
        }
        if (this.remoteODFlag != that.remoteODFlag) {
            return false;
        }
        if (this.streamDependenceFlag != that.streamDependenceFlag) {
            return false;
        }
        if (this.streamPriority != that.streamPriority) {
            return false;
        }
        if (this.URLString == null ? that.URLString != null : !this.URLString.equals(that.URLString)) {
            return false;
        }
        if (this.decoderConfigDescriptor == null ? that.decoderConfigDescriptor != null : !this.decoderConfigDescriptor.equals(that.decoderConfigDescriptor)) {
            return false;
        }
        if (this.otherDescriptors == null ? that.otherDescriptors != null : !this.otherDescriptors.equals(that.otherDescriptors)) {
            return false;
        }
        if (this.slConfigDescriptor != null) {
            if (this.slConfigDescriptor.equals(that.slConfigDescriptor)) {
                return true;
            }
        } else if (that.slConfigDescriptor == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int i;
        int i2;
        int i3 = 0;
        int hashCode = ((((((((((((((((((this.esId * 31) + this.streamDependenceFlag) * 31) + this.URLFlag) * 31) + this.oCRstreamFlag) * 31) + this.streamPriority) * 31) + this.URLLength) * 31) + (this.URLString != null ? this.URLString.hashCode() : 0)) * 31) + this.remoteODFlag) * 31) + this.dependsOnEsId) * 31) + this.oCREsId) * 31;
        if (this.decoderConfigDescriptor != null) {
            i = this.decoderConfigDescriptor.hashCode();
        } else {
            i = 0;
        }
        int i4 = (hashCode + i) * 31;
        if (this.slConfigDescriptor != null) {
            i2 = this.slConfigDescriptor.hashCode();
        } else {
            i2 = 0;
        }
        int i5 = (i4 + i2) * 31;
        if (this.otherDescriptors != null) {
            i3 = this.otherDescriptors.hashCode();
        }
        return i5 + i3;
    }
}
