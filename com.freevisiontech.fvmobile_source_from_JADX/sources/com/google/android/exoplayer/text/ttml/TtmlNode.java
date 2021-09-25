package com.google.android.exoplayer.text.ttml;

import android.text.Layout;
import android.text.SpannableStringBuilder;
import com.google.android.exoplayer.text.Cue;
import com.google.android.exoplayer.util.Assertions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

final class TtmlNode {
    public static final String ANONYMOUS_REGION_ID = "";
    public static final String ATTR_ID = "id";
    public static final String ATTR_TTS_BACKGROUND_COLOR = "backgroundColor";
    public static final String ATTR_TTS_COLOR = "color";
    public static final String ATTR_TTS_EXTENT = "extent";
    public static final String ATTR_TTS_FONT_FAMILY = "fontFamily";
    public static final String ATTR_TTS_FONT_SIZE = "fontSize";
    public static final String ATTR_TTS_FONT_STYLE = "fontStyle";
    public static final String ATTR_TTS_FONT_WEIGHT = "fontWeight";
    public static final String ATTR_TTS_ORIGIN = "origin";
    public static final String ATTR_TTS_TEXT_ALIGN = "textAlign";
    public static final String ATTR_TTS_TEXT_DECORATION = "textDecoration";
    public static final String BOLD = "bold";
    public static final String CENTER = "center";
    public static final String END = "end";
    public static final String ITALIC = "italic";
    public static final String LEFT = "left";
    public static final String LINETHROUGH = "linethrough";
    public static final String NO_LINETHROUGH = "nolinethrough";
    public static final String NO_UNDERLINE = "nounderline";
    public static final String RIGHT = "right";
    public static final String START = "start";
    public static final String TAG_BODY = "body";
    public static final String TAG_BR = "br";
    public static final String TAG_DIV = "div";
    public static final String TAG_HEAD = "head";
    public static final String TAG_LAYOUT = "layout";
    public static final String TAG_METADATA = "metadata";
    public static final String TAG_P = "p";
    public static final String TAG_REGION = "region";
    public static final String TAG_SMPTE_DATA = "smpte:data";
    public static final String TAG_SMPTE_IMAGE = "smpte:image";
    public static final String TAG_SMPTE_INFORMATION = "smpte:information";
    public static final String TAG_SPAN = "span";
    public static final String TAG_STYLE = "style";
    public static final String TAG_STYLING = "styling";
    public static final String TAG_TT = "tt";
    public static final long UNDEFINED_TIME = -1;
    public static final String UNDERLINE = "underline";
    private List<TtmlNode> children;
    public final long endTimeUs;
    public final boolean isTextNode;
    private final HashMap<String, Integer> nodeEndsByRegion;
    private final HashMap<String, Integer> nodeStartsByRegion;
    public final String regionId;
    public final long startTimeUs;
    public final TtmlStyle style;
    private final String[] styleIds;
    public final String tag;
    public final String text;

    public static TtmlNode buildTextNode(String text2) {
        return new TtmlNode((String) null, TtmlRenderUtil.applyTextElementSpacePolicy(text2), -1, -1, (TtmlStyle) null, (String[]) null, "");
    }

    public static TtmlNode buildNode(String tag2, long startTimeUs2, long endTimeUs2, TtmlStyle style2, String[] styleIds2, String regionId2) {
        return new TtmlNode(tag2, (String) null, startTimeUs2, endTimeUs2, style2, styleIds2, regionId2);
    }

    private TtmlNode(String tag2, String text2, long startTimeUs2, long endTimeUs2, TtmlStyle style2, String[] styleIds2, String regionId2) {
        this.tag = tag2;
        this.text = text2;
        this.style = style2;
        this.styleIds = styleIds2;
        this.isTextNode = text2 != null;
        this.startTimeUs = startTimeUs2;
        this.endTimeUs = endTimeUs2;
        this.regionId = (String) Assertions.checkNotNull(regionId2);
        this.nodeStartsByRegion = new HashMap<>();
        this.nodeEndsByRegion = new HashMap<>();
    }

    public boolean isActive(long timeUs) {
        return (this.startTimeUs == -1 && this.endTimeUs == -1) || (this.startTimeUs <= timeUs && this.endTimeUs == -1) || ((this.startTimeUs == -1 && timeUs < this.endTimeUs) || (this.startTimeUs <= timeUs && timeUs < this.endTimeUs));
    }

    public void addChild(TtmlNode child) {
        if (this.children == null) {
            this.children = new ArrayList();
        }
        this.children.add(child);
    }

    public TtmlNode getChild(int index) {
        if (this.children != null) {
            return this.children.get(index);
        }
        throw new IndexOutOfBoundsException();
    }

    public int getChildCount() {
        if (this.children == null) {
            return 0;
        }
        return this.children.size();
    }

    public long[] getEventTimesUs() {
        TreeSet<Long> eventTimeSet = new TreeSet<>();
        getEventTimes(eventTimeSet, false);
        long[] eventTimes = new long[eventTimeSet.size()];
        int i = 0;
        Iterator<Long> it = eventTimeSet.iterator();
        while (it.hasNext()) {
            eventTimes[i] = it.next().longValue();
            i++;
        }
        return eventTimes;
    }

    private void getEventTimes(TreeSet<Long> out, boolean descendsPNode) {
        boolean isPNode = TAG_P.equals(this.tag);
        if (descendsPNode || isPNode) {
            if (this.startTimeUs != -1) {
                out.add(Long.valueOf(this.startTimeUs));
            }
            if (this.endTimeUs != -1) {
                out.add(Long.valueOf(this.endTimeUs));
            }
        }
        if (this.children != null) {
            for (int i = 0; i < this.children.size(); i++) {
                this.children.get(i).getEventTimes(out, descendsPNode || isPNode);
            }
        }
    }

