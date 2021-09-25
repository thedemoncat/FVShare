package com.freevisiontech.fvmobile.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.p001v4.content.FileProvider;
import android.text.TextUtils;
import android.widget.Toast;
import java.io.File;

public class ShareUtil {
    public static final String QQ_PACKAGE_NAME = "";
    public static final String WEIXIN_PACKAGE_NAME = "";
    private Context context;

    public ShareUtil(Context context2) {
        this.context = context2;
    }

    public void shareText(String packageName, String className, String content, String title, String subject) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.setType("text/plain");
        if (stringCheck(className) && stringCheck(packageName)) {
            intent.setComponent(new ComponentName(packageName, className));
        } else if (stringCheck(packageName)) {
            intent.setPackage(packageName);
        }
        intent.putExtra("android.intent.extra.TEXT", content);
        if (title != null && !TextUtils.isEmpty(title)) {
            intent.putExtra("android.intent.extra.TITLE", title);
        }
        if (subject != null && !TextUtils.isEmpty(subject)) {
            intent.putExtra("android.intent.extra.SUBJECT", subject);
        }
        intent.putExtra("android.intent.extra.TITLE", title);
        this.context.startActivity(Intent.createChooser(intent, "分享到："));
    }

    public void shareUrl(String packageName, String className, String content, String title, String subject) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.setType("text/plain");
        if (stringCheck(className) && stringCheck(packageName)) {
            intent.setComponent(new ComponentName(packageName, className));
        } else if (stringCheck(packageName)) {
            intent.setPackage(packageName);
        }
        intent.putExtra("android.intent.extra.TEXT", content);
        if (title != null && !TextUtils.isEmpty(title)) {
            intent.putExtra("android.intent.extra.TITLE", title);
        }
        if (subject != null && !TextUtils.isEmpty(subject)) {
            intent.putExtra("android.intent.extra.SUBJECT", subject);
        }
        intent.putExtra("android.intent.extra.TITLE", title);
        this.context.startActivity(Intent.createChooser(intent, "分享到："));
    }

    public void shareImg(String packageName, String className, File file) {
        if (file.exists()) {
            Uri uri = FileProvider.getUriForFile(this.context, this.context.getApplicationContext().getPackageName() + ".fileProvider", file);
            Intent intent = new Intent();
            intent.setAction("android.intent.action.SEND");
            intent.setType("image/*");
            if (stringCheck(packageName) && stringCheck(className)) {
                intent.setComponent(new ComponentName(packageName, className));
            } else if (stringCheck(packageName)) {
                intent.setPackage(packageName);
            }
            intent.putExtra("android.intent.extra.STREAM", uri);
            this.context.startActivity(Intent.createChooser(intent, "分享到:"));
            return;
        }
        Toast.makeText(this.context, "文件不存在", 0).show();
    }

    public void shareAudio(String packageName, String className, File file) {
        if (file.exists()) {
            Uri uri = Uri.fromFile(file);
            Intent intent = new Intent();
            intent.setAction("android.intent.action.SEND");
            intent.setType("audio/*");
            if (stringCheck(packageName) && stringCheck(className)) {
                intent.setComponent(new ComponentName(packageName, className));
            } else if (stringCheck(packageName)) {
                intent.setPackage(packageName);
            }
            intent.putExtra("android.intent.extra.STREAM", uri);
            this.context.startActivity(Intent.createChooser(intent, "分享到:"));
            return;
        }
        Toast.makeText(this.context, "文件不存在", 0).show();
    }

    public void shareVideo(String packageName, String className, File file) {
        setIntent("video/*", packageName, className, file);
    }

    public void setIntent(String type, String packageName, String className, File file) {
        if (file.exists()) {
            Uri uri = FileProvider.getUriForFile(this.context, this.context.getApplicationContext().getPackageName() + ".fileProvider", file);
            Intent intent = new Intent();
            intent.setAction("android.intent.action.SEND");
            intent.setType(type);
            if (stringCheck(packageName) && stringCheck(className)) {
                intent.setComponent(new ComponentName(packageName, className));
            } else if (stringCheck(packageName)) {
                intent.setPackage(packageName);
            }
            intent.putExtra("android.intent.extra.STREAM", uri);
            this.context.startActivity(Intent.createChooser(intent, "分享到:"));
            return;
        }
        Toast.makeText(this.context, "文件不存在", 0).show();
    }

    public void shareImgToWXCircle(String title, String packageName, String className, File file) {
        if (file.exists()) {
            Uri uri = Uri.fromFile(file);
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(packageName, className));
            intent.setAction("android.intent.action.SEND");
            intent.setType("image/*");
            intent.putExtra("android.intent.extra.STREAM", uri);
            intent.putExtra("Kdescription", title);
            this.context.startActivity(intent);
            return;
        }
        Toast.makeText(this.context, "文件不存在", 1).show();
    }

    public boolean checkInstall(String packageName) {
        try {
            this.context.getPackageManager().getPackageInfo(packageName, 1);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this.context, "请先安装应用app", 0).show();
            return false;
        }
    }

    public void toInstallWebView(String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setData(Uri.parse(url));
        this.context.startActivity(intent);
    }

    public static boolean stringCheck(String str) {
        if (str == null || TextUtils.isEmpty(str)) {
            return false;
        }
        return true;
    }
}
