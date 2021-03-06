package com.google.android.exoplayer.upstream;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketException;

public final class UdpDataSource implements UriDataSource {
    public static final int DEAFULT_SOCKET_TIMEOUT_MILLIS = 8000;
    public static final int DEFAULT_MAX_PACKET_SIZE = 2000;
    private InetAddress address;
    private DataSpec dataSpec;
    private final TransferListener listener;
    private MulticastSocket multicastSocket;
    private boolean opened;
    private final DatagramPacket packet;
    private byte[] packetBuffer;
    private int packetRemaining;
    private DatagramSocket socket;
    private InetSocketAddress socketAddress;
    private final int socketTimeoutMillis;

    public static final class UdpDataSourceException extends IOException {
        public UdpDataSourceException(String message) {
            super(message);
        }

        public UdpDataSourceException(IOException cause) {
            super(cause);
        }
    }

    public UdpDataSource(TransferListener listener2) {
        this(listener2, 2000);
    }

    public UdpDataSource(TransferListener listener2, int maxPacketSize) {
        this(listener2, maxPacketSize, 8000);
    }

    public UdpDataSource(TransferListener listener2, int maxPacketSize, int socketTimeoutMillis2) {
        this.listener = listener2;
        this.socketTimeoutMillis = socketTimeoutMillis2;
        this.packetBuffer = new byte[maxPacketSize];
        this.packet = new DatagramPacket(this.packetBuffer, 0, maxPacketSize);
    }

    public long open(DataSpec dataSpec2) throws UdpDataSourceException {
        this.dataSpec = dataSpec2;
        String host = dataSpec2.uri.getHost();
        int port = dataSpec2.uri.getPort();
        try {
            this.address = InetAddress.getByName(host);
            this.socketAddress = new InetSocketAddress(this.address, port);
            if (this.address.isMulticastAddress()) {
                this.multicastSocket = new MulticastSocket(this.socketAddress);
                this.multicastSocket.joinGroup(this.address);
                this.socket = this.multicastSocket;
            } else {
                this.socket = new DatagramSocket(this.socketAddress);
            }
            try {
                this.socket.setSoTimeout(this.socketTimeoutMillis);
                this.opened = true;
                if (this.listener == null) {
                    return -1;
                }
                this.listener.onTransferStart();
                return -1;
            } catch (SocketException e) {
                throw new UdpDataSourceException((IOException) e);
            }
        } catch (IOException e2) {
            throw new UdpDataSourceException(e2);
        }
    }

    public int read(byte[] buffer, int offset, int readLength) throws UdpDataSourceException {
        if (this.packetRemaining == 0) {
            try {
                this.socket.receive(this.packet);
                this.packetRemaining = this.packet.getLength();
                if (this.listener != null) {
                    this.listener.onBytesTransferred(this.packetRemaining);
                }
            } catch (IOException e) {
                throw new UdpDataSourceException(e);
            }
        }
        int packetOffset = this.packet.getLength() - this.packetRemaining;
        int bytesToRead = Math.min(this.packetRemaining, readLength);
        System.arraycopy(this.packetBuffer, packetOffset, buffer, offset, bytesToRead);
        this.packetRemaining -= bytesToRead;
        return bytesToRead;
    }

    public void close() {
        if (this.multicastSocket != null) {
            try {
                this.multicastSocket.leaveGroup(this.address);
            } catch (IOException e) {
            }
            this.multicastSocket = null;
        }
        if (this.socket != null) {
            this.socket.close();
            this.socket = null;
        }
        this.address = null;
        this.socketAddress = null;
        this.packetRemaining = 0;
        if (this.opened) {
            this.opened = false;
            if (this.listener != null) {
                this.listener.onTransferEnd();
            }
        }
    }

    public String getUri() {
        if (this.dataSpec == null) {
            return null;
        }
        return this.dataSpec.uri.toString();
    }
}
