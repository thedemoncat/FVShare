package com.coremedia.iso.boxes.sampleentry;

import com.coremedia.iso.BoxParser;
import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.DataSource;
import com.googlecode.mp4parser.util.CastUtils;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import p012tv.danmaku.ijk.media.player.IjkMediaMeta;

public final class AudioSampleEntry extends AbstractSampleEntry {
    static final /* synthetic */ boolean $assertionsDisabled;
    public static final String TYPE1 = "samr";
    public static final String TYPE10 = "mlpa";
    public static final String TYPE11 = "dtsl";
    public static final String TYPE12 = "dtsh";
    public static final String TYPE13 = "dtse";
    public static final String TYPE2 = "sawb";
    public static final String TYPE3 = "mp4a";
    public static final String TYPE4 = "drms";
    public static final String TYPE5 = "alac";
    public static final String TYPE7 = "owma";
    public static final String TYPE8 = "ac-3";
    public static final String TYPE9 = "ec-3";
    public static final String TYPE_ENCRYPTED = "enca";
    private long bytesPerFrame;
    private long bytesPerPacket;
    private long bytesPerSample;
    private int channelCount;
    private int compressionId;
    private int packetSize;
    private int reserved1;
    private long reserved2;
    private long sampleRate;
    private int sampleSize;
    private long samplesPerPacket;
    private int soundVersion;
    private byte[] soundVersion2Data;

    static {
        boolean z;
        if (!AudioSampleEntry.class.desiredAssertionStatus()) {
            z = true;
        } else {
            z = false;
        }
        $assertionsDisabled = z;
    }

