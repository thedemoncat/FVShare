package com.freevisiontech.fvmobile.fragment;

import android.support.p003v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.fragment.FVZhaoPianWriteFragment;

public class FVZhaoPianWriteFragment$$ViewBinder<T extends FVZhaoPianWriteFragment> implements ButterKnife.ViewBinder<T> {
    public void bind(ButterKnife.Finder finder, T target, Object source) {
        target.fragment_recycler_zhaopian = (RecyclerView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.fragment_recycler_zhaopian, "field 'fragment_recycler_zhaopian'"), C0853R.C0855id.fragment_recycler_zhaopian, "field 'fragment_recycler_zhaopian'");
        target.fragment_file_zhaopian_write_none = (RelativeLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.fragment_file_zhaopian_write_none, "field 'fragment_file_zhaopian_write_none'"), C0853R.C0855id.fragment_file_zhaopian_write_none, "field 'fragment_file_zhaopian_write_none'");
    }

    public void unbind(T target) {
        target.fragment_recycler_zhaopian = null;
        target.fragment_file_zhaopian_write_none = null;
    }
}
