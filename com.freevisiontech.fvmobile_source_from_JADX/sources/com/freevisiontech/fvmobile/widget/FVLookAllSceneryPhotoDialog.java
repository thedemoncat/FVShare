package com.freevisiontech.fvmobile.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.p001v4.app.FragmentManager;
import android.support.p001v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.activity.FVMainActivity;
import com.freevisiontech.fvmobile.utils.LruCacheUtils;
import java.util.List;
import p010me.iwf.photopicker.fragment.ImagePagerFragment;

public class FVLookAllSceneryPhotoDialog extends BaseDialog implements View.OnClickListener {
    private ImageView act_look_all_scenery_photo_close;

    /* renamed from: fm */
    private FragmentManager f1107fm;

    /* renamed from: ft */
    private FragmentTransaction f1108ft;
    private ImagePagerFragment imagePagerFragment;
    private Context mContext;
    private LinearLayout photoPagerFragment;
    private final ImageView photo_pager_image_view = ((ImageView) findViewById(C0853R.C0855id.photo_pager_image_view));
    private List<String> strings;
    private View viewScale;

    public FVLookAllSceneryPhotoDialog(Context context, List list) {
        super(context, C0853R.layout.dialog_look_all_scenery_photo);
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            public void onSystemUiVisibilityChange(int visibility) {
                int uiOptions;
                if (Build.VERSION.SDK_INT >= 19) {
                    uiOptions = 1798 | 4096;
                } else {
                    uiOptions = 1798 | 1;
                }
                FVLookAllSceneryPhotoDialog.this.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
            }
        });
        this.mContext = context;
        FVMainActivity fVMainActivity = (FVMainActivity) context;
        getWindow().setLayout(-1, -1);
        setDialogAttributes(context);
        this.strings = list;
        String path = this.strings.get(0);
        Bitmap bitmap = LruCacheUtils.getInstance().getBitmapFromMemCache(path);
        if (bitmap != null) {
            this.photo_pager_image_view.setImageBitmap(bitmap);
        } else {
            displayImageTarget(this.photo_pager_image_view, path, getTarget(this.photo_pager_image_view, path));
        }
        this.act_look_all_scenery_photo_close = (ImageView) findViewById(C0853R.C0855id.act_look_all_scenery_photo_close);
        this.act_look_all_scenery_photo_close.setOnClickListener(this);
    }

    private BitmapImageViewTarget getTarget(ImageView imageView, final String url) {
        return new BitmapImageViewTarget(imageView) {
            /* access modifiers changed from: protected */
            public void setResource(Bitmap resource) {
                super.setResource(resource);
                LruCacheUtils.getInstance().addBitmapToMemoryCache(url, resource);
            }
        };
    }

    public void displayImageTarget(ImageView imageView, String url, BitmapImageViewTarget target) {
        Glide.get(imageView.getContext());
        Glide.with(imageView.getContext()).load(url).asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE).into(target);
    }

    private void setDialogAttributes(Context context) {
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = -1;
        lp.height = -1;
        dialogWindow.setGravity(80);
        dialogWindow.setAttributes(lp);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0853R.C0855id.act_look_all_scenery_photo_close:
                dismiss();
                return;
            default:
                return;
        }
    }
}
