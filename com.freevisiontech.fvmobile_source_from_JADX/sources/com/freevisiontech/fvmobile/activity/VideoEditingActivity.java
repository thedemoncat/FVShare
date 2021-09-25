package com.freevisiontech.fvmobile.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.p003v7.widget.LinearLayoutManager;
import android.support.p003v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.base.BaseRVAdapter;
import com.freevisiontech.fvmobile.base.BaseViewHolder;
import com.freevisiontech.fvmobile.bean.VideoEditingBean;
import com.freevisiontech.fvmobile.bean.VideoEditingRangSeekBar;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.freevisiontech.fvmobile.utility.BackgroundMusic;
import com.freevisiontech.fvmobile.utility.SPUtils;
import com.freevisiontech.fvmobile.utility.SharePrefConstant;
import com.freevisiontech.fvmobile.utility.Util;
import com.freevisiontech.fvmobile.utils.LoadingView;
import com.freevisiontech.fvmobile.utils.LocalResourceManager;
import com.freevisiontech.fvmobile.widget.FVKcfFreeStyleDialog;
import com.freevisiontech.fvmobile.widget.RangeSeekBar;
import com.freevisiontech.fvmobile.widget.view.IntentKey;
import com.freevisiontech.mediaproclib.Mp4ParseUtil;
import com.freevisiontech.mediaproclib.VideoClip;
import com.google.android.vending.expansion.downloader.Constants;
import com.umeng.analytics.C0015a;
import com.vise.log.ViseLog;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class VideoEditingActivity extends Activity implements View.OnClickListener {
    private final int PHOTOPATHSRESULT = 20;
    private final int PROGRESS_CHANGED = 10;
    @Bind({2131755526})
    ImageView act_edit_video_button_lvjing_dismiss;
    @Bind({2131756221})
    ImageView act_edit_video_button_music_dismiss;
    @Bind({2131755518})
    TextView act_video_editing_all_time;
    @Bind({2131755523})
    ImageView act_video_editing_bofang;
    @Bind({2131755535})
    LinearLayout act_video_editing_layout_buttom;
    @Bind({2131755524})
    ImageView act_video_editing_lvjing;
    @Bind({2131755525})
    LinearLayout act_video_editing_lvjing_mokuai;
    @Bind({2131755521})
    ImageView act_video_editing_music;
    @Bind({2131756223})
    RecyclerView act_video_editing_music_check_recycler;
    @Bind({2131756220})
    LinearLayout act_video_editing_music_mokuai;
    @Bind({2131756222})
    TextView act_video_editing_music_name;
    @Bind({2131755517})
    TextView act_video_editing_one_time;
    @Bind({2131755537})
    RecyclerView act_video_editing_recycler;
    @Bind({2131755536})
    LinearLayout act_video_editing_recycler_buttom;
    @Bind({2131755520})
    RecyclerView act_video_editing_seekbar_recycle;
    @Bind({2131755538})
    ImageView act_video_editing_tianjia;
    @Bind({2131755522})
    ImageView act_video_editing_zhanting;
    /* access modifiers changed from: private */
    public Activity activity;
    /* access modifiers changed from: private */
    public BaseRVAdapter adapter;
    /* access modifiers changed from: private */
    public BaseRVAdapter adapterMusic;
    /* access modifiers changed from: private */
    public long allVideoTime = 0;
    /* access modifiers changed from: private */
    public String basePath;
    private ArrayList<String> bgmTypes = new ArrayList<>();
    private HashMap<String, ArrayList> bgms = new HashMap<>();
    /* access modifiers changed from: private */
    public boolean checkMusic;
    /* access modifiers changed from: private */
    public ArrayList<String> curBGMs = new ArrayList<>();
    private int curBgmTypeIndex = 0;
    @Bind({2131755519})
    SeekBar edit_edit_view_seekbar;
    @Bind({2131756231})
    SeekBar edit_video_music_volume_seekbar;
    @Bind({2131756232})
    TextView edit_video_music_volume_value;
    /* access modifiers changed from: private */
    public FVKcfFreeStyleDialog fVKcfFreeStyleDialog;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    VideoEditingBean videoEditingBean = (VideoEditingBean) msg.obj;
                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "fvmobile");
                    String timeStamp5 = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
                    if (videoEditingBean.getStartTime() < videoEditingBean.getEndTime()) {
                        VideoClip.clip(videoEditingBean.getVideoStartPath(), file.getPath() + File.separator, "VID" + timeStamp5 + "Edit.mp4", videoEditingBean.getStartTime(), videoEditingBean.getEndTime());
                        File file2 = new File(VideoEditingActivity.this.basePath + "VID" + timeStamp5 + "Edit.mp4");
                        if (file2 != null && file2.exists()) {
                            VideoEditingActivity.this.videoHeBingList.add(VideoEditingActivity.this.basePath + "VID" + timeStamp5 + "Edit.mp4");
                            return;
                        }
                        return;
                    }
                    return;
                case 2:
                    if (VideoEditingActivity.this.videoHeBingList.size() == 0) {
                        VideoEditingActivity.this.hideProgress();
                        Toast.makeText(VideoEditingActivity.this.activity, C0853R.string.video_editing_video_comint_length, 0).show();
                        return;
                    }
                    new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "fvmobile");
                    String timeStamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
                    Mp4ParseUtil.appendMp4List(VideoEditingActivity.this.videoHeBingList, VideoEditingActivity.this.basePath + "VID" + timeStamp + "Edit.mp4");
                    for (int i = 0; i < VideoEditingActivity.this.videoHeBingList.size(); i++) {
                        File file3 = new File(VideoEditingActivity.this.videoHeBingList.get(i).toString());
                        if (file3 != null) {
                            file3.delete();
                        }
                    }
                    if (!new File(VideoEditingActivity.this.basePath + "VID" + timeStamp + "Edit.mp4").exists()) {
                        VideoEditingActivity.this.hideProgress();
                        Toast.makeText(VideoEditingActivity.this.activity, C0853R.string.video_editing_video_compound_failed, 0).show();
                        return;
                    }
                    if (VideoEditingActivity.this.playingBgmPosistion > 0) {
                        boolean unused = VideoEditingActivity.this.checkMusic = true;
                    } else {
                        boolean unused2 = VideoEditingActivity.this.checkMusic = false;
                    }
                    if (VideoEditingActivity.this.checkMusic) {
                        try {
                            InputStream is = LocalResourceManager.getLocalResourceStream(VideoEditingActivity.this.getApplicationContext(), VideoEditingActivity.this.pathMusic);
                            FileOutputStream fileOutputStream = new FileOutputStream(VideoEditingActivity.this.basePath + "video_20171008114555.m4a");
                            byte[] buffer = new byte[8192];
                            while (true) {
                                int count = is.read(buffer);
                                if (count > 0) {
                                    fileOutputStream.write(buffer, 0, count);
                                } else {
                                    fileOutputStream.close();
                                    is.close();
                                    MediaMetadataRetriever longMusicM4a = new MediaMetadataRetriever();
                                    longMusicM4a.setDataSource(VideoEditingActivity.this.basePath + "video_20171008114555.m4a");
                                    String timeLongMusicM4a = longMusicM4a.extractMetadata(9);
                                    MediaMetadataRetriever longMp4 = new MediaMetadataRetriever();
                                    if (new File(VideoEditingActivity.this.basePath + "VID" + timeStamp + "Edit.mp4") != null) {
                                        longMp4.setDataSource(VideoEditingActivity.this.basePath + "VID" + timeStamp + "Edit.mp4");
                                        String timeLongMp4 = longMp4.extractMetadata(9);
                                        if (Float.valueOf(timeLongMp4).floatValue() > Float.valueOf(timeLongMusicM4a).floatValue()) {
                                            ArrayList arrayList = new ArrayList();
                                            int n = (Integer.valueOf(timeLongMp4).intValue() / Integer.valueOf(timeLongMusicM4a).intValue()) + 1;
                                            for (int i2 = 0; i2 < n; i2++) {
                                                arrayList.add(VideoEditingActivity.this.basePath + "video_20171008114555.m4a");
                                            }
                                            Mp4ParseUtil.appendMp4List(arrayList, VideoEditingActivity.this.basePath + "video_20171008114555OK.m4a");
                                            try {
                                                final String str = timeStamp;
                                                new Timer().schedule(new TimerTask() {
                                                    public void run() {
                                                        VideoEditingActivity.this.sendToHandler(4, "VID" + str + "Edit.mp4");
                                                    }
                                                }, 1000);
                                                return;
                                            } catch (Exception e) {
                                                return;
                                            }
                                        } else {
                                            try {
                                                final String str2 = timeStamp;
                                                new Timer().schedule(new TimerTask() {
                                                    public void run() {
                                                        VideoEditingActivity.this.sendToHandler(3, "VID" + str2 + "Edit.mp4");
                                                    }
                                                }, 1000);
                                                return;
                                            } catch (Exception e2) {
                                                return;
                                            }
                                        }
                                    } else {
                                        return;
                                    }
                                }
                            }
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                    } else if (BleConstant.ISO.equals(VideoEditingActivity.this.musicForbid)) {
                        String timeStampNew = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
                        Mp4ParseUtil.splitMp4(VideoEditingActivity.this.basePath + "VID" + timeStamp + "Edit.mp4", VideoEditingActivity.this.basePath + "VID" + timeStampNew + "Edit.mp4");
                        new File(VideoEditingActivity.this.basePath + "VID" + timeStamp + "Edit.mp4").delete();
                        VideoEditingActivity.this.startActivity(VideoEditingEndActivity.createIntent(VideoEditingActivity.this.activity, "VID" + timeStampNew + "Edit.mp4"));
                        VideoEditingActivity.this.finish();
                        return;
                    } else {
                        VideoEditingActivity.this.startActivity(VideoEditingEndActivity.createIntent(VideoEditingActivity.this.activity, "VID" + timeStamp + "Edit.mp4"));
                        VideoEditingActivity.this.finish();
                        return;
                    }
                case 3:
                    final String heChengEndPath = (String) msg.obj;
                    File file4 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "fvmobile");
                    final String timeStamp4 = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
                    try {
                        Mp4ParseUtil.muxM4AMp4(VideoEditingActivity.this.basePath + "video_20171008114555.m4a", VideoEditingActivity.this.basePath + heChengEndPath, file4.getPath() + File.separator + "VID" + timeStamp4 + "Edit.mp4");
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                    final File mediaStorageDir6 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "fvmobile");
                    final String timeStamp6 = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
                    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                    mmr.setDataSource(VideoEditingActivity.this.basePath + heChengEndPath);
                    final String duration = mmr.extractMetadata(9);
                    try {
                        new Timer().schedule(new TimerTask() {
                            public void run() {
                                VideoClip.clip(VideoEditingActivity.this.basePath + "VID" + timeStamp4 + "Edit.mp4", mediaStorageDir6.getPath() + File.separator, "VID" + timeStamp6 + "Edit.mp4", 0.0d, Double.parseDouble(duration));
                                File file1 = new File(VideoEditingActivity.this.basePath + "VID" + timeStamp4 + "Edit.mp4");
                                if (file1 != null) {
                                    file1.delete();
                                }
                                file1.delete();
                                File file2 = new File(VideoEditingActivity.this.basePath + heChengEndPath);
                                if (file2 != null) {
                                    file2.delete();
                                }
                                VideoEditingActivity.this.startActivity(VideoEditingEndActivity.createIntent(VideoEditingActivity.this.activity, "VID" + timeStamp6 + "Edit.mp4"));
                                VideoEditingActivity.this.finish();
                            }
                        }, 1000);
                        return;
                    } catch (Exception e5) {
                        return;
                    }
                case 4:
                    final String heChengEndPathLong = (String) msg.obj;
                    File file5 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "fvmobile");
                    final String timeStampLong4 = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
                    try {
                        Mp4ParseUtil.muxM4AMp4(VideoEditingActivity.this.basePath + "video_20171008114555OK.m4a", VideoEditingActivity.this.basePath + heChengEndPathLong, file5.getPath() + File.separator + "VID" + timeStampLong4 + "Edit.mp4");
                    } catch (IOException e6) {
                        e6.printStackTrace();
                    }
                    final File mediaStorageDirLong6 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "fvmobile");
                    final String timeStampLong6 = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
                    MediaMetadataRetriever mmrLong = new MediaMetadataRetriever();
                    mmrLong.setDataSource(VideoEditingActivity.this.basePath + heChengEndPathLong);
                    final String durationLong = mmrLong.extractMetadata(9);
                    try {
                        new Timer().schedule(new TimerTask() {
                            public void run() {
                                VideoClip.clip(VideoEditingActivity.this.basePath + "VID" + timeStampLong4 + "Edit.mp4", mediaStorageDirLong6.getPath() + File.separator, "VID" + timeStampLong6 + "Edit.mp4", 0.0d, Double.parseDouble(durationLong));
                                File fileLong1 = new File(VideoEditingActivity.this.basePath + "VID" + timeStampLong4 + "Edit.mp4");
                                if (fileLong1 != null) {
                                    fileLong1.delete();
                                }
                                fileLong1.delete();
                                File fileLong2 = new File(VideoEditingActivity.this.basePath + heChengEndPathLong);
                                if (fileLong2 != null) {
                                    fileLong2.delete();
                                }
                                if (VideoEditingActivity.this.fVKcfFreeStyleDialog != null) {
                                    VideoEditingActivity.this.fVKcfFreeStyleDialog.finish();
                                }
                                VideoEditingActivity.this.startActivity(VideoEditingEndActivity.createIntent(VideoEditingActivity.this.activity, "VID" + timeStampLong6 + "Edit.mp4"));
                                VideoEditingActivity.this.finish();
                            }
                        }, 1000);
                        return;
                    } catch (Exception e7) {
                        return;
                    }
                case 11:
                    VideoEditingRangSeekBar videoEditingRangSeekBar = (VideoEditingRangSeekBar) msg.obj;
                    for (int a = 0; a < VideoEditingActivity.this.seekBarItemList.size(); a++) {
                        if (videoEditingRangSeekBar.getPosition() == a) {
                            VideoEditingActivity.this.seekBarItemList.set(a, videoEditingRangSeekBar.getPosition() + ":min=" + videoEditingRangSeekBar.getMin() + "max=" + videoEditingRangSeekBar.getMax());
                        }
                    }
                    return;
                case 12:
                    String ccc = (String) msg.obj;
                    VideoEditingActivity.this.seekBarItemList.set(Integer.valueOf(ccc.substring(0, ccc.indexOf(":min"))).intValue(), ccc);
                    return;
                default:
                    return;
            }
        }
    };
    @Bind({2131756214})
    ImageView img_video_editing_back;
    /* access modifiers changed from: private */
    public List<ImageView> mClcikImgList;
    private MediaController mController;
    private PopWindowOnClick mPopWindowOnClick;
    private AlertDialog mProgressDialog;
    /* access modifiers changed from: private */
    public String musicForbid = "";
    private List musicRecommend;
    private List musicRecommendName;
    private float musicVideoVolume = 0.5f;
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    if (!VideoEditingActivity.this.touchSeekBar) {
                        int unused = VideoEditingActivity.this.videoTimePosition = VideoEditingActivity.this.vv_video.getCurrentPosition();
                        long currentPosition = Long.parseLong(String.valueOf(VideoEditingActivity.this.vv_video.getCurrentPosition())) + VideoEditingActivity.this.startVideoTime;
                        int time = (int) ((100 * currentPosition) / VideoEditingActivity.this.allVideoTime);
                        if (time >= 99) {
                            VideoEditingActivity.this.edit_edit_view_seekbar.setProgress(100);
                            return;
                        } else if (!VideoEditingActivity.this.touchSeekBar) {
                            VideoEditingActivity.this.edit_edit_view_seekbar.setProgress(time);
                            VideoEditingActivity.this.act_video_editing_one_time.setText(VideoEditingActivity.this.getVideoFormatTime(currentPosition));
                            return;
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public String pathMusic;
    /* access modifiers changed from: private */
    public String playingBgmName = "";
    /* access modifiers changed from: private */
    public int playingBgmPosistion = 0;
    private PopupWindow popupWindow;
    int pos = 1000;
    /* access modifiers changed from: private */
    public long progressSeekTime;
    /* access modifiers changed from: private */
    public long progressTime = 0;
    @Bind({2131756226})
    RadioButton rb_classic;
    @Bind({2131756229})
    RadioButton rb_hip_hop;
    @Bind({2131756227})
    RadioButton rb_jazz;
    @Bind({2131756228})
    RadioButton rb_popular;
    @Bind({2131756225})
    RadioButton rb_recommend;
    @Bind({2131756230})
    RadioButton rb_rock;
    /* access modifiers changed from: private */
    public int recycleViewWidth;
    @Bind({2131756224})
    RadioGroup rg_bgm_types;
    /* access modifiers changed from: private */
    public List seekBarItemList;
    /* access modifiers changed from: private */
    public Boolean startVideoThread = true;
    /* access modifiers changed from: private */
    public long startVideoTime;
    /* access modifiers changed from: private */
    public boolean touchSeekBar = false;
    @Bind({2131756216})
    TextView tv_video_editing_all_baocun;
    @Bind({2131756217})
    TextView tv_video_editing_right_share;
    /* access modifiers changed from: private */
    public List videoHeBingList;
    /* access modifiers changed from: private */
    public List videoList;
    private List<String> videoPaths;
    /* access modifiers changed from: private */
    public int videoPosition = 0;
    /* access modifiers changed from: private */
    public List<String> videoSaveList;
    private VideoThreed videoThreed;
    /* access modifiers changed from: private */
    public List videoTimeBiLi;
    /* access modifiers changed from: private */
    public List videoTimeList;
    /* access modifiers changed from: private */
    public int videoTimePosition;
    private int videoTimePosition2;
    @Bind({2131755516})
    VideoView vv_video;

    public static Intent createIntent(Context context, List<String> listVideo) {
        Intent intent = new Intent(context, VideoEditingActivity.class);
        intent.putStringArrayListExtra(IntentKey.VIDEOS_PATH, (ArrayList) listVideo);
        return intent;
    }

    private void parseIntent() {
        this.videoPaths = getIntent().getStringArrayListExtra(IntentKey.VIDEOS_PATH);
    }

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0853R.layout.activity_video_editing);
        ButterKnife.bind((Activity) this);
        this.activity = this;
        initBasePath();
        parseIntent();
        initBgmTypes();
        initBgms();
        setCurBgms(this.curBgmTypeIndex);
        this.videoList = new ArrayList();
        this.videoList.addAll(this.videoPaths);
        this.seekBarItemList = new ArrayList();
        for (int w = 0; w < this.videoPaths.size(); w++) {
            this.seekBarItemList.add(w + ":min=" + 0 + "max=" + 1);
        }
        this.videoSaveList = new ArrayList();
        this.videoHeBingList = new ArrayList();
        initView();
        this.mController = new MediaController(this);
        playEditingVideoFirst(this.videoPosition, 0);
        initRecycler();
        BackgroundMusic.getInstance(this.activity).setBackgroundVolume(0.5f);
        this.videoTimeBiLi = new ArrayList();
        this.act_video_editing_seekbar_recycle.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                VideoEditingActivity.this.act_video_editing_seekbar_recycle.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int unused = VideoEditingActivity.this.recycleViewWidth = VideoEditingActivity.this.act_video_editing_seekbar_recycle.getWidth();
                for (int x = 0; x < VideoEditingActivity.this.videoTimeList.size(); x++) {
                    VideoEditingActivity.this.videoTimeBiLi.add(Double.valueOf(((double) Long.parseLong(VideoEditingActivity.this.videoTimeList.get(x).toString())) / ((double) VideoEditingActivity.this.allVideoTime)));
                }
                VideoEditingActivity.this.initSeekBarRecycler();
            }
        });
        setSeekBar();
        setVideoEditingMusicRecycle();
        setMusicVideoSeekBar();
        this.videoThreed = new VideoThreed();
        this.videoThreed.start();
    }

    private void initBasePath() {
        if (!((String) SPUtils.get(this.activity, SharePrefConstant.SAVE_STORAGE_SD_CARD_PATH, IntentKey.FILE_PATH)).contains(Constants.FILENAME_SEQUENCE_SEPARATOR)) {
            this.basePath = Util.getParentPath(this.activity);
        } else {
            this.basePath = IntentKey.FILE_PATH;
        }
    }

    private void initBgms() {
        ArrayList<String> recommend = new ArrayList<>();
        recommend.add((Object) null);
        recommend.add("cold_sober.m4a");
        recommend.add("gymnopedie_no3.m4a");
        recommend.add("happy_alley.m4a");
        recommend.add("pyro_flow.m4a");
        recommend.add("tech_live.m4a");
        this.bgms.put("recommend", recommend);
        ArrayList<String> classic = new ArrayList<>();
        classic.add((Object) null);
        classic.add("amazing_grace2011.m4a");
        classic.add("gymnopedie_no3.m4a");
        classic.add("ranzdes_vaches.m4a");
        classic.add("relent.m4a");
        classic.add("sunshine_a.m4a");
        this.bgms.put("classic", classic);
        ArrayList<String> jazz = new ArrayList<>();
        jazz.add((Object) null);
        jazz.add("bumminon_tremelo.m4a");
        jazz.add("cold_sober.m4a");
        jazz.add("sunday_dub.m4a");
        jazz.add("zazie.m4a");
        this.bgms.put("jazz", jazz);
        ArrayList<String> popular = new ArrayList<>();
        popular.add((Object) null);
        popular.add("basic_implosion.m4a");
        popular.add("cheery_monday.m4a");
        popular.add("clear_air.m4a");
        popular.add("happy_alley.m4a");
        popular.add("industrious_ferret.m4a");
        popular.add("life_of_riley.m4a");
        popular.add("somewhere_sunny.m4a");
        this.bgms.put("popular", popular);
        ArrayList<String> hip_hop = new ArrayList<>();
        hip_hop.add((Object) null);
        hip_hop.add("gutsand_bourbon.m4a");
        hip_hop.add("tech_live.m4a");
        this.bgms.put("hip_hop", hip_hop);
        ArrayList<String> rock = new ArrayList<>();
        rock.add((Object) null);
        rock.add("broken_reality.m4a");
        rock.add("exhilarate.m4a");
        rock.add("happy_bee.m4a");
        rock.add("i_feel_you.m4a");
        rock.add("motherlode.m4a");
        rock.add("pyro_flow.m4a");
        rock.add("ready_aim_fire.m4a");
        this.bgms.put("rock", rock);
    }

    private void initBgmTypes() {
        this.bgmTypes.add("recommend");
        this.bgmTypes.add("classic");
        this.bgmTypes.add("jazz");
        this.bgmTypes.add("popular");
        this.bgmTypes.add("hip_hop");
        this.bgmTypes.add("rock");
    }

    private void setCurBgms(int index) {
        this.curBgmTypeIndex = index;
        this.curBGMs.clear();
        ArrayList selectedBgms = this.bgms.get(this.bgmTypes.get(this.curBgmTypeIndex));
        ViseLog.m1466e("curBGMs selectedBgms: " + selectedBgms);
        this.curBGMs.addAll(selectedBgms);
        ViseLog.m1466e("curBGMs: " + this.curBGMs);
    }

    private String createBgmPlayPath(String bgmName) {
        return "";
    }

    private void setMusicVideoSeekBar() {
        this.edit_video_music_volume_seekbar.setMax(100);
        this.edit_video_music_volume_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private int sss;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                VideoEditingActivity.this.edit_video_music_volume_seekbar.setProgress(progress);
                VideoEditingActivity.this.edit_video_music_volume_value.setText(progress + "%");
                BackgroundMusic.getInstance(VideoEditingActivity.this.activity).setBackgroundVolume(((float) progress) / 100.0f);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void setVideoEditingMusicRecycle() {
        this.act_video_editing_music_check_recycler.setLayoutManager(new LinearLayoutManager(this.activity, 0, false));
        this.adapterMusic = new BaseRVAdapter(this.activity, this.curBGMs) {
            public int getLayoutId(int viewType) {
                return C0853R.layout.activity_video_editing_music_check_recycler_item;
            }

            public void onBind(BaseViewHolder holder, final int position) {
                ImageView act_video_editing_music_recycle_item_check_no = (ImageView) holder.getView(C0853R.C0855id.act_video_editing_music_recycle_item_check_no);
                ImageView act_video_editing_music_recycle_item_check_ok = (ImageView) holder.getView(C0853R.C0855id.act_video_editing_music_recycle_item_check_ok);
                if (position == 0) {
                    act_video_editing_music_recycle_item_check_no.setBackgroundResource(C0853R.mipmap.xml_radiobutton_video_editing_music_bg_img_wu);
                    act_video_editing_music_recycle_item_check_ok.setBackgroundResource(C0853R.mipmap.xml_radiobutton_video_editing_music_bg_img_wu);
                } else {
                    try {
                        Field picFiled = C0853R.mipmap.class.getField(((String) VideoEditingActivity.this.curBGMs.get(position)).replace(".m4a", ""));
                        try {
                            int picId = picFiled.getInt(picFiled.getName());
                            act_video_editing_music_recycle_item_check_no.setBackgroundResource(picId);
                            act_video_editing_music_recycle_item_check_ok.setBackgroundResource(picId);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    } catch (NoSuchFieldException e2) {
                        e2.printStackTrace();
                    }
                }
                if (position != VideoEditingActivity.this.playingBgmPosistion || !VideoEditingActivity.this.curBGMs.contains(VideoEditingActivity.this.playingBgmName + ".m4a")) {
                    act_video_editing_music_recycle_item_check_no.setVisibility(0);
                    act_video_editing_music_recycle_item_check_ok.setVisibility(8);
                } else {
                    act_video_editing_music_recycle_item_check_no.setVisibility(8);
                    act_video_editing_music_recycle_item_check_ok.setVisibility(0);
                }
                act_video_editing_music_recycle_item_check_no.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        int unused = VideoEditingActivity.this.playingBgmPosistion = position;
                        VideoEditingActivity.this.adapterMusic.notifyDataSetChanged();
                        if (position == 0) {
                            VideoEditingActivity.this.act_video_editing_music_name.setText("");
                            String unused2 = VideoEditingActivity.this.pathMusic = null;
                            BackgroundMusic.getInstance(VideoEditingActivity.this.activity).end();
                        } else {
                            String unused3 = VideoEditingActivity.this.playingBgmName = ((String) VideoEditingActivity.this.curBGMs.get(position)).toString().replace(".m4a", "");
                            VideoEditingActivity.this.act_video_editing_music_name.setText(VideoEditingActivity.this.firstUpperCase(VideoEditingActivity.this.playingBgmName));
                            String unused4 = VideoEditingActivity.this.pathMusic = ((String) VideoEditingActivity.this.curBGMs.get(position)).toString();
                            BackgroundMusic.getInstance(VideoEditingActivity.this.activity).playBackgroundMusic(VideoEditingActivity.this.pathMusic, true);
                        }
                        ViseLog.m1466e("VideoEditingActivity position: " + VideoEditingActivity.this.playingBgmPosistion);
                        ViseLog.m1466e("VideoEditingActivity pathMusic: " + VideoEditingActivity.this.pathMusic);
                    }
                });
            }
        };
        this.act_video_editing_music_check_recycler.setAdapter(this.adapterMusic);
    }

    /* access modifiers changed from: private */
    public String firstUpperCase(String name) {
        StringBuilder result = new StringBuilder();
        if (name == null || name.isEmpty()) {
            return "";
        }
        if (!name.contains("_")) {
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        for (String target : name.split("_")) {
            if (!target.isEmpty()) {
                result.append(target.substring(0, 1).toUpperCase());
                result.append(target.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    private void setSeekBar() {
        this.edit_edit_view_seekbar.setMax(100);
        this.edit_edit_view_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private int sss;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress != 0) {
                    VideoEditingActivity.this.edit_edit_view_seekbar.setProgress(progress);
                }
                if (VideoEditingActivity.this.touchSeekBar) {
                    long readTime = (((long) progress) * VideoEditingActivity.this.allVideoTime) / 100;
                    long unused = VideoEditingActivity.this.progressTime = 0;
                    long unused2 = VideoEditingActivity.this.progressSeekTime = 0;
                    this.sss = 0;
                    for (int s = 0; s < VideoEditingActivity.this.videoTimeList.size(); s++) {
                        if (readTime > VideoEditingActivity.this.progressTime) {
                            this.sss = s;
                            long unused3 = VideoEditingActivity.this.progressSeekTime = readTime - VideoEditingActivity.this.progressTime;
                        }
                        long unused4 = VideoEditingActivity.this.progressTime = VideoEditingActivity.this.progressTime + Long.parseLong(VideoEditingActivity.this.videoTimeList.get(s).toString());
                    }
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                boolean unused = VideoEditingActivity.this.touchSeekBar = true;
                VideoEditingActivity.this.act_video_editing_zhanting.setVisibility(0);
                VideoEditingActivity.this.act_video_editing_bofang.setVisibility(8);
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                boolean unused = VideoEditingActivity.this.touchSeekBar = false;
                int unused2 = VideoEditingActivity.this.videoPosition = this.sss;
                VideoEditingActivity.this.playEditingVideoFirst(this.sss, (int) VideoEditingActivity.this.progressSeekTime);
                if (VideoEditingActivity.this.pathMusic != null) {
                    BackgroundMusic.getInstance(VideoEditingActivity.this.activity).playBackgroundMusic(VideoEditingActivity.this.pathMusic, true);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void playEditingVideoFirst(int position, int startTime) {
        String obj = this.videoTimeList.get(position).toString();
        this.startVideoTime = 0;
        for (int e = 0; e < position; e++) {
            this.startVideoTime += Long.parseLong(this.videoTimeList.get(e).toString());
        }
        File file = new File(this.videoList.get(position).toString());
        if (file.exists()) {
            this.vv_video.setVideoPath(file.getAbsolutePath());
            this.mController.setMediaPlayer(this.vv_video);
            this.vv_video.seekTo(startTime);
            this.vv_video.start();
            this.vv_video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    if (VideoEditingActivity.this.videoPosition == VideoEditingActivity.this.videoList.size() - 1) {
                        int unused = VideoEditingActivity.this.videoPosition = VideoEditingActivity.this.videoPosition + 1;
                        VideoEditingActivity.this.edit_edit_view_seekbar.setProgress(100);
                        VideoEditingActivity.this.act_video_editing_zhanting.setVisibility(8);
                        VideoEditingActivity.this.act_video_editing_bofang.setVisibility(0);
                        if (VideoEditingActivity.this.pathMusic != null) {
                            BackgroundMusic.getInstance(VideoEditingActivity.this.activity).end();
                        }
                    }
                    if (VideoEditingActivity.this.videoPosition < VideoEditingActivity.this.videoList.size() - 1) {
                        int unused2 = VideoEditingActivity.this.videoPosition = VideoEditingActivity.this.videoPosition + 1;
                        VideoEditingActivity.this.playEditingVideoFirst(VideoEditingActivity.this.videoPosition, 0);
                    }
                }
            });
            if (this.videoThreed != null) {
            }
            this.startVideoThread = true;
        }
    }

    private void playEditingVideo(int position) {
        File file = new File(this.videoList.get(position).toString());
        if (file.exists()) {
            this.vv_video.setVideoPath(file.getAbsolutePath());
            this.mController.setMediaPlayer(this.vv_video);
            this.vv_video.start();
            this.vv_video.seekTo(0);
        }
    }

    /* access modifiers changed from: private */
    public void initSeekBarRecycler() {
        this.act_video_editing_seekbar_recycle.setLayoutManager(new LinearLayoutManager(this.activity, 0, false));
        this.act_video_editing_seekbar_recycle.setAdapter(new BaseRVAdapter(this.activity, this.videoList) {
            public int getLayoutId(int viewType) {
                return C0853R.layout.act_video_editing_recycler_seekbar_item;
            }

            public void onBind(BaseViewHolder holder, int position) {
                TextView recycler_seekbar_item_view = (TextView) holder.getView(C0853R.C0855id.act_video_editing_recycler_seekbar_item_view);
                ImageView recycler_seekbar_item_imageview = (ImageView) holder.getView(C0853R.C0855id.act_video_editing_recycler_seekbar_item_imageview);
                if (position == VideoEditingActivity.this.videoList.size() - 1) {
                    recycler_seekbar_item_imageview.setVisibility(8);
                }
                String aaaa = String.valueOf(((((double) VideoEditingActivity.this.recycleViewWidth) * Double.parseDouble(VideoEditingActivity.this.videoTimeBiLi.get(position).toString())) - 6.0d) + ((double) (6 / VideoEditingActivity.this.videoList.size())));
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) recycler_seekbar_item_view.getLayoutParams();
                params.width = Integer.parseInt(aaaa.substring(0, aaaa.indexOf(".")));
                recycler_seekbar_item_view.setLayoutParams(params);
            }
        });
    }

    private void initRecycler() {
        this.act_video_editing_recycler.setLayoutManager(new LinearLayoutManager(this.activity, 1, false));
        this.adapter = new BaseRVAdapter(this.activity, this.videoList) {
            public int getLayoutId(int viewType) {
                return C0853R.layout.act_video_editing_recycler_item;
            }

            public void onBind(BaseViewHolder holder, final int position) {
                ImageView recycler_item_image = (ImageView) holder.getView(C0853R.C0855id.act_video_editing_recycler_item_image);
                Glide.with(VideoEditingActivity.this.activity).load(VideoEditingActivity.this.videoList.get(position)).asBitmap().centerCrop().into(recycler_item_image);
                TextView act_video_editing_recycler_item_xuan_video = (TextView) holder.getView(C0853R.C0855id.act_video_editing_recycler_item_xuan_video);
                LinearLayout act_video_editing_recycler_item_left_top_gone = (LinearLayout) holder.getView(C0853R.C0855id.act_video_editing_recycler_item_left_top_gone);
                LinearLayout act_video_editing_recycler_item_left_top = (LinearLayout) holder.getView(C0853R.C0855id.act_video_editing_recycler_item_left_top);
                act_video_editing_recycler_item_left_top.setVisibility(0);
                act_video_editing_recycler_item_left_top_gone.setVisibility(8);
                act_video_editing_recycler_item_xuan_video.setVisibility(8);
                if (position == 0) {
                    act_video_editing_recycler_item_left_top.setVisibility(8);
                    act_video_editing_recycler_item_left_top_gone.setVisibility(0);
                    act_video_editing_recycler_item_xuan_video.setVisibility(0);
                }
                RelativeLayout recycler_item_buttom = (RelativeLayout) holder.getView(C0853R.C0855id.act_video_editing_recycler_item_buttom);
                recycler_item_buttom.setVisibility(8);
                if (VideoEditingActivity.this.pos == position) {
                    recycler_item_buttom.setVisibility(0);
                }
                recycler_item_image.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        VideoEditingActivity.this.pos = position;
                        VideoEditingActivity.this.adapter.notifyDataSetChanged();
                        int unused = VideoEditingActivity.this.videoPosition = position;
                        VideoEditingActivity.this.act_video_editing_zhanting.setVisibility(0);
                        VideoEditingActivity.this.act_video_editing_bofang.setVisibility(8);
                        VideoEditingActivity.this.playEditingVideoFirst(VideoEditingActivity.this.videoPosition, 0);
                    }
                });
                ((ImageView) holder.getView(C0853R.C0855id.act_video_editing_recycler_item_buttom_delete)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (VideoEditingActivity.this.videoList.size() > 1) {
                            VideoEditingActivity.this.videoList.remove(position);
                            VideoEditingActivity.this.seekBarItemList.remove(position);
                            VideoEditingActivity.this.pos = 1000;
                            VideoEditingActivity.this.adapter.notifyDataSetChanged();
                            VideoEditingActivity.this.notifyDataSeekBar();
                            VideoEditingActivity.this.playEditingVideoFirst(0, 0);
                            return;
                        }
                        Boolean unused = VideoEditingActivity.this.startVideoThread = false;
                        VideoEditingActivity.this.finish();
                    }
                });
                ((ImageView) holder.getView(C0853R.C0855id.act_video_editing_recycler_item_buttom_tiaojie)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        VideoEditingActivity.this.startActivity(VideoEditingItemEachActivity.createIntent(VideoEditingActivity.this.activity, VideoEditingActivity.this.videoList.get(position).toString()));
                    }
                });
                RangeSeekBar rsb4 = (RangeSeekBar) holder.getView(C0853R.C0855id.rsb_4);
                String aaa = VideoEditingActivity.this.seekBarItemList.get(position).toString();
                rsb4.setValue(Float.valueOf(aaa.substring(aaa.indexOf("min=") + 4, aaa.indexOf("max="))).floatValue(), Float.valueOf(aaa.substring(aaa.indexOf("max=") + 4, aaa.length())).floatValue());
                rsb4.setOnRangeChangedListener(new RangeSeekBar.OnRangeChangedListener() {
                    public void onRangeChanged(RangeSeekBar view, float min, float max) {
                        for (int a = 0; a < VideoEditingActivity.this.seekBarItemList.size(); a++) {
                            if (position == a) {
                                VideoEditingActivity.this.seekBarItemList.set(a, position + ":min=" + min + "max=" + max);
                            }
                        }
                    }
                });
            }
        };
        this.act_video_editing_recycler.setAdapter(this.adapter);
    }

    private void initView() {
        this.tv_video_editing_right_share.setOnClickListener(this);
        this.tv_video_editing_all_baocun.setOnClickListener(this);
        this.img_video_editing_back.setOnClickListener(this);
        this.act_video_editing_music.setOnClickListener(this);
        this.act_video_editing_bofang.setOnClickListener(this);
        this.act_video_editing_zhanting.setOnClickListener(this);
        this.act_video_editing_tianjia.setOnClickListener(this);
        this.rb_recommend.setOnClickListener(this);
        this.rb_classic.setOnClickListener(this);
        this.rb_jazz.setOnClickListener(this);
        this.rb_popular.setOnClickListener(this);
        this.rb_hip_hop.setOnClickListener(this);
        this.rb_rock.setOnClickListener(this);
        this.act_edit_video_button_music_dismiss.setOnClickListener(this);
        this.act_video_editing_lvjing.setOnClickListener(this);
        this.act_edit_video_button_lvjing_dismiss.setOnClickListener(this);
        this.vv_video.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        this.videoTimeList = new ArrayList();
        for (int i = 0; i < this.videoList.size(); i++) {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(this.videoList.get(i).toString());
            String duration = mmr.extractMetadata(9);
            this.videoTimeList.add(duration);
            this.allVideoTime += Long.parseLong(duration);
        }
        this.act_video_editing_all_time.setText("/" + getVideoFormatTime(this.allVideoTime));
    }

    /* access modifiers changed from: private */
    public String getVideoFormatTime(long allVideoTime2) {
        String timeFormat;
        if (allVideoTime2 / 1000 < 60) {
            timeFormat = "ss";
        } else if (allVideoTime2 / 1000 < 3600) {
            timeFormat = "mm:ss";
        } else {
            timeFormat = "HH:mm:ss";
        }
        String sDateTime = new SimpleDateFormat(timeFormat).format(new java.sql.Date(allVideoTime2));
        if (timeFormat.equals("HH:mm:ss")) {
            sDateTime = (allVideoTime2 / C0015a.f23j) + sDateTime.substring(sDateTime.length() - 6, sDateTime.length());
        }
        if (timeFormat.equals("ss")) {
            return "00:" + sDateTime;
        }
        return sDateTime;
    }

    class VideoThreed extends Thread {
        VideoThreed() {
        }

        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                if (VideoEditingActivity.this.startVideoThread.booleanValue()) {
                    Message message = new Message();
                    message.what = 10;
                    VideoEditingActivity.this.myHandler.sendMessage(message);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0853R.C0855id.act_video_editing_music:
                filter(v);
                return;
            case C0853R.C0855id.act_video_editing_zhanting:
                this.act_video_editing_zhanting.setVisibility(8);
                this.act_video_editing_bofang.setVisibility(0);
                this.videoTimePosition2 = this.vv_video.getCurrentPosition();
                this.touchSeekBar = true;
                this.vv_video.stopPlayback();
                if (this.pathMusic != null) {
                    BackgroundMusic.getInstance(this.activity).end();
                    return;
                }
                return;
            case C0853R.C0855id.act_video_editing_bofang:
                this.act_video_editing_zhanting.setVisibility(0);
                this.act_video_editing_bofang.setVisibility(8);
                if (this.videoPosition == this.videoList.size()) {
                    this.videoPosition = 0;
                    playEditingVideoFirst(this.videoPosition, 0);
                } else {
                    playEditingVideoFirst(this.videoPosition, this.videoTimePosition2);
                }
                this.touchSeekBar = false;
                if (this.pathMusic != null) {
                    BackgroundMusic.getInstance(this.activity).playBackgroundMusic(this.pathMusic, true);
                    return;
                }
                return;
            case C0853R.C0855id.act_edit_video_button_lvjing_dismiss:
                this.act_video_editing_recycler_buttom.setVisibility(0);
                this.act_video_editing_lvjing_mokuai.setVisibility(8);
                return;
            case C0853R.C0855id.act_video_editing_tianjia:
                startActivityForResult(new Intent(this.activity, SelectVideoEditingActivity.class), 20);
                return;
            case C0853R.C0855id.img_video_editing_back:
                this.startVideoThread = false;
                finish();
                return;
            case C0853R.C0855id.tv_video_editing_all_baocun:
                BackgroundMusic.getInstance(this.activity).end();
                showProgress(getString(C0853R.string.label_produce_video_ing));
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        int unused = VideoEditingActivity.this.videoTimePosition = VideoEditingActivity.this.vv_video.getCurrentPosition();
                        VideoEditingActivity.this.vv_video.stopPlayback();
                        Boolean unused2 = VideoEditingActivity.this.startVideoThread = false;
                        VideoEditingActivity.this.videoSaveList.clear();
                        for (int i = 0; i < VideoEditingActivity.this.seekBarItemList.size(); i++) {
                            String aaa = VideoEditingActivity.this.seekBarItemList.get(i).toString();
                            Double bbb = Double.valueOf(Double.valueOf(aaa.substring(aaa.indexOf("min=") + 4, aaa.indexOf("max="))).doubleValue() / 1000.0d);
                            Double ccc = Double.valueOf(Double.valueOf(aaa.substring(aaa.indexOf("max=") + 4, aaa.length())).doubleValue() / 1000.0d);
                            Double ddd = Double.valueOf(((double) Long.parseLong(VideoEditingActivity.this.videoTimeList.get(i).toString())) * bbb.doubleValue());
                            Double eee = Double.valueOf(((double) Long.parseLong(VideoEditingActivity.this.videoTimeList.get(i).toString())) * ccc.doubleValue());
                            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "fvmobile");
                            String hhh = mediaStorageDir.getPath() + File.separator + "VID" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + "Edit.mp4";
                            VideoEditingBean videoEditingBean = new VideoEditingBean();
                            videoEditingBean.setStartTime(ddd.doubleValue());
                            videoEditingBean.setEndTime(eee.doubleValue());
                            videoEditingBean.setVideoStartPath(VideoEditingActivity.this.videoList.get(i).toString());
                            videoEditingBean.setVideoEndPath(hhh);
                            VideoEditingActivity.this.videoSaveList.add(hhh);
                            VideoEditingActivity.this.sendToHandler(1, videoEditingBean);
                        }
                        try {
                            new Timer().schedule(new TimerTask() {
                                public void run() {
                                    VideoEditingActivity.this.sendToHandler(2, "video");
                                }
                            }, 2000);
                        } catch (Exception e) {
                        }
                    }
                }, 100);
                return;
            case C0853R.C0855id.tv_video_editing_right_share:
                this.startVideoThread = false;
                return;
            case C0853R.C0855id.act_edit_video_button_music_dismiss:
                this.act_video_editing_recycler_buttom.setVisibility(0);
                this.act_video_editing_music_mokuai.setVisibility(8);
                this.pathMusic = null;
                BackgroundMusic.getInstance(this.activity).end();
                this.playingBgmPosistion = 0;
                this.adapterMusic.notifyDataSetChanged();
                updateUIRestart();
                return;
            case C0853R.C0855id.rb_recommend:
                setCurBgms(0);
                this.adapterMusic.notifyDataSetChanged();
                return;
            case C0853R.C0855id.rb_classic:
                setCurBgms(1);
                this.adapterMusic.notifyDataSetChanged();
                return;
            case C0853R.C0855id.rb_jazz:
                setCurBgms(2);
                this.adapterMusic.notifyDataSetChanged();
                return;
            case C0853R.C0855id.rb_popular:
                setCurBgms(3);
                this.adapterMusic.notifyDataSetChanged();
                return;
            case C0853R.C0855id.rb_hip_hop:
                setCurBgms(4);
                this.adapterMusic.notifyDataSetChanged();
                return;
            case C0853R.C0855id.rb_rock:
                setCurBgms(5);
                this.adapterMusic.notifyDataSetChanged();
                return;
            default:
                return;
        }
    }

    public void updateUIRestart() {
        int size = this.mClcikImgList.size();
        for (int i = 0; i < size; i++) {
            this.mClcikImgList.get(i).setImageResource(C0853R.mipmap.checkbox_off);
        }
    }

    @SuppressLint({"InflateParams"})
    private void filter(View view) {
        if (this.popupWindow == null) {
            this.mClcikImgList = new ArrayList();
            int width = getScreenDispaly(this)[0];
            View rootView = getLayoutInflater().inflate(C0853R.layout.popup_window_activity_video_editing_check_music, (ViewGroup) null);
            this.popupWindow = new PopupWindow(view, width / 3, -2, true);
            this.mPopWindowOnClick = new PopWindowOnClick(this.popupWindow);
            rootView.findViewById(C0853R.C0855id.popup_window_near_people_woman).setOnClickListener(this.mPopWindowOnClick);
            rootView.findViewById(C0853R.C0855id.popup_window_near_people_man).setOnClickListener(this.mPopWindowOnClick);
            this.mClcikImgList.add((ImageView) rootView.findViewById(C0853R.C0855id.popup_window_near_people_woman_check));
            this.mClcikImgList.add((ImageView) rootView.findViewById(C0853R.C0855id.popup_window_near_people_man_check));
            this.popupWindow.setContentView(rootView);
            this.popupWindow.setBackgroundDrawable(new ColorDrawable(-1879048192));
            this.popupWindow.setAnimationStyle(C0853R.style.PopMenuAnimation);
            this.popupWindow.setOutsideTouchable(true);
        }
        this.popupWindow.showAsDropDown(view);
    }

    public static int[] getScreenDispaly(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        return new int[]{windowManager.getDefaultDisplay().getWidth(), windowManager.getDefaultDisplay().getHeight()};
    }

    private class PopWindowOnClick implements View.OnClickListener {
        private PopupWindow popupWindow;

        public PopWindowOnClick(PopupWindow popupWindow2) {
            this.popupWindow = popupWindow2;
        }

        public void onClick(View v) {
            switch (v.getId()) {
                case C0853R.C0855id.popup_window_near_people_woman:
                    updateUI(0);
                    String unused = VideoEditingActivity.this.musicForbid = BleConstant.ISO;
                    VideoEditingActivity.this.act_video_editing_music.setBackgroundResource(C0853R.mipmap.ic_video_editing_music_forbid);
                    VideoEditingActivity.this.act_video_editing_recycler_buttom.setVisibility(0);
                    VideoEditingActivity.this.act_video_editing_music_mokuai.setVisibility(8);
                    String unused2 = VideoEditingActivity.this.pathMusic = null;
                    BackgroundMusic.getInstance(VideoEditingActivity.this.activity).end();
                    int unused3 = VideoEditingActivity.this.playingBgmPosistion = 0;
                    VideoEditingActivity.this.adapterMusic.notifyDataSetChanged();
                    break;
                case C0853R.C0855id.popup_window_near_people_man:
                    updateUI(1);
                    VideoEditingActivity.this.act_video_editing_recycler_buttom.setVisibility(8);
                    VideoEditingActivity.this.act_video_editing_music_mokuai.setVisibility(0);
                    VideoEditingActivity.this.act_video_editing_music.setBackgroundResource(C0853R.mipmap.ic_video_editing_yinyue);
                    String unused4 = VideoEditingActivity.this.musicForbid = BleConstant.SHUTTER;
                    break;
            }
            if (this.popupWindow != null) {
                this.popupWindow.dismiss();
            }
        }

        public void updateUI(int position) {
            int size = VideoEditingActivity.this.mClcikImgList.size();
            for (int i = 0; i < size; i++) {
                ((ImageView) VideoEditingActivity.this.mClcikImgList.get(i)).setImageResource(C0853R.mipmap.checkbox_off);
            }
            ((ImageView) VideoEditingActivity.this.mClcikImgList.get(position)).setImageResource(C0853R.mipmap.checkbox_on);
        }
    }

    public void sendToHandler(int what, Object obj) {
        Message me = new Message();
        me.what = what;
        me.obj = obj;
        this.handler.sendMessage(me);
    }

    /* access modifiers changed from: protected */
    public void onRestart() {
        super.onRestart();
        this.act_video_editing_zhanting.setVisibility(0);
        this.act_video_editing_bofang.setVisibility(8);
        this.videoPosition = 0;
        playEditingVideoFirst(this.videoPosition, 0);
        if (this.pathMusic != null) {
            BackgroundMusic.getInstance(this.activity).playBackgroundMusic(this.pathMusic, true);
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 20:
                if (resultCode == -1 && data != null) {
                    List list = new ArrayList();
                    list.clear();
                    list.addAll(data.getStringArrayListExtra(IntentKey.VIDEOS_PATH));
                    this.videoList.addAll(data.getStringArrayListExtra(IntentKey.VIDEOS_PATH));
                    for (int w = 0; w < list.size(); w++) {
                        this.seekBarItemList.add(w + ":min=" + 0 + "max=" + 1);
                    }
                    this.adapter.notifyDataSetChanged();
                    notifyDataSeekBar();
                    return;
                }
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: private */
    public void notifyDataSeekBar() {
        this.videoTimeList.clear();
        this.allVideoTime = 0;
        for (int a = 0; a < this.videoList.size(); a++) {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(this.videoList.get(a).toString());
            String duration = mmr.extractMetadata(9);
            this.videoTimeList.add(duration);
            this.allVideoTime += Long.parseLong(duration);
        }
        this.act_video_editing_all_time.setText("/" + getVideoFormatTime(this.allVideoTime));
        this.videoTimeBiLi.clear();
        for (int x = 0; x < this.videoTimeList.size(); x++) {
            this.videoTimeBiLi.add(Double.valueOf(((double) Long.parseLong(this.videoTimeList.get(x).toString())) / ((double) this.allVideoTime)));
        }
        initSeekBarRecycler();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        this.vv_video.stopPlayback();
        BackgroundMusic.getInstance(this.activity).end();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        hideProgress();
        File fileOneMusicDelete = new File(this.basePath + "video_20171008114555.m4a");
        if (fileOneMusicDelete != null) {
            fileOneMusicDelete.delete();
        }
        File fileTwoMusicDelete = new File(this.basePath + "video_20171008114555OK.m4a");
        if (fileTwoMusicDelete != null) {
            fileTwoMusicDelete.delete();
        }
        BackgroundMusic.getInstance(this.activity).end();
    }

    public void showProgress(String msg) {
        if (this.mProgressDialog == null) {
            this.mProgressDialog = new LoadingView(this.activity);
            this.mProgressDialog.setCancelable(false);
            this.mProgressDialog.setCanceledOnTouchOutside(false);
        }
        this.mProgressDialog.setMessage(msg);
        this.mProgressDialog.show();
    }

    public void hideProgress() {
        if (this.mProgressDialog != null) {
            this.mProgressDialog.dismiss();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 || event.getAction() != 0) {
            return super.onKeyDown(keyCode, event);
        }
        this.startVideoThread = false;
        finish();
        return true;
    }
}
