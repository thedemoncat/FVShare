package com.freevisiontech.fvmobile.utils;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.freevisiontech.fvmobile.ViseBluetooth;
import com.freevisiontech.fvmobile.application.MyApplication;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.vise.log.ViseLog;
import java.util.LinkedList;
import java.util.Queue;

public class BleFirmWareUtil {
    private static final int FIRMWARE_UPGRADE_RESEND_FOR_GMU = 1011;
    private static final int FIRMWARE_UPGRADE_RESEND_FOR_IMU = 1012;
    private static final int FIRMWARE_UPGRADE_TIMEOUT = 10000;
    private static final int GMU_JUMPED_TO_BOOTER = 1013;
    private Queue<byte[]> dataInfoQueue = new LinkedList();
    private boolean gmuRestarted = false;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 7:
                    if (BleFirmWareUtil.this.mStateListener != null) {
                        BleFirmWareUtil.this.mStateListener.onFailed(14);
                        return;
                    }
                    return;
                case 9:
                    if (BleFirmWareUtil.this.mStateListener != null) {
                        ViseLog.m1466e("第一步握手消息超时触发");
                        BleFirmWareUtil.this.mStateListener.onFailed(2);
                        return;
                    }
                    return;
                case 1011:
                    BleFirmWareUtil.this.send(BleFirmWareUtil.this.mContents, BleFirmWareUtil.this.requreSequenceGMU, BleFirmWareUtil.this.requrePackageNumGMU);
                    return;
                case 1012:
                    BleFirmWareUtil.this.send(BleFirmWareUtil.this.mContents, BleFirmWareUtil.this.requreSequenceIMU, BleFirmWareUtil.this.requrePackageNumIMU);
                    return;
                default:
                    return;
            }
        }
    };
    private int isForce = 0;
    private boolean isGmuInBooter = false;
    private boolean isUpgradeOK = false;
    /* access modifiers changed from: private */
    public BluetoothGattCharacteristic mCharacteristic;
    /* access modifiers changed from: private */
    public byte[] mContents;
    private Context mContext;
    private String mCrc;
    private int mFiducial = 0;
    private boolean mHasFiducial = false;
    private OnProgressCallback mProgressListener;
    /* access modifiers changed from: private */
    public FirmwareUpgradeStateListener mStateListener;
    private int mType = 1;
    private String mVersion;
    /* access modifiers changed from: private */
    public int requrePackageNumGMU = -1;
    /* access modifiers changed from: private */
    public int requrePackageNumIMU = -1;
    /* access modifiers changed from: private */
    public int requreSequenceGMU = -1;
    /* access modifiers changed from: private */
    public int requreSequenceIMU = -1;
    private int retrysend = 0;
    private Runnable runnable = new Runnable() {
        public void run() {
            BleFirmWareUtil.this.send();
        }
    };
    private long startTime;

    public interface FirmwareUpgradeStateListener {
        void onFailed(int i);

        void onSuccess();
    }

    public interface OnProgressCallback {
        void onProgressChanging(int i);
    }

    public boolean isGmuInBooter() {
        return this.isGmuInBooter;
    }

    public void setGmuInBooter(boolean gmuInBooter) {
        this.isGmuInBooter = gmuInBooter;
    }

    public BleFirmWareUtil(Context context) {
        this.mContext = context;
    }

    public void init(BluetoothGattCharacteristic mWriteCharacteristic) {
        this.mCharacteristic = mWriteCharacteristic;
        ViseLog.m1466e("BluetoothGattCharacteristic == null " + (mWriteCharacteristic == null));
    }

    public void reSend() {
        if (this.handler != null) {
            ViseLog.m1466e("进入重发程序");
            this.handler.postDelayed(this.runnable, 50);
        }
    }

    public void reSendNext() {
        if (this.handler != null) {
            ViseLog.m1466e("进入重发程序");
            if (this.dataInfoQueue != null && !this.dataInfoQueue.isEmpty()) {
                this.dataInfoQueue.poll();
                this.handler.postDelayed(this.runnable, 50);
            }
        }
    }

    /* access modifiers changed from: private */
    public void send() {
        if (this.dataInfoQueue == null) {
            return;
        }
        if (this.dataInfoQueue.peek() != null) {
            ViseBluetooth.getInstance().writeCharacteristic(this.mCharacteristic, this.dataInfoQueue.peek(), System.currentTimeMillis());
            if (!this.mHasFiducial) {
                this.mFiducial = getSendContentSize();
                this.mHasFiducial = true;
            }
            this.mProgressListener.onProgressChanging(((getSendContentSize() - this.mFiducial) * 100) / (getContentSize() - this.mFiducial));
            return;
        }
        this.mProgressListener.onProgressChanging(100);
        this.dataInfoQueue = null;
        ViseLog.m1466e("数据发送结束,开启校验超时" + (System.currentTimeMillis() - this.startTime));
        this.handler.sendEmptyMessageDelayed(7, 10000);
    }

    public void sendGmuJumpToBootCommand() {
        ViseLog.m1466e("sendGmuJumpToBootCommand");
        byte[] bytes = new byte[16];
        bytes[0] = 91;
        bytes[1] = 0;
        bytes[2] = 1;
        for (int i = 3; i < bytes.length; i++) {
            bytes[i] = 0;
        }
        ViseBluetooth.getInstance().writeCharacteristic(this.mCharacteristic, bytes, System.currentTimeMillis());
    }

    private void sendGmuBootAck() {
        byte[] bytes = new byte[16];
        bytes[0] = 91;
        bytes[1] = 0;
        bytes[2] = 2;
        for (int i = 3; i < bytes.length; i++) {
            bytes[i] = 0;
        }
        ViseBluetooth.getInstance().writeCharacteristic(this.mCharacteristic, bytes, System.currentTimeMillis());
    }

    /* access modifiers changed from: private */
    public void send(byte[] data, int equencenumber, int packagenumber) {
        this.startTime = System.currentTimeMillis();
        if (this.dataInfoQueue != null) {
            this.dataInfoQueue.clear();
            if (equencenumber != 0 || packagenumber != 0) {
                int number = (equencenumber * 18 * 256) + (packagenumber * 18);
                int length = data.length - number;
                byte[] redirectData = new byte[length];
                System.arraycopy(data, number, redirectData, 0, length);
                if (this.mType == 1) {
                    this.dataInfoQueue = splitPacketFor20Byte(redirectData, (byte) 92, packagenumber);
                } else {
                    this.dataInfoQueue = splitPacketFor20Byte(redirectData, (byte) 76, packagenumber);
                }
            } else if (this.mType == 1) {
                this.dataInfoQueue = splitPacketFor20Byte(data, (byte) 92, packagenumber);
            } else {
                this.dataInfoQueue = splitPacketFor20Byte(data, (byte) 76, packagenumber);
            }
            if (this.dataInfoQueue != null && !this.dataInfoQueue.isEmpty()) {
                this.handler.postDelayed(this.runnable, 10);
                return;
            }
            return;
        }
        if (equencenumber != 0 || packagenumber != 0) {
            int number2 = (equencenumber * 18 * 256) + (packagenumber * 18);
            int length2 = data.length - number2;
            byte[] redirectData2 = new byte[length2];
            System.arraycopy(data, number2, redirectData2, 0, length2);
            if (this.mType == 1) {
                this.dataInfoQueue = splitPacketFor20Byte(redirectData2, (byte) 92, packagenumber);
            } else {
                this.dataInfoQueue = splitPacketFor20Byte(redirectData2, (byte) 76, packagenumber);
            }
        } else if (this.mType == 1) {
            this.dataInfoQueue = splitPacketFor20Byte(data, (byte) 92, packagenumber);
        } else {
            this.dataInfoQueue = splitPacketFor20Byte(data, (byte) 76, packagenumber);
        }
        if (this.dataInfoQueue != null && !this.dataInfoQueue.isEmpty()) {
            this.handler.postDelayed(this.runnable, 10);
        }
    }

    public void writeCallbackSuccess() {
        if (this.dataInfoQueue != null) {
            this.dataInfoQueue.poll();
            this.handler.postDelayed(this.runnable, 3);
        }
    }

    public void upgradeInfo(byte[] value) {
        if ((value[0] & 255) == 91) {
            if ((value[1] & 255) == 0 && (value[2] & 255) == 2) {
                ViseLog.m1466e("GMU JUMPED INTO BOOT");
                setGmuInBooter(true);
                sendGmuBootAck();
                inquiryIsAbleTorade();
            } else if ((value[1] & 255) == 2) {
                BlePtzParasConstant.isFirmwareUpgrading = true;
                if (this.handler != null) {
                    ViseLog.m1466e("GMU移除握手超时");
                    this.handler.removeMessages(9);
                }
                send(this.mContents, 0, 0);
            } else if ((value[1] & 255) == 3) {
                if ((value[2] & 255) == 0) {
                    if (this.handler != null) {
                        this.handler.removeMessages(9);
                    }
                    if (this.mStateListener != null) {
                        this.mStateListener.onFailed(3);
                    }
                }
            } else if ((value[1] & 255) == 4) {
                if ((value[2] & 255) == 0) {
                    if (this.handler != null) {
                        this.handler.removeMessages(9);
                    }
                    if (this.mStateListener != null) {
                        this.mStateListener.onFailed(4);
                    }
                }
            } else if ((value[1] & 255) == 6) {
                if ((value[2] & 255) == 0) {
                    this.isUpgradeOK = true;
                    if (this.handler != null) {
                        this.handler.removeMessages(7);
                    }
                    if (this.mStateListener != null) {
                        this.mStateListener.onSuccess();
                    }
                }
            } else if ((value[1] & 255) == 7) {
                if ((value[2] & 255) == 0) {
                    this.isUpgradeOK = false;
                    if (this.handler != null) {
                        this.handler.removeMessages(7);
                    }
                    if (this.mStateListener != null) {
                        this.mStateListener.onFailed(7);
                    }
                }
            } else if ((value[1] & 255) == 8 && (value[2] & 255) == 0) {
                this.isUpgradeOK = false;
                if (this.handler != null) {
                    this.handler.removeMessages(7);
                }
                if (this.mStateListener != null) {
                    this.mStateListener.onFailed(8);
                }
            }
        }
        if ((value[0] & 255) == 92 && (value[1] & 255) == 1) {
            if (this.dataInfoQueue != null) {
                this.dataInfoQueue.clear();
                this.dataInfoQueue = null;
            }
            if (this.handler != null) {
                this.handler.removeCallbacksAndMessages((Object) null);
            }
            this.requreSequenceGMU = value[2] & 255;
            this.requrePackageNumGMU = value[3] & 255;
            ViseLog.m1466e("GMU错误包重传:包序号" + this.requreSequenceGMU + "包数" + this.requrePackageNumGMU);
            this.handler.sendEmptyMessageDelayed(1011, 50);
        }
        if ((value[0] & 255) == 94) {
            if ((value[1] & 255) == 2) {
                BlePtzParasConstant.isFirmwareUpgrading = true;
                if (this.handler != null) {
                    ViseLog.m1466e("IMU移除握手超时");
                    this.handler.removeMessages(9);
                }
                send(this.mContents, 0, 0);
            } else if ((value[1] & 255) == 3) {
                if ((value[2] & 255) == 0) {
                    if (this.handler != null) {
                        this.handler.removeMessages(9);
                    }
                    if (this.mStateListener != null) {
                        this.mStateListener.onFailed(3);
                    }
                }
            } else if ((value[1] & 255) == 4) {
                if ((value[2] & 255) == 0) {
                    if (this.handler != null) {
                        this.handler.removeMessages(9);
                    }
                    if (this.mStateListener != null) {
                        this.mStateListener.onFailed(4);
                    }
                }
            } else if ((value[1] & 255) == 6) {
                if ((value[2] & 255) == 0) {
                    if (this.handler != null) {
                        this.handler.removeMessages(9);
                    }
                    if (this.mStateListener != null) {
                        this.mStateListener.onFailed(6);
                    }
                }
            } else if ((value[1] & 255) == 7) {
                if ((value[2] & 255) == 0) {
                    if (this.handler != null) {
                        this.handler.removeMessages(9);
                    }
                    if (this.mStateListener != null) {
                        this.mStateListener.onFailed(7);
                    }
                }
            } else if ((value[1] & 255) == 8) {
                if ((value[2] & 255) == 0) {
                    if (this.handler != null) {
                        this.handler.removeMessages(9);
                    }
                    if (this.mStateListener != null) {
                        this.mStateListener.onFailed(8);
                    }
                }
            } else if ((value[1] & 255) == 9) {
                if (this.handler != null) {
                    this.handler.removeMessages(7);
                }
                this.isUpgradeOK = false;
                if (this.mStateListener != null) {
                    this.mStateListener.onFailed(9);
                }
            } else if ((value[1] & 255) == 10) {
                if (this.handler != null) {
                    this.handler.removeMessages(7);
                }
                this.isUpgradeOK = false;
                if (this.mStateListener != null) {
                    this.mStateListener.onFailed(10);
                }
            } else if ((value[1] & 255) == 11) {
                if (this.handler != null) {
                    this.handler.removeMessages(7);
                }
                this.isUpgradeOK = true;
                if (this.mStateListener != null) {
                    this.mStateListener.onSuccess();
                }
            } else if ((value[1] & 255) == 12) {
                if (this.handler != null) {
                    this.handler.removeMessages(7);
                }
                this.isUpgradeOK = false;
                if (this.mStateListener != null) {
                    this.mStateListener.onFailed(12);
                }
            } else if ((value[1] & 255) == 13) {
                if (this.handler != null) {
                    this.handler.removeMessages(7);
                }
                this.isUpgradeOK = false;
                if (this.mStateListener != null) {
                    this.mStateListener.onFailed(13);
                }
            }
        }
        if ((value[0] & 255) == 76 && (value[1] & 255) == 1) {
            if (this.dataInfoQueue != null) {
                this.dataInfoQueue.clear();
                this.dataInfoQueue = null;
            }
            if (this.handler != null) {
                this.handler.removeCallbacksAndMessages((Object) null);
            }
            this.requreSequenceIMU = value[2] & 255;
            this.requrePackageNumIMU = value[3] & 255;
            ViseLog.m1466e("IMU错误包重传:包序号" + this.requreSequenceIMU + "包数" + this.requrePackageNumIMU);
            this.handler.sendEmptyMessageDelayed(1012, 50);
        }
    }

    public Queue<byte[]> splitPacketFor20Byte(byte[] data, byte command, int packagenumber) {
        Queue<byte[]> dataInfoQueue2 = new LinkedList<>();
        if (data != null) {
            int index = 0;
            do {
                byte[] surplusData = new byte[(data.length - index)];
                byte[] currentData = new byte[20];
                currentData[0] = command;
                if (packagenumber > 255) {
                    currentData[1] = (byte) (packagenumber % 256);
                } else {
                    currentData[1] = (byte) packagenumber;
                }
                System.arraycopy(data, index, surplusData, 0, data.length - index);
                if (surplusData.length < 18) {
                    System.arraycopy(surplusData, 0, currentData, 2, surplusData.length);
                    for (int p = 19; p >= surplusData.length + 2; p--) {
                        currentData[p] = -1;
                    }
                    index += surplusData.length;
                    packagenumber++;
                } else {
                    System.arraycopy(data, index, currentData, 2, 18);
                    index += 18;
                    packagenumber++;
                }
                dataInfoQueue2.offer(currentData);
            } while (index < data.length);
        }
        return dataInfoQueue2;
    }

    public void requireIntoUpdateMode(byte[] contents, int type, String crc, String version, FirmwareUpgradeStateListener listener, OnProgressCallback progressListener) {
        this.mContents = contents;
        this.mCrc = crc;
        this.mVersion = version;
        this.mStateListener = listener;
        this.mType = type;
        this.mProgressListener = progressListener;
        String ptzType = MyApplication.CURRENT_PTZ_TYPE;
        if (type != 1 || !BleConstant.FM_300.equals(ptzType) || isGmuInBoot()) {
            inquiryIsAbleTorade();
        } else {
            sendGmuJumpToBootCommand();
        }
    }

    private boolean isGmuInBoot() {
        if (BlePtzParasConstant.GET_GMU_FIRMWARE_APP_VERSION_STRING.equals("00.00.00.00")) {
            return true;
        }
        return false;
    }

    public void inquiryIsAbleTorade() {
        final byte[] bytes;
        ViseLog.m1466e("inquiryIsAbleTorade");
        if (this.mType == 1) {
            bytes = sendUpgradeStartMark(this.mContents, (byte) 91, this.mCrc, this.mVersion, this.isForce);
        } else {
            bytes = sendUpgradeStartMark(this.mContents, (byte) 94, this.mCrc, this.mVersion, this.isForce);
        }
        this.handler.sendEmptyMessageDelayed(9, 10000);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                ViseBluetooth.getInstance().writeCharacteristic(BleFirmWareUtil.this.mCharacteristic, bytes, System.currentTimeMillis());
            }
        }, 500);
    }

    public byte[] sendUpgradeStartMark(byte[] data, byte command, String crc, String version, int isForce2) {
        int fileLenth = data.length;
        byte[] value = new byte[20];
        value[0] = command;
        value[1] = 1;
        value[2] = (byte) ((fileLenth >> 0) & 255);
        value[3] = (byte) ((fileLenth >> 8) & 255);
        value[4] = (byte) ((fileLenth >> 16) & 255);
        value[5] = (byte) ((fileLenth >> 24) & 255);
        String crc1 = crc.substring(0, 2);
        String crc2 = crc.substring(2, 4);
        String crc3 = crc.substring(4, 6);
        String crc4 = crc.substring(6, crc.length());
        int int1 = Integer.parseInt(crc1, 16);
        int int2 = Integer.parseInt(crc2, 16);
        int int3 = Integer.parseInt(crc3, 16);
        value[6] = (byte) (Integer.parseInt(crc4, 16) & 255);
        value[7] = (byte) (int3 & 255);
        value[8] = (byte) (int2 & 255);
        value[9] = (byte) (int1 & 255);
        String[] split = version.split("\\.");
        String versions1 = split[0];
        String versions2 = split[1];
        String versions3 = split[2];
        String versions4 = split[3];
        int version1 = Integer.parseInt(versions1, 16);
        int version2 = Integer.parseInt(versions2, 16);
        int version3 = Integer.parseInt(versions3, 16);
        value[10] = (byte) (Integer.parseInt(versions4, 16) & 255);
        value[11] = (byte) (version3 & 255);
        value[12] = (byte) (version2 & 255);
        value[13] = (byte) (version1 & 255);
        value[14] = (byte) isForce2;
        for (int i = 15; i < value.length; i++) {
            value[i] = 0;
        }
        return value;
    }

    public int getContentSize() {
        return this.mContents.length;
    }

    public int getSendContentSize() {
        return this.mContents.length - this.dataInfoQueue.size();
    }
}
