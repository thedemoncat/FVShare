package com.freevisiontech.fvmobile.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.p001v4.app.Fragment;
import android.support.p001v4.app.FragmentManager;
import android.support.p001v4.app.FragmentTransaction;
import android.support.p003v7.app.AlertDialog;
import android.support.p003v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.ViseBluetooth;
import com.freevisiontech.fvmobile.application.MyApplication;
import com.freevisiontech.fvmobile.fragment.FVFileFragment2;
import com.freevisiontech.fvmobile.fragment.FVPTZFragment;
import com.freevisiontech.fvmobile.fragment.FVPTZListFragment;
import com.freevisiontech.fvmobile.fragment.FVSettingFragment;
import com.freevisiontech.fvmobile.utility.BaseActivityManager;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utility.Constants;
import com.freevisiontech.fvmobile.utility.SPUtils;
import com.freevisiontech.fvmobile.utility.SharePrefConstant;
import com.freevisiontech.fvmobile.utility.Util;
import com.freevisiontech.fvmobile.utils.BleUtil;
import com.freevisiontech.fvmobile.utils.BlueToothHistoryUtil;
import com.freevisiontech.fvmobile.utils.LoadingView;
import com.freevisiontech.fvmobile.utils.OpenBleOrGpsDialog;
import com.freevisiontech.fvmobile.utils.SPUtil;
import com.github.kayvannj.permission_utils.Func;
import com.github.kayvannj.permission_utils.PermissionUtil;
import com.umeng.analytics.MobclickAgent;
import com.vise.log.ViseLog;
import java.util.ArrayList;
import java.util.List;
import p004pl.droidsonroids.gif.GifDrawable;
import p010me.iwf.photopicker.widget.control.DataChangeNotification;
import p010me.iwf.photopicker.widget.control.IssueKey;
import p010me.iwf.photopicker.widget.control.OnDataChangeObserver;

