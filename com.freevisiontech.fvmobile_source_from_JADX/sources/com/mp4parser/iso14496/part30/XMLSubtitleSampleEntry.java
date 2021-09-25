package com.mp4parser.iso14496.part30;

import com.coremedia.iso.BoxParser;
import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.boxes.sampleentry.AbstractSampleEntry;
import com.googlecode.mp4parser.DataSource;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import p012tv.danmaku.ijk.media.player.IjkMediaMeta;

public class XMLSubtitleSampleEntry extends AbstractSampleEntry {
    public static final String TYPE = "stpp";
    private String auxiliaryMimeTypes = "";
    private String namespace = "";
    private String schemaLocation = "";

    public XMLSubtitleSampleEntry() {
        super(TYPE);
    }

    public long getSize() {
        long s = getContainerSize();
        long t = (long) (this.namespace.length() + 8 + this.schemaLocation.length() + this.auxiliaryMimeTypes.length() + 3);
        return ((long) ((this.largeBox || (s + t) + 8 >= IjkMediaMeta.AV_CH_WIDE_RIGHT) ? 16 : 8)) + s + t;
    }

    public void parse(DataSource dataSource, ByteBuffer header, long contentSize, BoxParser boxParser) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        dataSource.read((ByteBuffer) byteBuffer.rewind());
        byteBuffer.position(6);
        this.dataReferenceIndex = IsoTypeReader.readUInt16(byteBuffer);
        long start = dataSource.position();
        ByteBuffer content = ByteBuffer.allocate(1024);
        dataSource.read((ByteBuffer) content.rewind());
        this.namespace = IsoTypeReader.readString((ByteBuffer) content.rewind());
        dataSource.position(((long) this.namespace.length()) + start + 1);
        dataSource.read((ByteBuffer) content.rewind());
        this.schemaLocation = IsoTypeReader.readString((ByteBuffer) content.rewind());
        dataSource.position(((long) this.namespace.length()) + start + ((long) this.schemaLocation.length()) + 2);
        dataSource.read((ByteBuffer) content.rewind());
        this.auxiliaryMimeTypes = IsoTypeReader.readString((ByteBuffer) content.rewind());
        dataSource.position(((long) this.namespace.length()) + start + ((long) this.schemaLocation.length()) + ((long) this.auxiliaryMimeTypes.length()) + 3);
        initContainer(dataSource, contentSize - ((long) ((((header.remaining() + this.namespace.length()) + this.schemaLocation.length()) + this.auxiliaryMimeTypes.length()) + 3)), boxParser);
    }

    public void getBox(WritableByteChannel writableByteChannel) throws IOException {
        writableByteChannel.write(getHeader());
        ByteBuffer byteBuffer = ByteBuffer.allocate(this.namespace.length() + 8 + this.schemaLocation.length() + this.auxiliaryMimeTypes.length() + 3);
        byteBuffer.position(6);
        IsoTypeWriter.writeUInt16(byteBuffer, this.dataReferenceIndex);
        IsoTypeWriter.writeZeroTermUtf8String(byteBuffer, this.namespace);
        IsoTypeWriter.writeZeroTermUtf8String(byteBuffer, this.schemaLocation);
        IsoTypeWriter.writeZeroTermUtf8String(byteBuffer, this.auxiliaryMimeTypes);
        writableByteChannel.write((ByteBuffer) byteBuffer.rewind());
        writeContainer(writableByteChannel);
    }

    public String getNamespace() {
        return this.namespace;
    }

    public void setNamespace(String namespace2) {
        this.namespace = namespace2;
    }

    public String getSchemaLocation() {
        return this.schemaLocation;
    }

    public void setSchemaLocation(String schemaLocation2) {
        this.schemaLocation = schemaLocation2;
    }

    public String getAuxiliaryMimeTypes() {
        return this.auxiliaryMimeTypes;
    }

    public void setAuxiliaryMimeTypes(String auxiliaryMimeTypes2) {
        this.auxiliaryMimeTypes = auxiliaryMimeTypes2;
    }
}
