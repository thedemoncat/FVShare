package com.google.android.exoplayer.hls;

import android.text.TextUtils;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.ParserException;
import com.google.android.exoplayer.extractor.Extractor;
import com.google.android.exoplayer.extractor.ExtractorInput;
import com.google.android.exoplayer.extractor.ExtractorOutput;
import com.google.android.exoplayer.extractor.PositionHolder;
import com.google.android.exoplayer.extractor.SeekMap;
import com.google.android.exoplayer.extractor.TrackOutput;
import com.google.android.exoplayer.extractor.p016ts.PtsTimestampAdjuster;
import com.google.android.exoplayer.text.webvtt.WebvttCueParser;
import com.google.android.exoplayer.text.webvtt.WebvttParserUtil;
import com.google.android.exoplayer.util.MimeTypes;
import com.google.android.exoplayer.util.ParsableByteArray;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class WebvttExtractor implements Extractor {
    private static final Pattern LOCAL_TIMESTAMP = Pattern.compile("LOCAL:([^,]+)");
    private static final Pattern MEDIA_TIMESTAMP = Pattern.compile("MPEGTS:(\\d+)");
    private ExtractorOutput output;
    private final PtsTimestampAdjuster ptsTimestampAdjuster;
    private byte[] sampleData = new byte[1024];
    private final ParsableByteArray sampleDataWrapper = new ParsableByteArray();
    private int sampleSize;

    public WebvttExtractor(PtsTimestampAdjuster ptsTimestampAdjuster2) {
        this.ptsTimestampAdjuster = ptsTimestampAdjuster2;
    }

    public boolean sniff(ExtractorInput input) throws IOException, InterruptedException {
        throw new IllegalStateException();
    }

    public void init(ExtractorOutput output2) {
        this.output = output2;
        output2.seekMap(SeekMap.UNSEEKABLE);
    }

    public void seek() {
        throw new IllegalStateException();
    }

    public void release() {
    }

    public int read(ExtractorInput input, PositionHolder seekPosition) throws IOException, InterruptedException {
        int length;
        int currentFileSize = (int) input.getLength();
        if (this.sampleSize == this.sampleData.length) {
            byte[] bArr = this.sampleData;
            if (currentFileSize != -1) {
                length = currentFileSize;
            } else {
                length = this.sampleData.length;
            }
            this.sampleData = Arrays.copyOf(bArr, (length * 3) / 2);
        }
        int bytesRead = input.read(this.sampleData, this.sampleSize, this.sampleData.length - this.sampleSize);
        if (bytesRead != -1) {
            this.sampleSize += bytesRead;
            if (currentFileSize == -1 || this.sampleSize != currentFileSize) {
                return 0;
            }
        }
        processSample();
        return -1;
    }

    private void processSample() throws ParserException {
        ParsableByteArray parsableByteArray = new ParsableByteArray(this.sampleData);
        WebvttParserUtil.validateWebvttHeaderLine(parsableByteArray);
        long vttTimestampUs = 0;
        long tsTimestampUs = 0;
        while (true) {
            String line = parsableByteArray.readLine();
            if (TextUtils.isEmpty(line)) {
                Matcher cueHeaderMatcher = WebvttCueParser.findNextCueHeader(parsableByteArray);
                if (cueHeaderMatcher == null) {
                    buildTrackOutput(0);
                    return;
                }
                long firstCueTimeUs = WebvttParserUtil.parseTimestampUs(cueHeaderMatcher.group(1));
                long sampleTimeUs = this.ptsTimestampAdjuster.adjustTimestamp(PtsTimestampAdjuster.usToPts((firstCueTimeUs + tsTimestampUs) - vttTimestampUs));
                TrackOutput trackOutput = buildTrackOutput(sampleTimeUs - firstCueTimeUs);
                this.sampleDataWrapper.reset(this.sampleData, this.sampleSize);
                trackOutput.sampleData(this.sampleDataWrapper, this.sampleSize);
                trackOutput.sampleMetadata(sampleTimeUs, 1, this.sampleSize, 0, (byte[]) null);
                return;
            } else if (line.startsWith("X-TIMESTAMP-MAP")) {
                Matcher localTimestampMatcher = LOCAL_TIMESTAMP.matcher(line);
                if (!localTimestampMatcher.find()) {
                    throw new ParserException("X-TIMESTAMP-MAP doesn't contain local timestamp: " + line);
                }
                Matcher mediaTimestampMatcher = MEDIA_TIMESTAMP.matcher(line);
                if (!mediaTimestampMatcher.find()) {
                    throw new ParserException("X-TIMESTAMP-MAP doesn't contain media timestamp: " + line);
                }
                vttTimestampUs = WebvttParserUtil.parseTimestampUs(localTimestampMatcher.group(1));
                tsTimestampUs = PtsTimestampAdjuster.ptsToUs(Long.parseLong(mediaTimestampMatcher.group(1)));
            }
        }
    }

    private TrackOutput buildTrackOutput(long subsampleOffsetUs) {
        TrackOutput trackOutput = this.output.track(0);
        trackOutput.format(MediaFormat.createTextFormat("id", MimeTypes.TEXT_VTT, -1, -1, "en", subsampleOffsetUs));
        this.output.endTracks();
        return trackOutput;
    }
}
