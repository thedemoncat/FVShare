package com.freevisiontech.fvmobile.widget;

import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.ViseBluetooth;
import com.freevisiontech.fvmobile.adapter.DeviceAdapter;
import com.freevisiontech.fvmobile.callback.scan.PeriodLScanCallback;
import com.freevisiontech.fvmobile.callback.scan.PeriodScanCallback;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.freevisiontech.fvmobile.model.BluetoothLeDevice;
import com.freevisiontech.fvmobile.model.BluetoothLeDeviceStore;
import com.freevisiontech.fvmobile.utility.Util;
import com.freevisiontech.fvmobile.utils.BleByteUtil;
import com.freevisiontech.fvmobile.utils.BlePtzParasConstant;
import com.freevisiontech.fvmobile.utils.Event;
import com.freevisiontech.fvmobile.utils.EventBusUtil;
import com.freevisiontech.fvmobile.utils.HexUtil;
import com.freevisiontech.utils.ScreenOrientationUtil;
import com.vise.log.ViseLog;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FVMainForSearchBluetooth {
    private static final int CONNECTED_AND_NOTIFY_OTHERS = 1003;
    private static final int DISCONECT_AND_RECONNECT = 1002;
    private static final int GET_PTZ_VERSION_FOR_MAIN = 1001;
    public static final String NOTIFY_UUID_CONFIG = "18a50003-11a1-49fc-b05b-be7ede642dc7";
    public static final String WRITE_UUID_CONFIG = "18a50002-11a1-49fc-b05b-be7ede642dc7";
    private final int CAMERA_POPUP_HUN_EIGHTY = 12;
    private final int CAMERA_POPUP_NINTY = 11;
    private final int CAMERA_POPUP_TWO_SERTY = 13;
    private final int CAMERA_POPUP_ZARO = 10;
    /* access modifiers changed from: private */
    public DeviceAdapter adapter;
    /* access modifiers changed from: private */
    public volatile List<BluetoothLeDevice> bluetoothLeDeviceList = new ArrayList();
    /* access modifiers changed from: private */
    public BluetoothLeDeviceStore bluetoothLeDeviceStore;
    private OrientationBroadPopup broad;
    /* access modifiers changed from: private */
    public int connectType = 1;
    private Context context;
    /* access modifiers changed from: private */
    public BluetoothLeDevice device;
    /* access modifiers changed from: private */
    public ListView deviceLv;
    /* access modifiers changed from: private */
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1002:
                    ViseLog.m1466e("主控切换重连");
                    ViseBluetooth.getInstance().connect(FVMainForSearchBluetooth.this.device, false);
                    return;
                case 1003:
                    if (FVMainForSearchBluetooth.this.isConnected) {
                        EventBusUtil.sendEvent(new Event(120));
                        FVMainForSearchBluetooth.this.pop.dismiss();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public boolean isConnected = false;
    /* access modifiers changed from: private */
    public ImageView iv_search_device;
    private LinearLayout layout_camera_shortcut_pop_horizontal_bottom;
    private LinearLayout layout_camera_shortcut_pop_int_linear;
    private LinearLayout layout_camera_shortcut_pop_out_linear;
    private LinearLayout layout_camera_shortcut_pop_vertical_bottom_top;
    /* access modifiers changed from: private */
    public Activity mActivity;
    private BluetoothGattCharacteristic mWriteCharacteristic;
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    Log.e("------------", "--------收到消息 0-----");
                    FVMainForSearchBluetooth.this.setHorUiZero();
                    return;
                case 11:
                    Log.e("------------", "--------收到消息  90-------------");
                    FVMainForSearchBluetooth.this.setHorUiNinety();
                    return;
                case 12:
                    Log.e("------------", "--------收到消息  180-------------");
                    FVMainForSearchBluetooth.this.setHorUiZero180();
                    return;
                case 13:
                    Log.e("------------", "--------收到消息  270-------------");
                    FVMainForSearchBluetooth.this.setHorUiNinety270();
                    return;
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public Animation operatingAnim;
    private View parentView;
    private PeriodLScanCallback periodLScanCallback;
    private PeriodScanCallback periodScanCallback = new PeriodScanCallback() {
        public void scanTimeout() {
            ViseLog.m1466e("scan over");
            FVMainForSearchBluetooth.this.iv_search_device.clearAnimation();
            if (FVMainForSearchBluetooth.this.bluetoothLeDeviceList.size() <= 0) {
                FVMainForSearchBluetooth.this.deviceLv.setVisibility(8);
                FVMainForSearchBluetooth.this.tv_no_device.setVisibility(0);
            }
        }

        public void onDeviceFound(BluetoothLeDevice bluetoothLeDevice) {
            if (!(FVMainForSearchBluetooth.this.bluetoothLeDeviceStore == null || bluetoothLeDevice == null || bluetoothLeDevice.getName() == null || !bluetoothLeDevice.getName().contains("VILTA"))) {
                FVMainForSearchBluetooth.this.bluetoothLeDeviceStore.addDevice(bluetoothLeDevice);
                List unused = FVMainForSearchBluetooth.this.bluetoothLeDeviceList = FVMainForSearchBluetooth.this.bluetoothLeDeviceStore.getDeviceList();
            }
            FVMainForSearchBluetooth.this.mActivity.runOnUiThread(new Runnable() {
                public void run() {
                    FVMainForSearchBluetooth.this.adapter.setDeviceList(FVMainForSearchBluetooth.this.bluetoothLeDeviceList);
                }
            });
        }
    };
    /* access modifiers changed from: private */
    public PopupWindow pop;
    /* access modifiers changed from: private */
    public TextView tv_no_device;
    private View view;

    public void init(Activity activity, Context context2, int type) {
        this.mActivity = activity;
        this.context = context2;
        this.connectType = type;
        this.view = LayoutInflater.from(context2).inflate(C0853R.layout.dialog_search_bluetooth_new, (ViewGroup) null);
        initPop(this.view);
        EventBusUtil.register(this);
        startScanDelay();
        this.layout_camera_shortcut_pop_out_linear = (LinearLayout) this.view.findViewById(C0853R.C0855id.layout_camera_shortcut_pop_out_linear);
        this.layout_camera_shortcut_pop_int_linear = (LinearLayout) this.view.findViewById(C0853R.C0855id.layout_camera_shortcut_pop_int_linear);
        this.layout_camera_shortcut_pop_horizontal_bottom = (LinearLayout) this.view.findViewById(C0853R.C0855id.layout_camera_shortcut_pop_vertical_bottom);
        this.layout_camera_shortcut_pop_vertical_bottom_top = (LinearLayout) this.view.findViewById(C0853R.C0855id.layout_camera_shortcut_pop_vertical_bottom_top);
        this.broad = new OrientationBroadPopup();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ScreenOrientationUtil.BC_OrientationChanged);
        context2.registerReceiver(this.broad, filter);
        int orientation = ScreenOrientationUtil.getInstance().getOrientation();
        if (orientation != -1) {
            if (orientation == 0) {
                sendToHandler(10);
            } else if (orientation == 90) {
                sendToHandler(11);
            } else if (orientation == 180) {
                sendToHandler(12);
            } else if (orientation == 270) {
                sendToHandler(13);
            }
        }
        this.layout_camera_shortcut_pop_horizontal_bottom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (FVMainForSearchBluetooth.this.pop != null) {
                    FVMainForSearchBluetooth.this.pop.dismiss();
                }
            }
        });
        this.layout_camera_shortcut_pop_vertical_bottom_top.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (FVMainForSearchBluetooth.this.pop != null) {
                    FVMainForSearchBluetooth.this.pop.dismiss();
                }
            }
        });
    }

    private class OrientationBroadPopup extends BroadcastReceiver {
        private Message message;

        private OrientationBroadPopup() {
        }

        public void onReceive(Context context, Intent intent) {
            int orientation = intent.getIntExtra(ScreenOrientationUtil.BC_OrientationChangedKey, -1);
            Log.e("-------------", "----------888-- orientation --" + orientation);
            if (orientation == -1) {
                return;
            }
            if (orientation == 0) {
                FVMainForSearchBluetooth.this.sendToHandler(10);
            } else if (orientation == 90) {
                FVMainForSearchBluetooth.this.sendToHandler(11);
            } else if (orientation == 180) {
                FVMainForSearchBluetooth.this.sendToHandler(12);
            } else if (orientation == 270) {
                FVMainForSearchBluetooth.this.sendToHandler(13);
            }
        }
    }

    public void sendToHandler(int what) {
        Message me = new Message();
        me.what = what;
        this.myHandler.sendMessage(me);
    }

    /* access modifiers changed from: private */
    public void setHorUiZero180() {
        this.layout_camera_shortcut_pop_horizontal_bottom.setVisibility(0);
        this.layout_camera_shortcut_pop_vertical_bottom_top.setVisibility(8);
        this.layout_camera_shortcut_pop_out_linear.setOrientation(1);
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_int_linear.getLayoutParams();
        linearParams.height = Util.dip2px(this.context, 270.0f);
        linearParams.width = Util.dip2px(this.context, 330.0f);
        this.layout_camera_shortcut_pop_int_linear.setLayoutParams(linearParams);
        LinearLayout.LayoutParams linearParams2 = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_horizontal_bottom.getLayoutParams();
        linearParams2.height = Util.dip2px(this.context, 60.0f);
        linearParams2.width = Util.dip2px(this.context, 330.0f);
        this.layout_camera_shortcut_pop_horizontal_bottom.setLayoutParams(linearParams2);
        this.layout_camera_shortcut_pop_out_linear.setRotation(180.0f);
    }

    /* access modifiers changed from: private */
    public void setHorUiZero() {
        this.layout_camera_shortcut_pop_horizontal_bottom.setVisibility(0);
        this.layout_camera_shortcut_pop_vertical_bottom_top.setVisibility(8);
        this.layout_camera_shortcut_pop_out_linear.setOrientation(1);
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_int_linear.getLayoutParams();
        linearParams.height = Util.dip2px(this.context, 270.0f);
        linearParams.width = Util.dip2px(this.context, 330.0f);
        this.layout_camera_shortcut_pop_int_linear.setLayoutParams(linearParams);
        LinearLayout.LayoutParams linearParams2 = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_horizontal_bottom.getLayoutParams();
        linearParams2.height = Util.dip2px(this.context, 60.0f);
        linearParams2.width = Util.dip2px(this.context, 330.0f);
        this.layout_camera_shortcut_pop_horizontal_bottom.setLayoutParams(linearParams2);
        this.layout_camera_shortcut_pop_out_linear.setRotation(0.0f);
    }

    /* access modifiers changed from: private */
    public void setHorUiNinety() {
        this.layout_camera_shortcut_pop_horizontal_bottom.setVisibility(0);
        this.layout_camera_shortcut_pop_vertical_bottom_top.setVisibility(8);
        this.layout_camera_shortcut_pop_out_linear.setOrientation(1);
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_int_linear.getLayoutParams();
        linearParams.height = Util.dip2px(this.context, 270.0f);
        linearParams.width = Util.dip2px(this.context, 330.0f);
        this.layout_camera_shortcut_pop_int_linear.setLayoutParams(linearParams);
        LinearLayout.LayoutParams linearParams2 = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_horizontal_bottom.getLayoutParams();
        linearParams2.height = Util.dip2px(this.context, 60.0f);
        linearParams2.width = Util.dip2px(this.context, 330.0f);
        this.layout_camera_shortcut_pop_horizontal_bottom.setLayoutParams(linearParams2);
        this.layout_camera_shortcut_pop_out_linear.setRotation(-90.0f);
    }

    /* access modifiers changed from: private */
    public void setHorUiNinety270() {
        this.layout_camera_shortcut_pop_horizontal_bottom.setVisibility(0);
        this.layout_camera_shortcut_pop_vertical_bottom_top.setVisibility(8);
        this.layout_camera_shortcut_pop_out_linear.setOrientation(1);
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_int_linear.getLayoutParams();
        linearParams.height = Util.dip2px(this.context, 270.0f);
        linearParams.width = Util.dip2px(this.context, 330.0f);
        this.layout_camera_shortcut_pop_int_linear.setLayoutParams(linearParams);
        LinearLayout.LayoutParams linearParams2 = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_horizontal_bottom.getLayoutParams();
        linearParams2.height = Util.dip2px(this.context, 60.0f);
        linearParams2.width = Util.dip2px(this.context, 330.0f);
        this.layout_camera_shortcut_pop_horizontal_bottom.setLayoutParams(linearParams2);
        this.layout_camera_shortcut_pop_out_linear.setRotation(90.0f);
    }

    public View getView() {
        return this.view;
    }

    public void setPop(PopupWindow pop2, final FVMainForSearchBluetooth fvMainForSearchBluetooth) {
        this.pop = pop2;
        if (pop2 != null) {
            pop2.setOnDismissListener(new PopupWindow.OnDismissListener() {
                public void onDismiss() {
                    EventBus.getDefault().unregister(fvMainForSearchBluetooth);
                }
            });
        }
    }

    public void unRegisterEvent() {
        EventBusUtil.unregister(this);
        if (this.broad != null) {
            this.context.unregisterReceiver(this.broad);
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
        switch (event.getCode()) {
            case 17:
                ViseLog.m1466e("main search connect");
                this.isConnected = true;
                BluetoothGatt bluetoothGatt = (BluetoothGatt) event.getData();
                ViseBluetooth.getInstance().setBluetoothGatt(bluetoothGatt);
                Toast.makeText(this.context, this.context.getResources().getString(C0853R.string.lable_bluetooth_is_connected), 0).show();
                getGattSerAndChaAndNotify(bluetoothGatt.getServices());
                return;
            case 18:
                this.isConnected = false;
                Toast.makeText(this.context, this.context.getResources().getString(C0853R.string.lable_bluetooth_connected_timeout), 0).show();
                return;
            case 34:
                ViseLog.m1466e("main search disconnect");
                this.isConnected = false;
                ViseBluetooth.getInstance().close();
                return;
            case 68:
                ViseLog.m1466e("pop write data descriptor");
                if (this.mWriteCharacteristic != null) {
                    this.handler.sendEmptyMessageDelayed(1003, 500);
                    return;
                }
                return;
            case 119:
                byte[] value = (byte[]) event.getData();
                ViseLog.m1466e("main search data :byte[]:" + Arrays.toString(value) + " ,hex: " + HexUtil.encodeHexStr(value));
                return;
            default:
                return;
        }
    }

    private void initPop(View view2) {
        ImageView iv_close = (ImageView) view2.findViewById(C0853R.C0855id.iv_close);
        this.iv_search_device = (ImageView) view2.findViewById(C0853R.C0855id.iv_search_device);
        this.deviceLv = (ListView) view2.findViewById(C0853R.C0855id.deviceLv);
        this.tv_no_device = (TextView) view2.findViewById(C0853R.C0855id.tv_no_device);
        this.operatingAnim = AnimationUtils.loadAnimation(this.context, C0853R.anim.rotate);
        this.operatingAnim.setInterpolator(new LinearInterpolator());
        this.bluetoothLeDeviceStore = new BluetoothLeDeviceStore();
        this.adapter = new DeviceAdapter(this.context);
        this.deviceLv.setAdapter(this.adapter);
        this.deviceLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                FVMainForSearchBluetooth.this.stopScan();
                FVMainForSearchBluetooth.this.iv_search_device.clearAnimation();
                BluetoothLeDevice unused = FVMainForSearchBluetooth.this.device = (BluetoothLeDevice) FVMainForSearchBluetooth.this.adapter.getItem(position);
                if (FVMainForSearchBluetooth.this.device != null) {
                    ViseLog.m1466e("connectType" + FVMainForSearchBluetooth.this.connectType);
                    ViseBluetooth.getInstance().disconnect();
                    BlePtzParasConstant.restoreFactorySettings();
                    FVMainForSearchBluetooth.this.handler.sendEmptyMessageDelayed(1002, 1000);
                }
            }
        });
        if (Build.VERSION.SDK_INT >= 21) {
            this.periodLScanCallback = new PeriodLScanCallback() {
                public void scanTimeout() {
                    ViseLog.m1466e("scan over");
                    if (FVMainForSearchBluetooth.this.iv_search_device != null) {
                        FVMainForSearchBluetooth.this.iv_search_device.clearAnimation();
                    }
                    if (FVMainForSearchBluetooth.this.bluetoothLeDeviceList != null && FVMainForSearchBluetooth.this.bluetoothLeDeviceList.size() <= 0) {
                        FVMainForSearchBluetooth.this.deviceLv.setVisibility(8);
                        FVMainForSearchBluetooth.this.tv_no_device.setVisibility(0);
                    }
                }

                public void onDeviceFound(BluetoothLeDevice bluetoothLeDevice) {
                    ViseLog.m1466e("FVMainForSearchBluetooth bluetoothLeDevice: " + bluetoothLeDevice.getName());
                    if (!(FVMainForSearchBluetooth.this.bluetoothLeDeviceStore == null || bluetoothLeDevice == null || bluetoothLeDevice.getName() == null || !bluetoothLeDevice.getName().contains("VILTA"))) {
                        FVMainForSearchBluetooth.this.bluetoothLeDeviceStore.addDevice(bluetoothLeDevice);
                        List unused = FVMainForSearchBluetooth.this.bluetoothLeDeviceList = FVMainForSearchBluetooth.this.bluetoothLeDeviceStore.getDeviceList();
                    }
                    FVMainForSearchBluetooth.this.mActivity.runOnUiThread(new Runnable() {
                        public void run() {
                            FVMainForSearchBluetooth.this.adapter.setDeviceList(FVMainForSearchBluetooth.this.bluetoothLeDeviceList);
                            FVMainForSearchBluetooth.this.adapter.notifyDataSetChanged();
                        }
                    });
                }
            };
        }
        iv_close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FVMainForSearchBluetooth.this.pop.dismiss();
            }
        });
        this.iv_search_device.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FVMainForSearchBluetooth.this.startScan();
                FVMainForSearchBluetooth.this.iv_search_device.startAnimation(FVMainForSearchBluetooth.this.operatingAnim);
            }
        });
    }

    private void startScanDelay() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                FVMainForSearchBluetooth.this.startScan();
                FVMainForSearchBluetooth.this.iv_search_device.startAnimation(FVMainForSearchBluetooth.this.operatingAnim);
            }
        }, 300);
    }

    /* access modifiers changed from: private */
    public void startScan() {
        if (this.bluetoothLeDeviceStore != null) {
            this.bluetoothLeDeviceStore.clear();
        }
        if (!(this.adapter == null || this.bluetoothLeDeviceList == null)) {
            this.bluetoothLeDeviceList.clear();
            this.adapter.setDeviceList(this.bluetoothLeDeviceList);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            ViseBluetooth.getInstance().setScanTimeout(15000).startScan(this.periodLScanCallback);
        } else {
            ViseBluetooth.getInstance().setScanTimeout(15000).startScan(this.periodScanCallback);
        }
    }

    public void stopScan() {
        if (Build.VERSION.SDK_INT >= 21) {
            ViseBluetooth.getInstance().stopScan(this.periodLScanCallback);
        } else {
            ViseBluetooth.getInstance().stopScan(this.periodScanCallback);
        }
    }

    private void getGattSerAndChaAndNotify(List<BluetoothGattService> gattServices) {
        if (gattServices != null) {
            for (BluetoothGattService gattService : gattServices) {
                if (gattService.getUuid().equals(UUID.fromString(BleConstant.SERVICE_UUID_CONFIG))) {
                    ViseLog.m1466e("获取到服务uuid===" + gattService.getUuid());
                    BluetoothGattCharacteristic writeCharacteristic = gattService.getCharacteristic(UUID.fromString(WRITE_UUID_CONFIG));
                    if (writeCharacteristic != null) {
                        ViseLog.m1466e("获取到写入uuid===" + writeCharacteristic.getUuid());
                        this.mWriteCharacteristic = writeCharacteristic;
                        BleByteUtil.setWriteCharacteristic(writeCharacteristic);
                        ViseBluetooth.getInstance().setWriteCharacteristic(writeCharacteristic);
                    }
                    BluetoothGattCharacteristic notifyCharacteristic = gattService.getCharacteristic(UUID.fromString(NOTIFY_UUID_CONFIG));
                    if (notifyCharacteristic != null) {
                        ViseLog.m1466e("获取到读取uuid=== " + notifyCharacteristic.getUuid());
                        ViseLog.m1466e("notification===" + ViseBluetooth.getInstance().enableCharacteristicNotification(notifyCharacteristic, false));
                    }
                }
            }
        }
    }
}
