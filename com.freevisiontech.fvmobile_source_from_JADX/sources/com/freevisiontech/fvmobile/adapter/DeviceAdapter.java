package com.freevisiontech.fvmobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.ViseBluetooth;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.freevisiontech.fvmobile.model.BluetoothLeDevice;
import com.freevisiontech.fvmobile.utils.BlePtzParasConstant;
import java.util.ArrayList;
import java.util.List;

public class DeviceAdapter extends BaseAdapter {
    private BluetoothLeDevice connDevice;
    private Context context;
    private List<BluetoothLeDevice> deviceList = new ArrayList();

    public BluetoothLeDevice getConnDevice() {
        return this.connDevice;
    }

    public void setConnDevice(BluetoothLeDevice connDevice2) {
        this.connDevice = connDevice2;
    }

    public DeviceAdapter(Context context2) {
        this.context = context2;
    }

    public DeviceAdapter setDeviceList(List<BluetoothLeDevice> mDeviceList) {
        if (this.deviceList != null) {
            this.deviceList.clear();
        }
        if (ViseBluetooth.getInstance().isConnected() && BleConstant.SERVICE_UUID_CONFIG != null && BlePtzParasConstant.GET_CURRENT_PTZ_UUID.equals(BleConstant.SERVICE_UUID_CONFIG)) {
            this.deviceList.add(this.connDevice);
        }
        if (this.deviceList != null) {
            this.deviceList.addAll(mDeviceList);
        } else {
            this.deviceList = mDeviceList;
        }
        notifyDataSetChanged();
        return this;
    }

    public List<BluetoothLeDevice> getDeviceList() {
        return this.deviceList;
    }

    public int getCount() {
        if (this.deviceList != null) {
            return this.deviceList.size();
        }
        return 0;
    }

    public Object getItem(int position) {
        if (this.deviceList != null) {
            return this.deviceList.get(position);
        }
        return null;
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(this.context).inflate(C0853R.layout.item_scan_layout, (ViewGroup) null);
            viewHolder.deviceName = (TextView) convertView.findViewById(C0853R.C0855id.device_name);
            viewHolder.deviceMac = (TextView) convertView.findViewById(C0853R.C0855id.device_mac);
            viewHolder.deviceRssi = (ImageView) convertView.findViewById(C0853R.C0855id.device_rssi);
            viewHolder.deviceConnect = (ImageView) convertView.findViewById(C0853R.C0855id.device_connected);
            viewHolder.deviceScanRecord = (TextView) convertView.findViewById(C0853R.C0855id.device_scanRecord);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (!(this.deviceList == null || this.deviceList.get(position) == null || this.deviceList.get(position).getDevice() == null)) {
            String deviceName = this.deviceList.get(position).getDevice().getName();
            if (deviceName == null || deviceName.isEmpty()) {
                viewHolder.deviceName.setText("");
            } else {
                viewHolder.deviceName.setText(deviceName);
            }
            viewHolder.deviceMac.setText(this.deviceList.get(position).getDevice().getAddress());
            int correctRssi = this.deviceList.get(position).getRssi() + 150;
            if (correctRssi < 0) {
                correctRssi = 0;
            }
            if (correctRssi > 100) {
                correctRssi = 100;
            }
            if (correctRssi >= 0 && correctRssi < 25) {
                viewHolder.deviceRssi.setImageResource(C0853R.mipmap.rssi_1);
            } else if (correctRssi >= 25 && correctRssi < 50) {
                viewHolder.deviceRssi.setImageResource(C0853R.mipmap.rssi_2);
            } else if (correctRssi < 50 || correctRssi >= 75) {
                viewHolder.deviceRssi.setImageResource(C0853R.mipmap.rssi_4);
            } else {
                viewHolder.deviceRssi.setImageResource(C0853R.mipmap.rssi_3);
            }
            if (this.deviceList.get(position).isConnected()) {
                viewHolder.deviceConnect.setVisibility(0);
            } else {
                viewHolder.deviceConnect.setVisibility(8);
            }
        }
        return convertView;
    }

    class ViewHolder {
        ImageView deviceConnect;
        TextView deviceMac;
        TextView deviceName;
        ImageView deviceRssi;
        TextView deviceScanRecord;

        ViewHolder() {
        }
    }
}
