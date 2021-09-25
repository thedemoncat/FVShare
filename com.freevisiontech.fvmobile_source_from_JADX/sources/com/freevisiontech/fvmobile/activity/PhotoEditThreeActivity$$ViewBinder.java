package com.freevisiontech.fvmobile.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.activity.PhotoEditThreeActivity;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageView;

public class PhotoEditThreeActivity$$ViewBinder<T extends PhotoEditThreeActivity> implements ButterKnife.ViewBinder<T> {
    public void bind(ButterKnife.Finder finder, T target, Object source) {
        target.act_photo_edit_imageview = (GPUImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.act_photo_edit_imageview, "field 'act_photo_edit_imageview'"), C0853R.C0855id.act_photo_edit_imageview, "field 'act_photo_edit_imageview'");
        target.act_photo_edit_jiazai = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.act_photo_edit_jiazai, "field 'act_photo_edit_jiazai'"), C0853R.C0855id.act_photo_edit_jiazai, "field 'act_photo_edit_jiazai'");
        target.act_edit_buttom_tiaojie_layout = (LinearLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.act_edit_buttom_tiaojie_layout, "field 'act_edit_buttom_tiaojie_layout'"), C0853R.C0855id.act_edit_buttom_tiaojie_layout, "field 'act_edit_buttom_tiaojie_layout'");
        target.act_edit_buttom_tiaojie_dismiss = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.act_edit_buttom_tiaojie_dismiss, "field 'act_edit_buttom_tiaojie_dismiss'"), C0853R.C0855id.act_edit_buttom_tiaojie_dismiss, "field 'act_edit_buttom_tiaojie_dismiss'");
        target.edit_tiaojie_seekbar_bili = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.edit_tiaojie_seekbar_bili, "field 'edit_tiaojie_seekbar_bili'"), C0853R.C0855id.edit_tiaojie_seekbar_bili, "field 'edit_tiaojie_seekbar_bili'");
        target.id_edit_buttom_tiaojie_duibi = (RadioButton) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.id_edit_buttom_tiaojie_duibi, "field 'id_edit_buttom_tiaojie_duibi'"), C0853R.C0855id.id_edit_buttom_tiaojie_duibi, "field 'id_edit_buttom_tiaojie_duibi'");
        target.id_edit_buttom_tiaojie_liang = (RadioButton) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.id_edit_buttom_tiaojie_liang, "field 'id_edit_buttom_tiaojie_liang'"), C0853R.C0855id.id_edit_buttom_tiaojie_liang, "field 'id_edit_buttom_tiaojie_liang'");
        target.id_edit_buttom_tiaojie_bao = (RadioButton) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.id_edit_buttom_tiaojie_bao, "field 'id_edit_buttom_tiaojie_bao'"), C0853R.C0855id.id_edit_buttom_tiaojie_bao, "field 'id_edit_buttom_tiaojie_bao'");
        target.edit_tiaojie_seekbar = (SeekBar) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.edit_tiaojie_seekbar, "field 'edit_tiaojie_seekbar'"), C0853R.C0855id.edit_tiaojie_seekbar, "field 'edit_tiaojie_seekbar'");
        target.act_edit_buttom_tiaojie_ok = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.act_edit_buttom_tiaojie_ok, "field 'act_edit_buttom_tiaojie_ok'"), C0853R.C0855id.act_edit_buttom_tiaojie_ok, "field 'act_edit_buttom_tiaojie_ok'");
        target.act_photo_edit_imageview_linear = (LinearLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.act_photo_edit_imageview_linear, "field 'act_photo_edit_imageview_linear'"), C0853R.C0855id.act_photo_edit_imageview_linear, "field 'act_photo_edit_imageview_linear'");
    }

    public void unbind(T target) {
        target.act_photo_edit_imageview = null;
        target.act_photo_edit_jiazai = null;
        target.act_edit_buttom_tiaojie_layout = null;
        target.act_edit_buttom_tiaojie_dismiss = null;
        target.edit_tiaojie_seekbar_bili = null;
        target.id_edit_buttom_tiaojie_duibi = null;
        target.id_edit_buttom_tiaojie_liang = null;
        target.id_edit_buttom_tiaojie_bao = null;
        target.edit_tiaojie_seekbar = null;
        target.act_edit_buttom_tiaojie_ok = null;
        target.act_photo_edit_imageview_linear = null;
    }
}
