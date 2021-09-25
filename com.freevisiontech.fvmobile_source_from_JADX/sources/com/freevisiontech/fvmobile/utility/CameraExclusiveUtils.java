package com.freevisiontech.fvmobile.utility;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import com.freevisiontech.cameralib.FVCameraManager;
import com.freevisiontech.filterlib.FilterType;
import com.freevisiontech.fvmobile.C0853R;

public class CameraExclusiveUtils {
    public static void setFilterExclusive(Context context, FVCameraManager cameraManager, int position) {
        if (position == 0) {
            cameraManager.changeCameraManagerMode(1);
            return;
        }
        if (((Integer) SPUtils.get(context, SharePrefConstant.FULL_SHOT, Integer.valueOf(Constants.FULL_SHOT_NONE))).intValue() != 10024) {
            Toast.makeText(context, C0853R.string.label_close_full_shot_hint, 0).show();
            Util.sendIntEventMessge(Constants.FULL_SHOT_NONE);
            Util.sendIntEventMessge(Constants.FULL_SHOT_CLOSE);
            SPUtils.put(context, SharePrefConstant.FULL_SHOT, Integer.valueOf(Constants.FULL_SHOT_NONE));
        }
        if (((Integer) SPUtils.get(context, SharePrefConstant.LONG_EXPOSURE_MODE, Integer.valueOf(Constants.LONG_EXPOSURE_NONE))).intValue() != 106205) {
            Toast.makeText(context, C0853R.string.label_close_long_exposure_hint, 0).show();
            Util.sendIntEventMessge(Constants.DELAY_TAKE_PHOTO_0S);
            Util.sendIntEventMessge(Constants.LONG_EXPOSURE_CLOSE);
            SPUtils.put(context, SharePrefConstant.LONG_EXPOSURE_MODE, Integer.valueOf(Constants.LONG_EXPOSURE_NONE));
        }
        if (((Integer) SPUtils.get(context, SharePrefConstant.BEAUTY_MODE, Integer.valueOf(Constants.BEAUTY_OPEN))).intValue() == 10400) {
            Toast.makeText(context, C0853R.string.label_close_beauty_hint, 0).show();
            Util.sendIntEventMessge(Constants.BEAUTY_CLOSE);
            SPUtils.put(context, SharePrefConstant.BEAUTY_MODE, Integer.valueOf(Constants.BEAUTY_CLOSE));
            SPUtils.put(context, SharePrefConstant.BEAUTY_VALUE, 50);
        }
        if (((Integer) SPUtils.get(context, SharePrefConstant.HDR_MODE, Integer.valueOf(Constants.HDR_CLOSE))).intValue() == 106101) {
            Toast.makeText(context, C0853R.string.label_close_hdr_hint, 0).show();
            SPUtils.put(context, SharePrefConstant.HDR_MODE, Integer.valueOf(Constants.HDR_CLOSE));
            if (cameraManager.isSupportHdrMode()) {
                cameraManager.setHdrMode(false);
            }
        }
        if (CameraUtils.isFollowIng()) {
            Util.sendIntEventMessge(Constants.EXCLUSIVE_CLOSE_KCF);
        }
        cameraManager.changeCameraManagerMode(0);
    }

    public static void setLapsePhotoExclusive(Context context, FVCameraManager cameraManager, int position) {
        if (position != 0) {
            if (((Integer) SPUtils.get(context, SharePrefConstant.FULL_SHOT, Integer.valueOf(Constants.FULL_SHOT_NONE))).intValue() != 10024) {
                Toast.makeText(context, C0853R.string.label_close_full_shot_hint, 0).show();
                Util.sendIntEventMessge(Constants.FULL_SHOT_NONE);
                Util.sendIntEventMessge(Constants.FULL_SHOT_CLOSE);
                SPUtils.put(context, SharePrefConstant.FULL_SHOT, Integer.valueOf(Constants.FULL_SHOT_NONE));
            }
            if (((Integer) SPUtils.get(context, SharePrefConstant.LONG_EXPOSURE_MODE, Integer.valueOf(Constants.LONG_EXPOSURE_NONE))).intValue() != 106205) {
                Toast.makeText(context, C0853R.string.label_close_long_exposure_hint, 0).show();
                Util.sendIntEventMessge(Constants.DELAY_TAKE_PHOTO_0S);
                Util.sendIntEventMessge(Constants.LONG_EXPOSURE_CLOSE);
                cameraManager.changeCameraManagerMode(1);
                SPUtils.put(context, SharePrefConstant.LONG_EXPOSURE_MODE, Integer.valueOf(Constants.LONG_EXPOSURE_NONE));
            }
            if (((Integer) SPUtils.get(context, SharePrefConstant.HDR_MODE, Integer.valueOf(Constants.HDR_CLOSE))).intValue() == 106101) {
                Toast.makeText(context, C0853R.string.label_close_hdr_hint, 0).show();
                SPUtils.put(context, SharePrefConstant.HDR_MODE, Integer.valueOf(Constants.HDR_CLOSE));
                if (cameraManager.isSupportHdrMode()) {
                    cameraManager.setHdrMode(false);
                }
            }
        }
    }

