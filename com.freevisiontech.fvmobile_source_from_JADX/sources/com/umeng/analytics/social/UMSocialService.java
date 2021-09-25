package com.umeng.analytics.social;

import android.content.Context;
import android.os.AsyncTask;
import android.support.p001v4.app.NotificationCompat;
import android.text.TextUtils;
import com.lzy.okgo.cache.CacheEntity;
import org.json.JSONObject;

public abstract class UMSocialService {

    /* renamed from: com.umeng.analytics.social.UMSocialService$b */
    public interface C0296b {
        /* renamed from: a */
        void mo781a();

        /* renamed from: a */
        void mo782a(C0299c cVar, UMPlatformData... uMPlatformDataArr);
    }

    /* renamed from: a */
    private static void m1426a(Context context, C0296b bVar, String str, UMPlatformData... uMPlatformDataArr) {
        if (uMPlatformDataArr != null) {
            try {
                for (UMPlatformData isValid : uMPlatformDataArr) {
                    if (!isValid.isValid()) {
                        throw new C0297a("parameter is not valid.");
                    }
                }
            } catch (Exception e) {
                return;
            }
        }
        new C0295a(C0301e.m1445a(context, str, uMPlatformDataArr), bVar, uMPlatformDataArr).execute(new Void[0]);
    }

    public static void share(Context context, String str, UMPlatformData... uMPlatformDataArr) {
        m1426a(context, (C0296b) null, str, uMPlatformDataArr);
    }

    public static void share(Context context, UMPlatformData... uMPlatformDataArr) {
        m1426a(context, (C0296b) null, (String) null, uMPlatformDataArr);
    }

    /* renamed from: com.umeng.analytics.social.UMSocialService$a */
    private static class C0295a extends AsyncTask<Void, Void, C0299c> {

        /* renamed from: a */
        String f984a;

        /* renamed from: b */
        String f985b;

        /* renamed from: c */
        C0296b f986c;

        /* renamed from: d */
        UMPlatformData[] f987d;

        public C0295a(String[] strArr, C0296b bVar, UMPlatformData[] uMPlatformDataArr) {
            this.f984a = strArr[0];
            this.f985b = strArr[1];
            this.f986c = bVar;
            this.f987d = uMPlatformDataArr;
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            if (this.f986c != null) {
                this.f986c.mo781a();
            }
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public C0299c doInBackground(Void... voidArr) {
            String a;
            int i;
            if (TextUtils.isEmpty(this.f985b)) {
                a = C0298b.m1433a(this.f984a);
            } else {
                a = C0298b.m1434a(this.f984a, this.f985b);
            }
            try {
                JSONObject jSONObject = new JSONObject(a);
                int optInt = jSONObject.optInt("st");
                if (optInt == 0) {
                    i = -404;
                } else {
                    i = optInt;
                }
                C0299c cVar = new C0299c(i);
                String optString = jSONObject.optString(NotificationCompat.CATEGORY_MESSAGE);
                if (!TextUtils.isEmpty(optString)) {
                    cVar.mo787a(optString);
                }
                String optString2 = jSONObject.optString(CacheEntity.DATA);
                if (TextUtils.isEmpty(optString2)) {
                    return cVar;
                }
                cVar.mo789b(optString2);
                return cVar;
            } catch (Exception e) {
                return new C0299c(-99, e);
            }
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public void onPostExecute(C0299c cVar) {
            if (this.f986c != null) {
                this.f986c.mo782a(cVar, this.f987d);
            }
        }
    }
}
