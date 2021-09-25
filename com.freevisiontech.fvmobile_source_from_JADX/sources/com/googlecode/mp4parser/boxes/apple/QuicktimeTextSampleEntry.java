package com.googlecode.mp4parser.boxes.apple;

import android.support.p001v4.internal.view.SupportMenu;
import com.coremedia.iso.BoxParser;
import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.sampleentry.AbstractSampleEntry;
import com.googlecode.mp4parser.DataSource;
import com.googlecode.mp4parser.util.CastUtils;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.List;
import p012tv.danmaku.ijk.media.player.IjkMediaMeta;

public class QuicktimeTextSampleEntry extends AbstractSampleEntry {
    public static final String TYPE = "text";
    int backgroundB;
    int backgroundG;
    int backgroundR;
    int dataReferenceIndex;
    long defaultTextBox;
    int displayFlags;
    short fontFace;
    String fontName = "";
    short fontNumber;
    int foregroundB = SupportMenu.USER_MASK;
    int foregroundG = SupportMenu.USER_MASK;
    int foregroundR = SupportMenu.USER_MASK;
    long reserved1;
    byte reserved2;
    short reserved3;
    int textJustification;

    public QuicktimeTextSampleEntry() {
        super("text");
    }

    public void parse(DataSource dataSource, ByteBuffer header, long contentSize, BoxParser boxParser) throws IOException {
        ByteBuffer content = ByteBuffer.allocate(CastUtils.l2i(contentSize));
        dataSource.read(content);
        content.position(6);
        this.dataReferenceIndex = IsoTypeReader.readUInt16(content);
        this.displayFlags = content.getInt();
        this.textJustification = content.getInt();
        this.backgroundR = IsoTypeReader.readUInt16(content);
        this.backgroundG = IsoTypeReader.readUInt16(content);
        this.backgroundB = IsoTypeReader.readUInt16(content);
        this.defaultTextBox = IsoTypeReader.readUInt64(content);
        this.reserved1 = IsoTypeReader.readUInt64(content);
        this.fontNumber = content.getShort();
        this.fontFace = content.getShort();
        this.reserved2 = content.get();
        this.reserved3 = content.getShort();
        this.foregroundR = IsoTypeReader.readUInt16(content);
        this.foregroundG = IsoTypeReader.readUInt16(content);
        this.foregroundB = IsoTypeReader.readUInt16(content);
        if (content.remaining() > 0) {
            byte[] myFontName = new byte[IsoTypeReader.readUInt8(content)];
            content.get(myFontName);
            this.fontName = new String(myFontName);
            return;
        }
        this.fontName = null;
    }

    public void setBoxes(List<Box> list) {
        throw new RuntimeException("QuicktimeTextSampleEntries may not have child boxes");
    }

    public void addBox(Box box) {
        throw new RuntimeException("QuicktimeTextSampleEntries may not have child boxes");
    }

    public void getBox(WritableByteChannel writableByteChannel) throws IOException {
        writableByteChannel.write(getHeader());
        ByteBuffer byteBuffer = ByteBuffer.allocate((this.fontName != null ? this.fontName.length() : 0) + 52);
        byteBuffer.position(6);
        IsoTypeWriter.writeUInt16(byteBuffer, this.dataReferenceIndex);
        byteBuffer.putInt(this.displayFlags);
        byteBuffer.putInt(this.textJustification);
        IsoTypeWriter.writeUInt16(byteBuffer, this.backgroundR);
        IsoTypeWriter.writeUInt16(byteBuffer, this.backgroundG);
        IsoTypeWriter.writeUInt16(byteBuffer, this.backgroundB);
        IsoTypeWriter.writeUInt64(byteBuffer, this.defaultTextBox);
        IsoTypeWriter.writeUInt64(byteBuffer, this.reserved1);
        byteBuffer.putShort(this.fontNumber);
        byteBuffer.putShort(this.fontFace);
        byteBuffer.put(this.reserved2);
        byteBuffer.putShort(this.reserved3);
        IsoTypeWriter.writeUInt16(byteBuffer, this.foregroundR);
        IsoTypeWriter.writeUInt16(byteBuffer, this.foregroundG);
        IsoTypeWriter.writeUInt16(byteBuffer, this.foregroundB);
        if (this.fontName != null) {
            IsoTypeWriter.writeUInt8(byteBuffer, this.fontName.length());
            byteBuffer.put(this.fontName.getBytes());
        }
        writableByteChannel.write((ByteBuffer) byteBuffer.rewind());
    }

    public long getSize() {
        long s = 52 + getContainerSize() + ((long) (this.fontName != null ? this.fontName.length() : 0));
        return s + ((long) ((this.largeBox || 8 + s >= IjkMediaMeta.AV_CH_WIDE_RIGHT) ? 16 : 8));
    }

    public int getDisplayFlags() {
        return this.displayFlags;
    }

    public void setDisplayFlags(int displayFlags2) {
        this.displayFlags = displayFlags2;
    }

    public int getTextJustification() {
        return this.textJustification;
    }

    public void setTextJustification(int textJustification2) {
        this.textJustification = textJustification2;
    }

    public int getBackgroundR() {
        return this.backgroundR;
    }

    public void setBackgroundR(int backgroundR2) {
        this.backgroundR = backgroundR2;
    }

    public int getBackgroundG() {
        return this.backgroundG;
    }

    public void setBackgroundG(int backgroundG2) {
        this.backgroundG = backgroundG2;
    }

    public int getBackgroundB() {
        return this.backgroundB;
    }

    public void setBackgroundB(int backgroundB2) {
        this.backgroundB = backgroundB2;
    }

    public long getDefaultTextBox() {
        return this.defaultTextBox;
    }

    public void setDefaultTextBox(long defaultTextBox2) {
        this.defaultTextBox = defaultTextBox2;
    }

    public long getReserved1() {
        return this.reserved1;
    }

    public void setReserved1(long reserved12) {
        this.reserved1 = reserved12;
    }

    public short getFontNumber() {
        return this.fontNumber;
    }

    public void setFontNumber(short fontNumber2) {
        this.fontNumber = fontNumber2;
    }

    public short getFontFace() {
        return this.fontFace;
    }

    public void setFontFace(short fontFace2) {
        this.fontFace = fontFace2;
    }

    public byte getReserved2() {
        return this.reserved2;
    }

    public void setReserved2(byte reserved22) {
        this.reserved2 = reserved22;
    }

    public short getReserved3() {
        return this.reserved3;
    }

    public void setReserved3(short reserved32) {
        this.reserved3 = reserved32;
    }

    public int getForegroundR() {
        return this.foregroundR;
    }

    public void setForegroundR(int foregroundR2) {
        this.foregroundR = foregroundR2;
    }

    public int getForegroundG() {
        return this.foregroundG;
    }

    public void setForegroundG(int foregroundG2) {
        this.foregroundG = foregroundG2;
    }

    public int getForegroundB() {
        return this.foregroundB;
    }

    public void setForegroundB(int foregroundB2) {
        this.foregroundB = foregroundB2;
    }

    public String getFontName() {
        return this.fontName;
    }

    public void setFontName(String fontName2) {
        this.fontName = fontName2;
    }
}
