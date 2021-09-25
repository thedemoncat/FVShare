package com.freevisiontech.fvmobile.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.p001v4.app.Fragment;
import android.support.p001v4.app.FragmentActivity;
import android.support.p001v4.app.FragmentManager;
import android.support.p001v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.freevisiontech.fvmobile.fragment.FVShiPingWriteFragment;
import com.freevisiontech.fvmobile.fragment.FVZhaoPianWriteFragment;
import com.freevisiontech.fvmobile.utility.BaseActivityManager;
import com.umeng.analytics.MobclickAgent;
import java.util.ArrayList;
import java.util.List;

public class FileWriteActivity extends FragmentActivity implements View.OnClickListener {
    public static final int SHIPING = 1;
    public static final int ZHAOPIAN = 0;
    private String fragSelect;
    private List<Fragment> fragmentList;
    private FragmentManager fragmentManager;
    @Bind({2131756085})
    ImageView img_file_back;
    private int textColorCli;
    private int textColorNor;
    private List<TextView> textViews = new ArrayList();
    private TextView title_photo;
    private TextView title_video;
    private FragmentTransaction transaction;
    @Bind({2131756088})
    TextView tv_all_right;
    @Bind({2131756086})
    TextView tv_file_center_title;
    @Bind({2131756089})
    TextView tv_file_right_cancel;
    @Bind({2131756090})
    TextView tv_file_right_select;
    private View view;

    public static Intent createIntent(Context context, String fragSelect2) {
        Intent intent = new Intent(context, FileWriteActivity.class);
        intent.putExtra("fragSelect", fragSelect2);
        return intent;
    }

    private void parseIntent() {
        this.fragSelect = getIntent().getStringExtra("fragSelect");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0853R.layout.activity_file_write);
        ButterKnife.bind((Activity) this);
        parseIntent();
        BaseActivityManager.getActivityManager().pushActivity(this);
        initView();
        ininview();
    }

    private void initView() {
        this.img_file_back.setVisibility(0);
        this.tv_file_center_title.setVisibility(0);
        this.tv_all_right.setVisibility(8);
        this.tv_file_right_cancel.setVisibility(8);
        this.tv_file_right_select.setVisibility(8);
        this.tv_file_center_title.setText(getString(C0853R.string.file_select_creation));
        this.img_file_back.setOnClickListener(this);
    }

    private void ininview() {
        this.title_photo = (TextView) findViewById(C0853R.C0855id.frg_file_title_photo);
        this.title_video = (TextView) findViewById(C0853R.C0855id.frg_file_title_video);
        this.textViews.add(this.title_photo);
        this.textViews.add(this.title_video);
        this.textColorNor = getResources().getColor(C0853R.color.color_light_gray2);
        this.textColorCli = getResources().getColor(C0853R.color.color_black1);
        this.title_photo.setOnClickListener(this);
        this.title_video.setOnClickListener(this);
        this.fragmentList = new ArrayList();
        FVZhaoPianWriteFragment fragment1 = new FVZhaoPianWriteFragment();
        FVShiPingWriteFragment fragment2 = new FVShiPingWriteFragment();
        this.fragmentList.add(fragment1);
        this.fragmentList.add(fragment2);
        this.fragmentManager = getSupportFragmentManager();
        this.transaction = this.fragmentManager.beginTransaction();
        this.transaction.add((int) C0853R.C0855id.fragment_file, (Fragment) fragment1);
        this.transaction.add((int) C0853R.C0855id.fragment_file, (Fragment) fragment2);
        this.transaction.hide(fragment1);
        this.transaction.hide(fragment2);
        this.transaction.commit();
        if (this.fragSelect.equals("photo")) {
            hideFragment(0);
            getSharedPreferences("user", 0).edit().putString("camera", "0").commit();
        } else if (this.fragSelect.equals("shiping")) {
            hideFragment(1);
            getSharedPreferences("user", 0).edit().putString("camera", BleConstant.SHUTTER).commit();
        } else {
            hideFragment(0);
            getSharedPreferences("user", 0).edit().putString("camera", "0").commit();
        }
    }

    private void hideFragment(int index) {
        this.fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction2 = this.fragmentManager.beginTransaction();
        for (int i = 0; i < this.fragmentList.size(); i++) {
            if (i == index) {
                transaction2.show(this.fragmentList.get(i));
                this.textViews.get(i).setTextColor(this.textColorCli);
            } else {
                transaction2.hide(this.fragmentList.get(i));
                this.textViews.get(i).setTextColor(this.textColorNor);
            }
        }
        transaction2.commit();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0853R.C0855id.img_file_back:
                finish();
                return;
            case C0853R.C0855id.frg_file_title_photo:
                hideFragment(0);
                getSharedPreferences("user", 0).edit().putString("camera", "0").commit();
                return;
            case C0853R.C0855id.frg_file_title_video:
                hideFragment(1);
                getSharedPreferences("user", 0).edit().putString("camera", BleConstant.SHUTTER).commit();
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle outState) {
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
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
