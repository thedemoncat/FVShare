package com.freevisiontech.fvmobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.p001v4.view.MotionEventCompat;
import android.support.p001v4.view.ViewPager;
import android.support.p003v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.utility.BaseActivityManager;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utility.SPUtils;
import com.freevisiontech.fvmobile.utility.SharePrefConstant;
import com.freevisiontech.fvmobile.utility.Util;
import com.freevisiontech.fvmobile.utils.BleByteUtil;
import com.freevisiontech.fvmobile.utils.Event;
import com.freevisiontech.fvmobile.utils.HexUtil;
import com.freevisiontech.fvmobile.utils.ShareUtil;
import com.freevisiontech.fvmobile.widget.FVLookPhotoMessageDialog;
import com.freevisiontech.fvmobile.widget.FVPhotoVideoDeleteDialog;
import com.freevisiontech.fvmobile.widget.view.IntentKey;
import com.google.android.vending.expansion.downloader.Constants;
import com.umeng.analytics.MobclickAgent;
import com.vise.log.ViseLog;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import p010me.iwf.photopicker.fragment.ImagePagerFragment;
import p010me.iwf.photopicker.utils.PpEvent;
import p010me.iwf.photopicker.utils.PpEventConstant;
import p010me.iwf.photopicker.widget.control.DataChangeNotification;
import p010me.iwf.photopicker.widget.control.IssueKey;
import p010me.iwf.photopicker.widget.control.OnDataChangeObserver;

