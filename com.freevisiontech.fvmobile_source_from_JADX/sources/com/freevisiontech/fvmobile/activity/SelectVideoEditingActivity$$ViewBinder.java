package com.freevisiontech.fvmobile.activity;

import android.support.p003v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.activity.SelectVideoEditingActivity;

public class SelectVideoEditingActivity$$ViewBinder<T extends SelectVideoEditingActivity> implements ButterKnife.ViewBinder<T> {
    public void bind(ButterKnife.Finder finder, T target, Object source) {
        target.img_file_back = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.img_file_back, "field 'img_file_back'"), C0853R.C0855id.img_file_back, "field 'img_file_back'");
        target.tv_file_center_title = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_file_center_title, "field 'tv_file_center_title'"), C0853R.C0855id.tv_file_center_title, "field 'tv_file_center_title'");
        target.tv_all_right = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_all_right, "field 'tv_all_right'"), C0853R.C0855id.tv_all_right, "field 'tv_all_right'");
        target.tv_file_right_cancel = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_file_right_cancel, "field 'tv_file_right_cancel'"), C0853R.C0855id.tv_file_right_cancel, "field 'tv_file_right_cancel'");
        target.tv_file_right_select = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_file_right_select, "field 'tv_file_right_select'"), C0853R.C0855id.tv_file_right_select, "field 'tv_file_right_select'");
        target.fragment_file_shiping_create = (LinearLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.fragment_file_shiping_create, "field 'fragment_file_shiping_create'"), C0853R.C0855id.fragment_file_shiping_create, "field 'fragment_file_shiping_create'");
        target.fragment_recycler_shiping = (RecyclerView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.fragment_recycler_shiping, "field 'fragment_recycler_shiping'"), C0853R.C0855id.fragment_recycler_shiping, "field 'fragment_recycler_shiping'");
        target.tv_confirm = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_confirm, "field 'tv_confirm'"), C0853R.C0855id.tv_confirm, "field 'tv_confirm'");
    }

    public void unbind(T target) {
        target.img_file_back = null;
        target.tv_file_center_title = null;
        target.tv_all_right = null;
        target.tv_file_right_cancel = null;
        target.tv_file_right_select = null;
        target.fragment_file_shiping_create = null;
        target.fragment_recycler_shiping = null;
        target.tv_confirm = null;
    }
}