    public static void setFullShotExclusive(Context context, FVCameraManager cameraManager, int position) {
        if (position != 0) {
            if (((Integer) SPUtils.get(context, SharePrefConstant.LONG_EXPOSURE_MODE, Integer.valueOf(Constants.LONG_EXPOSURE_NONE))).intValue() != 106205) {
                Toast.makeText(context, C0853R.string.label_close_long_exposure_hint, 0).show();
                Util.sendIntEventMessge(Constants.DELAY_TAKE_PHOTO_0S);
                Util.sendIntEventMessge(Constants.LONG_EXPOSURE_CLOSE);
                cameraManager.changeCameraManagerMode(1);
                SPUtils.put(context, SharePrefConstant.LONG_EXPOSURE_MODE, Integer.valueOf(Constants.LONG_EXPOSURE_NONE));
            }
            if (((Integer) SPUtils.get(context, SharePrefConstant.BEAUTY_MODE, Integer.valueOf(Constants.BEAUTY_OPEN))).intValue() == 10400) {
                Toast.makeText(context, C0853R.string.label_close_beauty_hint, 0).show();
                cameraManager.changeCameraManagerMode(1);
                Util.sendIntEventMessge(Constants.BEAUTY_CLOSE);
                SPUtils.put(context, SharePrefConstant.BEAUTY_MODE, Integer.valueOf(Constants.BEAUTY_CLOSE));
                SPUtils.put(context, SharePrefConstant.BEAUTY_VALUE, 50);
            }
            if (((Integer) SPUtils.get(context, SharePrefConstant.FILTER_MODE, Integer.valueOf(Constants.FILTER_NONE_MODE))).intValue() != 10300) {
                Toast.makeText(context, C0853R.string.label_close_filter_hint, 0).show();
                cameraManager.changeCameraManagerMode(1);
                SPUtils.put(context, SharePrefConstant.FILTER_MODE, Integer.valueOf(Constants.FILTER_NONE_MODE));
            }
            if (((Integer) SPUtils.get(context, SharePrefConstant.HDR_MODE, Integer.valueOf(Constants.HDR_CLOSE))).intValue() == 106101) {
                Toast.makeText(context, C0853R.string.label_close_hdr_hint, 0).show();
                SPUtils.put(context, SharePrefConstant.HDR_MODE, Integer.valueOf(Constants.HDR_CLOSE));
                if (cameraManager.isSupportHdrMode()) {
                    cameraManager.setHdrMode(false);
                }
            }
            if (((Integer) SPUtils.get(context, SharePrefConstant.DELAY_TAKE_PHOTO_MODE, Integer.valueOf(Constants.DELAY_TAKE_PHOTO_0S))).intValue() != 100011) {
                Toast.makeText(context, C0853R.string.label_close_delay_photo_hint, 0).show();
                Util.sendIntEventMessge(Constants.DELAY_TAKE_PHOTO_0S);
                Util.sendIntEventMessge(Constants.DELAY_TAKE_PHOTO_CLOSE);
                SPUtils.put(context, SharePrefConstant.DELAY_TAKE_PHOTO_MODE, Integer.valueOf(Constants.DELAY_TAKE_PHOTO_0S));
            }
            if (CameraUtils.isFollowIng()) {
                Util.sendIntEventMessge(Constants.EXCLUSIVE_CLOSE_KCF);
            }
        }
    }

