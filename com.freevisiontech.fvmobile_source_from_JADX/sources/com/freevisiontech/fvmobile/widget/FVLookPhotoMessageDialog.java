package com.freevisiontech.fvmobile.widget;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.p003v7.app.AlertDialog;
import android.widget.TextView;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.utility.SPUtils;
import com.freevisiontech.fvmobile.utility.SharePrefConstant;
import com.freevisiontech.fvmobile.utility.Util;
import com.freevisiontech.fvmobile.widget.view.IntentKey;
import com.google.android.vending.expansion.downloader.Constants;

public class FVLookPhotoMessageDialog extends AlertDialog {
    private static final int PHOTO_UPDATE = 1;
    private Context context;
    private TextView dialog_fvlook_photo_msg_date;
    private TextView dialog_fvlook_photo_msg_path;
    private TextView dialog_fvlook_photo_msg_size;
    private Handler mUiHandler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    FVLookPhotoMessageDialog.this.updateInfo();
                    return false;
                default:
                    return false;
            }
        }
    });
    private String path;

    public FVLookPhotoMessageDialog(Context context2, String path2) {
        super(context2);
        this.context = context2;
        this.path = path2;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0853R.layout.dialog_fv_look_photo_message);
        this.dialog_fvlook_photo_msg_size = (TextView) findViewById(C0853R.C0855id.dialog_fvlook_photo_msg_size);
        this.dialog_fvlook_photo_msg_date = (TextView) findViewById(C0853R.C0855id.dialog_fvlook_photo_msg_date);
        this.dialog_fvlook_photo_msg_path = (TextView) findViewById(C0853R.C0855id.dialog_fvlook_photo_msg_path);
        getWindow().setBackgroundDrawableResource(17170445);
        updateInfo();
    }

    /* access modifiers changed from: private */
    public void updateInfo() {
        String basePath;
        if (!((String) SPUtils.get(this.context, SharePrefConstant.SAVE_STORAGE_SD_CARD_PATH, IntentKey.FILE_PATH)).contains(Constants.FILENAME_SEQUENCE_SEPARATOR)) {
            basePath = IntentKey.FILE_PATH;
        } else {
            basePath = Util.getParentPath(this.context);
        }
        this.dialog_fvlook_photo_msg_path.setText(basePath + this.path);
        if (!Util.isZh(this.context)) {
            this.dialog_fvlook_photo_msg_date.setText(Util.zhToEnglishMonth(Integer.valueOf(this.path.substring(7, 9)).intValue(), this.context) + " " + this.path.substring(9, 11) + "," + this.path.substring(3, 7));
        } else if (!(this.path.substring(3, 7) == null || this.path.substring(7, 9) == null || this.path.substring(9, 11) == null)) {
            this.dialog_fvlook_photo_msg_date.setText(this.path.substring(3, 7) + "年" + this.path.substring(7, 9) + "月" + this.path.substring(9, 11) + "日");
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(basePath + this.path, options);
        this.dialog_fvlook_photo_msg_size.setText(options.outWidth + "x" + options.outHeight);
    }

    public void updatePath(String newPath) {
        this.path = newPath;
        this.mUiHandler.sendEmptyMessage(1);
    }
}
