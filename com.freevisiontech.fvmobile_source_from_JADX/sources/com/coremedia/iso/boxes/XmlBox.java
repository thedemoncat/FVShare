package com.coremedia.iso.boxes;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.Utf8;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import java.nio.ByteBuffer;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class XmlBox extends AbstractFullBox {
    public static final String TYPE = "xml ";
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    String xml = "";

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("XmlBox.java", XmlBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getXml", "com.coremedia.iso.boxes.XmlBox", "", "", "", "java.lang.String"), 20);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setXml", "com.coremedia.iso.boxes.XmlBox", "java.lang.String", "xml", "", "void"), 24);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "toString", "com.coremedia.iso.boxes.XmlBox", "", "", "", "java.lang.String"), 46);
    }

    public XmlBox() {
        super(TYPE);
    }

    public String getXml() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.xml;
    }

    public void setXml(String xml2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, (Object) xml2));
        this.xml = xml2;
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return (long) (Utf8.utf8StringLengthInBytes(this.xml) + 4);
    }

    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        this.xml = IsoTypeReader.readString(content, content.remaining());
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        byteBuffer.put(Utf8.convert(this.xml));
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return "XmlBox{xml='" + this.xml + '\'' + '}';
    }
}
