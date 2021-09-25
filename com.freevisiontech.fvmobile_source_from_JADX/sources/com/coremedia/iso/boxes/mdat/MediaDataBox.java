package com.coremedia.iso.boxes.mdat;

import com.coremedia.iso.BoxParser;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.DataSource;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

public final class MediaDataBox implements Box {
    public static final String TYPE = "mdat";
    private DataSource dataSource;
    private long offset;
    Container parent;
    private long size;

    public Container getParent() {
        return this.parent;
    }

    public void setParent(Container parent2) {
        this.parent = parent2;
    }

    public String getType() {
        return TYPE;
    }

    private static void transfer(DataSource from, long position, long count, WritableByteChannel to) throws IOException {
        long offset2 = 0;
        while (offset2 < count) {
            offset2 += from.transferTo(position + offset2, Math.min(67076096, count - offset2), to);
        }
    }

    public void getBox(WritableByteChannel writableByteChannel) throws IOException {
        transfer(this.dataSource, this.offset, this.size, writableByteChannel);
    }

    public long getSize() {
        return this.size;
    }

    public long getOffset() {
        return this.offset;
    }

    public void parse(DataSource dataSource2, ByteBuffer header, long contentSize, BoxParser boxParser) throws IOException {
        this.offset = dataSource2.position() - ((long) header.remaining());
        this.dataSource = dataSource2;
        this.size = ((long) header.remaining()) + contentSize;
        dataSource2.position(dataSource2.position() + contentSize);
    }

    public String toString() {
        return "MediaDataBox{size=" + this.size + '}';
    }
}
