package com.freevisiontech.fvmobile.adapter;

import android.support.p001v4.app.Fragment;
import android.support.p001v4.app.FragmentManager;
import android.support.p001v4.app.FragmentPagerAdapter;
import com.freevisiontech.fvmobile.fragment.FVShiPingCameraFragment;
import com.freevisiontech.fvmobile.fragment.FVZhaoPianCameraFragment;

public class FragmentFileAdapter extends FragmentPagerAdapter {
    public static final int TAB_COUNT = 2;

    public FragmentFileAdapter(FragmentManager fm) {
        super(fm);
    }

    public Fragment getItem(int id) {
        switch (id) {
            case 0:
                return new FVZhaoPianCameraFragment();
            case 1:
                return new FVShiPingCameraFragment();
            default:
                return null;
        }
    }

    public int getCount() {
        return 2;
    }
}
