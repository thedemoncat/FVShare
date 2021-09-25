package com.freevisiontech.fvmobile.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.internal.DebouncingOnClickListener;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.activity.FVSplashActivity;

public class FVSplashActivity$$ViewBinder<T extends FVSplashActivity> implements ButterKnife.ViewBinder<T> {
    public void bind(ButterKnife.Finder finder, final T target, Object source) {
        View view = (View) finder.findRequiredView(source, C0853R.C0855id.ad_iv, "field 'adIv' and method 'onViewClicked'");
        target.adIv = (ImageView) finder.castView(view, C0853R.C0855id.ad_iv, "field 'adIv'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onViewClicked(p0);
            }
        });
        View view2 = (View) finder.findRequiredView(source, C0853R.C0855id.skip_ad_tv, "field 'skipAdTv' and method 'onViewClicked'");
        target.skipAdTv = (TextView) finder.castView(view2, C0853R.C0855id.skip_ad_tv, "field 'skipAdTv'");
        view2.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onViewClicked(p0);
            }
        });
    }

    public void unbind(T target) {
        target.adIv = null;
        target.skipAdTv = null;
    }
}
