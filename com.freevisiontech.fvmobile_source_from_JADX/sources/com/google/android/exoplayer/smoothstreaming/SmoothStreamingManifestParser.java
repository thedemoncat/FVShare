package com.google.android.exoplayer.smoothstreaming;

import android.util.Base64;
import android.util.Pair;
import com.coremedia.iso.boxes.sampleentry.AudioSampleEntry;
import com.google.android.exoplayer.ParserException;
import com.google.android.exoplayer.extractor.mp4.PsshAtomUtil;
import com.google.android.exoplayer.smoothstreaming.SmoothStreamingManifest;
import com.google.android.exoplayer.upstream.UriLoadable;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.CodecSpecificDataUtil;
import com.google.android.exoplayer.util.MimeTypes;
import com.google.android.exoplayer.util.Util;
import com.googlecode.mp4parser.boxes.AC3SpecificBox;
import com.googlecode.mp4parser.boxes.EC3SpecificBox;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class SmoothStreamingManifestParser implements UriLoadable.Parser<SmoothStreamingManifest> {
    private final XmlPullParserFactory xmlParserFactory;

    public SmoothStreamingManifestParser() {
        try {
            this.xmlParserFactory = XmlPullParserFactory.newInstance();
        } catch (XmlPullParserException e) {
            throw new RuntimeException("Couldn't create XmlPullParserFactory instance", e);
        }
    }

    public SmoothStreamingManifest parse(String connectionUrl, InputStream inputStream) throws IOException, ParserException {
        try {
            XmlPullParser xmlParser = this.xmlParserFactory.newPullParser();
            xmlParser.setInput(inputStream, (String) null);
            return (SmoothStreamingManifest) new SmoothStreamMediaParser((ElementParser) null, connectionUrl).parse(xmlParser);
        } catch (XmlPullParserException e) {
            throw new ParserException((Throwable) e);
        }
    }

    public static class MissingFieldException extends ParserException {
        public MissingFieldException(String fieldName) {
            super("Missing required field: " + fieldName);
        }
    }

    private static abstract class ElementParser {
        private final String baseUri;
        private final List<Pair<String, Object>> normalizedAttributes = new LinkedList();
        private final ElementParser parent;
        private final String tag;

        /* access modifiers changed from: protected */
        public abstract Object build();

        public ElementParser(ElementParser parent2, String baseUri2, String tag2) {
            this.parent = parent2;
            this.baseUri = baseUri2;
            this.tag = tag2;
        }

        public final Object parse(XmlPullParser xmlParser) throws XmlPullParserException, IOException, ParserException {
            boolean foundStartTag = false;
            int skippingElementDepth = 0;
            while (true) {
                switch (xmlParser.getEventType()) {
                    case 1:
                        return null;
                    case 2:
                        String tagName = xmlParser.getName();
                        if (!this.tag.equals(tagName)) {
                            if (foundStartTag) {
                                if (skippingElementDepth <= 0) {
                                    if (!handleChildInline(tagName)) {
                                        ElementParser childElementParser = newChildParser(this, tagName, this.baseUri);
                                        if (childElementParser != null) {
                                            addChild(childElementParser.parse(xmlParser));
                                            break;
                                        } else {
                                            skippingElementDepth = 1;
                                            break;
                                        }
                                    } else {
                                        parseStartTag(xmlParser);
                                        break;
                                    }
                                } else {
                                    skippingElementDepth++;
                                    break;
                                }
                            } else {
                                break;
                            }
                        } else {
                            foundStartTag = true;
                            parseStartTag(xmlParser);
                            break;
                        }
                    case 3:
                        if (foundStartTag) {
                            if (skippingElementDepth <= 0) {
                                String tagName2 = xmlParser.getName();
                                parseEndTag(xmlParser);
                                if (handleChildInline(tagName2)) {
                                    break;
                                } else {
                                    return build();
                                }
                            } else {
                                skippingElementDepth--;
                                break;
                            }
                        } else {
                            continue;
                        }
                    case 4:
                        if (foundStartTag && skippingElementDepth == 0) {
                            parseText(xmlParser);
                            break;
                        }
                }
                xmlParser.next();
            }
        }

        private ElementParser newChildParser(ElementParser parent2, String name, String baseUri2) {
            if (TrackElementParser.TAG.equals(name)) {
                return new TrackElementParser(parent2, baseUri2);
            }
            if (ProtectionElementParser.TAG.equals(name)) {
                return new ProtectionElementParser(parent2, baseUri2);
            }
            if (StreamElementParser.TAG.equals(name)) {
                return new StreamElementParser(parent2, baseUri2);
            }
            return null;
        }

        /* access modifiers changed from: protected */
        public final void putNormalizedAttribute(String key, Object value) {
            this.normalizedAttributes.add(Pair.create(key, value));
        }

        /* access modifiers changed from: protected */
        public final Object getNormalizedAttribute(String key) {
            for (int i = 0; i < this.normalizedAttributes.size(); i++) {
                Pair<String, Object> pair = this.normalizedAttributes.get(i);
                if (((String) pair.first).equals(key)) {
                    return pair.second;
                }
            }
            if (this.parent == null) {
                return null;
            }
            return this.parent.getNormalizedAttribute(key);
        }

        /* access modifiers changed from: protected */
        public boolean handleChildInline(String tagName) {
            return false;
        }

        /* access modifiers changed from: protected */
        public void parseStartTag(XmlPullParser xmlParser) throws ParserException {
        }

        /* access modifiers changed from: protected */
        public void parseText(XmlPullParser xmlParser) throws ParserException {
        }

        /* access modifiers changed from: protected */
        public void parseEndTag(XmlPullParser xmlParser) throws ParserException {
        }

        /* access modifiers changed from: protected */
        public void addChild(Object parsedChild) {
        }

        /* access modifiers changed from: protected */
        public final String parseRequiredString(XmlPullParser parser, String key) throws MissingFieldException {
            String value = parser.getAttributeValue((String) null, key);
            if (value != null) {
                return value;
            }
            throw new MissingFieldException(key);
        }

        /* access modifiers changed from: protected */
        public final int parseInt(XmlPullParser parser, String key, int defaultValue) throws ParserException {
            String value = parser.getAttributeValue((String) null, key);
            if (value == null) {
                return defaultValue;
            }
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                throw new ParserException((Throwable) e);
            }
        }

        /* access modifiers changed from: protected */
        public final int parseRequiredInt(XmlPullParser parser, String key) throws ParserException {
            String value = parser.getAttributeValue((String) null, key);
            if (value != null) {
                try {
                    return Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    throw new ParserException((Throwable) e);
                }
            } else {
                throw new MissingFieldException(key);
            }
        }

        /* access modifiers changed from: protected */
        public final long parseLong(XmlPullParser parser, String key, long defaultValue) throws ParserException {
            String value = parser.getAttributeValue((String) null, key);
            if (value == null) {
                return defaultValue;
            }
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException e) {
                throw new ParserException((Throwable) e);
            }
        }

        /* access modifiers changed from: protected */
        public final long parseRequiredLong(XmlPullParser parser, String key) throws ParserException {
            String value = parser.getAttributeValue((String) null, key);
            if (value != null) {
                try {
                    return Long.parseLong(value);
                } catch (NumberFormatException e) {
                    throw new ParserException((Throwable) e);
                }
            } else {
                throw new MissingFieldException(key);
            }
        }

        /* access modifiers changed from: protected */
        public final boolean parseBoolean(XmlPullParser parser, String key, boolean defaultValue) {
            String value = parser.getAttributeValue((String) null, key);
            if (value != null) {
                return Boolean.parseBoolean(value);
            }
            return defaultValue;
        }
    }

    private static class SmoothStreamMediaParser extends ElementParser {
        private static final String KEY_DURATION = "Duration";
        private static final String KEY_DVR_WINDOW_LENGTH = "DVRWindowLength";
        private static final String KEY_IS_LIVE = "IsLive";
        private static final String KEY_LOOKAHEAD_COUNT = "LookaheadCount";
        private static final String KEY_MAJOR_VERSION = "MajorVersion";
        private static final String KEY_MINOR_VERSION = "MinorVersion";
        private static final String KEY_TIME_SCALE = "TimeScale";
        public static final String TAG = "SmoothStreamingMedia";
        private long duration;
        private long dvrWindowLength;
        private boolean isLive;
        private int lookAheadCount = -1;
        private int majorVersion;
        private int minorVersion;
        private SmoothStreamingManifest.ProtectionElement protectionElement = null;
        private List<SmoothStreamingManifest.StreamElement> streamElements = new LinkedList();
        private long timescale;

        public SmoothStreamMediaParser(ElementParser parent, String baseUri) {
            super(parent, baseUri, TAG);
        }

        public void parseStartTag(XmlPullParser parser) throws ParserException {
            this.majorVersion = parseRequiredInt(parser, KEY_MAJOR_VERSION);
            this.minorVersion = parseRequiredInt(parser, KEY_MINOR_VERSION);
            this.timescale = parseLong(parser, KEY_TIME_SCALE, 10000000);
            this.duration = parseRequiredLong(parser, KEY_DURATION);
            this.dvrWindowLength = parseLong(parser, KEY_DVR_WINDOW_LENGTH, 0);
            this.lookAheadCount = parseInt(parser, KEY_LOOKAHEAD_COUNT, -1);
            this.isLive = parseBoolean(parser, KEY_IS_LIVE, false);
            putNormalizedAttribute(KEY_TIME_SCALE, Long.valueOf(this.timescale));
        }

        public void addChild(Object child) {
            if (child instanceof SmoothStreamingManifest.StreamElement) {
                this.streamElements.add((SmoothStreamingManifest.StreamElement) child);
            } else if (child instanceof SmoothStreamingManifest.ProtectionElement) {
                Assertions.checkState(this.protectionElement == null);
                this.protectionElement = (SmoothStreamingManifest.ProtectionElement) child;
            }
        }

        public Object build() {
            SmoothStreamingManifest.StreamElement[] streamElementArray = new SmoothStreamingManifest.StreamElement[this.streamElements.size()];
            this.streamElements.toArray(streamElementArray);
            return new SmoothStreamingManifest(this.majorVersion, this.minorVersion, this.timescale, this.duration, this.dvrWindowLength, this.lookAheadCount, this.isLive, this.protectionElement, streamElementArray);
        }
    }

    private static class ProtectionElementParser extends ElementParser {
        public static final String KEY_SYSTEM_ID = "SystemID";
        public static final String TAG = "Protection";
        public static final String TAG_PROTECTION_HEADER = "ProtectionHeader";
        private boolean inProtectionHeader;
        private byte[] initData;
        private UUID uuid;

        public ProtectionElementParser(ElementParser parent, String baseUri) {
            super(parent, baseUri, TAG);
        }

        public boolean handleChildInline(String tag) {
            return TAG_PROTECTION_HEADER.equals(tag);
        }

        public void parseStartTag(XmlPullParser parser) {
            if (TAG_PROTECTION_HEADER.equals(parser.getName())) {
                this.inProtectionHeader = true;
                this.uuid = UUID.fromString(stripCurlyBraces(parser.getAttributeValue((String) null, KEY_SYSTEM_ID)));
            }
        }

        public void parseText(XmlPullParser parser) {
            if (this.inProtectionHeader) {
                this.initData = Base64.decode(parser.getText(), 0);
            }
        }

        public void parseEndTag(XmlPullParser parser) {
            if (TAG_PROTECTION_HEADER.equals(parser.getName())) {
                this.inProtectionHeader = false;
            }
        }

        public Object build() {
            return new SmoothStreamingManifest.ProtectionElement(this.uuid, PsshAtomUtil.buildPsshAtom(this.uuid, this.initData));
        }

        private static String stripCurlyBraces(String uuidString) {
            if (uuidString.charAt(0) == '{' && uuidString.charAt(uuidString.length() - 1) == '}') {
                return uuidString.substring(1, uuidString.length() - 1);
            }
            return uuidString;
        }
    }

    private static class StreamElementParser extends ElementParser {
        private static final String KEY_DISPLAY_HEIGHT = "DisplayHeight";
        private static final String KEY_DISPLAY_WIDTH = "DisplayWidth";
        private static final String KEY_FRAGMENT_DURATION = "d";
        private static final String KEY_FRAGMENT_REPEAT_COUNT = "r";
        private static final String KEY_FRAGMENT_START_TIME = "t";
        private static final String KEY_LANGUAGE = "Language";
        private static final String KEY_MAX_HEIGHT = "MaxHeight";
        private static final String KEY_MAX_WIDTH = "MaxWidth";
        private static final String KEY_NAME = "Name";
        private static final String KEY_QUALITY_LEVELS = "QualityLevels";
        private static final String KEY_SUB_TYPE = "Subtype";
        private static final String KEY_TIME_SCALE = "TimeScale";
        private static final String KEY_TYPE = "Type";
        private static final String KEY_TYPE_AUDIO = "audio";
        private static final String KEY_TYPE_TEXT = "text";
        private static final String KEY_TYPE_VIDEO = "video";
        private static final String KEY_URL = "Url";
        public static final String TAG = "StreamIndex";
        private static final String TAG_STREAM_FRAGMENT = "c";
        private final String baseUri;
        private int displayHeight;
        private int displayWidth;
        private String language;
        private long lastChunkDuration;
        private int maxHeight;
        private int maxWidth;
        private String name;
        private int qualityLevels;
        private ArrayList<Long> startTimes;
        private String subType;
        private long timescale;
        private final List<SmoothStreamingManifest.TrackElement> tracks = new LinkedList();
        private int type;
        private String url;

        public StreamElementParser(ElementParser parent, String baseUri2) {
            super(parent, baseUri2, TAG);
            this.baseUri = baseUri2;
        }

        public boolean handleChildInline(String tag) {
            return TAG_STREAM_FRAGMENT.equals(tag);
        }

        public void parseStartTag(XmlPullParser parser) throws ParserException {
            if (TAG_STREAM_FRAGMENT.equals(parser.getName())) {
                parseStreamFragmentStartTag(parser);
            } else {
                parseStreamElementStartTag(parser);
            }
        }

        private void parseStreamFragmentStartTag(XmlPullParser parser) throws ParserException {
            int chunkIndex = this.startTimes.size();
            long startTime = parseLong(parser, KEY_FRAGMENT_START_TIME, -1);
            if (startTime == -1) {
                if (chunkIndex == 0) {
                    startTime = 0;
                } else if (this.lastChunkDuration != -1) {
                    startTime = this.startTimes.get(chunkIndex - 1).longValue() + this.lastChunkDuration;
                } else {
                    throw new ParserException("Unable to infer start time");
                }
            }
            int chunkIndex2 = chunkIndex + 1;
            this.startTimes.add(Long.valueOf(startTime));
            this.lastChunkDuration = parseLong(parser, KEY_FRAGMENT_DURATION, -1);
            long repeatCount = parseLong(parser, KEY_FRAGMENT_REPEAT_COUNT, 1);
            if (repeatCount <= 1 || this.lastChunkDuration != -1) {
                for (int i = 1; ((long) i) < repeatCount; i++) {
                    chunkIndex2++;
                    this.startTimes.add(Long.valueOf((this.lastChunkDuration * ((long) i)) + startTime));
                }
                return;
            }
            throw new ParserException("Repeated chunk with unspecified duration");
        }

        private void parseStreamElementStartTag(XmlPullParser parser) throws ParserException {
            this.type = parseType(parser);
            putNormalizedAttribute(KEY_TYPE, Integer.valueOf(this.type));
            if (this.type == 2) {
                this.subType = parseRequiredString(parser, KEY_SUB_TYPE);
            } else {
                this.subType = parser.getAttributeValue((String) null, KEY_SUB_TYPE);
            }
            this.name = parser.getAttributeValue((String) null, KEY_NAME);
            this.qualityLevels = parseInt(parser, KEY_QUALITY_LEVELS, -1);
            this.url = parseRequiredString(parser, KEY_URL);
            this.maxWidth = parseInt(parser, KEY_MAX_WIDTH, -1);
            this.maxHeight = parseInt(parser, KEY_MAX_HEIGHT, -1);
            this.displayWidth = parseInt(parser, KEY_DISPLAY_WIDTH, -1);
            this.displayHeight = parseInt(parser, KEY_DISPLAY_HEIGHT, -1);
            this.language = parser.getAttributeValue((String) null, KEY_LANGUAGE);
            putNormalizedAttribute(KEY_LANGUAGE, this.language);
            this.timescale = (long) parseInt(parser, KEY_TIME_SCALE, -1);
            if (this.timescale == -1) {
                this.timescale = ((Long) getNormalizedAttribute(KEY_TIME_SCALE)).longValue();
            }
            this.startTimes = new ArrayList<>();
        }

        private int parseType(XmlPullParser parser) throws ParserException {
            String value = parser.getAttributeValue((String) null, KEY_TYPE);
            if (value == null) {
                throw new MissingFieldException(KEY_TYPE);
            } else if ("audio".equalsIgnoreCase(value)) {
                return 0;
            } else {
                if ("video".equalsIgnoreCase(value)) {
                    return 1;
                }
                if ("text".equalsIgnoreCase(value)) {
                    return 2;
                }
                throw new ParserException("Invalid key value[" + value + "]");
            }
        }

        public void addChild(Object child) {
            if (child instanceof SmoothStreamingManifest.TrackElement) {
                this.tracks.add((SmoothStreamingManifest.TrackElement) child);
            }
        }

        public Object build() {
            SmoothStreamingManifest.TrackElement[] trackElements = new SmoothStreamingManifest.TrackElement[this.tracks.size()];
            this.tracks.toArray(trackElements);
            return new SmoothStreamingManifest.StreamElement(this.baseUri, this.url, this.type, this.subType, this.timescale, this.name, this.qualityLevels, this.maxWidth, this.maxHeight, this.displayWidth, this.displayHeight, this.language, trackElements, this.startTimes, this.lastChunkDuration);
        }
    }

    private static class TrackElementParser extends ElementParser {
        private static final String KEY_BITRATE = "Bitrate";
        private static final String KEY_CHANNELS = "Channels";
        private static final String KEY_CODEC_PRIVATE_DATA = "CodecPrivateData";
        private static final String KEY_FOUR_CC = "FourCC";
        private static final String KEY_INDEX = "Index";
        private static final String KEY_LANGUAGE = "Language";
        private static final String KEY_MAX_HEIGHT = "MaxHeight";
        private static final String KEY_MAX_WIDTH = "MaxWidth";
        private static final String KEY_SAMPLING_RATE = "SamplingRate";
        private static final String KEY_TYPE = "Type";
        public static final String TAG = "QualityLevel";
        private int bitrate;
        private int channels;
        private final List<byte[]> csd = new LinkedList();
        private int index;
        private String language;
        private int maxHeight;
        private int maxWidth;
        private String mimeType;
        private int samplingRate;

        public TrackElementParser(ElementParser parent, String baseUri) {
            super(parent, baseUri, TAG);
        }

        public void parseStartTag(XmlPullParser parser) throws ParserException {
            int type = ((Integer) getNormalizedAttribute(KEY_TYPE)).intValue();
            this.index = parseInt(parser, KEY_INDEX, -1);
            this.bitrate = parseRequiredInt(parser, KEY_BITRATE);
            this.language = (String) getNormalizedAttribute(KEY_LANGUAGE);
            if (type == 1) {
                this.maxHeight = parseRequiredInt(parser, KEY_MAX_HEIGHT);
                this.maxWidth = parseRequiredInt(parser, KEY_MAX_WIDTH);
                this.mimeType = fourCCToMimeType(parseRequiredString(parser, KEY_FOUR_CC));
            } else {
                this.maxHeight = -1;
                this.maxWidth = -1;
                String fourCC = parser.getAttributeValue((String) null, KEY_FOUR_CC);
                this.mimeType = fourCC != null ? fourCCToMimeType(fourCC) : type == 0 ? MimeTypes.AUDIO_AAC : null;
            }
            if (type == 0) {
                this.samplingRate = parseRequiredInt(parser, KEY_SAMPLING_RATE);
                this.channels = parseRequiredInt(parser, KEY_CHANNELS);
            } else {
                this.samplingRate = -1;
                this.channels = -1;
            }
            String value = parser.getAttributeValue((String) null, KEY_CODEC_PRIVATE_DATA);
            if (value != null && value.length() > 0) {
                byte[] codecPrivateData = Util.getBytesFromHexString(value);
                byte[][] split = CodecSpecificDataUtil.splitNalUnits(codecPrivateData);
                if (split == null) {
                    this.csd.add(codecPrivateData);
                    return;
                }
                for (byte[] add : split) {
                    this.csd.add(add);
                }
            }
        }

        public Object build() {
            byte[][] csdArray = null;
            if (!this.csd.isEmpty()) {
                csdArray = new byte[this.csd.size()][];
                this.csd.toArray(csdArray);
            }
            return new SmoothStreamingManifest.TrackElement(this.index, this.bitrate, this.mimeType, csdArray, this.maxWidth, this.maxHeight, this.samplingRate, this.channels, this.language);
        }

        private static String fourCCToMimeType(String fourCC) {
            if (fourCC.equalsIgnoreCase("H264") || fourCC.equalsIgnoreCase("X264") || fourCC.equalsIgnoreCase("AVC1") || fourCC.equalsIgnoreCase("DAVC")) {
                return MimeTypes.VIDEO_H264;
            }
            if (fourCC.equalsIgnoreCase("AAC") || fourCC.equalsIgnoreCase("AACL") || fourCC.equalsIgnoreCase("AACH") || fourCC.equalsIgnoreCase("AACP")) {
                return MimeTypes.AUDIO_AAC;
            }
            if (fourCC.equalsIgnoreCase("TTML")) {
                return MimeTypes.APPLICATION_TTML;
            }
            if (fourCC.equalsIgnoreCase(AudioSampleEntry.TYPE8) || fourCC.equalsIgnoreCase(AC3SpecificBox.TYPE)) {
                return MimeTypes.AUDIO_AC3;
            }
            if (fourCC.equalsIgnoreCase(AudioSampleEntry.TYPE9) || fourCC.equalsIgnoreCase(EC3SpecificBox.TYPE)) {
                return MimeTypes.AUDIO_E_AC3;
            }
            if (fourCC.equalsIgnoreCase("dtsc")) {
                return MimeTypes.AUDIO_DTS;
            }
            if (fourCC.equalsIgnoreCase(AudioSampleEntry.TYPE12) || fourCC.equalsIgnoreCase(AudioSampleEntry.TYPE11)) {
                return MimeTypes.AUDIO_DTS_HD;
            }
            if (fourCC.equalsIgnoreCase(AudioSampleEntry.TYPE13)) {
                return MimeTypes.AUDIO_DTS_EXPRESS;
            }
            if (fourCC.equalsIgnoreCase("opus")) {
                return MimeTypes.AUDIO_OPUS;
            }
            return null;
        }
    }
}
