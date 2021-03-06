package com.google.android.exoplayer.hls;

import com.google.android.exoplayer.ParserException;
import com.google.android.exoplayer.chunk.Format;
import com.google.android.exoplayer.hls.HlsMediaPlaylist;
import com.google.android.exoplayer.upstream.UriLoadable;
import com.google.android.exoplayer.util.MimeTypes;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Pattern;

public final class HlsPlaylistParser implements UriLoadable.Parser<HlsPlaylist> {
    private static final String AUDIO_TYPE = "AUDIO";
    private static final String BANDWIDTH_ATTR = "BANDWIDTH";
    private static final Pattern BANDWIDTH_ATTR_REGEX = Pattern.compile("BANDWIDTH=(\\d+)\\b");
    private static final Pattern BYTERANGE_REGEX = Pattern.compile("#EXT-X-BYTERANGE:(\\d+(?:@\\d+)?)\\b");
    private static final String BYTERANGE_TAG = "#EXT-X-BYTERANGE";
    private static final String CLOSED_CAPTIONS_TYPE = "CLOSED-CAPTIONS";
    private static final String CODECS_ATTR = "CODECS";
    private static final Pattern CODECS_ATTR_REGEX = Pattern.compile("CODECS=\"(.+?)\"");
    private static final String DISCONTINUITY_SEQUENCE_TAG = "#EXT-X-DISCONTINUITY-SEQUENCE";
    private static final String DISCONTINUITY_TAG = "#EXT-X-DISCONTINUITY";
    private static final String ENDLIST_TAG = "#EXT-X-ENDLIST";
    private static final String INSTREAM_ID_ATTR = "INSTREAM-ID";
    private static final Pattern INSTREAM_ID_ATTR_REGEX = Pattern.compile("INSTREAM-ID=\"(.+?)\"");
    private static final String IV_ATTR = "IV";
    private static final Pattern IV_ATTR_REGEX = Pattern.compile("IV=([^,.*]+)");
    private static final String KEY_TAG = "#EXT-X-KEY";
    private static final String LANGUAGE_ATTR = "LANGUAGE";
    private static final Pattern LANGUAGE_ATTR_REGEX = Pattern.compile("LANGUAGE=\"(.+?)\"");
    private static final Pattern MEDIA_DURATION_REGEX = Pattern.compile("#EXTINF:([\\d.]+)\\b");
    private static final String MEDIA_DURATION_TAG = "#EXTINF";
    private static final Pattern MEDIA_SEQUENCE_REGEX = Pattern.compile("#EXT-X-MEDIA-SEQUENCE:(\\d+)\\b");
    private static final String MEDIA_SEQUENCE_TAG = "#EXT-X-MEDIA-SEQUENCE";
    private static final String MEDIA_TAG = "#EXT-X-MEDIA";
    private static final String METHOD_AES128 = "AES-128";
    private static final String METHOD_ATTR = "METHOD";
    private static final Pattern METHOD_ATTR_REGEX = Pattern.compile("METHOD=(NONE|AES-128)");
    private static final String METHOD_NONE = "NONE";
    private static final String NAME_ATTR = "NAME";
    private static final Pattern NAME_ATTR_REGEX = Pattern.compile("NAME=\"(.+?)\"");
    private static final String RESOLUTION_ATTR = "RESOLUTION";
    private static final Pattern RESOLUTION_ATTR_REGEX = Pattern.compile("RESOLUTION=(\\d+x\\d+)");
    private static final String STREAM_INF_TAG = "#EXT-X-STREAM-INF";
    private static final String SUBTITLES_TYPE = "SUBTITLES";
    private static final Pattern TARGET_DURATION_REGEX = Pattern.compile("#EXT-X-TARGETDURATION:(\\d+)\\b");
    private static final String TARGET_DURATION_TAG = "#EXT-X-TARGETDURATION";
    private static final String TYPE_ATTR = "TYPE";
    private static final Pattern TYPE_ATTR_REGEX = Pattern.compile("TYPE=(AUDIO|VIDEO|SUBTITLES|CLOSED-CAPTIONS)");
    private static final String URI_ATTR = "URI";
    private static final Pattern URI_ATTR_REGEX = Pattern.compile("URI=\"(.+?)\"");
    private static final Pattern VERSION_REGEX = Pattern.compile("#EXT-X-VERSION:(\\d+)\\b");
    private static final String VERSION_TAG = "#EXT-X-VERSION";
    private static final String VIDEO_TYPE = "VIDEO";

