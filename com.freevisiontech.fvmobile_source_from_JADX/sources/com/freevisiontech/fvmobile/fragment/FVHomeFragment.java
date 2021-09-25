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

public class FVHomeFragment extends Fragment {
    @Bind({2131755266})
    FrameLayout flBottomBar;
    @Bind({2131755264})
    FrameLayout flContent;
    @Bind({2131755265})
    FrameLayout flTopBar;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(C0853R.layout.fragment_home, container, false);
        ButterKnife.bind((Object) this, view);
        initChild();
        return view;
    }

    private void initChild() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(C0853R.C0855id.fl_TopBar, new FVTopBarFragment()).commit();
        getActivity().getSupportFragmentManager().beginTransaction().replace(C0853R.C0855id.fl_Content, new FVContentFragment()).commit();
        getActivity().getSupportFragmentManager().beginTransaction().replace(C0853R.C0855id.fl_BottomBar, new FVBottomBarFragment()).commit();
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
