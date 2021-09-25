package com.github.barteksc.pdfviewer.model;

import android.graphics.Bitmap;
import android.graphics.RectF;

public class PagePart {
    private int cacheOrder;
    private float height;
    private int page;
    private RectF pageRelativeBounds;
    private Bitmap renderedBitmap;
    private boolean thumbnail;
    private int userPage;
    private float width;

    public PagePart(int userPage2, int page2, Bitmap renderedBitmap2, float width2, float height2, RectF pageRelativeBounds2, boolean thumbnail2, int cacheOrder2) {
        this.userPage = userPage2;
        this.page = page2;
        this.renderedBitmap = renderedBitmap2;
        this.pageRelativeBounds = pageRelativeBounds2;
        this.thumbnail = thumbnail2;
        this.cacheOrder = cacheOrder2;
    }

    public int getCacheOrder() {
        return this.cacheOrder;
    }

    public int getPage() {
        return this.page;
    }

    public int getUserPage() {
        return this.userPage;
    }

    public Bitmap getRenderedBitmap() {
        return this.renderedBitmap;
    }

    public RectF getPageRelativeBounds() {
        return this.pageRelativeBounds;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

    public boolean isThumbnail() {
        return this.thumbnail;
    }

    public void setCacheOrder(int cacheOrder2) {
        this.cacheOrder = cacheOrder2;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof PagePart)) {
            return false;
        }
        PagePart part = (PagePart) obj;
        if (part.getPage() == this.page && part.getUserPage() == this.userPage && part.getWidth() == this.width && part.getHeight() == this.height && part.getPageRelativeBounds().left == this.pageRelativeBounds.left && part.getPageRelativeBounds().right == this.pageRelativeBounds.right && part.getPageRelativeBounds().top == this.pageRelativeBounds.top && part.getPageRelativeBounds().bottom == this.pageRelativeBounds.bottom) {
            return true;
        }
        return false;
    }
}
