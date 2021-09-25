package com.github.barteksc.pdfviewer;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.github.barteksc.pdfviewer.model.PagePart;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;
import java.util.HashSet;
import java.util.Set;

class RenderingHandler extends Handler {
    static final int MSG_RENDER_TASK = 1;
    private final Set<Integer> openedPages = new HashSet();
    private PdfDocument pdfDocument;
    /* access modifiers changed from: private */
    public PDFView pdfView;
    private PdfiumCore pdfiumCore;
    private RectF renderBounds = new RectF();
    private Matrix renderMatrix = new Matrix();
    private Rect roundedRenderBounds = new Rect();
    private boolean running = false;

    RenderingHandler(Looper looper, PDFView pdfView2, PdfiumCore pdfiumCore2, PdfDocument pdfDocument2) {
        super(looper);
        this.pdfView = pdfView2;
        this.pdfiumCore = pdfiumCore2;
        this.pdfDocument = pdfDocument2;
    }

    /* access modifiers changed from: package-private */
    public void addRenderingTask(int userPage, int page, float width, float height, RectF bounds, boolean thumbnail, int cacheOrder, boolean bestQuality, boolean annotationRendering) {
        sendMessage(obtainMessage(1, new RenderingTask(width, height, bounds, userPage, page, thumbnail, cacheOrder, bestQuality, annotationRendering)));
    }

    public void handleMessage(Message message) {
        final PagePart part = proceed((RenderingTask) message.obj);
        if (part == null) {
            return;
        }
        if (this.running) {
            this.pdfView.post(new Runnable() {
                public void run() {
                    RenderingHandler.this.pdfView.onBitmapRendered(part);
                }
            });
        } else {
            part.getRenderedBitmap().recycle();
        }
    }

    private PagePart proceed(RenderingTask renderingTask) {
        if (!this.openedPages.contains(Integer.valueOf(renderingTask.page))) {
            this.openedPages.add(Integer.valueOf(renderingTask.page));
            this.pdfiumCore.openPage(this.pdfDocument, renderingTask.page);
        }
        int w = Math.round(renderingTask.width);
        int h = Math.round(renderingTask.height);
        try {
            Bitmap render = Bitmap.createBitmap(w, h, renderingTask.bestQuality ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
            calculateBounds(w, h, renderingTask.bounds);
            this.pdfiumCore.renderPageBitmap(this.pdfDocument, render, renderingTask.page, this.roundedRenderBounds.left, this.roundedRenderBounds.top, this.roundedRenderBounds.width(), this.roundedRenderBounds.height(), renderingTask.annotationRendering);
            return new PagePart(renderingTask.userPage, renderingTask.page, render, renderingTask.width, renderingTask.height, renderingTask.bounds, renderingTask.thumbnail, renderingTask.cacheOrder);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void calculateBounds(int width, int height, RectF pageSliceBounds) {
        this.renderMatrix.reset();
        this.renderMatrix.postTranslate((-pageSliceBounds.left) * ((float) width), (-pageSliceBounds.top) * ((float) height));
        this.renderMatrix.postScale(1.0f / pageSliceBounds.width(), 1.0f / pageSliceBounds.height());
        this.renderBounds.set(0.0f, 0.0f, (float) width, (float) height);
        this.renderMatrix.mapRect(this.renderBounds);
        this.renderBounds.round(this.roundedRenderBounds);
    }

    /* access modifiers changed from: package-private */
    public void stop() {
        this.running = false;
    }

    /* access modifiers changed from: package-private */
    public void start() {
        this.running = true;
    }

    private class RenderingTask {
        boolean annotationRendering;
        boolean bestQuality;
        RectF bounds;
        int cacheOrder;
        float height;
        int page;
        boolean thumbnail;
        int userPage;
        float width;

        RenderingTask(float width2, float height2, RectF bounds2, int userPage2, int page2, boolean thumbnail2, int cacheOrder2, boolean bestQuality2, boolean annotationRendering2) {
            this.page = page2;
            this.width = width2;
            this.height = height2;
            this.bounds = bounds2;
            this.userPage = userPage2;
            this.thumbnail = thumbnail2;
            this.cacheOrder = cacheOrder2;
            this.bestQuality = bestQuality2;
            this.annotationRendering = annotationRendering2;
        }
    }
}
