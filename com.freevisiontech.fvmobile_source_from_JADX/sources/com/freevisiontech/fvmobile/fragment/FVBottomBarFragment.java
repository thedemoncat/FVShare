package com.freevisiontech.fvmobile.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.p001v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.freevisiontech.cameralib.FVCameraManager;
import com.freevisiontech.cameralib.Size;
import com.freevisiontech.cameralib.impl.Camera2.Camera2Manager;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.ViseBluetooth;
import com.freevisiontech.fvmobile.activity.FVCameraFileTwoActivity;
import com.freevisiontech.fvmobile.bean.FVModeSelectEvent;
import com.freevisiontech.fvmobile.bean.MarkPointChangeSmoothBean;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.freevisiontech.fvmobile.model.resolver.CompanyIdentifierResolver;
import com.freevisiontech.fvmobile.utility.BackgroundMusic;
import com.freevisiontech.fvmobile.utility.BluetoothHeadsetUtil;
import com.freevisiontech.fvmobile.utility.CameraExclusiveUtils;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utility.Constants;
import com.freevisiontech.fvmobile.utility.FileUtils;
import com.freevisiontech.fvmobile.utility.SPUtils;
import com.freevisiontech.fvmobile.utility.SharePrefConstant;
import com.freevisiontech.fvmobile.utility.Util;
import com.freevisiontech.fvmobile.utils.BleByteUtil;
import com.freevisiontech.fvmobile.utils.BleUtil;
import com.freevisiontech.fvmobile.utils.CameraFirstPhotoPaths;
import com.freevisiontech.fvmobile.utils.Event;
import com.freevisiontech.fvmobile.utils.EventBusUtil;
import com.freevisiontech.fvmobile.utils.LoadingView;
import com.freevisiontech.fvmobile.utils.MoveTimelapseUtil;
import com.freevisiontech.fvmobile.widget.FVBeautyPop;
import com.freevisiontech.fvmobile.widget.FVFlashPop;
import com.freevisiontech.fvmobile.widget.FVLookAllSceneryPhotoDialog;
import com.freevisiontech.fvmobile.widget.FVMainForSearchBluetooth;
import com.freevisiontech.fvmobile.widget.FVPhotoModeCommonShortcutPop;
import com.freevisiontech.utils.ScreenOrientationUtil;
import com.lzy.okgo.model.Progress;
import com.makeramen.roundedimageview.RoundedImageView;
import com.vise.log.ViseLog;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.opencv.android.LoadOpenCV;

