package org.xutils.view;

import android.app.Activity;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import org.xutils.C2090x;
import org.xutils.ViewInjector;
import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

public final class ViewInjectorImpl implements ViewInjector {
    private static final HashSet<Class<?>> IGNORED = new HashSet<>();
    private static volatile ViewInjectorImpl instance;
    private static final Object lock = new Object();

    static {
        IGNORED.add(Object.class);
        IGNORED.add(Activity.class);
        IGNORED.add(Fragment.class);
        try {
            IGNORED.add(Class.forName("android.support.v4.app.Fragment"));
            IGNORED.add(Class.forName("android.support.v4.app.FragmentActivity"));
        } catch (Throwable th) {
        }
    }

    private ViewInjectorImpl() {
    }

    public static void registerInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new ViewInjectorImpl();
                }
            }
        }
        C2090x.Ext.setViewInjector(instance);
    }

    public void inject(View view) {
        injectObject(view, view.getClass(), new ViewFinder(view));
    }

    public void inject(Activity activity) {
        int viewId;
        Class<?> handlerType = activity.getClass();
        try {
            ContentView contentView = findContentView(handlerType);
            if (contentView != null && (viewId = contentView.value()) > 0) {
                handlerType.getMethod("setContentView", new Class[]{Integer.TYPE}).invoke(activity, new Object[]{Integer.valueOf(viewId)});
            }
        } catch (Throwable ex) {
            LogUtil.m1565e(ex.getMessage(), ex);
        }
        injectObject(activity, handlerType, new ViewFinder(activity));
    }

    public void inject(Object handler, View view) {
        injectObject(handler, handler.getClass(), new ViewFinder(view));
    }

    public View inject(Object fragment, LayoutInflater inflater, ViewGroup container) {
        int viewId;
        View view = null;
        Class<?> handlerType = fragment.getClass();
        try {
            ContentView contentView = findContentView(handlerType);
            if (contentView != null && (viewId = contentView.value()) > 0) {
                view = inflater.inflate(viewId, container, false);
            }
        } catch (Throwable ex) {
            LogUtil.m1565e(ex.getMessage(), ex);
        }
        injectObject(fragment, handlerType, new ViewFinder(view));
        return view;
    }

    private static ContentView findContentView(Class<?> thisCls) {
        if (thisCls == null || IGNORED.contains(thisCls)) {
            return null;
        }
        ContentView contentView = (ContentView) thisCls.getAnnotation(ContentView.class);
        if (contentView == null) {
            return findContentView(thisCls.getSuperclass());
        }
        return contentView;
    }

    private static void injectObject(Object handler, Class<?> handlerType, ViewFinder finder) {
        Event event;
        ViewInject viewInject;
        if (handlerType != null && !IGNORED.contains(handlerType)) {
            injectObject(handler, handlerType.getSuperclass(), finder);
            Field[] fields = handlerType.getDeclaredFields();
            if (fields != null && fields.length > 0) {
                int length = fields.length;
                for (int i = 0; i < length; i++) {
                    Field field = fields[i];
                    Class<?> fieldType = field.getType();
                    if (!Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers()) && !fieldType.isPrimitive() && !fieldType.isArray() && (viewInject = (ViewInject) field.getAnnotation(ViewInject.class)) != null) {
                        try {
                            View view = finder.findViewById(viewInject.value(), viewInject.parentId());
                            if (view != null) {
                                field.setAccessible(true);
                                field.set(handler, view);
                            } else {
                                throw new RuntimeException("Invalid @ViewInject for " + handlerType.getSimpleName() + "." + field.getName());
                            }
                        } catch (Throwable ex) {
                            LogUtil.m1565e(ex.getMessage(), ex);
                        }
                    }
                }
            }
            Method[] methods = handlerType.getDeclaredMethods();
            if (methods != null && methods.length > 0) {
                int length2 = methods.length;
                int i2 = 0;
                while (true) {
                    int i3 = i2;
                    if (i3 < length2) {
                        Method method = methods[i3];
                        if (!Modifier.isStatic(method.getModifiers()) && Modifier.isPrivate(method.getModifiers()) && (event = (Event) method.getAnnotation(Event.class)) != null) {
                            try {
                                int[] values = event.value();
                                int[] parentIds = event.parentId();
                                int parentIdsLen = parentIds == null ? 0 : parentIds.length;
                                int i4 = 0;
                                while (i4 < values.length) {
                                    int value = values[i4];
                                    if (value > 0) {
                                        ViewInfo info = new ViewInfo();
                                        info.value = value;
                                        info.parentId = parentIdsLen > i4 ? parentIds[i4] : 0;
                                        method.setAccessible(true);
                                        EventListenerManager.addEventMethod(finder, info, event, handler, method);
                                    }
                                    i4++;
                                }
                            } catch (Throwable ex2) {
                                LogUtil.m1565e(ex2.getMessage(), ex2);
                            }
                        }
                        i2 = i3 + 1;
                    } else {
                        return;
                    }
                }
            }
        }
    }
}
