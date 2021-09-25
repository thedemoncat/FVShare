package com.freevisiontech.fvmobile.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseLGAdapter<T> extends BaseAdapter {
    private Context mContext;
    private List<T> mList = new ArrayList();

    /* access modifiers changed from: protected */
    public abstract int getLayoutId();

    /* access modifiers changed from: protected */
    public abstract void onBind(BaseViewHolder baseViewHolder, int i);

    public BaseLGAdapter(Context context, List<T> list) {
        this.mContext = context;
        this.mList = list;
    }

    public void updateAdapter(@NonNull List<T> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    public void cleanAdapter() {
        this.mList.clear();
        notifyDataSetChanged();
    }

    public void addData(int position, T data) {
        this.mList.add(position, data);
        notifyDataSetChanged();
    }

    public void addDataLs(@NonNull List<T> list) {
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public void addDataLs(int position, @NonNull List<T> list) {
        this.mList.addAll(position, list);
        notifyDataSetChanged();
    }

    public void removeData(int position) {
        if (this.mList.size() > position) {
            this.mList.remove(position);
            notifyDataSetChanged();
        }
    }

    public void removeData(T data) {
        this.mList.remove(data);
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.mList.size();
    }

    public T getItem(int position) {
        if (this.mList.size() > position) {
            return this.mList.get(position);
        }
        return null;
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(this.mContext).inflate(getLayoutId(), parent, false);
        onBind(new BaseViewHolder(view), position);
        return view;
    }
}
