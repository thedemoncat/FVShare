package org.xutils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public interface ViewInjector {
    View inject(Object obj, LayoutInflater layoutInflater, ViewGroup viewGroup);

    void inject(Activity activity);

    void inject(View view);

    void inject(Object obj, View view);
}
