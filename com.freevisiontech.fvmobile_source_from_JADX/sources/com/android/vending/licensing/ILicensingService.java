package com.android.vending.licensing;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.android.vending.licensing.ILicenseResultListener;

public interface ILicensingService extends IInterface {
    void checkLicense(long j, String str, ILicenseResultListener iLicenseResultListener) throws RemoteException;

    public static abstract class Stub extends Binder implements ILicensingService {
        private static final String DESCRIPTOR = "com.android.vending.licensing.ILicensingService";
        static final int TRANSACTION_checkLicense = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ILicensingService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ILicensingService)) {
                return new Proxy(obj);
            }
            return (ILicensingService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    checkLicense(data.readLong(), data.readString(), ILicenseResultListener.Stub.asInterface(data.readStrongBinder()));
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements ILicensingService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public void checkLicense(long nonce, String packageName, ILicenseResultListener listener) throws RemoteException {
                IBinder iBinder = null;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(nonce);
                    _data.writeString(packageName);
                    if (listener != null) {
                        iBinder = listener.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    this.mRemote.transact(1, _data, (Parcel) null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
