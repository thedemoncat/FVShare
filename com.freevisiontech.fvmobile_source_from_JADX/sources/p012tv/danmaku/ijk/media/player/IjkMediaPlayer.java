package p012tv.danmaku.ijk.media.player;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import p012tv.danmaku.ijk.media.player.IjkMediaMeta;
import p012tv.danmaku.ijk.media.player.annotations.AccessedByNative;
import p012tv.danmaku.ijk.media.player.annotations.CalledByNative;
import p012tv.danmaku.ijk.media.player.misc.IAndroidIO;
import p012tv.danmaku.ijk.media.player.misc.IMediaDataSource;
import p012tv.danmaku.ijk.media.player.misc.IjkTrackInfo;
import p012tv.danmaku.ijk.media.player.pragma.DebugLog;

/* renamed from: tv.danmaku.ijk.media.player.IjkMediaPlayer */
public final class IjkMediaPlayer extends AbstractMediaPlayer {
    public static final int FFP_PROPV_DECODER_AVCODEC = 1;
    public static final int FFP_PROPV_DECODER_MEDIACODEC = 2;
    public static final int FFP_PROPV_DECODER_UNKNOWN = 0;
    public static final int FFP_PROPV_DECODER_VIDEOTOOLBOX = 3;
    public static final int FFP_PROP_FLOAT_DROP_FRAME_RATE = 10007;
    public static final int FFP_PROP_FLOAT_PLAYBACK_RATE = 10003;
    public static final int FFP_PROP_INT64_ASYNC_STATISTIC_BUF_BACKWARDS = 20201;
    public static final int FFP_PROP_INT64_ASYNC_STATISTIC_BUF_CAPACITY = 20203;
    public static final int FFP_PROP_INT64_ASYNC_STATISTIC_BUF_FORWARDS = 20202;
    public static final int FFP_PROP_INT64_AUDIO_CACHED_BYTES = 20008;
    public static final int FFP_PROP_INT64_AUDIO_CACHED_DURATION = 20006;
    public static final int FFP_PROP_INT64_AUDIO_CACHED_PACKETS = 20010;
    public static final int FFP_PROP_INT64_AUDIO_DECODER = 20004;
    public static final int FFP_PROP_INT64_BIT_RATE = 20100;
    public static final int FFP_PROP_INT64_CACHE_STATISTIC_COUNT_BYTES = 20208;
    public static final int FFP_PROP_INT64_CACHE_STATISTIC_FILE_FORWARDS = 20206;
    public static final int FFP_PROP_INT64_CACHE_STATISTIC_FILE_POS = 20207;
    public static final int FFP_PROP_INT64_CACHE_STATISTIC_PHYSICAL_POS = 20205;
    public static final int FFP_PROP_INT64_IMMEDIATE_RECONNECT = 20211;
    public static final int FFP_PROP_INT64_LATEST_SEEK_LOAD_DURATION = 20300;
    public static final int FFP_PROP_INT64_LOGICAL_FILE_SIZE = 20209;
    public static final int FFP_PROP_INT64_SELECTED_AUDIO_STREAM = 20002;
    public static final int FFP_PROP_INT64_SELECTED_TIMEDTEXT_STREAM = 20011;
    public static final int FFP_PROP_INT64_SELECTED_VIDEO_STREAM = 20001;
    public static final int FFP_PROP_INT64_SHARE_CACHE_DATA = 20210;
    public static final int FFP_PROP_INT64_TCP_SPEED = 20200;
    public static final int FFP_PROP_INT64_TRAFFIC_STATISTIC_BYTE_COUNT = 20204;
    public static final int FFP_PROP_INT64_VIDEO_CACHED_BYTES = 20007;
    public static final int FFP_PROP_INT64_VIDEO_CACHED_DURATION = 20005;
    public static final int FFP_PROP_INT64_VIDEO_CACHED_PACKETS = 20009;
    public static final int FFP_PROP_INT64_VIDEO_DECODER = 20003;
    public static final int IJK_LOG_DEBUG = 3;
    public static final int IJK_LOG_DEFAULT = 1;
    public static final int IJK_LOG_ERROR = 6;
    public static final int IJK_LOG_FATAL = 7;
    public static final int IJK_LOG_INFO = 4;
    public static final int IJK_LOG_SILENT = 8;
    public static final int IJK_LOG_UNKNOWN = 0;
    public static final int IJK_LOG_VERBOSE = 2;
    public static final int IJK_LOG_WARN = 5;
    private static final int MEDIA_BUFFERING_UPDATE = 3;
    private static final int MEDIA_ERROR = 100;
    private static final int MEDIA_INFO = 200;
    private static final int MEDIA_NOP = 0;
    private static final int MEDIA_PLAYBACK_COMPLETE = 2;
    private static final int MEDIA_PREPARED = 1;
    private static final int MEDIA_SEEK_COMPLETE = 4;
    protected static final int MEDIA_SET_VIDEO_SAR = 10001;
    private static final int MEDIA_SET_VIDEO_SIZE = 5;
    private static final int MEDIA_TIMED_TEXT = 99;
    public static final int OPT_CATEGORY_CODEC = 2;
    public static final int OPT_CATEGORY_FORMAT = 1;
    public static final int OPT_CATEGORY_PLAYER = 4;
    public static final int OPT_CATEGORY_SWS = 3;
    public static final int PROP_FLOAT_VIDEO_DECODE_FRAMES_PER_SECOND = 10001;
    public static final int PROP_FLOAT_VIDEO_OUTPUT_FRAMES_PER_SECOND = 10002;
    public static final int SDL_FCC_RV16 = 909203026;
    public static final int SDL_FCC_RV32 = 842225234;
    public static final int SDL_FCC_YV12 = 842094169;
    /* access modifiers changed from: private */
    public static final String TAG = IjkMediaPlayer.class.getName();
    private static volatile boolean mIsLibLoaded = false;
    private static volatile boolean mIsNativeInitialized = false;
    private static final IjkLibLoader sLocalLibLoader = new IjkLibLoader() {
        public void loadLibrary(String libName) throws UnsatisfiedLinkError, SecurityException {
            System.loadLibrary(libName);
        }
    };
    private String mDataSource;
    private EventHandler mEventHandler;
    @AccessedByNative
    private int mListenerContext;
    @AccessedByNative
    private long mNativeAndroidIO;
    @AccessedByNative
    private long mNativeMediaDataSource;
    /* access modifiers changed from: private */
    @AccessedByNative
    public long mNativeMediaPlayer;
    @AccessedByNative
    private int mNativeSurfaceTexture;
    private OnControlMessageListener mOnControlMessageListener;
    private OnMediaCodecSelectListener mOnMediaCodecSelectListener;
    private OnNativeInvokeListener mOnNativeInvokeListener;
    private boolean mScreenOnWhilePlaying;
    private boolean mStayAwake;
    private SurfaceHolder mSurfaceHolder;
    /* access modifiers changed from: private */
    public int mVideoHeight;
    /* access modifiers changed from: private */
    public int mVideoSarDen;
    /* access modifiers changed from: private */
    public int mVideoSarNum;
    /* access modifiers changed from: private */
    public int mVideoWidth;
    private PowerManager.WakeLock mWakeLock;

