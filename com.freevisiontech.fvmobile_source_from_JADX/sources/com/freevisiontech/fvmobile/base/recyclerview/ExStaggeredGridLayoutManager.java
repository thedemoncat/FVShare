package com.freevisiontech.fvmobile.base.recyclerview;

import android.support.p003v7.widget.RecyclerView;
import android.support.p003v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.exoplayer.C1907C;

public class ExStaggeredGridLayoutManager extends StaggeredGridLayoutManager {
    private int[] dimension;
    private int[] measuredDimension = new int[2];

    public ExStaggeredGridLayoutManager(int spanCount, int orientation) {
        super(spanCount, orientation);
    }

    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        int widthMode = View.MeasureSpec.getMode(widthSpec);
        int widthSize = View.MeasureSpec.getSize(widthSpec);
        int heightMode = View.MeasureSpec.getMode(heightSpec);
        int heightSize = View.MeasureSpec.getSize(heightSpec);
        int width = 0;
        int height = 0;
        int count = getItemCount();
        this.dimension = new int[getSpanCount()];
        for (int i = 0; i < count; i++) {
            measureScrapChild(recycler, i, View.MeasureSpec.makeMeasureSpec(i, 0), View.MeasureSpec.makeMeasureSpec(i, 0), this.measuredDimension);
            if (getOrientation() == 1) {
                int[] iArr = this.dimension;
                int findMinIndex = findMinIndex(this.dimension);
                iArr[findMinIndex] = iArr[findMinIndex] + this.measuredDimension[1];
            } else {
                int[] iArr2 = this.dimension;
                int findMinIndex2 = findMinIndex(this.dimension);
                iArr2[findMinIndex2] = iArr2[findMinIndex2] + this.measuredDimension[0];
            }
        }
        if (getOrientation() == 1) {
            height = findMax(this.dimension);
        } else {
            width = findMax(this.dimension);
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

    private void measureScrapChild(RecyclerView.Recycler recycler, int position, int widthSpec, int heightSpec, int[] measuredDimension2) {
        if (position < getItemCount()) {
            try {
                View view = recycler.getViewForPosition(position);
                if (view != null) {
                    RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
                    view.measure(ViewGroup.getChildMeasureSpec(widthSpec, getPaddingLeft() + getPaddingRight(), lp.width), ViewGroup.getChildMeasureSpec(heightSpec, getPaddingTop() + getPaddingBottom(), lp.height));
                    measuredDimension2[0] = view.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
                    measuredDimension2[1] = view.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
                    recycler.recycleView(view);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private int findMax(int[] array) {
        int max = array[0];
        for (int value : array) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private int findMinIndex(int[] array) {
        int index = 0;
        int min = array[0];
        for (int i = 0; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
                index = i;
            }
        }
        return index;
    }
}
