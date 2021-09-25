package com.freevisiontech.fvmobile.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.fragment.FVPTZListFragment;

public class FVPTZListFragment$$ViewBinder<T extends FVPTZListFragment> implements ButterKnife.ViewBinder<T> {
    public void bind(ButterKnife.Finder finder, T target, Object source) {
        target.img_back = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.img_back, "field 'img_back'"), C0853R.C0855id.img_back, "field 'img_back'");
        target.tv_center_title = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_center_title, "field 'tv_center_title'"), C0853R.C0855id.tv_center_title, "field 'tv_center_title'");
        target.tv_right = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_right, "field 'tv_right'"), C0853R.C0855id.tv_right, "field 'tv_right'");
        target.img_right = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.img_right, "field 'img_right'"), C0853R.C0855id.img_right, "field 'img_right'");
        target.iv_ptz_title = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.iv_ptz_title, "field 'iv_ptz_title'"), C0853R.C0855id.iv_ptz_title, "field 'iv_ptz_title'");
        target.indicator_0 = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.indicator_0, "field 'indicator_0'"), C0853R.C0855id.indicator_0, "field 'indicator_0'");
        target.indicator_1 = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.indicator_1, "field 'indicator_1'"), C0853R.C0855id.indicator_1, "field 'indicator_1'");
        target.indicator_2 = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.indicator_2, "field 'indicator_2'"), C0853R.C0855id.indicator_2, "field 'indicator_2'");
    }

    public void unbind(T target) {
        target.img_back = null;
        target.tv_center_title = null;
        target.tv_right = null;
        target.img_right = null;
        target.iv_ptz_title = null;
        target.indicator_0 = null;
        target.indicator_1 = null;
        target.indicator_2 = null;
    }
}
