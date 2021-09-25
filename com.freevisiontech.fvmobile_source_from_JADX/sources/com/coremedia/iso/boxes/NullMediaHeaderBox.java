package com.coremedia.iso.boxes;

import java.nio.ByteBuffer;

public class NullMediaHeaderBox extends AbstractMediaHeaderBox {
    public static final String TYPE = "nmhd";

    public NullMediaHeaderBox() {
        super(TYPE);
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return 4;
    }

    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
    }
}
