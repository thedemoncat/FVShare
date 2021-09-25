package com.googlecode.mp4parser.authoring.builder;

import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class ByteBufferHelper {
    public static List<ByteBuffer> mergeAdjacentBuffers(List<ByteBuffer> samples) {
        ArrayList<ByteBuffer> nuSamples = new ArrayList<>(samples.size());
        for (ByteBuffer buffer : samples) {
            int lastIndex = nuSamples.size() - 1;
            if (lastIndex >= 0 && buffer.hasArray() && nuSamples.get(lastIndex).hasArray() && buffer.array() == nuSamples.get(lastIndex).array()) {
                if (nuSamples.get(lastIndex).limit() + nuSamples.get(lastIndex).arrayOffset() == buffer.arrayOffset()) {
                    ByteBuffer oldBuffer = nuSamples.remove(lastIndex);
                    nuSamples.add(ByteBuffer.wrap(buffer.array(), oldBuffer.arrayOffset(), oldBuffer.limit() + buffer.limit()).slice());
                }
            }
            if (lastIndex < 0 || !(buffer instanceof MappedByteBuffer) || !(nuSamples.get(lastIndex) instanceof MappedByteBuffer) || nuSamples.get(lastIndex).limit() != nuSamples.get(lastIndex).capacity() - buffer.capacity()) {
                buffer.reset();
                nuSamples.add(buffer);
            } else {
                ByteBuffer oldBuffer2 = nuSamples.get(lastIndex);
                oldBuffer2.limit(buffer.limit() + oldBuffer2.limit());
            }
        }
        return nuSamples;
    }
}
