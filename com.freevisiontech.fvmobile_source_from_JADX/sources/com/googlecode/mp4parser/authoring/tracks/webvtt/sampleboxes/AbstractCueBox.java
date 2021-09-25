package com.googlecode.mp4parser.authoring.tracks.webvtt.sampleboxes;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.Utf8;
import com.googlecode.mp4parser.util.CastUtils;
import com.mp4parser.streaming.WriteOnlyBox;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

public abstract class AbstractCueBox extends WriteOnlyBox {
    String content = "";

    public AbstractCueBox(String type) {
        super(type);
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content2) {
        this.content = content2;
    }

    public long getSize() {
        return (long) (Utf8.utf8StringLengthInBytes(this.content) + 8);
    }

    public void getBox(WritableByteChannel writableByteChannel) throws IOException {
        ByteBuffer header = ByteBuffer.allocate(CastUtils.l2i(getSize()));
        IsoTypeWriter.writeUInt32(header, getSize());
        header.put(IsoFile.fourCCtoBytes(getType()));
        header.put(Utf8.convert(this.content));
        writableByteChannel.write((ByteBuffer) header.rewind());
    }
}
