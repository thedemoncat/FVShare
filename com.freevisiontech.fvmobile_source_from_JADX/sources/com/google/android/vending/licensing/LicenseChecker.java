package com.google.android.vending.licensing;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.RemoteException;
import com.google.android.vending.licensing.ILicenseResultListener;
import com.google.android.vending.licensing.ILicensingService;
import com.google.android.vending.licensing.util.Base64;
import com.google.android.vending.licensing.util.Base64DecoderException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LicenseChecker implements ServiceConnection {
    private static final boolean DEBUG_LICENSE_ERROR = false;
    private static final String KEY_FACTORY_ALGORITHM = "RSA";
    /* access modifiers changed from: private */
    public static final Logger LOG = LoggerFactory.getLogger("LicenseChecker");
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int TIMEOUT_MS = 10000;
    /* access modifiers changed from: private */
    public final Set<LicenseValidator> mChecksInProgress = new HashSet();
    private final Context mContext;
    /* access modifiers changed from: private */
    public Handler mHandler;
    private final String mPackageName;
    private final Queue<LicenseValidator> mPendingChecks = new LinkedList();
    private final Policy mPolicy;
    /* access modifiers changed from: private */
    public PublicKey mPublicKey;
    private ILicensingService mService;
    private final String mVersionCode;

    public LicenseChecker(Context context, Policy policy, String encodedPublicKey) {
        this.mContext = context;
        this.mPolicy = policy;
        this.mPublicKey = generatePublicKey(encodedPublicKey);
        this.mPackageName = this.mContext.getPackageName();
        this.mVersionCode = getVersionCode(context, this.mPackageName);
        HandlerThread handlerThread = new HandlerThread("background thread");
        handlerThread.start();
        this.mHandler = new Handler(handlerThread.getLooper());
    }

    private static PublicKey generatePublicKey(String encodedPublicKey) {
        try {
            return KeyFactory.getInstance(KEY_FACTORY_ALGORITHM).generatePublic(new X509EncodedKeySpec(Base64.decode(encodedPublicKey)));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (Base64DecoderException e2) {
            LOG.error("Could not decode from Base64.", (Throwable) e2);
            throw new IllegalArgumentException(e2);
        } catch (InvalidKeySpecException e3) {
            LOG.error("Invalid key specification.", (Throwable) e3);
            throw new IllegalArgumentException(e3);
        }
    }

    public synchronized void checkAccess(LicenseCheckerCallback callback) {
        if (this.mPolicy.allowAccess()) {
            LOG.info("Using cached license response");
            callback.allow(256);
        } else {
            LicenseValidator validator = new LicenseValidator(this.mPolicy, new NullDeviceLimiter(), callback, generateNonce(), this.mPackageName, this.mVersionCode);
            if (this.mService == null) {
                LOG.info("Binding to licensing service.");
                try {
                    if (this.mContext.bindService(new Intent(new String(Base64.decode("Y29tLmFuZHJvaWQudmVuZGluZy5saWNlbnNpbmcuSUxpY2Vuc2luZ1NlcnZpY2U="))).setPackage("com.android.vending"), this, 1)) {
                        this.mPendingChecks.offer(validator);
                    } else {
                        LOG.error("Could not bind to service.");
                        handleServiceConnectionError(validator);
                    }
                } catch (SecurityException e) {
                    callback.applicationError(6);
                } catch (Base64DecoderException e2) {
                    e2.printStackTrace();
                }
            } else {
                this.mPendingChecks.offer(validator);
                runChecks();
            }
        }
        return;
    }

    private void runChecks() {
        while (true) {
            LicenseValidator validator = this.mPendingChecks.poll();
            if (validator != null) {
                try {
                    LOG.info("Calling checkLicense on service for " + validator.getPackageName());
                    this.mService.checkLicense((long) validator.getNonce(), validator.getPackageName(), new ResultListener(validator));
                    this.mChecksInProgress.add(validator);
                } catch (RemoteException e) {
                    LOG.warn("RemoteException in checkLicense call.", (Throwable) e);
                    handleServiceConnectionError(validator);
                }
            } else {
                return;
            }
        }
    }

    /* access modifiers changed from: private */
    public synchronized void finishCheck(LicenseValidator validator) {
        this.mChecksInProgress.remove(validator);
        if (this.mChecksInProgress.isEmpty()) {
            cleanupService();
        }
    }

    private class ResultListener extends ILicenseResultListener.Stub {
        private static final int ERROR_CONTACTING_SERVER = 257;
        private static final int ERROR_INVALID_PACKAGE_NAME = 258;
        private static final int ERROR_NON_MATCHING_UID = 259;
        private Runnable mOnTimeout;
        /* access modifiers changed from: private */
        public final LicenseValidator mValidator;

        public ResultListener(LicenseValidator validator) {
            this.mValidator = validator;
            this.mOnTimeout = new Runnable(LicenseChecker.this) {
                public void run() {
                    LicenseChecker.LOG.info("Check timed out.");
                    LicenseChecker.this.handleServiceConnectionError(ResultListener.this.mValidator);
                    LicenseChecker.this.finishCheck(ResultListener.this.mValidator);
                }
            };
            startTimeout();
        }

        public void verifyLicense(final int responseCode, final String signedData, final String signature) {
            LicenseChecker.this.mHandler.post(new Runnable() {
                public void run() {
                    LicenseChecker.LOG.info("Received response.");
                    if (LicenseChecker.this.mChecksInProgress.contains(ResultListener.this.mValidator)) {
                        ResultListener.this.clearTimeout();
                        ResultListener.this.mValidator.verify(LicenseChecker.this.mPublicKey, responseCode, signedData, signature);
                        LicenseChecker.this.finishCheck(ResultListener.this.mValidator);
                    }
                }
            });
        }

        private void startTimeout() {
            LicenseChecker.LOG.info("Start monitoring timeout.");
            LicenseChecker.this.mHandler.postDelayed(this.mOnTimeout, 10000);
        }

        /* access modifiers changed from: private */
        public void clearTimeout() {
            LicenseChecker.LOG.info("Clearing timeout.");
            LicenseChecker.this.mHandler.removeCallbacks(this.mOnTimeout);
        }
    }

    public synchronized void onServiceConnected(ComponentName name, IBinder service) {
        this.mService = ILicensingService.Stub.asInterface(service);
        runChecks();
    }

    public synchronized void onServiceDisconnected(ComponentName name) {
        LOG.warn("Service unexpectedly disconnected.");
        this.mService = null;
    }

    /* access modifiers changed from: private */
    public synchronized void handleServiceConnectionError(LicenseValidator validator) {
        this.mPolicy.processServerResponse(291, (ResponseData) null);
        if (this.mPolicy.allowAccess()) {
            validator.getCallback().allow(291);
        } else {
            validator.getCallback().dontAllow(291);
        }
    }

    private void cleanupService() {
        if (this.mService != null) {
            try {
                this.mContext.unbindService(this);
            } catch (IllegalArgumentException e) {
                LOG.error("Unable to unbind from licensing service (already unbound)", (Throwable) e);
            }
            this.mService = null;
        }
    }

    public synchronized void onDestroy() {
        cleanupService();
        this.mHandler.getLooper().quit();
    }

    private int generateNonce() {
        return RANDOM.nextInt();
    }

    private static String getVersionCode(Context context, String packageName) {
        try {
            return String.valueOf(context.getPackageManager().getPackageInfo(packageName, 0).versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            LOG.error("Package not found. could not get version code.", (Throwable) e);
            return "";
        }
    }
}
