package com.freevisiontech.fvmobile.base.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.p001v4.content.ContextCompat;
import android.support.p003v7.widget.RecyclerView;
import android.view.View;

public class RecycleViewDivider extends RecyclerView.ItemDecoration {
    private static final int[] ATTRS = {16843284};
    private Drawable mDivider;
    private int mDividerHeight;
    private int mOrientation;
    private Paint mPaint;

    public RecycleViewDivider(Context context, int orientation) {
        this.mDividerHeight = 2;
        if (orientation == 1 || orientation == 0) {
            this.mOrientation = orientation;
            TypedArray a = context.obtainStyledAttributes(ATTRS);
            this.mDivider = a.getDrawable(0);
            a.recycle();
            return;
        }
        throw new IllegalArgumentException("请输入正确的参数！");
    }

    public RecycleViewDivider(Context context, int orientation, int drawableId) {
        this(context, orientation);
        this.mDivider = ContextCompat.getDrawable(context, drawableId);
        this.mDividerHeight = this.mDivider.getIntrinsicHeight();
    }

    public RecycleViewDivider(Context context, int orientation, int dividerHeight, int dividerColor) {
        this(context, orientation);
        this.mDividerHeight = dividerHeight;
        this.mPaint = new Paint(1);
        this.mPaint.setColor(dividerColor);
        this.mPaint.setStyle(Paint.Style.FILL);
    }

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(0, 0, 0, this.mDividerHeight);
    }

    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (this.mOrientation == 1) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        int left = parent.getPaddingLeft();
        int right = parent.getMeasuredWidth() - parent.getPaddingRight();
        int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            View child = parent.getChildAt(i);
            int top = child.getBottom() + ((RecyclerView.LayoutParams) child.getLayoutParams()).bottomMargin;
            int bottom = top + this.mDividerHeight;
            if (this.mDivider != null) {
                this.mDivider.setBounds(left, top, right, bottom);
                this.mDivider.draw(canvas);
            }
            if (this.mPaint != null) {
                canvas.drawRect((float) left, (float) top, (float) right, (float) bottom, this.mPaint);
            }
        }
    }

    private void drawVertical(Canvas canvas, RecyclerView parent) {
        int top = parent.getPaddingTop();
        int bottom = parent.getMeasuredHeight() - parent.getPaddingBottom();
        int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            View child = parent.getChildAt(i);
            int left = child.getRight() + ((RecyclerView.LayoutParams) child.getLayoutParams()).rightMargin;
            int right = left + this.mDividerHeight;
            if (this.mDivider != null) {
                this.mDivider.setBounds(left, top, right, bottom);
                this.mDivider.draw(canvas);
            }
            if (this.mPaint != null) {
                canvas.drawRect((float) left, (float) top, (float) right, (float) bottom, this.mPaint);
            }
        }
    }
}
