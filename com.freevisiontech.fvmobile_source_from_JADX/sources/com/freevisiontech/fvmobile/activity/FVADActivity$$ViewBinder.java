package com.freevisiontech.fvmobile.activity;

import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.internal.DebouncingOnClickListener;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.activity.FVADActivity;

public class FVADActivity$$ViewBinder<T extends FVADActivity> implements ButterKnife.ViewBinder<T> {
    public void bind(ButterKnife.Finder finder, final T target, Object source) {
        View view = (View) finder.findRequiredView(source, C0853R.C0855id.img_back, "field 'imgBack' and method 'onViewClicked'");
        target.imgBack = (ImageView) finder.castView(view, C0853R.C0855id.img_back, "field 'imgBack'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onViewClicked();
            }
        });
        target.tvCenterTitle = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_center_title, "field 'tvCenterTitle'"), C0853R.C0855id.tv_center_title, "field 'tvCenterTitle'");
        target.idWebview = (WebView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.id_webview, "field 'idWebview'"), C0853R.C0855id.id_webview, "field 'idWebview'");
    }

    public void unbind(T target) {
        target.imgBack = null;
        target.tvCenterTitle = null;
        target.idWebview = null;
    }
}
