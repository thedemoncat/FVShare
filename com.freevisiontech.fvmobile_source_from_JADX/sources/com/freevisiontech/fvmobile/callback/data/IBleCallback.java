package com.freevisiontech.fvmobile.callback.data;

import com.freevisiontech.fvmobile.exception.BleException;

public interface IBleCallback {
    void onFailure(BleException bleException);
}
