package com.googlecode.mp4parser.boxes.apple;

import com.freevisiontech.fvmobile.common.BleConstant;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import java.nio.ByteBuffer;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.internal.Conversions;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class AppleDiskNumberBox extends AppleDataBox {
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    private static final JoinPoint.StaticPart ajc$tjp_3 = null;

    /* renamed from: a */
    int f1030a;

    /* renamed from: b */
    short f1031b;

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("AppleDiskNumberBox.java", AppleDiskNumberBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getA", "com.googlecode.mp4parser.boxes.apple.AppleDiskNumberBox", "", "", "", "int"), 16);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setA", "com.googlecode.mp4parser.boxes.apple.AppleDiskNumberBox", "int", "a", "", "void"), 20);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getB", "com.googlecode.mp4parser.boxes.apple.AppleDiskNumberBox", "", "", "", "short"), 24);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setB", "com.googlecode.mp4parser.boxes.apple.AppleDiskNumberBox", "short", "b", "", "void"), 28);
    }

    public AppleDiskNumberBox() {
        super("disk", 0);
    }

    public int getA() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.f1030a;
    }

    public void setA(int a) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, Conversions.intObject(a)));
        this.f1030a = a;
    }

    public short getB() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return this.f1031b;
    }

    public void setB(short b) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, (Object) this, (Object) this, Conversions.shortObject(b)));
        this.f1031b = b;
    }

    /* access modifiers changed from: protected */
    public byte[] writeData() {
        ByteBuffer bb = ByteBuffer.allocate(6);
        bb.putInt(this.f1030a);
        bb.putShort(this.f1031b);
        return bb.array();
    }

    /* access modifiers changed from: protected */
    public void parseData(ByteBuffer data) {
        this.f1030a = data.getInt();
        this.f1031b = data.getShort();
    }

    /* access modifiers changed from: protected */
    public int getDataLength() {
        return 6;
    }
}
