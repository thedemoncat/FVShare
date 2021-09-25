package com.googlecode.mp4parser.boxes.dece;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.Utf8;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import java.nio.ByteBuffer;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class BaseLocationBox extends AbstractFullBox {
    public static final String TYPE = "bloc";
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    private static final JoinPoint.StaticPart ajc$tjp_3 = null;
    private static final JoinPoint.StaticPart ajc$tjp_4 = null;
    private static final JoinPoint.StaticPart ajc$tjp_5 = null;
    private static final JoinPoint.StaticPart ajc$tjp_6 = null;
    String baseLocation = "";
    String purchaseLocation = "";

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("BaseLocationBox.java", BaseLocationBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getBaseLocation", "com.googlecode.mp4parser.boxes.dece.BaseLocationBox", "", "", "", "java.lang.String"), 44);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setBaseLocation", "com.googlecode.mp4parser.boxes.dece.BaseLocationBox", "java.lang.String", "baseLocation", "", "void"), 48);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getPurchaseLocation", "com.googlecode.mp4parser.boxes.dece.BaseLocationBox", "", "", "", "java.lang.String"), 52);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setPurchaseLocation", "com.googlecode.mp4parser.boxes.dece.BaseLocationBox", "java.lang.String", "purchaseLocation", "", "void"), 56);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "equals", "com.googlecode.mp4parser.boxes.dece.BaseLocationBox", "java.lang.Object", "o", "", "boolean"), 86);
        ajc$tjp_5 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "hashCode", "com.googlecode.mp4parser.boxes.dece.BaseLocationBox", "", "", "", "int"), 100);
        ajc$tjp_6 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "toString", "com.googlecode.mp4parser.boxes.dece.BaseLocationBox", "", "", "", "java.lang.String"), 107);
    }

    public BaseLocationBox() {
        super(TYPE);
    }

    public BaseLocationBox(String baseLocation2, String purchaseLocation2) {
        super(TYPE);
        this.baseLocation = baseLocation2;
        this.purchaseLocation = purchaseLocation2;
    }

    public String getBaseLocation() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.baseLocation;
    }

    public void setBaseLocation(String baseLocation2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, (Object) baseLocation2));
        this.baseLocation = baseLocation2;
    }

    public String getPurchaseLocation() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return this.purchaseLocation;
    }

    public void setPurchaseLocation(String purchaseLocation2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, (Object) this, (Object) this, (Object) purchaseLocation2));
        this.purchaseLocation = purchaseLocation2;
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return 1028;
    }

    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        this.baseLocation = IsoTypeReader.readString(content);
        content.get(new byte[((256 - Utf8.utf8StringLengthInBytes(this.baseLocation)) - 1)]);
        this.purchaseLocation = IsoTypeReader.readString(content);
        content.get(new byte[((256 - Utf8.utf8StringLengthInBytes(this.purchaseLocation)) - 1)]);
        content.get(new byte[512]);
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        byteBuffer.put(Utf8.convert(this.baseLocation));
        byteBuffer.put(new byte[(256 - Utf8.utf8StringLengthInBytes(this.baseLocation))]);
        byteBuffer.put(Utf8.convert(this.purchaseLocation));
        byteBuffer.put(new byte[(256 - Utf8.utf8StringLengthInBytes(this.purchaseLocation))]);
        byteBuffer.put(new byte[512]);
    }

    public boolean equals(Object o) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, (Object) this, (Object) this, o));
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BaseLocationBox that = (BaseLocationBox) o;
        if (this.baseLocation == null ? that.baseLocation != null : !this.baseLocation.equals(that.baseLocation)) {
            return false;
        }
        if (this.purchaseLocation != null) {
            if (this.purchaseLocation.equals(that.purchaseLocation)) {
                return true;
            }
        } else if (that.purchaseLocation == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result;
        int i = 0;
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_5, this, this));
        if (this.baseLocation != null) {
            result = this.baseLocation.hashCode();
        } else {
            result = 0;
        }
        int i2 = result * 31;
        if (this.purchaseLocation != null) {
            i = this.purchaseLocation.hashCode();
        }
        return i2 + i;
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_6, this, this));
        return "BaseLocationBox{baseLocation='" + this.baseLocation + '\'' + ", purchaseLocation='" + this.purchaseLocation + '\'' + '}';
    }
}
