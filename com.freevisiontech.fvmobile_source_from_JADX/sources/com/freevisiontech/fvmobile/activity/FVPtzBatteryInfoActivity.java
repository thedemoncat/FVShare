package com.freevisiontech.fvmobile.activity;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.ViseBluetooth;
import com.freevisiontech.fvmobile.bean.FVModeSelectEvent;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utility.Constants;
import com.freevisiontech.fvmobile.utility.Util;
import com.freevisiontech.fvmobile.utils.BlePtzParasConstant;
import com.freevisiontech.fvmobile.utils.Event;
import com.freevisiontech.fvmobile.utils.EventBusUtil;
import com.umeng.analytics.MobclickAgent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FVPtzBatteryInfoActivity extends BaseActivity {
    private boolean connected = false;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    @Bind({2131755249})
    ImageView img_back;
    @Bind({2131756129})
    ImageView img_right;
    private Runnable runnable = new Runnable() {
        public void run() {
            FVPtzBatteryInfoActivity.this.getPtzBatteryInfo();
        }
    };
    @Bind({2131755502})
    TextView textChargePercentage;
    @Bind({2131755501})
    TextView textCurrentCapacity;
    @Bind({2131755500})
    TextView textDesignCapacity;
    @Bind({2131755506})
    TextView textElectricCurrent;
    @Bind({2131755504})
    TextView textLifePercentage;
    @Bind({2131755503})
    TextView textNumDischarges;
    @Bind({2131755505})
    TextView textTemperature;
    @Bind({2131755507})
    TextView textVoltage;
    @Bind({2131756127})
    TextView tv_center_title;
    @Bind({2131756128})
    TextView tv_right;
    private BluetoothGattCharacteristic writeCharacteristic;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0853R.layout.activity_ptz_battery_info);
        getWindow().setFlags(1024, 1024);
        Util.hideBottomUIMenu(this);
        ButterKnife.bind((Activity) this);
        initTitle();
        initView();
        initData();
        Boolean isConnected = Boolean.valueOf(ViseBluetooth.getInstance().isConnected());
        if (CameraUtils.getCurrentPageIndex() == 2 && isConnected.booleanValue()) {
            CameraUtils.setFrameLayerNumber(25);
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean isRegisterEventBus() {
        return true;
    }

    /* access modifiers changed from: protected */
    public void receiveEvent(Event event) {
        switch (event.getCode()) {
            case 34:
                setPtzParamatersText();
                return;
            default:
                return;
        }
    }

    private void initData() {
        this.connected = ViseBluetooth.getInstance().isConnected();
        if (this.connected) {
            this.handler.post(this.runnable);
        }
    }

    /* access modifiers changed from: private */
    public void getPtzBatteryInfo() {
        this.textDesignCapacity.setText(BlePtzParasConstant.GET_PTZ_BATTERY_DESIGN_CAPACITY + "mAh");
        this.textChargePercentage.setText(BlePtzParasConstant.GET_PTZ_BATTERY_SURPLUS_CAPACITY_PERCENTAGE + "%");
        this.textTemperature.setText(BlePtzParasConstant.GET_PTZ_BATTERY_TEMPERATURE + "℃");
        this.textLifePercentage.setText(BlePtzParasConstant.GET_PTZ_BATTERY_SURPLUS_LIFE_PERCENTAGE + "%");
        this.textCurrentCapacity.setText(BlePtzParasConstant.GET_PTZ_BATTERY_SURPLUS_CAPACITY + "mAh");
        this.textElectricCurrent.setText(BlePtzParasConstant.GET_PTZ_BATTERY_CAPACITY + "mA");
        this.textNumDischarges.setText(BlePtzParasConstant.GET_PTZ_BATTERY_CIRCULATION_COUNT + "" + getString(C0853R.string.label_battery_count));
        this.textVoltage.setText(BlePtzParasConstant.GET_PTZ_BATTERY_VOLTAGE + "mV");
        this.handler.postDelayed(this.runnable, 1000);
    }

    private void setPtzParamatersText() {
        this.textDesignCapacity.setText("");
        this.textChargePercentage.setText("");
        this.textTemperature.setText("");
        this.textLifePercentage.setText("");
        this.textCurrentCapacity.setText("");
        this.textElectricCurrent.setText("");
        this.textNumDischarges.setText("");
        this.textVoltage.setText("");
    }

    private void initView() {
    }

    private void initTitle() {
        this.img_back.setVisibility(0);
        this.img_back.setImageResource(C0853R.mipmap.fanhui);
        this.tv_center_title.setVisibility(0);
        this.tv_center_title.setText(getResources().getString(C0853R.string.ptz_battery_info));
        this.tv_right.setVisibility(8);
        this.img_right.setVisibility(8);
        this.img_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FVPtzBatteryInfoActivity.this.finish();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onModeSwitch(FVModeSelectEvent fvModeSelectEvent) {
        switch (fvModeSelectEvent.getMode()) {
            case Constants.LABEL_SETTING_OK_TOP_BAR_UP_OR_DOWN_210 /*107708*/:
                if (CameraUtils.getFrameLayerNumber() == 25) {
                }
                return;
            case Constants.LABEL_SETTING_RETURN_KEY_210 /*107709*/:
                if (CameraUtils.getFrameLayerNumber() == 25) {
                    finish();
                    return;
                }
                return;
            case Constants.LABEL_SETTING_ROCKING_BAR_UP_210 /*107710*/:
                if (CameraUtils.getFrameLayerNumber() == 25) {
                }
                return;
            case Constants.LABEL_SETTING_ROCKING_BAR_DOWN_210 /*107711*/:
                if (CameraUtils.getFrameLayerNumber() == 25) {
                }
                return;
            case Constants.LABEL_SETTING_LONG_RETURN_KEY_210 /*107718*/:
                if (CameraUtils.getFrameLayerNumber() == 25) {
                    finish();
                    CameraUtils.setFrameLayerNumber(0);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.handler != null) {
            this.handler.removeCallbacks(this.runnable);
        }
        EventBusUtil.unregister(this);
        ButterKnife.unbind(this);
        if (CameraUtils.getCurrentPageIndex() == 2) {
            if (EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().unregister(this);
            }
            CameraUtils.setFrameLayerNumber(22);
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
