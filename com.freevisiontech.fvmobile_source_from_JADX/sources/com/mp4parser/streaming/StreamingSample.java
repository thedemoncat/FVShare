package com.mp4parser.streaming;

import java.nio.ByteBuffer;

public interface StreamingSample {
    ByteBuffer getContent();

    long getDuration();

    SampleExtension[] getExtensions();
}
