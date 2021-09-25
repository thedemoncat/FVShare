package com.freevisiontech.fvmobile.utility;

import android.app.Activity;
import java.util.Stack;

public class BaseActivityManager {
    private static Stack<Activity> activityStack;
    private static BaseActivityManager instance;

    private BaseActivityManager() {
    }

    public static BaseActivityManager getActivityManager() {
        if (instance == null) {
            instance = new BaseActivityManager();
        }
        return instance;
    }

    public void popActivity() {
        Activity activity = (Activity) activityStack.lastElement();
        if (activity != null) {
            activity.finish();
        }
    }

    public void remove(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
        }
    }

    public void popActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            activityStack.remove(activity);
        }
    }

    public Activity currentActivity() {
        if (activityStack == null || activityStack.size() == 0) {
            return null;
        }
        return (Activity) activityStack.lastElement();
    }

    public void pushActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    public void popAllActivityExceptOne(Class<?> cls) {
        while (true) {
            Activity activity = currentActivity();
            if (activity != null && !activity.getClass().equals(cls)) {
                popActivity(activity);
            } else {
                return;
            }
        }
    }

    public void finshAllActivityExceptOne(Class<?> cls) {
        for (int i = 0; i < activityStack.size(); i++) {
            if (!((Activity) activityStack.get(i)).getClass().equals(cls)) {
                ((Activity) activityStack.get(i)).finish();
            }
        }
    }

    public void popActivityOne(Class<?> cls) {
        for (int i = 0; i < activityStack.size(); i++) {
            if (((Activity) activityStack.get(i)).getClass().equals(cls)) {
                ((Activity) activityStack.get(i)).finish();
            }
        }
    }

    public void exitApp() {
        while (true) {
            Activity activity = currentActivity();
            if (activity != null) {
                popActivity(activity);
            } else {
                return;
            }
        }
    }

    public Boolean isStart(Class<?> cls) {
        for (int i = 0; i < activityStack.size(); i++) {
            if (((Activity) activityStack.get(i)).getClass().equals(cls)) {
                return true;
            }
        }
        return false;
    }
}
