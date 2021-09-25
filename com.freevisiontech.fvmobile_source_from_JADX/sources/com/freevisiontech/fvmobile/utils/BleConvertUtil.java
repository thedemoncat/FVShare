package com.freevisiontech.fvmobile.utils;

import java.nio.ByteBuffer;

public class BleConvertUtil {
    public static boolean doesArrayBeginWith(byte[] array, byte[] prefix) {
        if (array.length < prefix.length) {
            return false;
        }
        for (int i = 0; i < prefix.length; i++) {
            if (array[i] != prefix[i]) {
                return false;
            }
        }
        return true;
    }

    public static int getIntFrom2ByteArray(byte[] input) {
        return getIntFromByteArray(new byte[]{0, 0, input[0], input[1]});
    }

    public static int getIntFromByte(byte bite) {
        return bite & 255;
    }

    public static int getIntFromByteArray(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }

    public static long getLongFromByteArray(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getLong();
    }

    public static void invertArray(byte[] array) {
        int size = array.length;
        for (int i = 0; i < size / 2; i++) {
            byte temp = array[i];
            array[i] = array[(size - 1) - i];
            array[(size - 1) - i] = temp;
        }
    }
}
