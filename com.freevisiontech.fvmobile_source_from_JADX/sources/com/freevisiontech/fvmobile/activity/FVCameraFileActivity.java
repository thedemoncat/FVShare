package com.freevisiontech.fvmobile.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.p001v4.app.Fragment;
import android.support.p001v4.app.FragmentActivity;
import android.support.p001v4.app.FragmentManager;
import android.support.p001v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.freevisiontech.fvmobile.fragment.FVShiPingCameraFragment;
import com.freevisiontech.fvmobile.fragment.FVZhaoPianCameraFragment;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.umeng.analytics.MobclickAgent;
import java.util.ArrayList;
import java.util.List;
import p010me.iwf.photopicker.widget.control.DataChangeNotification;
import p010me.iwf.photopicker.widget.control.IssueKey;

public class FVCameraFileActivity extends FragmentActivity implements View.OnClickListener {
    private List<Fragment> fragmentList;
    private FragmentManager fragmentManager;
    private ImageView img_file_back;
    private RelativeLayout main_share_delete;
    private TextView right_cancel;
    private TextView right_select;
    private int textColorCli;
    private int textColorNor;
    private List<TextView> textViews = new ArrayList();
    private ImageButton text_main_delete;
    private TextView title_photo;
    private TextView title_video;
    private FragmentTransaction transaction;
    private TextView tv_all_right;
    private TextView tv_center_title;
    private View view;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0853R.layout.activity_camera_file);
        this.text_main_delete = (ImageButton) findViewById(C0853R.C0855id.act_text_main_delete);
        this.text_main_delete.setOnClickListener(this);
        this.main_share_delete = (RelativeLayout) findViewById(C0853R.C0855id.main_share_delete);
        this.main_share_delete.setVisibility(8);
        initTitle();
        ininview();
    }

    private void initTitle() {
        this.img_file_back = (ImageView) findViewById(C0853R.C0855id.img_file_back);
        this.tv_center_title = (TextView) findViewById(C0853R.C0855id.tv_file_center_title);
        this.tv_all_right = (TextView) findViewById(C0853R.C0855id.tv_all_right);
        this.right_cancel = (TextView) findViewById(C0853R.C0855id.tv_file_right_cancel);
        this.right_select = (TextView) findViewById(C0853R.C0855id.tv_file_right_select);
        this.tv_center_title.setText(C0853R.string.home_fragment_bottom_file);
        this.img_file_back.setVisibility(0);
        this.title_photo = (TextView) findViewById(C0853R.C0855id.frg_file_title_photo);
        this.title_video = (TextView) findViewById(C0853R.C0855id.frg_file_title_video);
        this.textViews.add(this.title_photo);
        this.textViews.add(this.title_video);
        this.textColorNor = getResources().getColor(C0853R.color.color_light_gray2);
        this.textColorCli = getResources().getColor(C0853R.color.color_black1);
        this.fragmentList = new ArrayList();
        this.tv_all_right.setOnClickListener(this);
        this.right_cancel.setOnClickListener(this);
        this.right_select.setOnClickListener(this);
        this.img_file_back.setOnClickListener(this);
        this.title_photo.setOnClickListener(this);
        this.title_video.setOnClickListener(this);
        getSharedPreferences("user", 0).edit().putString("camera", "0").commit();
    }

    private void ininview() {
        FVZhaoPianCameraFragment fragment1 = new FVZhaoPianCameraFragment();
        FVShiPingCameraFragment fragment2 = new FVShiPingCameraFragment();
        this.fragmentList.add(fragment1);
        this.fragmentList.add(fragment2);
        this.fragmentManager = getSupportFragmentManager();
        this.transaction = this.fragmentManager.beginTransaction();
        this.transaction.add((int) C0853R.C0855id.fragment_file, (Fragment) fragment1);
        this.transaction.add((int) C0853R.C0855id.fragment_file, (Fragment) fragment2);
        this.transaction.hide(fragment1);
        this.transaction.hide(fragment2);
        this.transaction.commit();
        hideFragment(0);
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
            case C0853R.C0855id.act_text_main_delete:
                DataChangeNotification.getInstance().notifyDataChanged(IssueKey.PHOTO_VIDEO_CAMERA_DEIETE);
                return;
            case C0853R.C0855id.img_file_back:
                finish();
                return;
            case C0853R.C0855id.tv_all_right:
                String camera = getSharedPreferences("user", 0).getString("camera", "");
                if (camera.equals("0")) {
                    DataChangeNotification.getInstance().notifyDataChanged(IssueKey.CAMERA_CHECK_ALL);
                    return;
                } else if (camera.equals(BleConstant.SHUTTER)) {
                    DataChangeNotification.getInstance().notifyDataChanged(IssueKey.CAMERA_VIDEO_CHECK_ALL);
                    return;
                } else {
                    return;
                }
            case C0853R.C0855id.tv_file_right_cancel:
                this.tv_all_right.setVisibility(8);
                this.right_cancel.setVisibility(8);
                this.right_select.setVisibility(0);
                this.main_share_delete.setVisibility(8);
                DataChangeNotification.getInstance().notifyDataChanged(IssueKey.CAMERA_CHECK_LOOK);
                return;
            case C0853R.C0855id.tv_file_right_select:
                this.tv_all_right.setVisibility(0);
                this.right_cancel.setVisibility(0);
                this.right_select.setVisibility(8);
                this.main_share_delete.setVisibility(0);
                DataChangeNotification.getInstance().notifyDataChanged(IssueKey.CAMERA_CHECK_OK);
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
        CameraUtils.setLabelTopBarSelect(-1);
    }
}
