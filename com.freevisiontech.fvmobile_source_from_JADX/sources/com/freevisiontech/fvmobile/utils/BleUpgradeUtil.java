package com.freevisiontech.fvmobile.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.support.p001v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;
import com.freevisiontech.fvmobile.application.MyApplication;
import com.freevisiontech.fvmobile.bean.FVUpgradeBean;
import com.freevisiontech.fvmobile.bean.network.WirelessModelList;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.freevisiontech.fvmobile.common.InternetUrls;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utility.SPUtils;
import com.freevisiontech.fvmobile.utility.SharePrefConstant;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okgo.request.Request;
import com.vise.log.ViseLog;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;

public class BleUpgradeUtil {
    public static final String APKNAME = "fvMobile.apk";
    public static final String CAN_CHARGE_BY_WIRELESS_LIST_URL = "http://app.freevisiontech.com:8080/oss/chargeVersion/ChargeQueryListForApp.jspx";
    public static final String GMUNAME = "gmu.bin";
    public static final String IMUNAME = "imu.bin";
    public static final int TYPE_GMU = 1;
    public static final int TYPE_IMU = 2;
    private static BleUpgradeUtil bleUpgradeUtil;
    public String apk_releasenotes;
    /* access modifiers changed from: private */
    public FVUpgradeBean fvUpgradeBean;
    public int gmu_crc32;
    public int gmu_isforce;
    public String gmu_releasenotes;
    public String gmu_verifycode;
    public String gmu_version;
    public int imu_crc32;
    public int imu_isforce;
    public String imu_releasenotes;
    public String imu_verifycode;
    public String imu_version;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public DownloadListener mDownloadListener;
    /* access modifiers changed from: private */
    public AppVersionUpgradeListener mListener;
    /* access modifiers changed from: private */
    public String ptzType;

    public interface AppVersionUpgradeListener {
        void isAppNeedDownload(boolean z);

        void isGmuNeedDownoad(boolean z);

        void isImuNeedDownoad(boolean z);
    }

    public interface DownloadListener {
        void onError(String str);

        void onFinish();

        void onProgress(Progress progress);

        void onStart();

        void onSuccess();
    }

    public static BleUpgradeUtil getInstance() {
        if (bleUpgradeUtil == null) {
            synchronized (BleUpgradeUtil.class) {
                if (bleUpgradeUtil == null) {
                    bleUpgradeUtil = new BleUpgradeUtil();
                }
            }
        }
        return bleUpgradeUtil;
    }

    public void init(Context context) {
        this.mContext = context;
    }

    public FVUpgradeBean getUpgradeBean() {
        return this.fvUpgradeBean;
    }

