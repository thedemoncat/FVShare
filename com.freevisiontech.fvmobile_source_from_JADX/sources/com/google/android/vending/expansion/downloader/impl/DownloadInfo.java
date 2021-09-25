package com.google.android.vending.expansion.downloader.impl;

import com.google.android.vending.expansion.downloader.Helpers;

public class DownloadInfo {
    public int mControl;
    public long mCurrentBytes;
    public String mETag;
    public final String mFileName;
    public int mFuzz = Helpers.sRandom.nextInt(1001);
    public final int mIndex;
    boolean mInitialized;
    public long mLastMod;
    public int mNumFailed;
    public int mRedirectCount;
    public int mRetryAfter;
    public int mStatus;
    public long mTotalBytes;
    public String mUri;

    public DownloadInfo(int index, String fileName, String pkg) {
        this.mFileName = fileName;
        this.mIndex = index;
    }

    public void resetDownload() {
        this.mCurrentBytes = 0;
        this.mETag = "";
        this.mLastMod = 0;
        this.mStatus = 0;
        this.mControl = 0;
        this.mNumFailed = 0;
        this.mRetryAfter = 0;
        this.mRedirectCount = 0;
    }
}