    /* renamed from: tv.danmaku.ijk.media.player.IjkMediaPlayer$OnControlMessageListener */
    public interface OnControlMessageListener {
        String onControlResolveSegmentUrl(int i);
    }

    /* renamed from: tv.danmaku.ijk.media.player.IjkMediaPlayer$OnMediaCodecSelectListener */
    public interface OnMediaCodecSelectListener {
        String onMediaCodecSelect(IMediaPlayer iMediaPlayer, String str, int i, int i2);
    }

    /* renamed from: tv.danmaku.ijk.media.player.IjkMediaPlayer$OnNativeInvokeListener */
    public interface OnNativeInvokeListener {
        public static final String ARG_ERROR = "error";
        public static final String ARG_FAMILIY = "family";
        public static final String ARG_FD = "fd";
        public static final String ARG_FILE_SIZE = "file_size";
        public static final String ARG_HTTP_CODE = "http_code";
        public static final String ARG_IP = "ip";
        public static final String ARG_OFFSET = "offset";
        public static final String ARG_PORT = "port";
        public static final String ARG_RETRY_COUNTER = "retry_counter";
        public static final String ARG_SEGMENT_INDEX = "segment_index";
        public static final String ARG_URL = "url";
        public static final int CTRL_DID_TCP_OPEN = 131074;
        public static final int CTRL_WILL_CONCAT_RESOLVE_SEGMENT = 131079;
        public static final int CTRL_WILL_HTTP_OPEN = 131075;
        public static final int CTRL_WILL_LIVE_OPEN = 131077;
        public static final int CTRL_WILL_TCP_OPEN = 131073;
        public static final int EVENT_DID_HTTP_OPEN = 2;
        public static final int EVENT_DID_HTTP_SEEK = 4;
        public static final int EVENT_WILL_HTTP_OPEN = 1;
        public static final int EVENT_WILL_HTTP_SEEK = 3;

        boolean onNativeInvoke(int i, Bundle bundle);
    }

    private native String _getAudioCodecInfo();

    private static native String _getColorFormatName(int i);

    private native int _getLoopCount();

    private native Bundle _getMediaMeta();

    private native float _getPropertyFloat(int i, float f);

    private native long _getPropertyLong(int i, long j);

    private native String _getVideoCodecInfo();

    private native void _pause() throws IllegalStateException;

    private native void _release();

    private native void _reset();

    private native void _setAndroidIOCallback(IAndroidIO iAndroidIO) throws IllegalArgumentException, SecurityException, IllegalStateException;

    private native void _setDataSource(String str, String[] strArr, String[] strArr2) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException;

    private native void _setDataSource(IMediaDataSource iMediaDataSource) throws IllegalArgumentException, SecurityException, IllegalStateException;

    private native void _setDataSourceFd(int i) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException;

