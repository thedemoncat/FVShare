package com.freevisiontech.cameralib.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.p001v4.content.ContextCompat;
import com.google.android.vending.expansion.downloader.Constants;

public class LocationProvider {
    private static final String TAG = "HHBLocationProvider";
    private final Context context;
    private MyLocationListener[] locationListeners;
    private final LocationManager locationManager;
    private volatile boolean test_force_no_location;

    public LocationProvider(Context context2) {
        this.context = context2;
        this.locationManager = (LocationManager) context2.getSystemService("location");
    }

    public Location getLocation() {
        if (this.locationListeners == null) {
            return null;
        }
        if (this.test_force_no_location) {
            return null;
        }
        for (MyLocationListener locationListener : this.locationListeners) {
            Location location = locationListener.getLocation();
            if (location != null) {
                return location;
            }
        }
        return null;
    }

    private static class MyLocationListener implements LocationListener {
        private Location location;
        volatile boolean test_has_received_location;

        private MyLocationListener() {
        }

        /* access modifiers changed from: package-private */
        public Location getLocation() {
            return this.location;
        }

        public void onLocationChanged(Location location2) {
            CameraUtils.LogV(LocationProvider.TAG, "onLocationChanged");
            this.test_has_received_location = true;
            if (location2 == null) {
                return;
            }
            if (location2.getLatitude() != 0.0d || location2.getLongitude() != 0.0d) {
                this.location = location2;
            }
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case 0:
                case 1:
                    if (status == 0) {
                        CameraUtils.LogV(LocationProvider.TAG, "location provider out of service");
                    } else if (status == 1) {
                        CameraUtils.LogV(LocationProvider.TAG, "location provider temporarily unavailable");
                    }
                    this.location = null;
                    this.test_has_received_location = false;
                    return;
                default:
                    return;
            }
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
            CameraUtils.LogV(LocationProvider.TAG, "onProviderDisabled");
            this.location = null;
            this.test_has_received_location = false;
        }
    }

    public boolean setupLocationListener() {
        boolean has_fine_location_permission;
        if (this.locationListeners == null) {
            if (Build.VERSION.SDK_INT >= 23) {
                boolean has_coarse_location_permission = ContextCompat.checkSelfPermission(this.context, "android.permission.ACCESS_COARSE_LOCATION") == 0;
                if (ContextCompat.checkSelfPermission(this.context, "android.permission.ACCESS_FINE_LOCATION") == 0) {
                    has_fine_location_permission = true;
                } else {
                    has_fine_location_permission = false;
                }
                if (!has_coarse_location_permission || !has_fine_location_permission) {
                    CameraUtils.LogV(TAG, "location permission not available");
                    return false;
                }
            }
            this.locationListeners = new MyLocationListener[2];
            this.locationListeners[0] = new MyLocationListener();
            this.locationListeners[1] = new MyLocationListener();
            if (this.locationManager.getAllProviders().contains("network")) {
                this.locationManager.requestLocationUpdates("network", 1000, 0.0f, this.locationListeners[1]);
            } else {
                CameraUtils.LogV(TAG, "don't have a NETWORK_PROVIDER");
            }
            if (this.locationManager.getAllProviders().contains("gps")) {
                this.locationManager.requestLocationUpdates("gps", 1000, 0.0f, this.locationListeners[0]);
                CameraUtils.LogV(TAG, "created fine (gps) location listener");
            } else {
                CameraUtils.LogV(TAG, "don't have a GPS_PROVIDER");
            }
        }
        return true;
    }

    public void freeLocationListeners() {
        boolean has_fine_location_permission;
        CameraUtils.LogV(TAG, "freeLocationListeners");
        if (this.locationListeners != null) {
            if (Build.VERSION.SDK_INT >= 23) {
                boolean has_coarse_location_permission = ContextCompat.checkSelfPermission(this.context, "android.permission.ACCESS_COARSE_LOCATION") == 0;
                if (ContextCompat.checkSelfPermission(this.context, "android.permission.ACCESS_FINE_LOCATION") == 0) {
                    has_fine_location_permission = true;
                } else {
                    has_fine_location_permission = false;
                }
                if (!has_coarse_location_permission && !has_fine_location_permission) {
                    CameraUtils.LogV(TAG, "location permission not available");
                    return;
                }
            }
            for (int i = 0; i < this.locationListeners.length; i++) {
                this.locationManager.removeUpdates(this.locationListeners[i]);
                this.locationListeners[i] = null;
            }
            this.locationListeners = null;
        }
    }

    public void setForceNoLocation(boolean test_force_no_location2) {
        this.test_force_no_location = test_force_no_location2;
    }

    public boolean hasLocationListeners() {
        if (this.locationListeners == null || this.locationListeners.length != 2) {
            return false;
        }
        for (MyLocationListener locationListener : this.locationListeners) {
            if (locationListener == null) {
                return false;
            }
        }
        return true;
    }

    public static String locationToDMS(double coord) {
        boolean is_zero;
        boolean is_zero2;
        boolean is_zero3;
        String sign = coord < 0.0d ? Constants.FILENAME_SEQUENCE_SEPARATOR : "";
        double coord2 = Math.abs(coord);
        int intPart = (int) coord2;
        if (intPart == 0) {
            is_zero = true;
        } else {
            is_zero = false;
        }
        String degrees = String.valueOf(intPart);
        double coord3 = (coord2 - ((double) intPart)) * 60.0d;
        int intPart2 = (int) coord3;
        if (!is_zero || intPart2 != 0) {
            is_zero2 = false;
        } else {
            is_zero2 = true;
        }
        String minutes = String.valueOf(intPart2);
        int intPart3 = (int) ((coord3 - ((double) intPart2)) * 60.0d);
        if (!is_zero2 || intPart3 != 0) {
            is_zero3 = false;
        } else {
            is_zero3 = true;
        }
        String seconds = String.valueOf(intPart3);
        if (is_zero3) {
            sign = "";
        }
        return sign + degrees + "°" + minutes + "'" + seconds + "\"";
    }
}
