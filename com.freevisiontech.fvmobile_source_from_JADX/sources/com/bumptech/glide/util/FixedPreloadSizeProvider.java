package com.bumptech.glide.util;

import com.bumptech.glide.ListPreloader;
import java.util.Arrays;

public class FixedPreloadSizeProvider<T> implements ListPreloader.PreloadSizeProvider<T> {
    private final int[] size;

    public FixedPreloadSizeProvider(int width, int height) {
        this.size = new int[]{width, height};
    }

    public int[] getPreloadSize(T t, int adapterPosition, int itemPosition) {
        return Arrays.copyOf(this.size, this.size.length);
    }
}
