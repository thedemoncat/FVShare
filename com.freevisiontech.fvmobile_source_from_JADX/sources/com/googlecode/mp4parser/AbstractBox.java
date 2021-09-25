package com.googlecode.mp4parser;

import com.coremedia.iso.BoxParser;
import com.coremedia.iso.Hex;
import com.coremedia.iso.IsoFile;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.Container;
import com.coremedia.iso.boxes.UserBox;
import com.googlecode.mp4parser.annotations.DoNotParseDetail;
import com.googlecode.mp4parser.util.CastUtils;
import com.googlecode.mp4parser.util.Logger;
import com.googlecode.mp4parser.util.Path;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import p012tv.danmaku.ijk.media.player.IjkMediaMeta;

public abstract class AbstractBox implements Box {
    static final /* synthetic */ boolean $assertionsDisabled = (!AbstractBox.class.desiredAssertionStatus());
    private static Logger LOG = Logger.getLogger(AbstractBox.class);
    private ByteBuffer content;
    DataSource dataSource;
    private ByteBuffer deadBytes = null;
    boolean isParsed;
    long offset;
    private Container parent;
    protected String type;
    private byte[] userType;

    /* access modifiers changed from: protected */
    public abstract void _parseDetails(ByteBuffer byteBuffer);

    /* access modifiers changed from: protected */
    public abstract void getContent(ByteBuffer byteBuffer);

    /* access modifiers changed from: protected */
    public abstract long getContentSize();

    public long getOffset() {
        return this.offset;
    }

    protected AbstractBox(String type2) {
        this.type = type2;
        this.isParsed = true;
    }

    protected AbstractBox(String type2, byte[] userType2) {
        this.type = type2;
        this.userType = userType2;
        this.isParsed = true;
    }

    @DoNotParseDetail
    public void parse(DataSource dataSource2, ByteBuffer header, long contentSize, BoxParser boxParser) throws IOException {
        this.offset = dataSource2.position() - ((long) header.remaining());
        this.dataSource = dataSource2;
        this.content = ByteBuffer.allocate(CastUtils.l2i(contentSize));
        while (this.content.remaining() > 0) {
            dataSource2.read(this.content);
        }
        this.content.position(0);
        this.isParsed = false;
    }

    public void getBox(WritableByteChannel os) throws IOException {
        int i;
        int i2 = 16;
        if (this.isParsed) {
            ByteBuffer bb = ByteBuffer.allocate(CastUtils.l2i(getSize()));
            getHeader(bb);
            getContent(bb);
            if (this.deadBytes != null) {
                this.deadBytes.rewind();
                while (this.deadBytes.remaining() > 0) {
                    bb.put(this.deadBytes);
                }
            }
            os.write((ByteBuffer) bb.rewind());
            return;
        }
        if (isSmallBox()) {
            i = 8;
        } else {
            i = 16;
        }
        if (!UserBox.TYPE.equals(getType())) {
            i2 = 0;
        }
        ByteBuffer header = ByteBuffer.allocate(i + i2);
        getHeader(header);
        os.write((ByteBuffer) header.rewind());
        os.write((ByteBuffer) this.content.position(0));
    }

    public final synchronized void parseDetails() {
        LOG.logDebug("parsing details of " + getType());
        if (this.content != null) {
            ByteBuffer content2 = this.content;
            this.isParsed = true;
            content2.rewind();
            _parseDetails(content2);
            if (content2.remaining() > 0) {
                this.deadBytes = content2.slice();
            }
            this.content = null;
            if (!$assertionsDisabled && !verify(content2)) {
                throw new AssertionError();
            }
        }
    }

    public long getSize() {
        long size;
        int i;
        int i2;
        int i3 = 0;
        if (this.isParsed) {
            size = getContentSize();
        } else {
            size = (long) (this.content != null ? this.content.limit() : 0);
        }
        if (size >= 4294967288L) {
            i = 8;
        } else {
            i = 0;
        }
        int i4 = i + 8;
        if (UserBox.TYPE.equals(getType())) {
            i2 = 16;
        } else {
            i2 = 0;
        }
        long size2 = size + ((long) (i2 + i4));
        if (this.deadBytes != null) {
            i3 = this.deadBytes.limit();
        }
        return size2 + ((long) i3);
    }

    @DoNotParseDetail
    public String getType() {
        return this.type;
    }

    @DoNotParseDetail
    public byte[] getUserType() {
        return this.userType;
    }

    @DoNotParseDetail
    public Container getParent() {
        return this.parent;
    }

    @DoNotParseDetail
    public void setParent(Container parent2) {
        this.parent = parent2;
    }

    public boolean isParsed() {
        return this.isParsed;
    }

    private boolean verify(ByteBuffer content2) {
        ByteBuffer bb = ByteBuffer.allocate(CastUtils.l2i(((long) (this.deadBytes != null ? this.deadBytes.limit() : 0)) + getContentSize()));
        getContent(bb);
        if (this.deadBytes != null) {
            this.deadBytes.rewind();
            while (this.deadBytes.remaining() > 0) {
                bb.put(this.deadBytes);
            }
        }
        content2.rewind();
        bb.rewind();
        if (content2.remaining() != bb.remaining()) {
            System.err.print(String.valueOf(getType()) + ": remaining differs " + content2.remaining() + " vs. " + bb.remaining());
            LOG.logError(String.valueOf(getType()) + ": remaining differs " + content2.remaining() + " vs. " + bb.remaining());
            return false;
        }
        int p = content2.position();
        int i = content2.limit() - 1;
        int j = bb.limit() - 1;
        while (i >= p) {
            byte v1 = content2.get(i);
            byte v2 = bb.get(j);
            if (v1 != v2) {
                LOG.logError(String.format("%s: buffers differ at %d: %2X/%2X", new Object[]{getType(), Integer.valueOf(i), Byte.valueOf(v1), Byte.valueOf(v2)}));
                byte[] b1 = new byte[content2.remaining()];
                byte[] b2 = new byte[bb.remaining()];
                content2.get(b1);
                bb.get(b2);
                System.err.println("original      : " + Hex.encodeHex(b1, 4));
                System.err.println("reconstructed : " + Hex.encodeHex(b2, 4));
                return false;
            }
            i--;
            j--;
        }
        return true;
    }

    private boolean isSmallBox() {
        int i;
        int baseSize = 8;
        if (UserBox.TYPE.equals(getType())) {
            baseSize = 8 + 16;
        }
        if (this.isParsed) {
            long contentSize = getContentSize();
            if (this.deadBytes != null) {
                i = this.deadBytes.limit();
            } else {
                i = 0;
            }
            if (contentSize + ((long) i) + ((long) baseSize) < IjkMediaMeta.AV_CH_WIDE_RIGHT) {
                return true;
            }
            return false;
        } else if (((long) (this.content.limit() + baseSize)) >= IjkMediaMeta.AV_CH_WIDE_RIGHT) {
            return false;
        } else {
            return true;
        }
    }

    private void getHeader(ByteBuffer byteBuffer) {
        if (isSmallBox()) {
            IsoTypeWriter.writeUInt32(byteBuffer, getSize());
            byteBuffer.put(IsoFile.fourCCtoBytes(getType()));
        } else {
            IsoTypeWriter.writeUInt32(byteBuffer, 1);
            byteBuffer.put(IsoFile.fourCCtoBytes(getType()));
            IsoTypeWriter.writeUInt64(byteBuffer, getSize());
        }
        if (UserBox.TYPE.equals(getType())) {
            byteBuffer.put(getUserType());
        }
    }

    @DoNotParseDetail
    public String getPath() {
        return Path.createPath(this);
    }
}
