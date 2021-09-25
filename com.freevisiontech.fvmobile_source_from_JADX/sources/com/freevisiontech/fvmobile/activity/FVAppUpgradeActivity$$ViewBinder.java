package com.freevisiontech.fvmobile.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.activity.FVAppUpgradeActivity;

public class FVAppUpgradeActivity$$ViewBinder<T extends FVAppUpgradeActivity> implements ButterKnife.ViewBinder<T> {
    public void bind(ButterKnife.Finder finder, T target, Object source) {
        target.tv_confirm_upgrade = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_confirm_upgrade, "field 'tv_confirm_upgrade'"), C0853R.C0855id.tv_confirm_upgrade, "field 'tv_confirm_upgrade'");
        target.tv_cancel_upgrade = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_cancel_upgrade, "field 'tv_cancel_upgrade'"), C0853R.C0855id.tv_cancel_upgrade, "field 'tv_cancel_upgrade'");
        target.tv_upgrade_desc1 = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_upgrade_desc1, "field 'tv_upgrade_desc1'"), C0853R.C0855id.tv_upgrade_desc1, "field 'tv_upgrade_desc1'");
        target.tv_upgrade_desc2 = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_upgrade_desc2, "field 'tv_upgrade_desc2'"), C0853R.C0855id.tv_upgrade_desc2, "field 'tv_upgrade_desc2'");
        target.tv_upgrade_desc3 = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_upgrade_desc3, "field 'tv_upgrade_desc3'"), C0853R.C0855id.tv_upgrade_desc3, "field 'tv_upgrade_desc3'");
        target.tv_upgrade_desc4 = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_upgrade_desc4, "field 'tv_upgrade_desc4'"), C0853R.C0855id.tv_upgrade_desc4, "field 'tv_upgrade_desc4'");
        target.tv_upgrade_desc5 = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_upgrade_desc5, "field 'tv_upgrade_desc5'"), C0853R.C0855id.tv_upgrade_desc5, "field 'tv_upgrade_desc5'");
        target.tv_upgrade_desc6 = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_upgrade_desc6, "field 'tv_upgrade_desc6'"), C0853R.C0855id.tv_upgrade_desc6, "field 'tv_upgrade_desc6'");
        target.ll_upgrade_point = (LinearLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.ll_upgrade_point, "field 'll_upgrade_point'"), C0853R.C0855id.ll_upgrade_point, "field 'll_upgrade_point'");
        target.ll_upgrade = (LinearLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.ll_upgrade, "field 'll_upgrade'"), C0853R.C0855id.ll_upgrade, "field 'll_upgrade'");
        target.view_line = (View) finder.findRequiredView(source, C0853R.C0855id.view_line, "field 'view_line'");
    }

    public void unbind(T target) {
        target.tv_confirm_upgrade = null;
        target.tv_cancel_upgrade = null;
        target.tv_upgrade_desc1 = null;
        target.tv_upgrade_desc2 = null;
        target.tv_upgrade_desc3 = null;
        target.tv_upgrade_desc4 = null;
        target.tv_upgrade_desc5 = null;
        target.tv_upgrade_desc6 = null;
        target.ll_upgrade_point = null;
        target.ll_upgrade = null;
        target.view_line = null;
    }
}
