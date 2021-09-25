package com.freevisiontech.fvmobile.fragment;

import android.view.View;
import android.widget.FrameLayout;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.fragment.FVMainFragment;

public class FVMainFragment$$ViewBinder<T extends FVMainFragment> implements ButterKnife.ViewBinder<T> {
    public void bind(ButterKnife.Finder finder, T target, Object source) {
        target.flTopBar = (FrameLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.fl_TopBar, "field 'flTopBar'"), C0853R.C0855id.fl_TopBar, "field 'flTopBar'");
        target.flContent = (FrameLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.fl_Content, "field 'flContent'"), C0853R.C0855id.fl_Content, "field 'flContent'");
        target.flBottomBar = (FrameLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.fl_BottomBar, "field 'flBottomBar'"), C0853R.C0855id.fl_BottomBar, "field 'flBottomBar'");
    }

    public void unbind(T target) {
        target.flTopBar = null;
        target.flContent = null;
        target.flBottomBar = null;
    }
}
