package com.google.android.exoplayer.text;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

public final class SubtitleLayout extends View {
    private static final int ABSOLUTE = 2;
    public static final float DEFAULT_BOTTOM_PADDING_FRACTION = 0.08f;
    public static final float DEFAULT_TEXT_SIZE_FRACTION = 0.0533f;
    private static final int FRACTIONAL = 0;
    private static final int FRACTIONAL_IGNORE_PADDING = 1;
    private boolean applyEmbeddedStyles;
    private float bottomPaddingFraction;
    private List<Cue> cues;
    private final List<CuePainter> painters;
    private CaptionStyleCompat style;
    private float textSize;
    private int textSizeType;

    public SubtitleLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public SubtitleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.painters = new ArrayList();
        this.textSizeType = 0;
        this.textSize = 0.0533f;
        this.applyEmbeddedStyles = true;
        this.style = CaptionStyleCompat.DEFAULT;
        this.bottomPaddingFraction = 0.08f;
    }

    public void setCues(List<Cue> cues2) {
        if (this.cues != cues2) {
            this.cues = cues2;
            int cueCount = cues2 == null ? 0 : cues2.size();
            while (this.painters.size() < cueCount) {
                this.painters.add(new CuePainter(getContext()));
            }
            invalidate();
        }
    }

    public void setFixedTextSize(int unit, float size) {
        Resources resources;
        Context context = getContext();
        if (context == null) {
            resources = Resources.getSystem();
        } else {
            resources = context.getResources();
        }
        setTextSize(2, TypedValue.applyDimension(unit, size, resources.getDisplayMetrics()));
    }

    public void setFractionalTextSize(float fractionOfHeight) {
        setFractionalTextSize(fractionOfHeight, false);
    }

    public void setFractionalTextSize(float fractionOfHeight, boolean ignorePadding) {
        setTextSize(ignorePadding ? 1 : 0, fractionOfHeight);
    }

    private void setTextSize(int textSizeType2, float textSize2) {
        if (this.textSizeType != textSizeType2 || this.textSize != textSize2) {
            this.textSizeType = textSizeType2;
            this.textSize = textSize2;
            invalidate();
        }
    }

    public void setApplyEmbeddedStyles(boolean applyEmbeddedStyles2) {
        if (this.applyEmbeddedStyles != applyEmbeddedStyles2) {
            this.applyEmbeddedStyles = applyEmbeddedStyles2;
            invalidate();
        }
    }

    public void setStyle(CaptionStyleCompat style2) {
        if (this.style != style2) {
            this.style = style2;
            invalidate();
        }
    }

    public void setBottomPaddingFraction(float bottomPaddingFraction2) {
        if (this.bottomPaddingFraction != bottomPaddingFraction2) {
            this.bottomPaddingFraction = bottomPaddingFraction2;
            invalidate();
        }
    }

    public void dispatchDraw(Canvas canvas) {
        int cueCount;
        float textSizePx;
        if (this.cues == null) {
            cueCount = 0;
        } else {
            cueCount = this.cues.size();
        }
        int rawTop = getTop();
        int rawBottom = getBottom();
        int left = getLeft() + getPaddingLeft();
        int top = rawTop + getPaddingTop();
        int right = getRight() + getPaddingRight();
        int bottom = rawBottom - getPaddingBottom();
        if (bottom > top && right > left) {
            if (this.textSizeType == 2) {
                textSizePx = this.textSize;
            } else {
                textSizePx = this.textSize * ((float) (this.textSizeType == 0 ? bottom - top : rawBottom - rawTop));
            }
            if (textSizePx > 0.0f) {
                for (int i = 0; i < cueCount; i++) {
                    this.painters.get(i).draw(this.cues.get(i), this.applyEmbeddedStyles, this.style, textSizePx, this.bottomPaddingFraction, canvas, left, top, right, bottom);
                }
            }
        }
    }
}
