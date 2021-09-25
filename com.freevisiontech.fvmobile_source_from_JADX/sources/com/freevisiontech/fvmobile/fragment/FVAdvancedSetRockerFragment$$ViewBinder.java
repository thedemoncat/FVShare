package com.freevisiontech.fvmobile.fragment;

import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.fragment.FVAdvancedSetRockerFragment;

public class FVAdvancedSetRockerFragment$$ViewBinder<T extends FVAdvancedSetRockerFragment> implements ButterKnife.ViewBinder<T> {
    public void bind(ButterKnife.Finder finder, T target, Object source) {
        target.sb_pitch_sensitivity = (SeekBar) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.sb_pitch_sensitivity, "field 'sb_pitch_sensitivity'"), C0853R.C0855id.sb_pitch_sensitivity, "field 'sb_pitch_sensitivity'");
        target.sb_trunnion_sensitivity = (SeekBar) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.sb_trunnion_sensitivity, "field 'sb_trunnion_sensitivity'"), C0853R.C0855id.sb_trunnion_sensitivity, "field 'sb_trunnion_sensitivity'");
        target.sb_roll_sensitivity = (SeekBar) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.sb_roll_sensitivity, "field 'sb_roll_sensitivity'"), C0853R.C0855id.sb_roll_sensitivity, "field 'sb_roll_sensitivity'");
        target.sb_pitch_speed = (SeekBar) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.sb_pitch_speed, "field 'sb_pitch_speed'"), C0853R.C0855id.sb_pitch_speed, "field 'sb_pitch_speed'");
        target.sb_trunnion_speed = (SeekBar) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.sb_trunnion_speed, "field 'sb_trunnion_speed'"), C0853R.C0855id.sb_trunnion_speed, "field 'sb_trunnion_speed'");
        target.sb_roll_speed = (SeekBar) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.sb_roll_speed, "field 'sb_roll_speed'"), C0853R.C0855id.sb_roll_speed, "field 'sb_roll_speed'");
        target.sb_pitch_dead_zone = (SeekBar) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.sb_pitch_dead_zone, "field 'sb_pitch_dead_zone'"), C0853R.C0855id.sb_pitch_dead_zone, "field 'sb_pitch_dead_zone'");
        target.sb_trunnion_dead_zone = (SeekBar) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.sb_trunnion_dead_zone, "field 'sb_trunnion_dead_zone'"), C0853R.C0855id.sb_trunnion_dead_zone, "field 'sb_trunnion_dead_zone'");
        target.sb_roll_dead_zone = (SeekBar) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.sb_roll_dead_zone, "field 'sb_roll_dead_zone'"), C0853R.C0855id.sb_roll_dead_zone, "field 'sb_roll_dead_zone'");
        target.rg_rocker_orientation = (RadioGroup) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.rg_rocker_orientation, "field 'rg_rocker_orientation'"), C0853R.C0855id.rg_rocker_orientation, "field 'rg_rocker_orientation'");
        target.rb_up_down = (RadioButton) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.rb_up_down, "field 'rb_up_down'"), C0853R.C0855id.rb_up_down, "field 'rb_up_down'");
        target.rb_left_right = (RadioButton) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.rb_left_right, "field 'rb_left_right'"), C0853R.C0855id.rb_left_right, "field 'rb_left_right'");
        target.tv_pitch_st_pg = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_pitch_st_pg, "field 'tv_pitch_st_pg'"), C0853R.C0855id.tv_pitch_st_pg, "field 'tv_pitch_st_pg'");
        target.tv_trunnion_st_pg = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_trunnion_st_pg, "field 'tv_trunnion_st_pg'"), C0853R.C0855id.tv_trunnion_st_pg, "field 'tv_trunnion_st_pg'");
        target.tv_roll_st_pg = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_roll_st_pg, "field 'tv_roll_st_pg'"), C0853R.C0855id.tv_roll_st_pg, "field 'tv_roll_st_pg'");
        target.tv_pitch_sp_pg = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_pitch_sp_pg, "field 'tv_pitch_sp_pg'"), C0853R.C0855id.tv_pitch_sp_pg, "field 'tv_pitch_sp_pg'");
        target.tv_trunnion_sp_pg = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_trunnion_sp_pg, "field 'tv_trunnion_sp_pg'"), C0853R.C0855id.tv_trunnion_sp_pg, "field 'tv_trunnion_sp_pg'");
        target.tv_roll_sp_pg = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_roll_sp_pg, "field 'tv_roll_sp_pg'"), C0853R.C0855id.tv_roll_sp_pg, "field 'tv_roll_sp_pg'");
        target.tv_pitch_dead_pg = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_pitch_dead_pg, "field 'tv_pitch_dead_pg'"), C0853R.C0855id.tv_pitch_dead_pg, "field 'tv_pitch_dead_pg'");
        target.tv_trunnion_dead_pg = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_trunnion_dead_pg, "field 'tv_trunnion_dead_pg'"), C0853R.C0855id.tv_trunnion_dead_pg, "field 'tv_trunnion_dead_pg'");
        target.tv_roll_dead_pg = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.tv_roll_dead_pg, "field 'tv_roll_dead_pg'"), C0853R.C0855id.tv_roll_dead_pg, "field 'tv_roll_dead_pg'");
        target.cb_up_or_down = (CheckBox) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.cb_up_or_down, "field 'cb_up_or_down'"), C0853R.C0855id.cb_up_or_down, "field 'cb_up_or_down'");
        target.cb_left_or_right = (CheckBox) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.cb_left_or_right, "field 'cb_left_or_right'"), C0853R.C0855id.cb_left_or_right, "field 'cb_left_or_right'");
        target.ll_no_connect_ptz = (LinearLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.ll_no_connect_ptz, "field 'll_no_connect_ptz'"), C0853R.C0855id.ll_no_connect_ptz, "field 'll_no_connect_ptz'");
        target.scrollview = (ScrollView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.scrollview, "field 'scrollview'"), C0853R.C0855id.scrollview, "field 'scrollview'");
        target.ll_connect_ptz = (LinearLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.ll_connect_ptz, "field 'll_connect_ptz'"), C0853R.C0855id.ll_connect_ptz, "field 'll_connect_ptz'");
        target.layout_advance_joystick_dead_zone_what = (TextView) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.layout_advance_joystick_dead_zone_what, "field 'layout_advance_joystick_dead_zone_what'"), C0853R.C0855id.layout_advance_joystick_dead_zone_what, "field 'layout_advance_joystick_dead_zone_what'");
        target.rl_pitch_axis_sensitivity = (RelativeLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.rl_pitch_axis_sensitivity, "field 'rl_pitch_axis_sensitivity'"), C0853R.C0855id.rl_pitch_axis_sensitivity, "field 'rl_pitch_axis_sensitivity'");
        target.rl_pan_axis_sensitivity = (RelativeLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.rl_pan_axis_sensitivity, "field 'rl_pan_axis_sensitivity'"), C0853R.C0855id.rl_pan_axis_sensitivity, "field 'rl_pan_axis_sensitivity'");
        target.rl_roll_axis_sensitivity = (RelativeLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.rl_roll_axis_sensitivity, "field 'rl_roll_axis_sensitivity'"), C0853R.C0855id.rl_roll_axis_sensitivity, "field 'rl_roll_axis_sensitivity'");
        target.rl_pitch_speed = (RelativeLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.rl_pitch_speed, "field 'rl_pitch_speed'"), C0853R.C0855id.rl_pitch_speed, "field 'rl_pitch_speed'");
        target.rl_pan_speed = (RelativeLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.rl_pan_speed, "field 'rl_pan_speed'"), C0853R.C0855id.rl_pan_speed, "field 'rl_pan_speed'");
        target.rl_roll_speed = (RelativeLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.rl_roll_speed, "field 'rl_roll_speed'"), C0853R.C0855id.rl_roll_speed, "field 'rl_roll_speed'");
        target.rl_pitch_axis_dead_zone = (RelativeLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.rl_pitch_axis_dead_zone, "field 'rl_pitch_axis_dead_zone'"), C0853R.C0855id.rl_pitch_axis_dead_zone, "field 'rl_pitch_axis_dead_zone'");
        target.rl_pan_axis_dead_zone = (RelativeLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.rl_pan_axis_dead_zone, "field 'rl_pan_axis_dead_zone'"), C0853R.C0855id.rl_pan_axis_dead_zone, "field 'rl_pan_axis_dead_zone'");
        target.rl_roll_axis_dead_zone = (RelativeLayout) finder.castView((View) finder.findRequiredView(source, C0853R.C0855id.rl_roll_axis_dead_zone, "field 'rl_roll_axis_dead_zone'"), C0853R.C0855id.rl_roll_axis_dead_zone, "field 'rl_roll_axis_dead_zone'");
    }

    public void unbind(T target) {
        target.sb_pitch_sensitivity = null;
        target.sb_trunnion_sensitivity = null;
        target.sb_roll_sensitivity = null;
        target.sb_pitch_speed = null;
        target.sb_trunnion_speed = null;
        target.sb_roll_speed = null;
        target.sb_pitch_dead_zone = null;
        target.sb_trunnion_dead_zone = null;
        target.sb_roll_dead_zone = null;
        target.rg_rocker_orientation = null;
        target.rb_up_down = null;
        target.rb_left_right = null;
        target.tv_pitch_st_pg = null;
        target.tv_trunnion_st_pg = null;
        target.tv_roll_st_pg = null;
        target.tv_pitch_sp_pg = null;
        target.tv_trunnion_sp_pg = null;
        target.tv_roll_sp_pg = null;
        target.tv_pitch_dead_pg = null;
        target.tv_trunnion_dead_pg = null;
        target.tv_roll_dead_pg = null;
        target.cb_up_or_down = null;
        target.cb_left_or_right = null;
        target.ll_no_connect_ptz = null;
        target.scrollview = null;
        target.ll_connect_ptz = null;
        target.layout_advance_joystick_dead_zone_what = null;
        target.rl_pitch_axis_sensitivity = null;
        target.rl_pan_axis_sensitivity = null;
        target.rl_roll_axis_sensitivity = null;
        target.rl_pitch_speed = null;
        target.rl_pan_speed = null;
        target.rl_roll_speed = null;
        target.rl_pitch_axis_dead_zone = null;
        target.rl_pan_axis_dead_zone = null;
        target.rl_roll_axis_dead_zone = null;
    }
}
