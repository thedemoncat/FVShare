package com.freevisiontech.fvmobile.activity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.activity.FVCalibrationActivity4;

public class FVCalibrationActivity4$$ViewBinder<T extends FVCalibrationActivity4> implements ButterKnife.ViewBinder<T> {
    public void bind(ButterKnife.Finder finder, T target, Object source) {
        target.btnBack = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.img_back, "field 'btnBack'"), C0853R.C0855id.img_back, "field 'btnBack'");
        target.centerTitle = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_center_title, "field 'centerTitle'"), C0853R.C0855id.tv_center_title, "field 'centerTitle'");
        target.iconGuide = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.icon_guide, "field 'iconGuide'"), C0853R.C0855id.icon_guide, "field 'iconGuide'");
        target.iconGuide2 = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.icon_guide2, "field 'iconGuide2'"), C0853R.C0855id.icon_guide2, "field 'iconGuide2'");
        target.tvDescription = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_description, "field 'tvDescription'"), C0853R.C0855id.tv_description, "field 'tvDescription'");
        target.tvDescription2 = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_description2, "field 'tvDescription2'"), C0853R.C0855id.tv_description2, "field 'tvDescription2'");
        target.rlMsg2 = (RelativeLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.rl_msg2, "field 'rlMsg2'"), C0853R.C0855id.rl_msg2, "field 'rlMsg2'");
        target.btnNext = (Button) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.btn_next, "field 'btnNext'"), C0853R.C0855id.btn_next, "field 'btnNext'");
    }

    public void unbind(T target) {
        target.btnBack = null;
        target.centerTitle = null;
        target.iconGuide = null;
        target.iconGuide2 = null;
        target.tvDescription = null;
        target.tvDescription2 = null;
        target.rlMsg2 = null;
        target.btnNext = null;
    }
}
