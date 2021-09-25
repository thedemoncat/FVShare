package org.opencv.android;

public interface InstallCallbackInterface {
    public static final int INSTALLATION_PROGRESS = 1;
    public static final int NEW_INSTALLATION = 0;

    void cancel();

    String getPackageName();

    void install();

    void wait_install();
}
