package com.freevisiontech.fvmobile.adapter;

import android.content.Context;
import android.support.p003v7.widget.GridLayoutManager;
import android.support.p003v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.base.recyclerview.base.BaseRecyclerAdapter;
import java.util.List;

public class FVShiPingFragRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<String> list;
    private LayoutInflater mLayoutInflater;
    private String worth;

    public FVShiPingFragRecyclerAdapter(Context context2, List<String> list2) {
        this.context = context2;
        this.list = list2;
        this.mLayoutInflater = LayoutInflater.from(context2);
    }

    public FVShiPingFragRecyclerAdapter(Context context2) {
        this.context = context2;
        this.mLayoutInflater = LayoutInflater.from(context2);
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(this.mLayoutInflater.inflate(C0853R.layout.recycle_item_zhaopian, parent, false));
    }

    public void onBindViewHolder(RecyclerView.ViewHolder hd, int position) {
        MyViewHolder holder = (MyViewHolder) hd;
        holder.recycler_item.setLayoutManager(new GridLayoutManager(holder.recycler_item.getContext(), 3));
        FVShiPingFragItemRecyclerAdapter adapter = new FVShiPingFragItemRecyclerAdapter(holder.recycler_item.getContext());
        holder.recycler_item.setAdapter(new BaseRecyclerAdapter(adapter));
        adapter.setData(this.list);
        adapter.notifyDataSetChanged();
    }

    public void setData(List<String> list2) {
        this.list = list2;
    }

    public int getItemCount() {
        if (this.list == null) {
            return 0;
        }
        return this.list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        /* access modifiers changed from: private */
        public final RecyclerView recycler_item;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.recycler_item = (RecyclerView) itemView.findViewById(C0853R.C0855id.zhaopian_item_recycle_grid);
        }
    }
}
