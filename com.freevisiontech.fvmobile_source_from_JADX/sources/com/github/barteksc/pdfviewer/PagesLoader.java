package com.github.barteksc.pdfviewer;

import android.graphics.RectF;
import android.util.Pair;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.Constants;
import com.github.barteksc.pdfviewer.util.MathUtils;

class PagesLoader {
    private int cacheOrder;
    private float colWidth;
    private Pair<Integer, Integer> colsRows;
    private float pageRelativePartHeight;
    private float pageRelativePartWidth;
    private float partRenderHeight;
    private float partRenderWidth;
    private PDFView pdfView;
    private float rowHeight;
    private float scaledHeight;
    private float scaledSpacingPx;
    private float scaledWidth;
    private int thumbnailHeight;
    private final RectF thumbnailRect = new RectF(0.0f, 0.0f, 1.0f, 1.0f);
    private int thumbnailWidth;
    private float xOffset;
    private float yOffset;

    private class Holder {
        int col;
        int page;
        int row;

        private Holder() {
        }
    }

    PagesLoader(PDFView pdfView2) {
        this.pdfView = pdfView2;
    }

    private Pair<Integer, Integer> getPageColsRows() {
        float ratioX = 1.0f / this.pdfView.getOptimalPageWidth();
        float partHeight = (Constants.PART_SIZE * (1.0f / this.pdfView.getOptimalPageHeight())) / this.pdfView.getZoom();
        return new Pair<>(Integer.valueOf(MathUtils.ceil(1.0f / ((Constants.PART_SIZE * ratioX) / this.pdfView.getZoom()))), Integer.valueOf(MathUtils.ceil(1.0f / partHeight)));
    }

    private int documentPage(int userPage) {
        int documentPage = userPage;
        if (this.pdfView.getOriginalUserPages() != null) {
            if (userPage < 0 || userPage >= this.pdfView.getOriginalUserPages().length) {
                return -1;
            }
            documentPage = this.pdfView.getOriginalUserPages()[userPage];
        }
        if (documentPage < 0 || userPage >= this.pdfView.getDocumentPageCount()) {
            return -1;
        }
        return documentPage;
    }

    private Holder getPageAndCoordsByOffset(float offset, boolean endOffset) {
        float col;
        float row;
        Holder holder = new Holder();
        float fixOffset = -MathUtils.max(offset, 0.0f);
        if (this.pdfView.isSwipeVertical()) {
            holder.page = MathUtils.floor(fixOffset / (this.scaledHeight + this.scaledSpacingPx));
            row = Math.abs(fixOffset - ((this.scaledHeight + this.scaledSpacingPx) * ((float) holder.page))) / this.rowHeight;
            col = this.xOffset / this.colWidth;
        } else {
            holder.page = MathUtils.floor(fixOffset / (this.scaledWidth + this.scaledSpacingPx));
            col = Math.abs(fixOffset - ((this.scaledWidth + this.scaledSpacingPx) * ((float) holder.page))) / this.colWidth;
            row = this.yOffset / this.rowHeight;
        }
        if (endOffset) {
            holder.row = MathUtils.ceil(row);
            holder.col = MathUtils.ceil(col);
        } else {
            holder.row = MathUtils.floor(row);
            holder.col = MathUtils.floor(col);
        }
        return holder;
    }

    private void loadThumbnail(int userPage, int documentPage) {
        if (!this.pdfView.cacheManager.containsThumbnail(userPage, documentPage, (float) this.thumbnailWidth, (float) this.thumbnailHeight, this.thumbnailRect)) {
            this.pdfView.renderingHandler.addRenderingTask(userPage, documentPage, (float) this.thumbnailWidth, (float) this.thumbnailHeight, this.thumbnailRect, true, 0, this.pdfView.isBestQuality(), this.pdfView.isAnnotationRendering());
        }
    }

