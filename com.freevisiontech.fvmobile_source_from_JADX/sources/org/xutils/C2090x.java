package org.xutils;

import android.app.Application;
import android.content.Context;
import org.xutils.DbManager;
import org.xutils.common.TaskController;
import org.xutils.common.task.TaskControllerImpl;
import org.xutils.http.HttpManagerImpl;
import org.xutils.image.ImageManagerImpl;
import org.xutils.p018db.DbManagerImpl;
import org.xutils.view.ViewInjectorImpl;

/* renamed from: org.xutils.x */
public final class C2090x {
    private C2090x() {
    }

    public static boolean isDebug() {
        return Ext.debug;
    }

    public static Application app() {
        if (Ext.app == null) {
            try {
                Application unused = Ext.app = new MockApplication((Context) Class.forName("com.android.layoutlib.bridge.impl.RenderAction").getDeclaredMethod("getCurrentContext", new Class[0]).invoke((Object) null, new Object[0]));
            } catch (Throwable th) {
                throw new RuntimeException("please invoke x.Ext.init(app) on Application#onCreate() and register your Application in manifest.");
            }
        }
        return Ext.app;
    }

    public static TaskController task() {
        return Ext.taskController;
    }

    public static HttpManager http() {
        if (Ext.httpManager == null) {
            HttpManagerImpl.registerInstance();
        }
        return Ext.httpManager;
    }

    public static ImageManager image() {
        if (Ext.imageManager == null) {
            ImageManagerImpl.registerInstance();
        }
        return Ext.imageManager;
    }

    public static ViewInjector view() {
        if (Ext.viewInjector == null) {
            ViewInjectorImpl.registerInstance();
        }
        return Ext.viewInjector;
    }

    public static DbManager getDb(DbManager.DaoConfig daoConfig) {
        return DbManagerImpl.getInstance(daoConfig);
    }

    /* renamed from: org.xutils.x$Ext */
    public static class Ext {
        /* access modifiers changed from: private */
        public static Application app;
        /* access modifiers changed from: private */
        public static boolean debug;
        /* access modifiers changed from: private */
        public static HttpManager httpManager;
        /* access modifiers changed from: private */
        public static ImageManager imageManager;
        /* access modifiers changed from: private */
        public static TaskController taskController;
        /* access modifiers changed from: private */
        public static ViewInjector viewInjector;

        private Ext() {
        }

        public static void init(Application app2) {
            TaskControllerImpl.registerInstance();
            if (app == null) {
                app = app2;
            }
        }

        public static void setDebug(boolean debug2) {
            debug = debug2;
        }

        public static void setTaskController(TaskController taskController2) {
            if (taskController == null) {
                taskController = taskController2;
            }
        }

        public static void setHttpManager(HttpManager httpManager2) {
            httpManager = httpManager2;
        }

        public static void setImageManager(ImageManager imageManager2) {
            imageManager = imageManager2;
        }

        public static void setViewInjector(ViewInjector viewInjector2) {
            viewInjector = viewInjector2;
        }
    }

    /* renamed from: org.xutils.x$MockApplication */
    private static class MockApplication extends Application {
        public MockApplication(Context baseContext) {
            attachBaseContext(baseContext);
        }
    }
}
