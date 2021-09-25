package com.umeng.analytics.game;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.umeng.analytics.pro.C0064aw;
import com.umeng.analytics.pro.C0067az;
import java.io.Serializable;

/* renamed from: com.umeng.analytics.game.b */
/* compiled from: GameState */
public class C0025b {

    /* renamed from: a */
    public String f72a;

    /* renamed from: b */
    public String f73b;

    /* renamed from: c */
    private Context f74c;

    /* renamed from: d */
    private final String f75d = "um_g_cache";

    /* renamed from: e */
    private final String f76e = "single_level";

    /* renamed from: f */
    private final String f77f = "stat_player_level";

    /* renamed from: g */
    private final String f78g = "stat_game_level";

    /* renamed from: h */
    private C0026a f79h = null;

    public C0025b(Context context) {
        this.f74c = context;
    }

    /* renamed from: a */
    public C0026a mo88a(String str) {
        this.f79h = new C0026a(str);
        this.f79h.mo92a();
        return this.f79h;
    }

    /* renamed from: a */
    public void mo89a() {
        if (this.f79h != null) {
            this.f79h.mo94b();
            SharedPreferences.Editor edit = this.f74c.getSharedPreferences("um_g_cache", 0).edit();
            edit.putString("single_level", C0064aw.m280a((Serializable) this.f79h));
            edit.putString("stat_player_level", this.f73b);
            edit.putString("stat_game_level", this.f72a);
            edit.commit();
        }
    }

    /* renamed from: b */
    public void mo91b() {
        SharedPreferences a = C0067az.m286a(this.f74c, "um_g_cache");
        String string = a.getString("single_level", (String) null);
        if (!TextUtils.isEmpty(string)) {
            this.f79h = (C0026a) C0064aw.m279a(string);
            if (this.f79h != null) {
                this.f79h.mo95c();
            }
        }
        if (TextUtils.isEmpty(this.f73b)) {
            this.f73b = a.getString("stat_player_level", (String) null);
            if (this.f73b == null) {
                SharedPreferences a2 = C0067az.m285a(this.f74c);
                if (a2 != null) {
                    this.f73b = a2.getString("userlevel", (String) null);
                } else {
                    return;
                }
            }
        }
        if (this.f72a == null) {
            this.f72a = a.getString("stat_game_level", (String) null);
        }
    }

    /* renamed from: b */
    public C0026a mo90b(String str) {
        if (this.f79h != null) {
            this.f79h.mo96d();
            if (this.f79h.mo93a(str)) {
                C0026a aVar = this.f79h;
                this.f79h = null;
                return aVar;
            }
        }
        return null;
    }

    /* renamed from: com.umeng.analytics.game.b$a */
    /* compiled from: GameState */
    static class C0026a implements Serializable {

        /* renamed from: a */
        private static final long f80a = 20140327;

        /* renamed from: b */
        private String f81b;

        /* renamed from: c */
        private long f82c;

        /* renamed from: d */
        private long f83d;

        public C0026a(String str) {
            this.f81b = str;
        }

        /* renamed from: a */
        public boolean mo93a(String str) {
            return this.f81b.equals(str);
        }

        /* renamed from: a */
        public void mo92a() {
            this.f83d = System.currentTimeMillis();
        }

        /* renamed from: b */
        public void mo94b() {
            this.f82c += System.currentTimeMillis() - this.f83d;
            this.f83d = 0;
        }

        /* renamed from: c */
        public void mo95c() {
            mo92a();
        }

        /* renamed from: d */
        public void mo96d() {
            mo94b();
        }

        /* renamed from: e */
        public long mo97e() {
            return this.f82c;
        }

        /* renamed from: f */
        public String mo98f() {
            return this.f81b;
        }
    }
}
