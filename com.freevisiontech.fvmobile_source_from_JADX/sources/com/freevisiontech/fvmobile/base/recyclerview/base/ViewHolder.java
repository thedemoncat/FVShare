package com.freevisiontech.fvmobile.base.recyclerview.base;

import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder {
    private View view;
    private SparseArray<View> viewHolder = new SparseArray<>();

    public static ViewHolder getViewHolder(View view2) {
        ViewHolder viewHolder2 = (ViewHolder) view2.getTag();
        if (viewHolder2 != null) {
            return viewHolder2;
        }
        ViewHolder viewHolder3 = new ViewHolder(view2);
        view2.setTag(viewHolder3);
        return viewHolder3;
    }

    private ViewHolder(View view2) {
        this.view = view2;
        view2.setTag(this.viewHolder);
    }

    public <T extends View> T get(int id) {
        View childView = this.viewHolder.get(id);
        if (childView != null) {
            return childView;
        }
        View childView2 = this.view.findViewById(id);
        this.viewHolder.put(id, childView2);
        return childView2;
    }

    public View getConvertView() {
        return this.view;
    }

    public TextView getTextView(int id) {
        return (TextView) get(id);
    }

    public Button getButton(int id) {
        return (Button) get(id);
    }

    public ImageView getImageView(int id) {
        return (ImageView) get(id);
    }

    public void setTextView(int id, CharSequence charSequence) {
        getTextView(id).setText(charSequence);
    }
}
