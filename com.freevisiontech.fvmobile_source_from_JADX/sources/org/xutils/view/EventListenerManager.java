package org.xutils.view;

import android.text.TextUtils;
import android.view.View;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import org.xutils.common.util.DoubleKeyValueMap;
import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.Event;

final class EventListenerManager {
    /* access modifiers changed from: private */
    public static final HashSet<String> AVOID_QUICK_EVENT_SET = new HashSet<>(2);
    private static final long QUICK_EVENT_TIME_SPAN = 300;
    private static final DoubleKeyValueMap<ViewInfo, Class<?>, Object> listenerCache = new DoubleKeyValueMap<>();

    static {
        AVOID_QUICK_EVENT_SET.add("onClick");
        AVOID_QUICK_EVENT_SET.add("onItemClick");
    }

    private EventListenerManager() {
    }

    public static void addEventMethod(ViewFinder finder, ViewInfo info, Event event, Object handler, Method method) {
        try {
            View view = finder.findViewByInfo(info);
            if (view != null) {
                Class<?> listenerType = event.type();
                String listenerSetter = event.setter();
                if (TextUtils.isEmpty(listenerSetter)) {
                    listenerSetter = "set" + listenerType.getSimpleName();
                }
                String methodName = event.method();
                boolean addNewMethod = false;
                Object listener = listenerCache.get(info, listenerType);
                if (listener != null) {
                    DynamicHandler dynamicHandler = (DynamicHandler) Proxy.getInvocationHandler(listener);
                    addNewMethod = handler.equals(dynamicHandler.getHandler());
                    if (addNewMethod) {
                        dynamicHandler.addMethod(methodName, method);
                    }
                }
                if (!addNewMethod) {
                    DynamicHandler dynamicHandler2 = new DynamicHandler(handler);
                    dynamicHandler2.addMethod(methodName, method);
                    listener = Proxy.newProxyInstance(listenerType.getClassLoader(), new Class[]{listenerType}, dynamicHandler2);
                    listenerCache.put(info, listenerType, listener);
                }
                view.getClass().getMethod(listenerSetter, new Class[]{listenerType}).invoke(view, new Object[]{listener});
            }
        } catch (Throwable ex) {
            LogUtil.m1565e(ex.getMessage(), ex);
        }
    }

    public static class DynamicHandler implements InvocationHandler {
        private static long lastClickTime = 0;
        private WeakReference<Object> handlerRef;
        private final HashMap<String, Method> methodMap = new HashMap<>(1);

        public DynamicHandler(Object handler) {
            this.handlerRef = new WeakReference<>(handler);
        }

        public void addMethod(String name, Method method) {
            this.methodMap.put(name, method);
        }

        public Object getHandler() {
            return this.handlerRef.get();
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object handler = this.handlerRef.get();
            if (handler != null) {
                String eventMethod = method.getName();
                if ("toString".equals(eventMethod)) {
                    return DynamicHandler.class.getSimpleName();
                }
                Method method2 = this.methodMap.get(eventMethod);
                if (method2 == null && this.methodMap.size() == 1) {
                    Iterator<Map.Entry<String, Method>> it = this.methodMap.entrySet().iterator();
                    if (it.hasNext()) {
                        Map.Entry<String, Method> entry = it.next();
                        if (TextUtils.isEmpty(entry.getKey())) {
                            method2 = entry.getValue();
                        }
                    }
                }
                if (method2 != null) {
                    if (EventListenerManager.AVOID_QUICK_EVENT_SET.contains(eventMethod)) {
                        long timeSpan = System.currentTimeMillis() - lastClickTime;
                        if (timeSpan < EventListenerManager.QUICK_EVENT_TIME_SPAN) {
                            LogUtil.m1562d("onClick cancelled: " + timeSpan);
                            return null;
                        }
                        lastClickTime = System.currentTimeMillis();
                    }
                    try {
                        return method2.invoke(handler, args);
                    } catch (Throwable ex) {
                        throw new RuntimeException("invoke method error:" + handler.getClass().getName() + "#" + method2.getName(), ex);
                    }
                } else {
                    LogUtil.m1570w("method not impl: " + eventMethod + "(" + handler.getClass().getSimpleName() + ")");
                }
            }
            return null;
        }
    }
}
