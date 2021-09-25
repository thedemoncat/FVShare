package com.freevisiontech.fvmobile.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.activity.VideoEditingEndActivity;
import com.makeramen.roundedimageview.RoundedImageView;

public class VideoEditingEndActivity$$ViewBinder<T extends VideoEditingEndActivity> implements ButterKnife.ViewBinder<T> {
    public void bind(ButterKnife.Finder finder, T target, Object source) {
        target.act_video_editing_end_image = (RoundedImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.act_video_editing_end_image, "field 'act_video_editing_end_image'"), C0853R.C0855id.act_video_editing_end_image, "field 'act_video_editing_end_image'");
        target.img_file_back = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.img_file_back, "field 'img_file_back'"), C0853R.C0855id.img_file_back, "field 'img_file_back'");
        target.tv_file_right_select = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_file_right_select, "field 'tv_file_right_select'"), C0853R.C0855id.tv_file_right_select, "field 'tv_file_right_select'");
        target.act_video_editing_end_to_play_video = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.act_video_editing_end_to_play_video, "field 'act_video_editing_end_to_play_video'"), C0853R.C0855id.act_video_editing_end_to_play_video, "field 'act_video_editing_end_to_play_video'");
    }

    public void unbind(T target) {
        target.act_video_editing_end_image = null;
        target.img_file_back = null;
        target.tv_file_right_select = null;
        target.act_video_editing_end_to_play_video = null;
    }
}