    public void requestAppInfo(AppVersionUpgradeListener listener) {
        this.mListener = listener;
        this.ptzType = MyApplication.CURRENT_PTZ_TYPE;
        String appInfoUrl = InternetUrls.FM_200_URL;
        if (this.ptzType.equals(BleConstant.FM_300)) {
            appInfoUrl = InternetUrls.FM_300_URL;
        } else if (this.ptzType.equals(BleConstant.FM_210)) {
            appInfoUrl = InternetUrls.FM_210_URL;
        }
        ((PostRequest) ((PostRequest) OkGo.post(appInfoUrl).tag(this)).client(new OkHttpClient().newBuilder().readTimeout(1000, TimeUnit.MILLISECONDS).writeTimeout(1000, TimeUnit.MILLISECONDS).connectTimeout(1000, TimeUnit.MILLISECONDS).build())).execute(new StringCallback() {
            public void onSuccess(Response<String> response) {
                boolean z;
                boolean z2;
                if (response != null) {
                    ViseLog.m1466e("服务器返回成功");
                    Gson gson = new Gson();
                    ViseLog.m1466e("response.body(): " + response.body());
                    FVUpgradeBean unused = BleUpgradeUtil.this.fvUpgradeBean = (FVUpgradeBean) gson.fromJson(response.body(), FVUpgradeBean.class);
                    ViseLog.m1466e("fvUpgradeBean: " + BleUpgradeUtil.this.fvUpgradeBean);
                    Log.i("Kbein", "onSuccess: ---> fvUpgradeBean");
                    if (BleUpgradeUtil.this.fvUpgradeBean != null) {
                        StringBuilder append = new StringBuilder().append("fvUpgradeBean.getTotal() > 0 :");
                        if (BleUpgradeUtil.this.fvUpgradeBean.getTotal() > 0) {
                            z = true;
                        } else {
                            z = false;
                        }
                        ViseLog.m1466e(append.append(z).toString());
                        if (BleUpgradeUtil.this.fvUpgradeBean.getTotal() > 0) {
                            for (int i = 0; i < BleUpgradeUtil.this.fvUpgradeBean.getRows().size(); i++) {
                                if (BleUpgradeUtil.this.fvUpgradeBean.getRows().get(i).getApptype().equals("android_mobile")) {
                                    SPUtil.setParam(BleUpgradeUtil.this.mContext, "apkPath", BleUpgradeUtil.this.fvUpgradeBean.getRows().get(i).getApppath());
                                    BleUpgradeUtil.this.apk_releasenotes = BleUpgradeUtil.this.fvUpgradeBean.getRows().get(i).getReleasenotes();
                                    String appver = BleUpgradeUtil.this.fvUpgradeBean.getRows().get(i).getAppver();
                                    String apkVersion = BleUpgradeUtil.getAppVersionName(BleUpgradeUtil.this.mContext);
                                    Log.i("Kbein", "onSuccess: ------- " + appver + "<----->" + apkVersion);
                                    if (CameraUtils.getApkIsFromNormal()) {
                                        if (SPUtil.isEmpty(apkVersion)) {
                                            if (BleUpgradeUtil.this.mListener != null) {
                                                BleUpgradeUtil.this.mListener.isAppNeedDownload(false);
                                            }
                                        } else if (appver.compareTo(apkVersion) > 0) {
                                            if (BleUpgradeUtil.this.mListener != null) {
                                                BleUpgradeUtil.this.mListener.isAppNeedDownload(true);
                                            }
                                        } else if (BleUpgradeUtil.this.mListener != null) {
                                            BleUpgradeUtil.this.mListener.isAppNeedDownload(false);
                                        }
                                    } else if (BleUpgradeUtil.this.mListener != null) {
                                        BleUpgradeUtil.this.mListener.isAppNeedDownload(false);
                                    }
                                } else if (BleUpgradeUtil.this.fvUpgradeBean.getRows().get(i).getApptype().equals("gmu_mobile") || BleUpgradeUtil.this.fvUpgradeBean.getRows().get(i).getApptype().equals("gmu_fm300") || BleUpgradeUtil.this.fvUpgradeBean.getRows().get(i).getApptype().equals("gmu_fm210")) {
                                    SPUtil.setParam(BleUpgradeUtil.this.mContext, BleUpgradeUtil.this.ptzType + "gmuPath", BleUpgradeUtil.this.fvUpgradeBean.getRows().get(i).getApppath());
                                    SPUtil.setParam(BleUpgradeUtil.this.mContext, BleUpgradeUtil.this.ptzType + "gmuIsforce", BleUpgradeUtil.this.fvUpgradeBean.getRows().get(i).getIsforce());
                                    BleUpgradeUtil.this.gmu_releasenotes = BleUpgradeUtil.this.fvUpgradeBean.getRows().get(i).getReleasenotes();
                                    BleUpgradeUtil.this.gmu_version = BleUpgradeUtil.this.fvUpgradeBean.getRows().get(i).getAppver();
                                    BleUpgradeUtil.this.gmu_verifycode = BleUpgradeUtil.this.fvUpgradeBean.getRows().get(i).getVerifycode();
                                    String gmuVersion = SPUtil.getParam(BleUpgradeUtil.this.mContext, BleUpgradeUtil.this.ptzType + "gmuVersion", "").toString();
                                    ViseLog.m1466e("gmuVersion.equals(\"\"): " + gmuVersion.equals(""));
                                    ViseLog.m1466e("gmuVersion:" + gmuVersion);
                                    ViseLog.m1466e("gmu_version:" + BleUpgradeUtil.this.gmu_version);
                                    StringBuilder append2 = new StringBuilder().append("gmu_version.compareTo(gmuVersion) > 0'");
                                    if (BleUpgradeUtil.this.gmu_version.compareTo(gmuVersion) > 0) {
                                        z2 = true;
                                    } else {
                                        z2 = false;
                                    }
                                    ViseLog.m1466e(append2.append(z2).toString());
                                    if (gmuVersion.equals("")) {
                                        SPUtil.setParam(BleUpgradeUtil.this.mContext, BleUpgradeUtil.this.ptzType + "gmuVersion", BleUpgradeUtil.this.gmu_version);
                                        if (BleUpgradeUtil.this.mListener != null) {
                                            BleUpgradeUtil.this.mListener.isGmuNeedDownoad(true);
                                        }
                                    } else if (!BleUpgradeUtil.this.gmu_version.equals(gmuVersion)) {
                                        if (BleUpgradeUtil.this.mListener != null) {
                                            BleUpgradeUtil.this.mListener.isGmuNeedDownoad(true);
                                        }
                                    } else if (BleUpgradeUtil.this.mListener != null) {
                                        BleUpgradeUtil.this.mListener.isGmuNeedDownoad(false);
                                    }
                                } else if (BleUpgradeUtil.this.fvUpgradeBean.getRows().get(i).getApptype().equals("imu_mobile") || BleUpgradeUtil.this.fvUpgradeBean.getRows().get(i).getApptype().equals("imu_fm300") || BleUpgradeUtil.this.fvUpgradeBean.getRows().get(i).getApptype().equals("imu_fm210")) {
                                    SPUtil.setParam(BleUpgradeUtil.this.mContext, BleUpgradeUtil.this.ptzType + "imuPath", BleUpgradeUtil.this.fvUpgradeBean.getRows().get(i).getApppath());
                                    SPUtil.setParam(BleUpgradeUtil.this.mContext, BleUpgradeUtil.this.ptzType + "imuIsforce", BleUpgradeUtil.this.fvUpgradeBean.getRows().get(i).getIsforce());
                                    BleUpgradeUtil.this.imu_releasenotes = BleUpgradeUtil.this.fvUpgradeBean.getRows().get(i).getReleasenotes();
                                    BleUpgradeUtil.this.imu_version = BleUpgradeUtil.this.fvUpgradeBean.getRows().get(i).getAppver();
                                    BleUpgradeUtil.this.imu_verifycode = BleUpgradeUtil.this.fvUpgradeBean.getRows().get(i).getVerifycode();
                                    String imuVersion = SPUtil.getParam(BleUpgradeUtil.this.mContext, BleUpgradeUtil.this.ptzType + "imuVersion", "").toString();
                                    if (imuVersion.equals("")) {
                                        SPUtil.setParam(BleUpgradeUtil.this.mContext, BleUpgradeUtil.this.ptzType + "imuVersion", BleUpgradeUtil.this.imu_version);
                                        if (BleUpgradeUtil.this.mListener != null) {
                                            BleUpgradeUtil.this.mListener.isImuNeedDownoad(true);
                                        }
                                    } else if (!BleUpgradeUtil.this.imu_version.equals(imuVersion)) {
                                        if (BleUpgradeUtil.this.mListener != null) {
                                            BleUpgradeUtil.this.mListener.isImuNeedDownoad(true);
                                        }
                                    } else if (BleUpgradeUtil.this.mListener != null) {
                                        BleUpgradeUtil.this.mListener.isImuNeedDownoad(false);
                                    }
                                }
                            }
                        } else if (BleUpgradeUtil.this.mListener != null) {
                            BleUpgradeUtil.this.mListener.isAppNeedDownload(false);
                            BleUpgradeUtil.this.mListener.isGmuNeedDownoad(false);
                            BleUpgradeUtil.this.mListener.isImuNeedDownoad(false);
                        }
                    } else if (BleUpgradeUtil.this.mListener != null) {
                        BleUpgradeUtil.this.mListener.isAppNeedDownload(false);
                        BleUpgradeUtil.this.mListener.isGmuNeedDownoad(false);
                        BleUpgradeUtil.this.mListener.isImuNeedDownoad(false);
                    }
                } else if (BleUpgradeUtil.this.mListener != null) {
                    BleUpgradeUtil.this.mListener.isAppNeedDownload(false);
                    BleUpgradeUtil.this.mListener.isGmuNeedDownoad(false);
                    BleUpgradeUtil.this.mListener.isImuNeedDownoad(false);
                }
            }

            public void onError(Response<String> response) {
                if (BleUpgradeUtil.this.mListener != null) {
                    BleUpgradeUtil.this.mListener.isAppNeedDownload(false);
                    BleUpgradeUtil.this.mListener.isGmuNeedDownoad(false);
                    BleUpgradeUtil.this.mListener.isImuNeedDownoad(false);
                }
            }
        });
    }

