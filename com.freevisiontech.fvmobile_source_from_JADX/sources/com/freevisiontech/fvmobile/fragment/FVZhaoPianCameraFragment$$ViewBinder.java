package com.freevisiontech.fvmobile.fragment;

import android.support.p003v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.fragment.FVZhaoPianCameraFragment;

public class FVZhaoPianCameraFragment$$ViewBinder<T extends FVZhaoPianCameraFragment> implements ButterKnife.ViewBinder<T> {
    public void bind(ButterKnife.Finder finder, T target, Object source) {
        target.fragment_recycler_zhaopian = (RecyclerView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.fragment_recycler_zhaopian, "field 'fragment_recycler_zhaopian'"), C0853R.C0855id.fragment_recycler_zhaopian, "field 'fragment_recycler_zhaopian'");
        target.file_paint_to_activity = (LinearLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.file_paint_to_activity, "field 'file_paint_to_activity'"), C0853R.C0855id.file_paint_to_activity, "field 'file_paint_to_activity'");
        target.fragment_file_zhaopian_none = (RelativeLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.fragment_file_zhaopian_none, "field 'fragment_file_zhaopian_none'"), C0853R.C0855id.fragment_file_zhaopian_none, "field 'fragment_file_zhaopian_none'");
    }

    public void unbind(T target) {
        target.fragment_recycler_zhaopian = null;
        target.file_paint_to_activity = null;
        target.fragment_file_zhaopian_none = null;
    }
}
