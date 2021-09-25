package com.freevisiontech.fvmobile.adapter;

import android.support.p001v4.app.Fragment;
import android.support.p001v4.app.FragmentManager;
import android.support.p001v4.app.FragmentPagerAdapter;
import com.freevisiontech.fvmobile.fragment.FVShiPingFragment;
import com.freevisiontech.fvmobile.fragment.FVZhaoPianFragment;

public class ActivityFileAdapter extends FragmentPagerAdapter {
    public static final int TAB_COUNT = 2;

    public ActivityFileAdapter(FragmentManager fm) {
        super(fm);
    }

    public Fragment getItem(int id) {
        switch (id) {
            case 0:
                return new FVZhaoPianFragment();
            case 1:
                return new FVShiPingFragment();
            default:
                return null;
        }
    }

    public int getCount() {
        return 2;
    }
}
