package com.googlecode.mp4parser;

import com.coremedia.iso.BoxParser;
import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.FullBox;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.List;
import java.util.logging.Logger;
import p012tv.danmaku.ijk.media.player.IjkMediaMeta;

public abstract class FullContainerBox extends AbstractContainerBox implements FullBox {
    private static Logger LOG = Logger.getLogger(FullContainerBox.class.getName());
    private int flags;
    private int version;

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

    public <T extends Box> List<T> getBoxes(Class<T> clazz) {
        return getBoxes(clazz, false);
    }

    public FullContainerBox(String type) {
        super(type);
    }

    public void parse(DataSource dataSource, ByteBuffer header, long contentSize, BoxParser boxParser) throws IOException {
        ByteBuffer versionAndFlags = ByteBuffer.allocate(4);
        dataSource.read(versionAndFlags);
        parseVersionAndFlags((ByteBuffer) versionAndFlags.rewind());
        super.parse(dataSource, header, contentSize, boxParser);
    }

    public void getBox(WritableByteChannel writableByteChannel) throws IOException {
        super.getBox(writableByteChannel);
    }

    public String toString() {
        return String.valueOf(getClass().getSimpleName()) + "[childBoxes]";
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

    /* access modifiers changed from: protected */
    public ByteBuffer getHeader() {
        ByteBuffer header;
        if (this.largeBox || getSize() >= IjkMediaMeta.AV_CH_WIDE_RIGHT) {
            byte[] bArr = new byte[20];
            bArr[3] = 1;
            bArr[4] = this.type.getBytes()[0];
            bArr[5] = this.type.getBytes()[1];
            bArr[6] = this.type.getBytes()[2];
            bArr[7] = this.type.getBytes()[3];
            header = ByteBuffer.wrap(bArr);
            header.position(8);
            IsoTypeWriter.writeUInt64(header, getSize());
            writeVersionAndFlags(header);
        } else {
            byte[] bArr2 = new byte[12];
            bArr2[4] = this.type.getBytes()[0];
            bArr2[5] = this.type.getBytes()[1];
            bArr2[6] = this.type.getBytes()[2];
            bArr2[7] = this.type.getBytes()[3];
            header = ByteBuffer.wrap(bArr2);
            IsoTypeWriter.writeUInt32(header, getSize());
            header.position(8);
            writeVersionAndFlags(header);
        }
        header.rewind();
        return header;
    }
}
