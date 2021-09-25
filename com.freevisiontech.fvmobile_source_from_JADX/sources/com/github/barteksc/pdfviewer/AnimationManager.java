package com.github.barteksc.pdfviewer;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.view.animation.DecelerateInterpolator;
import android.widget.OverScroller;

class AnimationManager {
    private ValueAnimator animation;
    private boolean flinging = false;
    /* access modifiers changed from: private */
    public PDFView pdfView;
    private OverScroller scroller;

    public AnimationManager(PDFView pdfView2) {
        this.pdfView = pdfView2;
        this.scroller = new OverScroller(pdfView2.getContext());
    }

    public void startXAnimation(float xFrom, float xTo) {
        stopAll();
        this.animation = ValueAnimator.ofFloat(new float[]{xFrom, xTo});
        this.animation.setInterpolator(new DecelerateInterpolator());
        this.animation.addUpdateListener(new XAnimation());
        this.animation.setDuration(400);
        this.animation.start();
    }

    public void startYAnimation(float yFrom, float yTo) {
        stopAll();
        this.animation = ValueAnimator.ofFloat(new float[]{yFrom, yTo});
        this.animation.setInterpolator(new DecelerateInterpolator());
        this.animation.addUpdateListener(new YAnimation());
        this.animation.setDuration(400);
        this.animation.start();
    }

    public void startZoomAnimation(float centerX, float centerY, float zoomFrom, float zoomTo) {
        stopAll();
        this.animation = ValueAnimator.ofFloat(new float[]{zoomFrom, zoomTo});
        this.animation.setInterpolator(new DecelerateInterpolator());
        ZoomAnimation zoomAnim = new ZoomAnimation(centerX, centerY);
        this.animation.addUpdateListener(zoomAnim);
        this.animation.addListener(zoomAnim);
        this.animation.setDuration(400);
        this.animation.start();
    }

    public void startFlingAnimation(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY) {
        stopAll();
        this.flinging = true;
        this.scroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY);
    }

    /* access modifiers changed from: package-private */
    public void computeFling() {
        if (this.scroller.computeScrollOffset()) {
            this.pdfView.moveTo((float) this.scroller.getCurrX(), (float) this.scroller.getCurrY());
            this.pdfView.loadPageByOffset();
        } else if (this.flinging) {
            this.flinging = false;
            this.pdfView.loadPages();
            hideHandle();
        }
    }

    public void stopAll() {
        if (this.animation != null) {
            this.animation.cancel();
            this.animation = null;
        }
        stopFling();
    }

    public void stopFling() {
        this.flinging = false;
        this.scroller.forceFinished(true);
    }

    class XAnimation implements ValueAnimator.AnimatorUpdateListener {
        XAnimation() {
        }

        public void onAnimationUpdate(ValueAnimator animation) {
            AnimationManager.this.pdfView.moveTo(((Float) animation.getAnimatedValue()).floatValue(), AnimationManager.this.pdfView.getCurrentYOffset());
        }
    }

    class YAnimation implements ValueAnimator.AnimatorUpdateListener {
        YAnimation() {
        }

        public void onAnimationUpdate(ValueAnimator animation) {
            AnimationManager.this.pdfView.moveTo(AnimationManager.this.pdfView.getCurrentXOffset(), ((Float) animation.getAnimatedValue()).floatValue());
        }
    }

    class ZoomAnimation implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
        private final float centerX;
        private final float centerY;

        public ZoomAnimation(float centerX2, float centerY2) {
            this.centerX = centerX2;
            this.centerY = centerY2;
        }

        public void onAnimationUpdate(ValueAnimator animation) {
            AnimationManager.this.pdfView.zoomCenteredTo(((Float) animation.getAnimatedValue()).floatValue(), new PointF(this.centerX, this.centerY));
        }

        public void onAnimationCancel(Animator animation) {
        }

        public void onAnimationEnd(Animator animation) {
            AnimationManager.this.pdfView.loadPages();
            AnimationManager.this.hideHandle();
        }

        public void onAnimationRepeat(Animator animation) {
        }

        public void onAnimationStart(Animator animation) {
        }
    }

    /* access modifiers changed from: private */
    public void hideHandle() {
        if (this.pdfView.getScrollHandle() != null) {
            this.pdfView.getScrollHandle().hideDelayed();
        }
    }
}
