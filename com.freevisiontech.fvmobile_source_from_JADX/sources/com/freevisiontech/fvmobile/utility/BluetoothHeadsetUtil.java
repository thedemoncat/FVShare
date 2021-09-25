package com.freevisiontech.fvmobile.utility;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.media.AudioManager;

public class BluetoothHeadsetUtil {
    public static void changeBtChannelToSco(Context ctxt, boolean isSco) {
        AudioManager audioManager = (AudioManager) ctxt.getSystemService("audio");
        if (isSco) {
            audioManager.setMode(2);
            audioManager.startBluetoothSco();
            audioManager.setBluetoothScoOn(true);
            audioManager.setSpeakerphoneOn(false);
            return;
        }
        audioManager.setMode(0);
        audioManager.stopBluetoothSco();
        audioManager.setBluetoothScoOn(false);
        audioManager.setSpeakerphoneOn(true);
    }

    public static boolean isHeadSetCanUse() {
        BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
        int a2dp = ba.getProfileConnectionState(2);
        int headset = ba.getProfileConnectionState(1);
        if (a2dp == 2 || headset == 2) {
            return true;
        }
        return false;
    }
}
