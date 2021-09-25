package com.google.android.exoplayer.text.ttml;

import android.util.Log;
import android.util.Pair;
import com.google.android.exoplayer.ParserException;
import com.google.android.exoplayer.text.SubtitleParser;
import com.google.android.exoplayer.util.MimeTypes;
import com.google.android.exoplayer.util.ParserUtil;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public final class TtmlParser implements SubtitleParser {
    private static final String ATTR_BEGIN = "begin";
    private static final String ATTR_DURATION = "dur";
    private static final String ATTR_END = "end";
    private static final String ATTR_REGION = "region";
    private static final String ATTR_STYLE = "style";
    private static final Pattern CLOCK_TIME = Pattern.compile("^([0-9][0-9]+):([0-9][0-9]):([0-9][0-9])(?:(\\.[0-9]+)|:([0-9][0-9])(?:\\.([0-9]+))?)?$");
    private static final FrameAndTickRate DEFAULT_FRAME_AND_TICK_RATE = new FrameAndTickRate(30.0f, 1, 1);
    private static final int DEFAULT_FRAME_RATE = 30;
    private static final Pattern FONT_SIZE = Pattern.compile("^(([0-9]*.)?[0-9]+)(px|em|%)$");
    private static final Pattern OFFSET_TIME = Pattern.compile("^([0-9]+(?:\\.[0-9]+)?)(h|m|s|ms|f|t)$");
    private static final Pattern PERCENTAGE_COORDINATES = Pattern.compile("^(\\d+\\.?\\d*?)% (\\d+\\.?\\d*?)%$");
    private static final String TAG = "TtmlParser";
    private static final String TTP = "http://www.w3.org/ns/ttml#parameter";
    private final XmlPullParserFactory xmlParserFactory;

    public TtmlParser() {
        try {
            this.xmlParserFactory = XmlPullParserFactory.newInstance();
            this.xmlParserFactory.setNamespaceAware(true);
        } catch (XmlPullParserException e) {
            throw new RuntimeException("Couldn't create XmlPullParserFactory instance", e);
        }
    }

    public boolean canParse(String mimeType) {
        return MimeTypes.APPLICATION_TTML.equals(mimeType);
    }

    public TtmlSubtitle parse(byte[] bytes, int offset, int length) throws ParserException {
        try {
            XmlPullParser xmlParser = this.xmlParserFactory.newPullParser();
            Map<String, TtmlStyle> globalStyles = new HashMap<>();
            Map<String, TtmlRegion> regionMap = new HashMap<>();
            regionMap.put("", new TtmlRegion());
            xmlParser.setInput(new ByteArrayInputStream(bytes, offset, length), (String) null);
            TtmlSubtitle ttmlSubtitle = null;
            LinkedList<TtmlNode> nodeStack = new LinkedList<>();
            int unsupportedNodeDepth = 0;
            FrameAndTickRate frameAndTickRate = DEFAULT_FRAME_AND_TICK_RATE;
            for (int eventType = xmlParser.getEventType(); eventType != 1; eventType = xmlParser.getEventType()) {
                TtmlNode parent = nodeStack.peekLast();
                if (unsupportedNodeDepth == 0) {
                    String name = xmlParser.getName();
                    if (eventType == 2) {
                        if (TtmlNode.TAG_TT.equals(name)) {
                            frameAndTickRate = parseFrameAndTickRates(xmlParser);
                        }
                        if (!isSupportedTag(name)) {
                            Log.i(TAG, "Ignoring unsupported tag: " + xmlParser.getName());
                            unsupportedNodeDepth++;
                        } else if ("head".equals(name)) {
                            parseHeader(xmlParser, globalStyles, regionMap);
                        } else {
                            try {
                                TtmlNode node = parseNode(xmlParser, parent, regionMap, frameAndTickRate);
                                nodeStack.addLast(node);
                                if (parent != null) {
                                    parent.addChild(node);
                                }
                            } catch (ParserException e) {
                                Log.w(TAG, "Suppressing parser error", e);
                                unsupportedNodeDepth++;
                            }
                        }
                    } else if (eventType == 4) {
                        parent.addChild(TtmlNode.buildTextNode(xmlParser.getText()));
                    } else if (eventType == 3) {
                        if (xmlParser.getName().equals(TtmlNode.TAG_TT)) {
                            ttmlSubtitle = new TtmlSubtitle(nodeStack.getLast(), globalStyles, regionMap);
                        }
                        nodeStack.removeLast();
                    }
                } else if (eventType == 2) {
                    unsupportedNodeDepth++;
                } else if (eventType == 3) {
                    unsupportedNodeDepth--;
                }
                xmlParser.next();
            }
            return ttmlSubtitle;
        } catch (XmlPullParserException xppe) {
            throw new ParserException("Unable to parse source", xppe);
        } catch (IOException e2) {
            throw new IllegalStateException("Unexpected error when reading input.", e2);
        }
    }

    private FrameAndTickRate parseFrameAndTickRates(XmlPullParser xmlParser) throws ParserException {
        int frameRate = 30;
        String frameRateStr = xmlParser.getAttributeValue(TTP, "frameRate");
        if (frameRateStr != null) {
            frameRate = Integer.parseInt(frameRateStr);
        }
        float frameRateMultiplier = 1.0f;
        String frameRateMultiplierStr = xmlParser.getAttributeValue(TTP, "frameRateMultiplier");
        if (frameRateMultiplierStr != null) {
            String[] parts = frameRateMultiplierStr.split(" ");
            if (parts.length != 2) {
                throw new ParserException("frameRateMultiplier doesn't have 2 parts");
            }
            frameRateMultiplier = ((float) Integer.parseInt(parts[0])) / ((float) Integer.parseInt(parts[1]));
        }
        int subFrameRate = DEFAULT_FRAME_AND_TICK_RATE.subFrameRate;
        String subFrameRateStr = xmlParser.getAttributeValue(TTP, "subFrameRate");
        if (subFrameRateStr != null) {
            subFrameRate = Integer.parseInt(subFrameRateStr);
        }
        int tickRate = DEFAULT_FRAME_AND_TICK_RATE.tickRate;
        String tickRateStr = xmlParser.getAttributeValue(TTP, "tickRate");
        if (tickRateStr != null) {
            tickRate = Integer.parseInt(tickRateStr);
        }
        return new FrameAndTickRate(((float) frameRate) * frameRateMultiplier, subFrameRate, tickRate);
    }

    private Map<String, TtmlStyle> parseHeader(XmlPullParser xmlParser, Map<String, TtmlStyle> globalStyles, Map<String, TtmlRegion> globalRegions) throws IOException, XmlPullParserException {
        Pair<String, TtmlRegion> ttmlRegionInfo;
        do {
            xmlParser.next();
            if (ParserUtil.isStartTag(xmlParser, "style")) {
                String parentStyleId = ParserUtil.getAttributeValue(xmlParser, "style");
                TtmlStyle style = parseStyleAttributes(xmlParser, new TtmlStyle());
                if (parentStyleId != null) {
                    String[] ids = parseStyleIds(parentStyleId);
                    for (String str : ids) {
                        style.chain(globalStyles.get(str));
                    }
                }
                if (style.getId() != null) {
                    globalStyles.put(style.getId(), style);
                }
            } else if (ParserUtil.isStartTag(xmlParser, "region") && (ttmlRegionInfo = parseRegionAttributes(xmlParser)) != null) {
                globalRegions.put(ttmlRegionInfo.first, ttmlRegionInfo.second);
            }
        } while (!ParserUtil.isEndTag(xmlParser, "head"));
        return globalStyles;
    }

    private Pair<String, TtmlRegion> parseRegionAttributes(XmlPullParser xmlParser) {
        String regionId = ParserUtil.getAttributeValue(xmlParser, "id");
        String regionOrigin = ParserUtil.getAttributeValue(xmlParser, TtmlNode.ATTR_TTS_ORIGIN);
        String regionExtent = ParserUtil.getAttributeValue(xmlParser, TtmlNode.ATTR_TTS_EXTENT);
        if (regionOrigin == null || regionId == null) {
            return null;
        }
        float position = Float.MIN_VALUE;
        float line = Float.MIN_VALUE;
        Matcher originMatcher = PERCENTAGE_COORDINATES.matcher(regionOrigin);
        if (originMatcher.matches()) {
            try {
                position = Float.parseFloat(originMatcher.group(1)) / 100.0f;
                line = Float.parseFloat(originMatcher.group(2)) / 100.0f;
            } catch (NumberFormatException e) {
                Log.w(TAG, "Ignoring region with malformed origin: '" + regionOrigin + "'", e);
                position = Float.MIN_VALUE;
            }
        }
        float width = Float.MIN_VALUE;
        if (regionExtent != null) {
            Matcher extentMatcher = PERCENTAGE_COORDINATES.matcher(regionExtent);
            if (extentMatcher.matches()) {
                try {
                    width = Float.parseFloat(extentMatcher.group(1)) / 100.0f;
                } catch (NumberFormatException e2) {
                    Log.w(TAG, "Ignoring malformed region extent: '" + regionExtent + "'", e2);
                }
            }
        }
        if (position != Float.MIN_VALUE) {
            return new Pair<>(regionId, new TtmlRegion(position, line, 0, width));
        }
        return null;
    }

    private String[] parseStyleIds(String parentStyleIds) {
        return parentStyleIds.split("\\s+");
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.google.android.exoplayer.text.ttml.TtmlStyle parseStyleAttributes(org.xmlpull.v1.XmlPullParser r13, com.google.android.exoplayer.text.ttml.TtmlStyle r14) {
        /*
            r12 = this;
            r9 = 3
            r8 = 2
            r6 = -1
            r7 = 1
            r5 = 0
            int r0 = r13.getAttributeCount()
            r3 = 0
        L_0x000a:
            if (r3 >= r0) goto L_0x0240
            java.lang.String r1 = r13.getAttributeValue(r3)
            java.lang.String r4 = r13.getAttributeName(r3)
            int r10 = r4.hashCode()
            switch(r10) {
                case -1550943582: goto L_0x0064;
                case -1224696685: goto L_0x0043;
                case -1065511464: goto L_0x006f;
                case -879295043: goto L_0x007a;
                case -734428249: goto L_0x0059;
                case 3355: goto L_0x0022;
                case 94842723: goto L_0x0038;
                case 365601008: goto L_0x004e;
                case 1287124693: goto L_0x002d;
                default: goto L_0x001b;
            }
        L_0x001b:
            r4 = r6
        L_0x001c:
            switch(r4) {
                case 0: goto L_0x0086;
                case 1: goto L_0x009c;
                case 2: goto L_0x00cd;
                case 3: goto L_0x00fe;
                case 4: goto L_0x0108;
                case 5: goto L_0x0135;
                case 6: goto L_0x0146;
                case 7: goto L_0x0157;
                case 8: goto L_0x01db;
                default: goto L_0x001f;
            }
        L_0x001f:
            int r3 = r3 + 1
            goto L_0x000a
        L_0x0022:
            java.lang.String r10 = "id"
            boolean r4 = r4.equals(r10)
            if (r4 == 0) goto L_0x001b
            r4 = r5
            goto L_0x001c
        L_0x002d:
            java.lang.String r10 = "backgroundColor"
            boolean r4 = r4.equals(r10)
            if (r4 == 0) goto L_0x001b
            r4 = r7
            goto L_0x001c
        L_0x0038:
            java.lang.String r10 = "color"
            boolean r4 = r4.equals(r10)
            if (r4 == 0) goto L_0x001b
            r4 = r8
            goto L_0x001c
        L_0x0043:
            java.lang.String r10 = "fontFamily"
            boolean r4 = r4.equals(r10)
            if (r4 == 0) goto L_0x001b
            r4 = r9
            goto L_0x001c
        L_0x004e:
            java.lang.String r10 = "fontSize"
            boolean r4 = r4.equals(r10)
            if (r4 == 0) goto L_0x001b
            r4 = 4
            goto L_0x001c
        L_0x0059:
            java.lang.String r10 = "fontWeight"
            boolean r4 = r4.equals(r10)
            if (r4 == 0) goto L_0x001b
            r4 = 5
            goto L_0x001c
        L_0x0064:
            java.lang.String r10 = "fontStyle"
            boolean r4 = r4.equals(r10)
            if (r4 == 0) goto L_0x001b
            r4 = 6
            goto L_0x001c
        L_0x006f:
            java.lang.String r10 = "textAlign"
            boolean r4 = r4.equals(r10)
            if (r4 == 0) goto L_0x001b
            r4 = 7
            goto L_0x001c
        L_0x007a:
            java.lang.String r10 = "textDecoration"
            boolean r4 = r4.equals(r10)
            if (r4 == 0) goto L_0x001b
            r4 = 8
            goto L_0x001c
        L_0x0086:
            java.lang.String r4 = "style"
            java.lang.String r10 = r13.getName()
            boolean r4 = r4.equals(r10)
            if (r4 == 0) goto L_0x001f
            com.google.android.exoplayer.text.ttml.TtmlStyle r4 = r12.createIfNull(r14)
            com.google.android.exoplayer.text.ttml.TtmlStyle r14 = r4.setId(r1)
            goto L_0x001f
        L_0x009c:
            com.google.android.exoplayer.text.ttml.TtmlStyle r14 = r12.createIfNull(r14)
            int r4 = com.google.android.exoplayer.text.ttml.TtmlColorParser.parseColor(r1)     // Catch:{ IllegalArgumentException -> 0x00a9 }
            r14.setBackgroundColor(r4)     // Catch:{ IllegalArgumentException -> 0x00a9 }
            goto L_0x001f
        L_0x00a9:
            r2 = move-exception
            java.lang.String r4 = "TtmlParser"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = "failed parsing background value: '"
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.StringBuilder r10 = r10.append(r1)
            java.lang.String r11 = "'"
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.String r10 = r10.toString()
            android.util.Log.w(r4, r10)
            goto L_0x001f
        L_0x00cd:
            com.google.android.exoplayer.text.ttml.TtmlStyle r14 = r12.createIfNull(r14)
            int r4 = com.google.android.exoplayer.text.ttml.TtmlColorParser.parseColor(r1)     // Catch:{ IllegalArgumentException -> 0x00da }
            r14.setFontColor(r4)     // Catch:{ IllegalArgumentException -> 0x00da }
            goto L_0x001f
        L_0x00da:
            r2 = move-exception
            java.lang.String r4 = "TtmlParser"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = "failed parsing color value: '"
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.StringBuilder r10 = r10.append(r1)
            java.lang.String r11 = "'"
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.String r10 = r10.toString()
            android.util.Log.w(r4, r10)
            goto L_0x001f
        L_0x00fe:
            com.google.android.exoplayer.text.ttml.TtmlStyle r4 = r12.createIfNull(r14)
            com.google.android.exoplayer.text.ttml.TtmlStyle r14 = r4.setFontFamily(r1)
            goto L_0x001f
        L_0x0108:
            com.google.android.exoplayer.text.ttml.TtmlStyle r14 = r12.createIfNull(r14)     // Catch:{ ParserException -> 0x0111 }
            parseFontSize(r1, r14)     // Catch:{ ParserException -> 0x0111 }
            goto L_0x001f
        L_0x0111:
            r2 = move-exception
            java.lang.String r4 = "TtmlParser"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = "failed parsing fontSize value: '"
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.StringBuilder r10 = r10.append(r1)
            java.lang.String r11 = "'"
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.String r10 = r10.toString()
            android.util.Log.w(r4, r10)
            goto L_0x001f
        L_0x0135:
            com.google.android.exoplayer.text.ttml.TtmlStyle r4 = r12.createIfNull(r14)
            java.lang.String r10 = "bold"
            boolean r10 = r10.equalsIgnoreCase(r1)
            com.google.android.exoplayer.text.ttml.TtmlStyle r14 = r4.setBold(r10)
            goto L_0x001f
        L_0x0146:
            com.google.android.exoplayer.text.ttml.TtmlStyle r4 = r12.createIfNull(r14)
            java.lang.String r10 = "italic"
            boolean r10 = r10.equalsIgnoreCase(r1)
            com.google.android.exoplayer.text.ttml.TtmlStyle r14 = r4.setItalic(r10)
            goto L_0x001f
        L_0x0157:
            java.lang.String r4 = com.google.android.exoplayer.util.Util.toLowerInvariant(r1)
            int r10 = r4.hashCode()
            switch(r10) {
                case -1364013995: goto L_0x01a0;
                case 100571: goto L_0x0195;
                case 3317767: goto L_0x0174;
                case 108511772: goto L_0x018a;
                case 109757538: goto L_0x017f;
                default: goto L_0x0162;
            }
        L_0x0162:
            r4 = r6
        L_0x0163:
            switch(r4) {
                case 0: goto L_0x0168;
                case 1: goto L_0x01ab;
                case 2: goto L_0x01b7;
                case 3: goto L_0x01c3;
                case 4: goto L_0x01cf;
                default: goto L_0x0166;
            }
        L_0x0166:
            goto L_0x001f
        L_0x0168:
            com.google.android.exoplayer.text.ttml.TtmlStyle r4 = r12.createIfNull(r14)
            android.text.Layout$Alignment r10 = android.text.Layout.Alignment.ALIGN_NORMAL
            com.google.android.exoplayer.text.ttml.TtmlStyle r14 = r4.setTextAlign(r10)
            goto L_0x001f
        L_0x0174:
            java.lang.String r10 = "left"
            boolean r4 = r4.equals(r10)
            if (r4 == 0) goto L_0x0162
            r4 = r5
            goto L_0x0163
        L_0x017f:
            java.lang.String r10 = "start"
            boolean r4 = r4.equals(r10)
            if (r4 == 0) goto L_0x0162
            r4 = r7
            goto L_0x0163
        L_0x018a:
            java.lang.String r10 = "right"
            boolean r4 = r4.equals(r10)
            if (r4 == 0) goto L_0x0162
            r4 = r8
            goto L_0x0163
        L_0x0195:
            java.lang.String r10 = "end"
            boolean r4 = r4.equals(r10)
            if (r4 == 0) goto L_0x0162
            r4 = r9
            goto L_0x0163
        L_0x01a0:
            java.lang.String r10 = "center"
            boolean r4 = r4.equals(r10)
            if (r4 == 0) goto L_0x0162
            r4 = 4
            goto L_0x0163
        L_0x01ab:
            com.google.android.exoplayer.text.ttml.TtmlStyle r4 = r12.createIfNull(r14)
            android.text.Layout$Alignment r10 = android.text.Layout.Alignment.ALIGN_NORMAL
            com.google.android.exoplayer.text.ttml.TtmlStyle r14 = r4.setTextAlign(r10)
            goto L_0x001f
        L_0x01b7:
            com.google.android.exoplayer.text.ttml.TtmlStyle r4 = r12.createIfNull(r14)
            android.text.Layout$Alignment r10 = android.text.Layout.Alignment.ALIGN_OPPOSITE
            com.google.android.exoplayer.text.ttml.TtmlStyle r14 = r4.setTextAlign(r10)
            goto L_0x001f
        L_0x01c3:
            com.google.android.exoplayer.text.ttml.TtmlStyle r4 = r12.createIfNull(r14)
            android.text.Layout$Alignment r10 = android.text.Layout.Alignment.ALIGN_OPPOSITE
            com.google.android.exoplayer.text.ttml.TtmlStyle r14 = r4.setTextAlign(r10)
            goto L_0x001f
        L_0x01cf:
            com.google.android.exoplayer.text.ttml.TtmlStyle r4 = r12.createIfNull(r14)
            android.text.Layout$Alignment r10 = android.text.Layout.Alignment.ALIGN_CENTER
            com.google.android.exoplayer.text.ttml.TtmlStyle r14 = r4.setTextAlign(r10)
            goto L_0x001f
        L_0x01db:
            java.lang.String r4 = com.google.android.exoplayer.util.Util.toLowerInvariant(r1)
            int r10 = r4.hashCode()
            switch(r10) {
                case -1461280213: goto L_0x0217;
                case -1026963764: goto L_0x020c;
                case 913457136: goto L_0x0201;
                case 1679736913: goto L_0x01f6;
                default: goto L_0x01e6;
            }
        L_0x01e6:
            r4 = r6
        L_0x01e7:
            switch(r4) {
                case 0: goto L_0x01ec;
                case 1: goto L_0x0222;
                case 2: goto L_0x022c;
                case 3: goto L_0x0236;
                default: goto L_0x01ea;
            }
        L_0x01ea:
            goto L_0x001f
        L_0x01ec:
            com.google.android.exoplayer.text.ttml.TtmlStyle r4 = r12.createIfNull(r14)
            com.google.android.exoplayer.text.ttml.TtmlStyle r14 = r4.setLinethrough(r7)
            goto L_0x001f
        L_0x01f6:
            java.lang.String r10 = "linethrough"
            boolean r4 = r4.equals(r10)
            if (r4 == 0) goto L_0x01e6
            r4 = r5
            goto L_0x01e7
        L_0x0201:
            java.lang.String r10 = "nolinethrough"
            boolean r4 = r4.equals(r10)
            if (r4 == 0) goto L_0x01e6
            r4 = r7
            goto L_0x01e7
        L_0x020c:
            java.lang.String r10 = "underline"
            boolean r4 = r4.equals(r10)
            if (r4 == 0) goto L_0x01e6
            r4 = r8
            goto L_0x01e7
        L_0x0217:
            java.lang.String r10 = "nounderline"
            boolean r4 = r4.equals(r10)
            if (r4 == 0) goto L_0x01e6
            r4 = r9
            goto L_0x01e7
        L_0x0222:
            com.google.android.exoplayer.text.ttml.TtmlStyle r4 = r12.createIfNull(r14)
            com.google.android.exoplayer.text.ttml.TtmlStyle r14 = r4.setLinethrough(r5)
            goto L_0x001f
        L_0x022c:
            com.google.android.exoplayer.text.ttml.TtmlStyle r4 = r12.createIfNull(r14)
            com.google.android.exoplayer.text.ttml.TtmlStyle r14 = r4.setUnderline(r7)
            goto L_0x001f
        L_0x0236:
            com.google.android.exoplayer.text.ttml.TtmlStyle r4 = r12.createIfNull(r14)
            com.google.android.exoplayer.text.ttml.TtmlStyle r14 = r4.setUnderline(r5)
            goto L_0x001f
        L_0x0240:
            return r14
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer.text.ttml.TtmlParser.parseStyleAttributes(org.xmlpull.v1.XmlPullParser, com.google.android.exoplayer.text.ttml.TtmlStyle):com.google.android.exoplayer.text.ttml.TtmlStyle");
    }

    private TtmlStyle createIfNull(TtmlStyle style) {
        return style == null ? new TtmlStyle() : style;
    }

    private TtmlNode parseNode(XmlPullParser parser, TtmlNode parent, Map<String, TtmlRegion> regionMap, FrameAndTickRate frameAndTickRate) throws ParserException {
        long duration = 0;
        long startTime = -1;
        long endTime = -1;
        String regionId = "";
        String[] styleIds = null;
        int attributeCount = parser.getAttributeCount();
        TtmlStyle style = parseStyleAttributes(parser, (TtmlStyle) null);
        for (int i = 0; i < attributeCount; i++) {
            String attr = parser.getAttributeName(i);
            String value = parser.getAttributeValue(i);
            if (ATTR_BEGIN.equals(attr)) {
                startTime = parseTimeExpression(value, frameAndTickRate);
            } else if ("end".equals(attr)) {
                endTime = parseTimeExpression(value, frameAndTickRate);
            } else if (ATTR_DURATION.equals(attr)) {
                duration = parseTimeExpression(value, frameAndTickRate);
            } else if ("style".equals(attr)) {
                String[] ids = parseStyleIds(value);
                if (ids.length > 0) {
                    styleIds = ids;
                }
            } else if ("region".equals(attr) && regionMap.containsKey(value)) {
                regionId = value;
            }
        }
        if (!(parent == null || parent.startTimeUs == -1)) {
            if (startTime != -1) {
                startTime += parent.startTimeUs;
            }
            if (endTime != -1) {
                endTime += parent.startTimeUs;
            }
        }
        if (endTime == -1) {
            if (duration > 0) {
                endTime = startTime + duration;
            } else if (!(parent == null || parent.endTimeUs == -1)) {
                endTime = parent.endTimeUs;
            }
        }
        return TtmlNode.buildNode(parser.getName(), startTime, endTime, style, styleIds, regionId);
    }

    private static boolean isSupportedTag(String tag) {
        if (tag.equals(TtmlNode.TAG_TT) || tag.equals("head") || tag.equals("body") || tag.equals(TtmlNode.TAG_DIV) || tag.equals(TtmlNode.TAG_P) || tag.equals(TtmlNode.TAG_SPAN) || tag.equals(TtmlNode.TAG_BR) || tag.equals("style") || tag.equals(TtmlNode.TAG_STYLING) || tag.equals(TtmlNode.TAG_LAYOUT) || tag.equals("region") || tag.equals(TtmlNode.TAG_METADATA) || tag.equals(TtmlNode.TAG_SMPTE_IMAGE) || tag.equals(TtmlNode.TAG_SMPTE_DATA) || tag.equals(TtmlNode.TAG_SMPTE_INFORMATION)) {
            return true;
        }
        return false;
    }

    private static void parseFontSize(String expression, TtmlStyle out) throws ParserException {
        Matcher matcher;
        String[] expressions = expression.split("\\s+");
        if (expressions.length == 1) {
            matcher = FONT_SIZE.matcher(expression);
        } else if (expressions.length == 2) {
            matcher = FONT_SIZE.matcher(expressions[1]);
            Log.w(TAG, "Multiple values in fontSize attribute. Picking the second value for vertical font size and ignoring the first.");
        } else {
            throw new ParserException("Invalid number of entries for fontSize: " + expressions.length + ".");
        }
        if (matcher.matches()) {
            String unit = matcher.group(3);
            char c = 65535;
            switch (unit.hashCode()) {
                case 37:
                    if (unit.equals("%")) {
                        c = 2;
                        break;
                    }
                    break;
                case 3240:
                    if (unit.equals("em")) {
                        c = 1;
                        break;
                    }
                    break;
                case 3592:
                    if (unit.equals("px")) {
                        c = 0;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    out.setFontSizeUnit(1);
                    break;
                case 1:
                    out.setFontSizeUnit(2);
                    break;
                case 2:
                    out.setFontSizeUnit(3);
                    break;
                default:
                    throw new ParserException("Invalid unit for fontSize: '" + unit + "'.");
            }
            out.setFontSize(Float.valueOf(matcher.group(1)).floatValue());
            return;
        }
        throw new ParserException("Invalid expression for fontSize: '" + expression + "'.");
    }

    private static long parseTimeExpression(String time, FrameAndTickRate frameAndTickRate) throws ParserException {
        Matcher matcher = CLOCK_TIME.matcher(time);
        if (matcher.matches()) {
            double durationSeconds = ((double) (Long.parseLong(matcher.group(1)) * 3600)) + ((double) (Long.parseLong(matcher.group(2)) * 60)) + ((double) Long.parseLong(matcher.group(3)));
            String fraction = matcher.group(4);
            double durationSeconds2 = durationSeconds + (fraction != null ? Double.parseDouble(fraction) : 0.0d);
            String frames = matcher.group(5);
            double durationSeconds3 = durationSeconds2 + (frames != null ? (double) (((float) Long.parseLong(frames)) / frameAndTickRate.effectiveFrameRate) : 0.0d);
            String subframes = matcher.group(6);
            return (long) (1000000.0d * (durationSeconds3 + (subframes != null ? (((double) Long.parseLong(subframes)) / ((double) frameAndTickRate.subFrameRate)) / ((double) frameAndTickRate.effectiveFrameRate) : 0.0d)));
        }
        Matcher matcher2 = OFFSET_TIME.matcher(time);
        if (matcher2.matches()) {
            double offsetSeconds = Double.parseDouble(matcher2.group(1));
            String unit = matcher2.group(2);
            if (unit.equals("h")) {
                offsetSeconds *= 3600.0d;
            } else if (unit.equals("m")) {
                offsetSeconds *= 60.0d;
            } else if (!unit.equals("s")) {
                if (unit.equals("ms")) {
                    offsetSeconds /= 1000.0d;
                } else if (unit.equals("f")) {
                    offsetSeconds /= (double) frameAndTickRate.effectiveFrameRate;
                } else if (unit.equals("t")) {
                    offsetSeconds /= (double) frameAndTickRate.tickRate;
                }
            }
            return (long) (1000000.0d * offsetSeconds);
        }
        throw new ParserException("Malformed time expression: " + time);
    }

    private static final class FrameAndTickRate {
        final float effectiveFrameRate;
        final int subFrameRate;
        final int tickRate;

        FrameAndTickRate(float effectiveFrameRate2, int subFrameRate2, int tickRate2) {
            this.effectiveFrameRate = effectiveFrameRate2;
            this.subFrameRate = subFrameRate2;
            this.tickRate = tickRate2;
        }
    }
}
