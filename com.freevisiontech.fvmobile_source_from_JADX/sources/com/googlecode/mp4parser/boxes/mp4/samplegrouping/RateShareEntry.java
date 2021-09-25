package com.googlecode.mp4parser.boxes.mp4.samplegrouping;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.util.CastUtils;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

public class RateShareEntry extends GroupEntry {
    public static final String TYPE = "rash";
    private short discardPriority;
    private List<Entry> entries = new LinkedList();
    private int maximumBitrate;
    private int minimumBitrate;
    private short operationPointCut;
    private short targetRateShare;

    public String getType() {
        return TYPE;
    }

    public void parse(ByteBuffer byteBuffer) {
        this.operationPointCut = byteBuffer.getShort();
        if (this.operationPointCut != 1) {
            int entriesLeft = this.operationPointCut;
            while (true) {
                int entriesLeft2 = entriesLeft;
                entriesLeft = entriesLeft2 - 1;
                if (entriesLeft2 <= 0) {
                    break;
                }
                this.entries.add(new Entry(CastUtils.l2i(IsoTypeReader.readUInt32(byteBuffer)), byteBuffer.getShort()));
            }
        } else {
            this.targetRateShare = byteBuffer.getShort();
        }
        this.maximumBitrate = CastUtils.l2i(IsoTypeReader.readUInt32(byteBuffer));
        this.minimumBitrate = CastUtils.l2i(IsoTypeReader.readUInt32(byteBuffer));
        this.discardPriority = (short) IsoTypeReader.readUInt8(byteBuffer);
    }

    public ByteBuffer get() {
        ByteBuffer buf = ByteBuffer.allocate(this.operationPointCut == 1 ? 13 : (this.operationPointCut * 6) + 11);
        buf.putShort(this.operationPointCut);
        if (this.operationPointCut == 1) {
            buf.putShort(this.targetRateShare);
        } else {
            for (Entry entry : this.entries) {
                buf.putInt(entry.getAvailableBitrate());
                buf.putShort(entry.getTargetRateShare());
            }
        }
        buf.putInt(this.maximumBitrate);
        buf.putInt(this.minimumBitrate);
        IsoTypeWriter.writeUInt8(buf, this.discardPriority);
        buf.rewind();
        return buf;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RateShareEntry that = (RateShareEntry) o;
        if (this.discardPriority != that.discardPriority) {
            return false;
        }
        if (this.maximumBitrate != that.maximumBitrate) {
            return false;
        }
        if (this.minimumBitrate != that.minimumBitrate) {
            return false;
        }
        if (this.operationPointCut != that.operationPointCut) {
            return false;
        }
        if (this.targetRateShare != that.targetRateShare) {
            return false;
        }
        if (this.entries != null) {
            if (this.entries.equals(that.entries)) {
                return true;
            }
        } else if (that.entries == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (((((((((this.operationPointCut * 31) + this.targetRateShare) * 31) + (this.entries != null ? this.entries.hashCode() : 0)) * 31) + this.maximumBitrate) * 31) + this.minimumBitrate) * 31) + this.discardPriority;
    }

    public short getOperationPointCut() {
        return this.operationPointCut;
    }

    public void setOperationPointCut(short operationPointCut2) {
        this.operationPointCut = operationPointCut2;
    }

    public short getTargetRateShare() {
        return this.targetRateShare;
    }

    public void setTargetRateShare(short targetRateShare2) {
        this.targetRateShare = targetRateShare2;
    }

    public List<Entry> getEntries() {
        return this.entries;
    }

    public void setEntries(List<Entry> entries2) {
        this.entries = entries2;
    }

    public int getMaximumBitrate() {
        return this.maximumBitrate;
    }

    public void setMaximumBitrate(int maximumBitrate2) {
        this.maximumBitrate = maximumBitrate2;
    }

    public int getMinimumBitrate() {
        return this.minimumBitrate;
    }

    public void setMinimumBitrate(int minimumBitrate2) {
        this.minimumBitrate = minimumBitrate2;
    }

    public short getDiscardPriority() {
        return this.discardPriority;
    }

    public void setDiscardPriority(short discardPriority2) {
        this.discardPriority = discardPriority2;
    }

    public static class Entry {
        int availableBitrate;
        short targetRateShare;

        public Entry(int availableBitrate2, short targetRateShare2) {
            this.availableBitrate = availableBitrate2;
            this.targetRateShare = targetRateShare2;
        }

        public String toString() {
            return "{availableBitrate=" + this.availableBitrate + ", targetRateShare=" + this.targetRateShare + '}';
        }

        public int getAvailableBitrate() {
            return this.availableBitrate;
        }

        public void setAvailableBitrate(int availableBitrate2) {
            this.availableBitrate = availableBitrate2;
        }

        public short getTargetRateShare() {
            return this.targetRateShare;
        }

        public void setTargetRateShare(short targetRateShare2) {
            this.targetRateShare = targetRateShare2;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Entry entry = (Entry) o;
            if (this.availableBitrate != entry.availableBitrate) {
                return false;
            }
            if (this.targetRateShare != entry.targetRateShare) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return (this.availableBitrate * 31) + this.targetRateShare;
        }
    }
}
