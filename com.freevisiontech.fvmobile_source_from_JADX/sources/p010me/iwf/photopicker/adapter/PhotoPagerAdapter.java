package p010me.iwf.photopicker.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.p001v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.p007ny.ijk.upplayer.p015ui.PlayActivity;
import java.io.File;
import java.util.ArrayList;
import p010me.iwf.photopicker.C1661R;
import p010me.iwf.photopicker.utils.AndroidLifecycleUtils;
import p010me.iwf.photopicker.utils.PpEventConstant;
import p010me.iwf.photopicker.widget.control.DataChangeNotification;
import p010me.iwf.photopicker.widget.control.IssueKey;

/* renamed from: me.iwf.photopicker.adapter.PhotoPagerAdapter */
public class PhotoPagerAdapter extends PagerAdapter {
    private ImageView act_play_video_buttom;
    /* access modifiers changed from: private */
    public Activity activity;
    /* access modifiers changed from: private */
    public Context context;
    private RequestManager mGlide;
    private String path;
    /* access modifiers changed from: private */
    public ArrayList<String> paths = new ArrayList<>();

    public PhotoPagerAdapter(RequestManager glide, ArrayList<String> paths2, Activity activity2) {
        this.paths = paths2;
        this.mGlide = glide;
        this.activity = activity2;
    }

    public Object instantiateItem(ViewGroup container, final int position) {
        Uri uri;
        this.context = container.getContext();
        View itemView = LayoutInflater.from(this.context).inflate(C1661R.layout.__picker_picker_item_pager, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(C1661R.C1663id.iv_pager);
        this.act_play_video_buttom = (ImageView) itemView.findViewById(C1661R.C1663id.act_play_video_buttom);
        String camera = this.context.getSharedPreferences("user", 0).getString("camera", "");
        if (camera.equals("0")) {
            this.act_play_video_buttom.setVisibility(8);
        } else if (camera.equals(BleConstant.SHUTTER)) {
            this.act_play_video_buttom.setVisibility(0);
        }
        this.path = this.paths.get(position);
        if (this.path.startsWith("http")) {
            uri = Uri.parse(this.path);
        } else {
            uri = Uri.fromFile(new File(this.path));
        }
        if (AndroidLifecycleUtils.canLoadImage(this.context)) {
            this.mGlide.load(uri).placeholder(C1661R.C1662drawable.__picker_ic_photo_black_48dp).error(C1661R.C1662drawable.__picker_ic_broken_image_black_48dp).into(imageView);
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if ((PhotoPagerAdapter.this.context instanceof Activity) && !((Activity) PhotoPagerAdapter.this.context).isFinishing()) {
                    String look = PhotoPagerAdapter.this.context.getSharedPreferences("user", 0).getString("camera", "");
                    if (look.equals("0")) {
                        DataChangeNotification.getInstance().notifyDataChanged(IssueKey.LOOK_PHOTO_TOP_BUTTOM);
                    } else if (look.equals(BleConstant.SHUTTER)) {
                        DataChangeNotification.getInstance().notifyDataChanged(IssueKey.LOOK_VIDEO_TOP_BUTTOM);
                    }
                }
            }
        });
        this.act_play_video_buttom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(new Intent(PhotoPagerAdapter.this.activity, PlayActivity.class));
                intent.putStringArrayListExtra("urls", PhotoPagerAdapter.this.paths);
                intent.putExtra("position", position);
                PhotoPagerAdapter.this.activity.startActivity(intent);
            }
        });
        container.addView(itemView);
        return itemView;
    }

    public int getCount() {
        return this.paths.size();
    }

    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        Glide.clear((View) object);
    }

    public int getItemPosition(Object object) {
        return -2;
    }

    public void playByPtz() {
        Log.e("ViltaX", "--case STATUS_PLAY:");
        Intent intent = new Intent(new Intent(this.activity, PlayActivity.class));
        intent.putStringArrayListExtra("urls", this.paths);
        intent.putExtra("position", PpEventConstant.FM210_VIDEO_POSITION);
        this.activity.startActivity(intent);
    }
}
