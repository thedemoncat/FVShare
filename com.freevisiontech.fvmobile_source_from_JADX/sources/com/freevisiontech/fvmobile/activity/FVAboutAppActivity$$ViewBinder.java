package com.freevisiontech.fvmobile.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.activity.FVAboutAppActivity;

public class FVAboutAppActivity$$ViewBinder<T extends FVAboutAppActivity> implements ButterKnife.ViewBinder<T> {
    public void bind(ButterKnife.Finder finder, T target, Object source) {
        target.img_back = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.img_back, "field 'img_back'"), C0853R.C0855id.img_back, "field 'img_back'");
        target.tv_center_title = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_center_title, "field 'tv_center_title'"), C0853R.C0855id.tv_center_title, "field 'tv_center_title'");
        target.tv_right = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_right, "field 'tv_right'"), C0853R.C0855id.tv_right, "field 'tv_right'");
        target.img_right = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.img_right, "field 'img_right'"), C0853R.C0855id.img_right, "field 'img_right'");
        target.tv_app_version = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_app_version, "field 'tv_app_version'"), C0853R.C0855id.tv_app_version, "field 'tv_app_version'");
        target.iv_logo = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.iv_logo, "field 'iv_logo'"), C0853R.C0855id.iv_logo, "field 'iv_logo'");
    }

    public void unbind(T target) {
        target.img_back = null;
        target.tv_center_title = null;
        target.tv_right = null;
        target.img_right = null;
        target.tv_app_version = null;
        target.iv_logo = null;
    }
}
