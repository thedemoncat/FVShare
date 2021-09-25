package com.freevisiontech.fvmobile.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.p001v4.app.FragmentActivity;
import android.support.p001v4.view.MotionEventCompat;
import android.support.p001v4.view.ViewPager;
import android.support.p003v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.adapter.FragmentFileAdapter;
import com.freevisiontech.fvmobile.application.MyApplication;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.freevisiontech.fvmobile.utility.BaseActivityManager;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utility.Constants;
import com.freevisiontech.fvmobile.utility.SPUtils;
import com.freevisiontech.fvmobile.utility.SharePrefConstant;
import com.freevisiontech.fvmobile.utility.Util;
import com.freevisiontech.fvmobile.utils.BleByteUtil;
import com.freevisiontech.fvmobile.utils.Event;
import com.freevisiontech.fvmobile.utils.EventBusUtil;
import com.freevisiontech.fvmobile.utils.HexUtil;
import com.umeng.analytics.MobclickAgent;
import com.vise.log.ViseLog;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import p010me.iwf.photopicker.widget.control.DataChangeNotification;
import p010me.iwf.photopicker.widget.control.IssueKey;
import p010me.iwf.photopicker.widget.control.OnDataChangeObserver;

public class FVCameraFileTwoActivity extends FragmentActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, OnDataChangeObserver {
    public static final int SHIPING = 1;
    public static final int ZHAOPIAN = 0;
    private Activity activity;
    private ViewPager fileViewPager;
    private ImageView img_file_back;
    private boolean mIsFM210ButtonsEnable = true;
    private boolean mIsImageTabSelected = true;
    private RelativeLayout main_share_delete;
    private TextView right_cancel;
    private TextView right_select;
    private int textColorCli;
    private int textColorNor;
    private List<TextView> textViews = new ArrayList();
    private ImageView text_main_delete;
    private ImageView text_main_share;
    private TextView title_photo;
    private TextView title_video;
    private TextView tv_all_right;
    private RelativeLayout tv_all_right_cancel_select;
    private TextView tv_center_title;
    private View view;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0853R.layout.activity_camera_file_two);
        getWindow().setFlags(1024, 1024);
        Util.hideBottomUIMenu(this);
        this.activity = this;
        BaseActivityManager.getActivityManager().pushActivity(this);
        this.text_main_share = (ImageView) findViewById(C0853R.C0855id.text_main_share);
        this.text_main_delete = (ImageView) findViewById(C0853R.C0855id.text_main_camera_delete);
        this.main_share_delete = (RelativeLayout) findViewById(C0853R.C0855id.main_share_delete);
        this.tv_all_right_cancel_select = (RelativeLayout) findViewById(C0853R.C0855id.tv_all_right_cancel_select);
        this.text_main_delete.setBackgroundResource(C0853R.C0854drawable.file_delete_button_selector_none);
        this.text_main_delete.setOnClickListener(this);
        initTitle();
        ininview();
        DataChangeNotification.getInstance().addObserver(IssueKey.VIDEO_PHOTO_HAVE_OR_NONE, this);
        DataChangeNotification.getInstance().addObserver(IssueKey.VIDEO_PHOTO_CAMERA_DELETE_END, this);
        DataChangeNotification.getInstance().addObserver(IssueKey.VIDEO_PHOTO_CAMERA_DELETE_CHECK_HAVA, this);
        DataChangeNotification.getInstance().addObserver(IssueKey.VIDEO_PHOTO_CAMERA_DELETE_CHECK_HAVA_NONE, this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
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
        if (CameraUtils.getCurrentPageIndex() == 2) {
            this.textColorCli = getResources().getColor(C0853R.color.color_tab_select);
        } else {
            this.textColorCli = getResources().getColor(C0853R.color.color_black1);
        }
        this.tv_all_right.setOnClickListener(this);
        this.right_cancel.setOnClickListener(this);
        this.right_select.setOnClickListener(this);
        this.img_file_back.setOnClickListener(this);
        this.title_photo.setOnClickListener(this);
        this.title_video.setOnClickListener(this);
        getSharedPreferences("user", 0).edit().putString("camera", "0").commit();
    }

    private void ininview() {
        this.fileViewPager = (ViewPager) findViewById(C0853R.C0855id.file_view_pager);
        FragmentFileAdapter adapter = new FragmentFileAdapter(getSupportFragmentManager());
        this.fileViewPager.setAdapter(adapter);
        this.fileViewPager.addOnPageChangeListener(this);
        if (((Boolean) SPUtils.get(this, SharePrefConstant.LAST_TAB_IMAGE_SELECTED, false)).booleanValue()) {
            this.fileViewPager.setCurrentItem(0);
            this.title_photo.setTextColor(this.textColorCli);
            this.title_video.setTextColor(this.textColorNor);
        } else {
            this.fileViewPager.setCurrentItem(1);
            this.title_photo.setTextColor(this.textColorNor);
            this.title_video.setTextColor(this.textColorCli);
        }
        adapter.notifyDataSetChanged();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0853R.C0855id.text_main_camera_delete:
                if (CameraUtils.getPhotoSelectNums() + CameraUtils.getVideoSelectNums() != 0) {
                    deleteDialog();
                    return;
                }
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
                CameraUtils.setPhotoSelectNums(0);
                CameraUtils.setVideoSelectNums(0);
                return;
            case C0853R.C0855id.frg_file_title_photo:
                this.fileViewPager.setCurrentItem(0);
                getSharedPreferences("user", 0).edit().putString("camera", "0").commit();
                SPUtils.put(this, SharePrefConstant.LAST_TAB_IMAGE_SELECTED, true);
                return;
            case C0853R.C0855id.frg_file_title_video:
                this.fileViewPager.setCurrentItem(1);
                getSharedPreferences("user", 0).edit().putString("camera", BleConstant.SHUTTER).commit();
                SPUtils.put(this, SharePrefConstant.LAST_TAB_IMAGE_SELECTED, false);
                return;
            default:
                return;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCome(Event event) {
        if (event != null) {
            ViseLog.m1466e("ViltaX--mIsFM210ButtonsEnable--" + this.mIsFM210ButtonsEnable);
            if (this.mIsFM210ButtonsEnable) {
                receiveEvent(event);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void receiveEvent(Event event) {
        switch (event.getCode()) {
            case 119:
                byte[] value = (byte[]) event.getData();
                if ((value[0] & 255) == 90) {
                    processDataForX(value);
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void processDataForX(byte[] value) {
        if ((value[0] & 255) != 90) {
            return;
        }
        if ((value[1] & 255) == 72) {
            ViseLog.m1466e("ViltaX--0x48--data--" + HexUtil.encodeHexStr(value));
            Log.i("KBein", "FVCameraFileTwoActivity.processDataForX():--0x48--data--" + HexUtil.encodeHexStr(value));
            switch (value[2] & 255) {
                case 1:
                    Log.i("KBein", "FVCameraFileTwoActivity.processDataForX():----→");
                    BleByteUtil.ackPTZPanorama((byte) 72, (byte) 1);
                    if (this.mIsImageTabSelected) {
                        EventBusUtil.sendEvent(new Event(67));
                        return;
                    } else {
                        EventBusUtil.sendEvent(new Event(71));
                        return;
                    }
                case 2:
                    Log.i("KBein", "FVCameraFileTwoActivity.processDataForX():----←");
                    BleByteUtil.ackPTZPanorama((byte) 72, (byte) 2);
                    if (this.mIsImageTabSelected) {
                        EventBusUtil.sendEvent(new Event(66));
                        return;
                    } else {
                        EventBusUtil.sendEvent(new Event(70));
                        return;
                    }
                case 3:
                    Log.i("KBein", "FVCameraFileTwoActivity.processDataForX():----↑");
                    BleByteUtil.ackPTZPanorama((byte) 72, (byte) 3);
                    if (this.mIsImageTabSelected) {
                        EventBusUtil.sendEvent(new Event(64));
                        return;
                    } else {
                        EventBusUtil.sendEvent(new Event(68));
                        return;
                    }
                case 4:
                    Log.i("KBein", "FVCameraFileTwoActivity.processDataForX():----↓");
                    BleByteUtil.ackPTZPanorama((byte) 72, (byte) 4);
                    if (this.mIsImageTabSelected) {
                        EventBusUtil.sendEvent(new Event(65));
                        return;
                    } else {
                        EventBusUtil.sendEvent(new Event(69));
                        return;
                    }
                default:
                    return;
            }
        } else if ((value[1] & 255) == 70) {
            ViseLog.m1466e("ViltaX--0x46--data--" + HexUtil.encodeHexStr(value));
            Log.i("KBein", "FVCameraFileTwoActivity.processDataForX():--0x46--data--" + HexUtil.encodeHexStr(value));
            switch (value[2] & 255) {
                case 1:
                    ViseLog.m1466e("ViltaX" + getLocalClassName() + "1：菜单键单击：菜单/返回");
                    BleByteUtil.ackPTZPanorama((byte) 70, (byte) 1);
                    finish();
                    return;
                case 2:
                    ViseLog.m1466e("ViltaX2：菜单键长按：回到拍照界面");
                    BleByteUtil.ackPTZPanorama((byte) 70, (byte) 2);
                    startActivity(new Intent(this, FVMainActivity.class));
                    finish();
                    return;
                case 3:
                    BleByteUtil.ackPTZPanorama((byte) 70, (byte) 3);
                    if (this.mIsImageTabSelected) {
                        EventBusUtil.sendEvent(new Event(80));
                        return;
                    } else {
                        EventBusUtil.sendEvent(new Event(81));
                        return;
                    }
                default:
                    return;
            }
        } else if ((value[1] & 255) == 53) {
            ViseLog.m1466e("ViltaX--0x35--data--" + HexUtil.encodeHexStr(value));
            Log.i("KBein", "FVCameraFileTwoActivity.processDataForX():--0x35--data--" + HexUtil.encodeHexStr(value));
            Log.i("KBein", "FVCameraFileTwoActivity.processDataForX():--ptzType11--" + ((String) SPUtils.get(this, SharePrefConstant.CURRENT_PTZ_TYPE, "")));
            String ptzType = MyApplication.CURRENT_PTZ_TYPE;
            Log.i("KBein", "FVCameraFileTwoActivity.processDataForX():--ptzType22--" + ptzType);
            if (ptzType.equals(BleConstant.FM_210)) {
                int delta = ((value[2] << 0) & 255) + ((value[3] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK);
                if (delta > 32768) {
                    delta -= 65536;
                }
                ViseLog.m1466e("ViltaX--0x35--delta: " + delta);
                Log.i("KBein", "FVCameraFileTwoActivity.processDataForX():--0x35--delta: " + delta);
                if (delta < 0) {
                    this.title_video.performClick();
                    this.mIsImageTabSelected = false;
                    SPUtils.put(this, SharePrefConstant.LAST_TAB_IMAGE_SELECTED, false);
                    return;
                }
                this.title_photo.performClick();
                this.mIsImageTabSelected = true;
                SPUtils.put(this, SharePrefConstant.LAST_TAB_IMAGE_SELECTED, true);
            }
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

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                this.title_photo.setTextColor(this.textColorCli);
                this.title_video.setTextColor(this.textColorNor);
                getSharedPreferences("user", 0).edit().putString("camera", "0").commit();
                this.mIsImageTabSelected = true;
                SPUtils.put(this, SharePrefConstant.LAST_TAB_IMAGE_SELECTED, true);
                return;
            case 1:
                this.title_photo.setTextColor(this.textColorNor);
                this.title_video.setTextColor(this.textColorCli);
                getSharedPreferences("user", 0).edit().putString("camera", BleConstant.SHUTTER).commit();
                this.mIsImageTabSelected = false;
                SPUtils.put(this, SharePrefConstant.LAST_TAB_IMAGE_SELECTED, false);
                return;
            default:
                return;
        }
    }

    public void onPageScrollStateChanged(int state) {
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"MissingSuperCall"})
    public void onSaveInstanceState(Bundle outState) {
    }

    public void onDataChanged(IssueKey issue, Object o) {
        if (issue.equals(IssueKey.VIDEO_PHOTO_HAVE_OR_NONE)) {
            if (CameraUtils.getPhotoNums() == 0 && CameraUtils.getVideoNums() == 0) {
                this.main_share_delete.setVisibility(8);
                this.tv_all_right_cancel_select.setVisibility(8);
                return;
            }
            this.tv_all_right_cancel_select.setVisibility(0);
        } else if (issue.equals(IssueKey.VIDEO_PHOTO_CAMERA_DELETE_END)) {
            if (CameraUtils.getPhotoSelectNums() == 0 && CameraUtils.getVideoSelectNums() == 0) {
                this.tv_all_right.setVisibility(8);
                this.right_cancel.setVisibility(8);
                this.right_select.setVisibility(0);
                this.main_share_delete.setVisibility(8);
                DataChangeNotification.getInstance().notifyDataChanged(IssueKey.CAMERA_CHECK_LOOK);
            }
        } else if (issue.equals(IssueKey.VIDEO_PHOTO_CAMERA_DELETE_CHECK_HAVA_NONE)) {
            if (CameraUtils.getPhotoSelectNums() == 0 && CameraUtils.getVideoSelectNums() == 0) {
                this.text_main_delete.setBackgroundResource(C0853R.C0854drawable.file_delete_button_selector_none);
            }
        } else if (!issue.equals(IssueKey.VIDEO_PHOTO_CAMERA_DELETE_CHECK_HAVA)) {
        } else {
            if (CameraUtils.getPhotoSelectNums() != 0 || CameraUtils.getVideoSelectNums() != 0) {
                this.text_main_delete.setBackgroundResource(C0853R.C0854drawable.file_delete_button_selector);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        Util.hideBottomUIMenu(this);
        MobclickAgent.onPause(this);
        BleByteUtil.setPTZParameters((byte) 71, (byte) 0);
        this.mIsFM210ButtonsEnable = false;
    }

    /* access modifiers changed from: protected */
    public void onRestart() {
        super.onRestart();
        Util.hideBottomUIMenu(this);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        Util.sendIntEventMessge(Constants.LABEL_CAMERA_RESTART);
        BaseActivityManager.getActivityManager().popActivityOne(FVCameraFileTwoActivity.class);
        DataChangeNotification.getInstance().removeObserver((OnDataChangeObserver) this);
        EventBus.getDefault().unregister(this);
        BleByteUtil.setPTZParameters((byte) 71, (byte) 0);
        CameraUtils.setLabelTopBarSelect(-1);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        BleByteUtil.setPTZParameters((byte) 71, (byte) 1);
        this.mIsFM210ButtonsEnable = true;
    }
}
