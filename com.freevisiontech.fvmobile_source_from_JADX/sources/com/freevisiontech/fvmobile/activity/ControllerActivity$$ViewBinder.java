package com.freevisiontech.fvmobile.activity;

import android.view.View;
import android.widget.ImageView;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.activity.ControllerActivity;

public class ControllerActivity$$ViewBinder<T extends ControllerActivity> implements ButterKnife.ViewBinder<T> {
    public void bind(ButterKnife.Finder finder, T target, Object source) {
        target.img_back = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.img_back, "field 'img_back'"), C0853R.C0855id.img_back, "field 'img_back'");
    }

    public void unbind(T target) {
        target.img_back = null;
    }
}
