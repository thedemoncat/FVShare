package com.google.android.exoplayer.smoothstreaming;

import android.content.Context;
import com.google.android.exoplayer.chunk.VideoFormatSelectorUtil;
import com.google.android.exoplayer.smoothstreaming.SmoothStreamingManifest;
import com.google.android.exoplayer.smoothstreaming.SmoothStreamingTrackSelector;
import com.google.android.exoplayer.util.Util;
import java.io.IOException;
import java.util.Arrays;

public final class DefaultSmoothStreamingTrackSelector implements SmoothStreamingTrackSelector {
    private final Context context;
    private final boolean filterProtectedHdContent;
    private final boolean filterVideoRepresentations;
    private final int streamElementType;

    public static DefaultSmoothStreamingTrackSelector newVideoInstance(Context context2, boolean filterVideoRepresentations2, boolean filterProtectedHdContent2) {
        return new DefaultSmoothStreamingTrackSelector(1, context2, filterVideoRepresentations2, filterProtectedHdContent2);
    }

    public static DefaultSmoothStreamingTrackSelector newAudioInstance() {
        return new DefaultSmoothStreamingTrackSelector(0, (Context) null, false, false);
    }

    public static DefaultSmoothStreamingTrackSelector newTextInstance() {
        return new DefaultSmoothStreamingTrackSelector(2, (Context) null, false, false);
    }

    private DefaultSmoothStreamingTrackSelector(int streamElementType2, Context context2, boolean filterVideoRepresentations2, boolean filterProtectedHdContent2) {
        this.context = context2;
        this.streamElementType = streamElementType2;
        this.filterVideoRepresentations = filterVideoRepresentations2;
        this.filterProtectedHdContent = filterProtectedHdContent2;
    }

    public void selectTracks(SmoothStreamingManifest manifest, SmoothStreamingTrackSelector.Output output) throws IOException {
        int[] trackIndices;
        for (int i = 0; i < manifest.streamElements.length; i++) {
            SmoothStreamingManifest.TrackElement[] tracks = manifest.streamElements[i].tracks;
            if (manifest.streamElements[i].type == this.streamElementType) {
                if (this.streamElementType == 1) {
                    if (this.filterVideoRepresentations) {
                        trackIndices = VideoFormatSelectorUtil.selectVideoFormatsForDefaultDisplay(this.context, Arrays.asList(tracks), (String[]) null, this.filterProtectedHdContent && manifest.protectionElement != null);
                    } else {
                        trackIndices = Util.firstIntegersArray(tracks.length);
                    }
                    if (trackCount > 1) {
                        output.adaptiveTrack(manifest, i, trackIndices);
                    }
                    for (int fixedTrack : trackIndices) {
                        output.fixedTrack(manifest, i, fixedTrack);
                    }
                } else {
                    for (int j = 0; j < tracks.length; j++) {
                        output.fixedTrack(manifest, i, j);
                    }
                }
            }
        }
    }
}
