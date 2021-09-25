package com.freevisiontech.fvmobile.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.utility.BaseActivityManager;
import com.freevisiontech.fvmobile.utility.Util;
import com.freevisiontech.fvmobile.utils.LoadingView;
import com.freevisiontech.fvmobile.utils.PhotoEditUtils;
import com.freevisiontech.fvmobile.widget.view.IntentKey;
import com.umeng.analytics.MobclickAgent;
import com.vise.log.ViseLog;
import com.yalantis.ucrop.UCrop;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import p010me.iwf.photopicker.widget.control.DataChangeNotification;
import p010me.iwf.photopicker.widget.control.IssueKey;

public class PhotoEditActivity extends Activity implements View.OnClickListener {
    private static final int REQUEST_SELECT_PICTURE = 1;
    private static final String SAMPLE_CROPPED_IMAGE_NAME = "tupian";
    private static final String TAG = "PhotoEditActivity";
    private final int PHOTOEDITFOUR = 25;
    private final int PHOTOEDITONE = 22;
    private final int PHOTOEDITSIX = 21;
    private final int PHOTOEDITTHREE = 24;
    private final int PHOTOEDITTWO = 23;
    @Bind({2131755403})
    LinearLayout act_edit_buttom;
    @Bind({2131755421})
    TextView act_edit_buttom_caijian_btCai;
    @Bind({2131755422})
    TextView act_edit_buttom_caijian_btXuan;
    @Bind({2131755420})
    ImageView act_edit_buttom_caijian_dismiss;
    @Bind({2131755410})
    LinearLayout act_edit_buttom_caijian_layout;
    @Bind({2131755411})
    LinearLayout act_edit_buttom_caijian_layout_caijian;
    @Bind({2131755445})
    ImageView act_edit_buttom_lvjing_dismiss;
    @Bind({2131755423})
    LinearLayout act_edit_buttom_lvjing_layout;
    @Bind({2131755463})
    ImageView act_edit_buttom_masaike_dismiss;
    @Bind({2131755447})
    LinearLayout act_edit_buttom_masaike_layout;
    @Bind({2131755466})
    ImageView act_edit_buttom_meiyan_dismiss;
    @Bind({2131755465})
    LinearLayout act_edit_buttom_meiyan_layout;
    @Bind({2131755477})
    ImageView act_edit_buttom_paint_dismiss;
    @Bind({2131755468})
    LinearLayout act_edit_buttom_paint_layout;
    @Bind({2131755483})
    ImageView act_edit_buttom_tiaojie_dismiss;
    @Bind({2131755479})
    LinearLayout act_edit_buttom_tiaojie_layout;
    @Bind({2131755419})
    LinearLayout act_edit_buttom_xuanzhuan_layout;
    @Bind({2131755383})
    LinearLayout act_edit_over;
    @Bind({2131755382})
    LinearLayout act_edit_return;
    @Bind({2131755380})
    LinearLayout act_ic_edit_left;
    @Bind({2131755381})
    LinearLayout act_ic_edit_right;
    @Bind({2131755378})
    ImageView act_photo_edit_imageview;
    @Bind({2131755384})
    TextView act_photo_edit_jiazai;
    @Bind({2131755379})
    RelativeLayout act_photo_edit_return_and_go;
    private Activity activity;
    @Bind({2131755404})
    LinearLayout edit_lvjing_button;
    @Bind({2131755407})
    LinearLayout edit_masaike_button;
    @Bind({2131755405})
    LinearLayout edit_meirong_button;
    @Bind({2131755409})
    LinearLayout edit_paint_button;
    @Bind({2131755406})
    LinearLayout edit_tiaojie_button;
    @Bind({2131755408})
    LinearLayout edit_xuanzhuan_button;
    /* access modifiers changed from: private */
    public double fileSizeM;
    private TextWatcher mAspectRatioTextWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            PhotoEditActivity.this.mRadioGroupAspectRatio.clearCheck();
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
        }
    };
    private CheckBox mCheckBoxFreeStyleCrop;
    private CheckBox mCheckBoxHideBottomControls;
    private CheckBox mCheckBoxMaxSize;
    private EditText mEditTextMaxHeight;
    private EditText mEditTextMaxWidth;
    private EditText mEditTextRatioX;
    private EditText mEditTextRatioY;
    private MyCountDownTimer mMyCountDownTimer;
    private AlertDialog mProgressDialog;
    /* access modifiers changed from: private */
    public RadioGroup mRadioGroupAspectRatio;
    private RadioGroup mRadioGroupCompressionSettings;
    /* access modifiers changed from: private */
    public SeekBar mSeekBarQuality;
    /* access modifiers changed from: private */
    public TextView mTextViewQuality;
    private List<String> paths = new ArrayList();
    /* access modifiers changed from: private */
    public List photoEditList;
    private List photoEditListAll;
    /* access modifiers changed from: private */
    public String photo_edit_path;
    private String photo_path;
    private int position = 0;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0853R.layout.activity_photo_edit);
        ButterKnife.bind((Activity) this);
        this.activity = this;
        this.photo_edit_path = getIntent().getStringExtra(IntentKey.PHOTOEDITPATH);
        this.photo_path = this.photo_edit_path;
        Glide.with(this.activity).load(this.photo_edit_path).into(this.act_photo_edit_imageview);
        Util.getOutputMediaFile(1, 2, this);
        showProgress(getString(C0853R.string.file_show_pro_title));
        this.photoEditListAll = new ArrayList();
        this.photoEditList = new ArrayList();
        initview();
        if (this.mMyCountDownTimer != null) {
            this.mMyCountDownTimer.cancel();
            this.mMyCountDownTimer = new MyCountDownTimer(1000, 1000);
            this.mMyCountDownTimer.start();
            return;
        }
        this.mMyCountDownTimer = new MyCountDownTimer(1000, 1000);
        this.mMyCountDownTimer.start();
    }

    class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onTick(long millisUntilFinished) {
        }

        public void onFinish() {
            String pathBitNew;
            ViseLog.m1466e("MyCountDownTimer onFinish");
            double unused = PhotoEditActivity.this.fileSizeM = (((double) new File(PhotoEditActivity.this.photo_edit_path).length()) / 1024.0d) / 1024.0d;
            ViseLog.m1466e("fileSizeM:" + PhotoEditActivity.this.fileSizeM + "MB");
            if (PhotoEditActivity.this.fileSizeM > 0.5d) {
                Bitmap bitmap = PhotoEditActivity.this.getImage(PhotoEditActivity.this.photo_edit_path);
                String editMosaicPhotoOver = PhotoEditActivity.this.photo_edit_path;
                if (editMosaicPhotoOver.substring(editMosaicPhotoOver.length() - 10, editMosaicPhotoOver.length() - 4).equals("mosaic")) {
                    pathBitNew = PhotoEditUtils.saveBitmapToDrawableMosaicCameraCatch(bitmap, PhotoEditActivity.this);
                } else {
                    pathBitNew = PhotoEditUtils.saveBitmapToDrawableCameraCatch(bitmap, PhotoEditActivity.this);
                }
                Log.e("------------", "--------  pathBitNew ----" + pathBitNew);
                String unused2 = PhotoEditActivity.this.photo_edit_path = pathBitNew;
            }
            PhotoEditActivity.this.act_photo_edit_return_and_go.setVisibility(8);
            PhotoEditActivity.this.photoEditList.add(PhotoEditActivity.this.photo_edit_path);
            PhotoEditActivity.this.setupUI();
            PhotoEditActivity.this.hideProgress();
        }
    }

    private void initview() {
        this.act_edit_return.setOnClickListener(this);
        this.act_ic_edit_left.setOnClickListener(this);
        this.act_ic_edit_right.setOnClickListener(this);
        this.edit_xuanzhuan_button.setOnClickListener(this);
        this.act_edit_buttom_caijian_dismiss.setOnClickListener(this);
        this.act_edit_buttom_caijian_btXuan.setOnClickListener(this);
        this.act_edit_buttom_caijian_btCai.setOnClickListener(this);
        this.edit_tiaojie_button.setOnClickListener(this);
        this.act_edit_buttom_tiaojie_dismiss.setOnClickListener(this);
        this.act_edit_buttom_paint_dismiss.setOnClickListener(this);
        this.edit_paint_button.setOnClickListener(this);
        this.edit_meirong_button.setOnClickListener(this);
        this.act_edit_buttom_meiyan_dismiss.setOnClickListener(this);
        this.edit_lvjing_button.setOnClickListener(this);
        this.act_edit_buttom_lvjing_dismiss.setOnClickListener(this);
        this.act_edit_buttom_masaike_dismiss.setOnClickListener(this);
        this.edit_masaike_button.setOnClickListener(this);
        this.act_edit_over.setOnClickListener(this);
    }

    public void onClick(View v) {
        String savePath;
        switch (v.getId()) {
            case C0853R.C0855id.act_ic_edit_left:
                if (this.position == 0) {
                    Toast.makeText(this, C0853R.string.file_the_first_step, 0).show();
                    return;
                } else if (this.position >= 0 && this.position < this.photoEditList.size()) {
                    int pos = this.position - 1;
                    this.position = pos;
                    Glide.with(this.activity).load(this.photoEditList.get(pos)).into(this.act_photo_edit_imageview);
                    return;
                } else {
                    return;
                }
            case C0853R.C0855id.act_ic_edit_right:
                if (this.position == this.photoEditList.size() - 1) {
                    Toast.makeText(this, C0853R.string.file_the_last_step, 0).show();
                    return;
                } else if (this.position >= 0 && this.position < this.photoEditList.size()) {
                    int pos2 = this.position + 1;
                    this.position = pos2;
                    Glide.with(this.activity).load(this.photoEditList.get(pos2)).into(this.act_photo_edit_imageview);
                    return;
                } else {
                    return;
                }
            case C0853R.C0855id.act_edit_return:
                if (this.photoEditListAll.size() > 0) {
                    for (int a = 0; a < this.photoEditListAll.size(); a++) {
                        new File(this.photoEditListAll.get(a).toString()).delete();
                    }
                }
                finish();
                return;
            case C0853R.C0855id.act_edit_over:
                BaseActivityManager.getActivityManager().popActivityOne(LookPhotoActivity.class);
                showProgress(getString(C0853R.string.file_picture_generation_being));
                Bitmap bitmap = BitmapFactory.decodeFile(this.photoEditList.get(this.position).toString());
                String editMosaicPhotoOver = this.photoEditList.get(this.position).toString();
                Log.e("-------------", "--------  editMosaicPhotoOver -------" + editMosaicPhotoOver);
                if (editMosaicPhotoOver.substring(editMosaicPhotoOver.length() - 10, editMosaicPhotoOver.length() - 4).equals("mosaic")) {
                    savePath = PhotoEditUtils.saveBitmapToDrawableMosaicCamera(bitmap, this.activity);
                } else {
                    savePath = PhotoEditUtils.saveBitmapToDrawableCamera(bitmap, this.activity);
                }
                Util.updateGallery(this, savePath, "image/jpeg");
                if (this.photoEditListAll.size() > 0) {
                    for (int a2 = 0; a2 < this.photoEditListAll.size(); a2++) {
                        new File(this.photoEditListAll.get(a2).toString()).delete();
                    }
                }
                DataChangeNotification.getInstance().notifyDataChanged(IssueKey.PHOTO_VIDEO_DEIETE);
                hideProgress();
                finish();
                return;
            case C0853R.C0855id.edit_lvjing_button:
                Intent intent = new Intent(this.activity, PhotoEditOneActivity.class);
                intent.putExtra(IntentKey.PHOTOEDITPATH, this.photoEditList.get(this.position).toString());
                startActivityForResult(intent, 22);
                return;
            case C0853R.C0855id.edit_meirong_button:
                Intent intent2 = new Intent(this.activity, PhotoEditTwoActivity.class);
                intent2.putExtra(IntentKey.PHOTOEDITPATH, this.photoEditList.get(this.position).toString());
                startActivityForResult(intent2, 23);
                return;
            case C0853R.C0855id.edit_tiaojie_button:
                Intent intent3 = new Intent(this.activity, PhotoEditThreeActivity.class);
                intent3.putExtra(IntentKey.PHOTOEDITPATH, this.photoEditList.get(this.position).toString());
                startActivityForResult(intent3, 24);
                return;
            case C0853R.C0855id.edit_masaike_button:
                String editMosaicPhoto = this.photoEditList.get(this.position).toString();
                if (editMosaicPhoto.substring(editMosaicPhoto.length() - 10, editMosaicPhoto.length() - 4).equals("mosaic")) {
                    Toast.makeText(this.activity, getString(C0853R.string.file_edit_mosaic_text_two), 0).show();
                    return;
                }
                Intent intent4 = new Intent(this.activity, PhotoEditFourActivity.class);
                intent4.putExtra(IntentKey.PHOTOEDITPATH, this.photoEditList.get(this.position).toString());
                startActivityForResult(intent4, 25);
                return;
            case C0853R.C0855id.edit_xuanzhuan_button:
                startCropActivity(Uri.fromFile(new File(this.photoEditList.get(this.position).toString())));
                return;
            case C0853R.C0855id.edit_paint_button:
                Intent intent6 = new Intent(this.activity, PhotoEditSixActivity.class);
                intent6.putExtra(IntentKey.PHOTOEDITPATH, this.photoEditList.get(this.position).toString());
                startActivityForResult(intent6, 21);
                return;
            case C0853R.C0855id.act_edit_buttom_caijian_dismiss:
                this.act_edit_buttom_caijian_layout.setVisibility(8);
                return;
            case C0853R.C0855id.act_edit_buttom_caijian_btCai:
                this.act_edit_buttom_caijian_layout_caijian.setVisibility(0);
                this.act_edit_buttom_xuanzhuan_layout.setVisibility(8);
                return;
            case C0853R.C0855id.act_edit_buttom_caijian_btXuan:
                this.act_edit_buttom_xuanzhuan_layout.setVisibility(0);
                this.act_edit_buttom_caijian_layout_caijian.setVisibility(8);
                return;
            case C0853R.C0855id.act_edit_buttom_lvjing_dismiss:
                this.act_edit_buttom_lvjing_layout.setVisibility(8);
                return;
            case C0853R.C0855id.act_edit_buttom_masaike_dismiss:
                this.act_edit_buttom_masaike_layout.setVisibility(8);
                return;
            case C0853R.C0855id.act_edit_buttom_meiyan_dismiss:
                this.act_edit_buttom_meiyan_layout.setVisibility(8);
                return;
            case C0853R.C0855id.act_edit_buttom_paint_dismiss:
                this.act_edit_buttom_paint_layout.setVisibility(8);
                return;
            case C0853R.C0855id.act_edit_buttom_tiaojie_dismiss:
                this.act_edit_buttom_tiaojie_layout.setVisibility(8);
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: private */
    public Bitmap getImage(String path) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        Bitmap decodeFile = BitmapFactory.decodeFile(path, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        int be = 1;
        if (w > h && ((float) w) > 480.0f) {
            be = (int) (((float) newOpts.outWidth) / 480.0f);
        } else if (w < h && ((float) h) > 800.0f) {
            be = (int) (((float) newOpts.outHeight) / 800.0f);
        }
        if (be <= 0) {
            be = 1;
        }
        newOpts.inSampleSize = be;
        return compressImage(BitmapFactory.decodeFile(path, newOpts));
    }

    private Bitmap compressImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 90;
        bmp.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        while (baos.toByteArray().length / 1024 > 800) {
            baos.reset();
            options--;
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        ViseLog.m1466e("after compress: " + (baos.toByteArray().length / 1024) + "KB");
        return BitmapFactory.decodeStream(isBm, (Rect) null, (BitmapFactory.Options) null);
    }

    /* access modifiers changed from: private */
    public void setupUI() {
        this.mRadioGroupAspectRatio = (RadioGroup) findViewById(C0853R.C0855id.radio_group_aspect_ratio);
        this.mRadioGroupCompressionSettings = (RadioGroup) findViewById(C0853R.C0855id.radio_group_compression_settings);
        this.mCheckBoxMaxSize = (CheckBox) findViewById(C0853R.C0855id.checkbox_max_size);
        this.mEditTextRatioX = (EditText) findViewById(C0853R.C0855id.edit_text_ratio_x);
        this.mEditTextRatioY = (EditText) findViewById(C0853R.C0855id.edit_text_ratio_y);
        this.mEditTextMaxWidth = (EditText) findViewById(C0853R.C0855id.edit_text_max_width);
        this.mEditTextMaxHeight = (EditText) findViewById(C0853R.C0855id.edit_text_max_height);
        this.mSeekBarQuality = (SeekBar) findViewById(C0853R.C0855id.seekbar_quality);
        this.mTextViewQuality = (TextView) findViewById(C0853R.C0855id.text_view_quality);
        this.mCheckBoxHideBottomControls = (CheckBox) findViewById(C0853R.C0855id.checkbox_hide_bottom_controls);
        this.mCheckBoxFreeStyleCrop = (CheckBox) findViewById(C0853R.C0855id.checkbox_freestyle_crop);
        this.mRadioGroupAspectRatio.check(C0853R.C0855id.radio_dynamic);
        this.mEditTextRatioX.addTextChangedListener(this.mAspectRatioTextWatcher);
        this.mEditTextRatioY.addTextChangedListener(this.mAspectRatioTextWatcher);
        this.mRadioGroupCompressionSettings.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                PhotoEditActivity.this.mSeekBarQuality.setEnabled(checkedId == C0853R.C0855id.radio_jpeg);
            }
        });
        this.mRadioGroupCompressionSettings.check(C0853R.C0855id.radio_jpeg);
        this.mSeekBarQuality.setProgress(90);
        this.mTextViewQuality.setText(String.format(getString(C0853R.string.format_quality_d), new Object[]{Integer.valueOf(this.mSeekBarQuality.getProgress())}));
        this.mSeekBarQuality.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                PhotoEditActivity.this.mTextViewQuality.setText(String.format(PhotoEditActivity.this.getString(C0853R.string.format_quality_d), new Object[]{Integer.valueOf(progress)}));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void startCropActivity(@NonNull Uri uri) {
        String destinationFileName = SAMPLE_CROPPED_IMAGE_NAME + System.currentTimeMillis();
        switch (this.mRadioGroupCompressionSettings.getCheckedRadioButtonId()) {
            case C0853R.C0855id.radio_jpeg:
                destinationFileName = destinationFileName + ".jpg";
                break;
            case C0853R.C0855id.radio_png:
                destinationFileName = destinationFileName + ".png";
                break;
        }
        advancedConfig(basisConfig(UCrop.m1546of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName))))).start(this);
    }

    private UCrop basisConfig(@NonNull UCrop uCrop) {
        switch (this.mRadioGroupAspectRatio.getCheckedRadioButtonId()) {
            case C0853R.C0855id.radio_dynamic:
                break;
            case C0853R.C0855id.radio_origin:
                uCrop = uCrop.useSourceImageAspectRatio();
                break;
            case C0853R.C0855id.radio_square:
                uCrop = uCrop.withAspectRatio(1.0f, 1.0f);
                break;
            default:
                try {
                    float ratioX = Float.valueOf(this.mEditTextRatioX.getText().toString().trim()).floatValue();
                    float ratioY = Float.valueOf(this.mEditTextRatioY.getText().toString().trim()).floatValue();
                    if (ratioX > 0.0f && ratioY > 0.0f) {
                        uCrop = uCrop.withAspectRatio(ratioX, ratioY);
                        break;
                    }
                } catch (NumberFormatException e) {
                    Log.i(TAG, String.format("Number please: %s", new Object[]{e.getMessage()}));
                    break;
                }
        }
        if (!this.mCheckBoxMaxSize.isChecked()) {
            return uCrop;
        }
        try {
            int maxWidth = Integer.valueOf(this.mEditTextMaxWidth.getText().toString().trim()).intValue();
            int maxHeight = Integer.valueOf(this.mEditTextMaxHeight.getText().toString().trim()).intValue();
            if (maxWidth <= 0 || maxHeight <= 0) {
                return uCrop;
            }
            return uCrop.withMaxResultSize(maxWidth, maxHeight);
        } catch (NumberFormatException e2) {
            Log.e(TAG, "Number please", e2);
            return uCrop;
        }
    }

    private UCrop advancedConfig(@NonNull UCrop uCrop) {
        UCrop.Options options = new UCrop.Options();
        switch (this.mRadioGroupCompressionSettings.getCheckedRadioButtonId()) {
            case C0853R.C0855id.radio_png:
                options.setCompressionFormat(Bitmap.CompressFormat.PNG);
                break;
            default:
                options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                break;
        }
        options.setCompressionQuality(this.mSeekBarQuality.getProgress());
        options.setHideBottomControls(this.mCheckBoxHideBottomControls.isChecked());
        options.setFreeStyleCropEnabled(this.mCheckBoxFreeStyleCrop.isChecked());
        return uCrop.withOptions(options);
    }

    private void handleCropResult(@NonNull Intent result) {
        Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            try {
                String pathOne = PhotoEditUtils.saveBitmapToDrawable(MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri));
                removeNumsItem(this.photoEditList, this.position + 1);
                this.photo_edit_path = pathOne;
                this.photoEditListAll.add(this.photo_edit_path);
                this.photoEditList.add(this.photo_edit_path);
                this.position = this.photoEditList.size() - 1;
                Log.e("-------------", "----------photoEditList------" + this.photoEditList);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            Glide.with(this.activity).load(resultUri).into(this.act_photo_edit_imageview);
            getRealFilePath(this.activity, resultUri);
        }
    }

    public static String getRealFilePath(Context context, Uri uri) {
        Cursor cursor;
        int index;
        if (uri == null) {
            return null;
        }
        String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if ("file".equals(scheme)) {
            data = uri.getPath();
        } else if ("content".equals(scheme) && (cursor = context.getContentResolver().query(uri, new String[]{"_data"}, (String) null, (String[]) null, (String) null)) != null) {
            if (cursor.moveToFirst() && (index = cursor.getColumnIndex("_data")) > -1) {
                data = cursor.getString(index);
            }
            cursor.close();
        }
        return data;
    }

    public String saveMyBitmap(Bitmap bmp) {
        String paintPath = "/sdcard/tupian" + System.currentTimeMillis() + ".jpg";
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(new File(paintPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        return paintPath;
    }

    private void handleCropError(@NonNull Intent result) {
        Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Log.e(TAG, "handleCropError: ", cropError);
            Toast.makeText(this, cropError.getMessage(), 1).show();
            return;
        }
        Toast.makeText(this, C0853R.string.toast_unexpected_error, 0).show();
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 21:
                if (resultCode == -1 && data != null) {
                    this.act_photo_edit_return_and_go.setVisibility(0);
                    String paintPath = data.getStringExtra(IntentKey.PAINTPATH);
                    Glide.with(this.activity).load(paintPath).into(this.act_photo_edit_imageview);
                    removeNumsItem(this.photoEditList, this.position + 1);
                    this.photo_edit_path = paintPath;
                    this.photoEditListAll.add(this.photo_edit_path);
                    this.photoEditList.add(this.photo_edit_path);
                    this.position = this.photoEditList.size() - 1;
                    Log.e("-------------", "----------photoEditList------" + this.photoEditList);
                    return;
                }
                return;
            case 22:
                if (resultCode == -1 && data != null) {
                    this.act_photo_edit_return_and_go.setVisibility(0);
                    String paintPath2 = data.getStringExtra(IntentKey.PAINTPATH);
                    Glide.with(this.activity).load(paintPath2).into(this.act_photo_edit_imageview);
                    removeNumsItem(this.photoEditList, this.position + 1);
                    this.photo_edit_path = paintPath2;
                    this.photoEditListAll.add(this.photo_edit_path);
                    this.photoEditList.add(this.photo_edit_path);
                    this.position = this.photoEditList.size() - 1;
                    Log.e("-------------", "----------photoEditList------" + this.photoEditList);
                    return;
                }
                return;
            case 23:
                if (resultCode == -1 && data != null) {
                    this.act_photo_edit_return_and_go.setVisibility(0);
                    String paintPath3 = data.getStringExtra(IntentKey.PAINTPATH);
                    Glide.with(this.activity).load(paintPath3).into(this.act_photo_edit_imageview);
                    removeNumsItem(this.photoEditList, this.position + 1);
                    this.photo_edit_path = paintPath3;
                    this.photoEditListAll.add(this.photo_edit_path);
                    this.photoEditList.add(this.photo_edit_path);
                    this.position = this.photoEditList.size() - 1;
                    Log.e("-------------", "----------photoEditList------" + this.photoEditList);
                    return;
                }
                return;
            case 24:
                if (resultCode == -1 && data != null) {
                    this.act_photo_edit_return_and_go.setVisibility(0);
                    String paintPath4 = data.getStringExtra(IntentKey.PAINTPATH);
                    Glide.with(this.activity).load(paintPath4).into(this.act_photo_edit_imageview);
                    removeNumsItem(this.photoEditList, this.position + 1);
                    this.photo_edit_path = paintPath4;
                    this.photoEditListAll.add(this.photo_edit_path);
                    this.photoEditList.add(this.photo_edit_path);
                    this.position = this.photoEditList.size() - 1;
                    Log.e("-------------", "----------photoEditList------" + this.photoEditList);
                    return;
                }
                return;
            case 25:
                if (resultCode == -1 && data != null) {
                    this.act_photo_edit_return_and_go.setVisibility(0);
                    String paintPath5 = data.getStringExtra(IntentKey.PAINTPATH);
                    Glide.with(this.activity).load(paintPath5).into(this.act_photo_edit_imageview);
                    removeNumsItem(this.photoEditList, this.position + 1);
                    this.photo_edit_path = paintPath5;
                    this.photoEditListAll.add(this.photo_edit_path);
                    this.photoEditList.add(this.photo_edit_path);
                    this.position = this.photoEditList.size() - 1;
                    Log.e("-------------", "----------photoEditList------" + this.photoEditList);
                    return;
                }
                return;
            case 69:
                if (resultCode == -1) {
                    this.act_photo_edit_return_and_go.setVisibility(0);
                    handleCropResult(data);
                    return;
                }
                return;
            case 96:
                handleCropError(data);
                return;
            default:
                return;
        }
    }

    private List removeNumsItem(List list, int saveCount) {
        if (list != null) {
            int size = list.size();
            if (list.size() > saveCount) {
                for (int i = size; i > saveCount; i--) {
                    list.remove(i - 1);
                }
            }
        }
        return list;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 || event.getAction() != 0) {
            return super.onKeyDown(keyCode, event);
        }
        if (this.photoEditListAll.size() > 0) {
            for (int a = 0; a < this.photoEditListAll.size(); a++) {
                new File(this.photoEditListAll.get(a).toString()).delete();
            }
        }
        finish();
        return true;
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    public void showProgress(String msg) {
        if (this.mProgressDialog == null) {
            this.mProgressDialog = new LoadingView(this.activity);
            this.mProgressDialog.setCancelable(true);
            this.mProgressDialog.setCanceledOnTouchOutside(false);
        }
        this.mProgressDialog.setMessage(msg);
        this.mProgressDialog.show();
    }

    public void hideProgress() {
        if (this.mProgressDialog != null) {
            this.mProgressDialog.dismiss();
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
