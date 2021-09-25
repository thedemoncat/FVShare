package com.freevisiontech.fvmobile.fragment;

import android.support.p003v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.fragment.FVShiPingWriteFragment;

public class FVShiPingWriteFragment$$ViewBinder<T extends FVShiPingWriteFragment> implements ButterKnife.ViewBinder<T> {
    public void bind(ButterKnife.Finder finder, T target, Object source) {
        target.fragment_recycler_shiping = (RecyclerView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.fragment_recycler_shiping, "field 'fragment_recycler_shiping'"), C0853R.C0855id.fragment_recycler_shiping, "field 'fragment_recycler_shiping'");
        target.file_paint_to_activity = (LinearLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.file_paint_to_activity, "field 'file_paint_to_activity'"), C0853R.C0855id.file_paint_to_activity, "field 'file_paint_to_activity'");
        target.fragment_file_shiping_create = (LinearLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.fragment_file_shiping_create, "field 'fragment_file_shiping_create'"), C0853R.C0855id.fragment_file_shiping_create, "field 'fragment_file_shiping_create'");
        target.fragment_file_shiping_write_none = (RelativeLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.fragment_file_shiping_write_none, "field 'fragment_file_shiping_write_none'"), C0853R.C0855id.fragment_file_shiping_write_none, "field 'fragment_file_shiping_write_none'");
        target.video_edit_to_create = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.video_edit_to_create, "field 'video_edit_to_create'"), C0853R.C0855id.video_edit_to_create, "field 'video_edit_to_create'");
    }

    public void unbind(T target) {
        target.fragment_recycler_shiping = null;
        target.file_paint_to_activity = null;
        target.fragment_file_shiping_create = null;
        target.fragment_file_shiping_write_none = null;
        target.video_edit_to_create = null;
    }
}
