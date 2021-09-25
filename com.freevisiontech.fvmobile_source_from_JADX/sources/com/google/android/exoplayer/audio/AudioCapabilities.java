package com.google.android.exoplayer.audio;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import java.util.Arrays;

@TargetApi(21)
public final class AudioCapabilities {
    public static final AudioCapabilities DEFAULT_AUDIO_CAPABILITIES = new AudioCapabilities(new int[]{2}, 2);
    private final int maxChannelCount;
    private final int[] supportedEncodings;

    public static AudioCapabilities getCapabilities(Context context) {
        return getCapabilities(context.registerReceiver((BroadcastReceiver) null, new IntentFilter("android.media.action.HDMI_AUDIO_PLUG")));
    }

    @SuppressLint({"InlinedApi"})
    static AudioCapabilities getCapabilities(Intent intent) {
        if (intent == null || intent.getIntExtra("android.media.extra.AUDIO_PLUG_STATE", 0) == 0) {
            return DEFAULT_AUDIO_CAPABILITIES;
        }
        return new AudioCapabilities(intent.getIntArrayExtra("android.media.extra.ENCODINGS"), intent.getIntExtra("android.media.extra.MAX_CHANNEL_COUNT", 0));
    }

    AudioCapabilities(int[] supportedEncodings2, int maxChannelCount2) {
        if (supportedEncodings2 != null) {
            this.supportedEncodings = Arrays.copyOf(supportedEncodings2, supportedEncodings2.length);
            Arrays.sort(this.supportedEncodings);
        } else {
            this.supportedEncodings = new int[0];
        }
        this.maxChannelCount = maxChannelCount2;
    }

    public boolean supportsEncoding(int encoding) {
        return Arrays.binarySearch(this.supportedEncodings, encoding) >= 0;
    }

    public int getMaxChannelCount() {
        return this.maxChannelCount;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AudioCapabilities)) {
            return false;
        }
        AudioCapabilities audioCapabilities = (AudioCapabilities) other;
        if (!Arrays.equals(this.supportedEncodings, audioCapabilities.supportedEncodings) || this.maxChannelCount != audioCapabilities.maxChannelCount) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.maxChannelCount + (Arrays.hashCode(this.supportedEncodings) * 31);
    }

    public String toString() {
        return "AudioCapabilities[maxChannelCount=" + this.maxChannelCount + ", supportedEncodings=" + Arrays.toString(this.supportedEncodings) + "]";
    }
}
