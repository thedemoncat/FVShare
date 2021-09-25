package com.github.barteksc.pdfviewer.scroll;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.p001v4.content.ContextCompat;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.github.barteksc.pdfviewer.C1633R;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.Util;

public class DefaultScrollHandle extends RelativeLayout implements ScrollHandle {
    private static final int DEFAULT_TEXT_SIZE = 16;
    private static final int HANDLE_LONG = 65;
    private static final int HANDLE_SHORT = 40;
    protected Context context;
    private float currentPos;
    private Handler handler;
    private Runnable hidePageScrollerRunnable;
    private boolean inverted;
    private PDFView pdfView;
    private float relativeHandlerMiddle;
    protected TextView textView;

    public DefaultScrollHandle(Context context2) {
        this(context2, false);
    }

    public DefaultScrollHandle(Context context2, boolean inverted2) {
        super(context2);
        this.relativeHandlerMiddle = 0.0f;
        this.handler = new Handler();
        this.hidePageScrollerRunnable = new Runnable() {
            public void run() {
                DefaultScrollHandle.this.hide();
            }
        };
        this.context = context2;
        this.inverted = inverted2;
        this.textView = new TextView(context2);
        setVisibility(4);
        setTextColor(-16777216);
        setTextSize(16);
    }

    public void setupLayout(PDFView pdfView2) {
        int width;
        int height;
        int align;
        Drawable background;
        if (pdfView2.isSwipeVertical()) {
            width = 65;
            height = 40;
            if (this.inverted) {
                align = 9;
                background = ContextCompat.getDrawable(this.context, C1633R.C1634drawable.default_scroll_handle_left);
            } else {
                align = 11;
                background = ContextCompat.getDrawable(this.context, C1633R.C1634drawable.default_scroll_handle_right);
            }
        } else {
            width = 40;
            height = 65;
            if (this.inverted) {
                align = 10;
                background = ContextCompat.getDrawable(this.context, C1633R.C1634drawable.default_scroll_handle_top);
            } else {
                align = 12;
                background = ContextCompat.getDrawable(this.context, C1633R.C1634drawable.default_scroll_handle_bottom);
            }
        }
        if (Build.VERSION.SDK_INT < 16) {
            setBackgroundDrawable(background);
        } else {
            setBackground(background);
        }
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(Util.getDP(this.context, width), Util.getDP(this.context, height));
        lp.setMargins(0, 0, 0, 0);
        RelativeLayout.LayoutParams tvlp = new RelativeLayout.LayoutParams(-2, -2);
        tvlp.addRule(13, -1);
        addView(this.textView, tvlp);
        lp.addRule(align);
        pdfView2.addView(this, lp);
        this.pdfView = pdfView2;
    }

    public void destroyLayout() {
        this.pdfView.removeView(this);
    }

    public void setScroll(float position) {
        if (!shown()) {
            show();
        } else {
            this.handler.removeCallbacks(this.hidePageScrollerRunnable);
        }
        setPosition(((float) (this.pdfView.isSwipeVertical() ? this.pdfView.getHeight() : this.pdfView.getWidth())) * position);
    }

    private void setPosition(float pos) {
        float pdfViewSize;
        if (!Float.isInfinite(pos) && !Float.isNaN(pos)) {
            if (this.pdfView.isSwipeVertical()) {
                pdfViewSize = (float) this.pdfView.getHeight();
            } else {
                pdfViewSize = (float) this.pdfView.getWidth();
            }
            float pos2 = pos - this.relativeHandlerMiddle;
            if (pos2 < 0.0f) {
                pos2 = 0.0f;
            } else if (pos2 > pdfViewSize - ((float) Util.getDP(this.context, 40))) {
                pos2 = pdfViewSize - ((float) Util.getDP(this.context, 40));
            }
            if (this.pdfView.isSwipeVertical()) {
                setY(pos2);
            } else {
                setX(pos2);
            }
            calculateMiddle();
            invalidate();
        }
    }

    private void calculateMiddle() {
        float pos;
        float viewSize;
        float pdfViewSize;
        if (this.pdfView.isSwipeVertical()) {
            pos = getY();
            viewSize = (float) getHeight();
            pdfViewSize = (float) this.pdfView.getHeight();
        } else {
            pos = getX();
            viewSize = (float) getWidth();
            pdfViewSize = (float) this.pdfView.getWidth();
        }
        this.relativeHandlerMiddle = ((this.relativeHandlerMiddle + pos) / pdfViewSize) * viewSize;
    }

    public void hideDelayed() {
        this.handler.postDelayed(this.hidePageScrollerRunnable, 1000);
    }

    public void setPageNum(int pageNum) {
        String text = String.valueOf(pageNum);
        if (!this.textView.getText().equals(text)) {
            this.textView.setText(text);
        }
    }

    public boolean shown() {
        return getVisibility() == 0;
    }

    public void show() {
        setVisibility(0);
    }

    public void hide() {
        setVisibility(4);
    }

    public void setTextColor(int color) {
        this.textView.setTextColor(color);
    }

    public void setTextSize(int size) {
        this.textView.setTextSize(1, (float) size);
    }

    private boolean isPDFViewReady() {
        return this.pdfView != null && this.pdfView.getPageCount() > 0 && !this.pdfView.documentFitsView();
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!isPDFViewReady()) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case 0:
            case 5:
                this.pdfView.stopFling();
                this.handler.removeCallbacks(this.hidePageScrollerRunnable);
                if (!this.pdfView.isSwipeVertical()) {
                    this.currentPos = event.getRawX() - getX();
                    break;
                } else {
                    this.currentPos = event.getRawY() - getY();
                    break;
                }
            case 1:
            case 3:
            case 6:
                hideDelayed();
                return true;
            case 2:
                break;
            default:
                return super.onTouchEvent(event);
        }
        if (this.pdfView.isSwipeVertical()) {
            setPosition((event.getRawY() - this.currentPos) + this.relativeHandlerMiddle);
            this.pdfView.setPositionOffset(this.relativeHandlerMiddle / ((float) getHeight()), false);
            return true;
        }
        setPosition((event.getRawX() - this.currentPos) + this.relativeHandlerMiddle);
        this.pdfView.setPositionOffset(this.relativeHandlerMiddle / ((float) getWidth()), false);
        return true;
    }
}