    public AudioSampleEntry(String type) {
        super(type);
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getChannelCount() {
        return this.channelCount;
    }

    public int getSampleSize() {
        return this.sampleSize;
    }

    public long getSampleRate() {
        return this.sampleRate;
    }

    public int getSoundVersion() {
        return this.soundVersion;
    }

    public int getCompressionId() {
        return this.compressionId;
    }

    public int getPacketSize() {
        return this.packetSize;
    }

    public long getSamplesPerPacket() {
        return this.samplesPerPacket;
    }

    public long getBytesPerPacket() {
        return this.bytesPerPacket;
    }

    public long getBytesPerFrame() {
        return this.bytesPerFrame;
    }

    public long getBytesPerSample() {
        return this.bytesPerSample;
    }

    public byte[] getSoundVersion2Data() {
        return this.soundVersion2Data;
    }

    public int getReserved1() {
        return this.reserved1;
    }

    public long getReserved2() {
        return this.reserved2;
    }

    public void setChannelCount(int channelCount2) {
        this.channelCount = channelCount2;
    }

    public void setSampleSize(int sampleSize2) {
        this.sampleSize = sampleSize2;
    }

    public void setSampleRate(long sampleRate2) {
        this.sampleRate = sampleRate2;
    }

    public void setSoundVersion(int soundVersion2) {
        this.soundVersion = soundVersion2;
    }

    public void setCompressionId(int compressionId2) {
        this.compressionId = compressionId2;
    }

    public void setPacketSize(int packetSize2) {
        this.packetSize = packetSize2;
    }

    public void setSamplesPerPacket(long samplesPerPacket2) {
        this.samplesPerPacket = samplesPerPacket2;
    }

    public void setBytesPerPacket(long bytesPerPacket2) {
        this.bytesPerPacket = bytesPerPacket2;
    }

    public void setBytesPerFrame(long bytesPerFrame2) {
        this.bytesPerFrame = bytesPerFrame2;
    }

    public void setBytesPerSample(long bytesPerSample2) {
        this.bytesPerSample = bytesPerSample2;
    }

    public void setReserved1(int reserved12) {
        this.reserved1 = reserved12;
    }

    public void setReserved2(long reserved22) {
        this.reserved2 = reserved22;
    }

    public void setSoundVersion2Data(byte[] soundVersion2Data2) {
        this.soundVersion2Data = soundVersion2Data2;
    }

    public void parse(DataSource dataSource, ByteBuffer header, long contentSize, BoxParser boxParser) throws IOException {
        ByteBuffer content = ByteBuffer.allocate(28);
        dataSource.read(content);
        content.position(6);
        this.dataReferenceIndex = IsoTypeReader.readUInt16(content);
        this.soundVersion = IsoTypeReader.readUInt16(content);
        this.reserved1 = IsoTypeReader.readUInt16(content);
        this.reserved2 = IsoTypeReader.readUInt32(content);
        this.channelCount = IsoTypeReader.readUInt16(content);
        this.sampleSize = IsoTypeReader.readUInt16(content);
        this.compressionId = IsoTypeReader.readUInt16(content);
        this.packetSize = IsoTypeReader.readUInt16(content);
        this.sampleRate = IsoTypeReader.readUInt32(content);
        if (!this.type.equals(TYPE10)) {
            this.sampleRate >>>= 16;
        }
        if (this.soundVersion == 1) {
            ByteBuffer appleStuff = ByteBuffer.allocate(16);
            dataSource.read(appleStuff);
            appleStuff.rewind();
            this.samplesPerPacket = IsoTypeReader.readUInt32(appleStuff);
            this.bytesPerPacket = IsoTypeReader.readUInt32(appleStuff);
            this.bytesPerFrame = IsoTypeReader.readUInt32(appleStuff);
            this.bytesPerSample = IsoTypeReader.readUInt32(appleStuff);
        }
        if (this.soundVersion == 2) {
            ByteBuffer appleStuff2 = ByteBuffer.allocate(36);
            dataSource.read(appleStuff2);
            appleStuff2.rewind();
            this.samplesPerPacket = IsoTypeReader.readUInt32(appleStuff2);
            this.bytesPerPacket = IsoTypeReader.readUInt32(appleStuff2);
            this.bytesPerFrame = IsoTypeReader.readUInt32(appleStuff2);
            this.bytesPerSample = IsoTypeReader.readUInt32(appleStuff2);
            this.soundVersion2Data = new byte[20];
            appleStuff2.get(this.soundVersion2Data);
        }
        if (TYPE7.equals(this.type)) {
            System.err.println(TYPE7);
            final long remaining = ((contentSize - 28) - ((long) (this.soundVersion == 1 ? 16 : 0))) - ((long) (this.soundVersion == 2 ? 36 : 0));
            final ByteBuffer owmaSpecifics = ByteBuffer.allocate(CastUtils.l2i(remaining));
            dataSource.read(owmaSpecifics);
            addBox(new Box() {
                public Container getParent() {
                    return AudioSampleEntry.this;
                }

                public void setParent(Container parent) {
                    if (!AudioSampleEntry.$assertionsDisabled && parent != AudioSampleEntry.this) {
                        throw new AssertionError("you cannot diswown this special box");
                    }
                }

                public long getSize() {
                    return remaining;
                }

                public long getOffset() {
                    return 0;
                }

                public String getType() {
                    return "----";
                }

                public void getBox(WritableByteChannel writableByteChannel) throws IOException {
                    owmaSpecifics.rewind();
                    writableByteChannel.write(owmaSpecifics);
                }

                public void parse(DataSource dataSource, ByteBuffer header, long contentSize, BoxParser boxParser) throws IOException {
                    throw new RuntimeException("NotImplemented");
                }
            });
            return;
        }
        initContainer(dataSource, ((contentSize - 28) - ((long) (this.soundVersion == 1 ? 16 : 0))) - ((long) (this.soundVersion == 2 ? 36 : 0)), boxParser);
    }

    public void getBox(WritableByteChannel writableByteChannel) throws IOException {
        int i;
        int i2 = 0;
        writableByteChannel.write(getHeader());
        if (this.soundVersion == 1) {
            i = 16;
        } else {
            i = 0;
        }
        int i3 = i + 28;
        if (this.soundVersion == 2) {
            i2 = 36;
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(i3 + i2);
        byteBuffer.position(6);
        IsoTypeWriter.writeUInt16(byteBuffer, this.dataReferenceIndex);
        IsoTypeWriter.writeUInt16(byteBuffer, this.soundVersion);
        IsoTypeWriter.writeUInt16(byteBuffer, this.reserved1);
        IsoTypeWriter.writeUInt32(byteBuffer, this.reserved2);
        IsoTypeWriter.writeUInt16(byteBuffer, this.channelCount);
        IsoTypeWriter.writeUInt16(byteBuffer, this.sampleSize);
        IsoTypeWriter.writeUInt16(byteBuffer, this.compressionId);
        IsoTypeWriter.writeUInt16(byteBuffer, this.packetSize);
        if (this.type.equals(TYPE10)) {
            IsoTypeWriter.writeUInt32(byteBuffer, getSampleRate());
        } else {
            IsoTypeWriter.writeUInt32(byteBuffer, getSampleRate() << 16);
        }
        if (this.soundVersion == 1) {
            IsoTypeWriter.writeUInt32(byteBuffer, this.samplesPerPacket);
            IsoTypeWriter.writeUInt32(byteBuffer, this.bytesPerPacket);
            IsoTypeWriter.writeUInt32(byteBuffer, this.bytesPerFrame);
            IsoTypeWriter.writeUInt32(byteBuffer, this.bytesPerSample);
        }
        if (this.soundVersion == 2) {
            IsoTypeWriter.writeUInt32(byteBuffer, this.samplesPerPacket);
            IsoTypeWriter.writeUInt32(byteBuffer, this.bytesPerPacket);
            IsoTypeWriter.writeUInt32(byteBuffer, this.bytesPerFrame);
            IsoTypeWriter.writeUInt32(byteBuffer, this.bytesPerSample);
            byteBuffer.put(this.soundVersion2Data);
        }
        writableByteChannel.write((ByteBuffer) byteBuffer.rewind());
        writeContainer(writableByteChannel);
    }

    public long getSize() {
        int i;
        int i2 = 16;
        int i3 = 0;
        if (this.soundVersion == 1) {
            i = 16;
        } else {
            i = 0;
        }
        int i4 = i + 28;
        if (this.soundVersion == 2) {
            i3 = 36;
        }
        long s = ((long) (i4 + i3)) + getContainerSize();
        if (!this.largeBox && 8 + s < IjkMediaMeta.AV_CH_WIDE_RIGHT) {
            i2 = 8;
        }
        return s + ((long) i2);
    }

    public String toString() {
        return "AudioSampleEntry{bytesPerSample=" + this.bytesPerSample + ", bytesPerFrame=" + this.bytesPerFrame + ", bytesPerPacket=" + this.bytesPerPacket + ", samplesPerPacket=" + this.samplesPerPacket + ", packetSize=" + this.packetSize + ", compressionId=" + this.compressionId + ", soundVersion=" + this.soundVersion + ", sampleRate=" + this.sampleRate + ", sampleSize=" + this.sampleSize + ", channelCount=" + this.channelCount + ", boxes=" + getBoxes() + '}';
    }
}
