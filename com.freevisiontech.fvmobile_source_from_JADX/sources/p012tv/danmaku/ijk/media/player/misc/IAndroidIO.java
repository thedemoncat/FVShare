package p012tv.danmaku.ijk.media.player.misc;

import java.io.IOException;

/* renamed from: tv.danmaku.ijk.media.player.misc.IAndroidIO */
public interface IAndroidIO {
    int close() throws IOException;

    int open(String str) throws IOException;

    int read(byte[] bArr, int i) throws IOException;

    long seek(long j, int i) throws IOException;
}
