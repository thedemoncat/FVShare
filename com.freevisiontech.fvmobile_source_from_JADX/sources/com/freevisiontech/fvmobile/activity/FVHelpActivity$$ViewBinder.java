package com.freevisiontech.fvmobile.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.activity.FVHelpActivity;
import com.github.barteksc.pdfviewer.PDFView;

public class FVHelpActivity$$ViewBinder<T extends FVHelpActivity> implements ButterKnife.ViewBinder<T> {
    public void bind(ButterKnife.Finder finder, T target, Object source) {
        target.imgBack = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.img_back, "field 'imgBack'"), C0853R.C0855id.img_back, "field 'imgBack'");
        target.tvCenterTitle = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_center_title, "field 'tvCenterTitle'"), C0853R.C0855id.tv_center_title, "field 'tvCenterTitle'");
        target.tvRight = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_right, "field 'tvRight'"), C0853R.C0855id.tv_right, "field 'tvRight'");
        target.imgRight = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.img_right, "field 'imgRight'"), C0853R.C0855id.img_right, "field 'imgRight'");
        target.pdfView = (PDFView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.pdfView, "field 'pdfView'"), C0853R.C0855id.pdfView, "field 'pdfView'");
        target.tvPage = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_page, "field 'tvPage'"), C0853R.C0855id.tv_page, "field 'tvPage'");
    }

    public void unbind(T target) {
        target.imgBack = null;
        target.tvCenterTitle = null;
        target.tvRight = null;
        target.imgRight = null;
        target.pdfView = null;
        target.tvPage = null;
    }
}
