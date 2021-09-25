package com.freevisiontech.fvmobile.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.p001v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.ViseBluetooth;
import com.freevisiontech.fvmobile.activity.FVHelpActivity;
import com.freevisiontech.fvmobile.activity.FVHomeActivity;
import com.freevisiontech.fvmobile.activity.FVMainActivity;
import com.freevisiontech.fvmobile.adapter.DeviceAdapter;
import com.freevisiontech.fvmobile.callback.scan.PeriodLScanCallback;
import com.freevisiontech.fvmobile.callback.scan.PeriodScanCallback;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.freevisiontech.fvmobile.common.StateScan;
import com.freevisiontech.fvmobile.model.BluetoothLeDevice;
import com.freevisiontech.fvmobile.model.BluetoothLeDeviceStore;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utility.SPUtils;
import com.freevisiontech.fvmobile.utility.SharePrefConstant;
import com.freevisiontech.fvmobile.utility.Util;
import com.freevisiontech.fvmobile.utils.BleByteUtil;
import com.freevisiontech.fvmobile.utils.BleDeviceBean;
import com.freevisiontech.fvmobile.utils.BleNotifyDataUtil;
import com.freevisiontech.fvmobile.utils.BlePtzParasConstant;
import com.freevisiontech.fvmobile.utils.BlueToothHistoryUtil;
import com.freevisiontech.fvmobile.utils.Event;
import com.freevisiontech.fvmobile.utils.EventBusUtil;
import com.freevisiontech.fvmobile.utils.HexUtil;
import com.freevisiontech.fvmobile.utils.LoadingView;
import com.vise.log.ViseLog;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FVPTZFragment extends Fragment implements View.OnClickListener {
    private static final int COMPARE_TO_GMU_AND_IMU_VERSION = 2;
    private static final int DISCONECT_AND_RECONNECT = 3;
    private static final int GET_PTZ_GMU_AND_IMU_VERSION = 1;
    private static final int NEW_BLE_DEVICE_FOUND = 11;
    private static final int SCAN_BLE_DEVICE_TIMEOUT = -1;
    private static final int SCAN_BLE_TIMEOUT_DURATION = 10000;
    public static final String TAG = "FVPTZFragment";
    /* access modifiers changed from: private */
    public DeviceAdapter adapter;
    /* access modifiers changed from: private */
    public Boolean autoConnectTimeout = false;
    /* access modifiers changed from: private */
    public volatile List<BluetoothLeDevice> bluetoothLeDeviceList = new ArrayList();
    /* access modifiers changed from: private */
    public BluetoothLeDeviceStore bluetoothLeDeviceStore;
    private String currentPTZType;
    /* access modifiers changed from: private */
    public BluetoothLeDevice device;
    @Bind({2131755612})
    ListView deviceLv;
    @Bind({2131755249})
    ImageView img_back;
    @Bind({2131756263})
    ImageView img_ptz_scan;
    @Bind({2131756129})
    ImageView img_right;
    /* access modifiers changed from: private */
    public boolean isAutoConnect = true;
    /* access modifiers changed from: private */
    public boolean isConnected = false;
    @Bind({2131756212})
    ImageView iv_ptz_title;
    @Bind({2131755793})
    ImageView iv_refresh_ble;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case -1:
                    ViseLog.m1466e("FVPTZFragment SCAN_BLE_DEVICE_TIMEOUT dismiss");
                    if (FVPTZFragment.this.isAutoConnect) {
                        FVPTZFragment.this.ptzMenuSwitcher.onAutoConnectTimeout();
                        boolean unused = FVPTZFragment.this.isAutoConnect = false;
                        return;
                    }
                    if (FVPTZFragment.this.progressDialog != null) {
                        FVPTZFragment.this.progressDialog.dismiss();
                    }
                    FVPTZFragment.this.iv_refresh_ble.clearAnimation();
                    return;
                case 1:
                    if (FVPTZFragment.this.isConnected) {
                        BleByteUtil.getPTZSingleParameters((byte) 26);
                        FVPTZFragment.this.mHandler.sendEmptyMessageDelayed(2, 500);
                        return;
                    }
                    return;
                case 2:
                    FVPTZFragment.this.iv_refresh_ble.clearAnimation();
                    ViseLog.m1466e("FVPTZFragmentCOMPARE_TO_GMU_AND_IMU_VERSION dismiss");
                    FVPTZFragment.this.progressDialog.dismiss();
                    FVPTZFragment.this.ptzMenuSwitcher.hideAutoConnectHint();
                    boolean unused2 = FVPTZFragment.this.isAutoConnect = false;
                    FVPTZFragment.this.enterMainActivity();
                    return;
                case 3:
                    ViseLog.m1466e("重连");
                    if (FVPTZFragment.this.progressDialog != null) {
                        FVPTZFragment.this.progressDialog.dismiss();
                    }
                    FVPTZFragment.this.progressDialog.show();
                    FVPTZFragment.this.progressDialog.setMessage(FVPTZFragment.this.getString(C0853R.string.label_connect_device_ing));
                    ViseBluetooth.getInstance().connect(FVPTZFragment.this.device, false);
                    return;
                case 11:
                    FVPTZFragment.this.adapter.setDeviceList(FVPTZFragment.this.bluetoothLeDeviceList);
                    if (FVPTZFragment.this.isAutoConnect) {
                        FVPTZFragment.this.checkAutoConnect();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private String mImuFirmwareVersion;
    private BluetoothGattCharacteristic mWriteCharacteristic;
    /* access modifiers changed from: private */
    public Animation operatingAnim;
    private PeriodLScanCallback periodLScanCallback;
    private PeriodScanCallback periodScanCallback = new PeriodScanCallback() {
        public void scanTimeout() {
            ViseLog.m1466e("FVPTZFragment scan over");
            ViseLog.m1466e("FVPTZFragment autoConnect: " + FVPTZFragment.this.autoConnectTimeout);
            ViseLog.m1466e("FVPTZFragment PeriodScanCallback SCAN_BLE_DEVICE_TIMEOUT dismiss");
            FVPTZFragment.this.mHandler.sendEmptyMessage(-1);
        }

        public void onDeviceFound(BluetoothLeDevice bluetoothLeDevice) {
            if (FVPTZFragment.this.bluetoothLeDeviceStore != null && bluetoothLeDevice != null && bluetoothLeDevice.getName() != null && bluetoothLeDevice.getName().contains("VILTA") && !FVPTZFragment.this.bluetoothLeDeviceStore.getDeviceList().contains(bluetoothLeDevice)) {
                FVPTZFragment.this.bluetoothLeDeviceStore.addDevice(bluetoothLeDevice);
                List unused = FVPTZFragment.this.bluetoothLeDeviceList = FVPTZFragment.this.bluetoothLeDeviceStore.getDeviceList();
                FVPTZFragment.this.mHandler.sendEmptyMessageDelayed(11, 100);
            }
        }
    };
    /* access modifiers changed from: private */
    public LoadingView progressDialog;
    /* access modifiers changed from: private */
    public FVHomeActivity.PtzMenuSwitcher ptzMenuSwitcher;
    @Bind({2131755786})
    RelativeLayout rl_content_scaning;
    @Bind({2131755789})
    TextView tv_back;
    @Bind({2131756127})
    TextView tv_center_title;
    @Bind({2131755790})
    TextView tv_enter_camera;
    @Bind({2131756261})
    TextView tv_ptz_type_name;
    @Bind({2131756128})
    TextView tv_right;

    public String getCurrentPTZType() {
        return this.currentPTZType;
    }

    public void setCurrentPTZType(String currentPTZType2) {
        this.currentPTZType = currentPTZType2;
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(C0853R.layout.ptz_device_list, container, false);
        ButterKnife.bind((Object) this, view);
        initTitle();
        initView();
        initBluetooth();
        return view;
    }

    private void initTitle() {
        this.img_back.setVisibility(8);
        this.tv_right.setVisibility(8);
        this.img_right.setVisibility(0);
        this.img_right.setImageResource(C0853R.mipmap.ic_main_bangzhu);
        this.iv_ptz_title.setVisibility(0);
    }

    private void initView() {
        this.img_right.setOnClickListener(this);
        this.tv_back.setOnClickListener(this);
        this.tv_enter_camera.setOnClickListener(this);
        this.iv_refresh_ble.setOnClickListener(this);
        this.ptzMenuSwitcher = ((FVHomeActivity) getActivity()).ptzMenuSwitcher;
        this.operatingAnim = AnimationUtils.loadAnimation(getActivity(), C0853R.anim.rotate);
        this.operatingAnim.setInterpolator(new LinearInterpolator());
        this.progressDialog = new LoadingView(getActivity());
        this.progressDialog.setCancelable(true);
        this.progressDialog.setCanceledOnTouchOutside(false);
    }

    private void initBluetooth() {
        this.bluetoothLeDeviceStore = new BluetoothLeDeviceStore();
        this.adapter = new DeviceAdapter(getActivity());
        this.deviceLv.setAdapter(this.adapter);
        this.deviceLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                FVPTZFragment.this.stopScan();
                FVPTZFragment.this.iv_refresh_ble.clearAnimation();
                BluetoothLeDevice unused = FVPTZFragment.this.device = (BluetoothLeDevice) FVPTZFragment.this.adapter.getItem(position);
                if (FVPTZFragment.this.device != null) {
                    if (!ViseBluetooth.getInstance().isConnected()) {
                        ViseLog.m1466e("直接连接");
                        if (FVPTZFragment.this.progressDialog != null) {
                            FVPTZFragment.this.progressDialog.dismiss();
                        }
                        FVPTZFragment.this.progressDialog.show();
                        FVPTZFragment.this.progressDialog.setMessage(FVPTZFragment.this.getString(C0853R.string.label_connect_device_ing));
                        ViseBluetooth.getInstance().connect(FVPTZFragment.this.device, false);
                    } else if (FVPTZFragment.this.device.isConnected()) {
                        ViseLog.m1466e("相同设备,进入主控");
                        FVPTZFragment.this.enterMainActivity();
                    } else {
                        ViseLog.m1466e("不同设备,断开先前设备");
                        if (ViseBluetooth.getInstance().isConnected()) {
                            BleByteUtil.controlDefaultCamSwitch((byte) 87, 1);
                        }
                        ViseBluetooth.getInstance().disconnect();
                        BlePtzParasConstant.restoreFactorySettings();
                        FVPTZFragment.this.mHandler.sendEmptyMessageDelayed(3, 1000);
                    }
                }
            }
        });
        if (Build.VERSION.SDK_INT >= 21) {
            this.periodLScanCallback = new PeriodLScanCallback() {
                public void scanTimeout() {
                    ViseLog.m1466e("FVPTZFragment scan over");
                    ViseLog.m1466e("FVPTZFragment autoConnect: " + FVPTZFragment.this.autoConnectTimeout);
                    ViseLog.m1466e("FVPTZFragment PeriodLScanCallback SCAN_BLE_DEVICE_TIMEOUT dismiss");
                    FVPTZFragment.this.mHandler.sendEmptyMessage(-1);
                }

                public void onDeviceFound(BluetoothLeDevice bluetoothLeDevice) {
                    if (FVPTZFragment.this.bluetoothLeDeviceStore != null && bluetoothLeDevice != null && bluetoothLeDevice.getName() != null && bluetoothLeDevice.getName().contains("VILTA") && !FVPTZFragment.this.bluetoothLeDeviceStore.getDeviceList().contains(bluetoothLeDevice)) {
                        FVPTZFragment.this.bluetoothLeDeviceStore.addDevice(bluetoothLeDevice);
                        List unused = FVPTZFragment.this.bluetoothLeDeviceList = FVPTZFragment.this.bluetoothLeDeviceStore.getDeviceList();
                        FVPTZFragment.this.mHandler.sendEmptyMessageDelayed(11, 100);
                    }
                }
            };
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCome(Event event) {
        if (event != null) {
            receiveEvent(event);
        }
    }

    /* access modifiers changed from: protected */
    public void receiveEvent(Event event) {
        BluetoothLeDevice connDevice;
        switch (event.getCode()) {
            case 17:
                ViseLog.m1466e("FVPTZFragmentConnect Success!");
                this.isConnected = true;
                ViseBluetooth.getInstance().setBluetoothGatt((BluetoothGatt) event.getData());
                SPUtils.put(getActivity(), SharePrefConstant.LAST_PTZ_DEVICE_TYPE, this.currentPTZType);
                SPUtils.put(getActivity(), SharePrefConstant.CURRENT_PTZ_MAC, this.device.getAddress());
                Toast.makeText(getActivity(), getResources().getString(C0853R.string.lable_bluetooth_is_connected), 0).show();
                this.device.setConnected(true);
                if (this.adapter != null) {
                    this.adapter.setConnDevice(this.device);
                }
                BleDeviceBean curDevice = new BleDeviceBean();
                curDevice.setPtzType(this.currentPTZType);
                curDevice.setBleName(this.device.getName());
                BlueToothHistoryUtil.saveConnectedDevice(getActivity(), curDevice, this.currentPTZType);
                getGattSerAndChaAndNotify(ViseBluetooth.getInstance().getSupportedGattServices());
                return;
            case 18:
                this.isConnected = false;
                ViseLog.m1466e("FVPTZFragmentACTION_GATT_CONNECTED_TIMEOUT dismiss");
                this.progressDialog.dismiss();
                if (this.autoConnectTimeout.booleanValue()) {
                    showBleList();
                }
                Toast.makeText(getActivity(), getResources().getString(C0853R.string.lable_bluetooth_connected_timeout), 0).show();
                return;
            case 34:
                ViseLog.m1466e("FVPTZFragment Disconnect!");
                if (this.isAutoConnect) {
                    this.ptzMenuSwitcher.hideAutoConnectHint();
                }
                this.isConnected = false;
                if (!((Boolean) SPUtils.get(getActivity(), SharePrefConstant.IS_SWITCH_PTZ_TYPE, false)).booleanValue()) {
                    Toast.makeText(getActivity(), getResources().getString(C0853R.string.lable_bluetooth_is_disconnected), 0).show();
                }
                SPUtils.put(getActivity(), SharePrefConstant.IS_SWITCH_PTZ_TYPE, false);
                if (!(this.adapter == null || (connDevice = this.adapter.getConnDevice()) == null)) {
                    connDevice.setConnected(false);
                }
                if (this.progressDialog != null) {
                    this.progressDialog.dismiss();
                }
                this.ptzMenuSwitcher.hideAutoConnectHint();
                if (this.adapter != null) {
                    this.adapter.notifyDataSetChanged();
                    return;
                }
                return;
            case 68:
                ViseLog.m1466e("write data descriptor");
                if (this.mWriteCharacteristic != null) {
                    this.mHandler.sendEmptyMessageDelayed(1, 500);
                    return;
                }
                return;
            case 119:
                byte[] value = (byte[]) event.getData();
                if ((value[0] & 255) == 163 && (value[1] & 255) == 26) {
                    ViseLog.m1466e("ptzFragment data :byte[]:" + Arrays.toString(value) + " ,hex: " + HexUtil.encodeHexStr(value));
                    BleNotifyDataUtil.getInstance().init(getActivity());
                    byte ptzGetParametersNotifyData = BleNotifyDataUtil.getInstance().setPtzGetParametersNotifyData(value);
                    this.mImuFirmwareVersion = BlePtzParasConstant.GET_IMU_FIRMWARE_APP_VERSION_STRING;
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void getGattSerAndChaAndNotify(List<BluetoothGattService> gattServices) {
        ViseLog.m1466e("gattServices == null :" + (gattServices == null));
        if (gattServices != null) {
            for (BluetoothGattService gattService : gattServices) {
                if (gattService.getUuid().equals(UUID.fromString(BleConstant.SERVICE_UUID_CONFIG))) {
                    Log.e(TAG, "获取到服务uuid " + gattService.getUuid());
                    for (BluetoothGattCharacteristic characteristic : gattService.getCharacteristics()) {
                        int charaProp = characteristic.getProperties();
                        if ((charaProp & 8) > 0) {
                            Log.e(TAG, "获取到写入uuid " + characteristic.getUuid());
                            this.mWriteCharacteristic = characteristic;
                            BleByteUtil.setWriteCharacteristic(characteristic);
                            ViseBluetooth.getInstance().setWriteCharacteristic(characteristic);
                        }
                        if ((charaProp & 16) > 0) {
                            Log.e(TAG, "获取到读取uuid " + characteristic.getUuid());
                            Log.e(TAG, "notify=== " + ViseBluetooth.getInstance().enableCharacteristicNotification(characteristic, false));
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void startScan(int timeout) {
        ViseLog.m1466e("SERVICE_UUID_CONFIG: " + BleConstant.SERVICE_UUID_CONFIG);
        if (this.bluetoothLeDeviceStore != null) {
            this.bluetoothLeDeviceStore.clear();
        }
        if (!(this.adapter == null || this.bluetoothLeDeviceList == null)) {
            this.bluetoothLeDeviceList.clear();
            this.adapter.setDeviceList(this.bluetoothLeDeviceList);
            this.adapter.notifyDataSetChanged();
        }
        if (ViseBluetooth.getInstance().getScanState() == StateScan.SCAN_PROCESS) {
            stopScan();
        }
        if (Build.VERSION.SDK_INT >= 21) {
            List<ScanFilter> scanFileterList = ViseBluetooth.getInstance().getScanFileterList();
            ScanSettings scanSettings = ViseBluetooth.getInstance().buildScanSettings();
            if (scanFileterList == null || scanSettings == null) {
                throw new IllegalArgumentException("this serviceuuid is Null!");
            }
            ViseBluetooth.getInstance().setScanTimeout(timeout).startScan(scanFileterList, scanSettings, this.periodLScanCallback);
            return;
        }
        ViseBluetooth.getInstance().setScanTimeout(timeout).startLeScan(new UUID[]{UUID.fromString(BleConstant.SERVICE_UUIDS[CameraUtils.getCurrentPageIndex()])}, this.periodScanCallback);
    }

    /* access modifiers changed from: private */
    public void stopScan() {
        if (Build.VERSION.SDK_INT >= 21) {
            ViseBluetooth.getInstance().stopScan(this.periodLScanCallback);
        } else {
            ViseBluetooth.getInstance().stopScan(this.periodScanCallback);
        }
    }

    /* access modifiers changed from: private */
    public void enterMainActivity() {
        ViseLog.m1466e("enterMainActivity");
        Intent intent = new Intent(getActivity(), FVMainActivity.class);
        intent.addFlags(131072);
        startActivityForResult(intent, 0);
    }

    public void onResume() {
        super.onResume();
        ViseLog.m1466e("FVHomeActivity ptzfragment resume");
        EventBusUtil.register(this);
    }

    public void onHiddenChanged(boolean hidden) {
        boolean z;
        boolean z2 = true;
        super.onHiddenChanged(hidden);
        if (hidden) {
            ViseLog.m1466e("PTZFragment hidden true");
            return;
        }
        ViseLog.m1466e("PTZFragment hidden false");
        String lastDeviceType = (String) SPUtils.get(getActivity(), SharePrefConstant.LAST_PTZ_DEVICE_TYPE, "");
        if (this.currentPTZType != null && !this.currentPTZType.equals(lastDeviceType)) {
            ViseLog.m1466e("!currentPTZType.equals(lastDeviceType) ");
            ViseLog.m1466e("disconnect");
            ViseBluetooth.getInstance().disconnect();
        }
        String displayName = BleConstant.FM_200_DISPLAY_NAME;
        if (CameraUtils.getCurrentPageIndex() == 0) {
            this.img_ptz_scan.setImageResource(C0853R.mipmap.banner_m2);
            displayName = BleConstant.FM_200_DISPLAY_NAME;
        } else if (CameraUtils.getCurrentPageIndex() == 1) {
            this.img_ptz_scan.setImageResource(C0853R.mipmap.banner_s2);
            displayName = BleConstant.FM_300_DISPLAY_NAME;
        } else if (CameraUtils.getCurrentPageIndex() == 2) {
            this.img_ptz_scan.setImageResource(C0853R.mipmap.banner_x2);
            displayName = BleConstant.FM_210_DISPLAY_NAME;
        }
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "britannic_bold.ttf");
        this.tv_ptz_type_name.setTextSize((float) Util.px2sp(38.0f, 1.6f));
        this.tv_ptz_type_name.setTypeface(tf);
        this.tv_ptz_type_name.setText(displayName);
        if (this.adapter != null && this.bluetoothLeDeviceList != null) {
            StringBuilder append = new StringBuilder().append("adapter != null :");
            if (this.adapter != null) {
                z = true;
            } else {
                z = false;
            }
            ViseLog.m1466e(append.append(z).toString());
            StringBuilder append2 = new StringBuilder().append("bluetoothLeDeviceList != null :");
            if (this.bluetoothLeDeviceList == null) {
                z2 = false;
            }
            ViseLog.m1466e(append2.append(z2).toString());
            this.adapter.notifyDataSetChanged();
            startScan(15000);
        }
    }

    public void onPause() {
        super.onPause();
        ViseLog.m1466e("ptzfragment pause");
        stopScan();
        if (this.iv_refresh_ble != null) {
            this.iv_refresh_ble.clearAnimation();
        }
        EventBusUtil.unregister(this);
    }

    public void onStop() {
        super.onStop();
        ViseLog.m1466e("ptzfragment stop");
    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ptzfragment destory");
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages((Object) null);
        }
        ButterKnife.unbind(this);
    }

    public void connectDevice() {
        this.isAutoConnect = true;
        Context context = getActivity();
        this.iv_refresh_ble.startAnimation(this.operatingAnim);
        startScan(5000);
        BluetoothDevice connDevice = getConnectedDevice();
        if (connDevice != null) {
            this.ptzMenuSwitcher.showAutoConnectHint(connDevice.getName());
            if (ViseBluetooth.getInstance().getScanState().equals(StateScan.SCAN_PROCESS)) {
                stopScan();
            }
            this.isAutoConnect = false;
            ViseBluetooth.getInstance().connect(connDevice, false);
            this.device = new BluetoothLeDevice(connDevice, -90, new byte[2], System.currentTimeMillis());
        } else if (!BlueToothHistoryUtil.isEmpty(context, this.currentPTZType)) {
            ViseLog.m1466e("!BlueToothHistoryUtil.isEmpty(context,currentPTZType)");
            checkAutoConnect();
        } else {
            this.isAutoConnect = false;
            this.ptzMenuSwitcher.showPtzList();
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0853R.C0855id.tv_back:
                stopScan();
                this.ptzMenuSwitcher.showPtzSelect();
                return;
            case C0853R.C0855id.tv_enter_camera:
                enterMainActivity();
                stopScan();
                return;
            case C0853R.C0855id.iv_refresh_ble:
                stopScan();
                this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                        FVPTZFragment.this.startScan(15000);
                        FVPTZFragment.this.iv_refresh_ble.startAnimation(FVPTZFragment.this.operatingAnim);
                    }
                }, 1000);
                return;
            case C0853R.C0855id.img_right:
                startActivity(new Intent(getActivity(), FVHelpActivity.class));
                return;
            default:
                return;
        }
    }

    private BluetoothDevice getConnectedDevice() {
        BluetoothAdapter adapter2 = BluetoothAdapter.getDefaultAdapter();
        try {
            Method method = BluetoothAdapter.class.getDeclaredMethod("getConnectionState", (Class[]) null);
            method.setAccessible(true);
            if (((Integer) method.invoke(adapter2, (Object[]) null)).intValue() == 2) {
                for (BluetoothDevice device2 : adapter2.getBondedDevices()) {
                    Method isConnectedMethod = BluetoothDevice.class.getDeclaredMethod("isConnected", (Class[]) null);
                    isConnectedMethod.setAccessible(true);
                    if (((Boolean) isConnectedMethod.invoke(device2, (Object[]) null)).booleanValue() && device2.getName().contains("VILTA")) {
                        String ptzType = device2.getName().substring(6, 7);
                        if (ptzType.equals("S") && CameraUtils.getCurrentPageIndex() == 1) {
                            return device2;
                        }
                        if (ptzType.equals("X") && CameraUtils.getCurrentPageIndex() == 2) {
                            return device2;
                        }
                        if (ptzType.equals("M")) {
                            if (device2.getName().substring(6, 11).equals(BleConstant.FM_210_INSTRUCTIONS_NAME)) {
                                return device2;
                            }
                        } else if (CameraUtils.getCurrentPageIndex() == 0) {
                            return device2;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /* access modifiers changed from: private */
    public void checkAutoConnect() {
        String lastPTZDeviceType = (String) SPUtils.get(getActivity(), SharePrefConstant.LAST_PTZ_DEVICE_TYPE, "");
        ViseLog.m1466e("FVPTZFragmentlastPTZDeviceType: " + lastPTZDeviceType);
        ViseLog.m1466e("FVPTZFragmentcurrentPTZTypeName: " + this.currentPTZType);
        ViseLog.m1466e("FVPTZFragmentlastPTZDeviceType.equals(currentPTZTypeName): " + lastPTZDeviceType.equals(this.currentPTZType));
        Log.i("KBein", "FVPTZFragment.checkAutoConnect():--lastPTZDeviceType--" + lastPTZDeviceType);
        Log.i("KBein", "FVPTZFragment.checkAutoConnect():----currentPTZTypeName: " + this.currentPTZType);
        Log.i("KBein", "FVPTZFragment.checkAutoConnect():----lastPTZDeviceType.equals(currentPTZTypeName): " + lastPTZDeviceType.equals(this.currentPTZType));
        if (!ViseBluetooth.getInstance().isConnected() || !lastPTZDeviceType.equals(this.currentPTZType)) {
            List<BluetoothLeDevice> devices = this.bluetoothLeDeviceStore.getDeviceList();
            int deviceOnline = BlueToothHistoryUtil.findLastDeviceByTypeName(getActivity(), devices, this.currentPTZType);
            ViseLog.m1466e("FVPTZFragmentdeviceOnline: " + deviceOnline);
            if (deviceOnline != -1) {
                ViseLog.m1466e("toConnectLastDevice");
                this.device = devices.get(deviceOnline);
                ViseLog.m1466e("deviceName: " + this.device.getName());
                this.ptzMenuSwitcher.showAutoConnectHint(this.device.getName());
                if (ViseBluetooth.getInstance().getScanState().equals(StateScan.SCAN_PROCESS)) {
                    stopScan();
                }
                this.isAutoConnect = false;
                ViseBluetooth.getInstance().connect(this.device, false);
                return;
            }
            return;
        }
        ViseLog.m1466e("ViseBluetooth.getInstance().isConnected()&&lastPTZDeviceType.equals(currentPTZType)");
        this.isAutoConnect = false;
        this.ptzMenuSwitcher.hideAutoConnectHint();
        enterMainActivity();
    }

    private void startPropertyAnim(RelativeLayout linearLayout) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(linearLayout, "alpha", new float[]{1.0f, 1.0f});
        anim.setDuration(200);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.d("zhangphil", ((Float) animation.getAnimatedValue()).floatValue() + "");
            }
        });
        anim.start();
    }

    private void showBleList() {
        startPropertyAnim(this.rl_content_scaning);
        this.rl_content_scaning.setVisibility(0);
        this.iv_refresh_ble.startAnimation(this.operatingAnim);
        this.autoConnectTimeout = false;
        startScan(15000);
    }
}
