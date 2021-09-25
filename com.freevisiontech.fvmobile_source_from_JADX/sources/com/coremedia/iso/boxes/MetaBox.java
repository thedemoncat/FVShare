package com.coremedia.iso.boxes;

import com.coremedia.iso.BoxParser;
import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.AbstractContainerBox;
import com.googlecode.mp4parser.DataSource;
import com.googlecode.mp4parser.MemoryDataSourceImpl;
import com.googlecode.mp4parser.util.CastUtils;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import p012tv.danmaku.ijk.media.player.IjkMediaMeta;

public class MetaBox extends AbstractContainerBox {
    public static final String TYPE = "meta";
    private int flags;
    private boolean isFullBox = true;
    private int version;

    public MetaBox() {
        super(TYPE);
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version2) {
        this.version = version2;
    }

    public int getFlags() {
        return this.flags;
    }

    public void setFlags(int flags2) {
        this.flags = flags2;
    }

    /* access modifiers changed from: protected */
    public final long parseVersionAndFlags(ByteBuffer content) {
        this.version = IsoTypeReader.readUInt8(content);
        this.flags = IsoTypeReader.readUInt24(content);
        return 4;
    }

    /* access modifiers changed from: protected */
    public final void writeVersionAndFlags(ByteBuffer bb) {
        IsoTypeWriter.writeUInt8(bb, this.version);
        IsoTypeWriter.writeUInt24(bb, this.flags);
    }

    public void parse(DataSource dataSource, ByteBuffer header, long contentSize, BoxParser boxParser) throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(CastUtils.l2i(contentSize));
        dataSource.read(bb);
        bb.position(4);
        if (HandlerBox.TYPE.equals(IsoTypeReader.read4cc(bb))) {
            this.isFullBox = false;
            initContainer(new MemoryDataSourceImpl((ByteBuffer) bb.rewind()), contentSize, boxParser);
            return;
        }
        this.isFullBox = true;
        parseVersionAndFlags((ByteBuffer) bb.rewind());
        initContainer(new MemoryDataSourceImpl(bb), contentSize - 4, boxParser);
    }

    public void getBox(WritableByteChannel writableByteChannel) throws IOException {
        writableByteChannel.write(getHeader());
        if (this.isFullBox) {
            ByteBuffer bb = ByteBuffer.allocate(4);
            writeVersionAndFlags(bb);
            writableByteChannel.write((ByteBuffer) bb.rewind());
        }
        writeContainer(writableByteChannel);
    }

    public long getSize() {
        long s = getContainerSize();
        long t = 0;
        if (this.isFullBox) {
            t = 0 + 4;
        }
        return ((long) ((this.largeBox || s + t >= IjkMediaMeta.AV_CH_WIDE_RIGHT) ? 16 : 8)) + s + t;
    }
}
