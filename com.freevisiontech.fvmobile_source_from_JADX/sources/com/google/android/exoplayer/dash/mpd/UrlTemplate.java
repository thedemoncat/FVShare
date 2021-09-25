package com.google.android.exoplayer.dash.mpd;

import java.util.Locale;

public final class UrlTemplate {
    private static final String BANDWIDTH = "Bandwidth";
    private static final int BANDWIDTH_ID = 3;
    private static final String DEFAULT_FORMAT_TAG = "%01d";
    private static final String ESCAPED_DOLLAR = "$$";
    private static final String NUMBER = "Number";
    private static final int NUMBER_ID = 2;
    private static final String REPRESENTATION = "RepresentationID";
    private static final int REPRESENTATION_ID = 1;
    private static final String TIME = "Time";
    private static final int TIME_ID = 4;
    private final int identifierCount;
    private final String[] identifierFormatTags;
    private final int[] identifiers;
    private final String[] urlPieces;

    public static UrlTemplate compile(String template) {
        String[] urlPieces2 = new String[5];
        int[] identifiers2 = new int[4];
        String[] identifierFormatTags2 = new String[4];
        return new UrlTemplate(urlPieces2, identifiers2, identifierFormatTags2, parseTemplate(template, urlPieces2, identifiers2, identifierFormatTags2));
    }

    private UrlTemplate(String[] urlPieces2, int[] identifiers2, String[] identifierFormatTags2, int identifierCount2) {
        this.urlPieces = urlPieces2;
        this.identifiers = identifiers2;
        this.identifierFormatTags = identifierFormatTags2;
        this.identifierCount = identifierCount2;
    }

    public String buildUri(String representationId, int segmentNumber, int bandwidth, long time) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.identifierCount; i++) {
            builder.append(this.urlPieces[i]);
            if (this.identifiers[i] == 1) {
                builder.append(representationId);
            } else if (this.identifiers[i] == 2) {
                builder.append(String.format(Locale.US, this.identifierFormatTags[i], new Object[]{Integer.valueOf(segmentNumber)}));
            } else if (this.identifiers[i] == 3) {
                builder.append(String.format(Locale.US, this.identifierFormatTags[i], new Object[]{Integer.valueOf(bandwidth)}));
            } else if (this.identifiers[i] == 4) {
                builder.append(String.format(Locale.US, this.identifierFormatTags[i], new Object[]{Long.valueOf(time)}));
            }
        }
        builder.append(this.urlPieces[this.identifierCount]);
        return builder.toString();
    }

    private static int parseTemplate(String template, String[] urlPieces2, int[] identifiers2, String[] identifierFormatTags2) {
        urlPieces2[0] = "";
        int templateIndex = 0;
        int identifierCount2 = 0;
        while (templateIndex < template.length()) {
            int dollarIndex = template.indexOf("$", templateIndex);
            if (dollarIndex == -1) {
                urlPieces2[identifierCount2] = urlPieces2[identifierCount2] + template.substring(templateIndex);
                templateIndex = template.length();
            } else if (dollarIndex != templateIndex) {
                urlPieces2[identifierCount2] = urlPieces2[identifierCount2] + template.substring(templateIndex, dollarIndex);
                templateIndex = dollarIndex;
            } else if (template.startsWith(ESCAPED_DOLLAR, templateIndex)) {
                urlPieces2[identifierCount2] = urlPieces2[identifierCount2] + "$";
                templateIndex += 2;
            } else {
                int secondIndex = template.indexOf("$", templateIndex + 1);
                String identifier = template.substring(templateIndex + 1, secondIndex);
                if (identifier.equals(REPRESENTATION)) {
                    identifiers2[identifierCount2] = 1;
                } else {
                    int formatTagIndex = identifier.indexOf("%0");
                    String formatTag = DEFAULT_FORMAT_TAG;
                    if (formatTagIndex != -1) {
                        formatTag = identifier.substring(formatTagIndex);
                        if (!formatTag.endsWith("d")) {
                            formatTag = formatTag + "d";
                        }
                        identifier = identifier.substring(0, formatTagIndex);
                    }
                    if (identifier.equals(NUMBER)) {
                        identifiers2[identifierCount2] = 2;
                    } else if (identifier.equals(BANDWIDTH)) {
                        identifiers2[identifierCount2] = 3;
                    } else if (identifier.equals(TIME)) {
                        identifiers2[identifierCount2] = 4;
                    } else {
                        throw new IllegalArgumentException("Invalid template: " + template);
                    }
                    identifierFormatTags2[identifierCount2] = formatTag;
                }
                identifierCount2++;
                urlPieces2[identifierCount2] = "";
                templateIndex = secondIndex + 1;
            }
        }
        return identifierCount2;
    }
}
