package com.freevisiontech.fvmobile.base.recyclerview.base;

import android.content.Context;
import android.support.p003v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import java.util.List;

public abstract class AutoRVAdapter extends RecyclerView.Adapter<RVHolder> {
    private Context context;
    public List<?> list;
    /* access modifiers changed from: private */
    public AdapterView.OnItemClickListener onItemClickListener;

    public abstract void onBindViewHolder(ViewHolder viewHolder, int i);

    public abstract int onCreateViewLayoutID(int i);

    public AutoRVAdapter(Context context2, List<?> list2) {
        this.context = context2;
        this.list = list2;
    }

    public RVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RVHolder(LayoutInflater.from(this.context).inflate(onCreateViewLayoutID(viewType), (ViewGroup) null));
    }

    public void onViewRecycled(RVHolder holder) {
        super.onViewRecycled(holder);
    }

    public void onBindViewHolder(final RVHolder holder, int position) {
        onBindViewHolder(holder.getViewHolder(), position);
        if (this.onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    AutoRVAdapter.this.onItemClickListener.onItemClick((AdapterView) null, v, holder.getLayoutPosition(), holder.getItemId());
                }
            });
        }
    }

    public int getItemCount() {
        if (this.list == null) {
            return 0;
        }
        return this.list.size();
    }

    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return this.onItemClickListener;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener2) {
        this.onItemClickListener = onItemClickListener2;
    }
}
