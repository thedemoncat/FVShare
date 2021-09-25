package com.googlecode.mp4parser.boxes.mp4.samplegrouping;

import java.nio.ByteBuffer;

public class VisualRandomAccessEntry extends GroupEntry {
    public static final String TYPE = "rap ";
    private short numLeadingSamples;
    private boolean numLeadingSamplesKnown;

    public String getType() {
        return TYPE;
    }

    public boolean isNumLeadingSamplesKnown() {
        return this.numLeadingSamplesKnown;
    }

    public void setNumLeadingSamplesKnown(boolean numLeadingSamplesKnown2) {
        this.numLeadingSamplesKnown = numLeadingSamplesKnown2;
    }

    public short getNumLeadingSamples() {
        return this.numLeadingSamples;
    }

    public void setNumLeadingSamples(short numLeadingSamples2) {
        this.numLeadingSamples = numLeadingSamples2;
    }

    public void parse(ByteBuffer byteBuffer) {
        byte b = byteBuffer.get();
        this.numLeadingSamplesKnown = (b & 128) == 128;
        this.numLeadingSamples = (short) (b & Byte.MAX_VALUE);
    }

    public ByteBuffer get() {
        ByteBuffer content = ByteBuffer.allocate(1);
        content.put((byte) ((this.numLeadingSamplesKnown ? (short) 128 : 0) | (this.numLeadingSamples & 127)));
        content.rewind();
        return content;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VisualRandomAccessEntry that = (VisualRandomAccessEntry) o;
        if (this.numLeadingSamples != that.numLeadingSamples) {
            return false;
        }
        if (this.numLeadingSamplesKnown != that.numLeadingSamplesKnown) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return ((this.numLeadingSamplesKnown ? 1 : 0) * 31) + this.numLeadingSamples;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("VisualRandomAccessEntry");
        sb.append("{numLeadingSamplesKnown=").append(this.numLeadingSamplesKnown);
        sb.append(", numLeadingSamples=").append(this.numLeadingSamples);
        sb.append('}');
        return sb.toString();
    }
}