public class FVBottomBarFragment extends Fragment implements View.OnClickListener {
    public static final int BUTTOM_BAR_IMAGE_SMALL_SHOW = 34;
    private static final int CLOSE_FOLLOW_MODE_TIMEOUT = 9994;
    private static final int DELAYED_TAKE_PHOTO = 9989;
    private static final int DIAPOSE_PICTURE = 9990;
    private static final int EXPOSURE_OF_CAMERA_END = 10000;
    public static final int HITCH_COCK_ROOM_AND_MF_RESTART_ONE = 367;
    public static final int HITCH_COCK_ROOM_AND_MF_RESTART_TWO = 368;
    private static final int LENS_TENSILE_OF_CAMERA_END = 10001;
    public static final int MARK_POINT_MF_A_TO_B = 369;
    public static final int MARK_POINT_MF_B_TO_A = 370;
    public static final int MOVE_OR_TIMELAPSE = 20;
    private static final int PANORAMA_MODE_OVER_TIMEOUT = 9997;
    private static final int PANORAMA_MODE_START_TIMEOUT = 9999;
    private static final int PANORAMA_MODE_STEP_TIMEOUT = 9998;
    private static final int PANORAMA_TAKE_PICTRUE_TIMEOUT = 9996;
    public static final int Rotation_0 = 0;
    public static final int Rotation_180 = 180;
    public static final int Rotation_270 = 270;
    public static final int Rotation_90 = 90;
    public static final int SET_LAYOUT_VIDEO_STARTING_VOLUME_GONE = 37;
    public static final int SET_LAYOUT_VIDEO_STARTING_VOLUME_VISIBLE = 36;
    public static final int SET_TAKE_PHOTO_CAN_OK = 35;
    private static final int START_FOLLOW_MODE_TIMEOUT = 9995;
    public static final String TAG = "BottomFragment";
    public static final int TAKE_PHOTO = 31;
    public static final int TAKE_PHOTO_NEW = 32;
    public static final int VIDEO_BTN_RECOVER_CLICKABLE = 10;
    private boolean FullShotOpen = false;
    private final int HANDLERWHITE = 11;
    private boolean beautyOpen = false;
    private OrientationBroad broad;
    @Bind({2131755644})
    ImageView btnBleConnect;
    @Bind({2131755642})
    ImageView btnCommonPop;
    @Bind({2131755634})
    ImageView btnFlashSwitch;
    @Bind({2131755637})
    ImageView btnFollow;
    @Bind({2131755638})
    RoundedImageView btnIconShow;
    @Bind({2131755635})
    ImageView btnLensSwitch;
    @Bind({2131755643})
    ImageView btnOpenBeauty;
    @Bind({2131755631})
    LinearLayout btnPhotoMode;
    @Bind({2131755636})
    ImageButton btnPhotoVideo;
    @Bind({2131755633})
    LinearLayout btnVideoMode;
    @Bind({2131755645})
    ImageView btn_camera_hand_model_shortcut;
    @Bind({2131755649})
    ImageView btn_flash_switch_status;
    @Bind({2131755648})
    ImageView btn_follow_status;
    @Bind({2131755647})
    ImageView btn_video_starting_volume;
    private CameraHitchCockZoomThread cameraHitchCockZoomThread;
    /* access modifiers changed from: private */
    public FVCameraManager cameraManager;
    private int commonType = -1;
    /* access modifiers changed from: private */
    public boolean connected = false;
    private boolean delayPhotoOpen = false;
    private int deltaFM210;
    private int deltaNum;
    @Bind({2131755640})
    RelativeLayout fragment_buttom_bar_photo_check;
    @Bind({2131755641})
    RelativeLayout fragment_buttom_bar_video_check;
    private FVBeautyPop fvBeautyPop;
    /* access modifiers changed from: private */
    public PopupWindow fvBeautyPopWindow;
    private Handler handlerWhiteBalance = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 11:
                    if (FVBottomBarFragment.this.cameraManager.isCameraOpened()) {
                        FVBottomBarFragment.this.resetWhiteBalance();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private boolean hasGotoFile;
    private int heightPingMu;
    /* access modifiers changed from: private */
    public boolean isBeautyShowed = false;
    private boolean isFollowMode = false;
    /* access modifiers changed from: private */
    public boolean isNeedEnterNextStep = false;
    private boolean isPanoramaing = false;
    private boolean isPhotoMode = true;
    private boolean isResume = false;
    private int lastDeltaNum;
    /* access modifiers changed from: private */
    public ArrayList<String> list = new ArrayList<>();
    @Bind({2131755650})
    LinearLayout llBottomShadow;
    @Bind({2131755630})
    LinearLayout llLayout;
    @Bind({2131755646})
    LinearLayout ll_layout_video_starting_volume;
    private boolean longExposureOpen = false;
    /* access modifiers changed from: private */
    public FVLookAllSceneryPhotoDialog lookSceneryPhotoDialog;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            Size picsize;
            Size picsize1;
            switch (msg.what) {
                case 0:
                    if (FVBottomBarFragment.this.mStartAngle == -270) {
                        if (FVBottomBarFragment.this.btn_video_starting_volume.getVisibility() == 0) {
                            FVBottomBarFragment.this.rotateView(-360, FVBottomBarFragment.this.btnFlashSwitch, FVBottomBarFragment.this.btnLensSwitch, FVBottomBarFragment.this.btnFollow, FVBottomBarFragment.this.btnIconShow, FVBottomBarFragment.this.btnBleConnect, FVBottomBarFragment.this.btnCommonPop, FVBottomBarFragment.this.btnOpenBeauty, FVBottomBarFragment.this.btn_camera_hand_model_shortcut, FVBottomBarFragment.this.btn_video_starting_volume);
                            return;
                        }
                        FVBottomBarFragment.this.rotateView(-360, FVBottomBarFragment.this.btnFlashSwitch, FVBottomBarFragment.this.btnLensSwitch, FVBottomBarFragment.this.btnFollow, FVBottomBarFragment.this.btnIconShow, FVBottomBarFragment.this.btnBleConnect, FVBottomBarFragment.this.btnCommonPop, FVBottomBarFragment.this.btnOpenBeauty, FVBottomBarFragment.this.btn_camera_hand_model_shortcut);
                        return;
                    } else if (FVBottomBarFragment.this.btn_video_starting_volume.getVisibility() == 0) {
                        FVBottomBarFragment.this.rotateView(0, FVBottomBarFragment.this.btnFlashSwitch, FVBottomBarFragment.this.btnLensSwitch, FVBottomBarFragment.this.btnFollow, FVBottomBarFragment.this.btnIconShow, FVBottomBarFragment.this.btnBleConnect, FVBottomBarFragment.this.btnCommonPop, FVBottomBarFragment.this.btnOpenBeauty, FVBottomBarFragment.this.btn_camera_hand_model_shortcut, FVBottomBarFragment.this.btn_video_starting_volume);
                        return;
                    } else {
                        FVBottomBarFragment.this.rotateView(0, FVBottomBarFragment.this.btnFlashSwitch, FVBottomBarFragment.this.btnLensSwitch, FVBottomBarFragment.this.btnFollow, FVBottomBarFragment.this.btnIconShow, FVBottomBarFragment.this.btnBleConnect, FVBottomBarFragment.this.btnCommonPop, FVBottomBarFragment.this.btnOpenBeauty, FVBottomBarFragment.this.btn_camera_hand_model_shortcut);
                        return;
                    }
                case 10:
                    FVBottomBarFragment.this.setBtnPhotoVideoEnable(true);
                    return;
                case 20:
                    if (FVBottomBarFragment.this.cameraManager != null && FVBottomBarFragment.this.cameraManager.isMediaRecording() && CameraUtils.isMoveOrDelayTimeLapseIng()) {
                        FVBottomBarFragment.this.cameraManager.stopMediaRecord();
                        CameraUtils.setTimelapseIng(false);
                        Boolean unused = FVBottomBarFragment.this.startHitchCockVideo = false;
                        if (Constants.IS_MOVE_TIME_LAPSE_AND_SHOOTING) {
                            Constants.IS_MOVE_TIME_LAPSE_AND_SHOOTING = false;
                            if (FVBottomBarFragment.this.connected) {
                                MoveTimelapseUtil.getInstance().cancelShoot();
                            }
                            MoveTimelapseUtil.getInstance().detroy();
                            CameraUtils.setMoveTimelapseRecording(false);
                        } else if (FVBottomBarFragment.this.connected) {
                            BleByteUtil.setPTZParameters((byte) ClosedCaptionCtrl.CARRIAGE_RETURN, (byte) 0);
                        }
                        CameraUtils.setMoveOrDelayTimeLapsePath((String) null);
                        CameraUtils.setMoveOrDelayTimeLapseIng(false);
                        CameraUtils.setMoveOrDelayTimeLapseCurrentTime(0);
                        CameraUtils.setMoveOrDelayTimeLapseShutter(0.0f);
                        return;
                    }
                    return;
                case 31:
                    if (((Boolean) SPUtils.get(FVBottomBarFragment.this.mContext, SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, false)).booleanValue()) {
                        Size size = new Size(1280, 720);
                        int orientation1 = ScreenOrientationUtil.getInstance().getOrientation();
                        if (orientation1 == 0 || orientation1 == 180) {
                            FVBottomBarFragment.this.cameraManager.takePhoto(size.getWidth(), size.getHeight());
                        } else {
                            FVBottomBarFragment.this.cameraManager.takePhoto(size.getHeight(), size.getWidth());
                        }
                        FVBottomBarFragment.this.shootSound();
                        return;
                    }
                    int fullMode = ((Integer) SPUtils.get(FVBottomBarFragment.this.getActivity(), SharePrefConstant.FULL_SHOT, Integer.valueOf(Constants.FULL_SHOT_NONE))).intValue();
                    if (fullMode == 10028) {
                        picsize1 = new Size(1280, 720);
                    } else {
                        picsize1 = FVBottomBarFragment.this.cameraManager.getRecommendPictureSize();
                    }
                    if (fullMode == 10024) {
                        int orientation12 = ScreenOrientationUtil.getInstance().getOrientation();
                        if (orientation12 == 0 || orientation12 == 180) {
                            FVBottomBarFragment.this.cameraManager.takePhoto(picsize1.getWidth(), picsize1.getHeight());
                        } else {
                            FVBottomBarFragment.this.cameraManager.takePhoto(picsize1.getHeight(), picsize1.getWidth());
                        }
                        FVBottomBarFragment.this.shootSound();
                        return;
                    } else if (FVBottomBarFragment.this.list.size() == 0) {
                        Util.sendIntEventMessge(Constants.SMALL_SUN_DIS);
                        if (((Integer) SPUtils.get(FVBottomBarFragment.this.getActivity(), SharePrefConstant.PANAMA_PHOTO_FOCUS_LOCK_YES_OR_NO, Integer.valueOf(Constants.PANAMA_PHOTO_FOCUS_LOCK_YES))).intValue() != 107080) {
                            final Size size2 = picsize1;
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    int orientation1 = ScreenOrientationUtil.getInstance().getOrientation();
                                    if (orientation1 == 0 || orientation1 == 180) {
                                        FVBottomBarFragment.this.cameraManager.takePhoto(size2.getWidth(), size2.getHeight());
                                    } else {
                                        FVBottomBarFragment.this.cameraManager.takePhoto(size2.getHeight(), size2.getWidth());
                                    }
                                    FVBottomBarFragment.this.shootSound();
                                }
                            }, 3000);
                            return;
                        } else if (FVBottomBarFragment.this.cameraManager.getCameraManagerType() == 1) {
                            final Size size3 = picsize1;
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    if (FVBottomBarFragment.this.cameraManager.isSupportAutoFocus()) {
                                        FVBottomBarFragment.this.cameraManager.lockFocus((Camera.AutoFocusCallback) new Camera.AutoFocusCallback() {
                                            public void onAutoFocus(boolean success, Camera camera) {
                                                new Handler().postDelayed(new Runnable() {
                                                    public void run() {
                                                        int orientation1 = ScreenOrientationUtil.getInstance().getOrientation();
                                                        if (orientation1 == 0 || orientation1 == 180) {
                                                            FVBottomBarFragment.this.cameraManager.takePhoto(size3.getWidth(), size3.getHeight());
                                                        } else {
                                                            FVBottomBarFragment.this.cameraManager.takePhoto(size3.getHeight(), size3.getWidth());
                                                        }
                                                        FVBottomBarFragment.this.shootSound();
                                                    }
                                                }, 500);
                                            }
                                        });
                                        return;
                                    }
                                    int orientation1 = ScreenOrientationUtil.getInstance().getOrientation();
                                    if (orientation1 == 0 || orientation1 == 180) {
                                        FVBottomBarFragment.this.cameraManager.takePhoto(size3.getWidth(), size3.getHeight());
                                    } else {
                                        FVBottomBarFragment.this.cameraManager.takePhoto(size3.getHeight(), size3.getWidth());
                                    }
                                    FVBottomBarFragment.this.shootSound();
                                }
                            }, 3000);
                            return;
                        } else {
                            final Size size4 = picsize1;
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    boolean boo = FVBottomBarFragment.this.cameraManager.isSupportAutoFocus();
                                    if ("H8296".equals(FVBottomBarFragment.this.systemModel)) {
                                        boo = false;
                                    }
                                    if (boo) {
                                        Log.v("HTEST", "autoFocus locked  strat");
                                        FVBottomBarFragment.this.cameraManager.lockFocus((Camera2Manager.AutoFocusListener) new Camera2Manager.AutoFocusListener() {
                                            public void focusLocked() {
                                                Log.v("HTEST", "autoFocus locked");
                                                new Handler().postDelayed(new Runnable() {
                                                    public void run() {
                                                        Log.v("HTEST", "takeFullPhotoEvery");
                                                        int orientation1 = ScreenOrientationUtil.getInstance().getOrientation();
                                                        if (orientation1 == 0 || orientation1 == 180) {
                                                            FVBottomBarFragment.this.cameraManager.takePhoto(size4.getWidth(), size4.getHeight());
                                                        } else {
                                                            FVBottomBarFragment.this.cameraManager.takePhoto(size4.getHeight(), size4.getWidth());
                                                        }
                                                        FVBottomBarFragment.this.shootSound();
                                                    }
                                                }, 300);
                                            }

                                            public void focusUnlocked() {
                                                Log.v("TEST", "autoFocus unlocked");
                                            }
                                        });
                                        return;
                                    }
                                    int orientation1 = ScreenOrientationUtil.getInstance().getOrientation();
                                    if (orientation1 == 0 || orientation1 == 180) {
                                        FVBottomBarFragment.this.cameraManager.takePhoto(size4.getWidth(), size4.getHeight());
                                    } else {
                                        FVBottomBarFragment.this.cameraManager.takePhoto(size4.getHeight(), size4.getWidth());
                                    }
                                    FVBottomBarFragment.this.shootSound();
                                }
                            }, 3000);
                            return;
                        }
                    } else if (((Integer) SPUtils.get(FVBottomBarFragment.this.getActivity(), SharePrefConstant.PANAMA_PHOTO_FOCUS_LOCK_YES_OR_NO, Integer.valueOf(Constants.PANAMA_PHOTO_FOCUS_LOCK_YES))).intValue() == 107080) {
                        final Size size5 = picsize1;
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                int orientation1 = ScreenOrientationUtil.getInstance().getOrientation();
                                if (orientation1 == 0 || orientation1 == 180) {
                                    FVBottomBarFragment.this.cameraManager.takePhoto(size5.getWidth(), size5.getHeight());
                                } else {
                                    FVBottomBarFragment.this.cameraManager.takePhoto(size5.getHeight(), size5.getWidth());
                                }
                                FVBottomBarFragment.this.shootSound();
                            }
                        }, 500);
                        return;
                    } else if (FVBottomBarFragment.this.cameraManager.getCameraManagerType() == 1) {
                        final Size size6 = picsize1;
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                int orientation1 = ScreenOrientationUtil.getInstance().getOrientation();
                                if (orientation1 == 0 || orientation1 == 180) {
                                    FVBottomBarFragment.this.cameraManager.takePhoto(size6.getWidth(), size6.getHeight());
                                } else {
                                    FVBottomBarFragment.this.cameraManager.takePhoto(size6.getHeight(), size6.getWidth());
                                }
                                FVBottomBarFragment.this.shootSound();
                            }
                        }, 3000);
                        return;
                    } else {
                        final Size size7 = picsize1;
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                int orientation1 = ScreenOrientationUtil.getInstance().getOrientation();
                                if (orientation1 == 0 || orientation1 == 180) {
                                    FVBottomBarFragment.this.cameraManager.takePhoto(size7.getWidth(), size7.getHeight());
                                } else {
                                    FVBottomBarFragment.this.cameraManager.takePhoto(size7.getHeight(), size7.getWidth());
                                }
                                FVBottomBarFragment.this.shootSound();
                            }
                        }, 3000);
                        return;
                    }
                case 32:
                    CameraUtils.setMaxSupPictureSize(String.valueOf(FVBottomBarFragment.this.cameraManager.getMaxSupportedPictureSize()));
                    CameraUtils.setReComPictureSize(String.valueOf(FVBottomBarFragment.this.cameraManager.getRecommendPictureSize()));
                    if (((Integer) SPUtils.get(FVBottomBarFragment.this.getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
                        if (CameraUtils.getMaxSupOrReComPictureFrontSize() == 0) {
                            picsize = FVBottomBarFragment.this.cameraManager.getRecommendPictureSize();
                        } else {
                            picsize = FVBottomBarFragment.this.cameraManager.getMaxSupportedPictureSize();
                        }
                    } else if (CameraUtils.getMaxSupOrReComPictureSize() == 0) {
                        picsize = FVBottomBarFragment.this.cameraManager.getRecommendPictureSize();
                    } else {
                        picsize = FVBottomBarFragment.this.cameraManager.getMaxSupportedPictureSize();
                    }
                    int orientation = ScreenOrientationUtil.getInstance().getOrientation();
                    if (orientation == 0 || orientation == 180) {
                        FVBottomBarFragment.this.cameraManager.takePhoto(picsize.getWidth(), picsize.getHeight());
                    } else {
                        FVBottomBarFragment.this.cameraManager.takePhoto(picsize.getHeight(), picsize.getWidth());
                    }
                    FVBottomBarFragment.this.shootSound();
                    return;
                case 34:
                    List<String> imagePathFromSD = CameraFirstPhotoPaths.getImagePathFromSD(FVBottomBarFragment.this.mContext);
                    if (imagePathFromSD != null && imagePathFromSD.size() > 0) {
                        String parentPath = Util.getParentPath(FVBottomBarFragment.this.mContext);
                        String str1 = parentPath + "IMG" + imagePathFromSD.get(0) + ".jpg";
                        String str2 = parentPath + "VID" + imagePathFromSD.get(0) + ".mp4";
                        String str3 = parentPath + "VID" + imagePathFromSD.get(0) + "bianji.mp4";
                        String str4 = parentPath + "IMG" + imagePathFromSD.get(0) + "bianji.jpg";
                        String str5 = parentPath + "IMG" + imagePathFromSD.get(0) + "mosaic.jpg";
                        String str6 = parentPath + "VID" + imagePathFromSD.get(0) + "yidong.mp4";
                        String str7 = parentPath + "IMG" + imagePathFromSD.get(0) + "yidong.mp4";
                        String str8 = parentPath + "VID" + imagePathFromSD.get(0) + "TimeLapse.mp4";
                        String str9 = parentPath + "VID" + imagePathFromSD.get(0) + "Edit.mp4";
                        String str10 = parentPath + "IMG" + imagePathFromSD.get(0) + "TimeLapse.mp4";
                        String str11 = parentPath + "IMG" + imagePathFromSD.get(0) + "Edit.mp4";
                        File file1 = new File(str1);
                        File file2 = new File(str2);
                        File file3 = new File(str3);
                        File file4 = new File(str4);
                        File file5 = new File(str5);
                        File file6 = new File(str6);
                        File file7 = new File(str7);
                        File file8 = new File(str8);
                        File file9 = new File(str9);
                        File file10 = new File(str10);
                        File file11 = new File(str11);
                        if (file1.exists()) {
                            if (FVBottomBarFragment.this.mContext != null && FVBottomBarFragment.this.btnIconShow != null) {
                                Glide.with(FVBottomBarFragment.this.mContext).load(str1).error((int) C0853R.mipmap.ic_gallery_empty).into(FVBottomBarFragment.this.btnIconShow);
                                return;
                            }
                            return;
                        } else if (file2.exists()) {
                            if (FVBottomBarFragment.this.mContext != null && FVBottomBarFragment.this.btnIconShow != null) {
                                Glide.with(FVBottomBarFragment.this.mContext).load(str2).error((int) C0853R.mipmap.ic_gallery_empty).into(FVBottomBarFragment.this.btnIconShow);
                                return;
                            }
                            return;
                        } else if (file3.exists()) {
                            if (FVBottomBarFragment.this.mContext != null && FVBottomBarFragment.this.btnIconShow != null) {
                                Glide.with(FVBottomBarFragment.this.mContext).load(str3).error((int) C0853R.mipmap.ic_gallery_empty).into(FVBottomBarFragment.this.btnIconShow);
                                return;
                            }
                            return;
                        } else if (file4.exists()) {
                            if (FVBottomBarFragment.this.mContext != null && FVBottomBarFragment.this.btnIconShow != null) {
                                Glide.with(FVBottomBarFragment.this.mContext).load(str4).error((int) C0853R.mipmap.ic_gallery_empty).into(FVBottomBarFragment.this.btnIconShow);
                                return;
                            }
                            return;
                        } else if (file5.exists()) {
                            if (FVBottomBarFragment.this.mContext != null && FVBottomBarFragment.this.btnIconShow != null) {
                                Glide.with(FVBottomBarFragment.this.mContext).load(str5).error((int) C0853R.mipmap.ic_gallery_empty).into(FVBottomBarFragment.this.btnIconShow);
                                return;
                            }
                            return;
                        } else if (file6.exists()) {
                            if (FVBottomBarFragment.this.mContext != null && FVBottomBarFragment.this.btnIconShow != null) {
                                Glide.with(FVBottomBarFragment.this.mContext).load(str6).error((int) C0853R.mipmap.ic_gallery_empty).into(FVBottomBarFragment.this.btnIconShow);
                                return;
                            }
                            return;
                        } else if (file7.exists()) {
                            if (FVBottomBarFragment.this.mContext != null && FVBottomBarFragment.this.btnIconShow != null) {
                                Glide.with(FVBottomBarFragment.this.mContext).load(str7).error((int) C0853R.mipmap.ic_gallery_empty).into(FVBottomBarFragment.this.btnIconShow);
                                return;
                            }
                            return;
                        } else if (file8.exists()) {
                            if (FVBottomBarFragment.this.mContext != null && FVBottomBarFragment.this.btnIconShow != null) {
                                Glide.with(FVBottomBarFragment.this.mContext).load(str8).error((int) C0853R.mipmap.ic_gallery_empty).into(FVBottomBarFragment.this.btnIconShow);
                                return;
                            }
                            return;
                        } else if (file9.exists()) {
                            if (FVBottomBarFragment.this.mContext != null && FVBottomBarFragment.this.btnIconShow != null) {
                                Glide.with(FVBottomBarFragment.this.mContext).load(str9).error((int) C0853R.mipmap.ic_gallery_empty).into(FVBottomBarFragment.this.btnIconShow);
                                return;
                            }
                            return;
                        } else if (file10.exists()) {
                            if (FVBottomBarFragment.this.mContext != null && FVBottomBarFragment.this.btnIconShow != null) {
                                Glide.with(FVBottomBarFragment.this.mContext).load(str10).error((int) C0853R.mipmap.ic_gallery_empty).into(FVBottomBarFragment.this.btnIconShow);
                                return;
                            }
                            return;
                        } else if (!file11.exists()) {
                            FVBottomBarFragment.this.btnIconShow.setImageResource(C0853R.mipmap.ic_gallery_empty);
                            return;
                        } else if (FVBottomBarFragment.this.mContext != null && FVBottomBarFragment.this.btnIconShow != null) {
                            Glide.with(FVBottomBarFragment.this.mContext).load(str11).error((int) C0853R.mipmap.ic_gallery_empty).into(FVBottomBarFragment.this.btnIconShow);
                            return;
                        } else {
                            return;
                        }
                    } else if (FVBottomBarFragment.this.btnIconShow != null) {
                        FVBottomBarFragment.this.btnIconShow.setImageResource(C0853R.mipmap.ic_gallery_empty);
                        return;
                    } else {
                        return;
                    }
                case 35:
                    Boolean unused2 = FVBottomBarFragment.this.takePhotoCanOk = true;
                    return;
                case 36:
                    if (FVBottomBarFragment.this.ll_layout_video_starting_volume != null && FVBottomBarFragment.this.ll_layout_video_starting_volume.getVisibility() == 8) {
                        FVBottomBarFragment.this.ll_layout_video_starting_volume.setVisibility(0);
                    }
                    if (FVBottomBarFragment.this.btn_video_starting_volume != null) {
                        FVBottomBarFragment.this.btn_video_starting_volume.setImageResource(FVBottomBarFragment.this.videoStartingVolume[((Integer) msg.obj).intValue()]);
                        return;
                    }
                    return;
                case 37:
                    if (FVBottomBarFragment.this.ll_layout_video_starting_volume != null && FVBottomBarFragment.this.ll_layout_video_starting_volume.getVisibility() == 0) {
                        FVBottomBarFragment.this.ll_layout_video_starting_volume.setVisibility(8);
                        return;
                    }
                    return;
                case 90:
                    if (FVBottomBarFragment.this.mStartAngle == -360) {
                        int unused3 = FVBottomBarFragment.this.mStartAngle = 0;
                    }
                    if (FVBottomBarFragment.this.btn_video_starting_volume.getVisibility() == 0) {
                        FVBottomBarFragment.this.rotateView(-90, FVBottomBarFragment.this.btnFlashSwitch, FVBottomBarFragment.this.btnLensSwitch, FVBottomBarFragment.this.btnFollow, FVBottomBarFragment.this.btnIconShow, FVBottomBarFragment.this.btnBleConnect, FVBottomBarFragment.this.btnCommonPop, FVBottomBarFragment.this.btnOpenBeauty, FVBottomBarFragment.this.btn_camera_hand_model_shortcut, FVBottomBarFragment.this.btn_video_starting_volume);
                        return;
                    }
                    FVBottomBarFragment.this.rotateView(-90, FVBottomBarFragment.this.btnFlashSwitch, FVBottomBarFragment.this.btnLensSwitch, FVBottomBarFragment.this.btnFollow, FVBottomBarFragment.this.btnIconShow, FVBottomBarFragment.this.btnBleConnect, FVBottomBarFragment.this.btnCommonPop, FVBottomBarFragment.this.btnOpenBeauty, FVBottomBarFragment.this.btn_camera_hand_model_shortcut);
                    return;
                case 180:
                    if (FVBottomBarFragment.this.btn_video_starting_volume.getVisibility() == 0) {
                        FVBottomBarFragment.this.rotateView(-180, FVBottomBarFragment.this.btnFlashSwitch, FVBottomBarFragment.this.btnLensSwitch, FVBottomBarFragment.this.btnFollow, FVBottomBarFragment.this.btnIconShow, FVBottomBarFragment.this.btnBleConnect, FVBottomBarFragment.this.btnCommonPop, FVBottomBarFragment.this.btnOpenBeauty, FVBottomBarFragment.this.btn_camera_hand_model_shortcut, FVBottomBarFragment.this.btn_video_starting_volume);
                        return;
                    }
                    FVBottomBarFragment.this.rotateView(-180, FVBottomBarFragment.this.btnFlashSwitch, FVBottomBarFragment.this.btnLensSwitch, FVBottomBarFragment.this.btnFollow, FVBottomBarFragment.this.btnIconShow, FVBottomBarFragment.this.btnBleConnect, FVBottomBarFragment.this.btnCommonPop, FVBottomBarFragment.this.btnOpenBeauty, FVBottomBarFragment.this.btn_camera_hand_model_shortcut);
                    return;
                case 270:
                    if (FVBottomBarFragment.this.mStartAngle == 0) {
                        int unused4 = FVBottomBarFragment.this.mStartAngle = -360;
                    }
                    if (FVBottomBarFragment.this.btn_video_starting_volume.getVisibility() == 0) {
                        FVBottomBarFragment.this.rotateView(-270, FVBottomBarFragment.this.btnFlashSwitch, FVBottomBarFragment.this.btnLensSwitch, FVBottomBarFragment.this.btnFollow, FVBottomBarFragment.this.btnIconShow, FVBottomBarFragment.this.btnBleConnect, FVBottomBarFragment.this.btnCommonPop, FVBottomBarFragment.this.btnOpenBeauty, FVBottomBarFragment.this.btn_camera_hand_model_shortcut, FVBottomBarFragment.this.btn_video_starting_volume);
                        return;
                    }
                    FVBottomBarFragment.this.rotateView(-270, FVBottomBarFragment.this.btnFlashSwitch, FVBottomBarFragment.this.btnLensSwitch, FVBottomBarFragment.this.btnFollow, FVBottomBarFragment.this.btnIconShow, FVBottomBarFragment.this.btnBleConnect, FVBottomBarFragment.this.btnCommonPop, FVBottomBarFragment.this.btnOpenBeauty, FVBottomBarFragment.this.btn_camera_hand_model_shortcut);
                    return;
                case 350:
                    FVBottomBarFragment.this.cameraManager.setZoom(((Float) msg.obj).floatValue());
                    return;
                case FVBottomBarFragment.HITCH_COCK_ROOM_AND_MF_RESTART_ONE /*367*/:
                    FVBottomBarFragment.this.cameraManager.setZoom(1.0f);
                    return;
                case FVBottomBarFragment.HITCH_COCK_ROOM_AND_MF_RESTART_TWO /*368*/:
                    FVBottomBarFragment.this.cameraManager.setZoom(1.0f);
                    if (FVBottomBarFragment.this.cameraManager.isMaunalFocus()) {
                        FVBottomBarFragment.this.cameraManager.enableMFMode(false);
                    }
                    FVBottomBarFragment.this.cameraManager.enableManualMode(false);
                    return;
                case FVBottomBarFragment.MARK_POINT_MF_A_TO_B /*369*/:
                    if (!FVBottomBarFragment.this.startMarkPointMfAToB.booleanValue()) {
                        if (CameraUtils.getLlMarkPointMfA() <= CameraUtils.getLlMarkPointMfB()) {
                        }
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                Boolean unused = FVBottomBarFragment.this.startMarkPointMfAToB = true;
                            }
                        }, 5);
                        return;
                    }
                    return;
                case FVBottomBarFragment.MARK_POINT_MF_B_TO_A /*370*/:
                    if (!FVBottomBarFragment.this.startMarkPointMfBToA.booleanValue()) {
                        float markPointMfB3 = CameraUtils.getLlMarkPointMfB();
                        float llMarkPointMfA = CameraUtils.getLlMarkPointMfA();
                        if (markPointMfB3 <= markPointMfB3) {
                        }
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                Boolean unused = FVBottomBarFragment.this.startMarkPointMfBToA = true;
                            }
                        }, 5);
                        return;
                    }
                    return;
                case FVBottomBarFragment.DELAYED_TAKE_PHOTO /*9989*/:
                    if (FVBottomBarFragment.this.isNeedEnterNextStep) {
                        FVBottomBarFragment.this.takePhoto();
                        if (FVBottomBarFragment.this.mHandler != null) {
                            if (FVBottomBarFragment.this.mHandler != null) {
                                while (FVBottomBarFragment.this.mHandler.hasMessages(FVBottomBarFragment.PANORAMA_TAKE_PICTRUE_TIMEOUT)) {
                                    FVBottomBarFragment.this.mHandler.removeMessages(FVBottomBarFragment.PANORAMA_TAKE_PICTRUE_TIMEOUT);
                                }
                            }
                            if (FVBottomBarFragment.this.takedPictrueCount == 0) {
                                FVBottomBarFragment.this.mHandler.sendEmptyMessageDelayed(FVBottomBarFragment.PANORAMA_TAKE_PICTRUE_TIMEOUT, 8500);
                                return;
                            } else {
                                FVBottomBarFragment.this.mHandler.sendEmptyMessageDelayed(FVBottomBarFragment.PANORAMA_TAKE_PICTRUE_TIMEOUT, 8500);
                                return;
                            }
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                case FVBottomBarFragment.DIAPOSE_PICTURE /*9990*/:
                    if (FVBottomBarFragment.this.isNeedEnterNextStep) {
                        FVBottomBarFragment.this.sinleTask();
                        return;
                    }
                    return;
                case FVBottomBarFragment.CLOSE_FOLLOW_MODE_TIMEOUT /*9994*/:
                    ViseLog.m1466e("蓝牙通讯异常,关闭跟踪模式失败");
                    return;
                case FVBottomBarFragment.START_FOLLOW_MODE_TIMEOUT /*9995*/:
                    ViseLog.m1466e("蓝牙通讯异常,开启跟踪模式失败");
                    Toast.makeText(FVBottomBarFragment.this.getActivity(), C0853R.string.label_device_communicate_abnormal, 0).show();
                    FVBottomBarFragment.this.btnFollow.setImageResource(C0853R.mipmap.ic_follow_close);
                    if (!CameraUtils.getBooRecordStarted()) {
                        FVBottomBarFragment.this.btnLensSwitch.setVisibility(0);
                    }
                    FVBottomBarFragment.this.setCameraHandModelUIVisibleOrGone(false);
                    return;
                case FVBottomBarFragment.PANORAMA_TAKE_PICTRUE_TIMEOUT /*9996*/:
                    ViseLog.m1466e("全景拍照通讯超时");
                    int unused5 = FVBottomBarFragment.this.takedPictrueCount = 0;
                    int unused6 = FVBottomBarFragment.this.requreTakePictrueCount = 0;
                    FVBottomBarFragment.this.list.clear();
                    Util.sendIntEventMessge((int) Constants.FULL_SHOT_EXCEPTION_END, BleConstant.ISO);
                    if (FVBottomBarFragment.this.connected && FVBottomBarFragment.this.isNeedEnterNextStep) {
                        boolean unused7 = FVBottomBarFragment.this.isNeedEnterNextStep = false;
                        CameraUtils.setFullCameraErrorCode("10");
                        BleByteUtil.setPanoramaCount((byte) 36, (byte) 1);
                        if (FVBottomBarFragment.this.mHandler != null) {
                            while (FVBottomBarFragment.this.mHandler.hasMessages(FVBottomBarFragment.PANORAMA_MODE_OVER_TIMEOUT)) {
                                FVBottomBarFragment.this.mHandler.removeMessages(FVBottomBarFragment.PANORAMA_MODE_OVER_TIMEOUT);
                            }
                        }
                        FVBottomBarFragment.this.mHandler.sendEmptyMessageDelayed(FVBottomBarFragment.PANORAMA_MODE_OVER_TIMEOUT, 8000);
                    }
                    Toast.makeText(FVBottomBarFragment.this.mContext, FVBottomBarFragment.this.getString(C0853R.string.label_full_camera_fail) + CameraUtils.getFullCameraErrorCode(), 0).show();
                    return;
                case FVBottomBarFragment.PANORAMA_MODE_OVER_TIMEOUT /*9997*/:
                    ViseLog.m1466e("全景结束通讯超时");
                    FVBottomBarFragment.access$1208(FVBottomBarFragment.this);
                    if (FVBottomBarFragment.this.overFailCount >= 3) {
                        int unused8 = FVBottomBarFragment.this.overFailCount = 0;
                        boolean unused9 = FVBottomBarFragment.this.isNeedEnterNextStep = false;
                        CameraUtils.setFullCameraErrorCode("9");
                        int unused10 = FVBottomBarFragment.this.takedPictrueCount = 0;
                        int unused11 = FVBottomBarFragment.this.requreTakePictrueCount = 0;
                        FVBottomBarFragment.this.list.clear();
                        Util.sendIntEventMessge((int) Constants.FULL_SHOT_EXCEPTION_END, BleConstant.ISO);
                        return;
                    } else if (FVBottomBarFragment.this.connected) {
                        BleByteUtil.setPanoramaCount((byte) 36, (byte) 1);
                        if (FVBottomBarFragment.this.mHandler != null) {
                            while (FVBottomBarFragment.this.mHandler.hasMessages(FVBottomBarFragment.PANORAMA_MODE_OVER_TIMEOUT)) {
                                FVBottomBarFragment.this.mHandler.removeMessages(FVBottomBarFragment.PANORAMA_MODE_OVER_TIMEOUT);
                            }
                        }
                        FVBottomBarFragment.this.mHandler.sendEmptyMessageDelayed(FVBottomBarFragment.PANORAMA_MODE_OVER_TIMEOUT, 8000);
                        return;
                    } else {
                        int unused12 = FVBottomBarFragment.this.takedPictrueCount = 0;
                        FVBottomBarFragment.this.list.clear();
                        return;
                    }
                case FVBottomBarFragment.PANORAMA_MODE_STEP_TIMEOUT /*9998*/:
                    ViseLog.m1466e("全景云台位移超时");
                    int unused13 = FVBottomBarFragment.this.takedPictrueCount = 0;
                    int unused14 = FVBottomBarFragment.this.requreTakePictrueCount = 0;
                    FVBottomBarFragment.this.list.clear();
                    Util.sendIntEventMessge((int) Constants.FULL_SHOT_EXCEPTION_END, BleConstant.ISO);
                    if (FVBottomBarFragment.this.connected && FVBottomBarFragment.this.isNeedEnterNextStep) {
                        boolean unused15 = FVBottomBarFragment.this.isNeedEnterNextStep = false;
                        CameraUtils.setFullCameraErrorCode("8");
                        BleByteUtil.setPanoramaCount((byte) 36, (byte) 1);
                        if (FVBottomBarFragment.this.mHandler != null) {
                            while (FVBottomBarFragment.this.mHandler.hasMessages(FVBottomBarFragment.PANORAMA_MODE_OVER_TIMEOUT)) {
                                FVBottomBarFragment.this.mHandler.removeMessages(FVBottomBarFragment.PANORAMA_MODE_OVER_TIMEOUT);
                            }
                        }
                        FVBottomBarFragment.this.mHandler.sendEmptyMessageDelayed(FVBottomBarFragment.PANORAMA_MODE_OVER_TIMEOUT, 8000);
                        return;
                    }
                    return;
                case FVBottomBarFragment.PANORAMA_MODE_START_TIMEOUT /*9999*/:
                    ViseLog.m1466e("全景初始化超时");
                    FVBottomBarFragment.access$508(FVBottomBarFragment.this);
                    if (FVBottomBarFragment.this.startFailCount >= 3) {
                        int unused16 = FVBottomBarFragment.this.startFailCount = 0;
                        Log.e(FVBottomBarFragment.TAG, "handleMessage: 全景通讯初始化超时");
                        boolean unused17 = FVBottomBarFragment.this.isNeedEnterNextStep = false;
                        CameraUtils.setFullCameraErrorCode("7");
                        if (FVBottomBarFragment.this.connected) {
                            BleByteUtil.setPanoramaCount((byte) 36, (byte) 1);
                            if (FVBottomBarFragment.this.mHandler != null) {
                                while (FVBottomBarFragment.this.mHandler.hasMessages(FVBottomBarFragment.PANORAMA_MODE_OVER_TIMEOUT)) {
                                    FVBottomBarFragment.this.mHandler.removeMessages(FVBottomBarFragment.PANORAMA_MODE_OVER_TIMEOUT);
                                }
                            }
                            FVBottomBarFragment.this.mHandler.sendEmptyMessageDelayed(FVBottomBarFragment.PANORAMA_MODE_OVER_TIMEOUT, 8000);
                        }
                        Util.sendIntEventMessge((int) Constants.FULL_SHOT_EXCEPTION_END, BleConstant.ISO);
                        return;
                    } else if (FVBottomBarFragment.this.requreTakePictrueCount == 5) {
                        if (FVBottomBarFragment.this.connected) {
                            FVBottomBarFragment.this.sendPanormaStart(1);
                            return;
                        }
                        return;
                    } else if (FVBottomBarFragment.this.requreTakePictrueCount == 7) {
                        if (FVBottomBarFragment.this.connected) {
                            FVBottomBarFragment.this.sendPanormaStart(2);
                            return;
                        }
                        return;
                    } else if (FVBottomBarFragment.this.requreTakePictrueCount == 9) {
                        if (FVBottomBarFragment.this.connected) {
                            FVBottomBarFragment.this.sendPanormaStart(3);
                            return;
                        }
                        return;
                    } else if (FVBottomBarFragment.this.requreTakePictrueCount == 15 && FVBottomBarFragment.this.connected) {
                        FVBottomBarFragment.this.sendPanormaStart(4);
                        return;
                    } else {
                        return;
                    }
                case 10000:
                    Util.sendIntEventMessge(Constants.EXPOSURE_OF_CAMERA_END);
                    return;
                case 10001:
                    Util.sendIntEventMessge(Constants.LENS_TENSILE_OF_CAMERA_END);
                    return;
                default:
                    return;
            }
        }
    };
    private MyCountDownTimer mMyCountDownTimer;
    private LoadingView mProgressDialog;
    /* access modifiers changed from: private */
    public int mStartAngle = 0;
    private MarkPointChangeSmoothBean markPointChangeSmoothBean;
    private List markPointSmoothList;
    private int markPointSmoothPosition;
    /* access modifiers changed from: private */
    public double mfProRealTimeValueEnd210;
    /* access modifiers changed from: private */
    public int mfProRealTimeValueEnd210Old;
    /* access modifiers changed from: private */
    public double mfProRealTimeValueStart210;
    /* access modifiers changed from: private */
    public int mfProScaleForlevel210;
    /* access modifiers changed from: private */
    public Handler myHitchCockHandler;
    /* access modifiers changed from: private */
    public int overFailCount = 0;
    /* access modifiers changed from: private */
    public String panoramaPath;
    private PopupWindow popupWindow;
    private PopupWindow popupWindowFollow;
    private LoadingView progressDialog;
    /* access modifiers changed from: private */
    public List<Integer> queue;
    /* access modifiers changed from: private */
    public List<Integer> queueExposure;
    private int queueForlevel;
    /* access modifiers changed from: private */
    public List<Integer> queueHandModel;
    /* access modifiers changed from: private */
    public int queueHandModelPosition;
    /* access modifiers changed from: private */
    public int queuePosition;
    /* access modifiers changed from: private */
    public int queuePositionExposure;
    private int queueScaleForlevel;
    private List<Long> queueTime;
    private List<Long> queueTimeExposure;
    private Rect rect;
    /* access modifiers changed from: private */
    public int requreTakePictrueCount = 0;
    /* access modifiers changed from: private */
    public int scaleForExposurePosition;
    private float scaleForlevelNew = 0.0f;
    /* access modifiers changed from: private */
    public int scaleForlevelPosition;
    private SeekThreed seekThreed;
    private SeekZoomThreed210 seekZoomThreed210;
    private MediaPlayer shootMP;
    long sleepMillis = 100;
    /* access modifiers changed from: private */
    public int startFailCount = 0;
    /* access modifiers changed from: private */
    public Boolean startHitchCockVideo = false;
    /* access modifiers changed from: private */
    public Boolean startHitchCockVideoMF = true;
    /* access modifiers changed from: private */
    public Boolean startHitchCockVideoWT = true;
    private boolean startLongExposure = true;
    /* access modifiers changed from: private */
    public double startMarkPointAMF;
    /* access modifiers changed from: private */
    public Boolean startMarkPointAToB = true;
    private double startMarkPointBMF;
    /* access modifiers changed from: private */
    public double startMarkPointMFHalfSecondValue;
    /* access modifiers changed from: private */
    public Boolean startMarkPointMfAToB = false;
    /* access modifiers changed from: private */
    public Boolean startMarkPointMfBToA = false;
    /* access modifiers changed from: private */
    public Boolean startMarkPointMfChangeTimeHalfSecondBoo = false;
    /* access modifiers changed from: private */
    public double startRealTimeMF;
    /* access modifiers changed from: private */
    public double startRealTimeWT;
    /* access modifiers changed from: private */
    public Boolean startSeekThread = false;
    /* access modifiers changed from: private */
    public Boolean startSeekThread210 = false;
    /* access modifiers changed from: private */
    public Boolean startSeekThread300 = false;
    /* access modifiers changed from: private */
    public Boolean startSeekThreadExposure = false;
    /* access modifiers changed from: private */
    public Boolean startSeekThreadExposure300 = false;
    /* access modifiers changed from: private */
    public Boolean startSeekThreadHandModel210 = false;
    /* access modifiers changed from: private */
    public Boolean startSeekThreadMF210 = false;
    /* access modifiers changed from: private */
    public Boolean startSeekThreadNew210 = false;
    /* access modifiers changed from: private */
    public Boolean startSeekThreadNew300 = false;
    /* access modifiers changed from: private */
    public String systemModel;
    /* access modifiers changed from: private */
    public Boolean takePhotoCanOk = true;
    /* access modifiers changed from: private */
    public int takedPictrueCount = 0;
    private int timeNums;
    private int timeNumsExposure;
    /* access modifiers changed from: private */
    public int timeWTOld;
    /* access modifiers changed from: private */
    public int[] videoStartingVolume = {C0853R.mipmap.ic_video_starting_volume_zero, C0853R.mipmap.ic_video_starting_volume_one, C0853R.mipmap.ic_video_starting_volume_two, C0853R.mipmap.ic_video_starting_volume_three, C0853R.mipmap.ic_video_starting_volume_four, C0853R.mipmap.ic_video_starting_volume_five};
    private int widthPingMu;
    /* access modifiers changed from: private */
    public double wtProRealTimeValueEnd210;
    /* access modifiers changed from: private */
    public double wtProRealTimeValueEnd300;
    /* access modifiers changed from: private */
    public double wtProRealTimeValueStart210;
    /* access modifiers changed from: private */
    public double wtProRealTimeValueStart300;
    /* access modifiers changed from: private */
    public int wtProScaleForlevel210;
    /* access modifiers changed from: private */
    public int wtProScaleForlevel300;
    /* access modifiers changed from: private */
    public float yK1;
    /* access modifiers changed from: private */
    public float yK1Exposure;
    /* access modifiers changed from: private */

    /* renamed from: zK */
    public float f1096zK;
    /* access modifiers changed from: private */
    public float zKExposure;

    static /* synthetic */ int access$1208(FVBottomBarFragment x0) {
        int i = x0.overFailCount;
        x0.overFailCount = i + 1;
        return i;
    }

    static /* synthetic */ int access$4008(FVBottomBarFragment x0) {
        int i = x0.scaleForlevelPosition;
        x0.scaleForlevelPosition = i + 1;
        return i;
    }

    static /* synthetic */ int access$5008(FVBottomBarFragment x0) {
        int i = x0.scaleForExposurePosition;
        x0.scaleForExposurePosition = i + 1;
        return i;
    }

    static /* synthetic */ int access$508(FVBottomBarFragment x0) {
        int i = x0.startFailCount;
        x0.startFailCount = i + 1;
        return i;
    }

    class CameraHitchCockZoomThread extends Thread {
        CameraHitchCockZoomThread() {
        }

        public void run() {
            Looper.prepare();
            Handler unused = FVBottomBarFragment.this.myHitchCockHandler = new Handler() {
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case CompanyIdentifierResolver.ETC_SP_ZOO /*310*/:
                            float progress = ((Float) msg.obj).floatValue();
                            if (FVBottomBarFragment.this.cameraManager.getCameraManagerType() == 1) {
                                Log.e("---------------", "---------- 5555  666 777  222 ------  progress:" + progress);
                                FVBottomBarFragment.this.sendToHandler(350, Float.valueOf(progress));
                                return;
                            }
                            if (progress <= 1.02f) {
                                progress = 1.0f;
                            }
                            FVBottomBarFragment.this.cameraManager.setZoom(progress);
                            return;
                        case 320:
                            FVBottomBarFragment.this.cameraManager.setFocusDistance(((Float) msg.obj).floatValue());
                            return;
                        case 334:
                            FVBottomBarFragment.this.sendToHandler(FVBottomBarFragment.HITCH_COCK_ROOM_AND_MF_RESTART_ONE, Float.valueOf(1.0f));
                            return;
                        case 335:
                            FVBottomBarFragment.this.sendToHandler(FVBottomBarFragment.HITCH_COCK_ROOM_AND_MF_RESTART_TWO, Float.valueOf(1.0f));
                            return;
                        case 337:
                            if (!FVBottomBarFragment.this.cameraManager.isMaunalFocus()) {
                                FVBottomBarFragment.this.cameraManager.enableMFMode(true);
                            }
                            float markFocusDistance = ((Float) msg.obj).floatValue();
                            String va = CameraUtils.strSubTwoLength(String.valueOf(markFocusDistance));
                            FVBottomBarFragment.this.cameraManager.setFocusDistance(Float.valueOf(va).floatValue());
                            float seekSeekMf = (10.0f * markFocusDistance) + 10.0f;
                            double unused = FVBottomBarFragment.this.mfProRealTimeValueStart210 = (double) seekSeekMf;
                            double unused2 = FVBottomBarFragment.this.mfProRealTimeValueEnd210 = (double) seekSeekMf;
                            Util.sendIntEventMessge((int) Constants.LABEL_CAMERA_MF_FRAME_VISIBLE, String.valueOf(va));
                            return;
                        case 338:
                            String tv = (String) msg.obj;
                            Double seekSeekMf2 = Double.valueOf((Double.valueOf(tv).doubleValue() * 10.0d) + 10.0d);
                            double unused3 = FVBottomBarFragment.this.mfProRealTimeValueStart210 = seekSeekMf2.doubleValue();
                            double unused4 = FVBottomBarFragment.this.mfProRealTimeValueEnd210 = seekSeekMf2.doubleValue();
                            Util.sendIntEventMessge((int) Constants.LABEL_CAMERA_MF_FRAME_VISIBLE, tv);
                            return;
                        default:
                            return;
                    }
                }
            };
            Looper.loop();
        }
    }

    public void sendToHandlerHitchCock(int what, Object obj) {
        Message me = new Message();
        me.what = what;
        me.obj = obj;
        this.myHitchCockHandler.sendMessage(me);
    }

    private void takeFullPhotoEvery() {
        Size picsize1;
        if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.FULL_SHOT, Integer.valueOf(Constants.FULL_SHOT_NONE))).intValue() == 10028) {
            picsize1 = new Size(1280, 720);
            Log.e("-------------", "------- 1280 X 720  ------");
        } else {
            Log.e("-------------", "------- 1920 X 1080  ------");
            picsize1 = this.cameraManager.getRecommendPictureSize();
        }
        int orientation1 = ScreenOrientationUtil.getInstance().getOrientation();
        if (orientation1 == 0 || orientation1 == 180) {
            this.cameraManager.takePhoto(picsize1.getWidth(), picsize1.getHeight());
        } else {
            this.cameraManager.takePhoto(picsize1.getHeight(), picsize1.getWidth());
        }
        shootSound();
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(C0853R.layout.fragment_bottom_bar_two, container, false);
        ButterKnife.bind((Object) this, view);
        initListener();
        initStatus();
        getFVCameraManager();
        this.isResume = true;
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getRealMetrics(dm);
        this.widthPingMu = dm.widthPixels;
        this.heightPingMu = dm.heightPixels;
        this.queue = new ArrayList();
        this.queueTime = new ArrayList();
        this.queueExposure = new ArrayList();
        this.queueTimeExposure = new ArrayList();
        this.startSeekThread = false;
        this.startSeekThread300 = false;
        this.startSeekThreadNew300 = false;
        this.startSeekThread210 = false;
        this.startSeekThreadNew210 = false;
        this.startSeekThreadExposure = false;
        this.startSeekThreadExposure300 = false;
        this.startSeekThreadMF210 = false;
        this.startHitchCockVideo = false;
        this.startHitchCockVideoWT = true;
        this.startHitchCockVideoMF = true;
        this.startSeekThreadHandModel210 = false;
        this.queueHandModel = new ArrayList();
        if (this.seekThreed != null && this.seekThreed.isAlive()) {
            this.seekThreed.interrupt();
        }
        this.seekThreed = new SeekThreed();
        this.seekThreed.start();
        if (this.seekZoomThreed210 != null && this.seekZoomThreed210.isAlive()) {
            this.seekZoomThreed210.interrupt();
        }
        this.seekZoomThreed210 = new SeekZoomThreed210();
        this.seekZoomThreed210.start();
        this.mContext = getActivity();
        this.takePhotoCanOk = true;
        initCameraConfig();
        MediaVolumeTest(this.cameraManager, getActivity());
        if (this.cameraHitchCockZoomThread != null && this.cameraHitchCockZoomThread.isAlive()) {
            this.cameraHitchCockZoomThread.interrupt();
        }
        this.cameraHitchCockZoomThread = new CameraHitchCockZoomThread();
        this.cameraHitchCockZoomThread.start();
        this.systemModel = Util.getSystemModel();
        this.startMarkPointMfAToB = false;
        this.startMarkPointMfBToA = false;
        this.startMarkPointMfChangeTimeHalfSecondBoo = false;
        return view;
    }

    public void MediaVolumeTest(final FVCameraManager manager, Activity activity) {
        new Thread(new Runnable() {
            public void run() {
                int current;
                while (true) {
                    try {
                        if (CameraUtils.isRecordingIng() && CameraUtils.getBooRecordStarted()) {
                            double d = manager.getMediaRecordVolumeAmplitude();
                            com.freevisiontech.cameralib.utils.CameraUtils.LogV("MediaVolume", "media volume = " + d);
                            if (d < 5.0d) {
                                current = 0;
                            } else {
                                current = ((((int) d) - 5) / 19) + 1;
                            }
                            if (current > 5) {
                                current = 5;
                            }
                            FVBottomBarFragment.this.sendToHandlerVideoStartingVolume(36, Integer.valueOf(current));
                        } else if (FVBottomBarFragment.this.ll_layout_video_starting_volume != null && FVBottomBarFragment.this.ll_layout_video_starting_volume.getVisibility() == 0) {
                            FVBottomBarFragment.this.sendToHandlerVideoStartingVolume(37, "");
                        }
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void sendToHandlerVideoStartingVolume(int what, Object obj) {
        Message me = new Message();
        me.what = what;
        me.obj = obj;
        this.mHandler.sendMessage(me);
    }

    public void sendToHandler(int what, Object obj) {
        Message me = new Message();
        me.what = what;
        me.obj = obj;
        this.mHandler.sendMessage(me);
    }

    private void initCameraConfig() {
        int resolutionLevel = ((Integer) SPUtils.get(getActivity(), SharePrefConstant.PHOTO_RESOLUTION, -1)).intValue();
        int videoResolutionLevel = ((Integer) SPUtils.get(getActivity(), SharePrefConstant.VIDEO_RESOLUTION, -1)).intValue();
        int frontResolutionLevel = ((Integer) SPUtils.get(getActivity(), SharePrefConstant.FRONT_PHOTO_RESOLUTION, -1)).intValue();
        int frontVideoResolutionLevel = ((Integer) SPUtils.get(getActivity(), SharePrefConstant.FRONT_VIDEO_RESOLUTION, -1)).intValue();
        if (resolutionLevel != -1) {
            CameraUtils.setMaxSupOrReComPictureSize(resolutionLevel);
        } else {
            CameraUtils.setMaxSupOrReComPictureSize(0);
        }
        if (frontResolutionLevel != -1) {
            CameraUtils.setMaxSupOrReComPictureFrontSize(frontResolutionLevel);
        } else {
            CameraUtils.setMaxSupOrReComPictureFrontSize(0);
        }
        if (videoResolutionLevel != -1) {
            CameraUtils.setCheckMediaRecordSize(videoResolutionLevel);
        } else {
            CameraUtils.setCheckMediaRecordSize(6);
        }
        if (frontVideoResolutionLevel != -1) {
            CameraUtils.setCheckMediaRecordFrontSize(frontVideoResolutionLevel);
        } else {
            CameraUtils.setCheckMediaRecordFrontSize(6);
        }
    }

    public void onStart() {
        super.onStart();
        this.isResume = true;
    }

    public void onResume() {
        super.onResume();
        this.isResume = true;
        this.broad = new OrientationBroad();
        getActivity().registerReceiver(this.broad, new IntentFilter(ScreenOrientationUtil.BC_OrientationChanged));
        getAngle();
        getCurrentPic();
        audioChannelCheck();
    }

    public void onPause() {
        super.onPause();
        closeScoChannel(true);
        this.takePhotoCanOk = true;
        if (this.broad != null) {
            getActivity().unregisterReceiver(this.broad);
        }
        stopRecordOnly();
        if (!this.btnPhotoVideo.isEnabled()) {
            setBtnPhotoVideoEnable(true);
            setChangePhotoVideoModeEnable(true);
        }
    }

    public void onStop() {
        super.onStop();
    }

    public void onDestroy() {
        super.onDestroy();
        ViseLog.m1466e("BottomBarFragment OnDestroy");
        this.isResume = false;
        closeScoChannel(true);
    }

    private void getFVCameraManager() {
        FVContentFragment contentFragment = (FVContentFragment) getActivity().getSupportFragmentManager().findFragmentByTag("contentFragment");
        if (contentFragment != null) {
            this.cameraManager = contentFragment.getCameraManager();
        }
    }

    private Rect calculateTapAreaCenter(float x, float y, float coefficient) {
        int areaSize = Float.valueOf(200.0f * coefficient).intValue();
        int centerX = (int) (((x / ((float) this.cameraManager.getPreviewResolution().getWidth())) * 2000.0f) - 1000.0f);
        int centerY = (int) (((y / ((float) this.cameraManager.getPreviewResolution().getHeight())) * 2000.0f) - 1000.0f);
        int left = clamp(centerX - (areaSize / 2), -1000, 1000);
        int top = clamp(centerY - (areaSize / 2), -1000, 1000);
        RectF rectF = new RectF((float) left, (float) top, (float) (left + areaSize), (float) (top + areaSize));
        return new Rect(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
    }

    private int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        if (x < min) {
            return min;
        }
        return x;
    }

    private void initStatus() {
        switch (((Integer) SPUtils.get(getActivity(), SharePrefConstant.FLASH_MODE, -1)).intValue()) {
            case 10003:
                this.btnFlashSwitch.setImageResource(C0853R.mipmap.ic_flash_auto);
                break;
            case 10004:
                this.btnFlashSwitch.setImageResource(C0853R.mipmap.ic_flash_open);
                break;
            case 10005:
                this.btnFlashSwitch.setImageResource(C0853R.mipmap.ic_flash_close);
                break;
            case 10006:
                this.btnFlashSwitch.setImageResource(C0853R.mipmap.ic_flash_long_open);
                break;
        }
        switch (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_MODE, -1)).intValue()) {
            case 10001:
                this.fragment_buttom_bar_photo_check.setVisibility(0);
                this.fragment_buttom_bar_video_check.setVisibility(8);
                this.btnPhotoVideo.setImageDrawable(getResources().getDrawable(C0853R.C0854drawable.sl_photo_mode));
                this.isPhotoMode = true;
                return;
            case 10002:
                this.fragment_buttom_bar_photo_check.setVisibility(8);
                this.fragment_buttom_bar_video_check.setVisibility(0);
                this.isPhotoMode = false;
                return;
            default:
                return;
        }
    }

    private void initListener() {
        this.btnFlashSwitch.setOnClickListener(this);
        this.btnFollow.setOnClickListener(this);
        this.btnIconShow.setOnClickListener(this);
        this.btnLensSwitch.setOnClickListener(this);
        this.btnPhotoMode.setOnClickListener(this);
        this.btnPhotoVideo.setOnClickListener(this);
        this.btnVideoMode.setOnClickListener(this);
        this.btnOpenBeauty.setOnClickListener(this);
        this.btnCommonPop.setOnClickListener(this);
        this.btnBleConnect.setOnClickListener(this);
        this.btn_camera_hand_model_shortcut.setOnClickListener(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        this.progressDialog = new LoadingView(getActivity());
        this.connected = ViseBluetooth.getInstance().isConnected();
        if (this.connected) {
            this.btnBleConnect.setImageResource(C0853R.mipmap.ic_ble_connect);
        } else {
            this.btnBleConnect.setImageResource(C0853R.mipmap.ic_ble_disconnect);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onModeSwitch(FVModeSelectEvent fvModeSelectEvent) {
        switch (fvModeSelectEvent.getMode()) {
            case 19:
                CameraUtils.setCheckMediaHighSpeedRecordMapSize(this.cameraManager.getSlowMotionFpsResolutionMap());
                CameraUtils.setVideoStabilizationSupport(Boolean.valueOf(this.cameraManager.isVideoStabilizationSupport()));
                CameraUtils.setMaxSupPictureSize(String.valueOf(this.cameraManager.getMaxSupportedPictureSize()));
                CameraUtils.setReComPictureSize(String.valueOf(this.cameraManager.getRecommendPictureSize()));
                CameraUtils.setMediaRecordSize(this.cameraManager.getSupportedMediaRecordQuality());
                if (this.cameraManager.getCameraManagerType() == 1) {
                    CameraUtils.setUserCameraOneOrTwo(1);
                    CameraUtils.setMediaRecordSize(this.cameraManager.getSupportedMediaRecordQuality());
                    return;
                }
                CameraUtils.setUserCameraOneOrTwo(2);
                CameraUtils.setMediaRecordSizeTwo(this.cameraManager.getSupportedMediaRecordSizes());
                setVideoResoluCheck();
                return;
            case 10003:
                if (this.popupWindow != null) {
                    this.popupWindow.dismiss();
                }
                this.btnFlashSwitch.setImageResource(C0853R.mipmap.ic_flash_auto);
                this.btnFlashSwitch.setVisibility(0);
                return;
            case 10004:
                if (this.popupWindow != null) {
                    this.popupWindow.dismiss();
                }
                this.btnFlashSwitch.setImageResource(C0853R.mipmap.ic_flash_open);
                this.btnFlashSwitch.setVisibility(0);
                return;
            case 10005:
                if (this.popupWindow != null) {
                    this.popupWindow.dismiss();
                }
                this.btnFlashSwitch.setImageResource(C0853R.mipmap.ic_flash_close);
                this.btnFlashSwitch.setVisibility(0);
                return;
            case 10006:
                if (this.popupWindow != null) {
                    this.popupWindow.dismiss();
                }
                this.btnFlashSwitch.setImageResource(C0853R.mipmap.ic_flash_long_open);
                this.btnFlashSwitch.setVisibility(0);
                return;
            case Constants.DELAY_TAKE_PHOTO_OPEN /*10030*/:
                this.commonType = 2;
                this.delayPhotoOpen = true;
                setCommonPopStatus(2, true);
                return;
            case Constants.DELAY_TAKE_PHOTO_CLOSE /*10031*/:
                this.delayPhotoOpen = false;
                setCommonPopStatus(2, false);
                return;
            case Constants.FULL_SHOT_OVER_RECOVER_UNCLICK /*10050*/:
                recoverUnclickMode();
                return;
            case Constants.FULL_SHOT_OPEN /*10055*/:
                this.commonType = 0;
                this.FullShotOpen = true;
                setCommonPopStatus(0, true);
                return;
            case Constants.FULL_SHOT_CLOSE /*10056*/:
                this.FullShotOpen = false;
                setCommonPopStatus(0, false);
                return;
            case Constants.FLASH_AUTO_RESET /*10127*/:
                if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.FLASH_MODE, 10003)).intValue() == 10003) {
                    this.cameraManager.setFlashMode(3);
                }
                setAutoFocusModeNewCameraOpen();
                this.cameraManager.lockAutoExposure(false);
                return;
            case Constants.BEAUTY_OPEN /*10400*/:
                this.beautyOpen = true;
                setCommonPopStatus(3, true);
                return;
            case Constants.BEAUTY_CLOSE /*10401*/:
                this.beautyOpen = false;
                setCommonPopStatus(3, false);
                return;
            case Constants.START_TAKE_PHOTO /*10500*/:
                takePhoto();
                CameraUtils.setDelayPhotoIng(false);
                return;
            case Constants.HAVE_TAKE_PHOTO_OVER /*10505*/:
                showCurrentPicByEvent(fvModeSelectEvent);
                setBtnPhotoVideoEnable(true);
                setChangePhotoVideoModeEnable(true);
                setCameraLensEnable(true);
                return;
            case Constants.HAVE_TAKE_VIDEO_OVER /*10506*/:
                this.btnPhotoVideo.setImageResource(C0853R.mipmap.ic_video);
                recoverUnclickMode();
                showCurrentPicByEvent(fvModeSelectEvent);
                return;
            case Constants.START_TAKE_VIDEO /*10507*/:
                this.btnPhotoVideo.setImageResource(C0853R.mipmap.ic_video_ing);
                dealUnclickableMode();
                return;
            case Constants.CAMERA_PREVENT_JITTER_OPEN_CLOSE /*10997*/:
                if (this.cameraManager.isVideoStabilizationOpened()) {
                    if (this.cameraManager.closeVideoStabilizationMode()) {
                    }
                    return;
                } else {
                    if (this.cameraManager.openVideoStabilizationMode()) {
                    }
                    return;
                }
            case Constants.BTN_LENS_SWITCH_VISIBLE /*10998*/:
                if (CameraUtils.isFollowIng()) {
                    this.btnLensSwitch.setVisibility(4);
                    return;
                } else {
                    this.btnLensSwitch.setVisibility(0);
                    return;
                }
            case Constants.TAKE_PHOTO_OVER_TO_BLE /*105010*/:
                ViseLog.m1466e("拍摄完成,并转告云台");
                if (this.mHandler != null) {
                    while (this.mHandler.hasMessages(PANORAMA_TAKE_PICTRUE_TIMEOUT)) {
                        this.mHandler.removeMessages(PANORAMA_TAKE_PICTRUE_TIMEOUT);
                    }
                    this.mHandler.removeMessages(PANORAMA_TAKE_PICTRUE_TIMEOUT);
                }
                String filePath = (String) fvModeSelectEvent.getMessage();
                if (filePath != null) {
                    this.list.add(filePath);
                    this.takedPictrueCount++;
                    ViseLog.m1466e("拍摄张数" + this.takedPictrueCount);
                    if (this.takedPictrueCount == this.requreTakePictrueCount) {
                        ViseLog.m1466e("全景图全数拍摄完毕");
                        Util.sendIntEventMessge(Constants.FULL_SHOT_END);
                        if (!this.connected) {
                            ViseLog.m1466e("蓝牙断开,全景拍摄退出失败");
                            this.takedPictrueCount = 0;
                            this.list.clear();
                            return;
                        } else if (this.isNeedEnterNextStep) {
                            BleByteUtil.setPanoramaCount((byte) 36, (byte) 1);
                            if (this.mHandler != null) {
                                while (this.mHandler.hasMessages(PANORAMA_MODE_OVER_TIMEOUT)) {
                                    this.mHandler.removeMessages(PANORAMA_MODE_OVER_TIMEOUT);
                                }
                            }
                            this.mHandler.sendEmptyMessageDelayed(PANORAMA_MODE_OVER_TIMEOUT, 8000);
                            return;
                        } else {
                            return;
                        }
                    } else if (!this.connected) {
                        this.takedPictrueCount = 0;
                        this.list.clear();
                        return;
                    } else if (this.isNeedEnterNextStep) {
                        ViseLog.m1466e("告诉云台拍摄完成" + this.takedPictrueCount);
                        BleByteUtil.setPanoramaCount((byte) 35, (byte) this.takedPictrueCount);
                        while (this.mHandler.hasMessages(PANORAMA_MODE_STEP_TIMEOUT)) {
                            this.mHandler.removeMessages(PANORAMA_MODE_STEP_TIMEOUT);
                        }
                        this.mHandler.sendEmptyMessageDelayed(PANORAMA_MODE_STEP_TIMEOUT, 6000);
                        return;
                    } else {
                        return;
                    }
                } else {
                    ViseLog.m1466e("全景拍摄返回路径对象失败");
                    this.takedPictrueCount = 0;
                    this.list.clear();
                    Util.sendIntEventMessge((int) Constants.FULL_SHOT_EXCEPTION_END, BleConstant.ISO);
                    if (this.connected && this.isNeedEnterNextStep) {
                        this.isNeedEnterNextStep = false;
                        CameraUtils.setFullCameraErrorCode("11");
                        BleByteUtil.setPanoramaCount((byte) 36, (byte) 1);
                        if (this.mHandler != null) {
                            while (this.mHandler.hasMessages(PANORAMA_MODE_OVER_TIMEOUT)) {
                                this.mHandler.removeMessages(PANORAMA_MODE_OVER_TIMEOUT);
                            }
                        }
                        this.mHandler.sendEmptyMessageDelayed(PANORAMA_MODE_OVER_TIMEOUT, 8000);
                        return;
                    }
                    return;
                }
            case Constants.CANCLE_PANORAMA_TO_BLE /*105011*/:
                ViseLog.m1466e("手动取消全景");
                if (this.mHandler != null) {
                    this.mHandler.removeCallbacksAndMessages((Object) null);
                }
                if (!this.connected) {
                    ViseLog.m1466e("蓝牙断开,全景拍摄退出失败");
                    this.takedPictrueCount = 0;
                    this.list.clear();
                    return;
                } else if (this.isNeedEnterNextStep) {
                    this.isNeedEnterNextStep = false;
                    CameraUtils.setFullCameraErrorCode("12");
                    BleByteUtil.setPanoramaCount((byte) 36, (byte) 1);
                    if (this.mHandler != null) {
                        while (this.mHandler.hasMessages(PANORAMA_MODE_OVER_TIMEOUT)) {
                            this.mHandler.removeMessages(PANORAMA_MODE_OVER_TIMEOUT);
                        }
                    }
                    this.mHandler.sendEmptyMessageDelayed(PANORAMA_MODE_OVER_TIMEOUT, 8000);
                    return;
                } else {
                    return;
                }
            case Constants.LONG_EXPOSURE_OPEN /*106201*/:
                this.commonType = 1;
                this.longExposureOpen = true;
                setCommonPopStatus(1, true);
                return;
            case Constants.LONG_EXPOSURE_CLOSE /*106202*/:
                this.longExposureOpen = false;
                setCommonPopStatus(1, false);
                return;
            case Constants.START_KCF_TO_BOTTOMBAR /*106403*/:
                this.rect = fvModeSelectEvent.getRect();
                BleByteUtil.setPTZParameters((byte) 21, (byte) 1, (byte) 0);
                this.mHandler.sendEmptyMessageDelayed(START_FOLLOW_MODE_TIMEOUT, 1000);
                int angle = ScreenOrientationUtil.getInstance().getOrientation();
                if (angle == 0) {
                    CameraUtils.setPhoneAngle(0);
                    return;
                } else if (angle == 90) {
                    CameraUtils.setPhoneAngle(90);
                    return;
                } else if (angle == 180) {
                    CameraUtils.setPhoneAngle(180);
                    return;
                } else if (angle == 270) {
                    CameraUtils.setPhoneAngle(270);
                    return;
                } else {
                    return;
                }
            case Constants.EXCLUSIVE_CLOSE_KCF /*106404*/:
                MoveTimelapseUtil.getInstance();
                MoveTimelapseUtil.setCameraTrackingStart(0);
                this.isFollowMode = false;
                this.btnFollow.setImageResource(C0853R.mipmap.ic_follow_close);
                CameraUtils.setFollowIng(false);
                this.btnLensSwitch.setEnabled(true);
                if (this.connected) {
                    BleByteUtil.setPTZParameters((byte) 21, (byte) 0, (byte) 0);
                    Util.sendIntEventMessge(Constants.CLOSE_KCF);
                    if (!CameraUtils.getBooRecordStarted()) {
                        this.btnLensSwitch.setVisibility(0);
                    }
                    this.mHandler.sendEmptyMessageDelayed(CLOSE_FOLLOW_MODE_TIMEOUT, 1000);
                }
                setCameraHandModelUIVisibleOrGone(false);
                return;
            case Constants.FV_CAMERA_SLEEP_CLOSE_VIDEO /*106405*/:
                stopRecordOnly();
                return;
            case Constants.EXCLUSIVE_CLOSE_KCF_CONNECT /*106407*/:
                MoveTimelapseUtil.getInstance();
                MoveTimelapseUtil.setCameraTrackingStart(0);
                this.isFollowMode = false;
                this.btnFollow.setImageResource(C0853R.mipmap.ic_follow_close);
                CameraUtils.setFollowIng(false);
                this.btnLensSwitch.setEnabled(true);
                Util.sendIntEventMessge(Constants.CLOSE_KCF);
                if (!CameraUtils.getBooRecordStarted()) {
                    this.btnLensSwitch.setVisibility(0);
                }
                setCameraHandModelUIVisibleOrGone(false);
                return;
            case Constants.MOVE_OR_DELAY_TIMELAPSE_COUNTDOWN_END /*106950*/:
                String timeStr = (String) fvModeSelectEvent.getMessage();
                if (!Util.isEmpty(timeStr) && this.mHandler != null) {
                    this.mHandler.removeMessages(20);
                    this.mHandler.sendEmptyMessageDelayed(20, (long) Integer.valueOf(timeStr).intValue());
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_HAND_MODEL_OPEN_VISIBLE /*107215*/:
                this.btn_camera_hand_model_shortcut.setVisibility(0);
                return;
            case Constants.LABEL_CAMERA_HAND_MODEL_CLOSE_GONE /*107216*/:
                this.btn_camera_hand_model_shortcut.setVisibility(8);
                return;
            case Constants.CAMERA_FOLLOW_CLOSE_GRAY /*107501*/:
                if (this.popupWindowFollow != null) {
                    this.popupWindowFollow.dismiss();
                }
                BleByteUtil.setPTZParameters((byte) 21, (byte) 0, (byte) 0);
                this.mHandler.sendEmptyMessageDelayed(CLOSE_FOLLOW_MODE_TIMEOUT, 1000);
                return;
            case Constants.CAMERA_HITCH_COCK_START_VIDEO /*107601*/:
                hitchCockRecord();
                return;
            case Constants.CAMERA_HITCH_COCK_ZOOM_RESTART_TWO /*107604*/:
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        FVBottomBarFragment.this.sendToHandlerHitchCock(335, Float.valueOf(1.0f));
                    }
                }, 200);
                return;
            case Constants.CAMERA_HITCH_COCK_ZOOM_RESTART_ONE /*107605*/:
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        FVBottomBarFragment.this.sendToHandlerHitchCock(334, Float.valueOf(1.0f));
                    }
                }, 200);
                return;
            case Constants.GET_FLASH_OFF_210 /*107720*/:
                Util.sendIntEventMessge(10005);
                SPUtils.put(this.mContext, SharePrefConstant.FLASH_MODE, 10005);
                this.cameraManager.setFlashMode(0);
                return;
            case Constants.GET_FLASH_ON_210 /*107721*/:
                Util.sendIntEventMessge(10004);
                SPUtils.put(this.mContext, SharePrefConstant.FLASH_MODE, 10004);
                this.cameraManager.setFlashMode(1);
                return;
            case Constants.GET_FLASH_AUTO_210 /*107722*/:
                Util.sendIntEventMessge(10003);
                SPUtils.put(this.mContext, SharePrefConstant.FLASH_MODE, 10003);
                this.cameraManager.setFlashMode(3);
                return;
            case Constants.GET_FLASH_LONG_210 /*107723*/:
                Util.sendIntEventMessge(10006);
                SPUtils.put(this.mContext, SharePrefConstant.FLASH_MODE, 10006);
                this.cameraManager.setFlashMode(2);
                return;
            case Constants.MARK_POINT_A_MF_RETURN_210 /*107726*/:
                if (this.cameraManager != null && this.cameraManager.getCameraManagerType() == 2) {
                    if (!this.cameraManager.isMaunalFocus()) {
                        this.cameraManager.enableMFMode(true);
                    }
                    if (!this.startMarkPointMfAToB.booleanValue() && !this.startMarkPointMfBToA.booleanValue() && !this.startMarkPointMfChangeTimeHalfSecondBoo.booleanValue() && !this.startMarkPointMfChangeTimeHalfSecondBoo.booleanValue()) {
                        String valueMF = String.valueOf(this.cameraManager.getFocusDistance());
                        String va = CameraUtils.strSubTwoLength(valueMF);
                        if (((double) Math.abs(Float.valueOf(valueMF).floatValue() - CameraUtils.getLlMarkPointMfA())) < 0.4d) {
                            va = String.valueOf(CameraUtils.getLlMarkPointMfA());
                        }
                        Util.sendIntEventMessge((int) Constants.LABEL_CAMERA_MF_FRAME_VISIBLE, String.valueOf(va));
                    }
                    String value5 = CameraUtils.strSubTwoLength(String.valueOf(this.cameraManager.getFocusDistance()));
                    if (this.startMarkPointMfAToB.booleanValue() || this.startMarkPointMfBToA.booleanValue()) {
                        if (this.startMarkPointMfBToA.booleanValue()) {
                            Log.e("---------------", "----------  6989  3269 8745   2222  2222  2222  --------");
                            if (((double) Math.abs(Float.valueOf(value5).floatValue() - CameraUtils.getLlMarkPointMfA())) < 0.4d) {
                            }
                        } else if (this.startMarkPointMfAToB.booleanValue()) {
                            Log.e("---------------", "----------  6989  3269 8745   3333  3333  3333  --------");
                            this.startMarkPointMfAToB = null;
                            this.startMarkPointMfBToA = null;
                            this.startMarkPointMfChangeTimeHalfSecondBoo = null;
                            this.cameraManager.setFocusDistance(CameraUtils.getLlMarkPointMfB());
                            if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_MARK_POINT_CHANGE_SPEED, 3)).intValue() == 6) {
                                this.startMarkPointAToB = null;
                                this.startMarkPointMFHalfSecondValue = (double) CameraUtils.getLlMarkPointMfB();
                                this.startMarkPointMfChangeTimeHalfSecondBoo = 1;
                                this.startMarkPointMfAToB = null;
                                this.startMarkPointMfBToA = null;
                            } else {
                                if (!this.startMarkPointMfBToA.booleanValue()) {
                                    this.startMarkPointAMF = (double) this.cameraManager.getFocusDistance();
                                }
                                if (this.mHandler != null) {
                                    while (this.mHandler.hasMessages(MARK_POINT_MF_B_TO_A)) {
                                        this.mHandler.removeMessages(MARK_POINT_MF_B_TO_A);
                                    }
                                }
                                this.mHandler.sendEmptyMessage(MARK_POINT_MF_B_TO_A);
                            }
                        }
                    } else if (((double) Math.abs(Float.valueOf(value5).floatValue() - CameraUtils.getLlMarkPointMfB())) < 0.4d) {
                        if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_MARK_POINT_CHANGE_SPEED, 3)).intValue() == 6) {
                            this.startMarkPointMFHalfSecondValue = (double) CameraUtils.getLlMarkPointMfB();
                            this.startMarkPointAToB = null;
                            this.startMarkPointMfChangeTimeHalfSecondBoo = 1;
                            this.startMarkPointMfAToB = null;
                            this.startMarkPointMfBToA = null;
                        } else {
                            Log.e("---------------", "----------  6989  3269 8745   0000  0000  0000  --------");
                            if (!this.startMarkPointMfBToA.booleanValue()) {
                                this.startMarkPointAMF = (double) CameraUtils.getLlMarkPointMfB();
                            }
                            if (this.mHandler != null) {
                                while (this.mHandler.hasMessages(MARK_POINT_MF_B_TO_A)) {
                                    this.mHandler.removeMessages(MARK_POINT_MF_B_TO_A);
                                }
                            }
                            this.mHandler.sendEmptyMessage(MARK_POINT_MF_B_TO_A);
                        }
                    } else if (((double) Math.abs(Float.valueOf(value5).floatValue() - CameraUtils.getLlMarkPointMfA())) < 0.4d) {
                        Log.e("---------------", "----------  6989  3269 8745   1111  1111  1111  不作处理 不作处理 --------");
                    } else {
                        Log.e("---------------", "----------  6989  3269 8745   1111  1111  1111  --------");
                        if (!this.startMarkPointMfBToA.booleanValue()) {
                            this.startMarkPointAMF = (double) this.cameraManager.getFocusDistance();
                        }
                        if (this.mHandler != null) {
                            while (this.mHandler.hasMessages(MARK_POINT_MF_B_TO_A)) {
                                this.mHandler.removeMessages(MARK_POINT_MF_B_TO_A);
                            }
                        }
                        this.mHandler.sendEmptyMessage(MARK_POINT_MF_B_TO_A);
                    }
                    float llMarkPointMfB = CameraUtils.getLlMarkPointMfB();
                    CameraUtils.getLlMarkPointMfA();
                    return;
                }
                return;
            case Constants.MARK_POINT_B_MF_RETURN_210 /*107727*/:
                if (this.cameraManager != null && this.cameraManager.getCameraManagerType() == 2) {
                    if (!this.cameraManager.isMaunalFocus()) {
                        this.cameraManager.enableMFMode(true);
                    }
                    if (!this.startMarkPointMfAToB.booleanValue() && !this.startMarkPointMfBToA.booleanValue() && !this.startMarkPointMfChangeTimeHalfSecondBoo.booleanValue() && !this.startMarkPointMfChangeTimeHalfSecondBoo.booleanValue()) {
                        String valueMF2 = String.valueOf(this.cameraManager.getFocusDistance());
                        String va2 = CameraUtils.strSubTwoLength(valueMF2);
                        if (((double) Math.abs(Float.valueOf(valueMF2).floatValue() - CameraUtils.getLlMarkPointMfB())) < 0.4d) {
                            va2 = String.valueOf(CameraUtils.getLlMarkPointMfB());
                        }
                        Util.sendIntEventMessge((int) Constants.LABEL_CAMERA_MF_FRAME_VISIBLE, String.valueOf(va2));
                    }
                    String value52 = CameraUtils.strSubTwoLength(String.valueOf(this.cameraManager.getFocusDistance()));
                    if (this.startMarkPointMfAToB.booleanValue() || this.startMarkPointMfBToA.booleanValue()) {
                        if (this.startMarkPointMfAToB.booleanValue()) {
                            Log.e("---------------", "----------  6989  3269 8745   6666  6666  6666  --------");
                            if (((double) Math.abs(Float.valueOf(value52).floatValue() - CameraUtils.getLlMarkPointMfB())) < 0.4d) {
                            }
                        } else if (this.startMarkPointMfBToA.booleanValue()) {
                            Log.e("---------------", "----------  6989  3269 8745   7777  7777  7777  --------");
                            this.startMarkPointMfBToA = null;
                            this.startMarkPointMfAToB = null;
                            this.startMarkPointMfChangeTimeHalfSecondBoo = null;
                            if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_MARK_POINT_CHANGE_SPEED, 3)).intValue() == 6) {
                                this.cameraManager.setFocusDistance(CameraUtils.getLlMarkPointMfA());
                                this.startMarkPointAToB = 1;
                                this.startMarkPointMFHalfSecondValue = (double) CameraUtils.getLlMarkPointMfA();
                                this.startMarkPointMfChangeTimeHalfSecondBoo = 1;
                                this.startMarkPointMfAToB = null;
                                this.startMarkPointMfBToA = null;
                            } else {
                                if (!this.startMarkPointMfAToB.booleanValue()) {
                                    this.startMarkPointAMF = (double) this.cameraManager.getFocusDistance();
                                }
                                if (this.mHandler != null) {
                                    while (this.mHandler.hasMessages(MARK_POINT_MF_A_TO_B)) {
                                        this.mHandler.removeMessages(MARK_POINT_MF_A_TO_B);
                                    }
                                }
                                this.mHandler.sendEmptyMessage(MARK_POINT_MF_A_TO_B);
                            }
                        }
                    } else if (((double) Math.abs(Float.valueOf(value52).floatValue() - CameraUtils.getLlMarkPointMfA())) < 0.4d) {
                        if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_MARK_POINT_CHANGE_SPEED, 3)).intValue() == 6) {
                            this.startMarkPointAToB = 1;
                            this.startMarkPointMFHalfSecondValue = (double) CameraUtils.getLlMarkPointMfA();
                            this.startMarkPointMfChangeTimeHalfSecondBoo = 1;
                            this.startMarkPointMfAToB = null;
                            this.startMarkPointMfBToA = null;
                        } else {
                            Log.e("---------------", "----------  6989  3269 8745   4444  4444  4444  --------");
                            if (!this.startMarkPointMfAToB.booleanValue()) {
                                this.startMarkPointAMF = (double) CameraUtils.getLlMarkPointMfA();
                            }
                            if (this.mHandler != null) {
                                while (this.mHandler.hasMessages(MARK_POINT_MF_A_TO_B)) {
                                    this.mHandler.removeMessages(MARK_POINT_MF_A_TO_B);
                                }
                            }
                            this.mHandler.sendEmptyMessage(MARK_POINT_MF_A_TO_B);
                        }
                    } else if (((double) Math.abs(Float.valueOf(value52).floatValue() - CameraUtils.getLlMarkPointMfB())) < 0.4d) {
                        Log.e("---------------", "----------  6989  3269 8745   5555  5555  5555  不作处理 不作处理 --------");
                    } else {
                        Log.e("---------------", "----------  6989  3269 8745   5555  5555  5555  --------");
                        if (!this.startMarkPointMfAToB.booleanValue()) {
                            this.startMarkPointAMF = (double) this.cameraManager.getFocusDistance();
                        }
                        if (this.mHandler != null) {
                            while (this.mHandler.hasMessages(MARK_POINT_MF_A_TO_B)) {
                                this.mHandler.removeMessages(MARK_POINT_MF_A_TO_B);
                            }
                        }
                        this.mHandler.sendEmptyMessage(MARK_POINT_MF_A_TO_B);
                    }
                    float llMarkPointMfB2 = CameraUtils.getLlMarkPointMfB();
                    CameraUtils.getLlMarkPointMfA();
                    return;
                }
                return;
            case Constants.FV_BEAUTY_POP_SHOW /*107733*/:
                showBeautyPop();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        CameraUtils.setFrameLayerNumber(9);
                    }
                }, 100);
                return;
            case Constants.CAMERA_MARK_POINT_QUIT_OUT_THREAD_STOP /*107755*/:
                this.mfProRealTimeValueStart210 = 0.0d;
                this.mfProRealTimeValueEnd210 = 0.0d;
                this.startMarkPointMfAToB = null;
                this.startMarkPointMfBToA = null;
                this.startMarkPointMfChangeTimeHalfSecondBoo = null;
                return;
            case Constants.CAMERA_THREAD_210_WT_AND_MF_STOP /*107756*/:
                this.startSeekThreadNew210 = null;
                this.startSeekThreadMF210 = null;
                return;
            case Constants.CAMERA_THREAD_210_MF_EQUAL /*107765*/:
                float seekSeekMf = (Float.valueOf((String) fvModeSelectEvent.getMessage()).floatValue() * 10.0f) + 10.0f;
                this.mfProRealTimeValueStart210 = (double) seekSeekMf;
                this.mfProRealTimeValueEnd210 = (double) seekSeekMf;
                return;
            case Constants.CAMERA_MARK_POINT_THREAD_RESTRAST /*107767*/:
                this.startMarkPointMfAToB = null;
                this.startMarkPointMfBToA = null;
                this.startMarkPointMfChangeTimeHalfSecondBoo = null;
                return;
            default:
                return;
        }
    }

    private void audioChannelCheck() {
        closeScoChannel(((Integer) SPUtils.get(getActivity(), SharePrefConstant.MICROPHONE_SOUND_SYSYTEM_OR_BOUETOOTH, Integer.valueOf(Constants.MICROPHONE_SOUND_SYSYTEM))).intValue() == 107091);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCome(Event event) {
        if (event != null) {
            receiveEvent(event);
        }
    }

    /* JADX WARNING: type inference failed for: r27v1, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r28v17, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v20, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v23, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v26, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v29, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v32, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v35, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v38, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v41, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v44, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v47, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v50, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v53, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v56, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v59, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v63, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v80, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v83, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v86, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v98, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v100, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v145, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v147, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v195, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v198, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v201, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v205, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v208, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v211, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v272, types: [byte] */
    /* JADX WARNING: type inference failed for: r29v80, types: [byte] */
    /* JADX WARNING: type inference failed for: r32v8, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v277, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v299, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v313, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v320, types: [byte, int] */
    /* JADX WARNING: type inference failed for: r29v95, types: [byte, int] */
    /* JADX WARNING: type inference failed for: r29v116, types: [byte, int] */
    /* JADX WARNING: type inference failed for: r29v119, types: [byte] */
    /* JADX WARNING: type inference failed for: r29v123, types: [byte, int] */
    /* JADX WARNING: type inference failed for: r29v126, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v372, types: [byte, int] */
    /* JADX WARNING: type inference failed for: r29v129, types: [byte, int] */
    /* JADX WARNING: type inference failed for: r28v491, types: [byte] */
    /* JADX WARNING: type inference failed for: r5v0, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v502, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v515, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v518, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v521, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v524, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v527, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v530, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v536, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v539, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v546, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v549, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v561, types: [byte] */
    /* JADX WARNING: type inference failed for: r28v568, types: [byte] */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void receiveEvent(com.freevisiontech.fvmobile.utils.Event r35) {
        /*
            r34 = this;
            int r28 = r35.getCode()
            switch(r28) {
                case 34: goto L_0x0008;
                case 56: goto L_0x138d;
                case 119: goto L_0x00d3;
                case 120: goto L_0x00b2;
                case 121: goto L_0x00c8;
                case 128: goto L_0x0007;
                default: goto L_0x0007;
            }
        L_0x0007:
            return
        L_0x0008:
            java.lang.String r28 = "disconnect"
            com.vise.log.ViseLog.m1466e(r28)
            r28 = 0
            r0 = r28
            r1 = r34
            r1.connected = r0
            r28 = 0
            r0 = r28
            r1 = r34
            r1.isFollowMode = r0
            r0 = r34
            android.widget.ImageView r0 = r0.btnFollow
            r28 = r0
            r29 = 2130903226(0x7f0300ba, float:1.7413264E38)
            r28.setImageResource(r29)
            r0 = r34
            android.widget.ImageView r0 = r0.btnBleConnect
            r28 = r0
            r29 = 2130903129(0x7f030059, float:1.7413067E38)
            r28.setImageResource(r29)
            r28 = 0
            com.freevisiontech.fvmobile.utility.CameraUtils.setFollowIng(r28)
            r0 = r34
            android.widget.ImageView r0 = r0.btnLensSwitch
            r28 = r0
            r29 = 1
            r28.setEnabled(r29)
            r0 = r34
            android.os.Handler r0 = r0.mHandler
            r28 = r0
            if (r28 == 0) goto L_0x006c
            com.freevisiontech.fvmobile.utils.MoveTimelapseUtil r28 = com.freevisiontech.fvmobile.utils.MoveTimelapseUtil.getInstance()
            int r28 = r28.getCameraProgressLinear()
            r29 = 1
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x00a6
            r0 = r34
            android.os.Handler r0 = r0.mHandler
            r28 = r0
            r29 = 20
            boolean r28 = r28.hasMessages(r29)
            if (r28 == 0) goto L_0x00a6
        L_0x006c:
            r0 = r34
            boolean r0 = r0.isNeedEnterNextStep
            r28 = r0
            if (r28 == 0) goto L_0x009b
            r28 = 0
            r0 = r28
            r1 = r34
            r1.isNeedEnterNextStep = r0
            java.lang.String r28 = "13"
            com.freevisiontech.fvmobile.utility.CameraUtils.setFullCameraErrorCode(r28)
            r0 = r34
            java.util.ArrayList<java.lang.String> r0 = r0.list
            r28 = r0
            r28.clear()
            r28 = 0
            r0 = r28
            r1 = r34
            r1.takedPictrueCount = r0
            r28 = 10042(0x273a, float:1.4072E-41)
            java.lang.String r29 = "2"
            com.freevisiontech.fvmobile.utility.Util.sendIntEventMessge((int) r28, (java.lang.String) r29)
        L_0x009b:
            r28 = 0
            r0 = r34
            r1 = r28
            r0.setCameraHandModelUIVisibleOrGone(r1)
            goto L_0x0007
        L_0x00a6:
            r0 = r34
            android.os.Handler r0 = r0.mHandler
            r28 = r0
            r29 = 0
            r28.removeCallbacksAndMessages(r29)
            goto L_0x006c
        L_0x00b2:
            r28 = 1
            r0 = r28
            r1 = r34
            r1.connected = r0
            r0 = r34
            android.widget.ImageView r0 = r0.btnBleConnect
            r28 = r0
            r29 = 2130903128(0x7f030058, float:1.7413065E38)
            r28.setImageResource(r29)
            goto L_0x0007
        L_0x00c8:
            r28 = 1
            r0 = r34
            r1 = r28
            r0.showMainSearchPop(r1)
            goto L_0x0007
        L_0x00d3:
            java.lang.Object r28 = r35.getData()
            byte[] r28 = (byte[]) r28
            r27 = r28
            byte[] r27 = (byte[]) r27
            r28 = 0
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r28 = r0
            r29 = 165(0xa5, float:2.31E-43)
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x0363
            r28 = 1
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r28 = r0
            r29 = 34
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x015c
            r0 = r34
            android.os.Handler r0 = r0.mHandler
            r28 = r0
            if (r28 == 0) goto L_0x0123
        L_0x0109:
            r0 = r34
            android.os.Handler r0 = r0.mHandler
            r28 = r0
            r29 = 9999(0x270f, float:1.4012E-41)
            boolean r28 = r28.hasMessages(r29)
            if (r28 == 0) goto L_0x0123
            r0 = r34
            android.os.Handler r0 = r0.mHandler
            r28 = r0
            r29 = 9999(0x270f, float:1.4012E-41)
            r28.removeMessages(r29)
            goto L_0x0109
        L_0x0123:
            r28 = 1
            r0 = r28
            r1 = r34
            r1.isNeedEnterNextStep = r0
            r28 = 0
            r0 = r28
            r1 = r34
            r1.startFailCount = r0
        L_0x0133:
            r0 = r34
            android.os.Handler r0 = r0.mHandler
            r28 = r0
            r29 = 9998(0x270e, float:1.401E-41)
            boolean r28 = r28.hasMessages(r29)
            if (r28 == 0) goto L_0x014d
            r0 = r34
            android.os.Handler r0 = r0.mHandler
            r28 = r0
            r29 = 9998(0x270e, float:1.401E-41)
            r28.removeMessages(r29)
            goto L_0x0133
        L_0x014d:
            r0 = r34
            android.os.Handler r0 = r0.mHandler
            r28 = r0
            r29 = 9998(0x270e, float:1.401E-41)
            r30 = 6000(0x1770, double:2.9644E-320)
            r28.sendEmptyMessageDelayed(r29, r30)
            goto L_0x0007
        L_0x015c:
            r28 = 1
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r28 = r0
            r29 = 36
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x0225
            r28 = 2
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r28 = r0
            r29 = 1
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x0007
            r0 = r34
            android.os.Handler r0 = r0.mHandler
            r28 = r0
            if (r28 == 0) goto L_0x01a2
        L_0x0188:
            r0 = r34
            android.os.Handler r0 = r0.mHandler
            r28 = r0
            r29 = 9997(0x270d, float:1.4009E-41)
            boolean r28 = r28.hasMessages(r29)
            if (r28 == 0) goto L_0x01a2
            r0 = r34
            android.os.Handler r0 = r0.mHandler
            r28 = r0
            r29 = 9997(0x270d, float:1.4009E-41)
            r28.removeMessages(r29)
            goto L_0x0188
        L_0x01a2:
            r0 = r34
            boolean r0 = r0.isNeedEnterNextStep
            r28 = r0
            if (r28 == 0) goto L_0x01e5
            r28 = 0
            r0 = r28
            r1 = r34
            r1.takedPictrueCount = r0
            r28 = 0
            r0 = r28
            r1 = r34
            r1.overFailCount = r0
            java.lang.String r28 = "BottomFragment"
            java.lang.StringBuilder r29 = new java.lang.StringBuilder
            r29.<init>()
            java.lang.String r30 = "bottomfragment: 全景拍摄退出成功"
            java.lang.StringBuilder r29 = r29.append(r30)
            long r30 = java.lang.System.currentTimeMillis()
            java.lang.StringBuilder r29 = r29.append(r30)
            java.lang.String r29 = r29.toString()
            android.util.Log.e(r28, r29)
            r0 = r34
            android.os.Handler r0 = r0.mHandler
            r28 = r0
            r29 = 9990(0x2706, float:1.3999E-41)
            r28.sendEmptyMessage(r29)
            goto L_0x0007
        L_0x01e5:
            r0 = r34
            java.util.ArrayList<java.lang.String> r0 = r0.list
            r28 = r0
            r28.clear()
            r28 = 0
            r0 = r28
            r1 = r34
            r1.takedPictrueCount = r0
            java.lang.String r28 = "BottomFragment"
            java.lang.StringBuilder r29 = new java.lang.StringBuilder
            r29.<init>()
            java.lang.String r30 = "bottomfragment: 全景拍摄异常退出成功"
            java.lang.StringBuilder r29 = r29.append(r30)
            long r30 = java.lang.System.currentTimeMillis()
            java.lang.StringBuilder r29 = r29.append(r30)
            java.lang.String r29 = r29.toString()
            android.util.Log.e(r28, r29)
            java.lang.String r28 = "------------"
            java.lang.String r29 = "-------- 全景拍照  66666 ----"
            android.util.Log.e(r28, r29)
            r28 = 106406(0x19fa6, float:1.49107E-40)
            com.freevisiontech.fvmobile.utility.Util.sendIntEventMessge(r28)
            goto L_0x0007
        L_0x0225:
            r28 = 1
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r28 = r0
            r29 = 35
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x0265
            r28 = 2
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r18 = r0
            java.lang.StringBuilder r28 = new java.lang.StringBuilder
            r28.<init>()
            java.lang.String r29 = "云台收到第"
            java.lang.StringBuilder r28 = r28.append(r29)
            r0 = r28
            r1 = r18
            java.lang.StringBuilder r28 = r0.append(r1)
            java.lang.String r29 = "张拍摄完成并回复"
            java.lang.StringBuilder r28 = r28.append(r29)
            java.lang.String r28 = r28.toString()
            com.vise.log.ViseLog.m1466e(r28)
            goto L_0x0007
        L_0x0265:
            r28 = 1
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r28 = r0
            r29 = 21
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x0007
            r28 = 2
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r28 = r0
            if (r28 != 0) goto L_0x0302
            java.lang.String r28 = "自动跟随模式开关关闭"
            com.vise.log.ViseLog.m1466e(r28)
            r0 = r34
            android.os.Handler r0 = r0.mHandler
            r28 = r0
            if (r28 == 0) goto L_0x029c
            r0 = r34
            android.os.Handler r0 = r0.mHandler
            r28 = r0
            r29 = 9994(0x270a, float:1.4005E-41)
            r28.removeMessages(r29)
        L_0x029c:
            r28 = 3
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r28 = r0
            if (r28 != 0) goto L_0x02ee
            r28 = 0
            r0 = r28
            r1 = r34
            r1.isFollowMode = r0
            r28 = 106401(0x19fa1, float:1.491E-40)
            com.freevisiontech.fvmobile.utility.Util.sendIntEventMessge(r28)
            r0 = r34
            android.widget.ImageView r0 = r0.btnFollow
            r28 = r0
            r29 = 2130903226(0x7f0300ba, float:1.7413264E38)
            r28.setImageResource(r29)
            r28 = 0
            com.freevisiontech.fvmobile.utility.CameraUtils.setFollowIng(r28)
            r0 = r34
            android.widget.ImageView r0 = r0.btnLensSwitch
            r28 = r0
            r29 = 1
            r28.setEnabled(r29)
            boolean r28 = com.freevisiontech.fvmobile.utility.CameraUtils.getBooRecordStarted()
            if (r28 != 0) goto L_0x02e3
            r0 = r34
            android.widget.ImageView r0 = r0.btnLensSwitch
            r28 = r0
            r29 = 0
            r28.setVisibility(r29)
        L_0x02e3:
            r28 = 0
            r0 = r34
            r1 = r28
            r0.setCameraHandModelUIVisibleOrGone(r1)
            goto L_0x0007
        L_0x02ee:
            r28 = 3
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r28 = r0
            r29 = 1
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x0007
            goto L_0x0007
        L_0x0302:
            r28 = 2
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r28 = r0
            r29 = 1
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x0007
            java.lang.String r28 = "自动跟随模式开关打开"
            com.vise.log.ViseLog.m1466e(r28)
            r28 = 1
            r0 = r28
            r1 = r34
            r1.isFollowMode = r0
            r0 = r34
            android.os.Handler r0 = r0.mHandler
            r28 = r0
            if (r28 == 0) goto L_0x0335
            r0 = r34
            android.os.Handler r0 = r0.mHandler
            r28 = r0
            r29 = 9995(0x270b, float:1.4006E-41)
            r28.removeMessages(r29)
        L_0x0335:
            r28 = 3
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r28 = r0
            if (r28 != 0) goto L_0x034f
            r28 = 106402(0x19fa2, float:1.49101E-40)
            r0 = r34
            android.graphics.Rect r0 = r0.rect
            r29 = r0
            com.freevisiontech.fvmobile.utility.Util.sendIntEventMessge((int) r28, (android.graphics.Rect) r29)
            goto L_0x0007
        L_0x034f:
            r28 = 3
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r28 = r0
            r29 = 1
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x0007
            goto L_0x0007
        L_0x0363:
            r28 = 0
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r28 = r0
            r29 = 90
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x0007
            r28 = 1
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r28 = r0
            r29 = 35
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x03f6
            r0 = r34
            android.os.Handler r0 = r0.mHandler
            r28 = r0
            if (r28 == 0) goto L_0x03a9
        L_0x038f:
            r0 = r34
            android.os.Handler r0 = r0.mHandler
            r28 = r0
            r29 = 9998(0x270e, float:1.401E-41)
            boolean r28 = r28.hasMessages(r29)
            if (r28 == 0) goto L_0x03a9
            r0 = r34
            android.os.Handler r0 = r0.mHandler
            r28 = r0
            r29 = 9998(0x270e, float:1.401E-41)
            r28.removeMessages(r29)
            goto L_0x038f
        L_0x03a9:
            r28 = 2
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r18 = r0
            java.lang.StringBuilder r28 = new java.lang.StringBuilder
            r28.<init>()
            java.lang.String r29 = "云台第"
            java.lang.StringBuilder r28 = r28.append(r29)
            r0 = r28
            r1 = r18
            java.lang.StringBuilder r28 = r0.append(r1)
            java.lang.String r29 = "张移动完成"
            java.lang.StringBuilder r28 = r28.append(r29)
            java.lang.String r28 = r28.toString()
            com.vise.log.ViseLog.m1466e(r28)
            r0 = r34
            boolean r0 = r0.connected
            r28 = r0
            if (r28 == 0) goto L_0x0007
            r28 = 35
            r0 = r18
            byte r0 = (byte) r0
            r29 = r0
            com.freevisiontech.fvmobile.utils.BleByteUtil.ackPTZPanorama(r28, r29)
            r0 = r34
            android.os.Handler r0 = r0.mHandler
            r28 = r0
            r29 = 9989(0x2705, float:1.3998E-41)
            r30 = 500(0x1f4, double:2.47E-321)
            r28.sendEmptyMessageDelayed(r29, r30)
            goto L_0x0007
        L_0x03f6:
            r28 = 1
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r28 = r0
            r29 = 18
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x041b
            java.lang.String r28 = "turning  manual option switcher"
            com.vise.log.ViseLog.m1466e(r28)
            r28 = 107212(0x1a2cc, float:1.50236E-40)
            com.freevisiontech.fvmobile.utility.Util.sendIntEventMessge(r28)
            r25 = 18
            com.freevisiontech.fvmobile.utils.BleByteUtil.actPTZSettingChange(r25)
            goto L_0x0007
        L_0x041b:
            r28 = 1
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r28 = r0
            r29 = 52
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x045f
            r28 = 2
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r28 = r0
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_THUMB_WHEEL_SELECTION = r28
            r25 = 52
            r28 = 2
            byte r5 = r27[r28]
            r0 = r25
            com.freevisiontech.fvmobile.utils.BleByteUtil.actPTZSettingStateChange(r0, r5)
            java.lang.StringBuilder r28 = new java.lang.StringBuilder
            r28.<init>()
            java.lang.String r29 = "on impeller mode  change mode: "
            java.lang.StringBuilder r28 = r28.append(r29)
            int r29 = com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_THUMB_WHEEL_SELECTION
            java.lang.StringBuilder r28 = r28.append(r29)
            java.lang.String r28 = r28.toString()
            com.vise.log.ViseLog.m1466e(r28)
            goto L_0x0007
        L_0x045f:
            r28 = 1
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r28 = r0
            r29 = 53
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x0912
            java.lang.StringBuilder r28 = new java.lang.StringBuilder
            r28.<init>()
            java.lang.String r29 = "on impeller  change :byte[]:"
            java.lang.StringBuilder r28 = r28.append(r29)
            java.lang.String r29 = java.util.Arrays.toString(r27)
            java.lang.StringBuilder r28 = r28.append(r29)
            java.lang.String r29 = " ,hex: "
            java.lang.StringBuilder r28 = r28.append(r29)
            java.lang.String r29 = com.freevisiontech.fvmobile.utils.HexUtil.encodeHexStr(r27)
            java.lang.StringBuilder r28 = r28.append(r29)
            java.lang.String r28 = r28.toString()
            com.vise.log.ViseLog.m1466e(r28)
            java.lang.StringBuilder r28 = new java.lang.StringBuilder
            r28.<init>()
            java.lang.String r29 = "on impeller  change value[2]: "
            java.lang.StringBuilder r28 = r28.append(r29)
            r29 = 2
            byte r29 = r27[r29]
            java.lang.StringBuilder r28 = r28.append(r29)
            java.lang.String r29 = "(value[2] & 0xff): "
            java.lang.StringBuilder r28 = r28.append(r29)
            r29 = 2
            byte r29 = r27[r29]
            r0 = r29
            r0 = r0 & 255(0xff, float:3.57E-43)
            r29 = r0
            java.lang.StringBuilder r28 = r28.append(r29)
            java.lang.String r28 = r28.toString()
            com.vise.log.ViseLog.m1466e(r28)
            java.lang.StringBuilder r28 = new java.lang.StringBuilder
            r28.<init>()
            java.lang.String r29 = "on impeller  change value[3]: "
            java.lang.StringBuilder r28 = r28.append(r29)
            r29 = 3
            byte r29 = r27[r29]
            java.lang.StringBuilder r28 = r28.append(r29)
            java.lang.String r29 = "(value[3] & 0xff): "
            java.lang.StringBuilder r28 = r28.append(r29)
            r29 = 3
            byte r29 = r27[r29]
            r0 = r29
            r0 = r0 & 255(0xff, float:3.57E-43)
            r29 = r0
            java.lang.StringBuilder r28 = r28.append(r29)
            java.lang.String r28 = r28.toString()
            com.vise.log.ViseLog.m1466e(r28)
            r28 = 2
            byte r28 = r27[r28]
            int r28 = r28 << 0
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r28 = r0
            r29 = 3
            byte r29 = r27[r29]
            int r29 = r29 << 8
            r30 = 65280(0xff00, float:9.1477E-41)
            r29 = r29 & r30
            int r6 = r28 + r29
            r28 = 32768(0x8000, float:4.5918E-41)
            r0 = r28
            if (r6 <= r0) goto L_0x051f
            r28 = 65536(0x10000, float:9.18355E-41)
            int r6 = r6 - r28
        L_0x051f:
            boolean r16 = com.freevisiontech.fvmobile.utility.CameraUtils.getCameraHandModel()
            int r28 = com.freevisiontech.fvmobile.utility.CameraUtils.getCurrentPageIndex()
            r29 = 2
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x0531
            r16 = 1
        L_0x0531:
            if (r16 == 0) goto L_0x0705
            com.freevisiontech.fvmobile.utils.MoveTimelapseUtil r28 = com.freevisiontech.fvmobile.utils.MoveTimelapseUtil.getInstance()
            int r28 = r28.getCameraProgressLinear()
            r29 = 1
            r0 = r28
            r1 = r29
            if (r0 == r1) goto L_0x0007
            int r28 = com.freevisiontech.fvmobile.utility.CameraUtils.getCurrentPageIndex()
            r29 = 2
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x06e8
            r8 = r6
            float r28 = com.freevisiontech.fvmobile.utility.CameraUtils.getHandModelVisibleStateValue()
            r29 = 1084227584(0x40a00000, float:5.0)
            int r28 = (r28 > r29 ? 1 : (r28 == r29 ? 0 : -1))
            if (r28 == 0) goto L_0x0564
            float r28 = com.freevisiontech.fvmobile.utility.CameraUtils.getHandModelVisibleStateValue()
            r29 = 1086324736(0x40c00000, float:6.0)
            int r28 = (r28 > r29 ? 1 : (r28 == r29 ? 0 : -1))
            if (r28 != 0) goto L_0x0564
        L_0x0564:
            if (r16 == 0) goto L_0x05d3
            int r28 = r6 / 4
            int r17 = java.lang.Math.abs(r28)
            r12 = 0
        L_0x056d:
            r0 = r17
            if (r12 >= r0) goto L_0x0595
            if (r6 <= 0) goto L_0x0585
            r0 = r34
            java.util.List<java.lang.Integer> r0 = r0.queueHandModel
            r28 = r0
            r29 = 4
            java.lang.Integer r29 = java.lang.Integer.valueOf(r29)
            r28.add(r29)
        L_0x0582:
            int r12 = r12 + 1
            goto L_0x056d
        L_0x0585:
            r0 = r34
            java.util.List<java.lang.Integer> r0 = r0.queueHandModel
            r28 = r0
            r29 = -4
            java.lang.Integer r29 = java.lang.Integer.valueOf(r29)
            r28.add(r29)
            goto L_0x0582
        L_0x0595:
            r0 = r34
            java.util.List<java.lang.Integer> r0 = r0.queueHandModel
            r28 = r0
            int r28 = r28.size()
            r29 = 3
            r0 = r28
            r1 = r29
            if (r0 > r1) goto L_0x0606
            r28 = 100
            r0 = r28
            r2 = r34
            r2.sleepMillis = r0
        L_0x05af:
            r28 = 0
            java.lang.Boolean r28 = java.lang.Boolean.valueOf(r28)
            r0 = r28
            r1 = r34
            r1.startSeekThreadNew210 = r0
            r28 = 0
            java.lang.Boolean r28 = java.lang.Boolean.valueOf(r28)
            r0 = r28
            r1 = r34
            r1.startSeekThreadMF210 = r0
            r28 = 1
            java.lang.Boolean r28 = java.lang.Boolean.valueOf(r28)
            r0 = r28
            r1 = r34
            r1.startSeekThreadHandModel210 = r0
        L_0x05d3:
            int r28 = com.freevisiontech.fvmobile.utility.CameraUtils.getCurrentPageIndex()
            r29 = 2
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x0007
            android.content.Context r28 = r34.getContext()
            java.lang.String r29 = "beauty_mode"
            r30 = 10400(0x28a0, float:1.4574E-41)
            java.lang.Integer r30 = java.lang.Integer.valueOf(r30)
            java.lang.Object r28 = com.freevisiontech.fvmobile.utility.SPUtils.get(r28, r29, r30)
            java.lang.Integer r28 = (java.lang.Integer) r28
            int r4 = r28.intValue()
            r28 = 10400(0x28a0, float:1.4574E-41)
            r0 = r28
            if (r4 != r0) goto L_0x06d0
            if (r6 <= 0) goto L_0x06c8
            r28 = 107701(0x1a4b5, float:1.50921E-40)
            com.freevisiontech.fvmobile.utility.Util.sendIntEventMessge(r28)
            goto L_0x0007
        L_0x0606:
            r0 = r34
            java.util.List<java.lang.Integer> r0 = r0.queueHandModel
            r28 = r0
            int r28 = r28.size()
            r29 = 3
            r0 = r28
            r1 = r29
            if (r0 <= r1) goto L_0x0634
            r0 = r34
            java.util.List<java.lang.Integer> r0 = r0.queueHandModel
            r28 = r0
            int r28 = r28.size()
            r29 = 5
            r0 = r28
            r1 = r29
            if (r0 >= r1) goto L_0x0634
            r28 = 80
            r0 = r28
            r2 = r34
            r2.sleepMillis = r0
            goto L_0x05af
        L_0x0634:
            r0 = r34
            java.util.List<java.lang.Integer> r0 = r0.queueHandModel
            r28 = r0
            int r28 = r28.size()
            r29 = 5
            r0 = r28
            r1 = r29
            if (r0 < r1) goto L_0x0662
            r0 = r34
            java.util.List<java.lang.Integer> r0 = r0.queueHandModel
            r28 = r0
            int r28 = r28.size()
            r29 = 7
            r0 = r28
            r1 = r29
            if (r0 >= r1) goto L_0x0662
            r28 = 60
            r0 = r28
            r2 = r34
            r2.sleepMillis = r0
            goto L_0x05af
        L_0x0662:
            r0 = r34
            java.util.List<java.lang.Integer> r0 = r0.queueHandModel
            r28 = r0
            int r28 = r28.size()
            r29 = 7
            r0 = r28
            r1 = r29
            if (r0 < r1) goto L_0x0690
            r0 = r34
            java.util.List<java.lang.Integer> r0 = r0.queueHandModel
            r28 = r0
            int r28 = r28.size()
            r29 = 9
            r0 = r28
            r1 = r29
            if (r0 >= r1) goto L_0x0690
            r28 = 40
            r0 = r28
            r2 = r34
            r2.sleepMillis = r0
            goto L_0x05af
        L_0x0690:
            r0 = r34
            java.util.List<java.lang.Integer> r0 = r0.queueHandModel
            r28 = r0
            int r28 = r28.size()
            r29 = 9
            r0 = r28
            r1 = r29
            if (r0 < r1) goto L_0x06be
            r0 = r34
            java.util.List<java.lang.Integer> r0 = r0.queueHandModel
            r28 = r0
            int r28 = r28.size()
            r29 = 25
            r0 = r28
            r1 = r29
            if (r0 >= r1) goto L_0x06be
            r28 = 30
            r0 = r28
            r2 = r34
            r2.sleepMillis = r0
            goto L_0x05af
        L_0x06be:
            r28 = 20
            r0 = r28
            r2 = r34
            r2.sleepMillis = r0
            goto L_0x05af
        L_0x06c8:
            r28 = 107702(0x1a4b6, float:1.50923E-40)
            com.freevisiontech.fvmobile.utility.Util.sendIntEventMessge(r28)
            goto L_0x0007
        L_0x06d0:
            int r28 = com.freevisiontech.fvmobile.utility.CameraUtils.getFrameLayerNumber()
            if (r28 == 0) goto L_0x0007
            if (r6 <= 0) goto L_0x06e0
            r28 = 107701(0x1a4b5, float:1.50921E-40)
            com.freevisiontech.fvmobile.utility.Util.sendIntEventMessge(r28)
            goto L_0x0007
        L_0x06e0:
            r28 = 107702(0x1a4b6, float:1.50923E-40)
            com.freevisiontech.fvmobile.utility.Util.sendIntEventMessge(r28)
            goto L_0x0007
        L_0x06e8:
            r28 = 4
            r0 = r28
            if (r6 < r0) goto L_0x06fc
            int r6 = r6 + -2
        L_0x06f0:
            r28 = 107214(0x1a2ce, float:1.50239E-40)
            java.lang.String r29 = java.lang.String.valueOf(r6)
            com.freevisiontech.fvmobile.utility.Util.sendIntEventMessge((int) r28, (java.lang.String) r29)
            goto L_0x0007
        L_0x06fc:
            r28 = -4
            r0 = r28
            if (r6 > r0) goto L_0x06f0
            int r6 = r6 + 2
            goto L_0x06f0
        L_0x0705:
            int r28 = com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_THUMB_WHEEL_SELECTION
            r29 = 1
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x0858
            r21 = r6
            boolean r28 = r34.getPtzExposureExclusion()
            if (r28 != 0) goto L_0x0007
            boolean r28 = com.freevisiontech.fvmobile.utility.CameraUtils.getBosIsResume()
            r0 = r28
            r1 = r34
            r1.isResume = r0
            r0 = r34
            boolean r0 = r0.isResume
            r28 = r0
            if (r28 == 0) goto L_0x0007
            r28 = 0
            r0 = r28
            r1 = r34
            r1.scaleForExposurePosition = r0
            r28 = 100
            r0 = r21
            r1 = r28
            if (r0 == r1) goto L_0x0821
            r0 = r34
            java.util.List<java.lang.Integer> r0 = r0.queueExposure
            r28 = r0
            int r28 = r28.size()
            r29 = 1
            r0 = r28
            r1 = r29
            if (r0 <= r1) goto L_0x07f9
            r0 = r34
            java.util.List<java.lang.Integer> r0 = r0.queueExposure
            r28 = r0
            r29 = 0
            java.lang.Object r28 = r28.get(r29)
            java.lang.Integer r28 = (java.lang.Integer) r28
            int r29 = r28.intValue()
            r0 = r34
            java.util.List<java.lang.Integer> r0 = r0.queueExposure
            r28 = r0
            r30 = 1
            r0 = r28
            r1 = r30
            java.lang.Object r28 = r0.get(r1)
            java.lang.Integer r28 = (java.lang.Integer) r28
            int r28 = r28.intValue()
            int r28 = r28 * r29
            if (r28 >= 0) goto L_0x07d1
            r28 = 0
            r0 = r28
            r1 = r34
            r1.yK1Exposure = r0
            r0 = r34
            java.util.List<java.lang.Integer> r0 = r0.queueExposure
            r28 = r0
            r28.clear()
            r0 = r21
            r1 = r34
            r1.queueForlevel = r0
            r0 = r34
            java.util.List<java.lang.Integer> r0 = r0.queueExposure
            r28 = r0
            r29 = 0
            r0 = r34
            int r0 = r0.queueForlevel
            r30 = r0
            java.lang.Integer r30 = java.lang.Integer.valueOf(r30)
            r28.add(r29, r30)
            r28 = 1
            java.lang.Boolean r28 = java.lang.Boolean.valueOf(r28)
            r0 = r28
            r1 = r34
            r1.startSeekThreadExposure300 = r0
        L_0x07af:
            r0 = r34
            android.os.Handler r0 = r0.mHandler
            r28 = r0
            if (r28 == 0) goto L_0x0849
        L_0x07b7:
            r0 = r34
            android.os.Handler r0 = r0.mHandler
            r28 = r0
            r29 = 10000(0x2710, float:1.4013E-41)
            boolean r28 = r28.hasMessages(r29)
            if (r28 == 0) goto L_0x0849
            r0 = r34
            android.os.Handler r0 = r0.mHandler
            r28 = r0
            r29 = 10000(0x2710, float:1.4013E-41)
            r28.removeMessages(r29)
            goto L_0x07b7
        L_0x07d1:
            r0 = r21
            r1 = r34
            r1.queueForlevel = r0
            r0 = r34
            java.util.List<java.lang.Integer> r0 = r0.queueExposure
            r28 = r0
            r29 = 0
            r0 = r34
            int r0 = r0.queueForlevel
            r30 = r0
            java.lang.Integer r30 = java.lang.Integer.valueOf(r30)
            r28.add(r29, r30)
            r28 = 1
            java.lang.Boolean r28 = java.lang.Boolean.valueOf(r28)
            r0 = r28
            r1 = r34
            r1.startSeekThreadExposure300 = r0
            goto L_0x07af
        L_0x07f9:
            r0 = r21
            r1 = r34
            r1.queueForlevel = r0
            r0 = r34
            java.util.List<java.lang.Integer> r0 = r0.queueExposure
            r28 = r0
            r29 = 0
            r0 = r34
            int r0 = r0.queueForlevel
            r30 = r0
            java.lang.Integer r30 = java.lang.Integer.valueOf(r30)
            r28.add(r29, r30)
            r28 = 1
            java.lang.Boolean r28 = java.lang.Boolean.valueOf(r28)
            r0 = r28
            r1 = r34
            r1.startSeekThreadExposure300 = r0
            goto L_0x07af
        L_0x0821:
            r28 = 0
            r0 = r28
            r1 = r34
            r1.yK1Exposure = r0
            r28 = 0
            java.lang.Boolean r28 = java.lang.Boolean.valueOf(r28)
            r0 = r28
            r1 = r34
            r1.startSeekThreadExposure300 = r0
            r0 = r34
            java.util.List<java.lang.Integer> r0 = r0.queueExposure
            r28 = r0
            r28.clear()
            r0 = r34
            java.util.List<java.lang.Long> r0 = r0.queueTimeExposure
            r28 = r0
            r28.clear()
            goto L_0x07af
        L_0x0849:
            r0 = r34
            android.os.Handler r0 = r0.mHandler
            r28 = r0
            r29 = 10000(0x2710, float:1.4013E-41)
            r30 = 8000(0x1f40, double:3.9525E-320)
            r28.sendEmptyMessageDelayed(r29, r30)
            goto L_0x0007
        L_0x0858:
            int r28 = com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_THUMB_WHEEL_SELECTION
            if (r28 != 0) goto L_0x0007
            r22 = r6
            int r28 = r6 * 10
            r0 = r28
            r1 = r34
            r1.wtProScaleForlevel300 = r0
            boolean r28 = r34.getPtzStretchLensExclusion()
            if (r28 != 0) goto L_0x0007
            boolean r28 = com.freevisiontech.fvmobile.utility.CameraUtils.getBosIsResume()
            r0 = r28
            r1 = r34
            r1.isResume = r0
            r0 = r34
            boolean r0 = r0.isResume
            r28 = r0
            if (r28 == 0) goto L_0x0007
            r0 = r34
            double r0 = r0.wtProRealTimeValueStart300
            r28 = r0
            r0 = r34
            int r0 = r0.wtProScaleForlevel300
            r30 = r0
            r0 = r30
            double r0 = (double) r0
            r30 = r0
            java.lang.Double r30 = java.lang.Double.valueOf(r30)
            double r30 = r30.doubleValue()
            double r28 = r28 + r30
            java.lang.Double r28 = java.lang.Double.valueOf(r28)
            double r28 = r28.doubleValue()
            r0 = r28
            r2 = r34
            r2.wtProRealTimeValueStart300 = r0
            r0 = r34
            double r0 = r0.wtProRealTimeValueStart300
            r28 = r0
            r30 = 4652007308841189376(0x408f400000000000, double:1000.0)
            int r28 = (r28 > r30 ? 1 : (r28 == r30 ? 0 : -1))
            if (r28 < 0) goto L_0x08fd
            r28 = 4652007308841189376(0x408f400000000000, double:1000.0)
            r0 = r28
            r2 = r34
            r2.wtProRealTimeValueStart300 = r0
        L_0x08c1:
            r28 = 1
            java.lang.Boolean r28 = java.lang.Boolean.valueOf(r28)
            r0 = r28
            r1 = r34
            r1.startSeekThreadNew300 = r0
            r0 = r34
            android.os.Handler r0 = r0.mHandler
            r28 = r0
            if (r28 == 0) goto L_0x08ee
            r0 = r34
            android.os.Handler r0 = r0.mHandler
            r28 = r0
            r29 = 10001(0x2711, float:1.4014E-41)
            boolean r28 = r28.hasMessages(r29)
            if (r28 == 0) goto L_0x08ee
            r0 = r34
            android.os.Handler r0 = r0.mHandler
            r28 = r0
            r29 = 10001(0x2711, float:1.4014E-41)
            r28.removeMessages(r29)
        L_0x08ee:
            r0 = r34
            android.os.Handler r0 = r0.mHandler
            r28 = r0
            r29 = 10001(0x2711, float:1.4014E-41)
            r30 = 8000(0x1f40, double:3.9525E-320)
            r28.sendEmptyMessageDelayed(r29, r30)
            goto L_0x0007
        L_0x08fd:
            r0 = r34
            double r0 = r0.wtProRealTimeValueStart300
            r28 = r0
            r30 = 0
            int r28 = (r28 > r30 ? 1 : (r28 == r30 ? 0 : -1))
            if (r28 >= 0) goto L_0x08c1
            r28 = 0
            r0 = r28
            r2 = r34
            r2.wtProRealTimeValueStart300 = r0
            goto L_0x08c1
        L_0x0912:
            r28 = 1
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r28 = r0
            r29 = 56
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x0a4d
            r28 = 2
            byte r28 = r27[r28]
            int r28 = r28 << 0
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r28 = r0
            r29 = 3
            byte r29 = r27[r29]
            int r29 = r29 << 8
            r30 = 65280(0xff00, float:9.1477E-41)
            r29 = r29 & r30
            int r7 = r28 + r29
            r20 = 0
            r0 = r34
            int r0 = r0.lastDeltaNum
            r28 = r0
            if (r28 == 0) goto L_0x094f
            r0 = r34
            int r0 = r0.lastDeltaNum
            r28 = r0
            int r20 = r7 - r28
        L_0x094f:
            r0 = r34
            r0.lastDeltaNum = r7
            int r28 = java.lang.Math.abs(r20)
            r29 = 30000(0x7530, float:4.2039E-41)
            r0 = r28
            r1 = r29
            if (r0 >= r1) goto L_0x09d3
            int r20 = r20 / 100
            r28 = 3
            r0 = r20
            r1 = r28
            if (r0 <= r1) goto L_0x09c8
            r20 = 3
        L_0x096b:
            r0 = r20
            r1 = r34
            r1.deltaFM210 = r0
            java.lang.StringBuilder r28 = new java.lang.StringBuilder
            r28.<init>()
            java.lang.String r29 = "ViltaX--FM210，在此处进行WT调节--0x38--data--"
            java.lang.StringBuilder r28 = r28.append(r29)
            java.lang.String r29 = com.freevisiontech.fvmobile.utils.HexUtil.encodeHexStr(r27)
            java.lang.StringBuilder r28 = r28.append(r29)
            java.lang.String r29 = "      delta:"
            java.lang.StringBuilder r28 = r28.append(r29)
            r0 = r34
            int r0 = r0.deltaFM210
            r29 = r0
            java.lang.StringBuilder r28 = r28.append(r29)
            java.lang.String r28 = r28.toString()
            com.vise.log.ViseLog.m1466e(r28)
            r0 = r34
            com.freevisiontech.cameralib.FVCameraManager r0 = r0.cameraManager
            r28 = r0
            int r28 = r28.getCameraManagerType()
            r29 = 1
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x09d6
            r28 = 0
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_THUMB_WHEEL_SELECTION = r28
        L_0x09b3:
            boolean r16 = com.freevisiontech.fvmobile.utility.CameraUtils.getCameraHandModel()
            if (r16 == 0) goto L_0x0a1f
            float r28 = com.freevisiontech.fvmobile.utility.CameraUtils.getHandModelVisibleStateValue()
            r29 = 1084227584(0x40a00000, float:5.0)
            int r28 = (r28 > r29 ? 1 : (r28 == r29 ? 0 : -1))
            if (r28 != 0) goto L_0x09f8
            r34.setFM210ImpellerModeMf()
            goto L_0x0007
        L_0x09c8:
            r28 = -3
            r0 = r20
            r1 = r28
            if (r0 >= r1) goto L_0x096b
            r20 = -3
            goto L_0x096b
        L_0x09d3:
            r20 = 0
            goto L_0x096b
        L_0x09d6:
            android.support.v4.app.FragmentActivity r28 = r34.getActivity()
            java.lang.String r29 = "camera_lens_mode"
            r30 = 10102(0x2776, float:1.4156E-41)
            java.lang.Integer r30 = java.lang.Integer.valueOf(r30)
            java.lang.Object r28 = com.freevisiontech.fvmobile.utility.SPUtils.get(r28, r29, r30)
            java.lang.Integer r28 = (java.lang.Integer) r28
            int r15 = r28.intValue()
            r28 = 10101(0x2775, float:1.4155E-41)
            r0 = r28
            if (r15 != r0) goto L_0x09b3
            r28 = 0
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_THUMB_WHEEL_SELECTION = r28
            goto L_0x09b3
        L_0x09f8:
            float r28 = com.freevisiontech.fvmobile.utility.CameraUtils.getHandModelVisibleStateValue()
            r29 = 1086324736(0x40c00000, float:6.0)
            int r28 = (r28 > r29 ? 1 : (r28 == r29 ? 0 : -1))
            if (r28 != 0) goto L_0x0a07
            r34.setFM210ImpellerModeZoom()
            goto L_0x0007
        L_0x0a07:
            int r28 = com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_THUMB_WHEEL_SELECTION
            if (r28 != 0) goto L_0x0a10
            r34.setFM210ImpellerModeZoom()
            goto L_0x0007
        L_0x0a10:
            int r28 = com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_THUMB_WHEEL_SELECTION
            r29 = 21
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x0007
            r34.setFM210ImpellerModeMf()
            goto L_0x0007
        L_0x0a1f:
            boolean r28 = com.freevisiontech.fvmobile.utility.CameraUtils.isHitchCockRecordUI()
            if (r28 != 0) goto L_0x0007
            com.freevisiontech.fvmobile.utils.MoveTimelapseUtil r28 = com.freevisiontech.fvmobile.utils.MoveTimelapseUtil.getInstance()
            int r28 = r28.getIsHitchCockRecord()
            r29 = 1
            r0 = r28
            r1 = r29
            if (r0 == r1) goto L_0x0007
            int r28 = com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_THUMB_WHEEL_SELECTION
            if (r28 != 0) goto L_0x0a3e
            r34.setFM210ImpellerModeZoom()
            goto L_0x0007
        L_0x0a3e:
            int r28 = com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_THUMB_WHEEL_SELECTION
            r29 = 21
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x0007
            r34.setFM210ImpellerModeMf()
            goto L_0x0007
        L_0x0a4d:
            r28 = 1
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r28 = r0
            r29 = 54
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x0a94
            r28 = 2
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r19 = r0
            r0 = r34
            boolean r0 = r0.connected
            r28 = r0
            if (r28 == 0) goto L_0x0a76
            r28 = 54
            com.freevisiontech.fvmobile.utils.BleByteUtil.actPTZSettingChange(r28)
        L_0x0a76:
            r28 = 1
            r0 = r19
            r1 = r28
            if (r0 != r1) goto L_0x0007
            boolean r16 = com.freevisiontech.fvmobile.utility.CameraUtils.getCameraHandModel()
            if (r16 == 0) goto L_0x0a8c
            r28 = 107213(0x1a2cd, float:1.50237E-40)
            com.freevisiontech.fvmobile.utility.Util.sendIntEventMessge(r28)
            goto L_0x0007
        L_0x0a8c:
            r28 = 107211(0x1a2cb, float:1.50235E-40)
            com.freevisiontech.fvmobile.utility.Util.sendIntEventMessge(r28)
            goto L_0x0007
        L_0x0a94:
            r28 = 1
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r28 = r0
            r29 = 36
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x0b19
            r0 = r34
            boolean r0 = r0.connected
            r28 = r0
            if (r28 == 0) goto L_0x0ab3
            r28 = 36
            com.freevisiontech.fvmobile.utils.BleByteUtil.actPTZSettingChange(r28)
        L_0x0ab3:
            r28 = 2
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r28 = r0
            r29 = 2
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x0007
            java.lang.String r28 = "BottomFragment"
            java.lang.String r29 = "receiveEvent: 云台全景拍摄位置异常"
            android.util.Log.e(r28, r29)
            r0 = r34
            boolean r0 = r0.connected
            r28 = r0
            if (r28 == 0) goto L_0x0add
            r28 = 36
            r29 = 2
            com.freevisiontech.fvmobile.utils.BleByteUtil.actPTZSettingStateChange(r28, r29)
        L_0x0add:
            r0 = r34
            android.os.Handler r0 = r0.mHandler
            r28 = r0
            if (r28 == 0) goto L_0x0af0
            r0 = r34
            android.os.Handler r0 = r0.mHandler
            r28 = r0
            r29 = 0
            r28.removeCallbacksAndMessages(r29)
        L_0x0af0:
            r28 = 0
            r0 = r28
            r1 = r34
            r1.isNeedEnterNextStep = r0
            java.lang.String r28 = "14"
            com.freevisiontech.fvmobile.utility.CameraUtils.setFullCameraErrorCode(r28)
            r28 = 0
            r0 = r28
            r1 = r34
            r1.takedPictrueCount = r0
            r0 = r34
            java.util.ArrayList<java.lang.String> r0 = r0.list
            r28 = r0
            r28.clear()
            r28 = 10042(0x273a, float:1.4072E-41)
            java.lang.String r29 = "1"
            com.freevisiontech.fvmobile.utility.Util.sendIntEventMessge((int) r28, (java.lang.String) r29)
            goto L_0x0007
        L_0x0b19:
            r28 = 1
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r28 = r0
            r29 = 19
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x0bba
            java.lang.String r28 = "自拍模式开关"
            com.vise.log.ViseLog.m1466e(r28)
            r0 = r34
            boolean r0 = r0.connected
            r28 = r0
            if (r28 == 0) goto L_0x0b3e
            r28 = 19
            com.freevisiontech.fvmobile.utils.BleByteUtil.actPTZSettingChange(r28)
        L_0x0b3e:
            boolean r28 = r34.getPtzChangeLensExclusion()
            if (r28 != 0) goto L_0x0007
            boolean r28 = com.freevisiontech.fvmobile.utility.CameraUtils.getBosIsResume()
            r0 = r28
            r1 = r34
            r1.isResume = r0
            r0 = r34
            boolean r0 = r0.isResume
            r28 = r0
            if (r28 == 0) goto L_0x0007
            int r28 = com.freevisiontech.fvmobile.utility.CameraUtils.getCurrentPageIndex()
            r29 = 2
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x0bb5
            r0 = r34
            android.content.Context r0 = r0.mContext
            r28 = r0
            java.lang.String r29 = "pop_show_outside_click_interceptor"
            r30 = 0
            java.lang.Boolean r30 = java.lang.Boolean.valueOf(r30)
            java.lang.Object r28 = com.freevisiontech.fvmobile.utility.SPUtils.get(r28, r29, r30)
            java.lang.Boolean r28 = (java.lang.Boolean) r28
            boolean r13 = r28.booleanValue()
            if (r13 != 0) goto L_0x0007
            boolean r28 = com.freevisiontech.fvmobile.utility.CameraUtils.isRecordingIng()
            if (r28 == 0) goto L_0x0b89
            boolean r28 = com.freevisiontech.fvmobile.utility.CameraUtils.getBooRecordStarted()
            if (r28 != 0) goto L_0x0007
        L_0x0b89:
            boolean r28 = com.freevisiontech.fvmobile.utility.CameraUtils.isFollowIng()
            if (r28 != 0) goto L_0x0007
            boolean r28 = com.freevisiontech.fvmobile.utility.CameraUtils.getIsBooleanTimeLapseUIShow()
            if (r28 != 0) goto L_0x0007
            int r28 = com.freevisiontech.fvmobile.utility.CameraUtils.getFrameLayerNumber()
            if (r28 == 0) goto L_0x0bb0
            int r26 = com.freevisiontech.fvmobile.utility.CameraUtils.getLabelTopBarSelectMemory()
            r28 = 107705(0x1a4b9, float:1.50927E-40)
            com.freevisiontech.fvmobile.utility.Util.sendIntEventMessge(r28)
            com.freevisiontech.fvmobile.utility.CameraUtils.setLabelTopBarSelectMemory(r26)
            r28 = 107744(0x1a4e0, float:1.50982E-40)
            com.freevisiontech.fvmobile.utility.Util.sendIntEventMessge(r28)
            goto L_0x0007
        L_0x0bb0:
            r34.switchCameraLens()
            goto L_0x0007
        L_0x0bb5:
            r34.switchCameraLens()
            goto L_0x0007
        L_0x0bba:
            r28 = 1
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r28 = r0
            r29 = 40
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x0c5c
            java.lang.String r28 = "0x28"
            com.vise.log.ViseLog.m1466e(r28)
            java.lang.String r28 = "0x28"
            r29 = 1
            r0 = r29
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r29 = r0
            r30 = 0
            java.lang.StringBuilder r31 = new java.lang.StringBuilder
            r31.<init>()
            java.lang.String r32 = " data: byte[]:"
            java.lang.StringBuilder r31 = r31.append(r32)
            java.lang.String r32 = java.util.Arrays.toString(r27)
            java.lang.StringBuilder r31 = r31.append(r32)
            java.lang.String r32 = " ,hex: "
            java.lang.StringBuilder r31 = r31.append(r32)
            java.lang.String r32 = com.freevisiontech.fvmobile.utils.HexUtil.encodeHexStr(r27)
            java.lang.StringBuilder r31 = r31.append(r32)
            java.lang.String r31 = r31.toString()
            r29[r30] = r31
            com.vise.log.ViseLog.m1467e(r28, r29)
            java.lang.String r28 = "0x28"
            r29 = 1
            r0 = r29
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r29 = r0
            r30 = 0
            java.lang.StringBuilder r31 = new java.lang.StringBuilder
            r31.<init>()
            java.lang.String r32 = " value[2]& 0xff: byte[]:"
            java.lang.StringBuilder r31 = r31.append(r32)
            r32 = 2
            byte r32 = r27[r32]
            r0 = r32
            r0 = r0 & 255(0xff, float:3.57E-43)
            r32 = r0
            java.lang.StringBuilder r31 = r31.append(r32)
            java.lang.String r31 = r31.toString()
            r29[r30] = r31
            com.vise.log.ViseLog.m1467e(r28, r29)
            r28 = 2
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r28 = r0
            r29 = 3
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x0007
            r0 = r34
            android.os.Handler r0 = r0.mHandler
            r28 = r0
            r29 = 20
            r30 = 500(0x1f4, double:2.47E-321)
            r28.sendEmptyMessageDelayed(r29, r30)
            goto L_0x0007
        L_0x0c5c:
            r28 = 1
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r28 = r0
            r29 = 45
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x0f1d
            r0 = r34
            boolean r0 = r0.connected
            r28 = r0
            if (r28 == 0) goto L_0x0c81
            r28 = 1
            byte r28 = r27[r28]
            r29 = 2
            byte r29 = r27[r29]
            com.freevisiontech.fvmobile.utils.BleByteUtil.ackPTZPanorama(r28, r29)
        L_0x0c81:
            java.lang.String r28 = "KBein"
            java.lang.StringBuilder r29 = new java.lang.StringBuilder
            r29.<init>()
            java.lang.String r30 = "FVBottomBarFragment.receiveEvent():-- 0x2d--"
            java.lang.StringBuilder r29 = r29.append(r30)
            java.lang.String r30 = com.freevisiontech.fvmobile.utils.HexUtil.encodeHexStr(r27)
            java.lang.StringBuilder r29 = r29.append(r30)
            java.lang.String r29 = r29.toString()
            android.util.Log.i(r28, r29)
            boolean r28 = com.freevisiontech.fvmobile.utility.CameraUtils.getBosIsResume()
            r0 = r28
            r1 = r34
            r1.isResume = r0
            r0 = r34
            boolean r0 = r0.isResume
            r28 = r0
            if (r28 == 0) goto L_0x0eeb
            java.lang.String r28 = "相机功能设置====    开始  ===="
            com.vise.log.ViseLog.m1466e(r28)
            r28 = 2
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r28 = r0
            if (r28 != 0) goto L_0x0cd6
            java.lang.String r28 = "相机功能设置===停止"
            com.vise.log.ViseLog.m1466e(r28)
            r0 = r34
            boolean r0 = r0.isResume
            r28 = r0
            if (r28 == 0) goto L_0x0007
            r34.stopRecordOnly()
            goto L_0x0007
        L_0x0cd6:
            r28 = 2
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r28 = r0
            r29 = 1
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x0dfa
            java.lang.String r28 = "相机功能设置===拍照"
            com.vise.log.ViseLog.m1466e(r28)
            r0 = r34
            android.content.Context r0 = r0.mContext
            r28 = r0
            java.lang.String r29 = "pop_show_outside_click_interceptor"
            r30 = 0
            java.lang.Boolean r30 = java.lang.Boolean.valueOf(r30)
            java.lang.Object r28 = com.freevisiontech.fvmobile.utility.SPUtils.get(r28, r29, r30)
            java.lang.Boolean r28 = (java.lang.Boolean) r28
            boolean r13 = r28.booleanValue()
            if (r13 == 0) goto L_0x0d54
            r28 = 1
            com.freevisiontech.fvmobile.utility.CameraUtils.setMoveTimelapseRecording(r28)
        L_0x0d0e:
            boolean r28 = r34.getPtzTakePhotoExclusion()
            if (r28 != 0) goto L_0x0007
            r0 = r34
            boolean r0 = r0.isResume
            r28 = r0
            if (r28 == 0) goto L_0x0007
            com.freevisiontech.fvmobile.utils.MoveTimelapseUtil.getInstance()
            int r28 = com.freevisiontech.fvmobile.utils.MoveTimelapseUtil.getCameraFvShareSleep()
            r29 = 1
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x0daa
            android.support.v4.app.FragmentActivity r28 = r34.getActivity()
            java.lang.String r29 = "full_shot"
            r30 = 10024(0x2728, float:1.4047E-41)
            java.lang.Integer r30 = java.lang.Integer.valueOf(r30)
            java.lang.Object r28 = com.freevisiontech.fvmobile.utility.SPUtils.get(r28, r29, r30)
            java.lang.Integer r28 = (java.lang.Integer) r28
            int r9 = r28.intValue()
            r28 = 10024(0x2728, float:1.4047E-41)
            r0 = r28
            if (r9 == r0) goto L_0x0d5a
            com.freevisiontech.fvmobile.utils.Event r28 = new com.freevisiontech.fvmobile.utils.Event
            r29 = 153(0x99, float:2.14E-43)
            r28.<init>(r29)
            com.freevisiontech.fvmobile.utils.EventBusUtil.sendEvent(r28)
            goto L_0x0007
        L_0x0d54:
            r28 = 0
            com.freevisiontech.fvmobile.utility.CameraUtils.setMoveTimelapseRecording(r28)
            goto L_0x0d0e
        L_0x0d5a:
            r0 = r34
            java.lang.Boolean r0 = r0.takePhotoCanOk
            r28 = r0
            boolean r28 = r28.booleanValue()
            if (r28 == 0) goto L_0x0007
            int r28 = com.freevisiontech.fvmobile.utility.CameraUtils.getCurrentPageIndex()
            r29 = 2
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x0d9f
            boolean r28 = com.freevisiontech.fvmobile.utility.CameraUtils.isRecordingIng()
            if (r28 == 0) goto L_0x0d7e
            boolean r28 = com.freevisiontech.fvmobile.utility.CameraUtils.getBooRecordStarted()
            if (r28 != 0) goto L_0x0007
        L_0x0d7e:
            boolean r28 = com.freevisiontech.fvmobile.utility.CameraUtils.getIsBooleanTimeLapseUIShow()
            if (r28 != 0) goto L_0x0007
            com.freevisiontech.fvmobile.utils.MoveTimelapseUtil r28 = com.freevisiontech.fvmobile.utils.MoveTimelapseUtil.getInstance()
            int r28 = r28.getCameraProgressLinear()
            r29 = 1
            r0 = r28
            r1 = r29
            if (r0 == r1) goto L_0x0007
            r34.sendPtzPhotoOrVideoDismissPop()
            r34.changeCamera2PhotoMode()
            r34.takePhotoOrVideo()
            goto L_0x0007
        L_0x0d9f:
            r34.sendPtzPhotoOrVideoDismissPop()
            r34.changeCamera2PhotoMode()
            r34.takePhotoOrVideo()
            goto L_0x0007
        L_0x0daa:
            r0 = r34
            java.lang.Boolean r0 = r0.takePhotoCanOk
            r28 = r0
            boolean r28 = r28.booleanValue()
            if (r28 == 0) goto L_0x0007
            int r28 = com.freevisiontech.fvmobile.utility.CameraUtils.getCurrentPageIndex()
            r29 = 2
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x0def
            boolean r28 = com.freevisiontech.fvmobile.utility.CameraUtils.isRecordingIng()
            if (r28 == 0) goto L_0x0dce
            boolean r28 = com.freevisiontech.fvmobile.utility.CameraUtils.getBooRecordStarted()
            if (r28 != 0) goto L_0x0007
        L_0x0dce:
            boolean r28 = com.freevisiontech.fvmobile.utility.CameraUtils.getIsBooleanTimeLapseUIShow()
            if (r28 != 0) goto L_0x0007
            com.freevisiontech.fvmobile.utils.MoveTimelapseUtil r28 = com.freevisiontech.fvmobile.utils.MoveTimelapseUtil.getInstance()
            int r28 = r28.getCameraProgressLinear()
            r29 = 1
            r0 = r28
            r1 = r29
            if (r0 == r1) goto L_0x0007
            r34.sendPtzPhotoOrVideoDismissPop()
            r34.changeCamera2PhotoMode()
            r34.takePhotoOrVideo()
            goto L_0x0007
        L_0x0def:
            r34.sendPtzPhotoOrVideoDismissPop()
            r34.changeCamera2PhotoMode()
            r34.takePhotoOrVideo()
            goto L_0x0007
        L_0x0dfa:
            r28 = 2
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r28 = r0
            r29 = 2
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x0007
            java.lang.String r28 = "相机功能设置===录像"
            com.vise.log.ViseLog.m1466e(r28)
            java.lang.String r28 = "KBein"
            java.lang.String r29 = "FVBottomBarFragment.receiveEvent():--鐩告満鍔熻兘璁剧疆===褰曞儚- 0x02 -"
            android.util.Log.i(r28, r29)
            r0 = r34
            android.content.Context r0 = r0.mContext
            r28 = r0
            java.lang.String r29 = "pop_show_outside_click_interceptor"
            r30 = 0
            java.lang.Boolean r30 = java.lang.Boolean.valueOf(r30)
            java.lang.Object r28 = com.freevisiontech.fvmobile.utility.SPUtils.get(r28, r29, r30)
            java.lang.Boolean r28 = (java.lang.Boolean) r28
            boolean r13 = r28.booleanValue()
            if (r13 == 0) goto L_0x0eb5
            r28 = 1
            com.freevisiontech.fvmobile.utility.CameraUtils.setMoveTimelapseRecording(r28)
        L_0x0e3b:
            boolean r28 = r34.getPtzRecordExclusion()
            if (r28 != 0) goto L_0x0007
            r0 = r34
            boolean r0 = r0.isResume
            r28 = r0
            if (r28 == 0) goto L_0x0007
            boolean r28 = com.freevisiontech.fvmobile.utility.CameraUtils.isFollowIng()
            if (r28 == 0) goto L_0x0e98
            java.lang.String r11 = ""
            android.support.v4.app.FragmentActivity r28 = r34.getActivity()
            java.lang.String r29 = "camera_lens_mode"
            r30 = 10102(0x2776, float:1.4156E-41)
            java.lang.Integer r30 = java.lang.Integer.valueOf(r30)
            java.lang.Object r28 = com.freevisiontech.fvmobile.utility.SPUtils.get(r28, r29, r30)
            java.lang.Integer r28 = (java.lang.Integer) r28
            int r10 = r28.intValue()
            r28 = 10101(0x2775, float:1.4155E-41)
            r0 = r28
            if (r10 != r0) goto L_0x0ebb
            android.support.v4.app.FragmentActivity r28 = r34.getActivity()
            java.lang.String r29 = "front_high_speed_video_resolution"
            java.lang.String r30 = ""
            java.lang.Object r28 = com.freevisiontech.fvmobile.utility.SPUtils.get(r28, r29, r30)
            java.lang.String r11 = r28.toString()
        L_0x0e81:
            java.lang.String r28 = ""
            r0 = r28
            boolean r28 = r0.equals(r11)
            if (r28 != 0) goto L_0x0e98
            r0 = r34
            com.freevisiontech.cameralib.FVCameraManager r0 = r0.cameraManager
            r28 = r0
            boolean r28 = r28.isMediaRecording()
            if (r28 == 0) goto L_0x0ece
        L_0x0e98:
            int r28 = com.freevisiontech.fvmobile.utility.CameraUtils.getCurrentPageIndex()
            r29 = 2
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x0ee0
            boolean r28 = com.freevisiontech.fvmobile.utility.CameraUtils.getIsBooleanTimeLapseUIShow()
            if (r28 != 0) goto L_0x0007
            r34.sendPtzPhotoOrVideoDismissPop()
            r34.changeCamera2VideoMode()
            r34.record()
            goto L_0x0007
        L_0x0eb5:
            r28 = 0
            com.freevisiontech.fvmobile.utility.CameraUtils.setMoveTimelapseRecording(r28)
            goto L_0x0e3b
        L_0x0ebb:
            android.support.v4.app.FragmentActivity r28 = r34.getActivity()
            java.lang.String r29 = "high_speed_video_resolution"
            java.lang.String r30 = ""
            java.lang.Object r28 = com.freevisiontech.fvmobile.utility.SPUtils.get(r28, r29, r30)
            java.lang.String r11 = r28.toString()
            goto L_0x0e81
        L_0x0ece:
            android.support.v4.app.FragmentActivity r28 = r34.getActivity()
            r29 = 2131296310(0x7f090036, float:1.8210533E38)
            r30 = 0
            android.widget.Toast r28 = android.widget.Toast.makeText(r28, r29, r30)
            r28.show()
            goto L_0x0007
        L_0x0ee0:
            r34.sendPtzPhotoOrVideoDismissPop()
            r34.changeCamera2VideoMode()
            r34.record()
            goto L_0x0007
        L_0x0eeb:
            r28 = 2
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r28 = r0
            if (r28 == 0) goto L_0x0007
            r28 = 2
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r28 = r0
            r29 = 1
            r0 = r28
            r1 = r29
            if (r0 == r1) goto L_0x0007
            r28 = 2
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r28 = r0
            r29 = 2
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x0007
            goto L_0x0007
        L_0x0f1d:
            r28 = 1
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r28 = r0
            r29 = 46
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x10ba
            r28 = 2
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r24 = r0
            r28 = 3
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r22 = r0
            boolean r28 = r34.getPtzStretchLensExclusion()
            if (r28 != 0) goto L_0x0007
            boolean r28 = com.freevisiontech.fvmobile.utility.CameraUtils.getBosIsResume()
            r0 = r28
            r1 = r34
            r1.isResume = r0
            r0 = r34
            boolean r0 = r0.isResume
            r28 = r0
            if (r28 == 0) goto L_0x0007
            r28 = 0
            r0 = r28
            r1 = r34
            r1.scaleForlevelPosition = r0
            r28 = 100
            r0 = r22
            r1 = r28
            if (r0 == r1) goto L_0x107d
            r0 = r34
            java.util.List<java.lang.Integer> r0 = r0.queue
            r28 = r0
            int r28 = r28.size()
            r29 = 1
            r0 = r28
            r1 = r29
            if (r0 <= r1) goto L_0x1054
            r0 = r34
            java.util.List<java.lang.Integer> r0 = r0.queue
            r28 = r0
            r29 = 0
            java.lang.Object r28 = r28.get(r29)
            java.lang.Integer r28 = (java.lang.Integer) r28
            int r28 = r28.intValue()
            int r29 = r28 + -100
            r0 = r34
            java.util.List<java.lang.Integer> r0 = r0.queue
            r28 = r0
            r30 = 1
            r0 = r28
            r1 = r30
            java.lang.Object r28 = r0.get(r1)
            java.lang.Integer r28 = (java.lang.Integer) r28
            int r28 = r28.intValue()
            int r28 = r28 + -100
            int r28 = r28 * r29
            if (r28 >= 0) goto L_0x1007
            r28 = 0
            r0 = r28
            r1 = r34
            r1.yK1 = r0
            r0 = r34
            java.util.List<java.lang.Integer> r0 = r0.queue
            r28 = r0
            r28.clear()
            r0 = r22
            r1 = r34
            r1.queueScaleForlevel = r0
            r0 = r34
            java.util.List<java.lang.Integer> r0 = r0.queue
            r28 = r0
            r29 = 0
            r0 = r34
            int r0 = r0.queueScaleForlevel
            r30 = r0
            java.lang.Integer r30 = java.lang.Integer.valueOf(r30)
            r28.add(r29, r30)
            r28 = 0
            java.lang.Boolean r28 = java.lang.Boolean.valueOf(r28)
            r0 = r28
            r1 = r34
            r1.startSeekThread = r0
        L_0x0fe5:
            r0 = r34
            android.os.Handler r0 = r0.mHandler
            r28 = r0
            if (r28 == 0) goto L_0x0ff8
            r0 = r34
            android.os.Handler r0 = r0.mHandler
            r28 = r0
            r29 = 10001(0x2711, float:1.4014E-41)
            r28.removeMessages(r29)
        L_0x0ff8:
            r0 = r34
            android.os.Handler r0 = r0.mHandler
            r28 = r0
            r29 = 10001(0x2711, float:1.4014E-41)
            r30 = 8000(0x1f40, double:3.9525E-320)
            r28.sendEmptyMessageDelayed(r29, r30)
            goto L_0x0007
        L_0x1007:
            r0 = r34
            int r0 = r0.queueScaleForlevel
            r28 = r0
            r0 = r28
            double r0 = (double) r0
            r28 = r0
            r30 = 4606281698874543309(0x3feccccccccccccd, double:0.9)
            double r28 = r28 * r30
            r0 = r22
            double r0 = (double) r0
            r30 = r0
            r32 = 4591870180066957722(0x3fb999999999999a, double:0.1)
            double r30 = r30 * r32
            double r28 = r28 + r30
            r0 = r28
            int r0 = (int) r0
            r28 = r0
            r0 = r28
            r1 = r34
            r1.queueScaleForlevel = r0
            r0 = r34
            java.util.List<java.lang.Integer> r0 = r0.queue
            r28 = r0
            r29 = 0
            r0 = r34
            int r0 = r0.queueScaleForlevel
            r30 = r0
            java.lang.Integer r30 = java.lang.Integer.valueOf(r30)
            r28.add(r29, r30)
            r28 = 1
            java.lang.Boolean r28 = java.lang.Boolean.valueOf(r28)
            r0 = r28
            r1 = r34
            r1.startSeekThread = r0
            goto L_0x0fe5
        L_0x1054:
            r0 = r22
            r1 = r34
            r1.queueScaleForlevel = r0
            r0 = r34
            java.util.List<java.lang.Integer> r0 = r0.queue
            r28 = r0
            r29 = 0
            r0 = r34
            int r0 = r0.queueScaleForlevel
            r30 = r0
            java.lang.Integer r30 = java.lang.Integer.valueOf(r30)
            r28.add(r29, r30)
            r28 = 1
            java.lang.Boolean r28 = java.lang.Boolean.valueOf(r28)
            r0 = r28
            r1 = r34
            r1.startSeekThread = r0
            goto L_0x0fe5
        L_0x107d:
            r28 = 106503(0x1a007, float:1.49242E-40)
            r0 = r34
            float r0 = r0.f1096zK
            r29 = r0
            r0 = r29
            int r0 = (int) r0
            r29 = r0
            java.lang.String r29 = java.lang.String.valueOf(r29)
            com.freevisiontech.fvmobile.utility.Util.sendIntEventMessge((int) r28, (java.lang.String) r29)
            r28 = 0
            r0 = r28
            r1 = r34
            r1.yK1 = r0
            r28 = 0
            java.lang.Boolean r28 = java.lang.Boolean.valueOf(r28)
            r0 = r28
            r1 = r34
            r1.startSeekThread = r0
            r0 = r34
            java.util.List<java.lang.Integer> r0 = r0.queue
            r28 = r0
            r28.clear()
            r0 = r34
            java.util.List<java.lang.Long> r0 = r0.queueTime
            r28 = r0
            r28.clear()
            goto L_0x0fe5
        L_0x10ba:
            r28 = 1
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r28 = r0
            r29 = 47
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x1251
            r28 = 2
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r23 = r0
            r28 = 3
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r21 = r0
            boolean r28 = r34.getPtzExposureExclusion()
            if (r28 != 0) goto L_0x0007
            boolean r28 = com.freevisiontech.fvmobile.utility.CameraUtils.getBosIsResume()
            r0 = r28
            r1 = r34
            r1.isResume = r0
            r0 = r34
            boolean r0 = r0.isResume
            r28 = r0
            if (r28 == 0) goto L_0x0007
            r28 = 0
            r0 = r28
            r1 = r34
            r1.scaleForExposurePosition = r0
            r28 = 100
            r0 = r21
            r1 = r28
            if (r0 == r1) goto L_0x121a
            r0 = r34
            java.util.List<java.lang.Integer> r0 = r0.queueExposure
            r28 = r0
            int r28 = r28.size()
            r29 = 1
            r0 = r28
            r1 = r29
            if (r0 <= r1) goto L_0x11f1
            r0 = r34
            java.util.List<java.lang.Integer> r0 = r0.queueExposure
            r28 = r0
            r29 = 0
            java.lang.Object r28 = r28.get(r29)
            java.lang.Integer r28 = (java.lang.Integer) r28
            int r28 = r28.intValue()
            int r29 = r28 + -100
            r0 = r34
            java.util.List<java.lang.Integer> r0 = r0.queueExposure
            r28 = r0
            r30 = 1
            r0 = r28
            r1 = r30
            java.lang.Object r28 = r0.get(r1)
            java.lang.Integer r28 = (java.lang.Integer) r28
            int r28 = r28.intValue()
            int r28 = r28 + -100
            int r28 = r28 * r29
            if (r28 >= 0) goto L_0x11a4
            r28 = 0
            r0 = r28
            r1 = r34
            r1.yK1Exposure = r0
            r0 = r34
            java.util.List<java.lang.Integer> r0 = r0.queueExposure
            r28 = r0
            r28.clear()
            r0 = r21
            r1 = r34
            r1.queueForlevel = r0
            r0 = r34
            java.util.List<java.lang.Integer> r0 = r0.queueExposure
            r28 = r0
            r29 = 0
            r0 = r34
            int r0 = r0.queueForlevel
            r30 = r0
            java.lang.Integer r30 = java.lang.Integer.valueOf(r30)
            r28.add(r29, r30)
            r28 = 0
            java.lang.Boolean r28 = java.lang.Boolean.valueOf(r28)
            r0 = r28
            r1 = r34
            r1.startSeekThreadExposure = r0
        L_0x1182:
            r0 = r34
            android.os.Handler r0 = r0.mHandler
            r28 = r0
            if (r28 == 0) goto L_0x1242
        L_0x118a:
            r0 = r34
            android.os.Handler r0 = r0.mHandler
            r28 = r0
            r29 = 10000(0x2710, float:1.4013E-41)
            boolean r28 = r28.hasMessages(r29)
            if (r28 == 0) goto L_0x1242
            r0 = r34
            android.os.Handler r0 = r0.mHandler
            r28 = r0
            r29 = 10000(0x2710, float:1.4013E-41)
            r28.removeMessages(r29)
            goto L_0x118a
        L_0x11a4:
            r0 = r34
            int r0 = r0.queueForlevel
            r28 = r0
            r0 = r28
            double r0 = (double) r0
            r28 = r0
            r30 = 4606281698874543309(0x3feccccccccccccd, double:0.9)
            double r28 = r28 * r30
            r0 = r21
            double r0 = (double) r0
            r30 = r0
            r32 = 4591870180066957722(0x3fb999999999999a, double:0.1)
            double r30 = r30 * r32
            double r28 = r28 + r30
            r0 = r28
            int r0 = (int) r0
            r28 = r0
            r0 = r28
            r1 = r34
            r1.queueForlevel = r0
            r0 = r34
            java.util.List<java.lang.Integer> r0 = r0.queueExposure
            r28 = r0
            r29 = 0
            r0 = r34
            int r0 = r0.queueForlevel
            r30 = r0
            java.lang.Integer r30 = java.lang.Integer.valueOf(r30)
            r28.add(r29, r30)
            r28 = 1
            java.lang.Boolean r28 = java.lang.Boolean.valueOf(r28)
            r0 = r28
            r1 = r34
            r1.startSeekThreadExposure = r0
            goto L_0x1182
        L_0x11f1:
            r0 = r21
            r1 = r34
            r1.queueForlevel = r0
            r0 = r34
            java.util.List<java.lang.Integer> r0 = r0.queueExposure
            r28 = r0
            r29 = 0
            r0 = r34
            int r0 = r0.queueForlevel
            r30 = r0
            java.lang.Integer r30 = java.lang.Integer.valueOf(r30)
            r28.add(r29, r30)
            r28 = 1
            java.lang.Boolean r28 = java.lang.Boolean.valueOf(r28)
            r0 = r28
            r1 = r34
            r1.startSeekThreadExposure = r0
            goto L_0x1182
        L_0x121a:
            r28 = 0
            r0 = r28
            r1 = r34
            r1.yK1Exposure = r0
            r28 = 0
            java.lang.Boolean r28 = java.lang.Boolean.valueOf(r28)
            r0 = r28
            r1 = r34
            r1.startSeekThreadExposure = r0
            r0 = r34
            java.util.List<java.lang.Integer> r0 = r0.queueExposure
            r28 = r0
            r28.clear()
            r0 = r34
            java.util.List<java.lang.Long> r0 = r0.queueTimeExposure
            r28 = r0
            r28.clear()
            goto L_0x1182
        L_0x1242:
            r0 = r34
            android.os.Handler r0 = r0.mHandler
            r28 = r0
            r29 = 10000(0x2710, float:1.4013E-41)
            r30 = 8000(0x1f40, double:3.9525E-320)
            r28.sendEmptyMessageDelayed(r29, r30)
            goto L_0x0007
        L_0x1251:
            r28 = 1
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r28 = r0
            r29 = 48
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x0007
            r0 = r34
            boolean r0 = r0.connected
            r28 = r0
            if (r28 == 0) goto L_0x1289
            r28 = 2
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r28 = r0
            r29 = 1
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x130d
            r28 = 48
            r29 = 1
            com.freevisiontech.fvmobile.utils.BleByteUtil.actPTZSettingStateChange(r28, r29)
            r28 = 1
            com.freevisiontech.fvmobile.utility.CameraUtils.setCameraFocusing(r28)
        L_0x1289:
            r28 = 2
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r28 = r0
            r29 = 1
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x0007
            boolean r28 = r34.getPtzCenterFoucusExclusion()
            if (r28 != 0) goto L_0x0007
            boolean r28 = com.freevisiontech.fvmobile.utility.CameraUtils.getBosIsResume()
            r0 = r28
            r1 = r34
            r1.isResume = r0
            r0 = r34
            boolean r0 = r0.isResume
            r28 = r0
            if (r28 == 0) goto L_0x0007
            android.support.v4.app.FragmentActivity r28 = r34.getActivity()
            java.lang.String r29 = "advance_set_ptz_long_press_focus"
            r30 = 0
            java.lang.Boolean r30 = java.lang.Boolean.valueOf(r30)
            java.lang.Object r28 = com.freevisiontech.fvmobile.utility.SPUtils.get(r28, r29, r30)
            java.lang.Boolean r28 = (java.lang.Boolean) r28
            boolean r14 = r28.booleanValue()
            int r28 = com.freevisiontech.fvmobile.utility.CameraUtils.getCurrentPageIndex()
            r29 = 1
            r0 = r28
            r1 = r29
            if (r0 == r1) goto L_0x12e2
            int r28 = com.freevisiontech.fvmobile.utility.CameraUtils.getCurrentPageIndex()
            r29 = 2
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x12e3
        L_0x12e2:
            r14 = 1
        L_0x12e3:
            if (r14 == 0) goto L_0x0007
            r28 = 106600(0x1a068, float:1.49378E-40)
            com.freevisiontech.fvmobile.utility.Util.sendIntEventMessge(r28)
            r0 = r34
            android.os.Handler r0 = r0.mHandler
            r28 = r0
            if (r28 == 0) goto L_0x137e
        L_0x12f3:
            r0 = r34
            android.os.Handler r0 = r0.mHandler
            r28 = r0
            r29 = 10000(0x2710, float:1.4013E-41)
            boolean r28 = r28.hasMessages(r29)
            if (r28 == 0) goto L_0x137e
            r0 = r34
            android.os.Handler r0 = r0.mHandler
            r28 = r0
            r29 = 10000(0x2710, float:1.4013E-41)
            r28.removeMessages(r29)
            goto L_0x12f3
        L_0x130d:
            r28 = 2
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r28 = r0
            if (r28 != 0) goto L_0x1351
            r28 = 48
            r29 = 0
            com.freevisiontech.fvmobile.utils.BleByteUtil.actPTZSettingStateChange(r28, r29)
            r28 = 0
            com.freevisiontech.fvmobile.utility.CameraUtils.setCameraFocusing(r28)
            r28 = 106503(0x1a007, float:1.49242E-40)
            r0 = r34
            float r0 = r0.f1096zK
            r29 = r0
            r0 = r29
            int r0 = (int) r0
            r29 = r0
            java.lang.String r29 = java.lang.String.valueOf(r29)
            com.freevisiontech.fvmobile.utility.Util.sendIntEventMessge((int) r28, (java.lang.String) r29)
            r28 = 106512(0x1a010, float:1.49255E-40)
            r0 = r34
            float r0 = r0.zKExposure
            r29 = r0
            r0 = r29
            int r0 = (int) r0
            r29 = r0
            java.lang.String r29 = java.lang.String.valueOf(r29)
            com.freevisiontech.fvmobile.utility.Util.sendIntEventMessge((int) r28, (java.lang.String) r29)
            goto L_0x1289
        L_0x1351:
            r28 = 2
            byte r28 = r27[r28]
            r0 = r28
            r0 = r0 & 255(0xff, float:3.57E-43)
            r28 = r0
            r29 = 2
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x1289
            r28 = 48
            r29 = 2
            com.freevisiontech.fvmobile.utils.BleByteUtil.ackPTZPanorama(r28, r29)
            int r28 = com.freevisiontech.fvmobile.utility.CameraUtils.getCurrentPageIndex()
            r29 = 1
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x1289
            r28 = 107410(0x1a392, float:1.50513E-40)
            com.freevisiontech.fvmobile.utility.Util.sendIntEventMessge(r28)
            goto L_0x1289
        L_0x137e:
            r0 = r34
            android.os.Handler r0 = r0.mHandler
            r28 = r0
            r29 = 10000(0x2710, float:1.4013E-41)
            r30 = 8000(0x1f40, double:3.9525E-320)
            r28.sendEmptyMessageDelayed(r29, r30)
            goto L_0x0007
        L_0x138d:
            r0 = r34
            android.content.Context r0 = r0.mContext
            r28 = r0
            java.lang.String r29 = "pop_show_outside_click_interceptor"
            r30 = 0
            java.lang.Boolean r30 = java.lang.Boolean.valueOf(r30)
            java.lang.Object r28 = com.freevisiontech.fvmobile.utility.SPUtils.get(r28, r29, r30)
            java.lang.Boolean r28 = (java.lang.Boolean) r28
            boolean r13 = r28.booleanValue()
            if (r13 != 0) goto L_0x0007
            boolean r28 = com.freevisiontech.fvmobile.utility.CameraUtils.getIsBooleanTimeLapseUIShow()
            if (r28 != 0) goto L_0x0007
            boolean r28 = com.freevisiontech.fvmobile.utility.CameraUtils.isRecordingIng()
            if (r28 == 0) goto L_0x13ba
            boolean r28 = com.freevisiontech.fvmobile.utility.CameraUtils.getBooRecordStarted()
            if (r28 != 0) goto L_0x0007
        L_0x13ba:
            boolean r28 = com.freevisiontech.fvmobile.utility.CameraUtils.isFollowIng()
            if (r28 != 0) goto L_0x0007
            com.freevisiontech.fvmobile.utils.MoveTimelapseUtil r28 = com.freevisiontech.fvmobile.utils.MoveTimelapseUtil.getInstance()
            int r28 = r28.getCameraProgressLinear()
            r29 = 1
            r0 = r28
            r1 = r29
            if (r0 == r1) goto L_0x0007
            int r28 = com.freevisiontech.fvmobile.utility.CameraUtils.getFrameLayerNumber()
            if (r28 == 0) goto L_0x13eb
            int r26 = com.freevisiontech.fvmobile.utility.CameraUtils.getLabelTopBarSelectMemory()
            r28 = 107705(0x1a4b9, float:1.50927E-40)
            com.freevisiontech.fvmobile.utility.Util.sendIntEventMessge(r28)
            com.freevisiontech.fvmobile.utility.CameraUtils.setLabelTopBarSelectMemory(r26)
            r28 = 107744(0x1a4e0, float:1.50982E-40)
            com.freevisiontech.fvmobile.utility.Util.sendIntEventMessge(r28)
            goto L_0x0007
        L_0x13eb:
            r0 = r34
            com.makeramen.roundedimageview.RoundedImageView r0 = r0.btnIconShow
            r28 = r0
            r28.performClick()
            goto L_0x0007
        */
        throw new UnsupportedOperationException("Method not decompiled: com.freevisiontech.fvmobile.fragment.FVBottomBarFragment.receiveEvent(com.freevisiontech.fvmobile.utils.Event):void");
    }

    private void setFM210ImpellerModeZoom() {
        if (!getPtzStretchLensExclusionAnd210()) {
            this.isResume = CameraUtils.getBosIsResume();
            if (this.isResume) {
                if (this.deltaFM210 >= 1) {
                    this.deltaFM210 = 1;
                } else if (this.deltaFM210 <= -1) {
                    this.deltaFM210 = -1;
                }
                this.wtProScaleForlevel210 = this.deltaFM210 * 10;
                if (CameraUtils.getHandModelScaleValueWT() != 0.0f) {
                    this.wtProRealTimeValueStart210 = (double) CameraUtils.getHandModelScaleValueWT();
                    this.wtProRealTimeValueEnd210 = this.wtProRealTimeValueStart210;
                    CameraUtils.setHandModelScaleValueWT(0);
                }
                this.wtProRealTimeValueStart210 = Double.valueOf(this.wtProRealTimeValueStart210 + Double.valueOf((double) this.wtProScaleForlevel210).doubleValue()).doubleValue();
                if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.MARK_POINT_SPACING_YES_NO, Integer.valueOf(Constants.MARK_POINT_SPACING_NO))).intValue() != 107021 || !CameraUtils.getMarkPointUIIsVisible()) {
                    if (this.wtProRealTimeValueStart210 >= 1000.0d) {
                        this.wtProRealTimeValueStart210 = 1000.0d;
                    } else if (this.wtProRealTimeValueStart210 < 0.0d) {
                        this.wtProRealTimeValueStart210 = 0.0d;
                    }
                    this.startSeekThreadNew210 = true;
                    this.startSeekThreadHandModel210 = false;
                } else {
                    this.startSeekThreadNew210 = true;
                    this.startSeekThreadHandModel210 = false;
                    float markPointWtA = CameraUtils.getLlMarkPointWtA();
                    float markPointWtB = CameraUtils.getLlMarkPointWtB();
                    double maxWT = CameraUtils.getScaleScrollViewWTMaxNums() + 1.0d;
                    double realValueA = (((double) (markPointWtA - 1.0f)) * 1000.0d) / (maxWT - 1.0d);
                    double realValueB = (((double) (markPointWtB - 1.0f)) * 1000.0d) / (maxWT - 1.0d);
                    if (markPointWtA < markPointWtB) {
                        if (this.wtProRealTimeValueStart210 >= realValueB) {
                            this.wtProRealTimeValueStart210 = realValueB;
                        } else if (this.wtProRealTimeValueStart210 < realValueA) {
                            this.wtProRealTimeValueStart210 = realValueA;
                        }
                    } else if (this.wtProRealTimeValueStart210 >= realValueA) {
                        this.wtProRealTimeValueStart210 = realValueA;
                    } else if (this.wtProRealTimeValueStart210 < realValueB) {
                        this.wtProRealTimeValueStart210 = realValueB;
                    }
                }
                if (this.mHandler != null && this.mHandler.hasMessages(10001)) {
                    this.mHandler.removeMessages(10001);
                }
                this.mHandler.sendEmptyMessageDelayed(10001, 8000);
            }
        }
    }

    public boolean isMarkRunning() {
        if (this.startMarkPointMfAToB.booleanValue() || this.startMarkPointMfBToA.booleanValue() || this.startMarkPointMfChangeTimeHalfSecondBoo.booleanValue() || this.startMarkPointMfChangeTimeHalfSecondBoo.booleanValue()) {
            return true;
        }
        return false;
    }

    private void setFM210ImpellerModeMf() {
        if (!getPtzStretchLensExclusionAnd210() && this.cameraManager.getCameraManagerType() == 2) {
            if (this.startMarkPointMfAToB.booleanValue() || this.startMarkPointMfBToA.booleanValue() || this.startMarkPointMfChangeTimeHalfSecondBoo.booleanValue() || this.startMarkPointMfChangeTimeHalfSecondBoo.booleanValue()) {
                EventBusUtil.sendEvent(new Event(133));
                return;
            }
            this.isResume = CameraUtils.getBosIsResume();
            if (this.isResume) {
                int maxMF = Util.getDrawScaleMFMax();
                if (this.deltaFM210 >= 1) {
                    this.deltaFM210 = 1;
                } else if (this.deltaFM210 <= -1) {
                    this.deltaFM210 = -1;
                }
                this.mfProScaleForlevel210 = this.deltaFM210;
                if (CameraUtils.getHandModelScaleValueMF() != 0.0f) {
                    this.mfProRealTimeValueStart210 = (double) CameraUtils.getHandModelScaleValueMF();
                    this.mfProRealTimeValueEnd210 = this.mfProRealTimeValueStart210;
                    CameraUtils.setHandModelScaleValueMF(0);
                }
                this.mfProRealTimeValueStart210 = Double.valueOf(this.mfProRealTimeValueStart210 + Double.valueOf((double) this.mfProScaleForlevel210).doubleValue()).doubleValue();
                if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.MARK_POINT_SPACING_YES_NO, Integer.valueOf(Constants.MARK_POINT_SPACING_NO))).intValue() != 107021 || !CameraUtils.getMarkPointUIIsVisible()) {
                    if (this.mfProRealTimeValueStart210 >= ((double) maxMF)) {
                        this.mfProRealTimeValueStart210 = (double) maxMF;
                    } else if (this.mfProRealTimeValueStart210 < 0.0d) {
                        this.mfProRealTimeValueStart210 = 0.0d;
                    }
                    this.startSeekThreadMF210 = true;
                    this.startSeekThreadHandModel210 = false;
                } else {
                    this.startSeekThreadMF210 = true;
                    this.startSeekThreadHandModel210 = false;
                    float markPointMfA = CameraUtils.getLlMarkPointMfA();
                    float markPointMfB = CameraUtils.getLlMarkPointMfB();
                    if (markPointMfA < markPointMfB) {
                        if (this.mfProRealTimeValueStart210 >= ((double) ((markPointMfB * 10.0f) + 10.0f))) {
                            this.mfProRealTimeValueStart210 = (double) ((markPointMfB * 10.0f) + 10.0f);
                            this.mfProRealTimeValueEnd210 = this.mfProRealTimeValueStart210;
                            this.startSeekThreadMF210 = false;
                            Util.sendIntEventMessge((int) Constants.LABEL_CAMERA_MF_FRAME_VISIBLE, String.valueOf(markPointMfB));
                        } else if (this.mfProRealTimeValueStart210 < ((double) ((markPointMfA * 10.0f) + 10.0f))) {
                            this.mfProRealTimeValueStart210 = (double) ((markPointMfA * 10.0f) + 10.0f);
                            this.mfProRealTimeValueEnd210 = this.mfProRealTimeValueStart210;
                            this.startSeekThreadMF210 = false;
                            Util.sendIntEventMessge((int) Constants.LABEL_CAMERA_MF_FRAME_VISIBLE, String.valueOf(markPointMfA));
                        }
                    } else if (this.mfProRealTimeValueStart210 >= ((double) ((markPointMfA * 10.0f) + 10.0f))) {
                        this.mfProRealTimeValueStart210 = (double) ((markPointMfA * 10.0f) + 10.0f);
                        this.mfProRealTimeValueEnd210 = this.mfProRealTimeValueStart210;
                        this.startSeekThreadMF210 = false;
                        Util.sendIntEventMessge((int) Constants.LABEL_CAMERA_MF_FRAME_VISIBLE, String.valueOf(markPointMfA));
                    } else if (this.mfProRealTimeValueStart210 < ((double) ((markPointMfB * 10.0f) + 10.0f))) {
                        this.mfProRealTimeValueStart210 = (double) ((markPointMfB * 10.0f) + 10.0f);
                        this.mfProRealTimeValueEnd210 = this.mfProRealTimeValueStart210;
                        this.startSeekThreadMF210 = false;
                        Util.sendIntEventMessge((int) Constants.LABEL_CAMERA_MF_FRAME_VISIBLE, String.valueOf(markPointMfB));
                    }
                }
                if (this.mHandler != null) {
                    while (this.mHandler.hasMessages(10000)) {
                        this.mHandler.removeMessages(10000);
                    }
                }
                this.mHandler.sendEmptyMessageDelayed(10000, 8000);
            }
        }
    }

    class SeekZoomThreed210 extends Thread {
        SeekZoomThreed210() {
        }

        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                if (FVBottomBarFragment.this.startSeekThreadNew210.booleanValue()) {
                    if (((Integer) SPUtils.get(FVBottomBarFragment.this.getActivity(), SharePrefConstant.ZOOM_VELOCITY_MEDIUM_OR_SLOW, Integer.valueOf(Constants.ZOOM_VELOCITY_MEDIUM))).intValue() == 107094) {
                        double unused = FVBottomBarFragment.this.wtProRealTimeValueEnd210 = FVBottomBarFragment.this.wtProRealTimeValueEnd210 * 0.965d;
                        double unused2 = FVBottomBarFragment.this.wtProRealTimeValueEnd210 = FVBottomBarFragment.this.wtProRealTimeValueEnd210 + (0.035d * FVBottomBarFragment.this.wtProRealTimeValueStart210);
                    } else {
                        double unused3 = FVBottomBarFragment.this.wtProRealTimeValueEnd210 = FVBottomBarFragment.this.wtProRealTimeValueEnd210 * 0.98d;
                        double unused4 = FVBottomBarFragment.this.wtProRealTimeValueEnd210 = FVBottomBarFragment.this.wtProRealTimeValueEnd210 + (0.02d * FVBottomBarFragment.this.wtProRealTimeValueStart210);
                    }
                    if (CameraUtils.getCameraHandModel() && CameraUtils.getHandModelVisibleStateValue() == 6.0f) {
                        int maxWT = (Util.getDrawScaleWTMax() / 10) * 10;
                        int timeWT = ((int) (FVBottomBarFragment.this.wtProRealTimeValueEnd210 * ((double) maxWT))) / 1000;
                        if (Math.abs(1000.0d - FVBottomBarFragment.this.wtProRealTimeValueEnd210) < 5.0d) {
                            timeWT = maxWT;
                        }
                        if (FVBottomBarFragment.this.timeWTOld != timeWT) {
                            Util.sendIntEventMessge((int) Constants.LABEL_CAMERA_IMPELLER_MODE_EXPOSURE_210, String.valueOf(timeWT));
                        }
                        int unused5 = FVBottomBarFragment.this.timeWTOld = timeWT;
                    } else if (FVBottomBarFragment.this.wtProScaleForlevel210 >= 0) {
                        Util.sendIntEventMessge((int) Constants.LENS_TENSILE_OF_CAMERA, String.valueOf((int) (FVBottomBarFragment.this.wtProRealTimeValueEnd210 + 10.0d)));
                    } else {
                        Util.sendIntEventMessge((int) Constants.LENS_TENSILE_OF_CAMERA, String.valueOf((int) FVBottomBarFragment.this.wtProRealTimeValueEnd210));
                    }
                    if (Math.abs(FVBottomBarFragment.this.wtProRealTimeValueEnd210 - FVBottomBarFragment.this.wtProRealTimeValueStart210) < 0.01d) {
                        Boolean unused6 = FVBottomBarFragment.this.startSeekThreadNew210 = false;
                    }
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }

    class SeekThreed extends Thread {
        SeekThreed() {
        }

        public void run() {
            double startMF;
            double endMF;
            while (!Thread.currentThread().isInterrupted()) {
                if (FVBottomBarFragment.this.startSeekThreadHandModel210.booleanValue()) {
                    if (FVBottomBarFragment.this.queueHandModel.size() <= 0) {
                        FVBottomBarFragment.this.queueHandModel.clear();
                        Boolean unused = FVBottomBarFragment.this.startSeekThreadHandModel210 = false;
                        FVBottomBarFragment.this.sleepMillis = 100;
                    } else if (!FVBottomBarFragment.this.queueHandModel.contains((Object) null) && (FVBottomBarFragment.this.queueHandModel.contains(4) || FVBottomBarFragment.this.queueHandModel.contains(-4))) {
                        int unused2 = FVBottomBarFragment.this.queueHandModelPosition = ((Integer) FVBottomBarFragment.this.queueHandModel.get(0)).intValue();
                        FVBottomBarFragment.this.queueHandModel.remove(0);
                        int delta = FVBottomBarFragment.this.queueHandModelPosition / 2;
                        if (CameraUtils.getFrameLayerNumber() == 0) {
                            Util.sendIntEventMessge((int) Constants.LABEL_CAMERA_HAND_MODEL_INT_CHANGE, String.valueOf(delta));
                        }
                    }
                    try {
                        Thread.sleep(FVBottomBarFragment.this.sleepMillis);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                } else if (FVBottomBarFragment.this.startSeekThreadNew300.booleanValue()) {
                    if (((Integer) SPUtils.get(FVBottomBarFragment.this.getActivity(), SharePrefConstant.ZOOM_VELOCITY_MEDIUM_OR_SLOW, Integer.valueOf(Constants.ZOOM_VELOCITY_MEDIUM))).intValue() == 107094) {
                        if ("Redmi Note 5".equals(FVBottomBarFragment.this.systemModel)) {
                            double unused3 = FVBottomBarFragment.this.wtProRealTimeValueEnd300 = FVBottomBarFragment.this.wtProRealTimeValueEnd300 * 0.965d;
                            double unused4 = FVBottomBarFragment.this.wtProRealTimeValueEnd300 = FVBottomBarFragment.this.wtProRealTimeValueEnd300 + (0.035d * FVBottomBarFragment.this.wtProRealTimeValueStart300);
                        } else {
                            double unused5 = FVBottomBarFragment.this.wtProRealTimeValueEnd300 = FVBottomBarFragment.this.wtProRealTimeValueEnd300 * 0.95d;
                            double unused6 = FVBottomBarFragment.this.wtProRealTimeValueEnd300 = FVBottomBarFragment.this.wtProRealTimeValueEnd300 + (0.05d * FVBottomBarFragment.this.wtProRealTimeValueStart300);
                        }
                    } else if ("Redmi Note 5".equals(FVBottomBarFragment.this.systemModel)) {
                        double unused7 = FVBottomBarFragment.this.wtProRealTimeValueEnd300 = FVBottomBarFragment.this.wtProRealTimeValueEnd300 * 0.98d;
                        double unused8 = FVBottomBarFragment.this.wtProRealTimeValueEnd300 = FVBottomBarFragment.this.wtProRealTimeValueEnd300 + (0.02d * FVBottomBarFragment.this.wtProRealTimeValueStart300);
                    } else {
                        double unused9 = FVBottomBarFragment.this.wtProRealTimeValueEnd300 = FVBottomBarFragment.this.wtProRealTimeValueEnd300 * 0.965d;
                        double unused10 = FVBottomBarFragment.this.wtProRealTimeValueEnd300 = FVBottomBarFragment.this.wtProRealTimeValueEnd300 + (0.035d * FVBottomBarFragment.this.wtProRealTimeValueStart300);
                    }
                    if (FVBottomBarFragment.this.wtProScaleForlevel300 >= 0) {
                        Util.sendIntEventMessge((int) Constants.LENS_TENSILE_OF_CAMERA, String.valueOf((int) (FVBottomBarFragment.this.wtProRealTimeValueEnd300 + 10.0d)));
                    } else {
                        Util.sendIntEventMessge((int) Constants.LENS_TENSILE_OF_CAMERA, String.valueOf((int) FVBottomBarFragment.this.wtProRealTimeValueEnd300));
                    }
                    if (Math.abs(FVBottomBarFragment.this.wtProRealTimeValueEnd300 - FVBottomBarFragment.this.wtProRealTimeValueStart300) < 0.001d) {
                        Boolean unused11 = FVBottomBarFragment.this.startSeekThreadNew300 = false;
                    }
                    try {
                        if ("Redmi Note 5".equals(FVBottomBarFragment.this.systemModel)) {
                            Thread.sleep(5);
                        } else {
                            Thread.sleep(15);
                        }
                    } catch (InterruptedException e2) {
                        Thread.currentThread().interrupt();
                    }
                } else if (FVBottomBarFragment.this.startSeekThread300.booleanValue()) {
                    FVBottomBarFragment.access$4008(FVBottomBarFragment.this);
                    if (FVBottomBarFragment.this.scaleForlevelPosition == 1 && FVBottomBarFragment.this.queue.size() > 0) {
                        if (CameraUtils.getCamLengthProgress() != -1) {
                            int unused12 = FVBottomBarFragment.this.queuePosition = CameraUtils.getCamLengthProgress();
                            float unused13 = FVBottomBarFragment.this.f1096zK = (float) FVBottomBarFragment.this.queuePosition;
                            CameraUtils.setCamLengthProgress(-1);
                        } else {
                            int unused14 = FVBottomBarFragment.this.queuePosition = ((Integer) FVBottomBarFragment.this.queue.get(0)).intValue();
                        }
                        if (FVBottomBarFragment.this.queue.size() > 0) {
                            if (((Integer) SPUtils.get(FVBottomBarFragment.this.getActivity(), SharePrefConstant.ZOOM_VELOCITY_MEDIUM_OR_SLOW, Integer.valueOf(Constants.ZOOM_VELOCITY_MEDIUM))).intValue() == 107094) {
                                float unused15 = FVBottomBarFragment.this.yK1 = (float) ((0.8d * ((double) FVBottomBarFragment.this.yK1)) + (0.036d * ((double) FVBottomBarFragment.this.queuePosition)));
                            } else {
                                float unused16 = FVBottomBarFragment.this.yK1 = (float) ((0.8d * ((double) FVBottomBarFragment.this.yK1)) + (0.024d * ((double) FVBottomBarFragment.this.queuePosition)));
                            }
                            float unused17 = FVBottomBarFragment.this.f1096zK = FVBottomBarFragment.this.f1096zK + FVBottomBarFragment.this.yK1;
                            if (FVBottomBarFragment.this.f1096zK > 100.0f) {
                                float unused18 = FVBottomBarFragment.this.f1096zK = 100.0f;
                            }
                            if (FVBottomBarFragment.this.f1096zK < 0.0f) {
                                float unused19 = FVBottomBarFragment.this.f1096zK = 0.0f;
                            }
                            Util.sendIntEventMessge((int) Constants.LENS_TENSILE_OF_CAMERA, String.valueOf((int) FVBottomBarFragment.this.f1096zK));
                            Log.e("------------------", "-------777----  FM300镜头拉伸  FM300镜头拉伸  FM300镜头拉伸 --------");
                        }
                    } else if (FVBottomBarFragment.this.scaleForlevelPosition < 3) {
                        if (((Integer) SPUtils.get(FVBottomBarFragment.this.getActivity(), SharePrefConstant.ZOOM_VELOCITY_MEDIUM_OR_SLOW, Integer.valueOf(Constants.ZOOM_VELOCITY_MEDIUM))).intValue() == 107094) {
                            float unused20 = FVBottomBarFragment.this.yK1 = (float) (0.18d * ((double) FVBottomBarFragment.this.queuePosition));
                        } else {
                            float unused21 = FVBottomBarFragment.this.yK1 = (float) (0.12d * ((double) FVBottomBarFragment.this.queuePosition));
                        }
                        float unused22 = FVBottomBarFragment.this.f1096zK = FVBottomBarFragment.this.f1096zK + FVBottomBarFragment.this.yK1;
                        if (FVBottomBarFragment.this.f1096zK > 100.0f) {
                            float unused23 = FVBottomBarFragment.this.f1096zK = 100.0f;
                        }
                        if (FVBottomBarFragment.this.f1096zK < 0.0f) {
                            float unused24 = FVBottomBarFragment.this.f1096zK = 0.0f;
                        }
                        Log.e("--------------", "---------  scaleForlevelPosition  scaleForlevelPosition  ------" + FVBottomBarFragment.this.scaleForlevelPosition);
                        Util.sendIntEventMessge((int) Constants.LENS_TENSILE_OF_CAMERA, String.valueOf((int) FVBottomBarFragment.this.f1096zK));
                        Log.e("------------------", "-------777----  FM300镜头拉伸  FM300镜头拉伸  FM300镜头拉伸 --------");
                    } else {
                        Boolean unused25 = FVBottomBarFragment.this.startSeekThread300 = false;
                        Util.sendIntEventMessge((int) Constants.LENS_TENSILE_OF_CAMERA_OVER, String.valueOf((int) FVBottomBarFragment.this.f1096zK));
                    }
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e3) {
                        Thread.currentThread().interrupt();
                    }
                } else if (FVBottomBarFragment.this.startSeekThread210.booleanValue()) {
                    FVBottomBarFragment.access$4008(FVBottomBarFragment.this);
                    if (FVBottomBarFragment.this.scaleForlevelPosition == 1 && FVBottomBarFragment.this.queue.size() > 0) {
                        if (CameraUtils.getCamLengthProgress() != -1) {
                            int unused26 = FVBottomBarFragment.this.queuePosition = CameraUtils.getCamLengthProgress();
                            float unused27 = FVBottomBarFragment.this.f1096zK = (float) FVBottomBarFragment.this.queuePosition;
                            CameraUtils.setCamLengthProgress(-1);
                        } else {
                            int unused28 = FVBottomBarFragment.this.queuePosition = ((Integer) FVBottomBarFragment.this.queue.get(0)).intValue();
                        }
                        if (FVBottomBarFragment.this.queue.size() > 0) {
                            if (((Integer) SPUtils.get(FVBottomBarFragment.this.getActivity(), SharePrefConstant.ZOOM_VELOCITY_MEDIUM_OR_SLOW, Integer.valueOf(Constants.ZOOM_VELOCITY_MEDIUM))).intValue() == 107094) {
                                float unused29 = FVBottomBarFragment.this.yK1 = (float) ((0.8d * ((double) FVBottomBarFragment.this.yK1)) + (0.036d * ((double) FVBottomBarFragment.this.queuePosition)));
                            } else {
                                float unused30 = FVBottomBarFragment.this.yK1 = (float) ((0.8d * ((double) FVBottomBarFragment.this.yK1)) + (0.024d * ((double) FVBottomBarFragment.this.queuePosition)));
                            }
                            float unused31 = FVBottomBarFragment.this.f1096zK = FVBottomBarFragment.this.f1096zK + FVBottomBarFragment.this.yK1;
                            if (FVBottomBarFragment.this.f1096zK > 100.0f) {
                                float unused32 = FVBottomBarFragment.this.f1096zK = 100.0f;
                            }
                            if (FVBottomBarFragment.this.f1096zK < 0.0f) {
                                float unused33 = FVBottomBarFragment.this.f1096zK = 0.0f;
                            }
                            Util.sendIntEventMessge((int) Constants.LENS_TENSILE_OF_CAMERA, String.valueOf((int) FVBottomBarFragment.this.f1096zK));
                            Log.e("------------------", "-------777----  FM210镜头拉伸  FM210镜头拉伸  FM210镜头拉伸 --------");
                        }
                    } else if (FVBottomBarFragment.this.scaleForlevelPosition < 13) {
                        if (((Integer) SPUtils.get(FVBottomBarFragment.this.getActivity(), SharePrefConstant.ZOOM_VELOCITY_MEDIUM_OR_SLOW, Integer.valueOf(Constants.ZOOM_VELOCITY_MEDIUM))).intValue() == 107094) {
                            float unused34 = FVBottomBarFragment.this.yK1 = (float) (0.18d * ((double) FVBottomBarFragment.this.queuePosition));
                        } else {
                            float unused35 = FVBottomBarFragment.this.yK1 = (float) (0.12d * ((double) FVBottomBarFragment.this.queuePosition));
                        }
                        float unused36 = FVBottomBarFragment.this.f1096zK = FVBottomBarFragment.this.f1096zK + FVBottomBarFragment.this.yK1;
                        if (FVBottomBarFragment.this.f1096zK > 100.0f) {
                            float unused37 = FVBottomBarFragment.this.f1096zK = 100.0f;
                        }
                        if (FVBottomBarFragment.this.f1096zK < 0.0f) {
                            float unused38 = FVBottomBarFragment.this.f1096zK = 0.0f;
                        }
                        Log.e("--------------", "---------  scaleForlevelPosition  scaleForlevelPosition  ------" + FVBottomBarFragment.this.scaleForlevelPosition);
                        Util.sendIntEventMessge((int) Constants.LENS_TENSILE_OF_CAMERA, String.valueOf((int) FVBottomBarFragment.this.f1096zK));
                        Log.e("------------------", "-------777----  FM210镜头拉伸  FM210镜头拉伸  FM210镜头拉伸 --------");
                    } else {
                        Boolean unused39 = FVBottomBarFragment.this.startSeekThread210 = false;
                        Util.sendIntEventMessge((int) Constants.LENS_TENSILE_OF_CAMERA_OVER, String.valueOf((int) FVBottomBarFragment.this.f1096zK));
                    }
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e4) {
                        Thread.currentThread().interrupt();
                    }
                } else if (FVBottomBarFragment.this.startSeekThreadMF210.booleanValue()) {
                    double unused40 = FVBottomBarFragment.this.mfProRealTimeValueEnd210 = FVBottomBarFragment.this.mfProRealTimeValueEnd210 * 0.965d;
                    double unused41 = FVBottomBarFragment.this.mfProRealTimeValueEnd210 = FVBottomBarFragment.this.mfProRealTimeValueEnd210 + (0.035d * FVBottomBarFragment.this.mfProRealTimeValueStart210);
                    if (FVBottomBarFragment.this.mfProRealTimeValueEnd210Old != ((int) FVBottomBarFragment.this.mfProRealTimeValueEnd210)) {
                        Log.e("----------------", "------------  8596  3269  8751  0258  ---------  MF: " + ((int) FVBottomBarFragment.this.mfProRealTimeValueEnd210));
                        if (FVBottomBarFragment.this.mfProScaleForlevel210 >= 0) {
                            Util.sendIntEventMessge((int) Constants.MF_OF_CAMERA, String.valueOf(((int) FVBottomBarFragment.this.mfProRealTimeValueEnd210) + 1));
                        } else {
                            Util.sendIntEventMessge((int) Constants.MF_OF_CAMERA, String.valueOf((int) FVBottomBarFragment.this.mfProRealTimeValueEnd210));
                        }
                    }
                    int unused42 = FVBottomBarFragment.this.mfProRealTimeValueEnd210Old = (int) FVBottomBarFragment.this.mfProRealTimeValueEnd210;
                    if (Math.abs(FVBottomBarFragment.this.mfProRealTimeValueEnd210 - FVBottomBarFragment.this.mfProRealTimeValueStart210) < 0.01d) {
                        Boolean unused43 = FVBottomBarFragment.this.startSeekThreadMF210 = false;
                    }
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e5) {
                        Thread.currentThread().interrupt();
                    }
                } else if (FVBottomBarFragment.this.startSeekThreadExposure300.booleanValue()) {
                    FVBottomBarFragment.access$5008(FVBottomBarFragment.this);
                    if (FVBottomBarFragment.this.scaleForExposurePosition == 1 && FVBottomBarFragment.this.queueExposure.size() > 0) {
                        if (CameraUtils.getCamExposureLengthProgress() != -1) {
                            int unused44 = FVBottomBarFragment.this.queuePositionExposure = CameraUtils.getCamExposureLengthProgress();
                            float unused45 = FVBottomBarFragment.this.zKExposure = (float) FVBottomBarFragment.this.queuePositionExposure;
                            CameraUtils.setCamExposureLengthProgress(-1);
                        } else {
                            int unused46 = FVBottomBarFragment.this.queuePositionExposure = ((Integer) FVBottomBarFragment.this.queueExposure.get(0)).intValue();
                        }
                        if (FVBottomBarFragment.this.queueExposure.size() > 0) {
                            float unused47 = FVBottomBarFragment.this.yK1Exposure = (float) ((0.8d * ((double) FVBottomBarFragment.this.yK1Exposure)) + (0.036d * ((double) FVBottomBarFragment.this.queuePositionExposure)));
                            float unused48 = FVBottomBarFragment.this.zKExposure = FVBottomBarFragment.this.zKExposure + FVBottomBarFragment.this.yK1Exposure;
                            if (FVBottomBarFragment.this.zKExposure > 100.0f) {
                                float unused49 = FVBottomBarFragment.this.zKExposure = 100.0f;
                            }
                            if (FVBottomBarFragment.this.zKExposure < 0.0f) {
                                float unused50 = FVBottomBarFragment.this.zKExposure = 0.0f;
                            }
                            Util.sendIntEventMessge((int) Constants.EXPOSURE_OF_CAMERA, String.valueOf((int) FVBottomBarFragment.this.zKExposure));
                        }
                    } else if (FVBottomBarFragment.this.scaleForExposurePosition < 13) {
                        float unused51 = FVBottomBarFragment.this.yK1Exposure = (float) (0.18d * ((double) FVBottomBarFragment.this.queuePositionExposure));
                        float unused52 = FVBottomBarFragment.this.zKExposure = FVBottomBarFragment.this.zKExposure + FVBottomBarFragment.this.yK1Exposure;
                        if (FVBottomBarFragment.this.zKExposure > 100.0f) {
                            float unused53 = FVBottomBarFragment.this.zKExposure = 100.0f;
                        }
                        if (FVBottomBarFragment.this.zKExposure < 0.0f) {
                            float unused54 = FVBottomBarFragment.this.zKExposure = 0.0f;
                        }
                        Util.sendIntEventMessge((int) Constants.EXPOSURE_OF_CAMERA, String.valueOf((int) FVBottomBarFragment.this.zKExposure));
                    } else {
                        Boolean unused55 = FVBottomBarFragment.this.startSeekThreadExposure300 = false;
                        Util.sendIntEventMessge((int) Constants.EXPOSURE_OF_CAMERA_OVER, String.valueOf((int) FVBottomBarFragment.this.zKExposure));
                    }
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e6) {
                        Thread.currentThread().interrupt();
                    }
                } else if (FVBottomBarFragment.this.startSeekThread.booleanValue()) {
                    FVBottomBarFragment.access$4008(FVBottomBarFragment.this);
                    if (FVBottomBarFragment.this.scaleForlevelPosition == 1 && FVBottomBarFragment.this.queue.size() > 0) {
                        if (CameraUtils.getCamLengthProgress() != -1) {
                            int unused56 = FVBottomBarFragment.this.queuePosition = CameraUtils.getCamLengthProgress() / 10;
                            float unused57 = FVBottomBarFragment.this.f1096zK = (float) FVBottomBarFragment.this.queuePosition;
                            CameraUtils.setCamLengthProgress(-1);
                        } else {
                            int unused58 = FVBottomBarFragment.this.queuePosition = ((Integer) FVBottomBarFragment.this.queue.get(0)).intValue();
                        }
                        if (FVBottomBarFragment.this.queue.size() > 0) {
                            if (CameraUtils.getCameraHandModel()) {
                                if (((Integer) SPUtils.get(FVBottomBarFragment.this.getActivity(), SharePrefConstant.ZOOM_VELOCITY_MEDIUM_OR_SLOW, Integer.valueOf(Constants.ZOOM_VELOCITY_MEDIUM))).intValue() == 107094) {
                                    float unused59 = FVBottomBarFragment.this.yK1 = (float) ((0.9d * ((double) FVBottomBarFragment.this.yK1)) + (3.0000000000000003E-4d * ((double) (FVBottomBarFragment.this.queuePosition - 100))));
                                } else {
                                    float unused60 = FVBottomBarFragment.this.yK1 = (float) ((0.9d * ((double) FVBottomBarFragment.this.yK1)) + (2.0E-4d * ((double) (FVBottomBarFragment.this.queuePosition - 100))));
                                }
                            } else if (((Integer) SPUtils.get(FVBottomBarFragment.this.getActivity(), SharePrefConstant.ZOOM_VELOCITY_MEDIUM_OR_SLOW, Integer.valueOf(Constants.ZOOM_VELOCITY_MEDIUM))).intValue() == 107094) {
                                float unused61 = FVBottomBarFragment.this.yK1 = (float) ((0.9d * ((double) FVBottomBarFragment.this.yK1)) + (3.0000000000000003E-4d * ((double) (FVBottomBarFragment.this.queuePosition - 100))));
                            } else {
                                float unused62 = FVBottomBarFragment.this.yK1 = (float) ((0.9d * ((double) FVBottomBarFragment.this.yK1)) + (2.0E-4d * ((double) (FVBottomBarFragment.this.queuePosition - 100))));
                            }
                            float unused63 = FVBottomBarFragment.this.f1096zK = FVBottomBarFragment.this.f1096zK + FVBottomBarFragment.this.yK1;
                            if (FVBottomBarFragment.this.f1096zK > 100.0f) {
                                float unused64 = FVBottomBarFragment.this.f1096zK = 100.0f;
                            }
                            if (FVBottomBarFragment.this.f1096zK < 0.0f) {
                                float unused65 = FVBottomBarFragment.this.f1096zK = 0.0f;
                            }
                            Util.sendIntEventMessge((int) Constants.LENS_TENSILE_OF_CAMERA, String.valueOf((int) (FVBottomBarFragment.this.f1096zK * 10.0f)));
                            Log.e("------------------", "-------777----  FM200镜头拉伸  FM200镜头拉伸  FM200镜头拉伸 --------");
                        }
                    } else if (FVBottomBarFragment.this.scaleForlevelPosition < 13) {
                        if (CameraUtils.getCameraHandModel()) {
                            if (((Integer) SPUtils.get(FVBottomBarFragment.this.getActivity(), SharePrefConstant.ZOOM_VELOCITY_MEDIUM_OR_SLOW, Integer.valueOf(Constants.ZOOM_VELOCITY_MEDIUM))).intValue() == 107094) {
                                float unused66 = FVBottomBarFragment.this.yK1 = (float) (0.003d * ((double) (FVBottomBarFragment.this.queuePosition - 100)));
                            } else {
                                float unused67 = FVBottomBarFragment.this.yK1 = (float) (0.002d * ((double) (FVBottomBarFragment.this.queuePosition - 100)));
                            }
                        } else if (((Integer) SPUtils.get(FVBottomBarFragment.this.getActivity(), SharePrefConstant.ZOOM_VELOCITY_MEDIUM_OR_SLOW, Integer.valueOf(Constants.ZOOM_VELOCITY_MEDIUM))).intValue() == 107094) {
                            float unused68 = FVBottomBarFragment.this.yK1 = (float) (0.003d * ((double) (FVBottomBarFragment.this.queuePosition - 100)));
                        } else {
                            float unused69 = FVBottomBarFragment.this.yK1 = (float) (0.002d * ((double) (FVBottomBarFragment.this.queuePosition - 100)));
                        }
                        float unused70 = FVBottomBarFragment.this.f1096zK = FVBottomBarFragment.this.f1096zK + FVBottomBarFragment.this.yK1;
                        if (FVBottomBarFragment.this.f1096zK > 100.0f) {
                            float unused71 = FVBottomBarFragment.this.f1096zK = 100.0f;
                        }
                        if (FVBottomBarFragment.this.f1096zK < 0.0f) {
                            float unused72 = FVBottomBarFragment.this.f1096zK = 0.0f;
                        }
                        Util.sendIntEventMessge((int) Constants.LENS_TENSILE_OF_CAMERA, String.valueOf((int) (FVBottomBarFragment.this.f1096zK * 10.0f)));
                        Log.e("------------------", "-------777----  FM200镜头拉伸  FM200镜头拉伸  FM200镜头拉伸 --------");
                    } else {
                        Boolean unused73 = FVBottomBarFragment.this.startSeekThread = false;
                        Util.sendIntEventMessge((int) Constants.LENS_TENSILE_OF_CAMERA_OVER, String.valueOf((int) (FVBottomBarFragment.this.f1096zK * 10.0f)));
                    }
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e7) {
                        Thread.currentThread().interrupt();
                    }
                } else if (FVBottomBarFragment.this.startSeekThreadExposure.booleanValue()) {
                    FVBottomBarFragment.access$5008(FVBottomBarFragment.this);
                    if (FVBottomBarFragment.this.scaleForExposurePosition == 1 && FVBottomBarFragment.this.queueExposure.size() > 0) {
                        if (CameraUtils.getCamExposureLengthProgress() != -1) {
                            int unused74 = FVBottomBarFragment.this.queuePositionExposure = CameraUtils.getCamExposureLengthProgress();
                            float unused75 = FVBottomBarFragment.this.zKExposure = (float) FVBottomBarFragment.this.queuePositionExposure;
                            CameraUtils.setCamExposureLengthProgress(-1);
                        } else {
                            int unused76 = FVBottomBarFragment.this.queuePositionExposure = ((Integer) FVBottomBarFragment.this.queueExposure.get(0)).intValue();
                        }
                        if (FVBottomBarFragment.this.queueExposure.size() > 0) {
                            if (CameraUtils.getCameraHandModel()) {
                                float unused77 = FVBottomBarFragment.this.yK1Exposure = (float) ((0.9d * ((double) FVBottomBarFragment.this.yK1Exposure)) + (6.000000000000001E-4d * ((double) (FVBottomBarFragment.this.queuePositionExposure - 100))));
                            } else {
                                float unused78 = FVBottomBarFragment.this.yK1Exposure = (float) ((0.9d * ((double) FVBottomBarFragment.this.yK1Exposure)) + (3.0000000000000003E-4d * ((double) (FVBottomBarFragment.this.queuePositionExposure - 100))));
                            }
                            float unused79 = FVBottomBarFragment.this.zKExposure = FVBottomBarFragment.this.zKExposure + FVBottomBarFragment.this.yK1Exposure;
                            if (FVBottomBarFragment.this.zKExposure > 100.0f) {
                                float unused80 = FVBottomBarFragment.this.zKExposure = 100.0f;
                            }
                            if (FVBottomBarFragment.this.zKExposure < 0.0f) {
                                float unused81 = FVBottomBarFragment.this.zKExposure = 0.0f;
                            }
                            Util.sendIntEventMessge((int) Constants.EXPOSURE_OF_CAMERA, String.valueOf((int) FVBottomBarFragment.this.zKExposure));
                        }
                    } else if (FVBottomBarFragment.this.scaleForExposurePosition < 13) {
                        if (CameraUtils.getCameraHandModel()) {
                            float unused82 = FVBottomBarFragment.this.yK1Exposure = (float) (0.006d * ((double) (FVBottomBarFragment.this.queuePositionExposure - 100)));
                        } else {
                            float unused83 = FVBottomBarFragment.this.yK1Exposure = (float) (0.003d * ((double) (FVBottomBarFragment.this.queuePositionExposure - 100)));
                        }
                        float unused84 = FVBottomBarFragment.this.zKExposure = FVBottomBarFragment.this.zKExposure + FVBottomBarFragment.this.yK1Exposure;
                        if (FVBottomBarFragment.this.zKExposure > 100.0f) {
                            float unused85 = FVBottomBarFragment.this.zKExposure = 100.0f;
                        }
                        if (FVBottomBarFragment.this.zKExposure < 0.0f) {
                            float unused86 = FVBottomBarFragment.this.zKExposure = 0.0f;
                        }
                        Util.sendIntEventMessge((int) Constants.EXPOSURE_OF_CAMERA, String.valueOf((int) FVBottomBarFragment.this.zKExposure));
                    } else {
                        Boolean unused87 = FVBottomBarFragment.this.startSeekThreadExposure = false;
                        Util.sendIntEventMessge((int) Constants.EXPOSURE_OF_CAMERA_OVER, String.valueOf((int) FVBottomBarFragment.this.zKExposure));
                    }
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e8) {
                        Thread.currentThread().interrupt();
                    }
                } else if (FVBottomBarFragment.this.startHitchCockVideo.booleanValue()) {
                    double startWT = (double) CameraUtils.getHitchCockStartPlaceWT();
                    double startMF2 = CameraUtils.getHitchCockStartPlaceMF();
                    double endWT = (double) CameraUtils.getHitchCockEndPlaceWT();
                    double endMF2 = CameraUtils.getHitchCockEndPlaceMF();
                    double rangeWT = Math.abs(startWT - endWT);
                    double rangeMF = Math.abs(startMF2 - endMF2);
                    int nums = (((int) ((double) CameraUtils.getHitchCockVideoTime())) * 1000) / 30;
                    double rangOneRangeWT = rangeWT / ((double) nums);
                    double rangOneRangeMF = rangeMF / ((double) nums);
                    if (startWT < endWT) {
                        if (FVBottomBarFragment.this.startRealTimeWT < endWT) {
                            double unused88 = FVBottomBarFragment.this.startRealTimeWT = FVBottomBarFragment.this.startRealTimeWT + rangOneRangeWT;
                            if (FVBottomBarFragment.this.cameraManager.getCameraManagerType() == 1) {
                                if (FVBottomBarFragment.this.startRealTimeWT > CameraUtils.getScaleScrollViewTradWTMaxNums()) {
                                    double unused89 = FVBottomBarFragment.this.startRealTimeWT = CameraUtils.getScaleScrollViewTradWTMaxNums();
                                }
                                FVBottomBarFragment.this.sendToHandlerHitchCock(CompanyIdentifierResolver.ETC_SP_ZOO, Float.valueOf((float) ((int) FVBottomBarFragment.this.startRealTimeWT)));
                            } else {
                                if (FVBottomBarFragment.this.startRealTimeWT > CameraUtils.getScaleScrollViewWTMaxNums() + 1.0d) {
                                    double unused90 = FVBottomBarFragment.this.startRealTimeWT = CameraUtils.getScaleScrollViewWTMaxNums() + 1.0d;
                                }
                                FVBottomBarFragment.this.sendToHandlerHitchCock(CompanyIdentifierResolver.ETC_SP_ZOO, Float.valueOf((float) FVBottomBarFragment.this.startRealTimeWT));
                            }
                        } else {
                            Boolean unused91 = FVBottomBarFragment.this.startHitchCockVideoWT = false;
                        }
                    } else if (startWT == endWT) {
                        Boolean unused92 = FVBottomBarFragment.this.startHitchCockVideoWT = false;
                    } else if (FVBottomBarFragment.this.startRealTimeWT > endWT) {
                        double unused93 = FVBottomBarFragment.this.startRealTimeWT = FVBottomBarFragment.this.startRealTimeWT - rangOneRangeWT;
                        if (FVBottomBarFragment.this.cameraManager.getCameraManagerType() == 1) {
                            if (FVBottomBarFragment.this.startRealTimeWT < 0.0d) {
                                double unused94 = FVBottomBarFragment.this.startRealTimeWT = 0.0d;
                            }
                            FVBottomBarFragment.this.sendToHandlerHitchCock(CompanyIdentifierResolver.ETC_SP_ZOO, Float.valueOf((float) ((int) FVBottomBarFragment.this.startRealTimeWT)));
                        } else {
                            if (FVBottomBarFragment.this.startRealTimeWT < 1.0d) {
                                double unused95 = FVBottomBarFragment.this.startRealTimeWT = 1.0d;
                            }
                            FVBottomBarFragment.this.sendToHandlerHitchCock(CompanyIdentifierResolver.ETC_SP_ZOO, Float.valueOf((float) FVBottomBarFragment.this.startRealTimeWT));
                        }
                    } else {
                        Boolean unused96 = FVBottomBarFragment.this.startHitchCockVideoWT = false;
                    }
                    if (!CameraUtils.getHitchCockStartPlaceStringMF().equals("AF")) {
                        if (startMF2 < endMF2) {
                            if (FVBottomBarFragment.this.startRealTimeMF < endMF2) {
                                double unused97 = FVBottomBarFragment.this.startRealTimeMF = FVBottomBarFragment.this.startRealTimeMF + rangOneRangeMF;
                                if (FVBottomBarFragment.this.startRealTimeMF > CameraUtils.getScaleScrollViewMFMaxNums()) {
                                    double unused98 = FVBottomBarFragment.this.startRealTimeMF = CameraUtils.getScaleScrollViewMFMaxNums();
                                }
                                FVBottomBarFragment.this.sendToHandlerHitchCock(320, Float.valueOf((float) FVBottomBarFragment.this.startRealTimeMF));
                            } else {
                                Boolean unused99 = FVBottomBarFragment.this.startHitchCockVideoMF = false;
                            }
                        } else if (startMF2 == endMF2) {
                            Boolean unused100 = FVBottomBarFragment.this.startHitchCockVideoMF = false;
                        } else if (FVBottomBarFragment.this.startRealTimeMF > endMF2) {
                            double unused101 = FVBottomBarFragment.this.startRealTimeMF = FVBottomBarFragment.this.startRealTimeMF - rangOneRangeMF;
                            if (FVBottomBarFragment.this.startRealTimeMF < 0.0d) {
                                double unused102 = FVBottomBarFragment.this.startRealTimeMF = 0.0d;
                            }
                            FVBottomBarFragment.this.sendToHandlerHitchCock(320, Float.valueOf((float) FVBottomBarFragment.this.startRealTimeMF));
                        } else {
                            Boolean unused103 = FVBottomBarFragment.this.startHitchCockVideoMF = false;
                        }
                    }
                    if (!FVBottomBarFragment.this.startHitchCockVideoWT.booleanValue() && !FVBottomBarFragment.this.startHitchCockVideoMF.booleanValue()) {
                        Boolean unused104 = FVBottomBarFragment.this.startHitchCockVideo = false;
                        Util.sendIntEventMessge(Constants.CAMERA_HITCH_COCK_ZOOM_RESTART_TWO);
                        Log.e("----------------", "----------   6666  7777  8888  9999  0000   false  false  false  false  false  ");
                    }
                    if (FVBottomBarFragment.this.cameraManager.getCameraManagerType() != 2) {
                        if (!FVBottomBarFragment.this.startHitchCockVideoWT.booleanValue()) {
                            Boolean unused105 = FVBottomBarFragment.this.startHitchCockVideo = false;
                            Util.sendIntEventMessge(Constants.CAMERA_HITCH_COCK_ZOOM_RESTART_ONE);
                        }
                        Log.e("----------------", "----------   6666  7777  8888  9999  0000  cameraOne  false  false  false  false  false  ");
                    } else if (FVCameraManager.GetCameraLevel(FVBottomBarFragment.this.mContext) == 2) {
                        if (!FVBottomBarFragment.this.startHitchCockVideoWT.booleanValue()) {
                            Boolean unused106 = FVBottomBarFragment.this.startHitchCockVideo = false;
                            Util.sendIntEventMessge(Constants.CAMERA_HITCH_COCK_ZOOM_RESTART_TWO);
                        }
                    } else if (!FVBottomBarFragment.this.startHitchCockVideoWT.booleanValue()) {
                        Boolean unused107 = FVBottomBarFragment.this.startHitchCockVideo = false;
                        Util.sendIntEventMessge(Constants.CAMERA_HITCH_COCK_ZOOM_RESTART_TWO);
                        Log.e("----------------", "----------   6666  7777  8888  9999  0000  cameraTwo  false  false  false  false  false  ");
                    }
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e9) {
                        Thread.currentThread().interrupt();
                    }
                } else if (FVBottomBarFragment.this.startMarkPointMfChangeTimeHalfSecondBoo.booleanValue()) {
                    float markPointMfB = CameraUtils.getLlMarkPointMfB();
                    float markPointMfA = CameraUtils.getLlMarkPointMfA();
                    if (FVBottomBarFragment.this.startMarkPointAToB.booleanValue()) {
                        startMF = (double) markPointMfA;
                        endMF = (double) markPointMfB;
                    } else {
                        startMF = (double) markPointMfB;
                        endMF = (double) markPointMfA;
                    }
                    double rangOneRangeMF2 = Math.abs(startMF - endMF) / ((double) 16);
                    if (startMF < endMF) {
                        double unused108 = FVBottomBarFragment.this.startMarkPointMFHalfSecondValue = FVBottomBarFragment.this.startMarkPointMFHalfSecondValue + rangOneRangeMF2;
                        if (FVBottomBarFragment.this.startMarkPointMFHalfSecondValue > endMF) {
                            double unused109 = FVBottomBarFragment.this.startMarkPointMFHalfSecondValue = endMF;
                            Boolean unused110 = FVBottomBarFragment.this.startMarkPointMfChangeTimeHalfSecondBoo = false;
                            Boolean unused111 = FVBottomBarFragment.this.startMarkPointMfAToB = false;
                            Boolean unused112 = FVBottomBarFragment.this.startMarkPointMfBToA = false;
                        }
                        FVBottomBarFragment.this.sendToHandlerHitchCock(337, Float.valueOf((float) FVBottomBarFragment.this.startMarkPointMFHalfSecondValue));
                    } else if (startMF >= endMF) {
                        double unused113 = FVBottomBarFragment.this.startMarkPointMFHalfSecondValue = FVBottomBarFragment.this.startMarkPointMFHalfSecondValue - rangOneRangeMF2;
                        if (FVBottomBarFragment.this.startMarkPointMFHalfSecondValue < endMF) {
                            double unused114 = FVBottomBarFragment.this.startMarkPointMFHalfSecondValue = endMF;
                            Boolean unused115 = FVBottomBarFragment.this.startMarkPointMfChangeTimeHalfSecondBoo = false;
                            Boolean unused116 = FVBottomBarFragment.this.startMarkPointMfAToB = false;
                            Boolean unused117 = FVBottomBarFragment.this.startMarkPointMfBToA = false;
                        }
                        FVBottomBarFragment.this.sendToHandlerHitchCock(337, Float.valueOf((float) FVBottomBarFragment.this.startMarkPointMFHalfSecondValue));
                    }
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e10) {
                        Thread.currentThread().interrupt();
                    }
                } else if (FVBottomBarFragment.this.startMarkPointMfAToB.booleanValue()) {
                    float markPointMfB2 = CameraUtils.getLlMarkPointMfB();
                    double llMarkPointMfA = (double) CameraUtils.getLlMarkPointMfA();
                    double endMF3 = (double) markPointMfB2;
                    double markPointMfRatio = 0.99d;
                    int changeSmooth = ((Integer) SPUtils.get(FVBottomBarFragment.this.getActivity(), SharePrefConstant.CAMERA_MARK_POINT_CHANGE_SMOOTH, 0)).intValue();
                    if (changeSmooth == 0) {
                        markPointMfRatio = 0.99d;
                    } else if (changeSmooth == 1) {
                        markPointMfRatio = 0.985d;
                    } else if (changeSmooth == 2) {
                        markPointMfRatio = 0.98d;
                    } else if (changeSmooth == 3) {
                        markPointMfRatio = 0.975d;
                    } else if (changeSmooth == 4) {
                        markPointMfRatio = 0.97d;
                    } else if (changeSmooth == 5) {
                        markPointMfRatio = 0.965d;
                    }
                    if (FVBottomBarFragment.this.startMarkPointAMF < endMF3) {
                        double unused118 = FVBottomBarFragment.this.startMarkPointAMF = FVBottomBarFragment.this.startMarkPointAMF * markPointMfRatio;
                        double unused119 = FVBottomBarFragment.this.startMarkPointAMF = FVBottomBarFragment.this.startMarkPointAMF + ((1.0d - markPointMfRatio) * endMF3);
                        if (FVBottomBarFragment.this.startMarkPointAMF > CameraUtils.getScaleScrollViewMFMaxNums()) {
                            double unused120 = FVBottomBarFragment.this.startMarkPointAMF = CameraUtils.getScaleScrollViewMFMaxNums();
                            Boolean unused121 = FVBottomBarFragment.this.startMarkPointMfAToB = false;
                            Boolean unused122 = FVBottomBarFragment.this.startMarkPointMfBToA = false;
                            Boolean unused123 = FVBottomBarFragment.this.startMarkPointMfChangeTimeHalfSecondBoo = false;
                            Boolean unused124 = FVBottomBarFragment.this.startMarkPointMfAToB = false;
                            Boolean unused125 = FVBottomBarFragment.this.startMarkPointMfBToA = false;
                        }
                        FVBottomBarFragment.this.sendToHandlerHitchCock(337, Float.valueOf((float) FVBottomBarFragment.this.startMarkPointAMF));
                    } else {
                        double unused126 = FVBottomBarFragment.this.startMarkPointAMF = FVBottomBarFragment.this.startMarkPointAMF * markPointMfRatio;
                        double unused127 = FVBottomBarFragment.this.startMarkPointAMF = FVBottomBarFragment.this.startMarkPointAMF + ((1.0d - markPointMfRatio) * endMF3);
                        if (FVBottomBarFragment.this.startMarkPointAMF < 0.0d) {
                            double unused128 = FVBottomBarFragment.this.startMarkPointAMF = 0.0d;
                            Boolean unused129 = FVBottomBarFragment.this.startMarkPointMfAToB = false;
                            Boolean unused130 = FVBottomBarFragment.this.startMarkPointMfBToA = false;
                            Boolean unused131 = FVBottomBarFragment.this.startMarkPointMfChangeTimeHalfSecondBoo = false;
                        }
                        FVBottomBarFragment.this.sendToHandlerHitchCock(337, Float.valueOf((float) FVBottomBarFragment.this.startMarkPointAMF));
                    }
                    if (Math.abs(FVBottomBarFragment.this.startMarkPointAMF - endMF3) < 0.015d) {
                        Boolean unused132 = FVBottomBarFragment.this.startMarkPointMfAToB = false;
                        Boolean unused133 = FVBottomBarFragment.this.startMarkPointMfBToA = false;
                        Boolean unused134 = FVBottomBarFragment.this.startMarkPointMfChangeTimeHalfSecondBoo = false;
                        FVBottomBarFragment.this.sendToHandlerHitchCock(338, String.valueOf(markPointMfB2));
                    }
                    try {
                        Thread.sleep((long) (10 - ((Integer) SPUtils.get(FVBottomBarFragment.this.getActivity(), SharePrefConstant.CAMERA_MARK_POINT_CHANGE_SPEED, 3)).intValue()));
                    } catch (InterruptedException e11) {
                        Thread.currentThread().interrupt();
                    }
                } else if (FVBottomBarFragment.this.startMarkPointMfBToA.booleanValue()) {
                    float markPointMfA2 = CameraUtils.getLlMarkPointMfA();
                    double llMarkPointMfB = (double) CameraUtils.getLlMarkPointMfB();
                    double endMF4 = (double) markPointMfA2;
                    double markPointMfRatio2 = 0.99d;
                    int changeSmooth2 = ((Integer) SPUtils.get(FVBottomBarFragment.this.getActivity(), SharePrefConstant.CAMERA_MARK_POINT_CHANGE_SMOOTH, 0)).intValue();
                    if (changeSmooth2 == 0) {
                        markPointMfRatio2 = 0.99d;
                    } else if (changeSmooth2 == 1) {
                        markPointMfRatio2 = 0.985d;
                    } else if (changeSmooth2 == 2) {
                        markPointMfRatio2 = 0.98d;
                    } else if (changeSmooth2 == 3) {
                        markPointMfRatio2 = 0.975d;
                    } else if (changeSmooth2 == 4) {
                        markPointMfRatio2 = 0.97d;
                    } else if (changeSmooth2 == 5) {
                        markPointMfRatio2 = 0.965d;
                    }
                    if (FVBottomBarFragment.this.startMarkPointAMF < endMF4) {
                        double unused135 = FVBottomBarFragment.this.startMarkPointAMF = FVBottomBarFragment.this.startMarkPointAMF * markPointMfRatio2;
                        double unused136 = FVBottomBarFragment.this.startMarkPointAMF = FVBottomBarFragment.this.startMarkPointAMF + ((1.0d - markPointMfRatio2) * endMF4);
                        if (FVBottomBarFragment.this.startMarkPointAMF > CameraUtils.getScaleScrollViewMFMaxNums()) {
                            double unused137 = FVBottomBarFragment.this.startMarkPointAMF = CameraUtils.getScaleScrollViewMFMaxNums();
                            Boolean unused138 = FVBottomBarFragment.this.startMarkPointMfBToA = false;
                            Boolean unused139 = FVBottomBarFragment.this.startMarkPointMfAToB = false;
                            Boolean unused140 = FVBottomBarFragment.this.startMarkPointMfChangeTimeHalfSecondBoo = false;
                        }
                        FVBottomBarFragment.this.sendToHandlerHitchCock(337, Float.valueOf((float) FVBottomBarFragment.this.startMarkPointAMF));
                    } else {
                        double unused141 = FVBottomBarFragment.this.startMarkPointAMF = FVBottomBarFragment.this.startMarkPointAMF * markPointMfRatio2;
                        double unused142 = FVBottomBarFragment.this.startMarkPointAMF = FVBottomBarFragment.this.startMarkPointAMF + ((1.0d - markPointMfRatio2) * endMF4);
                        if (FVBottomBarFragment.this.startMarkPointAMF < 0.0d) {
                            double unused143 = FVBottomBarFragment.this.startMarkPointAMF = 0.0d;
                            Boolean unused144 = FVBottomBarFragment.this.startMarkPointMfBToA = false;
                            Boolean unused145 = FVBottomBarFragment.this.startMarkPointMfAToB = false;
                            Boolean unused146 = FVBottomBarFragment.this.startMarkPointMfChangeTimeHalfSecondBoo = false;
                        }
                        FVBottomBarFragment.this.sendToHandlerHitchCock(337, Float.valueOf((float) FVBottomBarFragment.this.startMarkPointAMF));
                    }
                    if (Math.abs(FVBottomBarFragment.this.startMarkPointAMF - endMF4) < 0.015d) {
                        Boolean unused147 = FVBottomBarFragment.this.startMarkPointMfBToA = false;
                        Boolean unused148 = FVBottomBarFragment.this.startMarkPointMfAToB = false;
                        Boolean unused149 = FVBottomBarFragment.this.startMarkPointMfChangeTimeHalfSecondBoo = false;
                        FVBottomBarFragment.this.sendToHandlerHitchCock(338, String.valueOf(markPointMfA2));
                    }
                    try {
                        Thread.sleep((long) (10 - ((Integer) SPUtils.get(FVBottomBarFragment.this.getActivity(), SharePrefConstant.CAMERA_MARK_POINT_CHANGE_SPEED, 3)).intValue()));
                    } catch (InterruptedException e12) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void sinleTask() {
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                int result = msg.getData().getInt("dispose", -1);
                if (result == 0) {
                    Util.sendIntEventMessge(Constants.FULL_SHOT_SYNTHESIS_END);
                    ViseLog.m1466e("全景图片合成成功666");
                    Toast.makeText(FVBottomBarFragment.this.getActivity(), C0853R.string.label_full_shot_compound_success, 1).show();
                    if (FVBottomBarFragment.this.btnIconShow != null) {
                        Glide.with(FVBottomBarFragment.this.getActivity()).load(FVBottomBarFragment.this.panoramaPath).error((int) C0853R.mipmap.ic_gallery_empty).into(FVBottomBarFragment.this.btnIconShow);
                    }
                    Util.updateGallery(FVBottomBarFragment.this.getContext(), FVBottomBarFragment.this.panoramaPath, "image");
                    if (FVBottomBarFragment.this.mHandler != null) {
                        while (FVBottomBarFragment.this.mHandler.hasMessages(FVBottomBarFragment.PANORAMA_MODE_OVER_TIMEOUT)) {
                            FVBottomBarFragment.this.mHandler.removeMessages(FVBottomBarFragment.PANORAMA_MODE_OVER_TIMEOUT);
                        }
                    }
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            if (FVBottomBarFragment.this.panoramaPath != null) {
                                List listItemPathLook = new ArrayList();
                                listItemPathLook.add(FVBottomBarFragment.this.panoramaPath);
                                FVLookAllSceneryPhotoDialog unused = FVBottomBarFragment.this.lookSceneryPhotoDialog = new FVLookAllSceneryPhotoDialog(FVBottomBarFragment.this.mContext, (ArrayList) listItemPathLook);
                                FVBottomBarFragment.this.lookSceneryPhotoDialog.show();
                            }
                        }
                    }, 500);
                } else if (result == -1) {
                    CameraUtils.setFullCameraErrorCode(BleConstant.FOCUS);
                    Util.sendIntEventMessge(Constants.FULL_SHOT_SYNTHESIS_FAILED);
                    ViseLog.m1466e("全景图片合成失败555");
                    Toast.makeText(FVBottomBarFragment.this.getActivity(), FVBottomBarFragment.this.getString(C0853R.string.label_full_shot_compound_failed) + CameraUtils.getFullCameraErrorCode(), 1).show();
                    Log.e("------------", "-------- 全景拍照  999999 ----");
                } else if (result == -2) {
                    CameraUtils.setFullCameraErrorCode("5");
                    Util.sendIntEventMessge(Constants.FULL_SHOT_SYNTHESIS_FAILED);
                    ViseLog.m1466e("全景图片合成失败555");
                    Toast.makeText(FVBottomBarFragment.this.getActivity(), FVBottomBarFragment.this.getString(C0853R.string.label_full_shot_compound_failed) + CameraUtils.getFullCameraErrorCode(), 1).show();
                    Log.e("------------", "-------- 全景拍照  999999  超时 ----");
                } else {
                    CameraUtils.setFullCameraErrorCode("6");
                    Util.sendIntEventMessge(Constants.FULL_SHOT_SYNTHESIS_FAILED);
                    ViseLog.m1466e("全景图片合成失败555");
                    Toast.makeText(FVBottomBarFragment.this.getActivity(), FVBottomBarFragment.this.getString(C0853R.string.label_full_shot_compound_failed) + CameraUtils.getFullCameraErrorCode(), 1).show();
                    Log.e("------------", "-------- 全景拍照  999999  照片数量不符 ----");
                }
            }
        };
        new Thread(new Runnable() {
            public void run() {
                ViseLog.m1466e("进入图片处理阶段" + FVBottomBarFragment.this.list.size() + "aaa线程id" + Thread.currentThread().getId());
                int fitCount = 0;
                ArrayList<String> mList = new ArrayList<>();
                mList.addAll(FVBottomBarFragment.this.list);
                FVBottomBarFragment.this.list.clear();
                if (mList != null && mList.size() > 0) {
                    int i = 0;
                    while (i < mList.size()) {
                        String filePath = mList.get(i);
                        if (filePath != null) {
                            ViseLog.m1466e(Progress.FILE_PATH + filePath);
                            boolean matching = LoadOpenCV.xStitcher.addImageAndMatching(filePath, i);
                            ViseLog.m1466e("单张图片处理返回结果" + matching + "当前处理张数" + fitCount);
                            if (fitCount == 0) {
                                FVBottomBarFragment.this.cameraManager.lockAutoExposure(false);
                            }
                            if (matching) {
                                fitCount++;
                                if (fitCount == mList.size()) {
                                    String unused = FVBottomBarFragment.this.panoramaPath = Util.getOutputMediaFile(FVBottomBarFragment.this.mContext);
                                    ViseLog.m1466e("进入图片合成最终阶段及合成路径" + FVBottomBarFragment.this.panoramaPath);
                                    int stitching = -1;
                                    try {
                                        stitching = LoadOpenCV.xStitcher.stitching(FVBottomBarFragment.this.panoramaPath);
                                    } catch (Exception e) {
                                        Log.d("stitching", "stitching is gg: " + e.getMessage());
                                    } finally {
                                        FVBottomBarFragment.this.sendDisposeResult(handler, stitching);
                                    }
                                }
                                i++;
                            } else {
                                Log.e(FVBottomBarFragment.TAG, "run: 单张合成失败");
                                FVBottomBarFragment.this.sendDisposeResult(handler, -1);
                                return;
                            }
                        } else {
                            FVBottomBarFragment.this.sendDisposeResult(handler, -1);
                            return;
                        }
                    }
                }
            }
        }).start();
    }

    private void setAutoFocusModeNewCameraOpen() {
        if (this.cameraManager.getCameraManagerType() == 1) {
            this.cameraManager.cancelAutoFocus();
        } else {
            this.cameraManager.cancelAutoFocus(new Camera2Manager.AutoFocusListener() {
                public void focusLocked() {
                    Log.v("Camera2", "cancelAutoFocusLock locked");
                }

                public void focusUnlocked() {
                    Log.v("Camera2", "cancelAutoFocusLock unlocked");
                }
            });
        }
    }

    private void setAutoFocusModeNewCameraClose() {
        if (this.cameraManager.getCameraManagerType() == 1) {
            this.cameraManager.autoFocus(calculateTapAreaCenter((float) (this.widthPingMu / 2), (float) (this.heightPingMu / 2), 1.0f), (Camera.AutoFocusCallback) null);
            return;
        }
        this.cameraManager.autoFocus((double) (this.widthPingMu / 2), (double) (this.heightPingMu / 2), false, new Camera2Manager.AutoFocusListener() {
            public void focusLocked() {
                Log.v("TEST", "autoFocus locked");
            }

            public void focusUnlocked() {
                Log.v("TEST", "autoFocus unlocked");
            }
        });
    }

    /* access modifiers changed from: private */
    public void sendDisposeResult(Handler handler, int result) {
        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putInt("dispose", result);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0853R.C0855id.btn_photo_mode:
                changeCamera2PhotoMode();
                return;
            case C0853R.C0855id.btn_video_mode:
                changeCamera2VideoMode();
                return;
            case C0853R.C0855id.btn_flash_switch:
                this.btn_flash_switch_status.setVisibility(0);
                this.llLayout.setBackground(getResources().getDrawable(C0853R.C0854drawable.sp_white_round5_bg));
                final FVFlashPop fvFlashPop = new FVFlashPop();
                fvFlashPop.init(getActivity());
                this.popupWindow = new PopupWindow(fvFlashPop.getView(), -2, -2, true);
                this.popupWindow.setBackgroundDrawable(new ColorDrawable(0));
                this.popupWindow.setAnimationStyle(C0853R.style.popAnimation6);
                this.popupWindow.setOutsideTouchable(true);
                this.popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    public void onDismiss() {
                        fvFlashPop.unRegisterListener();
                        FVBottomBarFragment.this.llLayout.setBackground(FVBottomBarFragment.this.getResources().getDrawable(C0853R.C0854drawable.sp_white50_round5_bg));
                        FVBottomBarFragment.this.btn_flash_switch_status.setVisibility(8);
                    }
                });
                int[] llLocation = new int[2];
                this.llLayout.getLocationOnScreen(llLocation);
                int popWidth = Util.dip2px(getActivity(), 177.0f);
                int popHeight = Util.dip2px(getActivity(), 44.0f);
                int x = llLocation[0] - popWidth;
                int singleItemWidth = this.llLayout.getHeight() / 5;
                int y = llLocation[1] + (singleItemWidth * 4) + ((singleItemWidth - popHeight) / 2);
                this.popupWindow.setFocusable(false);
                this.popupWindow.update();
                this.popupWindow.showAtLocation(this.btnFlashSwitch, 0, x, y);
                this.popupWindow.setFocusable(true);
                this.popupWindow.update();
                Util.fullScreenImmersive(this.popupWindow.getContentView());
                return;
            case C0853R.C0855id.btn_lens_switch:
                switchCameraLens();
                return;
            case C0853R.C0855id.btn_photo_video:
                takePhotoOrVideo();
                return;
            case C0853R.C0855id.btn_follow:
                if (!this.connected) {
                    Toast.makeText(getActivity(), C0853R.string.label_device_not_connected, 0).show();
                    return;
                } else if (!Util.isPovTracking(getActivity()) && !CameraExclusiveUtils.openFollowExclusive(getActivity())) {
                    if (!this.isFollowMode) {
                        this.isFollowMode = true;
                        if (LoadOpenCV.LoadOpenCVSccuss) {
                            MoveTimelapseUtil.getInstance();
                            if (MoveTimelapseUtil.getCameraFvShareSleep() == 1) {
                                EventBusUtil.sendEvent(new Event(153));
                                return;
                            }
                            this.btnFollow.setImageResource(C0853R.mipmap.ic_follow);
                            Util.sendIntEventMessge(Constants.OPEN_KCF);
                            CameraUtils.setFollowIng(true);
                            this.btnLensSwitch.setEnabled(false);
                            this.btnLensSwitch.setVisibility(8);
                            setCameraHandModelUIVisibleOrGone(true);
                            if (CameraUtils.getCurrentPageIndex() == 2) {
                                toastAboutMarkPointCancelTV();
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    BleByteUtil.setPTZParameters((byte) 21, (byte) 0, (byte) 0);
                    this.mHandler.sendEmptyMessageDelayed(CLOSE_FOLLOW_MODE_TIMEOUT, 1000);
                    return;
                } else {
                    return;
                }
            case C0853R.C0855id.btn_icon_show:
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    CameraUtils.setFrameLayerNumber(0);
                    CameraUtils.setLabelTopBarSelect(-1);
                    Util.sendIntEventMessge(Constants.LABEL_CAMERA_POP_DISMISS_KEY_210);
                    Util.sendIntEventMessge(Constants.CAMERA_MARK_POINT_QUIT_OUT);
                    CameraUtils.setLabelTopBarSelectMemory(-1);
                }
                startActivity(new Intent(getActivity(), FVCameraFileTwoActivity.class));
                this.hasGotoFile = true;
                return;
            case C0853R.C0855id.btn_common_pop:
                showCommonPop();
                return;
            case C0853R.C0855id.btn_open_beauty:
                showBeautyPop();
                return;
            case C0853R.C0855id.btn_camera_hand_model_shortcut:
                Util.sendIntEventMessge(Constants.LABEL_CAMERA_HAND_MODEL_VISIBLE_OR_GONE);
                return;
            default:
                return;
        }
    }

    private void toastAboutMarkPointCancelTV() {
        if (CameraUtils.getCurrentPageIndex() == 2 && CameraUtils.getMarkPointUIIsVisible()) {
            EventBusUtil.sendEvent(new Event(145));
        }
    }

    /* access modifiers changed from: private */
    public void setCameraHandModelUIVisibleOrGone(boolean boo) {
        if (boo) {
            if (((Integer) SPUtils.get(this.mContext, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE))).intValue() != 107212) {
                Util.sendIntEventMessge(Constants.LABEL_CAMERA_HAND_MODEL_GONE);
            }
        } else if (((Integer) SPUtils.get(this.mContext, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE))).intValue() != 107212 && MoveTimelapseUtil.getInstance().getCameraProgressLinear() == 0) {
            Util.sendIntEventMessge(Constants.LABEL_CAMERA_HAND_MODEL_VISIBLE);
        }
    }

    private void showMainSearchPop(int type) {
        final FVMainForSearchBluetooth fvMainForSearchBluetooth = new FVMainForSearchBluetooth();
        fvMainForSearchBluetooth.init(getActivity(), getActivity(), type);
        PopupWindow popupWindow2 = new PopupWindow(fvMainForSearchBluetooth.getView(), Util.dip2px(getActivity(), 330.0f), Util.dip2px(getActivity(), 330.0f), true);
        fvMainForSearchBluetooth.setPop(popupWindow2, fvMainForSearchBluetooth);
        popupWindow2.setBackgroundDrawable(new ColorDrawable(0));
        popupWindow2.setOutsideTouchable(true);
        popupWindow2.setAnimationStyle(C0853R.style.popAnimation);
        popupWindow2.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                ViseLog.m1466e("bluetooth pop dismiss");
                fvMainForSearchBluetooth.unRegisterEvent();
            }
        });
        popupWindow2.showAtLocation(fvMainForSearchBluetooth.getView(), 17, 0, 0);
    }

    private void scanBluetooth() {
        if (BleUtil.isBleEnable(getActivity())) {
            showMainSearchPop(1);
        } else {
            BleUtil.enableBluetooth(getActivity(), 3);
        }
    }

    private void takePhotoOrVideo() {
        String highSpeed;
        if (!this.isPhotoMode) {
            if (CameraUtils.isFollowIng()) {
                if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
                    highSpeed = SPUtils.get(getActivity(), SharePrefConstant.FRONT_HIGH_SPEED_VIDEO_RESOLUTION, "").toString();
                } else {
                    highSpeed = SPUtils.get(getActivity(), SharePrefConstant.HIGH_SPEED_VIDEO_RESOLUTION, "").toString();
                }
                if (!"".equals(highSpeed) && !this.cameraManager.isMediaRecording()) {
                    Toast.makeText(getActivity(), C0853R.string.camera_is_following_to_high_speed_video_resolution, 0).show();
                    return;
                }
            }
            record();
        } else if (!FileUtils.takePhotoMemoryEnough(getActivity())) {
            Toast.makeText(getActivity(), C0853R.string.label_available_memory_not_enough, 0).show();
        } else {
            int delay = ((Integer) SPUtils.get(getActivity(), SharePrefConstant.DELAY_TAKE_PHOTO_MODE, Integer.valueOf(Constants.DELAY_TAKE_PHOTO_0S))).intValue();
            int longExposure = ((Integer) SPUtils.get(getActivity(), SharePrefConstant.LONG_EXPOSURE_MODE, Integer.valueOf(Constants.LONG_EXPOSURE_NONE))).intValue();
            int fullMode = ((Integer) SPUtils.get(getActivity(), SharePrefConstant.FULL_SHOT, Integer.valueOf(Constants.FULL_SHOT_NONE))).intValue();
            if (fullMode == 10024) {
                if (delay != 100011) {
                    Util.sendIntEventMessge(Constants.SHOW_TIME_DELAY_ANIMATION);
                    setBtnPhotoVideoEnable(false);
                    setChangePhotoVideoModeEnable(false);
                } else if (longExposure == 106205) {
                    takePhotoNew();
                } else if (this.startLongExposure) {
                    dealUnclickableMode();
                    Util.sendIntEventMessge(Constants.START_TAKE_PHOTO_SHADOW);
                    this.btnPhotoVideo.setImageResource(C0853R.mipmap.ic_long_exposure_ing);
                    this.cameraManager.startSlowShutter();
                    this.startLongExposure = false;
                    CameraUtils.setLongExposureIng(true);
                } else {
                    recoverUnclickMode();
                    this.btnPhotoVideo.setImageDrawable(getResources().getDrawable(C0853R.C0854drawable.sl_photo_mode));
                    this.cameraManager.stopSlowShutter();
                    this.startLongExposure = true;
                    CameraUtils.setLongExposureIng(false);
                }
            } else if (fullMode == 10025) {
                ViseLog.m1466e("aaa");
                if (!this.connected) {
                    Toast.makeText(getActivity(), C0853R.string.label_please_connect_device, 0).show();
                } else if (LoadOpenCV.LoadOpenCVSccuss && Util.isPovReverPano(getActivity()) && cameraVerticalPanoramicCancel(getActivity())) {
                    this.takedPictrueCount = 0;
                    this.requreTakePictrueCount = 5;
                    Util.sendIntEventMessge(Constants.FULL_SHOT_START);
                    dealUnclickableMode();
                    LoadOpenCV.xStitcher.setScanType(0);
                    sendPanormaStart(1);
                }
            } else if (fullMode == 10026) {
                ViseLog.m1466e("bbb");
                if (!this.connected) {
                    Toast.makeText(getActivity(), C0853R.string.label_please_connect_device, 0).show();
                } else if (LoadOpenCV.LoadOpenCVSccuss && Util.isPovReverPano(getActivity()) && cameraVerticalPanoramicCancel(getActivity())) {
                    this.takedPictrueCount = 0;
                    this.requreTakePictrueCount = 7;
                    Util.sendIntEventMessge(Constants.FULL_SHOT_START);
                    dealUnclickableMode();
                    LoadOpenCV.xStitcher.setScanType(6);
                    sendPanormaStart(2);
                }
            } else if (fullMode == 10027) {
                ViseLog.m1466e("ccc");
                if (!this.connected) {
                    Toast.makeText(getActivity(), C0853R.string.label_please_connect_device, 0).show();
                } else if (LoadOpenCV.LoadOpenCVSccuss && Util.isPovReverPano(getActivity()) && cameraVerticalPanoramicCancel(getActivity())) {
                    this.takedPictrueCount = 0;
                    this.requreTakePictrueCount = 9;
                    Util.sendIntEventMessge(Constants.FULL_SHOT_START);
                    dealUnclickableMode();
                    LoadOpenCV.xStitcher.setScanType(2);
                    sendPanormaStart(3);
                }
            } else if (fullMode == 10028) {
                ViseLog.m1466e("ddd");
                if (!this.connected) {
                    Toast.makeText(getActivity(), C0853R.string.label_please_connect_device, 0).show();
                } else if (LoadOpenCV.LoadOpenCVSccuss && Util.isPovReverPano(getActivity()) && cameraVerticalPanoramicCancel(getActivity())) {
                    this.takedPictrueCount = 0;
                    this.requreTakePictrueCount = 15;
                    Util.sendIntEventMessge(Constants.FULL_SHOT_START);
                    dealUnclickableMode();
                    LoadOpenCV.xStitcher.setScanType(5);
                    sendPanormaStart(4);
                }
            }
        }
    }

    private static boolean cameraVerticalPanoramicCancel(Context mContext2) {
        int angle = ScreenOrientationUtil.getInstance().getOrientation();
        if (CameraUtils.getCurrentPageIndex() == 0) {
            return true;
        }
        if (angle != 90 && angle != 270) {
            return true;
        }
        Util.sendIntEventMessge(Constants.LABEL_CAMERA_VERTICAL_PANORAMIC_CANCEL);
        Toast.makeText(mContext2, C0853R.string.label_camera_vertical_panoramic_cancel, 0).show();
        return false;
    }

    /* access modifiers changed from: private */
    public void sendPanormaStart(int type) {
        ViseLog.m1466e("全景初始化" + type);
        BleByteUtil.setPanoramaStart((byte) 34, (byte) type, (byte) 0);
        this.mHandler.sendEmptyMessageDelayed(PANORAMA_MODE_START_TIMEOUT, 1000);
    }

    /* access modifiers changed from: private */
    public void takePhoto() {
        if (!FileUtils.takePhotoMemoryEnough(getActivity())) {
            Toast.makeText(getActivity(), C0853R.string.label_available_memory_not_enough, 0).show();
            return;
        }
        int filterMode = ((Integer) SPUtils.get(getActivity(), SharePrefConstant.FILTER_MODE, Integer.valueOf(Constants.FILTER_NONE_MODE))).intValue();
        int beautyMode = ((Integer) SPUtils.get(getActivity(), SharePrefConstant.BEAUTY_MODE, Integer.valueOf(Constants.BEAUTY_CLOSE))).intValue();
        if (!(filterMode == 10300 && beautyMode == 10401)) {
            setBtnPhotoVideoEnable(false);
            setChangePhotoVideoModeEnable(false);
            setCameraLensEnable(false);
        }
        Util.sendIntEventMessge(Constants.START_TAKE_PHOTO_SHADOW);
        this.mHandler.sendEmptyMessage(31);
    }

    private void takePhotoNew() {
        if (this.takePhotoCanOk.booleanValue()) {
            this.takePhotoCanOk = false;
            if (!FileUtils.takePhotoMemoryEnough(getActivity())) {
                Toast.makeText(getActivity(), C0853R.string.label_available_memory_not_enough, 0).show();
                return;
            }
            int filterMode = ((Integer) SPUtils.get(getActivity(), SharePrefConstant.FILTER_MODE, Integer.valueOf(Constants.FILTER_NONE_MODE))).intValue();
            int beautyMode = ((Integer) SPUtils.get(getActivity(), SharePrefConstant.BEAUTY_MODE, Integer.valueOf(Constants.BEAUTY_CLOSE))).intValue();
            if (!(filterMode == 10300 && beautyMode == 10401)) {
                setBtnPhotoVideoEnable(false);
                setChangePhotoVideoModeEnable(false);
                setCameraLensEnable(false);
            }
            Util.sendIntEventMessge(Constants.START_TAKE_PHOTO_SHADOW);
            this.mHandler.sendEmptyMessage(32);
            CameraUtils.setFrameLayerNumber(0);
        }
    }

    public void shootSound() {
        if (((AudioManager) getContext().getSystemService("audio")).getStreamVolume(5) != 0) {
            if (this.shootMP == null) {
                this.shootMP = MediaPlayer.create(getContext(), Uri.parse("file:///system/media/audio/ui/camera_click.ogg"));
            }
            if (this.shootMP != null) {
                this.shootMP.start();
            }
        }
    }

    private void switchCameraLens() {
        setCameraLensEnable(false);
        int cur = this.cameraManager.getFacing();
        if (this.cameraManager.getCameraManagerType() == 1) {
            if (cur == 1) {
                this.cameraManager.setFacing(0);
                SPUtils.put(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE));
            } else {
                this.cameraManager.setFacing(1);
                SPUtils.put(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_FRONT_MODE));
            }
        } else if (cur == 0) {
            this.cameraManager.setFacing(1);
            SPUtils.put(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE));
        } else {
            this.cameraManager.setFacing(0);
            SPUtils.put(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_FRONT_MODE));
            if (CameraUtils.getCurrentPageIndex() == 2 && CameraUtils.getMarkPointUIIsVisible()) {
                EventBusUtil.sendEvent(new Event(145));
            }
        }
        setFlashStatus();
        CameraUtils.setCameraPreviewWidth(this.cameraManager.getPreviewResolution().getHeight());
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Size picsize;
                FVBottomBarFragment.this.setCameraLensEnable(true);
                if (((Integer) SPUtils.get(FVBottomBarFragment.this.getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
                    if (CameraUtils.getMaxSupOrReComPictureFrontSize() == 0) {
                        picsize = FVBottomBarFragment.this.cameraManager.getRecommendPictureSize();
                    } else {
                        picsize = FVBottomBarFragment.this.cameraManager.getMaxSupportedPictureSize();
                    }
                } else if (CameraUtils.getMaxSupOrReComPictureSize() == 0) {
                    picsize = FVBottomBarFragment.this.cameraManager.getRecommendPictureSize();
                } else {
                    picsize = FVBottomBarFragment.this.cameraManager.getMaxSupportedPictureSize();
                }
                FVBottomBarFragment.this.cameraManager.setPictureResolution(picsize);
                if (((Integer) SPUtils.get(FVBottomBarFragment.this.getActivity(), SharePrefConstant.WB_MODE, Integer.valueOf(Constants.WB_AUTO))).intValue() != 10019) {
                    Util.sendIntEventMessge(Constants.CAMERA_RESET_WHITE_BALANCE);
                }
            }
        }, 500);
        if (this.mHandler != null) {
            this.mHandler.removeMessages(35);
            this.mHandler.sendEmptyMessageDelayed(35, 1000);
        }
        if (((Integer) SPUtils.get(this.mContext, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE))).intValue() != 107212) {
            Util.sendIntEventMessge(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE);
        }
        Util.sendIntEventMessge(Constants.EXPOSURE_OF_CAMERA_GONE);
    }

    private void setFlashStatus() {
        int mode = ((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_FRONT_MODE))).intValue();
        int flashMode = ((Integer) SPUtils.get(getActivity(), SharePrefConstant.FLASH_MODE, 10003)).intValue();
        if (mode == 10101) {
            if (flashMode == 10003) {
                this.btnFlashSwitch.setImageResource(C0853R.mipmap.ic_flash_auto_enable);
            } else if (flashMode == 10004) {
                this.btnFlashSwitch.setImageResource(C0853R.mipmap.ic_flash_open_enable);
            } else if (flashMode == 10005) {
                this.btnFlashSwitch.setImageResource(C0853R.mipmap.ic_flash_close_enable);
            } else {
                this.btnFlashSwitch.setImageResource(C0853R.mipmap.ic_flash_long_open_enable);
            }
            this.btnFlashSwitch.setEnabled(false);
            return;
        }
        this.btnFlashSwitch.setEnabled(true);
        if (flashMode == 10003) {
            this.btnFlashSwitch.setImageResource(C0853R.mipmap.ic_flash_auto);
        } else if (flashMode == 10004) {
            this.btnFlashSwitch.setImageResource(C0853R.mipmap.ic_flash_open);
        } else if (flashMode == 10005) {
            this.btnFlashSwitch.setImageResource(C0853R.mipmap.ic_flash_close);
        } else {
            this.btnFlashSwitch.setImageResource(C0853R.mipmap.ic_flash_long_open);
        }
    }

    private void rotateBeautyPop(final int angle) {
        if (!this.isBeautyShowed) {
            return;
        }
        if (angle == 90 || angle == 270) {
            if (this.fvBeautyPopWindow != null && this.fvBeautyPopWindow.isShowing()) {
                this.fvBeautyPopWindow.dismiss();
                showBeautyPop();
            }
        } else if (angle == 0 || angle == 180) {
            DisplayMetrics displayMetrics = Util.getDisplayMetrics(getActivity());
            int popWidth = displayMetrics.heightPixels - Util.dip2px(getActivity(), 30.0f);
            int[] commonLocation = new int[2];
            this.llLayout.getLocationOnScreen(commonLocation);
            int x = (displayMetrics.widthPixels / 2) - (popWidth / 2);
            int y = commonLocation[1];
            if (this.fvBeautyPop != null && this.fvBeautyPopWindow.isShowing() && this.mStartAngle != angle) {
                ObjectAnimator animator = ObjectAnimator.ofFloat(this.fvBeautyPopWindow.getContentView(), "rotation", new float[]{(float) this.mStartAngle, (float) angle});
                animator.setDuration(300);
                animator.setInterpolator(new LinearInterpolator());
                animator.start();
                animator.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        int unused = FVBottomBarFragment.this.mStartAngle = angle;
                    }
                });
                this.fvBeautyPopWindow.dismiss();
                this.fvBeautyPopWindow.showAtLocation(this.llLayout, 0, x, y);
            }
        }
    }

    private void hitchCockRecord() {
        if (((Boolean) SPUtils.get(this.mContext, SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, false)).booleanValue()) {
            CameraUtils.setMoveTimelapseRecording(true);
        } else {
            CameraUtils.setMoveTimelapseRecording(false);
        }
        if (this.isResume) {
            sendPtzPhotoOrVideoDismissPop();
            changeCamera2VideoMode();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    MoveTimelapseUtil.getInstance().setIsHitchCockRecord(1);
                    FVBottomBarFragment.this.hitchCockRecordStart();
                }
            }, 3500);
            Util.sendIntEventMessge(Constants.CAMERA_HITCH_COCK_START_VIDEO_TIME);
        }
    }

    /* access modifiers changed from: private */
    public void hitchCockRecordStart() {
        if (!FileUtils.takeVideoMemoryEnough(getActivity())) {
            Toast.makeText(getActivity(), C0853R.string.label_available_memory_not_enough, 0).show();
        } else if (this.cameraManager.getCameramode() == 0) {
        } else {
            if (this.cameraManager.getCameraManagerType() != 1) {
                setBtnPhotoVideoEnable(false);
                CameraUtils.setRecordingIng(true);
                BackgroundMusic.getInstance(getContext()).playRecordSound(getContext(), "record_start");
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        Boolean unused = FVBottomBarFragment.this.startHitchCockVideo = true;
                        Boolean unused2 = FVBottomBarFragment.this.startHitchCockVideoWT = true;
                        Boolean unused3 = FVBottomBarFragment.this.startHitchCockVideoMF = true;
                        double unused4 = FVBottomBarFragment.this.startRealTimeWT = (double) CameraUtils.getHitchCockStartPlaceWT();
                        double unused5 = FVBottomBarFragment.this.startRealTimeMF = CameraUtils.getHitchCockStartPlaceMF();
                        double hitchCockEndPlaceMF = CameraUtils.getHitchCockEndPlaceMF();
                        FVBottomBarFragment.this.startVideoRecord();
                    }
                }, 300);
            } else if ("Redmi Note 5".equals(this.systemModel)) {
                setBtnPhotoVideoEnable(false);
                CameraUtils.setRecordingIng(true);
                BackgroundMusic.getInstance(getContext()).playRecordSound(getContext(), "record_start");
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        Boolean unused = FVBottomBarFragment.this.startHitchCockVideo = true;
                        Boolean unused2 = FVBottomBarFragment.this.startHitchCockVideoWT = true;
                        Boolean unused3 = FVBottomBarFragment.this.startHitchCockVideoMF = true;
                        double unused4 = FVBottomBarFragment.this.startRealTimeWT = (double) CameraUtils.getHitchCockStartPlaceWT();
                        double unused5 = FVBottomBarFragment.this.startRealTimeMF = CameraUtils.getHitchCockStartPlaceMF();
                        double endRealTimeMF = CameraUtils.getHitchCockEndPlaceMF();
                        if (!(FVCameraManager.GetCameraLevel(FVBottomBarFragment.this.mContext) == 2 || FVBottomBarFragment.this.startRealTimeMF == endRealTimeMF || FVBottomBarFragment.this.cameraManager.isMaunalFocus())) {
                            FVBottomBarFragment.this.cameraManager.enableMFMode(true);
                        }
                        FVBottomBarFragment.this.startVideoRecord();
                    }
                }, 300);
            } else {
                this.startHitchCockVideo = true;
                this.startHitchCockVideoWT = true;
                this.startHitchCockVideoMF = true;
                this.startRealTimeWT = (double) CameraUtils.getHitchCockStartPlaceWT();
                this.startRealTimeMF = CameraUtils.getHitchCockStartPlaceMF();
                double endRealTimeMF = CameraUtils.getHitchCockEndPlaceMF();
                if (!(FVCameraManager.GetCameraLevel(this.mContext) == 2 || this.startRealTimeMF == endRealTimeMF || this.cameraManager.isMaunalFocus())) {
                    this.cameraManager.enableMFMode(true);
                }
                startVideoRecord();
            }
        }
    }

    private void record() {
        if (!FileUtils.takeVideoMemoryEnough(getActivity())) {
            Toast.makeText(getActivity(), C0853R.string.label_available_memory_not_enough, 0).show();
        } else if (this.cameraManager.getCameramode() == 0) {
        } else {
            if (this.cameraManager.isMediaRecording()) {
                CameraUtils.setMoveTimelapseRecording(false);
                CameraUtils.setTimelapseIng(false);
                CameraUtils.setLongExposureIng(false);
                CameraUtils.setRecordingIng(false);
                this.cameraManager.stopMediaRecord();
                if (this.startHitchCockVideo.booleanValue()) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            if (FVBottomBarFragment.this.cameraManager.getCameraManagerType() != 2) {
                                Util.sendIntEventMessge(Constants.CAMERA_HITCH_COCK_ZOOM_RESTART_ONE);
                            } else if (FVCameraManager.GetCameraLevel(FVBottomBarFragment.this.mContext) != 2) {
                                Util.sendIntEventMessge(Constants.CAMERA_HITCH_COCK_ZOOM_RESTART_TWO);
                            }
                        }
                    }, 200);
                    this.startHitchCockVideo = false;
                }
                if (Constants.IS_MOVE_TIME_LAPSE_AND_SHOOTING) {
                    BleByteUtil.setPTZParameters((byte) 40, (byte) 3);
                    Constants.IS_MOVE_TIME_LAPSE_AND_SHOOTING = false;
                } else {
                    BleByteUtil.setPTZParameters((byte) ClosedCaptionCtrl.CARRIAGE_RETURN, (byte) 0);
                }
                moveOrTimeLapseVideoLess1S();
            } else if (this.cameraManager.getCameraManagerType() != 1) {
                setBtnPhotoVideoEnable(false);
                CameraUtils.setRecordingIng(true);
                startVideoRecord();
            } else if ("Redmi Note 5".equals(this.systemModel)) {
                setBtnPhotoVideoEnable(false);
                CameraUtils.setRecordingIng(true);
                startVideoRecord();
            } else {
                startVideoRecord();
            }
        }
    }

    private void closeScoChannel(boolean isClose) {
        if (BluetoothHeadsetUtil.isHeadSetCanUse()) {
            BluetoothHeadsetUtil.changeBtChannelToSco(this.mContext, !isClose);
        }
    }

    /* access modifiers changed from: private */
    public void startVideoRecord() {
        int qulity;
        Size size;
        if (this.connected) {
            BleByteUtil.setPTZParameters((byte) ClosedCaptionCtrl.CARRIAGE_RETURN, (byte) 2);
        }
        File file = Util.getOutputMediaFile(3, 1, this.mContext);
        if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
            qulity = CameraUtils.getCheckMediaRecordFrontSize();
        } else {
            qulity = CameraUtils.getCheckMediaRecordSize();
        }
        if (file != null && qulity > 0) {
            int orientation = ScreenOrientationUtil.getInstance().getOrientation();
            if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
                if (orientation == 90) {
                    orientation = 270;
                } else if (orientation == 270) {
                    orientation = 90;
                }
            }
            if (this.cameraManager.getCameraManagerType() == 1) {
                this.cameraManager.startMediaRecordEx(file.getPath(), qulity, orientation);
            } else {
                setVideoResoluCheck();
                if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
                    String highSpeed = SPUtils.get(getActivity(), SharePrefConstant.FRONT_HIGH_SPEED_VIDEO_RESOLUTION, "").toString();
                    if ("".equals(highSpeed)) {
                        size = CameraUtils.getCheckMediaRecordFrontSizeTwo();
                        this.cameraManager.startMediaRecordExt(file.getPath(), size);
                    } else {
                        size = CameraUtils.getRecordStringToSize(highSpeed.substring(0, highSpeed.indexOf("[")));
                        this.cameraManager.startMediaRecord(file.getPath(), size, Integer.valueOf(highSpeed.substring(highSpeed.indexOf("[") + 1, highSpeed.indexOf(","))).intValue());
                        this.btnFollow.setVisibility(4);
                    }
                } else {
                    String highSpeed2 = SPUtils.get(getActivity(), SharePrefConstant.HIGH_SPEED_VIDEO_RESOLUTION, "").toString();
                    if ("".equals(highSpeed2)) {
                        size = CameraUtils.getCheckMediaRecordSizeTwo();
                        this.cameraManager.startMediaRecordExt(file.getPath(), size);
                    } else {
                        size = CameraUtils.getRecordStringToSize(highSpeed2.substring(0, highSpeed2.indexOf("[")));
                        this.cameraManager.startMediaRecord(file.getPath(), size, Integer.valueOf(highSpeed2.substring(highSpeed2.indexOf("[") + 1, highSpeed2.indexOf(","))).intValue());
                        this.btnFollow.setVisibility(4);
                    }
                }
                Log.e("-------------", "------- 6666666  设置  SIZE ------ " + size);
            }
            SPUtils.put(getActivity(), SharePrefConstant.CURRENT_VIDEO_PATH, file.getPath());
            setBtnPhotoVideoEnable(false);
            this.mHandler.sendEmptyMessageDelayed(10, 1100);
            CameraUtils.setRecordingIng(true);
        }
    }

    private void setVideoResoluCheck() {
        CameraUtils.setMediaRecordSizeTwo(this.cameraManager.getSupportedMediaRecordSizes());
        if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
            if ("".equals((String) SPUtils.get(getActivity(), SharePrefConstant.FRONT_VIDEO_RESOLUTION_TWO, ""))) {
                CameraUtils.setCheckMediaRecordFrontSizeTwo(CameraUtils.getMediaRecordSizeTwo()[0]);
            }
        } else if ("".equals((String) SPUtils.get(getActivity(), SharePrefConstant.VIDEO_RESOLUTION_TWO, ""))) {
            CameraUtils.setCheckMediaRecordSizeTwo(CameraUtils.getMediaRecordSizeTwo()[0]);
        }
    }

    private void stopRecordOnly() {
        if (!this.isPhotoMode) {
            if (this.cameraManager.isMediaRecording()) {
                moveOrTimeLapseVideoLess1S();
                CameraUtils.setMoveTimelapseRecording(false);
                CameraUtils.setTimelapseIng(false);
                CameraUtils.setLongExposureIng(false);
                CameraUtils.setRecordingIng(false);
                this.cameraManager.stopMediaRecord();
                if (this.startHitchCockVideo.booleanValue()) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            if (FVBottomBarFragment.this.cameraManager.getCameraManagerType() != 2) {
                                Util.sendIntEventMessge(Constants.CAMERA_HITCH_COCK_ZOOM_RESTART_ONE);
                            } else if (FVCameraManager.GetCameraLevel(FVBottomBarFragment.this.mContext) != 2) {
                                Util.sendIntEventMessge(Constants.CAMERA_HITCH_COCK_ZOOM_RESTART_TWO);
                            }
                        }
                    }, 200);
                    this.startHitchCockVideo = false;
                }
            }
            if (!this.connected) {
                return;
            }
            if (Constants.IS_MOVE_TIME_LAPSE_AND_SHOOTING) {
                BleByteUtil.setPTZParameters((byte) 40, (byte) 3);
                Constants.IS_MOVE_TIME_LAPSE_AND_SHOOTING = false;
                return;
            }
            BleByteUtil.setPTZParameters((byte) ClosedCaptionCtrl.CARRIAGE_RETURN, (byte) 0);
            boolean isConnected = ViseBluetooth.getInstance().isConnected();
            ((Boolean) SPUtils.get(getActivity(), SharePrefConstant.TRIPOD_MODE_SWITCH, false)).booleanValue();
        }
    }

    private void changemode() {
        if (this.cameraManager.getCameramode() == 0) {
            closePhotoModeSetting();
            this.cameraManager.changeCamreMode(1);
            this.btnPhotoVideo.setImageResource(C0853R.mipmap.ic_video);
            this.fragment_buttom_bar_video_check.setVisibility(0);
            this.fragment_buttom_bar_photo_check.setVisibility(8);
            return;
        }
        this.cameraManager.changeCamreMode(0);
        this.btnPhotoVideo.setImageDrawable(getResources().getDrawable(C0853R.C0854drawable.sl_photo_mode));
        this.fragment_buttom_bar_video_check.setVisibility(8);
        this.fragment_buttom_bar_photo_check.setVisibility(0);
    }

    private void closePhotoModeSetting() {
        this.cameraManager.changeCameraManagerMode(1);
        if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.BEAUTY_MODE, Integer.valueOf(Constants.BEAUTY_OPEN))).intValue() == 10400) {
            SPUtils.put(getActivity(), SharePrefConstant.BEAUTY_MODE, Integer.valueOf(Constants.BEAUTY_CLOSE));
        }
        this.btnCommonPop.setVisibility(4);
        this.btnOpenBeauty.setVisibility(4);
        SPUtils.put(getActivity(), SharePrefConstant.FILTER_MODE, Integer.valueOf(Constants.FILTER_NONE_MODE));
        SPUtils.put(getActivity(), SharePrefConstant.HDR_MODE, Integer.valueOf(Constants.HDR_CLOSE));
        SPUtils.put(getActivity(), SharePrefConstant.LONG_EXPOSURE_MODE, Integer.valueOf(Constants.LONG_EXPOSURE_NONE));
        SPUtils.put(getActivity(), SharePrefConstant.FULL_SHOT, Integer.valueOf(Constants.FULL_SHOT_NONE));
        SPUtils.put(getActivity(), SharePrefConstant.DELAY_TAKE_PHOTO_MODE, Integer.valueOf(Constants.DELAY_TAKE_PHOTO_0S));
        this.longExposureOpen = false;
        this.delayPhotoOpen = false;
        this.FullShotOpen = false;
        this.beautyOpen = false;
    }

    private void changeCamera2PhotoMode() {
        if (!this.isPhotoMode) {
            this.cameraManager.setContinuosFocusMode(4);
            this.fragment_buttom_bar_photo_check.setVisibility(0);
            this.fragment_buttom_bar_video_check.setVisibility(8);
            changemode();
            this.isPhotoMode = true;
            SPUtils.put(getActivity(), SharePrefConstant.CAMERA_MODE, 10001);
        }
    }

    private void changeCamera2VideoMode() {
        if (this.isPhotoMode) {
            this.cameraManager.setContinuosFocusMode(3);
            this.fragment_buttom_bar_photo_check.setVisibility(8);
            this.fragment_buttom_bar_video_check.setVisibility(0);
            changemode();
            this.isPhotoMode = false;
            Util.sendIntEventMessge(Constants.DELAY_TAKE_PHOTO_0S);
            SPUtils.put(getActivity(), SharePrefConstant.CAMERA_MODE, 10002);
            if (((Integer) SPUtils.get(this.mContext, SharePrefConstant.WB_MODE, Integer.valueOf(Constants.WB_AUTO))).intValue() != 10019 && this.handlerWhiteBalance != null) {
                while (this.handlerWhiteBalance.hasMessages(11)) {
                    this.handlerWhiteBalance.removeMessages(11);
                }
                this.handlerWhiteBalance.sendEmptyMessageDelayed(11, 1000);
            }
        }
    }

    /* access modifiers changed from: private */
    public void resetWhiteBalance() {
        int wbMode = ((Integer) SPUtils.get(this.mContext, SharePrefConstant.WB_MODE, Integer.valueOf(Constants.WB_AUTO))).intValue();
        if (wbMode == 10019) {
            this.cameraManager.setWhiteBalance(Constants.SCENE_MODE_AUTO);
        } else if (wbMode == 10020) {
            this.cameraManager.setWhiteBalance("daylight");
        } else if (wbMode == 10021) {
            this.cameraManager.setWhiteBalance("cloudy-daylight");
        } else if (wbMode == 10022) {
            this.cameraManager.setWhiteBalance("fluorescent");
        } else if (wbMode == 10023) {
            this.cameraManager.setWhiteBalance("incandescent");
        }
    }

    private void moveOrTimeLapseVideoLess1S() {
        if (CameraUtils.isMoveOrDelayTimeLapseIng()) {
            if (((float) CameraUtils.getMoveOrDelayTimeLapseCurrentTime()) / CameraUtils.getMoveOrDelayTimeLapseShutter() < 14.0f && MoveTimelapseUtil.getMotionLapseTimeYesOrNo()) {
                Toast.makeText(getActivity(), C0853R.string.label_move_or_delay_timelapse_video_less_1s, 0).show();
                if (!Util.isEmpty(CameraUtils.getMoveOrDelayTimeLapsePath())) {
                    File file = new File(CameraUtils.getMoveOrDelayTimeLapsePath());
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }
            CameraUtils.setMoveOrDelayTimeLapsePath((String) null);
            CameraUtils.setMoveOrDelayTimeLapseIng(false);
            CameraUtils.setMoveOrDelayTimeLapseCurrentTime(0);
            CameraUtils.setMoveOrDelayTimeLapseShutter(0.0f);
            this.mHandler.removeMessages(20);
        }
    }

    private void showBeautyPop() {
        int x;
        int y;
        int angle = ScreenOrientationUtil.getInstance().getOrientation();
        this.fvBeautyPop = new FVBeautyPop();
        this.fvBeautyPop.init(getActivity(), (View) null, this.cameraManager);
        DisplayMetrics displayMetrics = Util.getDisplayMetrics(getActivity());
        int bottomBarWidth = displayMetrics.heightPixels - Util.dip2px(getActivity(), 30.0f);
        this.fvBeautyPopWindow = new PopupWindow(this.fvBeautyPop.getView(), bottomBarWidth, bottomBarWidth, false);
        this.fvBeautyPopWindow.setBackgroundDrawable(new ColorDrawable(0));
        this.fvBeautyPopWindow.setAnimationStyle(C0853R.style.popAnimation5);
        this.fvBeautyPopWindow.setOutsideTouchable(true);
        this.fvBeautyPop.setPop(this.fvBeautyPopWindow, this.fvBeautyPop);
        int[] commonLocation = new int[2];
        this.llLayout.getLocationOnScreen(commonLocation);
        if (angle == 0) {
            this.fvBeautyPopWindow.getContentView().setRotation(360.0f);
            x = (displayMetrics.widthPixels / 2) - ((displayMetrics.heightPixels - Util.dip2px(getActivity(), 30.0f)) / 2);
            y = commonLocation[1];
        } else if (angle == 180) {
            this.fvBeautyPopWindow.getContentView().setRotation(180.0f);
            x = (displayMetrics.widthPixels / 2) - ((displayMetrics.heightPixels - Util.dip2px(getActivity(), 30.0f)) / 2);
            y = commonLocation[1];
        } else if (angle == 270) {
            this.fvBeautyPopWindow.getContentView().findViewById(C0853R.C0855id.rl_beauty_content).setRotation(180.0f);
            x = (commonLocation[0] - bottomBarWidth) - Util.dip2px(getActivity(), 46.0f);
            y = (displayMetrics.heightPixels - bottomBarWidth) / 2;
        } else {
            x = (commonLocation[0] - bottomBarWidth) - Util.dip2px(getActivity(), 46.0f);
            y = (displayMetrics.heightPixels - bottomBarWidth) / 2;
        }
        this.fvBeautyPopWindow.setFocusable(false);
        this.fvBeautyPopWindow.update();
        this.fvBeautyPopWindow.showAtLocation(this.llLayout, 0, x, y);
        this.fvBeautyPopWindow.setFocusable(true);
        this.fvBeautyPopWindow.update();
        this.fvBeautyPop.setButtonOnClick(new FVBeautyPop.CheckButtonOnclick() {
            public void onClick(View view) {
                FVBottomBarFragment.this.fvBeautyPopWindow.dismiss();
                ViseLog.m1466e("onClick isBeautyShowed :" + FVBottomBarFragment.this.isBeautyShowed);
                boolean unused = FVBottomBarFragment.this.isBeautyShowed = false;
            }
        });
    }

    private void showPhotoCommonPop(int type) {
        final FVPhotoModeCommonShortcutPop csPop = new FVPhotoModeCommonShortcutPop();
        csPop.init(getActivity(), this.cameraManager, type);
        PopupWindow pop = new PopupWindow(csPop.getView(), Util.dip2px(getActivity(), 60.0f), Util.getDeviceSize(getActivity()).y - Util.dip2px(getActivity(), 30.0f), true);
        pop.setBackgroundDrawable(new ColorDrawable(0));
        pop.setAnimationStyle(C0853R.style.popAnimation4);
        pop.setOutsideTouchable(true);
        csPop.setPop(pop, csPop);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                csPop.unRegisterListener();
            }
        });
        pop.setFocusable(false);
        pop.update();
        int[] commonLocation = new int[2];
        this.btnCommonPop.getLocationOnScreen(commonLocation);
        pop.showAtLocation(this.btnCommonPop, 0, (commonLocation[0] - Util.dip2px(getActivity(), 60.0f)) - Util.dip2px(getActivity(), 30.0f), Util.dip2px(getActivity(), 15.0f));
        Util.fullScreenImmersive(pop.getContentView());
        pop.setFocusable(true);
        pop.update();
    }

    private void dealUnclickableMode() {
        if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.FULL_SHOT, Integer.valueOf(Constants.FULL_SHOT_NONE))).intValue() != 10024) {
            this.llLayout.setVisibility(4);
            this.btnCommonPop.setVisibility(4);
            this.btnBleConnect.setVisibility(4);
            this.btnOpenBeauty.setEnabled(false);
        } else {
            this.llLayout.setBackgroundColor(getResources().getColor(C0853R.color.transparent));
            this.llBottomShadow.setVisibility(0);
            this.btnIconShow.setVisibility(4);
            this.btnLensSwitch.setVisibility(4);
            this.btnFlashSwitch.setVisibility(4);
            if (((Boolean) SPUtils.get(this.mContext, SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, false)).booleanValue()) {
                this.btnFollow.setVisibility(4);
            }
            if (MoveTimelapseUtil.getInstance().getCameraProgressLinear() == 3 || MoveTimelapseUtil.getInstance().getCameraProgressLinear() == 2 || MoveTimelapseUtil.getInstance().getIsHitchCockRecord() == 1 || CameraUtils.utilsIsMIUI()) {
                this.btnFollow.setVisibility(4);
            }
        }
        if (this.isPhotoMode) {
            this.fragment_buttom_bar_photo_check.setVisibility(4);
        } else {
            this.fragment_buttom_bar_video_check.setVisibility(4);
        }
        Util.sendIntEventMessge((int) Constants.TOP_BAR_FRAGMENT_BUTTON_UNCLICK, BleConstant.SHUTTER);
    }

    private void recoverUnclickMode() {
        if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.FULL_SHOT, Integer.valueOf(Constants.FULL_SHOT_NONE))).intValue() != 10024) {
            this.llLayout.setVisibility(0);
            this.btnCommonPop.setVisibility(0);
            this.btnBleConnect.setVisibility(0);
            this.btnOpenBeauty.setEnabled(true);
        } else {
            this.llBottomShadow.setVisibility(8);
            this.llLayout.setBackground(getResources().getDrawable(C0853R.C0854drawable.sp_white50_round5_bg));
            this.btnIconShow.setVisibility(0);
            this.btnLensSwitch.setVisibility(0);
            this.btnFlashSwitch.setVisibility(0);
            this.btnFollow.setVisibility(0);
        }
        if (this.isPhotoMode) {
            this.fragment_buttom_bar_photo_check.setVisibility(0);
        } else {
            this.fragment_buttom_bar_video_check.setVisibility(0);
        }
        Util.sendIntEventMessge((int) Constants.TOP_BAR_FRAGMENT_BUTTON_UNCLICK, "0");
    }

    private void getCurrentPic() {
        if (this.mMyCountDownTimer != null) {
            this.mMyCountDownTimer.cancel();
            this.mMyCountDownTimer = new MyCountDownTimer(1000, 1000);
            this.mMyCountDownTimer.start();
            return;
        }
        this.mMyCountDownTimer = new MyCountDownTimer(1000, 1000);
        this.mMyCountDownTimer.start();
    }

    class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onTick(long millisUntilFinished) {
        }

        public void onFinish() {
            if (FVBottomBarFragment.this.mHandler != null) {
                while (FVBottomBarFragment.this.mHandler.hasMessages(34)) {
                    FVBottomBarFragment.this.mHandler.removeMessages(34);
                }
                FVBottomBarFragment.this.mHandler.sendEmptyMessage(34);
            }
        }
    }

    private void showCurrentPicByEvent(FVModeSelectEvent fvModeSelectEvent) {
        String path = (String) fvModeSelectEvent.getMessage();
        if (Util.isEmpty(path)) {
            return;
        }
        if (new File(path).exists()) {
            Glide.with(getActivity()).load(path).error((int) C0853R.mipmap.ic_gallery_empty).into(new GlideDrawableImageViewTarget(this.btnIconShow) {
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                    super.onResourceReady(resource, animation);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            Boolean unused = FVBottomBarFragment.this.takePhotoCanOk = true;
                            Log.e("-----------", "------- 拍照结束 加载 -----" + System.currentTimeMillis());
                        }
                    }, 100);
                }
            });
            return;
        }
        this.mHandler.sendEmptyMessage(34);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Boolean unused = FVBottomBarFragment.this.takePhotoCanOk = true;
                Log.e("-----------", "------- 拍照结束 异常 -----" + System.currentTimeMillis());
            }
        }, 100);
    }

    /* access modifiers changed from: private */
    public void setBtnPhotoVideoEnable(boolean enable) {
        if (this.btnPhotoVideo != null) {
            this.btnPhotoVideo.setEnabled(enable);
        }
    }

    private void setChangePhotoVideoModeEnable(boolean enable) {
        if (this.isPhotoMode) {
            this.btnVideoMode.setEnabled(enable);
        } else {
            this.btnPhotoMode.setEnabled(enable);
        }
    }

    /* access modifiers changed from: private */
    public void setCameraLensEnable(boolean enable) {
        if (this.btnLensSwitch != null) {
            this.btnLensSwitch.setEnabled(enable);
        }
    }

    public void sendPtzPhotoOrVideoDismissPop() {
        Util.sendIntEventMessge(Constants.PTZ_SEND_PHOTO_OR_VIDEO_DISMISS_POP);
    }

    public boolean getPtzRecordExclusion() {
        return CameraUtils.isFullShotIng() || CameraUtils.isMoveTimelapseIng() || CameraUtils.isMoveTimelapseRecording() || CameraUtils.isTimelapseIng() || CameraUtils.isDelayPhotoIng();
    }

    public boolean getPtzTakePhotoExclusion() {
        return CameraUtils.isFullShotIng() || CameraUtils.isMoveTimelapseIng() || CameraUtils.isMoveTimelapseRecording() || CameraUtils.isTimelapseIng() || CameraUtils.isDelayPhotoIng();
    }

    public boolean getPtzStretchLensExclusion() {
        return CameraUtils.isFullShotIng() || CameraUtils.isLongExposureIng() || CameraUtils.isFollowIng();
    }

    public boolean getPtzStretchLensExclusionAnd210() {
        if (CameraUtils.getCurrentPageIndex() == 2) {
            if (CameraUtils.isFullShotIng() || CameraUtils.isLongExposureIng()) {
                return true;
            }
            return false;
        } else if (CameraUtils.isFullShotIng() || CameraUtils.isLongExposureIng() || CameraUtils.isFollowIng()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getPtzCenterFoucusExclusion() {
        return CameraUtils.isFullShotIng() || CameraUtils.isLongExposureIng();
    }

    public boolean getPtzExposureExclusion() {
        return CameraUtils.isFullShotIng() || CameraUtils.isLongExposureIng();
    }

    public boolean getPtzChangeLensExclusion() {
        return CameraUtils.isFullShotIng() || CameraUtils.isMoveTimelapseIng() || CameraUtils.isMoveTimelapseRecording() || CameraUtils.isLongExposureIng() || CameraUtils.isTimelapseIng() || CameraUtils.isFollowIng() || CameraUtils.isRecordingIng();
    }

    private void setCommonPopStatus(int type, boolean opened) {
        if (type == 0) {
            if (opened) {
                int fsMode = ((Integer) SPUtils.get(getActivity(), SharePrefConstant.FULL_SHOT, Integer.valueOf(Constants.FULL_SHOT_NONE))).intValue();
                if (fsMode == 10025) {
                    this.btnCommonPop.setImageResource(C0853R.mipmap.ic_180_small);
                } else if (fsMode == 10026) {
                    this.btnCommonPop.setImageResource(C0853R.mipmap.ic_330_small);
                } else if (fsMode == 10027) {
                    this.btnCommonPop.setImageResource(C0853R.mipmap.ic_3_3_small);
                } else if (fsMode == 10028) {
                    this.btnCommonPop.setImageResource(C0853R.mipmap.ic_3_5_small);
                }
                this.btnCommonPop.setVisibility(0);
                this.btnCommonPop.setTag(0);
                return;
            }
            this.btnCommonPop.setVisibility(4);
        } else if (type == 1) {
            if (opened) {
                int leMode = ((Integer) SPUtils.get(getActivity(), SharePrefConstant.LONG_EXPOSURE_MODE, Integer.valueOf(Constants.LONG_EXPOSURE_NONE))).intValue();
                if (leMode == 106206) {
                    this.btnCommonPop.setImageResource(C0853R.mipmap.ic_le_double_image);
                } else if (leMode == 106207) {
                    this.btnCommonPop.setImageResource(C0853R.mipmap.ic_le_track);
                }
                this.btnCommonPop.setVisibility(0);
                this.btnCommonPop.setTag(1);
                return;
            }
            this.btnCommonPop.setVisibility(4);
        } else if (type == 2) {
            if (opened) {
                int dpMode = ((Integer) SPUtils.get(getActivity(), SharePrefConstant.DELAY_TAKE_PHOTO_MODE, Integer.valueOf(Constants.DELAY_TAKE_PHOTO_0S))).intValue();
                if (dpMode == 100012) {
                    this.btnCommonPop.setImageResource(C0853R.mipmap.ic_delay_2s_less);
                } else if (dpMode == 100013) {
                    this.btnCommonPop.setImageResource(C0853R.mipmap.ic_delay_5s_less);
                } else if (dpMode == 10014) {
                    this.btnCommonPop.setImageResource(C0853R.mipmap.ic_delay_10s_less);
                }
                if (this.beautyOpen) {
                    this.btnCommonPop.setVisibility(0);
                    this.btnOpenBeauty.setVisibility(0);
                } else {
                    this.btnCommonPop.setVisibility(0);
                }
                this.btnCommonPop.setTag(2);
            } else if (this.beautyOpen) {
                this.btnOpenBeauty.setVisibility(4);
                this.btnCommonPop.setImageResource(C0853R.mipmap.ic_open_beauty);
                this.btnCommonPop.setVisibility(0);
                this.btnCommonPop.setTag(3);
            } else {
                this.btnOpenBeauty.setVisibility(4);
                this.btnCommonPop.setVisibility(4);
            }
        } else if (type != 3) {
        } else {
            if (opened) {
                if (this.delayPhotoOpen) {
                    this.btnOpenBeauty.setVisibility(0);
                    return;
                }
                this.btnCommonPop.setImageResource(C0853R.mipmap.ic_open_beauty);
                this.btnCommonPop.setVisibility(0);
                this.btnOpenBeauty.setVisibility(4);
                this.btnCommonPop.setTag(3);
            } else if (this.delayPhotoOpen) {
                this.btnOpenBeauty.setVisibility(4);
            } else {
                this.btnCommonPop.setVisibility(4);
            }
        }
    }

    private void showCommonPop() {
        int type = ((Integer) this.btnCommonPop.getTag()).intValue();
        if (type == 0) {
            showPhotoCommonPop(type);
        } else if (type == 1) {
            showPhotoCommonPop(type);
        } else if (type == 2) {
            showPhotoCommonPop(type);
        } else if (type == 3) {
            showBeautyPop();
        }
    }

    private class OrientationBroad extends BroadcastReceiver {
        private OrientationBroad() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getIntExtra(ScreenOrientationUtil.BC_OrientationChangedKey, -1) != -1) {
                FVBottomBarFragment.this.getAngle();
            }
        }
    }

    /* access modifiers changed from: private */
    public void getAngle() {
        int angle = ScreenOrientationUtil.getInstance().getOrientation();
        if (angle == 0) {
            this.mHandler.sendEmptyMessage(0);
        } else if (angle == 90) {
            this.mHandler.sendEmptyMessage(90);
        } else if (angle == 180) {
            this.mHandler.sendEmptyMessage(180);
        } else if (angle == 270) {
            this.mHandler.sendEmptyMessage(270);
        }
    }

    /* access modifiers changed from: private */
    public void rotateView(final int angle, View... views) {
        int length = views.length;
        int i = 0;
        while (i < length) {
            View view = views[i];
            if (this.mStartAngle != angle) {
                ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", new float[]{(float) this.mStartAngle, (float) angle});
                animator.setDuration(300);
                animator.setInterpolator(new LinearInterpolator());
                animator.start();
                animator.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        int unused = FVBottomBarFragment.this.mStartAngle = angle;
                    }
                });
                i++;
            } else {
                return;
            }
        }
    }

    public void showProgress(String msg) {
        if (this.mProgressDialog == null) {
            this.mProgressDialog = new LoadingView(this.mContext);
            this.mProgressDialog.setCancelable(true);
            this.mProgressDialog.setCanceledOnTouchOutside(true);
        }
        this.mProgressDialog.setMessage(msg);
        this.mProgressDialog.show();
    }

    public void hideProgress() {
        if (this.mProgressDialog != null) {
            this.mProgressDialog.dismiss();
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        ViseLog.m1466e("BottomBarFragment onDestroyView");
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages((Object) null);
        }
        if (this.myHitchCockHandler != null) {
            this.myHitchCockHandler.removeCallbacksAndMessages((Object) null);
        }
        if (this.handlerWhiteBalance != null) {
            this.handlerWhiteBalance.removeCallbacksAndMessages((Object) null);
        }
        if (this.mMyCountDownTimer != null) {
            this.mMyCountDownTimer.cancel();
        }
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
        this.startSeekThread = false;
        this.startSeekThreadExposure = false;
        this.startSeekThread300 = false;
        this.startSeekThreadNew300 = false;
        this.startSeekThread210 = false;
        this.startSeekThreadNew210 = false;
        this.startSeekThreadExposure300 = false;
        this.startSeekThreadMF210 = false;
        this.startHitchCockVideo = false;
        this.startHitchCockVideoWT = true;
        this.startHitchCockVideoMF = true;
        this.startSeekThreadHandModel210 = false;
        if (this.seekThreed != null && this.seekThreed.isAlive()) {
            this.seekThreed.interrupt();
        }
        if (this.seekZoomThreed210 != null && this.seekZoomThreed210.isAlive()) {
            this.seekZoomThreed210.interrupt();
        }
    }
}
