package com.p007ny.ijk.upplayer.media;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.view.View;
import android.widget.TableLayout;
import com.p007ny.ijk.upplayer.C1646R;
import java.util.Locale;
import p012tv.danmaku.ijk.media.player.IMediaPlayer;
import p012tv.danmaku.ijk.media.player.IjkMediaPlayer;
import p012tv.danmaku.ijk.media.player.MediaPlayerProxy;

/* renamed from: com.ny.ijk.upplayer.media.InfoHudViewHolder */
public class InfoHudViewHolder {
    private static final int MSG_UPDATE_HUD = 1;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            IMediaPlayer internal;
            switch (msg.what) {
                case 1:
                    InfoHudViewHolder infoHudViewHolder = InfoHudViewHolder.this;
                    IjkMediaPlayer mp = null;
                    if (InfoHudViewHolder.this.mMediaPlayer != null) {
                        if (InfoHudViewHolder.this.mMediaPlayer instanceof IjkMediaPlayer) {
                            mp = (IjkMediaPlayer) InfoHudViewHolder.this.mMediaPlayer;
                        } else if ((InfoHudViewHolder.this.mMediaPlayer instanceof MediaPlayerProxy) && (internal = ((MediaPlayerProxy) InfoHudViewHolder.this.mMediaPlayer).getInternalMediaPlayer()) != null && (internal instanceof IjkMediaPlayer)) {
                            mp = (IjkMediaPlayer) internal;
                        }
                        if (mp != null) {
                            switch (mp.getVideoDecoder()) {
                                case 1:
                                    InfoHudViewHolder.this.setRowValue(C1646R.string.vdec, "avcodec");
                                    break;
                                case 2:
                                    InfoHudViewHolder.this.setRowValue(C1646R.string.vdec, "MediaCodec");
                                    break;
                                default:
                                    InfoHudViewHolder.this.setRowValue(C1646R.string.vdec, "");
                                    break;
                            }
                            float fpsOutput = mp.getVideoOutputFramesPerSecond();
                            float fpsDecode = mp.getVideoDecodeFramesPerSecond();
                            InfoHudViewHolder.this.setRowValue(C1646R.string.fps, String.format(Locale.US, "%.2f / %.2f", new Object[]{Float.valueOf(fpsDecode), Float.valueOf(fpsOutput)}));
                            long videoCachedDuration = mp.getVideoCachedDuration();
                            long audioCachedDuration = mp.getAudioCachedDuration();
                            long videoCachedBytes = mp.getVideoCachedBytes();
                            long audioCachedBytes = mp.getAudioCachedBytes();
                            long tcpSpeed = mp.getTcpSpeed();
                            long bitRate = mp.getBitRate();
                            long seekLoadDuration = mp.getSeekLoadDuration();
                            InfoHudViewHolder.this.setRowValue(C1646R.string.v_cache, String.format(Locale.US, "%s, %s", new Object[]{InfoHudViewHolder.formatedDurationMilli(videoCachedDuration), InfoHudViewHolder.formatedSize(videoCachedBytes)}));
                            InfoHudViewHolder.this.setRowValue(C1646R.string.a_cache, String.format(Locale.US, "%s, %s", new Object[]{InfoHudViewHolder.formatedDurationMilli(audioCachedDuration), InfoHudViewHolder.formatedSize(audioCachedBytes)}));
                            InfoHudViewHolder.this.setRowValue(C1646R.string.load_cost, String.format(Locale.US, "%d ms", new Object[]{Long.valueOf(InfoHudViewHolder.this.mLoadCost)}));
                            InfoHudViewHolder.this.setRowValue(C1646R.string.seek_cost, String.format(Locale.US, "%d ms", new Object[]{Long.valueOf(InfoHudViewHolder.this.mSeekCost)}));
                            InfoHudViewHolder.this.setRowValue(C1646R.string.seek_load_cost, String.format(Locale.US, "%d ms", new Object[]{Long.valueOf(seekLoadDuration)}));
                            InfoHudViewHolder.this.setRowValue(C1646R.string.tcp_speed, String.format(Locale.US, "%s", new Object[]{InfoHudViewHolder.formatedSpeed(tcpSpeed, 1000)}));
                            InfoHudViewHolder.this.setRowValue(C1646R.string.bit_rate, String.format(Locale.US, "%.2f kbs", new Object[]{Float.valueOf(((float) bitRate) / 1000.0f)}));
                            InfoHudViewHolder.this.mHandler.removeMessages(1);
                            InfoHudViewHolder.this.mHandler.sendEmptyMessageDelayed(1, 500);
                            return;
                        }
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public long mLoadCost = 0;
    /* access modifiers changed from: private */
    public IMediaPlayer mMediaPlayer;
    private SparseArray<View> mRowMap = new SparseArray<>();
    /* access modifiers changed from: private */
    public long mSeekCost = 0;
    private TableLayoutBinder mTableLayoutBinder;

    public InfoHudViewHolder(Context context, TableLayout tableLayout) {
        this.mTableLayoutBinder = new TableLayoutBinder(context, tableLayout);
    }

    private void appendSection(int nameId) {
        this.mTableLayoutBinder.appendSection(nameId);
    }

    private void appendRow(int nameId) {
        this.mRowMap.put(nameId, this.mTableLayoutBinder.appendRow2(nameId, (String) null));
    }

    /* access modifiers changed from: private */
    public void setRowValue(int id, String value) {
        View rowView = this.mRowMap.get(id);
        if (rowView == null) {
            this.mRowMap.put(id, this.mTableLayoutBinder.appendRow2(id, value));
            return;
        }
        this.mTableLayoutBinder.setValueText(rowView, value);
    }

    public void setMediaPlayer(IMediaPlayer mp) {
        this.mMediaPlayer = mp;
        if (this.mMediaPlayer != null) {
            this.mHandler.sendEmptyMessageDelayed(1, 500);
        } else {
            this.mHandler.removeMessages(1);
        }
    }

    /* access modifiers changed from: private */
    public static String formatedDurationMilli(long duration) {
        if (duration >= 1000) {
            return String.format(Locale.US, "%.2f sec", new Object[]{Float.valueOf(((float) duration) / 1000.0f)});
        }
        return String.format(Locale.US, "%d msec", new Object[]{Long.valueOf(duration)});
    }

    /* access modifiers changed from: private */
    public static String formatedSpeed(long bytes, long elapsed_milli) {
        if (elapsed_milli <= 0 || bytes <= 0) {
            return "0 B/s";
        }
        float bytes_per_sec = (((float) bytes) * 1000.0f) / ((float) elapsed_milli);
        if (bytes_per_sec >= 1000000.0f) {
            return String.format(Locale.US, "%.2f MB/s", new Object[]{Float.valueOf((bytes_per_sec / 1000.0f) / 1000.0f)});
        } else if (bytes_per_sec >= 1000.0f) {
            return String.format(Locale.US, "%.1f KB/s", new Object[]{Float.valueOf(bytes_per_sec / 1000.0f)});
        } else {
            return String.format(Locale.US, "%d B/s", new Object[]{Long.valueOf((long) bytes_per_sec)});
        }
    }

    public void updateLoadCost(long time) {
        this.mLoadCost = time;
    }

    public void updateSeekCost(long time) {
        this.mSeekCost = time;
    }

    /* access modifiers changed from: private */
    public static String formatedSize(long bytes) {
        if (bytes >= 100000) {
            return String.format(Locale.US, "%.2f MB", new Object[]{Float.valueOf((((float) bytes) / 1000.0f) / 1000.0f)});
        } else if (bytes >= 100) {
            return String.format(Locale.US, "%.1f KB", new Object[]{Float.valueOf(((float) bytes) / 1000.0f)});
        } else {
            return String.format(Locale.US, "%d B", new Object[]{Long.valueOf(bytes)});
        }
    }
}
