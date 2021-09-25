package com.p007ny.ijk.upplayer.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.p007ny.ijk.upplayer.C1646R;

/* renamed from: com.ny.ijk.upplayer.application.Settings */
public class Settings {
    public static final int PV_PLAYER__AndroidMediaPlayer = 1;
    public static final int PV_PLAYER__Auto = 0;
    public static final int PV_PLAYER__IjkExoMediaPlayer = 3;
    public static final int PV_PLAYER__IjkMediaPlayer = 2;
    private Context mAppContext;
    private SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mAppContext);

    public Settings(Context context) {
        this.mAppContext = context.getApplicationContext();
    }

    public boolean getEnableBackgroundPlay() {
        return this.mSharedPreferences.getBoolean(this.mAppContext.getString(C1646R.string.pref_key_enable_background_play), false);
    }

    public int getPlayer() {
        return this.mSharedPreferences.getInt(this.mAppContext.getString(C1646R.string.pref_key_player), 2);
    }

    public boolean getUsingMediaCodec() {
        return this.mSharedPreferences.getBoolean(this.mAppContext.getString(C1646R.string.pref_key_using_media_codec), false);
    }

    public boolean getUsingMediaCodecAutoRotate() {
        return this.mSharedPreferences.getBoolean(this.mAppContext.getString(C1646R.string.pref_key_using_media_codec_auto_rotate), false);
    }

    public boolean getMediaCodecHandleResolutionChange() {
        return this.mSharedPreferences.getBoolean(this.mAppContext.getString(C1646R.string.pref_key_media_codec_handle_resolution_change), false);
    }

    public boolean getUsingOpenSLES() {
        return this.mSharedPreferences.getBoolean(this.mAppContext.getString(C1646R.string.pref_key_using_opensl_es), false);
    }

    public String getPixelFormat() {
        return this.mSharedPreferences.getString(this.mAppContext.getString(C1646R.string.pref_key_pixel_format), "");
    }

    public boolean getEnableNoView() {
        return this.mSharedPreferences.getBoolean(this.mAppContext.getString(C1646R.string.pref_key_enable_no_view), false);
    }

    public boolean getEnableSurfaceView() {
        return this.mSharedPreferences.getBoolean(this.mAppContext.getString(C1646R.string.pref_key_enable_surface_view), false);
    }

    public boolean getEnableTextureView() {
        return this.mSharedPreferences.getBoolean(this.mAppContext.getString(C1646R.string.pref_key_enable_texture_view), false);
    }

    public boolean getEnableDetachedSurfaceTextureView() {
        return this.mSharedPreferences.getBoolean(this.mAppContext.getString(C1646R.string.pref_key_enable_detached_surface_texture), false);
    }

    public boolean getUsingMediaDataSource() {
        return this.mSharedPreferences.getBoolean(this.mAppContext.getString(C1646R.string.pref_key_using_mediadatasource), false);
    }

    public String getLastDirectory() {
        return this.mSharedPreferences.getString(this.mAppContext.getString(C1646R.string.pref_key_last_directory), "/");
    }

    public void setLastDirectory(String path) {
        this.mSharedPreferences.edit().putString(this.mAppContext.getString(C1646R.string.pref_key_last_directory), path).apply();
    }
}
