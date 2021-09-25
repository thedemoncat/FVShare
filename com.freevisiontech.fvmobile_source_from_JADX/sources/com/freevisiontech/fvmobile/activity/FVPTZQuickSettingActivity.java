package com.freevisiontech.fvmobile.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.p003v7.app.AppCompatActivity;
import com.umeng.analytics.MobclickAgent;

public class FVPTZQuickSettingActivity extends AppCompatActivity {
    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
