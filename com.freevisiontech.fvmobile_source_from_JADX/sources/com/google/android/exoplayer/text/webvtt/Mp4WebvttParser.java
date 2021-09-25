package com.google.android.exoplayer.text.webvtt;

import com.google.android.exoplayer.ParserException;
import com.google.android.exoplayer.text.Cue;
import com.google.android.exoplayer.text.SubtitleParser;
import com.google.android.exoplayer.text.webvtt.WebvttCue;
import com.google.android.exoplayer.util.MimeTypes;
import com.google.android.exoplayer.util.ParsableByteArray;
import com.google.android.exoplayer.util.Util;
import java.util.ArrayList;
import java.util.List;

public final class Mp4WebvttParser implements SubtitleParser {
    private static final int BOX_HEADER_SIZE = 8;
    private static final int TYPE_payl = Util.getIntegerCodeForString("payl");
    private static final int TYPE_sttg = Util.getIntegerCodeForString("sttg");
    private static final int TYPE_vttc = Util.getIntegerCodeForString("vttc");
    private final WebvttCue.Builder builder = new WebvttCue.Builder();
    private final ParsableByteArray sampleData = new ParsableByteArray();

    public boolean canParse(String mimeType) {
        return MimeTypes.APPLICATION_MP4VTT.equals(mimeType);
    }

    public Mp4WebvttSubtitle parse(byte[] bytes, int offset, int length) throws ParserException {
        this.sampleData.reset(bytes, offset + length);
        this.sampleData.setPosition(offset);
        List<Cue> resultingCueList = new ArrayList<>();
        while (this.sampleData.bytesLeft() > 0) {
            if (this.sampleData.bytesLeft() < 8) {
                throw new ParserException("Incomplete Mp4Webvtt Top Level box header found.");
            }
            int boxSize = this.sampleData.readInt();
            if (this.sampleData.readInt() == TYPE_vttc) {
                resultingCueList.add(parseVttCueBox(this.sampleData, this.builder, boxSize - 8));
            } else {
                this.sampleData.skipBytes(boxSize - 8);
            }
        }
        return new Mp4WebvttSubtitle(resultingCueList);
    }

    private static Cue parseVttCueBox(ParsableByteArray sampleData2, WebvttCue.Builder builder2, int remainingCueBoxBytes) throws ParserException {
        builder2.reset();
        while (remainingCueBoxBytes > 0) {
            if (remainingCueBoxBytes < 8) {
                throw new ParserException("Incomplete vtt cue box header found.");
            }
            int boxSize = sampleData2.readInt();
            int boxType = sampleData2.readInt();
            int payloadLength = boxSize - 8;
            String boxPayload = new String(sampleData2.data, sampleData2.getPosition(), payloadLength);
            sampleData2.skipBytes(payloadLength);
            remainingCueBoxBytes = (remainingCueBoxBytes - 8) - payloadLength;
            if (boxType == TYPE_sttg) {
                WebvttCueParser.parseCueSettingsList(boxPayload, builder2);
            } else if (boxType == TYPE_payl) {
                WebvttCueParser.parseCueText(boxPayload.trim(), builder2);
            }
        }
        return builder2.build();
    }
}
