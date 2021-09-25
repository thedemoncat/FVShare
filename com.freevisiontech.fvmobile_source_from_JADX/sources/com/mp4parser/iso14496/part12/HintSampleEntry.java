package com.mp4parser.iso14496.part12;

import com.coremedia.iso.BoxParser;
import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.boxes.sampleentry.AbstractSampleEntry;
import com.googlecode.mp4parser.DataSource;
import com.googlecode.mp4parser.util.CastUtils;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import p012tv.danmaku.ijk.media.player.IjkMediaMeta;

public class HintSampleEntry extends AbstractSampleEntry {
    protected byte[] data;

    public HintSampleEntry(String type) {
        super(type);
    }

    public void parse(DataSource dataSource, ByteBuffer header, long contentSize, BoxParser boxParser) throws IOException {
        ByteBuffer b1 = ByteBuffer.allocate(8);
        dataSource.read(b1);
        b1.position(6);
        this.dataReferenceIndex = IsoTypeReader.readUInt16(b1);
        this.data = new byte[CastUtils.l2i(contentSize - 8)];
        dataSource.read(ByteBuffer.wrap(this.data));
    }

    public void getBox(WritableByteChannel writableByteChannel) throws IOException {
        writableByteChannel.write(getHeader());
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        byteBuffer.position(6);
        IsoTypeWriter.writeUInt16(byteBuffer, this.dataReferenceIndex);
        byteBuffer.rewind();
        writableByteChannel.write(byteBuffer);
        writableByteChannel.write(ByteBuffer.wrap(this.data));
    }

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] data2) {
        this.data = data2;
    }

    public long getSize() {
        long s = (long) (this.data.length + 8);
        return ((long) ((this.largeBox || 8 + s >= IjkMediaMeta.AV_CH_WIDE_RIGHT) ? 16 : 8)) + s;
    }
}
