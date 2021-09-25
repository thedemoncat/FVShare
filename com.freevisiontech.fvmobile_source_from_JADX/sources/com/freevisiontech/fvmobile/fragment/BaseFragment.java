package com.freevisiontech.fvmobile.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.p001v4.app.Fragment;
import com.freevisiontech.fvmobile.utils.Event;
import com.freevisiontech.fvmobile.utils.EventBusUtil;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public abstract class BaseFragment extends Fragment {
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isRegisterEventBus()) {
            EventBusUtil.register(this);
        }
    }

    /* access modifiers changed from: protected */
    public boolean isRegisterEventBus() {
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCome(Event event) {
        if (event != null) {
            receiveEvent(event);
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onStickyEventBusCome(Event event) {
        if (event != null) {
            receiveStickyEvent(event);
        }
    }

    /* access modifiers changed from: protected */
    public void receiveEvent(Event event) {
    }

    /* access modifiers changed from: protected */
    public void receiveStickyEvent(Event event) {
    }

    public void onDestroy() {
        super.onDestroy();
        if (isRegisterEventBus()) {
            EventBusUtil.unregister(this);
        }
    }
}
