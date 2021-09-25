package com.freevisiontech.fvmobile;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelUuid;
import android.util.Log;
import com.freevisiontech.fvmobile.activity.FVMainActivity;
import com.freevisiontech.fvmobile.callback.IConnectCallback;
import com.freevisiontech.fvmobile.callback.data.IBleCallback;
import com.freevisiontech.fvmobile.callback.data.ICharacteristicCallback;
import com.freevisiontech.fvmobile.callback.data.IDescriptorCallback;
import com.freevisiontech.fvmobile.callback.data.IRssiCallback;
import com.freevisiontech.fvmobile.callback.scan.PeriodLScanCallback;
import com.freevisiontech.fvmobile.callback.scan.PeriodMacScanCallback;
import com.freevisiontech.fvmobile.callback.scan.PeriodNameScanCallback;
import com.freevisiontech.fvmobile.callback.scan.PeriodScanCallback;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.freevisiontech.fvmobile.common.State;
import com.freevisiontech.fvmobile.common.StateScan;
import com.freevisiontech.fvmobile.exception.GattException;
import com.freevisiontech.fvmobile.exception.InitiatedException;
import com.freevisiontech.fvmobile.exception.OtherException;
import com.freevisiontech.fvmobile.exception.TimeoutException;
import com.freevisiontech.fvmobile.model.BluetoothLeDevice;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utils.BleByteUtil;
import com.freevisiontech.fvmobile.utils.BlePtzParasConstant;
import com.freevisiontech.fvmobile.utils.Event;
import com.freevisiontech.fvmobile.utils.EventBusUtil;
import com.freevisiontech.fvmobile.utils.HexUtil;
import com.vise.log.ViseLog;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ViseBluetooth {
    private static ViseBluetooth viseBluetooth;
    public static BluetoothGattCharacteristic writeCharacteristic;
    /* access modifiers changed from: private */
    public volatile Set<IBleCallback> bleCallbacks = new LinkedHashSet();
    private volatile Set<Long> bleLongTimes = new LinkedHashSet();
    private BluetoothAdapter bluetoothAdapter;
    /* access modifiers changed from: private */
    public BluetoothDevice bluetoothDevice;
    /* access modifiers changed from: private */
    public BluetoothGatt bluetoothGatt;
    private BluetoothManager bluetoothManager;
    private BluetoothGattCharacteristic characteristic;
    private BluetoothLeDevice connDevice;
    private int connectTimeout = 15000;
    private Context context;
    private BluetoothGattCallback coreGattCallback = new BluetoothGattCallback() {
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            ViseLog.m1466e("onConnectionStateChange  status: " + status + " ,newState: " + newState + "  ,thread: " + Thread.currentThread().getId());
            if (newState == 2) {
                BlePtzParasConstant.GET_CURRENT_PTZ_UUID = BleConstant.SERVICE_UUID_CONFIG;
                gatt.discoverServices();
            } else if (newState == 0) {
                State unused = ViseBluetooth.this.state = State.DISCONNECT;
                if (ViseBluetooth.this.handler != null) {
                    ViseBluetooth.this.handler.removeMessages(6);
                }
                ViseBluetooth.this.close();
                ViseBluetooth.this.runOnMainThread(new Runnable() {
                    public void run() {
                        EventBusUtil.sendEvent(new Event(34));
                    }
                });
            } else if (newState == 1) {
                State unused2 = ViseBluetooth.this.state = State.CONNECT_PROCESS;
            }
        }

        public void onServicesDiscovered(final BluetoothGatt gatt, int status) {
            ViseLog.m1466e("onServicesDiscovered  status: " + status);
            if (ViseBluetooth.this.handler != null) {
                ViseBluetooth.this.handler.removeMessages(6);
            }
            if (status == 0) {
                BluetoothGatt unused = ViseBluetooth.this.bluetoothGatt = gatt;
                BluetoothDevice unused2 = ViseBluetooth.this.bluetoothDevice = gatt.getDevice();
                State unused3 = ViseBluetooth.this.state = State.CONNECT_SUCCESS;
                ViseBluetooth.this.refreshDeviceCache();
                ViseBluetooth.this.runOnMainThread(new Runnable() {
                    public void run() {
                        EventBusUtil.sendEvent(new Event<>(17, gatt));
                    }
                });
                return;
            }
            State unused4 = ViseBluetooth.this.state = State.CONNECT_FAILURE;
            ViseBluetooth.this.close();
            ViseBluetooth.this.runOnMainThread(new Runnable() {
                public void run() {
                    EventBusUtil.sendEvent(new Event(34));
                }
            });
        }

        public void onCharacteristicRead(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic, final int status) {
            ViseLog.m1466e("onCharacteristicRead  status: " + status + ", data:" + HexUtil.encodeHexStr(characteristic.getValue()));
            ViseLog.m1466e("on Characteristic Read  Uuid: " + characteristic.getUuid());
            if (ViseBluetooth.this.bleCallbacks != null) {
                if (ViseBluetooth.this.handler != null) {
                    ViseBluetooth.this.handler.removeMessages(3);
                }
                ViseBluetooth.this.runOnMainThread(new Runnable() {
                    public void run() {
                        for (IBleCallback bleCallback : ViseBluetooth.this.bleCallbacks) {
                            if (bleCallback instanceof ICharacteristicCallback) {
                                if (status == 0) {
                                    ((ICharacteristicCallback) bleCallback).onSuccess(characteristic);
                                } else {
                                    bleCallback.onFailure(new GattException(status));
                                }
                            }
                        }
                        ViseBluetooth.this.removeBleCallback(ViseBluetooth.this.tempBleCallback);
                    }
                });
            }
        }

        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, final int status) {
            if (!(characteristic == null || characteristic.getValue() == null)) {
                ViseLog.m1466e("onCharacteristicWrite  status: " + status + ", data:" + HexUtil.encodeHexStr(characteristic.getValue()));
            }
            if (ViseBluetooth.this.handler != null) {
                ViseBluetooth.this.handler.removeMessages(1);
            }
            ViseBluetooth.this.runOnMainThread(new Runnable() {
                public void run() {
                    if (status == 0) {
                        EventBusUtil.sendEvent(new Event(85));
                    } else {
                        EventBusUtil.sendEvent(new Event(85));
                    }
                    ViseBluetooth.this.removeBleCallback(ViseBluetooth.this.tempTimeMillis);
                }
            });
        }

        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            byte[] value = characteristic.getValue();
            if (!((value[1] & 255) == 31 || (value[1] & 255) == 14)) {
                ViseLog.m1466e("onCharacteristicChanged data : " + HexUtil.encodeHexStr(characteristic.getValue()));
            }
            if ((value[0] & 255) == 90) {
                Log.i("KBein", "ViseBluetooth.onCharacteristicChanged():--ACTION_DATA_AVAILABLE---0x5a---" + HexUtil.encodeHexStr(value));
            }
            byte[] replyAckArray = new byte[20];
            System.arraycopy(value, 0, replyAckArray, 0, value.length);
            ViseBluetooth.this.replyAck(replyAckArray);
            EventBusUtil.sendEvent(new Event<>(119, value));
        }

        public void onDescriptorRead(BluetoothGatt gatt, final BluetoothGattDescriptor descriptor, final int status) {
            ViseLog.m1466e("onDescriptorRead  status: " + status + ", data:" + HexUtil.encodeHexStr(descriptor.getValue()));
            if (ViseBluetooth.this.bleCallbacks != null) {
                if (ViseBluetooth.this.handler != null) {
                    ViseBluetooth.this.handler.removeMessages(4);
                }
                ViseBluetooth.this.runOnMainThread(new Runnable() {
                    public void run() {
                        for (IBleCallback bleCallback : ViseBluetooth.this.bleCallbacks) {
                            if (bleCallback instanceof IDescriptorCallback) {
                                if (status == 0) {
                                    ((IDescriptorCallback) bleCallback).onSuccess(descriptor);
                                } else {
                                    bleCallback.onFailure(new GattException(status));
                                }
                            }
                        }
                        ViseBluetooth.this.removeBleCallback(ViseBluetooth.this.tempBleCallback);
                    }
                });
            }
        }

        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            ViseLog.m1466e("onDescriptorWrite  status: " + status + ", data:" + HexUtil.encodeHexStr(descriptor.getValue()));
            EventBusUtil.sendEvent(new Event(68));
        }

        public void onReadRemoteRssi(BluetoothGatt gatt, final int rssi, final int status) {
            ViseLog.m1466e("onReadRemoteRssi  status: " + status + ", rssi:" + rssi);
            if (ViseBluetooth.this.bleCallbacks != null) {
                if (ViseBluetooth.this.handler != null) {
                    ViseBluetooth.this.handler.removeMessages(5);
                }
                ViseBluetooth.this.runOnMainThread(new Runnable() {
                    public void run() {
                        for (IBleCallback bleCallback : ViseBluetooth.this.bleCallbacks) {
                            if (bleCallback instanceof IRssiCallback) {
                                if (status == 0) {
                                    ((IRssiCallback) bleCallback).onSuccess(rssi);
                                } else {
                                    bleCallback.onFailure(new GattException(status));
                                }
                            }
                        }
                        ViseBluetooth.this.removeBleCallback(ViseBluetooth.this.tempBleCallback);
                    }
                });
            }
        }

        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);
        }
    };
    private BluetoothGattDescriptor descriptor;
    /* access modifiers changed from: private */
    public Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            if (msg.what == 6) {
                Long l = (Long) msg.obj;
                if (ViseBluetooth.this.state != State.CONNECT_SUCCESS) {
                    ViseBluetooth.this.close();
                    State unused = ViseBluetooth.this.state = State.CONNECT_TIMEOUT;
                    EventBusUtil.sendEvent(new Event(18));
                }
            } else if (msg.what == 1) {
                EventBusUtil.sendEvent(new Event<>(102, 97));
                ViseBluetooth.this.removeBleCallback(((Long) msg.obj).longValue());
            } else if (msg.what != 8) {
                IBleCallback bleCallback = (IBleCallback) msg.obj;
                if (bleCallback != null) {
                    bleCallback.onFailure(new TimeoutException());
                    ViseBluetooth.this.removeBleCallback(bleCallback);
                }
            } else if (ViseBluetooth.this.state != State.CONNECT_SUCCESS) {
            }
            msg.obj = null;
        }
    };
    /* access modifiers changed from: private */
    public boolean isFound = false;
    private int operateTimeout = 1000;
    private StateScan scanState = StateScan.SCAN_DISSCAN;
    private int scanTimeout = BleConstant.DEFAULT_SCAN_TIME;
    private BluetoothGattService service;
    /* access modifiers changed from: private */
    public State state = State.DISCONNECT;
    /* access modifiers changed from: private */
    public IBleCallback tempBleCallback;
    /* access modifiers changed from: private */
    public long tempTimeMillis;

    public static ViseBluetooth getInstance() {
        if (viseBluetooth == null) {
            synchronized (ViseBluetooth.class) {
                if (viseBluetooth == null) {
                    viseBluetooth = new ViseBluetooth();
                }
            }
        }
        return viseBluetooth;
    }

    public List<BluetoothGattService> getSupportedGattServices() {
        if (this.bluetoothGatt == null) {
            return null;
        }
        return this.bluetoothGatt.getServices();
    }

    /* access modifiers changed from: private */
    public void replyAck(byte[] value) {
        if ((value[0] & 255) == 90) {
            value[0] = -91;
            Log.i("KBein", "ViseBluetooth.replyAck():--收到回复--" + HexUtil.encodeHexStr(value));
            BleByteUtil.ackPTZPanorama(value);
        }
    }

    public void getGattSerAndChaAndNotify(List<BluetoothGattService> gattServices) {
        if (gattServices != null) {
            for (BluetoothGattService gattService : gattServices) {
                if (gattService.getUuid().equals(UUID.fromString(BleConstant.SERVICE_UUID_CONFIG))) {
                    Log.e(FVMainActivity.TAG, "获取到服务uuid " + gattService.getUuid());
                    for (BluetoothGattCharacteristic characteristic2 : gattService.getCharacteristics()) {
                        int charaProp = characteristic2.getProperties();
                        if ((charaProp & 8) > 0) {
                            Log.e(FVMainActivity.TAG, "获取到写入uuid " + characteristic2.getUuid());
                            writeCharacteristic = characteristic2;
                            BleByteUtil.setWriteCharacteristic(characteristic2);
                        }
                        if ((charaProp & 16) > 0) {
                            Log.e(FVMainActivity.TAG, "获取到读取uuid " + characteristic2.getUuid());
                            Log.e(FVMainActivity.TAG, "notify=== " + getInstance().enableCharacteristicNotification(characteristic2, false));
                        }
                    }
                }
            }
        }
    }

    private ViseBluetooth() {
    }

    public void init(Context context2) {
        if (this.context == null) {
            this.context = context2.getApplicationContext();
            this.bluetoothManager = (BluetoothManager) this.context.getSystemService("bluetooth");
            this.bluetoothAdapter = this.bluetoothManager.getAdapter();
        }
    }

    public void reconnectByName() {
        ViseLog.m1466e("on reconnectByName");
        this.handler.postDelayed(new Runnable() {
            public void run() {
                boolean z = false;
                ViseLog.m1466e("do reconnect");
                BluetoothGatt unused = ViseBluetooth.this.bluetoothGatt = ViseBluetooth.this.connect(ViseBluetooth.this.bluetoothDevice, false);
                StringBuilder append = new StringBuilder().append("reconnect bluetoothGatt == null: ");
                if (ViseBluetooth.this.bluetoothGatt == null) {
                    z = true;
                }
                ViseLog.m1466e(append.append(z).toString());
            }
        }, 30000);
    }

    public void startLeScan(BluetoothAdapter.LeScanCallback leScanCallback) {
        if (this.bluetoothAdapter != null) {
            this.bluetoothAdapter.startLeScan(leScanCallback);
            this.scanState = StateScan.SCAN_PROCESS;
        }
    }

    public void startLeScan(UUID[] uuids, BluetoothAdapter.LeScanCallback leScanCallback) {
        if (this.bluetoothAdapter != null && uuids != null && uuids.length != 0) {
            this.bluetoothAdapter.startLeScan(uuids, leScanCallback);
            this.scanState = StateScan.SCAN_PROCESS;
        }
    }

    public void stopLeScan(BluetoothAdapter.LeScanCallback leScanCallback) {
        if (this.bluetoothAdapter != null) {
            this.bluetoothAdapter.stopLeScan(leScanCallback);
        }
    }

    public void startScan(PeriodScanCallback periodScanCallback) {
        if (periodScanCallback == null) {
            throw new IllegalArgumentException("this PeriodScanCallback is Null!");
        }
        periodScanCallback.setViseBluetooth(this).setScan(true).setScanTimeout(this.scanTimeout).scan();
    }

    public void stopScan(PeriodScanCallback periodScanCallback) {
        if (periodScanCallback == null) {
            throw new IllegalArgumentException("this PeriodScanCallback is Null!");
        }
        periodScanCallback.setViseBluetooth(this).setScan(false).removeHandlerMsg().scan();
    }

    @TargetApi(21)
    public void startLeScan(ScanCallback leScanCallback) {
        if (this.bluetoothAdapter != null && this.bluetoothAdapter.getBluetoothLeScanner() != null) {
            this.bluetoothAdapter.getBluetoothLeScanner().startScan(leScanCallback);
            this.scanState = StateScan.SCAN_PROCESS;
        }
    }

    @TargetApi(21)
    public void startLeScan(List<ScanFilter> filters, ScanSettings settings, ScanCallback leScanCallback) {
        if (this.bluetoothAdapter != null && this.bluetoothAdapter.getBluetoothLeScanner() != null) {
            this.bluetoothAdapter.getBluetoothLeScanner().startScan(filters, settings, leScanCallback);
            this.scanState = StateScan.SCAN_PROCESS;
        }
    }

    @TargetApi(21)
    public void stopLeScan(ScanCallback leScanCallback) {
        if (this.bluetoothAdapter != null && this.bluetoothAdapter.getBluetoothLeScanner() != null) {
            this.bluetoothAdapter.getBluetoothLeScanner().stopScan(leScanCallback);
        }
    }

    @TargetApi(21)
    public void startScan(PeriodLScanCallback periodScanCallback) {
        if (periodScanCallback == null) {
            throw new IllegalArgumentException("this PeriodScanCallback is Null!");
        }
        periodScanCallback.setViseBluetooth(this).setScan(true).setScanTimeout(this.scanTimeout).scan();
    }

    @TargetApi(21)
    public void startScan(List<ScanFilter> filters, ScanSettings settings, PeriodLScanCallback periodScanCallback) {
        if (periodScanCallback == null) {
            throw new IllegalArgumentException("this PeriodScanCallback is Null!");
        }
        periodScanCallback.setViseBluetooth(this).setScan(true).setScanTimeout(this.scanTimeout).setFilters(filters).setSettings(settings).scan();
    }

    @TargetApi(21)
    public void stopScan(PeriodLScanCallback periodScanCallback) {
        if (periodScanCallback == null) {
            throw new IllegalArgumentException("this PeriodScanCallback is Null!");
        }
        periodScanCallback.setViseBluetooth(this).setScan(false).removeHandlerMsg().scan();
    }

    @TargetApi(21)
    public ScanSettings buildScanSettings() {
        ScanSettings.Builder builder = new ScanSettings.Builder();
        builder.setScanMode(2);
        return builder.build();
    }

    @TargetApi(21)
    public List<ScanFilter> getScanFileterList() {
        List<ScanFilter> bleScanFilters = new ArrayList<>();
        bleScanFilters.add(new ScanFilter.Builder().setServiceUuid(ParcelUuid.fromString(BleConstant.SERVICE_UUIDS[CameraUtils.getCurrentPageIndex()])).build());
        return bleScanFilters;
    }

    public synchronized BluetoothGatt connect(BluetoothDevice bluetoothDevice2, boolean autoConnect) {
        if (bluetoothDevice2 == null) {
            throw new IllegalArgumentException("this BluetoothDevice or IConnectCallback is Null!");
        }
        if (this.handler != null) {
            this.handler.sendMessageDelayed(this.handler.obtainMessage(6, Long.valueOf(System.currentTimeMillis())), (long) this.connectTimeout);
        }
        this.state = State.CONNECT_PROCESS;
        return bluetoothDevice2.connectGatt(this.context, autoConnect, this.coreGattCallback);
    }

    public void connect(BluetoothLeDevice bluetoothLeDevice, boolean autoConnect) {
        if (bluetoothLeDevice == null) {
            throw new IllegalArgumentException("this BluetoothLeDevice is Null!");
        }
        connect(bluetoothLeDevice.getDevice(), autoConnect);
    }

    public void connectByName(String name, boolean autoConnect, final IConnectCallback connectCallback) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Illegal Name!");
        }
        this.isFound = false;
        startScan((PeriodScanCallback) new PeriodNameScanCallback(name) {
            public void onDeviceFound(BluetoothLeDevice bluetoothLeDevice) {
                boolean unused = ViseBluetooth.this.isFound = true;
                ViseBluetooth.this.runOnMainThread(new Runnable() {
                    public void run() {
                    }
                });
            }

            public void scanTimeout() {
                if (ViseBluetooth.this.isFound) {
                    boolean unused = ViseBluetooth.this.isFound = false;
                } else {
                    ViseBluetooth.this.runOnMainThread(new Runnable() {
                        public void run() {
                            if (connectCallback != null) {
                                connectCallback.onConnectFailure(new TimeoutException());
                            }
                        }
                    });
                }
            }
        });
    }

    public void connectByMac(String mac, boolean autoConnect, final IConnectCallback connectCallback) {
        if (mac == null || mac.split(":").length != 6) {
            throw new IllegalArgumentException("Illegal MAC!");
        }
        this.isFound = false;
        startScan((PeriodScanCallback) new PeriodMacScanCallback(mac) {
            public void onDeviceFound(BluetoothLeDevice bluetoothLeDevice) {
                boolean unused = ViseBluetooth.this.isFound = true;
                ViseBluetooth.this.runOnMainThread(new Runnable() {
                    public void run() {
                    }
                });
            }

            public void scanTimeout() {
                if (ViseBluetooth.this.isFound) {
                    boolean unused = ViseBluetooth.this.isFound = false;
                } else {
                    ViseBluetooth.this.runOnMainThread(new Runnable() {
                        public void run() {
                            if (connectCallback != null) {
                                connectCallback.onConnectFailure(new TimeoutException());
                            }
                        }
                    });
                }
            }
        });
    }

    @TargetApi(21)
    public void connectByLName(String name, boolean autoConnect, final IConnectCallback connectCallback) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Illegal Name!");
        }
        this.isFound = false;
        List<ScanFilter> bleScanFilters = new ArrayList<>();
        bleScanFilters.add(new ScanFilter.Builder().setDeviceName(name).build());
        startScan(bleScanFilters, (ScanSettings) null, new PeriodLScanCallback() {
            public void onDeviceFound(BluetoothLeDevice bluetoothLeDevice) {
                boolean unused = ViseBluetooth.this.isFound = true;
                ViseBluetooth.this.runOnMainThread(new Runnable() {
                    public void run() {
                    }
                });
            }

            public void scanTimeout() {
                if (ViseBluetooth.this.isFound) {
                    boolean unused = ViseBluetooth.this.isFound = false;
                } else {
                    ViseBluetooth.this.runOnMainThread(new Runnable() {
                        public void run() {
                            if (connectCallback != null) {
                                connectCallback.onConnectFailure(new TimeoutException());
                            }
                        }
                    });
                }
            }
        });
    }

    @TargetApi(21)
    public void connectByLMac(String mac, boolean autoConnect, final IConnectCallback connectCallback) {
        if (mac == null || mac.split(":").length != 6) {
            throw new IllegalArgumentException("Illegal MAC!");
        }
        this.isFound = false;
        List<ScanFilter> bleScanFilters = new ArrayList<>();
        bleScanFilters.add(new ScanFilter.Builder().setDeviceAddress(mac).build());
        startScan(bleScanFilters, (ScanSettings) null, new PeriodLScanCallback() {
            public void onDeviceFound(BluetoothLeDevice bluetoothLeDevice) {
                boolean unused = ViseBluetooth.this.isFound = true;
                ViseBluetooth.this.runOnMainThread(new Runnable() {
                    public void run() {
                    }
                });
            }

            public void scanTimeout() {
                if (ViseBluetooth.this.isFound) {
                    boolean unused = ViseBluetooth.this.isFound = false;
                } else {
                    ViseBluetooth.this.runOnMainThread(new Runnable() {
                        public void run() {
                            if (connectCallback != null) {
                                connectCallback.onConnectFailure(new TimeoutException());
                            }
                        }
                    });
                }
            }
        });
    }

    public ViseBluetooth withUUID(UUID serviceUUID, UUID characteristicUUID, UUID descriptorUUID) {
        if (!(serviceUUID == null || this.bluetoothGatt == null)) {
            this.service = this.bluetoothGatt.getService(serviceUUID);
        }
        if (!(this.service == null || characteristicUUID == null)) {
            this.characteristic = this.service.getCharacteristic(characteristicUUID);
        }
        if (!(this.characteristic == null || descriptorUUID == null)) {
            this.descriptor = this.characteristic.getDescriptor(descriptorUUID);
        }
        return this;
    }

    public ViseBluetooth withUUIDString(String serviceUUID, String characteristicUUID, String descriptorUUID) {
        return withUUID(formUUID(serviceUUID), formUUID(characteristicUUID), formUUID(descriptorUUID));
    }

    private UUID formUUID(String uuid) {
        if (uuid == null) {
            return null;
        }
        return UUID.fromString(uuid);
    }

    public boolean writeCharacteristic(BluetoothGattCharacteristic characteristic2, byte[] data, final ICharacteristicCallback bleCallback) {
        if (characteristic2 == null) {
            if (bleCallback != null) {
                runOnMainThread(new Runnable() {
                    public void run() {
                        bleCallback.onFailure(new OtherException("this characteristic is null!"));
                        ViseBluetooth.this.removeBleCallback((IBleCallback) bleCallback);
                    }
                });
            }
            return false;
        }
        ViseLog.m1468i(characteristic2.getUuid() + " characteristic write bytes: " + Arrays.toString(data) + " ,hex: " + HexUtil.encodeHexStr(data));
        listenAndTimer((IBleCallback) bleCallback, 1);
        characteristic2.setValue(data);
        return handleAfterInitialed(getBluetoothGatt().writeCharacteristic(characteristic2), (IBleCallback) bleCallback);
    }

    public boolean writeCharacteristic(BluetoothGattCharacteristic characteristic2, byte[] data, final long timeMillis) {
        if (characteristic2 == null || getBluetoothGatt() == null) {
            runOnMainThread(new Runnable() {
                public void run() {
                    ViseLog.m1466e("GMU writeCharacteristic FAIL");
                    EventBusUtil.sendEvent(new Event<>(102, 98));
                    ViseBluetooth.this.removeBleCallback(timeMillis);
                }
            });
            return false;
        }
        ViseLog.m1466e("characteristic write bytes:  " + HexUtil.encodeHexStr(data));
        listenAndTimer(timeMillis, 1);
        characteristic2.setValue(data);
        boolean result = getBluetoothGatt().writeCharacteristic(characteristic2);
        ViseLog.m1466e("getBluetoothGatt().writeCharacteristic(characteristic) result: " + result);
        if ((data[1] & 255) == 45) {
            Log.i("KBein", "ViseBluetooth.writeCharacteristic():--getBluetoothGatt().writeCharacteristic(characteristic) result: --" + result);
            Log.i("KBein", "ViseBluetooth.writeCharacteristic():--data--" + HexUtil.encodeHexStr(data));
        }
        return handleAfterInitialed(result, timeMillis);
    }

    public boolean writeDescriptor(byte[] data, IDescriptorCallback bleCallback) {
        return writeDescriptor(getDescriptor(), data, bleCallback);
    }

    public boolean writeDescriptor(BluetoothGattDescriptor descriptor2, byte[] data, final IDescriptorCallback bleCallback) {
        if (descriptor2 == null) {
            if (bleCallback != null) {
                runOnMainThread(new Runnable() {
                    public void run() {
                        bleCallback.onFailure(new OtherException("this descriptor is null!"));
                        ViseBluetooth.this.removeBleCallback((IBleCallback) bleCallback);
                    }
                });
            }
            return false;
        }
        ViseLog.m1466e(descriptor2.getUuid() + " descriptor write bytes: " + Arrays.toString(data) + " ,hex: " + HexUtil.encodeHexStr(data));
        descriptor2.setValue(data);
        return handleAfterInitialed(getBluetoothGatt().writeDescriptor(descriptor2), (IBleCallback) bleCallback);
    }

    public boolean readCharacteristic(ICharacteristicCallback bleCallback) {
        return readCharacteristic(getCharacteristic(), bleCallback);
    }

    public boolean readCharacteristic(BluetoothGattCharacteristic characteristic2, final ICharacteristicCallback bleCallback) {
        if (characteristic2 != null && (characteristic2.getProperties() | 2) > 0) {
            setCharacteristicNotification(getBluetoothGatt(), characteristic2, false, false);
            return handleAfterInitialed(getBluetoothGatt().readCharacteristic(characteristic2), (IBleCallback) bleCallback);
        } else if (bleCallback == null) {
            return false;
        } else {
            runOnMainThread(new Runnable() {
                public void run() {
                    bleCallback.onFailure(new OtherException("Characteristic [is not] readable!"));
                    ViseBluetooth.this.removeBleCallback((IBleCallback) bleCallback);
                }
            });
            return false;
        }
    }

    public boolean readDescriptor(IDescriptorCallback bleCallback) {
        return readDescriptor(getDescriptor(), bleCallback);
    }

    public boolean readDescriptor(BluetoothGattDescriptor descriptor2, IDescriptorCallback bleCallback) {
        return handleAfterInitialed(getBluetoothGatt().readDescriptor(descriptor2), (IBleCallback) bleCallback);
    }

    public boolean readRemoteRssi(IRssiCallback bleCallback) {
        return handleAfterInitialed(getBluetoothGatt().readRemoteRssi(), (IBleCallback) bleCallback);
    }

    public boolean enableCharacteristicNotification(ICharacteristicCallback bleCallback, boolean isIndication) {
        return enableCharacteristicNotification(getCharacteristic(), isIndication);
    }

    public boolean enableCharacteristicNotification(BluetoothGattCharacteristic characteristic2, boolean isIndication) {
        if (characteristic2 == null || (characteristic2.getProperties() | 16) <= 0) {
            return false;
        }
        return setCharacteristicNotification(getBluetoothGatt(), characteristic2, true, isIndication);
    }

    public boolean setNotification(boolean enable, boolean isIndication) {
        return setNotification(getBluetoothGatt(), getCharacteristic(), getDescriptor(), enable, isIndication);
    }

    public boolean setNotification(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic2, BluetoothGattDescriptor descriptor2, boolean enable, boolean isIndication) {
        return setCharacteristicNotification(gatt, characteristic2, enable, isIndication) && setDescriptorNotification(gatt, descriptor2, enable);
    }

    public boolean setCharacteristicNotification(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic2, boolean enable, boolean isIndication) {
        BluetoothGattDescriptor descriptor2;
        if (gatt == null || characteristic2 == null) {
            return false;
        }
        ViseLog.m1466e("Characteristic set notification value: " + enable);
        ViseLog.m1466e("Characteristic set notification Uuid: " + characteristic2.getUuid());
        boolean success = gatt.setCharacteristicNotification(characteristic2, enable);
        if (!enable || (descriptor2 = characteristic2.getDescriptor(UUID.fromString(BleConstant.CLIENT_CHARACTERISTIC_CONFIG))) == null) {
            return success;
        }
        if (isIndication) {
            descriptor2.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
        } else {
            descriptor2.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        }
        gatt.writeDescriptor(descriptor2);
        ViseLog.m1466e("Characteristic set notification is Success!");
        return success;
    }

    public boolean setDescriptorNotification(BluetoothGatt gatt, BluetoothGattDescriptor descriptor2, boolean enable) {
        if (gatt == null || descriptor2 == null) {
            return false;
        }
        ViseLog.m1468i("Descriptor set notification value: " + enable);
        if (enable) {
            descriptor2.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        } else {
            descriptor2.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
        }
        return gatt.writeDescriptor(descriptor2);
    }

    private boolean handleAfterInitialed(boolean initiated, final IBleCallback bleCallback) {
        if (bleCallback != null && !initiated) {
            if (this.handler != null) {
                this.handler.removeCallbacksAndMessages((Object) null);
            }
            runOnMainThread(new Runnable() {
                public void run() {
                    bleCallback.onFailure(new InitiatedException());
                    ViseBluetooth.this.removeBleCallback(bleCallback);
                }
            });
        }
        return initiated;
    }

    private boolean handleAfterInitialed(boolean initiated, final long timeMillis) {
        if (!initiated) {
            if (this.handler != null) {
                this.handler.removeCallbacksAndMessages((Object) null);
            } else {
                runOnMainThread(new Runnable() {
                    public void run() {
                        EventBusUtil.sendEvent(new Event<>(102, 99));
                        ViseBluetooth.this.removeBleCallback(timeMillis);
                    }
                });
            }
        }
        return initiated;
    }

    private synchronized void listenAndTimer(IBleCallback bleCallback, int what) {
        if (!(this.bleCallbacks == null || this.handler == null)) {
            this.tempBleCallback = bleCallback;
            this.bleCallbacks.add(bleCallback);
            this.handler.sendMessageDelayed(this.handler.obtainMessage(what, bleCallback), 200);
        }
    }

    private synchronized void listenAndTimer(long mTimeMillis, int what) {
        if (!(this.bleLongTimes == null || this.handler == null)) {
            this.tempTimeMillis = mTimeMillis;
            this.bleLongTimes.add(Long.valueOf(mTimeMillis));
            this.handler.sendMessageDelayed(this.handler.obtainMessage(what, Long.valueOf(mTimeMillis)), 3000);
        }
    }

    public boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public void runOnMainThread(Runnable runnable) {
        if (isMainThread()) {
            runnable.run();
        } else if (this.handler != null) {
            this.handler.post(runnable);
        }
    }

    public boolean isConnected() {
        if (this.state == State.CONNECT_SUCCESS) {
            return true;
        }
        return false;
    }

    public synchronized void removeBleCallback(IBleCallback bleCallback) {
        if (this.bleCallbacks != null && this.bleCallbacks.size() > 0) {
            this.bleCallbacks.remove(bleCallback);
        }
    }

    public synchronized void removeBleCallback(long timeMillis) {
        if (this.bleLongTimes != null && this.bleLongTimes.size() > 0) {
            this.bleLongTimes.remove(Long.valueOf(timeMillis));
        }
    }

    public synchronized boolean refreshDeviceCache() {
        boolean success;
        try {
            Method refresh = BluetoothGatt.class.getMethod("refresh", new Class[0]);
            if (!(refresh == null || this.bluetoothGatt == null)) {
                success = ((Boolean) refresh.invoke(getBluetoothGatt(), new Object[0])).booleanValue();
                ViseLog.m1468i("Refreshing result: " + success);
            }
        } catch (Exception e) {
            ViseLog.m1466e("An exception occured while refreshing device" + e);
        }
        success = false;
        return success;
    }

    public synchronized void disconnect() {
        if (this.bluetoothGatt != null) {
            this.bluetoothGatt.disconnect();
        }
    }

    public synchronized void close() {
        ViseLog.m1466e("GATT CLOSE");
        if (this.bluetoothGatt != null) {
            this.bluetoothGatt.close();
        }
    }

    public synchronized void clear() {
        ViseLog.m1466e("clear GATT");
        disconnect();
        refreshDeviceCache();
        close();
        this.bluetoothGatt = null;
        this.bluetoothDevice = null;
        if (this.bleCallbacks != null) {
            this.bleCallbacks.clear();
        }
        if (this.handler != null) {
            this.handler.removeCallbacksAndMessages((Object) null);
        }
    }

    public BluetoothManager getBluetoothManager() {
        return this.bluetoothManager;
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return this.bluetoothAdapter;
    }

    public BluetoothGatt getBluetoothGatt() {
        return this.bluetoothGatt;
    }

    public void setBluetoothGatt(BluetoothGatt gatt) {
        this.bluetoothGatt = gatt;
    }

    public Set<IBleCallback> getBleCallbacks() {
        return this.bleCallbacks;
    }

    public BluetoothGattService getService() {
        return this.service;
    }

    public ViseBluetooth setService(BluetoothGattService service2) {
        this.service = service2;
        return this;
    }

    public BluetoothGattCharacteristic getCharacteristic() {
        return this.characteristic;
    }

    public ViseBluetooth setCharacteristic(BluetoothGattCharacteristic characteristic2) {
        this.characteristic = characteristic2;
        return this;
    }

    public BluetoothGattCharacteristic getWriteCharacteristic() {
        return writeCharacteristic;
    }

    public ViseBluetooth setWriteCharacteristic(BluetoothGattCharacteristic characteristic2) {
        writeCharacteristic = characteristic2;
        return this;
    }

    public BluetoothGattDescriptor getDescriptor() {
        return this.descriptor;
    }

    public ViseBluetooth setDescriptor(BluetoothGattDescriptor descriptor2) {
        this.descriptor = descriptor2;
        return this;
    }

    public int getOperateTimeout() {
        return this.operateTimeout;
    }

    public ViseBluetooth setOperateTimeout(int operateTimeout2) {
        this.operateTimeout = operateTimeout2;
        return this;
    }

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    public ViseBluetooth setConnectTimeout(int connectTimeout2) {
        this.connectTimeout = connectTimeout2;
        return this;
    }

    public int getScanTimeout() {
        return this.scanTimeout;
    }

    public ViseBluetooth setScanTimeout(int scanTimeout2) {
        this.scanTimeout = scanTimeout2;
        return this;
    }

    public State getState() {
        return this.state;
    }

    public ViseBluetooth setState(State state2) {
        this.state = state2;
        return this;
    }

    public StateScan getScanState() {
        return this.scanState;
    }

    public void setScanState(StateScan scanState2) {
        this.scanState = scanState2;
    }

    public BluetoothLeDevice getConnDevice() {
        return this.connDevice;
    }

    public void setConnDevice(BluetoothLeDevice device) {
        this.connDevice = device;
    }
}
