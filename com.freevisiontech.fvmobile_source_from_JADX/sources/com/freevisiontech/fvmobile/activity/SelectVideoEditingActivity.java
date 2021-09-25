package com.freevisiontech.fvmobile.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.p001v4.app.ActivityCompat;
import android.support.p001v4.app.FragmentActivity;
import android.support.p003v7.widget.GridLayoutManager;
import android.support.p003v7.widget.LinearLayoutManager;
import android.support.p003v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.base.BaseRVAdapter;
import com.freevisiontech.fvmobile.base.BaseViewHolder;
import com.freevisiontech.fvmobile.model.FilePhotoModel;
import com.freevisiontech.fvmobile.utility.SPUtils;
import com.freevisiontech.fvmobile.utility.SharePrefConstant;
import com.freevisiontech.fvmobile.utility.Util;
import com.freevisiontech.fvmobile.utils.CameraPaths;
import com.freevisiontech.fvmobile.utils.GsonPhotoModelUtils;
import com.freevisiontech.fvmobile.widget.view.IntentKey;
import com.google.android.vending.expansion.downloader.Constants;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SelectVideoEditingActivity extends FragmentActivity implements View.OnClickListener {
    private static String[] PERMISSIONS_STORAGE = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    /* access modifiers changed from: private */
    public Activity activity;
    private BaseRVAdapter adapterOut;
    private String biaozi;
    /* access modifiers changed from: private */
    public FilePhotoModel filePhotoModel;
    @Bind({2131755509})
    LinearLayout fragment_file_shiping_create;
    @Bind({2131755508})
    RecyclerView fragment_recycler_shiping;
    @Bind({2131756085})
    ImageView img_file_back;
    private List<String> listPath;
    @Bind({2131756088})
    TextView tv_all_right;
    @Bind({2131755510})
    TextView tv_confirm;
    @Bind({2131756086})
    TextView tv_file_center_title;
    @Bind({2131756089})
    TextView tv_file_right_cancel;
    @Bind({2131756090})
    TextView tv_file_right_select;
    /* access modifiers changed from: private */
    public List videosPath;

    public static Intent createIntent(Context context, String biaozi2) {
        Intent intent = new Intent(context, VideoEditingActivity.class);
        intent.putExtra(IntentKey.BIAO_ZI, biaozi2);
        return intent;
    }

    private void parseIntent() {
        this.biaozi = getIntent().getStringExtra(IntentKey.BIAO_ZI);
    }

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0853R.layout.activity_select_video_editing);
        ButterKnife.bind((Activity) this);
        this.activity = this;
        parseIntent();
        initTitleView();
        verifyStoragePermissions(this.activity);
        initData();
        initView();
    }

    private void initData() {
        this.videosPath = new ArrayList();
        this.listPath = new ArrayList();
        this.listPath.addAll(CameraPaths.getVideoPathFromSD(this.activity));
        Iterator<String> sListIterator = this.listPath.iterator();
        while (sListIterator.hasNext()) {
            String e = sListIterator.next();
            if (e.substring(e.length() - 10, e.length() - 4).equals("bianji") || e.substring(e.length() - 10, e.length() - 4).equals("yidong") || e.substring(e.length() - 13, e.length() - 4).equals("TimeLapse") || e.substring(e.length() - 8, e.length() - 4).equals("Edit")) {
                sListIterator.remove();
            }
        }
        this.fragment_file_shiping_create.setOnClickListener(this);
        this.fragment_file_shiping_create.setBackgroundResource(C0853R.color.white);
        this.filePhotoModel = (FilePhotoModel) new Gson().fromJson(String.valueOf(GsonPhotoModelUtils.getPhotoModelGson(this.listPath)), FilePhotoModel.class);
    }

    private void initTitleView() {
        this.img_file_back.setVisibility(0);
        this.tv_file_center_title.setVisibility(0);
        this.tv_all_right.setVisibility(8);
        this.tv_file_right_cancel.setVisibility(8);
        this.tv_file_right_select.setVisibility(8);
        this.tv_file_center_title.setText(getString(C0853R.string.label_video));
        this.img_file_back.setOnClickListener(this);
    }

    private void initView() {
        this.fragment_recycler_shiping.setLayoutManager(new LinearLayoutManager(this.activity, 1, false));
        this.adapterOut = new BaseRVAdapter(this.activity, this.filePhotoModel.getDate()) {
            public int getLayoutId(int viewType) {
                return C0853R.layout.recycle_item_zhaopian;
            }

            public void onBind(BaseViewHolder holder, int position) {
                ((TextView) holder.getView(C0853R.C0855id.recycler_item_zhaopian_date)).setText(SelectVideoEditingActivity.this.filePhotoModel.getDate().get(position).getTime());
                RecyclerView recyclerView = (RecyclerView) holder.getView(C0853R.C0855id.zhaopian_item_recycle_grid);
                recyclerView.setLayoutManager(new GridLayoutManager(SelectVideoEditingActivity.this.activity, 3));
                final List<FilePhotoModel.DateBean.ContentBean> listaa = SelectVideoEditingActivity.this.filePhotoModel.getDate().get(position).getContent();
                recyclerView.setAdapter(new BaseRVAdapter(SelectVideoEditingActivity.this.activity, listaa) {
                    public int getLayoutId(int viewType) {
                        return C0853R.layout.recycle_item_grid_item;
                    }

                    public void onBind(BaseViewHolder holder, int position) {
                        String basePath;
                        ImageView itemImage = (ImageView) holder.getView(C0853R.C0855id.iv_recycle_item_image);
                        final LinearLayout iv_file_item_pho_ll = (LinearLayout) holder.getView(C0853R.C0855id.iv_file_item_pho_ll);
                        final ImageView iv_file_item_pho_gou = (ImageView) holder.getView(C0853R.C0855id.iv_file_item_pho_gou);
                        if (!((String) SPUtils.get(this.mContext, SharePrefConstant.SAVE_STORAGE_SD_CARD_PATH, IntentKey.FILE_PATH)).contains(Constants.FILENAME_SEQUENCE_SEPARATOR)) {
                            basePath = IntentKey.FILE_PATH;
                        } else {
                            basePath = Util.getParentPath(this.mContext);
                        }
                        final String path = basePath + ((FilePhotoModel.DateBean.ContentBean) listaa.get(position)).getPath();
                        Glide.with(SelectVideoEditingActivity.this.activity).load(path).asBitmap().centerCrop().into(itemImage);
                        itemImage.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                iv_file_item_pho_ll.setVisibility(0);
                                iv_file_item_pho_gou.setVisibility(0);
                                SelectVideoEditingActivity.this.videosPath.add(path);
                                SelectVideoEditingActivity.this.tv_confirm.setAlpha(1.0f);
                            }
                        });
                        iv_file_item_pho_ll.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                iv_file_item_pho_ll.setVisibility(8);
                                iv_file_item_pho_gou.setVisibility(8);
                                Iterator<String> sListIterator = SelectVideoEditingActivity.this.videosPath.iterator();
                                while (sListIterator.hasNext()) {
                                    if (sListIterator.next().equals(path)) {
                                        sListIterator.remove();
                                    }
                                }
                                if (SelectVideoEditingActivity.this.videosPath.size() == 0) {
                                    SelectVideoEditingActivity.this.tv_confirm.setAlpha(0.2f);
                                } else {
                                    SelectVideoEditingActivity.this.tv_confirm.setAlpha(1.0f);
                                }
                            }
                        });
                    }
                });
            }
        };
        this.fragment_recycler_shiping.setAdapter(this.adapterOut);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0853R.C0855id.fragment_file_shiping_create:
                if (this.videosPath.size() == 0) {
                    Toast.makeText(this.activity, getString(C0853R.string.file_select_video), 0).show();
                    return;
                }
                Intent intent = new Intent();
                intent.putStringArrayListExtra(IntentKey.VIDEOS_PATH, (ArrayList) this.videosPath);
                setResult(-1, intent);
                finish();
                return;
            case C0853R.C0855id.img_file_back:
                finish();
                return;
            default:
                return;
        }
    }

    public static void verifyStoragePermissions(Activity activity2) {
        if (ActivityCompat.checkSelfPermission(activity2, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            ActivityCompat.requestPermissions(activity2, PERMISSIONS_STORAGE, 1);
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
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
