package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import com.coremedia.iso.IsoTypeReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public abstract class InitialObjectDescriptor extends ObjectDescriptorBase {
    int audioProfileLevelIndication;
    List<ESDescriptor> esDescriptors = new ArrayList();
    List<ExtensionDescriptor> extensionDescriptors = new ArrayList();
    int graphicsProfileLevelIndication;
    int includeInlineProfileLevelFlag;
    int oDProfileLevelIndication;
    private int objectDescriptorId;
    int sceneProfileLevelIndication;
    List<BaseDescriptor> unknownDescriptors = new ArrayList();
    int urlFlag;
    int urlLength;
    String urlString;
    int visualProfileLevelIndication;

    public void parseDetail(ByteBuffer bb) throws IOException {
        int sizeLeft;
        int data = IsoTypeReader.readUInt16(bb);
        this.objectDescriptorId = (65472 & data) >> 6;
        this.urlFlag = (data & 63) >> 5;
        this.includeInlineProfileLevelFlag = (data & 31) >> 4;
        int sizeLeft2 = getSize() - 2;
        if (this.urlFlag == 1) {
            this.urlLength = IsoTypeReader.readUInt8(bb);
            this.urlString = IsoTypeReader.readString(bb, this.urlLength);
            sizeLeft = sizeLeft2 - (this.urlLength + 1);
        } else {
            this.oDProfileLevelIndication = IsoTypeReader.readUInt8(bb);
            this.sceneProfileLevelIndication = IsoTypeReader.readUInt8(bb);
            this.audioProfileLevelIndication = IsoTypeReader.readUInt8(bb);
            this.visualProfileLevelIndication = IsoTypeReader.readUInt8(bb);
            this.graphicsProfileLevelIndication = IsoTypeReader.readUInt8(bb);
            sizeLeft = sizeLeft2 - 5;
            if (sizeLeft > 2) {
                BaseDescriptor descriptor = ObjectDescriptorFactory.createFrom(-1, bb);
                sizeLeft -= descriptor.getSize();
                if (descriptor instanceof ESDescriptor) {
                    this.esDescriptors.add((ESDescriptor) descriptor);
                } else {
                    this.unknownDescriptors.add(descriptor);
                }
            }
        }
        if (sizeLeft > 2) {
            BaseDescriptor descriptor2 = ObjectDescriptorFactory.createFrom(-1, bb);
            if (descriptor2 instanceof ExtensionDescriptor) {
                this.extensionDescriptors.add((ExtensionDescriptor) descriptor2);
            } else {
                this.unknownDescriptors.add(descriptor2);
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("InitialObjectDescriptor");
        sb.append("{objectDescriptorId=").append(this.objectDescriptorId);
        sb.append(", urlFlag=").append(this.urlFlag);
        sb.append(", includeInlineProfileLevelFlag=").append(this.includeInlineProfileLevelFlag);
        sb.append(", urlLength=").append(this.urlLength);
        sb.append(", urlString='").append(this.urlString).append('\'');
        sb.append(", oDProfileLevelIndication=").append(this.oDProfileLevelIndication);
        sb.append(", sceneProfileLevelIndication=").append(this.sceneProfileLevelIndication);
        sb.append(", audioProfileLevelIndication=").append(this.audioProfileLevelIndication);
        sb.append(", visualProfileLevelIndication=").append(this.visualProfileLevelIndication);
        sb.append(", graphicsProfileLevelIndication=").append(this.graphicsProfileLevelIndication);
        sb.append(", esDescriptors=").append(this.esDescriptors);
        sb.append(", extensionDescriptors=").append(this.extensionDescriptors);
        sb.append(", unknownDescriptors=").append(this.unknownDescriptors);
        sb.append('}');
        return sb.toString();
    }
}
