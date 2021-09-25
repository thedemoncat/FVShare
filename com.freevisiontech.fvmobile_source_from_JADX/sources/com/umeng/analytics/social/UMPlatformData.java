package com.umeng.analytics.social;

import android.text.TextUtils;
import com.umeng.analytics.pro.C0138bw;
import java.util.Locale;

public class UMPlatformData {

    /* renamed from: a */
    private UMedia f977a;

    /* renamed from: b */
    private String f978b = "";

    /* renamed from: c */
    private String f979c = "";

    /* renamed from: d */
    private String f980d;

    /* renamed from: e */
    private GENDER f981e;

    public enum UMedia {
        SINA_WEIBO {
            public String toString() {
                return "sina";
            }
        },
        TENCENT_WEIBO {
            public String toString() {
                return "tencent";
            }
        },
        TENCENT_QZONE {
            public String toString() {
                return "qzone";
            }
        },
        TENCENT_QQ {
            public String toString() {
                return "qq";
            }
        },
        WEIXIN_FRIENDS {
            public String toString() {
                return "wxsesion";
            }
        },
        WEIXIN_CIRCLE {
            public String toString() {
                return "wxtimeline";
            }
        },
        RENREN {
            public String toString() {
                return "renren";
            }
        },
        DOUBAN {
            public String toString() {
                return "douban";
            }
        }
    }

    public enum GENDER {
        MALE(0) {
            public String toString() {
                return String.format(Locale.US, "Male:%d", new Object[]{Integer.valueOf(this.value)});
            }
        },
        FEMALE(1) {
            public String toString() {
                return String.format(Locale.US, "Female:%d", new Object[]{Integer.valueOf(this.value)});
            }
        };
        
        public int value;

        private GENDER(int i) {
            this.value = i;
        }
    }

    public UMPlatformData(UMedia uMedia, String str) {
        if (uMedia == null || TextUtils.isEmpty(str)) {
            C0138bw.m849e("parameter is not valid");
            return;
        }
        this.f977a = uMedia;
        this.f978b = str;
    }

    public String getWeiboId() {
        return this.f979c;
    }

    public void setWeiboId(String str) {
        this.f979c = str;
    }

    public UMedia getMeida() {
        return this.f977a;
    }

    public String getUsid() {
        return this.f978b;
    }

    public String getName() {
        return this.f980d;
    }

    public void setName(String str) {
        this.f980d = str;
    }

    public GENDER getGender() {
        return this.f981e;
    }

    public void setGender(GENDER gender) {
        this.f981e = gender;
    }

    public boolean isValid() {
        if (this.f977a == null || TextUtils.isEmpty(this.f978b)) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "UMPlatformData [meida=" + this.f977a + ", usid=" + this.f978b + ", weiboId=" + this.f979c + ", name=" + this.f980d + ", gender=" + this.f981e + "]";
    }
}
