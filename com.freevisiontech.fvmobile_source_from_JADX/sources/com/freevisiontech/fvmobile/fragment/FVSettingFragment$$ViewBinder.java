package com.freevisiontech.fvmobile.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.fragment.FVSettingFragment;

public class FVSettingFragment$$ViewBinder<T extends FVSettingFragment> implements ButterKnife.ViewBinder<T> {
    public void bind(ButterKnife.Finder finder, T target, Object source) {
        target.img_back = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.img_back, "field 'img_back'"), C0853R.C0855id.img_back, "field 'img_back'");
        target.tv_center_title = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_center_title, "field 'tv_center_title'"), C0853R.C0855id.tv_center_title, "field 'tv_center_title'");
        target.tv_right = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_right, "field 'tv_right'"), C0853R.C0855id.tv_right, "field 'tv_right'");
        target.img_right = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.img_right, "field 'img_right'"), C0853R.C0855id.img_right, "field 'img_right'");
        target.listViewFirst = (ListView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.listviewfirst, "field 'listViewFirst'"), C0853R.C0855id.listviewfirst, "field 'listViewFirst'");
    }

    public void unbind(T target) {
        target.img_back = null;
        target.tv_center_title = null;
        target.tv_right = null;
        target.img_right = null;
        target.listViewFirst = null;
    }
}
