package android.support.p001v4.content.p013pm;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.p001v4.graphics.drawable.IconCompat;
import android.text.TextUtils;
import java.util.Arrays;

/* renamed from: android.support.v4.content.pm.ShortcutInfoCompat */
public class ShortcutInfoCompat {
    /* access modifiers changed from: private */
    public ComponentName mActivity;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public CharSequence mDisabledMessage;
    /* access modifiers changed from: private */
    public IconCompat mIcon;
    /* access modifiers changed from: private */
    public String mId;
    /* access modifiers changed from: private */
    public Intent[] mIntents;
    /* access modifiers changed from: private */
    public CharSequence mLabel;
    /* access modifiers changed from: private */
    public CharSequence mLongLabel;

    private ShortcutInfoCompat() {
    }

    /* access modifiers changed from: package-private */
    @RequiresApi(26)
    public ShortcutInfo toShortcutInfo() {
        ShortcutInfo.Builder builder = new ShortcutInfo.Builder(this.mContext, this.mId).setShortLabel(this.mLabel).setIntents(this.mIntents);
        if (this.mIcon != null) {
            builder.setIcon(this.mIcon.toIcon());
        }
        if (!TextUtils.isEmpty(this.mLongLabel)) {
            builder.setLongLabel(this.mLongLabel);
        }
        if (!TextUtils.isEmpty(this.mDisabledMessage)) {
            builder.setDisabledMessage(this.mDisabledMessage);
        }
        if (this.mActivity != null) {
            builder.setActivity(this.mActivity);
        }
        return builder.build();
    }

    /* access modifiers changed from: package-private */
    public Intent addToIntent(Intent outIntent) {
        outIntent.putExtra("android.intent.extra.shortcut.INTENT", this.mIntents[this.mIntents.length - 1]).putExtra("android.intent.extra.shortcut.NAME", this.mLabel.toString());
        if (this.mIcon != null) {
            this.mIcon.addToShortcutIntent(outIntent);
        }
        return outIntent;
    }

    @NonNull
    public String getId() {
        return this.mId;
    }

    @Nullable
    public ComponentName getActivity() {
        return this.mActivity;
    }

    @NonNull
    public CharSequence getShortLabel() {
        return this.mLabel;
    }

    @Nullable
    public CharSequence getLongLabel() {
        return this.mLongLabel;
    }

    @Nullable
    public CharSequence getDisabledMessage() {
        return this.mDisabledMessage;
    }

    @NonNull
    public Intent getIntent() {
        return this.mIntents[this.mIntents.length - 1];
    }

    @NonNull
    public Intent[] getIntents() {
        return (Intent[]) Arrays.copyOf(this.mIntents, this.mIntents.length);
    }

    /* renamed from: android.support.v4.content.pm.ShortcutInfoCompat$Builder */
    public static class Builder {
        private final ShortcutInfoCompat mInfo = new ShortcutInfoCompat();

        public Builder(@NonNull Context context, @NonNull String id) {
            Context unused = this.mInfo.mContext = context;
            String unused2 = this.mInfo.mId = id;
        }

        @NonNull
        public Builder setShortLabel(@NonNull CharSequence shortLabel) {
            CharSequence unused = this.mInfo.mLabel = shortLabel;
            return this;
        }

        @NonNull
        public Builder setLongLabel(@NonNull CharSequence longLabel) {
            CharSequence unused = this.mInfo.mLongLabel = longLabel;
            return this;
        }

        @NonNull
        public Builder setDisabledMessage(@NonNull CharSequence disabledMessage) {
            CharSequence unused = this.mInfo.mDisabledMessage = disabledMessage;
            return this;
        }

        @NonNull
        public Builder setIntent(@NonNull Intent intent) {
            return setIntents(new Intent[]{intent});
        }

        @NonNull
        public Builder setIntents(@NonNull Intent[] intents) {
            Intent[] unused = this.mInfo.mIntents = intents;
            return this;
        }

        @NonNull
        public Builder setIcon(@NonNull Bitmap icon) {
            return setIcon(IconCompat.createWithBitmap(icon));
        }

        @NonNull
        public Builder setIcon(@DrawableRes int icon) {
            return setIcon(IconCompat.createWithResource(this.mInfo.mContext, icon));
        }

        @NonNull
        public Builder setIcon(IconCompat icon) {
            IconCompat unused = this.mInfo.mIcon = icon;
            return this;
        }

        @NonNull
        public Builder setActivity(@NonNull ComponentName activity) {
            ComponentName unused = this.mInfo.mActivity = activity;
            return this;
        }

        @NonNull
        public ShortcutInfoCompat build() {
            if (TextUtils.isEmpty(this.mInfo.mLabel)) {
                throw new IllegalArgumentException("Shortcut much have a non-empty label");
            } else if (this.mInfo.mIntents != null && this.mInfo.mIntents.length != 0) {
                return this.mInfo;
            } else {
                throw new IllegalArgumentException("Shortcut much have an intent");
            }
        }
    }
}