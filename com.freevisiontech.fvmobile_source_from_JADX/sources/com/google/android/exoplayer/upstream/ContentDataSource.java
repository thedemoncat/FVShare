package com.google.android.exoplayer.upstream;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class ContentDataSource implements UriDataSource {
    private AssetFileDescriptor assetFileDescriptor;
    private long bytesRemaining;
    private InputStream inputStream;
    private final TransferListener listener;
    private boolean opened;
    private final ContentResolver resolver;
    private String uriString;

    public static class ContentDataSourceException extends IOException {
        public ContentDataSourceException(IOException cause) {
            super(cause);
        }
    }

    public ContentDataSource(Context context) {
        this(context, (TransferListener) null);
    }

    public ContentDataSource(Context context, TransferListener listener2) {
        this.resolver = context.getContentResolver();
        this.listener = listener2;
    }

    public long open(DataSpec dataSpec) throws ContentDataSourceException {
        try {
            this.uriString = dataSpec.uri.toString();
            this.assetFileDescriptor = this.resolver.openAssetFileDescriptor(dataSpec.uri, "r");
            this.inputStream = new FileInputStream(this.assetFileDescriptor.getFileDescriptor());
            if (this.inputStream.skip(dataSpec.position) < dataSpec.position) {
                throw new EOFException();
            }
            if (dataSpec.length != -1) {
                this.bytesRemaining = dataSpec.length;
            } else {
                this.bytesRemaining = (long) this.inputStream.available();
                if (this.bytesRemaining == 0) {
                    this.bytesRemaining = -1;
                }
            }
            this.opened = true;
            if (this.listener != null) {
                this.listener.onTransferStart();
            }
            return this.bytesRemaining;
        } catch (IOException e) {
            throw new ContentDataSourceException(e);
        }
    }

    public int read(byte[] buffer, int offset, int readLength) throws ContentDataSourceException {
        int bytesToRead;
        if (this.bytesRemaining == 0) {
            return -1;
        }
        try {
            if (this.bytesRemaining == -1) {
                bytesToRead = readLength;
            } else {
                bytesToRead = (int) Math.min(this.bytesRemaining, (long) readLength);
            }
            int bytesRead = this.inputStream.read(buffer, offset, bytesToRead);
            if (bytesRead <= 0) {
                return bytesRead;
            }
            if (this.bytesRemaining != -1) {
                this.bytesRemaining -= (long) bytesRead;
            }
            if (this.listener == null) {
                return bytesRead;
            }
            this.listener.onBytesTransferred(bytesRead);
            return bytesRead;
        } catch (IOException e) {
            throw new ContentDataSourceException(e);
        }
    }

    public String getUri() {
        return this.uriString;
    }

    public void close() throws ContentDataSourceException {
        this.uriString = null;
        try {
            if (this.inputStream != null) {
                this.inputStream.close();
            }
            this.inputStream = null;
            try {
                if (this.assetFileDescriptor != null) {
                    this.assetFileDescriptor.close();
                }
                this.assetFileDescriptor = null;
                if (this.opened) {
                    this.opened = false;
                    if (this.listener != null) {
                        this.listener.onTransferEnd();
                    }
                }
            } catch (IOException e) {
                throw new ContentDataSourceException(e);
            } catch (Throwable th) {
                this.assetFileDescriptor = null;
                if (this.opened) {
                    this.opened = false;
                    if (this.listener != null) {
                        this.listener.onTransferEnd();
                    }
                }
                throw th;
            }
        } catch (IOException e2) {
            throw new ContentDataSourceException(e2);
        } catch (Throwable th2) {
            this.inputStream = null;
            try {
                if (this.assetFileDescriptor != null) {
                    this.assetFileDescriptor.close();
                }
                this.assetFileDescriptor = null;
                if (this.opened) {
                    this.opened = false;
                    if (this.listener != null) {
                        this.listener.onTransferEnd();
                    }
                }
                throw th2;
            } catch (IOException e3) {
                throw new ContentDataSourceException(e3);
            } catch (Throwable th3) {
                this.assetFileDescriptor = null;
                if (this.opened) {
                    this.opened = false;
                    if (this.listener != null) {
                        this.listener.onTransferEnd();
                    }
                }
                throw th3;
            }
        }
    }
}
