package com.github.barteksc.pdfviewer;

import android.graphics.PointF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.github.barteksc.pdfviewer.scroll.ScrollHandle;
import com.github.barteksc.pdfviewer.util.Constants;

class DragPinchManager implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, ScaleGestureDetector.OnScaleGestureListener, View.OnTouchListener {
    private AnimationManager animationManager;
    private GestureDetector gestureDetector;
    private boolean isSwipeEnabled;
    private PDFView pdfView;
    private ScaleGestureDetector scaleGestureDetector;
    private boolean scaling = false;
    private boolean scrolling = false;
    private boolean swipeVertical;

    public DragPinchManager(PDFView pdfView2, AnimationManager animationManager2) {
        this.pdfView = pdfView2;
        this.animationManager = animationManager2;
        this.isSwipeEnabled = false;
        this.swipeVertical = pdfView2.isSwipeVertical();
        this.gestureDetector = new GestureDetector(pdfView2.getContext(), this);
        this.scaleGestureDetector = new ScaleGestureDetector(pdfView2.getContext(), this);
        pdfView2.setOnTouchListener(this);
    }

    public void enableDoubletap(boolean enableDoubletap) {
        if (enableDoubletap) {
            this.gestureDetector.setOnDoubleTapListener(this);
        } else {
            this.gestureDetector.setOnDoubleTapListener((GestureDetector.OnDoubleTapListener) null);
        }
    }

    public boolean isZooming() {
        return this.pdfView.isZooming();
    }

    private boolean isPageChange(float distance) {
        return Math.abs(distance) > Math.abs(this.pdfView.toCurrentScale(this.swipeVertical ? this.pdfView.getOptimalPageHeight() : this.pdfView.getOptimalPageWidth()) / 2.0f);
    }

    public void setSwipeEnabled(boolean isSwipeEnabled2) {
        this.isSwipeEnabled = isSwipeEnabled2;
    }

    public void setSwipeVertical(boolean swipeVertical2) {
        this.swipeVertical = swipeVertical2;
    }

    public boolean onSingleTapConfirmed(MotionEvent e) {
        ScrollHandle ps;
        OnTapListener onTapListener = this.pdfView.getOnTapListener();
        if ((onTapListener == null || !onTapListener.onTap(e)) && (ps = this.pdfView.getScrollHandle()) != null && !this.pdfView.documentFitsView()) {
            if (!ps.shown()) {
                ps.show();
            } else {
                ps.hide();
            }
        }
        this.pdfView.performClick();
        return true;
    }

    public boolean onDoubleTap(MotionEvent e) {
        if (this.pdfView.getZoom() < this.pdfView.getMidZoom()) {
            this.pdfView.zoomWithAnimation(e.getX(), e.getY(), this.pdfView.getMidZoom());
            return true;
        } else if (this.pdfView.getZoom() < this.pdfView.getMaxZoom()) {
            this.pdfView.zoomWithAnimation(e.getX(), e.getY(), this.pdfView.getMaxZoom());
            return true;
        } else {
            this.pdfView.resetZoomWithAnimation();
            return true;
        }
    }

    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    public boolean onDown(MotionEvent e) {
        this.animationManager.stopFling();
        return true;
    }

    public void onShowPress(MotionEvent e) {
    }

    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        this.scrolling = true;
        if (isZooming() || this.isSwipeEnabled) {
            this.pdfView.moveRelativeTo(-distanceX, -distanceY);
        }
        if (!this.scaling || this.pdfView.doRenderDuringScale()) {
            this.pdfView.loadPageByOffset();
        }
        return true;
    }

    public void onScrollEnd(MotionEvent event) {
        this.pdfView.loadPages();
        hideHandle();
    }

    public void onLongPress(MotionEvent e) {
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float minX;
        float minY;
        int xOffset = (int) this.pdfView.getCurrentXOffset();
        int yOffset = (int) this.pdfView.getCurrentYOffset();
        if (this.pdfView.isSwipeVertical()) {
            minX = -(this.pdfView.toCurrentScale(this.pdfView.getOptimalPageWidth()) - ((float) this.pdfView.getWidth()));
            minY = -(this.pdfView.calculateDocLength() - ((float) this.pdfView.getHeight()));
        } else {
            minX = -(this.pdfView.calculateDocLength() - ((float) this.pdfView.getWidth()));
            minY = -(this.pdfView.toCurrentScale(this.pdfView.getOptimalPageHeight()) - ((float) this.pdfView.getHeight()));
        }
        this.animationManager.startFlingAnimation(xOffset, yOffset, (int) velocityX, (int) velocityY, (int) minX, 0, (int) minY, 0);
        return true;
    }

    public boolean onScale(ScaleGestureDetector detector) {
        float dr = detector.getScaleFactor();
        float wantedZoom = this.pdfView.getZoom() * dr;
        if (wantedZoom < Constants.Pinch.MINIMUM_ZOOM) {
            dr = Constants.Pinch.MINIMUM_ZOOM / this.pdfView.getZoom();
        } else if (wantedZoom > Constants.Pinch.MAXIMUM_ZOOM) {
            dr = Constants.Pinch.MAXIMUM_ZOOM / this.pdfView.getZoom();
        }
        this.pdfView.zoomCenteredRelativeTo(dr, new PointF(detector.getFocusX(), detector.getFocusY()));
        return true;
    }

    public boolean onScaleBegin(ScaleGestureDetector detector) {
        this.scaling = true;
        return true;
    }

    public void onScaleEnd(ScaleGestureDetector detector) {
        this.pdfView.loadPages();
        hideHandle();
        this.scaling = false;
    }

    public boolean onTouch(View v, MotionEvent event) {
        boolean retVal = this.gestureDetector.onTouchEvent(event) || this.scaleGestureDetector.onTouchEvent(event);
        if (event.getAction() == 1 && this.scrolling) {
            this.scrolling = false;
            onScrollEnd(event);
        }
        return retVal;
    }

    private void hideHandle() {
        if (this.pdfView.getScrollHandle() != null && this.pdfView.getScrollHandle().shown()) {
            this.pdfView.getScrollHandle().hideDelayed();
        }
    }
}
