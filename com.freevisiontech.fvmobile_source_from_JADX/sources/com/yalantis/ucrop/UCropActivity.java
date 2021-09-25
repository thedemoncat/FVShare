package com.yalantis.ucrop;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.p001v4.content.ContextCompat;
import android.support.p003v7.app.ActionBar;
import android.support.p003v7.app.AppCompatActivity;
import android.support.p003v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.callback.BitmapCropCallback;
import com.yalantis.ucrop.model.AspectRatio;
import com.yalantis.ucrop.util.SelectedStateListDrawable;
import com.yalantis.ucrop.view.CropImageView;
import com.yalantis.ucrop.view.GestureCropImageView;
import com.yalantis.ucrop.view.OverlayView;
import com.yalantis.ucrop.view.TransformImageView;
import com.yalantis.ucrop.view.UCropView;
import com.yalantis.ucrop.view.widget.AspectRatioTextView;
import com.yalantis.ucrop.view.widget.HorizontalProgressWheelView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class UCropActivity extends AppCompatActivity {
    public static final int ALL = 3;
    public static final Bitmap.CompressFormat DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.JPEG;
    public static final int DEFAULT_COMPRESS_QUALITY = 90;
    public static final int NONE = 0;
    public static final int ROTATE = 2;
    private static final int ROTATE_WIDGET_SENSITIVITY_COEFFICIENT = 42;
    public static final int SCALE = 1;
    private static final int SCALE_WIDGET_SENSITIVITY_COEFFICIENT = 15000;
    private static final int TABS_COUNT = 3;
    private static final String TAG = "UCropActivity";
    private int mActiveWidgetColor;
    private int[] mAllowedGestures = {1, 2, 3};
    /* access modifiers changed from: private */
    public View mBlockingView;
    private Bitmap.CompressFormat mCompressFormat = DEFAULT_COMPRESS_FORMAT;
    private int mCompressQuality = 90;
    /* access modifiers changed from: private */
    public List<ViewGroup> mCropAspectRatioViews = new ArrayList();
    /* access modifiers changed from: private */
    public GestureCropImageView mGestureCropImageView;
    private TransformImageView.TransformImageListener mImageListener = new TransformImageView.TransformImageListener() {
        public void onRotate(float currentAngle) {
            UCropActivity.this.setAngleText(currentAngle);
        }

        public void onScale(float currentScale) {
            UCropActivity.this.setScaleText(currentScale);
        }

        public void onLoadComplete() {
            UCropActivity.this.mUCropView.animate().alpha(1.0f).setDuration(300).setInterpolator(new AccelerateInterpolator());
            UCropActivity.this.mBlockingView.setClickable(false);
            boolean unused = UCropActivity.this.mShowLoader = false;
            UCropActivity.this.supportInvalidateOptionsMenu();
        }

        public void onLoadFailure(@NonNull Exception e) {
            UCropActivity.this.setResultError(e);
            UCropActivity.this.finish();
        }
    };
    private ViewGroup mLayoutAspectRatio;
    private ViewGroup mLayoutRotate;
    private ViewGroup mLayoutScale;
    private int mLogoColor;
    private OverlayView mOverlayView;
    @ColorInt
    private int mRootViewBackgroundColor;
    private boolean mShowBottomControls;
    /* access modifiers changed from: private */
    public boolean mShowLoader = true;
    private final View.OnClickListener mStateClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (!v.isSelected()) {
                UCropActivity.this.setWidgetState(v.getId());
            }
        }
    };
    private ViewGroup mState_rotate_duihao;
    private int mStatusBarColor;
    private TextView mTextViewRotateAngle;
    private TextView mTextViewScalePercent;
    @DrawableRes
    private int mToolbarCancelDrawable;
    private int mToolbarColor;
    @DrawableRes
    private int mToolbarCropDrawable;
    private String mToolbarTitle;
    private int mToolbarWidgetColor;
    /* access modifiers changed from: private */
    public UCropView mUCropView;
    private ViewGroup mWrapperStateAspectRatio;
    private ViewGroup mWrapperStateRotate;
    private ViewGroup mWrapperStateScale;
    private ViewGroup state_rotate_dismiss;
    private AspectRatioTextView state_rotate_textview_imageview00;
    private AspectRatioTextView state_rotate_textview_imageview11;
    private AspectRatioTextView state_rotate_textview_imageview169;
    private AspectRatioTextView state_rotate_textview_imageview34;
    private AspectRatioTextView state_rotate_textview_imageview43;
    private AspectRatioTextView state_rotate_textview_imageview916;

    @Retention(RetentionPolicy.SOURCE)
    public @interface GestureTypes {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C1654R.layout.ucrop_activity_photobox);
        Intent intent = getIntent();
        setupViews(intent);
        setImageData(intent);
        setInitialState();
        addBlockingView();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C1654R.C1657menu.ucrop_menu_activity, menu);
        MenuItem menuItemLoader = menu.findItem(C1654R.C1656id.menu_loader);
        Drawable menuItemLoaderIcon = menuItemLoader.getIcon();
        if (menuItemLoaderIcon != null) {
            try {
                menuItemLoaderIcon.mutate();
                menuItemLoaderIcon.setColorFilter(this.mToolbarWidgetColor, PorterDuff.Mode.SRC_ATOP);
                menuItemLoader.setIcon(menuItemLoaderIcon);
            } catch (IllegalStateException e) {
                Log.i(TAG, String.format("%s - %s", new Object[]{e.getMessage(), getString(C1654R.string.ucrop_mutate_exception_hint)}));
            }
            ((Animatable) menuItemLoader.getIcon()).start();
        }
        MenuItem menuItemCrop = menu.findItem(C1654R.C1656id.menu_crop);
        Drawable menuItemCropIcon = ContextCompat.getDrawable(this, this.mToolbarCropDrawable);
        if (menuItemCropIcon != null) {
            menuItemCropIcon.mutate();
            menuItemCropIcon.setColorFilter(this.mToolbarWidgetColor, PorterDuff.Mode.SRC_ATOP);
            menuItemCrop.setIcon(menuItemCropIcon);
        }
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(C1654R.C1656id.menu_crop).setVisible(!this.mShowLoader);
        menu.findItem(C1654R.C1656id.menu_loader).setVisible(this.mShowLoader);
        return super.onPrepareOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == C1654R.C1656id.menu_crop) {
            cropAndSaveImage();
        } else if (item.getItemId() == 16908332) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        if (this.mGestureCropImageView != null) {
            this.mGestureCropImageView.cancelAllAnimations();
        }
    }

    private void setImageData(@NonNull Intent intent) {
        Uri inputUri = (Uri) intent.getParcelableExtra(UCrop.EXTRA_INPUT_URI);
        Uri outputUri = (Uri) intent.getParcelableExtra(UCrop.EXTRA_OUTPUT_URI);
        processOptions(intent);
        if (inputUri == null || outputUri == null) {
            setResultError(new NullPointerException(getString(C1654R.string.ucrop_error_input_data_is_absent)));
            finish();
            return;
        }
        try {
            this.mGestureCropImageView.setImageUri(inputUri, outputUri);
        } catch (Exception e) {
            setResultError(e);
            finish();
        }
    }

    private void processOptions(@NonNull Intent intent) {
        String compressionFormatName = intent.getStringExtra(UCrop.Options.EXTRA_COMPRESSION_FORMAT_NAME);
        Bitmap.CompressFormat compressFormat = null;
        if (!TextUtils.isEmpty(compressionFormatName)) {
            compressFormat = Bitmap.CompressFormat.valueOf(compressionFormatName);
        }
        if (compressFormat == null) {
            compressFormat = DEFAULT_COMPRESS_FORMAT;
        }
        this.mCompressFormat = compressFormat;
        this.mCompressQuality = intent.getIntExtra(UCrop.Options.EXTRA_COMPRESSION_QUALITY, 90);
        int[] allowedGestures = intent.getIntArrayExtra(UCrop.Options.EXTRA_ALLOWED_GESTURES);
        if (allowedGestures != null && allowedGestures.length == 3) {
            this.mAllowedGestures = allowedGestures;
        }
        this.mGestureCropImageView.setMaxBitmapSize(intent.getIntExtra(UCrop.Options.EXTRA_MAX_BITMAP_SIZE, 0));
        this.mGestureCropImageView.setMaxScaleMultiplier(intent.getFloatExtra(UCrop.Options.EXTRA_MAX_SCALE_MULTIPLIER, 10.0f));
        this.mGestureCropImageView.setImageToWrapCropBoundsAnimDuration((long) intent.getIntExtra(UCrop.Options.EXTRA_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION, CropImageView.DEFAULT_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION));
        this.mOverlayView.setFreestyleCropEnabled(intent.getBooleanExtra(UCrop.Options.EXTRA_FREE_STYLE_CROP, false));
        this.mOverlayView.setDimmedColor(intent.getIntExtra(UCrop.Options.EXTRA_DIMMED_LAYER_COLOR, getResources().getColor(C1654R.color.ucrop_color_default_dimmed)));
        this.mOverlayView.setCircleDimmedLayer(intent.getBooleanExtra(UCrop.Options.EXTRA_CIRCLE_DIMMED_LAYER, false));
        this.mOverlayView.setShowCropFrame(intent.getBooleanExtra(UCrop.Options.EXTRA_SHOW_CROP_FRAME, true));
        this.mOverlayView.setCropFrameColor(intent.getIntExtra(UCrop.Options.EXTRA_CROP_FRAME_COLOR, getResources().getColor(C1654R.color.ucrop_color_default_crop_frame)));
        this.mOverlayView.setCropFrameStrokeWidth(intent.getIntExtra(UCrop.Options.EXTRA_CROP_FRAME_STROKE_WIDTH, getResources().getDimensionPixelSize(C1654R.dimen.ucrop_default_crop_frame_stoke_width)));
        this.mOverlayView.setShowCropGrid(intent.getBooleanExtra(UCrop.Options.EXTRA_SHOW_CROP_GRID, true));
        this.mOverlayView.setCropGridRowCount(intent.getIntExtra(UCrop.Options.EXTRA_CROP_GRID_ROW_COUNT, 2));
        this.mOverlayView.setCropGridColumnCount(intent.getIntExtra(UCrop.Options.EXTRA_CROP_GRID_COLUMN_COUNT, 2));
        this.mOverlayView.setCropGridColor(intent.getIntExtra(UCrop.Options.EXTRA_CROP_GRID_COLOR, getResources().getColor(C1654R.color.ucrop_color_default_crop_grid)));
        this.mOverlayView.setCropGridStrokeWidth(intent.getIntExtra(UCrop.Options.EXTRA_CROP_GRID_STROKE_WIDTH, getResources().getDimensionPixelSize(C1654R.dimen.ucrop_default_crop_grid_stoke_width)));
        float aspectRatioX = intent.getFloatExtra(UCrop.EXTRA_ASPECT_RATIO_X, 0.0f);
        float aspectRatioY = intent.getFloatExtra(UCrop.EXTRA_ASPECT_RATIO_Y, 0.0f);
        int aspectRationSelectedByDefault = intent.getIntExtra(UCrop.Options.EXTRA_ASPECT_RATIO_SELECTED_BY_DEFAULT, 0);
        ArrayList<AspectRatio> aspectRatioList = intent.getParcelableArrayListExtra(UCrop.Options.EXTRA_ASPECT_RATIO_OPTIONS);
        if (aspectRatioX > 0.0f && aspectRatioY > 0.0f) {
            if (this.mWrapperStateAspectRatio != null) {
                this.mWrapperStateAspectRatio.setVisibility(8);
            }
            this.mGestureCropImageView.setTargetAspectRatio(aspectRatioX / aspectRatioY);
        } else if (aspectRatioList == null || aspectRationSelectedByDefault >= aspectRatioList.size()) {
            this.mGestureCropImageView.setTargetAspectRatio(0.0f);
        } else {
            this.mGestureCropImageView.setTargetAspectRatio(aspectRatioList.get(aspectRationSelectedByDefault).getAspectRatioX() / aspectRatioList.get(aspectRationSelectedByDefault).getAspectRatioY());
        }
        int maxSizeX = intent.getIntExtra(UCrop.EXTRA_MAX_SIZE_X, 0);
        int maxSizeY = intent.getIntExtra(UCrop.EXTRA_MAX_SIZE_Y, 0);
        if (maxSizeX > 0 && maxSizeY > 0) {
            this.mGestureCropImageView.setMaxResultImageSizeX(maxSizeX);
            this.mGestureCropImageView.setMaxResultImageSizeY(maxSizeY);
        }
    }

    private void setupViews(@NonNull Intent intent) {
        boolean z;
        this.mStatusBarColor = intent.getIntExtra(UCrop.Options.EXTRA_STATUS_BAR_COLOR, ContextCompat.getColor(this, C1654R.color.ucrop_color_toolbar_widget));
        this.mToolbarColor = intent.getIntExtra(UCrop.Options.EXTRA_TOOL_BAR_COLOR, ContextCompat.getColor(this, C1654R.color.ucrop_color_toolbar_widget));
        this.mActiveWidgetColor = intent.getIntExtra(UCrop.Options.EXTRA_UCROP_COLOR_WIDGET_ACTIVE, ContextCompat.getColor(this, C1654R.color.ucrop_color_widget_active));
        this.mToolbarWidgetColor = intent.getIntExtra(UCrop.Options.EXTRA_UCROP_WIDGET_COLOR_TOOLBAR, ContextCompat.getColor(this, C1654R.color.ucrop_color_toolbar_widget));
        this.mToolbarCancelDrawable = intent.getIntExtra(UCrop.Options.EXTRA_UCROP_WIDGET_CANCEL_DRAWABLE, C1654R.C1655drawable.ucrop_ic_cross);
        this.mToolbarCropDrawable = intent.getIntExtra(UCrop.Options.EXTRA_UCROP_WIDGET_CROP_DRAWABLE, C1654R.C1655drawable.ucrop_ic_done);
        this.mToolbarTitle = intent.getStringExtra(UCrop.Options.EXTRA_UCROP_TITLE_TEXT_TOOLBAR);
        this.mToolbarTitle = this.mToolbarTitle != null ? this.mToolbarTitle : getResources().getString(C1654R.string.ucrop_label_edit_photo);
        this.mLogoColor = intent.getIntExtra(UCrop.Options.EXTRA_UCROP_LOGO_COLOR, ContextCompat.getColor(this, C1654R.color.ucrop_color_default_logo));
        if (!intent.getBooleanExtra(UCrop.Options.EXTRA_HIDE_BOTTOM_CONTROLS, false)) {
            z = true;
        } else {
            z = false;
        }
        this.mShowBottomControls = z;
        this.mRootViewBackgroundColor = intent.getIntExtra(UCrop.Options.EXTRA_UCROP_ROOT_VIEW_BACKGROUND_COLOR, ContextCompat.getColor(this, C1654R.color.ucrop_color_crop_background));
        initiateRootViews();
        if (this.mShowBottomControls) {
            View.inflate(this, C1654R.layout.ucrop_controls, (ViewGroup) findViewById(C1654R.C1656id.ucrop_photobox));
            this.mWrapperStateAspectRatio = (ViewGroup) findViewById(C1654R.C1656id.state_aspect_ratio);
            this.mWrapperStateAspectRatio.setOnClickListener(this.mStateClickListener);
            this.mWrapperStateRotate = (ViewGroup) findViewById(C1654R.C1656id.state_rotate);
            this.mWrapperStateRotate.setOnClickListener(this.mStateClickListener);
            this.mWrapperStateScale = (ViewGroup) findViewById(C1654R.C1656id.state_scale);
            this.mWrapperStateScale.setOnClickListener(this.mStateClickListener);
            this.mLayoutAspectRatio = (ViewGroup) findViewById(C1654R.C1656id.layout_aspect_ratio);
            this.mLayoutRotate = (ViewGroup) findViewById(C1654R.C1656id.layout_rotate_wheel);
            this.mLayoutScale = (ViewGroup) findViewById(C1654R.C1656id.layout_scale_wheel);
            setupAspectRatioWidget(intent);
            setupRotateWidget();
            setupScaleWidget();
            setupStatesWrapper();
            this.state_rotate_textview_imageview11 = (AspectRatioTextView) findViewById(C1654R.C1656id.state_rotate_textview_imageview11);
            this.state_rotate_textview_imageview34 = (AspectRatioTextView) findViewById(C1654R.C1656id.state_rotate_textview_imageview34);
            this.state_rotate_textview_imageview00 = (AspectRatioTextView) findViewById(C1654R.C1656id.state_rotate_textview_imageview00);
            this.state_rotate_textview_imageview43 = (AspectRatioTextView) findViewById(C1654R.C1656id.state_rotate_textview_imageview43);
            this.state_rotate_textview_imageview916 = (AspectRatioTextView) findViewById(C1654R.C1656id.state_rotate_textview_imageview916);
            this.state_rotate_textview_imageview169 = (AspectRatioTextView) findViewById(C1654R.C1656id.state_rotate_textview_imageview169);
            setCheckCaiJianBiLi(3);
            this.mState_rotate_duihao = (ViewGroup) findViewById(C1654R.C1656id.state_rotate_duihao);
            this.mState_rotate_duihao.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    UCropActivity.this.cropAndSaveImage();
                }
            });
            this.state_rotate_dismiss = (ViewGroup) findViewById(C1654R.C1656id.state_rotate_dismiss);
            this.state_rotate_dismiss.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    UCropActivity.this.onBackPressed();
                }
            });
            ((ViewGroup) findViewById(C1654R.C1656id.state_rotate_textview11)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    UCropActivity.this.setCheckCaiJianBiLi(1);
                    UCropActivity.this.mGestureCropImageView.setTargetAspectRatio(1.0f);
                    UCropActivity.this.mGestureCropImageView.setImageToWrapCropBounds();
                }
            });
            ((ViewGroup) findViewById(C1654R.C1656id.state_rotate_textview34)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    UCropActivity.this.setCheckCaiJianBiLi(2);
                    UCropActivity.this.mGestureCropImageView.setTargetAspectRatio(0.75f);
                    UCropActivity.this.mGestureCropImageView.setImageToWrapCropBounds();
                }
            });
            ((ViewGroup) findViewById(C1654R.C1656id.state_rotate_textview00)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    UCropActivity.this.setCheckCaiJianBiLi(3);
                    UCropActivity.this.mGestureCropImageView.setTargetAspectRatio(0.0f);
                    UCropActivity.this.mGestureCropImageView.setImageToWrapCropBounds();
                }
            });
            ((ViewGroup) findViewById(C1654R.C1656id.state_rotate_textview43)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    UCropActivity.this.setCheckCaiJianBiLi(4);
                    UCropActivity.this.mGestureCropImageView.setTargetAspectRatio(1.3333334f);
                    UCropActivity.this.mGestureCropImageView.setImageToWrapCropBounds();
                }
            });
            ((ViewGroup) findViewById(C1654R.C1656id.state_rotate_textview916)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    UCropActivity.this.setCheckCaiJianBiLi(5);
                    UCropActivity.this.mGestureCropImageView.setTargetAspectRatio(0.5625f);
                    UCropActivity.this.mGestureCropImageView.setImageToWrapCropBounds();
                }
            });
            ((ViewGroup) findViewById(C1654R.C1656id.state_rotate_textview169)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    UCropActivity.this.setCheckCaiJianBiLi(6);
                    UCropActivity.this.mGestureCropImageView.setTargetAspectRatio(1.7777778f);
                    UCropActivity.this.mGestureCropImageView.setImageToWrapCropBounds();
                }
            });
            findViewById(C1654R.C1656id.wrapper_reset_rotate0).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    UCropActivity.this.resetRotation();
                }
            });
            findViewById(C1654R.C1656id.wrapper_rotate_by_angle0).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    UCropActivity.this.rotateByAngle(90);
                }
            });
            findViewById(C1654R.C1656id.wrapper_rotate_by_left_angle0).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    UCropActivity.this.rotateByAngle(-90);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void setCheckCaiJianBiLi(int i) {
        this.state_rotate_textview_imageview11.setBackgroundResource(C1654R.mipmap.ic_edit_caijain_11_no);
        this.state_rotate_textview_imageview34.setBackgroundResource(C1654R.mipmap.ic_edit_caijain_34_no);
        this.state_rotate_textview_imageview00.setBackgroundResource(C1654R.mipmap.ic_edit_caijain_yuanshi2);
        this.state_rotate_textview_imageview43.setBackgroundResource(C1654R.mipmap.ic_edit_caijain_43_no);
        this.state_rotate_textview_imageview916.setBackgroundResource(C1654R.mipmap.ic_edit_caijain_916_no);
        this.state_rotate_textview_imageview169.setBackgroundResource(C1654R.mipmap.ic_edit_caijain_169_no);
        switch (i) {
            case 1:
                this.state_rotate_textview_imageview11.setBackgroundResource(C1654R.mipmap.ic_edit_caijain_11_check);
                return;
            case 2:
                this.state_rotate_textview_imageview34.setBackgroundResource(C1654R.mipmap.ic_edit_caijain_34_check);
                return;
            case 3:
                this.state_rotate_textview_imageview00.setBackgroundResource(C1654R.mipmap.ic_edit_caijain_yuanshi1);
                return;
            case 4:
                this.state_rotate_textview_imageview43.setBackgroundResource(C1654R.mipmap.ic_edit_caijain_43_check);
                return;
            case 5:
                this.state_rotate_textview_imageview916.setBackgroundResource(C1654R.mipmap.ic_edit_caijain_916_check);
                return;
            case 6:
                this.state_rotate_textview_imageview169.setBackgroundResource(C1654R.mipmap.ic_edit_caijain_169_check);
                return;
            default:
                return;
        }
    }

    private void setupAppBar() {
        setStatusBarColor(this.mStatusBarColor);
        Toolbar toolbar = (Toolbar) findViewById(C1654R.C1656id.toolbar);
        toolbar.setBackgroundColor(this.mToolbarColor);
        toolbar.setTitleTextColor(this.mToolbarWidgetColor);
        TextView toolbarTitle = (TextView) toolbar.findViewById(C1654R.C1656id.toolbar_title);
        toolbarTitle.setTextColor(this.mToolbarWidgetColor);
        toolbarTitle.setText(this.mToolbarTitle);
        Drawable stateButtonDrawable = ContextCompat.getDrawable(this, this.mToolbarCancelDrawable).mutate();
        stateButtonDrawable.setColorFilter(this.mToolbarWidgetColor, PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationIcon(stateButtonDrawable);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void initiateRootViews() {
        this.mUCropView = (UCropView) findViewById(C1654R.C1656id.ucrop);
        this.mGestureCropImageView = this.mUCropView.getCropImageView();
        this.mOverlayView = this.mUCropView.getOverlayView();
        this.mGestureCropImageView.setTransformImageListener(this.mImageListener);
        ((ImageView) findViewById(C1654R.C1656id.image_view_logo)).setColorFilter(this.mLogoColor, PorterDuff.Mode.SRC_ATOP);
        findViewById(C1654R.C1656id.ucrop_frame).setBackgroundColor(this.mRootViewBackgroundColor);
    }

    private void setupStatesWrapper() {
        ImageView stateScaleImageView = (ImageView) findViewById(C1654R.C1656id.image_view_state_scale);
        ImageView stateRotateImageView = (ImageView) findViewById(C1654R.C1656id.image_view_state_rotate);
        ImageView stateAspectRatioImageView = (ImageView) findViewById(C1654R.C1656id.image_view_state_aspect_ratio);
        stateScaleImageView.setImageDrawable(new SelectedStateListDrawable(stateScaleImageView.getDrawable(), this.mActiveWidgetColor));
        stateRotateImageView.setImageDrawable(new SelectedStateListDrawable(stateRotateImageView.getDrawable(), this.mActiveWidgetColor));
        stateAspectRatioImageView.setImageDrawable(new SelectedStateListDrawable(stateAspectRatioImageView.getDrawable(), this.mActiveWidgetColor));
    }

    @TargetApi(21)
    private void setStatusBarColor(@ColorInt int color) {
        Window window;
        if (Build.VERSION.SDK_INT >= 21 && (window = getWindow()) != null) {
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(color);
        }
    }

    private void setupAspectRatioWidget(@NonNull Intent intent) {
        int aspectRationSelectedByDefault = intent.getIntExtra(UCrop.Options.EXTRA_ASPECT_RATIO_SELECTED_BY_DEFAULT, 0);
        ArrayList<AspectRatio> aspectRatioList = intent.getParcelableArrayListExtra(UCrop.Options.EXTRA_ASPECT_RATIO_OPTIONS);
        if (aspectRatioList == null || aspectRatioList.isEmpty()) {
            aspectRationSelectedByDefault = 2;
            aspectRatioList = new ArrayList<>();
            aspectRatioList.add(new AspectRatio((String) null, 1.0f, 1.0f));
            aspectRatioList.add(new AspectRatio((String) null, 3.0f, 4.0f));
            aspectRatioList.add(new AspectRatio(getString(C1654R.string.ucrop_label_original).toUpperCase(), 0.0f, 0.0f));
            aspectRatioList.add(new AspectRatio((String) null, 4.0f, 3.0f));
            aspectRatioList.add(new AspectRatio((String) null, 9.0f, 16.0f));
            aspectRatioList.add(new AspectRatio((String) null, 16.0f, 9.0f));
        }
        LinearLayout wrapperAspectRatioList = (LinearLayout) findViewById(C1654R.C1656id.layout_aspect_ratio);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, -1);
        lp.weight = 1.0f;
        Iterator<AspectRatio> it = aspectRatioList.iterator();
        while (it.hasNext()) {
            FrameLayout wrapperAspectRatio = (FrameLayout) getLayoutInflater().inflate(C1654R.layout.ucrop_aspect_ratio, (ViewGroup) null);
            wrapperAspectRatio.setLayoutParams(lp);
            AspectRatioTextView aspectRatioTextView = (AspectRatioTextView) wrapperAspectRatio.getChildAt(0);
            aspectRatioTextView.setActiveColor(this.mActiveWidgetColor);
            aspectRatioTextView.setAspectRatio(it.next());
            wrapperAspectRatioList.addView(wrapperAspectRatio);
            this.mCropAspectRatioViews.add(wrapperAspectRatio);
        }
        this.mCropAspectRatioViews.get(aspectRationSelectedByDefault).setSelected(true);
        for (ViewGroup cropAspectRatioView : this.mCropAspectRatioViews) {
            cropAspectRatioView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (!v.isSelected()) {
                        UCropActivity.this.mGestureCropImageView.setTargetAspectRatio(((AspectRatioTextView) ((ViewGroup) v).getChildAt(0)).getAspectRatio(v.isSelected()));
                        UCropActivity.this.mGestureCropImageView.setImageToWrapCropBounds();
                        if (!v.isSelected()) {
                            for (ViewGroup cropAspectRatioView : UCropActivity.this.mCropAspectRatioViews) {
                                cropAspectRatioView.setSelected(cropAspectRatioView == v);
                            }
                        }
                    }
                }
            });
        }
    }

    private void setupRotateWidget() {
        this.mTextViewRotateAngle = (TextView) findViewById(C1654R.C1656id.text_view_rotate);
        ((HorizontalProgressWheelView) findViewById(C1654R.C1656id.rotate_scroll_wheel)).setScrollingListener(new HorizontalProgressWheelView.ScrollingListener() {
            public void onScroll(float delta, float totalDistance) {
                UCropActivity.this.mGestureCropImageView.postRotate(delta / 42.0f);
            }

            public void onScrollEnd() {
                UCropActivity.this.mGestureCropImageView.setImageToWrapCropBounds();
            }

            public void onScrollStart() {
                UCropActivity.this.mGestureCropImageView.cancelAllAnimations();
            }
        });
        ((HorizontalProgressWheelView) findViewById(C1654R.C1656id.rotate_scroll_wheel)).setMiddleLineColor(this.mActiveWidgetColor);
        findViewById(C1654R.C1656id.wrapper_reset_rotate).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UCropActivity.this.resetRotation();
            }
        });
        findViewById(C1654R.C1656id.wrapper_rotate_by_angle).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UCropActivity.this.rotateByAngle(90);
            }
        });
        findViewById(C1654R.C1656id.wrapper_rotate_by_left_angle).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UCropActivity.this.rotateByAngle(-90);
            }
        });
    }

    private void setupScaleWidget() {
        this.mTextViewScalePercent = (TextView) findViewById(C1654R.C1656id.text_view_scale);
        ((HorizontalProgressWheelView) findViewById(C1654R.C1656id.scale_scroll_wheel)).setScrollingListener(new HorizontalProgressWheelView.ScrollingListener() {
            public void onScroll(float delta, float totalDistance) {
                if (delta > 0.0f) {
                    UCropActivity.this.mGestureCropImageView.zoomInImage(UCropActivity.this.mGestureCropImageView.getCurrentScale() + (((UCropActivity.this.mGestureCropImageView.getMaxScale() - UCropActivity.this.mGestureCropImageView.getMinScale()) / 15000.0f) * delta));
                } else {
                    UCropActivity.this.mGestureCropImageView.zoomOutImage(UCropActivity.this.mGestureCropImageView.getCurrentScale() + (((UCropActivity.this.mGestureCropImageView.getMaxScale() - UCropActivity.this.mGestureCropImageView.getMinScale()) / 15000.0f) * delta));
                }
            }

            public void onScrollEnd() {
                UCropActivity.this.mGestureCropImageView.setImageToWrapCropBounds();
            }

            public void onScrollStart() {
                UCropActivity.this.mGestureCropImageView.cancelAllAnimations();
            }
        });
        ((HorizontalProgressWheelView) findViewById(C1654R.C1656id.scale_scroll_wheel)).setMiddleLineColor(this.mActiveWidgetColor);
    }

    /* access modifiers changed from: private */
    public void setAngleText(float angle) {
        if (this.mTextViewRotateAngle != null) {
            this.mTextViewRotateAngle.setText(String.format(Locale.getDefault(), "%.1f°", new Object[]{Float.valueOf(angle)}));
        }
    }

    /* access modifiers changed from: private */
    public void setScaleText(float scale) {
        if (this.mTextViewScalePercent != null) {
            this.mTextViewScalePercent.setText(String.format(Locale.getDefault(), "%d%%", new Object[]{Integer.valueOf((int) (100.0f * scale))}));
        }
    }

    /* access modifiers changed from: private */
    public void resetRotation() {
        this.mGestureCropImageView.postRotate(-this.mGestureCropImageView.getCurrentAngle());
        this.mGestureCropImageView.setImageToWrapCropBounds();
    }

    /* access modifiers changed from: private */
    public void rotateByAngle(int angle) {
        this.mGestureCropImageView.postRotate((float) angle);
        this.mGestureCropImageView.setImageToWrapCropBounds();
    }

    private void setInitialState() {
        if (!this.mShowBottomControls) {
            setAllowedGestures(0);
        } else if (this.mWrapperStateAspectRatio.getVisibility() == 0) {
            setWidgetState(C1654R.C1656id.state_aspect_ratio);
        } else {
            setWidgetState(C1654R.C1656id.state_scale);
        }
    }

    /* access modifiers changed from: private */
    public void setWidgetState(@IdRes int stateViewId) {
        boolean z;
        boolean z2;
        boolean z3;
        int i;
        int i2;
        int i3;
        if (this.mShowBottomControls) {
            ViewGroup viewGroup = this.mWrapperStateAspectRatio;
            if (stateViewId == C1654R.C1656id.state_aspect_ratio) {
                z = true;
            } else {
                z = false;
            }
            viewGroup.setSelected(z);
            ViewGroup viewGroup2 = this.mWrapperStateRotate;
            if (stateViewId == C1654R.C1656id.state_rotate) {
                z2 = true;
            } else {
                z2 = false;
            }
            viewGroup2.setSelected(z2);
            ViewGroup viewGroup3 = this.mWrapperStateScale;
            if (stateViewId == C1654R.C1656id.state_scale) {
                z3 = true;
            } else {
                z3 = false;
            }
            viewGroup3.setSelected(z3);
            ViewGroup viewGroup4 = this.mLayoutAspectRatio;
            if (stateViewId == C1654R.C1656id.state_aspect_ratio) {
                i = 0;
            } else {
                i = 8;
            }
            viewGroup4.setVisibility(i);
            ViewGroup viewGroup5 = this.mLayoutRotate;
            if (stateViewId == C1654R.C1656id.state_rotate) {
                i2 = 0;
            } else {
                i2 = 8;
            }
            viewGroup5.setVisibility(i2);
            ViewGroup viewGroup6 = this.mLayoutScale;
            if (stateViewId == C1654R.C1656id.state_scale) {
                i3 = 0;
            } else {
                i3 = 8;
            }
            viewGroup6.setVisibility(i3);
            TextView image_view_state_aspect_ratio_textview1 = (TextView) findViewById(C1654R.C1656id.image_view_state_aspect_ratio_textview1);
            TextView image_view_state_aspect_ratio_textview2 = (TextView) findViewById(C1654R.C1656id.image_view_state_aspect_ratio_textview2);
            if (stateViewId == C1654R.C1656id.state_scale) {
                setAllowedGestures(0);
            } else if (stateViewId == C1654R.C1656id.state_rotate) {
                image_view_state_aspect_ratio_textview1.setTextColor(getResources().getColor(C1654R.color.ucrop_color_c55));
                image_view_state_aspect_ratio_textview2.setTextColor(getResources().getColor(C1654R.color.ucrop_color_black55));
                findViewById(C1654R.C1656id.ucrop_controls_wrapper_reset_rotate).setVisibility(0);
                findViewById(C1654R.C1656id.ucrop_controls_horizontal_scroll_view).setVisibility(8);
                setAllowedGestures(1);
            } else {
                image_view_state_aspect_ratio_textview1.setTextColor(getResources().getColor(C1654R.color.ucrop_color_black55));
                image_view_state_aspect_ratio_textview2.setTextColor(getResources().getColor(C1654R.color.ucrop_color_c55));
                findViewById(C1654R.C1656id.ucrop_controls_wrapper_reset_rotate).setVisibility(8);
                findViewById(C1654R.C1656id.ucrop_controls_horizontal_scroll_view).setVisibility(0);
                setAllowedGestures(2);
            }
        }
    }

    private void setAllowedGestures(int tab) {
        boolean z;
        boolean z2 = false;
        GestureCropImageView gestureCropImageView = this.mGestureCropImageView;
        if (this.mAllowedGestures[tab] == 3 || this.mAllowedGestures[tab] == 1) {
            z = true;
        } else {
            z = false;
        }
        gestureCropImageView.setScaleEnabled(z);
        GestureCropImageView gestureCropImageView2 = this.mGestureCropImageView;
        if (this.mAllowedGestures[tab] == 3 || this.mAllowedGestures[tab] == 2) {
            z2 = true;
        }
        gestureCropImageView2.setRotateEnabled(z2);
    }

    private void addBlockingView() {
        if (this.mBlockingView == null) {
            this.mBlockingView = new View(this);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(-1, -1);
            lp.addRule(3, C1654R.C1656id.toolbar);
            this.mBlockingView.setLayoutParams(lp);
            this.mBlockingView.setClickable(true);
        }
        ((RelativeLayout) findViewById(C1654R.C1656id.ucrop_photobox)).addView(this.mBlockingView);
    }

    /* access modifiers changed from: protected */
    public void cropAndSaveImage() {
        this.mBlockingView.setClickable(true);
        this.mShowLoader = true;
        supportInvalidateOptionsMenu();
        this.mGestureCropImageView.cropAndSaveImage(this.mCompressFormat, this.mCompressQuality, new BitmapCropCallback() {
            public void onBitmapCropped(@NonNull Uri resultUri, int offsetX, int offsetY, int imageWidth, int imageHeight) {
                UCropActivity.this.setResultUri(resultUri, UCropActivity.this.mGestureCropImageView.getTargetAspectRatio(), offsetX, offsetY, imageWidth, imageHeight);
                UCropActivity.this.finish();
            }

            public void onCropFailure(@NonNull Throwable t) {
                UCropActivity.this.setResultError(t);
                UCropActivity.this.finish();
            }
        });
    }

    /* access modifiers changed from: protected */
    public void setResultUri(Uri uri, float resultAspectRatio, int offsetX, int offsetY, int imageWidth, int imageHeight) {
        setResult(-1, new Intent().putExtra(UCrop.EXTRA_OUTPUT_URI, uri).putExtra(UCrop.EXTRA_OUTPUT_CROP_ASPECT_RATIO, resultAspectRatio).putExtra(UCrop.EXTRA_OUTPUT_IMAGE_WIDTH, imageWidth).putExtra(UCrop.EXTRA_OUTPUT_IMAGE_HEIGHT, imageHeight).putExtra(UCrop.EXTRA_OUTPUT_OFFSET_X, offsetX).putExtra(UCrop.EXTRA_OUTPUT_OFFSET_Y, offsetY));
    }

    /* access modifiers changed from: protected */
    public void setResultError(Throwable throwable) {
        setResult(96, new Intent().putExtra(UCrop.EXTRA_ERROR, throwable));
    }
}