    public static void setLongExposureExclusive(Context context, FVCameraManager cameraManager, int position) {
        if (position == 0) {
            cameraManager.changeCameraManagerMode(1);
            return;
        }
        if (((Integer) SPUtils.get(context, SharePrefConstant.FULL_SHOT, Integer.valueOf(Constants.FULL_SHOT_NONE))).intValue() != 10024) {
            Toast.makeText(context, C0853R.string.label_close_full_shot_hint, 0).show();
            Util.sendIntEventMessge(Constants.FULL_SHOT_NONE);
            Util.sendIntEventMessge(Constants.FULL_SHOT_CLOSE);
            SPUtils.put(context, SharePrefConstant.FULL_SHOT, Integer.valueOf(Constants.FULL_SHOT_NONE));
        }
        if (((Integer) SPUtils.get(context, SharePrefConstant.BEAUTY_MODE, Integer.valueOf(Constants.BEAUTY_OPEN))).intValue() == 10400) {
            Toast.makeText(context, C0853R.string.label_close_beauty_hint, 0).show();
            Util.sendIntEventMessge(Constants.BEAUTY_CLOSE);
            SPUtils.put(context, SharePrefConstant.BEAUTY_MODE, Integer.valueOf(Constants.BEAUTY_CLOSE));
            SPUtils.put(context, SharePrefConstant.BEAUTY_VALUE, 50);
        }
        if (((Integer) SPUtils.get(context, SharePrefConstant.FILTER_MODE, Integer.valueOf(Constants.FILTER_NONE_MODE))).intValue() != 10300) {
            Toast.makeText(context, C0853R.string.label_close_filter_hint, 0).show();
            SPUtils.put(context, SharePrefConstant.FILTER_MODE, Integer.valueOf(Constants.FILTER_NONE_MODE));
        }
        if (((Integer) SPUtils.get(context, SharePrefConstant.HDR_MODE, Integer.valueOf(Constants.HDR_CLOSE))).intValue() == 106101) {
            Toast.makeText(context, C0853R.string.label_close_hdr_hint, 0).show();
            SPUtils.put(context, SharePrefConstant.HDR_MODE, Integer.valueOf(Constants.HDR_CLOSE));
            if (cameraManager.isSupportHdrMode()) {
                cameraManager.setHdrMode(false);
            }
        }
        if (((Integer) SPUtils.get(context, SharePrefConstant.DELAY_TAKE_PHOTO_MODE, Integer.valueOf(Constants.DELAY_TAKE_PHOTO_0S))).intValue() != 100011) {
            Toast.makeText(context, C0853R.string.label_close_delay_photo_hint, 0).show();
            Util.sendIntEventMessge(Constants.DELAY_TAKE_PHOTO_0S);
            Util.sendIntEventMessge(Constants.DELAY_TAKE_PHOTO_CLOSE);
            SPUtils.put(context, SharePrefConstant.DELAY_TAKE_PHOTO_MODE, Integer.valueOf(Constants.DELAY_TAKE_PHOTO_0S));
        }
        if (CameraUtils.isFollowIng()) {
            Util.sendIntEventMessge(Constants.EXCLUSIVE_CLOSE_KCF);
        }
        cameraManager.changeCameraManagerMode(0);
        cameraManager.setFilter(FilterType.NOFILTER);
        if (position == 1 || position == 2) {
        }
    }

