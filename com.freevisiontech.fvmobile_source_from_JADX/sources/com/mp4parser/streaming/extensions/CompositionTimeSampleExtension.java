package com.mp4parser.streaming.extensions;

import com.mp4parser.streaming.SampleExtension;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CompositionTimeSampleExtension implements SampleExtension {
    public static Map<Integer, CompositionTimeSampleExtension> pool = Collections.synchronizedMap(new HashMap());
    private int ctts;

    public static CompositionTimeSampleExtension create(int offset) {
        CompositionTimeSampleExtension c = pool.get(Integer.valueOf(offset));
        if (c != null) {
            return c;
        }
        CompositionTimeSampleExtension c2 = new CompositionTimeSampleExtension();
        c2.ctts = offset;
        pool.put(Integer.valueOf(offset), c2);
        return c2;
    }

    public int getCompositionTimeOffset() {
        return this.ctts;
    }
}