public class LookPhotoActivity extends AppCompatActivity implements OnDataChangeObserver, View.OnClickListener {
    @Bind({2131756083})
    LinearLayout act_layout_file_buttom;
    @Bind({2131756126})
    RelativeLayout act_look_photo_title;
    /* access modifiers changed from: private */
    public Activity activity;
    private FVLookPhotoMessageDialog fVLookPhotoMessageDialog;
    /* access modifiers changed from: private */
    public FVPhotoVideoDeleteDialog fVPhotoVideoDeleteDialog;
    @Bind({2131755249})
    ImageView img_back;
    @Bind({2131756129})
    ImageView img_right;
    /* access modifiers changed from: private */
    public Boolean lookYesOrNo;
    private MyCountDownTimer mMyCountDownTimer;
    private TextView mPhotoSave;
    /* access modifiers changed from: private */
    public ImagePagerFragment pagerFragment;
    /* access modifiers changed from: private */
    public String pathOne;
    /* access modifiers changed from: private */
    public String pathTwo;
    /* access modifiers changed from: private */
    public List<String> paths = new ArrayList();
    /* access modifiers changed from: private */
    public int position;
    /* access modifiers changed from: private */
    public ShareUtil shareUtil;
    /* access modifiers changed from: private */
    public List<String> strings;
    private TextView textView;
    @Bind({2131756084})
    ImageButton text_main_chuangzuo;
    @Bind({2131755374})
    ImageButton text_main_delete;
    @Bind({2131755243})
    ImageButton text_main_share;
    @Bind({2131756127})
    TextView tv_center_title;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = this;
        setContentView((int) C0853R.layout.activity_look_photo);
        ButterKnife.bind((Activity) this);
        Util.setFullScreen(this);
        BaseActivityManager.getActivityManager().pushActivity(this);
        this.shareUtil = new ShareUtil(this);
        this.lookYesOrNo = true;
        CameraUtils.setHasTakePhotoOrVideo(false);
        this.img_back.setVisibility(0);
        this.img_right.setVisibility(0);
        this.img_back.setOnClickListener(this);
        this.img_right.setImageResource(C0853R.mipmap.bangzhu);
        this.img_right.setOnClickListener(this);
        this.textView = (TextView) findViewById(C0853R.C0855id.photo_page);
        this.mPhotoSave = (TextView) findViewById(C0853R.C0855id.photo_save);
        this.mPhotoSave.setOnClickListener(this);
        this.text_main_delete.setOnClickListener(this);
        this.text_main_chuangzuo.setOnClickListener(this);
        DataChangeNotification.getInstance().addObserver(IssueKey.LOOK_PHOTO_TOP_BUTTOM, this);
        int currentItem = getIntent().getIntExtra(IntentKey.CLASS_POSION, 0);
        this.strings = getIntent().getStringArrayListExtra(IntentKey.CLASS_JSON);
        if (getIntent().getBooleanExtra(IntentKey.SHOW_TEXTVIEW, true)) {
            this.textView.setVisibility(0);
        } else {
            this.textView.setVisibility(8);
        }
        this.textView.setVisibility(8);
        this.paths.clear();
        for (String str : this.strings) {
            if (str != null && str.length() > 0) {
                if (!((String) SPUtils.get(this, SharePrefConstant.SAVE_STORAGE_SD_CARD_PATH, IntentKey.FILE_PATH)).contains(Constants.FILENAME_SEQUENCE_SEPARATOR)) {
                    this.paths.add(IntentKey.FILE_PATH + str);
                } else {
                    this.paths.add(Util.getParentPath(this) + str);
                }
            }
        }
        this.pagerFragment = (ImagePagerFragment) getSupportFragmentManager().findFragmentById(C0853R.C0855id.photoPagerFragment);
        this.pagerFragment.setPhotos(this.paths, currentItem);
        updateActionBarTitle();
        this.pagerFragment.getViewPager().addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e("LookPhoto:======", position + "");
                LookPhotoActivity.this.updateActionBarTitle();
            }
        });
        this.text_main_share.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String basePath;
                if (!((String) SPUtils.get(LookPhotoActivity.this.activity, SharePrefConstant.SAVE_STORAGE_SD_CARD_PATH, IntentKey.FILE_PATH)).contains(Constants.FILENAME_SEQUENCE_SEPARATOR)) {
                    basePath = IntentKey.FILE_PATH;
                } else {
                    basePath = Util.getParentPath(LookPhotoActivity.this.activity);
                }
                LookPhotoActivity.this.shareUtil.shareImg((String) null, (String) null, new File(basePath + LookPhotoActivity.this.pathTwo));
            }
        });
        if (this.mMyCountDownTimer != null) {
            this.mMyCountDownTimer.cancel();
            this.mMyCountDownTimer = new MyCountDownTimer(3000, 1000);
            this.mMyCountDownTimer.start();
        } else {
            this.mMyCountDownTimer = new MyCountDownTimer(3000, 1000);
            this.mMyCountDownTimer.start();
        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        this.fVLookPhotoMessageDialog = new FVLookPhotoMessageDialog(this, this.pathTwo);
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
    }

    public void onResume() {
        super.onResume();
        Util.setFullScreen(this);
        MobclickAgent.onResume(this);
        BleByteUtil.setPTZParameters((byte) 71, (byte) 1);
    }

    /* access modifiers changed from: protected */
    public void onRestart() {
        super.onRestart();
        Util.setFullScreen(this);
    }

    public void updateActionBarTitle() {
        this.position = this.pagerFragment.getViewPager().getCurrentItem();
        this.pathOne = this.paths.get(this.pagerFragment.getViewPager().getCurrentItem());
        this.pathTwo = this.strings.get(this.pagerFragment.getViewPager().getCurrentItem());
        this.textView.setText((this.pagerFragment.getViewPager().getCurrentItem() + 1) + "/" + this.pagerFragment.getPaths().size());
        this.tv_center_title.setText((this.pagerFragment.getViewPager().getCurrentItem() + 1) + "/" + this.pagerFragment.getPaths().size());
        if (this.mMyCountDownTimer != null) {
            this.mMyCountDownTimer.cancel();
            this.mMyCountDownTimer = new MyCountDownTimer(3000, 1000);
            this.mMyCountDownTimer.start();
            return;
        }
        this.mMyCountDownTimer = new MyCountDownTimer(3000, 1000);
        this.mMyCountDownTimer.start();
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        BleByteUtil.setPTZParameters((byte) 71, (byte) 0);
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
    }

    public void onClick(View v) {
        String basePath;
        switch (v.getId()) {
            case C0853R.C0855id.img_back:
                finish();
                return;
            case C0853R.C0855id.text_main_delete:
                this.fVPhotoVideoDeleteDialog = new FVPhotoVideoDeleteDialog(this);
                this.fVPhotoVideoDeleteDialog.show();
                this.fVPhotoVideoDeleteDialog.setButtonOnClick(new FVPhotoVideoDeleteDialog.CheckButtonOnclick() {
                    public void onClick(View view) {
                        LookPhotoActivity.this.fVPhotoVideoDeleteDialog.finish();
                    }
                });
                this.fVPhotoVideoDeleteDialog.setButtonSureOnClick(new FVPhotoVideoDeleteDialog.CheckButtonSureOnclick() {
                    public void onClick(View view) {
                        new File(LookPhotoActivity.this.pathOne).delete();
                        LookPhotoActivity.this.activity.getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "_data='" + LookPhotoActivity.this.pathOne + "'", (String[]) null);
                        CameraUtils.setHasTakePhotoOrVideo(true);
                        LookPhotoActivity.this.fVPhotoVideoDeleteDialog.finish();
                        if (LookPhotoActivity.this.paths.size() > 0) {
                            LookPhotoActivity.this.paths.remove(LookPhotoActivity.this.position);
                            LookPhotoActivity.this.strings.remove(LookPhotoActivity.this.position);
                        }
                        if (LookPhotoActivity.this.paths.size() > 0) {
                            LookPhotoActivity.this.pagerFragment.setPhotos(LookPhotoActivity.this.paths, LookPhotoActivity.this.position);
                            LookPhotoActivity.this.updateActionBarTitle();
                            return;
                        }
                        LookPhotoActivity.this.finish();
                    }
                });
                return;
            case C0853R.C0855id.text_main_chuangzuo:
                Intent intent = new Intent(this.activity, PhotoEditActivity.class);
                if (!((String) SPUtils.get(this.activity, SharePrefConstant.SAVE_STORAGE_SD_CARD_PATH, IntentKey.FILE_PATH)).contains(Constants.FILENAME_SEQUENCE_SEPARATOR)) {
                    basePath = IntentKey.FILE_PATH;
                } else {
                    basePath = Util.getParentPath(this.activity);
                }
                intent.putExtra(IntentKey.PHOTOEDITPATH, basePath + this.pathTwo);
                startActivity(intent);
                return;
            case C0853R.C0855id.img_right:
                this.fVLookPhotoMessageDialog.updatePath(this.pathTwo);
                this.fVLookPhotoMessageDialog.show();
                return;
            default:
                return;
        }
    }

    public void onDataChanged(IssueKey issue, Object o) {
        if (!issue.equals(IssueKey.LOOK_PHOTO_TOP_BUTTOM)) {
            return;
        }
        if (this.lookYesOrNo.booleanValue()) {
            this.lookYesOrNo = false;
            setTopButtomGoneAndVisible(this.lookYesOrNo);
            return;
        }
        this.lookYesOrNo = true;
        setTopButtomGoneAndVisible(this.lookYesOrNo);
    }

    private void setTopButtomGoneAndVisible(Boolean display) {
        Util.setFullScreen(this);
        if (!this.activity.getSharedPreferences("user", 0).getString("camera", "").equals("0")) {
            return;
        }
        if (display.booleanValue()) {
            ((RelativeLayout) findViewById(C0853R.C0855id.act_look_photo_title)).setVisibility(0);
            ((LinearLayout) findViewById(C0853R.C0855id.act_layout_file_buttom)).setVisibility(0);
            if (this.mMyCountDownTimer != null) {
                this.mMyCountDownTimer.cancel();
                this.mMyCountDownTimer = new MyCountDownTimer(3000, 1000);
                this.mMyCountDownTimer.start();
                return;
            }
            this.mMyCountDownTimer = new MyCountDownTimer(3000, 1000);
            this.mMyCountDownTimer.start();
            return;
        }
        ((RelativeLayout) findViewById(C0853R.C0855id.act_look_photo_title)).setVisibility(8);
        ((LinearLayout) findViewById(C0853R.C0855id.act_layout_file_buttom)).setVisibility(8);
    }

    class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onTick(long millisUntilFinished) {
        }

        public void onFinish() {
            ((RelativeLayout) LookPhotoActivity.this.findViewById(C0853R.C0855id.act_look_photo_title)).setVisibility(8);
            ((LinearLayout) LookPhotoActivity.this.findViewById(C0853R.C0855id.act_layout_file_buttom)).setVisibility(8);
            Boolean unused = LookPhotoActivity.this.lookYesOrNo = false;
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        if (this.mMyCountDownTimer != null) {
            this.mMyCountDownTimer.cancel();
        }
        ButterKnife.unbind(this);
        if (CameraUtils.getHasTakePhotoOrVideo()) {
            DataChangeNotification.getInstance().notifyDataChanged(IssueKey.PHOTO_VIDEO_DEIETE);
            CameraUtils.setHasTakePhotoOrVideo(false);
        }
        DataChangeNotification.getInstance().removeObserver((OnDataChangeObserver) this);
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCome(Event event) {
        if (event != null) {
            receiveEvent(event);
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
        if ((value[1] & 255) == 70) {
            ViseLog.m1466e("ViltaX--0x46--data--" + HexUtil.encodeHexStr(value));
            switch (value[2] & 255) {
                case 1:
                    ViseLog.m1466e("ViltaX" + getLocalClassName() + "1：菜单键单击：菜单/返回");
                    BleByteUtil.ackPTZPanorama((byte) 70, (byte) 1);
                    if (this.fVLookPhotoMessageDialog.isShowing()) {
                        this.fVLookPhotoMessageDialog.dismiss();
                        return;
                    } else {
                        finish();
                        return;
                    }
                case 2:
                    ViseLog.m1466e("ViltaX2：菜单键长按：回到拍照界面");
                    BleByteUtil.ackPTZPanorama((byte) 70, (byte) 2);
                    startActivity(new Intent(this, FVMainActivity.class));
                    finish();
                    return;
                case 3:
                    BleByteUtil.ackPTZPanorama((byte) 70, (byte) 3);
                    if (this.fVLookPhotoMessageDialog.isShowing()) {
                        this.fVLookPhotoMessageDialog.dismiss();
                        return;
                    } else {
                        this.img_right.performClick();
                        return;
                    }
                default:
                    return;
            }
        } else if ((value[1] & 255) == 72) {
            switch (value[2] & 255) {
                case 1:
                    BleByteUtil.ackPTZPanorama((byte) 72, (byte) 1);
                    if (this.pagerFragment.getViewPager().getCurrentItem() < this.pagerFragment.getViewPager().getAdapter().getCount() - 1) {
                        this.pagerFragment.getViewPager().setCurrentItem(this.pagerFragment.getViewPager().getCurrentItem() + 1);
                        return;
                    }
                    return;
                case 2:
                    BleByteUtil.ackPTZPanorama((byte) 72, (byte) 2);
                    if (this.pagerFragment.getViewPager().getCurrentItem() > 0) {
                        this.pagerFragment.getViewPager().setCurrentItem(this.pagerFragment.getViewPager().getCurrentItem() - 1);
                        return;
                    }
                    return;
                default:
                    return;
            }
        } else if ((value[1] & 255) == 53) {
            int delta = ((value[2] << 0) & 255) + ((value[3] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK);
            if (delta > 32768) {
                delta -= 65536;
            }
            if (delta < 0) {
                EventBus.getDefault().post(new PpEvent(PpEventConstant.STATUS_PTZ_ZOOM_BIG));
            } else {
                EventBus.getDefault().post(new PpEvent(PpEventConstant.STATUS_PTZ_ZOOM_SMALL));
            }
        }
    }
}
