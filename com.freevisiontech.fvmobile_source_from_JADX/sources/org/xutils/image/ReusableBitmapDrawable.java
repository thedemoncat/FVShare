package org.xutils.image;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

final class ReusableBitmapDrawable extends BitmapDrawable implements ReusableDrawable {
    private MemCacheKey key;

    public ReusableBitmapDrawable(Resources res, Bitmap bitmap) {
        super(res, bitmap);
    }

    public MemCacheKey getMemCacheKey() {
        return this.key;
    }

    public void setMemCacheKey(MemCacheKey key2) {
        this.key = key2;
    }
}
