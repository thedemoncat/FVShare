package com.google.android.exoplayer.extractor.mp4;

import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.util.Util;

public final class Track {
    public static final int TYPE_sbtl = Util.getIntegerCodeForString("sbtl");
    public static final int TYPE_soun = Util.getIntegerCodeForString("soun");
    public static final int TYPE_subt = Util.getIntegerCodeForString("subt");
    public static final int TYPE_text = Util.getIntegerCodeForString("text");
    public static final int TYPE_vide = Util.getIntegerCodeForString("vide");
    public final long durationUs;
    public final long[] editListDurations;
    public final long[] editListMediaTimes;

    /* renamed from: id */
    public final int f1196id;
    public final MediaFormat mediaFormat;
    public final long movieTimescale;
    public final int nalUnitLengthFieldLength;
    public final TrackEncryptionBox[] sampleDescriptionEncryptionBoxes;
    public final long timescale;
    public final int type;

    public Track(int id, int type2, long timescale2, long movieTimescale2, long durationUs2, MediaFormat mediaFormat2, TrackEncryptionBox[] sampleDescriptionEncryptionBoxes2, int nalUnitLengthFieldLength2, long[] editListDurations2, long[] editListMediaTimes2) {
        this.f1196id = id;
        this.type = type2;
        this.timescale = timescale2;
        this.movieTimescale = movieTimescale2;
        this.durationUs = durationUs2;
        this.mediaFormat = mediaFormat2;
        this.sampleDescriptionEncryptionBoxes = sampleDescriptionEncryptionBoxes2;
        this.nalUnitLengthFieldLength = nalUnitLengthFieldLength2;
        this.editListDurations = editListDurations2;
        this.editListMediaTimes = editListMediaTimes2;
    }
}
