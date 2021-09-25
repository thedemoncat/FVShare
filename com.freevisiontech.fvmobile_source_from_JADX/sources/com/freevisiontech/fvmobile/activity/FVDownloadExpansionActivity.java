package com.freevisiontech.fvmobile.activity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Messenger;
import android.support.p003v7.app.AlertDialog;
import android.support.p003v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.coremedia.iso.boxes.FreeSpaceBox;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.utils.DownloadExpansionService;
import com.freevisiontech.fvmobile.utils.LocalResourceManager;
import com.google.android.exoplayer.C1907C;
import com.google.android.vending.expansion.downloader.DownloadProgressInfo;
import com.google.android.vending.expansion.downloader.DownloaderClientMarshaller;
import com.google.android.vending.expansion.downloader.DownloaderServiceMarshaller;
import com.google.android.vending.expansion.downloader.Helpers;
import com.google.android.vending.expansion.downloader.IDownloaderClient;
import com.google.android.vending.expansion.downloader.IDownloaderService;
import com.google.android.vending.expansion.downloader.IStub;

public class FVDownloadExpansionActivity extends AppCompatActivity implements IDownloaderClient {
    private static final String LOG_TAG = "LVLDownloader";
    private static final float SMOOTHING_FACTOR = 0.005f;
    private TextView mAverageSpeed;
    /* access modifiers changed from: private */
    public boolean mCancelValidation;
    /* access modifiers changed from: private */
    public View mCellMessage;
    /* access modifiers changed from: private */
    public Context mContext = this;
    /* access modifiers changed from: private */
    public View mDashboard;
    private IStub mDownloaderClientStub;
    private ProgressBar mPB;
    /* access modifiers changed from: private */
    public Button mPauseButton;
    private TextView mProgressFraction;
    private TextView mProgressPercent;
    /* access modifiers changed from: private */
    public IDownloaderService mRemoteService;
    private int mState;
    /* access modifiers changed from: private */
    public boolean mStatePaused;
    /* access modifiers changed from: private */
    public TextView mStatusText;
    private TextView mTimeRemaining;
    private Button mWiFiSettingsButton;
    private boolean showSkip = true;
    /* access modifiers changed from: private */
    public AlertDialog skipDialog;

    private void setState(int newState) {
        if (this.mState != newState) {
            this.mState = newState;
            this.mStatusText.setText(Helpers.getDownloaderStringResourceIDFromState(newState));
        }
    }

    /* access modifiers changed from: private */
    public void setButtonPausedState(boolean paused) {
        this.mStatePaused = paused;
        this.mPauseButton.setText(paused ? C0853R.string.text_button_resume : C0853R.string.text_button_pause);
        if (paused) {
            skipDownloadExpansion();
        }
    }

