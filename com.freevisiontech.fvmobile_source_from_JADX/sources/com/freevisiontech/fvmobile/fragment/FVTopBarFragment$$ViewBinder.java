package com.freevisiontech.fvmobile.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.fragment.FVTopBarFragment;

public class FVTopBarFragment$$ViewBinder<T extends FVTopBarFragment> implements ButterKnife.ViewBinder<T> {
    public void bind(ButterKnife.Finder finder, T target, Object source) {
        target.btnBack = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.btn_back, "field 'btnBack'"), C0853R.C0855id.btn_back, "field 'btnBack'");
        target.rlBack = (RelativeLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.rl_back, "field 'rlBack'"), C0853R.C0855id.rl_back, "field 'rlBack'");
        target.btnCamera = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.btn_camera, "field 'btnCamera'"), C0853R.C0855id.btn_camera, "field 'btnCamera'");
        target.btnCameraStatus = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.btn_camera_status, "field 'btnCameraStatus'"), C0853R.C0855id.btn_camera_status, "field 'btnCameraStatus'");
        target.rlCamera = (RelativeLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.rl_camera, "field 'rlCamera'"), C0853R.C0855id.rl_camera, "field 'rlCamera'");
        target.btnVilta = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.btn_vilta, "field 'btnVilta'"), C0853R.C0855id.btn_vilta, "field 'btnVilta'");
        target.btnViltaStatus = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.btn_vilta_status, "field 'btnViltaStatus'"), C0853R.C0855id.btn_vilta_status, "field 'btnViltaStatus'");
        target.rlVilta = (RelativeLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.rl_vilta, "field 'rlVilta'"), C0853R.C0855id.rl_vilta, "field 'rlVilta'");
        target.btnSetting = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.btn_setting, "field 'btnSetting'"), C0853R.C0855id.btn_setting, "field 'btnSetting'");
        target.rlSetting = (RelativeLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.rl_setting, "field 'rlSetting'"), C0853R.C0855id.rl_setting, "field 'rlSetting'");
        target.llRoot = (RelativeLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.ll_root, "field 'llRoot'"), C0853R.C0855id.ll_root, "field 'llRoot'");
        target.tv_ptz_battery_remain = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_ptz_battery_remain, "field 'tv_ptz_battery_remain'"), C0853R.C0855id.tv_ptz_battery_remain, "field 'tv_ptz_battery_remain'");
        target.icon_ptz_battery = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.icon_ptz_battery, "field 'icon_ptz_battery'"), C0853R.C0855id.icon_ptz_battery, "field 'icon_ptz_battery'");
        target.rl_ptz_battery = (RelativeLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.rl_ptz_battery, "field 'rl_ptz_battery'"), C0853R.C0855id.rl_ptz_battery, "field 'rl_ptz_battery'");
        target.rl_battery = (RelativeLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.rl_battery, "field 'rl_battery'"), C0853R.C0855id.rl_battery, "field 'rl_battery'");
        target.tv_phone_battery_remain = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_phone_battery_remain, "field 'tv_phone_battery_remain'"), C0853R.C0855id.tv_phone_battery_remain, "field 'tv_phone_battery_remain'");
        target.icon_phone_battery = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.icon_phone_battery, "field 'icon_phone_battery'"), C0853R.C0855id.icon_phone_battery, "field 'icon_phone_battery'");
        target.btn_bg_color_yellow = (LinearLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.btn_bg_color_yellow, "field 'btn_bg_color_yellow'"), C0853R.C0855id.btn_bg_color_yellow, "field 'btn_bg_color_yellow'");
    }

    public void unbind(T target) {
        target.btnBack = null;
        target.rlBack = null;
        target.btnCamera = null;
        target.btnCameraStatus = null;
        target.rlCamera = null;
        target.btnVilta = null;
        target.btnViltaStatus = null;
        target.rlVilta = null;
        target.btnSetting = null;
        target.rlSetting = null;
        target.llRoot = null;
        target.tv_ptz_battery_remain = null;
        target.icon_ptz_battery = null;
        target.rl_ptz_battery = null;
        target.rl_battery = null;
        target.tv_phone_battery_remain = null;
        target.icon_phone_battery = null;
        target.btn_bg_color_yellow = null;
    }
}
