package com.googlecode.mp4parser.boxes.apple;

import com.coremedia.iso.IsoTypeReaderVariable;
import com.coremedia.iso.IsoTypeWriterVariable;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import java.nio.ByteBuffer;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.internal.Conversions;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public abstract class AppleVariableSignedIntegerBox extends AppleDataBox {
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    private static final JoinPoint.StaticPart ajc$tjp_3 = null;
    int intLength = 1;
    long value;

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("AppleVariableSignedIntegerBox.java", AppleVariableSignedIntegerBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getIntLength", "com.googlecode.mp4parser.boxes.apple.AppleVariableSignedIntegerBox", "", "", "", "int"), 19);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setIntLength", "com.googlecode.mp4parser.boxes.apple.AppleVariableSignedIntegerBox", "int", "intLength", "", "void"), 23);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getValue", "com.googlecode.mp4parser.boxes.apple.AppleVariableSignedIntegerBox", "", "", "", "long"), 27);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setValue", "com.googlecode.mp4parser.boxes.apple.AppleVariableSignedIntegerBox", "long", "value", "", "void"), 36);
    }

    protected AppleVariableSignedIntegerBox(String type) {
        super(type, 15);
    }

    public int getIntLength() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.intLength;
    }

    public void setIntLength(int intLength2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, Conversions.intObject(intLength2)));
        this.intLength = intLength2;
    }

    public long getValue() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        if (!isParsed()) {
            parseDetails();
        }
        return this.value;
    }

    public void setValue(long value2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, (Object) this, (Object) this, Conversions.longObject(value2)));
        if (value2 <= 127 && value2 > -128) {
            this.intLength = 1;
        } else if (value2 <= 32767 && value2 > -32768 && this.intLength < 2) {
            this.intLength = 2;
        } else if (value2 > 8388607 || value2 <= -8388608 || this.intLength >= 3) {
            this.intLength = 4;
        } else {
            this.intLength = 3;
        }
        this.value = value2;
    }

    /* access modifiers changed from: protected */
    public byte[] writeData() {
        int dLength = getDataLength();
        ByteBuffer b = ByteBuffer.wrap(new byte[dLength]);
        IsoTypeWriterVariable.write(this.value, b, dLength);
        return b.array();
    }

    /* access modifiers changed from: protected */
    public void parseData(ByteBuffer data) {
        int intLength2 = data.remaining();
        this.value = IsoTypeReaderVariable.read(data, intLength2);
        this.intLength = intLength2;
    }

    /* access modifiers changed from: protected */
    public int getDataLength() {
        return this.intLength;
    }
}
