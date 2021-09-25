package butterknife.internal;

import android.view.View;

public abstract class DebouncingOnClickListener implements View.OnClickListener {
    private static final Runnable ENABLE_AGAIN = new Runnable() {
        public void run() {
            boolean unused = DebouncingOnClickListener.enabled = true;
        }
    };
    /* access modifiers changed from: private */
    public static boolean enabled = true;

    public abstract void doClick(View view);

    public final void onClick(View v) {
        if (enabled) {
            enabled = false;
            v.post(ENABLE_AGAIN);
            doClick(v);
        }
    }
}
