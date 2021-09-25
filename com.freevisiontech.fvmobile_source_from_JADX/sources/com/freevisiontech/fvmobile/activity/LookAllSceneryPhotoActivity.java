package com.freevisiontech.fvmobile.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.p001v4.view.ViewPager;
import android.support.p003v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.utility.BaseActivityManager;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utility.Util;
import com.freevisiontech.fvmobile.widget.view.IntentKey;
import com.umeng.analytics.MobclickAgent;
import java.util.ArrayList;
import java.util.List;
import p010me.iwf.photopicker.fragment.ImagePagerFragment;
import p010me.iwf.photopicker.widget.control.DataChangeNotification;
import p010me.iwf.photopicker.widget.control.IssueKey;
import p010me.iwf.photopicker.widget.control.OnDataChangeObserver;

public class LookAllSceneryPhotoActivity extends AppCompatActivity implements OnDataChangeObserver, View.OnClickListener {
    @Bind({2131755360})
    ImageView act_look_all_scenery_photo_close;
    private Activity activity;
    private ImagePagerFragment pagerFragment;
    private String pathOne;
    private String pathTwo;
    private List<String> paths = new ArrayList();
    private List<String> strings;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = this;
        setContentView((int) C0853R.layout.activity_look_all_scenery_photo);
        ButterKnife.bind((Activity) this);
        getSharedPreferences("user", 0).edit().putString("camera", "0").commit();
        Util.setFullScreen(this);
        BaseActivityManager.getActivityManager().pushActivity(this);
        CameraUtils.setFullShotIng(false);
        this.act_look_all_scenery_photo_close.setOnClickListener(this);
        DataChangeNotification.getInstance().addObserver(IssueKey.LOOK_PHOTO_TOP_BUTTOM, this);
        int currentItem = getIntent().getIntExtra(IntentKey.CLASS_POSION, 0);
        this.strings = getIntent().getStringArrayListExtra(IntentKey.CLASS_JSON);
        this.pagerFragment = (ImagePagerFragment) getSupportFragmentManager().findFragmentById(C0853R.C0855id.photoPagerFragment);
        this.pagerFragment.setPhotos(this.strings, currentItem);
        updateActionBarTitle();
        this.pagerFragment.getViewPager().addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e("LookPhoto:======", position + "");
                LookAllSceneryPhotoActivity.this.updateActionBarTitle();
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onRestart() {
        super.onRestart();
        Util.setFullScreen(this);
    }

    public void updateActionBarTitle() {
        this.pathTwo = this.strings.get(this.pagerFragment.getViewPager().getCurrentItem());
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

    public void onClick(View v) {
        switch (v.getId()) {
            case C0853R.C0855id.act_look_all_scenery_photo_close:
                finish();
                return;
            default:
                return;
        }
    }

    public void onDataChanged(IssueKey issue, Object o) {
        if (issue.equals(IssueKey.LOOK_PHOTO_TOP_BUTTOM)) {
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