public class FVHomeActivity extends AppCompatActivity implements View.OnClickListener, OnDataChangeObserver {
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 100;
    public static final String TAG = "FVHomeActivity";
    private Activity activity;
    private LinearLayout activity_title_main_button;
    private int[] bottomImgCli = {C0853R.mipmap.yuntai3, C0853R.mipmap.wenjian1, C0853R.mipmap.shezhi1};
    private int[] bottomImgNor = {C0853R.mipmap.yuntai4, C0853R.mipmap.wenjian2, C0853R.mipmap.shezhi2};
    /* access modifiers changed from: private */
    public Context context;
    /* access modifiers changed from: private */
    public int currentItem = 0;
    private long exitTime = 0;
    /* access modifiers changed from: private */
    public Fragment fragment1;
    private Fragment fragment2;
    private Fragment fragment3;
    /* access modifiers changed from: private */
    public Fragment fragment4;
    private List<Fragment> fragmentList;
    /* access modifiers changed from: private */
    public FragmentManager fragmentManager;
    private GifDrawable gifDrawable;
    private ImageView image1;
    private ImageView image2;
    private ImageView image3;
    private List<ImageView> images = new ArrayList();
    private ImageView iv_gif_image_view_close;
    private ImageView iv_left_right_sliding_effect;
    private RelativeLayout layout1;
    private RelativeLayout layout2;
    private RelativeLayout layout3;
    private RelativeLayout main_share_delete;
    /* access modifiers changed from: private */
    public LoadingView progressDialog;
    public PtzMenuSwitcher ptzMenuSwitcher = new PtzMenuSwitcher() {
        public void showPtzList() {
            if (!isGpsOpen() || !BleUtil.isBleEnable(FVHomeActivity.this)) {
                showOpenHint();
                return;
            }
            FVHomeActivity.this.hideFragment(3);
            int unused = FVHomeActivity.this.currentItem = 3;
        }

        public void showPtzSelect() {
            FragmentTransaction unused = FVHomeActivity.this.transaction = FVHomeActivity.this.fragmentManager.beginTransaction();
            FVHomeActivity.this.transaction.hide(FVHomeActivity.this.fragment4);
            FVHomeActivity.this.transaction.show(FVHomeActivity.this.fragment1);
            FVHomeActivity.this.transaction.commitAllowingStateLoss();
            int unused2 = FVHomeActivity.this.currentItem = 0;
        }

        public void showOpenHint() {
            OpenBleOrGpsDialog OpenBleOrGpsDialog = new OpenBleOrGpsDialog(FVHomeActivity.this);
            OpenBleOrGpsDialog.show();
            Display d = FVHomeActivity.this.getWindowManager().getDefaultDisplay();
            WindowManager.LayoutParams params = OpenBleOrGpsDialog.getWindow().getAttributes();
            params.width = (int) (((double) d.getWidth()) * 0.6d);
            params.height = (int) (((double) d.getHeight()) * 0.25d);
            params.gravity = 1;
            params.gravity = 16;
            OpenBleOrGpsDialog.getWindow().setAttributes(params);
        }

        public void checkAutoConnect(String ptzTypeName) {
            if (!isGpsOpen() || !BleUtil.isBleEnable(FVHomeActivity.this)) {
                showOpenHint();
                return;
            }
            showAutoConnectHint("");
            ((FVPTZFragment) FVHomeActivity.this.fragment4).setCurrentPTZType(ptzTypeName);
            ((FVPTZFragment) FVHomeActivity.this.fragment4).connectDevice();
        }

        public void showAutoConnectHint(String deviceName) {
            String str = (String) SPUtils.get(FVHomeActivity.this, SharePrefConstant.CURRENT_PTZ_TYPE, "");
            String currentPtzType = MyApplication.CURRENT_PTZ_TYPE;
            Log.i("KBein", "FVHomeActivity.showAutoConnectHint():--currentPtzType--" + currentPtzType);
            if (!BlueToothHistoryUtil.isEmpty(FVHomeActivity.this, currentPtzType)) {
                FVHomeActivity.this.progressDialog.setMessage(FVHomeActivity.this.getResources().getString(C0853R.string.auto_connect) + " " + deviceName);
                FVHomeActivity.this.progressDialog.show();
                return;
            }
            showPtzList();
        }

        public void onAutoConnectTimeout() {
            hideAutoConnectHint();
            showPtzList();
        }

        public void hideAutoConnectHint() {
            if (FVHomeActivity.this.progressDialog != null) {
                FVHomeActivity.this.progressDialog.dismiss();
            }
        }

        public boolean isGpsOpen() {
            LocationManager lManager = (LocationManager) FVHomeActivity.this.getSystemService("location");
            if (lManager.isProviderEnabled("gps") || lManager.isProviderEnabled("network")) {
                return true;
            }
            return false;
        }
    };
    private PermissionUtil.PermissionRequestObject requestObject;
    /* access modifiers changed from: private */
    public RelativeLayout rl_gif_image_view;
    private TextView text1;
    private TextView text2;
    private TextView text3;
    private int textColorCli;
    private int textColorNor;
    private List<TextView> textViews = new ArrayList();
    private ImageView text_main_baocun;
    private ImageView text_main_delete;
    private ImageView text_main_share;
    /* access modifiers changed from: private */
    public FragmentTransaction transaction;

    public interface PtzMenuSwitcher {
        void checkAutoConnect(String str);

        void hideAutoConnectHint();

        boolean isGpsOpen();

        void onAutoConnectTimeout();

        void showAutoConnectHint(String str);

        void showOpenHint();

        void showPtzList();

