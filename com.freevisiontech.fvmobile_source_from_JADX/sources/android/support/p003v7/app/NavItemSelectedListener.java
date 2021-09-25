package android.support.p003v7.app;

import android.support.p003v7.app.ActionBar;
import android.view.View;
import android.widget.AdapterView;

/* renamed from: android.support.v7.app.NavItemSelectedListener */
class NavItemSelectedListener implements AdapterView.OnItemSelectedListener {
    private final ActionBar.OnNavigationListener mListener;

    public NavItemSelectedListener(ActionBar.OnNavigationListener listener) {
        this.mListener = listener;
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        if (this.mListener != null) {
            this.mListener.onNavigationItemSelected(position, id);
        }
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
