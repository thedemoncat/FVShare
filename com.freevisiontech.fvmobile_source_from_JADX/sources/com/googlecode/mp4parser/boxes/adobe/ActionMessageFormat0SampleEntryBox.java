package com.googlecode.mp4parser.boxes.adobe;

import com.coremedia.iso.BoxParser;
import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.boxes.sampleentry.AbstractSampleEntry;
import com.googlecode.mp4parser.DataSource;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import p012tv.danmaku.ijk.media.player.IjkMediaMeta;

public class ActionMessageFormat0SampleEntryBox extends AbstractSampleEntry {
    public static final String TYPE = "amf0";

    public ActionMessageFormat0SampleEntryBox() {
        super(TYPE);
    }

    public void parse(DataSource dataSource, ByteBuffer header, long contentSize, BoxParser boxParser) throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(8);
        dataSource.read(bb);
        bb.position(6);
        this.dataReferenceIndex = IsoTypeReader.readUInt16(bb);
        initContainer(dataSource, contentSize - 8, boxParser);
    }

    public void getBox(WritableByteChannel writableByteChannel) throws IOException {
        writableByteChannel.write(getHeader());
        ByteBuffer bb = ByteBuffer.allocate(8);
        bb.position(6);
        IsoTypeWriter.writeUInt16(bb, this.dataReferenceIndex);
        writableByteChannel.write((ByteBuffer) bb.rewind());
        writeContainer(writableByteChannel);
    }

    public long getSize() {
        long s = getContainerSize();
        return ((long) ((this.largeBox || s + 8 >= IjkMediaMeta.AV_CH_WIDE_RIGHT) ? 16 : 8)) + s + 8;
    }
}
