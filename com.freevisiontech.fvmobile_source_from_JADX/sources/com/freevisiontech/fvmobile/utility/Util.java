package com.freevisiontech.fvmobile.utility;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Range;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.bean.FVModeSelectEvent;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.freevisiontech.fvmobile.utils.BlePtzParasConstant;
import com.freevisiontech.fvmobile.utils.MoveTimelapseUtil;
import com.freevisiontech.fvmobile.utils.SPUtil;
import com.freevisiontech.fvmobile.widget.view.IntentKey;
import com.freevisiontech.utils.ScreenOrientationUtil;
import com.google.android.exoplayer.util.MimeTypes;
import com.google.android.vending.expansion.downloader.Constants;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import org.greenrobot.eventbus.EventBus;

public class Util {
    public static int mEndAngle = 0;
    public static int mStartAngle = 0;
    public static int preOritation = 0;

    public static int dip2px(Context context, float dpValue) {
        return (int) ((dpValue * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static float px2dp(Context context, float pxVal) {
        return pxVal / context.getResources().getDisplayMetrics().density;
    }

    public static int px2sp(float pxValue, float fontScale) {
        return (int) ((pxValue / fontScale) + 0.5f);
    }

    public static Point getDeviceSize(Context context) {
        Display display = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        Point deviceSize = new Point();
        display.getSize(deviceSize);
        return deviceSize;
    }

    public static Point getDisplaySize(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getRealMetrics(dm);
        Point deviceSize = new Point();
        deviceSize.x = dm.widthPixels;
        deviceSize.y = dm.heightPixels;
        return deviceSize;
    }

    public static DisplayMetrics getDisplayMetrics(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getRealMetrics(dm);
        return dm;
    }

    public static boolean getMobileOrientation(Context context) {
        if (context.getResources().getConfiguration().orientation == 1) {
            return true;
        }
        return false;
    }

    public static boolean getPhoneVirtualKey(Context context) {
        return !KeyCharacterMap.deviceHasKey(4) || !KeyCharacterMap.deviceHasKey(3);
    }

    public static void hideBottomUIMenu(Activity context) {
        if (Build.VERSION.SDK_INT < 19) {
            context.getWindow().getDecorView().setSystemUiVisibility(8);
        } else if (Build.VERSION.SDK_INT >= 19) {
            context.getWindow().getDecorView().setSystemUiVisibility(4102);
        }
    }

    public static void setFullScreen(Activity context) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.flags |= 1024;
        context.getWindow().setAttributes(lp);
        context.getWindow().addFlags(512);
        hideBottomUIMenu(context);
    }

    public static void fullScreenImmersive(View view) {
        if (Build.VERSION.SDK_INT >= 19) {
            view.setSystemUiVisibility(5894);
        }
    }

    public static void setPrimaryDarkColor(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = activity.getWindow();
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(activity.getResources().getColor(C0853R.color.color_black1));
        }
    }

    public static boolean isZh(Context context) {
        if (context.getResources().getConfiguration().locale.getLanguage().endsWith("zh")) {
            return true;
        }
        return false;
    }

    public static boolean isZhFanTi(Context context) {
        String language = context.getResources().getConfiguration().locale.toString();
        if (language.indexOf("zh_tw") == -1 && language.indexOf("zh_hk") == -1 && language.indexOf("zh_mo") == -1 && language.indexOf("zh_TW") == -1 && language.indexOf("zh_HK") == -1 && language.indexOf("zh_MO") == -1) {
            return false;
        }
        return true;
    }

    public static boolean isDe(Context context) {
        if (context.getResources().getConfiguration().locale.getLanguage().endsWith("de")) {
            return true;
        }
        return false;
    }

    public static boolean isHan(Context context) {
        if (context.getResources().getConfiguration().locale.getLanguage().endsWith("ko")) {
            return true;
        }
        return false;
    }

    public static boolean isCs(Context context) {
        if (context.getResources().getConfiguration().locale.getLanguage().endsWith("cs")) {
            return true;
        }
        return false;
    }

    public static boolean isDa(Context context) {
        if (context.getResources().getConfiguration().locale.getLanguage().endsWith("da")) {
            return true;
        }
        return false;
    }

    public static boolean isFi(Context context) {
        if (context.getResources().getConfiguration().locale.getLanguage().endsWith("fi")) {
            return true;
        }
        return false;
    }

    public static boolean isNo(Context context) {
        if (context.getResources().getConfiguration().locale.getLanguage().endsWith("nb")) {
            return true;
        }
        return false;
    }

    public static boolean isFr(Context context) {
        if (context.getResources().getConfiguration().locale.getLanguage().endsWith("fr")) {
            return true;
        }
        return false;
    }

    public static boolean isSv(Context context) {
        if (context.getResources().getConfiguration().locale.getLanguage().endsWith("sv")) {
            return true;
        }
        return false;
    }

    public static boolean isPovTracking(Context context) {
        if (BlePtzParasConstant.SET_PTZ_FOLLOW_MODE != 3) {
            return false;
        }
        Toast.makeText(context, C0853R.string.text_fv_ptz_setting_pov_check_tracking, 0).show();
        return true;
    }

    public static boolean isPovReverPano(Context context) {
        if (BlePtzParasConstant.SET_PTZ_FOLLOW_MODE != 3) {
            return true;
        }
        Toast.makeText(context, C0853R.string.text_fv_ptz_setting_pov_check_pano, 0).show();
        return false;
    }

    public static boolean isPovReverTime(Context context) {
        if (BlePtzParasConstant.SET_PTZ_FOLLOW_MODE != 3) {
            return true;
        }
        Toast.makeText(context, C0853R.string.text_fv_ptz_setting_pov_check_time, 0).show();
        return false;
    }

    public static boolean isPovReverLapse(Context context) {
        if (BlePtzParasConstant.SET_PTZ_FOLLOW_MODE != 3) {
            return true;
        }
        Toast.makeText(context, C0853R.string.text_fv_ptz_setting_pov_check_lapse, 0).show();
        return false;
    }

    public static boolean isPovReverTimeLapse(Context context) {
        if (MoveTimelapseUtil.getMotionLapseTimeYesOrNo()) {
            return isPovReverTime(context);
        }
        return isPovReverLapse(context);
    }

    public static String zhToEnglishMonth(int month, Context mContext) {
        String yue = mContext.getString(C0853R.string.file_month_january);
        switch (month) {
            case 1:
                if (isDe(mContext)) {
                    return mContext.getString(C0853R.string.file_month_january);
                }
                return mContext.getString(C0853R.string.file_month_january);
            case 2:
                if (isDe(mContext)) {
                    return mContext.getString(C0853R.string.file_month_february);
                }
                return mContext.getString(C0853R.string.file_month_february);
            case 3:
                if (isDe(mContext)) {
                    return mContext.getString(C0853R.string.file_month_march);
                }
                return mContext.getString(C0853R.string.file_month_march);
            case 4:
                if (isDe(mContext)) {
                    return mContext.getString(C0853R.string.file_month_april);
                }
                return mContext.getString(C0853R.string.file_month_april);
            case 5:
                if (isDe(mContext)) {
                    return mContext.getString(C0853R.string.file_month_may);
                }
                return mContext.getString(C0853R.string.file_month_may);
            case 6:
                if (isDe(mContext)) {
                    return mContext.getString(C0853R.string.file_month_june);
                }
                return mContext.getString(C0853R.string.file_month_june);
            case 7:
                if (isDe(mContext)) {
                    return mContext.getString(C0853R.string.file_month_july);
                }
                return mContext.getString(C0853R.string.file_month_july);
            case 8:
                if (isDe(mContext)) {
                    return mContext.getString(C0853R.string.file_month_august);
                }
                return mContext.getString(C0853R.string.file_month_august);
            case 9:
                if (isDe(mContext)) {
                    return mContext.getString(C0853R.string.file_month_september);
                }
                return mContext.getString(C0853R.string.file_month_september);
            case 10:
                if (isDe(mContext)) {
                    return mContext.getString(C0853R.string.file_month_october);
                }
                return mContext.getString(C0853R.string.file_month_october);
            case 11:
                if (isDe(mContext)) {
                    return mContext.getString(C0853R.string.file_month_november);
                }
                return mContext.getString(C0853R.string.file_month_november);
            case 12:
                if (isDe(mContext)) {
                    return mContext.getString(C0853R.string.file_month_december);
                }
                return mContext.getString(C0853R.string.file_month_december);
            default:
                return yue;
        }
    }

    public static void sendIntEventMessge(int message) {
        FVModeSelectEvent fvMode = new FVModeSelectEvent();
        fvMode.setMode(message);
        EventBus.getDefault().post(fvMode);
    }

    public static void sendIntEventMessge(int command, Bitmap bitmap) {
        FVModeSelectEvent fvMode = new FVModeSelectEvent();
        fvMode.setMode(command);
        fvMode.setMessage(bitmap);
        EventBus.getDefault().post(fvMode);
    }

    public static void sendIntEventMessge(int command, MotionEvent motionEvent) {
        FVModeSelectEvent fvMode = new FVModeSelectEvent();
        fvMode.setMode(command);
        fvMode.setMessage(motionEvent);
        EventBus.getDefault().post(fvMode);
    }

    public static void sendIntEventMessge(int command, List list) {
        FVModeSelectEvent fvMode = new FVModeSelectEvent();
        fvMode.setMode(command);
        fvMode.setList(list);
        EventBus.getDefault().post(fvMode);
    }

    public static void sendIntEventMessge(int command, String path) {
        FVModeSelectEvent fvMode = new FVModeSelectEvent();
        fvMode.setMode(command);
        fvMode.setMessage(path);
        EventBus.getDefault().post(fvMode);
    }

    public static void sendIntEventMessge(int command, Rect rect) {
        FVModeSelectEvent fvMode = new FVModeSelectEvent();
        fvMode.setMode(command);
        fvMode.setRect(rect);
        EventBus.getDefault().post(fvMode);
    }

    public static boolean isEmpty(String string) {
        return string == null || string.equals("") || string.equals("null");
    }

    public static long timeToSec(String timeStr) {
        int hh;
        int mi;
        int ss;
        if (isEmpty(timeStr)) {
            return -1;
        }
        String timeStrNew = Pattern.compile("[^0-9]").matcher(timeStr).replaceAll("").trim();
        if (timeStrNew.length() > 4) {
            hh = Integer.parseInt(timeStrNew.substring(0, timeStrNew.length() - 4));
        } else {
            hh = 0;
        }
        if (timeStrNew.length() > 2) {
            mi = Integer.parseInt(timeStrNew.substring(timeStrNew.length() - 4, timeStrNew.length() - 2));
        } else {
            mi = 0;
        }
        if (timeStrNew.length() > 0) {
            ss = Integer.parseInt(timeStrNew.substring(timeStrNew.length() - 2, timeStrNew.length()));
        } else {
            ss = 0;
        }
        return (long) ((hh * 60 * 60) + (mi * 60) + ss);
    }

    public static String secToTime(int time) {
        if (time <= 0) {
            return "00:00:00";
        }
        int minute = time / 60;
        if (minute < 60) {
            return unitFormat(0) + ":" + unitFormat(minute) + ":" + unitFormat(time % 60);
        }
        int hour = minute / 60;
        if (hour > 99) {
            return "99:59:59";
        }
        int minute2 = minute % 60;
        return unitFormat(hour) + ":" + unitFormat(minute2) + ":" + unitFormat((time - (hour * 3600)) - (minute2 * 60));
    }

    public static String unitFormat(int i) {
        if (i < 0 || i >= 10) {
            return "" + i;
        }
        return "0" + Integer.toString(i);
    }

    public static String getSystemModel() {
        return Build.MODEL;
    }

    public static void updateGallery(Context context, String filepath, String mimeType) {
        if (!isEmpty(filepath)) {
            try {
                MediaScannerConnection.scanFile(context, new String[]{new File(filepath).getAbsolutePath()}, new String[]{mimeType}, (MediaScannerConnection.OnScanCompletedListener) null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void updateGalleryForVideo(Context context, String filepath) {
        try {
            if (!isEmpty(filepath)) {
                long size = new File(filepath).length();
                String name = filepath.substring(filepath.lastIndexOf(47) + 1, filepath.length());
                ContentValues localContentValues = new ContentValues();
                localContentValues.put("_data", filepath);
                localContentValues.put("title", name);
                localContentValues.put("mime_type", MimeTypes.VIDEO_MP4);
                localContentValues.put("_size", Long.valueOf(size));
                localContentValues.put("_display_name", name);
                context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, localContentValues);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File getOutputMediaFile(int type, int catchType, Context context) {
        File mediaStorageDir;
        if (catchType == 1) {
            mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "fvmobile");
        } else {
            mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "fvmobile/Catch");
        }
        if (mediaStorageDir.exists() || mediaStorageDir.mkdirs()) {
            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
            String parentPath = getParentPath(context);
            if (type == 1) {
                if (getSDCardPath() == null) {
                    return new File(mediaStorageDir.getPath() + File.separator + "IMG" + timeStamp + ".jpg");
                }
                return new File(parentPath + File.separator + "IMG" + timeStamp + ".jpg");
            } else if (type != 3) {
                return null;
            } else {
                if (getSDCardPath() == null) {
                    return new File(mediaStorageDir.getPath() + File.separator + "VID" + timeStamp + ".mp4");
                }
                return new File(parentPath + File.separator + "VID" + timeStamp + ".mp4");
            }
        } else {
            Log.d("MyCameraApp", "failed to create directory");
            return null;
        }
    }

    public static File getOutputMoveTimeLapseMediaFile(int type, Context context) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "fvmobile");
        if (mediaStorageDir.exists() || mediaStorageDir.mkdirs()) {
            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
            String parentPath = getParentPath(context);
            if (type == 1) {
                if (getSDCardPath() == null) {
                    return new File(mediaStorageDir.getPath() + File.separator + "IMG" + timeStamp + ".jpg");
                }
                return new File(parentPath + File.separator + "IMG" + timeStamp + ".jpg");
            } else if (type != 3) {
                return null;
            } else {
                if (getSDCardPath() == null) {
                    return new File(mediaStorageDir.getPath() + File.separator + "VID" + timeStamp + "TimeLapse.mp4");
                }
                return new File(parentPath + File.separator + "VID" + timeStamp + "TimeLapse.mp4");
            }
        } else {
            Log.d("MyCameraApp", "failed to create directory");
            return null;
        }
    }

    public static File getOutputMoveLapseMediaFile(int type, Context context) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "fvmobile");
        if (mediaStorageDir.exists() || mediaStorageDir.mkdirs()) {
            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
            String parentPath = getParentPath(context);
            if (type == 1) {
                if (getSDCardPath() == null) {
                    return new File(mediaStorageDir.getPath() + File.separator + "IMG" + timeStamp + ".jpg");
                }
                return new File(parentPath + File.separator + "IMG" + timeStamp + ".jpg");
            } else if (type != 3) {
                return null;
            } else {
                if (getSDCardPath() == null) {
                    return new File(mediaStorageDir.getPath() + File.separator + "VID" + timeStamp + ".mp4");
                }
                return new File(parentPath + File.separator + "VID" + timeStamp + ".mp4");
            }
        } else {
            Log.d("MyCameraApp", "failed to create directory");
            return null;
        }
    }

    public static String getOutputMediaFile(Context context) {
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/fvmobile/";
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        if (getSDCardPath() == null) {
            return filePath + "IMG" + timeStamp + ".jpg";
        }
        return getParentPath(context) + "IMG" + timeStamp + ".jpg";
    }

    public static String getOutputPhotoFile(Context context) {
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/fvmobile/";
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        if (getSDCardPath() == null) {
            return filePath + "IMG" + timeStamp + "Edit.jpg";
        }
        return getParentPath(context) + "IMG" + timeStamp + "Edit.jpg";
    }

    public static String getOutputPhotoFileCatch(Context context) {
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/fvmobile/Catch/";
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        if (getSDCardPath() == null) {
            return filePath + "IMG" + timeStamp + "Edit.jpg";
        }
        String parentPath = getParentPath(context);
        if (!parentPath.contains("Catch")) {
            parentPath = parentPath + "Catch/";
            File file = new File(parentPath);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        return parentPath + "IMG" + timeStamp + "Edit.jpg";
    }

    public static String getOutputPhotoMosaicFile(Context context) {
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/fvmobile/";
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        if (getSDCardPath() == null) {
            return filePath + "IMG" + timeStamp + "mosaic.jpg";
        }
        return getParentPath(context) + "IMG" + timeStamp + "mosaic.jpg";
    }

    public static String getOutputPhotoMosaicFileCatch(Context context) {
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/fvmobile/Catch/";
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        if (getSDCardPath() == null) {
            return filePath + "IMG" + timeStamp + "mosaic.jpg";
        }
        return getParentPath(context) + "IMG" + timeStamp + "mosaic.jpg";
    }

    public static Bitmap rotateBitmapJudgment(Context context, Bitmap bitmap) {
        int orientation = ScreenOrientationUtil.getInstance().getOrientation();
        if (((Integer) SPUtils.get(context, SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() != 10102) {
            int longExposure = ((Integer) SPUtils.get(context, SharePrefConstant.LONG_EXPOSURE_MODE, Integer.valueOf(Constants.LONG_EXPOSURE_NONE))).intValue();
            int beautyMode = ((Integer) SPUtils.get(context, SharePrefConstant.BEAUTY_MODE, Integer.valueOf(Constants.BEAUTY_CLOSE))).intValue();
            int filterMode = ((Integer) SPUtils.get(context, SharePrefConstant.FILTER_MODE, Integer.valueOf(Constants.FILTER_NONE_MODE))).intValue();
            if (longExposure == 106205 && beautyMode != 10400 && filterMode == 10300) {
                if (orientation == 0) {
                    return bitmap;
                }
                if (orientation == 90) {
                    return rotateBitmap(bitmap, -90);
                }
                if (orientation == 180) {
                    return rotateBitmap(bitmap, 180);
                }
                return rotateBitmap(bitmap, 90);
            } else if (orientation == 0) {
                return bitmap;
            } else {
                if (orientation == 90) {
                    return rotateBitmap(bitmap, 90);
                }
                if (orientation == 180) {
                    return rotateBitmap(bitmap, 180);
                }
                return rotateBitmap(bitmap, -90);
            }
        } else if (orientation == 0) {
            return bitmap;
        } else {
            if (orientation == 90) {
                return rotateBitmap(bitmap, 90);
            }
            if (orientation == 180) {
                return rotateBitmap(bitmap, 180);
            }
            return rotateBitmap(bitmap, -90);
        }
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        if (degrees == 0 || bitmap == null) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate((float) degrees, (float) (bitmap.getWidth() / 2), (float) (bitmap.getHeight() / 2));
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (bitmap == null) {
            return createBitmap;
        }
        bitmap.recycle();
        return createBitmap;
    }

    public static void dealAngle(int orientation, View... views) {
        switch (preOritation) {
            case 0:
                switch (orientation) {
                    case 90:
                        viewRotation(-90, views);
                        break;
                    case 180:
                        viewRotation(-180, views);
                        break;
                    case 270:
                        viewRotation(90, views);
                        break;
                }
            case 90:
                switch (orientation) {
                    case 0:
                        viewRotation(90, views);
                        break;
                    case 180:
                        viewRotation(-90, views);
                        break;
                    case 270:
                        viewRotation(-180, views);
                        break;
                }
            case 180:
                switch (orientation) {
                    case 0:
                        viewRotation(-180, views);
                        break;
                    case 90:
                        viewRotation(90, views);
                        break;
                    case 270:
                        viewRotation(-90, views);
                        break;
                }
            case 270:
                switch (orientation) {
                    case 0:
                        viewRotation(-90, views);
                        break;
                    case 90:
                        viewRotation(-180, views);
                        break;
                    case 180:
                        viewRotation(90, views);
                        break;
                }
        }
        preOritation = orientation;
    }

    private static void viewRotation(int angle, View... views) {
        for (View view : views) {
            startRotation(view, angle);
        }
    }

    private static void startRotation(View view, int angle) {
        mEndAngle = mStartAngle + angle;
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", new float[]{(float) mStartAngle, (float) mEndAngle});
        animator.setDuration(300);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Util.mStartAngle = Util.mEndAngle;
            }
        });
    }

    public static void deleteCatchPictrue() {
        File[] files;
        File catchPictrueDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "/fvmobile/Catch/");
        if (catchPictrueDir.exists() && (files = catchPictrueDir.listFiles()) != null && files.length > 0) {
            for (File file : files) {
                file.delete();
            }
        }
    }

