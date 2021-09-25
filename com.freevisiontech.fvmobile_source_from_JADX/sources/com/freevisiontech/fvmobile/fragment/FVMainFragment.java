package com.freevisiontech.fvmobile.fragment;

import android.os.Bundle;
import android.support.p001v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.utility.Constants;
import com.freevisiontech.fvmobile.utility.SPUtils;
import com.freevisiontech.fvmobile.utility.SharePrefConstant;
import com.vise.log.ViseLog;

public class FVMainFragment extends Fragment {
    @Bind({2131755266})
    FrameLayout flBottomBar;
    @Bind({2131755264})
    FrameLayout flContent;
    @Bind({2131755265})
    FrameLayout flTopBar;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(C0853R.layout.fragment_main_two, container, false);
        ButterKnife.bind((Object) this, view);
        initStatusSetting();
        initChild();
        return view;
    }

    private void initChild() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(C0853R.C0855id.fl_TopBar, new FVTopBarFragment(), "topFragment").commit();
        getActivity().getSupportFragmentManager().beginTransaction().replace(C0853R.C0855id.fl_Content, new FVContentFragment(), "contentFragment").commit();
        getActivity().getSupportFragmentManager().beginTransaction().replace(C0853R.C0855id.fl_BottomBar, new FVBottomBarFragment(), "bottomFragment").commit();
    }

    private void initStatusSetting() {
        SPUtils.put(getActivity(), SharePrefConstant.DELAY_TAKE_PHOTO_MODE, Integer.valueOf(Constants.DELAY_TAKE_PHOTO_0S));
        SPUtils.put(getActivity(), SharePrefConstant.FULL_SHOT, Integer.valueOf(Constants.FULL_SHOT_NONE));
        SPUtils.put(getActivity(), SharePrefConstant.LONG_EXPOSURE_MODE, Integer.valueOf(Constants.LONG_EXPOSURE_NONE));
        SPUtils.put(getActivity(), SharePrefConstant.FILTER_MODE, Integer.valueOf(Constants.FILTER_NONE_MODE));
        SPUtils.put(getActivity(), SharePrefConstant.BEAUTY_MODE, Integer.valueOf(Constants.BEAUTY_CLOSE));
        SPUtils.put(getContext(), SharePrefConstant.BEAUTY_VALUE, 50);
        SPUtils.put(getActivity(), SharePrefConstant.HDR_MODE, Integer.valueOf(Constants.HDR_CLOSE));
        SPUtils.put(getActivity(), SharePrefConstant.WB_MODE, Integer.valueOf(Constants.WB_AUTO));
        SPUtils.put(getActivity(), SharePrefConstant.CAMERA_MODE, 10001);
        SPUtils.put(getActivity(), SharePrefConstant.FLASH_MODE, 10003);
        SPUtils.put(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE));
        SPUtils.put(getActivity(), SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, false);
        SPUtils.put(getActivity(), SharePrefConstant.CAMERA_FOCUS_LOCK_OR_MOVE, Integer.valueOf(Constants.CAMERA_FOCUS_MOVE));
        SPUtils.put(getActivity(), SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE));
    }

    public void onDestroy() {
        super.onDestroy();
        ViseLog.m1466e("MainFragment onDestroy");
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        initStatusSetting();
        ViseLog.m1466e("MainFragment onDestroyView");
    }
}
