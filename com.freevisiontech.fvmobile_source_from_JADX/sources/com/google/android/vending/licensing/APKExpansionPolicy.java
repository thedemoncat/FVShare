package com.google.android.vending.licensing;

import android.content.Context;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class APKExpansionPolicy implements Policy {
    private static final String DEFAULT_MAX_RETRIES = "0";
    private static final String DEFAULT_RETRY_COUNT = "0";
    private static final String DEFAULT_RETRY_UNTIL = "0";
    private static final String DEFAULT_VALIDITY_TIMESTAMP = "0";
    private static final Logger LOG = LoggerFactory.getLogger("APKExpansionPolicy");
    public static final int MAIN_FILE_URL_INDEX = 0;
    private static final long MILLIS_PER_MINUTE = 60000;
    public static final int PATCH_FILE_URL_INDEX = 1;
    private static final String PREFS_FILE = "com.android.vending.licensing.APKExpansionPolicy";
    private static final String PREF_LAST_RESPONSE = "lastResponse";
    private static final String PREF_MAX_RETRIES = "maxRetries";
    private static final String PREF_RETRY_COUNT = "retryCount";
    private static final String PREF_RETRY_UNTIL = "retryUntil";
    private static final String PREF_VALIDITY_TIMESTAMP = "validityTimestamp";
    private Vector<String> mExpansionFileNames = new Vector<>();
    private Vector<Long> mExpansionFileSizes = new Vector<>();
    private Vector<String> mExpansionURLs = new Vector<>();
    private int mLastResponse;
    private long mLastResponseTime = 0;
    private long mMaxRetries;
    private PreferenceObfuscator mPreferences;
    private long mRetryCount;
    private long mRetryUntil;
    private long mValidityTimestamp;

    public APKExpansionPolicy(Context context, Obfuscator obfuscator) {
        this.mPreferences = new PreferenceObfuscator(context.getSharedPreferences(PREFS_FILE, 0), obfuscator);
        this.mLastResponse = Integer.parseInt(this.mPreferences.getString(PREF_LAST_RESPONSE, Integer.toString(291)));
        this.mValidityTimestamp = Long.parseLong(this.mPreferences.getString(PREF_VALIDITY_TIMESTAMP, "0"));
        this.mRetryUntil = Long.parseLong(this.mPreferences.getString(PREF_RETRY_UNTIL, "0"));
        this.mMaxRetries = Long.parseLong(this.mPreferences.getString(PREF_MAX_RETRIES, "0"));
        this.mRetryCount = Long.parseLong(this.mPreferences.getString(PREF_RETRY_COUNT, "0"));
    }

    public void resetPolicy() {
        this.mPreferences.putString(PREF_LAST_RESPONSE, Integer.toString(291));
        setRetryUntil("0");
        setMaxRetries("0");
        setRetryCount(Long.parseLong("0"));
        setValidityTimestamp("0");
        this.mPreferences.commit();
    }

    public void processServerResponse(int response, ResponseData rawData) {
        if (response != 291) {
            setRetryCount(0);
        } else {
            setRetryCount(this.mRetryCount + 1);
        }
        if (response == 256) {
            Map<String, String> extras = decodeExtras(rawData.extra);
            this.mLastResponse = response;
            setValidityTimestamp(Long.toString(System.currentTimeMillis() + 60000));
            for (String key : extras.keySet()) {
                if (key.equals("VT")) {
                    setValidityTimestamp(extras.get(key));
                } else if (key.equals("GT")) {
                    setRetryUntil(extras.get(key));
                } else if (key.equals("GR")) {
                    setMaxRetries(extras.get(key));
                } else if (key.startsWith("FILE_URL")) {
                    setExpansionURL(Integer.parseInt(key.substring("FILE_URL".length())) - 1, extras.get(key));
                } else if (key.startsWith("FILE_NAME")) {
                    setExpansionFileName(Integer.parseInt(key.substring("FILE_NAME".length())) - 1, extras.get(key));
                } else if (key.startsWith("FILE_SIZE")) {
                    setExpansionFileSize(Integer.parseInt(key.substring("FILE_SIZE".length())) - 1, Long.parseLong(extras.get(key)));
                }
            }
        } else if (response == 561) {
            setValidityTimestamp("0");
            setRetryUntil("0");
            setMaxRetries("0");
        }
        setLastResponse(response);
        this.mPreferences.commit();
    }

    private void setLastResponse(int l) {
        this.mLastResponseTime = System.currentTimeMillis();
        this.mLastResponse = l;
        this.mPreferences.putString(PREF_LAST_RESPONSE, Integer.toString(l));
    }

    private void setRetryCount(long c) {
        this.mRetryCount = c;
        this.mPreferences.putString(PREF_RETRY_COUNT, Long.toString(c));
    }

    public long getRetryCount() {
        return this.mRetryCount;
    }

    private void setValidityTimestamp(String validityTimestamp) {
        Long lValidityTimestamp;
        try {
            lValidityTimestamp = Long.valueOf(Long.parseLong(validityTimestamp));
        } catch (NumberFormatException e) {
            LOG.warn("License validity timestamp (VT) missing, caching for a minute");
            lValidityTimestamp = Long.valueOf(System.currentTimeMillis() + 60000);
            validityTimestamp = Long.toString(lValidityTimestamp.longValue());
        }
        this.mValidityTimestamp = lValidityTimestamp.longValue();
        this.mPreferences.putString(PREF_VALIDITY_TIMESTAMP, validityTimestamp);
    }

    public long getValidityTimestamp() {
        return this.mValidityTimestamp;
    }

    private void setRetryUntil(String retryUntil) {
        Long lRetryUntil;
        try {
            lRetryUntil = Long.valueOf(Long.parseLong(retryUntil));
        } catch (NumberFormatException e) {
            LOG.warn("License retry timestamp (GT) missing, grace period disabled");
            retryUntil = "0";
            lRetryUntil = 0L;
        }
        this.mRetryUntil = lRetryUntil.longValue();
        this.mPreferences.putString(PREF_RETRY_UNTIL, retryUntil);
    }

    public long getRetryUntil() {
        return this.mRetryUntil;
    }

    private void setMaxRetries(String maxRetries) {
        Long lMaxRetries;
        try {
            lMaxRetries = Long.valueOf(Long.parseLong(maxRetries));
        } catch (NumberFormatException e) {
            LOG.warn("Licence retry count (GR) missing, grace period disabled");
            maxRetries = "0";
            lMaxRetries = 0L;
        }
        this.mMaxRetries = lMaxRetries.longValue();
        this.mPreferences.putString(PREF_MAX_RETRIES, maxRetries);
    }

    public long getMaxRetries() {
        return this.mMaxRetries;
    }

    public int getExpansionURLCount() {
        return this.mExpansionURLs.size();
    }

    public String getExpansionURL(int index) {
        if (index < this.mExpansionURLs.size()) {
            return this.mExpansionURLs.elementAt(index);
        }
        return null;
    }

    public void setExpansionURL(int index, String URL) {
        if (index >= this.mExpansionURLs.size()) {
            this.mExpansionURLs.setSize(index + 1);
        }
        this.mExpansionURLs.set(index, URL);
    }

    public String getExpansionFileName(int index) {
        if (index < this.mExpansionFileNames.size()) {
            return this.mExpansionFileNames.elementAt(index);
        }
        return null;
    }

    public void setExpansionFileName(int index, String name) {
        if (index >= this.mExpansionFileNames.size()) {
            this.mExpansionFileNames.setSize(index + 1);
        }
        this.mExpansionFileNames.set(index, name);
    }

    public long getExpansionFileSize(int index) {
        if (index < this.mExpansionFileSizes.size()) {
            return this.mExpansionFileSizes.elementAt(index).longValue();
        }
        return -1;
    }

    public void setExpansionFileSize(int index, long size) {
        if (index >= this.mExpansionFileSizes.size()) {
            this.mExpansionFileSizes.setSize(index + 1);
        }
        this.mExpansionFileSizes.set(index, Long.valueOf(size));
    }

    public boolean allowAccess() {
        long ts = System.currentTimeMillis();
        if (this.mLastResponse == 256) {
            if (ts <= this.mValidityTimestamp) {
                return true;
            }
            return false;
        } else if (this.mLastResponse != 291 || ts >= this.mLastResponseTime + 60000) {
            return false;
        } else {
            if (ts <= this.mRetryUntil || this.mRetryCount <= this.mMaxRetries) {
                return true;
            }
            return false;
        }
    }

    private Map<String, String> decodeExtras(String extras) {
        Map<String, String> results = new HashMap<>();
        try {
            for (NameValuePair item : URLEncodedUtils.parse(new URI("?" + extras), "UTF-8")) {
                String name = item.getName();
                int i = 0;
                while (results.containsKey(name)) {
                    i++;
                    name = item.getName() + i;
                }
                results.put(name, item.getValue());
            }
        } catch (URISyntaxException e) {
            LOG.warn("Invalid syntax error while decoding extras data from server.");
        }
        return results;
    }
}
