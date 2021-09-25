package com.coremedia.iso;

import com.umeng.analytics.pro.C0217dk;
import java.io.ByteArrayOutputStream;

public class Hex {
    private static final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String encodeHex(byte[] data) {
        return encodeHex(data, 0);
    }

    public static String encodeHex(byte[] data, int group) {
        int j;
        int l = data.length;
        char[] out = new char[((group > 0 ? l / group : 0) + (l << 1))];
        int i = 0;
        int j2 = 0;
        while (i < l) {
            if (group <= 0 || i % group != 0 || j2 <= 0) {
                j = j2;
            } else {
                j = j2 + 1;
                out[j2] = '-';
            }
            int j3 = j + 1;
            out[j] = DIGITS[(data[i] & 240) >>> 4];
            out[j3] = DIGITS[data[i] & C0217dk.f723m];
            i++;
            j2 = j3 + 1;
        }
        return new String(out);
    }

    public static byte[] decodeHex(String hexString) {
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        for (int i = 0; i < hexString.length(); i += 2) {
            bas.write(Integer.parseInt(hexString.substring(i, i + 2), 16));
        }
        return bas.toByteArray();
    }
}
