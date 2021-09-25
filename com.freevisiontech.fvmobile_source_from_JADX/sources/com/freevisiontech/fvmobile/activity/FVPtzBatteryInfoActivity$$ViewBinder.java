package com.freevisiontech.fvmobile.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.activity.FVPtzBatteryInfoActivity;

public class FVPtzBatteryInfoActivity$$ViewBinder<T extends FVPtzBatteryInfoActivity> implements ButterKnife.ViewBinder<T> {
    public void bind(ButterKnife.Finder finder, T target, Object source) {
        target.img_back = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.img_back, "field 'img_back'"), C0853R.C0855id.img_back, "field 'img_back'");
        target.tv_center_title = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_center_title, "field 'tv_center_title'"), C0853R.C0855id.tv_center_title, "field 'tv_center_title'");
        target.tv_right = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_right, "field 'tv_right'"), C0853R.C0855id.tv_right, "field 'tv_right'");
        target.img_right = (ImageView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.img_right, "field 'img_right'"), C0853R.C0855id.img_right, "field 'img_right'");
        target.textDesignCapacity = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.text_design_capacity, "field 'textDesignCapacity'"), C0853R.C0855id.text_design_capacity, "field 'textDesignCapacity'");
        target.textChargePercentage = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.text_charge_percentage, "field 'textChargePercentage'"), C0853R.C0855id.text_charge_percentage, "field 'textChargePercentage'");
        target.textTemperature = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.text_temperature, "field 'textTemperature'"), C0853R.C0855id.text_temperature, "field 'textTemperature'");
        target.textLifePercentage = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.text_life_percentage, "field 'textLifePercentage'"), C0853R.C0855id.text_life_percentage, "field 'textLifePercentage'");
        target.textCurrentCapacity = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.text_current_capacity, "field 'textCurrentCapacity'"), C0853R.C0855id.text_current_capacity, "field 'textCurrentCapacity'");
        target.textElectricCurrent = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.text_electric_current, "field 'textElectricCurrent'"), C0853R.C0855id.text_electric_current, "field 'textElectricCurrent'");
        target.textNumDischarges = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.text_num_discharges, "field 'textNumDischarges'"), C0853R.C0855id.text_num_discharges, "field 'textNumDischarges'");
        target.textVoltage = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.text_voltage, "field 'textVoltage'"), C0853R.C0855id.text_voltage, "field 'textVoltage'");
    }

    public void unbind(T target) {
        target.img_back = null;
        target.tv_center_title = null;
        target.tv_right = null;
        target.img_right = null;
        target.textDesignCapacity = null;
        target.textChargePercentage = null;
        target.textTemperature = null;
        target.textLifePercentage = null;
        target.textCurrentCapacity = null;
        target.textElectricCurrent = null;
        target.textNumDischarges = null;
        target.textVoltage = null;
    }
}