    public HlsPlaylist parse(String connectionUrl, InputStream inputStream) throws IOException, ParserException {
        String line;
        HlsPlaylist parseMasterPlaylist;
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        Queue<String> extraLines = new LinkedList<>();
        while (true) {
            try {
                String line2 = reader.readLine();
                if (line2 != null) {
                    line = line2.trim();
                    if (!line.isEmpty()) {
                        if (line.startsWith(STREAM_INF_TAG)) {
                            extraLines.add(line);
                            parseMasterPlaylist = parseMasterPlaylist(new LineIterator(extraLines, reader), connectionUrl);
                            reader.close();
                            break;
                        } else if (line.startsWith(TARGET_DURATION_TAG) || line.startsWith(MEDIA_SEQUENCE_TAG) || line.startsWith(MEDIA_DURATION_TAG) || line.startsWith(KEY_TAG) || line.startsWith(BYTERANGE_TAG) || line.equals(DISCONTINUITY_TAG) || line.equals(DISCONTINUITY_SEQUENCE_TAG) || line.equals(ENDLIST_TAG)) {
                            extraLines.add(line);
                            parseMasterPlaylist = parseMediaPlaylist(new LineIterator(extraLines, reader), connectionUrl);
                        } else {
                            extraLines.add(line);
                        }
                    }
                } else {
                    reader.close();
                    throw new ParserException("Failed to parse the playlist, could not identify any tags.");
                }
            } finally {
                reader.close();
            }
        }
        extraLines.add(line);
        parseMasterPlaylist = parseMediaPlaylist(new LineIterator(extraLines, reader), connectionUrl);
        return parseMasterPlaylist;
    }

    private static HlsMasterPlaylist parseMasterPlaylist(LineIterator iterator, String baseUri) throws IOException {
        String name;
        ArrayList<Variant> variants = new ArrayList<>();
        ArrayList<Variant> audios = new ArrayList<>();
        ArrayList<Variant> subtitles = new ArrayList<>();
        int bitrate = 0;
        String codecs = null;
        int width = -1;
        int height = -1;
        String muxedAudioLanguage = null;
        String muxedCaptionLanguage = null;
        boolean expectingStreamInfUrl = false;
        String name2 = null;
        while (iterator.hasNext()) {
            String line = iterator.next();
            if (line.startsWith(MEDIA_TAG)) {
                String type = HlsParserUtil.parseStringAttr(line, TYPE_ATTR_REGEX, TYPE_ATTR);
                if (CLOSED_CAPTIONS_TYPE.equals(type)) {
                    if ("CC1".equals(HlsParserUtil.parseStringAttr(line, INSTREAM_ID_ATTR_REGEX, INSTREAM_ID_ATTR))) {
                        muxedCaptionLanguage = HlsParserUtil.parseOptionalStringAttr(line, LANGUAGE_ATTR_REGEX);
                    }
                } else if (SUBTITLES_TYPE.equals(type)) {
                    String subtitleName = HlsParserUtil.parseStringAttr(line, NAME_ATTR_REGEX, NAME_ATTR);
                    subtitles.add(new Variant(HlsParserUtil.parseStringAttr(line, URI_ATTR_REGEX, "URI"), new Format(subtitleName, MimeTypes.APPLICATION_M3U8, -1, -1, -1.0f, -1, -1, -1, HlsParserUtil.parseOptionalStringAttr(line, LANGUAGE_ATTR_REGEX), codecs)));
                } else if (AUDIO_TYPE.equals(type)) {
                    String language = HlsParserUtil.parseOptionalStringAttr(line, LANGUAGE_ATTR_REGEX);
                    String uri = HlsParserUtil.parseOptionalStringAttr(line, URI_ATTR_REGEX);
                    if (uri != null) {
                        audios.add(new Variant(uri, new Format(HlsParserUtil.parseStringAttr(line, NAME_ATTR_REGEX, NAME_ATTR), MimeTypes.APPLICATION_M3U8, -1, -1, -1.0f, -1, -1, -1, language, codecs)));
                    } else {
                        muxedAudioLanguage = language;
                    }
                }
            } else if (line.startsWith(STREAM_INF_TAG)) {
                bitrate = HlsParserUtil.parseIntAttr(line, BANDWIDTH_ATTR_REGEX, BANDWIDTH_ATTR);
                codecs = HlsParserUtil.parseOptionalStringAttr(line, CODECS_ATTR_REGEX);
                String name3 = HlsParserUtil.parseOptionalStringAttr(line, NAME_ATTR_REGEX);
                String resolutionString = HlsParserUtil.parseOptionalStringAttr(line, RESOLUTION_ATTR_REGEX);
                if (resolutionString != null) {
                    String[] widthAndHeight = resolutionString.split("x");
                    width = Integer.parseInt(widthAndHeight[0]);
                    if (width <= 0) {
                        width = -1;
                    }
                    height = Integer.parseInt(widthAndHeight[1]);
                    if (height <= 0) {
                        height = -1;
                    }
                } else {
                    width = -1;
                    height = -1;
                }
                expectingStreamInfUrl = true;
                name2 = name3;
            } else if (!line.startsWith("#") && expectingStreamInfUrl) {
                if (name2 == null) {
                    name = Integer.toString(variants.size());
                } else {
                    name = name2;
                }
                variants.add(new Variant(line, new Format(name, MimeTypes.APPLICATION_M3U8, width, height, -1.0f, -1, -1, bitrate, (String) null, codecs)));
                bitrate = 0;
                codecs = null;
                width = -1;
                height = -1;
                expectingStreamInfUrl = false;
                name2 = null;
            }
        }
        return new HlsMasterPlaylist(baseUri, variants, audios, subtitles, muxedAudioLanguage, muxedCaptionLanguage);
    }

