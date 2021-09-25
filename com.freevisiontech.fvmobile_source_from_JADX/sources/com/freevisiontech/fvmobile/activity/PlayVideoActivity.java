package com.freevisiontech.fvmobile.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.freevisiontech.fvmobile.utility.BaseActivityManager;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utility.SPUtils;
import com.freevisiontech.fvmobile.utility.SharePrefConstant;
import com.freevisiontech.fvmobile.utility.Util;
import com.freevisiontech.fvmobile.utils.BleByteUtil;
import com.freevisiontech.fvmobile.utils.Event;
import com.freevisiontech.fvmobile.utils.HexUtil;
import com.freevisiontech.fvmobile.utils.ShareUtil;
import com.freevisiontech.fvmobile.widget.FVLookVideoMessageDialog;
import com.freevisiontech.fvmobile.widget.FVPhotoVideoDeleteDialog;
import com.freevisiontech.fvmobile.widget.view.IntentKey;
import com.google.android.vending.expansion.downloader.Constants;
import com.p007ny.ijk.upplayer.utils.UpEvent;
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

public class PlayVideoActivity extends AppCompatActivity implements View.OnClickListener, OnDataChangeObserver {
    @Bind({2131756083})
    LinearLayout act_layout_file_buttom;
    @Bind({2131756126})
    RelativeLayout act_look_photo_title;
    private int currentItem;
    private FVLookVideoMessageDialog fVLookVideoMessageDialog;
    /* access modifiers changed from: private */
    public FVPhotoVideoDeleteDialog fVPhotoVideoDeleteDialog;
    @Bind({2131755249})
    ImageView img_back;
    @Bind({2131756129})
    ImageView img_right;
    /* access modifiers changed from: private */
    public Boolean lookYesOrNo;
    /* access modifiers changed from: private */
    public Context mContext;
    private boolean mIsPlaying = false;
    private MyCountDownTimer mMyCountDownTimer;
    private TextView mPhotoSave;
    /* access modifiers changed from: private */
    public ImagePagerFragment pagerFragment;
    /* access modifiers changed from: private */
    public String pathTwo;
    /* access modifiers changed from: private */
    public List<String> paths = new ArrayList();
    /* access modifiers changed from: private */
    public int position;
    private int positionItem;
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
        setContentView((int) C0853R.layout.activity_play_video);
        ButterKnife.bind((Activity) this);
        this.img_back.setVisibility(0);
        this.img_back.setOnClickListener(this);
        this.shareUtil = new ShareUtil(this);
        this.lookYesOrNo = true;
        this.mContext = this;
        Util.setFullScreen(this);
        BaseActivityManager.getActivityManager().pushActivity(this);
        DataChangeNotification.getInstance().addObserver(IssueKey.VIDEO_PLAY, this);
        DataChangeNotification.getInstance().addObserver(IssueKey.LOOK_VIDEO_TOP_BUTTOM, this);
        CameraUtils.setHasTakePhotoOrVideo(false);
        this.textView = (TextView) findViewById(C0853R.C0855id.photo_page);
        this.mPhotoSave = (TextView) findViewById(C0853R.C0855id.photo_save);
        this.mPhotoSave.setOnClickListener(this);
        this.text_main_chuangzuo.setOnClickListener(this);
        this.img_right.setVisibility(0);
        this.img_right.setImageResource(C0853R.mipmap.bangzhu);
        this.img_right.setOnClickListener(this);
        this.currentItem = getIntent().getIntExtra(IntentKey.CLASS_POSION, 0);
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
        this.pagerFragment.setPhotos(this.paths, this.currentItem);
        Log.e("ViltaX", "LookPhoto:======pagerFragment.setPhotos(paths, currentItem):" + this.paths.size() + this.paths.get(0));
        PpEventConstant.FM210_VIDEO_POSITION = this.currentItem;
        updateActionBarTitle();
        this.pagerFragment.getViewPager().addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                PlayVideoActivity.this.updateActionBarTitle();
            }

            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }

            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Log.e("ViltaX", "LookPhoto:======onPageSelected position:  " + position);
                PpEventConstant.FM210_VIDEO_POSITION = position;
            }
        });
        this.text_main_share.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String basePath;
                if (!((String) SPUtils.get(PlayVideoActivity.this.mContext, SharePrefConstant.SAVE_STORAGE_SD_CARD_PATH, IntentKey.FILE_PATH)).contains(Constants.FILENAME_SEQUENCE_SEPARATOR)) {
                    basePath = IntentKey.FILE_PATH;
                } else {
                    basePath = Util.getParentPath(PlayVideoActivity.this.mContext);
                }
                PlayVideoActivity.this.shareUtil.shareVideo((String) null, (String) null, new File(basePath + PlayVideoActivity.this.pathTwo));
            }
        });
        this.text_main_delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FVPhotoVideoDeleteDialog unused = PlayVideoActivity.this.fVPhotoVideoDeleteDialog = new FVPhotoVideoDeleteDialog(PlayVideoActivity.this);
                PlayVideoActivity.this.fVPhotoVideoDeleteDialog.show();
                PlayVideoActivity.this.fVPhotoVideoDeleteDialog.setButtonOnClick(new FVPhotoVideoDeleteDialog.CheckButtonOnclick() {
                    public void onClick(View view) {
                        PlayVideoActivity.this.fVPhotoVideoDeleteDialog.finish();
                    }
                });
                PlayVideoActivity.this.fVPhotoVideoDeleteDialog.setButtonSureOnClick(new FVPhotoVideoDeleteDialog.CheckButtonSureOnclick() {
                    public void onClick(View view) {
                        String basePath;
                        if (!((String) SPUtils.get(PlayVideoActivity.this.mContext, SharePrefConstant.SAVE_STORAGE_SD_CARD_PATH, IntentKey.FILE_PATH)).contains(Constants.FILENAME_SEQUENCE_SEPARATOR)) {
                            basePath = IntentKey.FILE_PATH;
                        } else {
                            basePath = Util.getParentPath(PlayVideoActivity.this.mContext);
                        }
                        File fileVideo = new File(basePath + PlayVideoActivity.this.pathTwo);
                        if (fileVideo != null) {
                            fileVideo.delete();
                            PlayVideoActivity.this.fVPhotoVideoDeleteDialog.finish();
                            CameraUtils.setHasTakePhotoOrVideo(true);
                            Util.updataMediaStore(PlayVideoActivity.this.mContext, fileVideo);
                            PlayVideoActivity.this.fVPhotoVideoDeleteDialog.finish();
                            if (PlayVideoActivity.this.paths.size() > 0) {
                                PlayVideoActivity.this.paths.remove(PlayVideoActivity.this.position);
                                PlayVideoActivity.this.strings.remove(PlayVideoActivity.this.position);
                            }
                            if (PlayVideoActivity.this.paths.size() > 0) {
                                PlayVideoActivity.this.pagerFragment.setPhotos(PlayVideoActivity.this.paths, PlayVideoActivity.this.position);
                                PlayVideoActivity.this.updateActionBarTitle();
                                return;
                            }
                            PlayVideoActivity.this.finish();
                        }
                    }
                });
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
        this.fVLookVideoMessageDialog = new FVLookVideoMessageDialog(this, this.pathTwo);
    }

    public void updateActionBarTitle() {
        this.position = this.pagerFragment.getViewPager().getCurrentItem();
        this.positionItem = this.pagerFragment.getViewPager().getCurrentItem();
        this.pathTwo = this.strings.get(this.positionItem);
        this.textView.setText((this.positionItem + 1) + "/" + this.pagerFragment.getPaths().size());
        this.tv_center_title.setText((this.positionItem + 1) + "/" + this.pagerFragment.getPaths().size());
        if (this.mMyCountDownTimer != null) {
            this.mMyCountDownTimer.cancel();
            this.mMyCountDownTimer = new MyCountDownTimer(3000, 1000);
            this.mMyCountDownTimer.start();
            return;
        }
        this.mMyCountDownTimer = new MyCountDownTimer(3000, 1000);
        this.mMyCountDownTimer.start();
    }

    public void onDataChanged(IssueKey issue, Object o) {
        if (IssueKey.VIDEO_PLAY.equals(issue)) {
            String str = this.paths.get(this.positionItem);
            startActivity(ControllerActivity.createIntent(this, this.paths, String.valueOf(this.positionItem)));
        } else if (!issue.equals(IssueKey.LOOK_VIDEO_TOP_BUTTOM)) {
        } else {
            if (this.lookYesOrNo.booleanValue()) {
                this.lookYesOrNo = false;
                setTopButtomGoneAndVisible(this.lookYesOrNo);
                return;
            }
            this.lookYesOrNo = true;
            setTopButtomGoneAndVisible(this.lookYesOrNo);
        }
    }

    private void setTopButtomGoneAndVisible(Boolean display) {
        Util.setFullScreen(this);
        if (!getSharedPreferences("user", 0).getString("camera", "").equals(BleConstant.SHUTTER)) {
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

    public void onResume() {
        super.onResume();
        Util.setFullScreen(this);
        MobclickAgent.onResume(this);
        this.mIsPlaying = false;
        BleByteUtil.setPTZParameters((byte) 71, (byte) 1);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        this.mIsPlaying = true;
        BleByteUtil.setPTZParameters((byte) 71, (byte) 0);
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    /* access modifiers changed from: protected */
    public void onRestart() {
        super.onRestart();
        Util.setFullScreen(this);
    }

    public void onClick(View v) {
        String basePath;
        switch (v.getId()) {
            case C0853R.C0855id.img_back:
                finish();
                return;
            case C0853R.C0855id.text_main_chuangzuo:
                if (this.pathTwo.substring(this.pathTwo.length() - 10, this.pathTwo.length() - 4).equals("yidong") || this.pathTwo.substring(this.pathTwo.length() - 13, this.pathTwo.length() - 4).equals("TimeLapse")) {
                    Toast.makeText(this, C0853R.string.file_edit_video_no, 0).show();
                    return;
                }
                List list = new ArrayList();
                if (!((String) SPUtils.get(this, SharePrefConstant.SAVE_STORAGE_SD_CARD_PATH, IntentKey.FILE_PATH)).contains(Constants.FILENAME_SEQUENCE_SEPARATOR)) {
                    basePath = IntentKey.FILE_PATH;
                } else {
                    basePath = Util.getParentPath(this);
                }
                list.add(basePath + this.pathTwo);
                startActivity(VideoEditingActivity.createIntent(this, list));
                return;
            case C0853R.C0855id.img_right:
                this.fVLookVideoMessageDialog.updatePath(this.pathTwo);
                this.fVLookVideoMessageDialog.show();
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        if (this.mMyCountDownTimer != null) {
            this.mMyCountDownTimer.cancel();
        }
        ButterKnife.unbind(this);
        BaseActivityManager.getActivityManager().popActivityOne(PlayVideoActivity.class);
        if (CameraUtils.getHasTakePhotoOrVideo()) {
            DataChangeNotification.getInstance().notifyDataChanged(IssueKey.PHOTO_VIDEO_DEIETE);
            CameraUtils.setHasTakePhotoOrVideo(false);
        }
        DataChangeNotification.getInstance().removeObserver((OnDataChangeObserver) this);
        EventBus.getDefault().unregister(this);
    }

    class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onTick(long millisUntilFinished) {
        }

        public void onFinish() {
            ((RelativeLayout) PlayVideoActivity.this.findViewById(C0853R.C0855id.act_look_photo_title)).setVisibility(8);
            ((LinearLayout) PlayVideoActivity.this.findViewById(C0853R.C0855id.act_layout_file_buttom)).setVisibility(8);
            Boolean unused = PlayVideoActivity.this.lookYesOrNo = false;
        }
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
            ViseLog.m1466e("ViltaX--0x46--data--" + getLocalClassName() + HexUtil.encodeHexStr(value));
            switch (value[2] & 255) {
                case 1:
                    ViseLog.m1466e("ViltaX" + getLocalClassName() + "1：菜单键单击：菜单/返回--mIsPlaying--" + this.mIsPlaying);
                    BleByteUtil.ackPTZPanorama((byte) 70, (byte) 1);
                    if (!this.mIsPlaying) {
                        finish();
                        return;
                    } else {
                        EventBus.getDefault().post(new UpEvent(PpEventConstant.FM210_VIDEO_BACK));
                        return;
                    }
                case 2:
                    ViseLog.m1466e("ViltaX2：菜单键长按：回到拍照界面");
                    BleByteUtil.ackPTZPanorama((byte) 70, (byte) 2);
                    startActivity(new Intent(this, FVMainActivity.class));
                    finish();
                    return;
                case 3:
                    ViseLog.m1466e("ViltaX" + getLocalClassName() + "3：确认键单击：确认/DISP--mIsPlaying--" + this.mIsPlaying);
                    BleByteUtil.ackPTZPanorama((byte) 70, (byte) 3);
                    if (!this.mIsPlaying) {
                        EventBus.getDefault().post(new PpEvent(PpEventConstant.FM210_VIDEO_START_TO_PLAY));
                        return;
                    } else {
                        EventBus.getDefault().post(new UpEvent(PpEventConstant.FM210_VIDEO_PLAY_OR_PAUSE));
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
            if (!this.mIsPlaying) {
                return;
            }
            if (delta < 0) {
                EventBus.getDefault().post(new UpEvent(PpEventConstant.STATUS_PTZ_SEEK_AFTER));
            } else {
                EventBus.getDefault().post(new UpEvent(PpEventConstant.STATUS_PTZ_SEEK_BEFORE));
            }
        }
    }
}