    public static String saveBitmapToDrawablePath() {
        return (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/fvmobile/Catch/") + "IMG" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + ".jpg";
    }

    public static List getScaleShutter(Range<Long> range) {
        return null;
    }

    public static int getDrawScalePaint(int k2) {
        int i = k2;
        return Integer.parseInt(new ArrayList<>(Arrays.asList(new String[]{BleConstant.ISO, BleConstant.f1095WB, BleConstant.FOCUS, "5", "6", "8", "10", "11", "13", "15", "20", "23", "24", "25", "30", "40", "45", "48", "50", "60", "72", "80", "90", "100", "120", "125", "150", "160", "180", "200", "240", "250", "300", "320", "350", "360", "400", "480", "500", "600", "640", "720", "750", "800", "1000", "1200", "1250", "1500", "1600", "2000", "2400", "2500", "3000", "3200", "3600", "4000", "4800", "5000", "6000", "7000", "7200", "8000"})).get((k2 / 10) - 1));
    }

    public static int getDrawScaleISOPaint(int k2) {
        int i = k2;
        return Integer.parseInt(CameraUtils.getScaleIsoList().get((k2 / 10) - 1));
    }

    public static int getDrawScaleShutterNumNear(int k3) {
        int min;
        int nearNum = k3;
        int i = k3;
        List<String> list = new ArrayList<>(Arrays.asList(new String[]{BleConstant.ISO, BleConstant.f1095WB, BleConstant.FOCUS, "5", "6", "8", "10", "11", "13", "15", "20", "23", "24", "25", "30", "40", "45", "48", "50", "60", "72", "80", "90", "100", "120", "125", "150", "160", "180", "200", "240", "250", "300", "320", "350", "360", "400", "480", "500", "600", "640", "720", "750", "800", "1000", "1200", "1250", "1500", "1600", "2000", "2400", "2500", "3000", "3200", "3600", "4000", "4800", "5000", "6000", "7000", "7200", "8000"}));
        if (k3 <= 20) {
            nearNum = 20;
        } else if (k3 >= 8000) {
            nearNum = 8000;
        } else {
            int i2 = 0;
            while (true) {
                if (i2 >= list.size()) {
                    break;
                } else if (k3 < Integer.valueOf(list.get(i2)).intValue()) {
                    int max = Integer.valueOf(list.get(i2)).intValue();
                    int cen = Integer.valueOf(list.get(i2 - 1)).intValue();
                    if (i2 >= 2) {
                        min = Integer.valueOf(list.get(i2 - 2)).intValue();
                    } else {
                        min = 0;
                    }
                    if (k3 - cen > (max - min) / 4) {
                        nearNum = max;
                    } else {
                        nearNum = cen;
                    }
                    if (k3 == cen) {
                        nearNum = cen;
                    } else if (k3 == max) {
                        nearNum = max;
                    }
                } else {
                    i2++;
                }
            }
        }
        int nearI = 0;
        int i3 = 0;
        while (true) {
            if (i3 >= list.size()) {
                break;
            } else if (Integer.valueOf(list.get(i3)).intValue() == nearNum) {
                nearI = i3;
                break;
            } else {
                i3++;
            }
        }
        return nearI + 1;
    }

    public static int getDrawScaleISONumNear(int k3) {
        int min;
        int nearNum = k3;
        int i = k3;
        new ArrayList(Arrays.asList(new String[]{"29", "40", "50", "64", "80", "100", "125", "160", "200", "250", "320", "400", "500", "640", "800", "1000", "1250", "1600", "1856", "2000", "2200", "2300"}));
        List<String> list = CameraUtils.getScaleIsoList();
        if (list.size() == 0 || list == null || list.isEmpty()) {
            list = new ArrayList<>(Arrays.asList(new String[]{"29", "40", "50", "64", "80", "100", "125", "160", "200", "250", "320", "400", "500", "640", "800", "1000", "1250", "1600", "1856", "2000", "2200", "2300"}));
        }
        if (list != null && !list.isEmpty() && list != null && list.get(0) != null) {
            int firstNum = Integer.valueOf(list.get(0)).intValue();
            if (k3 <= firstNum) {
                nearNum = firstNum;
            } else if (k3 >= 2300) {
                nearNum = 2300;
            } else {
                int i2 = 0;
                while (true) {
                    if (i2 >= list.size()) {
                        break;
                    } else if (k3 < Integer.valueOf(list.get(i2)).intValue()) {
                        int max = Integer.valueOf(list.get(i2)).intValue();
                        int cen = Integer.valueOf(list.get(i2 - 1)).intValue();
                        if (i2 >= 2) {
                            min = Integer.valueOf(list.get(i2 - 2)).intValue();
                        } else {
                            min = 0;
                        }
                        if (k3 - cen > (max - min) / 4) {
                            nearNum = max;
                        } else {
                            nearNum = cen;
                        }
                        if (k3 == cen) {
                            nearNum = cen;
                        } else if (k3 == max) {
                            nearNum = max;
                        }
                    } else {
                        i2++;
                    }
                }
            }
        }
        int nearI = 0;
        int i3 = 0;
        while (true) {
            if (i3 >= list.size()) {
                break;
            } else if (Integer.valueOf(list.get(i3)).intValue() == nearNum) {
                nearI = i3;
                break;
            } else {
                i3++;
            }
        }
        return nearI + 1;
    }

    public static int getDrawScaleEVPaint(int k2) {
        int i = k2;
        return Integer.parseInt(new ArrayList<>(Arrays.asList(new String[]{"-8", "-7", "-6", "-5", "-4", "-3", "-2", "-1", "0", BleConstant.SHUTTER, BleConstant.ISO, BleConstant.f1095WB, BleConstant.FOCUS, "5", "6", "7", "8"})).get(k2 / 10));
    }

    public static int getDrawScaleWBPaint(int k2) {
        int i = k2;
        return Integer.parseInt(new ArrayList<>(Arrays.asList(new String[]{"2000", "3000", "4000", "5000", "6000", "7000", "8000"})).get((k2 / 10) - 1));
    }

    public static int getDrawScaleMFMax() {
        return (((int) (10.0d * CameraUtils.getScaleScrollViewMFMaxNums())) / 10) * 10;
    }

    public static int getDrawScaleWBMax() {
        return (((int) (100.0d * CameraUtils.getScaleScrollViewWBMaxNums())) / 10) * 10;
    }

    public static int getDrawScaleEVMax() {
        return (((int) (10.0d * (CameraUtils.getScaleScrollViewEVMaxNums() * 2.0d))) / 10) * 10;
    }

    public static int getDrawScaleWTMax() {
        int mf = (int) (10.0d * CameraUtils.getScaleScrollViewWTMaxNums());
        int i = (mf / 10) * 10;
        return mf;
    }

    public static double setDrawScaleMFGrid(float focusMax, int sca) {
        return (((double) sca) * ((double) focusMax)) / 100.0d;
    }

    public static boolean versionRelease() {
        int re = Integer.valueOf(filterNumber(Build.VERSION.RELEASE)).intValue();
        if (re < 10) {
            re *= 1000;
        } else if (re < 100) {
            re *= 100;
        } else if (re < 1000) {
            re *= 10;
        }
        if (re < 8000) {
            return false;
        }
        return true;
    }

    public static String filterNumber(String number) {
        return number.replaceAll("[^(0-9)]", "");
    }

    public static String getSDCardPath() {
        String path = null;
        try {
            File file = new File("storage");
            if (!file.exists()) {
                return null;
            }
            String[] list = file.list();
            if (list == null) {
                return "";
            }
            for (int i = 0; i < list.length; i++) {
                if (list[i].indexOf(Constants.FILENAME_SEQUENCE_SEPARATOR) > 0) {
                    return "/storage/" + list[i];
                }
                if (!"emulated".equals(list[i]) && !"self".equals(list[i])) {
                    path = "/storage/" + list[i];
                }
            }
            return path;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getParentPath(Context context) {
        if (getSDCardPath() != null) {
            return (String) SPUtils.get(context, SharePrefConstant.SAVE_STORAGE_SD_CARD_PATH, Environment.getExternalStorageDirectory().toString() + File.separator + "DCIM/fvmobile/");
        }
        return IntentKey.FILE_PATH;
    }

    public static void updataMediaStore(Context context, File file) {
        if (Build.VERSION.SDK_INT >= 19) {
            MediaScannerConnection.scanFile(context, new String[]{Environment.getExternalStorageDirectory().toString()}, (String[]) null, (MediaScannerConnection.OnScanCompletedListener) null);
            MediaScannerConnection.scanFile(context, new String[]{file.getAbsolutePath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                }
            });
            return;
        }
        context.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.parse("file://" + Environment.getExternalStorageDirectory())));
    }

    public static boolean isPhoneCanCharge(Context context) {
        boolean canCharge = false;
        ArrayList<String> tempGetList = (ArrayList) SPUtils.getDataList(context, SharePrefConstant.PTZ_FM300_CAN_CHARGE_BY_WIRELESS_LIST);
        String brand = Build.BRAND;
        String model = Build.MODEL;
        if (!(brand == null || model == null || tempGetList == null)) {
            Iterator<String> it = tempGetList.iterator();
            while (it.hasNext()) {
                String[] canChargeModelInfo = it.next().split(",");
                if (brand.equals(canChargeModelInfo[0]) && model.equals(canChargeModelInfo[1])) {
                    canCharge = true;
                }
            }
        }
        return canCharge;
    }

    public static boolean isSupportWirelessCharge(Context context) {
        return ((Integer) SPUtil.getParam(context, "save_storage_path", 0)).intValue() == 1;
    }
}
