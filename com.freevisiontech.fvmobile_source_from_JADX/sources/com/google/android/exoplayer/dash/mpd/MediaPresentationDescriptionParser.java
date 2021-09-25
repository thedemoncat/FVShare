package com.google.android.exoplayer.dash.mpd;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import com.google.android.exoplayer.ParserException;
import com.google.android.exoplayer.chunk.Format;
import com.google.android.exoplayer.dash.mpd.SegmentBase;
import com.google.android.exoplayer.drm.DrmInitData;
import com.google.android.exoplayer.extractor.mp4.PsshAtomUtil;
import com.google.android.exoplayer.upstream.UriLoadable;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.MimeTypes;
import com.google.android.exoplayer.util.ParserUtil;
import com.google.android.exoplayer.util.UriUtil;
import com.google.android.exoplayer.util.Util;
import com.google.android.vending.expansion.downloader.Constants;
import com.lzy.okgo.model.HttpHeaders;
import com.mp4parser.iso14496.part30.WebVTTSampleEntry;
import com.mp4parser.iso14496.part30.XMLSubtitleSampleEntry;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import p012tv.danmaku.ijk.media.player.IjkMediaMeta;

public class MediaPresentationDescriptionParser extends DefaultHandler implements UriLoadable.Parser<MediaPresentationDescription> {
    private static final Pattern FRAME_RATE_PATTERN = Pattern.compile("(\\d+)(?:/(\\d+))?");
    private static final String TAG = "MediaPresentationDescriptionParser";
    private final String contentId;
    private final XmlPullParserFactory xmlParserFactory;

    public MediaPresentationDescriptionParser() {
        this((String) null);
    }

    public MediaPresentationDescriptionParser(String contentId2) {
        this.contentId = contentId2;
        try {
            this.xmlParserFactory = XmlPullParserFactory.newInstance();
        } catch (XmlPullParserException e) {
            throw new RuntimeException("Couldn't create XmlPullParserFactory instance", e);
        }
    }

    public MediaPresentationDescription parse(String connectionUrl, InputStream inputStream) throws IOException, ParserException {
        try {
            XmlPullParser xpp = this.xmlParserFactory.newPullParser();
            xpp.setInput(inputStream, (String) null);
            if (xpp.next() == 2 && "MPD".equals(xpp.getName())) {
                return parseMediaPresentationDescription(xpp, connectionUrl);
            }
            throw new ParserException("inputStream does not contain a valid media presentation description");
        } catch (XmlPullParserException e) {
            throw new ParserException((Throwable) e);
        } catch (ParseException e2) {
            throw new ParserException((Throwable) e2);
        }
    }

    /* access modifiers changed from: protected */
    public MediaPresentationDescription parseMediaPresentationDescription(XmlPullParser xpp, String baseUrl) throws XmlPullParserException, IOException, ParseException {
        long availabilityStartTime = parseDateTime(xpp, "availabilityStartTime", -1);
        long durationMs = parseDuration(xpp, "mediaPresentationDuration", -1);
        long minBufferTimeMs = parseDuration(xpp, "minBufferTime", -1);
        String typeString = xpp.getAttributeValue((String) null, IjkMediaMeta.IJKM_KEY_TYPE);
        boolean dynamic = typeString != null ? typeString.equals("dynamic") : false;
        long minUpdateTimeMs = dynamic ? parseDuration(xpp, "minimumUpdatePeriod", -1) : -1;
        long timeShiftBufferDepthMs = dynamic ? parseDuration(xpp, "timeShiftBufferDepth", -1) : -1;
        UtcTimingElement utcTiming = null;
        String location = null;
        ArrayList arrayList = new ArrayList();
        long nextPeriodStartMs = dynamic ? -1 : 0;
        boolean seenEarlyAccessPeriod = false;
        boolean seenFirstBaseUrl = false;
        do {
            xpp.next();
            if (ParserUtil.isStartTag(xpp, "BaseURL")) {
                if (!seenFirstBaseUrl) {
                    baseUrl = parseBaseUrl(xpp, baseUrl);
                    seenFirstBaseUrl = true;
                }
            } else if (ParserUtil.isStartTag(xpp, "UTCTiming")) {
                utcTiming = parseUtcTiming(xpp);
            } else if (ParserUtil.isStartTag(xpp, HttpHeaders.HEAD_KEY_LOCATION)) {
                location = xpp.nextText();
            } else if (ParserUtil.isStartTag(xpp, "Period") && !seenEarlyAccessPeriod) {
                Pair<Period, Long> periodWithDurationMs = parsePeriod(xpp, baseUrl, nextPeriodStartMs);
                Period period = (Period) periodWithDurationMs.first;
                if (period.startMs != -1) {
                    long periodDurationMs = ((Long) periodWithDurationMs.second).longValue();
                    nextPeriodStartMs = periodDurationMs == -1 ? -1 : period.startMs + periodDurationMs;
                    arrayList.add(period);
                } else if (dynamic) {
                    seenEarlyAccessPeriod = true;
                } else {
                    throw new ParserException("Unable to determine start of period " + arrayList.size());
                }
            }
        } while (!ParserUtil.isEndTag(xpp, "MPD"));
        if (durationMs == -1) {
            if (nextPeriodStartMs != -1) {
                durationMs = nextPeriodStartMs;
            } else if (!dynamic) {
                throw new ParserException("Unable to determine duration of static manifest.");
            }
        }
        if (!arrayList.isEmpty()) {
            return buildMediaPresentationDescription(availabilityStartTime, durationMs, minBufferTimeMs, dynamic, minUpdateTimeMs, timeShiftBufferDepthMs, utcTiming, location, arrayList);
        }
        throw new ParserException("No periods found.");
    }

