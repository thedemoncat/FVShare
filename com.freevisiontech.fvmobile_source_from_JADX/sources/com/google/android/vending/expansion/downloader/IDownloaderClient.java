package com.google.android.vending.expansion.downloader;

import android.os.Messenger;

public interface IDownloaderClient {
    public static final int STATE_COMPLETED = 5;
    public static final int STATE_CONNECTING = 3;
    public static final int STATE_DOWNLOADING = 4;
    public static final int STATE_FAILED = 19;
    public static final int STATE_FAILED_CANCELED = 18;
    public static final int STATE_FAILED_FETCHING_URL = 16;
    public static final int STATE_FAILED_SDCARD_FULL = 17;
    public static final int STATE_FAILED_UNLICENSED = 15;
    public static final int STATE_FETCHING_URL = 2;
    public static final int STATE_IDLE = 1;
    public static final int STATE_PAUSED_BY_REQUEST = 7;
    public static final int STATE_PAUSED_NEED_CELLULAR_PERMISSION = 9;
    public static final int STATE_PAUSED_NEED_WIFI = 11;
    public static final int STATE_PAUSED_NETWORK_SETUP_FAILURE = 13;
    public static final int STATE_PAUSED_NETWORK_UNAVAILABLE = 6;
    public static final int STATE_PAUSED_ROAMING = 12;
    public static final int STATE_PAUSED_SDCARD_UNAVAILABLE = 14;
    public static final int STATE_PAUSED_WIFI_DISABLED = 10;
    public static final int STATE_PAUSED_WIFI_DISABLED_NEED_CELLULAR_PERMISSION = 8;

    void onDownloadProgress(DownloadProgressInfo downloadProgressInfo);

    void onDownloadStateChanged(int i);

    void onServiceConnected(Messenger messenger);
}