    /* access modifiers changed from: package-private */
    public void validateXAPKZipFiles() {
        new AsyncTask<Object, DownloadProgressInfo, Boolean>() {
            /* access modifiers changed from: protected */
            public void onPreExecute() {
                FVDownloadExpansionActivity.this.mDashboard.setVisibility(0);
                FVDownloadExpansionActivity.this.mCellMessage.setVisibility(8);
                FVDownloadExpansionActivity.this.mStatusText.setText(C0853R.string.text_verifying_download);
                FVDownloadExpansionActivity.this.mPauseButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        boolean unused = FVDownloadExpansionActivity.this.mCancelValidation = true;
                    }
                });
                FVDownloadExpansionActivity.this.mPauseButton.setText(C0853R.string.text_button_cancel_verify);
                super.onPreExecute();
            }

            /* access modifiers changed from: protected */
            /* JADX WARNING: Code restructure failed: missing block: B:36:0x013a, code lost:
                if (r0 == null) goto L_?;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:38:?, code lost:
                r0.close();
             */
            /* JADX WARNING: Code restructure failed: missing block: B:46:0x015f, code lost:
                android.util.Log.e(com.google.android.vending.expansion.downloader.Constants.TAG, "CRC does not match for entry: " + r20.mFileName);
                android.util.Log.e(com.google.android.vending.expansion.downloader.Constants.TAG, "In file: " + r20.getZipFileName());
             */
            /* JADX WARNING: Code restructure failed: missing block: B:47:0x01a0, code lost:
                if (r0 == null) goto L_?;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:49:?, code lost:
                r0.close();
             */
            /* JADX WARNING: Code restructure failed: missing block: B:71:?, code lost:
                return true;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:73:?, code lost:
                return false;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:74:?, code lost:
                return false;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:76:?, code lost:
                return true;
             */
            /* JADX WARNING: Removed duplicated region for block: B:55:0x01b5 A[Catch:{ IOException -> 0x0141 }] */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public java.lang.Boolean doInBackground(java.lang.Object... r41) {
                /*
                    r40 = this;
                    r0 = r40
                    com.freevisiontech.fvmobile.activity.FVDownloadExpansionActivity r3 = com.freevisiontech.fvmobile.activity.FVDownloadExpansionActivity.this
                    android.content.Context r3 = r3.mContext
                    com.google.android.vending.expansion.downloader.impl.DownloadsDB r13 = com.google.android.vending.expansion.downloader.impl.DownloadsDB.getDB(r3)
                    com.google.android.vending.expansion.downloader.impl.DownloadInfo[] r23 = r13.getDownloads()
                    r0 = r23
                    int r0 = r0.length
                    r36 = r0
                    r3 = 0
                    r35 = r3
                L_0x0018:
                    r0 = r35
                    r1 = r36
                    if (r0 >= r1) goto L_0x01bf
                    r22 = r23[r35]
                    r0 = r22
                    java.lang.String r0 = r0.mFileName
                    r21 = r0
                    r0 = r40
                    com.freevisiontech.fvmobile.activity.FVDownloadExpansionActivity r3 = com.freevisiontech.fvmobile.activity.FVDownloadExpansionActivity.this
                    r0 = r22
                    long r6 = r0.mTotalBytes
                    r34 = 0
                    r0 = r21
                    r1 = r34
                    boolean r3 = com.google.android.vending.expansion.downloader.Helpers.doesFileExist(r3, r0, r6, r1)
                    if (r3 != 0) goto L_0x0040
                    r3 = 0
                    java.lang.Boolean r3 = java.lang.Boolean.valueOf(r3)
                L_0x003f:
                    return r3
                L_0x0040:
                    r0 = r40
                    com.freevisiontech.fvmobile.activity.FVDownloadExpansionActivity r3 = com.freevisiontech.fvmobile.activity.FVDownloadExpansionActivity.this
                    r0 = r21
                    java.lang.String r21 = com.google.android.vending.expansion.downloader.Helpers.generateSaveFileName(r3, r0)
                    r3 = 262144(0x40000, float:3.67342E-40)
                    byte[] r2 = new byte[r3]
                    com.android.vending.expansion.zipfile.ZipResourceFile r27 = new com.android.vending.expansion.zipfile.ZipResourceFile     // Catch:{ IOException -> 0x0141 }
                    r0 = r27
                    r1 = r21
                    r0.<init>(r1)     // Catch:{ IOException -> 0x0141 }
                    com.android.vending.expansion.zipfile.ZipResourceFile$ZipEntryRO[] r19 = r27.getAllEntries()     // Catch:{ IOException -> 0x0141 }
                    r4 = 0
                    r0 = r19
                    int r6 = r0.length     // Catch:{ IOException -> 0x0141 }
                    r3 = 0
                L_0x0061:
                    if (r3 >= r6) goto L_0x0080
                    r20 = r19[r3]     // Catch:{ IOException -> 0x0141 }
                    r0 = r20
                    long r0 = r0.mCompressedLength     // Catch:{ IOException -> 0x0141 }
                    r38 = r0
                    long r4 = r4 + r38
                    java.lang.String r7 = "getZipFileName: "
                    android.content.res.AssetFileDescriptor r34 = r20.getAssetFileDescriptor()     // Catch:{ IOException -> 0x0141 }
                    java.lang.String r34 = java.lang.String.valueOf(r34)     // Catch:{ IOException -> 0x0141 }
                    r0 = r34
                    android.util.Log.e(r7, r0)     // Catch:{ IOException -> 0x0141 }
                    int r3 = r3 + 1
                    goto L_0x0061
                L_0x0080:
                    r10 = 0
                    r32 = r4
                    r0 = r19
                    int r0 = r0.length     // Catch:{ IOException -> 0x0141 }
                    r37 = r0
                    r3 = 0
                    r34 = r3
                L_0x008b:
                    r0 = r34
                    r1 = r37
                    if (r0 >= r1) goto L_0x01b9
                    r20 = r19[r34]     // Catch:{ IOException -> 0x0141 }
                    r6 = -1
                    r0 = r20
                    long r0 = r0.mCRC32     // Catch:{ IOException -> 0x0141 }
                    r38 = r0
                    int r3 = (r6 > r38 ? 1 : (r6 == r38 ? 0 : -1))
                    if (r3 == 0) goto L_0x01ac
                    r0 = r20
                    long r0 = r0.mUncompressedLength     // Catch:{ IOException -> 0x0141 }
                    r24 = r0
                    java.util.zip.CRC32 r11 = new java.util.zip.CRC32     // Catch:{ IOException -> 0x0141 }
                    r11.<init>()     // Catch:{ IOException -> 0x0141 }
                    r16 = 0
                    java.io.DataInputStream r17 = new java.io.DataInputStream     // Catch:{ all -> 0x01b2 }
                    r0 = r20
                    java.lang.String r3 = r0.mFileName     // Catch:{ all -> 0x01b2 }
                    r0 = r27
                    java.io.InputStream r3 = r0.getInputStream(r3)     // Catch:{ all -> 0x01b2 }
                    r0 = r17
                    r0.<init>(r3)     // Catch:{ all -> 0x01b2 }
                    long r28 = android.os.SystemClock.uptimeMillis()     // Catch:{ all -> 0x01c6 }
                L_0x00c1:
                    r6 = 0
                    int r3 = (r24 > r6 ? 1 : (r24 == r6 ? 0 : -1))
                    if (r3 <= 0) goto L_0x0151
                    int r3 = r2.length     // Catch:{ all -> 0x01c6 }
                    long r6 = (long) r3     // Catch:{ all -> 0x01c6 }
                    int r3 = (r24 > r6 ? 1 : (r24 == r6 ? 0 : -1))
                    if (r3 <= 0) goto L_0x014c
                    int r3 = r2.length     // Catch:{ all -> 0x01c6 }
                    long r6 = (long) r3     // Catch:{ all -> 0x01c6 }
                L_0x00cf:
                    int r0 = (int) r6     // Catch:{ all -> 0x01c6 }
                    r26 = r0
                    r3 = 0
                    r0 = r17
                    r1 = r26
                    r0.readFully(r2, r3, r1)     // Catch:{ all -> 0x01c6 }
                    r3 = 0
                    r0 = r26
                    r11.update(r2, r3, r0)     // Catch:{ all -> 0x01c6 }
                    r0 = r26
                    long r6 = (long) r0     // Catch:{ all -> 0x01c6 }
                    long r24 = r24 - r6
                    long r14 = android.os.SystemClock.uptimeMillis()     // Catch:{ all -> 0x01c6 }
                    long r30 = r14 - r28
                    r6 = 0
                    int r3 = (r30 > r6 ? 1 : (r30 == r6 ? 0 : -1))
                    if (r3 <= 0) goto L_0x0129
                    r0 = r26
                    float r3 = (float) r0     // Catch:{ all -> 0x01c6 }
                    r0 = r30
                    float r6 = (float) r0     // Catch:{ all -> 0x01c6 }
                    float r12 = r3 / r6
                    r3 = 0
                    int r3 = (r3 > r10 ? 1 : (r3 == r10 ? 0 : -1))
                    if (r3 == 0) goto L_0x014f
                    r3 = 1000593162(0x3ba3d70a, float:0.005)
                    float r3 = r3 * r12
                    r6 = 1065269330(0x3f7eb852, float:0.995)
                    float r6 = r6 * r10
                    float r10 = r3 + r6
                L_0x0108:
                    r0 = r26
                    long r6 = (long) r0     // Catch:{ all -> 0x01c6 }
                    long r32 = r32 - r6
                    r0 = r32
                    float r3 = (float) r0     // Catch:{ all -> 0x01c6 }
                    float r3 = r3 / r10
                    long r8 = (long) r3     // Catch:{ all -> 0x01c6 }
                    r3 = 1
                    com.google.android.vending.expansion.downloader.DownloadProgressInfo[] r0 = new com.google.android.vending.expansion.downloader.DownloadProgressInfo[r3]     // Catch:{ all -> 0x01c6 }
                    r38 = r0
                    r39 = 0
                    com.google.android.vending.expansion.downloader.DownloadProgressInfo r3 = new com.google.android.vending.expansion.downloader.DownloadProgressInfo     // Catch:{ all -> 0x01c6 }
                    long r6 = r4 - r32
                    r3.<init>(r4, r6, r8, r10)     // Catch:{ all -> 0x01c6 }
                    r38[r39] = r3     // Catch:{ all -> 0x01c6 }
                    r0 = r40
                    r1 = r38
                    r0.publishProgress(r1)     // Catch:{ all -> 0x01c6 }
                L_0x0129:
                    r28 = r14
                    r0 = r40
                    com.freevisiontech.fvmobile.activity.FVDownloadExpansionActivity r3 = com.freevisiontech.fvmobile.activity.FVDownloadExpansionActivity.this     // Catch:{ all -> 0x01c6 }
                    boolean r3 = r3.mCancelValidation     // Catch:{ all -> 0x01c6 }
                    if (r3 == 0) goto L_0x00c1
                    r3 = 1
                    java.lang.Boolean r3 = java.lang.Boolean.valueOf(r3)     // Catch:{ all -> 0x01c6 }
                    if (r17 == 0) goto L_0x003f
                    r17.close()     // Catch:{ IOException -> 0x0141 }
                    goto L_0x003f
                L_0x0141:
                    r18 = move-exception
                    r18.printStackTrace()
                    r3 = 0
                    java.lang.Boolean r3 = java.lang.Boolean.valueOf(r3)
                    goto L_0x003f
                L_0x014c:
                    r6 = r24
                    goto L_0x00cf
                L_0x014f:
                    r10 = r12
                    goto L_0x0108
                L_0x0151:
                    long r6 = r11.getValue()     // Catch:{ all -> 0x01c6 }
                    r0 = r20
                    long r0 = r0.mCRC32     // Catch:{ all -> 0x01c6 }
                    r38 = r0
                    int r3 = (r6 > r38 ? 1 : (r6 == r38 ? 0 : -1))
                    if (r3 == 0) goto L_0x01a7
                    java.lang.String r3 = "LVLDL"
                    java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x01c6 }
                    r6.<init>()     // Catch:{ all -> 0x01c6 }
                    java.lang.String r7 = "CRC does not match for entry: "
                    java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ all -> 0x01c6 }
                    r0 = r20
                    java.lang.String r7 = r0.mFileName     // Catch:{ all -> 0x01c6 }
                    java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ all -> 0x01c6 }
                    java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x01c6 }
                    android.util.Log.e(r3, r6)     // Catch:{ all -> 0x01c6 }
                    java.lang.String r3 = "LVLDL"
                    java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x01c6 }
                    r6.<init>()     // Catch:{ all -> 0x01c6 }
                    java.lang.String r7 = "In file: "
                    java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ all -> 0x01c6 }
                    java.lang.String r7 = r20.getZipFileName()     // Catch:{ all -> 0x01c6 }
                    java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ all -> 0x01c6 }
                    java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x01c6 }
                    android.util.Log.e(r3, r6)     // Catch:{ all -> 0x01c6 }
                    r3 = 0
                    java.lang.Boolean r3 = java.lang.Boolean.valueOf(r3)     // Catch:{ all -> 0x01c6 }
                    if (r17 == 0) goto L_0x003f
                    r17.close()     // Catch:{ IOException -> 0x0141 }
                    goto L_0x003f
                L_0x01a7:
                    if (r17 == 0) goto L_0x01ac
                    r17.close()     // Catch:{ IOException -> 0x0141 }
                L_0x01ac:
                    int r3 = r34 + 1
                    r34 = r3
                    goto L_0x008b
                L_0x01b2:
                    r3 = move-exception
                L_0x01b3:
                    if (r16 == 0) goto L_0x01b8
                    r16.close()     // Catch:{ IOException -> 0x0141 }
                L_0x01b8:
                    throw r3     // Catch:{ IOException -> 0x0141 }
                L_0x01b9:
                    int r3 = r35 + 1
                    r35 = r3
                    goto L_0x0018
                L_0x01bf:
                    r3 = 1
                    java.lang.Boolean r3 = java.lang.Boolean.valueOf(r3)
                    goto L_0x003f
                L_0x01c6:
                    r3 = move-exception
                    r16 = r17
                    goto L_0x01b3
                */
                throw new UnsupportedOperationException("Method not decompiled: com.freevisiontech.fvmobile.activity.FVDownloadExpansionActivity.C09181.doInBackground(java.lang.Object[]):java.lang.Boolean");
            }

            /* access modifiers changed from: protected */
            public void onProgressUpdate(DownloadProgressInfo... values) {
                FVDownloadExpansionActivity.this.onDownloadProgress(values[0]);
                super.onProgressUpdate(values);
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(Boolean result) {
                if (result.booleanValue()) {
                    FVDownloadExpansionActivity.this.mDashboard.setVisibility(0);
                    FVDownloadExpansionActivity.this.mCellMessage.setVisibility(8);
                    FVDownloadExpansionActivity.this.mStatusText.setText(C0853R.string.text_validation_complete);
                    FVDownloadExpansionActivity.this.startActivity(new Intent(FVDownloadExpansionActivity.this, FVHomeActivity.class));
                    FVDownloadExpansionActivity.this.finish();
                    FVDownloadExpansionActivity.this.mPauseButton.setText(17039370);
                } else {
                    FVDownloadExpansionActivity.this.mDashboard.setVisibility(0);
                    FVDownloadExpansionActivity.this.mCellMessage.setVisibility(8);
                    FVDownloadExpansionActivity.this.mStatusText.setText(C0853R.string.text_validation_failed);
                    FVDownloadExpansionActivity.this.mPauseButton.setText(17039360);
                    FVDownloadExpansionActivity.this.finish();
                }
                super.onPostExecute(result);
            }
        }.execute(new Object[]{new Object()});
    }

    private void initializeDownloadUI() {
        this.mDownloaderClientStub = DownloaderClientMarshaller.CreateStub(this, DownloadExpansionService.class);
        setContentView((int) C0853R.layout.activity_downlaod_expansion);
        this.mPB = (ProgressBar) findViewById(C0853R.C0855id.progressBar);
        this.mStatusText = (TextView) findViewById(C0853R.C0855id.statusText);
        this.mProgressFraction = (TextView) findViewById(C0853R.C0855id.progressAsFraction);
        this.mProgressPercent = (TextView) findViewById(C0853R.C0855id.progressAsPercentage);
        this.mAverageSpeed = (TextView) findViewById(C0853R.C0855id.progressAverageSpeed);
        this.mTimeRemaining = (TextView) findViewById(C0853R.C0855id.progressTimeRemaining);
        this.mDashboard = findViewById(C0853R.C0855id.downloaderDashboard);
        this.mCellMessage = findViewById(C0853R.C0855id.approveCellular);
        this.mPauseButton = (Button) findViewById(C0853R.C0855id.pauseButton);
        this.mWiFiSettingsButton = (Button) findViewById(C0853R.C0855id.wifiSettingsButton);
        this.mPauseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (FVDownloadExpansionActivity.this.mStatePaused) {
                    FVDownloadExpansionActivity.this.mRemoteService.requestContinueDownload();
                } else {
                    FVDownloadExpansionActivity.this.mRemoteService.requestPauseDownload();
                }
                FVDownloadExpansionActivity.this.setButtonPausedState(!FVDownloadExpansionActivity.this.mStatePaused);
            }
        });
        this.mWiFiSettingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FVDownloadExpansionActivity.this.startActivity(new Intent("android.settings.WIFI_SETTINGS"));
            }
        });
        ((Button) findViewById(C0853R.C0855id.resumeOverCellular)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FVDownloadExpansionActivity.this.mRemoteService.setDownloadFlags(1);
                FVDownloadExpansionActivity.this.mRemoteService.requestContinueDownload();
                FVDownloadExpansionActivity.this.mCellMessage.setVisibility(8);
            }
        });
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        initializeDownloadUI();
        if (!LocalResourceManager.expansionFilesDelivered(this)) {
            try {
                Intent launchIntent = getIntent();
                Intent intentToLaunchThisActivityFromNotification = new Intent(this, getClass());
                intentToLaunchThisActivityFromNotification.setFlags(335544320);
                intentToLaunchThisActivityFromNotification.setAction(launchIntent.getAction());
                if (launchIntent.getCategories() != null) {
                    for (String category : launchIntent.getCategories()) {
                        intentToLaunchThisActivityFromNotification.addCategory(category);
                    }
                }
                if (DownloaderClientMarshaller.startDownloadServiceIfRequired((Context) this, PendingIntent.getActivity(this, 0, intentToLaunchThisActivityFromNotification, C1907C.SAMPLE_FLAG_DECODE_ONLY), (Class<?>) DownloadExpansionService.class) != 0) {
                    initializeDownloadUI();
                }
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(LOG_TAG, "Cannot find own package! MAYDAY!");
                e.printStackTrace();
            }
        } else {
            validateXAPKZipFiles();
        }
    }

    private void skipDownloadExpansion() {
        if (this.skipDialog == null) {
            this.skipDialog = new AlertDialog.Builder(this).setTitle((CharSequence) "Download expansion file failed.").setMessage((CharSequence) "would you skip download it,this may cause some resource unavailable").setNegativeButton((CharSequence) "exit app", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (FVDownloadExpansionActivity.this.skipDialog != null) {
                        FVDownloadExpansionActivity.this.skipDialog.dismiss();
                    }
                    FVDownloadExpansionActivity.this.finish();
                }
            }).setPositiveButton((CharSequence) FreeSpaceBox.TYPE, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (FVDownloadExpansionActivity.this.skipDialog != null) {
                        FVDownloadExpansionActivity.this.skipDialog.dismiss();
                    }
                    FVDownloadExpansionActivity.this.startActivity(new Intent(FVDownloadExpansionActivity.this, FVHomeActivity.class));
                    FVDownloadExpansionActivity.this.finish();
                }
            }).create();
        }
        if (this.showSkip) {
            this.skipDialog.show();
        }
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        this.showSkip = true;
        if (this.mDownloaderClientStub != null) {
            this.mDownloaderClientStub.connect(this);
        }
        super.onStart();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        this.showSkip = false;
        if (this.mDownloaderClientStub != null) {
            this.mDownloaderClientStub.disconnect(this);
        }
        super.onStop();
    }

    public void onServiceConnected(Messenger m) {
        this.mRemoteService = DownloaderServiceMarshaller.CreateProxy(m);
        this.mRemoteService.onClientUpdated(this.mDownloaderClientStub.getMessenger());
    }

    public void onDownloadStateChanged(int newState) {
        boolean paused;
        boolean indeterminate;
        int newDashboardVisibility;
        int cellMessageVisibility;
        setState(newState);
        boolean showDashboard = true;
        boolean showCellMessage = false;
        switch (newState) {
            case 1:
                paused = false;
                indeterminate = true;
                break;
            case 2:
            case 3:
                showDashboard = true;
                paused = false;
                indeterminate = true;
                break;
            case 4:
                paused = false;
                showDashboard = true;
                indeterminate = false;
                break;
            case 5:
                validateXAPKZipFiles();
                return;
            case 7:
                paused = true;
                indeterminate = false;
                break;
            case 8:
            case 9:
                showDashboard = false;
                paused = true;
                indeterminate = false;
                showCellMessage = true;
                break;
            case 12:
            case 14:
                paused = true;
                indeterminate = false;
                break;
            case 15:
            case 16:
            case 18:
            case 19:
                paused = true;
                showDashboard = false;
                indeterminate = false;
                break;
            default:
                paused = true;
                indeterminate = true;
                showDashboard = true;
                break;
        }
        if (showDashboard) {
            newDashboardVisibility = 0;
        } else {
            newDashboardVisibility = 8;
        }
        if (this.mDashboard.getVisibility() != newDashboardVisibility) {
            this.mDashboard.setVisibility(newDashboardVisibility);
        }
        if (showCellMessage) {
            cellMessageVisibility = 0;
        } else {
            cellMessageVisibility = 8;
        }
        if (this.mCellMessage.getVisibility() != cellMessageVisibility) {
            this.mCellMessage.setVisibility(cellMessageVisibility);
        }
        this.mPB.setIndeterminate(indeterminate);
        setButtonPausedState(paused);
    }

    public void onDownloadProgress(DownloadProgressInfo progress) {
        this.mAverageSpeed.setText(getString(C0853R.string.kilobytes_per_second, new Object[]{Helpers.getSpeedString(progress.mCurrentSpeed)}));
        this.mTimeRemaining.setText(getString(C0853R.string.time_remaining, new Object[]{Helpers.getTimeRemaining(progress.mTimeRemaining)}));
        progress.mOverallTotal = progress.mOverallTotal;
        this.mPB.setMax((int) (progress.mOverallTotal >> 8));
        this.mPB.setProgress((int) (progress.mOverallProgress >> 8));
        this.mProgressPercent.setText(Long.toString((progress.mOverallProgress * 100) / progress.mOverallTotal) + "%");
        this.mProgressFraction.setText(Helpers.getDownloadProgressString(progress.mOverallProgress, progress.mOverallTotal));
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        this.mCancelValidation = true;
        super.onDestroy();
    }
}
