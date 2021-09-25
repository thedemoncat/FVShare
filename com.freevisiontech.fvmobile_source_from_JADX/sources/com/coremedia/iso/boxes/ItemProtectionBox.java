package com.coremedia.iso.boxes;

import com.coremedia.iso.BoxParser;
import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.AbstractContainerBox;
import com.googlecode.mp4parser.DataSource;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import p012tv.danmaku.ijk.media.player.IjkMediaMeta;

public class ItemProtectionBox extends AbstractContainerBox implements FullBox {
    public static final String TYPE = "ipro";
    private int flags;
    private int version;

    public ItemProtectionBox() {
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

    public SchemeInformationBox getItemProtectionScheme() {
        if (!getBoxes(SchemeInformationBox.class).isEmpty()) {
            return getBoxes(SchemeInformationBox.class).get(0);
        }
        return null;
    }

    public void parse(DataSource dataSource, ByteBuffer header, long contentSize, BoxParser boxParser) throws IOException {
        ByteBuffer versionFlagNumOfChildBoxes = ByteBuffer.allocate(6);
        dataSource.read(versionFlagNumOfChildBoxes);
        versionFlagNumOfChildBoxes.rewind();
        this.version = IsoTypeReader.readUInt8(versionFlagNumOfChildBoxes);
        this.flags = IsoTypeReader.readUInt24(versionFlagNumOfChildBoxes);
        initContainer(dataSource, contentSize - 6, boxParser);
    }

    public void getBox(WritableByteChannel writableByteChannel) throws IOException {
        writableByteChannel.write(getHeader());
        ByteBuffer versionFlagNumOfChildBoxes = ByteBuffer.allocate(6);
        IsoTypeWriter.writeUInt8(versionFlagNumOfChildBoxes, this.version);
        IsoTypeWriter.writeUInt24(versionFlagNumOfChildBoxes, this.flags);
        IsoTypeWriter.writeUInt16(versionFlagNumOfChildBoxes, getBoxes().size());
        writableByteChannel.write((ByteBuffer) versionFlagNumOfChildBoxes.rewind());
        writeContainer(writableByteChannel);
    }

    public long getSize() {
        long s = getContainerSize();
        return ((long) ((this.largeBox || s + 6 >= IjkMediaMeta.AV_CH_WIDE_RIGHT) ? 16 : 8)) + s + 6;
    }
}
