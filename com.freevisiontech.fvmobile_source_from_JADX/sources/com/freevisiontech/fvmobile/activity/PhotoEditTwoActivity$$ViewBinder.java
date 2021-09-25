package com.freevisiontech.fvmobile.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.activity.PhotoEditTwoActivity;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageView;

public class PhotoEditTwoActivity$$ViewBinder<T extends PhotoEditTwoActivity> implements ButterKnife.ViewBinder<T> {
    public void bind(ButterKnife.Finder finder, T target, Object source) {
        target.act_photo_edit_imageview = (GPUImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.act_photo_edit_imageview, "field 'act_photo_edit_imageview'"), C0853R.C0855id.act_photo_edit_imageview, "field 'act_photo_edit_imageview'");
        target.act_photo_edit_jiazai = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.act_photo_edit_jiazai, "field 'act_photo_edit_jiazai'"), C0853R.C0855id.act_photo_edit_jiazai, "field 'act_photo_edit_jiazai'");
        target.act_edit_buttom_meiyan_layout = (LinearLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.act_edit_buttom_meiyan_layout, "field 'act_edit_buttom_meiyan_layout'"), C0853R.C0855id.act_edit_buttom_meiyan_layout, "field 'act_edit_buttom_meiyan_layout'");
        target.act_edit_buttom_meiyan_dismiss = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.act_edit_buttom_meiyan_dismiss, "field 'act_edit_buttom_meiyan_dismiss'"), C0853R.C0855id.act_edit_buttom_meiyan_dismiss, "field 'act_edit_buttom_meiyan_dismiss'");
        target.edit_meiyan_seekbar = (SeekBar) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.edit_meiyan_seekbar, "field 'edit_meiyan_seekbar'"), C0853R.C0855id.edit_meiyan_seekbar, "field 'edit_meiyan_seekbar'");
        target.edit_meiyan_seekbar_bili = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.edit_meiyan_seekbar_bili, "field 'edit_meiyan_seekbar_bili'"), C0853R.C0855id.edit_meiyan_seekbar_bili, "field 'edit_meiyan_seekbar_bili'");
        target.act_edit_buttom_meiyan_ok = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.act_edit_buttom_meiyan_ok, "field 'act_edit_buttom_meiyan_ok'"), C0853R.C0855id.act_edit_buttom_meiyan_ok, "field 'act_edit_buttom_meiyan_ok'");
        target.act_photo_edit_imageview_linear = (LinearLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.act_photo_edit_imageview_linear, "field 'act_photo_edit_imageview_linear'"), C0853R.C0855id.act_photo_edit_imageview_linear, "field 'act_photo_edit_imageview_linear'");
    }

    public void unbind(T target) {
        target.act_photo_edit_imageview = null;
        target.act_photo_edit_jiazai = null;
        target.act_edit_buttom_meiyan_layout = null;
        target.act_edit_buttom_meiyan_dismiss = null;
        target.edit_meiyan_seekbar = null;
        target.edit_meiyan_seekbar_bili = null;
        target.act_edit_buttom_meiyan_ok = null;
        target.act_photo_edit_imageview_linear = null;
    }
}
