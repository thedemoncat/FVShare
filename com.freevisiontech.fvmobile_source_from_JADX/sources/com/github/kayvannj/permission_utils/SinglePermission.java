package com.github.kayvannj.permission_utils;

public class SinglePermission {
    private String mPermissionName;
    private boolean mRationalNeeded = false;
    private String mReason;

    public SinglePermission(String permissionName) {
        this.mPermissionName = permissionName;
    }

    public SinglePermission(String permissionName, String reason) {
        this.mPermissionName = permissionName;
        this.mReason = reason;
    }

    public boolean isRationalNeeded() {
        return this.mRationalNeeded;
    }

    public void setRationalNeeded(boolean rationalNeeded) {
        this.mRationalNeeded = rationalNeeded;
    }

    public String getReason() {
        return this.mReason == null ? "" : this.mReason;
    }

    public void setReason(String reason) {
        this.mReason = reason;
    }

    public String getPermissionName() {
        return this.mPermissionName;
    }

    public void setPermissionName(String permissionName) {
        this.mPermissionName = permissionName;
    }
}
