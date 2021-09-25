package com.google.android.exoplayer.text.webvtt;

import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import com.google.android.exoplayer.text.webvtt.WebvttCue;
import com.google.android.exoplayer.util.ParsableByteArray;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class WebvttCueParser {
    private static final char CHAR_AMPERSAND = '&';
    private static final char CHAR_GREATER_THAN = '>';
    private static final char CHAR_LESS_THAN = '<';
    private static final char CHAR_SEMI_COLON = ';';
    private static final char CHAR_SLASH = '/';
    private static final char CHAR_SPACE = ' ';
    private static final Pattern COMMENT = Pattern.compile("^NOTE(( |\t).*)?$");
    public static final Pattern CUE_HEADER_PATTERN = Pattern.compile("^(\\S+)\\s+-->\\s+(\\S+)(.*)?$");
    private static final Pattern CUE_SETTING_PATTERN = Pattern.compile("(\\S+?):(\\S+)");
    private static final String ENTITY_AMPERSAND = "amp";
    private static final String ENTITY_GREATER_THAN = "gt";
    private static final String ENTITY_LESS_THAN = "lt";
    private static final String ENTITY_NON_BREAK_SPACE = "nbsp";
    private static final String SPACE = " ";
    private static final int STYLE_BOLD = 1;
    private static final int STYLE_ITALIC = 2;
    private static final String TAG = "WebvttCueParser";
    private static final String TAG_BOLD = "b";
    private static final String TAG_CLASS = "c";
    private static final String TAG_ITALIC = "i";
    private static final String TAG_LANG = "lang";
    private static final String TAG_UNDERLINE = "u";
    private static final String TAG_VOICE = "v";
    private final StringBuilder textBuilder = new StringBuilder();

    /* access modifiers changed from: package-private */
    public boolean parseNextValidCue(ParsableByteArray webvttData, WebvttCue.Builder builder) {
        Matcher cueHeaderMatcher;
        do {
            cueHeaderMatcher = findNextCueHeader(webvttData);
            if (cueHeaderMatcher == null) {
                return false;
            }
        } while (!parseCue(cueHeaderMatcher, webvttData, builder, this.textBuilder));
        return true;
    }

    static void parseCueSettingsList(String cueSettingsList, WebvttCue.Builder builder) {
        Matcher cueSettingMatcher = CUE_SETTING_PATTERN.matcher(cueSettingsList);
        while (cueSettingMatcher.find()) {
            String name = cueSettingMatcher.group(1);
            String value = cueSettingMatcher.group(2);
            try {
                if ("line".equals(name)) {
                    parseLineAttribute(value, builder);
                } else if ("align".equals(name)) {
                    builder.setTextAlignment(parseTextAlignment(value));
                } else if ("position".equals(name)) {
                    parsePositionAttribute(value, builder);
                } else if ("size".equals(name)) {
                    builder.setWidth(WebvttParserUtil.parsePercentage(value));
                } else {
                    Log.w(TAG, "Unknown cue setting " + name + ":" + value);
                }
            } catch (NumberFormatException e) {
                Log.w(TAG, "Skipping bad cue setting: " + cueSettingMatcher.group());
            }
        }
    }

    public static Matcher findNextCueHeader(ParsableByteArray input) {
        String line;
        while (true) {
            String line2 = input.readLine();
            if (line2 == null) {
                return null;
            }
            if (COMMENT.matcher(line2).matches()) {
                do {
                    line = input.readLine();
                    if (line == null) {
                        break;
                    }
                } while (line.isEmpty());
            } else {
                Matcher cueHeaderMatcher = CUE_HEADER_PATTERN.matcher(line2);
                if (cueHeaderMatcher.matches()) {
                    return cueHeaderMatcher;
                }
            }
        }
    }

    static void parseCueText(String markup, WebvttCue.Builder builder) {
        int i;
        SpannableStringBuilder spannedText = new SpannableStringBuilder();
        Stack<StartTag> startTagStack = new Stack<>();
        int pos = 0;
        while (pos < markup.length()) {
            char curr = markup.charAt(pos);
            switch (curr) {
                case '&':
                    int semiColonEnd = markup.indexOf(59, pos + 1);
                    int spaceEnd = markup.indexOf(32, pos + 1);
                    int entityEnd = semiColonEnd == -1 ? spaceEnd : spaceEnd == -1 ? semiColonEnd : Math.min(semiColonEnd, spaceEnd);
                    if (entityEnd == -1) {
                        spannedText.append(curr);
                        pos++;
                        break;
                    } else {
                        applyEntity(markup.substring(pos + 1, entityEnd), spannedText);
                        if (entityEnd == spaceEnd) {
                            spannedText.append(SPACE);
                        }
                        pos = entityEnd + 1;
                        break;
                    }
                case '<':
                    if (pos + 1 < markup.length()) {
                        int ltPos = pos;
                        boolean isClosingTag = markup.charAt(ltPos + 1) == '/';
                        pos = findEndOfTag(markup, ltPos + 1);
                        boolean isVoidTag = markup.charAt(pos + -2) == '/';
                        int i2 = ltPos + (isClosingTag ? 2 : 1);
                        if (isVoidTag) {
                            i = pos - 2;
                        } else {
                            i = pos - 1;
                        }
                        String[] tagTokens = tokenizeTag(markup.substring(i2, i));
                        if (tagTokens != null && isSupportedTag(tagTokens[0])) {
                            if (!isClosingTag) {
                                if (isVoidTag) {
                                    break;
                                } else {
                                    startTagStack.push(new StartTag(tagTokens[0], spannedText.length()));
                                    break;
                                }
                            } else {
                                while (!startTagStack.isEmpty()) {
                                    StartTag startTag = startTagStack.pop();
                                    applySpansForTag(startTag, spannedText);
                                    if (startTag.name.equals(tagTokens[0])) {
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                    } else {
                        pos++;
                        break;
                    }
                default:
                    spannedText.append(curr);
                    pos++;
                    break;
            }
        }
        while (!startTagStack.isEmpty()) {
            applySpansForTag(startTagStack.pop(), spannedText);
        }
        builder.setText(spannedText);
    }

    private static boolean parseCue(Matcher cueHeaderMatcher, ParsableByteArray webvttData, WebvttCue.Builder builder, StringBuilder textBuilder2) {
        try {
            builder.setStartTime(WebvttParserUtil.parseTimestampUs(cueHeaderMatcher.group(1))).setEndTime(WebvttParserUtil.parseTimestampUs(cueHeaderMatcher.group(2)));
            parseCueSettingsList(cueHeaderMatcher.group(3), builder);
            textBuilder2.setLength(0);
            while (true) {
                String line = webvttData.readLine();
                if (line == null || line.isEmpty()) {
                    parseCueText(textBuilder2.toString(), builder);
                } else {
                    if (textBuilder2.length() > 0) {
                        textBuilder2.append("\n");
                    }
                    textBuilder2.append(line.trim());
                }
            }
            parseCueText(textBuilder2.toString(), builder);
            return true;
        } catch (NumberFormatException e) {
            Log.w(TAG, "Skipping cue with bad header: " + cueHeaderMatcher.group());
            return false;
        }
    }

    private static void parseLineAttribute(String s, WebvttCue.Builder builder) throws NumberFormatException {
        int commaPosition = s.indexOf(44);
        if (commaPosition != -1) {
            builder.setLineAnchor(parsePositionAnchor(s.substring(commaPosition + 1)));
            s = s.substring(0, commaPosition);
        } else {
            builder.setLineAnchor(Integer.MIN_VALUE);
        }
        if (s.endsWith("%")) {
            builder.setLine(WebvttParserUtil.parsePercentage(s)).setLineType(0);
        } else {
            builder.setLine((float) Integer.parseInt(s)).setLineType(1);
        }
    }

    private static void parsePositionAttribute(String s, WebvttCue.Builder builder) throws NumberFormatException {
        int commaPosition = s.indexOf(44);
        if (commaPosition != -1) {
            builder.setPositionAnchor(parsePositionAnchor(s.substring(commaPosition + 1)));
            s = s.substring(0, commaPosition);
        } else {
            builder.setPositionAnchor(Integer.MIN_VALUE);
        }
        builder.setPosition(WebvttParserUtil.parsePercentage(s));
    }

    private static int parsePositionAnchor(String s) {
        char c = 65535;
        switch (s.hashCode()) {
            case -1364013995:
                if (s.equals(TtmlNode.CENTER)) {
                    c = 1;
                    break;
                }
                break;
            case -1074341483:
                if (s.equals("middle")) {
                    c = 2;
                    break;
                }
                break;
            case 100571:
                if (s.equals(TtmlNode.END)) {
                    c = 3;
                    break;
                }
                break;
            case 109757538:
                if (s.equals(TtmlNode.START)) {
                    c = 0;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return 0;
            case 1:
            case 2:
                return 1;
            case 3:
                return 2;
            default:
                Log.w(TAG, "Invalid anchor value: " + s);
                return Integer.MIN_VALUE;
        }
    }

    private static Layout.Alignment parseTextAlignment(String s) {
        char c = 65535;
        switch (s.hashCode()) {
            case -1364013995:
                if (s.equals(TtmlNode.CENTER)) {
                    c = 2;
                    break;
                }
                break;
            case -1074341483:
                if (s.equals("middle")) {
                    c = 3;
                    break;
                }
                break;
            case 100571:
                if (s.equals(TtmlNode.END)) {
                    c = 4;
                    break;
                }
                break;
            case 3317767:
                if (s.equals(TtmlNode.LEFT)) {
                    c = 1;
                    break;
                }
                break;
            case 108511772:
                if (s.equals(TtmlNode.RIGHT)) {
                    c = 5;
                    break;
                }
                break;
            case 109757538:
                if (s.equals(TtmlNode.START)) {
                    c = 0;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
            case 1:
                return Layout.Alignment.ALIGN_NORMAL;
            case 2:
            case 3:
                return Layout.Alignment.ALIGN_CENTER;
            case 4:
            case 5:
                return Layout.Alignment.ALIGN_OPPOSITE;
            default:
                Log.w(TAG, "Invalid alignment value: " + s);
                return null;
        }
    }

    private static int findEndOfTag(String markup, int startPos) {
        int idx = markup.indexOf(62, startPos);
        return idx == -1 ? markup.length() : idx + 1;
    }

    private static void applyEntity(String entity, SpannableStringBuilder spannedText) {
        char c = 65535;
        switch (entity.hashCode()) {
            case 3309:
                if (entity.equals(ENTITY_GREATER_THAN)) {
                    c = 1;
                    break;
                }
                break;
            case 3464:
                if (entity.equals(ENTITY_LESS_THAN)) {
                    c = 0;
                    break;
                }
                break;
            case 96708:
                if (entity.equals(ENTITY_AMPERSAND)) {
                    c = 3;
                    break;
                }
                break;
            case 3374865:
                if (entity.equals(ENTITY_NON_BREAK_SPACE)) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                spannedText.append(CHAR_LESS_THAN);
                return;
            case 1:
                spannedText.append(CHAR_GREATER_THAN);
                return;
            case 2:
                spannedText.append(CHAR_SPACE);
                return;
            case 3:
                spannedText.append(CHAR_AMPERSAND);
                return;
            default:
                Log.w(TAG, "ignoring unsupported entity: '&" + entity + ";'");
                return;
        }
    }

    private static boolean isSupportedTag(String tagName) {
        char c = 65535;
        switch (tagName.hashCode()) {
            case 98:
                if (tagName.equals(TAG_BOLD)) {
                    c = 0;
                    break;
                }
                break;
            case 99:
                if (tagName.equals(TAG_CLASS)) {
                    c = 1;
                    break;
                }
                break;
            case 105:
                if (tagName.equals(TAG_ITALIC)) {
                    c = 2;
                    break;
                }
                break;
            case 117:
                if (tagName.equals(TAG_UNDERLINE)) {
                    c = 4;
                    break;
                }
                break;
            case 118:
                if (tagName.equals(TAG_VOICE)) {
                    c = 5;
                    break;
                }
                break;
            case 3314158:
                if (tagName.equals(TAG_LANG)) {
                    c = 3;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return true;
            default:
                return false;
        }
    }

    private static void applySpansForTag(StartTag startTag, SpannableStringBuilder spannedText) {
        String str = startTag.name;
        char c = 65535;
        switch (str.hashCode()) {
            case 98:
                if (str.equals(TAG_BOLD)) {
                    c = 0;
                    break;
                }
                break;
            case 105:
                if (str.equals(TAG_ITALIC)) {
                    c = 1;
                    break;
                }
                break;
            case 117:
                if (str.equals(TAG_UNDERLINE)) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                spannedText.setSpan(new StyleSpan(1), startTag.position, spannedText.length(), 33);
                return;
            case 1:
                spannedText.setSpan(new StyleSpan(2), startTag.position, spannedText.length(), 33);
                return;
            case 2:
                spannedText.setSpan(new UnderlineSpan(), startTag.position, spannedText.length(), 33);
                return;
            default:
                return;
        }
    }

    private static String[] tokenizeTag(String fullTagExpression) {
        String fullTagExpression2 = fullTagExpression.replace("\\s+", SPACE).trim();
        if (fullTagExpression2.length() == 0) {
            return null;
        }
        if (fullTagExpression2.contains(SPACE)) {
            fullTagExpression2 = fullTagExpression2.substring(0, fullTagExpression2.indexOf(SPACE));
        }
        return fullTagExpression2.split("\\.");
    }

    private static final class StartTag {
        public final String name;
        public final int position;

        public StartTag(String name2, int position2) {
            this.position = position2;
            this.name = name2;
        }
    }
}
