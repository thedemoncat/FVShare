package org.opencv.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

public abstract class BaseLoaderCallback implements LoaderCallbackInterface {
    private static final String TAG = "OpenCVLoader/BaseLoaderCallback";
    protected Context mAppContext;

    public BaseLoaderCallback(Context AppContext) {
        this.mAppContext = AppContext;
    }

    public void onManagerConnected(int status) {
        switch (status) {
            case 0:
                return;
            case 2:
                Log.e(TAG, "Package installation failed!");
                AlertDialog MarketErrorMessage = new AlertDialog.Builder(this.mAppContext).create();
                MarketErrorMessage.setTitle("OpenCV Manager");
                MarketErrorMessage.setMessage("Package installation failed!");
                MarketErrorMessage.setCancelable(false);
                MarketErrorMessage.setButton(-1, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        BaseLoaderCallback.this.finish();
                    }
                });
                MarketErrorMessage.show();
                return;
            case 3:
                Log.d(TAG, "OpenCV library instalation was canceled by user");
                finish();
                return;
            case 4:
                Log.d(TAG, "OpenCV Manager Service is uncompatible with this app!");
                AlertDialog IncomatibilityMessage = new AlertDialog.Builder(this.mAppContext).create();
                IncomatibilityMessage.setTitle("OpenCV Manager");
                IncomatibilityMessage.setMessage("OpenCV Manager service is incompatible with this app. Try to update it via Google Play.");
                IncomatibilityMessage.setCancelable(false);
                IncomatibilityMessage.setButton(-1, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        BaseLoaderCallback.this.finish();
                    }
                });
                IncomatibilityMessage.show();
                return;
            default:
                Log.e(TAG, "OpenCV loading failed!");
                AlertDialog InitFailedDialog = new AlertDialog.Builder(this.mAppContext).create();
                InitFailedDialog.setTitle("OpenCV error");
                InitFailedDialog.setMessage("OpenCV was not initialised correctly. Application will be shut down");
                InitFailedDialog.setCancelable(false);
                InitFailedDialog.setButton(-1, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        BaseLoaderCallback.this.finish();
                    }
                });
                InitFailedDialog.show();
                return;
        }
    }

    public void onPackageInstall(int operation, final InstallCallbackInterface callback) {
        switch (operation) {
            case 0:
                AlertDialog InstallMessage = new AlertDialog.Builder(this.mAppContext).create();
                InstallMessage.setTitle("Package not found");
                InstallMessage.setMessage(callback.getPackageName() + " package was not found! Try to install it?");
                InstallMessage.setCancelable(false);
                InstallMessage.setButton(-1, "Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        callback.install();
                    }
                });
                InstallMessage.setButton(-2, "No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        callback.cancel();
                    }
                });
                InstallMessage.show();
                return;
            case 1:
                AlertDialog WaitMessage = new AlertDialog.Builder(this.mAppContext).create();
                WaitMessage.setTitle("OpenCV is not ready");
                WaitMessage.setMessage("Installation is in progress. Wait or exit?");
                WaitMessage.setCancelable(false);
                WaitMessage.setButton(-1, "Wait", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        callback.wait_install();
                    }
                });
                WaitMessage.setButton(-2, "Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        callback.cancel();
                    }
                });
                WaitMessage.show();
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: package-private */
    public void finish() {
        ((Activity) this.mAppContext).finish();
    }
}