    private native void _setFrameAtTime(String str, long j, long j2, int i, int i2) throws IllegalArgumentException, IllegalStateException;

    private native void _setLoopCount(int i);

    private native void _setOption(int i, String str, long j);

    private native void _setOption(int i, String str, String str2);

    private native void _setPropertyFloat(int i, float f);

    private native void _setPropertyLong(int i, long j);

    private native void _setStreamSelected(int i, boolean z);

    private native void _setVideoSurface(Surface surface);

    private native void _start() throws IllegalStateException;

    private native void _stop() throws IllegalStateException;

    private native void native_finalize();

    private static native void native_init();

    private native void native_message_loop(Object obj);

    public static native void native_profileBegin(String str);

    public static native void native_profileEnd();

    public static native void native_setLogLevel(int i);

    private native void native_setup(Object obj);

    public native void _prepareAsync() throws IllegalStateException;

    public native int getAudioSessionId();

    public native long getCurrentPosition();

    public native long getDuration();

    public native boolean isPlaying();

    public native void seekTo(long j) throws IllegalStateException;

    public native void setVolume(float f, float f2);

    public static void loadLibrariesOnce(IjkLibLoader libLoader) {
        synchronized (IjkMediaPlayer.class) {
            if (!mIsLibLoaded) {
                if (libLoader == null) {
                    libLoader = sLocalLibLoader;
                }
                libLoader.loadLibrary("ijkffmpeg");
                libLoader.loadLibrary("ijksdl");
                libLoader.loadLibrary("ijkplayer");
                mIsLibLoaded = true;
            }
        }
    }

    private static void initNativeOnce() {
        synchronized (IjkMediaPlayer.class) {
            if (!mIsNativeInitialized) {
                native_init();
                mIsNativeInitialized = true;
            }
        }
    }

    public IjkMediaPlayer() {
        this(sLocalLibLoader);
    }

    public IjkMediaPlayer(IjkLibLoader libLoader) {
        this.mWakeLock = null;
        initPlayer(libLoader);
    }

    private void initPlayer(IjkLibLoader libLoader) {
        loadLibrariesOnce(libLoader);
        initNativeOnce();
        Looper looper = Looper.myLooper();
        if (looper != null) {
            this.mEventHandler = new EventHandler(this, looper);
        } else {
            Looper looper2 = Looper.getMainLooper();
            if (looper2 != null) {
                this.mEventHandler = new EventHandler(this, looper2);
            } else {
                this.mEventHandler = null;
            }
        }
        native_setup(new WeakReference(this));
    }

    public void setDisplay(SurfaceHolder sh) {
        Surface surface;
        this.mSurfaceHolder = sh;
        if (sh != null) {
            surface = sh.getSurface();
        } else {
            surface = null;
        }
        _setVideoSurface(surface);
        updateSurfaceScreenOn();
    }

    public void setSurface(Surface surface) {
        if (this.mScreenOnWhilePlaying && surface != null) {
            DebugLog.m1558w(TAG, "setScreenOnWhilePlaying(true) is ineffective for Surface");
        }
        this.mSurfaceHolder = null;
        _setVideoSurface(surface);
        updateSurfaceScreenOn();
    }

