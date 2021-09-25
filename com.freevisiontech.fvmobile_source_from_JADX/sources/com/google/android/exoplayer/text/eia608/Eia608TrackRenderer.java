package com.google.android.exoplayer.text.eia608;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.MediaFormatHolder;
import com.google.android.exoplayer.SampleHolder;
import com.google.android.exoplayer.SampleSource;
import com.google.android.exoplayer.SampleSourceTrackRenderer;
import com.google.android.exoplayer.text.Cue;
import com.google.android.exoplayer.text.TextRenderer;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.Util;
import java.util.Collections;
import java.util.TreeSet;

public final class Eia608TrackRenderer extends SampleSourceTrackRenderer implements Handler.Callback {
    private static final int CC_MODE_PAINT_ON = 3;
    private static final int CC_MODE_POP_ON = 2;
    private static final int CC_MODE_ROLL_UP = 1;
    private static final int CC_MODE_UNKNOWN = 0;
    private static final int DEFAULT_CAPTIONS_ROW_COUNT = 4;
    private static final int MAX_SAMPLE_READAHEAD_US = 5000000;
    private static final int MSG_INVOKE_RENDERER = 0;
    private String caption;
    private int captionMode;
    private int captionRowCount;
    private final StringBuilder captionStringBuilder;
    private final Eia608Parser eia608Parser;
    private final MediaFormatHolder formatHolder;
    private boolean inputStreamEnded;
    private String lastRenderedCaption;
    private final TreeSet<ClosedCaptionList> pendingCaptionLists;
    private ClosedCaptionCtrl repeatableControl;
    private final SampleHolder sampleHolder;
    private final TextRenderer textRenderer;
    private final Handler textRendererHandler;

    public Eia608TrackRenderer(SampleSource source, TextRenderer textRenderer2, Looper textRendererLooper) {
        super(source);
        this.textRenderer = (TextRenderer) Assertions.checkNotNull(textRenderer2);
        this.textRendererHandler = textRendererLooper == null ? null : new Handler(textRendererLooper, this);
        this.eia608Parser = new Eia608Parser();
        this.formatHolder = new MediaFormatHolder();
        this.sampleHolder = new SampleHolder(1);
        this.captionStringBuilder = new StringBuilder();
        this.pendingCaptionLists = new TreeSet<>();
    }

    /* access modifiers changed from: protected */
    public boolean handlesTrack(MediaFormat mediaFormat) {
        return this.eia608Parser.canParse(mediaFormat.mimeType);
    }

    /* access modifiers changed from: protected */
    public void onEnabled(int track, long positionUs, boolean joining) throws ExoPlaybackException {
        super.onEnabled(track, positionUs, joining);
    }

