package p010me.iwf.photopicker.utils;

import java.io.File;

/* renamed from: me.iwf.photopicker.utils.FileUtils */
public class FileUtils {
    public static boolean fileIsExists(String path) {
        if (path == null || path.trim().length() <= 0) {
            return false;
        }
        try {
            if (new File(path).exists()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
