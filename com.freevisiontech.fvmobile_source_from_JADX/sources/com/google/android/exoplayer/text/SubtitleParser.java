package com.google.android.exoplayer.text;

import com.google.android.exoplayer.ParserException;

public interface SubtitleParser {
    boolean canParse(String str);

    Subtitle parse(byte[] bArr, int i, int i2) throws ParserException;
}
