package com.freevisiontech.fvmobile.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.p003v7.widget.LinearLayoutManager;
import android.support.p003v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.freevisiontech.cameralib.FVCameraManager;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.activity.FVMainActivity;
import com.freevisiontech.fvmobile.base.recyclerview.RecycleViewDivider;
import com.freevisiontech.fvmobile.bean.SceneModeBean;
import com.freevisiontech.fvmobile.fragment.FVContentFragment;
import com.freevisiontech.fvmobile.utility.Util;
import com.freevisiontech.utils.ScreenOrientationUtil;
import java.util.ArrayList;
import java.util.List;

public class FVCameraSceneModeShortcut extends FVCameraShortcutPopNext {
    private final int CAMERA_POPUP_HUN_EIGHTY = 12;
    private final int CAMERA_POPUP_NINTY = 11;
    private final int CAMERA_POPUP_TWO_SERTY = 13;
    private final int CAMERA_POPUP_ZARO = 10;
    private OrientationBroadPopup broad;
    /* access modifiers changed from: private */
    public FVCameraManager cameraManager;
    private Context context;
    private List<SceneModeBean> datas;
    private int height;
    private LinearLayout layout_camera_shortcut_pop_horizontal_bottom;
    private LinearLayout layout_camera_shortcut_pop_int_linear;
    private LinearLayout layout_camera_shortcut_pop_out_linear;
    private LinearLayout layout_camera_shortcut_pop_vertical_bottom_top;
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    Log.e("------------", "--------收到消息 0-----");
                    FVCameraSceneModeShortcut.this.setHorUiZero();
                    return;
                case 11:
                    Log.e("------------", "--------收到消息  90-------------");
                    FVCameraSceneModeShortcut.this.setHorUiNinety();
                    return;
                case 12:
                    Log.e("------------", "--------收到消息  180-------------");
                    FVCameraSceneModeShortcut.this.setHorUiZero180();
                    return;
                case 13:
                    Log.e("------------", "--------收到消息  270-------------");
                    FVCameraSceneModeShortcut.this.setHorUiNinety270();
                    return;
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public PopupWindow pop;
    private RecyclerView rv_scene_mode_shortcut;
    private int selectPosition = -1;
    private View view;

