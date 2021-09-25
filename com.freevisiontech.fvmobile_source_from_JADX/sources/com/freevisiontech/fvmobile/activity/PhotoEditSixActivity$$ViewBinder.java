package com.freevisiontech.fvmobile.activity;

import android.support.p003v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.activity.PhotoEditSixActivity;

public class PhotoEditSixActivity$$ViewBinder<T extends PhotoEditSixActivity> implements ButterKnife.ViewBinder<T> {
    public void bind(ButterKnife.Finder finder, T target, Object source) {
        target.act_photo_edit_imageview = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.act_photo_edit_imageview, "field 'act_photo_edit_imageview'"), C0853R.C0855id.act_photo_edit_imageview, "field 'act_photo_edit_imageview'");
        target.act_photo_edit_jiazai = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.act_photo_edit_jiazai, "field 'act_photo_edit_jiazai'"), C0853R.C0855id.act_photo_edit_jiazai, "field 'act_photo_edit_jiazai'");
        target.act_edit_buttom_paint_layout = (LinearLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.act_edit_buttom_paint_layout, "field 'act_edit_buttom_paint_layout'"), C0853R.C0855id.act_edit_buttom_paint_layout, "field 'act_edit_buttom_paint_layout'");
        target.act_edit_buttom_paint_dismiss = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.act_edit_buttom_paint_dismiss, "field 'act_edit_buttom_paint_dismiss'"), C0853R.C0855id.act_edit_buttom_paint_dismiss, "field 'act_edit_buttom_paint_dismiss'");
        target.act_edit_buttom_paint_ok = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.act_edit_buttom_paint_ok, "field 'act_edit_buttom_paint_ok'"), C0853R.C0855id.act_edit_buttom_paint_ok, "field 'act_edit_buttom_paint_ok'");
        target.act_edit_buttom_paint_recycler = (RecyclerView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.act_edit_buttom_paint_recycler, "field 'act_edit_buttom_paint_recycler'"), C0853R.C0855id.act_edit_buttom_paint_recycler, "field 'act_edit_buttom_paint_recycler'");
        target.photo_edit_paint_view_linear = (LinearLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.photo_edit_paint_view_linear, "field 'photo_edit_paint_view_linear'"), C0853R.C0855id.photo_edit_paint_view_linear, "field 'photo_edit_paint_view_linear'");
        target.act_photo_edit_imageview_linear = (LinearLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.act_photo_edit_imageview_linear, "field 'act_photo_edit_imageview_linear'"), C0853R.C0855id.act_photo_edit_imageview_linear, "field 'act_photo_edit_imageview_linear'");
    }

    public void unbind(T target) {
        target.act_photo_edit_imageview = null;
        target.act_photo_edit_jiazai = null;
        target.act_edit_buttom_paint_layout = null;
        target.act_edit_buttom_paint_dismiss = null;
        target.act_edit_buttom_paint_ok = null;
        target.act_edit_buttom_paint_recycler = null;
        target.photo_edit_paint_view_linear = null;
        target.act_photo_edit_imageview_linear = null;
    }
}