    private static HlsMediaPlaylist parseMediaPlaylist(LineIterator iterator, String baseUri) throws IOException {
        String segmentEncryptionIV;
        int mediaSequence = 0;
        int targetDurationSecs = 0;
        int version = 1;
        boolean live = true;
        ArrayList arrayList = new ArrayList();
        double segmentDurationSecs = 0.0d;
        int discontinuitySequenceNumber = 0;
        long segmentStartTimeUs = 0;
        long segmentByterangeOffset = 0;
        long segmentByterangeLength = -1;
        int segmentMediaSequence = 0;
        boolean isEncrypted = false;
        String encryptionKeyUri = null;
        String encryptionIV = null;
        while (iterator.hasNext()) {
            String line = iterator.next();
            if (line.startsWith(TARGET_DURATION_TAG)) {
                targetDurationSecs = HlsParserUtil.parseIntAttr(line, TARGET_DURATION_REGEX, TARGET_DURATION_TAG);
            } else if (line.startsWith(MEDIA_SEQUENCE_TAG)) {
                mediaSequence = HlsParserUtil.parseIntAttr(line, MEDIA_SEQUENCE_REGEX, MEDIA_SEQUENCE_TAG);
                segmentMediaSequence = mediaSequence;
            } else if (line.startsWith(VERSION_TAG)) {
                version = HlsParserUtil.parseIntAttr(line, VERSION_REGEX, VERSION_TAG);
            } else if (line.startsWith(MEDIA_DURATION_TAG)) {
                segmentDurationSecs = HlsParserUtil.parseDoubleAttr(line, MEDIA_DURATION_REGEX, MEDIA_DURATION_TAG);
            } else if (line.startsWith(KEY_TAG)) {
                isEncrypted = "AES-128".equals(HlsParserUtil.parseStringAttr(line, METHOD_ATTR_REGEX, METHOD_ATTR));
                if (isEncrypted) {
                    encryptionKeyUri = HlsParserUtil.parseStringAttr(line, URI_ATTR_REGEX, "URI");
                    encryptionIV = HlsParserUtil.parseOptionalStringAttr(line, IV_ATTR_REGEX);
                } else {
                    encryptionKeyUri = null;
                    encryptionIV = null;
                }
            } else if (line.startsWith(BYTERANGE_TAG)) {
                String[] splitByteRange = HlsParserUtil.parseStringAttr(line, BYTERANGE_REGEX, BYTERANGE_TAG).split("@");
                segmentByterangeLength = Long.parseLong(splitByteRange[0]);
                if (splitByteRange.length > 1) {
                    segmentByterangeOffset = Long.parseLong(splitByteRange[1]);
                }
            } else if (line.startsWith(DISCONTINUITY_SEQUENCE_TAG)) {
                discontinuitySequenceNumber = Integer.parseInt(line.substring(line.indexOf(58) + 1));
            } else if (line.equals(DISCONTINUITY_TAG)) {
                discontinuitySequenceNumber++;
            } else if (!line.startsWith("#")) {
                if (!isEncrypted) {
                    segmentEncryptionIV = null;
                } else if (encryptionIV != null) {
                    segmentEncryptionIV = encryptionIV;
                } else {
                    segmentEncryptionIV = Integer.toHexString(segmentMediaSequence);
                }
                segmentMediaSequence++;
                if (segmentByterangeLength == -1) {
                    segmentByterangeOffset = 0;
                }
                arrayList.add(new HlsMediaPlaylist.Segment(line, segmentDurationSecs, discontinuitySequenceNumber, segmentStartTimeUs, isEncrypted, encryptionKeyUri, segmentEncryptionIV, segmentByterangeOffset, segmentByterangeLength));
                segmentStartTimeUs += (long) (1000000.0d * segmentDurationSecs);
                segmentDurationSecs = 0.0d;
                if (segmentByterangeLength != -1) {
                    segmentByterangeOffset += segmentByterangeLength;
                }
                segmentByterangeLength = -1;
            } else if (line.equals(ENDLIST_TAG)) {
                live = false;
            }
        }
        return new HlsMediaPlaylist(baseUri, mediaSequence, targetDurationSecs, version, live, Collections.unmodifiableList(arrayList));
    }

    private static class LineIterator {
        private final Queue<String> extraLines;
        private String next;
        private final BufferedReader reader;

        public LineIterator(Queue<String> extraLines2, BufferedReader reader2) {
            this.extraLines = extraLines2;
            this.reader = reader2;
        }

        public boolean hasNext() throws IOException {
            if (this.next != null) {
                return true;
            }
            if (!this.extraLines.isEmpty()) {
                this.next = this.extraLines.poll();
                return true;
            }
            do {
                String readLine = this.reader.readLine();
                this.next = readLine;
                if (readLine == null) {
                    return false;
                }
                this.next = this.next.trim();
            } while (this.next.isEmpty());
            return true;
        }

        public String next() throws IOException {
            if (!hasNext()) {
                return null;
            }
            String result = this.next;
            this.next = null;
            return result;
        }
    }
}
