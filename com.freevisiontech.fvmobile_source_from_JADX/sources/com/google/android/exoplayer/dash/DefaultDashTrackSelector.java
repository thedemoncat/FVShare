package com.google.android.exoplayer.dash;

import android.content.Context;
import com.google.android.exoplayer.chunk.VideoFormatSelectorUtil;
import com.google.android.exoplayer.dash.DashTrackSelector;
import com.google.android.exoplayer.dash.mpd.AdaptationSet;
import com.google.android.exoplayer.dash.mpd.MediaPresentationDescription;
import com.google.android.exoplayer.dash.mpd.Period;
import com.google.android.exoplayer.util.Util;
import java.io.IOException;

public final class DefaultDashTrackSelector implements DashTrackSelector {
    private final int adaptationSetType;
    private final Context context;
    private final boolean filterProtectedHdContent;
    private final boolean filterVideoRepresentations;

    public static DefaultDashTrackSelector newVideoInstance(Context context2, boolean filterVideoRepresentations2, boolean filterProtectedHdContent2) {
        return new DefaultDashTrackSelector(0, context2, filterVideoRepresentations2, filterProtectedHdContent2);
    }

    public static DefaultDashTrackSelector newAudioInstance() {
        return new DefaultDashTrackSelector(1, (Context) null, false, false);
    }

    public static DefaultDashTrackSelector newTextInstance() {
        return new DefaultDashTrackSelector(2, (Context) null, false, false);
    }

    private DefaultDashTrackSelector(int adaptationSetType2, Context context2, boolean filterVideoRepresentations2, boolean filterProtectedHdContent2) {
        this.adaptationSetType = adaptationSetType2;
        this.context = context2;
        this.filterVideoRepresentations = filterVideoRepresentations2;
        this.filterProtectedHdContent = filterProtectedHdContent2;
    }

    public void selectTracks(MediaPresentationDescription manifest, int periodIndex, DashTrackSelector.Output output) throws IOException {
        int[] representations;
        Period period = manifest.getPeriod(periodIndex);
        for (int i = 0; i < period.adaptationSets.size(); i++) {
            AdaptationSet adaptationSet = period.adaptationSets.get(i);
            if (adaptationSet.type == this.adaptationSetType) {
                if (this.adaptationSetType == 0) {
                    if (this.filterVideoRepresentations) {
                        representations = VideoFormatSelectorUtil.selectVideoFormatsForDefaultDisplay(this.context, adaptationSet.representations, (String[]) null, this.filterProtectedHdContent && adaptationSet.hasContentProtection());
                    } else {
                        representations = Util.firstIntegersArray(adaptationSet.representations.size());
                    }
                    if (representationCount > 1) {
                        output.adaptiveTrack(manifest, periodIndex, i, representations);
                    }
                    for (int fixedTrack : representations) {
                        output.fixedTrack(manifest, periodIndex, i, fixedTrack);
                    }
                } else {
                    for (int j = 0; j < adaptationSet.representations.size(); j++) {
                        output.fixedTrack(manifest, periodIndex, i, j);
                    }
                }
            }
        }
    }
}
