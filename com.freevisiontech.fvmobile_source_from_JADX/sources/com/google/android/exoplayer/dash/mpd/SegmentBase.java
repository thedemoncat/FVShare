package com.google.android.exoplayer.dash.mpd;

import com.google.android.exoplayer.C1907C;
import com.google.android.exoplayer.util.Util;
import java.util.List;

public abstract class SegmentBase {
    final RangedUri initialization;
    final long presentationTimeOffset;
    final long timescale;

    public SegmentBase(RangedUri initialization2, long timescale2, long presentationTimeOffset2) {
        this.initialization = initialization2;
        this.timescale = timescale2;
        this.presentationTimeOffset = presentationTimeOffset2;
    }

    public RangedUri getInitialization(Representation representation) {
        return this.initialization;
    }

    public long getPresentationTimeOffsetUs() {
        return Util.scaleLargeTimestamp(this.presentationTimeOffset, C1907C.MICROS_PER_SECOND, this.timescale);
    }

    public static class SingleSegmentBase extends SegmentBase {
        final long indexLength;
        final long indexStart;
        public final String uri;

        public SingleSegmentBase(RangedUri initialization, long timescale, long presentationTimeOffset, String uri2, long indexStart2, long indexLength2) {
            super(initialization, timescale, presentationTimeOffset);
            this.uri = uri2;
            this.indexStart = indexStart2;
            this.indexLength = indexLength2;
        }

        public SingleSegmentBase(String uri2) {
            this((RangedUri) null, 1, 0, uri2, 0, -1);
        }

        public RangedUri getIndex() {
            if (this.indexLength <= 0) {
                return null;
            }
            return new RangedUri(this.uri, (String) null, this.indexStart, this.indexLength);
        }
    }

    public static abstract class MultiSegmentBase extends SegmentBase {
        final long duration;
        final List<SegmentTimelineElement> segmentTimeline;
        final int startNumber;

        public abstract int getLastSegmentNum(long j);

        public abstract RangedUri getSegmentUrl(Representation representation, int i);

        public MultiSegmentBase(RangedUri initialization, long timescale, long presentationTimeOffset, int startNumber2, long duration2, List<SegmentTimelineElement> segmentTimeline2) {
            super(initialization, timescale, presentationTimeOffset);
            this.startNumber = startNumber2;
            this.duration = duration2;
            this.segmentTimeline = segmentTimeline2;
        }

        public int getSegmentNum(long timeUs, long periodDurationUs) {
            int firstSegmentNum = getFirstSegmentNum();
            int lowIndex = firstSegmentNum;
            int highIndex = getLastSegmentNum(periodDurationUs);
            if (this.segmentTimeline == null) {
                int segmentNum = this.startNumber + ((int) (timeUs / ((this.duration * C1907C.MICROS_PER_SECOND) / this.timescale)));
                if (segmentNum < lowIndex) {
                    return lowIndex;
                }
                if (highIndex == -1 || segmentNum <= highIndex) {
                    return segmentNum;
                }
                return highIndex;
            }
            while (lowIndex <= highIndex) {
                int midIndex = (lowIndex + highIndex) / 2;
                long midTimeUs = getSegmentTimeUs(midIndex);
                if (midTimeUs < timeUs) {
                    lowIndex = midIndex + 1;
                } else if (midTimeUs <= timeUs) {
                    return midIndex;
                } else {
                    highIndex = midIndex - 1;
                }
            }
            return lowIndex == firstSegmentNum ? lowIndex : highIndex;
        }

        public final long getSegmentDurationUs(int sequenceNumber, long periodDurationUs) {
            if (this.segmentTimeline != null) {
                return (this.segmentTimeline.get(sequenceNumber - this.startNumber).duration * C1907C.MICROS_PER_SECOND) / this.timescale;
            }
            return sequenceNumber == getLastSegmentNum(periodDurationUs) ? periodDurationUs - getSegmentTimeUs(sequenceNumber) : (this.duration * C1907C.MICROS_PER_SECOND) / this.timescale;
        }

