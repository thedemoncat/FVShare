package com.freevisiontech.fvmobile.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.activity.VideoEditingItemEachActivity;

public class VideoEditingItemEachActivity$$ViewBinder<T extends VideoEditingItemEachActivity> implements ButterKnife.ViewBinder<T> {
    public void bind(ButterKnife.Finder finder, T target, Object source) {
        target.tv_video_editing_right_share = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_video_editing_right_share, "field 'tv_video_editing_right_share'"), C0853R.C0855id.tv_video_editing_right_share, "field 'tv_video_editing_right_share'");
        target.tv_video_editing_all_baocun = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_video_editing_all_baocun, "field 'tv_video_editing_all_baocun'"), C0853R.C0855id.tv_video_editing_all_baocun, "field 'tv_video_editing_all_baocun'");
        target.img_video_editing_back = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.img_video_editing_back, "field 'img_video_editing_back'"), C0853R.C0855id.img_video_editing_back, "field 'img_video_editing_back'");
        target.vv_video = (VideoView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.act_video_editing_each_vv_video, "field 'vv_video'"), C0853R.C0855id.act_video_editing_each_vv_video, "field 'vv_video'");
        target.act_video_editing_item_each_imageview = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.act_video_editing_item_each_imageview, "field 'act_video_editing_item_each_imageview'"), C0853R.C0855id.act_video_editing_item_each_imageview, "field 'act_video_editing_item_each_imageview'");
        target.act_video_editing_item_each_imageview1 = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.act_video_editing_item_each_imageview1, "field 'act_video_editing_item_each_imageview1'"), C0853R.C0855id.act_video_editing_item_each_imageview1, "field 'act_video_editing_item_each_imageview1'");
        target.act_video_editing_item_each_imageview2 = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.act_video_editing_item_each_imageview2, "field 'act_video_editing_item_each_imageview2'"), C0853R.C0855id.act_video_editing_item_each_imageview2, "field 'act_video_editing_item_each_imageview2'");
        target.act_video_editing_zhanting = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.act_video_editing_zhanting, "field 'act_video_editing_zhanting'"), C0853R.C0855id.act_video_editing_zhanting, "field 'act_video_editing_zhanting'");
        target.act_video_editing_bofang = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.act_video_editing_bofang, "field 'act_video_editing_bofang'"), C0853R.C0855id.act_video_editing_bofang, "field 'act_video_editing_bofang'");
    }

    public void unbind(T target) {
        target.tv_video_editing_right_share = null;
        target.tv_video_editing_all_baocun = null;
        target.img_video_editing_back = null;
        target.vv_video = null;
        target.act_video_editing_item_each_imageview = null;
        target.act_video_editing_item_each_imageview1 = null;
        target.act_video_editing_item_each_imageview2 = null;
        target.act_video_editing_zhanting = null;
        target.act_video_editing_bofang = null;
    }
}
