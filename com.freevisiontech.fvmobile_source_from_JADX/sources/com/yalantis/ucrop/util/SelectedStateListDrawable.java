package com.yalantis.ucrop.util;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

public class SelectedStateListDrawable extends StateListDrawable {
    private int mSelectionColor;

    public SelectedStateListDrawable(Drawable drawable, int selectionColor) {
        this.mSelectionColor = selectionColor;
        addState(new int[]{16842913}, drawable);
        addState(new int[0], drawable);
    }

    /* access modifiers changed from: protected */
    public boolean onStateChange(int[] states) {
        boolean isStatePressedInArray = false;
        for (int state : states) {
            if (state == 16842913) {
                isStatePressedInArray = true;
            }
        }
        if (isStatePressedInArray) {
            super.setColorFilter(this.mSelectionColor, PorterDuff.Mode.SRC_ATOP);
        } else {
            super.clearColorFilter();
        }
        return super.onStateChange(states);
    }

    public boolean isStateful() {
        return true;
    }
}