    /* access modifiers changed from: protected */
    public void onDiscontinuity(long positionUs) {
        this.inputStreamEnded = false;
        this.repeatableControl = null;
        this.pendingCaptionLists.clear();
        clearPendingSample();
        this.captionRowCount = 4;
        setCaptionMode(0);
        invokeRenderer((String) null);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: CFG modification limit reached, blocks count: 130 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void doSomeWork(long r8, long r10, boolean r12) throws com.google.android.exoplayer.ExoPlaybackException {
        /*
            r7 = this;
            r2 = -1
            r3 = -3
            boolean r4 = r7.isSamplePending()
            if (r4 == 0) goto L_0x000b
            r7.maybeParsePendingSample(r8)
        L_0x000b:
            boolean r4 = r7.inputStreamEnded
            if (r4 == 0) goto L_0x0026
            r1 = r2
        L_0x0010:
            boolean r4 = r7.isSamplePending()
            if (r4 != 0) goto L_0x0042
            if (r1 != r3) goto L_0x0042
            com.google.android.exoplayer.MediaFormatHolder r4 = r7.formatHolder
            com.google.android.exoplayer.SampleHolder r5 = r7.sampleHolder
            int r1 = r7.readSource(r8, r4, r5)
            if (r1 != r3) goto L_0x0028
            r7.maybeParsePendingSample(r8)
            goto L_0x0010
        L_0x0026:
            r1 = r3
            goto L_0x0010
        L_0x0028:
            if (r1 != r2) goto L_0x0010
            r4 = 1
            r7.inputStreamEnded = r4
            goto L_0x0010
        L_0x002e:
            java.util.TreeSet<com.google.android.exoplayer.text.eia608.ClosedCaptionList> r2 = r7.pendingCaptionLists
            java.lang.Object r0 = r2.pollFirst()
            com.google.android.exoplayer.text.eia608.ClosedCaptionList r0 = (com.google.android.exoplayer.text.eia608.ClosedCaptionList) r0
            r7.consumeCaptionList(r0)
            boolean r2 = r0.decodeOnly
            if (r2 != 0) goto L_0x0042
            java.lang.String r2 = r7.caption
            r7.invokeRenderer(r2)
        L_0x0042:
            java.util.TreeSet<com.google.android.exoplayer.text.eia608.ClosedCaptionList> r2 = r7.pendingCaptionLists
            boolean r2 = r2.isEmpty()
            if (r2 != 0) goto L_0x0058
            java.util.TreeSet<com.google.android.exoplayer.text.eia608.ClosedCaptionList> r2 = r7.pendingCaptionLists
            java.lang.Object r2 = r2.first()
            com.google.android.exoplayer.text.eia608.ClosedCaptionList r2 = (com.google.android.exoplayer.text.eia608.ClosedCaptionList) r2
            long r2 = r2.timeUs
            int r2 = (r2 > r8 ? 1 : (r2 == r8 ? 0 : -1))
            if (r2 <= 0) goto L_0x002e
        L_0x0058:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer.text.eia608.Eia608TrackRenderer.doSomeWork(long, long, boolean):void");
    }

    /* access modifiers changed from: protected */
    public long getBufferedPositionUs() {
        return -3;
    }

    /* access modifiers changed from: protected */
    public boolean isEnded() {
        return this.inputStreamEnded;
    }

    /* access modifiers changed from: protected */
    public boolean isReady() {
        return true;
    }

    private void invokeRenderer(String text) {
        if (!Util.areEqual(this.lastRenderedCaption, text)) {
            this.lastRenderedCaption = text;
            if (this.textRendererHandler != null) {
                this.textRendererHandler.obtainMessage(0, text).sendToTarget();
            } else {
                invokeRendererInternal(text);
            }
        }
    }

    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 0:
                invokeRendererInternal((String) msg.obj);
                return true;
            default:
                return false;
        }
    }

    private void invokeRendererInternal(String cueText) {
        if (cueText == null) {
            this.textRenderer.onCues(Collections.emptyList());
        } else {
            this.textRenderer.onCues(Collections.singletonList(new Cue(cueText)));
        }
    }

    private void maybeParsePendingSample(long positionUs) {
        if (this.sampleHolder.timeUs <= 5000000 + positionUs) {
            ClosedCaptionList holder = this.eia608Parser.parse(this.sampleHolder);
            clearPendingSample();
            if (holder != null) {
                this.pendingCaptionLists.add(holder);
            }
        }
    }

    private void consumeCaptionList(ClosedCaptionList captionList) {
        if (captionBufferSize != 0) {
            boolean isRepeatableControl = false;
            for (ClosedCaption caption2 : captionList.captions) {
                if (caption2.type == 0) {
                    ClosedCaptionCtrl captionCtrl = (ClosedCaptionCtrl) caption2;
                    isRepeatableControl = captionBufferSize == 1 && captionCtrl.isRepeatable();
                    if (!isRepeatableControl || this.repeatableControl == null || this.repeatableControl.cc1 != captionCtrl.cc1 || this.repeatableControl.cc2 != captionCtrl.cc2) {
                        if (isRepeatableControl) {
                            this.repeatableControl = captionCtrl;
                        }
                        if (captionCtrl.isMiscCode()) {
                            handleMiscCode(captionCtrl);
                        } else if (captionCtrl.isPreambleAddressCode()) {
                            handlePreambleAddressCode();
                        }
                    } else {
                        this.repeatableControl = null;
                    }
                } else {
                    handleText((ClosedCaptionText) caption2);
                }
            }
            if (!isRepeatableControl) {
                this.repeatableControl = null;
            }
            if (this.captionMode == 1 || this.captionMode == 3) {
                this.caption = getDisplayCaption();
            }
        }
    }

