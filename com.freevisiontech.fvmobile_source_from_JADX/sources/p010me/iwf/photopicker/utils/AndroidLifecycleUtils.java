package p010me.iwf.photopicker.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.p001v4.app.Fragment;

/* renamed from: me.iwf.photopicker.utils.AndroidLifecycleUtils */
public class AndroidLifecycleUtils {
    public static boolean canLoadImage(Fragment fragment) {
        if (fragment == null) {
            return true;
        }
        return canLoadImage((Activity) fragment.getActivity());
    }

    public static boolean canLoadImage(Context context) {
        if (context != null && (context instanceof Activity)) {
            return canLoadImage((Activity) context);
        }
        return true;
    }

    public static boolean canLoadImage(Activity activity) {
        boolean destroyed;
        if (activity == null) {
            return true;
        }
        if (Build.VERSION.SDK_INT < 17 || !activity.isDestroyed()) {
            destroyed = false;
        } else {
            destroyed = true;
        }
        if (destroyed || activity.isFinishing()) {
            return false;
        }
        return true;
    }
}
