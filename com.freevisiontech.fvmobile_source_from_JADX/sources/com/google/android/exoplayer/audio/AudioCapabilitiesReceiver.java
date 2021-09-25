package com.google.android.exoplayer.audio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.Util;

public final class AudioCapabilitiesReceiver {
    AudioCapabilities audioCapabilities;
    private final Context context;
    /* access modifiers changed from: private */
    public final Listener listener;
    private final BroadcastReceiver receiver;

    public interface Listener {
        void onAudioCapabilitiesChanged(AudioCapabilities audioCapabilities);
    }

    public AudioCapabilitiesReceiver(Context context2, Listener listener2) {
        HdmiAudioPlugBroadcastReceiver hdmiAudioPlugBroadcastReceiver;
        this.context = (Context) Assertions.checkNotNull(context2);
        this.listener = (Listener) Assertions.checkNotNull(listener2);
        if (Util.SDK_INT >= 21) {
            hdmiAudioPlugBroadcastReceiver = new HdmiAudioPlugBroadcastReceiver();
        } else {
            hdmiAudioPlugBroadcastReceiver = null;
        }
        this.receiver = hdmiAudioPlugBroadcastReceiver;
    }

    public AudioCapabilities register() {
        Intent stickyIntent;
        if (this.receiver == null) {
            stickyIntent = null;
        } else {
            stickyIntent = this.context.registerReceiver(this.receiver, new IntentFilter("android.media.action.HDMI_AUDIO_PLUG"));
        }
        this.audioCapabilities = AudioCapabilities.getCapabilities(stickyIntent);
        return this.audioCapabilities;
    }

    public void unregister() {
        if (this.receiver != null) {
            this.context.unregisterReceiver(this.receiver);
        }
    }

    private final class HdmiAudioPlugBroadcastReceiver extends BroadcastReceiver {
        private HdmiAudioPlugBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (!isInitialStickyBroadcast()) {
                AudioCapabilities newAudioCapabilities = AudioCapabilities.getCapabilities(intent);
                if (!newAudioCapabilities.equals(AudioCapabilitiesReceiver.this.audioCapabilities)) {
                    AudioCapabilitiesReceiver.this.audioCapabilities = newAudioCapabilities;
                    AudioCapabilitiesReceiver.this.listener.onAudioCapabilitiesChanged(newAudioCapabilities);
                }
            }
        }
    }
}
