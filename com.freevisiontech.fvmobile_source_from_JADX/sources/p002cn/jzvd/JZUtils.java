package p002cn.jzvd;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.p003v7.app.AppCompatActivity;
import android.support.p003v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.Window;
import com.umeng.analytics.C0015a;
import java.util.Formatter;
import java.util.LinkedHashMap;
import java.util.Locale;

/* renamed from: cn.jzvd.JZUtils */
public class JZUtils {
    public static final String TAG = "JiaoZiVideoPlayer";

    public static String stringForTime(long timeMs) {
        if (timeMs <= 0 || timeMs >= C0015a.f22i) {
            return "00:00";
        }
        long totalSeconds = timeMs / 1000;
        int seconds = (int) (totalSeconds % 60);
        int minutes = (int) ((totalSeconds / 60) % 60);
        int hours = (int) (totalSeconds / 3600);
        Formatter mFormatter = new Formatter(new StringBuilder(), Locale.getDefault());
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", new Object[]{Integer.valueOf(hours), Integer.valueOf(minutes), Integer.valueOf(seconds)}).toString();
        }
        return mFormatter.format("%02d:%02d", new Object[]{Integer.valueOf(minutes), Integer.valueOf(seconds)}).toString();
    }

    public static boolean isWifiConnected(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (networkInfo == null || networkInfo.getType() != 1) {
            return false;
        }
        return true;
    }

    public static Activity scanForActivity(Context context) {
        if (context == null) {
            return null;
        }
        if (context instanceof Activity) {
            return (Activity) context;
        }
        if (context instanceof ContextWrapper) {
            return scanForActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

    public static AppCompatActivity getAppCompActivity(Context context) {
        if (context == null) {
            return null;
        }
        if (context instanceof AppCompatActivity) {
            return (AppCompatActivity) context;
        }
        if (context instanceof ContextThemeWrapper) {
            return getAppCompActivity(((ContextThemeWrapper) context).getBaseContext());
        }
        return null;
    }

    public static void setRequestedOrientation(Context context, int orientation) {
        if (getAppCompActivity(context) != null) {
            getAppCompActivity(context).setRequestedOrientation(orientation);
        } else {
            scanForActivity(context).setRequestedOrientation(orientation);
        }
    }

    public static Window getWindow(Context context) {
        if (getAppCompActivity(context) != null) {
            return getAppCompActivity(context).getWindow();
        }
        return scanForActivity(context).getWindow();
    }

    public static int dip2px(Context context, float dpValue) {
        return (int) ((dpValue * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static void saveProgress(Context context, Object url, long progress) {
        if (JZVideoPlayer.SAVE_PROGRESS) {
            Log.i("JiaoZiVideoPlayer", "saveProgress: " + progress);
            if (progress < 5000) {
                progress = 0;
            }
            context.getSharedPreferences("JZVD_PROGRESS", 0).edit().putLong("newVersion:" + url.toString(), progress).apply();
        }
    }

    public static long getSavedProgress(Context context, Object url) {
        if (!JZVideoPlayer.SAVE_PROGRESS) {
            return 0;
        }
        return context.getSharedPreferences("JZVD_PROGRESS", 0).getLong("newVersion:" + url.toString(), 0);
    }

    public static void clearSavedProgress(Context context, Object url) {
        if (url == null) {
            context.getSharedPreferences("JZVD_PROGRESS", 0).edit().clear().apply();
        } else {
            context.getSharedPreferences("JZVD_PROGRESS", 0).edit().putLong("newVersion:" + url.toString(), 0).apply();
        }
    }

    public static Object getCurrentFromDataSource(Object[] dataSourceObjects, int index) {
        LinkedHashMap<String, Object> map = dataSourceObjects[0];
        if (map == null || map.size() <= 0) {
            return null;
        }
        return getValueFromLinkedMap(map, index);
    }

    public static Object getValueFromLinkedMap(LinkedHashMap<String, Object> map, int index) {
        int currentIndex = 0;
        for (String key : map.keySet()) {
            if (currentIndex == index) {
                return map.get(key);
            }
            currentIndex++;
        }
        return null;
    }

    public static boolean dataSourceObjectsContainsUri(Object[] dataSourceObjects, Object object) {
        LinkedHashMap<String, Object> map = dataSourceObjects[0];
        if (map == null || object == null) {
            return false;
        }
        return map.containsValue(object);
    }

    public static String getKeyFromDataSource(Object[] dataSourceObjects, int index) {
        int currentIndex = 0;
        for (String key : dataSourceObjects[0].keySet()) {
            if (currentIndex == index) {
                return key;
            }
            currentIndex++;
        }
        return null;
    }
}