    private void handleText(ClosedCaptionText captionText) {
        if (this.captionMode != 0) {
            this.captionStringBuilder.append(captionText.text);
        }
    }

    private void handleMiscCode(ClosedCaptionCtrl captionCtrl) {
        switch (captionCtrl.cc2) {
            case 32:
                setCaptionMode(2);
                return;
            case 37:
                this.captionRowCount = 2;
                setCaptionMode(1);
                return;
            case 38:
                this.captionRowCount = 3;
                setCaptionMode(1);
                return;
            case 39:
                this.captionRowCount = 4;
                setCaptionMode(1);
                return;
            case 41:
                setCaptionMode(3);
                return;
            default:
                if (this.captionMode != 0) {
                    switch (captionCtrl.cc2) {
                        case 33:
                            if (this.captionStringBuilder.length() > 0) {
                                this.captionStringBuilder.setLength(this.captionStringBuilder.length() - 1);
                                return;
                            }
                            return;
                        case 44:
                            this.caption = null;
                            if (this.captionMode == 1 || this.captionMode == 3) {
                                this.captionStringBuilder.setLength(0);
                                return;
                            }
                            return;
                        case 45:
                            maybeAppendNewline();
                            return;
                        case 46:
                            this.captionStringBuilder.setLength(0);
                            return;
                        case 47:
                            this.caption = getDisplayCaption();
                            this.captionStringBuilder.setLength(0);
                            return;
                        default:
                            return;
                    }
                } else {
                    return;
                }
        }
    }

    private void handlePreambleAddressCode() {
        maybeAppendNewline();
    }

    private void setCaptionMode(int captionMode2) {
        if (this.captionMode != captionMode2) {
            this.captionMode = captionMode2;
            this.captionStringBuilder.setLength(0);
            if (captionMode2 == 1 || captionMode2 == 0) {
                this.caption = null;
            }
        }
    }

    private void maybeAppendNewline() {
        int buildLength = this.captionStringBuilder.length();
        if (buildLength > 0 && this.captionStringBuilder.charAt(buildLength - 1) != 10) {
            this.captionStringBuilder.append(10);
        }
    }

    private String getDisplayCaption() {
        boolean endsWithNewline;
        int endIndex;
        int buildLength = this.captionStringBuilder.length();
        if (buildLength == 0) {
            return null;
        }
        if (this.captionStringBuilder.charAt(buildLength - 1) == 10) {
            endsWithNewline = true;
        } else {
            endsWithNewline = false;
        }
        if (buildLength == 1 && endsWithNewline) {
            return null;
        }
        if (endsWithNewline) {
            endIndex = buildLength - 1;
        } else {
            endIndex = buildLength;
        }
        if (this.captionMode != 1) {
            return this.captionStringBuilder.substring(0, endIndex);
        }
        int startIndex = 0;
        int searchBackwardFromIndex = endIndex;
        for (int i = 0; i < this.captionRowCount && searchBackwardFromIndex != -1; i++) {
            searchBackwardFromIndex = this.captionStringBuilder.lastIndexOf("\n", searchBackwardFromIndex - 1);
        }
        if (searchBackwardFromIndex != -1) {
            startIndex = searchBackwardFromIndex + 1;
        }
        this.captionStringBuilder.delete(0, startIndex);
        return this.captionStringBuilder.substring(0, endIndex - startIndex);
    }

    private void clearPendingSample() {
        this.sampleHolder.timeUs = -1;
        this.sampleHolder.clearData();
    }

    private boolean isSamplePending() {
        return this.sampleHolder.timeUs != -1;
    }
}
