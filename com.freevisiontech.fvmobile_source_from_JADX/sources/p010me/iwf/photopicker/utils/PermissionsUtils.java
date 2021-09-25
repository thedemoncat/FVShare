package p010me.iwf.photopicker.utils;

import android.app.Activity;
import android.os.Build;
import android.support.p001v4.app.ActivityCompat;
import android.support.p001v4.app.Fragment;
import android.support.p001v4.content.ContextCompat;

/* renamed from: me.iwf.photopicker.utils.PermissionsUtils */
public class PermissionsUtils {
    public static boolean checkReadStoragePermission(Activity activity) {
        boolean readStoragePermissionGranted = true;
        if (Build.VERSION.SDK_INT >= 16) {
            if (ContextCompat.checkSelfPermission(activity, "android.permission.READ_EXTERNAL_STORAGE") != 0) {
                readStoragePermissionGranted = false;
            }
            if (!readStoragePermissionGranted) {
                ActivityCompat.requestPermissions(activity, PermissionsConstant.PERMISSIONS_EXTERNAL_READ, 2);
            }
        }
        return readStoragePermissionGranted;
    }

    public static boolean checkWriteStoragePermission(Fragment fragment) {
        boolean writeStoragePermissionGranted = ContextCompat.checkSelfPermission(fragment.getContext(), "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
        if (!writeStoragePermissionGranted) {
            fragment.requestPermissions(PermissionsConstant.PERMISSIONS_EXTERNAL_WRITE, 3);
        }
        return writeStoragePermissionGranted;
    }

    public static boolean checkCameraPermission(Fragment fragment) {
        boolean cameraPermissionGranted = ContextCompat.checkSelfPermission(fragment.getContext(), "android.permission.CAMERA") == 0;
        if (!cameraPermissionGranted) {
            fragment.requestPermissions(PermissionsConstant.PERMISSIONS_CAMERA, 1);
        }
        return cameraPermissionGranted;
    }
}