    /* access modifiers changed from: protected */
    public MediaPresentationDescription buildMediaPresentationDescription(long availabilityStartTime, long durationMs, long minBufferTimeMs, boolean dynamic, long minUpdateTimeMs, long timeShiftBufferDepthMs, UtcTimingElement utcTiming, String location, List<Period> periods) {
        return new MediaPresentationDescription(availabilityStartTime, durationMs, minBufferTimeMs, dynamic, minUpdateTimeMs, timeShiftBufferDepthMs, utcTiming, location, periods);
    }

    /* access modifiers changed from: protected */
    public UtcTimingElement parseUtcTiming(XmlPullParser xpp) {
        return buildUtcTimingElement(xpp.getAttributeValue((String) null, "schemeIdUri"), xpp.getAttributeValue((String) null, "value"));
    }

    /* access modifiers changed from: protected */
    public UtcTimingElement buildUtcTimingElement(String schemeIdUri, String value) {
        return new UtcTimingElement(schemeIdUri, value);
    }

    /* access modifiers changed from: protected */
    public Pair<Period, Long> parsePeriod(XmlPullParser xpp, String baseUrl, long defaultStartMs) throws XmlPullParserException, IOException {
        String id = xpp.getAttributeValue((String) null, "id");
        long startMs = parseDuration(xpp, TtmlNode.START, defaultStartMs);
        long durationMs = parseDuration(xpp, "duration", -1);
        SegmentBase segmentBase = null;
        List<AdaptationSet> adaptationSets = new ArrayList<>();
        boolean seenFirstBaseUrl = false;
        do {
            xpp.next();
            if (ParserUtil.isStartTag(xpp, "BaseURL")) {
                if (!seenFirstBaseUrl) {
                    baseUrl = parseBaseUrl(xpp, baseUrl);
                    seenFirstBaseUrl = true;
                }
            } else if (ParserUtil.isStartTag(xpp, "AdaptationSet")) {
                adaptationSets.add(parseAdaptationSet(xpp, baseUrl, segmentBase));
            } else if (ParserUtil.isStartTag(xpp, "SegmentBase")) {
                segmentBase = parseSegmentBase(xpp, baseUrl, (SegmentBase.SingleSegmentBase) null);
            } else if (ParserUtil.isStartTag(xpp, "SegmentList")) {
                segmentBase = parseSegmentList(xpp, baseUrl, (SegmentBase.SegmentList) null);
            } else if (ParserUtil.isStartTag(xpp, "SegmentTemplate")) {
                segmentBase = parseSegmentTemplate(xpp, baseUrl, (SegmentBase.SegmentTemplate) null);
            }
        } while (!ParserUtil.isEndTag(xpp, "Period"));
        return Pair.create(buildPeriod(id, startMs, adaptationSets), Long.valueOf(durationMs));
    }

    /* access modifiers changed from: protected */
    public Period buildPeriod(String id, long startMs, List<AdaptationSet> adaptationSets) {
        return new Period(id, startMs, adaptationSets);
    }

