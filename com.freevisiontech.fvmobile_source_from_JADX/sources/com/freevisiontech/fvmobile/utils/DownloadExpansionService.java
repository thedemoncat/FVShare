package com.freevisiontech.fvmobile.utils;

import com.google.android.vending.expansion.downloader.impl.DownloaderService;

public class DownloadExpansionService extends DownloaderService {
    private static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlzxn8pYRL62NnnY9Hg49h7HQjoza1D+9K0tcggBXfTlBx+ZtiU6Rduxe5HEaQH3D3GRzs+SdWGFVVgSKviAzDOrI6+DWCS3pDWlZAbqu92TEWggV5z9DHW0IZlGMevuD3eh8JbmOLwnmkUyMAgPrnxzueodbfme48WVbXfzjl/xo6Hrczpc6ra37uIldDYmepeuu05Ead2edxD7mgLyP3LLSaWN6WeS4smsHY6jInJtM9PQ0rmMusesZKhPicNtCqPqYH2Zac1YaHsfR1pUAN2T1/HxnYFl9SyQRY6TbmE1yU3bIfDqdnfW3pPHOVzkib5nsuRa+zJQZSh/YHQqizwIDAQAB";
    private static final byte[] SALT = {1, 43, -12, -1, 54, 98, -100, -12, 43, 2, -8, -4, 9, 5, -106, -108, -33, ClosedCaptionCtrl.CARRIAGE_RETURN, -1, 84};

    public String getPublicKey() {
        return BASE64_PUBLIC_KEY;
    }

    public byte[] getSALT() {
        return SALT;
    }

    public String getAlarmReceiverClassName() {
        return DownloadExpansionAlarmReceiver.class.getName();
    }
}
