package com.freevisiontech.fvmobile.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.p001v4.content.ContextCompat;
import android.support.p003v7.widget.GridLayoutManager;
import android.support.p003v7.widget.LinearLayoutManager;
import android.support.p003v7.widget.RecyclerView;
import android.support.p003v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

public class RecycleViewDivider extends RecyclerView.ItemDecoration {
    private static final int[] ATTRS = {16843284};
    private static final String TAG = "RecycleViewDivider";
    private boolean cut_off_foot;
    private boolean is_draw_foot_line;
    private Drawable mDivider;
    private int mDividerHeight;
    private int mOrientation;
    private Paint mPaint;
    private int marginLeft;
    private int marginRight;
    private boolean start_draw_foot;

    public void setCut_off_foot(boolean cut_off_foot2) {
        this.cut_off_foot = cut_off_foot2;
    }

    public boolean is_draw_foot_line() {
        return this.is_draw_foot_line;
    }

    public void setIs_draw_foot_line(boolean is_draw_foot_line2) {
        this.is_draw_foot_line = is_draw_foot_line2;
    }

    public int getMarginLeft() {
        return this.marginLeft;
    }

    public void setMarginLeft(int marginLeft2) {
        this.marginLeft = marginLeft2;
    }

    public int getMarginRight() {
        return this.marginRight;
    }

    public void setMarginRight(int marginRight2) {
        this.marginRight = marginRight2;
    }

    public RecycleViewDivider(Context context, int orientation) {
        this.mDividerHeight = 2;
        this.cut_off_foot = false;
        this.marginLeft = 0;
        this.marginRight = 0;
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

    private int getSpanCount(RecyclerView parent) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            return ((GridLayoutManager) layoutManager).getSpanCount();
        }
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return -1;
    }

    private int getSpanIndex(RecyclerView.Adapter adapter) {
        if (adapter instanceof BaseRVAdapter) {
            return ((BaseRVAdapter) adapter).mSpanIndex + 1;
        }
        return 0;
    }

    private boolean getItemState(RecyclerView.Adapter adapter) {
        if (adapter instanceof BaseRVAdapter) {
            return ((BaseRVAdapter) adapter).isMainItem;
        }
        return true;
    }

    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        boolean isLastRaw;
        boolean isLastColum;
        int intrinsicWidth;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        int cut_off = 0;
        if (this.cut_off_foot) {
            cut_off = 1;
        }
        int spanCount = getSpanCount(parent);
        if (isVertical(layoutManager)) {
            isLastRaw = itemPosition + cut_off >= getMaxRaw(parent.getAdapter(), spanCount);
        } else {
            isLastRaw = getSpanIndex(parent.getAdapter()) == spanCount;
        }
        if (isVertical(layoutManager)) {
            isLastColum = spanCount == getSpanIndex(parent.getAdapter());
        } else {
            isLastColum = itemPosition + cut_off >= getMaxRaw(parent.getAdapter(), spanCount);
        }
        if (isLastRaw) {
            this.start_draw_foot = true;
        } else {
            this.start_draw_foot = false;
        }
        if (getItemState(parent.getAdapter())) {
            if (isLastColum) {
                intrinsicWidth = 0;
            } else {
                intrinsicWidth = this.mDivider.getIntrinsicWidth();
            }
            outRect.set(0, 0, intrinsicWidth, isLastRaw ? 0 : this.mDividerHeight);
        }
    }

    private boolean isVertical(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) {
            if (((GridLayoutManager) layoutManager).getOrientation() == 1) {
                return true;
            }
            return false;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            if (((StaggeredGridLayoutManager) layoutManager).getOrientation() != 1) {
                return false;
            }
            return true;
        } else if (!(layoutManager instanceof LinearLayoutManager) || ((LinearLayoutManager) layoutManager).getOrientation() == 1) {
            return true;
        } else {
            return false;
        }
    }

    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (this.mOrientation == 1) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    private int getMaxRaw(RecyclerView.Adapter adapter, int spanCount) {
        int childCount = adapter.getItemCount();
        int maxRawSize = childCount - (childCount % spanCount);
        if (!(adapter instanceof BaseRVAdapter)) {
            return maxRawSize;
        }
        BaseRVAdapter baseRVAdapter = (BaseRVAdapter) adapter;
        if (baseRVAdapter.mFooterViews == null || baseRVAdapter.mHeaderViews == null) {
            return maxRawSize;
        }
        int childCount2 = baseRVAdapter.mList.size();
        return (childCount2 - (childCount2 % spanCount)) + baseRVAdapter.mHeaderViews.size();
    }

    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        int left = parent.getPaddingLeft() + this.marginLeft;
        int right = (parent.getMeasuredWidth() - parent.getPaddingRight()) - this.marginRight;
        int childSize = parent.getChildCount();
        if (!this.is_draw_foot_line && this.start_draw_foot && (parent.getAdapter() instanceof BaseRVAdapter)) {
            childSize -= ((BaseRVAdapter) parent.getAdapter()).mFooterViews.size();
        }
        Log.e(TAG, "getItemOffsets: ");
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
