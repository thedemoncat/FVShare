package com.yalantis.ucrop.util;

import android.annotation.TargetApi;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.os.Build;
import android.util.Log;
import javax.microedition.khronos.egl.EGL10;

public class EglUtils {
    private static final String TAG = "EglUtils";

    private EglUtils() {
    }

    public static int getMaxTextureSize() {
        try {
            if (Build.VERSION.SDK_INT >= 17) {
                return getMaxTextureEgl14();
            }
            return getMaxTextureEgl10();
        } catch (Exception e) {
            Log.d(TAG, "getMaxTextureSize: ", e);
            return 0;
        }
    }

    @TargetApi(17)
    private static int getMaxTextureEgl14() {
        EGLDisplay dpy = EGL14.eglGetDisplay(0);
        int[] vers = new int[2];
        EGL14.eglInitialize(dpy, vers, 0, vers, 1);
        EGLConfig[] configs = new EGLConfig[1];
        int[] numConfig = new int[1];
        EGL14.eglChooseConfig(dpy, new int[]{12351, 12430, 12329, 0, 12352, 4, 12339, 1, 12344}, 0, configs, 0, 1, numConfig, 0);
        if (numConfig[0] == 0) {
            return 0;
        }
        EGLConfig config = configs[0];
        EGLSurface surf = EGL14.eglCreatePbufferSurface(dpy, config, new int[]{12375, 64, 12374, 64, 12344}, 0);
        EGLContext ctx = EGL14.eglCreateContext(dpy, config, EGL14.EGL_NO_CONTEXT, new int[]{12440, 2, 12344}, 0);
        EGL14.eglMakeCurrent(dpy, surf, surf, ctx);
        int[] maxSize = new int[1];
        GLES20.glGetIntegerv(3379, maxSize, 0);
        EGL14.eglMakeCurrent(dpy, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT);
        EGL14.eglDestroySurface(dpy, surf);
        EGL14.eglDestroyContext(dpy, ctx);
        EGL14.eglTerminate(dpy);
        return maxSize[0];
    }

    @TargetApi(14)
    private static int getMaxTextureEgl10() {
        EGL10 egl = (EGL10) javax.microedition.khronos.egl.EGLContext.getEGL();
        javax.microedition.khronos.egl.EGLDisplay dpy = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        egl.eglInitialize(dpy, new int[2]);
        javax.microedition.khronos.egl.EGLConfig[] configs = new javax.microedition.khronos.egl.EGLConfig[1];
        int[] numConfig = new int[1];
        egl.eglChooseConfig(dpy, new int[]{12351, 12430, 12329, 0, 12339, 1, 12344}, configs, 1, numConfig);
        if (numConfig[0] == 0) {
            return 0;
        }
        javax.microedition.khronos.egl.EGLConfig config = configs[0];
        javax.microedition.khronos.egl.EGLSurface surf = egl.eglCreatePbufferSurface(dpy, config, new int[]{12375, 64, 12374, 64, 12344});
        javax.microedition.khronos.egl.EGLContext ctx = egl.eglCreateContext(dpy, config, EGL10.EGL_NO_CONTEXT, new int[]{12440, 1, 12344});
        egl.eglMakeCurrent(dpy, surf, surf, ctx);
        int[] maxSize = new int[1];
        GLES10.glGetIntegerv(3379, maxSize, 0);
        egl.eglMakeCurrent(dpy, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
        egl.eglDestroySurface(dpy, surf);
        egl.eglDestroyContext(dpy, ctx);
        egl.eglTerminate(dpy);
        return maxSize[0];
    }
}
