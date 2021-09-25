package com.github.barteksc.pdfviewer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.HandlerThread;
import android.support.p001v4.internal.view.SupportMenu;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.github.barteksc.pdfviewer.model.PagePart;
import com.github.barteksc.pdfviewer.scroll.ScrollHandle;
import com.github.barteksc.pdfviewer.source.AssetSource;
import com.github.barteksc.pdfviewer.source.ByteArraySource;
import com.github.barteksc.pdfviewer.source.DocumentSource;
import com.github.barteksc.pdfviewer.source.FileSource;
import com.github.barteksc.pdfviewer.source.InputStreamSource;
import com.github.barteksc.pdfviewer.source.UriSource;
import com.github.barteksc.pdfviewer.util.ArrayUtils;
import com.github.barteksc.pdfviewer.util.Constants;
import com.github.barteksc.pdfviewer.util.MathUtils;
import com.github.barteksc.pdfviewer.util.Util;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PDFView extends RelativeLayout {
    public static final float DEFAULT_MAX_SCALE = 3.0f;
    public static final float DEFAULT_MID_SCALE = 1.75f;
    public static final float DEFAULT_MIN_SCALE = 1.0f;
    private static final String TAG = PDFView.class.getSimpleName();
    private AnimationManager animationManager;
    private boolean annotationRendering = false;
    private PaintFlagsDrawFilter antialiasFilter = new PaintFlagsDrawFilter(0, 3);
    private boolean bestQuality = false;
    CacheManager cacheManager;
    private int currentFilteredPage;
    private int currentPage;
    private float currentXOffset = 0.0f;
    private float currentYOffset = 0.0f;
    private Paint debugPaint;
    private DecodingAsyncTask decodingAsyncTask;
    private int defaultPage = 0;
    private int documentPageCount;
    /* access modifiers changed from: private */
    public DragPinchManager dragPinchManager;
    private boolean enableAntialiasing = true;
    private int[] filteredUserPageIndexes;
    private int[] filteredUserPages;
    private boolean isScrollHandleInit = false;
    private float maxZoom = 3.0f;
    private float midZoom = 1.75f;
    private float minZoom = 1.0f;
    private OnDrawListener onDrawAllListener;
    private OnDrawListener onDrawListener;
    private List<Integer> onDrawPagesNums = new ArrayList(10);
    private OnErrorListener onErrorListener;
    private OnLoadCompleteListener onLoadCompleteListener;
    private OnPageChangeListener onPageChangeListener;
    private OnPageScrollListener onPageScrollListener;
    private OnRenderListener onRenderListener;
    private OnTapListener onTapListener;
    private float optimalPageHeight;
    private float optimalPageWidth;
    private int[] originalUserPages;
    private int pageHeight;
    private int pageWidth;
    private PagesLoader pagesLoader;
    private Paint paint;
    private PdfDocument pdfDocument;
    private PdfiumCore pdfiumCore;
    private boolean recycled = true;
    private boolean renderDuringScale = false;
    RenderingHandler renderingHandler;
    private final HandlerThread renderingHandlerThread = new HandlerThread("PDF renderer");
    private ScrollDir scrollDir = ScrollDir.NONE;
    private ScrollHandle scrollHandle;
    private int spacingPx = 0;
    private State state = State.DEFAULT;
    /* access modifiers changed from: private */
    public boolean swipeVertical = true;
    private float zoom = 1.0f;

    enum ScrollDir {
        NONE,
        START,
        END
    }

    private enum State {
        DEFAULT,
        LOADED,
        SHOWN,
        ERROR
    }

    /* access modifiers changed from: package-private */
    public ScrollHandle getScrollHandle() {
        return this.scrollHandle;
    }

    public PDFView(Context context, AttributeSet set) {
        super(context, set);
        if (!isInEditMode()) {
            this.cacheManager = new CacheManager();
            this.animationManager = new AnimationManager(this);
            this.dragPinchManager = new DragPinchManager(this, this.animationManager);
            this.paint = new Paint();
            this.debugPaint = new Paint();
            this.debugPaint.setStyle(Paint.Style.STROKE);
            this.pdfiumCore = new PdfiumCore(context);
            setWillNotDraw(false);
        }
    }

    /* access modifiers changed from: private */
    public void load(DocumentSource docSource, String password, OnLoadCompleteListener listener, OnErrorListener onErrorListener2) {
        load(docSource, password, listener, onErrorListener2, (int[]) null);
    }

    /* access modifiers changed from: private */
    public void load(DocumentSource docSource, String password, OnLoadCompleteListener onLoadCompleteListener2, OnErrorListener onErrorListener2, int[] userPages) {
        if (!this.recycled) {
            throw new IllegalStateException("Don't call load on a PDF View without recycling it first.");
        }
        if (userPages != null) {
            this.originalUserPages = userPages;
            this.filteredUserPages = ArrayUtils.deleteDuplicatedPages(this.originalUserPages);
            this.filteredUserPageIndexes = ArrayUtils.calculateIndexesInDuplicateArray(this.originalUserPages);
        }
        this.onLoadCompleteListener = onLoadCompleteListener2;
        this.onErrorListener = onErrorListener2;
        int firstPageIdx = 0;
        if (this.originalUserPages != null) {
            firstPageIdx = this.originalUserPages[0];
        }
        this.recycled = false;
        this.decodingAsyncTask = new DecodingAsyncTask(docSource, password, this, this.pdfiumCore, firstPageIdx);
        this.decodingAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    public void jumpTo(int page, boolean withAnimation) {
        float offset = -calculatePageOffset(page);
        if (this.swipeVertical) {
            if (withAnimation) {
                this.animationManager.startYAnimation(this.currentYOffset, offset);
            } else {
                moveTo(this.currentXOffset, offset);
            }
        } else if (withAnimation) {
            this.animationManager.startXAnimation(this.currentXOffset, offset);
        } else {
            moveTo(offset, this.currentYOffset);
        }
        showPage(page);
    }

    public void jumpTo(int page) {
        jumpTo(page, false);
    }

    /* access modifiers changed from: package-private */
    public void showPage(int pageNb) {
        if (!this.recycled) {
            int pageNb2 = determineValidPageNumberFrom(pageNb);
            this.currentPage = pageNb2;
            this.currentFilteredPage = pageNb2;
            if (this.filteredUserPageIndexes != null && pageNb2 >= 0 && pageNb2 < this.filteredUserPageIndexes.length) {
                this.currentFilteredPage = this.filteredUserPageIndexes[pageNb2];
            }
            loadPages();
            if (this.scrollHandle != null && !documentFitsView()) {
                this.scrollHandle.setPageNum(this.currentPage + 1);
            }
            if (this.onPageChangeListener != null) {
                this.onPageChangeListener.onPageChanged(this.currentPage, getPageCount());
            }
        }
    }

    public float getPositionOffset() {
        float offset;
        if (this.swipeVertical) {
            offset = (-this.currentYOffset) / (calculateDocLength() - ((float) getHeight()));
        } else {
            offset = (-this.currentXOffset) / (calculateDocLength() - ((float) getWidth()));
        }
        return MathUtils.limit(offset, 0.0f, 1.0f);
    }

    public void setPositionOffset(float progress, boolean moveHandle) {
        if (this.swipeVertical) {
            moveTo(this.currentXOffset, ((-calculateDocLength()) + ((float) getHeight())) * progress, moveHandle);
        } else {
            moveTo(((-calculateDocLength()) + ((float) getWidth())) * progress, this.currentYOffset, moveHandle);
        }
        loadPageByOffset();
    }

    public void setPositionOffset(float progress) {
        setPositionOffset(progress, true);
    }

    private float calculatePageOffset(int page) {
        if (this.swipeVertical) {
            return toCurrentScale((((float) page) * this.optimalPageHeight) + ((float) (this.spacingPx * page)));
        }
        return toCurrentScale((((float) page) * this.optimalPageWidth) + ((float) (this.spacingPx * page)));
    }

    /* access modifiers changed from: package-private */
    public float calculateDocLength() {
        int pageCount = getPageCount();
        if (this.swipeVertical) {
            return toCurrentScale((((float) pageCount) * this.optimalPageHeight) + ((float) ((pageCount - 1) * this.spacingPx)));
        }
        return toCurrentScale((((float) pageCount) * this.optimalPageWidth) + ((float) ((pageCount - 1) * this.spacingPx)));
    }

    public void stopFling() {
        this.animationManager.stopFling();
    }

    public int getPageCount() {
        if (this.originalUserPages != null) {
            return this.originalUserPages.length;
        }
        return this.documentPageCount;
    }

    public void enableSwipe(boolean enableSwipe) {
        this.dragPinchManager.setSwipeEnabled(enableSwipe);
    }

    public void enableDoubletap(boolean enableDoubletap) {
        this.dragPinchManager.enableDoubletap(enableDoubletap);
    }

    /* access modifiers changed from: private */
    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener2) {
        this.onPageChangeListener = onPageChangeListener2;
    }

    /* access modifiers changed from: package-private */
    public OnPageChangeListener getOnPageChangeListener() {
        return this.onPageChangeListener;
    }

    /* access modifiers changed from: private */
    public void setOnPageScrollListener(OnPageScrollListener onPageScrollListener2) {
        this.onPageScrollListener = onPageScrollListener2;
    }

    /* access modifiers changed from: package-private */
    public OnPageScrollListener getOnPageScrollListener() {
        return this.onPageScrollListener;
    }

    /* access modifiers changed from: private */
    public void setOnRenderListener(OnRenderListener onRenderListener2) {
        this.onRenderListener = onRenderListener2;
    }

    /* access modifiers changed from: package-private */
    public OnRenderListener getOnRenderListener() {
        return this.onRenderListener;
    }

    /* access modifiers changed from: private */
    public void setOnTapListener(OnTapListener onTapListener2) {
        this.onTapListener = onTapListener2;
    }

    /* access modifiers changed from: package-private */
    public OnTapListener getOnTapListener() {
        return this.onTapListener;
    }

    /* access modifiers changed from: private */
    public void setOnDrawListener(OnDrawListener onDrawListener2) {
        this.onDrawListener = onDrawListener2;
    }

    /* access modifiers changed from: private */
    public void setOnDrawAllListener(OnDrawListener onDrawAllListener2) {
        this.onDrawAllListener = onDrawAllListener2;
    }

    public void recycle() {
        this.animationManager.stopAll();
        if (this.renderingHandler != null) {
            this.renderingHandler.stop();
            this.renderingHandler.removeMessages(1);
        }
        if (this.decodingAsyncTask != null) {
            this.decodingAsyncTask.cancel(true);
        }
        this.cacheManager.recycle();
        if (this.scrollHandle != null && this.isScrollHandleInit) {
            this.scrollHandle.destroyLayout();
        }
        if (!(this.pdfiumCore == null || this.pdfDocument == null)) {
            this.pdfiumCore.closeDocument(this.pdfDocument);
        }
        this.renderingHandler = null;
        this.originalUserPages = null;
        this.filteredUserPages = null;
        this.filteredUserPageIndexes = null;
        this.pdfDocument = null;
        this.scrollHandle = null;
        this.isScrollHandleInit = false;
        this.currentYOffset = 0.0f;
        this.currentXOffset = 0.0f;
        this.zoom = 1.0f;
        this.recycled = true;
        this.state = State.DEFAULT;
    }

    public boolean isRecycled() {
        return this.recycled;
    }

    public void computeScroll() {
        super.computeScroll();
        this.animationManager.computeFling();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        recycle();
        super.onDetachedFromWindow();
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (!isInEditMode() && this.state == State.SHOWN) {
            this.animationManager.stopAll();
            calculateOptimalWidthAndHeight();
            if (this.swipeVertical) {
                moveTo(this.currentXOffset, -calculatePageOffset(this.currentPage));
            } else {
                moveTo(-calculatePageOffset(this.currentPage), this.currentYOffset);
            }
            loadPageByOffset();
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (!isInEditMode()) {
            if (this.enableAntialiasing) {
                canvas.setDrawFilter(this.antialiasFilter);
            }
            Drawable bg = getBackground();
            if (bg == null) {
                canvas.drawColor(-1);
            } else {
                bg.draw(canvas);
            }
            if (!this.recycled && this.state == State.SHOWN) {
                float currentXOffset2 = this.currentXOffset;
                float currentYOffset2 = this.currentYOffset;
                canvas.translate(currentXOffset2, currentYOffset2);
                for (PagePart part : this.cacheManager.getThumbnails()) {
                    drawPart(canvas, part);
                }
                for (PagePart part2 : this.cacheManager.getPageParts()) {
                    drawPart(canvas, part2);
                    if (this.onDrawAllListener != null && !this.onDrawPagesNums.contains(Integer.valueOf(part2.getUserPage()))) {
                        this.onDrawPagesNums.add(Integer.valueOf(part2.getUserPage()));
                    }
                }
                for (Integer page : this.onDrawPagesNums) {
                    drawWithListener(canvas, page.intValue(), this.onDrawAllListener);
                }
                this.onDrawPagesNums.clear();
                drawWithListener(canvas, this.currentPage, this.onDrawListener);
                canvas.translate(-currentXOffset2, -currentYOffset2);
            }
        }
    }

    private void drawWithListener(Canvas canvas, int page, OnDrawListener listener) {
        float translateY;
        float translateX;
        if (listener != null) {
            if (this.swipeVertical) {
                translateX = 0.0f;
                translateY = calculatePageOffset(page);
            } else {
                translateY = 0.0f;
                translateX = calculatePageOffset(page);
            }
            canvas.translate(translateX, translateY);
            listener.onLayerDrawn(canvas, toCurrentScale(this.optimalPageWidth), toCurrentScale(this.optimalPageHeight), page);
            canvas.translate(-translateX, -translateY);
        }
    }

    private void drawPart(Canvas canvas, PagePart part) {
        RectF pageRelativeBounds = part.getPageRelativeBounds();
        Bitmap renderedBitmap = part.getRenderedBitmap();
        if (!renderedBitmap.isRecycled()) {
            float localTranslationX = 0.0f;
            float localTranslationY = 0.0f;
            if (this.swipeVertical) {
                localTranslationY = calculatePageOffset(part.getUserPage());
            } else {
                localTranslationX = calculatePageOffset(part.getUserPage());
            }
            canvas.translate(localTranslationX, localTranslationY);
            Rect srcRect = new Rect(0, 0, renderedBitmap.getWidth(), renderedBitmap.getHeight());
            float offsetX = toCurrentScale(pageRelativeBounds.left * this.optimalPageWidth);
            float offsetY = toCurrentScale(pageRelativeBounds.top * this.optimalPageHeight);
            RectF dstRect = new RectF((float) ((int) offsetX), (float) ((int) offsetY), (float) ((int) (offsetX + toCurrentScale(pageRelativeBounds.width() * this.optimalPageWidth))), (float) ((int) (offsetY + toCurrentScale(pageRelativeBounds.height() * this.optimalPageHeight))));
            float translationX = this.currentXOffset + localTranslationX;
            float translationY = this.currentYOffset + localTranslationY;
            if (dstRect.left + translationX >= ((float) getWidth()) || dstRect.right + translationX <= 0.0f || dstRect.top + translationY >= ((float) getHeight()) || dstRect.bottom + translationY <= 0.0f) {
                canvas.translate(-localTranslationX, -localTranslationY);
                return;
            }
            canvas.drawBitmap(renderedBitmap, srcRect, dstRect, this.paint);
            if (Constants.DEBUG_MODE) {
                this.debugPaint.setColor(part.getUserPage() % 2 == 0 ? SupportMenu.CATEGORY_MASK : -16776961);
                canvas.drawRect(dstRect, this.debugPaint);
            }
            canvas.translate(-localTranslationX, -localTranslationY);
        }
    }

    public void loadPages() {
        if (this.optimalPageWidth != 0.0f && this.optimalPageHeight != 0.0f && this.renderingHandler != null) {
            this.renderingHandler.removeMessages(1);
            this.cacheManager.makeANewSet();
            this.pagesLoader.loadPages();
            redraw();
        }
    }

    /* access modifiers changed from: package-private */
    public void loadComplete(PdfDocument pdfDocument2, int pageWidth2, int pageHeight2) {
        this.state = State.LOADED;
        this.documentPageCount = this.pdfiumCore.getPageCount(pdfDocument2);
        this.pdfDocument = pdfDocument2;
        this.pageWidth = pageWidth2;
        this.pageHeight = pageHeight2;
        calculateOptimalWidthAndHeight();
        this.pagesLoader = new PagesLoader(this);
        if (!this.renderingHandlerThread.isAlive()) {
            this.renderingHandlerThread.start();
        }
        this.renderingHandler = new RenderingHandler(this.renderingHandlerThread.getLooper(), this, this.pdfiumCore, pdfDocument2);
        this.renderingHandler.start();
        if (this.scrollHandle != null) {
            this.scrollHandle.setupLayout(this);
            this.isScrollHandleInit = true;
        }
        if (this.onLoadCompleteListener != null) {
            this.onLoadCompleteListener.loadComplete(this.documentPageCount);
        }
        jumpTo(this.defaultPage, false);
    }

    /* access modifiers changed from: package-private */
    public void loadError(Throwable t) {
        this.state = State.ERROR;
        recycle();
        invalidate();
        if (this.onErrorListener != null) {
            this.onErrorListener.onError(t);
        } else {
            Log.e("PDFView", "load pdf error", t);
        }
    }

    /* access modifiers changed from: package-private */
    public void redraw() {
        invalidate();
    }

    public void onBitmapRendered(PagePart part) {
        if (this.state == State.LOADED) {
            this.state = State.SHOWN;
            if (this.onRenderListener != null) {
                this.onRenderListener.onInitiallyRendered(getPageCount(), this.optimalPageWidth, this.optimalPageHeight);
            }
        }
        if (part.isThumbnail()) {
            this.cacheManager.cacheThumbnail(part);
        } else {
            this.cacheManager.cachePart(part);
        }
        redraw();
    }

    private int determineValidPageNumberFrom(int userPage) {
        if (userPage <= 0) {
            return 0;
        }
        if (this.originalUserPages != null) {
            if (userPage >= this.originalUserPages.length) {
                return this.originalUserPages.length - 1;
            }
            return userPage;
        } else if (userPage >= this.documentPageCount) {
            return this.documentPageCount - 1;
        } else {
            return userPage;
        }
    }

    private float calculateCenterOffsetForPage(int pageNb) {
        if (this.swipeVertical) {
            return (-((((float) pageNb) * this.optimalPageHeight) + ((float) (this.spacingPx * pageNb)))) + (((float) (getHeight() / 2)) - (this.optimalPageHeight / 2.0f));
        }
        return (-((((float) pageNb) * this.optimalPageWidth) + ((float) (this.spacingPx * pageNb)))) + (((float) (getWidth() / 2)) - (this.optimalPageWidth / 2.0f));
    }

    private void calculateOptimalWidthAndHeight() {
        if (this.state != State.DEFAULT && getWidth() != 0) {
            float maxWidth = (float) getWidth();
            float maxHeight = (float) getHeight();
            float ratio = ((float) this.pageWidth) / ((float) this.pageHeight);
            float w = maxWidth;
            float h = (float) Math.floor((double) (maxWidth / ratio));
            if (h > maxHeight) {
                h = maxHeight;
                w = (float) Math.floor((double) (maxHeight * ratio));
            }
            this.optimalPageWidth = w;
            this.optimalPageHeight = h;
        }
    }

    public void moveTo(float offsetX, float offsetY) {
        moveTo(offsetX, offsetY, true);
    }

    public void moveTo(float offsetX, float offsetY, boolean moveHandle) {
        if (this.swipeVertical) {
            float scaledPageWidth = toCurrentScale(this.optimalPageWidth);
            if (scaledPageWidth < ((float) getWidth())) {
                offsetX = ((float) (getWidth() / 2)) - (scaledPageWidth / 2.0f);
            } else if (offsetX > 0.0f) {
                offsetX = 0.0f;
            } else if (offsetX + scaledPageWidth < ((float) getWidth())) {
                offsetX = ((float) getWidth()) - scaledPageWidth;
            }
            float contentHeight = calculateDocLength();
            if (contentHeight < ((float) getHeight())) {
                offsetY = (((float) getHeight()) - contentHeight) / 2.0f;
            } else if (offsetY > 0.0f) {
                offsetY = 0.0f;
            } else if (offsetY + contentHeight < ((float) getHeight())) {
                offsetY = (-contentHeight) + ((float) getHeight());
            }
            if (offsetY < this.currentYOffset) {
                this.scrollDir = ScrollDir.END;
            } else if (offsetY > this.currentYOffset) {
                this.scrollDir = ScrollDir.START;
            } else {
                this.scrollDir = ScrollDir.NONE;
            }
        } else {
            float scaledPageHeight = toCurrentScale(this.optimalPageHeight);
            if (scaledPageHeight < ((float) getHeight())) {
                offsetY = ((float) (getHeight() / 2)) - (scaledPageHeight / 2.0f);
            } else if (offsetY > 0.0f) {
                offsetY = 0.0f;
            } else if (offsetY + scaledPageHeight < ((float) getHeight())) {
                offsetY = ((float) getHeight()) - scaledPageHeight;
            }
            float contentWidth = calculateDocLength();
            if (contentWidth < ((float) getWidth())) {
                offsetX = (((float) getWidth()) - contentWidth) / 2.0f;
            } else if (offsetX > 0.0f) {
                offsetX = 0.0f;
            } else if (offsetX + contentWidth < ((float) getWidth())) {
                offsetX = (-contentWidth) + ((float) getWidth());
            }
            if (offsetX < this.currentXOffset) {
                this.scrollDir = ScrollDir.END;
            } else if (offsetX > this.currentXOffset) {
                this.scrollDir = ScrollDir.START;
            } else {
                this.scrollDir = ScrollDir.NONE;
            }
        }
        this.currentXOffset = offsetX;
        this.currentYOffset = offsetY;
        float positionOffset = getPositionOffset();
        if (moveHandle && this.scrollHandle != null && !documentFitsView()) {
            this.scrollHandle.setScroll(positionOffset);
        }
        if (this.onPageScrollListener != null) {
            this.onPageScrollListener.onPageScrolled(getCurrentPage(), positionOffset);
        }
        redraw();
    }

    /* access modifiers changed from: package-private */
    public ScrollDir getScrollDir() {
        return this.scrollDir;
    }

    /* access modifiers changed from: package-private */
    public void loadPageByOffset() {
        float offset;
        float optimal;
        float screenCenter;
        if (getPageCount() != 0) {
            float spacingPerPage = (float) (this.spacingPx - (this.spacingPx / getPageCount()));
            if (this.swipeVertical) {
                offset = this.currentYOffset;
                optimal = this.optimalPageHeight + spacingPerPage;
                screenCenter = ((float) getHeight()) / 2.0f;
            } else {
                offset = this.currentXOffset;
                optimal = this.optimalPageWidth + spacingPerPage;
                screenCenter = ((float) getWidth()) / 2.0f;
            }
            int page = (int) Math.floor((double) ((Math.abs(offset) + screenCenter) / toCurrentScale(optimal)));
            if (page < 0 || page > getPageCount() - 1 || page == getCurrentPage()) {
                loadPages();
            } else {
                showPage(page);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public int[] getFilteredUserPages() {
        return this.filteredUserPages;
    }

    /* access modifiers changed from: package-private */
    public int[] getOriginalUserPages() {
        return this.originalUserPages;
    }

    /* access modifiers changed from: package-private */
    public int[] getFilteredUserPageIndexes() {
        return this.filteredUserPageIndexes;
    }

    /* access modifiers changed from: package-private */
    public int getDocumentPageCount() {
        return this.documentPageCount;
    }

    public void moveRelativeTo(float dx, float dy) {
        moveTo(this.currentXOffset + dx, this.currentYOffset + dy);
    }

    public void zoomTo(float zoom2) {
        this.zoom = zoom2;
    }

    public void zoomCenteredTo(float zoom2, PointF pivot) {
        float dzoom = zoom2 / this.zoom;
        zoomTo(zoom2);
        moveTo((this.currentXOffset * dzoom) + (pivot.x - (pivot.x * dzoom)), (this.currentYOffset * dzoom) + (pivot.y - (pivot.y * dzoom)));
    }

    public void zoomCenteredRelativeTo(float dzoom, PointF pivot) {
        zoomCenteredTo(this.zoom * dzoom, pivot);
    }

    public boolean documentFitsView() {
        int pageCount = getPageCount();
        int spacing = (pageCount - 1) * this.spacingPx;
        if (this.swipeVertical) {
            if ((((float) pageCount) * this.optimalPageHeight) + ((float) spacing) < ((float) getHeight())) {
                return true;
            }
            return false;
        } else if ((((float) pageCount) * this.optimalPageWidth) + ((float) spacing) >= ((float) getWidth())) {
            return false;
        } else {
            return true;
        }
    }

    public void fitToWidth(int page) {
        if (this.state != State.SHOWN) {
            Log.e(TAG, "Cannot fit, document not rendered yet");
            return;
        }
        fitToWidth();
        jumpTo(page);
    }

    public void fitToWidth() {
        if (this.state != State.SHOWN) {
            Log.e(TAG, "Cannot fit, document not rendered yet");
            return;
        }
        zoomTo(((float) getWidth()) / this.optimalPageWidth);
        setPositionOffset(0.0f);
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public float getCurrentXOffset() {
        return this.currentXOffset;
    }

    public float getCurrentYOffset() {
        return this.currentYOffset;
    }

    public float toRealScale(float size) {
        return size / this.zoom;
    }

    public float toCurrentScale(float size) {
        return this.zoom * size;
    }

    public float getZoom() {
        return this.zoom;
    }

    public boolean isZooming() {
        return this.zoom != this.minZoom;
    }

    public float getOptimalPageWidth() {
        return this.optimalPageWidth;
    }

    public float getOptimalPageHeight() {
        return this.optimalPageHeight;
    }

    /* access modifiers changed from: private */
    public void setDefaultPage(int defaultPage2) {
        this.defaultPage = defaultPage2;
    }

    public void resetZoom() {
        zoomTo(this.minZoom);
    }

    public void resetZoomWithAnimation() {
        zoomWithAnimation(this.minZoom);
    }

    public void zoomWithAnimation(float centerX, float centerY, float scale) {
        this.animationManager.startZoomAnimation(centerX, centerY, this.zoom, scale);
    }

    public void zoomWithAnimation(float scale) {
        this.animationManager.startZoomAnimation((float) (getWidth() / 2), (float) (getHeight() / 2), this.zoom, scale);
    }

    /* access modifiers changed from: private */
    public void setScrollHandle(ScrollHandle scrollHandle2) {
        this.scrollHandle = scrollHandle2;
    }

    public int getPageAtPositionOffset(float positionOffset) {
        int page = (int) Math.floor((double) (((float) getPageCount()) * positionOffset));
        return page == getPageCount() ? page - 1 : page;
    }

    public float getMinZoom() {
        return this.minZoom;
    }

    public void setMinZoom(float minZoom2) {
        this.minZoom = minZoom2;
    }

    public float getMidZoom() {
        return this.midZoom;
    }

    public void setMidZoom(float midZoom2) {
        this.midZoom = midZoom2;
    }

    public float getMaxZoom() {
        return this.maxZoom;
    }

    public void setMaxZoom(float maxZoom2) {
        this.maxZoom = maxZoom2;
    }

    public void useBestQuality(boolean bestQuality2) {
        this.bestQuality = bestQuality2;
    }

    public boolean isBestQuality() {
        return this.bestQuality;
    }

    public boolean isSwipeVertical() {
        return this.swipeVertical;
    }

    public void setSwipeVertical(boolean swipeVertical2) {
        this.swipeVertical = swipeVertical2;
    }

    public void enableAnnotationRendering(boolean annotationRendering2) {
        this.annotationRendering = annotationRendering2;
    }

    public boolean isAnnotationRendering() {
        return this.annotationRendering;
    }

    public void enableRenderDuringScale(boolean renderDuringScale2) {
        this.renderDuringScale = renderDuringScale2;
    }

    public boolean isAntialiasing() {
        return this.enableAntialiasing;
    }

    public void enableAntialiasing(boolean enableAntialiasing2) {
        this.enableAntialiasing = enableAntialiasing2;
    }

    /* access modifiers changed from: package-private */
    public int getSpacingPx() {
        return this.spacingPx;
    }

    /* access modifiers changed from: private */
    public void setSpacing(int spacing) {
        this.spacingPx = Util.getDP(getContext(), spacing);
    }

    public boolean doRenderDuringScale() {
        return this.renderDuringScale;
    }

    public PdfDocument.Meta getDocumentMeta() {
        if (this.pdfDocument == null) {
            return null;
        }
        return this.pdfiumCore.getDocumentMeta(this.pdfDocument);
    }

    public List<PdfDocument.Bookmark> getTableOfContents() {
        if (this.pdfDocument == null) {
            return new ArrayList();
        }
        return this.pdfiumCore.getTableOfContents(this.pdfDocument);
    }

    public Configurator fromAsset(String assetName) {
        return new Configurator(new AssetSource(assetName));
    }

    public Configurator fromFile(File file) {
        return new Configurator(new FileSource(file));
    }

    public Configurator fromUri(Uri uri) {
        return new Configurator(new UriSource(uri));
    }

    public Configurator fromBytes(byte[] bytes) {
        return new Configurator(new ByteArraySource(bytes));
    }

    public Configurator fromStream(InputStream stream) {
        return new Configurator(new InputStreamSource(stream));
    }

    public Configurator fromSource(DocumentSource docSource) {
        return new Configurator(docSource);
    }

    public class Configurator {
        private boolean annotationRendering;
        private boolean antialiasing;
        private int defaultPage;
        private final DocumentSource documentSource;
        private boolean enableDoubletap;
        private boolean enableSwipe;
        private OnDrawListener onDrawAllListener;
        private OnDrawListener onDrawListener;
        private OnErrorListener onErrorListener;
        private OnLoadCompleteListener onLoadCompleteListener;
        private OnPageChangeListener onPageChangeListener;
        private OnPageScrollListener onPageScrollListener;
        private OnRenderListener onRenderListener;
        private OnTapListener onTapListener;
        private int[] pageNumbers;
        private String password;
        private ScrollHandle scrollHandle;
        private int spacing;
        private boolean swipeHorizontal;

        private Configurator(DocumentSource documentSource2) {
            this.pageNumbers = null;
            this.enableSwipe = true;
            this.enableDoubletap = true;
            this.defaultPage = 0;
            this.swipeHorizontal = false;
            this.annotationRendering = false;
            this.password = null;
            this.scrollHandle = null;
            this.antialiasing = true;
            this.spacing = 0;
            this.documentSource = documentSource2;
        }

        public Configurator pages(int... pageNumbers2) {
            this.pageNumbers = pageNumbers2;
            return this;
        }

        public Configurator enableSwipe(boolean enableSwipe2) {
            this.enableSwipe = enableSwipe2;
            return this;
        }

        public Configurator enableDoubletap(boolean enableDoubletap2) {
            this.enableDoubletap = enableDoubletap2;
            return this;
        }

        public Configurator enableAnnotationRendering(boolean annotationRendering2) {
            this.annotationRendering = annotationRendering2;
            return this;
        }

        public Configurator onDraw(OnDrawListener onDrawListener2) {
            this.onDrawListener = onDrawListener2;
            return this;
        }

        public Configurator onDrawAll(OnDrawListener onDrawAllListener2) {
            this.onDrawAllListener = onDrawAllListener2;
            return this;
        }

        public Configurator onLoad(OnLoadCompleteListener onLoadCompleteListener2) {
            this.onLoadCompleteListener = onLoadCompleteListener2;
            return this;
        }

        public Configurator onPageScroll(OnPageScrollListener onPageScrollListener2) {
            this.onPageScrollListener = onPageScrollListener2;
            return this;
        }

        public Configurator onError(OnErrorListener onErrorListener2) {
            this.onErrorListener = onErrorListener2;
            return this;
        }

        public Configurator onPageChange(OnPageChangeListener onPageChangeListener2) {
            this.onPageChangeListener = onPageChangeListener2;
            return this;
        }

        public Configurator onRender(OnRenderListener onRenderListener2) {
            this.onRenderListener = onRenderListener2;
            return this;
        }

        public Configurator onTap(OnTapListener onTapListener2) {
            this.onTapListener = onTapListener2;
            return this;
        }

        public Configurator defaultPage(int defaultPage2) {
            this.defaultPage = defaultPage2;
            return this;
        }

        public Configurator swipeHorizontal(boolean swipeHorizontal2) {
            this.swipeHorizontal = swipeHorizontal2;
            return this;
        }

        public Configurator password(String password2) {
            this.password = password2;
            return this;
        }

        public Configurator scrollHandle(ScrollHandle scrollHandle2) {
            this.scrollHandle = scrollHandle2;
            return this;
        }

        public Configurator enableAntialiasing(boolean antialiasing2) {
            this.antialiasing = antialiasing2;
            return this;
        }

        public Configurator spacing(int spacing2) {
            this.spacing = spacing2;
            return this;
        }

        public void load() {
            PDFView.this.recycle();
            PDFView.this.setOnDrawListener(this.onDrawListener);
            PDFView.this.setOnDrawAllListener(this.onDrawAllListener);
            PDFView.this.setOnPageChangeListener(this.onPageChangeListener);
            PDFView.this.setOnPageScrollListener(this.onPageScrollListener);
            PDFView.this.setOnRenderListener(this.onRenderListener);
            PDFView.this.setOnTapListener(this.onTapListener);
            PDFView.this.enableSwipe(this.enableSwipe);
            PDFView.this.enableDoubletap(this.enableDoubletap);
            PDFView.this.setDefaultPage(this.defaultPage);
            PDFView.this.setSwipeVertical(!this.swipeHorizontal);
            PDFView.this.enableAnnotationRendering(this.annotationRendering);
            PDFView.this.setScrollHandle(this.scrollHandle);
            PDFView.this.enableAntialiasing(this.antialiasing);
            PDFView.this.setSpacing(this.spacing);
            PDFView.this.dragPinchManager.setSwipeVertical(PDFView.this.swipeVertical);
            if (this.pageNumbers != null) {
                PDFView.this.load(this.documentSource, this.password, this.onLoadCompleteListener, this.onErrorListener, this.pageNumbers);
            } else {
                PDFView.this.load(this.documentSource, this.password, this.onLoadCompleteListener, this.onErrorListener);
            }
        }
    }
}
