package com.freevisiontech.fvmobile.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.p003v7.widget.GridLayoutManager;
import android.support.p003v7.widget.RecyclerView;
import android.support.p003v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseRVAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    public static final int TYPE_FOOTER = -1;
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_NORMAL = 0;
    public boolean isCloseItemAnim;
    public boolean isMainItem;
    public Context mContext;
    public List<View> mFooterViews = new ArrayList();
    public List<View> mHeaderViews = new ArrayList();
    public List<T> mList = new ArrayList();
    /* access modifiers changed from: private */
    public OnItemClickListener mOnItemClickListener;
    public int mSpanIndex;

    public interface OnItemClickListener {
        void itemSelect(int i);
    }

    public abstract int getLayoutId(int i);

    public abstract void onBind(BaseViewHolder baseViewHolder, int i);

    public BaseRVAdapter(Context context, @NonNull List<T> list) {
        this.mList = list;
        this.mContext = context;
    }

    public T getData(int position) {
        return this.mList.get(position);
    }

    public void updateAdapter(@NonNull List<T> list) {
        this.mList.clear();
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public void updateData(int position, T object) {
        this.mList.set(position, object);
        if (this.isCloseItemAnim) {
            notifyDataSetChanged();
            return;
        }
        int updateIndex = startIndex(position);
        notifyItemChanged(updateIndex);
        notifyItemRangeChanged(updateIndex, this.mList.size());
    }

    public void cleanData() {
        if (!this.isCloseItemAnim) {
            this.mList.clear();
            notifyDataSetChanged();
            return;
        }
        int cleanIndex = this.mHeaderViews.size();
        notifyItemRangeRemoved(cleanIndex, this.mList.size());
        this.mList.clear();
        notifyItemRangeChanged(cleanIndex, this.mList.size());
    }

    public void removeData(int position) {
        if (this.mList.size() > position) {
            if (this.isCloseItemAnim) {
                this.mList.remove(position);
                notifyDataSetChanged();
                return;
            }
            notifyItemRemoved(startIndex(position));
            this.mList.remove(position);
            notifyItemRangeChanged(startIndex(position), this.mList.size());
        }
    }

    public void addDataLs(int position, @NonNull List<T> list) {
        this.mList.addAll(position, list);
        if (this.isCloseItemAnim) {
            notifyDataSetChanged();
            return;
        }
        int addIndex = startIndex(position);
        notifyItemRangeInserted(addIndex, list.size());
        notifyItemRangeChanged(addIndex, this.mList.size());
    }

    public void addDataLs(@NonNull List<T> list) {
        addDataLs(startIndex(this.mList.size()), list);
    }

    public void addData(int position, T object) {
        int startIndex = startIndex(position);
        this.mList.add(position, object);
        if (this.isCloseItemAnim) {
            notifyDataSetChanged();
            return;
        }
        notifyItemInserted(startIndex);
        notifyItemRangeChanged(startIndex, this.mList.size());
    }

    public void addData(T object) {
        addData(startIndex(this.mList.size()), object);
    }

    public void addHeaderViews(@NonNull List<View> headerViews) {
        this.mHeaderViews.addAll(headerViews);
        notifyDataSetChanged();
    }

    public void addHeaderView(@NonNull View headerView) {
        this.mHeaderViews.add(headerView);
        notifyDataSetChanged();
    }

    public void addFooterViews(@NonNull List<View> footerViews) {
        this.mFooterViews.addAll(footerViews);
        notifyDataSetChanged();
    }

    public void addFooterView(@NonNull View footerView) {
        this.mFooterViews.add(footerView);
        notifyDataSetChanged();
    }

    private int startIndex(int doneIndex) {
        return this.mHeaderViews.size() + doneIndex;
    }

    public int getItemViewType(int position) {
        if (!this.mHeaderViews.isEmpty() && position < this.mHeaderViews.size()) {
            return position + 1;
        }
        if (this.mFooterViews.isEmpty() || position < this.mHeaderViews.size() + this.mList.size()) {
            return 0;
        }
        return -1 - (position - (this.mHeaderViews.size() + this.mList.size()));
    }

    public int getItemCount() {
        int i = 0;
        if (this.mList != null && !this.mList.isEmpty()) {
            return this.mHeaderViews.size() + this.mList.size() + this.mFooterViews.size();
        }
        if (this.mHeaderViews == null) {
            return 0;
        }
        int size = this.mHeaderViews.size();
        if (this.mFooterViews != null) {
            i = this.mFooterViews.size();
        }
        return i + size;
    }

    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (!this.mHeaderViews.isEmpty() && viewType > 0) {
            return new BaseViewHolder(this.mHeaderViews.get(viewType - 1));
        }
        if (this.mFooterViews.isEmpty() || viewType > -1) {
            return new BaseViewHolder(LayoutInflater.from(this.mContext).inflate(getLayoutId(viewType), parent, false));
        }
        return new BaseViewHolder(this.mFooterViews.get((-viewType) - 1));
    }

    public void onBindViewHolder(final BaseViewHolder holder, int position) {
        if (getItemViewType(position) == 0) {
            onBind(holder, position - this.mHeaderViews.size());
            if (this.mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        BaseRVAdapter.this.mOnItemClickListener.itemSelect(holder.getAdapterPosition() - BaseRVAdapter.this.mHeaderViews.size());
                    }
                });
            }
        }
    }

    public void onViewRecycled(BaseViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = (GridLayoutManager) manager;
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                public int getSpanSize(int position) {
                    if (BaseRVAdapter.this.getItemViewType(position) != 0) {
                        return gridManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
    }

    public void onViewAttachedToWindow(BaseViewHolder holder) {
        boolean z;
        boolean z2 = true;
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null) {
            if (getItemViewType(holder.getLayoutPosition()) == 0) {
                z = true;
            } else {
                z = false;
            }
            this.isMainItem = z;
            if (lp instanceof GridLayoutManager.LayoutParams) {
                this.mSpanIndex = ((GridLayoutManager.LayoutParams) lp).getSpanIndex();
            } else if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                this.mSpanIndex = p.getSpanIndex();
                if (this.isMainItem) {
                    z2 = false;
                }
                p.setFullSpan(z2);
            }
        }
    }

    public void addItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
