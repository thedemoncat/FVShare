package com.freevisiontech.fvmobile.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.activity.PhotoEditFiveActivity;

public class PhotoEditFiveActivity$$ViewBinder<T extends PhotoEditFiveActivity> implements ButterKnife.ViewBinder<T> {
    public void bind(ButterKnife.Finder finder, T target, Object source) {
        target.act_photo_edit_imageview = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.act_photo_edit_imageview, "field 'act_photo_edit_imageview'"), C0853R.C0855id.act_photo_edit_imageview, "field 'act_photo_edit_imageview'");
        target.act_photo_edit_jiazai = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.act_photo_edit_jiazai, "field 'act_photo_edit_jiazai'"), C0853R.C0855id.act_photo_edit_jiazai, "field 'act_photo_edit_jiazai'");
        target.act_edit_buttom_caijian_layout = (LinearLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.act_edit_buttom_caijian_layout, "field 'act_edit_buttom_caijian_layout'"), C0853R.C0855id.act_edit_buttom_caijian_layout, "field 'act_edit_buttom_caijian_layout'");
        target.act_edit_buttom_caijian_dismiss = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.act_edit_buttom_caijian_dismiss, "field 'act_edit_buttom_caijian_dismiss'"), C0853R.C0855id.act_edit_buttom_caijian_dismiss, "field 'act_edit_buttom_caijian_dismiss'");
        target.act_edit_buttom_caijian_btXuan = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.act_edit_buttom_caijian_btXuan, "field 'act_edit_buttom_caijian_btXuan'"), C0853R.C0855id.act_edit_buttom_caijian_btXuan, "field 'act_edit_buttom_caijian_btXuan'");
        target.act_edit_buttom_caijian_btCai = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.act_edit_buttom_caijian_btCai, "field 'act_edit_buttom_caijian_btCai'"), C0853R.C0855id.act_edit_buttom_caijian_btCai, "field 'act_edit_buttom_caijian_btCai'");
        target.act_edit_buttom_xuanzhuan_layout = (LinearLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.act_edit_buttom_xuanzhuan_layout, "field 'act_edit_buttom_xuanzhuan_layout'"), C0853R.C0855id.act_edit_buttom_xuanzhuan_layout, "field 'act_edit_buttom_xuanzhuan_layout'");
        target.act_edit_buttom_caijian_layout_caijian = (LinearLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.act_edit_buttom_caijian_layout_caijian, "field 'act_edit_buttom_caijian_layout_caijian'"), C0853R.C0855id.act_edit_buttom_caijian_layout_caijian, "field 'act_edit_buttom_caijian_layout_caijian'");
    }

    public void unbind(T target) {
        target.act_photo_edit_imageview = null;
        target.act_photo_edit_jiazai = null;
        target.act_edit_buttom_caijian_layout = null;
        target.act_edit_buttom_caijian_dismiss = null;
        target.act_edit_buttom_caijian_btXuan = null;
        target.act_edit_buttom_caijian_btCai = null;
        target.act_edit_buttom_xuanzhuan_layout = null;
        target.act_edit_buttom_caijian_layout_caijian = null;
    }
}
