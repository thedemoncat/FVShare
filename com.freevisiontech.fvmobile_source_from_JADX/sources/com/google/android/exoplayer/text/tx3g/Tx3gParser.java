package com.google.android.exoplayer.text.tx3g;

import com.google.android.exoplayer.text.Cue;
import com.google.android.exoplayer.text.Subtitle;
import com.google.android.exoplayer.text.SubtitleParser;
import com.google.android.exoplayer.util.MimeTypes;
import com.google.android.exoplayer.util.ParsableByteArray;

public final class Tx3gParser implements SubtitleParser {
    private final ParsableByteArray parsableByteArray = new ParsableByteArray();

    public boolean canParse(String mimeType) {
        return MimeTypes.APPLICATION_TX3G.equals(mimeType);
    }

    public Subtitle parse(byte[] bytes, int offset, int length) {
        this.parsableByteArray.reset(bytes, length);
        int textLength = this.parsableByteArray.readUnsignedShort();
        if (textLength == 0) {
            return Tx3gSubtitle.EMPTY;
        }
        return new Tx3gSubtitle(new Cue(this.parsableByteArray.readString(textLength)));
    }
}
