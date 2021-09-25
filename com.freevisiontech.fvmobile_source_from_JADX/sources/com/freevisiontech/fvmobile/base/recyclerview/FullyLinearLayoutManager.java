package com.freevisiontech.fvmobile.base.recyclerview;

import android.content.Context;
import android.support.p003v7.widget.LinearLayoutManager;
import android.support.p003v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.exoplayer.C1907C;

public class FullyLinearLayoutManager extends LinearLayoutManager {
    private static final String TAG = FullyLinearLayoutManager.class.getSimpleName();
    private int[] mMeasuredDimension = new int[2];

    public FullyLinearLayoutManager(Context context) {
        super(context);
    }

    public FullyLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        int widthMode = View.MeasureSpec.getMode(widthSpec);
        int heightMode = View.MeasureSpec.getMode(heightSpec);
        int widthSize = View.MeasureSpec.getSize(widthSpec);
        int heightSize = View.MeasureSpec.getSize(heightSpec);
        int width = 0;
        int height = 0;
        for (int i = 0; i < getItemCount(); i++) {
            measureScrapChild(recycler, i, View.MeasureSpec.makeMeasureSpec(i, 0), View.MeasureSpec.makeMeasureSpec(i, 0), this.mMeasuredDimension);
            if (getOrientation() == 0) {
                width += this.mMeasuredDimension[0];
                if (i == 0) {
                    height = this.mMeasuredDimension[1];
                }
            } else {
                height += this.mMeasuredDimension[1];
                if (i == 0) {
                    width = this.mMeasuredDimension[0];
                }
            }
        }
        switch (widthMode) {
            case C1907C.ENCODING_PCM_32BIT /*1073741824*/:
                width = widthSize;
                break;
        }
        switch (heightMode) {
            case C1907C.ENCODING_PCM_32BIT /*1073741824*/:
                height = heightSize;
                break;
        }
        setMeasuredDimension(width, height);
    }

    private void measureScrapChild(RecyclerView.Recycler recycler, int position, int widthSpec, int heightSpec, int[] measuredDimension) {
        try {
            View view = recycler.getViewForPosition(0);
            if (view != null) {
                RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view.getLayoutParams();
                view.measure(ViewGroup.getChildMeasureSpec(widthSpec, getPaddingLeft() + getPaddingRight(), p.width), ViewGroup.getChildMeasureSpec(heightSpec, getPaddingTop() + getPaddingBottom(), p.height));
                measuredDimension[0] = view.getMeasuredWidth() + p.leftMargin + p.rightMargin;
                measuredDimension[1] = view.getMeasuredHeight() + p.bottomMargin + p.topMargin;
                recycler.recycleView(view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