        public final long getSegmentTimeUs(int sequenceNumber) {
            long unscaledSegmentTime;
            if (this.segmentTimeline != null) {
                unscaledSegmentTime = this.segmentTimeline.get(sequenceNumber - this.startNumber).startTime - this.presentationTimeOffset;
            } else {
                unscaledSegmentTime = ((long) (sequenceNumber - this.startNumber)) * this.duration;
            }
            return Util.scaleLargeTimestamp(unscaledSegmentTime, C1907C.MICROS_PER_SECOND, this.timescale);
        }

        public int getFirstSegmentNum() {
            return this.startNumber;
        }

        public boolean isExplicit() {
            return this.segmentTimeline != null;
        }
    }

    public static class SegmentList extends MultiSegmentBase {
        final List<RangedUri> mediaSegments;

        public SegmentList(RangedUri initialization, long timescale, long presentationTimeOffset, int startNumber, long duration, List<SegmentTimelineElement> segmentTimeline, List<RangedUri> mediaSegments2) {
            super(initialization, timescale, presentationTimeOffset, startNumber, duration, segmentTimeline);
            this.mediaSegments = mediaSegments2;
        }

        public RangedUri getSegmentUrl(Representation representation, int sequenceNumber) {
            return this.mediaSegments.get(sequenceNumber - this.startNumber);
        }

        public int getLastSegmentNum(long periodDurationUs) {
            return (this.startNumber + this.mediaSegments.size()) - 1;
        }

        public boolean isExplicit() {
            return true;
        }
    }

    public static class SegmentTemplate extends MultiSegmentBase {
        private final String baseUrl;
        final UrlTemplate initializationTemplate;
        final UrlTemplate mediaTemplate;

        public SegmentTemplate(RangedUri initialization, long timescale, long presentationTimeOffset, int startNumber, long duration, List<SegmentTimelineElement> segmentTimeline, UrlTemplate initializationTemplate2, UrlTemplate mediaTemplate2, String baseUrl2) {
            super(initialization, timescale, presentationTimeOffset, startNumber, duration, segmentTimeline);
            this.initializationTemplate = initializationTemplate2;
            this.mediaTemplate = mediaTemplate2;
            this.baseUrl = baseUrl2;
        }

        public RangedUri getInitialization(Representation representation) {
            if (this.initializationTemplate == null) {
                return super.getInitialization(representation);
            }
            return new RangedUri(this.baseUrl, this.initializationTemplate.buildUri(representation.format.f1192id, 0, representation.format.bitrate, 0), 0, -1);
        }

        public RangedUri getSegmentUrl(Representation representation, int sequenceNumber) {
            long time;
            if (this.segmentTimeline != null) {
                time = ((SegmentTimelineElement) this.segmentTimeline.get(sequenceNumber - this.startNumber)).startTime;
            } else {
                time = ((long) (sequenceNumber - this.startNumber)) * this.duration;
            }
            return new RangedUri(this.baseUrl, this.mediaTemplate.buildUri(representation.format.f1192id, sequenceNumber, representation.format.bitrate, time), 0, -1);
        }

        public int getLastSegmentNum(long periodDurationUs) {
            if (this.segmentTimeline != null) {
                return (this.segmentTimeline.size() + this.startNumber) - 1;
            }
            if (periodDurationUs == -1) {
                return -1;
            }
            return (this.startNumber + ((int) Util.ceilDivide(periodDurationUs, (this.duration * C1907C.MICROS_PER_SECOND) / this.timescale))) - 1;
        }
    }

    public static class SegmentTimelineElement {
        long duration;
        long startTime;

        public SegmentTimelineElement(long startTime2, long duration2) {
            this.startTime = startTime2;
            this.duration = duration2;
        }
    }
}