    private int loadRelative(int number, int nbOfPartsLoadable, boolean belowView) {
        float newOffset;
        int loaded = 0;
        if (this.pdfView.isSwipeVertical()) {
            newOffset = (this.pdfView.getCurrentYOffset() - ((float) (belowView ? this.pdfView.getHeight() : 0))) - ((this.rowHeight * ((float) number)) + 1.0f);
        } else {
            newOffset = (this.pdfView.getCurrentXOffset() - ((float) (belowView ? this.pdfView.getWidth() : 0))) - (this.colWidth * ((float) number));
        }
        Holder holder = getPageAndCoordsByOffset(newOffset, false);
        int documentPage = documentPage(holder.page);
        if (documentPage < 0) {
            return 0;
        }
        loadThumbnail(holder.page, documentPage);
        if (this.pdfView.isSwipeVertical()) {
            int firstCol = MathUtils.min(MathUtils.floor(this.xOffset / this.colWidth) - 1, 0);
            int lastCol = MathUtils.max(MathUtils.ceil((this.xOffset + ((float) this.pdfView.getWidth())) / this.colWidth) + 1, ((Integer) this.colsRows.first).intValue());
            for (int col = firstCol; col <= lastCol; col++) {
                if (loadCell(holder.page, documentPage, holder.row, col, this.pageRelativePartWidth, this.pageRelativePartHeight)) {
                    loaded++;
                }
                if (loaded >= nbOfPartsLoadable) {
                    return loaded;
                }
            }
        } else {
            int firstRow = MathUtils.min(MathUtils.floor(this.yOffset / this.rowHeight) - 1, 0);
            int lastRow = MathUtils.max(MathUtils.ceil((this.yOffset + ((float) this.pdfView.getHeight())) / this.rowHeight) + 1, ((Integer) this.colsRows.second).intValue());
            for (int row = firstRow; row <= lastRow; row++) {
                if (loadCell(holder.page, documentPage, row, holder.col, this.pageRelativePartWidth, this.pageRelativePartHeight)) {
                    loaded++;
                }
                if (loaded >= nbOfPartsLoadable) {
                    return loaded;
                }
            }
        }
        return loaded;
    }

    public int loadVisible() {
        Holder firstHolder;
        int visibleCols;
        int visibleRows;
        int parts = 0;
        if (this.pdfView.isSwipeVertical()) {
            firstHolder = getPageAndCoordsByOffset(this.pdfView.getCurrentYOffset(), false);
            Holder lastHolder = getPageAndCoordsByOffset((this.pdfView.getCurrentYOffset() - ((float) this.pdfView.getHeight())) + 1.0f, true);
            if (firstHolder.page == lastHolder.page) {
                visibleRows = (lastHolder.row - firstHolder.row) + 1;
            } else {
                int visibleRows2 = 0 + (((Integer) this.colsRows.second).intValue() - firstHolder.row);
                for (int page = firstHolder.page + 1; page < lastHolder.page; page++) {
                    visibleRows2 += ((Integer) this.colsRows.second).intValue();
                }
                visibleRows = visibleRows2 + lastHolder.row + 1;
            }
            for (int i = 0; i < visibleRows && parts < Constants.Cache.CACHE_SIZE; i++) {
                parts += loadRelative(i, Constants.Cache.CACHE_SIZE - parts, false);
            }
        } else {
            firstHolder = getPageAndCoordsByOffset(this.pdfView.getCurrentXOffset(), false);
            Holder lastHolder2 = getPageAndCoordsByOffset((this.pdfView.getCurrentXOffset() - ((float) this.pdfView.getWidth())) + 1.0f, true);
            if (firstHolder.page == lastHolder2.page) {
                visibleCols = (lastHolder2.col - firstHolder.col) + 1;
            } else {
                int visibleCols2 = 0 + (((Integer) this.colsRows.first).intValue() - firstHolder.col);
                for (int page2 = firstHolder.page + 1; page2 < lastHolder2.page; page2++) {
                    visibleCols2 += ((Integer) this.colsRows.first).intValue();
                }
                visibleCols = visibleCols2 + lastHolder2.col + 1;
            }
            for (int i2 = 0; i2 < visibleCols && parts < Constants.Cache.CACHE_SIZE; i2++) {
                parts += loadRelative(i2, Constants.Cache.CACHE_SIZE - parts, false);
            }
        }
        int prevDocPage = documentPage(firstHolder.page - 1);
        if (prevDocPage >= 0) {
            loadThumbnail(firstHolder.page - 1, prevDocPage);
        }
        int nextDocPage = documentPage(firstHolder.page + 1);
        if (nextDocPage >= 0) {
            loadThumbnail(firstHolder.page + 1, nextDocPage);
        }
        return parts;
    }