    public void setDataSource(Context context, Uri uri) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        setDataSource(context, uri, (Map<String, String>) null);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0080, code lost:
        android.util.Log.d(TAG, "Couldn't open file on client side, trying server side");
        setDataSource(r11.toString(), r12);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:?, code lost:
        return;
     */
    @android.annotation.TargetApi(14)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setDataSource(android.content.Context r10, android.net.Uri r11, java.util.Map<java.lang.String, java.lang.String> r12) throws java.io.IOException, java.lang.IllegalArgumentException, java.lang.SecurityException, java.lang.IllegalStateException {
        /*
            r9 = this;
            java.lang.String r8 = r11.getScheme()
            java.lang.String r0 = "file"
            boolean r0 = r0.equals(r8)
            if (r0 == 0) goto L_0x0015
            java.lang.String r0 = r11.getPath()
            r9.setDataSource((java.lang.String) r0)
        L_0x0014:
            return
        L_0x0015:
            java.lang.String r0 = "content"
            boolean r0 = r0.equals(r8)
            if (r0 == 0) goto L_0x003e
            java.lang.String r0 = "settings"
            java.lang.String r1 = r11.getAuthority()
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x003e
            int r0 = android.media.RingtoneManager.getDefaultType(r11)
            android.net.Uri r11 = android.media.RingtoneManager.getActualDefaultRingtoneUri(r10, r0)
            if (r11 != 0) goto L_0x003e
            java.io.FileNotFoundException r0 = new java.io.FileNotFoundException
            java.lang.String r1 = "Failed to resolve default ringtone"
            r0.<init>(r1)
            throw r0
        L_0x003e:
            r6 = 0
            android.content.ContentResolver r7 = r10.getContentResolver()     // Catch:{ SecurityException -> 0x007a, IOException -> 0x0090, all -> 0x0097 }
            java.lang.String r0 = "r"
            android.content.res.AssetFileDescriptor r6 = r7.openAssetFileDescriptor(r11, r0)     // Catch:{ SecurityException -> 0x007a, IOException -> 0x0090, all -> 0x0097 }
            if (r6 != 0) goto L_0x0052
            if (r6 == 0) goto L_0x0014
            r6.close()
            goto L_0x0014
        L_0x0052:
            long r0 = r6.getDeclaredLength()     // Catch:{ SecurityException -> 0x007a, IOException -> 0x0090, all -> 0x0097 }
            r2 = 0
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 >= 0) goto L_0x0069
            java.io.FileDescriptor r0 = r6.getFileDescriptor()     // Catch:{ SecurityException -> 0x007a, IOException -> 0x0090, all -> 0x0097 }
            r9.setDataSource((java.io.FileDescriptor) r0)     // Catch:{ SecurityException -> 0x007a, IOException -> 0x0090, all -> 0x0097 }
        L_0x0063:
            if (r6 == 0) goto L_0x0014
            r6.close()
            goto L_0x0014
        L_0x0069:
            java.io.FileDescriptor r1 = r6.getFileDescriptor()     // Catch:{ SecurityException -> 0x007a, IOException -> 0x0090, all -> 0x0097 }
            long r2 = r6.getStartOffset()     // Catch:{ SecurityException -> 0x007a, IOException -> 0x0090, all -> 0x0097 }
            long r4 = r6.getDeclaredLength()     // Catch:{ SecurityException -> 0x007a, IOException -> 0x0090, all -> 0x0097 }
            r0 = r9
            r0.setDataSource((java.io.FileDescriptor) r1, (long) r2, (long) r4)     // Catch:{ SecurityException -> 0x007a, IOException -> 0x0090, all -> 0x0097 }
            goto L_0x0063
        L_0x007a:
            r0 = move-exception
            if (r6 == 0) goto L_0x0080
            r6.close()
        L_0x0080:
            java.lang.String r0 = TAG
            java.lang.String r1 = "Couldn't open file on client side, trying server side"
            android.util.Log.d(r0, r1)
            java.lang.String r0 = r11.toString()
            r9.setDataSource((java.lang.String) r0, (java.util.Map<java.lang.String, java.lang.String>) r12)
            goto L_0x0014
        L_0x0090:
            r0 = move-exception
            if (r6 == 0) goto L_0x0080
            r6.close()
            goto L_0x0080
        L_0x0097:
            r0 = move-exception
            if (r6 == 0) goto L_0x009d
            r6.close()
        L_0x009d:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: p012tv.danmaku.ijk.media.player.IjkMediaPlayer.setDataSource(android.content.Context, android.net.Uri, java.util.Map):void");
    }

    public void setDataSource(String path) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        this.mDataSource = path;
        _setDataSource(path, (String[]) null, (String[]) null);
    }

    public void setDataSource(String path, Map<String, String> headers) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        if (headers != null && !headers.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                sb.append(entry.getKey());
                sb.append(":");
                if (!TextUtils.isEmpty(entry.getValue())) {
                    sb.append(entry.getValue());
                }
                sb.append("\r\n");
                setOption(1, "headers", sb.toString());
                setOption(1, "protocol_whitelist", "async,cache,crypto,file,http,https,ijkhttphook,ijkinject,ijklivehook,ijklongurl,ijksegment,ijktcphook,pipe,rtp,tcp,tls,udp,ijkurlhook,data");
            }
        }
        setDataSource(path);
    }

    @TargetApi(13)
    public void setDataSource(FileDescriptor fd) throws IOException, IllegalArgumentException, IllegalStateException {
        if (Build.VERSION.SDK_INT < 12) {
            try {
                Field f = fd.getClass().getDeclaredField("descriptor");
                f.setAccessible(true);
                _setDataSourceFd(f.getInt(fd));
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e2) {
                throw new RuntimeException(e2);
            }
        } else {
            ParcelFileDescriptor pfd = ParcelFileDescriptor.dup(fd);
            try {
                _setDataSourceFd(pfd.getFd());
            } finally {
                pfd.close();
            }
        }
    }

    private void setDataSource(FileDescriptor fd, long offset, long length) throws IOException, IllegalArgumentException, IllegalStateException {
        setDataSource(fd);
    }

    public void setDataSource(IMediaDataSource mediaDataSource) throws IllegalArgumentException, SecurityException, IllegalStateException {
        _setDataSource(mediaDataSource);
    }

    public void setAndroidIOCallback(IAndroidIO androidIO) throws IllegalArgumentException, SecurityException, IllegalStateException {
        _setAndroidIOCallback(androidIO);
    }

    public String getDataSource() {
        return this.mDataSource;
    }

    public void prepareAsync() throws IllegalStateException {
        _prepareAsync();
    }

    public void start() throws IllegalStateException {
        stayAwake(true);
        _start();
    }

    public void stop() throws IllegalStateException {
        stayAwake(false);
        _stop();
    }

    public void pause() throws IllegalStateException {
        stayAwake(false);
        _pause();
    }

    @SuppressLint({"Wakelock"})
    public void setWakeMode(Context context, int mode) {
        boolean washeld = false;
        if (this.mWakeLock != null) {
            if (this.mWakeLock.isHeld()) {
                washeld = true;
                this.mWakeLock.release();
            }
            this.mWakeLock = null;
        }
        this.mWakeLock = ((PowerManager) context.getSystemService("power")).newWakeLock(536870912 | mode, IjkMediaPlayer.class.getName());
        this.mWakeLock.setReferenceCounted(false);
        if (washeld) {
            this.mWakeLock.acquire();
        }
    }

    public void setScreenOnWhilePlaying(boolean screenOn) {
        if (this.mScreenOnWhilePlaying != screenOn) {
            if (screenOn && this.mSurfaceHolder == null) {
                DebugLog.m1558w(TAG, "setScreenOnWhilePlaying(true) is ineffective without a SurfaceHolder");
            }
            this.mScreenOnWhilePlaying = screenOn;
            updateSurfaceScreenOn();
        }
    }

    /* access modifiers changed from: private */
    @SuppressLint({"Wakelock"})
    public void stayAwake(boolean awake) {
        if (this.mWakeLock != null) {
            if (awake && !this.mWakeLock.isHeld()) {
                this.mWakeLock.acquire();
            } else if (!awake && this.mWakeLock.isHeld()) {
                this.mWakeLock.release();
            }
        }
        this.mStayAwake = awake;
        updateSurfaceScreenOn();
    }

    private void updateSurfaceScreenOn() {
        if (this.mSurfaceHolder != null) {
            this.mSurfaceHolder.setKeepScreenOn(this.mScreenOnWhilePlaying && this.mStayAwake);
        }
    }

    public IjkTrackInfo[] getTrackInfo() {
        IjkMediaMeta mediaMeta;
        Bundle bundle = getMediaMeta();
        if (bundle == null || (mediaMeta = IjkMediaMeta.parse(bundle)) == null || mediaMeta.mStreams == null) {
            return null;
        }
        ArrayList<IjkTrackInfo> trackInfos = new ArrayList<>();
        Iterator<IjkMediaMeta.IjkStreamMeta> it = mediaMeta.mStreams.iterator();
        while (it.hasNext()) {
            IjkMediaMeta.IjkStreamMeta streamMeta = it.next();
            IjkTrackInfo trackInfo = new IjkTrackInfo(streamMeta);
            if (streamMeta.mType.equalsIgnoreCase("video")) {
                trackInfo.setTrackType(1);
            } else if (streamMeta.mType.equalsIgnoreCase("audio")) {
                trackInfo.setTrackType(2);
            } else if (streamMeta.mType.equalsIgnoreCase("timedtext")) {
                trackInfo.setTrackType(3);
            }
            trackInfos.add(trackInfo);
        }
        return (IjkTrackInfo[]) trackInfos.toArray(new IjkTrackInfo[trackInfos.size()]);
    }

    public int getSelectedTrack(int trackType) {
        switch (trackType) {
            case 1:
                return (int) _getPropertyLong(FFP_PROP_INT64_SELECTED_VIDEO_STREAM, -1);
            case 2:
                return (int) _getPropertyLong(FFP_PROP_INT64_SELECTED_AUDIO_STREAM, -1);
            case 3:
                return (int) _getPropertyLong(FFP_PROP_INT64_SELECTED_TIMEDTEXT_STREAM, -1);
            default:
                return -1;
        }
    }

    public void selectTrack(int track) {
        _setStreamSelected(track, true);
    }

    public void deselectTrack(int track) {
        _setStreamSelected(track, false);
    }

    public int getVideoWidth() {
        return this.mVideoWidth;
    }

    public int getVideoHeight() {
        return this.mVideoHeight;
    }

    public int getVideoSarNum() {
        return this.mVideoSarNum;
    }

    public int getVideoSarDen() {
        return this.mVideoSarDen;
    }

    public void release() {
        stayAwake(false);
        updateSurfaceScreenOn();
        resetListeners();
        _release();
    }

    public void reset() {
        stayAwake(false);
        _reset();
        this.mEventHandler.removeCallbacksAndMessages((Object) null);
        this.mVideoWidth = 0;
        this.mVideoHeight = 0;
    }

    public void setLooping(boolean looping) {
        int loopCount = looping ? 0 : 1;
        setOption(4, "loop", (long) loopCount);
        _setLoopCount(loopCount);
    }

    public boolean isLooping() {
        if (_getLoopCount() != 1) {
            return true;
        }
        return false;
    }

    public void setSpeed(float speed) {
        _setPropertyFloat(10003, speed);
    }

    public float getSpeed(float speed) {
        return _getPropertyFloat(10003, 0.0f);
    }

    public int getVideoDecoder() {
        return (int) _getPropertyLong(FFP_PROP_INT64_VIDEO_DECODER, 0);
    }

    public float getVideoOutputFramesPerSecond() {
        return _getPropertyFloat(10002, 0.0f);
    }

    public float getVideoDecodeFramesPerSecond() {
        return _getPropertyFloat(10001, 0.0f);
    }

    public long getVideoCachedDuration() {
        return _getPropertyLong(FFP_PROP_INT64_VIDEO_CACHED_DURATION, 0);
    }

    public long getAudioCachedDuration() {
        return _getPropertyLong(FFP_PROP_INT64_AUDIO_CACHED_DURATION, 0);
    }

    public long getVideoCachedBytes() {
        return _getPropertyLong(FFP_PROP_INT64_VIDEO_CACHED_BYTES, 0);
    }

    public long getAudioCachedBytes() {
        return _getPropertyLong(FFP_PROP_INT64_AUDIO_CACHED_BYTES, 0);
    }

    public long getVideoCachedPackets() {
        return _getPropertyLong(FFP_PROP_INT64_VIDEO_CACHED_PACKETS, 0);
    }

    public long getAudioCachedPackets() {
        return _getPropertyLong(FFP_PROP_INT64_AUDIO_CACHED_PACKETS, 0);
    }

    public long getAsyncStatisticBufBackwards() {
        return _getPropertyLong(FFP_PROP_INT64_ASYNC_STATISTIC_BUF_BACKWARDS, 0);
    }

    public long getAsyncStatisticBufForwards() {
        return _getPropertyLong(FFP_PROP_INT64_ASYNC_STATISTIC_BUF_FORWARDS, 0);
    }

    public long getAsyncStatisticBufCapacity() {
        return _getPropertyLong(FFP_PROP_INT64_ASYNC_STATISTIC_BUF_CAPACITY, 0);
    }

    public long getTrafficStatisticByteCount() {
        return _getPropertyLong(FFP_PROP_INT64_TRAFFIC_STATISTIC_BYTE_COUNT, 0);
    }

    public long getCacheStatisticPhysicalPos() {
        return _getPropertyLong(FFP_PROP_INT64_CACHE_STATISTIC_PHYSICAL_POS, 0);
    }

    public long getCacheStatisticFileForwards() {
        return _getPropertyLong(FFP_PROP_INT64_CACHE_STATISTIC_FILE_FORWARDS, 0);
    }

    public long getCacheStatisticFilePos() {
        return _getPropertyLong(FFP_PROP_INT64_CACHE_STATISTIC_FILE_POS, 0);
    }

    public long getCacheStatisticCountBytes() {
        return _getPropertyLong(FFP_PROP_INT64_CACHE_STATISTIC_COUNT_BYTES, 0);
    }

    public long getFileSize() {
        return _getPropertyLong(FFP_PROP_INT64_LOGICAL_FILE_SIZE, 0);
    }

    public long getBitRate() {
        return _getPropertyLong(FFP_PROP_INT64_BIT_RATE, 0);
    }

    public long getTcpSpeed() {
        return _getPropertyLong(FFP_PROP_INT64_TCP_SPEED, 0);
    }

    public long getSeekLoadDuration() {
        return _getPropertyLong(FFP_PROP_INT64_LATEST_SEEK_LOAD_DURATION, 0);
    }

    public float getDropFrameRate() {
        return _getPropertyFloat(10007, 0.0f);
    }

    public MediaInfo getMediaInfo() {
        MediaInfo mediaInfo = new MediaInfo();
        mediaInfo.mMediaPlayerName = "ijkplayer";
        String videoCodecInfo = _getVideoCodecInfo();
        if (!TextUtils.isEmpty(videoCodecInfo)) {
            String[] nodes = videoCodecInfo.split(",");
            if (nodes.length >= 2) {
                mediaInfo.mVideoDecoder = nodes[0];
                mediaInfo.mVideoDecoderImpl = nodes[1];
            } else if (nodes.length >= 1) {
                mediaInfo.mVideoDecoder = nodes[0];
                mediaInfo.mVideoDecoderImpl = "";
            }
        }
        String audioCodecInfo = _getAudioCodecInfo();
        if (!TextUtils.isEmpty(audioCodecInfo)) {
            String[] nodes2 = audioCodecInfo.split(",");
            if (nodes2.length >= 2) {
                mediaInfo.mAudioDecoder = nodes2[0];
                mediaInfo.mAudioDecoderImpl = nodes2[1];
            } else if (nodes2.length >= 1) {
                mediaInfo.mAudioDecoder = nodes2[0];
                mediaInfo.mAudioDecoderImpl = "";
            }
        }
        try {
            mediaInfo.mMeta = IjkMediaMeta.parse(_getMediaMeta());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return mediaInfo;
    }

    public void setLogEnabled(boolean enable) {
    }

    public boolean isPlayable() {
        return true;
    }

    public void setOption(int category, String name, String value) {
        _setOption(category, name, value);
    }

    public void setOption(int category, String name, long value) {
        _setOption(category, name, value);
    }

    public Bundle getMediaMeta() {
        return _getMediaMeta();
    }

    public static String getColorFormatName(int mediaCodecColorFormat) {
        return _getColorFormatName(mediaCodecColorFormat);
    }

    public void setAudioStreamType(int streamtype) {
    }

    public void setKeepInBackground(boolean keepInBackground) {
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        super.finalize();
        native_finalize();
    }

    public void httphookReconnect() {
        _setPropertyLong(FFP_PROP_INT64_IMMEDIATE_RECONNECT, 1);
    }

    public void setCacheShare(int share) {
        _setPropertyLong(FFP_PROP_INT64_SHARE_CACHE_DATA, (long) share);
    }

    /* renamed from: tv.danmaku.ijk.media.player.IjkMediaPlayer$EventHandler */
    private static class EventHandler extends Handler {
        private final WeakReference<IjkMediaPlayer> mWeakPlayer;

        public EventHandler(IjkMediaPlayer mp, Looper looper) {
            super(looper);
            this.mWeakPlayer = new WeakReference<>(mp);
        }

        public void handleMessage(Message msg) {
            IjkMediaPlayer player = (IjkMediaPlayer) this.mWeakPlayer.get();
            if (player == null || player.mNativeMediaPlayer == 0) {
                DebugLog.m1558w(IjkMediaPlayer.TAG, "IjkMediaPlayer went away with unhandled events");
                return;
            }
            switch (msg.what) {
                case 0:
                    return;
                case 1:
                    player.notifyOnPrepared();
                    return;
                case 2:
                    player.stayAwake(false);
                    player.notifyOnCompletion();
                    return;
                case 3:
                    long bufferPosition = (long) msg.arg1;
                    if (bufferPosition < 0) {
                        bufferPosition = 0;
                    }
                    long percent = 0;
                    long duration = player.getDuration();
                    if (duration > 0) {
                        percent = (100 * bufferPosition) / duration;
                    }
                    if (percent >= 100) {
                        percent = 100;
                    }
                    player.notifyOnBufferingUpdate((int) percent);
                    return;
                case 4:
                    player.notifyOnSeekComplete();
                    return;
                case 5:
                    int unused = player.mVideoWidth = msg.arg1;
                    int unused2 = player.mVideoHeight = msg.arg2;
                    player.notifyOnVideoSizeChanged(player.mVideoWidth, player.mVideoHeight, player.mVideoSarNum, player.mVideoSarDen);
                    return;
                case 99:
                    if (msg.obj == null) {
                        player.notifyOnTimedText((IjkTimedText) null);
                        return;
                    } else {
                        player.notifyOnTimedText(new IjkTimedText(new Rect(0, 0, 1, 1), (String) msg.obj));
                        return;
                    }
                case 100:
                    DebugLog.m1552e(IjkMediaPlayer.TAG, "Error (" + msg.arg1 + "," + msg.arg2 + ")");
                    if (!player.notifyOnError(msg.arg1, msg.arg2)) {
                        player.notifyOnCompletion();
                    }
                    player.stayAwake(false);
                    return;
                case 200:
                    switch (msg.arg1) {
                        case 3:
                            DebugLog.m1554i(IjkMediaPlayer.TAG, "Info: MEDIA_INFO_VIDEO_RENDERING_START\n");
                            break;
                    }
                    player.notifyOnInfo(msg.arg1, msg.arg2);
                    return;
                case 10001:
                    int unused3 = player.mVideoSarNum = msg.arg1;
                    int unused4 = player.mVideoSarDen = msg.arg2;
                    player.notifyOnVideoSizeChanged(player.mVideoWidth, player.mVideoHeight, player.mVideoSarNum, player.mVideoSarDen);
                    return;
                default:
                    DebugLog.m1552e(IjkMediaPlayer.TAG, "Unknown message type " + msg.what);
                    return;
            }
        }
    }

    @CalledByNative
    private static void postEventFromNative(Object weakThiz, int what, int arg1, int arg2, Object obj) {
        IjkMediaPlayer mp;
        if (weakThiz != null && (mp = (IjkMediaPlayer) ((WeakReference) weakThiz).get()) != null) {
            if (what == 200 && arg1 == 2) {
                mp.start();
            }
            if (mp.mEventHandler != null) {
                mp.mEventHandler.sendMessage(mp.mEventHandler.obtainMessage(what, arg1, arg2, obj));
            }
        }
    }

    public void setOnControlMessageListener(OnControlMessageListener listener) {
        this.mOnControlMessageListener = listener;
    }

    public void setOnNativeInvokeListener(OnNativeInvokeListener listener) {
        this.mOnNativeInvokeListener = listener;
    }

    @CalledByNative
    private static boolean onNativeInvoke(Object weakThiz, int what, Bundle args) {
        DebugLog.ifmt(TAG, "onNativeInvoke %d", Integer.valueOf(what));
        if (weakThiz == null || !(weakThiz instanceof WeakReference)) {
            throw new IllegalStateException("<null weakThiz>.onNativeInvoke()");
        }
        IjkMediaPlayer player = (IjkMediaPlayer) ((WeakReference) weakThiz).get();
        if (player == null) {
            throw new IllegalStateException("<null weakPlayer>.onNativeInvoke()");
        }
        OnNativeInvokeListener listener = player.mOnNativeInvokeListener;
        if (listener != null && listener.onNativeInvoke(what, args)) {
            return true;
        }
        switch (what) {
            case OnNativeInvokeListener.CTRL_WILL_CONCAT_RESOLVE_SEGMENT /*131079*/:
                OnControlMessageListener onControlMessageListener = player.mOnControlMessageListener;
                if (onControlMessageListener == null) {
                    return false;
                }
                int segmentIndex = args.getInt(OnNativeInvokeListener.ARG_SEGMENT_INDEX, -1);
                if (segmentIndex < 0) {
                    throw new InvalidParameterException("onNativeInvoke(invalid segment index)");
                }
                String newUrl = onControlMessageListener.onControlResolveSegmentUrl(segmentIndex);
                if (newUrl == null) {
                    throw new RuntimeException(new IOException("onNativeInvoke() = <NULL newUrl>"));
                }
                args.putString("url", newUrl);
                return true;
            default:
                return false;
        }
    }

    public void setOnMediaCodecSelectListener(OnMediaCodecSelectListener listener) {
        this.mOnMediaCodecSelectListener = listener;
    }

    public void resetListeners() {
        super.resetListeners();
        this.mOnMediaCodecSelectListener = null;
    }

    @CalledByNative
    private static String onSelectCodec(Object weakThiz, String mimeType, int profile, int level) {
        IjkMediaPlayer player;
        if (weakThiz == null || !(weakThiz instanceof WeakReference) || (player = (IjkMediaPlayer) ((WeakReference) weakThiz).get()) == null) {
            return null;
        }
        OnMediaCodecSelectListener listener = player.mOnMediaCodecSelectListener;
        if (listener == null) {
            listener = DefaultMediaCodecSelector.sInstance;
        }
        return listener.onMediaCodecSelect(player, mimeType, profile, level);
    }

    /* renamed from: tv.danmaku.ijk.media.player.IjkMediaPlayer$DefaultMediaCodecSelector */
    public static class DefaultMediaCodecSelector implements OnMediaCodecSelectListener {
        public static final DefaultMediaCodecSelector sInstance = new DefaultMediaCodecSelector();

        @TargetApi(16)
        public String onMediaCodecSelect(IMediaPlayer mp, String mimeType, int profile, int level) {
            String[] types;
            IjkMediaCodecInfo candidate;
            if (Build.VERSION.SDK_INT < 16 || TextUtils.isEmpty(mimeType)) {
                return null;
            }
            Log.i(IjkMediaPlayer.TAG, String.format(Locale.US, "onSelectCodec: mime=%s, profile=%d, level=%d", new Object[]{mimeType, Integer.valueOf(profile), Integer.valueOf(level)}));
            ArrayList<IjkMediaCodecInfo> candidateCodecList = new ArrayList<>();
            int numCodecs = MediaCodecList.getCodecCount();
            for (int i = 0; i < numCodecs; i++) {
                MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);
                Log.d(IjkMediaPlayer.TAG, String.format(Locale.US, "  found codec: %s", new Object[]{codecInfo.getName()}));
                if (!codecInfo.isEncoder() && (types = codecInfo.getSupportedTypes()) != null) {
                    for (String type : types) {
                        if (!TextUtils.isEmpty(type)) {
                            Log.d(IjkMediaPlayer.TAG, String.format(Locale.US, "    mime: %s", new Object[]{type}));
                            if (type.equalsIgnoreCase(mimeType) && (candidate = IjkMediaCodecInfo.setupCandidate(codecInfo, mimeType)) != null) {
                                candidateCodecList.add(candidate);
                                Log.i(IjkMediaPlayer.TAG, String.format(Locale.US, "candidate codec: %s rank=%d", new Object[]{codecInfo.getName(), Integer.valueOf(candidate.mRank)}));
                                candidate.dumpProfileLevels(mimeType);
                            }
                        }
                    }
                }
            }
            if (candidateCodecList.isEmpty()) {
                return null;
            }
            IjkMediaCodecInfo bestCodec = candidateCodecList.get(0);
            Iterator<IjkMediaCodecInfo> it = candidateCodecList.iterator();
            while (it.hasNext()) {
                IjkMediaCodecInfo codec = it.next();
                if (codec.mRank > bestCodec.mRank) {
                    bestCodec = codec;
                }
            }
            if (bestCodec.mRank < 600) {
                Log.w(IjkMediaPlayer.TAG, String.format(Locale.US, "unaccetable codec: %s", new Object[]{bestCodec.mCodecInfo.getName()}));
                return null;
            }
            Log.i(IjkMediaPlayer.TAG, String.format(Locale.US, "selected codec: %s rank=%d", new Object[]{bestCodec.mCodecInfo.getName(), Integer.valueOf(bestCodec.mRank)}));
            return bestCodec.mCodecInfo.getName();
        }
    }
}
