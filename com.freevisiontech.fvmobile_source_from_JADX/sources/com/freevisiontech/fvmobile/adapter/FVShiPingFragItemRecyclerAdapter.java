package com.freevisiontech.fvmobile.adapter;

import android.content.Context;
import android.support.p003v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.widget.view.IntentKey;
import java.util.List;

public class FVShiPingFragItemRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<String> list;
    private LayoutInflater mLayoutInflater;
    private String worth;

    public FVShiPingFragItemRecyclerAdapter(Context context2, List<String> list2) {
        this.context = context2;
        this.list = list2;
        this.mLayoutInflater = LayoutInflater.from(context2);
    }

    public FVShiPingFragItemRecyclerAdapter(Context context2) {
        this.context = context2;
        this.mLayoutInflater = LayoutInflater.from(context2);
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(this.mLayoutInflater.inflate(C0853R.layout.recycle_item_grid_item, parent, false));
    }

    public void onBindViewHolder(RecyclerView.ViewHolder hd, int position) {
        Glide.with(this.context).load(IntentKey.FILE_PATH + this.list.get(position)).into(((MyViewHolder) hd).recycler_item_image);
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
        public final ImageView recycler_item_image;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.recycler_item_image = (ImageView) itemView.findViewById(C0853R.C0855id.iv_recycle_item_image);
        }
    }
}
