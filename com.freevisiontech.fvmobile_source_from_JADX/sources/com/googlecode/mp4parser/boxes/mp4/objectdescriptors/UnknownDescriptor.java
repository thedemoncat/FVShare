package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

public class UnknownDescriptor extends BaseDescriptor {
    private static Logger log = Logger.getLogger(UnknownDescriptor.class.getName());
    private ByteBuffer data;

    public void parseDetail(ByteBuffer bb) throws IOException {
        this.data = bb.slice();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UnknownDescriptor");
        sb.append("{tag=").append(this.tag);
        sb.append(", sizeOfInstance=").append(this.sizeOfInstance);
        sb.append(", data=").append(this.data);
        sb.append('}');
        return sb.toString();
    }

    public ByteBuffer serialize() {
        throw new RuntimeException("sdjlhfl");
    }

    /* access modifiers changed from: package-private */
    public int getContentSize() {
        throw new RuntimeException("sdjlhfl");
    }
}
