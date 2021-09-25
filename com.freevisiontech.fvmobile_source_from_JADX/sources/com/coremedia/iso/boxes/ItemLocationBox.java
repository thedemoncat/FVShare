package com.coremedia.iso.boxes;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeReaderVariable;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.IsoTypeWriterVariable;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.freevisiontech.fvmobile.model.resolver.CompanyIdentifierResolver;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.internal.Conversions;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class ItemLocationBox extends AbstractFullBox {
    public static final String TYPE = "iloc";
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_10 = null;
    private static final JoinPoint.StaticPart ajc$tjp_11 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    private static final JoinPoint.StaticPart ajc$tjp_3 = null;
    private static final JoinPoint.StaticPart ajc$tjp_4 = null;
    private static final JoinPoint.StaticPart ajc$tjp_5 = null;
    private static final JoinPoint.StaticPart ajc$tjp_6 = null;
    private static final JoinPoint.StaticPart ajc$tjp_7 = null;
    private static final JoinPoint.StaticPart ajc$tjp_8 = null;
    private static final JoinPoint.StaticPart ajc$tjp_9 = null;
    public int baseOffsetSize = 8;
    public int indexSize = 0;
    public List<Item> items = new LinkedList();
    public int lengthSize = 8;
    public int offsetSize = 8;

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("ItemLocationBox.java", ItemLocationBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getOffsetSize", "com.coremedia.iso.boxes.ItemLocationBox", "", "", "", "int"), 119);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setOffsetSize", "com.coremedia.iso.boxes.ItemLocationBox", "int", "offsetSize", "", "void"), 123);
        ajc$tjp_10 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "createItem", "com.coremedia.iso.boxes.ItemLocationBox", "int:int:int:long:java.util.List", "itemId:constructionMethod:dataReferenceIndex:baseOffset:extents", "", "com.coremedia.iso.boxes.ItemLocationBox$Item"), 160);
        ajc$tjp_11 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "createExtent", "com.coremedia.iso.boxes.ItemLocationBox", "long:long:long", "extentOffset:extentLength:extentIndex", "", "com.coremedia.iso.boxes.ItemLocationBox$Extent"), (int) CompanyIdentifierResolver.ARENDI_AG);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getLengthSize", "com.coremedia.iso.boxes.ItemLocationBox", "", "", "", "int"), 127);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setLengthSize", "com.coremedia.iso.boxes.ItemLocationBox", "int", "lengthSize", "", "void"), 131);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getBaseOffsetSize", "com.coremedia.iso.boxes.ItemLocationBox", "", "", "", "int"), 135);
        ajc$tjp_5 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setBaseOffsetSize", "com.coremedia.iso.boxes.ItemLocationBox", "int", "baseOffsetSize", "", "void"), 139);
        ajc$tjp_6 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getIndexSize", "com.coremedia.iso.boxes.ItemLocationBox", "", "", "", "int"), (int) CompanyIdentifierResolver.STOLLMAN_EV_GMBH);
        ajc$tjp_7 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setIndexSize", "com.coremedia.iso.boxes.ItemLocationBox", "int", "indexSize", "", "void"), 147);
        ajc$tjp_8 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getItems", "com.coremedia.iso.boxes.ItemLocationBox", "", "", "", "java.util.List"), 151);
        ajc$tjp_9 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setItems", "com.coremedia.iso.boxes.ItemLocationBox", "java.util.List", "items", "", "void"), (int) CompanyIdentifierResolver.JIANGSU_TOPPOWER_AUTOMOTIVE_ELECTRONICS_CO_LTD);
    }

    public ItemLocationBox() {
        super(TYPE);
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        long size = 8;
        for (Item item : this.items) {
            size += (long) item.getSize();
        }
        return size;
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeUInt8(byteBuffer, (this.offsetSize << 4) | this.lengthSize);
        if (getVersion() == 1) {
            IsoTypeWriter.writeUInt8(byteBuffer, (this.baseOffsetSize << 4) | this.indexSize);
        } else {
            IsoTypeWriter.writeUInt8(byteBuffer, this.baseOffsetSize << 4);
        }
        IsoTypeWriter.writeUInt16(byteBuffer, this.items.size());
        for (Item item : this.items) {
            item.getContent(byteBuffer);
        }
    }

    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        int tmp = IsoTypeReader.readUInt8(content);
        this.offsetSize = tmp >>> 4;
        this.lengthSize = tmp & 15;
        int tmp2 = IsoTypeReader.readUInt8(content);
        this.baseOffsetSize = tmp2 >>> 4;
        if (getVersion() == 1) {
            this.indexSize = tmp2 & 15;
        }
        int itemCount = IsoTypeReader.readUInt16(content);
        for (int i = 0; i < itemCount; i++) {
            this.items.add(new Item(content));
        }
    }

    public int getOffsetSize() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.offsetSize;
    }

    public void setOffsetSize(int offsetSize2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, Conversions.intObject(offsetSize2)));
        this.offsetSize = offsetSize2;
    }

    public int getLengthSize() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return this.lengthSize;
    }

    public void setLengthSize(int lengthSize2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, (Object) this, (Object) this, Conversions.intObject(lengthSize2)));
        this.lengthSize = lengthSize2;
    }

    public int getBaseOffsetSize() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, this, this));
        return this.baseOffsetSize;
    }

    public void setBaseOffsetSize(int baseOffsetSize2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_5, (Object) this, (Object) this, Conversions.intObject(baseOffsetSize2)));
        this.baseOffsetSize = baseOffsetSize2;
    }

    public int getIndexSize() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_6, this, this));
        return this.indexSize;
    }

    public void setIndexSize(int indexSize2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_7, (Object) this, (Object) this, Conversions.intObject(indexSize2)));
        this.indexSize = indexSize2;
    }

    public List<Item> getItems() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_8, this, this));
        return this.items;
    }

    public void setItems(List<Item> items2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_9, (Object) this, (Object) this, (Object) items2));
        this.items = items2;
    }

    public Item createItem(int itemId, int constructionMethod, int dataReferenceIndex, long baseOffset, List<Extent> extents) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_10, (Object) this, (Object) this, new Object[]{Conversions.intObject(itemId), Conversions.intObject(constructionMethod), Conversions.intObject(dataReferenceIndex), Conversions.longObject(baseOffset), extents}));
        return new Item(itemId, constructionMethod, dataReferenceIndex, baseOffset, extents);
    }

    /* access modifiers changed from: package-private */
    public Item createItem(ByteBuffer bb) {
        return new Item(bb);
    }

    public class Item {
        public long baseOffset;
        public int constructionMethod;
        public int dataReferenceIndex;
        public List<Extent> extents = new LinkedList();
        public int itemId;

        public Item(ByteBuffer in) {
            this.itemId = IsoTypeReader.readUInt16(in);
            if (ItemLocationBox.this.getVersion() == 1) {
                this.constructionMethod = IsoTypeReader.readUInt16(in) & 15;
            }
            this.dataReferenceIndex = IsoTypeReader.readUInt16(in);
            if (ItemLocationBox.this.baseOffsetSize > 0) {
                this.baseOffset = IsoTypeReaderVariable.read(in, ItemLocationBox.this.baseOffsetSize);
            } else {
                this.baseOffset = 0;
            }
            int extentCount = IsoTypeReader.readUInt16(in);
            for (int i = 0; i < extentCount; i++) {
                this.extents.add(new Extent(in));
            }
        }

        public Item(int itemId2, int constructionMethod2, int dataReferenceIndex2, long baseOffset2, List<Extent> extents2) {
            this.itemId = itemId2;
            this.constructionMethod = constructionMethod2;
            this.dataReferenceIndex = dataReferenceIndex2;
            this.baseOffset = baseOffset2;
            this.extents = extents2;
        }

        public int getSize() {
            int size = 2;
            if (ItemLocationBox.this.getVersion() == 1) {
                size = 2 + 2;
            }
            int size2 = size + 2 + ItemLocationBox.this.baseOffsetSize + 2;
            for (Extent extent : this.extents) {
                size2 += extent.getSize();
            }
            return size2;
        }

        public void setBaseOffset(long baseOffset2) {
            this.baseOffset = baseOffset2;
        }

        public void getContent(ByteBuffer bb) {
            IsoTypeWriter.writeUInt16(bb, this.itemId);
            if (ItemLocationBox.this.getVersion() == 1) {
                IsoTypeWriter.writeUInt16(bb, this.constructionMethod);
            }
            IsoTypeWriter.writeUInt16(bb, this.dataReferenceIndex);
            if (ItemLocationBox.this.baseOffsetSize > 0) {
                IsoTypeWriterVariable.write(this.baseOffset, bb, ItemLocationBox.this.baseOffsetSize);
            }
            IsoTypeWriter.writeUInt16(bb, this.extents.size());
            for (Extent extent : this.extents) {
                extent.getContent(bb);
            }
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Item item = (Item) o;
            if (this.baseOffset != item.baseOffset) {
                return false;
            }
            if (this.constructionMethod != item.constructionMethod) {
                return false;
            }
            if (this.dataReferenceIndex != item.dataReferenceIndex) {
                return false;
            }
            if (this.itemId != item.itemId) {
                return false;
            }
            if (this.extents != null) {
                if (this.extents.equals(item.extents)) {
                    return true;
                }
            } else if (item.extents == null) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return (((((((this.itemId * 31) + this.constructionMethod) * 31) + this.dataReferenceIndex) * 31) + ((int) (this.baseOffset ^ (this.baseOffset >>> 32)))) * 31) + (this.extents != null ? this.extents.hashCode() : 0);
        }

        public String toString() {
            return "Item{baseOffset=" + this.baseOffset + ", itemId=" + this.itemId + ", constructionMethod=" + this.constructionMethod + ", dataReferenceIndex=" + this.dataReferenceIndex + ", extents=" + this.extents + '}';
        }
    }

    public Extent createExtent(long extentOffset, long extentLength, long extentIndex) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_11, (Object) this, (Object) this, new Object[]{Conversions.longObject(extentOffset), Conversions.longObject(extentLength), Conversions.longObject(extentIndex)}));
        return new Extent(extentOffset, extentLength, extentIndex);
    }

    /* access modifiers changed from: package-private */
    public Extent createExtent(ByteBuffer bb) {
        return new Extent(bb);
    }

    public class Extent {
        public long extentIndex;
        public long extentLength;
        public long extentOffset;

        public Extent(long extentOffset2, long extentLength2, long extentIndex2) {
            this.extentOffset = extentOffset2;
            this.extentLength = extentLength2;
            this.extentIndex = extentIndex2;
        }

        public Extent(ByteBuffer in) {
            if (ItemLocationBox.this.getVersion() == 1 && ItemLocationBox.this.indexSize > 0) {
                this.extentIndex = IsoTypeReaderVariable.read(in, ItemLocationBox.this.indexSize);
            }
            this.extentOffset = IsoTypeReaderVariable.read(in, ItemLocationBox.this.offsetSize);
            this.extentLength = IsoTypeReaderVariable.read(in, ItemLocationBox.this.lengthSize);
        }

        public void getContent(ByteBuffer os) {
            if (ItemLocationBox.this.getVersion() == 1 && ItemLocationBox.this.indexSize > 0) {
                IsoTypeWriterVariable.write(this.extentIndex, os, ItemLocationBox.this.indexSize);
            }
            IsoTypeWriterVariable.write(this.extentOffset, os, ItemLocationBox.this.offsetSize);
            IsoTypeWriterVariable.write(this.extentLength, os, ItemLocationBox.this.lengthSize);
        }

        public int getSize() {
            return (ItemLocationBox.this.indexSize > 0 ? ItemLocationBox.this.indexSize : 0) + ItemLocationBox.this.offsetSize + ItemLocationBox.this.lengthSize;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Extent extent = (Extent) o;
            if (this.extentIndex != extent.extentIndex) {
                return false;
            }
            if (this.extentLength != extent.extentLength) {
                return false;
            }
            if (this.extentOffset != extent.extentOffset) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return (((((int) (this.extentOffset ^ (this.extentOffset >>> 32))) * 31) + ((int) (this.extentLength ^ (this.extentLength >>> 32)))) * 31) + ((int) (this.extentIndex ^ (this.extentIndex >>> 32)));
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Extent");
            sb.append("{extentOffset=").append(this.extentOffset);
            sb.append(", extentLength=").append(this.extentLength);
            sb.append(", extentIndex=").append(this.extentIndex);
            sb.append('}');
            return sb.toString();
        }
    }
}
