package com.freevisiontech.fvmobile.activity;

import android.view.View;
import android.widget.ImageView;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.activity.LookAllSceneryPhotoActivity;

public class LookAllSceneryPhotoActivity$$ViewBinder<T extends LookAllSceneryPhotoActivity> implements ButterKnife.ViewBinder<T> {
    public void bind(ButterKnife.Finder finder, T target, Object source) {
        target.act_look_all_scenery_photo_close = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.act_look_all_scenery_photo_close, "field 'act_look_all_scenery_photo_close'"), C0853R.C0855id.act_look_all_scenery_photo_close, "field 'act_look_all_scenery_photo_close'");
    }

    public void unbind(T target) {
        target.act_look_all_scenery_photo_close = null;
    }
}
