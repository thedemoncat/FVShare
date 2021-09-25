package com.mp4parser.iso14496.part15;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.googlecode.mp4parser.AbstractBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import java.nio.ByteBuffer;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.internal.Conversions;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class PriotityRangeBox extends AbstractBox {
    public static final String TYPE = "svpr";
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    private static final JoinPoint.StaticPart ajc$tjp_3 = null;
    private static final JoinPoint.StaticPart ajc$tjp_4 = null;
    private static final JoinPoint.StaticPart ajc$tjp_5 = null;
    private static final JoinPoint.StaticPart ajc$tjp_6 = null;
    private static final JoinPoint.StaticPart ajc$tjp_7 = null;
    int max_priorityId;
    int min_priorityId;
    int reserved1 = 0;
    int reserved2 = 0;

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("PriotityRangeBox.java", PriotityRangeBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getReserved1", "com.mp4parser.iso14496.part15.PriotityRangeBox", "", "", "", "int"), 45);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setReserved1", "com.mp4parser.iso14496.part15.PriotityRangeBox", "int", "reserved1", "", "void"), 49);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getMin_priorityId", "com.mp4parser.iso14496.part15.PriotityRangeBox", "", "", "", "int"), 53);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setMin_priorityId", "com.mp4parser.iso14496.part15.PriotityRangeBox", "int", "min_priorityId", "", "void"), 57);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getReserved2", "com.mp4parser.iso14496.part15.PriotityRangeBox", "", "", "", "int"), 61);
        ajc$tjp_5 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setReserved2", "com.mp4parser.iso14496.part15.PriotityRangeBox", "int", "reserved2", "", "void"), 65);
        ajc$tjp_6 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getMax_priorityId", "com.mp4parser.iso14496.part15.PriotityRangeBox", "", "", "", "int"), 69);
        ajc$tjp_7 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setMax_priorityId", "com.mp4parser.iso14496.part15.PriotityRangeBox", "int", "max_priorityId", "", "void"), 73);
    }

    public PriotityRangeBox() {
        super(TYPE);
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return 2;
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        IsoTypeWriter.writeUInt8(byteBuffer, (this.reserved1 << 6) + this.min_priorityId);
        IsoTypeWriter.writeUInt8(byteBuffer, (this.reserved2 << 6) + this.max_priorityId);
    }

    /* access modifiers changed from: protected */
    public void _parseDetails(ByteBuffer content) {
        this.min_priorityId = IsoTypeReader.readUInt8(content);
        this.reserved1 = (this.min_priorityId & 192) >> 6;
        this.min_priorityId &= 63;
        this.max_priorityId = IsoTypeReader.readUInt8(content);
        this.reserved2 = (this.max_priorityId & 192) >> 6;
        this.max_priorityId &= 63;
    }

    public int getReserved1() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.reserved1;
    }

    public void setReserved1(int reserved12) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, Conversions.intObject(reserved12)));
        this.reserved1 = reserved12;
    }

    public int getMin_priorityId() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return this.min_priorityId;
    }

    public void setMin_priorityId(int min_priorityId2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, (Object) this, (Object) this, Conversions.intObject(min_priorityId2)));
        this.min_priorityId = min_priorityId2;
    }

    public int getReserved2() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, this, this));
        return this.reserved2;
    }

    public void setReserved2(int reserved22) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_5, (Object) this, (Object) this, Conversions.intObject(reserved22)));
        this.reserved2 = reserved22;
    }

    public int getMax_priorityId() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_6, this, this));
        return this.max_priorityId;
    }

    public void setMax_priorityId(int max_priorityId2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_7, (Object) this, (Object) this, Conversions.intObject(max_priorityId2)));
        this.max_priorityId = max_priorityId2;
    }
}