    private boolean loadCell(int userPage, int documentPage, int row, int col, float pageRelativePartWidth2, float pageRelativePartHeight2) {
        float relX = pageRelativePartWidth2 * ((float) col);
        float relY = pageRelativePartHeight2 * ((float) row);
        float relWidth = pageRelativePartWidth2;
        float relHeight = pageRelativePartHeight2;
        float renderWidth = this.partRenderWidth;
        float renderHeight = this.partRenderHeight;
        if (relX + relWidth > 1.0f) {
            relWidth = 1.0f - relX;
        }
        if (relY + relHeight > 1.0f) {
            relHeight = 1.0f - relY;
        }
        float renderWidth2 = renderWidth * relWidth;
        float renderHeight2 = renderHeight * relHeight;
        RectF pageRelativeBounds = new RectF(relX, relY, relX + relWidth, relY + relHeight);
        if (renderWidth2 <= 0.0f || renderHeight2 <= 0.0f) {
            return false;
        }
        if (!this.pdfView.cacheManager.upPartIfContained(userPage, documentPage, renderWidth2, renderHeight2, pageRelativeBounds, this.cacheOrder)) {
            this.pdfView.renderingHandler.addRenderingTask(userPage, documentPage, renderWidth2, renderHeight2, pageRelativeBounds, false, this.cacheOrder, this.pdfView.isBestQuality(), this.pdfView.isAnnotationRendering());
        }
        this.cacheOrder++;
        return true;
    }

    public void loadPages() {
        this.scaledHeight = this.pdfView.toCurrentScale(this.pdfView.getOptimalPageHeight());
        this.scaledWidth = this.pdfView.toCurrentScale(this.pdfView.getOptimalPageWidth());
        this.thumbnailWidth = (int) (this.pdfView.getOptimalPageWidth() * Constants.THUMBNAIL_RATIO);
        this.thumbnailHeight = (int) (this.pdfView.getOptimalPageHeight() * Constants.THUMBNAIL_RATIO);
        this.colsRows = getPageColsRows();
        this.xOffset = -MathUtils.max(this.pdfView.getCurrentXOffset(), 0.0f);
        this.yOffset = -MathUtils.max(this.pdfView.getCurrentYOffset(), 0.0f);
        this.rowHeight = this.scaledHeight / ((float) ((Integer) this.colsRows.second).intValue());
        this.colWidth = this.scaledWidth / ((float) ((Integer) this.colsRows.first).intValue());
        this.pageRelativePartWidth = 1.0f / ((float) ((Integer) this.colsRows.first).intValue());
        this.pageRelativePartHeight = 1.0f / ((float) ((Integer) this.colsRows.second).intValue());
        this.partRenderWidth = Constants.PART_SIZE / this.pageRelativePartWidth;
        this.partRenderHeight = Constants.PART_SIZE / this.pageRelativePartHeight;
        this.cacheOrder = 1;
        this.scaledSpacingPx = this.pdfView.toCurrentScale((float) this.pdfView.getSpacingPx());
        this.scaledSpacingPx -= this.scaledSpacingPx / ((float) this.pdfView.getPageCount());
        int loaded = loadVisible();
        if (this.pdfView.getScrollDir().equals(PDFView.ScrollDir.END)) {
            for (int i = 0; i < Constants.PRELOAD_COUNT && loaded < Constants.Cache.CACHE_SIZE; i++) {
                loaded += loadRelative(i, loaded, true);
            }
            return;
        }
        for (int i2 = 0; i2 > (-Constants.PRELOAD_COUNT) && loaded < Constants.Cache.CACHE_SIZE; i2--) {
            loaded += loadRelative(i2, loaded, false);
        }
    }
}
