package com.google.android.vending.licensing;

import android.text.TextUtils;
import com.google.android.vending.licensing.util.Base64;
import com.google.android.vending.licensing.util.Base64DecoderException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class LicenseValidator {
    private static final int ERROR_CONTACTING_SERVER = 257;
    private static final int ERROR_INVALID_PACKAGE_NAME = 258;
    private static final int ERROR_NON_MATCHING_UID = 259;
    private static final int ERROR_NOT_MARKET_MANAGED = 3;
    private static final int ERROR_OVER_QUOTA = 5;
    private static final int ERROR_SERVER_FAILURE = 4;
    private static final int LICENSED = 0;
    private static final int LICENSED_OLD_KEY = 2;
    private static final Logger LOG = LoggerFactory.getLogger("LicenseValidator");
    private static final int NOT_LICENSED = 1;
    private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
    private final LicenseCheckerCallback mCallback;
    private final DeviceLimiter mDeviceLimiter;
    private final int mNonce;
    private final String mPackageName;
    private final Policy mPolicy;
    private final String mVersionCode;

    LicenseValidator(Policy policy, DeviceLimiter deviceLimiter, LicenseCheckerCallback callback, int nonce, String packageName, String versionCode) {
        this.mPolicy = policy;
        this.mDeviceLimiter = deviceLimiter;
        this.mCallback = callback;
        this.mNonce = nonce;
        this.mPackageName = packageName;
        this.mVersionCode = versionCode;
    }

    public LicenseCheckerCallback getCallback() {
        return this.mCallback;
    }

    public int getNonce() {
        return this.mNonce;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public void verify(PublicKey publicKey, int responseCode, String signedData, String signature) {
        String userId = null;
        ResponseData data = null;
        if (responseCode == 0 || responseCode == 1 || responseCode == 2) {
            try {
                if (TextUtils.isEmpty(signedData)) {
                    LOG.error("Signature verification failed: signedData is empty");
                    handleInvalidResponse();
                    return;
                }
                Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);
                sig.initVerify(publicKey);
                sig.update(signedData.getBytes());
                if (!sig.verify(Base64.decode(signature))) {
                    LOG.error("Signature verification failed.");
                    handleInvalidResponse();
                    return;
                }
                try {
                    data = ResponseData.parse(signedData);
                    if (data.responseCode != responseCode) {
                        LOG.error("Response codes don't match.");
                        handleInvalidResponse();
                        return;
                    } else if (data.nonce != this.mNonce) {
                        LOG.error("Nonce doesn't match.");
                        handleInvalidResponse();
                        return;
                    } else if (!data.packageName.equals(this.mPackageName)) {
                        LOG.error("Package name doesn't match.");
                        handleInvalidResponse();
                        return;
                    } else if (!data.versionCode.equals(this.mVersionCode)) {
                        LOG.error("Version codes don't match.");
                        handleInvalidResponse();
                        return;
                    } else {
                        userId = data.userId;
                        if (TextUtils.isEmpty(userId)) {
                            LOG.error("User identifier is empty.");
                            handleInvalidResponse();
                            return;
                        }
                    }
                } catch (IllegalArgumentException e) {
                    LOG.error("Could not parse response.", (Throwable) e);
                    handleInvalidResponse();
                    return;
                }
            } catch (NoSuchAlgorithmException e2) {
                throw new RuntimeException(e2);
            } catch (InvalidKeyException e3) {
                handleApplicationError(5);
                return;
            } catch (SignatureException e4) {
                throw new RuntimeException(e4);
            } catch (Base64DecoderException e5) {
                LOG.error("Could not Base64-decode signature.", (Throwable) e5);
                handleInvalidResponse();
                return;
            }
        }
        switch (responseCode) {
            case 0:
            case 2:
                handleResponse(this.mDeviceLimiter.isDeviceAllowed(userId), data);
                return;
            case 1:
                handleResponse(Policy.NOT_LICENSED, data);
                return;
            case 3:
                handleApplicationError(3);
                return;
            case 4:
                LOG.warn("An error has occurred on the licensing server.");
                handleResponse(291, data);
                return;
            case 5:
                LOG.warn("Licensing server is refusing to talk to this device, over quota.");
                handleResponse(291, data);
                return;
            case 257:
                LOG.warn("Error contacting licensing server.");
                handleResponse(291, data);
                return;
            case 258:
                handleApplicationError(1);
                return;
            case 259:
                handleApplicationError(2);
                return;
            default:
                LOG.error("Unknown response code for license check.");
                handleInvalidResponse();
                return;
        }
    }

    private void handleResponse(int response, ResponseData rawData) {
        this.mPolicy.processServerResponse(response, rawData);
        if (this.mPolicy.allowAccess()) {
            this.mCallback.allow(response);
        } else {
            this.mCallback.dontAllow(response);
        }
    }

    private void handleApplicationError(int code) {
        this.mCallback.applicationError(code);
    }

    private void handleInvalidResponse() {
        this.mCallback.dontAllow(Policy.NOT_LICENSED);
    }
}
