package com.freevisiontech.fvmobile.fragment;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.fragment.FVAdvancedSetInfoFragment;

public class FVAdvancedSetInfoFragment$$ViewBinder<T extends FVAdvancedSetInfoFragment> implements ButterKnife.ViewBinder<T> {
    public void bind(ButterKnife.Finder finder, T target, Object source) {
        target.rl_battery_info = (RelativeLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.rl_battery_info, "field 'rl_battery_info'"), C0853R.C0855id.rl_battery_info, "field 'rl_battery_info'");
        target.rl_about_ptz = (RelativeLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.rl_about_ptz, "field 'rl_about_ptz'"), C0853R.C0855id.rl_about_ptz, "field 'rl_about_ptz'");
        target.rl_about_app = (RelativeLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.rl_about_app, "field 'rl_about_app'"), C0853R.C0855id.rl_about_app, "field 'rl_about_app'");
        target.rl_live = (RelativeLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.rl_live, "field 'rl_live'"), C0853R.C0855id.rl_live, "field 'rl_live'");
        target.rl_help = (RelativeLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.rl_help, "field 'rl_help'"), C0853R.C0855id.rl_help, "field 'rl_help'");
        target.rl_teach_video = (RelativeLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.rl_teach_video, "field 'rl_teach_video'"), C0853R.C0855id.rl_teach_video, "field 'rl_teach_video'");
        target.scrollview = (ScrollView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.scrollview, "field 'scrollview'"), C0853R.C0855id.scrollview, "field 'scrollview'");
    }

    public void unbind(T target) {
        target.rl_battery_info = null;
        target.rl_about_ptz = null;
        target.rl_about_app = null;
        target.rl_live = null;
        target.rl_help = null;
        target.rl_teach_video = null;
        target.scrollview = null;
    }
}
