package com.freevisiontech.fvmobile.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.p001v4.app.Fragment;
import android.support.p001v4.view.PagerAdapter;
import android.support.p001v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.activity.FVAppUpgradeActivity;
import com.freevisiontech.fvmobile.activity.FVHelpActivity;
import com.freevisiontech.fvmobile.activity.FVHomeActivity;
import com.freevisiontech.fvmobile.application.MyApplication;
import com.freevisiontech.fvmobile.bean.FVPTZBean;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utility.Constants;
import com.freevisiontech.fvmobile.utility.SPUtils;
import com.freevisiontech.fvmobile.utility.SharePrefConstant;
import com.freevisiontech.fvmobile.utility.Util;
import com.freevisiontech.fvmobile.utils.BleUpgradeUtil;
import com.freevisiontech.fvmobile.utils.LoadingView;
import com.freevisiontech.fvmobile.utils.PermissionSystemSettingDialog;
import com.freevisiontech.fvmobile.utils.SPUtil;
import com.freevisiontech.fvmobile.utils.ScaleInTransformer;
import com.vise.log.ViseLog;
import java.util.ArrayList;

public class FVPTZListFragment extends Fragment implements View.OnClickListener {
    /* access modifiers changed from: private */
    public int DEVICE_SUM = 3;
    /* access modifiers changed from: private */
    public int MAX_INDEX = (this.DEVICE_SUM - 1);
    private int cardWidth;
    /* access modifiers changed from: private */
    public int currentItem = 0;
    @Bind({2131755249})
    ImageView img_back;
    @Bind({2131756129})
    ImageView img_right;
    /* access modifiers changed from: private */
    public ArrayList<ImageView> indicatorList = new ArrayList<>();
    @Bind({2131756193})
    ImageView indicator_0;
    @Bind({2131756194})
    ImageView indicator_1;
    @Bind({2131756195})
    ImageView indicator_2;
    public boolean isAppNeedUpgrade = false;
    @Bind({2131756212})
    ImageView iv_ptz_title;
    /* access modifiers changed from: private */
    public LoadingView mConnLoadingProgress;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public String mPtzHelpType = "";
    private PagerAdapter pagerAdapter;
    /* access modifiers changed from: private */
    public ArrayList<FVPTZBean> ptzBeans = new ArrayList<>();
    /* access modifiers changed from: private */
    public ArrayList<View> ptzFragments = new ArrayList<>();
    @Bind({2131756127})
    TextView tv_center_title;
    @Bind({2131756128})
    TextView tv_right;
    /* access modifiers changed from: private */
    public BleUpgradeUtil.AppVersionUpgradeListener upgradeListener = new BleUpgradeUtil.AppVersionUpgradeListener() {
        public void isAppNeedDownload(boolean result) {
            ViseLog.m1466e("isAppNeedDownload");
            FVPTZListFragment.this.isAppNeedUpgrade = result;
            String ptzType = MyApplication.CURRENT_PTZ_TYPE;
            if (result) {
                ViseLog.m1466e("isAppNeedDownload result true");
                Log.i("Kbein", "isAppNeedDownload:  isAppNeedDownload result true");
                FVPTZListFragment.this.startActivityForResult(new Intent(FVPTZListFragment.this.getActivity(), FVAppUpgradeActivity.class), 0);
            } else {
                ViseLog.m1466e("isAppNeedDownload result false");
                Log.i("KBein", "FVPTZListFragment.isAppNeedDownload():--ptzType--" + ptzType);
                ((FVHomeActivity) FVPTZListFragment.this.getActivity()).ptzMenuSwitcher.checkAutoConnect(ptzType);
            }
            FVPTZListFragment.this.mConnLoadingProgress.dismiss();
        }

        public void isGmuNeedDownoad(boolean gmuResult) {
            ViseLog.m1466e("isGmuNeedDownoad");
            if (gmuResult) {
                BleUpgradeUtil.getInstance().whatDownload(2);
            }
        }

        public void isImuNeedDownoad(boolean imuResult) {
            ViseLog.m1466e("isImuNeedDownoad");
            if (imuResult) {
                BleUpgradeUtil.getInstance().whatDownload(3);
            }
        }
    };
    /* access modifiers changed from: private */