        void showPtzSelect();
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0853R.layout.activity_main_new);
        this.activity = this;
        this.context = this;
        if (((Integer) SPUtil.getParam(this.context, SharePrefConstant.LEFT_RIGHT_GIF, Integer.valueOf(Constants.LEFT_RIGHT_GIF_UI_GONE))).intValue() == 107764) {
            this.rl_gif_image_view = (RelativeLayout) findViewById(C0853R.C0855id.rl_gif_image_view);
            this.iv_left_right_sliding_effect = (ImageView) findViewById(C0853R.C0855id.iv_left_right_sliding_effect);
            this.iv_gif_image_view_close = (ImageView) findViewById(C0853R.C0855id.iv_gif_image_view_close);
            this.rl_gif_image_view.setVisibility(0);
            if (Util.isZh(this)) {
                this.iv_left_right_sliding_effect.setImageResource(C0853R.mipmap.ic_left_right_sliding_effect);
                this.iv_gif_image_view_close.setImageResource(C0853R.mipmap.ic_i_konw);
            } else {
                this.iv_left_right_sliding_effect.setImageResource(C0853R.mipmap.ic_left_right_sliding_effect_en);
                this.iv_gif_image_view_close.setImageResource(C0853R.mipmap.ic_i_konw_en);
            }
            this.iv_gif_image_view_close.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    SPUtil.setParam(FVHomeActivity.this.context, SharePrefConstant.LEFT_RIGHT_GIF, Integer.valueOf(Constants.LEFT_RIGHT_GIF_UI_VISIBLE));
                    FVHomeActivity.this.rl_gif_image_view.setVisibility(8);
                }
            });
        }
        this.textColorNor = getResources().getColor(C0853R.color.color_light_gray2);
        this.textColorCli = getResources().getColor(C0853R.color.color_light_gray3);
        initView();
        DataChangeNotification.getInstance().addObserver(IssueKey.PHOTO_VIDEO_DEIETE, this);
        BaseActivityManager.getActivityManager().pushActivity(this);
        checkPermission();
        DataChangeNotification.getInstance().addObserver(IssueKey.VIDEO_PHOTO_CAMERA_DELETE_CHECK_HAVA, this);
        DataChangeNotification.getInstance().addObserver(IssueKey.VIDEO_PHOTO_CAMERA_DELETE_CHECK_HAVA_NONE, this);
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            this.requestObject = PermissionUtil.with((AppCompatActivity) this).request("android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.ACCESS_COARSE_LOCATION", "android.permission.CAMERA", "android.permission.RECORD_AUDIO").onAllGranted(new Func() {
                /* access modifiers changed from: protected */
                public void call() {
                    Log.e(FVHomeActivity.TAG, "call: 权限通过");
                    FVHomeActivity.this.scanBluetooth();
                }
            }).onAnyDenied(new Func() {
                /* access modifiers changed from: protected */
                public void call() {
                    Log.e(FVHomeActivity.TAG, "call: 权限被拒");
                }
            }).ask(0);
            return;
        }
        scanBluetooth();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions.length > 0) {
            this.requestObject.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /* access modifiers changed from: private */
    public void scanBluetooth() {
        if (!BleUtil.isBleEnable(this)) {
            BleUtil.enableBluetooth(this, 1);
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int i = newConfig.orientation;
        getResources().getConfiguration();
        if (i == 1) {
            ViseLog.m1466e("HomeActivity--------------------------竖屏----");
            setRequestedOrientation(1);
            return;
        }
        int i2 = newConfig.orientation;
        getResources().getConfiguration();
        if (i2 == 2) {
            ViseLog.m1466e("HomeActivity---------------------------横屏-------");
            setRequestedOrientation(1);
        }
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle outState) {
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        ViseBluetooth.getInstance().clear();
        super.onDestroy();
    }

    /* access modifiers changed from: protected */
    public void onRestart() {
        super.onRestart();
    }

    private void initView() {
        this.main_share_delete = (RelativeLayout) findViewById(C0853R.C0855id.main_share_delete);
        this.activity_title_main_button = (LinearLayout) findViewById(C0853R.C0855id.activity_title_main_button);
        this.text_main_share = (ImageView) findViewById(C0853R.C0855id.text_main_share);
        this.text_main_baocun = (ImageView) findViewById(C0853R.C0855id.text_main_baocun);
        this.text_main_delete = (ImageView) findViewById(C0853R.C0855id.text_main_delete);
        this.progressDialog = new LoadingView(this);
        this.text_main_share.setOnClickListener(this);
        this.text_main_baocun.setOnClickListener(this);
        this.text_main_delete.setOnClickListener(this);
        this.text_main_delete.setBackgroundResource(C0853R.C0854drawable.file_delete_button_selector_none);
        this.text_main_share.setBackgroundResource(C0853R.C0854drawable.file_share_button_selector_none);
        this.text_main_share.setVisibility(8);
        this.image1 = (ImageView) findViewById(C0853R.C0855id.main_image1);
        this.image2 = (ImageView) findViewById(C0853R.C0855id.main_image2);
        this.image3 = (ImageView) findViewById(C0853R.C0855id.main_image3);
        this.images.add(this.image1);
        this.images.add(this.image2);
        this.images.add(this.image3);
        this.text1 = (TextView) findViewById(C0853R.C0855id.main_text1);
        this.text2 = (TextView) findViewById(C0853R.C0855id.main_text2);
        this.text3 = (TextView) findViewById(C0853R.C0855id.main_text3);
        this.textViews.add(this.text1);
        this.textViews.add(this.text2);
        this.textViews.add(this.text3);
        this.layout1 = (RelativeLayout) findViewById(C0853R.C0855id.tab1);
        this.layout2 = (RelativeLayout) findViewById(C0853R.C0855id.tab2);
        this.layout3 = (RelativeLayout) findViewById(C0853R.C0855id.tab3);
        this.layout1.setOnClickListener(this);
        this.layout2.setOnClickListener(this);
        this.layout3.setOnClickListener(this);
        this.fragmentList = new ArrayList();
        this.fragment1 = new FVPTZListFragment();
        this.fragment2 = new FVFileFragment2();
        this.fragment3 = new FVSettingFragment();
        this.fragment4 = new FVPTZFragment();
        this.fragmentList.add(this.fragment1);
        this.fragmentList.add(this.fragment2);
        this.fragmentList.add(this.fragment3);
        this.fragmentList.add(this.fragment4);
        this.fragmentManager = getSupportFragmentManager();
        this.transaction = this.fragmentManager.beginTransaction();
        this.transaction.add((int) C0853R.C0855id.fragment_main_A, this.fragment1);
        this.transaction.add((int) C0853R.C0855id.fragment_main_A, this.fragment2);
        this.transaction.add((int) C0853R.C0855id.fragment_main_A, this.fragment3);
        this.transaction.add((int) C0853R.C0855id.fragment_main_A, this.fragment4);
        this.transaction.hide(this.fragment2);
        this.transaction.hide(this.fragment3);
        this.transaction.hide(this.fragment4);
        this.transaction.commitAllowingStateLoss();
        hideFragment(0);
    }

    /* access modifiers changed from: private */
    public void hideFragment(int index) {
        this.fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction2 = this.fragmentManager.beginTransaction();
        int showIndex = index % 3;
        for (int i = 0; i < this.fragmentList.size(); i++) {
            int current = i % 3;
            transaction2.hide(this.fragmentList.get(i));
            this.textViews.get(current).setTextColor(this.textColorNor);
            this.images.get(current).setImageResource(this.bottomImgNor[current]);
        }
        transaction2.show(this.fragmentList.get(index));
        this.textViews.get(showIndex).setTextColor(this.textColorCli);
        this.images.get(showIndex).setImageResource(this.bottomImgCli[showIndex]);
        transaction2.commitAllowingStateLoss();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0853R.C0855id.text_main_share:
                DataChangeNotification.getInstance().notifyDataChanged(IssueKey.PHOTO_CHECK);
                return;
            case C0853R.C0855id.text_main_baocun:
                DataChangeNotification.getInstance().notifyDataChanged(IssueKey.PHOTO_CHECK);
                return;
            case C0853R.C0855id.tab1:
                hideFragment(this.currentItem);
                return;
            case C0853R.C0855id.tab2:
                hideFragment(1);
                if (CameraUtils.getHasTakePhotoOrVideo()) {
                    DataChangeNotification.getInstance().notifyDataChanged(IssueKey.PHOTO_VIDEO_DEIETE);
                    CameraUtils.setHasTakePhotoOrVideo(false);
                    return;
                }
                return;
            case C0853R.C0855id.tab3:
                hideFragment(2);
                return;
            case C0853R.C0855id.text_main_delete:
                if (CameraUtils.getPhotoSelectNums() + CameraUtils.getVideoSelectNums() != 0) {
                    deleteDialog();
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void deleteDialog() {
        String aaa = null;
        if (CameraUtils.getPhotoSelectNums() == 0 || CameraUtils.getVideoSelectNums() == 0) {
            if (CameraUtils.getPhotoSelectNums() == 0) {
                if (Util.isZh(this.activity)) {
                    aaa = getString(C0853R.string.file_photo_video_delete_yes_or_no) + CameraUtils.getVideoSelectNums() + getString(C0853R.string.file_photo_video_video_nums) + "";
                } else {
                    aaa = getString(C0853R.string.file_photo_video_delete) + " " + getString(C0853R.string.label_video) + " " + CameraUtils.getVideoSelectNums() + "?";
                }
            } else if (CameraUtils.getVideoSelectNums() == 0) {
                if (Util.isZh(this.activity)) {
                    aaa = getString(C0853R.string.file_photo_video_delete_yes_or_no) + CameraUtils.getPhotoSelectNums() + getString(C0853R.string.file_photo_video_photo_nums) + "";
                } else {
                    aaa = getString(C0853R.string.file_photo_video_delete) + " " + getString(C0853R.string.label_photo) + " " + CameraUtils.getPhotoSelectNums() + "?";
                }
            }
        } else if (Util.isZh(this.activity)) {
            aaa = getString(C0853R.string.file_photo_video_delete_yes_or_no) + CameraUtils.getPhotoSelectNums() + getString(C0853R.string.file_photo_video_delete_and) + CameraUtils.getVideoSelectNums() + getString(C0853R.string.file_photo_video_video_nums) + "";
        } else {
            aaa = getString(C0853R.string.file_photo_video_delete) + " " + getString(C0853R.string.label_photo) + " " + CameraUtils.getPhotoSelectNums() + " " + getString(C0853R.string.label_video) + " " + CameraUtils.getVideoSelectNums() + "?";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
        builder.setMessage((CharSequence) aaa);
        builder.setNegativeButton((int) C0853R.string.label_cancel, (DialogInterface.OnClickListener) null);
        builder.setPositiveButton((int) C0853R.string.label_sure, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                DataChangeNotification.getInstance().notifyDataChanged(IssueKey.PHOTO_VIDEO_DEIETE);
            }
        });
        builder.show();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 || event.getAction() != 0) {
            return super.onKeyDown(keyCode, event);
        }
        if (System.currentTimeMillis() - this.exitTime > 2000) {
            Toast.makeText(getApplicationContext(), C0853R.string.file_home_act_back, 0).show();
            this.exitTime = System.currentTimeMillis();
        } else {
            Util.deleteCatchPictrue();
            System.exit(0);
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: " + requestCode);
        Log.e(TAG, "onActivityResult: " + resultCode);
        if (resultCode == -1) {
            if (requestCode == 1) {
            }
            if (requestCode == 2) {
            }
        } else if (resultCode == 0) {
            if (requestCode == 1) {
                startActivity(new Intent(this, FVMainActivity.class));
                finish();
            }
            if (requestCode == 2) {
                startActivity(new Intent(this, FVMainActivity.class));
                finish();
            }
        } else if (resultCode == 2) {
            this.ptzMenuSwitcher.showPtzList();
        }
    }

    public void dateSelectChange() {
        this.activity_title_main_button.setVisibility(8);
        this.main_share_delete.setVisibility(0);
    }

    public void dateCancelChange() {
        this.activity_title_main_button.setVisibility(0);
        this.main_share_delete.setVisibility(8);
    }

    public void onDataChanged(IssueKey issue, Object o) {
        if (issue.equals(IssueKey.VIDEO_PHOTO_CAMERA_DELETE_CHECK_HAVA_NONE)) {
            if (CameraUtils.getPhotoSelectNums() == 0 && CameraUtils.getVideoSelectNums() == 0) {
                this.text_main_delete.setBackgroundResource(C0853R.C0854drawable.file_delete_button_selector_none);
                this.text_main_share.setBackgroundResource(C0853R.C0854drawable.file_share_button_selector_none);
            }
        } else if (!issue.equals(IssueKey.VIDEO_PHOTO_CAMERA_DELETE_CHECK_HAVA)) {
        } else {
            if (CameraUtils.getPhotoSelectNums() != 0 || CameraUtils.getVideoSelectNums() != 0) {
                this.text_main_delete.setBackgroundResource(C0853R.C0854drawable.file_delete_button_selector);
                this.text_main_share.setBackgroundResource(C0853R.C0854drawable.file_share_button_selector);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        getWindow().addFlags(128);
        Log.e("--------------", "-------- 666 ---- onResume ---");
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        Log.e("--------------", "-------- 666 ---- onPause ---");
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        Log.e("--------------", "-------- 666 ---- onStop ---");
    }
}
