package com.google.android.exoplayer.extractor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class GaplessInfo {
    private static final String GAPLESS_COMMENT_ID = "iTunSMPB";
    private static final Pattern GAPLESS_COMMENT_PATTERN = Pattern.compile("^ [0-9a-fA-F]{8} ([0-9a-fA-F]{8}) ([0-9a-fA-F]{8})");
    public final int encoderDelay;
    public final int encoderPadding;

    public static GaplessInfo createFromComment(String name, String data) {
        if (!GAPLESS_COMMENT_ID.equals(name)) {
            return null;
        }
        Matcher matcher = GAPLESS_COMMENT_PATTERN.matcher(data);
        if (!matcher.find()) {
            return null;
        }
        try {
            int encoderDelay2 = Integer.parseInt(matcher.group(1), 16);
            int encoderPadding2 = Integer.parseInt(matcher.group(2), 16);
            if (encoderDelay2 == 0 && encoderPadding2 == 0) {
                return null;
            }
            return new GaplessInfo(encoderDelay2, encoderPadding2);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static GaplessInfo createFromXingHeaderValue(int value) {
        int encoderDelay2 = value >> 12;
        int encoderPadding2 = value & 4095;
        if (encoderDelay2 == 0 && encoderPadding2 == 0) {
            return null;
        }
        return new GaplessInfo(encoderDelay2, encoderPadding2);
    }

    private GaplessInfo(int encoderDelay2, int encoderPadding2) {
        this.encoderDelay = encoderDelay2;
        this.encoderPadding = encoderPadding2;
    }
}
