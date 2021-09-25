package com.freevisiontech.fvmobile.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import com.freevisiontech.fvmobile.application.MyApplication;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.umeng.analytics.pro.C0217dk;
import com.vise.log.ViseLog;
import java.util.HashMap;
import java.util.Map;

public class BleNotifyDataUtil {
    private static BleNotifyDataUtil bleNotifyDataUtil;
    private static Map<Byte, Boolean> changeMap;
    private static Map<String, Integer> map;
    private Context context;
    private String mPtzType = "";
    private byte[] notifyData;

    public static BleNotifyDataUtil getInstance() {
        if (bleNotifyDataUtil == null) {
            synchronized (BleNotifyDataUtil.class) {
                if (bleNotifyDataUtil == null) {
                    bleNotifyDataUtil = new BleNotifyDataUtil();
                    createMap();
                }
            }
        }
        return bleNotifyDataUtil;
    }

    public void init(Context context2) {
        if (this.context == null) {
            this.context = context2;
            this.mPtzType = MyApplication.CURRENT_PTZ_TYPE;
        }
    }

    public void destroy() {
        if (bleNotifyDataUtil != null) {
            bleNotifyDataUtil = null;
        }
    }

    public static void createMap() {
        ViseLog.m1466e("createMap");
        map = new HashMap();
        changeMap = new HashMap();
        map.put(BleConstant.SHUTTER, 0);
        map.put(BleConstant.ISO, 0);
        map.put(BleConstant.f1095WB, 0);
        map.put(BleConstant.FOCUS, 0);
        map.put("5", 0);
        map.put("6", 0);
        map.put("7", 0);
        map.put("8", 0);
        map.put("9", 0);
        map.put("10", 0);
        map.put("11", 0);
        map.put("12", 0);
        map.put("13", 0);
        map.put("14", 0);
        map.put("15", 0);
        map.put("16", 0);
    }

    public static void resetMap() {
        ViseLog.m1466e("resetMap");
        map.clear();
        map.put(BleConstant.SHUTTER, 0);
        map.put(BleConstant.ISO, 0);
        map.put(BleConstant.f1095WB, 0);
        map.put(BleConstant.FOCUS, 0);
        map.put("5", 0);
        map.put("6", 0);
        map.put("7", 0);
        map.put("8", 0);
        map.put("9", 0);
        map.put("10", 0);
        map.put("11", 0);
        map.put("12", 0);
        map.put("13", 0);
        map.put("14", 0);
        map.put("15", 0);
        map.put("16", 0);
    }

    public boolean setStatuChange(int key, int result) {
        if (map == null || !map.containsKey(String.valueOf(key))) {
            return false;
        }
        if (map.get(String.valueOf(key)).intValue() != 0 || result != 1) {
            return false;
        }
        map.put(String.valueOf(key), Integer.valueOf(result));
        return true;
    }

    public Map<Byte, Boolean> setPtzStatusSyncNotifyData(byte[] value) {
        if (value == null) {
            return null;
        }
        if ((value[0] & 255) == 85) {
            if ((value[1] & 255) == 1) {
                BlePtzParasConstant.SYNC_PTZ_DORMANCY_SWITCH = value[2] & 255;
            } else if ((value[1] & 255) == 2) {
                BlePtzParasConstant.SYNC_PTZ_STABILIZE_STATU = value[2] & 255;
            } else if ((value[1] & 255) == 3) {
                BlePtzParasConstant.SYNC_PTZ_WORK_STATU = value[2] & 255;
            } else if ((value[1] & 255) == 4) {
                BlePtzParasConstant.SYNC_PTZ_HAND_BREAK_MODE_NOT_OPEN = value[2] & 255;
            } else if ((value[1] & 255) == 5) {
                BlePtzParasConstant.SYNC_PTZ_REACH_LIMIT_AREA = value[2] & 255;
            } else if ((value[1] & 255) == 6) {
                BlePtzParasConstant.SYNC_PTZ_LONGTIME_NO_OPERATION = value[2] & 255;
            } else if ((value[1] & 255) == 7) {
                BlePtzParasConstant.SYNC_PTZ_FOLDING_PATTERNS = value[2] & 255;
            } else if ((value[1] & 255) == 8) {
                BlePtzParasConstant.SYNC_PTZ_PITCH_MOTOR_INIT_FAIL = value[2] & 255;
            } else if ((value[1] & 255) == 9) {
                BlePtzParasConstant.SYNC_PTZ_ROLL_MOTOR_INIT_FAIL = value[2] & 255;
            } else if ((value[1] & 255) == 10) {
                BlePtzParasConstant.SYNC_PTZ_YAW_MOTOR_INIT_FAIL = value[2] & 255;
            } else if ((value[1] & 255) == 11) {
                BlePtzParasConstant.SYNC_PTZ_BUSY_STATU = value[2] & 255;
            } else if ((value[1] & 255) == 12) {
                BlePtzParasConstant.SYNC_PTZ_CHECK_NO_PHONE = value[2] & 255;
            } else if ((value[1] & 255) == 13) {
                BlePtzParasConstant.SYNC_PTZ_STATU_LIGHT_EXCEPTION_GLITTER = value[2] & 255;
            } else if ((value[1] & 255) == 14) {
                BlePtzParasConstant.SYNC_PTZ_IMU_UNCALIBRAED = value[2] & 255;
            }
            boolean statuChange = setStatuChange(value[1] & 255, value[2] & 255);
            if (changeMap != null) {
                changeMap.clear();
                changeMap.put(Byte.valueOf(value[1]), Boolean.valueOf(statuChange));
            }
        }
        return changeMap;
    }

