package com.coremedia.iso.boxes.sampleentry;

import android.support.p001v4.media.session.PlaybackStateCompat;
import com.coremedia.iso.BoxParser;
import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.DataSource;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.Arrays;
import p012tv.danmaku.ijk.media.player.IjkMediaMeta;

public class TextSampleEntry extends AbstractSampleEntry {
    public static final String TYPE1 = "tx3g";
    public static final String TYPE_ENCRYPTED = "enct";
    private int[] backgroundColorRgba = new int[4];
    private BoxRecord boxRecord = new BoxRecord();
    private long displayFlags;
    private int horizontalJustification;
    private StyleRecord styleRecord = new StyleRecord();
    private int verticalJustification;

    public TextSampleEntry() {
        super(TYPE1);
    }

    public TextSampleEntry(String type) {
        super(type);
    }

    public void setType(String type) {
        this.type = type;
    }

    public void parse(DataSource dataSource, ByteBuffer header, long contentSize, BoxParser boxParser) throws IOException {
        ByteBuffer content = ByteBuffer.allocate(38);
        dataSource.read(content);
        content.position(6);
        this.dataReferenceIndex = IsoTypeReader.readUInt16(content);
        this.displayFlags = IsoTypeReader.readUInt32(content);
        this.horizontalJustification = IsoTypeReader.readUInt8(content);
        this.verticalJustification = IsoTypeReader.readUInt8(content);
        this.backgroundColorRgba = new int[4];
        this.backgroundColorRgba[0] = IsoTypeReader.readUInt8(content);
        this.backgroundColorRgba[1] = IsoTypeReader.readUInt8(content);
        this.backgroundColorRgba[2] = IsoTypeReader.readUInt8(content);
        this.backgroundColorRgba[3] = IsoTypeReader.readUInt8(content);
        this.boxRecord = new BoxRecord();
        this.boxRecord.parse(content);
        this.styleRecord = new StyleRecord();
        this.styleRecord.parse(content);
        initContainer(dataSource, contentSize - 38, boxParser);
    }

    public void getBox(WritableByteChannel writableByteChannel) throws IOException {
        writableByteChannel.write(getHeader());
        ByteBuffer byteBuffer = ByteBuffer.allocate(38);
        byteBuffer.position(6);
        IsoTypeWriter.writeUInt16(byteBuffer, this.dataReferenceIndex);
        IsoTypeWriter.writeUInt32(byteBuffer, this.displayFlags);
        IsoTypeWriter.writeUInt8(byteBuffer, this.horizontalJustification);
        IsoTypeWriter.writeUInt8(byteBuffer, this.verticalJustification);
        IsoTypeWriter.writeUInt8(byteBuffer, this.backgroundColorRgba[0]);
        IsoTypeWriter.writeUInt8(byteBuffer, this.backgroundColorRgba[1]);
        IsoTypeWriter.writeUInt8(byteBuffer, this.backgroundColorRgba[2]);
        IsoTypeWriter.writeUInt8(byteBuffer, this.backgroundColorRgba[3]);
        this.boxRecord.getContent(byteBuffer);
        this.styleRecord.getContent(byteBuffer);
        writableByteChannel.write((ByteBuffer) byteBuffer.rewind());
        writeContainer(writableByteChannel);
    }

    public String toString() {
        return "TextSampleEntry";
    }

    public BoxRecord getBoxRecord() {
        return this.boxRecord;
    }

    public void setBoxRecord(BoxRecord boxRecord2) {
        this.boxRecord = boxRecord2;
    }

    public StyleRecord getStyleRecord() {
        return this.styleRecord;
    }

    public void setStyleRecord(StyleRecord styleRecord2) {
        this.styleRecord = styleRecord2;
    }

    public boolean isScrollIn() {
        return (this.displayFlags & 32) == 32;
    }

    public void setScrollIn(boolean scrollIn) {
        if (scrollIn) {
            this.displayFlags |= 32;
        } else {
            this.displayFlags &= -33;
        }
    }

    public boolean isScrollOut() {
        return (this.displayFlags & 64) == 64;
    }

    public void setScrollOut(boolean scrollOutIn) {
        if (scrollOutIn) {
            this.displayFlags |= 64;
        } else {
            this.displayFlags &= -65;
        }
    }

    public boolean isScrollDirection() {
        return (this.displayFlags & 384) == 384;
    }

    public void setScrollDirection(boolean scrollOutIn) {
        if (scrollOutIn) {
            this.displayFlags |= 384;
        } else {
            this.displayFlags &= -385;
        }
    }

    public boolean isContinuousKaraoke() {
        return (this.displayFlags & 2048) == 2048;
    }

    public void setContinuousKaraoke(boolean continuousKaraoke) {
        if (continuousKaraoke) {
            this.displayFlags |= 2048;
        } else {
            this.displayFlags &= -2049;
        }
    }

    public boolean isWriteTextVertically() {
        return (this.displayFlags & 131072) == 131072;
    }

    public void setWriteTextVertically(boolean writeTextVertically) {
        if (writeTextVertically) {
            this.displayFlags |= 131072;
        } else {
            this.displayFlags &= -131073;
        }
    }

    public boolean isFillTextRegion() {
        return (this.displayFlags & PlaybackStateCompat.ACTION_SET_REPEAT_MODE) == PlaybackStateCompat.ACTION_SET_REPEAT_MODE;
    }

