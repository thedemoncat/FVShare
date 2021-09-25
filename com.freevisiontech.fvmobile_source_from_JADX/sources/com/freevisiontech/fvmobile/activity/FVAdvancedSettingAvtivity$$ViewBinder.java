package com.freevisiontech.fvmobile.activity;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.activity.FVAdvancedSettingAvtivity;

public class FVAdvancedSettingAvtivity$$ViewBinder<T extends FVAdvancedSettingAvtivity> implements ButterKnife.ViewBinder<T> {
    public void bind(ButterKnife.Finder finder, T target, Object source) {
        target.img_back = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.img_back, "field 'img_back'"), C0853R.C0855id.img_back, "field 'img_back'");
        target.tv_center_title = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_center_title, "field 'tv_center_title'"), C0853R.C0855id.tv_center_title, "field 'tv_center_title'");
        target.tv_right = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_right, "field 'tv_right'"), C0853R.C0855id.tv_right, "field 'tv_right'");
        target.img_right = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.img_right, "field 'img_right'"), C0853R.C0855id.img_right, "field 'img_right'");
        target.rg_bottom_layout = (RadioGroup) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.rg_bottom_layout, "field 'rg_bottom_layout'"), C0853R.C0855id.rg_bottom_layout, "field 'rg_bottom_layout'");
        target.btn_camera = (RadioButton) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.btn_camera, "field 'btn_camera'"), C0853R.C0855id.btn_camera, "field 'btn_camera'");
        target.btn_ptz = (RadioButton) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.btn_ptz, "field 'btn_ptz'"), C0853R.C0855id.btn_ptz, "field 'btn_ptz'");
        target.btn_rocker = (RadioButton) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.btn_rocker, "field 'btn_rocker'"), C0853R.C0855id.btn_rocker, "field 'btn_rocker'");
        target.btn_info = (RadioButton) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.btn_info, "field 'btn_info'"), C0853R.C0855id.btn_info, "field 'btn_info'");
        target.fl_main = (FrameLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.fl_main, "field 'fl_main'"), C0853R.C0855id.fl_main, "field 'fl_main'");
    }

    public void unbind(T target) {
        target.img_back = null;
        target.tv_center_title = null;
        target.tv_right = null;
        target.img_right = null;
        target.rg_bottom_layout = null;
        target.btn_camera = null;
        target.btn_ptz = null;
        target.btn_rocker = null;
        target.btn_info = null;
        target.fl_main = null;
    }
}