    /* access modifiers changed from: protected */
    public AdaptationSet parseAdaptationSet(XmlPullParser xpp, String baseUrl, SegmentBase segmentBase) throws XmlPullParserException, IOException {
        int id = parseInt(xpp, "id", -1);
        int contentType = parseContentType(xpp);
        String mimeType = xpp.getAttributeValue((String) null, "mimeType");
        String codecs = xpp.getAttributeValue((String) null, "codecs");
        int width = parseInt(xpp, "width", -1);
        int height = parseInt(xpp, "height", -1);
        float frameRate = parseFrameRate(xpp, -1.0f);
        int audioChannels = -1;
        int audioSamplingRate = parseInt(xpp, "audioSamplingRate", -1);
        String language = xpp.getAttributeValue((String) null, "lang");
        ContentProtectionsBuilder contentProtectionsBuilder = new ContentProtectionsBuilder();
        ArrayList arrayList = new ArrayList();
        boolean seenFirstBaseUrl = false;
        do {
            xpp.next();
            if (ParserUtil.isStartTag(xpp, "BaseURL")) {
                if (!seenFirstBaseUrl) {
                    baseUrl = parseBaseUrl(xpp, baseUrl);
                    seenFirstBaseUrl = true;
                }
            } else if (ParserUtil.isStartTag(xpp, "ContentProtection")) {
                ContentProtection contentProtection = parseContentProtection(xpp);
                if (contentProtection != null) {
                    contentProtectionsBuilder.addAdaptationSetProtection(contentProtection);
                }
            } else if (ParserUtil.isStartTag(xpp, "ContentComponent")) {
                language = checkLanguageConsistency(language, xpp.getAttributeValue((String) null, "lang"));
                contentType = checkContentTypeConsistency(contentType, parseContentType(xpp));
            } else if (ParserUtil.isStartTag(xpp, "Representation")) {
                Representation representation = parseRepresentation(xpp, baseUrl, mimeType, codecs, width, height, frameRate, audioChannels, audioSamplingRate, language, segmentBase, contentProtectionsBuilder);
                contentProtectionsBuilder.endRepresentation();
                contentType = checkContentTypeConsistency(contentType, getContentType(representation));
                arrayList.add(representation);
            } else if (ParserUtil.isStartTag(xpp, "AudioChannelConfiguration")) {
                audioChannels = parseAudioChannelConfiguration(xpp);
            } else if (ParserUtil.isStartTag(xpp, "SegmentBase")) {
                segmentBase = parseSegmentBase(xpp, baseUrl, (SegmentBase.SingleSegmentBase) segmentBase);
            } else if (ParserUtil.isStartTag(xpp, "SegmentList")) {
                segmentBase = parseSegmentList(xpp, baseUrl, (SegmentBase.SegmentList) segmentBase);
            } else if (ParserUtil.isStartTag(xpp, "SegmentTemplate")) {
                segmentBase = parseSegmentTemplate(xpp, baseUrl, (SegmentBase.SegmentTemplate) segmentBase);
            } else if (ParserUtil.isStartTag(xpp)) {
                parseAdaptationSetChild(xpp);
            }
        } while (!ParserUtil.isEndTag(xpp, "AdaptationSet"));
        return buildAdaptationSet(id, contentType, arrayList, contentProtectionsBuilder.build());
    }

    /* access modifiers changed from: protected */
    public AdaptationSet buildAdaptationSet(int id, int contentType, List<Representation> representations, List<ContentProtection> contentProtections) {
        return new AdaptationSet(id, contentType, representations, contentProtections);
    }

    /* access modifiers changed from: protected */
    public int parseContentType(XmlPullParser xpp) {
        String contentType = xpp.getAttributeValue((String) null, "contentType");
        if (TextUtils.isEmpty(contentType)) {
            return -1;
        }
        if ("audio".equals(contentType)) {
            return 1;
        }
        if ("video".equals(contentType)) {
            return 0;
        }
        if ("text".equals(contentType)) {
            return 2;
        }
        return -1;
    }

    /* access modifiers changed from: protected */
    public int getContentType(Representation representation) {
        String mimeType = representation.format.mimeType;
        if (TextUtils.isEmpty(mimeType)) {
            return -1;
        }
        if (MimeTypes.isVideo(mimeType)) {
            return 0;
        }
        if (MimeTypes.isAudio(mimeType)) {
            return 1;
        }
        if (MimeTypes.isText(mimeType) || MimeTypes.APPLICATION_TTML.equals(mimeType)) {
            return 2;
        }
        if (!MimeTypes.APPLICATION_MP4.equals(mimeType)) {
            return -1;
        }
        String codecs = representation.format.codecs;
        if (XMLSubtitleSampleEntry.TYPE.equals(codecs) || WebVTTSampleEntry.TYPE.equals(codecs)) {
            return 2;
        }
        return -1;
    }

    /* access modifiers changed from: protected */
    public ContentProtection parseContentProtection(XmlPullParser xpp) throws XmlPullParserException, IOException {
        String schemeIdUri = xpp.getAttributeValue((String) null, "schemeIdUri");
        UUID uuid = null;
        DrmInitData.SchemeInitData data = null;
        boolean seenPsshElement = false;
        do {
            xpp.next();
            if (ParserUtil.isStartTag(xpp, "cenc:pssh") && xpp.next() == 4) {
                seenPsshElement = true;
                data = new DrmInitData.SchemeInitData(MimeTypes.VIDEO_MP4, Base64.decode(xpp.getText(), 0));
                uuid = PsshAtomUtil.parseUuid(data.data);
            }
        } while (!ParserUtil.isEndTag(xpp, "ContentProtection"));
        if (!seenPsshElement || uuid != null) {
            return buildContentProtection(schemeIdUri, uuid, data);
        }
        Log.w(TAG, "Skipped unsupported ContentProtection element");
        return null;
    }

    /* access modifiers changed from: protected */
    public ContentProtection buildContentProtection(String schemeIdUri, UUID uuid, DrmInitData.SchemeInitData data) {
        return new ContentProtection(schemeIdUri, uuid, data);
    }

