package com.bumptech.glide.manager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.FragmentManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.p001v4.app.Fragment;
import android.support.p001v4.app.FragmentActivity;
import android.util.Log;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.util.Util;
import java.util.HashMap;
import java.util.Map;

public class RequestManagerRetriever implements Handler.Callback {
    static final String FRAGMENT_TAG = "com.bumptech.glide.manager";
    private static final int ID_REMOVE_FRAGMENT_MANAGER = 1;
    private static final int ID_REMOVE_SUPPORT_FRAGMENT_MANAGER = 2;
    private static final RequestManagerRetriever INSTANCE = new RequestManagerRetriever();
    private static final String TAG = "RMRetriever";
    private volatile RequestManager applicationManager;
    private final Handler handler = new Handler(Looper.getMainLooper(), this);
    final Map<FragmentManager, RequestManagerFragment> pendingRequestManagerFragments = new HashMap();
    final Map<android.support.p001v4.app.FragmentManager, SupportRequestManagerFragment> pendingSupportRequestManagerFragments = new HashMap();

    public static RequestManagerRetriever get() {
        return INSTANCE;
    }

    RequestManagerRetriever() {
    }

    private RequestManager getApplicationManager(Context context) {
        if (this.applicationManager == null) {
            synchronized (this) {
                if (this.applicationManager == null) {
                    this.applicationManager = new RequestManager(context.getApplicationContext(), new ApplicationLifecycle(), new EmptyRequestManagerTreeNode());
                }
            }
        }
        return this.applicationManager;
    }

    public RequestManager get(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("You cannot start a load on a null Context");
        }
        if (Util.isOnMainThread() && !(context instanceof Application)) {
            if (context instanceof FragmentActivity) {
                return get((FragmentActivity) context);
            }
            if (context instanceof Activity) {
                return get((Activity) context);
            }
            if (context instanceof ContextWrapper) {
                return get(((ContextWrapper) context).getBaseContext());
            }
        }
        return getApplicationManager(context);
    }

    public RequestManager get(FragmentActivity activity) {
        if (Util.isOnBackgroundThread()) {
            return get(activity.getApplicationContext());
        }
        assertNotDestroyed(activity);
        return supportFragmentGet(activity, activity.getSupportFragmentManager());
    }

    public RequestManager get(Fragment fragment) {
        if (fragment.getActivity() == null) {
            throw new IllegalArgumentException("You cannot start a load on a fragment before it is attached");
        } else if (Util.isOnBackgroundThread()) {
            return get(fragment.getActivity().getApplicationContext());
        } else {
            return supportFragmentGet(fragment.getActivity(), fragment.getChildFragmentManager());
        }
    }

    @TargetApi(11)
    public RequestManager get(Activity activity) {
        if (Util.isOnBackgroundThread() || Build.VERSION.SDK_INT < 11) {
            return get(activity.getApplicationContext());
        }
        assertNotDestroyed(activity);
        return fragmentGet(activity, activity.getFragmentManager());
    }

    @TargetApi(17)
    private static void assertNotDestroyed(Activity activity) {
        if (Build.VERSION.SDK_INT >= 17 && activity.isDestroyed()) {
            throw new IllegalArgumentException("You cannot start a load for a destroyed activity");
        }
    }

    @TargetApi(17)
    public RequestManager get(android.app.Fragment fragment) {
        if (fragment.getActivity() == null) {
            throw new IllegalArgumentException("You cannot start a load on a fragment before it is attached");
        } else if (Util.isOnBackgroundThread() || Build.VERSION.SDK_INT < 17) {
            return get(fragment.getActivity().getApplicationContext());
        } else {
            return fragmentGet(fragment.getActivity(), fragment.getChildFragmentManager());
        }
    }

    /* access modifiers changed from: package-private */
    @TargetApi(17)
    public RequestManagerFragment getRequestManagerFragment(FragmentManager fm) {
        RequestManagerFragment current = (RequestManagerFragment) fm.findFragmentByTag(FRAGMENT_TAG);
        if (current != null) {
            return current;
        }
        RequestManagerFragment current2 = this.pendingRequestManagerFragments.get(fm);
        if (current2 != null) {
            return current2;
        }
        RequestManagerFragment current3 = new RequestManagerFragment();
        this.pendingRequestManagerFragments.put(fm, current3);
        fm.beginTransaction().add(current3, FRAGMENT_TAG).commitAllowingStateLoss();
        this.handler.obtainMessage(1, fm).sendToTarget();
        return current3;
    }

    /* access modifiers changed from: package-private */
    @TargetApi(11)
    public RequestManager fragmentGet(Context context, FragmentManager fm) {
        RequestManagerFragment current = getRequestManagerFragment(fm);
        RequestManager requestManager = current.getRequestManager();
        if (requestManager != null) {
            return requestManager;
        }
        RequestManager requestManager2 = new RequestManager(context, current.getLifecycle(), current.getRequestManagerTreeNode());
        current.setRequestManager(requestManager2);
        return requestManager2;
    }

    /* access modifiers changed from: package-private */
    public SupportRequestManagerFragment getSupportRequestManagerFragment(android.support.p001v4.app.FragmentManager fm) {
        SupportRequestManagerFragment current = (SupportRequestManagerFragment) fm.findFragmentByTag(FRAGMENT_TAG);
        if (current != null) {
            return current;
        }
        SupportRequestManagerFragment current2 = this.pendingSupportRequestManagerFragments.get(fm);
        if (current2 != null) {
            return current2;
        }
        SupportRequestManagerFragment current3 = new SupportRequestManagerFragment();
        this.pendingSupportRequestManagerFragments.put(fm, current3);
        fm.beginTransaction().add((Fragment) current3, FRAGMENT_TAG).commitAllowingStateLoss();
        this.handler.obtainMessage(2, fm).sendToTarget();
        return current3;
    }

    /* access modifiers changed from: package-private */
    public RequestManager supportFragmentGet(Context context, android.support.p001v4.app.FragmentManager fm) {
        SupportRequestManagerFragment current = getSupportRequestManagerFragment(fm);
        RequestManager requestManager = current.getRequestManager();
        if (requestManager != null) {
            return requestManager;
        }
        RequestManager requestManager2 = new RequestManager(context, current.getLifecycle(), current.getRequestManagerTreeNode());
        current.setRequestManager(requestManager2);
        return requestManager2;
    }

    public boolean handleMessage(Message message) {
        boolean handled = true;
        Object removed = null;
        Object key = null;
        switch (message.what) {
            case 1:
                Object fm = (FragmentManager) message.obj;
                key = fm;
                removed = this.pendingRequestManagerFragments.remove(fm);
                break;
            case 2:
                Object supportFm = (android.support.p001v4.app.FragmentManager) message.obj;
                key = supportFm;
                removed = this.pendingSupportRequestManagerFragments.remove(supportFm);
                break;
            default:
                handled = false;
                break;
        }
        if (handled && removed == null && Log.isLoggable(TAG, 5)) {
            Log.w(TAG, "Failed to remove expected request manager fragment, manager: " + key);
        }
        return handled;
    }
}
