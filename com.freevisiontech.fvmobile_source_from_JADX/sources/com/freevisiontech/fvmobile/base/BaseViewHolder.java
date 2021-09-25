package com.freevisiontech.fvmobile.base;

import android.graphics.Bitmap;
import android.support.p003v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class BaseViewHolder extends RecyclerView.ViewHolder {
    private View mConvertView;
    private SparseArray<View> mViews;

    public BaseViewHolder(View view) {
        super(view);
        this.mViews = new SparseArray<>();
        this.mViews = new SparseArray<>();
        this.mConvertView = this.itemView;
        this.mConvertView.setTag(this.mViews);
    }

    public <T extends View> T getView(int viewId) {
        View view = this.mViews.get(viewId);
        if (view != null) {
            return view;
        }
        View view2 = this.mConvertView.findViewById(viewId);
        this.mViews.put(viewId, view2);
        return view2;
    }

    public ImageView getImageView(int id) {
        return (ImageView) getView(id);
    }

    public TextView getTextView(int id) {
        return (TextView) getView(id);
    }

    public Button getButton(int id) {
        return (Button) getView(id);
    }

    public void setImageResource(int id, int resId) {
        getImageView(id).setImageResource(resId);
    }

    public void setImageResource(int id, Bitmap bmp) {
        getImageView(id).setImageBitmap(bmp);
    }

    public void setTextView(int id, CharSequence charSequence) {
        getTextView(id).setText(charSequence + "");
    }

    public void setVisibility(int id, boolean isVisibility) {
        getView(id).setVisibility(isVisibility ? 0 : 8);
    }

    public void setCheckStats(int id, boolean isCheck) {
        if (getView(id) instanceof CheckBox) {
            ((CheckBox) getView(id)).setChecked(isCheck);
        } else if (getView(id) instanceof RadioButton) {
            ((RadioButton) getView(id)).setChecked(isCheck);
        }
    }
}
