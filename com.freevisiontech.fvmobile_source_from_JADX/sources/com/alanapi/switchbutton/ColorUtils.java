package com.alanapi.switchbutton;

import android.content.res.ColorStateList;

public class ColorUtils {
    private static final int CHECKED_ATTR = 16842912;
    private static final int ENABLE_ATTR = 16842910;
    private static final int PRESSED_ATTR = 16842919;

    public static ColorStateList generateThumbColorWithTintColor(int tintColor) {
        return new ColorStateList(new int[][]{new int[]{-16842910, CHECKED_ATTR}, new int[]{-16842910}, new int[]{PRESSED_ATTR, -16842912}, new int[]{PRESSED_ATTR, CHECKED_ATTR}, new int[]{CHECKED_ATTR}, new int[]{-16842912}}, new int[]{tintColor - -1442840576, -4539718, tintColor - -1728053248, tintColor - -1728053248, -16777216 | tintColor, -1118482});
    }

    public static ColorStateList generateBackColorWithTintColor(int tintColor) {
        return new ColorStateList(new int[][]{new int[]{-16842910, CHECKED_ATTR}, new int[]{-16842910}, new int[]{CHECKED_ATTR, PRESSED_ATTR}, new int[]{-16842912, PRESSED_ATTR}, new int[]{CHECKED_ATTR}, new int[]{-16842912}}, new int[]{tintColor - -520093696, 268435456, tintColor - -805306368, 536870912, tintColor - -805306368, 536870912});
    }
}
