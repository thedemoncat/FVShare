package com.google.android.vending.expansion.downloader.impl;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Messenger;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.p001v4.app.NotificationCompat;
import android.util.Log;
import com.google.android.exoplayer.C1907C;
import com.google.android.vending.expansion.downloader.Constants;
import com.google.android.vending.expansion.downloader.DownloadProgressInfo;
import com.google.android.vending.expansion.downloader.DownloaderServiceMarshaller;
import com.google.android.vending.expansion.downloader.Helpers;
import com.google.android.vending.expansion.downloader.IDownloaderService;
import com.google.android.vending.expansion.downloader.IStub;
import com.google.android.vending.licensing.AESObfuscator;
import com.google.android.vending.licensing.APKExpansionPolicy;
import com.google.android.vending.licensing.LicenseChecker;
import com.google.android.vending.licensing.LicenseCheckerCallback;
import com.google.android.vending.licensing.Policy;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DownloaderService extends CustomIntentService implements IDownloaderService {
    public static final String ACTION_DOWNLOADS_CHANGED = "downloadsChanged";
    public static final String ACTION_DOWNLOAD_COMPLETE = "lvldownloader.intent.action.DOWNLOAD_COMPLETE";
    public static final String ACTION_DOWNLOAD_STATUS = "lvldownloader.intent.action.DOWNLOAD_STATUS";
    public static final int CONTROL_PAUSED = 1;
    public static final int CONTROL_RUN = 0;
    public static final int DOWNLOAD_REQUIRED = 2;
    public static final String EXTRA_FILE_NAME = "downloadId";
    public static final String EXTRA_IS_WIFI_REQUIRED = "isWifiRequired";
    public static final String EXTRA_MESSAGE_HANDLER = "EMH";
    public static final String EXTRA_PACKAGE_NAME = "EPN";
    public static final String EXTRA_PENDING_INTENT = "EPI";
    public static final String EXTRA_STATUS_CURRENT_FILE_SIZE = "CFS";
    public static final String EXTRA_STATUS_CURRENT_PROGRESS = "CFP";
    public static final String EXTRA_STATUS_STATE = "ESS";
    public static final String EXTRA_STATUS_TOTAL_PROGRESS = "TFP";
    public static final String EXTRA_STATUS_TOTAL_SIZE = "ETS";
    /* access modifiers changed from: private */
    public static final Logger LOG = LoggerFactory.getLogger("DownloaderService");
    public static final int LVL_CHECK_REQUIRED = 1;
    public static final int NETWORK_CANNOT_USE_ROAMING = 5;
    public static final int NETWORK_MOBILE = 1;
    public static final int NETWORK_NO_CONNECTION = 2;
    public static final int NETWORK_OK = 1;
    public static final int NETWORK_RECOMMENDED_UNUSABLE_DUE_TO_SIZE = 4;
    public static final int NETWORK_TYPE_DISALLOWED_BY_REQUESTOR = 6;
    public static final int NETWORK_UNUSABLE_DUE_TO_SIZE = 3;
    public static final int NETWORK_WIFI = 2;
    public static final int NO_DOWNLOAD_REQUIRED = 0;
    private static final float SMOOTHING_FACTOR = 0.005f;
    public static final int STATUS_CANCELED = 490;
    public static final int STATUS_CANNOT_RESUME = 489;
    public static final int STATUS_DEVICE_NOT_FOUND_ERROR = 499;
    public static final int STATUS_FILE_ALREADY_EXISTS_ERROR = 488;
    public static final int STATUS_FILE_DELIVERED_INCORRECTLY = 487;
    public static final int STATUS_FILE_ERROR = 492;
    public static final int STATUS_FORBIDDEN = 403;
    public static final int STATUS_HTTP_DATA_ERROR = 495;
    public static final int STATUS_HTTP_EXCEPTION = 496;
    public static final int STATUS_INSUFFICIENT_SPACE_ERROR = 498;
    public static final int STATUS_PAUSED_BY_APP = 193;
    public static final int STATUS_PENDING = 190;
    public static final int STATUS_QUEUED_FOR_WIFI = 197;
    public static final int STATUS_QUEUED_FOR_WIFI_OR_CELLULAR_PERMISSION = 196;
    public static final int STATUS_RUNNING = 192;
    public static final int STATUS_SUCCESS = 200;
    public static final int STATUS_TOO_MANY_REDIRECTS = 497;
    public static final int STATUS_UNHANDLED_HTTP_CODE = 494;
    public static final int STATUS_UNHANDLED_REDIRECT = 493;
    public static final int STATUS_UNKNOWN_ERROR = 491;
    public static final int STATUS_WAITING_FOR_NETWORK = 195;
    public static final int STATUS_WAITING_TO_RETRY = 194;
    private static final String TEMP_EXT = ".tmp";
    public static final int VISIBILITY_HIDDEN = 2;
    public static final int VISIBILITY_VISIBLE = 0;
    public static final int VISIBILITY_VISIBLE_NOTIFY_COMPLETED = 1;
    private static boolean sIsRunning;
    private PendingIntent mAlarmIntent;
    float mAverageDownloadSpeed;
    long mBytesAtSample;
    long mBytesSoFar;
    private Messenger mClientMessenger;
    private BroadcastReceiver mConnReceiver;
    private ConnectivityManager mConnectivityManager;
    private int mControl;
    int mFileCount;
    private boolean mIsAtLeast3G;
    private boolean mIsAtLeast4G;
    private boolean mIsCellularConnection;
    private boolean mIsConnected;
    private boolean mIsFailover;
    private boolean mIsRoaming;
    long mMillisecondsAtSample;
    /* access modifiers changed from: private */
    public DownloadNotification mNotification;
    private PackageInfo mPackageInfo;
    /* access modifiers changed from: private */
    public PendingIntent mPendingIntent;
    private final Messenger mServiceMessenger = this.mServiceStub.getMessenger();
    private final IStub mServiceStub = DownloaderServiceMarshaller.CreateStub(this);
    /* access modifiers changed from: private */
    public boolean mStateChanged;
    private int mStatus;
    long mTotalLength;
    private WifiManager mWifiManager;

    public abstract String getAlarmReceiverClassName();

    public abstract String getPublicKey();

    public abstract byte[] getSALT();

    public DownloaderService() {
        super("LVLDownloadService");
    }

    public static boolean isStatusInformational(int status) {
        return status >= 100 && status < 200;
    }

    public static boolean isStatusSuccess(int status) {
        return status >= 200 && status < 300;
    }

    public static boolean isStatusError(int status) {
        return status >= 400 && status < 600;
    }

    public static boolean isStatusClientError(int status) {
        return status >= 400 && status < 500;
    }

    public static boolean isStatusServerError(int status) {
        return status >= 500 && status < 600;
    }

    public static boolean isStatusCompleted(int status) {
        return (status >= 200 && status < 300) || (status >= 400 && status < 600);
    }

    public IBinder onBind(Intent paramIntent) {
        LOG.debug("Service Bound");
        return this.mServiceMessenger.getBinder();
    }

    public boolean isWiFi() {
        return this.mIsConnected && !this.mIsCellularConnection;
    }

    private void updateNetworkType(int type, int subType) {
        switch (type) {
            case 0:
                this.mIsCellularConnection = true;
                switch (subType) {
                    case 1:
                    case 2:
                    case 4:
                    case 7:
                    case 11:
                        this.mIsAtLeast3G = false;
                        this.mIsAtLeast4G = false;
                        return;
                    case 3:
                    case 5:
                    case 6:
                    case 8:
                    case 9:
                    case 10:
                        this.mIsAtLeast3G = true;
                        this.mIsAtLeast4G = false;
                        return;
                    case 13:
                    case 14:
                    case 15:
                        this.mIsAtLeast3G = true;
                        this.mIsAtLeast4G = true;
                        return;
                    default:
                        this.mIsCellularConnection = false;
                        this.mIsAtLeast3G = false;
                        this.mIsAtLeast4G = false;
                        return;
                }
            case 1:
            case 7:
            case 9:
                this.mIsCellularConnection = false;
                this.mIsAtLeast3G = false;
                this.mIsAtLeast4G = false;
                return;
            case 6:
                this.mIsCellularConnection = true;
                this.mIsAtLeast3G = true;
                this.mIsAtLeast4G = true;
                return;
            default:
                return;
        }
    }

    private void updateNetworkState(NetworkInfo info) {
        boolean z = false;
        boolean isConnected = this.mIsConnected;
        boolean isFailover = this.mIsFailover;
        boolean isCellularConnection = this.mIsCellularConnection;
        boolean isRoaming = this.mIsRoaming;
        boolean isAtLeast3G = this.mIsAtLeast3G;
        if (info != null) {
            this.mIsRoaming = info.isRoaming();
            this.mIsFailover = info.isFailover();
            this.mIsConnected = info.isConnected();
            updateNetworkType(info.getType(), info.getSubtype());
        } else {
            this.mIsRoaming = false;
            this.mIsFailover = false;
            this.mIsConnected = false;
            updateNetworkType(-1, -1);
        }
        if (!(!this.mStateChanged && isConnected == this.mIsConnected && isFailover == this.mIsFailover && isCellularConnection == this.mIsCellularConnection && isRoaming == this.mIsRoaming && isAtLeast3G == this.mIsAtLeast3G)) {
            z = true;
        }
        this.mStateChanged = z;
    }

    /* access modifiers changed from: package-private */
    public void pollNetworkState() {
        if (this.mConnectivityManager == null) {
            this.mConnectivityManager = (ConnectivityManager) getSystemService("connectivity");
        }
        if (this.mWifiManager == null) {
            this.mWifiManager = (WifiManager) getApplicationContext().getSystemService("wifi");
        }
        if (this.mConnectivityManager == null) {
            LOG.warn("couldn't get connectivity manager to poll network state");
        } else {
            updateNetworkState(this.mConnectivityManager.getActiveNetworkInfo());
        }
    }

    private static boolean isLVLCheckRequired(DownloadsDB db, PackageInfo pi) {
        if (db.mVersionCode != pi.versionCode) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: private */
    public static synchronized boolean isServiceRunning() {
        boolean z;
        synchronized (DownloaderService.class) {
            z = sIsRunning;
        }
        return z;
    }

    /* access modifiers changed from: private */
    public static synchronized void setServiceRunning(boolean isRunning) {
        synchronized (DownloaderService.class) {
            sIsRunning = isRunning;
        }
    }

    public static int startDownloadServiceIfRequired(Context context, Intent intent, Class<?> serviceClass) throws PackageManager.NameNotFoundException {
        return startDownloadServiceIfRequired(context, (PendingIntent) intent.getParcelableExtra(EXTRA_PENDING_INTENT), serviceClass);
    }

    public static int startDownloadServiceIfRequired(Context context, PendingIntent pendingIntent, Class<?> serviceClass) throws PackageManager.NameNotFoundException {
        return startDownloadServiceIfRequired(context, pendingIntent, context.getPackageName(), serviceClass.getName());
    }

    public static int startDownloadServiceIfRequired(Context context, PendingIntent pendingIntent, String classPackage, String className) throws PackageManager.NameNotFoundException {
        int i = 0;
        PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        int status = 0;
        DownloadsDB db = DownloadsDB.getDB(context);
        if (isLVLCheckRequired(db, pi)) {
            status = 1;
        }
        if (db.mStatus == 0) {
            DownloadInfo[] infos = db.getDownloads();
            if (infos != null) {
                int length = infos.length;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    DownloadInfo info = infos[i];
                    Log.e("DownloadInfo url: ", info.mUri);
                    if (!Helpers.doesFileExist(context, info.mFileName, info.mTotalBytes, true)) {
                        status = 2;
                        db.updateStatus(-1);
                        break;
                    }
                    i++;
                }
            }
        } else {
            status = 2;
        }
        switch (status) {
            case 1:
            case 2:
                Intent fileIntent = new Intent();
                fileIntent.setClassName(classPackage, className);
                fileIntent.putExtra(EXTRA_PENDING_INTENT, pendingIntent);
                context.startService(fileIntent);
                break;
        }
        return status;
    }

    public void requestAbortDownload() {
        this.mControl = 1;
        this.mStatus = 490;
    }

    public void requestPauseDownload() {
        this.mControl = 1;
        this.mStatus = 193;
    }

    public void setDownloadFlags(int flags) {
        DownloadsDB.getDB(this).updateFlags(flags);
    }

    public void requestContinueDownload() {
        if (this.mControl == 1) {
            this.mControl = 0;
        }
        Intent fileIntent = new Intent(this, getClass());
        fileIntent.putExtra(EXTRA_PENDING_INTENT, this.mPendingIntent);
        startService(fileIntent);
    }

    private class LVLRunnable implements Runnable {
        final Context mContext;

        LVLRunnable(Context context, PendingIntent intent) {
            this.mContext = context;
            PendingIntent unused = DownloaderService.this.mPendingIntent = intent;
        }

        public void run() {
            DownloaderService.setServiceRunning(true);
            DownloaderService.this.mNotification.onDownloadStateChanged(2);
            final APKExpansionPolicy aep = new APKExpansionPolicy(this.mContext, new AESObfuscator(DownloaderService.this.getSALT(), this.mContext.getPackageName(), Settings.Secure.getString(this.mContext.getContentResolver(), "android_id")));
            aep.resetPolicy();
            new LicenseChecker(this.mContext, aep, DownloaderService.this.getPublicKey()).checkAccess(new LicenseCheckerCallback() {
                public void allow(int reason) {
                    try {
                        int count = aep.getExpansionURLCount();
                        DownloadsDB db = DownloadsDB.getDB(LVLRunnable.this.mContext);
                        int status = 0;
                        if (count != 0) {
                            for (int i = 0; i < count; i++) {
                                String currentFileName = aep.getExpansionFileName(i);
                                if (currentFileName != null) {
                                    DownloadInfo di = new DownloadInfo(i, currentFileName, LVLRunnable.this.mContext.getPackageName());
                                    long fileSize = aep.getExpansionFileSize(i);
                                    if (DownloaderService.this.handleFileUpdated(db, i, currentFileName, fileSize)) {
                                        status |= -1;
                                        di.resetDownload();
                                        di.mUri = aep.getExpansionURL(i);
                                        di.mTotalBytes = fileSize;
                                        di.mStatus = status;
                                        db.updateDownload(di);
                                    } else {
                                        DownloadInfo dbdi = db.getDownloadInfoByFileName(di.mFileName);
                                        if (dbdi == null) {
                                            DownloaderService.LOG.debug("file " + di.mFileName + " found. Not downloading.");
                                            di.mStatus = 200;
                                            di.mTotalBytes = fileSize;
                                            di.mCurrentBytes = fileSize;
                                            di.mUri = aep.getExpansionURL(i);
                                            db.updateDownload(di);
                                        } else if (dbdi.mStatus != 200) {
                                            dbdi.mUri = aep.getExpansionURL(i);
                                            db.updateDownload(dbdi);
                                            status |= -1;
                                        }
                                    }
                                }
                            }
                        }
                        db.updateMetadata(LVLRunnable.this.mContext.getPackageManager().getPackageInfo(LVLRunnable.this.mContext.getPackageName(), 0).versionCode, status);
                        switch (DownloaderService.startDownloadServiceIfRequired(LVLRunnable.this.mContext, DownloaderService.this.mPendingIntent, DownloaderService.this.getClass())) {
                            case 0:
                                DownloaderService.this.mNotification.onDownloadStateChanged(5);
                                break;
                            case 1:
                                DownloaderService.LOG.error("In LVL checking loop!");
                                DownloaderService.this.mNotification.onDownloadStateChanged(15);
                                throw new RuntimeException("Error with LVL checking and database integrity");
                        }
                        DownloaderService.setServiceRunning(false);
                    } catch (PackageManager.NameNotFoundException e1) {
                        e1.printStackTrace();
                        throw new RuntimeException("Error with getting information from package name");
                    } catch (Throwable th) {
                        DownloaderService.setServiceRunning(false);
                        throw th;
                    }
                }

                public void dontAllow(int reason) {
                    switch (reason) {
                        case 291:
                            DownloaderService.this.mNotification.onDownloadStateChanged(16);
                            break;
                        case Policy.NOT_LICENSED:
                            try {
                                DownloaderService.this.mNotification.onDownloadStateChanged(15);
                                break;
                            } catch (Throwable th) {
                                DownloaderService.setServiceRunning(false);
                                throw th;
                            }
                    }
                    DownloaderService.setServiceRunning(false);
                }

                public void applicationError(int errorCode) {
                    try {
                        DownloaderService.this.mNotification.onDownloadStateChanged(16);
                    } finally {
                        DownloaderService.setServiceRunning(false);
                    }
                }
            });
        }
    }

    public void updateLVL(Context context) {
        Context c = context.getApplicationContext();
        new Handler(c.getMainLooper()).post(new LVLRunnable(c, this.mPendingIntent));
    }

    public boolean handleFileUpdated(DownloadsDB db, int index, String filename, long fileSize) {
        String oldFile;
        boolean z = true;
        DownloadInfo di = db.getDownloadInfoByFileName(filename);
        if (!(di == null || (oldFile = di.mFileName) == null)) {
            if (filename.equals(oldFile)) {
                return false;
            }
            File f = new File(Helpers.generateSaveFileName(this, oldFile));
            if (f.exists()) {
                f.delete();
            }
        }
        if (Helpers.doesFileExist(this, filename, fileSize, true)) {
            z = false;
        }
        return z;
    }

    private void scheduleAlarm(long wakeUp) {
        AlarmManager alarms = (AlarmManager) getSystemService(NotificationCompat.CATEGORY_ALARM);
        if (alarms == null) {
            LOG.error("couldn't get alarm manager");
            return;
        }
        String className = getAlarmReceiverClassName();
        Intent intent = new Intent(Constants.ACTION_RETRY);
        intent.putExtra(EXTRA_PENDING_INTENT, this.mPendingIntent);
        intent.setClassName(getPackageName(), className);
        this.mAlarmIntent = PendingIntent.getBroadcast(this, 0, intent, C1907C.ENCODING_PCM_32BIT);
        alarms.set(0, System.currentTimeMillis() + wakeUp, this.mAlarmIntent);
    }

    private void cancelAlarms() {
        if (this.mAlarmIntent != null) {
            AlarmManager alarms = (AlarmManager) getSystemService(NotificationCompat.CATEGORY_ALARM);
            if (alarms == null) {
                LOG.error("couldn't get alarm manager");
                return;
            }
            alarms.cancel(this.mAlarmIntent);
            this.mAlarmIntent = null;
        }
    }

    private class InnerBroadcastReceiver extends BroadcastReceiver {
        final Service mService;

        InnerBroadcastReceiver(Service service) {
            this.mService = service;
        }

        public void onReceive(Context context, Intent intent) {
            DownloaderService.this.pollNetworkState();
            if (DownloaderService.this.mStateChanged && !DownloaderService.isServiceRunning()) {
                DownloaderService.LOG.debug("InnerBroadcastReceiver Called");
                Intent fileIntent = new Intent(context, this.mService.getClass());
                fileIntent.putExtra(DownloaderService.EXTRA_PENDING_INTENT, DownloaderService.this.mPendingIntent);
                context.startService(fileIntent);
            }
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onHandleIntent(android.content.Intent r23) {
        /*
            r22 = this;
            r16 = 1
            setServiceRunning(r16)
            com.google.android.vending.expansion.downloader.impl.DownloadsDB r6 = com.google.android.vending.expansion.downloader.impl.DownloadsDB.getDB(r22)     // Catch:{ all -> 0x005a }
            java.lang.String r16 = "EPI"
            r0 = r23
            r1 = r16
            android.os.Parcelable r12 = r0.getParcelableExtra(r1)     // Catch:{ all -> 0x005a }
            android.app.PendingIntent r12 = (android.app.PendingIntent) r12     // Catch:{ all -> 0x005a }
            if (r12 == 0) goto L_0x0042
            r0 = r22
            com.google.android.vending.expansion.downloader.impl.DownloadNotification r0 = r0.mNotification     // Catch:{ all -> 0x005a }
            r16 = r0
            r0 = r16
            r0.setClientIntent(r12)     // Catch:{ all -> 0x005a }
            r0 = r22
            r0.mPendingIntent = r12     // Catch:{ all -> 0x005a }
        L_0x0027:
            r0 = r22
            android.content.pm.PackageInfo r0 = r0.mPackageInfo     // Catch:{ all -> 0x005a }
            r16 = r0
            r0 = r16
            boolean r16 = isLVLCheckRequired(r6, r0)     // Catch:{ all -> 0x005a }
            if (r16 == 0) goto L_0x006f
            r0 = r22
            r1 = r22
            r0.updateLVL(r1)     // Catch:{ all -> 0x005a }
            r16 = 0
            setServiceRunning(r16)
        L_0x0041:
            return
        L_0x0042:
            r0 = r22
            android.app.PendingIntent r0 = r0.mPendingIntent     // Catch:{ all -> 0x005a }
            r16 = r0
            if (r16 == 0) goto L_0x0061
            r0 = r22
            com.google.android.vending.expansion.downloader.impl.DownloadNotification r0 = r0.mNotification     // Catch:{ all -> 0x005a }
            r16 = r0
            r0 = r22
            android.app.PendingIntent r0 = r0.mPendingIntent     // Catch:{ all -> 0x005a }
            r17 = r0
            r16.setClientIntent(r17)     // Catch:{ all -> 0x005a }
            goto L_0x0027
        L_0x005a:
            r16 = move-exception
            r17 = 0
            setServiceRunning(r17)
            throw r16
        L_0x0061:
            org.slf4j.Logger r16 = LOG     // Catch:{ all -> 0x005a }
            java.lang.String r17 = "Downloader started in bad state without notification intent."
            r16.error(r17)     // Catch:{ all -> 0x005a }
            r16 = 0
            setServiceRunning(r16)
            goto L_0x0041
        L_0x006f:
            com.google.android.vending.expansion.downloader.impl.DownloadInfo[] r9 = r6.getDownloads()     // Catch:{ all -> 0x005a }
            r16 = 0
            r0 = r16
            r2 = r22
            r2.mBytesSoFar = r0     // Catch:{ all -> 0x005a }
            r16 = 0
            r0 = r16
            r2 = r22
            r2.mTotalLength = r0     // Catch:{ all -> 0x005a }
            int r0 = r9.length     // Catch:{ all -> 0x005a }
            r16 = r0
            r0 = r16
            r1 = r22
            r1.mFileCount = r0     // Catch:{ all -> 0x005a }
            int r0 = r9.length     // Catch:{ all -> 0x005a }
            r17 = r0
            r16 = 0
        L_0x0091:
            r0 = r16
            r1 = r17
            if (r0 >= r1) goto L_0x00f0
            r8 = r9[r16]     // Catch:{ all -> 0x005a }
            int r0 = r8.mStatus     // Catch:{ all -> 0x005a }
            r18 = r0
            r19 = 200(0xc8, float:2.8E-43)
            r0 = r18
            r1 = r19
            if (r0 != r1) goto L_0x00c9
            java.lang.String r0 = r8.mFileName     // Catch:{ all -> 0x005a }
            r18 = r0
            long r0 = r8.mTotalBytes     // Catch:{ all -> 0x005a }
            r20 = r0
            r19 = 1
            r0 = r22
            r1 = r18
            r2 = r20
            r4 = r19
            boolean r18 = com.google.android.vending.expansion.downloader.Helpers.doesFileExist(r0, r1, r2, r4)     // Catch:{ all -> 0x005a }
            if (r18 != 0) goto L_0x00c9
            r18 = 0
            r0 = r18
            r8.mStatus = r0     // Catch:{ all -> 0x005a }
            r18 = 0
            r0 = r18
            r8.mCurrentBytes = r0     // Catch:{ all -> 0x005a }
        L_0x00c9:
            r0 = r22
            long r0 = r0.mTotalLength     // Catch:{ all -> 0x005a }
            r18 = r0
            long r0 = r8.mTotalBytes     // Catch:{ all -> 0x005a }
            r20 = r0
            long r18 = r18 + r20
            r0 = r18
            r2 = r22
            r2.mTotalLength = r0     // Catch:{ all -> 0x005a }
            r0 = r22
            long r0 = r0.mBytesSoFar     // Catch:{ all -> 0x005a }
            r18 = r0
            long r0 = r8.mCurrentBytes     // Catch:{ all -> 0x005a }
            r20 = r0
            long r18 = r18 + r20
            r0 = r18
            r2 = r22
            r2.mBytesSoFar = r0     // Catch:{ all -> 0x005a }
            int r16 = r16 + 1
            goto L_0x0091
        L_0x00f0:
            r22.pollNetworkState()     // Catch:{ all -> 0x005a }
            r0 = r22
            android.content.BroadcastReceiver r0 = r0.mConnReceiver     // Catch:{ all -> 0x005a }
            r16 = r0
            if (r16 != 0) goto L_0x012b
            com.google.android.vending.expansion.downloader.impl.DownloaderService$InnerBroadcastReceiver r16 = new com.google.android.vending.expansion.downloader.impl.DownloaderService$InnerBroadcastReceiver     // Catch:{ all -> 0x005a }
            r0 = r16
            r1 = r22
            r2 = r22
            r0.<init>(r2)     // Catch:{ all -> 0x005a }
            r0 = r16
            r1 = r22
            r1.mConnReceiver = r0     // Catch:{ all -> 0x005a }
            android.content.IntentFilter r10 = new android.content.IntentFilter     // Catch:{ all -> 0x005a }
            java.lang.String r16 = "android.net.conn.CONNECTIVITY_CHANGE"
            r0 = r16
            r10.<init>(r0)     // Catch:{ all -> 0x005a }
            java.lang.String r16 = "android.net.wifi.WIFI_STATE_CHANGED"
            r0 = r16
            r10.addAction(r0)     // Catch:{ all -> 0x005a }
            r0 = r22
            android.content.BroadcastReceiver r0 = r0.mConnReceiver     // Catch:{ all -> 0x005a }
            r16 = r0
            r0 = r22
            r1 = r16
            r0.registerReceiver(r1, r10)     // Catch:{ all -> 0x005a }
        L_0x012b:
            int r0 = r9.length     // Catch:{ all -> 0x005a }
            r17 = r0
            r16 = 0
        L_0x0130:
            r0 = r16
            r1 = r17
            if (r0 >= r1) goto L_0x0211
            r8 = r9[r16]     // Catch:{ all -> 0x005a }
            long r14 = r8.mCurrentBytes     // Catch:{ all -> 0x005a }
            int r0 = r8.mStatus     // Catch:{ all -> 0x005a }
            r18 = r0
            r19 = 200(0xc8, float:2.8E-43)
            r0 = r18
            r1 = r19
            if (r0 == r1) goto L_0x0167
            com.google.android.vending.expansion.downloader.impl.DownloadThread r7 = new com.google.android.vending.expansion.downloader.impl.DownloadThread     // Catch:{ all -> 0x005a }
            r0 = r22
            com.google.android.vending.expansion.downloader.impl.DownloadNotification r0 = r0.mNotification     // Catch:{ all -> 0x005a }
            r18 = r0
            r0 = r22
            r1 = r18
            r7.<init>(r8, r0, r1)     // Catch:{ all -> 0x005a }
            r22.cancelAlarms()     // Catch:{ all -> 0x005a }
            r18 = 5000(0x1388, double:2.4703E-320)
            r0 = r22
            r1 = r18
            r0.scheduleAlarm(r1)     // Catch:{ all -> 0x005a }
            r7.run()     // Catch:{ all -> 0x005a }
            r22.cancelAlarms()     // Catch:{ all -> 0x005a }
        L_0x0167:
            r6.updateFromDb(r8)     // Catch:{ all -> 0x005a }
            r13 = 0
            int r0 = r8.mStatus     // Catch:{ all -> 0x005a }
            r18 = r0
            switch(r18) {
                case 193: goto L_0x01da;
                case 194: goto L_0x01dc;
                case 195: goto L_0x01dc;
                case 196: goto L_0x01df;
                case 197: goto L_0x01df;
                case 200: goto L_0x01a0;
                case 403: goto L_0x0192;
                case 487: goto L_0x01cd;
                case 490: goto L_0x01fd;
                case 498: goto L_0x0202;
                case 499: goto L_0x0207;
                default: goto L_0x0172;
            }     // Catch:{ all -> 0x005a }
        L_0x0172:
            r11 = 19
        L_0x0174:
            if (r13 == 0) goto L_0x020c
            r16 = 60000(0xea60, double:2.9644E-319)
            r0 = r22
            r1 = r16
            r0.scheduleAlarm(r1)     // Catch:{ all -> 0x005a }
        L_0x0180:
            r0 = r22
            com.google.android.vending.expansion.downloader.impl.DownloadNotification r0 = r0.mNotification     // Catch:{ all -> 0x005a }
            r16 = r0
            r0 = r16
            r0.onDownloadStateChanged(r11)     // Catch:{ all -> 0x005a }
            r16 = 0
            setServiceRunning(r16)
            goto L_0x0041
        L_0x0192:
            r0 = r22
            r1 = r22
            r0.updateLVL(r1)     // Catch:{ all -> 0x005a }
            r16 = 0
            setServiceRunning(r16)
            goto L_0x0041
        L_0x01a0:
            r0 = r22
            long r0 = r0.mBytesSoFar     // Catch:{ all -> 0x005a }
            r18 = r0
            long r0 = r8.mCurrentBytes     // Catch:{ all -> 0x005a }
            r20 = r0
            long r20 = r20 - r14
            long r18 = r18 + r20
            r0 = r18
            r2 = r22
            r2.mBytesSoFar = r0     // Catch:{ all -> 0x005a }
            r0 = r22
            android.content.pm.PackageInfo r0 = r0.mPackageInfo     // Catch:{ all -> 0x005a }
            r18 = r0
            r0 = r18
            int r0 = r0.versionCode     // Catch:{ all -> 0x005a }
            r18 = r0
            r19 = 0
            r0 = r18
            r1 = r19
            r6.updateMetadata(r0, r1)     // Catch:{ all -> 0x005a }
            int r16 = r16 + 1
            goto L_0x0130
        L_0x01cd:
            r11 = 13
            r16 = 0
            r0 = r16
            r8.mCurrentBytes = r0     // Catch:{ all -> 0x005a }
            r6.updateDownload(r8)     // Catch:{ all -> 0x005a }
            r13 = 1
            goto L_0x0174
        L_0x01da:
            r11 = 7
            goto L_0x0174
        L_0x01dc:
            r11 = 6
            r13 = 1
            goto L_0x0174
        L_0x01df:
            r0 = r22
            android.net.wifi.WifiManager r0 = r0.mWifiManager     // Catch:{ all -> 0x005a }
            r16 = r0
            if (r16 == 0) goto L_0x01f8
            r0 = r22
            android.net.wifi.WifiManager r0 = r0.mWifiManager     // Catch:{ all -> 0x005a }
            r16 = r0
            boolean r16 = r16.isWifiEnabled()     // Catch:{ all -> 0x005a }
            if (r16 != 0) goto L_0x01f8
            r11 = 8
            r13 = 1
            goto L_0x0174
        L_0x01f8:
            r11 = 9
            r13 = 1
            goto L_0x0174
        L_0x01fd:
            r11 = 18
            r13 = 1
            goto L_0x0174
        L_0x0202:
            r11 = 17
            r13 = 1
            goto L_0x0174
        L_0x0207:
            r11 = 14
            r13 = 1
            goto L_0x0174
        L_0x020c:
            r22.cancelAlarms()     // Catch:{ all -> 0x005a }
            goto L_0x0180
        L_0x0211:
            r0 = r22
            com.google.android.vending.expansion.downloader.impl.DownloadNotification r0 = r0.mNotification     // Catch:{ all -> 0x005a }
            r16 = r0
            r17 = 5
            r16.onDownloadStateChanged(r17)     // Catch:{ all -> 0x005a }
            r16 = 0
            setServiceRunning(r16)
            goto L_0x0041
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.vending.expansion.downloader.impl.DownloaderService.onHandleIntent(android.content.Intent):void");
    }

    public void onDestroy() {
        if (this.mConnReceiver != null) {
            unregisterReceiver(this.mConnReceiver);
            this.mConnReceiver = null;
        }
        this.mServiceStub.disconnect(this);
        super.onDestroy();
    }

    public int getNetworkAvailabilityState(DownloadsDB db) {
        if (!this.mIsConnected) {
            return 2;
        }
        if (!this.mIsCellularConnection) {
            return 1;
        }
        int flags = db.mFlags;
        if (this.mIsRoaming) {
            return 5;
        }
        if ((flags & 1) == 0) {
            return 6;
        }
        return 1;
    }

    public void onCreate() {
        super.onCreate();
        try {
            this.mPackageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            this.mNotification = new DownloadNotification(this, getPackageManager().getApplicationLabel(getApplicationInfo()));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static class GenerateSaveFileError extends Exception {
        private static final long serialVersionUID = 3465966015408936540L;
        String mMessage;
        int mStatus;

        public GenerateSaveFileError(int status, String message) {
            this.mStatus = status;
            this.mMessage = message;
        }
    }

    public String generateTempSaveFileName(String fileName) {
        return Helpers.getSaveFilePath(this) + File.separator + fileName + TEMP_EXT;
    }

    public String generateSaveFile(String filename, long filesize) throws GenerateSaveFileError {
        String path = generateTempSaveFileName(filename);
        File expPath = new File(path);
        if (!Helpers.isExternalMediaMounted()) {
            LOG.debug("External media not mounted: " + path);
            throw new GenerateSaveFileError(499, "external media is not yet mounted");
        } else if (expPath.exists()) {
            LOG.debug("File already exists: " + path);
            throw new GenerateSaveFileError(488, "requested destination file already exists");
        } else if (Helpers.getAvailableBytes(Helpers.getFilesystemRoot(path)) >= filesize) {
            return path;
        } else {
            throw new GenerateSaveFileError(498, "insufficient space on external storage");
        }
    }

    public String getLogMessageForNetworkError(int networkError) {
        switch (networkError) {
            case 2:
                return "no network connection available";
            case 3:
                return "download size exceeds limit for mobile network";
            case 4:
                return "download size exceeds recommended limit for mobile network";
            case 5:
                return "download cannot use the current network connection because it is roaming";
            case 6:
                return "download was requested to not use the current network type";
            default:
                return "unknown error with network connectivity";
        }
    }

    public int getControl() {
        return this.mControl;
    }

    public int getStatus() {
        return this.mStatus;
    }

    public void notifyUpdateBytes(long totalBytesSoFar) {
        long timeRemaining;
        long currentTime = SystemClock.uptimeMillis();
        if (0 != this.mMillisecondsAtSample) {
            float currentSpeedSample = ((float) (totalBytesSoFar - this.mBytesAtSample)) / ((float) (currentTime - this.mMillisecondsAtSample));
            if (0.0f != this.mAverageDownloadSpeed) {
                this.mAverageDownloadSpeed = (SMOOTHING_FACTOR * currentSpeedSample) + (0.995f * this.mAverageDownloadSpeed);
            } else {
                this.mAverageDownloadSpeed = currentSpeedSample;
            }
            timeRemaining = (long) (((float) (this.mTotalLength - totalBytesSoFar)) / this.mAverageDownloadSpeed);
        } else {
            timeRemaining = -1;
        }
        this.mMillisecondsAtSample = currentTime;
        this.mBytesAtSample = totalBytesSoFar;
        this.mNotification.onDownloadProgress(new DownloadProgressInfo(this.mTotalLength, totalBytesSoFar, timeRemaining, this.mAverageDownloadSpeed));
    }

    /* access modifiers changed from: protected */
    public boolean shouldStop() {
        if (DownloadsDB.getDB(this).mStatus == 0) {
            return true;
        }
        return false;
    }

    public void requestDownloadStatus() {
        this.mNotification.resendState();
    }

    public void onClientUpdated(Messenger clientMessenger) {
        this.mClientMessenger = clientMessenger;
        this.mNotification.setMessenger(this.mClientMessenger);
    }
}
