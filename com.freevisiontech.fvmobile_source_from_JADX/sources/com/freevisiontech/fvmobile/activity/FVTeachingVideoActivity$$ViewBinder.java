package com.freevisiontech.fvmobile.activity;

import android.support.p003v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.activity.FVTeachingVideoActivity;

public class FVTeachingVideoActivity$$ViewBinder<T extends FVTeachingVideoActivity> implements ButterKnife.ViewBinder<T> {
    public void bind(ButterKnife.Finder finder, T target, Object source) {
        target.mBackIv = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.img_back, "field 'mBackIv'"), C0853R.C0855id.img_back, "field 'mBackIv'");
        target.mTitleTv = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_center_title, "field 'mTitleTv'"), C0853R.C0855id.tv_center_title, "field 'mTitleTv'");
        target.mRightTv = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_right, "field 'mRightTv'"), C0853R.C0855id.tv_right, "field 'mRightTv'");
        target.mRightIv = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.img_right, "field 'mRightIv'"), C0853R.C0855id.img_right, "field 'mRightIv'");
        target.mVideosRv = (RecyclerView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.videos_rv_teaching_video, "field 'mVideosRv'"), C0853R.C0855id.videos_rv_teaching_video, "field 'mVideosRv'");
    }

    public void unbind(T target) {
        target.mBackIv = null;
        target.mTitleTv = null;
        target.mRightTv = null;
        target.mRightIv = null;
        target.mVideosRv = null;
    }
}