    /* renamed from: vp */
    public ViewPager f1098vp;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(C0853R.layout.layout_ptz_list, container, false);
        ButterKnife.bind((Object) this, view);
        this.mContext = getActivity();
        initViews(view);
        initTitle();
        BleUpgradeUtil.getInstance().init(getActivity());
        return view;
    }

    private void initViews(View root) {
        this.f1098vp = (ViewPager) root.findViewById(C0853R.C0855id.vp_ptz_device_selector);
        this.f1098vp.setPageMargin(getResources().getDimensionPixelSize(C0853R.dimen.vp_page_margin));
        int camera_device_sum = ((Integer) SPUtil.getParam(getActivity(), SharePrefConstant.PTZ_LIST_CAMERA_DEVICE_SUM, 0)).intValue();
        if (camera_device_sum != 0) {
            this.DEVICE_SUM = camera_device_sum;
        }
        if (this.DEVICE_SUM == 1) {
            this.indicator_2.setVisibility(8);
            this.indicator_1.setVisibility(8);
            this.indicator_0.setImageResource(C0853R.mipmap.indicator_selected);
        } else if (this.DEVICE_SUM == 2) {
            this.indicator_2.setVisibility(8);
            this.indicator_0.setImageResource(C0853R.mipmap.indicator_selected);
        }
        this.mConnLoadingProgress = new LoadingView(getActivity());
        this.mConnLoadingProgress.setMessage(getString(C0853R.string.file_show_pro_title));
        this.ptzBeans = new ArrayList<>();
        FVPTZBean bean1 = new FVPTZBean();
        bean1.setDeviceTypeName("");
        bean1.setDeviceDisplayName(BleConstant.FM_200_DISPLAY_NAME);
        bean1.setDeviceServiceUUID(BleConstant.FM200_SERVICE_UUID);
        FVPTZBean bean2 = new FVPTZBean();
        bean2.setDeviceTypeName(BleConstant.FM_300);
        bean2.setDeviceDisplayName(BleConstant.FM_300_DISPLAY_NAME);
        bean2.setDeviceServiceUUID(BleConstant.FM300_SERVICE_UUID);
        FVPTZBean bean3 = new FVPTZBean();
        bean3.setDeviceTypeName(BleConstant.FM_210);
        bean3.setDeviceDisplayName(BleConstant.FM_210_DISPLAY_NAME);
        bean3.setDeviceServiceUUID(BleConstant.FM210_SERVICE_UUID);
        this.ptzBeans.add(bean1);
        this.ptzBeans.add(bean2);
        this.ptzBeans.add(bean3);
        for (int i = 0; i < this.DEVICE_SUM; i++) {
            View view = LayoutInflater.from(getActivity()).inflate(C0853R.layout.ptz_connect, (ViewGroup) null);
            changePtzImg((ImageView) view.findViewById(C0853R.C0855id.iv_background), this.MAX_INDEX - i, false);
            int screenRatio = 1;
            DisplayMetrics displayMetrics = Util.getDisplayMetrics(getActivity());
            if (displayMetrics.heightPixels / displayMetrics.widthPixels >= 2) {
                screenRatio = 2;
            }
            if (screenRatio >= 2) {
                view = LayoutInflater.from(getActivity()).inflate(C0853R.layout.ptz_connect_long, (ViewGroup) null);
                changePtzImg((ImageView) view.findViewById(C0853R.C0855id.iv_background), this.MAX_INDEX - i, true);
            }
            TextView textView = (TextView) view.findViewById(C0853R.C0855id.ptz_device_type_name);
            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "britannic_bold.ttf");
            textView.setTextSize((float) Util.px2sp(38.0f, 1.6f));
            textView.setTypeface(tf);
            textView.setText(this.ptzBeans.get(this.MAX_INDEX - i).getDeviceDisplayName());
            ((Button) view.findViewById(C0853R.C0855id.btn_connect_ptz)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int permSystemSetting = ((Integer) SPUtil.getParam(FVPTZListFragment.this.mContext, SharePrefConstant.PERMISSION_SYSTEM_SETTING, Integer.valueOf(Constants.PERMISSION_SYSTEM_SETTING_REMIND_OPEN))).intValue();
                    if (Build.VERSION.SDK_INT < 23 || permSystemSetting != 107768) {
                        FVPTZListFragment.this.mConnLoadingProgress.show();
                        FVPTZBean curBean = (FVPTZBean) FVPTZListFragment.this.ptzBeans.get(FVPTZListFragment.this.MAX_INDEX - FVPTZListFragment.this.currentItem);
                        BleConstant.SERVICE_UUID_CONFIG = curBean.getDeviceServiceUUID();
                        SPUtils.put(FVPTZListFragment.this.getActivity(), SharePrefConstant.IS_SWITCH_PTZ_TYPE, true);
                        SPUtils.put(FVPTZListFragment.this.getActivity(), SharePrefConstant.CURRENT_PTZ_TYPE, curBean.getDeviceTypeName());
                        MyApplication.CURRENT_PTZ_TYPE = curBean.getDeviceTypeName();
                        SPUtils.put(FVPTZListFragment.this.getActivity(), SharePrefConstant.CURRENT_PTZ_DISPLAY_NAME, curBean.getDeviceDisplayName());
                        BleUpgradeUtil.getInstance().checkWhatNeedUpgrade(FVPTZListFragment.this.upgradeListener);
                        BleUpgradeUtil.getInstance().getCanChargeByWirelessList();
                    } else if (!Settings.System.canWrite(FVPTZListFragment.this.getActivity())) {
                        FVPTZListFragment.this.permissionSystemWriteSetting();
                    } else {
                        FVPTZListFragment.this.mConnLoadingProgress.show();
                        FVPTZBean curBean2 = (FVPTZBean) FVPTZListFragment.this.ptzBeans.get(FVPTZListFragment.this.MAX_INDEX - FVPTZListFragment.this.currentItem);
                        BleConstant.SERVICE_UUID_CONFIG = curBean2.getDeviceServiceUUID();
                        SPUtils.put(FVPTZListFragment.this.getActivity(), SharePrefConstant.IS_SWITCH_PTZ_TYPE, true);
                        SPUtils.put(FVPTZListFragment.this.getActivity(), SharePrefConstant.CURRENT_PTZ_TYPE, curBean2.getDeviceTypeName());
                        MyApplication.CURRENT_PTZ_TYPE = curBean2.getDeviceTypeName();
                        SPUtils.put(FVPTZListFragment.this.getActivity(), SharePrefConstant.CURRENT_PTZ_DISPLAY_NAME, curBean2.getDeviceDisplayName());
                        BleUpgradeUtil.getInstance().checkWhatNeedUpgrade(FVPTZListFragment.this.upgradeListener);
                        BleUpgradeUtil.getInstance().getCanChargeByWirelessList();
                    }
                }
            });
            this.ptzFragments.add(view);
        }
        this.indicatorList.add(this.indicator_0);
        this.indicatorList.add(this.indicator_1);
        this.indicatorList.add(this.indicator_2);
        this.pagerAdapter = new PagerAdapter() {
            public int getCount() {
                return FVPTZListFragment.this.DEVICE_SUM;
            }

            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            public void destroyItem(ViewGroup container, int position, Object object) {
                ViseLog.m1466e("destroyItem position:" + position);
                container.removeView(container);
            }

            public Object instantiateItem(ViewGroup container, int position) {
                ViseLog.m1466e("instantiateItem position : " + position);
                ViseLog.m1466e("instantiateItem position % ptzFragments.size() : " + (position % FVPTZListFragment.this.ptzFragments.size()));
                View currentItem = (View) FVPTZListFragment.this.ptzFragments.get(position);
                if (currentItem.getParent() != null) {
                    ((ViewGroup) currentItem.getParent()).removeView(currentItem);
                }
                container.addView(currentItem, 0);
                return currentItem;
            }
        };
        this.f1098vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ViseLog.m1466e("onPageScrolled position: " + position);
                ViseLog.m1466e("onPageScrolled positionOffset: " + positionOffset);
                ViseLog.m1466e("onPageScrolled positionOffsetPixels: " + positionOffsetPixels);
            }

            public void onPageSelected(int position) {
                ViseLog.m1466e("onPageSelected position: " + position);
                ((ImageView) FVPTZListFragment.this.indicatorList.get(FVPTZListFragment.this.currentItem)).setImageResource(C0853R.mipmap.indicator_unselected);
                int unused = FVPTZListFragment.this.currentItem = position;
                ((ImageView) FVPTZListFragment.this.indicatorList.get(FVPTZListFragment.this.currentItem)).setImageResource(C0853R.mipmap.indicator_selected);
                switch (position) {
                    case 0:
                        if (FVPTZListFragment.this.DEVICE_SUM != 3) {
                            if (FVPTZListFragment.this.DEVICE_SUM != 2) {
                                if (FVPTZListFragment.this.DEVICE_SUM == 1) {
                                    String unused2 = FVPTZListFragment.this.mPtzHelpType = "";
                                    break;
                                }
                            } else {
                                String unused3 = FVPTZListFragment.this.mPtzHelpType = BleConstant.FM_300;
                                break;
                            }
                        } else {
                            String unused4 = FVPTZListFragment.this.mPtzHelpType = BleConstant.FM_210;
                            break;
                        }
                        break;
                    case 1:
                        if (FVPTZListFragment.this.DEVICE_SUM != 3) {
                            if (FVPTZListFragment.this.DEVICE_SUM == 2) {
                                String unused5 = FVPTZListFragment.this.mPtzHelpType = "";
                                break;
                            }
                        } else {
                            String unused6 = FVPTZListFragment.this.mPtzHelpType = BleConstant.FM_300;
                            break;
                        }
                        break;
                    case 2:
                        if (FVPTZListFragment.this.DEVICE_SUM == 3) {
                            String unused7 = FVPTZListFragment.this.mPtzHelpType = "";
                            break;
                        }
                        break;
                }
                CameraUtils.setCurrentPageIndex(FVPTZListFragment.this.MAX_INDEX - position);
                SPUtils.put(FVPTZListFragment.this.getActivity(), SharePrefConstant.LAST_PTZ_TYPE_ENTRANCE, Integer.valueOf(position));
            }

            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case 0:
                        ViseLog.m1466e("onPageScrollStateChanged vp currentItem: " + FVPTZListFragment.this.f1098vp.getCurrentItem());
                        ViseLog.m1467e("MainActivity", "--CurrentItem:" + FVPTZListFragment.this.f1098vp.getCurrentItem());
                        return;
                    default:
                        return;
                }
            }
        });
        this.f1098vp.setAdapter(this.pagerAdapter);
        this.f1098vp.setCurrentItem(((Integer) SPUtils.get(getActivity(), SharePrefConstant.LAST_PTZ_TYPE_ENTRANCE, 0)).intValue());
        this.f1098vp.setPageTransformer(true, new ScaleInTransformer());
        this.img_right.setOnClickListener(this);
    }

    /* access modifiers changed from: private */
    public void permissionSystemWriteSetting() {
        PermissionSystemSettingDialog OpenBleOrGpsDialog = new PermissionSystemSettingDialog(this.mContext);
        OpenBleOrGpsDialog.show();
        Display d = getActivity().getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams params = OpenBleOrGpsDialog.getWindow().getAttributes();
        params.width = (int) (((double) d.getWidth()) * 0.6d);
        params.height = (int) (((double) d.getHeight()) * 0.25d);
        params.gravity = 1;
        params.gravity = 16;
        OpenBleOrGpsDialog.getWindow().setAttributes(params);
    }

    private void changePtzImg(ImageView ptzIv, int i, boolean isLong) {
        int ptzMImgId;
        int ptzSImgId;
        int ptzXImgId;
        if (!isLong) {
            ptzMImgId = C0853R.mipmap.banner_m;
            ptzSImgId = C0853R.mipmap.banner_s;
            ptzXImgId = C0853R.mipmap.banner_x;
        } else {
            ptzMImgId = C0853R.mipmap.banner_m_long;
            ptzSImgId = C0853R.mipmap.banner_s_long;
            ptzXImgId = C0853R.mipmap.banner_x_long;
        }
        switch (i) {
            case 0:
                ptzIv.setImageResource(ptzMImgId);
                return;
            case 1:
                ptzIv.setImageResource(ptzSImgId);
                return;
            case 2:
                ptzIv.setImageResource(ptzXImgId);
                return;
            default:
                return;
        }
    }

    private void initTitle() {
        this.img_back.setVisibility(8);
        this.tv_right.setVisibility(8);
        this.img_right.setVisibility(0);
        this.img_right.setImageResource(C0853R.mipmap.ic_main_bangzhu);
        this.iv_ptz_title.setVisibility(0);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0853R.C0855id.img_right:
                Intent intent = new Intent(getActivity(), FVHelpActivity.class);
                intent.putExtra("help_ptz_type", this.mPtzHelpType);
                startActivity(intent);
                return;
            default:
                return;
        }
    }
}
