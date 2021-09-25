package android.support.p003v7.app;

import android.app.UiModeManager;
import android.content.Context;
import android.support.annotation.RequiresApi;
import android.support.p003v7.app.AppCompatDelegateImplV14;
import android.view.ActionMode;
import android.view.Window;

@RequiresApi(23)
/* renamed from: android.support.v7.app.AppCompatDelegateImplV23 */
class AppCompatDelegateImplV23 extends AppCompatDelegateImplV14 {
    private final UiModeManager mUiModeManager;

    AppCompatDelegateImplV23(Context context, Window window, AppCompatCallback callback) {
        super(context, window, callback);
        this.mUiModeManager = (UiModeManager) context.getSystemService("uimode");
    }

    /* access modifiers changed from: package-private */
    public Window.Callback wrapWindowCallback(Window.Callback callback) {
        return new AppCompatWindowCallbackV23(callback);
    }

    /* access modifiers changed from: package-private */
    public int mapNightMode(int mode) {
        if (mode == 0 && this.mUiModeManager.getNightMode() == 0) {
            return -1;
        }
        return super.mapNightMode(mode);
    }

    /* renamed from: android.support.v7.app.AppCompatDelegateImplV23$AppCompatWindowCallbackV23 */
    class AppCompatWindowCallbackV23 extends AppCompatDelegateImplV14.AppCompatWindowCallbackV14 {
        AppCompatWindowCallbackV23(Window.Callback callback) {
            super(callback);
        }

        public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int type) {
            if (AppCompatDelegateImplV23.this.isHandleNativeActionModesEnabled()) {
                switch (type) {
                    case 0:
                        return startAsSupportActionMode(callback);
                }
            }
            return super.onWindowStartingActionMode(callback, type);
        }

        public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
            return null;
        }
    }
}
