package com.coremedia.iso.boxes.sampleentry;

import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.Container;

public interface SampleEntry extends Box, Container {
    int getDataReferenceIndex();

    void setDataReferenceIndex(int i);
}
