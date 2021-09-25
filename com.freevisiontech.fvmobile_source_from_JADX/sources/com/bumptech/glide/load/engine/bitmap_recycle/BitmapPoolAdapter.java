package com.bumptech.glide.load.engine.bitmap_recycle;

import android.graphics.Bitmap;

public class BitmapPoolAdapter implements BitmapPool {
    public int getMaxSize() {
        return 0;
    }

    public void setSizeMultiplier(float sizeMultiplier) {
    }

    public boolean put(Bitmap bitmap) {
        return false;
    }

    public Bitmap get(int width, int height, Bitmap.Config config) {
        return null;
    }

    public Bitmap getDirty(int width, int height, Bitmap.Config config) {
        return null;
    }

    public void clearMemory() {
    }

    public void trimMemory(int level) {
    }
}