    public static void setBeautyExclusive(Context context, final FVCameraManager cameraManager, boolean checked) {
        if (!checked) {
            cameraManager.changeCameraManagerMode(1);
            Util.sendIntEventMessge(Constants.BEAUTY_CLOSE);
            SPUtils.put(context, SharePrefConstant.BEAUTY_MODE, Integer.valueOf(Constants.BEAUTY_CLOSE));
            SPUtils.put(context, SharePrefConstant.BEAUTY_VALUE, 0);
            return;
        }
        if (((Integer) SPUtils.get(context, SharePrefConstant.FULL_SHOT, Integer.valueOf(Constants.FULL_SHOT_NONE))).intValue() != 10024) {
            Toast.makeText(context, C0853R.string.label_close_full_shot_hint, 0).show();
            Util.sendIntEventMessge(Constants.FULL_SHOT_NONE);
            Util.sendIntEventMessge(Constants.FULL_SHOT_CLOSE);
            SPUtils.put(context, SharePrefConstant.FULL_SHOT, Integer.valueOf(Constants.FULL_SHOT_NONE));
        }
        if (((Integer) SPUtils.get(context, SharePrefConstant.FILTER_MODE, Integer.valueOf(Constants.FILTER_NONE_MODE))).intValue() != 10300) {
            Toast.makeText(context, C0853R.string.label_close_filter_hint, 0).show();
            SPUtils.put(context, SharePrefConstant.FILTER_MODE, Integer.valueOf(Constants.FILTER_NONE_MODE));
        }
        if (((Integer) SPUtils.get(context, SharePrefConstant.LONG_EXPOSURE_MODE, Integer.valueOf(Constants.LONG_EXPOSURE_NONE))).intValue() != 106205) {
            Toast.makeText(context, C0853R.string.label_close_long_exposure_hint, 0).show();
            Util.sendIntEventMessge(Constants.DELAY_TAKE_PHOTO_0S);
            Util.sendIntEventMessge(Constants.LONG_EXPOSURE_CLOSE);
            SPUtils.put(context, SharePrefConstant.LONG_EXPOSURE_MODE, Integer.valueOf(Constants.LONG_EXPOSURE_NONE));
        }
        if (((Integer) SPUtils.get(context, SharePrefConstant.HDR_MODE, Integer.valueOf(Constants.HDR_CLOSE))).intValue() == 106101) {
            Toast.makeText(context, C0853R.string.label_close_hdr_hint, 0).show();
            SPUtils.put(context, SharePrefConstant.HDR_MODE, Integer.valueOf(Constants.HDR_CLOSE));
            if (cameraManager.isSupportHdrMode()) {
                cameraManager.setHdrMode(false);
            }
        }
        if (CameraUtils.isFollowIng()) {
            Util.sendIntEventMessge(Constants.EXCLUSIVE_CLOSE_KCF);
        }
        Util.sendIntEventMessge(Constants.BEAUTY_OPEN);
        SPUtils.put(context, SharePrefConstant.BEAUTY_MODE, Integer.valueOf(Constants.BEAUTY_OPEN));
        SPUtils.put(context, SharePrefConstant.BEAUTY_VALUE, 50);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                cameraManager.changeCameraManagerMode(0);
                cameraManager.setFilter(FilterType.BEAUTY);
                cameraManager.changeFilterIntensity(50);
            }
        }, 500);
    }

    public static void setHDRExclusive(final Context context, final FVCameraManager cameraManager, boolean checked) {
        if (checked) {
            if (((Integer) SPUtils.get(context, SharePrefConstant.FULL_SHOT, Integer.valueOf(Constants.FULL_SHOT_NONE))).intValue() != 10024) {
                Toast.makeText(context, C0853R.string.label_close_full_shot_hint, 0).show();
                Util.sendIntEventMessge(Constants.FULL_SHOT_NONE);
                Util.sendIntEventMessge(Constants.FULL_SHOT_CLOSE);
                SPUtils.put(context, SharePrefConstant.FULL_SHOT, Integer.valueOf(Constants.FULL_SHOT_NONE));
            }
            if (((Integer) SPUtils.get(context, SharePrefConstant.LONG_EXPOSURE_MODE, Integer.valueOf(Constants.LONG_EXPOSURE_NONE))).intValue() != 106205) {
                Toast.makeText(context, C0853R.string.label_close_long_exposure_hint, 0).show();
                Util.sendIntEventMessge(Constants.DELAY_TAKE_PHOTO_0S);
                Util.sendIntEventMessge(Constants.LONG_EXPOSURE_CLOSE);
                cameraManager.changeCameraManagerMode(1);
                SPUtils.put(context, SharePrefConstant.LONG_EXPOSURE_MODE, Integer.valueOf(Constants.LONG_EXPOSURE_NONE));
            }
            if (((Integer) SPUtils.get(context, SharePrefConstant.DELAY_TAKE_PHOTO_MODE, Integer.valueOf(Constants.DELAY_TAKE_PHOTO_0S))).intValue() != 100011) {
                Toast.makeText(context, C0853R.string.label_close_delay_photo_hint, 0).show();
                Util.sendIntEventMessge(Constants.DELAY_TAKE_PHOTO_0S);
                Util.sendIntEventMessge(Constants.DELAY_TAKE_PHOTO_CLOSE);
                SPUtils.put(context, SharePrefConstant.DELAY_TAKE_PHOTO_MODE, Integer.valueOf(Constants.DELAY_TAKE_PHOTO_0S));
            }
            if (((Integer) SPUtils.get(context, SharePrefConstant.FILTER_MODE, Integer.valueOf(Constants.FILTER_NONE_MODE))).intValue() != 10300) {
                Toast.makeText(context, C0853R.string.label_close_filter_hint, 0).show();
                cameraManager.changeCameraManagerMode(1);
                SPUtils.put(context, SharePrefConstant.FILTER_MODE, Integer.valueOf(Constants.FILTER_NONE_MODE));
            }
            if (((Integer) SPUtils.get(context, SharePrefConstant.BEAUTY_MODE, Integer.valueOf(Constants.BEAUTY_OPEN))).intValue() == 10400) {
                Toast.makeText(context, C0853R.string.label_close_beauty_hint, 0).show();
                cameraManager.changeCameraManagerMode(1);
                Util.sendIntEventMessge(Constants.BEAUTY_CLOSE);
                SPUtils.put(context, SharePrefConstant.BEAUTY_MODE, Integer.valueOf(Constants.BEAUTY_CLOSE));
                SPUtils.put(context, SharePrefConstant.BEAUTY_VALUE, 50);
            }
            SPUtils.put(context, SharePrefConstant.HDR_MODE, Integer.valueOf(Constants.HDR_OPEN));
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (cameraManager != null && cameraManager.isSupportHdrMode()) {
                        cameraManager.setHdrMode(true);
                        SPUtils.put(context, SharePrefConstant.HDR_MODE, Integer.valueOf(Constants.HDR_OPEN));
                    }
                }
            }, 500);
        } else if (cameraManager.isSupportHdrMode()) {
            cameraManager.setHdrMode(false);
            SPUtils.put(context, SharePrefConstant.HDR_MODE, Integer.valueOf(Constants.HDR_CLOSE));
        } else {
            SPUtils.put(context, SharePrefConstant.HDR_MODE, Integer.valueOf(Constants.HDR_CLOSE));
        }
    }

    public static void setCamHandModelExclusive(Context context, FVCameraManager cameraManager, boolean checked) {
        if (!checked) {
            Log.e("-----------", "------- 555 -- Checked No 22222 ------");
            SPUtils.put(context, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE));
            Util.sendIntEventMessge(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE);
            return;
        }
        Log.e("-----------", "------- 555 -- Checked Yes 22222 ------");
        SPUtils.put(context, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_OPEN));
        Util.sendIntEventMessge(Constants.LABEL_CAMERA_HAND_MODEL_OPEN);
    }

    public static boolean openFollowExclusive(Context context) {
        boolean exclusive = false;
        int fullShot = ((Integer) SPUtils.get(context, SharePrefConstant.FULL_SHOT, Integer.valueOf(Constants.FULL_SHOT_NONE))).intValue();
        int longExposure = ((Integer) SPUtils.get(context, SharePrefConstant.LONG_EXPOSURE_MODE, Integer.valueOf(Constants.LONG_EXPOSURE_NONE))).intValue();
        int filter = ((Integer) SPUtils.get(context, SharePrefConstant.FILTER_MODE, Integer.valueOf(Constants.FILTER_NONE_MODE))).intValue();
        int beauty = ((Integer) SPUtils.get(context, SharePrefConstant.BEAUTY_MODE, Integer.valueOf(Constants.BEAUTY_CLOSE))).intValue();
        if (fullShot != 10024) {
            exclusive = true;
            Toast.makeText(context, C0853R.string.label_full_shot_nonsupport_follow, 0).show();
        }
        if (longExposure != 106205) {
            exclusive = true;
            Toast.makeText(context, C0853R.string.label_long_exposure_nonsupport_follow, 0).show();
        }
        if (filter != 10300) {
            exclusive = true;
            Toast.makeText(context, C0853R.string.label_filter_nonsupport_follow, 0).show();
        }
        if (beauty == 10401) {
            return exclusive;
        }
        Toast.makeText(context, C0853R.string.label_beauty_nonsupport_follow, 0).show();
        return true;
    }

    public static boolean openStretchLensExclusive(Context context) {
        return CameraUtils.isFullShotIng() || CameraUtils.isFollowIng() || CameraUtils.isLongExposureIng();
    }
}