    public String[] getStyleIds() {
        return this.styleIds;
    }

    public List<Cue> getCues(long timeUs, Map<String, TtmlStyle> globalStyles, Map<String, TtmlRegion> regionMap) {
        TreeMap<String, SpannableStringBuilder> regionOutputs = new TreeMap<>();
        traverseForText(timeUs, false, this.regionId, regionOutputs);
        traverseForStyle(globalStyles, regionOutputs);
        List<Cue> cues = new ArrayList<>();
        for (Map.Entry<String, SpannableStringBuilder> entry : regionOutputs.entrySet()) {
            TtmlRegion region = regionMap.get(entry.getKey());
            cues.add(new Cue(cleanUpText(entry.getValue()), (Layout.Alignment) null, region.line, region.lineType, Integer.MIN_VALUE, region.position, Integer.MIN_VALUE, region.width));
        }
        return cues;
    }

    private void traverseForText(long timeUs, boolean descendsPNode, String inheritedRegion, Map<String, SpannableStringBuilder> regionOutputs) {
        this.nodeStartsByRegion.clear();
        this.nodeEndsByRegion.clear();
        String resolvedRegionId = this.regionId;
        if ("".equals(resolvedRegionId)) {
            resolvedRegionId = inheritedRegion;
        }
        if (this.isTextNode && descendsPNode) {
            getRegionOutput(resolvedRegionId, regionOutputs).append(this.text);
        } else if (TAG_BR.equals(this.tag) && descendsPNode) {
            getRegionOutput(resolvedRegionId, regionOutputs).append(10);
        } else if (!TAG_METADATA.equals(this.tag) && isActive(timeUs)) {
            boolean isPNode = TAG_P.equals(this.tag);
            for (Map.Entry<String, SpannableStringBuilder> entry : regionOutputs.entrySet()) {
                this.nodeStartsByRegion.put(entry.getKey(), Integer.valueOf(entry.getValue().length()));
            }
            for (int i = 0; i < getChildCount(); i++) {
                getChild(i).traverseForText(timeUs, descendsPNode || isPNode, resolvedRegionId, regionOutputs);
            }
            if (isPNode) {
                TtmlRenderUtil.endParagraph(getRegionOutput(resolvedRegionId, regionOutputs));
            }
            for (Map.Entry<String, SpannableStringBuilder> entry2 : regionOutputs.entrySet()) {
                this.nodeEndsByRegion.put(entry2.getKey(), Integer.valueOf(entry2.getValue().length()));
            }
        }
    }

    private static SpannableStringBuilder getRegionOutput(String resolvedRegionId, Map<String, SpannableStringBuilder> regionOutputs) {
        if (!regionOutputs.containsKey(resolvedRegionId)) {
            regionOutputs.put(resolvedRegionId, new SpannableStringBuilder());
        }
        return regionOutputs.get(resolvedRegionId);
    }

    private void traverseForStyle(Map<String, TtmlStyle> globalStyles, Map<String, SpannableStringBuilder> regionOutputs) {
        for (Map.Entry<String, Integer> entry : this.nodeEndsByRegion.entrySet()) {
            String regionId2 = entry.getKey();
            applyStyleToOutput(globalStyles, regionOutputs.get(regionId2), this.nodeStartsByRegion.containsKey(regionId2) ? this.nodeStartsByRegion.get(regionId2).intValue() : 0, entry.getValue().intValue());
            for (int i = 0; i < getChildCount(); i++) {
                getChild(i).traverseForStyle(globalStyles, regionOutputs);
            }
        }
    }

    private void applyStyleToOutput(Map<String, TtmlStyle> globalStyles, SpannableStringBuilder regionOutput, int start, int end) {
        TtmlStyle resolvedStyle;
        if (start != end && (resolvedStyle = TtmlRenderUtil.resolveStyle(this.style, this.styleIds, globalStyles)) != null) {
            TtmlRenderUtil.applyStylesToSpan(regionOutput, start, end, resolvedStyle);
        }
    }

    private SpannableStringBuilder cleanUpText(SpannableStringBuilder builder) {
        int builderLength = builder.length();
        for (int i = 0; i < builderLength; i++) {
            if (builder.charAt(i) == ' ') {
                int j = i + 1;
                while (j < builder.length() && builder.charAt(j) == ' ') {
                    j++;
                }
                int spacesToDelete = j - (i + 1);
                if (spacesToDelete > 0) {
                    builder.delete(i, i + spacesToDelete);
                    builderLength -= spacesToDelete;
                }
            }
        }
        if (builderLength > 0 && builder.charAt(0) == ' ') {
            builder.delete(0, 1);
            builderLength--;
        }
        for (int i2 = 0; i2 < builderLength - 1; i2++) {
            if (builder.charAt(i2) == 10 && builder.charAt(i2 + 1) == ' ') {
                builder.delete(i2 + 1, i2 + 2);
                builderLength--;
            }
        }
        if (builderLength > 0 && builder.charAt(builderLength - 1) == ' ') {
            builder.delete(builderLength - 1, builderLength);
            builderLength--;
        }
        for (int i3 = 0; i3 < builderLength - 1; i3++) {
            if (builder.charAt(i3) == ' ' && builder.charAt(i3 + 1) == 10) {
                builder.delete(i3, i3 + 1);
                builderLength--;
            }
        }
        if (builderLength > 0 && builder.charAt(builderLength - 1) == 10) {
            builder.delete(builderLength - 1, builderLength);
        }
        return builder;
    }
}
