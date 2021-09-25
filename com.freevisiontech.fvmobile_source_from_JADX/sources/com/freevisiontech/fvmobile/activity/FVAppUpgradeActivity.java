package com.freevisiontech.fvmobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.BuildConfig;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.bean.FVUpgradeBean;
import com.freevisiontech.fvmobile.utility.Util;
import com.freevisiontech.fvmobile.utils.BleUpgradeUtil;
import com.umeng.analytics.MobclickAgent;
import java.text.NumberFormat;

public class FVAppUpgradeActivity extends Activity {
    public static final String TAG = "FVAppUpgradeActivity";
    @Bind({2131755236})
    LinearLayout ll_upgrade;
    @Bind({2131755224})
    LinearLayout ll_upgrade_point;
    private NumberFormat numberFormat;
    @Bind({2131755237})
    TextView tv_cancel_upgrade;
    @Bind({2131755238})
    TextView tv_confirm_upgrade;
    @Bind({2131755225})
    TextView tv_upgrade_desc1;
    @Bind({2131755226})
    TextView tv_upgrade_desc2;
    @Bind({2131755227})
    TextView tv_upgrade_desc3;
    @Bind({2131755228})
    TextView tv_upgrade_desc4;
    @Bind({2131755229})
    TextView tv_upgrade_desc5;
    @Bind({2131755230})
    TextView tv_upgrade_desc6;
    private BleUpgradeUtil util;
    @Bind({2131755235})
    View view_line;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0853R.layout.activity_has_new_version);
        ButterKnife.bind((Activity) this);
        Util.setPrimaryDarkColor(this);
        initView();
        initData();
    }

    private void initView() {
        this.numberFormat = NumberFormat.getPercentInstance();
        this.numberFormat.setMinimumFractionDigits(0);
        BleUpgradeUtil.getInstance().init(this);
        this.tv_confirm_upgrade.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FVAppUpgradeActivity.this.toPlayStore();
            }
        });
        this.tv_cancel_upgrade.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FVAppUpgradeActivity.this.enterHomeActivity();
            }
        });
    }

    /* access modifiers changed from: private */
    public void toPlayStore() {
        try {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID));
            intent.setPackage("com.android.vending");
            intent.addFlags(268435456);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public void enterHomeActivity() {
        setResult(2);
        finish();
    }

    private void initData() {
        FVUpgradeBean upgradeBean = BleUpgradeUtil.getInstance().getUpgradeBean();
        if (upgradeBean != null) {
            String releasenotes = upgradeBean.getRows().get(0).getReleasenotes();
            if (!releasenotes.equals("")) {
                String[] languageSp = releasenotes.split("\\$\\$");
                Log.e(TAG, "onCreate: " + languageSp.length);
                String aaa = "";
                String bbb = "";
                for (int i = 0; i < languageSp.length; i++) {
                    Log.e(TAG, "截: " + languageSp[i]);
                    if (languageSp[i].substring(0, 3).equals("cn=")) {
                        aaa = languageSp[i];
                    } else {
                        bbb = languageSp[i];
                    }
                }
                if (Util.isZh(this)) {
                    setReleasenotesText(aaa);
                } else {
                    setReleasenotesText(bbb);
                }
            } else {
                Log.e(TAG, "onCreate: 无更新点");
                this.tv_upgrade_desc1.setVisibility(0);
                this.tv_upgrade_desc1.setText(getString(C0853R.string.app_no_release_points));
            }
        } else {
            this.tv_upgrade_desc1.setVisibility(0);
            this.tv_upgrade_desc1.setText(getString(C0853R.string.app_no_release_points));
        }
    }

    private void setReleasenotesText(String s) {
        String substring = s.substring(3, s.length());
        Log.e(TAG, "setReleasenotesText: " + substring);
        String[] split = substring.split("##");
        for (int i = 0; i < split.length; i++) {
            Log.e(TAG, "onCreate: " + split[i]);
        }
        if (split.length >= 1) {
            this.tv_upgrade_desc1.setVisibility(0);
            this.tv_upgrade_desc1.setText(split[0]);
        }
        if (split.length >= 2) {
            this.tv_upgrade_desc2.setVisibility(0);
            this.tv_upgrade_desc2.setText(split[1]);
        }
        if (split.length >= 3) {
            this.tv_upgrade_desc3.setVisibility(0);
            this.tv_upgrade_desc3.setText(split[2]);
        }
        if (split.length >= 4) {
            this.tv_upgrade_desc4.setVisibility(0);
            this.tv_upgrade_desc4.setText(split[3]);
        }
        if (split.length >= 5) {
            this.tv_upgrade_desc5.setVisibility(0);
            this.tv_upgrade_desc5.setText(split[4]);
        }
        if (split.length >= 6) {
            this.tv_upgrade_desc6.setVisibility(0);
            this.tv_upgrade_desc6.setText(split[5]);
        }
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

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