    /* access modifiers changed from: protected */
    public void parseAdaptationSetChild(XmlPullParser xpp) throws XmlPullParserException, IOException {
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r30v8, resolved type: com.google.android.exoplayer.dash.mpd.SegmentBase} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r30v12, resolved type: com.google.android.exoplayer.dash.mpd.SegmentBase} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r30v13, resolved type: com.google.android.exoplayer.dash.mpd.SegmentBase} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r30v14, resolved type: com.google.android.exoplayer.dash.mpd.SegmentBase} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r30v15, resolved type: com.google.android.exoplayer.dash.mpd.SegmentBase} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r30v16, resolved type: com.google.android.exoplayer.dash.mpd.SegmentBase} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r30v17, resolved type: com.google.android.exoplayer.dash.mpd.SegmentBase} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r30v18, resolved type: com.google.android.exoplayer.dash.mpd.SegmentBase$SegmentTemplate} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r30v19, resolved type: com.google.android.exoplayer.dash.mpd.SegmentBase$SegmentList} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r30v20, resolved type: com.google.android.exoplayer.dash.mpd.SegmentBase$SingleSegmentBase} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r30v22, resolved type: com.google.android.exoplayer.dash.mpd.SegmentBase$SingleSegmentBase} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v22, resolved type: com.google.android.exoplayer.dash.mpd.SegmentBase$SingleSegmentBase} */
    /* JADX WARNING: type inference failed for: r30v10 */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x009f, code lost:
        if (com.google.android.exoplayer.util.ParserUtil.isStartTag(r20, "AudioChannelConfiguration") == false) goto L_0x00a6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x00a1, code lost:
        r10 = parseAudioChannelConfiguration(r20);
        r30 = r30;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x00af, code lost:
        if (com.google.android.exoplayer.util.ParserUtil.isStartTag(r20, "SegmentBase") == false) goto L_0x00c0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x00b1, code lost:
        r30 = parseSegmentBase(r20, r21, (com.google.android.exoplayer.dash.mpd.SegmentBase.SingleSegmentBase) r30);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x00c9, code lost:
        if (com.google.android.exoplayer.util.ParserUtil.isStartTag(r20, "SegmentList") == false) goto L_0x00da;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x00cb, code lost:
        r30 = parseSegmentList(r20, r21, (com.google.android.exoplayer.dash.mpd.SegmentBase.SegmentList) r30);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x00e3, code lost:
        if (com.google.android.exoplayer.util.ParserUtil.isStartTag(r20, "SegmentTemplate") == false) goto L_0x00f5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x00e5, code lost:
        r30 = parseSegmentTemplate(r20, r21, (com.google.android.exoplayer.dash.mpd.SegmentBase.SegmentTemplate) r30);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x00f5, code lost:
        r30 = r30;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x00fe, code lost:
        if (com.google.android.exoplayer.util.ParserUtil.isStartTag(r20, "ContentProtection") == false) goto L_0x0070;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0100, code lost:
        r15 = parseContentProtection(r20);
        r30 = r30;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0104, code lost:
        if (r15 == null) goto L_0x0070;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0106, code lost:
        r31.addAdaptationSetProtection(r15);
        r30 = r30;
     */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.google.android.exoplayer.dash.mpd.Representation parseRepresentation(org.xmlpull.v1.XmlPullParser r20, java.lang.String r21, java.lang.String r22, java.lang.String r23, int r24, int r25, float r26, int r27, int r28, java.lang.String r29, com.google.android.exoplayer.dash.mpd.SegmentBase r30, com.google.android.exoplayer.dash.mpd.MediaPresentationDescriptionParser.ContentProtectionsBuilder r31) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            r19 = this;
            r4 = 0
            java.lang.String r18 = "id"
            r0 = r20
            r1 = r18
            java.lang.String r5 = r0.getAttributeValue(r4, r1)
            java.lang.String r4 = "bandwidth"
            r0 = r20
            int r12 = parseInt(r0, r4)
            java.lang.String r4 = "mimeType"
            r0 = r20
            r1 = r22
            java.lang.String r6 = parseString(r0, r4, r1)
            java.lang.String r4 = "codecs"
            r0 = r20
            r1 = r23
            java.lang.String r14 = parseString(r0, r4, r1)
            java.lang.String r4 = "width"
            r0 = r20
            r1 = r24
            int r7 = parseInt(r0, r4, r1)
            java.lang.String r4 = "height"
            r0 = r20
            r1 = r25
            int r8 = parseInt(r0, r4, r1)
            r0 = r20
            r1 = r26
            float r9 = parseFrameRate(r0, r1)
            r10 = r27
            java.lang.String r4 = "audioSamplingRate"
            r0 = r20
            r1 = r28
            int r11 = parseInt(r0, r4, r1)
            r13 = r29
            r17 = 0
        L_0x005a:
            r20.next()
            java.lang.String r4 = "BaseURL"
            r0 = r20
            boolean r4 = com.google.android.exoplayer.util.ParserUtil.isStartTag(r0, r4)
            if (r4 == 0) goto L_0x0096
            if (r17 != 0) goto L_0x0070
            java.lang.String r21 = parseBaseUrl(r20, r21)
            r17 = 1
        L_0x0070:
            java.lang.String r4 = "Representation"
            r0 = r20
            boolean r4 = com.google.android.exoplayer.util.ParserUtil.isEndTag(r0, r4)
            if (r4 == 0) goto L_0x005a
            r4 = r19
            com.google.android.exoplayer.chunk.Format r16 = r4.buildFormat(r5, r6, r7, r8, r9, r10, r11, r12, r13, r14)
            r0 = r19
            java.lang.String r4 = r0.contentId
            r18 = -1
            if (r30 == 0) goto L_0x010d
        L_0x0089:
            r0 = r19
            r1 = r18
            r2 = r16
            r3 = r30
            com.google.android.exoplayer.dash.mpd.Representation r4 = r0.buildRepresentation(r4, r1, r2, r3)
            return r4
        L_0x0096:
            java.lang.String r4 = "AudioChannelConfiguration"
            r0 = r20
            boolean r4 = com.google.android.exoplayer.util.ParserUtil.isStartTag(r0, r4)
            if (r4 == 0) goto L_0x00a6
            int r10 = r19.parseAudioChannelConfiguration(r20)
            goto L_0x0070
        L_0x00a6:
            java.lang.String r4 = "SegmentBase"
            r0 = r20
            boolean r4 = com.google.android.exoplayer.util.ParserUtil.isStartTag(r0, r4)
            if (r4 == 0) goto L_0x00c0
            com.google.android.exoplayer.dash.mpd.SegmentBase$SingleSegmentBase r30 = (com.google.android.exoplayer.dash.mpd.SegmentBase.SingleSegmentBase) r30
            r0 = r19
            r1 = r20
            r2 = r21
            r3 = r30
            com.google.android.exoplayer.dash.mpd.SegmentBase$SingleSegmentBase r30 = r0.parseSegmentBase(r1, r2, r3)
            goto L_0x0070
        L_0x00c0:
            java.lang.String r4 = "SegmentList"
            r0 = r20
            boolean r4 = com.google.android.exoplayer.util.ParserUtil.isStartTag(r0, r4)
            if (r4 == 0) goto L_0x00da
            com.google.android.exoplayer.dash.mpd.SegmentBase$SegmentList r30 = (com.google.android.exoplayer.dash.mpd.SegmentBase.SegmentList) r30
            r0 = r19
            r1 = r20
            r2 = r21
            r3 = r30
            com.google.android.exoplayer.dash.mpd.SegmentBase$SegmentList r30 = r0.parseSegmentList(r1, r2, r3)
            goto L_0x0070
        L_0x00da:
            java.lang.String r4 = "SegmentTemplate"
            r0 = r20
            boolean r4 = com.google.android.exoplayer.util.ParserUtil.isStartTag(r0, r4)
            if (r4 == 0) goto L_0x00f5
            com.google.android.exoplayer.dash.mpd.SegmentBase$SegmentTemplate r30 = (com.google.android.exoplayer.dash.mpd.SegmentBase.SegmentTemplate) r30
            r0 = r19
            r1 = r20
            r2 = r21
            r3 = r30
            com.google.android.exoplayer.dash.mpd.SegmentBase$SegmentTemplate r30 = r0.parseSegmentTemplate(r1, r2, r3)
            goto L_0x0070
        L_0x00f5:
            java.lang.String r4 = "ContentProtection"
            r0 = r20
            boolean r4 = com.google.android.exoplayer.util.ParserUtil.isStartTag(r0, r4)
            if (r4 == 0) goto L_0x0070
            com.google.android.exoplayer.dash.mpd.ContentProtection r15 = r19.parseContentProtection(r20)
            if (r15 == 0) goto L_0x0070
            r0 = r31
            r0.addAdaptationSetProtection(r15)
            goto L_0x0070
        L_0x010d:
            com.google.android.exoplayer.dash.mpd.SegmentBase$SingleSegmentBase r30 = new com.google.android.exoplayer.dash.mpd.SegmentBase$SingleSegmentBase
            r0 = r30
            r1 = r21
            r0.<init>(r1)
            goto L_0x0089
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer.dash.mpd.MediaPresentationDescriptionParser.parseRepresentation(org.xmlpull.v1.XmlPullParser, java.lang.String, java.lang.String, java.lang.String, int, int, float, int, int, java.lang.String, com.google.android.exoplayer.dash.mpd.SegmentBase, com.google.android.exoplayer.dash.mpd.MediaPresentationDescriptionParser$ContentProtectionsBuilder):com.google.android.exoplayer.dash.mpd.Representation");
    }

    /* access modifiers changed from: protected */
    public Format buildFormat(String id, String mimeType, int width, int height, float frameRate, int audioChannels, int audioSamplingRate, int bandwidth, String language, String codecs) {
        return new Format(id, mimeType, width, height, frameRate, audioChannels, audioSamplingRate, bandwidth, language, codecs);
    }

    /* access modifiers changed from: protected */
    public Representation buildRepresentation(String contentId2, int revisionId, Format format, SegmentBase segmentBase) {
        return Representation.newInstance(contentId2, (long) revisionId, format, segmentBase);
    }

    /* access modifiers changed from: protected */
    public SegmentBase.SingleSegmentBase parseSegmentBase(XmlPullParser xpp, String baseUrl, SegmentBase.SingleSegmentBase parent) throws XmlPullParserException, IOException {
        long timescale = parseLong(xpp, "timescale", parent != null ? parent.timescale : 1);
        long presentationTimeOffset = parseLong(xpp, "presentationTimeOffset", parent != null ? parent.presentationTimeOffset : 0);
        long indexStart = parent != null ? parent.indexStart : 0;
        long indexLength = parent != null ? parent.indexLength : -1;
        String indexRangeText = xpp.getAttributeValue((String) null, "indexRange");
        if (indexRangeText != null) {
            String[] indexRange = indexRangeText.split(Constants.FILENAME_SEQUENCE_SEPARATOR);
            indexStart = Long.parseLong(indexRange[0]);
            indexLength = (Long.parseLong(indexRange[1]) - indexStart) + 1;
        }
        RangedUri initialization = parent != null ? parent.initialization : null;
        do {
            xpp.next();
            if (ParserUtil.isStartTag(xpp, "Initialization")) {
                initialization = parseInitialization(xpp, baseUrl);
            }
        } while (!ParserUtil.isEndTag(xpp, "SegmentBase"));
        return buildSingleSegmentBase(initialization, timescale, presentationTimeOffset, baseUrl, indexStart, indexLength);
    }

    /* access modifiers changed from: protected */
    public SegmentBase.SingleSegmentBase buildSingleSegmentBase(RangedUri initialization, long timescale, long presentationTimeOffset, String baseUrl, long indexStart, long indexLength) {
        return new SegmentBase.SingleSegmentBase(initialization, timescale, presentationTimeOffset, baseUrl, indexStart, indexLength);
    }

    /* access modifiers changed from: protected */
    public SegmentBase.SegmentList parseSegmentList(XmlPullParser xpp, String baseUrl, SegmentBase.SegmentList parent) throws XmlPullParserException, IOException {
        long timescale = parseLong(xpp, "timescale", parent != null ? parent.timescale : 1);
        long presentationTimeOffset = parseLong(xpp, "presentationTimeOffset", parent != null ? parent.presentationTimeOffset : 0);
        long duration = parseLong(xpp, "duration", parent != null ? parent.duration : -1);
        int startNumber = parseInt(xpp, "startNumber", parent != null ? parent.startNumber : 1);
        RangedUri initialization = null;
        List<SegmentBase.SegmentTimelineElement> timeline = null;
        List<RangedUri> segments = null;
        do {
            xpp.next();
            if (ParserUtil.isStartTag(xpp, "Initialization")) {
                initialization = parseInitialization(xpp, baseUrl);
            } else if (ParserUtil.isStartTag(xpp, "SegmentTimeline")) {
                timeline = parseSegmentTimeline(xpp);
            } else if (ParserUtil.isStartTag(xpp, "SegmentURL")) {
                if (segments == null) {
                    segments = new ArrayList<>();
                }
                segments.add(parseSegmentUrl(xpp, baseUrl));
            }
        } while (!ParserUtil.isEndTag(xpp, "SegmentList"));
        if (parent != null) {
            if (initialization == null) {
                initialization = parent.initialization;
            }
            if (timeline == null) {
                timeline = parent.segmentTimeline;
            }
            if (segments == null) {
                segments = parent.mediaSegments;
            }
        }
        return buildSegmentList(initialization, timescale, presentationTimeOffset, startNumber, duration, timeline, segments);
    }

    /* access modifiers changed from: protected */
    public SegmentBase.SegmentList buildSegmentList(RangedUri initialization, long timescale, long presentationTimeOffset, int startNumber, long duration, List<SegmentBase.SegmentTimelineElement> timeline, List<RangedUri> segments) {
        return new SegmentBase.SegmentList(initialization, timescale, presentationTimeOffset, startNumber, duration, timeline, segments);
    }

    /* access modifiers changed from: protected */
    public SegmentBase.SegmentTemplate parseSegmentTemplate(XmlPullParser xpp, String baseUrl, SegmentBase.SegmentTemplate parent) throws XmlPullParserException, IOException {
        long timescale = parseLong(xpp, "timescale", parent != null ? parent.timescale : 1);
        long presentationTimeOffset = parseLong(xpp, "presentationTimeOffset", parent != null ? parent.presentationTimeOffset : 0);
        long duration = parseLong(xpp, "duration", parent != null ? parent.duration : -1);
        int startNumber = parseInt(xpp, "startNumber", parent != null ? parent.startNumber : 1);
        UrlTemplate mediaTemplate = parseUrlTemplate(xpp, "media", parent != null ? parent.mediaTemplate : null);
        UrlTemplate initializationTemplate = parseUrlTemplate(xpp, JoinPoint.INITIALIZATION, parent != null ? parent.initializationTemplate : null);
        RangedUri initialization = null;
        List<SegmentBase.SegmentTimelineElement> timeline = null;
        do {
            xpp.next();
            if (ParserUtil.isStartTag(xpp, "Initialization")) {
                initialization = parseInitialization(xpp, baseUrl);
            } else if (ParserUtil.isStartTag(xpp, "SegmentTimeline")) {
                timeline = parseSegmentTimeline(xpp);
            }
        } while (!ParserUtil.isEndTag(xpp, "SegmentTemplate"));
        if (parent != null) {
            if (initialization == null) {
                initialization = parent.initialization;
            }
            if (timeline == null) {
                timeline = parent.segmentTimeline;
            }
        }
        return buildSegmentTemplate(initialization, timescale, presentationTimeOffset, startNumber, duration, timeline, initializationTemplate, mediaTemplate, baseUrl);
    }

    /* access modifiers changed from: protected */
    public SegmentBase.SegmentTemplate buildSegmentTemplate(RangedUri initialization, long timescale, long presentationTimeOffset, int startNumber, long duration, List<SegmentBase.SegmentTimelineElement> timeline, UrlTemplate initializationTemplate, UrlTemplate mediaTemplate, String baseUrl) {
        return new SegmentBase.SegmentTemplate(initialization, timescale, presentationTimeOffset, startNumber, duration, timeline, initializationTemplate, mediaTemplate, baseUrl);
    }

    /* access modifiers changed from: protected */
    public List<SegmentBase.SegmentTimelineElement> parseSegmentTimeline(XmlPullParser xpp) throws XmlPullParserException, IOException {
        List<SegmentBase.SegmentTimelineElement> segmentTimeline = new ArrayList<>();
        long elapsedTime = 0;
        do {
            xpp.next();
            if (ParserUtil.isStartTag(xpp, "S")) {
                elapsedTime = parseLong(xpp, "t", elapsedTime);
                long duration = parseLong(xpp, "d");
                int count = parseInt(xpp, "r", 0) + 1;
                for (int i = 0; i < count; i++) {
                    segmentTimeline.add(buildSegmentTimelineElement(elapsedTime, duration));
                    elapsedTime += duration;
                }
            }
        } while (!ParserUtil.isEndTag(xpp, "SegmentTimeline"));
        return segmentTimeline;
    }

    /* access modifiers changed from: protected */
    public SegmentBase.SegmentTimelineElement buildSegmentTimelineElement(long elapsedTime, long duration) {
        return new SegmentBase.SegmentTimelineElement(elapsedTime, duration);
    }

    /* access modifiers changed from: protected */
    public UrlTemplate parseUrlTemplate(XmlPullParser xpp, String name, UrlTemplate defaultValue) {
        String valueString = xpp.getAttributeValue((String) null, name);
        if (valueString != null) {
            return UrlTemplate.compile(valueString);
        }
        return defaultValue;
    }

    /* access modifiers changed from: protected */
    public RangedUri parseInitialization(XmlPullParser xpp, String baseUrl) {
        return parseRangedUrl(xpp, baseUrl, "sourceURL", "range");
    }

    /* access modifiers changed from: protected */
    public RangedUri parseSegmentUrl(XmlPullParser xpp, String baseUrl) {
        return parseRangedUrl(xpp, baseUrl, "media", "mediaRange");
    }

    /* access modifiers changed from: protected */
    public RangedUri parseRangedUrl(XmlPullParser xpp, String baseUrl, String urlAttribute, String rangeAttribute) {
        String urlText = xpp.getAttributeValue((String) null, urlAttribute);
        long rangeStart = 0;
        long rangeLength = -1;
        String rangeText = xpp.getAttributeValue((String) null, rangeAttribute);
        if (rangeText != null) {
            String[] rangeTextArray = rangeText.split(Constants.FILENAME_SEQUENCE_SEPARATOR);
            rangeStart = Long.parseLong(rangeTextArray[0]);
            if (rangeTextArray.length == 2) {
                rangeLength = (Long.parseLong(rangeTextArray[1]) - rangeStart) + 1;
            }
        }
        return buildRangedUri(baseUrl, urlText, rangeStart, rangeLength);
    }

    /* access modifiers changed from: protected */
    public RangedUri buildRangedUri(String baseUrl, String urlText, long rangeStart, long rangeLength) {
        return new RangedUri(baseUrl, urlText, rangeStart, rangeLength);
    }

    /* access modifiers changed from: protected */
    public int parseAudioChannelConfiguration(XmlPullParser xpp) throws XmlPullParserException, IOException {
        int audioChannels;
        if ("urn:mpeg:dash:23003:3:audio_channel_configuration:2011".equals(parseString(xpp, "schemeIdUri", (String) null))) {
            audioChannels = parseInt(xpp, "value");
        } else {
            audioChannels = -1;
        }
        do {
            xpp.next();
        } while (!ParserUtil.isEndTag(xpp, "AudioChannelConfiguration"));
        return audioChannels;
    }

    private static String checkLanguageConsistency(String firstLanguage, String secondLanguage) {
        if (firstLanguage == null) {
            return secondLanguage;
        }
        if (secondLanguage == null) {
            return firstLanguage;
        }
        Assertions.checkState(firstLanguage.equals(secondLanguage));
        return firstLanguage;
    }

    private static int checkContentTypeConsistency(int firstType, int secondType) {
        if (firstType == -1) {
            return secondType;
        }
        if (secondType == -1) {
            return firstType;
        }
        Assertions.checkState(firstType == secondType);
        return firstType;
    }

    protected static float parseFrameRate(XmlPullParser xpp, float defaultValue) {
        float frameRate = defaultValue;
        String frameRateAttribute = xpp.getAttributeValue((String) null, "frameRate");
        if (frameRateAttribute == null) {
            return frameRate;
        }
        Matcher frameRateMatcher = FRAME_RATE_PATTERN.matcher(frameRateAttribute);
        if (!frameRateMatcher.matches()) {
            return frameRate;
        }
        int numerator = Integer.parseInt(frameRateMatcher.group(1));
        String denominatorString = frameRateMatcher.group(2);
        if (!TextUtils.isEmpty(denominatorString)) {
            return ((float) numerator) / ((float) Integer.parseInt(denominatorString));
        }
        return (float) numerator;
    }

    protected static long parseDuration(XmlPullParser xpp, String name, long defaultValue) {
        String value = xpp.getAttributeValue((String) null, name);
        return value == null ? defaultValue : Util.parseXsDuration(value);
    }

    protected static long parseDateTime(XmlPullParser xpp, String name, long defaultValue) throws ParseException {
        String value = xpp.getAttributeValue((String) null, name);
        return value == null ? defaultValue : Util.parseXsDateTime(value);
    }

    protected static String parseBaseUrl(XmlPullParser xpp, String parentBaseUrl) throws XmlPullParserException, IOException {
        xpp.next();
        return UriUtil.resolve(parentBaseUrl, xpp.getText());
    }

    protected static int parseInt(XmlPullParser xpp, String name) {
        return parseInt(xpp, name, -1);
    }

    protected static int parseInt(XmlPullParser xpp, String name, int defaultValue) {
        String value = xpp.getAttributeValue((String) null, name);
        return value == null ? defaultValue : Integer.parseInt(value);
    }

    protected static long parseLong(XmlPullParser xpp, String name) {
        return parseLong(xpp, name, -1);
    }

    protected static long parseLong(XmlPullParser xpp, String name, long defaultValue) {
        String value = xpp.getAttributeValue((String) null, name);
        return value == null ? defaultValue : Long.parseLong(value);
    }

    protected static String parseString(XmlPullParser xpp, String name, String defaultValue) {
        String value = xpp.getAttributeValue((String) null, name);
        return value == null ? defaultValue : value;
    }

    protected static final class ContentProtectionsBuilder implements Comparator<ContentProtection> {
        private ArrayList<ContentProtection> adaptationSetProtections;
        private ArrayList<ContentProtection> currentRepresentationProtections;
        private ArrayList<ContentProtection> representationProtections;
        private boolean representationProtectionsSet;

        protected ContentProtectionsBuilder() {
        }

        public void addAdaptationSetProtection(ContentProtection contentProtection) {
            if (this.adaptationSetProtections == null) {
                this.adaptationSetProtections = new ArrayList<>();
            }
            maybeAddContentProtection(this.adaptationSetProtections, contentProtection);
        }

        public void addRepresentationProtection(ContentProtection contentProtection) {
            if (this.currentRepresentationProtections == null) {
                this.currentRepresentationProtections = new ArrayList<>();
            }
            maybeAddContentProtection(this.currentRepresentationProtections, contentProtection);
        }

        public void endRepresentation() {
            boolean z = true;
            if (!this.representationProtectionsSet) {
                if (this.currentRepresentationProtections != null) {
                    Collections.sort(this.currentRepresentationProtections, this);
                }
                this.representationProtections = this.currentRepresentationProtections;
                this.representationProtectionsSet = true;
            } else if (this.currentRepresentationProtections == null) {
                if (this.representationProtections != null) {
                    z = false;
                }
                Assertions.checkState(z);
            } else {
                Collections.sort(this.currentRepresentationProtections, this);
                Assertions.checkState(this.currentRepresentationProtections.equals(this.representationProtections));
            }
            this.currentRepresentationProtections = null;
        }

        public ArrayList<ContentProtection> build() {
            if (this.adaptationSetProtections == null) {
                return this.representationProtections;
            }
            if (this.representationProtections == null) {
                return this.adaptationSetProtections;
            }
            for (int i = 0; i < this.representationProtections.size(); i++) {
                maybeAddContentProtection(this.adaptationSetProtections, this.representationProtections.get(i));
            }
            return this.adaptationSetProtections;
        }

        private void maybeAddContentProtection(List<ContentProtection> contentProtections, ContentProtection contentProtection) {
            if (!contentProtections.contains(contentProtection)) {
                for (int i = 0; i < contentProtections.size(); i++) {
                    Assertions.checkState(!contentProtections.get(i).schemeUriId.equals(contentProtection.schemeUriId));
                }
                contentProtections.add(contentProtection);
            }
        }

        public int compare(ContentProtection first, ContentProtection second) {
            return first.schemeUriId.compareTo(second.schemeUriId);
        }
    }
}
