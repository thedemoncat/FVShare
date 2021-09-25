package org.opencv.engine;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface OpenCVEngineInterface extends IInterface {
    int getEngineVersion() throws RemoteException;

    String getLibPathByVersion(String str) throws RemoteException;

    String getLibraryList(String str) throws RemoteException;

    boolean installVersion(String str) throws RemoteException;

    public static abstract class Stub extends Binder implements OpenCVEngineInterface {
        private static final String DESCRIPTOR = "org.opencv.engine.OpenCVEngineInterface";
        static final int TRANSACTION_getEngineVersion = 1;
        static final int TRANSACTION_getLibPathByVersion = 2;
        static final int TRANSACTION_getLibraryList = 4;
        static final int TRANSACTION_installVersion = 3;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static OpenCVEngineInterface asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof OpenCVEngineInterface)) {
                return new Proxy(obj);
            }
            return (OpenCVEngineInterface) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    int _result = getEngineVersion();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    String _result2 = getLibPathByVersion(data.readString());
                    reply.writeNoException();
                    reply.writeString(_result2);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result3 = installVersion(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result3 ? 1 : 0);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    String _result4 = getLibraryList(data.readString());
                    reply.writeNoException();
                    reply.writeString(_result4);
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements OpenCVEngineInterface {
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

            public int getEngineVersion() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getLibPathByVersion(String version) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(version);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean installVersion(String version) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(version);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getLibraryList(String version) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(version);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
