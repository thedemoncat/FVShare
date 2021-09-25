package com.google.android.vending.expansion.downloader;

import com.lzy.okgo.cache.CacheEntity;
import java.io.File;

public class Constants {
    public static final String ACTION_HIDE = "android.intent.action.DOWNLOAD_HIDE";
    public static final String ACTION_LIST = "android.intent.action.DOWNLOAD_LIST";
    public static final String ACTION_OPEN = "android.intent.action.DOWNLOAD_OPEN";
    public static final String ACTION_RETRY = "android.intent.action.DOWNLOAD_WAKEUP";
    public static final long ACTIVE_THREAD_WATCHDOG = 5000;
    public static final int BUFFER_SIZE = 4096;
    public static final String DEFAULT_USER_AGENT = "Android.LVLDM";
    public static final String EXP_PATH = (File.separator + "Android" + File.separator + "obb" + File.separator);
    public static final String EXP_PATH_API23 = (File.separator + "Android" + File.separator + CacheEntity.DATA + File.separator);
    public static final String FILENAME_SEQUENCE_SEPARATOR = "-";
    private static final boolean LOCAL_LOGVV = false;
    public static final boolean LOGV = false;
    public static final boolean LOGVV = false;
    public static final boolean LOGX = true;
    public static final int MAX_DOWNLOADS = 1000;
    public static final int MAX_REDIRECTS = 10;
    public static final int MAX_RETRIES = 5;
    public static final int MAX_RETRY_AFTER = 86400;
    public static final int MIN_ARTIFICIAL_ERROR_STATUS = 488;
    public static final int MIN_PROGRESS_STEP = 4096;
    public static final long MIN_PROGRESS_TIME = 1000;
    public static final int MIN_RETRY_AFTER = 30;
    public static final int RETRY_FIRST_DELAY = 30;
    public static final int STATUS_BAD_REQUEST = 400;
    public static final int STATUS_CANCELED = 490;
    public static final int STATUS_CANNOT_RESUME = 489;
    public static final int STATUS_DEVICE_NOT_FOUND_ERROR = 499;
    public static final int STATUS_FILE_ALREADY_EXISTS_ERROR = 488;
    public static final int STATUS_FILE_ERROR = 492;
    public static final int STATUS_HTTP_DATA_ERROR = 495;
    public static final int STATUS_HTTP_EXCEPTION = 496;
    public static final int STATUS_INSUFFICIENT_SPACE_ERROR = 498;
    public static final int STATUS_LENGTH_REQUIRED = 411;
    public static final int STATUS_NOT_ACCEPTABLE = 406;
    public static final int STATUS_PRECONDITION_FAILED = 412;
    public static final int STATUS_SUCCESS = 200;
    public static final int STATUS_TOO_MANY_REDIRECTS = 497;
    public static final int STATUS_UNHANDLED_HTTP_CODE = 494;
    public static final int STATUS_UNHANDLED_REDIRECT = 493;
    public static final int STATUS_UNKNOWN_ERROR = 491;
    public static final String TAG = "LVLDL";
    public static final long WATCHDOG_WAKE_TIMER = 60000;
}
