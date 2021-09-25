package com.freevisiontech.fvmobile.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.activity.FVAboutPtzActivity;

public class FVAboutPtzActivity$$ViewBinder<T extends FVAboutPtzActivity> implements ButterKnife.ViewBinder<T> {
    public void bind(ButterKnife.Finder finder, T target, Object source) {
        target.img_back = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.img_back, "field 'img_back'"), C0853R.C0855id.img_back, "field 'img_back'");
        target.tv_center_title = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_center_title, "field 'tv_center_title'"), C0853R.C0855id.tv_center_title, "field 'tv_center_title'");
        target.tv_right = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_right, "field 'tv_right'"), C0853R.C0855id.tv_right, "field 'tv_right'");
        target.img_right = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.img_right, "field 'img_right'"), C0853R.C0855id.img_right, "field 'img_right'");
        target.textImuBootstrapProgram = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.text_imu_bootstrap_program, "field 'textImuBootstrapProgram'"), C0853R.C0855id.text_imu_bootstrap_program, "field 'textImuBootstrapProgram'");
        target.textImuHardwareVersion = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.text_imu_hardware_version, "field 'textImuHardwareVersion'"), C0853R.C0855id.text_imu_hardware_version, "field 'textImuHardwareVersion'");
        target.textImuFirmwareVersion = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.text_imu_firmware_version, "field 'textImuFirmwareVersion'"), C0853R.C0855id.text_imu_firmware_version, "field 'textImuFirmwareVersion'");
        target.textImuFirmwareUpgrade = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.text_imu_firmware_upgrade, "field 'textImuFirmwareUpgrade'"), C0853R.C0855id.text_imu_firmware_upgrade, "field 'textImuFirmwareUpgrade'");
        target.textGmuBootstrapProgram = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.text_gmu_bootstrap_program, "field 'textGmuBootstrapProgram'"), C0853R.C0855id.text_gmu_bootstrap_program, "field 'textGmuBootstrapProgram'");
        target.textGmuHardwareVersion = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.text_gmu_hardware_version, "field 'textGmuHardwareVersion'"), C0853R.C0855id.text_gmu_hardware_version, "field 'textGmuHardwareVersion'");
        target.textGmuFirmwareVersion = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.text_gmu_firmware_version, "field 'textGmuFirmwareVersion'"), C0853R.C0855id.text_gmu_firmware_version, "field 'textGmuFirmwareVersion'");
        target.textGmuFirmwareUpgrade = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.text_gmu_firmware_upgrade, "field 'textGmuFirmwareUpgrade'"), C0853R.C0855id.text_gmu_firmware_upgrade, "field 'textGmuFirmwareUpgrade'");
    }

    public void unbind(T target) {
        target.img_back = null;
        target.tv_center_title = null;
        target.tv_right = null;
        target.img_right = null;
        target.textImuBootstrapProgram = null;
        target.textImuHardwareVersion = null;
        target.textImuFirmwareVersion = null;
        target.textImuFirmwareUpgrade = null;
        target.textGmuBootstrapProgram = null;
        target.textGmuHardwareVersion = null;
        target.textGmuFirmwareVersion = null;
        target.textGmuFirmwareUpgrade = null;
    }
}
