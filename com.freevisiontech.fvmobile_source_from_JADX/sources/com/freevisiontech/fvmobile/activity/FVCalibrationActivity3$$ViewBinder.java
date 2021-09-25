package com.freevisiontech.fvmobile.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.activity.FVCalibrationActivity3;

public class FVCalibrationActivity3$$ViewBinder<T extends FVCalibrationActivity3> implements ButterKnife.ViewBinder<T> {
    public void bind(ButterKnife.Finder finder, T target, Object source) {
        target.btnBack = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.img_back, "field 'btnBack'"), C0853R.C0855id.img_back, "field 'btnBack'");
        target.centerTitle = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_center_title, "field 'centerTitle'"), C0853R.C0855id.tv_center_title, "field 'centerTitle'");
        target.progressBar = (ProgressBar) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.progress, "field 'progressBar'"), C0853R.C0855id.progress, "field 'progressBar'");
    }

    public void unbind(T target) {
        target.btnBack = null;
        target.centerTitle = null;
        target.progressBar = null;
    }
}