    public void checkWhatNeedUpgrade(AppVersionUpgradeListener appVersionUpgradeListener) {
        Log.i("Kbein", "checkWhatNeedUpgrade:  ---------");
        getInstance().requestAppInfo(appVersionUpgradeListener);
    }

    public void getCanChargeByWirelessList() {
        ((PostRequest) ((PostRequest) OkGo.post(CAN_CHARGE_BY_WIRELESS_LIST_URL).tag(this)).client(new OkHttpClient().newBuilder().readTimeout(1000, TimeUnit.MILLISECONDS).writeTimeout(1000, TimeUnit.MILLISECONDS).connectTimeout(1000, TimeUnit.MILLISECONDS).build())).execute(new StringCallback() {
            public void onSuccess(Response<String> response) {
                try {
                    WirelessModelList modelList = (WirelessModelList) new Gson().fromJson(new JsonParser().parse(response.body()).getAsString(), WirelessModelList.class);
                    if (modelList == null) {
                        return;
                    }
                    if (modelList.getCode().equals("1000")) {
                        ArrayList<String> localList = (ArrayList) SPUtils.getDataList(BleUpgradeUtil.this.mContext, SharePrefConstant.PTZ_FM300_CAN_CHARGE_BY_WIRELESS_LIST);
                        List<WirelessModelList.DataBean> netList = modelList.getData();
                        if (netList != null && netList.size() > localList.size()) {
                            localList.clear();
                            for (WirelessModelList.DataBean model : netList) {
                                localList.add(model.getBrand() + "," + model.getModel());
                            }
                            SPUtils.setDataList(BleUpgradeUtil.this.mContext, SharePrefConstant.PTZ_FM300_CAN_CHARGE_BY_WIRELESS_LIST, localList);
                        }
                    } else if (modelList.getCode().equals("1001")) {
                        Toast.makeText(BleUpgradeUtil.this.mContext, "Get wireless phone list error:" + modelList.getCode(), 1).show();
                    } else if (modelList.getCode().equals("1002")) {
                        Toast.makeText(BleUpgradeUtil.this.mContext, "Get wireless phone list error:" + modelList.getCode(), 1).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ViseLog.m1466e("Charge--e.printStackTrace()");
                }
            }

            public void onError(Response<String> response) {
                super.onError(response);
            }
        });
    }

    public void whatDownload(int type, DownloadListener listener) {
        this.mDownloadListener = listener;
        if (this.fvUpgradeBean != null) {
            String apppath = this.fvUpgradeBean.getRows().get(0).getApppath();
            ViseLog.m1466e("apppath: " + apppath);
            String appdownloaddir = getFileDir();
            ViseLog.m1466e("appdownloaddir: " + appdownloaddir);
            String apkLocalPath = appdownloaddir + APKNAME;
            File file = new File(apkLocalPath);
            ViseLog.m1466e("checkAPKCompleteness(mContext,apkLocalPath): " + checkAPKCompleteness(this.mContext, apkLocalPath));
            if (file.exists()) {
                file.delete();
            }
            if (!checkAPKCompleteness(this.mContext, apkLocalPath)) {
                file.delete();
            }
            if (!appdownloaddir.equals("")) {
                download(type, apppath, appdownloaddir, APKNAME);
            } else if (this.mDownloadListener != null) {
                this.mDownloadListener.onError("SDcard no exist");
            }
        }
    }

    public boolean checkAPKCompleteness(Context context, String filePath) {
        try {
            PackageManager pm = context.getPackageManager();
            Log.e("archiveFilePath", filePath);
            if (pm.getPackageArchiveInfo(filePath, 1) != null) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public void whatDownload(int type) {
        if (this.fvUpgradeBean != null) {
            if (type == 2) {
                if (this.fvUpgradeBean.getRows().size() > 1) {
                    String gmuDownloadUrl = this.fvUpgradeBean.getRows().get(1).getApppath();
                    String gmuDownloadDir = getFirmwareDir();
                    Log.e("------  gmu  ------", "------  gmu  ----" + gmuDownloadDir);
                    if (!gmuDownloadDir.equals("")) {
                        String path = gmuDownloadUrl;
                        String fileName = path.substring(path.indexOf("FM"));
                        cleanFirmware();
                        download(type, gmuDownloadUrl, gmuDownloadDir, fileName);
                    }
                }
            } else if (this.fvUpgradeBean.getRows().size() > 2) {
                String imuDownloadUrl = this.fvUpgradeBean.getRows().get(2).getApppath();
                String imuDownloadDir = getFirmwareDir();
                Log.e("------  imu  ------", "------  imu  ----" + imuDownloadDir);
                if (!imuDownloadDir.equals("")) {
                    String path2 = imuDownloadUrl;
                    String fileName2 = path2.substring(path2.indexOf("FM"));
                    cleanFirmware();
                    download(type, imuDownloadUrl, imuDownloadDir, fileName2);
                }
            }
        }
    }

    private void cleanFirmware() {
        File[] files = new File(getFirmwareDir()).listFiles();
        if (files != null) {
            for (File tempFile : files) {
                if (tempFile.getName().contains(".bin")) {
                    tempFile.delete();
                }
            }
        }
    }

    public void cancelRequest() {
        OkGo.getInstance().cancelTag(this.mContext);
    }

    public void download(int type, String downloadpath, String doloadFileDir, String downloadFileName) {
        this.ptzType = MyApplication.CURRENT_PTZ_TYPE;
        ViseLog.m1466e("AdTest--downloadpath--" + downloadpath);
        final String str = downloadpath;
        final String str2 = doloadFileDir;
        final String str3 = downloadFileName;
        final int i = type;
        new Thread(new Runnable() {
            public void run() {
                ((PostRequest) OkGo.post(str).tag(BleUpgradeUtil.this.mContext)).execute(new FileCallback(str2, str3) {
                    public void onSuccess(Response<File> response) {
                        ViseLog.m1466e("下载成功");
                        ViseLog.m1466e("下载成功 response.body():" + response.body());
                        if (i == 1) {
                            SPUtil.setParam(BleUpgradeUtil.this.mContext, "apkDownloadFileDir", str2 + str3);
                            SPUtil.setParam(BleUpgradeUtil.this.mContext, "apkReleasenotes", BleUpgradeUtil.this.apk_releasenotes);
                        } else if (i == 2) {
                            SPUtil.setParam(BleUpgradeUtil.this.mContext, BleUpgradeUtil.this.ptzType + "gmuDownloadFileDir", str2 + str3);
                            SPUtil.setParam(BleUpgradeUtil.this.mContext, BleUpgradeUtil.this.ptzType + "gmuVersion", BleUpgradeUtil.this.gmu_version);
                            SPUtil.setParam(BleUpgradeUtil.this.mContext, BleUpgradeUtil.this.ptzType + "gmuVerifycode", BleUpgradeUtil.this.gmu_verifycode);
                            SPUtil.setParam(BleUpgradeUtil.this.mContext, BleUpgradeUtil.this.ptzType + "gmuReleasenotes", BleUpgradeUtil.this.gmu_releasenotes);
                            ViseLog.m1466e("gmuDownloadFileDir" + str2 + str3);
                            ViseLog.m1466e("gmu_version" + BleUpgradeUtil.this.gmu_version);
                            ViseLog.m1466e("gmu_verifycode" + BleUpgradeUtil.this.gmu_verifycode);
                        } else {
                            SPUtil.setParam(BleUpgradeUtil.this.mContext, BleUpgradeUtil.this.ptzType + "imuDownloadFileDir", str2 + str3);
                            SPUtil.setParam(BleUpgradeUtil.this.mContext, BleUpgradeUtil.this.ptzType + "imuVersion", BleUpgradeUtil.this.imu_version);
                            SPUtil.setParam(BleUpgradeUtil.this.mContext, BleUpgradeUtil.this.ptzType + "imuVerifycode", BleUpgradeUtil.this.imu_verifycode);
                            SPUtil.setParam(BleUpgradeUtil.this.mContext, BleUpgradeUtil.this.ptzType + "imuReleasenotes", BleUpgradeUtil.this.imu_releasenotes);
                            ViseLog.m1466e("imuDownloadFileDir" + str2 + str3);
                            ViseLog.m1466e("imuVersion" + BleUpgradeUtil.this.imu_version);
                            ViseLog.m1466e("imu_verifycode" + BleUpgradeUtil.this.imu_verifycode);
                        }
                        if (BleUpgradeUtil.this.mDownloadListener != null) {
                            BleUpgradeUtil.this.mDownloadListener.onSuccess();
                        }
                    }

                    public void onStart(Request<File, ? extends Request> request) {
                        ViseLog.m1466e("下载开始");
                        if (BleUpgradeUtil.this.mDownloadListener != null) {
                            BleUpgradeUtil.this.mDownloadListener.onStart();
                        }
                    }

                    public void downloadProgress(Progress progress) {
                        if (BleUpgradeUtil.this.mDownloadListener != null) {
                            BleUpgradeUtil.this.mDownloadListener.onProgress(progress);
                        }
                    }

                    public void onFinish() {
                    }

                    public void onError(Response<File> response) {
                        ViseLog.m1466e("下载失败");
                        if (response != null && response.getException() != null) {
                            String exception = response.getException().getMessage().toString();
                            if (BleUpgradeUtil.this.mDownloadListener != null) {
                                BleUpgradeUtil.this.mDownloadListener.onError(exception);
                            }
                        }
                    }
                });
            }
        }).start();
    }

    public boolean isSdCardExist() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public String getFirmwareDir() {
        boolean exist = isSdCardExist();
        String filePath = "";
        String currentPtzType = MyApplication.CURRENT_PTZ_TYPE;
        String ptzTypeDir = "";
        if (currentPtzType.equals("")) {
            ptzTypeDir = BleConstant.FM_200_Dir;
        } else if (currentPtzType.equals(BleConstant.FM_300)) {
            ptzTypeDir = BleConstant.FM_300_Dir;
        } else if (currentPtzType.equals(BleConstant.FM_210)) {
            ptzTypeDir = BleConstant.FM_210_Dir;
        }
        if (exist) {
            String tempPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/FVMobile/download/" + ptzTypeDir;
            File file = new File(tempPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            filePath = tempPath;
        }
        ViseLog.m1466e("filePath: " + filePath);
        return filePath;
    }

    public String getFileDir() {
        String filePath = "";
        if (isSdCardExist()) {
            String tempPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/FVMobile/download/";
            File file = new File(tempPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            filePath = tempPath;
        }
        ViseLog.m1466e("filePath: " + filePath);
        return filePath;
    }

    public String getAPKPath(String filename) {
        String fileDir = getFileDir();
        if (!fileDir.equals("")) {
            return fileDir + filename;
        }
        return "";
    }

    public String getFilePath(String filename) {
        String fileDir = getFileDir();
        if (!fileDir.equals("")) {
            return fileDir + filename;
        }
        return "";
    }

    public String getFirmwarePath(String filename) {
        String fileDir = getFirmwareDir();
        if (!fileDir.equals("")) {
            return fileDir + filename;
        }
        return "";
    }

    public void installAPP(String path) {
        if (Build.VERSION.SDK_INT <= 23) {
            Intent installIntent = new Intent("android.intent.action.VIEW");
            installIntent.setFlags(268435456);
            installIntent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
            this.mContext.startActivity(installIntent);
            Process.killProcess(Process.myPid());
            return;
        }
        File apkFile = new File(path);
        if (apkFile.exists()) {
            Uri uri = FileProvider.getUriForFile(this.mContext, this.mContext.getPackageName() + ".fileProvider", apkFile);
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addFlags(1);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            this.mContext.startActivity(intent);
        }
    }

    public static String getAppVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    public byte[] getGMUBinaryFromAssert() {
        return ReadAssertBinaryResource(GMUNAME);
    }

    public byte[] getIMUBinaryFromAssert() {
        return ReadAssertBinaryResource(IMUNAME);
    }

    public byte[] ReadAssertBinaryResource(String strAssertFileName) {
        byte[] contents = null;
        try {
            byte[] bArr = new byte[1024];
            InputStream ims = this.mContext.getAssets().open(strAssertFileName);
            contents = getBytesFromInputStream(ims);
            ims.close();
            return contents;
        } catch (IOException e) {
            e.printStackTrace();
            return contents;
        }
    }

    public static byte[] getBytesFromInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while (true) {
            int len = is.read(buffer);
            if (len != -1) {
                bos.write(buffer, 0, len);
            } else {
                bos.flush();
                byte[] result = bos.toByteArray();
                bos.close();
                return result;
            }
        }
    }

    public byte[] getIMUBinaryFromSDCard() {
        String path = getFirmwarePath(getGmuImuName(2));
        if (!isRightOffset(path) || path.isEmpty()) {
            return null;
        }
        return ReadSDCardBinaryResource(path);
    }

    public byte[] getGMUBinaryFromSDCard() {
        String path = getFirmwarePath(getGmuImuName(1));
        if (path.isEmpty()) {
            return null;
        }
        return ReadSDCardBinaryResource(path);
    }

    public String getGmuImuName(int type) {
        String firmeType;
        String fileName = "";
        String fileType = "";
        this.ptzType = MyApplication.CURRENT_PTZ_TYPE;
        if (this.ptzType.equals("")) {
            fileType = "FM200";
        } else if (this.ptzType.equals(BleConstant.FM_300)) {
            fileType = "FM300";
        } else if (this.ptzType.equals(BleConstant.FM_210)) {
            fileType = "FM210";
        }
        if (type == 1) {
            firmeType = "GMU";
        } else {
            firmeType = "IMU";
        }
        File[] files = new File(getFirmwareDir()).listFiles();
        if (files != null) {
            for (File tempFile : files) {
                if (tempFile.getName().contains(firmeType) && tempFile.getName().contains(fileType)) {
                    fileName = tempFile.getName();
                }
            }
        } else {
            ViseLog.m1466e("File---empty dir");
        }
        return fileName;
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:0x0077 A[SYNTHETIC, Splitter:B:32:0x0077] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0080 A[SYNTHETIC, Splitter:B:37:0x0080] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean isRightOffset(java.lang.String r9) {
        /*
            r8 = this;
            r3 = 0
            r4 = 0
            java.io.RandomAccessFile r5 = new java.io.RandomAccessFile     // Catch:{ IOException -> 0x0071 }
            java.lang.String r6 = "r"
            r5.<init>(r9, r6)     // Catch:{ IOException -> 0x0071 }
            r0 = 3584(0xe00, float:5.022E-42)
            long r6 = (long) r0
            r5.seek(r6)     // Catch:{ IOException -> 0x0089, all -> 0x0086 }
            r6 = 5
            byte[] r1 = new byte[r6]     // Catch:{ IOException -> 0x0089, all -> 0x0086 }
            r5.read(r1)     // Catch:{ IOException -> 0x0089, all -> 0x0086 }
            java.lang.String r6 = com.freevisiontech.fvmobile.application.MyApplication.CURRENT_PTZ_TYPE     // Catch:{ IOException -> 0x0089, all -> 0x0086 }
            r8.ptzType = r6     // Catch:{ IOException -> 0x0089, all -> 0x0086 }
            java.lang.String r6 = r8.ptzType     // Catch:{ IOException -> 0x0089, all -> 0x0086 }
            java.lang.String r7 = ""
            boolean r6 = r6.equals(r7)     // Catch:{ IOException -> 0x0089, all -> 0x0086 }
            if (r6 == 0) goto L_0x003a
            java.lang.String r6 = r8.readHex(r1)     // Catch:{ IOException -> 0x0089, all -> 0x0086 }
            java.lang.String r7 = "VILTA"
            boolean r6 = r6.equals(r7)     // Catch:{ IOException -> 0x0089, all -> 0x0086 }
            if (r6 == 0) goto L_0x003a
            r3 = 1
        L_0x0033:
            if (r5 == 0) goto L_0x008c
            r5.close()     // Catch:{ IOException -> 0x006e }
            r4 = r5
        L_0x0039:
            return r3
        L_0x003a:
            java.lang.String r6 = r8.ptzType     // Catch:{ IOException -> 0x0089, all -> 0x0086 }
            java.lang.String r7 = "VILTA_MOBILE_FM300"
            boolean r6 = r6.equals(r7)     // Catch:{ IOException -> 0x0089, all -> 0x0086 }
            if (r6 == 0) goto L_0x0054
            java.lang.String r6 = r8.readHex(r1)     // Catch:{ IOException -> 0x0089, all -> 0x0086 }
            java.lang.String r7 = "FM300"
            boolean r6 = r6.equals(r7)     // Catch:{ IOException -> 0x0089, all -> 0x0086 }
            if (r6 == 0) goto L_0x0054
            r3 = 1
            goto L_0x0033
        L_0x0054:
            java.lang.String r6 = r8.ptzType     // Catch:{ IOException -> 0x0089, all -> 0x0086 }
            java.lang.String r7 = "VILTA_MOBILE_FM210"
            boolean r6 = r6.equals(r7)     // Catch:{ IOException -> 0x0089, all -> 0x0086 }
            if (r6 == 0) goto L_0x0033
            java.lang.String r6 = r8.readHex(r1)     // Catch:{ IOException -> 0x0089, all -> 0x0086 }
            java.lang.String r7 = "FM210"
            boolean r6 = r6.equals(r7)     // Catch:{ IOException -> 0x0089, all -> 0x0086 }
            if (r6 == 0) goto L_0x0033
            r3 = 1
            goto L_0x0033
        L_0x006e:
            r6 = move-exception
            r4 = r5
            goto L_0x0039
        L_0x0071:
            r2 = move-exception
        L_0x0072:
            r2.printStackTrace()     // Catch:{ all -> 0x007d }
            if (r4 == 0) goto L_0x0039
            r4.close()     // Catch:{ IOException -> 0x007b }
            goto L_0x0039
        L_0x007b:
            r6 = move-exception
            goto L_0x0039
        L_0x007d:
            r6 = move-exception
        L_0x007e:
            if (r4 == 0) goto L_0x0083
            r4.close()     // Catch:{ IOException -> 0x0084 }
        L_0x0083:
            throw r6
        L_0x0084:
            r7 = move-exception
            goto L_0x0083
        L_0x0086:
            r6 = move-exception
            r4 = r5
            goto L_0x007e
        L_0x0089:
            r2 = move-exception
            r4 = r5
            goto L_0x0072
        L_0x008c:
            r4 = r5
            goto L_0x0039
        */
        throw new UnsupportedOperationException("Method not decompiled: com.freevisiontech.fvmobile.utils.BleUpgradeUtil.isRightOffset(java.lang.String):boolean");
    }

    private String readHex(byte[] value) {
        if (value.length <= 0) {
            return null;
        }
        char[] snChars = new char[5];
        for (int i = 0; i < 5; i++) {
            snChars[i] = (char) value[i];
        }
        return new String(snChars);
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x002f A[SYNTHETIC, Splitter:B:22:0x002f] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public byte[] ReadSDCardBinaryResource(java.lang.String r7) {
        /*
            r6 = this;
            r0 = 0
            r3 = 0
            java.io.File r2 = new java.io.File     // Catch:{ IOException -> 0x001d }
            r2.<init>(r7)     // Catch:{ IOException -> 0x001d }
            java.io.FileInputStream r4 = new java.io.FileInputStream     // Catch:{ IOException -> 0x001d }
            r4.<init>(r2)     // Catch:{ IOException -> 0x001d }
            byte[] r0 = getBytesFromInputStream(r4)     // Catch:{ IOException -> 0x003b, all -> 0x0038 }
            if (r4 == 0) goto L_0x003e
            r4.close()     // Catch:{ Exception -> 0x0017 }
            r3 = r4
        L_0x0016:
            return r0
        L_0x0017:
            r1 = move-exception
            r1.printStackTrace()
            r3 = r4
            goto L_0x0016
        L_0x001d:
            r1 = move-exception
        L_0x001e:
            r1.printStackTrace()     // Catch:{ all -> 0x002c }
            if (r3 == 0) goto L_0x0016
            r3.close()     // Catch:{ Exception -> 0x0027 }
            goto L_0x0016
        L_0x0027:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0016
        L_0x002c:
            r5 = move-exception
        L_0x002d:
            if (r3 == 0) goto L_0x0032
            r3.close()     // Catch:{ Exception -> 0x0033 }
        L_0x0032:
            throw r5
        L_0x0033:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0032
        L_0x0038:
            r5 = move-exception
            r3 = r4
            goto L_0x002d
        L_0x003b:
            r1 = move-exception
            r3 = r4
            goto L_0x001e
        L_0x003e:
            r3 = r4
            goto L_0x0016
        */
        throw new UnsupportedOperationException("Method not decompiled: com.freevisiontech.fvmobile.utils.BleUpgradeUtil.ReadSDCardBinaryResource(java.lang.String):byte[]");
    }
}