    public void setFillTextRegion(boolean fillTextRegion) {
        if (fillTextRegion) {
            this.displayFlags |= PlaybackStateCompat.ACTION_SET_REPEAT_MODE;
        } else {
            this.displayFlags &= -262145;
        }
    }

    public int getHorizontalJustification() {
        return this.horizontalJustification;
    }

    public void setHorizontalJustification(int horizontalJustification2) {
        this.horizontalJustification = horizontalJustification2;
    }

    public int getVerticalJustification() {
        return this.verticalJustification;
    }

    public void setVerticalJustification(int verticalJustification2) {
        this.verticalJustification = verticalJustification2;
    }

    public int[] getBackgroundColorRgba() {
        return this.backgroundColorRgba;
    }

    public void setBackgroundColorRgba(int[] backgroundColorRgba2) {
        this.backgroundColorRgba = backgroundColorRgba2;
    }

    public static class BoxRecord {
        int bottom;
        int left;
        int right;
        int top;

        public BoxRecord() {
        }

        public BoxRecord(int top2, int left2, int bottom2, int right2) {
            this.top = top2;
            this.left = left2;
            this.bottom = bottom2;
            this.right = right2;
        }

        public void parse(ByteBuffer in) {
            this.top = IsoTypeReader.readUInt16(in);
            this.left = IsoTypeReader.readUInt16(in);
            this.bottom = IsoTypeReader.readUInt16(in);
            this.right = IsoTypeReader.readUInt16(in);
        }

        public void getContent(ByteBuffer bb) {
            IsoTypeWriter.writeUInt16(bb, this.top);
            IsoTypeWriter.writeUInt16(bb, this.left);
            IsoTypeWriter.writeUInt16(bb, this.bottom);
            IsoTypeWriter.writeUInt16(bb, this.right);
        }

        public int getSize() {
            return 8;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            BoxRecord boxRecord = (BoxRecord) o;
            if (this.bottom != boxRecord.bottom) {
                return false;
            }
            if (this.left != boxRecord.left) {
                return false;
            }
            if (this.right != boxRecord.right) {
                return false;
            }
            if (this.top != boxRecord.top) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return (((((this.top * 31) + this.left) * 31) + this.bottom) * 31) + this.right;
        }
    }

    public static class StyleRecord {
        int endChar;
        int faceStyleFlags;
        int fontId;
        int fontSize;
        int startChar;
        int[] textColor = {255, 255, 255, 255};

        public StyleRecord() {
        }

        public StyleRecord(int startChar2, int endChar2, int fontId2, int faceStyleFlags2, int fontSize2, int[] textColor2) {
            this.startChar = startChar2;
            this.endChar = endChar2;
            this.fontId = fontId2;
            this.faceStyleFlags = faceStyleFlags2;
            this.fontSize = fontSize2;
            this.textColor = textColor2;
        }

        public void parse(ByteBuffer in) {
            this.startChar = IsoTypeReader.readUInt16(in);
            this.endChar = IsoTypeReader.readUInt16(in);
            this.fontId = IsoTypeReader.readUInt16(in);
            this.faceStyleFlags = IsoTypeReader.readUInt8(in);
            this.fontSize = IsoTypeReader.readUInt8(in);
            this.textColor = new int[4];
            this.textColor[0] = IsoTypeReader.readUInt8(in);
            this.textColor[1] = IsoTypeReader.readUInt8(in);
            this.textColor[2] = IsoTypeReader.readUInt8(in);
            this.textColor[3] = IsoTypeReader.readUInt8(in);
        }

        public void getContent(ByteBuffer bb) {
            IsoTypeWriter.writeUInt16(bb, this.startChar);
            IsoTypeWriter.writeUInt16(bb, this.endChar);
            IsoTypeWriter.writeUInt16(bb, this.fontId);
            IsoTypeWriter.writeUInt8(bb, this.faceStyleFlags);
            IsoTypeWriter.writeUInt8(bb, this.fontSize);
            IsoTypeWriter.writeUInt8(bb, this.textColor[0]);
            IsoTypeWriter.writeUInt8(bb, this.textColor[1]);
            IsoTypeWriter.writeUInt8(bb, this.textColor[2]);
            IsoTypeWriter.writeUInt8(bb, this.textColor[3]);
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            StyleRecord that = (StyleRecord) o;
            if (this.endChar != that.endChar) {
                return false;
            }
            if (this.faceStyleFlags != that.faceStyleFlags) {
                return false;
            }
            if (this.fontId != that.fontId) {
                return false;
            }
            if (this.fontSize != that.fontSize) {
                return false;
            }
            if (this.startChar != that.startChar) {
                return false;
            }
            if (!Arrays.equals(this.textColor, that.textColor)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return (((((((((this.startChar * 31) + this.endChar) * 31) + this.fontId) * 31) + this.faceStyleFlags) * 31) + this.fontSize) * 31) + (this.textColor != null ? Arrays.hashCode(this.textColor) : 0);
        }

        public int getSize() {
            return 12;
        }
    }

    public long getSize() {
        long s = getContainerSize();
        return ((long) ((this.largeBox || s + 38 >= IjkMediaMeta.AV_CH_WIDE_RIGHT) ? 16 : 8)) + s + 38;
    }
}
