package com.freevisiontech.fvmobile.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.p001v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.ViseBluetooth;
import com.freevisiontech.fvmobile.activity.FVAboutAppActivity;
import com.freevisiontech.fvmobile.activity.FVAboutPtzActivity;
import com.freevisiontech.fvmobile.activity.FVHelpActivity;
import com.freevisiontech.fvmobile.activity.FVPtzBatteryInfoActivity;
import com.freevisiontech.fvmobile.application.MyApplication;
import com.freevisiontech.fvmobile.bean.FVModeSelectEvent;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utility.Constants;
import com.freevisiontech.fvmobile.utility.Util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FVAdvancedSetInfoFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "AdvancedSetInfo";
    /* access modifiers changed from: private */
    public List itemVisibleSort;
    private Context mContext;
    private String mPtzHelpType = "";
    @Bind({2131755961})
    RelativeLayout rl_about_app;
    @Bind({2131755955})
    RelativeLayout rl_about_ptz;
    @Bind({2131755953})
    RelativeLayout rl_battery_info;
    @Bind({2131755957})
    RelativeLayout rl_help;
    @Bind({2131755963})
    RelativeLayout rl_live;
    @Bind({2131755959})
    RelativeLayout rl_teach_video;
    private Runnable runnable = new Runnable() {
        public void run() {
            if (FVAdvancedSetInfoFragment.this.scrollview != null) {
                FVAdvancedSetInfoFragment.this.scrollview.scrollTo(0, FVAdvancedSetInfoFragment.this.stirPosition * 150);
            }
        }
    };
    private boolean scaleSlide = false;
    @Bind({2131755203})
    ScrollView scrollview;
    /* access modifiers changed from: private */
    public int stirPosition = -1;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(C0853R.layout.layout_advance_set_info, container, false);
        ButterKnife.bind((Object) this, view);
        this.mContext = getActivity();
        this.mPtzHelpType = MyApplication.CURRENT_PTZ_TYPE;
        initView();
        this.itemVisibleSort = new ArrayList(Arrays.asList(new String[]{"rl_battery_info", "rl_about_ptz", "rl_help", "rl_teach_video", "rl_about_app"}));
        CameraUtils.setFrameLayerNumber(22);
        Boolean isConnected = Boolean.valueOf(ViseBluetooth.getInstance().isConnected());
        if (CameraUtils.getCurrentPageIndex() == 2 && isConnected.booleanValue()) {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    int unused = FVAdvancedSetInfoFragment.this.stirPosition = 0;
                    FVAdvancedSetInfoFragment.this.setControlOnClickItemBlackgroundColor(FVAdvancedSetInfoFragment.this.controlItemStringToView(FVAdvancedSetInfoFragment.this.itemVisibleSort.get(FVAdvancedSetInfoFragment.this.stirPosition).toString()));
                }
            }, 100);
        }
        return view;
    }

    private void initView() {
        this.rl_battery_info.setOnClickListener(this);
        this.rl_about_ptz.setOnClickListener(this);
        this.rl_about_app.setOnClickListener(this);
        this.rl_live.setOnClickListener(this);
        this.rl_help.setOnClickListener(this);
        this.rl_teach_video.setOnClickListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onModeSwitch(FVModeSelectEvent fvModeSelectEvent) {
        switch (fvModeSelectEvent.getMode()) {
            case Constants.LABEL_SETTING_OK_TOP_BAR_UP_OR_DOWN_210 /*107708*/:
                if (CameraUtils.getFrameLayerNumber() == 22) {
                    Log.e("-----------------", "----------  7777  8888  9999   -------  OK 键  OK键  OK键  OK键" + this.stirPosition);
                    setControlOnClickItemDown(controlItemStringToSeekBarView(this.itemVisibleSort.get(this.stirPosition).toString()));
                    return;
                }
                return;
            case Constants.LABEL_SETTING_ROCKING_BAR_UP_210 /*107710*/:
                if (CameraUtils.getFrameLayerNumber() == 22) {
                    this.stirPosition--;
                    if (this.stirPosition < 0) {
                        this.stirPosition = 0;
                    }
                    Log.e("-----------------", "----------  7777  8888  9999   -------  210 摇杆拨动向上   向上   向上 " + this.stirPosition);
                    setControlOnClickItemBlackgroundColor(controlItemStringToView(this.itemVisibleSort.get(this.stirPosition).toString()));
                    return;
                }
                return;
            case Constants.LABEL_SETTING_ROCKING_BAR_DOWN_210 /*107711*/:
                if (CameraUtils.getFrameLayerNumber() == 22) {
                    this.stirPosition++;
                    if (this.stirPosition > this.itemVisibleSort.size() - 1) {
                        this.stirPosition = this.itemVisibleSort.size() - 1;
                    }
                    Log.e("-----------------", "----------  7777  8888  9999   -------  摇杆拨动向下   向下   向下" + this.stirPosition);
                    setControlOnClickItemBlackgroundColor(controlItemStringToView(this.itemVisibleSort.get(this.stirPosition).toString()));
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void setControlOnClickItemDown(View view) {
        if (view == this.rl_battery_info) {
            startActivity(new Intent(getActivity(), FVPtzBatteryInfoActivity.class));
        } else if (view == this.rl_about_ptz) {
            startActivity(new Intent(getActivity(), FVAboutPtzActivity.class));
        } else if (view == this.rl_help) {
            Intent helpIntent = new Intent(getActivity(), FVHelpActivity.class);
            helpIntent.putExtra("help_ptz_type", this.mPtzHelpType);
            startActivity(helpIntent);
        } else if (view == this.rl_teach_video) {
            openBrowser(getActivity(), "");
        } else if (view == this.rl_about_app) {
            startActivity(new Intent(getActivity(), FVAboutAppActivity.class));
        }
    }

    public View controlItemStringToSeekBarView(String str) {
        if (str.equals("rl_battery_info")) {
            return this.rl_battery_info;
        }
        if (str.equals("rl_about_ptz")) {
            return this.rl_about_ptz;
        }
        if (str.equals("rl_help")) {
            return this.rl_help;
        }
        if (str.equals("rl_teach_video")) {
            return this.rl_teach_video;
        }
        return this.rl_about_app;
    }

    public void setControlOnClickItemBlackgroundColor(View view) {
        this.rl_battery_info.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_about_ptz.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_help.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_teach_video.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_about_app.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        view.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.black15));
        new Handler().postDelayed(this.runnable, 200);
    }

    public View controlItemStringToView(String str) {
        if (str.equals("rl_battery_info")) {
            return this.rl_battery_info;
        }
        if (str.equals("rl_about_ptz")) {
            return this.rl_about_ptz;
        }
        if (str.equals("rl_help")) {
            return this.rl_help;
        }
        if (str.equals("rl_teach_video")) {
            return this.rl_teach_video;
        }
        return this.rl_about_app;
    }

    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0853R.C0855id.rl_battery_info:
                startActivity(new Intent(getActivity(), FVPtzBatteryInfoActivity.class));
                return;
            case C0853R.C0855id.rl_about_ptz:
                startActivity(new Intent(getActivity(), FVAboutPtzActivity.class));
                return;
            case C0853R.C0855id.rl_help:
                Intent helpIntent = new Intent(getActivity(), FVHelpActivity.class);
                helpIntent.putExtra("help_ptz_type", this.mPtzHelpType);
                startActivity(helpIntent);
                return;
            case C0853R.C0855id.rl_teach_video:
                openBrowser(getActivity(), "");
                return;
            case C0853R.C0855id.rl_about_app:
                startActivity(new Intent(getActivity(), FVAboutAppActivity.class));
                return;
            case C0853R.C0855id.rl_live:
                Toast.makeText(getActivity(), getActivity().getResources().getString(C0853R.string.live_not_open), 0).show();
                return;
            default:
                return;
        }
    }

    public static void openBrowser(Context context, String url) {
        String url2;
        if (!Util.isZh(context)) {
            url2 = "http://www.freevisiontech.com/oss/help/SelectModelEnglish.html";
        } else if (Util.isZhFanTi(context)) {
            url2 = "http://www.freevisiontech.com/oss/help/SelectModelFanti.html";
        } else {
            url2 = "http://www.freevisiontech.com/oss/help/SelectModel.html";
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setData(Uri.parse(url2));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            ComponentName resolveActivity = intent.resolveActivity(context.getPackageManager());
            context.startActivity(Intent.createChooser(intent, context.getString(C0853R.string.label_browser_select)));
            return;
        }
        Toast.makeText(context.getApplicationContext(), context.getString(C0853R.string.label_browser_down), 0).show();
    }
}