    public byte setPtzSettingParametersNotifyData(byte[] value) {
        if (value == null) {
            return 0;
        }
        if ((value[0] & 255) != 165 && (value[0] & 255) != 90) {
            return 0;
        }
        if ((value[1] & 255) == 4) {
            BlePtzParasConstant.SET_PTZ_RESET_TO_DEFAULT = value[2] & 255;
            return 4;
        } else if ((value[1] & 255) == 5) {
            BlePtzParasConstant.SET_PTZ_FOLLOW_MODE = value[2] & 255;
            return 5;
        } else if ((value[1] & 255) == 9) {
            BlePtzParasConstant.SET_PITCH_OF_FOLLOW_SPEED_SETTING_FOR = value[2] & 255;
            BlePtzParasConstant.SET_ROLL_OF_FOLLOW_SPEED_SETTING_FOR = value[3] & 255;
            BlePtzParasConstant.SET_YAW_OF_FOLLOW_SPEED_SETTING_FOR = value[4] & 255;
            return 9;
        } else if ((value[1] & 255) == 10) {
            BlePtzParasConstant.SET_PITCH_OF_PTZ_FOLLOW_DEADBAND = value[2] & 255;
            BlePtzParasConstant.SET_ROLL_OF_PTZ_FOLLOW_DEADBAND = value[3] & 255;
            BlePtzParasConstant.SET_YAW_OF_PTZ_FOLLOW_DEADBAND = value[4] & 255;
            return 10;
        } else if ((value[1] & 255) == 11) {
            BlePtzParasConstant.SET_PITCH_OF_PTZ_ATTITUDE_ANGLE_FOR_FINE_TUNING = value[2];
            BlePtzParasConstant.SET_ROLL_OF_PTZ_ATTITUDE_ANGLE_FOR_FINE_TUNING = value[3];
            BlePtzParasConstant.SET_YAW_OF_PTZ_ATTITUDE_ANGLE_FOR_FINE_TUNING = value[4];
            return 11;
        } else if ((value[1] & 255) == 12) {
            BlePtzParasConstant.SET_PITCH_OF_ROCKER_FOR_DEAD_ZONE = value[2] & 255;
            BlePtzParasConstant.SET_ROLL_OF_ROCKER_FOR_DEAD_ZONE = value[3] & 255;
            BlePtzParasConstant.SET_YAW_OF_ROCKER_FOR_DEAD_ZONE = value[4] & 255;
            return 12;
        } else if ((value[1] & 255) == 13) {
            BlePtzParasConstant.SET_PITCH_OF_ROCKER_FOR_SPEED = value[2] & 255;
            BlePtzParasConstant.SET_ROLL_OF_ROCKER_FOR_SPEED = value[3] & 255;
            BlePtzParasConstant.SET_YAW_OF_ROCKER_FOR_SPEED = value[4] & 255;
            return C0217dk.f721k;
        } else if ((value[1] & 255) == 14) {
            BlePtzParasConstant.SET_PITCH_OF_ROCKER_FOR_SENSITIVITY_CURVE = value[2] & 255;
            BlePtzParasConstant.SET_ROLL_OF_ROCKER_FOR_SENSITIVITY_CURVE = value[3] & 255;
            BlePtzParasConstant.SET_YAW_OF_ROCKER_FOR_SENSITIVITY_CURVE = value[4] & 255;
            return C0217dk.f722l;
        } else if ((value[1] & 255) == 15) {
            BlePtzParasConstant.SET_UP_OR_DOWN_OF_ROCKER_FOR_ORIENTATION = value[2] & 255;
            BlePtzParasConstant.SET_LEFT_OR_RIGHT_OF_ROCKER_FOR_ORIENTATION = value[3] & 255;
            return C0217dk.f723m;
        } else if ((value[1] & 255) == 16) {
            BlePtzParasConstant.SET_MOTOR_DORMANCY_SWITCH = value[2] & 255;
            return C0217dk.f724n;
        } else if ((value[1] & 255) == 17) {
            BlePtzParasConstant.SET_SILENT_MODE_SWITCH = value[2] & 255;
            return ClosedCaptionCtrl.MID_ROW_CHAN_1;
        } else if ((value[1] & 255) == 18) {
            BlePtzParasConstant.SET_MANUAL_MODE_SWITCH = value[2] & 255;
            return 18;
        } else if ((value[1] & 255) == 19) {
            BlePtzParasConstant.SET_PTZ_SELF_TIMER_SWITCH = value[2] & 255;
            return 19;
        } else if ((value[1] & 255) == 20) {
            BlePtzParasConstant.SET_PTZ_ATTITUDE_RESET_SWITCH = value[2] & 255;
            return ClosedCaptionCtrl.MISC_CHAN_1;
        } else if ((value[1] & 255) == 21) {
            BlePtzParasConstant.SET_SELF_FOLLOW_MODE_SWITCH = value[2] & 255;
            return 21;
        } else if ((value[1] & 255) == 22) {
            BlePtzParasConstant.SET_PHONE_CHECK_SWICTH = value[2] & 255;
            return 22;
        } else if ((value[1] & 255) == 23) {
            BlePtzParasConstant.SET_PTZ_CALIBRATION_SWITCH = value[2] & 255;
            return ClosedCaptionCtrl.TAB_OFFSET_CHAN_1;
        } else if ((value[1] & 255) == 24) {
            BlePtzParasConstant.SET_PHONE_BATTERY_CHARGING_SWITCH = value[2] & 255;
            return 24;
        } else if ((value[1] & 255) == 25) {
            BlePtzParasConstant.SET_CONTEXTUAL_MODEL = value[2] & 255;
            return ClosedCaptionCtrl.MID_ROW_CHAN_2;
        } else if ((value[1] & 255) == 34) {
            return 34;
        } else {
            if ((value[1] & 255) == 35) {
                return 35;
            }
            if ((value[1] & 255) == 36) {
                return 36;
            }
            if ((value[1] & 255) == 37) {
                return ClosedCaptionCtrl.ROLL_UP_CAPTIONS_2_ROWS;
            }
            if ((value[1] & 255) == 38) {
                return ClosedCaptionCtrl.ROLL_UP_CAPTIONS_3_ROWS;
            }
            if ((value[1] & 255) == 39) {
                return ClosedCaptionCtrl.ROLL_UP_CAPTIONS_4_ROWS;
            }
            if ((value[1] & 255) == 40) {
                return 40;
            }
            if ((value[1] & 255) == 49) {
                if (this.mPtzType.equals(BleConstant.FM_300)) {
                    BlePtzParasConstant.SET_FM300_KALEIDOSCOPE_MODE_SWITCH = value[2] & 255;
                } else {
                    BlePtzParasConstant.SET_ROLL_OR_EXPOSURE_MODEL = value[2] & 255;
                }
                return 49;
            } else if ((value[1] & 255) != 86) {
                return 0;
            } else {
                BlePtzParasConstant.SET_FOCUS_KNOB_FUNCTION = value[2] & 255;
                BlePtzParasConstant.SET_THUMB_WHEEL_SELECTION = value[2] & 255;
                if (BlePtzParasConstant.SET_THUMB_WHEEL_SELECTION == 1) {
                    BlePtzParasConstant.SET_THUMB_WHEEL_SELECTION = 21;
                }
                return 86;
            }
        }
    }

