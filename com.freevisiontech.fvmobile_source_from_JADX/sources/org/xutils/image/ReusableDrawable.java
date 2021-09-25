package org.xutils.image;

interface ReusableDrawable {
    MemCacheKey getMemCacheKey();

    void setMemCacheKey(MemCacheKey memCacheKey);
}
