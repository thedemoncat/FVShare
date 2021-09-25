package com.freevisiontech.fvmobile.activity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.activity.FVCalibrationActivity2;

public class FVCalibrationActivity2$$ViewBinder<T extends FVCalibrationActivity2> implements ButterKnife.ViewBinder<T> {
    public void bind(ButterKnife.Finder finder, T target, Object source) {
        target.btnBack = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.img_back, "field 'btnBack'"), C0853R.C0855id.img_back, "field 'btnBack'");
        target.centerTitle = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_center_title, "field 'centerTitle'"), C0853R.C0855id.tv_center_title, "field 'centerTitle'");
        target.btnNext = (Button) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.btn_next, "field 'btnNext'"), C0853R.C0855id.btn_next, "field 'btnNext'");
        target.icon_guide = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.icon_guide, "field 'icon_guide'"), C0853R.C0855id.icon_guide, "field 'icon_guide'");
        target.text_guide_tv = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.text_guide_tv, "field 'text_guide_tv'"), C0853R.C0855id.text_guide_tv, "field 'text_guide_tv'");
    }

    public void unbind(T target) {
        target.btnBack = null;
        target.centerTitle = null;
        target.btnNext = null;
        target.icon_guide = null;
        target.text_guide_tv = null;
    }
}
