package butterknife;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.util.Property;
import android.view.View;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ButterKnife {
    static final Map<Class<?>, ViewBinder<Object>> BINDERS = new LinkedHashMap();
    static final ViewBinder<Object> NOP_VIEW_BINDER = new ViewBinder<Object>() {
        public void bind(Finder finder, Object target, Object source) {
        }

        public void unbind(Object target) {
        }
    };
    private static final String TAG = "ButterKnife";
    private static boolean debug = false;

    public interface Action<T extends View> {
        void apply(T t, int i);
    }

    public interface Setter<T extends View, V> {
        void set(T t, V v, int i);
    }

    public interface ViewBinder<T> {
        void bind(Finder finder, T t, Object obj);

        void unbind(T t);
    }

    private ButterKnife() {
        throw new AssertionError("No instances.");
    }

    public enum Finder {
        VIEW {
            /* access modifiers changed from: protected */
            public View findView(Object source, int id) {
                return ((View) source).findViewById(id);
            }

            public Context getContext(Object source) {
                return ((View) source).getContext();
            }
        },
        ACTIVITY {
            /* access modifiers changed from: protected */
            public View findView(Object source, int id) {
                return ((Activity) source).findViewById(id);
            }

            public Context getContext(Object source) {
                return (Activity) source;
            }
        },
        DIALOG {
            /* access modifiers changed from: protected */
            public View findView(Object source, int id) {
                return ((Dialog) source).findViewById(id);
            }

            public Context getContext(Object source) {
                return ((Dialog) source).getContext();
            }
        };

        /* access modifiers changed from: protected */
        public abstract View findView(Object obj, int i);

        public abstract Context getContext(Object obj);

        private static <T> T[] filterNull(T[] views) {
            int nextIndex;
            int i = 0;
            int newSize = views.length;
            for (T view : views) {
                if (view == null) {
                    newSize--;
                }
            }
            if (newSize == views.length) {
                return views;
            }
            T[] newViews = (Object[]) new Object[newSize];
            int length = views.length;
            int nextIndex2 = 0;
            while (i < length) {
                T view2 = views[i];
                if (view2 != null) {
                    nextIndex = nextIndex2 + 1;
                    newViews[nextIndex2] = view2;
                } else {
                    nextIndex = nextIndex2;
                }
                i++;
                nextIndex2 = nextIndex;
            }
            return newViews;
        }

        public static <T> T[] arrayOf(T... views) {
            return filterNull(views);
        }

        public static <T> List<T> listOf(T... views) {
            return new ImmutableList(filterNull(views));
        }

        public <T> T findRequiredView(Object source, int id, String who) {
            T view = findOptionalView(source, id, who);
            if (view != null) {
                return view;
            }
            throw new IllegalStateException("Required view '" + getContext(source).getResources().getResourceEntryName(id) + "' with ID " + id + " for " + who + " was not found. If this view is optional add '@Nullable' annotation.");
        }

        public <T> T findOptionalView(Object source, int id, String who) {
            return castView(findView(source, id), id, who);
        }

        public <T> T castView(View view, int id, String who) {
            return view;
        }

        public <T> T castParam(Object value, String from, int fromPosition, String to, int toPosition) {
            return value;
        }
    }

    public static void setDebug(boolean debug2) {
        debug = debug2;
    }

    public static void bind(Activity target) {
        bind(target, target, Finder.ACTIVITY);
    }

    public static void bind(View target) {
        bind(target, target, Finder.VIEW);
    }

    public static void bind(Dialog target) {
        bind(target, target, Finder.DIALOG);
    }

    public static void bind(Object target, Activity source) {
        bind(target, source, Finder.ACTIVITY);
    }

    public static void bind(Object target, View source) {
        bind(target, source, Finder.VIEW);
    }

    public static void bind(Object target, Dialog source) {
        bind(target, source, Finder.DIALOG);
    }

    public static void unbind(Object target) {
        Class<?> targetClass = target.getClass();
        try {
            if (debug) {
                Log.d(TAG, "Looking up view binder for " + targetClass.getName());
            }
            ViewBinder<Object> viewBinder = findViewBinderForClass(targetClass);
            if (viewBinder != null) {
                viewBinder.unbind(target);
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to unbind views for " + targetClass.getName(), e);
        }
    }

    static void bind(Object target, Object source, Finder finder) {
        Class<?> targetClass = target.getClass();
        try {
            if (debug) {
                Log.d(TAG, "Looking up view binder for " + targetClass.getName());
            }
            ViewBinder<Object> viewBinder = findViewBinderForClass(targetClass);
            if (viewBinder != null) {
                viewBinder.bind(finder, target, source);
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to bind views for " + targetClass.getName(), e);
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v17, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: butterknife.ButterKnife$ViewBinder<java.lang.Object>} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static butterknife.ButterKnife.ViewBinder<java.lang.Object> findViewBinderForClass(java.lang.Class<?> r8) throws java.lang.IllegalAccessException, java.lang.InstantiationException {
        /*
            java.util.Map<java.lang.Class<?>, butterknife.ButterKnife$ViewBinder<java.lang.Object>> r5 = BINDERS
            java.lang.Object r3 = r5.get(r8)
            butterknife.ButterKnife$ViewBinder r3 = (butterknife.ButterKnife.ViewBinder) r3
            if (r3 == 0) goto L_0x0019
            boolean r5 = debug
            if (r5 == 0) goto L_0x0017
            java.lang.String r5 = "ButterKnife"
            java.lang.String r6 = "HIT: Cached in view binder map."
            android.util.Log.d(r5, r6)
        L_0x0017:
            r5 = r3
        L_0x0018:
            return r5
        L_0x0019:
            java.lang.String r1 = r8.getName()
            java.lang.String r5 = "android."
            boolean r5 = r1.startsWith(r5)
            if (r5 != 0) goto L_0x002f
            java.lang.String r5 = "java."
            boolean r5 = r1.startsWith(r5)
            if (r5 == 0) goto L_0x003f
        L_0x002f:
            boolean r5 = debug
            if (r5 == 0) goto L_0x003c
            java.lang.String r5 = "ButterKnife"
            java.lang.String r6 = "MISS: Reached framework class. Abandoning search."
            android.util.Log.d(r5, r6)
        L_0x003c:
            butterknife.ButterKnife$ViewBinder<java.lang.Object> r5 = NOP_VIEW_BINDER
            goto L_0x0018
        L_0x003f:
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ ClassNotFoundException -> 0x0073 }
            r5.<init>()     // Catch:{ ClassNotFoundException -> 0x0073 }
            java.lang.StringBuilder r5 = r5.append(r1)     // Catch:{ ClassNotFoundException -> 0x0073 }
            java.lang.String r6 = "$$ViewBinder"
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ ClassNotFoundException -> 0x0073 }
            java.lang.String r5 = r5.toString()     // Catch:{ ClassNotFoundException -> 0x0073 }
            java.lang.Class r4 = java.lang.Class.forName(r5)     // Catch:{ ClassNotFoundException -> 0x0073 }
            java.lang.Object r5 = r4.newInstance()     // Catch:{ ClassNotFoundException -> 0x0073 }
            r0 = r5
            butterknife.ButterKnife$ViewBinder r0 = (butterknife.ButterKnife.ViewBinder) r0     // Catch:{ ClassNotFoundException -> 0x0073 }
            r3 = r0
            boolean r5 = debug     // Catch:{ ClassNotFoundException -> 0x0073 }
            if (r5 == 0) goto L_0x006c
            java.lang.String r5 = "ButterKnife"
            java.lang.String r6 = "HIT: Loaded view binder class."
            android.util.Log.d(r5, r6)     // Catch:{ ClassNotFoundException -> 0x0073 }
        L_0x006c:
            java.util.Map<java.lang.Class<?>, butterknife.ButterKnife$ViewBinder<java.lang.Object>> r5 = BINDERS
            r5.put(r8, r3)
            r5 = r3
            goto L_0x0018
        L_0x0073:
            r2 = move-exception
            boolean r5 = debug
            if (r5 == 0) goto L_0x009a
            java.lang.String r5 = "ButterKnife"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "Not found. Trying superclass "
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.Class r7 = r8.getSuperclass()
            java.lang.String r7 = r7.getName()
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.String r6 = r6.toString()
            android.util.Log.d(r5, r6)
        L_0x009a:
            java.lang.Class r5 = r8.getSuperclass()
            butterknife.ButterKnife$ViewBinder r3 = findViewBinderForClass(r5)
            goto L_0x006c
        */
        throw new UnsupportedOperationException("Method not decompiled: butterknife.ButterKnife.findViewBinderForClass(java.lang.Class):butterknife.ButterKnife$ViewBinder");
    }

    public static <T extends View> void apply(List<T> list, Action<? super T> action) {
        int count = list.size();
        for (int i = 0; i < count; i++) {
            action.apply((View) list.get(i), i);
        }
    }

    public static <T extends View, V> void apply(List<T> list, Setter<? super T, V> setter, V value) {
        int count = list.size();
        for (int i = 0; i < count; i++) {
            setter.set((View) list.get(i), value, i);
        }
    }

    @TargetApi(14)
    public static <T extends View, V> void apply(List<T> list, Property<? super T, V> setter, V value) {
        int count = list.size();
        for (int i = 0; i < count; i++) {
            setter.set(list.get(i), value);
        }
    }

    public static <T extends View> T findById(View view, int id) {
        return view.findViewById(id);
    }

    public static <T extends View> T findById(Activity activity, int id) {
        return activity.findViewById(id);
    }

    public static <T extends View> T findById(Dialog dialog, int id) {
        return dialog.findViewById(id);
    }
}
