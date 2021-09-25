package com.github.kayvannj.permission_utils;

import android.support.p001v4.app.ActivityCompat;
import android.support.p001v4.app.Fragment;
import android.support.p001v4.content.ContextCompat;
import android.support.p003v7.app.AppCompatActivity;
import android.util.Log;
import java.util.ArrayList;

public class PermissionUtil {
    public static PermissionObject with(AppCompatActivity activity) {
        return new PermissionObject(activity);
    }

    public static PermissionObject with(Fragment fragment) {
        return new PermissionObject(fragment);
    }

    public static class PermissionObject {
        private AppCompatActivity mActivity;
        private Fragment mFragment;

        PermissionObject(AppCompatActivity activity) {
            this.mActivity = activity;
        }

        PermissionObject(Fragment fragment) {
            this.mFragment = fragment;
        }

        public boolean has(String permissionName) {
            int permissionCheck;
            if (this.mActivity != null) {
                permissionCheck = ContextCompat.checkSelfPermission(this.mActivity, permissionName);
            } else {
                permissionCheck = ContextCompat.checkSelfPermission(this.mFragment.getContext(), permissionName);
            }
            return permissionCheck == 0;
        }

        public PermissionRequestObject request(String permissionName) {
            if (this.mActivity != null) {
                return new PermissionRequestObject(this.mActivity, new String[]{permissionName});
            }
            return new PermissionRequestObject(this.mFragment, new String[]{permissionName});
        }

        public PermissionRequestObject request(String... permissionNames) {
            return new PermissionRequestObject(this.mActivity, permissionNames);
        }
    }

    public static class PermissionRequestObject {
        private static final String TAG = PermissionObject.class.getSimpleName();
        private AppCompatActivity mActivity;
        private Func mDenyFunc;
        private Fragment mFragment;
        private Func mGrantFunc;
        private String[] mPermissionNames;
        private ArrayList<SinglePermission> mPermissionsWeDontHave;
        private Func3 mRationalFunc;
        private int mRequestCode;
        private Func2 mResultFunc;

        public PermissionRequestObject(AppCompatActivity activity, String[] permissionNames) {
            this.mActivity = activity;
            this.mPermissionNames = permissionNames;
        }

        public PermissionRequestObject(Fragment fragment, String[] permissionNames) {
            this.mFragment = fragment;
            this.mPermissionNames = permissionNames;
        }

        public PermissionRequestObject ask(int reqCode) {
            this.mRequestCode = reqCode;
            this.mPermissionsWeDontHave = new ArrayList<>(this.mPermissionNames.length);
            for (String mPermissionName : this.mPermissionNames) {
                this.mPermissionsWeDontHave.add(new SinglePermission(mPermissionName));
            }
            if (needToAsk()) {
                Log.i(TAG, "Asking for permission");
                if (this.mActivity != null) {
                    ActivityCompat.requestPermissions(this.mActivity, this.mPermissionNames, reqCode);
                } else {
                    this.mFragment.requestPermissions(this.mPermissionNames, reqCode);
                }
            } else {
                Log.i(TAG, "No need to ask for permission");
                if (this.mGrantFunc != null) {
                    this.mGrantFunc.call();
                }
            }
            return this;
        }

        private boolean needToAsk() {
            int checkRes;
            boolean shouldShowRequestPermissionRationale;
            ArrayList<SinglePermission> neededPermissions = new ArrayList<>(this.mPermissionsWeDontHave);
            for (int i = 0; i < this.mPermissionsWeDontHave.size(); i++) {
                SinglePermission perm = this.mPermissionsWeDontHave.get(i);
                if (this.mActivity != null) {
                    checkRes = ContextCompat.checkSelfPermission(this.mActivity, perm.getPermissionName());
                } else {
                    checkRes = ContextCompat.checkSelfPermission(this.mFragment.getContext(), perm.getPermissionName());
                }
                if (checkRes == 0) {
                    neededPermissions.remove(perm);
                } else {
                    if (this.mActivity != null) {
                        shouldShowRequestPermissionRationale = ActivityCompat.shouldShowRequestPermissionRationale(this.mActivity, perm.getPermissionName());
                    } else {
                        shouldShowRequestPermissionRationale = this.mFragment.shouldShowRequestPermissionRationale(perm.getPermissionName());
                    }
                    if (shouldShowRequestPermissionRationale) {
                        perm.setRationalNeeded(true);
                    }
                }
            }
            this.mPermissionsWeDontHave = neededPermissions;
            this.mPermissionNames = new String[this.mPermissionsWeDontHave.size()];
            for (int i2 = 0; i2 < this.mPermissionsWeDontHave.size(); i2++) {
                this.mPermissionNames[i2] = this.mPermissionsWeDontHave.get(i2).getPermissionName();
            }
            return this.mPermissionsWeDontHave.size() != 0;
        }

        public PermissionRequestObject onRational(Func3 rationalFunc) {
            this.mRationalFunc = rationalFunc;
            return this;
        }

        public PermissionRequestObject onAllGranted(Func grantFunc) {
            this.mGrantFunc = grantFunc;
            return this;
        }

        public PermissionRequestObject onAnyDenied(Func denyFunc) {
            this.mDenyFunc = denyFunc;
            return this;
        }

        public PermissionRequestObject onResult(Func2 resultFunc) {
            this.mResultFunc = resultFunc;
            return this;
        }

        public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
            if (this.mRequestCode == requestCode) {
                Log.i(TAG, String.format("ReqCode: %d, ResCode: %d, PermissionName: %s", new Object[]{Integer.valueOf(requestCode), Integer.valueOf(grantResults[0]), permissions[0]}));
                if (this.mResultFunc != null) {
                    Log.i(TAG, "Calling Results Func");
                    this.mResultFunc.call(requestCode, permissions, grantResults);
                    return;
                }
                int i = 0;
                while (i < permissions.length) {
                    if (grantResults[i] != -1) {
                        i++;
                    } else if (this.mPermissionsWeDontHave.get(i).isRationalNeeded() && this.mRationalFunc != null) {
                        Log.i(TAG, "Calling Rational Func");
                        this.mRationalFunc.call(this.mPermissionsWeDontHave.get(i).getPermissionName());
                        return;
                    } else if (this.mDenyFunc != null) {
                        Log.i(TAG, "Calling Deny Func");
                        this.mDenyFunc.call();
                        return;
                    } else {
                        Log.e(TAG, "NUll DENY FUNCTIONS");
                        return;
                    }
                }
                if (this.mGrantFunc != null) {
                    Log.i(TAG, "Calling Grant Func");
                    this.mGrantFunc.call();
                    return;
                }
                Log.e(TAG, "NUll GRANT FUNCTIONS");
            }
        }
    }
}
