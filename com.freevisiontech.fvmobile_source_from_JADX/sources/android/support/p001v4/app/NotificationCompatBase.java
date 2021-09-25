package android.support.p001v4.app;

import android.app.PendingIntent;
import android.os.Bundle;
import android.support.annotation.RestrictTo;
import android.support.p001v4.app.RemoteInputCompatBase;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
/* renamed from: android.support.v4.app.NotificationCompatBase */
public class NotificationCompatBase {

    /* renamed from: android.support.v4.app.NotificationCompatBase$Action */
    public static abstract class Action {

        /* renamed from: android.support.v4.app.NotificationCompatBase$Action$Factory */
        public interface Factory {
            Action build(int i, CharSequence charSequence, PendingIntent pendingIntent, Bundle bundle, RemoteInputCompatBase.RemoteInput[] remoteInputArr, RemoteInputCompatBase.RemoteInput[] remoteInputArr2, boolean z);

            Action[] newArray(int i);
        }

        public abstract PendingIntent getActionIntent();

        public abstract boolean getAllowGeneratedReplies();

        public abstract RemoteInputCompatBase.RemoteInput[] getDataOnlyRemoteInputs();

        public abstract Bundle getExtras();

        public abstract int getIcon();

        public abstract RemoteInputCompatBase.RemoteInput[] getRemoteInputs();

        public abstract CharSequence getTitle();
    }

    /* renamed from: android.support.v4.app.NotificationCompatBase$UnreadConversation */
    public static abstract class UnreadConversation {

        /* renamed from: android.support.v4.app.NotificationCompatBase$UnreadConversation$Factory */
        public interface Factory {
            UnreadConversation build(String[] strArr, RemoteInputCompatBase.RemoteInput remoteInput, PendingIntent pendingIntent, PendingIntent pendingIntent2, String[] strArr2, long j);
        }

        /* access modifiers changed from: package-private */
        public abstract long getLatestTimestamp();

        /* access modifiers changed from: package-private */
        public abstract String[] getMessages();

        /* access modifiers changed from: package-private */
        public abstract String getParticipant();

        /* access modifiers changed from: package-private */
        public abstract String[] getParticipants();

        /* access modifiers changed from: package-private */
        public abstract PendingIntent getReadPendingIntent();

        /* access modifiers changed from: package-private */
        public abstract RemoteInputCompatBase.RemoteInput getRemoteInput();

        /* access modifiers changed from: package-private */
        public abstract PendingIntent getReplyPendingIntent();
    }
}
