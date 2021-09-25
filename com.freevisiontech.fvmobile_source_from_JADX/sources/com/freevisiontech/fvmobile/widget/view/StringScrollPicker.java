package com.freevisiontech.fvmobile.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.utility.ColorUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringScrollPicker extends ScrollPickerView<CharSequence> {
    private Layout.Alignment mAlignment;
    private int mEndColor;
    private int mMaxLineWidth;
    private int mMaxTextSize;
    private int mMeasureHeight;
    private int mMeasureWidth;
    private int mMinTextSize;
    private TextPaint mPaint;
    private int mStartColor;

    public StringScrollPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StringScrollPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mMinTextSize = 24;
        this.mMaxTextSize = 32;
        this.mStartColor = getResources().getColor(C0853R.color.color_black4);
        this.mEndColor = getResources().getColor(C0853R.color.color_light_gray);
        this.mMaxLineWidth = -1;
        this.mAlignment = Layout.Alignment.ALIGN_CENTER;
        this.mPaint = new TextPaint(1);
        this.mPaint.setStyle(Paint.Style.FILL);
        this.mPaint.setColor(-16777216);
        init(attrs);
        setData(new ArrayList(Arrays.asList(new String[]{"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven", "twelve"})));
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, C0853R.styleable.StringScrollPicker);
            this.mMinTextSize = typedArray.getDimensionPixelSize(0, this.mMinTextSize);
            this.mMaxTextSize = typedArray.getDimensionPixelSize(1, this.mMaxTextSize);
            this.mStartColor = typedArray.getColor(2, this.mStartColor);
            this.mEndColor = typedArray.getColor(3, this.mEndColor);
            this.mMaxLineWidth = typedArray.getDimensionPixelSize(4, this.mMaxLineWidth);
            int align = typedArray.getInt(5, 1);
            if (align == 2) {
                this.mAlignment = Layout.Alignment.ALIGN_NORMAL;
            } else if (align == 3) {
                this.mAlignment = Layout.Alignment.ALIGN_OPPOSITE;
            } else {
                this.mAlignment = Layout.Alignment.ALIGN_CENTER;
            }
            typedArray.recycle();
        }
    }

    public void setColor(int startColor, int endColor) {
        this.mStartColor = startColor;
        this.mEndColor = endColor;
        invalidate();
    }

    public void setTextSize(int minText, int maxText) {
        this.mMinTextSize = minText;
        this.mMaxTextSize = maxText;
        invalidate();
    }

    public int getStartColor() {
        return this.mStartColor;
    }

    public int getEndColor() {
        return this.mEndColor;
    }

    public int getMinTextSize() {
        return this.mMinTextSize;
    }

    public int getMaxTextSize() {
        return this.mMaxTextSize;
    }

    public int getMaxLineWidth() {
        return this.mMaxLineWidth;
    }

    public void setMaxLineWidth(int maxLineWidth) {
        this.mMaxLineWidth = maxLineWidth;
    }

    public Layout.Alignment getAlignment() {
        return this.mAlignment;
    }

    public void setAlignment(Layout.Alignment alignment) {
        this.mAlignment = alignment;
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mMeasureWidth = getMeasuredWidth();
        this.mMeasureHeight = getMeasuredHeight();
        if (this.mMaxLineWidth < 0) {
            this.mMaxLineWidth = getItemWidth();
        }
    }

    public void drawItem(Canvas canvas, List<CharSequence> data, int position, int relative, float moveLength, float top) {
        float x;
        float y;
        CharSequence text = data.get(position);
        int itemSize = getItemSize();
        if (relative == -1) {
            if (moveLength < 0.0f) {
                this.mPaint.setTextSize((float) this.mMinTextSize);
            } else {
                this.mPaint.setTextSize(((float) this.mMinTextSize) + ((((float) (this.mMaxTextSize - this.mMinTextSize)) * moveLength) / ((float) itemSize)));
            }
        } else if (relative == 0) {
            this.mPaint.setTextSize(((float) this.mMinTextSize) + ((((float) (this.mMaxTextSize - this.mMinTextSize)) * (((float) itemSize) - Math.abs(moveLength))) / ((float) itemSize)));
        } else if (relative != 1) {
            this.mPaint.setTextSize((float) this.mMinTextSize);
        } else if (moveLength > 0.0f) {
            this.mPaint.setTextSize((float) this.mMinTextSize);
        } else {
            this.mPaint.setTextSize(((float) this.mMinTextSize) + ((((float) (this.mMaxTextSize - this.mMinTextSize)) * (-moveLength)) / ((float) itemSize)));
        }
        StaticLayout layout = new StaticLayout(text, 0, text.length(), this.mPaint, this.mMaxLineWidth, this.mAlignment, 1.0f, 0.0f, true, (TextUtils.TruncateAt) null, 0);
        float lineWidth = (float) layout.getWidth();
        if (isHorizontal()) {
            x = top + ((((float) getItemWidth()) - lineWidth) / 2.0f);
            y = (float) ((getItemHeight() - layout.getHeight()) / 2);
        } else {
            x = (((float) getItemWidth()) - lineWidth) / 2.0f;
            y = top + ((float) ((getItemHeight() - layout.getHeight()) / 2));
        }
        computeColor(relative, itemSize, moveLength);
        canvas.save();
        canvas.translate(x, y);
        layout.draw(canvas);
        canvas.restore();
    }

    private void computeColor(int relative, int itemSize, float moveLength) {
        int color = this.mEndColor;
        if (relative == -1 || relative == 1) {
            if ((relative != -1 || moveLength >= 0.0f) && (relative != 1 || moveLength <= 0.0f)) {
                color = ColorUtil.computeGradientColor(this.mStartColor, this.mEndColor, (((float) itemSize) - Math.abs(moveLength)) / ((float) itemSize));
            } else {
                color = this.mEndColor;
            }
        } else if (relative == 0) {
            color = ColorUtil.computeGradientColor(this.mStartColor, this.mEndColor, Math.abs(moveLength) / ((float) itemSize));
        }
        this.mPaint.setColor(color);
    }
}