    /* JADX WARNING: type inference failed for: r9v0, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r0v1, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v4, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v6, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v8, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v10, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v12, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v14, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v16, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v18, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v20, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v22, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v24, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v26, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v28, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v30, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v32, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v34, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v36, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v38, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v40, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v42, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v44, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v46, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v48, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v50, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v52, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v54, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v56, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v58, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v60, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v62, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v66, types: [byte, int] */
    /* JADX WARNING: type inference failed for: r0v68, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v71, types: [byte, int] */
    /* JADX WARNING: type inference failed for: r0v72, types: [byte, int] */
    /* JADX WARNING: type inference failed for: r0v73, types: [byte, int] */
    /* JADX WARNING: type inference failed for: r0v75, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v77, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v79, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v82, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v84, types: [byte, int] */
    /* JADX WARNING: type inference failed for: r0v85, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v88, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v91, types: [byte, int] */
    /* JADX WARNING: type inference failed for: r1v32, types: [byte, int] */
    /* JADX WARNING: type inference failed for: r0v96, types: [byte, int] */
    /* JADX WARNING: type inference failed for: r1v36, types: [byte, int] */
    /* JADX WARNING: type inference failed for: r0v101, types: [byte, int] */
    /* JADX WARNING: type inference failed for: r1v40, types: [byte, int] */
    /* JADX WARNING: type inference failed for: r0v106, types: [byte, int] */
    /* JADX WARNING: type inference failed for: r1v44, types: [byte, int] */
    /* JADX WARNING: type inference failed for: r0v111, types: [byte, int] */
    /* JADX WARNING: type inference failed for: r1v48, types: [byte, int] */
    /* JADX WARNING: type inference failed for: r0v116, types: [byte, int] */
    /* JADX WARNING: type inference failed for: r1v52, types: [byte, int] */
    /* JADX WARNING: type inference failed for: r0v123, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v125, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v127, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v130, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v133, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v136, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v139, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v142, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v146, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v148, types: [byte, int] */
    /* JADX WARNING: type inference failed for: r0v149, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v152, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v155, types: [byte, int] */
    /* JADX WARNING: type inference failed for: r1v56, types: [byte, int] */
    /* JADX WARNING: type inference failed for: r0v160, types: [byte, int] */
    /* JADX WARNING: type inference failed for: r1v60, types: [byte, int] */
    /* JADX WARNING: type inference failed for: r0v165, types: [byte, int] */
    /* JADX WARNING: type inference failed for: r1v64, types: [byte, int] */
    /* JADX WARNING: type inference failed for: r0v170, types: [byte, int] */
    /* JADX WARNING: type inference failed for: r1v68, types: [byte, int] */
    /* JADX WARNING: type inference failed for: r0v175, types: [byte, int] */
    /* JADX WARNING: type inference failed for: r1v72, types: [byte, int] */
    /* JADX WARNING: type inference failed for: r0v180, types: [byte, int] */
    /* JADX WARNING: type inference failed for: r1v76, types: [byte, int] */
    /* JADX WARNING: type inference failed for: r0v187, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v189, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v191, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v194, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v197, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v200, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v203, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v206, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v209, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v212, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v215, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v218, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v223, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v228, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v230, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v232, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v234, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v237, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v240, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v243, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v246, types: [byte, int] */
    /* JADX WARNING: type inference failed for: r0v248, types: [byte, int] */
    /* JADX WARNING: type inference failed for: r0v250, types: [byte, int] */
    /* JADX WARNING: type inference failed for: r0v252, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v255, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v258, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v261, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v264, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v267, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v270, types: [byte] */
    /* JADX WARNING: type inference failed for: r1v80, types: [byte] */
    /* JADX WARNING: type inference failed for: r1v81, types: [byte] */
    /* JADX WARNING: type inference failed for: r2v1, types: [byte] */
    /* JADX WARNING: type inference failed for: r3v1, types: [byte] */
    /* JADX WARNING: type inference failed for: r4v1, types: [byte] */
    /* JADX WARNING: type inference failed for: r5v1, types: [byte] */
    /* JADX WARNING: type inference failed for: r6v1, types: [byte] */
    /* JADX WARNING: type inference failed for: r1v82, types: [byte] */
    /* JADX WARNING: type inference failed for: r2v2, types: [byte] */
    /* JADX WARNING: type inference failed for: r3v2, types: [byte] */
    /* JADX WARNING: type inference failed for: r4v2, types: [byte] */
    /* JADX WARNING: type inference failed for: r5v2, types: [byte] */
    /* JADX WARNING: type inference failed for: r6v2, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v289, types: [byte] */
    /* JADX WARNING: type inference failed for: r1v83, types: [byte] */
    /* JADX WARNING: type inference failed for: r2v3, types: [byte] */
    /* JADX WARNING: type inference failed for: r3v3, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v292, types: [byte] */
    /* JADX WARNING: type inference failed for: r1v85, types: [byte] */
    /* JADX WARNING: type inference failed for: r2v5, types: [byte] */
    /* JADX WARNING: type inference failed for: r3v5, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v295, types: [byte] */
    /* JADX WARNING: type inference failed for: r1v87, types: [byte] */
    /* JADX WARNING: type inference failed for: r2v7, types: [byte] */
    /* JADX WARNING: type inference failed for: r3v7, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v298, types: [byte] */
    /* JADX WARNING: type inference failed for: r1v89, types: [byte] */
    /* JADX WARNING: type inference failed for: r2v9, types: [byte] */
    /* JADX WARNING: type inference failed for: r3v9, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v301, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v304, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v307, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v310, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v313, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v316, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v319, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v322, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v325, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v328, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v331, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v334, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v337, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v339, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v342, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v344, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v346, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v349, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v351, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v353, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v356, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v358, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v360, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v363, types: [byte, int] */
    /* JADX WARNING: type inference failed for: r0v364, types: [byte, int] */
    /* JADX WARNING: type inference failed for: r0v365, types: [byte, int] */
    /* JADX WARNING: type inference failed for: r0v367, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v369, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v371, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v374, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v376, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v378, types: [byte] */
    /* JADX WARNING: type inference failed for: r0v381, types: [byte] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public byte setPtzGetParametersNotifyData(byte[] r9) {
        /*
            r8 = this;
            r5 = 65280(0xff00, float:9.1477E-41)
            r6 = 4
            r4 = 3
            r3 = 2
            r2 = 1
            if (r9 != 0) goto L_0x000b
            r0 = 0
        L_0x000a:
            return r0
        L_0x000b:
            r0 = 0
            byte r0 = r9[r0]
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 163(0xa3, float:2.28E-43)
            if (r0 != r1) goto L_0x0430
            byte r0 = r9[r2]
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 5
            if (r0 != r1) goto L_0x0023
            byte r0 = r9[r3]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_PTZ_FOLLOW_MODE = r0
            r0 = 5
            goto L_0x000a
        L_0x0023:
            byte r0 = r9[r2]
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 9
            if (r0 != r1) goto L_0x0040
            byte r0 = r9[r3]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_PITCH_OF_FOLLOW_SPEED_SETTING_FOR = r0
            byte r0 = r9[r4]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_ROLL_OF_FOLLOW_SPEED_SETTING_FOR = r0
            byte r0 = r9[r6]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_YAW_OF_FOLLOW_SPEED_SETTING_FOR = r0
            r0 = 9
            goto L_0x000a
        L_0x0040:
            byte r0 = r9[r2]
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 10
            if (r0 != r1) goto L_0x005d
            byte r0 = r9[r3]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_PITCH_OF_PTZ_FOLLOW_DEADBAND = r0
            byte r0 = r9[r4]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_ROLL_OF_PTZ_FOLLOW_DEADBAND = r0
            byte r0 = r9[r6]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_YAW_OF_PTZ_FOLLOW_DEADBAND = r0
            r0 = 10
            goto L_0x000a
        L_0x005d:
            byte r0 = r9[r2]
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 11
            if (r0 != r1) goto L_0x0074
            byte r0 = r9[r3]
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_PITCH_OF_PTZ_ATTITUDE_ANGLE_FOR_FINE_TUNING = r0
            byte r0 = r9[r4]
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_ROLL_OF_PTZ_ATTITUDE_ANGLE_FOR_FINE_TUNING = r0
            byte r0 = r9[r6]
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_YAW_OF_PTZ_ATTITUDE_ANGLE_FOR_FINE_TUNING = r0
            r0 = 11
            goto L_0x000a
        L_0x0074:
            byte r0 = r9[r2]
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 12
            if (r0 != r1) goto L_0x0092
            byte r0 = r9[r3]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_PITCH_OF_ROCKER_FOR_DEAD_ZONE = r0
            byte r0 = r9[r4]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_ROLL_OF_ROCKER_FOR_DEAD_ZONE = r0
            byte r0 = r9[r6]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_YAW_OF_ROCKER_FOR_DEAD_ZONE = r0
            r0 = 12
            goto L_0x000a
        L_0x0092:
            byte r0 = r9[r2]
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 13
            if (r0 != r1) goto L_0x00b0
            byte r0 = r9[r3]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_PITCH_OF_ROCKER_FOR_SPEED = r0
            byte r0 = r9[r4]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_ROLL_OF_ROCKER_FOR_SPEED = r0
            byte r0 = r9[r6]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_YAW_OF_ROCKER_FOR_SPEED = r0
            r0 = 13
            goto L_0x000a
        L_0x00b0:
            byte r0 = r9[r2]
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 14
            if (r0 != r1) goto L_0x00ce
            byte r0 = r9[r3]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_PITCH_OF_ROCKER_FOR_SENSITIVITY_CURVE = r0
            byte r0 = r9[r4]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_ROLL_OF_ROCKER_FOR_SENSITIVITY_CURVE = r0
            byte r0 = r9[r6]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_YAW_OF_ROCKER_FOR_SENSITIVITY_CURVE = r0
            r0 = 14
            goto L_0x000a
        L_0x00ce:
            byte r0 = r9[r2]
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 15
            if (r0 != r1) goto L_0x00e6
            byte r0 = r9[r3]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_UP_OR_DOWN_OF_ROCKER_FOR_ORIENTATION = r0
            byte r0 = r9[r4]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_LEFT_OR_RIGHT_OF_ROCKER_FOR_ORIENTATION = r0
            r0 = 15
            goto L_0x000a
        L_0x00e6:
            byte r0 = r9[r2]
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 16
            if (r0 != r1) goto L_0x00f8
            byte r0 = r9[r3]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_MOTOR_DORMANCY_SWITCH = r0
            r0 = 16
            goto L_0x000a
        L_0x00f8:
            byte r0 = r9[r2]
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 17
            if (r0 != r1) goto L_0x010a
            byte r0 = r9[r3]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_SILENT_MODE_SWITCH = r0
            r0 = 17
            goto L_0x000a
        L_0x010a:
            byte r0 = r9[r2]
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 18
            if (r0 != r1) goto L_0x011c
            byte r0 = r9[r3]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_MANUAL_MODE_SWITCH = r0
            r0 = 18
            goto L_0x000a
        L_0x011c:
            byte r0 = r9[r2]
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 19
            if (r0 != r1) goto L_0x012e
            byte r0 = r9[r3]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_PTZ_SELF_TIMER_SWITCH = r0
            r0 = 19
            goto L_0x000a
        L_0x012e:
            byte r0 = r9[r2]
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 20
            if (r0 != r1) goto L_0x0140
            byte r0 = r9[r3]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_PTZ_ATTITUDE_RESET_SWITCH = r0
            r0 = 20
            goto L_0x000a
        L_0x0140:
            byte r0 = r9[r2]
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 21
            if (r0 != r1) goto L_0x0152
            byte r0 = r9[r3]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_SELF_FOLLOW_MODE_SWITCH = r0
            r0 = 21
            goto L_0x000a
        L_0x0152:
            byte r0 = r9[r2]
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 22
            if (r0 != r1) goto L_0x0164
            byte r0 = r9[r3]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_PHONE_CHECK_SWICTH = r0
            r0 = 22
            goto L_0x000a
        L_0x0164:
            byte r0 = r9[r2]
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 23
            if (r0 != r1) goto L_0x0176
            byte r0 = r9[r3]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_PTZ_CALIBRATION_SWITCH = r0
            r0 = 23
            goto L_0x000a
        L_0x0176:
            byte r0 = r9[r2]
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 24
            if (r0 != r1) goto L_0x0188
            byte r0 = r9[r3]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_PHONE_BATTERY_CHARGING_SWITCH = r0
            r0 = 24
            goto L_0x000a
        L_0x0188:
            byte r0 = r9[r2]
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 25
            if (r0 != r1) goto L_0x019a
            byte r0 = r9[r3]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_CONTEXTUAL_MODEL = r0
            r0 = 25
            goto L_0x000a
        L_0x019a:
            byte r0 = r9[r2]
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 26
            if (r0 != r1) goto L_0x0205
            r0 = 5
            byte r0 = r9[r0]
            byte r1 = r9[r6]
            byte r2 = r9[r4]
            byte r3 = r9[r3]
            java.lang.String r0 = r8.getVersion2String(r0, r1, r2, r3)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.GET_IMU_FIRMWARE_BOOT_VERSION = r0
            r0 = 9
            byte r0 = r9[r0]
            r1 = 8
            byte r1 = r9[r1]
            r2 = 7
            byte r2 = r9[r2]
            r3 = 6
            byte r3 = r9[r3]
            java.lang.String r0 = r8.getVersion2String(r0, r1, r2, r3)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.GET_IMU_FIRMWARE_APP_VERSION_STRING = r0
            r0 = 13
            byte r0 = r9[r0]
            r1 = 12
            byte r1 = r9[r1]
            r2 = 11
            byte r2 = r9[r2]
            r3 = 10
            byte r3 = r9[r3]
            java.lang.String r0 = r8.getVersion2String(r0, r1, r2, r3)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.GET_GMU_FIRMWARE_BOOT_VERSION = r0
            r0 = 17
            byte r0 = r9[r0]
            r1 = 16
            byte r1 = r9[r1]
            r2 = 15
            byte r2 = r9[r2]
            r3 = 14
            byte r3 = r9[r3]
            java.lang.String r0 = r8.getVersion2String(r0, r1, r2, r3)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.GET_GMU_FIRMWARE_APP_VERSION_STRING = r0
            r0 = 18
            byte r0 = r9[r0]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.GET_GMU_FIRMWARE_ID = r0
            r0 = 19
            byte r0 = r9[r0]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.GET_IMU_FIRMWARE_ID = r0
            r0 = 26
            goto L_0x000a
        L_0x0205:
            byte r0 = r9[r2]
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 27
            if (r0 != r1) goto L_0x0254
            byte r0 = r9[r4]
            byte r1 = r9[r3]
            java.lang.String r0 = r8.getVersion2String(r0, r1)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.GET_COMMUNICATION_PROTOCOL_VERSION = r0
            r0 = 9
            byte r1 = r9[r0]
            r0 = 8
            byte r2 = r9[r0]
            r0 = 7
            byte r3 = r9[r0]
            r0 = 6
            byte r4 = r9[r0]
            r0 = 5
            byte r5 = r9[r0]
            byte r6 = r9[r6]
            r0 = r8
            java.lang.String r0 = r0.getVersion2String(r1, r2, r3, r4, r5, r6)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.GET_PRODUCT_ID_NUMBER = r0
            r0 = 15
            byte r1 = r9[r0]
            r0 = 14
            byte r2 = r9[r0]
            r0 = 13
            byte r3 = r9[r0]
            r0 = 12
            byte r4 = r9[r0]
            r0 = 11
            byte r5 = r9[r0]
            r0 = 10
            byte r6 = r9[r0]
            r0 = r8
            java.lang.String r0 = r0.getVersion2String(r1, r2, r3, r4, r5, r6)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.GET_PTZ_MAC_ADDRESS = r0
            r0 = 27
            goto L_0x000a
        L_0x0254:
            byte r0 = r9[r2]
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 28
            if (r0 != r1) goto L_0x02c9
            byte r0 = r9[r3]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_PTZ_FOLLOW_MODE = r0
            byte r0 = r9[r4]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_PITCH_OF_FOLLOW_SPEED_SETTING_FOR = r0
            byte r0 = r9[r6]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_ROLL_OF_FOLLOW_SPEED_SETTING_FOR = r0
            r0 = 5
            byte r0 = r9[r0]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_YAW_OF_FOLLOW_SPEED_SETTING_FOR = r0
            r0 = 6
            byte r0 = r9[r0]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_PITCH_OF_PTZ_FOLLOW_DEADBAND = r0
            r0 = 7
            byte r0 = r9[r0]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_YAW_OF_PTZ_FOLLOW_DEADBAND = r0
            r0 = 8
            byte r0 = r9[r0]
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_PITCH_OF_PTZ_ATTITUDE_ANGLE_FOR_FINE_TUNING = r0
            r0 = 9
            byte r0 = r9[r0]
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_ROLL_OF_PTZ_ATTITUDE_ANGLE_FOR_FINE_TUNING = r0
            r0 = 10
            byte r0 = r9[r0]
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_YAW_OF_PTZ_ATTITUDE_ANGLE_FOR_FINE_TUNING = r0
            r0 = 11
            byte r0 = r9[r0]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_PITCH_OF_ROCKER_FOR_DEAD_ZONE = r0
            r0 = 12
            byte r0 = r9[r0]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_YAW_OF_ROCKER_FOR_DEAD_ZONE = r0
            r0 = 13
            byte r0 = r9[r0]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_PITCH_OF_ROCKER_FOR_SPEED = r0
            r0 = 14
            byte r0 = r9[r0]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_YAW_OF_ROCKER_FOR_SPEED = r0
            r0 = 15
            byte r0 = r9[r0]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_PITCH_OF_ROCKER_FOR_SENSITIVITY_CURVE = r0
            r0 = 16
            byte r0 = r9[r0]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_YAW_OF_ROCKER_FOR_SENSITIVITY_CURVE = r0
            r0 = 28
            goto L_0x000a
        L_0x02c9:
            byte r0 = r9[r2]
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 29
            if (r0 != r1) goto L_0x0350
            byte r0 = r9[r3]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_UP_OR_DOWN_OF_ROCKER_FOR_ORIENTATION = r0
            byte r0 = r9[r4]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_LEFT_OR_RIGHT_OF_ROCKER_FOR_ORIENTATION = r0
            byte r0 = r9[r6]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_MOTOR_DORMANCY_SWITCH = r0
            r0 = 5
            byte r0 = r9[r0]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_SILENT_MODE_SWITCH = r0
            r0 = 6
            byte r0 = r9[r0]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_MANUAL_MODE_SWITCH = r0
            r0 = 7
            byte r0 = r9[r0]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_PTZ_SELF_TIMER_SWITCH = r0
            r0 = 8
            byte r0 = r9[r0]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_PTZ_ATTITUDE_RESET_SWITCH = r0
            r0 = 9
            byte r0 = r9[r0]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_SELF_FOLLOW_MODE_SWITCH = r0
            r0 = 10
            byte r0 = r9[r0]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_PHONE_CHECK_SWICTH = r0
            r0 = 11
            byte r0 = r9[r0]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_PTZ_CALIBRATION_SWITCH = r0
            r0 = 12
            byte r0 = r9[r0]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_PHONE_BATTERY_CHARGING_SWITCH = r0
            r0 = 13
            byte r0 = r9[r0]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_CONTEXTUAL_MODEL = r0
            java.lang.String r0 = r8.mPtzType
            java.lang.String r1 = "VILTA_MOBILE_FM300"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0347
            r0 = 14
            byte r0 = r9[r0]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_FM300_KALEIDOSCOPE_MODE_SWITCH = r0
        L_0x033b:
            com.freevisiontech.fvmobile.utils.MoveTimelapseUtil.getInstance()
            int r0 = com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_MOTOR_DORMANCY_SWITCH
            com.freevisiontech.fvmobile.utils.MoveTimelapseUtil.setCameraFvShareSleep(r0)
            r0 = 29
            goto L_0x000a
        L_0x0347:
            r0 = 14
            byte r0 = r9[r0]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_ROLL_OR_EXPOSURE_MODEL = r0
            goto L_0x033b
        L_0x0350:
            byte r0 = r9[r2]
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 30
            if (r0 != r1) goto L_0x03e9
            byte r0 = r9[r3]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.GET_PTZ_BATTERY_CHARGING_NOTIFY = r0
            byte r0 = r9[r4]
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.GET_PTZ_BATTERY_TEMPERATURE = r0
            byte r0 = r9[r6]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.GET_PTZ_BATTERY_SURPLUS_CAPACITY_PERCENTAGE = r0
            r0 = 5
            byte r0 = r9[r0]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.GET_PTZ_BATTERY_SURPLUS_LIFE_PERCENTAGE = r0
            r0 = 6
            byte r0 = r9[r0]
            int r0 = r0 << 0
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 7
            byte r1 = r9[r1]
            int r1 = r1 << 8
            r1 = r1 & r5
            int r0 = r0 + r1
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.GET_PTZ_BATTERY_VOLTAGE = r0
            r0 = 8
            byte r0 = r9[r0]
            int r0 = r0 << 0
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 9
            byte r1 = r9[r1]
            int r1 = r1 << 8
            r1 = r1 & r5
            int r7 = r0 + r1
            r0 = 32767(0x7fff, float:4.5916E-41)
            if (r7 <= r0) goto L_0x03e6
            r0 = 65536(0x10000, float:9.18355E-41)
            int r0 = r7 - r0
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.GET_PTZ_BATTERY_CAPACITY = r0
        L_0x039a:
            r0 = 10
            byte r0 = r9[r0]
            int r0 = r0 << 0
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 11
            byte r1 = r9[r1]
            int r1 = r1 << 8
            r1 = r1 & r5
            int r0 = r0 + r1
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.GET_PTZ_BATTERY_SURPLUS_CAPACITY = r0
            r0 = 12
            byte r0 = r9[r0]
            int r0 = r0 << 0
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 13
            byte r1 = r9[r1]
            int r1 = r1 << 8
            r1 = r1 & r5
            int r0 = r0 + r1
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.GET_PTZ_BATTERY_FULL_CAPACITY = r0
            r0 = 14
            byte r0 = r9[r0]
            int r0 = r0 << 0
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 15
            byte r1 = r9[r1]
            int r1 = r1 << 8
            r1 = r1 & r5
            int r0 = r0 + r1
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.GET_PTZ_BATTERY_DESIGN_CAPACITY = r0
            r0 = 16
            byte r0 = r9[r0]
            int r0 = r0 << 0
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 17
            byte r1 = r9[r1]
            int r1 = r1 << 8
            r1 = r1 & r5
            int r0 = r0 + r1
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.GET_PTZ_BATTERY_CIRCULATION_COUNT = r0
            r0 = 30
            goto L_0x000a
        L_0x03e6:
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.GET_PTZ_BATTERY_CAPACITY = r7
            goto L_0x039a
        L_0x03e9:
            byte r0 = r9[r2]
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 37
            if (r0 != r1) goto L_0x0433
            byte r0 = r9[r3]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_ROLL_OF_FOLLOW_SPEED_SETTING_FOR = r0
            byte r0 = r9[r4]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_ROLL_OF_PTZ_FOLLOW_DEADBAND = r0
            byte r0 = r9[r6]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_ROLL_OF_ROCKER_FOR_DEAD_ZONE = r0
            r0 = 5
            byte r0 = r9[r0]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_ROLL_OF_ROCKER_FOR_SPEED = r0
            r0 = 6
            byte r0 = r9[r0]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_ROLL_OF_ROCKER_FOR_SENSITIVITY_CURVE = r0
            r0 = 7
            byte r0 = r9[r0]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.GET_PTZ_FN_OPTION = r0
            r0 = 10
            byte r0 = r9[r0]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_FOCUS_KNOB_FUNCTION = r0
            r0 = 10
            byte r0 = r9[r0]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_THUMB_WHEEL_SELECTION = r0
            int r0 = com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_THUMB_WHEEL_SELECTION
            if (r0 != r2) goto L_0x0430
            r0 = 21
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_THUMB_WHEEL_SELECTION = r0
        L_0x0430:
            r0 = 0
            goto L_0x000a
        L_0x0433:
            byte r0 = r9[r2]
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 31
            if (r0 != r1) goto L_0x04cc
            byte r0 = r9[r3]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.GET_PTZ_BATTERY_CHARGING_NOTIFY = r0
            byte r0 = r9[r4]
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.GET_PTZ_BATTERY_TEMPERATURE = r0
            byte r0 = r9[r6]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.GET_PTZ_BATTERY_SURPLUS_CAPACITY_PERCENTAGE = r0
            r0 = 5
            byte r0 = r9[r0]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.GET_PTZ_BATTERY_SURPLUS_LIFE_PERCENTAGE = r0
            r0 = 6
            byte r0 = r9[r0]
            int r0 = r0 << 0
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 7
            byte r1 = r9[r1]
            int r1 = r1 << 8
            r1 = r1 & r5
            int r0 = r0 + r1
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.GET_PTZ_BATTERY_VOLTAGE = r0
            r0 = 8
            byte r0 = r9[r0]
            int r0 = r0 << 0
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 9
            byte r1 = r9[r1]
            int r1 = r1 << 8
            r1 = r1 & r5
            int r7 = r0 + r1
            r0 = 32767(0x7fff, float:4.5916E-41)
            if (r7 <= r0) goto L_0x04c9
            r0 = 65536(0x10000, float:9.18355E-41)
            int r0 = r7 - r0
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.GET_PTZ_BATTERY_CAPACITY = r0
        L_0x047d:
            r0 = 10
            byte r0 = r9[r0]
            int r0 = r0 << 0
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 11
            byte r1 = r9[r1]
            int r1 = r1 << 8
            r1 = r1 & r5
            int r0 = r0 + r1
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.GET_PTZ_BATTERY_SURPLUS_CAPACITY = r0
            r0 = 12
            byte r0 = r9[r0]
            int r0 = r0 << 0
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 13
            byte r1 = r9[r1]
            int r1 = r1 << 8
            r1 = r1 & r5
            int r0 = r0 + r1
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.GET_PTZ_BATTERY_FULL_CAPACITY = r0
            r0 = 14
            byte r0 = r9[r0]
            int r0 = r0 << 0
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 15
            byte r1 = r9[r1]
            int r1 = r1 << 8
            r1 = r1 & r5
            int r0 = r0 + r1
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.GET_PTZ_BATTERY_DESIGN_CAPACITY = r0
            r0 = 16
            byte r0 = r9[r0]
            int r0 = r0 << 0
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 17
            byte r1 = r9[r1]
            int r1 = r1 << 8
            r1 = r1 & r5
            int r0 = r0 + r1
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.GET_PTZ_BATTERY_CIRCULATION_COUNT = r0
            r0 = 31
            goto L_0x000a
        L_0x04c9:
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.GET_PTZ_BATTERY_CAPACITY = r7
            goto L_0x047d
        L_0x04cc:
            byte r0 = r9[r2]
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 32
            if (r0 != r1) goto L_0x04ea
            byte r0 = r9[r3]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.GET_PITCH_OF_PTZ_MOTOR_OUTPUT = r0
            byte r0 = r9[r3]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.GET_ROLL_OF_PTZ_MOTOR_OUTPUT = r0
            byte r0 = r9[r3]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.GET_YAW_OF_PTZ_MOTOR_OUTPUT = r0
            r0 = 32
            goto L_0x000a
        L_0x04ea:
            byte r0 = r9[r2]
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 33
            if (r0 != r1) goto L_0x0502
            byte r0 = r9[r3]
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.GET_PITCH_OF_PTZ_REAL_TIME_ATTITUDE = r0
            byte r0 = r9[r4]
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.GET_ROLL_OF_PTZ_REAL_TIME_ATTITUDE = r0
            byte r0 = r9[r6]
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.GET_YAW_OF_PTZ_REAL_TIME_ATTITUDE = r0
            r0 = 33
            goto L_0x000a
        L_0x0502:
            byte r0 = r9[r2]
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 49
            if (r0 != r1) goto L_0x0514
            byte r0 = r9[r3]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.SET_ROLL_OR_EXPOSURE_MODEL = r0
            r0 = 49
            goto L_0x000a
        L_0x0514:
            byte r0 = r9[r2]
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 58
            if (r0 != r1) goto L_0x0524
            byte r0 = r9[r3]
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.GET_PTZ_ACTIVATE_STATUS = r0
            r0 = 58
            goto L_0x000a
        L_0x0524:
            byte r0 = r9[r2]
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 59
            if (r0 != r1) goto L_0x0430
            java.lang.String r0 = r8.readSn(r9)
            com.freevisiontech.fvmobile.utils.BlePtzParasConstant.GET_PTZ_SN_CODE = r0
            r0 = 59
            goto L_0x000a
        */
        throw new UnsupportedOperationException("Method not decompiled: com.freevisiontech.fvmobile.utils.BleNotifyDataUtil.setPtzGetParametersNotifyData(byte[]):byte");
    }

    private String readSn(byte[] value) {
        if (value.length <= 0) {
            return null;
        }
        char[] snChars = new char[14];
        for (int i = 0; i < 14; i++) {
            snChars[i] = (char) value[i + 2];
        }
        return new String(snChars);
    }

    @NonNull
    private String getVersion2String(byte value4, byte value3, byte value2, byte value1) {
        String fourth;
        String third;
        String second;
        String first;
        if (value4 < 10) {
            fourth = "0" + value4;
        } else {
            fourth = Integer.toHexString(value4);
        }
        if (value3 < 10) {
            third = "0" + value3;
        } else {
            third = Integer.toHexString(value3);
        }
        if (value2 < 10) {
            second = "0" + value2;
        } else {
            second = Integer.toHexString(value2);
        }
        if (value1 < 10) {
            first = "0" + value1;
        } else {
            first = Integer.toHexString(value1);
        }
        return fourth + "." + third + "." + second + "." + first;
    }

    private String getVersion2String(byte value2, byte value1) {
        String second;
        String first;
        if (value2 < 10) {
            second = "0" + value2;
        } else {
            second = Integer.toHexString(value2);
        }
        if (value1 < 10) {
            first = "0" + value1;
        } else {
            first = Integer.toHexString(value1);
        }
        return second + "." + first;
    }

    private String getVersion2String(byte value6, byte value5, byte value4, byte value3, byte value2, byte value1) {
        String six;
        String five;
        String fourth;
        String third;
        String second;
        String first;
        if (value6 < 10) {
            six = "0" + value6;
        } else {
            six = Integer.toHexString(value6);
        }
        if (value5 < 10) {
            five = "0" + value5;
        } else {
            five = Integer.toHexString(value5);
        }
        if (value4 < 10) {
            fourth = "0" + value4;
        } else {
            fourth = Integer.toHexString(value4);
        }
        if (value3 < 10) {
            third = "0" + value3;
        } else {
            third = Integer.toHexString(value3);
        }
        if (value2 < 10) {
            second = "0" + value2;
        } else {
            second = Integer.toHexString(value2);
        }
        if (value1 < 10) {
            first = "0" + value1;
        } else {
            first = Integer.toHexString(value1);
        }
        return six + ":" + five + ":" + fourth + ":" + third + ":" + second + ":" + first;
    }
}
