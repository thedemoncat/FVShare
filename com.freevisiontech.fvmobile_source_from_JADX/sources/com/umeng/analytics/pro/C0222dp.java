package com.umeng.analytics.pro;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* renamed from: com.umeng.analytics.pro.dp */
/* compiled from: TIOStreamTransport */
public class C0222dp extends C0224dr {

    /* renamed from: a */
    protected InputStream f725a = null;

    /* renamed from: b */
    protected OutputStream f726b = null;

    protected C0222dp() {
    }

    public C0222dp(InputStream inputStream) {
        this.f725a = inputStream;
    }

    public C0222dp(OutputStream outputStream) {
        this.f726b = outputStream;
    }

    public C0222dp(InputStream inputStream, OutputStream outputStream) {
        this.f725a = inputStream;
        this.f726b = outputStream;
    }

    /* renamed from: a */
    public boolean mo633a() {
        return true;
    }

    /* renamed from: b */
    public void mo634b() throws C0225ds {
    }

    /* renamed from: c */
    public void mo636c() {
        if (this.f725a != null) {
            try {
                this.f725a.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.f725a = null;
        }
        if (this.f726b != null) {
            try {
                this.f726b.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            this.f726b = null;
        }
    }

    /* renamed from: a */
    public int mo632a(byte[] bArr, int i, int i2) throws C0225ds {
        if (this.f725a == null) {
            throw new C0225ds(1, "Cannot read from null inputStream");
        }
        try {
            int read = this.f725a.read(bArr, i, i2);
            if (read >= 0) {
                return read;
            }
            throw new C0225ds(4);
        } catch (IOException e) {
            throw new C0225ds(0, (Throwable) e);
        }
    }

    /* renamed from: b */
    public void mo635b(byte[] bArr, int i, int i2) throws C0225ds {
        if (this.f726b == null) {
            throw new C0225ds(1, "Cannot write to null outputStream");
        }
        try {
            this.f726b.write(bArr, i, i2);
        } catch (IOException e) {
            throw new C0225ds(0, (Throwable) e);
        }
    }

    /* renamed from: d */
    public void mo637d() throws C0225ds {
        if (this.f726b == null) {
            throw new C0225ds(1, "Cannot flush null outputStream");
        }
        try {
            this.f726b.flush();
        } catch (IOException e) {
            throw new C0225ds(0, (Throwable) e);
        }
    }
}
