package com.google.android.vending.expansion.downloader.impl;

import android.content.Context;
import android.net.Proxy;
import android.os.Build;
import android.os.PowerManager;
import android.os.Process;
import com.freevisiontech.fvmobile.model.resolver.CompanyIdentifierResolver;
import com.google.android.vending.expansion.downloader.Constants;
import com.google.android.vending.expansion.downloader.Helpers;
import com.google.android.vending.expansion.downloader.impl.DownloaderService;
import com.lzy.okgo.model.HttpHeaders;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRouteParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DownloadThread {
    private static final Logger LOG = LoggerFactory.getLogger("DownloadThread");
    private Context mContext;
    private final DownloadsDB mDB;
    private DownloadInfo mInfo;
    private final DownloadNotification mNotification;
    private DownloaderService mService;
    private String mUserAgent;

    public DownloadThread(DownloadInfo info, DownloaderService service, DownloadNotification notification) {
        this.mContext = service;
        this.mInfo = info;
        this.mService = service;
        this.mNotification = notification;
        this.mDB = DownloadsDB.getDB(service);
        this.mUserAgent = "APKXDL (Linux; U; Android " + Build.VERSION.RELEASE + ";" + Locale.getDefault().toString() + "; " + Build.DEVICE + "/" + Build.ID + ")" + service.getPackageName();
    }

    private String userAgent() {
        return this.mUserAgent;
    }

    private static class State {
        public boolean mCountRetry = false;
        public String mFilename;
        public boolean mGotData = false;
        public String mNewUri;
        public int mRedirectCount = 0;
        public String mRequestUri;
        public int mRetryAfter = 0;
        public FileOutputStream mStream;

        public State(DownloadInfo info, DownloaderService service) {
            this.mRedirectCount = info.mRedirectCount;
            this.mRequestUri = info.mUri;
            this.mFilename = service.generateTempSaveFileName(info.mFileName);
        }
    }

    private static class InnerState {
        public int mBytesNotified;
        public int mBytesSoFar;
        public int mBytesThisSession;
        public boolean mContinuingDownload;
        public String mHeaderContentDisposition;
        public String mHeaderContentLength;
        public String mHeaderContentLocation;
        public String mHeaderETag;
        public long mTimeLastNotification;

        private InnerState() {
            this.mBytesSoFar = 0;
            this.mBytesThisSession = 0;
            this.mContinuingDownload = false;
            this.mBytesNotified = 0;
            this.mTimeLastNotification = 0;
        }
    }

    private class StopRequest extends Throwable {
        private static final long serialVersionUID = 6338592678988347973L;
        public int mFinalStatus;

        public StopRequest(int finalStatus, String message) {
            super(message);
            this.mFinalStatus = finalStatus;
        }

        public StopRequest(int finalStatus, String message, Throwable throwable) {
            super(message, throwable);
            this.mFinalStatus = finalStatus;
        }
    }

    private class RetryDownload extends Throwable {
        private static final long serialVersionUID = 6196036036517540229L;

        private RetryDownload() {
        }
    }

    public HttpHost getPreferredHttpHost(Context context, String url) {
        String proxyHost;
        if (isLocalHost(url) || this.mService.isWiFi() || (proxyHost = Proxy.getHost(context)) == null) {
            return null;
        }
        return new HttpHost(proxyHost, Proxy.getPort(context), "http");
    }

    private static final boolean isLocalHost(String url) {
        if (url == null) {
            return false;
        }
        try {
            String host = URI.create(url).getHost();
            if (host == null) {
                return false;
            }
            if (host.equalsIgnoreCase("localhost") || host.equals("127.0.0.1") || host.equals("[::1]")) {
                return true;
            }
            return false;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /* JADX INFO: finally extract failed */
    public void run() {
        HttpGet request;
        Process.setThreadPriority(10);
        State state = new State(this.mInfo, this.mService);
        AndroidHttpClient client = null;
        PowerManager.WakeLock wakeLock = null;
        try {
            wakeLock = ((PowerManager) this.mContext.getSystemService("power")).newWakeLock(1, Constants.TAG);
            wakeLock.acquire();
            client = AndroidHttpClient.newInstance(userAgent(), this.mContext);
            boolean finished = false;
            while (!finished) {
                ConnRouteParams.setDefaultProxy(client.getParams(), getPreferredHttpHost(this.mContext, state.mRequestUri));
                request = new HttpGet(state.mRequestUri);
                executeDownload(state, client, request);
                finished = true;
                request.abort();
            }
            finalizeDestinationFile(state);
            if (wakeLock != null) {
                wakeLock.release();
            }
            if (client != null) {
                client.close();
            }
            cleanupDestination(state, 200);
            notifyDownloadCompleted(200, state.mCountRetry, state.mRetryAfter, state.mRedirectCount, state.mGotData, state.mFilename);
        } catch (RetryDownload e) {
            request.abort();
        } catch (StopRequest error) {
            try {
                LOG.warn("Aborting request for download " + this.mInfo.mFileName + ": " + error.getMessage());
                error.printStackTrace();
                int finalStatus = error.mFinalStatus;
                if (wakeLock != null) {
                    wakeLock.release();
                }
                if (client != null) {
                    client.close();
                }
                cleanupDestination(state, finalStatus);
                notifyDownloadCompleted(finalStatus, state.mCountRetry, state.mRetryAfter, state.mRedirectCount, state.mGotData, state.mFilename);
            } catch (Throwable th) {
                Throwable th2 = th;
                if (wakeLock != null) {
                    wakeLock.release();
                }
                if (client != null) {
                    client.close();
                }
                cleanupDestination(state, 491);
                notifyDownloadCompleted(491, state.mCountRetry, state.mRetryAfter, state.mRedirectCount, state.mGotData, state.mFilename);
                throw th2;
            }
        } catch (Throwable ex) {
            LOG.warn("Exception for " + this.mInfo.mFileName + ": " + ex);
            if (wakeLock != null) {
                wakeLock.release();
            }
            if (client != null) {
                client.close();
            }
            cleanupDestination(state, 491);
            notifyDownloadCompleted(491, state.mCountRetry, state.mRetryAfter, state.mRedirectCount, state.mGotData, state.mFilename);
        }
    }

    private void executeDownload(State state, AndroidHttpClient client, HttpGet request) throws StopRequest, RetryDownload {
        InnerState innerState = new InnerState();
        checkPausedOrCanceled(state);
        setupDestinationFile(state, innerState);
        addRequestHeaders(innerState, request);
        checkConnectivity(state);
        this.mNotification.onDownloadStateChanged(3);
        HttpResponse response = sendRequest(state, client, request);
        handleExceptionalStatus(state, innerState, response);
        processResponseHeaders(state, innerState, response);
        InputStream entityStream = openResponseEntity(state, response);
        this.mNotification.onDownloadStateChanged(4);
        transferData(state, innerState, new byte[4096], entityStream);
    }

    private void checkConnectivity(State state) throws StopRequest {
        switch (this.mService.getNetworkAvailabilityState(this.mDB)) {
            case 2:
                throw new StopRequest(195, "waiting for network to return");
            case 3:
                throw new StopRequest(197, "waiting for wifi");
            case 5:
                throw new StopRequest(195, "roaming is not allowed");
            case 6:
                throw new StopRequest(196, "waiting for wifi or for download over cellular to be authorized");
            default:
                return;
        }
    }

    private void transferData(State state, InnerState innerState, byte[] data, InputStream entityStream) throws StopRequest {
        while (true) {
            int bytesRead = readFromResponse(state, innerState, data, entityStream);
            if (bytesRead == -1) {
                handleEndOfStream(state, innerState);
                return;
            }
            state.mGotData = true;
            writeDataToDestination(state, data, bytesRead);
            innerState.mBytesSoFar += bytesRead;
            innerState.mBytesThisSession += bytesRead;
            reportProgress(state, innerState);
            checkPausedOrCanceled(state);
        }
    }

    private void finalizeDestinationFile(State state) throws StopRequest {
        syncDestination(state);
        String tempFilename = state.mFilename;
        String finalFilename = Helpers.generateSaveFileName(this.mService, this.mInfo.mFileName);
        if (!state.mFilename.equals(finalFilename)) {
            File startFile = new File(tempFilename);
            File destFile = new File(finalFilename);
            if (this.mInfo.mTotalBytes == -1 || this.mInfo.mCurrentBytes != this.mInfo.mTotalBytes) {
                throw new StopRequest(DownloaderService.STATUS_FILE_DELIVERED_INCORRECTLY, "file delivered with incorrect size. probably due to network not browser configured");
            } else if (!startFile.renameTo(destFile)) {
                throw new StopRequest(492, "unable to finalize destination file");
            }
        }
    }

    private void cleanupDestination(State state, int finalStatus) {
        closeDestination(state);
        if (state.mFilename != null && DownloaderService.isStatusError(finalStatus)) {
            new File(state.mFilename).delete();
            state.mFilename = null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0056 A[SYNTHETIC, Splitter:B:17:0x0056] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0097 A[SYNTHETIC, Splitter:B:27:0x0097] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00db A[SYNTHETIC, Splitter:B:37:0x00db] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x0101 A[SYNTHETIC, Splitter:B:47:0x0101] */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x011f A[SYNTHETIC, Splitter:B:55:0x011f] */
    /* JADX WARNING: Removed duplicated region for block: B:76:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:79:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:82:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:85:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:34:0x00b3=Splitter:B:34:0x00b3, B:44:0x00f7=Splitter:B:44:0x00f7, B:14:0x002e=Splitter:B:14:0x002e, B:24:0x006f=Splitter:B:24:0x006f} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void syncDestination(com.google.android.vending.expansion.downloader.impl.DownloadThread.State r7) {
        /*
            r6 = this;
            r0 = 0
            java.io.FileOutputStream r1 = new java.io.FileOutputStream     // Catch:{ FileNotFoundException -> 0x002d, SyncFailedException -> 0x006e, IOException -> 0x00b2, RuntimeException -> 0x00f6 }
            java.lang.String r3 = r7.mFilename     // Catch:{ FileNotFoundException -> 0x002d, SyncFailedException -> 0x006e, IOException -> 0x00b2, RuntimeException -> 0x00f6 }
            r4 = 1
            r1.<init>(r3, r4)     // Catch:{ FileNotFoundException -> 0x002d, SyncFailedException -> 0x006e, IOException -> 0x00b2, RuntimeException -> 0x00f6 }
            java.io.FileDescriptor r3 = r1.getFD()     // Catch:{ FileNotFoundException -> 0x0145, SyncFailedException -> 0x0141, IOException -> 0x013d, RuntimeException -> 0x013a, all -> 0x0137 }
            r3.sync()     // Catch:{ FileNotFoundException -> 0x0145, SyncFailedException -> 0x0141, IOException -> 0x013d, RuntimeException -> 0x013a, all -> 0x0137 }
            if (r1 == 0) goto L_0x0149
            r1.close()     // Catch:{ IOException -> 0x0017, RuntimeException -> 0x0022 }
            r0 = r1
        L_0x0016:
            return
        L_0x0017:
            r2 = move-exception
            org.slf4j.Logger r3 = LOG
            java.lang.String r4 = "IOException while closing synced file: "
            r3.warn((java.lang.String) r4, (java.lang.Throwable) r2)
            r0 = r1
            goto L_0x0016
        L_0x0022:
            r2 = move-exception
            org.slf4j.Logger r3 = LOG
            java.lang.String r4 = "exception while closing file: "
            r3.warn((java.lang.String) r4, (java.lang.Throwable) r2)
            r0 = r1
            goto L_0x0016
        L_0x002d:
            r2 = move-exception
        L_0x002e:
            org.slf4j.Logger r3 = LOG     // Catch:{ all -> 0x011c }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x011c }
            r4.<init>()     // Catch:{ all -> 0x011c }
            java.lang.String r5 = "file "
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x011c }
            java.lang.String r5 = r7.mFilename     // Catch:{ all -> 0x011c }
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x011c }
            java.lang.String r5 = " not found: "
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x011c }
            java.lang.StringBuilder r4 = r4.append(r2)     // Catch:{ all -> 0x011c }
            java.lang.String r4 = r4.toString()     // Catch:{ all -> 0x011c }
            r3.warn(r4)     // Catch:{ all -> 0x011c }
            if (r0 == 0) goto L_0x0016
            r0.close()     // Catch:{ IOException -> 0x005a, RuntimeException -> 0x0064 }
            goto L_0x0016
        L_0x005a:
            r2 = move-exception
            org.slf4j.Logger r3 = LOG
            java.lang.String r4 = "IOException while closing synced file: "
            r3.warn((java.lang.String) r4, (java.lang.Throwable) r2)
            goto L_0x0016
        L_0x0064:
            r2 = move-exception
            org.slf4j.Logger r3 = LOG
            java.lang.String r4 = "exception while closing file: "
            r3.warn((java.lang.String) r4, (java.lang.Throwable) r2)
            goto L_0x0016
        L_0x006e:
            r2 = move-exception
        L_0x006f:
            org.slf4j.Logger r3 = LOG     // Catch:{ all -> 0x011c }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x011c }
            r4.<init>()     // Catch:{ all -> 0x011c }
            java.lang.String r5 = "file "
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x011c }
            java.lang.String r5 = r7.mFilename     // Catch:{ all -> 0x011c }
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x011c }
            java.lang.String r5 = " sync failed: "
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x011c }
            java.lang.StringBuilder r4 = r4.append(r2)     // Catch:{ all -> 0x011c }
            java.lang.String r4 = r4.toString()     // Catch:{ all -> 0x011c }
            r3.warn(r4)     // Catch:{ all -> 0x011c }
            if (r0 == 0) goto L_0x0016
            r0.close()     // Catch:{ IOException -> 0x009c, RuntimeException -> 0x00a7 }
            goto L_0x0016
        L_0x009c:
            r2 = move-exception
            org.slf4j.Logger r3 = LOG
            java.lang.String r4 = "IOException while closing synced file: "
            r3.warn((java.lang.String) r4, (java.lang.Throwable) r2)
            goto L_0x0016
        L_0x00a7:
            r2 = move-exception
            org.slf4j.Logger r3 = LOG
            java.lang.String r4 = "exception while closing file: "
            r3.warn((java.lang.String) r4, (java.lang.Throwable) r2)
            goto L_0x0016
        L_0x00b2:
            r2 = move-exception
        L_0x00b3:
            org.slf4j.Logger r3 = LOG     // Catch:{ all -> 0x011c }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x011c }
            r4.<init>()     // Catch:{ all -> 0x011c }
            java.lang.String r5 = "IOException trying to sync "
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x011c }
            java.lang.String r5 = r7.mFilename     // Catch:{ all -> 0x011c }
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x011c }
            java.lang.String r5 = ": "
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x011c }
            java.lang.StringBuilder r4 = r4.append(r2)     // Catch:{ all -> 0x011c }
            java.lang.String r4 = r4.toString()     // Catch:{ all -> 0x011c }
            r3.warn(r4)     // Catch:{ all -> 0x011c }
            if (r0 == 0) goto L_0x0016
            r0.close()     // Catch:{ IOException -> 0x00e0, RuntimeException -> 0x00eb }
            goto L_0x0016
        L_0x00e0:
            r2 = move-exception
            org.slf4j.Logger r3 = LOG
            java.lang.String r4 = "IOException while closing synced file: "
            r3.warn((java.lang.String) r4, (java.lang.Throwable) r2)
            goto L_0x0016
        L_0x00eb:
            r2 = move-exception
            org.slf4j.Logger r3 = LOG
            java.lang.String r4 = "exception while closing file: "
            r3.warn((java.lang.String) r4, (java.lang.Throwable) r2)
            goto L_0x0016
        L_0x00f6:
            r2 = move-exception
        L_0x00f7:
            org.slf4j.Logger r3 = LOG     // Catch:{ all -> 0x011c }
            java.lang.String r4 = "exception while syncing file: "
            r3.warn((java.lang.String) r4, (java.lang.Throwable) r2)     // Catch:{ all -> 0x011c }
            if (r0 == 0) goto L_0x0016
            r0.close()     // Catch:{ IOException -> 0x0106, RuntimeException -> 0x0111 }
            goto L_0x0016
        L_0x0106:
            r2 = move-exception
            org.slf4j.Logger r3 = LOG
            java.lang.String r4 = "IOException while closing synced file: "
            r3.warn((java.lang.String) r4, (java.lang.Throwable) r2)
            goto L_0x0016
        L_0x0111:
            r2 = move-exception
            org.slf4j.Logger r3 = LOG
            java.lang.String r4 = "exception while closing file: "
            r3.warn((java.lang.String) r4, (java.lang.Throwable) r2)
            goto L_0x0016
        L_0x011c:
            r3 = move-exception
        L_0x011d:
            if (r0 == 0) goto L_0x0122
            r0.close()     // Catch:{ IOException -> 0x0123, RuntimeException -> 0x012d }
        L_0x0122:
            throw r3
        L_0x0123:
            r2 = move-exception
            org.slf4j.Logger r4 = LOG
            java.lang.String r5 = "IOException while closing synced file: "
            r4.warn((java.lang.String) r5, (java.lang.Throwable) r2)
            goto L_0x0122
        L_0x012d:
            r2 = move-exception
            org.slf4j.Logger r4 = LOG
            java.lang.String r5 = "exception while closing file: "
            r4.warn((java.lang.String) r5, (java.lang.Throwable) r2)
            goto L_0x0122
        L_0x0137:
            r3 = move-exception
            r0 = r1
            goto L_0x011d
        L_0x013a:
            r2 = move-exception
            r0 = r1
            goto L_0x00f7
        L_0x013d:
            r2 = move-exception
            r0 = r1
            goto L_0x00b3
        L_0x0141:
            r2 = move-exception
            r0 = r1
            goto L_0x006f
        L_0x0145:
            r2 = move-exception
            r0 = r1
            goto L_0x002e
        L_0x0149:
            r0 = r1
            goto L_0x0016
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.vending.expansion.downloader.impl.DownloadThread.syncDestination(com.google.android.vending.expansion.downloader.impl.DownloadThread$State):void");
    }

    private void closeDestination(State state) {
        try {
            if (state.mStream != null) {
                state.mStream.close();
                state.mStream = null;
            }
        } catch (IOException e) {
        }
    }

    private void checkPausedOrCanceled(State state) throws StopRequest {
        if (this.mService.getControl() == 1) {
            switch (this.mService.getStatus()) {
                case 193:
                    throw new StopRequest(this.mService.getStatus(), "download paused");
                default:
                    return;
            }
        }
    }

    private void reportProgress(State state, InnerState innerState) {
        long now = System.currentTimeMillis();
        if (innerState.mBytesSoFar - innerState.mBytesNotified > 4096 && now - innerState.mTimeLastNotification > 1000) {
            this.mInfo.mCurrentBytes = (long) innerState.mBytesSoFar;
            this.mDB.updateDownloadCurrentBytes(this.mInfo);
            innerState.mBytesNotified = innerState.mBytesSoFar;
            innerState.mTimeLastNotification = now;
            this.mService.notifyUpdateBytes(((long) innerState.mBytesThisSession) + this.mService.mBytesSoFar);
        }
    }

    private void writeDataToDestination(State state, byte[] data, int bytesRead) throws StopRequest {
        try {
            if (state.mStream == null) {
                state.mStream = new FileOutputStream(state.mFilename, true);
            }
            state.mStream.write(data, 0, bytesRead);
            closeDestination(state);
        } catch (IOException ex) {
            if (!Helpers.isExternalMediaMounted()) {
                throw new StopRequest(499, "external media not mounted while writing destination file");
            } else if (Helpers.getAvailableBytes(Helpers.getFilesystemRoot(state.mFilename)) < ((long) bytesRead)) {
                throw new StopRequest(498, "insufficient space while writing destination file", ex);
            } else {
                throw new StopRequest(492, "while writing destination file: " + ex.toString(), ex);
            }
        }
    }

    private void handleEndOfStream(State state, InnerState innerState) throws StopRequest {
        this.mInfo.mCurrentBytes = (long) innerState.mBytesSoFar;
        this.mDB.updateDownload(this.mInfo);
        if (!((innerState.mHeaderContentLength == null || innerState.mBytesSoFar == Integer.parseInt(innerState.mHeaderContentLength)) ? false : true)) {
            return;
        }
        if (cannotResume(innerState)) {
            throw new StopRequest(489, "mismatched content length");
        }
        throw new StopRequest(getFinalStatusForHttpError(state), "closed socket before end of file");
    }

    private boolean cannotResume(InnerState innerState) {
        return innerState.mBytesSoFar > 0 && innerState.mHeaderETag == null;
    }

    private int readFromResponse(State state, InnerState innerState, byte[] data, InputStream entityStream) throws StopRequest {
        try {
            return entityStream.read(data);
        } catch (IOException ex) {
            logNetworkState();
            this.mInfo.mCurrentBytes = (long) innerState.mBytesSoFar;
            this.mDB.updateDownload(this.mInfo);
            if (cannotResume(innerState)) {
                throw new StopRequest(489, "while reading response: " + ex.toString() + ", can't resume interrupted download with no ETag", ex);
            }
            throw new StopRequest(getFinalStatusForHttpError(state), "while reading response: " + ex.toString(), ex);
        }
    }

    private InputStream openResponseEntity(State state, HttpResponse response) throws StopRequest {
        try {
            return response.getEntity().getContent();
        } catch (IOException ex) {
            logNetworkState();
            throw new StopRequest(getFinalStatusForHttpError(state), "while getting entity: " + ex.toString(), ex);
        }
    }

    private void logNetworkState() {
        LOG.info("Net " + (this.mService.getNetworkAvailabilityState(this.mDB) == 1 ? "Up" : "Down"));
    }

    private void processResponseHeaders(State state, InnerState innerState, HttpResponse response) throws StopRequest {
        if (!innerState.mContinuingDownload) {
            readResponseHeaders(state, innerState, response);
            try {
                state.mFilename = this.mService.generateSaveFile(this.mInfo.mFileName, this.mInfo.mTotalBytes);
                try {
                    state.mStream = new FileOutputStream(state.mFilename);
                } catch (FileNotFoundException exc) {
                    try {
                        if (new File(Helpers.getSaveFilePath(this.mService)).mkdirs()) {
                            state.mStream = new FileOutputStream(state.mFilename);
                        }
                    } catch (Exception e) {
                        throw new StopRequest(492, "while opening destination file: " + exc.toString(), exc);
                    }
                }
                updateDatabaseFromHeaders(state, innerState);
                checkConnectivity(state);
            } catch (DownloaderService.GenerateSaveFileError exc2) {
                throw new StopRequest(exc2.mStatus, exc2.mMessage);
            }
        }
    }

    private void updateDatabaseFromHeaders(State state, InnerState innerState) {
        this.mInfo.mETag = innerState.mHeaderETag;
        this.mDB.updateDownload(this.mInfo);
    }

    private void readResponseHeaders(State state, InnerState innerState, HttpResponse response) throws StopRequest {
        Header header;
        Header header2 = response.getFirstHeader(HttpHeaders.HEAD_KEY_CONTENT_DISPOSITION);
        if (header2 != null) {
            innerState.mHeaderContentDisposition = header2.getValue();
        }
        Header header3 = response.getFirstHeader("Content-Location");
        if (header3 != null) {
            innerState.mHeaderContentLocation = header3.getValue();
        }
        Header header4 = response.getFirstHeader(HttpHeaders.HEAD_KEY_E_TAG);
        if (header4 != null) {
            innerState.mHeaderETag = header4.getValue();
        }
        String headerTransferEncoding = null;
        Header header5 = response.getFirstHeader("Transfer-Encoding");
        if (header5 != null) {
            headerTransferEncoding = header5.getValue();
        }
        Header header6 = response.getFirstHeader(HttpHeaders.HEAD_KEY_CONTENT_TYPE);
        if (header6 == null || header6.getValue().equals("application/vnd.android.obb")) {
            if (headerTransferEncoding == null && (header = response.getFirstHeader(HttpHeaders.HEAD_KEY_CONTENT_LENGTH)) != null) {
                innerState.mHeaderContentLength = header.getValue();
                long contentLength = Long.parseLong(innerState.mHeaderContentLength);
                if (!(contentLength == -1 || contentLength == this.mInfo.mTotalBytes)) {
                    LOG.error("Incorrect file size delivered.");
                }
            }
            if (innerState.mHeaderContentLength == null && (headerTransferEncoding == null || !headerTransferEncoding.equalsIgnoreCase("chunked"))) {
                throw new StopRequest(495, "can't know size of download, giving up");
            }
            return;
        }
        throw new StopRequest(DownloaderService.STATUS_FILE_DELIVERED_INCORRECTLY, "file delivered with incorrect Mime type");
    }

    private void handleExceptionalStatus(State state, InnerState innerState, HttpResponse response) throws StopRequest, RetryDownload {
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 503 && this.mInfo.mNumFailed < 5) {
            handleServiceUnavailable(state, response);
        }
        if (statusCode == 301 || statusCode == 302 || statusCode == 303 || statusCode == 307) {
            handleRedirect(state, response, statusCode);
        }
        if (statusCode != (innerState.mContinuingDownload ? CompanyIdentifierResolver.ELGATO_SYSTEMS_GMBH : 200)) {
            handleOtherStatus(state, innerState, statusCode);
        } else {
            state.mRedirectCount = 0;
        }
    }

    private void handleOtherStatus(State state, InnerState innerState, int statusCode) throws StopRequest {
        int finalStatus;
        if (DownloaderService.isStatusError(statusCode)) {
            finalStatus = statusCode;
        } else if (statusCode >= 300 && statusCode < 400) {
            finalStatus = 493;
        } else if (!innerState.mContinuingDownload || statusCode != 200) {
            finalStatus = 494;
        } else {
            finalStatus = 489;
        }
        throw new StopRequest(finalStatus, "http error " + statusCode);
    }

    private void handleRedirect(State state, HttpResponse response, int statusCode) throws StopRequest, RetryDownload {
        if (state.mRedirectCount >= 10) {
            throw new StopRequest(497, "too many redirects");
        }
        Header header = response.getFirstHeader(HttpHeaders.HEAD_KEY_LOCATION);
        if (header != null) {
            try {
                String newUri = new URI(this.mInfo.mUri).resolve(new URI(header.getValue())).toString();
                state.mRedirectCount++;
                state.mRequestUri = newUri;
                if (statusCode == 301 || statusCode == 303) {
                    state.mNewUri = newUri;
                }
                throw new RetryDownload();
            } catch (URISyntaxException e) {
                throw new StopRequest(495, "Couldn't resolve redirect URI");
            }
        }
    }

    private void addRequestHeaders(InnerState innerState, HttpGet request) {
        if (innerState.mContinuingDownload) {
            if (innerState.mHeaderETag != null) {
                request.addHeader("If-Match", innerState.mHeaderETag);
            }
            request.addHeader(HttpHeaders.HEAD_KEY_RANGE, "bytes=" + innerState.mBytesSoFar + Constants.FILENAME_SEQUENCE_SEPARATOR);
        }
    }

    private void handleServiceUnavailable(State state, HttpResponse response) throws StopRequest {
        state.mCountRetry = true;
        Header header = response.getFirstHeader("Retry-After");
        if (header != null) {
            try {
                state.mRetryAfter = Integer.parseInt(header.getValue());
                if (state.mRetryAfter < 0) {
                    state.mRetryAfter = 0;
                } else {
                    if (state.mRetryAfter < 30) {
                        state.mRetryAfter = 30;
                    } else if (state.mRetryAfter > 86400) {
                        state.mRetryAfter = Constants.MAX_RETRY_AFTER;
                    }
                    state.mRetryAfter += Helpers.sRandom.nextInt(31);
                    state.mRetryAfter *= 1000;
                }
            } catch (NumberFormatException e) {
            }
        }
        throw new StopRequest(194, "got 503 Service Unavailable, will retry later");
    }

    private HttpResponse sendRequest(State state, AndroidHttpClient client, HttpGet request) throws StopRequest {
        try {
            return client.execute(request);
        } catch (IllegalArgumentException ex) {
            throw new StopRequest(495, "while trying to execute request: " + ex.toString(), ex);
        } catch (IOException ex2) {
            logNetworkState();
            throw new StopRequest(getFinalStatusForHttpError(state), "while trying to execute request: " + ex2.toString(), ex2);
        }
    }

    private int getFinalStatusForHttpError(State state) {
        if (this.mService.getNetworkAvailabilityState(this.mDB) != 1) {
            return 195;
        }
        if (this.mInfo.mNumFailed < 5) {
            state.mCountRetry = true;
            return 194;
        }
        LOG.warn("reached max retries for " + this.mInfo.mNumFailed);
        return 495;
    }

    private void setupDestinationFile(State state, InnerState innerState) throws StopRequest {
        if (state.mFilename != null) {
            if (!Helpers.isFilenameValid(state.mFilename)) {
                throw new StopRequest(492, "found invalid internal destination filename");
            }
            File f = new File(state.mFilename);
            if (f.exists()) {
                long fileLength = f.length();
                if (fileLength == 0) {
                    f.delete();
                    state.mFilename = null;
                } else if (this.mInfo.mETag == null) {
                    f.delete();
                    throw new StopRequest(489, "Trying to resume a download that can't be resumed");
                } else {
                    try {
                        state.mStream = new FileOutputStream(state.mFilename, true);
                        innerState.mBytesSoFar = (int) fileLength;
                        if (this.mInfo.mTotalBytes != -1) {
                            innerState.mHeaderContentLength = Long.toString(this.mInfo.mTotalBytes);
                        }
                        innerState.mHeaderETag = this.mInfo.mETag;
                        innerState.mContinuingDownload = true;
                    } catch (FileNotFoundException exc) {
                        throw new StopRequest(492, "while opening destination for resuming: " + exc.toString(), exc);
                    }
                }
            }
        }
        if (state.mStream != null) {
            closeDestination(state);
        }
    }

    private void notifyDownloadCompleted(int status, boolean countRetry, int retryAfter, int redirectCount, boolean gotData, String filename) {
        updateDownloadDatabase(status, countRetry, retryAfter, redirectCount, gotData, filename);
        if (DownloaderService.isStatusCompleted(status)) {
        }
    }

    private void updateDownloadDatabase(int status, boolean countRetry, int retryAfter, int redirectCount, boolean gotData, String filename) {
        this.mInfo.mStatus = status;
        this.mInfo.mRetryAfter = retryAfter;
        this.mInfo.mRedirectCount = redirectCount;
        this.mInfo.mLastMod = System.currentTimeMillis();
        if (!countRetry) {
            this.mInfo.mNumFailed = 0;
        } else if (gotData) {
            this.mInfo.mNumFailed = 1;
        } else {
            this.mInfo.mNumFailed++;
        }
        this.mDB.updateDownload(this.mInfo);
    }
}
