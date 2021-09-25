package com.freevisiontech.fvmobile.widget.view;

import android.os.Environment;
import java.io.File;

public class IntentKey {
    public static final String APK_PATH = (Environment.getExternalStorageDirectory().toString() + File.separator + "FVMobile/download/");
    public static final String BIAO_ZI = "baiozi";
    public static final String CLASS_JSON = "class_json";
    public static final String CLASS_POSION = "class_posion";
    public static final String FILE_PATH = (Environment.getExternalStorageDirectory().toString() + File.separator + "DCIM/fvmobile/");
    public static final String FILE_PATH_CAMER = "/storage/emulated/0/DCIM/Camera/";
    public static final String PAINTPATH = "paint_path";
    public static final String PHOTOEDITPATH = "photo_edit_path";
    public static final String SHOW_TEXTVIEW = "show_textview";
    public static final String VIDEOS_PATH = "videos_path";
}
