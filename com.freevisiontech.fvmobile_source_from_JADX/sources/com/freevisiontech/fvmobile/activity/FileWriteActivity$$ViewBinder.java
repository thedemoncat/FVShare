package com.freevisiontech.fvmobile.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.activity.FileWriteActivity;

public class FileWriteActivity$$ViewBinder<T extends FileWriteActivity> implements ButterKnife.ViewBinder<T> {
    public void bind(ButterKnife.Finder finder, T target, Object source) {
        target.img_file_back = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.img_file_back, "field 'img_file_back'"), C0853R.C0855id.img_file_back, "field 'img_file_back'");
        target.tv_file_center_title = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_file_center_title, "field 'tv_file_center_title'"), C0853R.C0855id.tv_file_center_title, "field 'tv_file_center_title'");
        target.tv_all_right = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_all_right, "field 'tv_all_right'"), C0853R.C0855id.tv_all_right, "field 'tv_all_right'");
        target.tv_file_right_cancel = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_file_right_cancel, "field 'tv_file_right_cancel'"), C0853R.C0855id.tv_file_right_cancel, "field 'tv_file_right_cancel'");
        target.tv_file_right_select = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_file_right_select, "field 'tv_file_right_select'"), C0853R.C0855id.tv_file_right_select, "field 'tv_file_right_select'");
    }

    public void unbind(T target) {
        target.img_file_back = null;
        target.tv_file_center_title = null;
        target.tv_all_right = null;
        target.tv_file_right_cancel = null;
        target.tv_file_right_select = null;
    }
}
