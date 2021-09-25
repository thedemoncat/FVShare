package com.googlecode.mp4parser.authoring.tracks.webvtt;

import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.SampleDescriptionBox;
import com.googlecode.mp4parser.authoring.AbstractTrack;
import com.googlecode.mp4parser.authoring.Sample;
import com.googlecode.mp4parser.authoring.TrackMetaData;
import com.googlecode.mp4parser.authoring.tracks.webvtt.sampleboxes.CuePayloadBox;
import com.googlecode.mp4parser.authoring.tracks.webvtt.sampleboxes.CueSettingsBox;
import com.googlecode.mp4parser.authoring.tracks.webvtt.sampleboxes.VTTCueBox;
import com.googlecode.mp4parser.authoring.tracks.webvtt.sampleboxes.VTTEmptyCueBox;
import com.googlecode.mp4parser.util.ByteBufferByteChannel;
import com.googlecode.mp4parser.util.CastUtils;
import com.googlecode.mp4parser.util.Mp4Arrays;
import com.mp4parser.iso14496.part30.WebVTTConfigurationBox;
import com.mp4parser.iso14496.part30.WebVTTSampleEntry;
import com.mp4parser.iso14496.part30.WebVTTSourceLabelBox;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebVttTrack extends AbstractTrack {
    private static final Sample EMPTY_SAMPLE = new Sample() {
        ByteBuffer vtte;

        {
            VTTEmptyCueBox vttEmptyCueBox = new VTTEmptyCueBox();
            this.vtte = ByteBuffer.allocate(CastUtils.l2i(vttEmptyCueBox.getSize()));
            try {
                vttEmptyCueBox.getBox(new ByteBufferByteChannel(this.vtte));
                this.vtte.rewind();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void writeTo(WritableByteChannel channel) throws IOException {
            channel.write(this.vtte.duplicate());
        }

        public long getSize() {
            return (long) this.vtte.remaining();
        }

        public ByteBuffer asByteBuffer() {
            return this.vtte.duplicate();
        }
    };
    private static final Pattern WEBVTT_CUE_IDENTIFIER = Pattern.compile(WEBVTT_CUE_IDENTIFIER_STRING);
    private static final String WEBVTT_CUE_IDENTIFIER_STRING = "^(?!.*(-->)).*$";
    private static final Pattern WEBVTT_CUE_SETTING = Pattern.compile(WEBVTT_CUE_SETTING_STRING);
    private static final String WEBVTT_CUE_SETTING_STRING = "\\S*:\\S*";
    private static final Pattern WEBVTT_FILE_HEADER = Pattern.compile(WEBVTT_FILE_HEADER_STRING);
    private static final String WEBVTT_FILE_HEADER_STRING = "^﻿?WEBVTT((\\u0020|\t).*)?$";
    private static final Pattern WEBVTT_METADATA_HEADER = Pattern.compile(WEBVTT_METADATA_HEADER_STRING);
    private static final String WEBVTT_METADATA_HEADER_STRING = "\\S*[:=]\\S*";
    private static final Pattern WEBVTT_TIMESTAMP = Pattern.compile(WEBVTT_TIMESTAMP_STRING);
    private static final String WEBVTT_TIMESTAMP_STRING = "(\\d+:)?[0-5]\\d:[0-5]\\d\\.\\d{3}";
    long[] sampleDurations = new long[0];
    WebVTTSampleEntry sampleEntry;
    List<Sample> samples = new ArrayList();
    SampleDescriptionBox stsd;
    TrackMetaData trackMetaData = new TrackMetaData();

    private static class BoxBearingSample implements Sample {
        List<Box> boxes;

        public BoxBearingSample(List<Box> boxes2) {
            this.boxes = boxes2;
        }

        public void writeTo(WritableByteChannel channel) throws IOException {
            for (Box box : this.boxes) {
                box.getBox(channel);
            }
        }

        public long getSize() {
            long l = 0;
            for (Box box : this.boxes) {
                l += box.getSize();
            }
            return l;
        }

        public ByteBuffer asByteBuffer() {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                writeTo(Channels.newChannel(baos));
                return ByteBuffer.wrap(baos.toByteArray());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public WebVttTrack(InputStream is, String trackName, Locale locale) throws IOException {
        super(trackName);
        this.trackMetaData.setTimescale(1000);
        this.trackMetaData.setLanguage(locale.getISO3Language());
        long mediaTimestampUs = 0;
        this.stsd = new SampleDescriptionBox();
        this.sampleEntry = new WebVTTSampleEntry();
        this.stsd.addBox(this.sampleEntry);
        WebVTTConfigurationBox webVttConf = new WebVTTConfigurationBox();
        this.sampleEntry.addBox(webVttConf);
        this.sampleEntry.addBox(new WebVTTSourceLabelBox());
        BufferedReader webvttData = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        String line = webvttData.readLine();
        if (line == null || !WEBVTT_FILE_HEADER.matcher(line).matches()) {
            throw new IOException("Expected WEBVTT. Got " + line);
        }
        webVttConf.setConfig(String.valueOf(webVttConf.getConfig()) + "\n" + line);
        while (true) {
            String line2 = webvttData.readLine();
            if (line2 == null) {
                throw new IOException("Expected an empty line after webvtt header");
            } else if (line2.isEmpty()) {
                while (true) {
                    String line3 = webvttData.readLine();
                    if (line3 != null) {
                        if (!"".equals(line3.trim())) {
                            line3 = WEBVTT_CUE_IDENTIFIER.matcher(line3).find() ? webvttData.readLine() : line3;
                            Matcher matcher = WEBVTT_TIMESTAMP.matcher(line3);
                            if (!matcher.find()) {
                                throw new IOException("Expected cue start time: " + line3);
                            }
                            long startTime = parseTimestampUs(matcher.group());
                            if (!matcher.find()) {
                                throw new IOException("Expected cue end time: " + line3);
                            }
                            String endTimeString = matcher.group();
                            long endTime = parseTimestampUs(endTimeString);
                            Matcher matcher2 = WEBVTT_CUE_SETTING.matcher(line3.substring(line3.indexOf(endTimeString) + endTimeString.length()));
                            String settings = null;
                            while (matcher2.find()) {
                                settings = matcher2.group();
                            }
                            StringBuilder payload = new StringBuilder();
                            while (true) {
                                String line4 = webvttData.readLine();
                                if (line4 != null && !line4.isEmpty()) {
                                    if (payload.length() > 0) {
                                        payload.append("\n");
                                    }
                                    payload.append(line4.trim());
                                }
                            }
                            if (startTime != mediaTimestampUs) {
                                this.sampleDurations = Mp4Arrays.copyOfAndAppend(this.sampleDurations, startTime - mediaTimestampUs);
                                this.samples.add(EMPTY_SAMPLE);
                            }
                            this.sampleDurations = Mp4Arrays.copyOfAndAppend(this.sampleDurations, endTime - startTime);
                            VTTCueBox vttCueBox = new VTTCueBox();
                            if (settings != null) {
                                CueSettingsBox csb = new CueSettingsBox();
                                csb.setContent(settings);
                                vttCueBox.setCueSettingsBox(csb);
                            }
                            CuePayloadBox cuePayloadBox = new CuePayloadBox();
                            cuePayloadBox.setContent(payload.toString());
                            vttCueBox.setCuePayloadBox(cuePayloadBox);
                            this.samples.add(new BoxBearingSample(Collections.singletonList(vttCueBox)));
                            mediaTimestampUs = endTime;
                        }
                    } else {
                        return;
                    }
                }
            } else if (!WEBVTT_METADATA_HEADER.matcher(line2).find()) {
                throw new IOException("Expected WebVTT metadata header. Got " + line2);
            } else {
                webVttConf.setConfig(String.valueOf(webVttConf.getConfig()) + "\n" + line2);
            }
        }
    }

    private static long parseTimestampUs(String s) throws NumberFormatException {
        if (!s.matches(WEBVTT_TIMESTAMP_STRING)) {
            throw new NumberFormatException("has invalid format");
        }
        String[] parts = s.split("\\.", 2);
        long value = 0;
        for (String group : parts[0].split(":")) {
            value = (60 * value) + Long.parseLong(group);
        }
        return (1000 * value) + Long.parseLong(parts[1]);
    }

    public SampleDescriptionBox getSampleDescriptionBox() {
        return this.stsd;
    }

    public long[] getSampleDurations() {
        long[] adoptedSampleDuration = new long[this.sampleDurations.length];
        for (int i = 0; i < adoptedSampleDuration.length; i++) {
            adoptedSampleDuration[i] = (this.sampleDurations[i] * this.trackMetaData.getTimescale()) / 1000;
        }
        return adoptedSampleDuration;
    }

    public TrackMetaData getTrackMetaData() {
        return this.trackMetaData;
    }

    public String getHandler() {
        return "text";
    }

    public List<Sample> getSamples() {
        return this.samples;
    }

    public void close() throws IOException {
    }
}
