package com.googlecode.mp4parser.authoring.tracks.webvtt.sampleboxes;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.IsoTypeWriter;
import com.mp4parser.streaming.WriteOnlyBox;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

public class VTTCueBox extends WriteOnlyBox {
    CueIDBox cueIDBox;
    CuePayloadBox cuePayloadBox;
    CueSettingsBox cueSettingsBox;
    CueSourceIDBox cueSourceIDBox;
    CueTimeBox cueTimeBox;

    public VTTCueBox() {
        super("vtcc");
    }

    public long getSize() {
        long j;
        long j2;
        long j3;
        long j4;
        long j5 = 0;
        if (this.cueSourceIDBox != null) {
            j = this.cueSourceIDBox.getSize();
        } else {
            j = 0;
        }
        long j6 = 8 + j;
        if (this.cueIDBox != null) {
            j2 = this.cueIDBox.getSize();
        } else {
            j2 = 0;
        }
        long j7 = j6 + j2;
        if (this.cueTimeBox != null) {
            j3 = this.cueTimeBox.getSize();
        } else {
            j3 = 0;
        }
        long j8 = j7 + j3;
        if (this.cueSettingsBox != null) {
            j4 = this.cueSettingsBox.getSize();
        } else {
            j4 = 0;
        }
        long j9 = j4 + j8;
        if (this.cuePayloadBox != null) {
            j5 = this.cuePayloadBox.getSize();
        }
        return j9 + j5;
    }

    public void getBox(WritableByteChannel writableByteChannel) throws IOException {
        ByteBuffer header = ByteBuffer.allocate(8);
        IsoTypeWriter.writeUInt32(header, getSize());
        header.put(IsoFile.fourCCtoBytes(getType()));
        writableByteChannel.write((ByteBuffer) header.rewind());
        if (this.cueSourceIDBox != null) {
            this.cueSourceIDBox.getBox(writableByteChannel);
        }
        if (this.cueIDBox != null) {
            this.cueIDBox.getBox(writableByteChannel);
        }
        if (this.cueTimeBox != null) {
            this.cueTimeBox.getBox(writableByteChannel);
        }
        if (this.cueSettingsBox != null) {
            this.cueSettingsBox.getBox(writableByteChannel);
        }
        if (this.cuePayloadBox != null) {
            this.cuePayloadBox.getBox(writableByteChannel);
        }
    }

    public CueSourceIDBox getCueSourceIDBox() {
        return this.cueSourceIDBox;
    }

    public void setCueSourceIDBox(CueSourceIDBox cueSourceIDBox2) {
        this.cueSourceIDBox = cueSourceIDBox2;
    }

    public CueIDBox getCueIDBox() {
        return this.cueIDBox;
    }

    public void setCueIDBox(CueIDBox cueIDBox2) {
        this.cueIDBox = cueIDBox2;
    }

    public CueTimeBox getCueTimeBox() {
        return this.cueTimeBox;
    }

    public void setCueTimeBox(CueTimeBox cueTimeBox2) {
        this.cueTimeBox = cueTimeBox2;
    }

    public CueSettingsBox getCueSettingsBox() {
        return this.cueSettingsBox;
    }

    public void setCueSettingsBox(CueSettingsBox cueSettingsBox2) {
        this.cueSettingsBox = cueSettingsBox2;
    }

    public CuePayloadBox getCuePayloadBox() {
        return this.cuePayloadBox;
    }

    public void setCuePayloadBox(CuePayloadBox cuePayloadBox2) {
        this.cuePayloadBox = cuePayloadBox2;
    }
}
