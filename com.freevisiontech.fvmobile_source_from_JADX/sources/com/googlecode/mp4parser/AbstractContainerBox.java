package com.googlecode.mp4parser;

import com.coremedia.iso.BoxParser;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.Container;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import p012tv.danmaku.ijk.media.player.IjkMediaMeta;

public class AbstractContainerBox extends BasicContainer implements Box {
    protected boolean largeBox;
    private long offset;
    Container parent;
    protected String type;

    public AbstractContainerBox(String type2) {
        this.type = type2;
    }

    public Container getParent() {
        return this.parent;
    }

    public long getOffset() {
        return this.offset;
    }

    public void setParent(Container parent2) {
        this.parent = parent2;
    }

    public long getSize() {
        long s = getContainerSize();
        return ((long) ((this.largeBox || 8 + s >= IjkMediaMeta.AV_CH_WIDE_RIGHT) ? 16 : 8)) + s;
    }

    public String getType() {
        return this.type;
    }

    /* access modifiers changed from: protected */
    public ByteBuffer getHeader() {
        ByteBuffer header;
        if (this.largeBox || getSize() >= IjkMediaMeta.AV_CH_WIDE_RIGHT) {
            byte[] bArr = new byte[16];
            bArr[3] = 1;
            bArr[4] = this.type.getBytes()[0];
            bArr[5] = this.type.getBytes()[1];
            bArr[6] = this.type.getBytes()[2];
            bArr[7] = this.type.getBytes()[3];
            header = ByteBuffer.wrap(bArr);
            header.position(8);
            IsoTypeWriter.writeUInt64(header, getSize());
        } else {
            byte[] bArr2 = new byte[8];
            bArr2[4] = this.type.getBytes()[0];
            bArr2[5] = this.type.getBytes()[1];
            bArr2[6] = this.type.getBytes()[2];
            bArr2[7] = this.type.getBytes()[3];
            header = ByteBuffer.wrap(bArr2);
            IsoTypeWriter.writeUInt32(header, getSize());
        }
        header.rewind();
        return header;
    }

    public void parse(DataSource dataSource, ByteBuffer header, long contentSize, BoxParser boxParser) throws IOException {
        this.offset = dataSource.position() - ((long) header.remaining());
        this.largeBox = header.remaining() == 16;
        initContainer(dataSource, contentSize, boxParser);
    }

    public void getBox(WritableByteChannel writableByteChannel) throws IOException {
        writableByteChannel.write(getHeader());
        writeContainer(writableByteChannel);
    }

    public void initContainer(DataSource dataSource, long containerSize, BoxParser boxParser) throws IOException {
        this.dataSource = dataSource;
        this.parsePosition = dataSource.position();
        this.startPosition = this.parsePosition - ((long) ((this.largeBox || 8 + containerSize >= IjkMediaMeta.AV_CH_WIDE_RIGHT) ? 16 : 8));
        dataSource.position(dataSource.position() + containerSize);
        this.endPosition = dataSource.position();
        this.boxParser = boxParser;
    }
}
