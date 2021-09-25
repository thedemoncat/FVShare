package com.coremedia.iso.boxes;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class ProgressiveDownloadInformationBox extends AbstractFullBox {
    public static final String TYPE = "pdin";
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    List<Entry> entries = Collections.emptyList();

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("ProgressiveDownloadInformationBox.java", ProgressiveDownloadInformationBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getEntries", "com.coremedia.iso.boxes.ProgressiveDownloadInformationBox", "", "", "", "java.util.List"), 38);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setEntries", "com.coremedia.iso.boxes.ProgressiveDownloadInformationBox", "java.util.List", "entries", "", "void"), 42);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "toString", "com.coremedia.iso.boxes.ProgressiveDownloadInformationBox", "", "", "", "java.lang.String"), 112);
    }

    public ProgressiveDownloadInformationBox() {
        super(TYPE);
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return (long) ((this.entries.size() * 8) + 4);
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        for (Entry entry : this.entries) {
            IsoTypeWriter.writeUInt32(byteBuffer, entry.getRate());
            IsoTypeWriter.writeUInt32(byteBuffer, entry.getInitialDelay());
        }
    }

    public List<Entry> getEntries() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.entries;
    }

    public void setEntries(List<Entry> entries2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, (Object) entries2));
        this.entries = entries2;
    }

    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        this.entries = new LinkedList();
        while (content.remaining() >= 8) {
            this.entries.add(new Entry(IsoTypeReader.readUInt32(content), IsoTypeReader.readUInt32(content)));
        }
    }

    public static class Entry {
        long initialDelay;
        long rate;

        public Entry(long rate2, long initialDelay2) {
            this.rate = rate2;
            this.initialDelay = initialDelay2;
        }

        public long getRate() {
            return this.rate;
        }

        public void setRate(long rate2) {
            this.rate = rate2;
        }

        public long getInitialDelay() {
            return this.initialDelay;
        }

        public void setInitialDelay(long initialDelay2) {
            this.initialDelay = initialDelay2;
        }

        public String toString() {
            return "Entry{rate=" + this.rate + ", initialDelay=" + this.initialDelay + '}';
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Entry entry = (Entry) o;
            if (this.initialDelay != entry.initialDelay) {
                return false;
            }
            if (this.rate != entry.rate) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return (((int) (this.rate ^ (this.rate >>> 32))) * 31) + ((int) (this.initialDelay ^ (this.initialDelay >>> 32)));
        }
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return "ProgressiveDownloadInfoBox{entries=" + this.entries + '}';
    }
}