    public void init(Context context2, List<SceneModeBean> datas2) {
        this.context = context2;
        this.cameraManager = ((FVContentFragment) ((FVMainActivity) context2).getSupportFragmentManager().findFragmentByTag("contentFragment")).getCameraManager();
        this.view = LayoutInflater.from(context2).inflate(C0853R.layout.layout_scene_mode_shortcut, (ViewGroup) null);
        this.datas = datas2;
        View btnBack = this.view.findViewById(C0853R.C0855id.btn_back);
        btnBack.setVisibility(0);
        btnBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (FVCameraSceneModeShortcut.this.pop != null) {
                    FVCameraSceneModeShortcut.this.pop.dismiss();
                    Util.sendIntEventMessge(10007);
                }
            }
        });
        this.rv_scene_mode_shortcut = (RecyclerView) this.view.findViewById(C0853R.C0855id.rv_scene_mode_shortcut);
        this.rv_scene_mode_shortcut.addItemDecoration(new RecycleViewDivider(context2, 0));
        this.rv_scene_mode_shortcut.setLayoutManager(new LinearLayoutManager(context2));
        this.rv_scene_mode_shortcut.setAdapter(new SceneModeAdapter(context2, datas2));
        this.height = Util.getDeviceSize(context2).y - Util.dip2px(context2, 30.0f);
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
                if (FVCameraSceneModeShortcut.this.pop != null) {
                    FVCameraSceneModeShortcut.this.pop.dismiss();
                }
            }
        });
        this.layout_camera_shortcut_pop_vertical_bottom_top.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (FVCameraSceneModeShortcut.this.pop != null) {
                    FVCameraSceneModeShortcut.this.pop.dismiss();
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
                FVCameraSceneModeShortcut.this.sendToHandler(10);
            } else if (orientation == 90) {
                FVCameraSceneModeShortcut.this.sendToHandler(11);
            } else if (orientation == 180) {
                FVCameraSceneModeShortcut.this.sendToHandler(12);
            } else if (orientation == 270) {
                FVCameraSceneModeShortcut.this.sendToHandler(13);
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
        this.layout_camera_shortcut_pop_horizontal_bottom.setVisibility(8);
        this.layout_camera_shortcut_pop_vertical_bottom_top.setVisibility(0);
        this.layout_camera_shortcut_pop_out_linear.setOrientation(0);
        LinearLayout.LayoutParams linearParamsAll = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_out_linear.getLayoutParams();
        linearParamsAll.height = this.height;
        linearParamsAll.width = this.height;
        this.layout_camera_shortcut_pop_out_linear.setLayoutParams(linearParamsAll);
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_int_linear.getLayoutParams();
        linearParams.height = this.height;
        linearParams.width = (this.height * 2) / 3;
        this.layout_camera_shortcut_pop_int_linear.setLayoutParams(linearParams);
        LinearLayout.LayoutParams linearParams2 = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_horizontal_bottom.getLayoutParams();
        linearParams2.height = this.height;
        linearParams2.width = this.height / 3;
        this.layout_camera_shortcut_pop_horizontal_bottom.setLayoutParams(linearParams2);
        LinearLayout.LayoutParams linearParams3 = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_vertical_bottom_top.getLayoutParams();
        linearParams3.height = this.height;
        linearParams3.width = this.height / 3;
        this.layout_camera_shortcut_pop_vertical_bottom_top.setLayoutParams(linearParams3);
        this.layout_camera_shortcut_pop_out_linear.setRotation(180.0f);
    }

    /* access modifiers changed from: private */
    public void setHorUiZero() {
        this.layout_camera_shortcut_pop_horizontal_bottom.setVisibility(0);
        this.layout_camera_shortcut_pop_vertical_bottom_top.setVisibility(8);
        this.layout_camera_shortcut_pop_out_linear.setOrientation(0);
        LinearLayout.LayoutParams linearParamsAll = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_out_linear.getLayoutParams();
        linearParamsAll.height = this.height;
        linearParamsAll.width = this.height;
        this.layout_camera_shortcut_pop_out_linear.setLayoutParams(linearParamsAll);
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_int_linear.getLayoutParams();
        linearParams.height = this.height;
        linearParams.width = (this.height * 2) / 3;
        this.layout_camera_shortcut_pop_int_linear.setLayoutParams(linearParams);
        LinearLayout.LayoutParams linearParams2 = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_horizontal_bottom.getLayoutParams();
        linearParams2.height = this.height;
        linearParams2.width = this.height / 3;
        this.layout_camera_shortcut_pop_horizontal_bottom.setLayoutParams(linearParams2);
        this.layout_camera_shortcut_pop_out_linear.setRotation(0.0f);
    }

    /* access modifiers changed from: private */
    public void setHorUiNinety() {
        this.layout_camera_shortcut_pop_horizontal_bottom.setVisibility(0);
        this.layout_camera_shortcut_pop_vertical_bottom_top.setVisibility(8);
        this.layout_camera_shortcut_pop_out_linear.setOrientation(1);
        LinearLayout.LayoutParams linearParamsAll = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_out_linear.getLayoutParams();
        linearParamsAll.height = this.height;
        linearParamsAll.width = this.height;
        this.layout_camera_shortcut_pop_out_linear.setLayoutParams(linearParamsAll);
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_int_linear.getLayoutParams();
        linearParams.height = (this.height * 2) / 3;
        linearParams.width = this.height;
        this.layout_camera_shortcut_pop_int_linear.setLayoutParams(linearParams);
        LinearLayout.LayoutParams linearParams2 = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_horizontal_bottom.getLayoutParams();
        linearParams2.height = this.height / 3;
        linearParams2.width = this.height;
        this.layout_camera_shortcut_pop_horizontal_bottom.setLayoutParams(linearParams2);
        this.layout_camera_shortcut_pop_out_linear.setRotation(-90.0f);
    }

    /* access modifiers changed from: private */
    public void setHorUiNinety270() {
        this.layout_camera_shortcut_pop_horizontal_bottom.setVisibility(8);
        this.layout_camera_shortcut_pop_vertical_bottom_top.setVisibility(0);
        this.layout_camera_shortcut_pop_out_linear.setOrientation(1);
        LinearLayout.LayoutParams linearParamsAll = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_out_linear.getLayoutParams();
        linearParamsAll.height = this.height;
        linearParamsAll.width = this.height;
        this.layout_camera_shortcut_pop_out_linear.setLayoutParams(linearParamsAll);
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_int_linear.getLayoutParams();
        linearParams.height = (this.height * 2) / 3;
        linearParams.width = this.height;
        this.layout_camera_shortcut_pop_int_linear.setLayoutParams(linearParams);
        LinearLayout.LayoutParams linearParams2 = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_horizontal_bottom.getLayoutParams();
        linearParams2.height = this.height / 3;
        linearParams2.width = this.height;
        this.layout_camera_shortcut_pop_horizontal_bottom.setLayoutParams(linearParams2);
        LinearLayout.LayoutParams linearParams3 = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_vertical_bottom_top.getLayoutParams();
        linearParams3.height = this.height / 3;
        linearParams3.width = this.height;
        this.layout_camera_shortcut_pop_vertical_bottom_top.setLayoutParams(linearParams3);
        this.layout_camera_shortcut_pop_out_linear.setRotation(90.0f);
    }

    /* access modifiers changed from: private */
    public void changeSceneMode(String sceneMode) {
        this.cameraManager.setSceneMode(sceneMode);
    }

    public void setPop(PopupWindow pop2) {
        this.pop = pop2;
    }

    public void unRegisterListener() {
        if (this.broad != null) {
            this.context.unregisterReceiver(this.broad);
        }
    }

    public View getView() {
        return this.view;
    }

    public class SceneModeAdapter extends RecyclerView.Adapter<SceneModeHolder> {
        /* access modifiers changed from: private */
        public List<SceneModeBean> datas = new ArrayList();
        private Context mContext;

        public SceneModeAdapter(Context context, List<SceneModeBean> datas2) {
            this.datas = datas2;
            this.mContext = context;
        }

        public SceneModeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new SceneModeHolder(LayoutInflater.from(this.mContext).inflate(C0853R.layout.scene_mode_item, (ViewGroup) null));
        }

        public void onBindViewHolder(SceneModeHolder holder, int position) {
            SceneModeBean curCameraSecondItem = this.datas.get(position);
            holder.tv_scene_mode_name.setText(curCameraSecondItem.getSceneName());
            holder.iv_scene_mode_icon.setImageResource(curCameraSecondItem.getResourceId());
            String unused = holder.sceneMode = curCameraSecondItem.getSceneMode();
            int unused2 = holder.position = position;
            if (FVCameraSceneModeShortcut.this.cameraManager.getSceneMode().equals(holder.sceneMode)) {
                holder.btn_select.setVisibility(0);
                curCameraSecondItem.setChecked(true);
                return;
            }
            holder.btn_select.setVisibility(4);
            curCameraSecondItem.setChecked(false);
        }

        public int getItemCount() {
            return this.datas.size();
        }

        public class SceneModeHolder extends RecyclerView.ViewHolder {
            /* access modifiers changed from: private */
            public ImageView btn_select;
            /* access modifiers changed from: private */
            public ImageView iv_scene_mode_icon;
            /* access modifiers changed from: private */
            public int position;
            /* access modifiers changed from: private */
            public String sceneMode;
            /* access modifiers changed from: private */
            public TextView tv_scene_mode_name;

            public SceneModeHolder(View itemView) {
                super(itemView);
                this.iv_scene_mode_icon = (ImageView) itemView.findViewById(C0853R.C0855id.iv_scene_mode_icon);
                this.tv_scene_mode_name = (TextView) itemView.findViewById(C0853R.C0855id.tv_scene_mode_name);
                this.btn_select = (ImageView) itemView.findViewById(C0853R.C0855id.btn_select);
                itemView.setOnClickListener(new View.OnClickListener(SceneModeAdapter.this) {
                    public void onClick(View v) {
                        ((SceneModeBean) SceneModeAdapter.this.datas.get(SceneModeHolder.this.position)).setChecked(true);
                        FVCameraSceneModeShortcut.this.changeSceneMode(SceneModeHolder.this.sceneMode);
                        SceneModeAdapter.this.notifyDataSetChanged();
                    }
                });
            }
        }
    }
}
