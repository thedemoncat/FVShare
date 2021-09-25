package com.freevisiontech.fvmobile.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.fragment.FVPTZFragment;

public class FVPTZFragment$$ViewBinder<T extends FVPTZFragment> implements ButterKnife.ViewBinder<T> {
    public void bind(ButterKnife.Finder finder, T target, Object source) {
        target.img_back = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.img_back, "field 'img_back'"), C0853R.C0855id.img_back, "field 'img_back'");
        target.tv_center_title = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_center_title, "field 'tv_center_title'"), C0853R.C0855id.tv_center_title, "field 'tv_center_title'");
        target.tv_right = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_right, "field 'tv_right'"), C0853R.C0855id.tv_right, "field 'tv_right'");
        target.img_right = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.img_right, "field 'img_right'"), C0853R.C0855id.img_right, "field 'img_right'");
        target.rl_content_scaning = (RelativeLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.rl_content_scaning, "field 'rl_content_scaning'"), C0853R.C0855id.rl_content_scaning, "field 'rl_content_scaning'");
        target.tv_back = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_back, "field 'tv_back'"), C0853R.C0855id.tv_back, "field 'tv_back'");
        target.tv_enter_camera = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_enter_camera, "field 'tv_enter_camera'"), C0853R.C0855id.tv_enter_camera, "field 'tv_enter_camera'");
        target.iv_refresh_ble = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.iv_refresh_ble, "field 'iv_refresh_ble'"), C0853R.C0855id.iv_refresh_ble, "field 'iv_refresh_ble'");
        target.deviceLv = (ListView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.deviceLv, "field 'deviceLv'"), C0853R.C0855id.deviceLv, "field 'deviceLv'");
        target.tv_ptz_type_name = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.ptz_device_type_name, "field 'tv_ptz_type_name'"), C0853R.C0855id.ptz_device_type_name, "field 'tv_ptz_type_name'");
        target.iv_ptz_title = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.iv_ptz_title, "field 'iv_ptz_title'"), C0853R.C0855id.iv_ptz_title, "field 'iv_ptz_title'");
        target.img_ptz_scan = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.img_ptz_scan, "field 'img_ptz_scan'"), C0853R.C0855id.img_ptz_scan, "field 'img_ptz_scan'");
    }

    public void unbind(T target) {
        target.img_back = null;
        target.tv_center_title = null;
        target.tv_right = null;
        target.img_right = null;
        target.rl_content_scaning = null;
        target.tv_back = null;
        target.tv_enter_camera = null;
        target.iv_refresh_ble = null;
        target.deviceLv = null;
        target.tv_ptz_type_name = null;
        target.iv_ptz_title = null;
        target.img_ptz_scan = null;
    }
}
