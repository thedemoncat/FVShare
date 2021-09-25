package com.googlecode.mp4parser.boxes.apple;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import java.nio.ByteBuffer;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.internal.Conversions;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class BaseMediaInfoAtom extends AbstractFullBox {
    public static final String TYPE = "gmin";
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_10 = null;
    private static final JoinPoint.StaticPart ajc$tjp_11 = null;
    private static final JoinPoint.StaticPart ajc$tjp_12 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    private static final JoinPoint.StaticPart ajc$tjp_3 = null;
    private static final JoinPoint.StaticPart ajc$tjp_4 = null;
    private static final JoinPoint.StaticPart ajc$tjp_5 = null;
    private static final JoinPoint.StaticPart ajc$tjp_6 = null;
    private static final JoinPoint.StaticPart ajc$tjp_7 = null;
    private static final JoinPoint.StaticPart ajc$tjp_8 = null;
    private static final JoinPoint.StaticPart ajc$tjp_9 = null;
    short balance;
    short graphicsMode = 64;
    int opColorB = 32768;
    int opColorG = 32768;
    int opColorR = 32768;
    short reserved;

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("BaseMediaInfoAtom.java", BaseMediaInfoAtom.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getGraphicsMode", "com.googlecode.mp4parser.boxes.apple.BaseMediaInfoAtom", "", "", "", "short"), 54);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setGraphicsMode", "com.googlecode.mp4parser.boxes.apple.BaseMediaInfoAtom", "short", "graphicsMode", "", "void"), 58);
        ajc$tjp_10 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getReserved", "com.googlecode.mp4parser.boxes.apple.BaseMediaInfoAtom", "", "", "", "short"), 94);
        ajc$tjp_11 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setReserved", "com.googlecode.mp4parser.boxes.apple.BaseMediaInfoAtom", "short", "reserved", "", "void"), 98);
        ajc$tjp_12 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "toString", "com.googlecode.mp4parser.boxes.apple.BaseMediaInfoAtom", "", "", "", "java.lang.String"), 103);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getOpColorR", "com.googlecode.mp4parser.boxes.apple.BaseMediaInfoAtom", "", "", "", "int"), 62);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setOpColorR", "com.googlecode.mp4parser.boxes.apple.BaseMediaInfoAtom", "int", "opColorR", "", "void"), 66);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getOpColorG", "com.googlecode.mp4parser.boxes.apple.BaseMediaInfoAtom", "", "", "", "int"), 70);
        ajc$tjp_5 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setOpColorG", "com.googlecode.mp4parser.boxes.apple.BaseMediaInfoAtom", "int", "opColorG", "", "void"), 74);
        ajc$tjp_6 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getOpColorB", "com.googlecode.mp4parser.boxes.apple.BaseMediaInfoAtom", "", "", "", "int"), 78);
        ajc$tjp_7 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setOpColorB", "com.googlecode.mp4parser.boxes.apple.BaseMediaInfoAtom", "int", "opColorB", "", "void"), 82);
        ajc$tjp_8 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getBalance", "com.googlecode.mp4parser.boxes.apple.BaseMediaInfoAtom", "", "", "", "short"), 86);
        ajc$tjp_9 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setBalance", "com.googlecode.mp4parser.boxes.apple.BaseMediaInfoAtom", "short", "balance", "", "void"), 90);
    }

    public BaseMediaInfoAtom() {
        super(TYPE);
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return 16;
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        byteBuffer.putShort(this.graphicsMode);
        IsoTypeWriter.writeUInt16(byteBuffer, this.opColorR);
        IsoTypeWriter.writeUInt16(byteBuffer, this.opColorG);
        IsoTypeWriter.writeUInt16(byteBuffer, this.opColorB);
        byteBuffer.putShort(this.balance);
        byteBuffer.putShort(this.reserved);
    }

    /* access modifiers changed from: protected */
    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        this.graphicsMode = content.getShort();
        this.opColorR = IsoTypeReader.readUInt16(content);
        this.opColorG = IsoTypeReader.readUInt16(content);
        this.opColorB = IsoTypeReader.readUInt16(content);
        this.balance = content.getShort();
        this.reserved = content.getShort();
    }

    public short getGraphicsMode() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.graphicsMode;
    }

    public void setGraphicsMode(short graphicsMode2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, Conversions.shortObject(graphicsMode2)));
        this.graphicsMode = graphicsMode2;
    }

    public int getOpColorR() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return this.opColorR;
    }

    public void setOpColorR(int opColorR2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, (Object) this, (Object) this, Conversions.intObject(opColorR2)));
        this.opColorR = opColorR2;
    }

    public int getOpColorG() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, this, this));
        return this.opColorG;
    }

    public void setOpColorG(int opColorG2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_5, (Object) this, (Object) this, Conversions.intObject(opColorG2)));
        this.opColorG = opColorG2;
    }

    public int getOpColorB() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_6, this, this));
        return this.opColorB;
    }

    public void setOpColorB(int opColorB2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_7, (Object) this, (Object) this, Conversions.intObject(opColorB2)));
        this.opColorB = opColorB2;
    }

    public short getBalance() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_8, this, this));
        return this.balance;
    }

    public void setBalance(short balance2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_9, (Object) this, (Object) this, Conversions.shortObject(balance2)));
        this.balance = balance2;
    }

    public short getReserved() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_10, this, this));
        return this.reserved;
    }

    public void setReserved(short reserved2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_11, (Object) this, (Object) this, Conversions.shortObject(reserved2)));
        this.reserved = reserved2;
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_12, this, this));
        return "BaseMediaInfoAtom{graphicsMode=" + this.graphicsMode + ", opColorR=" + this.opColorR + ", opColorG=" + this.opColorG + ", opColorB=" + this.opColorB + ", balance=" + this.balance + ", reserved=" + this.reserved + '}';
    }
}
