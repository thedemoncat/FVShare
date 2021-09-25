package com.freevisiontech.fvmobile.activity;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.activity.LookPhotoActivity;

public class LookPhotoActivity$$ViewBinder<T extends LookPhotoActivity> implements ButterKnife.ViewBinder<T> {
    public void bind(ButterKnife.Finder finder, T target, Object source) {
        target.img_back = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.img_back, "field 'img_back'"), C0853R.C0855id.img_back, "field 'img_back'");
        target.img_right = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.img_right, "field 'img_right'"), C0853R.C0855id.img_right, "field 'img_right'");
        target.text_main_delete = (ImageButton) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.text_main_delete, "field 'text_main_delete'"), C0853R.C0855id.text_main_delete, "field 'text_main_delete'");
        target.text_main_share = (ImageButton) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.text_main_share, "field 'text_main_share'"), C0853R.C0855id.text_main_share, "field 'text_main_share'");
        target.text_main_chuangzuo = (ImageButton) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.text_main_chuangzuo, "field 'text_main_chuangzuo'"), C0853R.C0855id.text_main_chuangzuo, "field 'text_main_chuangzuo'");
        target.act_look_photo_title = (RelativeLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.act_look_photo_title, "field 'act_look_photo_title'"), C0853R.C0855id.act_look_photo_title, "field 'act_look_photo_title'");
        target.act_layout_file_buttom = (LinearLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.act_layout_file_buttom, "field 'act_layout_file_buttom'"), C0853R.C0855id.act_layout_file_buttom, "field 'act_layout_file_buttom'");
        target.tv_center_title = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_center_title, "field 'tv_center_title'"), C0853R.C0855id.tv_center_title, "field 'tv_center_title'");
    }

    public void unbind(T target) {
        target.img_back = null;
        target.img_right = null;
        target.text_main_delete = null;
        target.text_main_share = null;
        target.text_main_chuangzuo = null;
        target.act_look_photo_title = null;
        target.act_layout_file_buttom = null;
        target.tv_center_title = null;
    }
}
