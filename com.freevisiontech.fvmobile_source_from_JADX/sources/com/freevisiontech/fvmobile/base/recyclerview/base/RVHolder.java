package com.freevisiontech.fvmobile.base.recyclerview.base;

import android.support.p003v7.widget.RecyclerView;
import android.view.View;

public class RVHolder extends RecyclerView.ViewHolder {
    private ViewHolder viewHolder;

    public RVHolder(View itemView) {
        super(itemView);
        this.viewHolder = ViewHolder.getViewHolder(itemView);
    }

    public ViewHolder getViewHolder() {
        return this.viewHolder;
    }
}
