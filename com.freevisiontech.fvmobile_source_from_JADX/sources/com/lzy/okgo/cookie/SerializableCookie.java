package com.lzy.okgo.cookie;

import android.content.ContentValues;
import android.database.Cursor;
import com.lzy.okgo.utils.OkLogger;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Locale;
import okhttp3.Cookie;

public class SerializableCookie implements Serializable {
    public static final String COOKIE = "cookie";
    public static final String DOMAIN = "domain";
    public static final String HOST = "host";
    public static final String NAME = "name";
    private static final long serialVersionUID = 6374381323722046732L;
    private transient Cookie clientCookie;
    private transient Cookie cookie;
    public String domain;
    public String host;
    public String name;

    public SerializableCookie(String host2, Cookie cookie2) {
        this.cookie = cookie2;
        this.host = host2;
        this.name = cookie2.name();
        this.domain = cookie2.domain();
    }

    public Cookie getCookie() {
        Cookie bestCookie = this.cookie;
        if (this.clientCookie != null) {
            return this.clientCookie;
        }
        return bestCookie;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(this.cookie.name());
        out.writeObject(this.cookie.value());
        out.writeLong(this.cookie.expiresAt());
        out.writeObject(this.cookie.domain());
        out.writeObject(this.cookie.path());
        out.writeBoolean(this.cookie.secure());
        out.writeBoolean(this.cookie.httpOnly());
        out.writeBoolean(this.cookie.hostOnly());
        out.writeBoolean(this.cookie.persistent());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        long expiresAt = in.readLong();
        String domain2 = (String) in.readObject();
        String path = (String) in.readObject();
        boolean secure = in.readBoolean();
        boolean httpOnly = in.readBoolean();
        boolean hostOnly = in.readBoolean();
        boolean readBoolean = in.readBoolean();
        Cookie.Builder builder = new Cookie.Builder().name((String) in.readObject()).value((String) in.readObject()).expiresAt(expiresAt);
        Cookie.Builder builder2 = (hostOnly ? builder.hostOnlyDomain(domain2) : builder.domain(domain2)).path(path);
        if (secure) {
            builder2 = builder2.secure();
        }
        if (httpOnly) {
            builder2 = builder2.httpOnly();
        }
        this.clientCookie = builder2.build();
    }

    public static SerializableCookie parseCursorToBean(Cursor cursor) {
        return new SerializableCookie(cursor.getString(cursor.getColumnIndex(HOST)), bytesToCookie(cursor.getBlob(cursor.getColumnIndex(COOKIE))));
    }

    public static ContentValues getContentValues(SerializableCookie serializableCookie) {
        ContentValues values = new ContentValues();
        values.put(HOST, serializableCookie.host);
        values.put(NAME, serializableCookie.name);
        values.put(DOMAIN, serializableCookie.domain);
        values.put(COOKIE, cookieToBytes(serializableCookie.host, serializableCookie.getCookie()));
        return values;
    }

    public static String encodeCookie(String host2, Cookie cookie2) {
        if (cookie2 == null) {
            return null;
        }
        return byteArrayToHexString(cookieToBytes(host2, cookie2));
    }

    public static byte[] cookieToBytes(String host2, Cookie cookie2) {
        SerializableCookie serializableCookie = new SerializableCookie(host2, cookie2);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            new ObjectOutputStream(os).writeObject(serializableCookie);
            return os.toByteArray();
        } catch (IOException e) {
            OkLogger.printStackTrace(e);
            return null;
        }
    }

    public static Cookie decodeCookie(String cookieString) {
        return bytesToCookie(hexStringToByteArray(cookieString));
    }

    public static Cookie bytesToCookie(byte[] bytes) {
        try {
            return ((SerializableCookie) new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject()).getCookie();
        } catch (Exception e) {
            OkLogger.printStackTrace(e);
            return null;
        }
    }

    private static String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte element : bytes) {
            int v = element & 255;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.US);
    }

    private static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[(len / 2)];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SerializableCookie that = (SerializableCookie) o;
        if (this.host != null) {
            if (!this.host.equals(that.host)) {
                return false;
            }
        } else if (that.host != null) {
            return false;
        }
        if (this.name != null) {
            if (!this.name.equals(that.name)) {
                return false;
            }
        } else if (that.name != null) {
            return false;
        }
        if (this.domain != null) {
            z = this.domain.equals(that.domain);
        } else if (that.domain != null) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result;
        int i;
        int i2 = 0;
        if (this.host != null) {
            result = this.host.hashCode();
        } else {
            result = 0;
        }
        int i3 = result * 31;
        if (this.name != null) {
            i = this.name.hashCode();
        } else {
            i = 0;
        }
        int i4 = (i3 + i) * 31;
        if (this.domain != null) {
            i2 = this.domain.hashCode();
        }
        return i4 + i2;
    }
}
