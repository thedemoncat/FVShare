package p010me.iwf.photopicker.utils;

import android.support.annotation.RequiresApi;

/* renamed from: me.iwf.photopicker.utils.PermissionsConstant */
public class PermissionsConstant {
    public static final String[] PERMISSIONS_CAMERA = {"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
    @RequiresApi(api = 16)
    public static final String[] PERMISSIONS_EXTERNAL_READ = {"android.permission.READ_EXTERNAL_STORAGE"};
    public static final String[] PERMISSIONS_EXTERNAL_WRITE = {"android.permission.WRITE_EXTERNAL_STORAGE"};
    public static final int REQUEST_CAMERA = 1;
    public static final int REQUEST_EXTERNAL_READ = 2;
    public static final int REQUEST_EXTERNAL_WRITE = 3;
}
