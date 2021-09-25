package com.freevisiontech.fvmobile.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.p001v4.app.Fragment;
import android.support.p003v7.widget.GridLayoutManager;
import android.support.p003v7.widget.LinearLayoutManager;
import android.support.p003v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.activity.LookPhotoActivity;
import com.freevisiontech.fvmobile.base.BaseRVAdapter;
import com.freevisiontech.fvmobile.base.BaseViewHolder;
import com.freevisiontech.fvmobile.model.FilePhotoModel;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utility.SPUtils;
import com.freevisiontech.fvmobile.utility.SharePrefConstant;
import com.freevisiontech.fvmobile.utility.Util;
import com.freevisiontech.fvmobile.utils.CameraPaths;
import com.freevisiontech.fvmobile.utils.Event;
import com.freevisiontech.fvmobile.utils.GsonPhotoModelUtils;
import com.freevisiontech.fvmobile.utils.LoadingView;
import com.freevisiontech.fvmobile.utils.LruCacheUtils;
import com.freevisiontech.fvmobile.widget.view.IntentKey;
import com.google.android.vending.expansion.downloader.Constants;
import com.google.gson.Gson;
import com.vise.log.ViseLog;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import p010me.iwf.photopicker.widget.control.DataChangeNotification;
import p010me.iwf.photopicker.widget.control.IssueKey;
import p010me.iwf.photopicker.widget.control.OnDataChangeObserver;

public class FVZhaoPianCameraFragment extends Fragment implements OnDataChangeObserver {
    private static int LENGTH_DAY = 151;
    private static int LENGTH_LINE = 102;
    private static final int REFRESH_ADAPTER = 7;
    /* access modifiers changed from: private */
    public BaseRVAdapter adapterInt;
    private BaseRVAdapter adapterOut;
    /* access modifiers changed from: private */
    public FilePhotoModel filePhotoModel;
    @Bind({2131755769})
    LinearLayout file_paint_to_activity;
    @Bind({2131755775})
    RelativeLayout fragment_file_zhaopian_none;
    @Bind({2131755774})
    RecyclerView fragment_recycler_zhaopian;
    /* access modifiers changed from: private */
    public int horizontalItemNum;
    /* access modifiers changed from: private */
    public List<FilePhotoModel.DateBean> listFileModel;
    /* access modifiers changed from: private */
    public List listItemPathLook;
    /* access modifiers changed from: private */
    public List<String> listPath;
    /* access modifiers changed from: private */
    public List listSelectPath;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public int mCurrentSelectPosition = 0;
    private boolean mIsFM210ButtonsEnable = true;
    private LinearLayoutManager mLinearLayoutManager;
    /* access modifiers changed from: private */
    public int mListmodelPosition = 0;
    /* access modifiers changed from: private */
    public int mOperand = 3;
    /* access modifiers changed from: private */
    public AlertDialog mProgressDialog;
    /* access modifiers changed from: private */
    public Handler mUiHandler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 7:
                    Log.i("KBein", "FVZhaoPianCameraFragment.handleMessage():--刷新Adapter--");
                    FVZhaoPianCameraFragment.this.refreshAdapter();
                    return false;
                default:
                    return false;
            }
        }
    });
    /* access modifiers changed from: private */
    public boolean onSelect = true;
    private Boolean onSelectAll = false;
    /* access modifiers changed from: private */
    public int phoneHeight;
    /* access modifiers changed from: private */
    public int phoneWidth;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(C0853R.layout.fragment_file_zhao_pian, container, false);
        ButterKnife.bind((Object) this, view);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        this.mContext = getActivity();
        initData();
        init();
        return view;
    }

    private void initData() {
        this.listPath = new ArrayList();
        this.listFileModel = new ArrayList();
        this.listSelectPath = new ArrayList();
        this.listItemPathLook = new ArrayList();
        this.file_paint_to_activity.setVisibility(8);
        int ori = getResources().getConfiguration().orientation;
        if (ori == 2) {
            initViewHorizontal();
        } else if (ori == 1) {
            initView();
        }
    }

    private void init() {
        DataChangeNotification.getInstance().addObserver(IssueKey.CAMERA_CHECK, this);
        DataChangeNotification.getInstance().addObserver(IssueKey.CAMERA_CHECK_OK, this);
        DataChangeNotification.getInstance().addObserver(IssueKey.CAMERA_CHECK_LOOK, this);
        DataChangeNotification.getInstance().addObserver(IssueKey.CAMERA_CHECK_ALL, this);
        DataChangeNotification.getInstance().addObserver(IssueKey.PHOTO_VIDEO_DEIETE, this);
        upDateFiles();
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getRealMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        if (width > height) {
            this.phoneWidth = height;
            this.phoneHeight = width;
        } else {
            this.phoneWidth = width;
            this.phoneHeight = height;
        }
        this.horizontalItemNum = 5;
        this.horizontalItemNum = (this.phoneHeight - Util.dip2px(this.mContext, 37.0f)) / Util.dip2px(this.mContext, 102.0f);
    }

    private void upDateFiles() {
        showProgress(getString(C0853R.string.file_show_pro_title));
        new Thread(new Runnable() {
            public void run() {
                FVZhaoPianCameraFragment.this.listPath.clear();
                FVZhaoPianCameraFragment.this.listPath.addAll(CameraPaths.getImagePathFromSD(FVZhaoPianCameraFragment.this.getActivity()));
                Gson gson = new Gson();
                String json = String.valueOf(GsonPhotoModelUtils.getPhotoModelGson(FVZhaoPianCameraFragment.this.listPath));
                Log.i("KBein", "FVZhaoPianCameraFragment.run(): json -->" + json);
                FilePhotoModel unused = FVZhaoPianCameraFragment.this.filePhotoModel = (FilePhotoModel) gson.fromJson(json, FilePhotoModel.class);
                FVZhaoPianCameraFragment.this.listFileModel.clear();
                FVZhaoPianCameraFragment.this.listFileModel.addAll(FVZhaoPianCameraFragment.this.filePhotoModel.getDate());
                FVZhaoPianCameraFragment.this.mUiHandler.sendEmptyMessage(7);
            }
        }).start();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int i = newConfig.orientation;
        this.mContext.getResources().getConfiguration();
        if (i == 1) {
            initView();
            return;
        }
        int i2 = newConfig.orientation;
        this.mContext.getResources().getConfiguration();
        if (i2 == 2) {
            initViewHorizontal();
        }
    }

    private void initView() {
        final String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        this.mLinearLayoutManager = new LinearLayoutManager(this.mContext, 1, false);
        this.fragment_recycler_zhaopian.setLayoutManager(this.mLinearLayoutManager);
        this.adapterOut = new BaseRVAdapter(this.mContext, this.listFileModel) {
            public int getLayoutId(int viewType) {
                return C0853R.layout.recycle_item_zhaopian;
            }

            public void onBind(BaseViewHolder holder, int pos) {
                int aaa = ((FVZhaoPianCameraFragment.this.phoneWidth - Util.dip2px(this.mContext, 300.0f)) - Util.dip2px(this.mContext, 4.0f)) - Util.dip2px(this.mContext, 37.0f);
                LinearLayout recycle_item_zhaopian_recycle = (LinearLayout) holder.getView(C0853R.C0855id.recycle_item_zhaopian_recycle);
                LinearLayout.LayoutParams lps = (LinearLayout.LayoutParams) recycle_item_zhaopian_recycle.getLayoutParams();
                lps.width = (FVZhaoPianCameraFragment.this.phoneWidth - aaa) - Util.dip2px(this.mContext, 37.0f);
                recycle_item_zhaopian_recycle.setLayoutParams(lps);
                View recycler_item_zhaopian_view_top = holder.getView(C0853R.C0855id.recycler_item_zhaopian_view_top);
                if (pos == 0) {
                    recycler_item_zhaopian_view_top.setBackgroundColor(FVZhaoPianCameraFragment.this.getResources().getColor(C0853R.color.transparent));
                }
                TextView item_date_text_view = (TextView) holder.getView(C0853R.C0855id.recycler_item_zhaopian_date);
                if (Util.isZh(this.mContext)) {
                    if (timeStamp.substring(0, 4).equals(((FilePhotoModel.DateBean) FVZhaoPianCameraFragment.this.listFileModel.get(pos)).getTime().substring(0, 4))) {
                        item_date_text_view.setText(((FilePhotoModel.DateBean) FVZhaoPianCameraFragment.this.listFileModel.get(pos)).getTime().substring(4, 6) + "月" + ((FilePhotoModel.DateBean) FVZhaoPianCameraFragment.this.listFileModel.get(pos)).getTime().substring(6, ((FilePhotoModel.DateBean) FVZhaoPianCameraFragment.this.listFileModel.get(pos)).getTime().length()) + "日");
                    } else {
                        item_date_text_view.setText(((FilePhotoModel.DateBean) FVZhaoPianCameraFragment.this.listFileModel.get(pos)).getTime().substring(0, 4) + "年" + ((FilePhotoModel.DateBean) FVZhaoPianCameraFragment.this.listFileModel.get(pos)).getTime().substring(4, 6) + "月" + ((FilePhotoModel.DateBean) FVZhaoPianCameraFragment.this.listFileModel.get(pos)).getTime().substring(6, ((FilePhotoModel.DateBean) FVZhaoPianCameraFragment.this.listFileModel.get(pos)).getTime().length()) + "日");
                    }
                } else if (timeStamp.substring(0, 4).equals(((FilePhotoModel.DateBean) FVZhaoPianCameraFragment.this.listFileModel.get(pos)).getTime().substring(0, 4))) {
                    item_date_text_view.setText(Util.zhToEnglishMonth(Integer.valueOf(((FilePhotoModel.DateBean) FVZhaoPianCameraFragment.this.listFileModel.get(pos)).getTime().substring(4, 6)).intValue(), this.mContext) + " " + ((FilePhotoModel.DateBean) FVZhaoPianCameraFragment.this.listFileModel.get(pos)).getTime().substring(6, ((FilePhotoModel.DateBean) FVZhaoPianCameraFragment.this.listFileModel.get(pos)).getTime().length()));
                } else {
                    item_date_text_view.setText(Util.zhToEnglishMonth(Integer.valueOf(((FilePhotoModel.DateBean) FVZhaoPianCameraFragment.this.listFileModel.get(pos)).getTime().substring(4, 6)).intValue(), this.mContext) + " " + ((FilePhotoModel.DateBean) FVZhaoPianCameraFragment.this.listFileModel.get(pos)).getTime().substring(6, ((FilePhotoModel.DateBean) FVZhaoPianCameraFragment.this.listFileModel.get(pos)).getTime().length()) + "," + ((FilePhotoModel.DateBean) FVZhaoPianCameraFragment.this.listFileModel.get(pos)).getTime().substring(0, 4));
                }
                RecyclerView recyclerView = (RecyclerView) holder.getView(C0853R.C0855id.zhaopian_item_recycle_grid);
                recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 10);
                recyclerView.setLayoutManager(new GridLayoutManager(this.mContext, 3));
                int unused = FVZhaoPianCameraFragment.this.mOperand = 3;
                List<FilePhotoModel.DateBean.ContentBean> listaa = ((FilePhotoModel.DateBean) FVZhaoPianCameraFragment.this.listFileModel.get(pos)).getContent();
                List listItemPath = new ArrayList();
                listItemPath.clear();
                for (int s = 0; s < listaa.size(); s++) {
                    listItemPath.add(listaa.get(s).getPath());
                }
                final List<FilePhotoModel.DateBean.ContentBean> list = listaa;
                final int i = pos;
                BaseRVAdapter unused2 = FVZhaoPianCameraFragment.this.adapterInt = new BaseRVAdapter(this.mContext, listaa) {
                    public int getLayoutId(int viewType) {
                        return C0853R.layout.recycle_item_grid_item;
                    }

                    public void onBind(BaseViewHolder holder, final int position) {
                        String path;
                        ImageView itemImage = (ImageView) holder.getView(C0853R.C0855id.iv_recycle_item_image);
                        final LinearLayout iv_file_item_pho_ll = (LinearLayout) holder.getView(C0853R.C0855id.iv_file_item_pho_ll);
                        final ImageView iv_file_item_pho_gou = (ImageView) holder.getView(C0853R.C0855id.iv_file_item_pho_gou);
                        TextView item_image_yuanpian = (TextView) holder.getView(C0853R.C0855id.iv_recycle_item_image_yuanpian);
                        TextView item_image_bianji = (TextView) holder.getView(C0853R.C0855id.iv_recycle_item_image_bianji);
                        item_image_yuanpian.setVisibility(0);
                        item_image_yuanpian.setText(FVZhaoPianCameraFragment.this.getString(C0853R.string.file_edit_the_original_image));
                        item_image_bianji.setVisibility(8);
                        if (!((String) SPUtils.get(this.mContext, SharePrefConstant.SAVE_STORAGE_SD_CARD_PATH, IntentKey.FILE_PATH)).contains(Constants.FILENAME_SEQUENCE_SEPARATOR)) {
                            path = IntentKey.FILE_PATH + ((FilePhotoModel.DateBean.ContentBean) list.get(position)).getPath();
                        } else {
                            path = Util.getParentPath(this.mContext) + ((FilePhotoModel.DateBean.ContentBean) list.get(position)).getPath();
                        }
                        if (path.substring(path.indexOf(".") - 6, path.indexOf(".")).equals("bianji") || path.substring(path.indexOf(".") - 6, path.indexOf(".")).equals("mosaic") || path.substring(path.indexOf(".") - 4, path.indexOf(".")).equals("Edit")) {
                            item_image_bianji.setVisibility(0);
                            item_image_yuanpian.setVisibility(8);
                        }
                        if (FVZhaoPianCameraFragment.this.isListFileModelNotNull()) {
                            ((FilePhotoModel.DateBean) FVZhaoPianCameraFragment.this.listFileModel.get(FVZhaoPianCameraFragment.this.mListmodelPosition)).getContent().get(FVZhaoPianCameraFragment.this.mCurrentSelectPosition).setSelected(true);
                        }
                        if (CameraUtils.getCurrentPageIndex() == 2) {
                            itemImage.setSelected(((FilePhotoModel.DateBean.ContentBean) list.get(position)).isSelected());
                        }
                        ImageView photoView = itemImage;
                        FVZhaoPianCameraFragment.this.displayImageTarget(photoView, path, FVZhaoPianCameraFragment.this.getTarget(photoView, path, position));
                        if (FVZhaoPianCameraFragment.this.listSelectPath.contains(((FilePhotoModel.DateBean.ContentBean) list.get(position)).getPath())) {
                            iv_file_item_pho_ll.setVisibility(0);
                            iv_file_item_pho_gou.setVisibility(0);
                        } else {
                            iv_file_item_pho_ll.setVisibility(8);
                            iv_file_item_pho_gou.setVisibility(8);
                        }
                        itemImage.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                if (FVZhaoPianCameraFragment.this.onSelect) {
                                    List listItemPath = new ArrayList();
                                    listItemPath.clear();
                                    for (int s = 0; s < list.size(); s++) {
                                        listItemPath.add(((FilePhotoModel.DateBean.ContentBean) list.get(s)).getPath());
                                    }
                                    int aaa = 0;
                                    for (int s2 = 0; s2 < i; s2++) {
                                        aaa += ((FilePhotoModel.DateBean) FVZhaoPianCameraFragment.this.listFileModel.get(s2)).getContent().size();
                                    }
                                    C13021.this.mContext.getSharedPreferences("user", 0).edit().putString("camera", "0").commit();
                                    Intent intent = new Intent(C13021.this.mContext, LookPhotoActivity.class);
                                    intent.putStringArrayListExtra(IntentKey.CLASS_JSON, (ArrayList) FVZhaoPianCameraFragment.this.listItemPathLook);
                                    intent.putExtra(IntentKey.CLASS_POSION, position + aaa);
                                    C13021.this.mContext.startActivity(intent);
                                    return;
                                }
                                iv_file_item_pho_ll.setVisibility(0);
                                iv_file_item_pho_gou.setVisibility(0);
                                FVZhaoPianCameraFragment.this.listSelectPath.add(((FilePhotoModel.DateBean.ContentBean) list.get(position)).getPath());
                                CameraUtils.setPhotoSelectNums(FVZhaoPianCameraFragment.this.listSelectPath.size());
                                if (FVZhaoPianCameraFragment.this.listSelectPath.size() == 1) {
                                    DataChangeNotification.getInstance().notifyDataChanged(IssueKey.VIDEO_PHOTO_CAMERA_DELETE_CHECK_HAVA);
                                }
                            }
                        });
                        iv_file_item_pho_ll.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                iv_file_item_pho_ll.setVisibility(8);
                                iv_file_item_pho_gou.setVisibility(8);
                                Iterator<String> sListIterator = FVZhaoPianCameraFragment.this.listSelectPath.iterator();
                                while (sListIterator.hasNext()) {
                                    if (sListIterator.next().equals(((FilePhotoModel.DateBean.ContentBean) list.get(position)).getPath())) {
                                        sListIterator.remove();
                                    }
                                }
                                CameraUtils.setPhotoSelectNums(FVZhaoPianCameraFragment.this.listSelectPath.size());
                                if (FVZhaoPianCameraFragment.this.listSelectPath.size() == 0) {
                                    DataChangeNotification.getInstance().notifyDataChanged(IssueKey.VIDEO_PHOTO_CAMERA_DELETE_CHECK_HAVA_NONE);
                                }
                            }
                        });
                    }
                };
                recyclerView.setAdapter(FVZhaoPianCameraFragment.this.adapterInt);
            }
        };
        this.fragment_recycler_zhaopian.setAdapter(this.adapterOut);
        this.fragment_recycler_zhaopian.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == 0 || newState == 1) {
                    Glide.with(FVZhaoPianCameraFragment.this.mContext).resumeRequests();
                } else {
                    Glide.with(FVZhaoPianCameraFragment.this.mContext).pauseRequests();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public boolean isListFileModelNotNull() {
        if (this.listFileModel == null || this.mListmodelPosition >= this.listFileModel.size() || this.mCurrentSelectPosition >= this.listFileModel.get(this.mListmodelPosition).getContent().size()) {
            return false;
        }
        return true;
    }

    private void initViewHorizontal() {
        final String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        this.mLinearLayoutManager = new LinearLayoutManager(this.mContext, 1, false);
        this.fragment_recycler_zhaopian.setLayoutManager(this.mLinearLayoutManager);
        this.adapterOut = new BaseRVAdapter(this.mContext, this.listFileModel) {
            public int getLayoutId(int viewType) {
                return C0853R.layout.recycle_item_zhaopian;
            }

            public void onBind(BaseViewHolder holder, int pos) {
                int aaa = ((FVZhaoPianCameraFragment.this.phoneHeight - Util.dip2px(this.mContext, (float) (FVZhaoPianCameraFragment.this.horizontalItemNum * 100))) - Util.dip2px(this.mContext, (float) ((FVZhaoPianCameraFragment.this.horizontalItemNum * 2) - 2))) - Util.dip2px(this.mContext, 37.0f);
                LinearLayout recycle_item_zhaopian_recycle = (LinearLayout) holder.getView(C0853R.C0855id.recycle_item_zhaopian_recycle);
                LinearLayout.LayoutParams lps = (LinearLayout.LayoutParams) recycle_item_zhaopian_recycle.getLayoutParams();
                lps.width = (FVZhaoPianCameraFragment.this.phoneHeight - aaa) - Util.dip2px(this.mContext, 37.0f);
                recycle_item_zhaopian_recycle.setLayoutParams(lps);
                View recycler_item_zhaopian_view_top = holder.getView(C0853R.C0855id.recycler_item_zhaopian_view_top);
                if (pos == 0) {
                    recycler_item_zhaopian_view_top.setBackgroundColor(FVZhaoPianCameraFragment.this.getResources().getColor(C0853R.color.transparent));
                }
                TextView item_date_text_view = (TextView) holder.getView(C0853R.C0855id.recycler_item_zhaopian_date);
                if (Util.isZh(this.mContext)) {
                    if (FVZhaoPianCameraFragment.this.listFileModel.size() != 0) {
                        if (timeStamp.substring(0, 4).equals(((FilePhotoModel.DateBean) FVZhaoPianCameraFragment.this.listFileModel.get(pos)).getTime().substring(0, 4))) {
                            item_date_text_view.setText(((FilePhotoModel.DateBean) FVZhaoPianCameraFragment.this.listFileModel.get(pos)).getTime().substring(4, 6) + "月" + ((FilePhotoModel.DateBean) FVZhaoPianCameraFragment.this.listFileModel.get(pos)).getTime().substring(6, ((FilePhotoModel.DateBean) FVZhaoPianCameraFragment.this.listFileModel.get(pos)).getTime().length()) + "日");
                        } else {
                            item_date_text_view.setText(((FilePhotoModel.DateBean) FVZhaoPianCameraFragment.this.listFileModel.get(pos)).getTime().substring(0, 4) + "年" + ((FilePhotoModel.DateBean) FVZhaoPianCameraFragment.this.listFileModel.get(pos)).getTime().substring(4, 6) + "月" + ((FilePhotoModel.DateBean) FVZhaoPianCameraFragment.this.listFileModel.get(pos)).getTime().substring(6, ((FilePhotoModel.DateBean) FVZhaoPianCameraFragment.this.listFileModel.get(pos)).getTime().length()) + "日");
                        }
                    }
                } else if (FVZhaoPianCameraFragment.this.listFileModel.size() != 0) {
                    if (timeStamp.substring(0, 4).equals(((FilePhotoModel.DateBean) FVZhaoPianCameraFragment.this.listFileModel.get(pos)).getTime().substring(0, 4))) {
                        item_date_text_view.setText(Util.zhToEnglishMonth(Integer.valueOf(((FilePhotoModel.DateBean) FVZhaoPianCameraFragment.this.listFileModel.get(pos)).getTime().substring(4, 6)).intValue(), this.mContext) + " " + ((FilePhotoModel.DateBean) FVZhaoPianCameraFragment.this.listFileModel.get(pos)).getTime().substring(6, ((FilePhotoModel.DateBean) FVZhaoPianCameraFragment.this.listFileModel.get(pos)).getTime().length()));
                    } else {
                        item_date_text_view.setText(Util.zhToEnglishMonth(Integer.valueOf(((FilePhotoModel.DateBean) FVZhaoPianCameraFragment.this.listFileModel.get(pos)).getTime().substring(4, 6)).intValue(), this.mContext) + " " + ((FilePhotoModel.DateBean) FVZhaoPianCameraFragment.this.listFileModel.get(pos)).getTime().substring(6, ((FilePhotoModel.DateBean) FVZhaoPianCameraFragment.this.listFileModel.get(pos)).getTime().length()) + "," + ((FilePhotoModel.DateBean) FVZhaoPianCameraFragment.this.listFileModel.get(pos)).getTime().substring(0, 4));
                    }
                }
                RecyclerView recyclerView = (RecyclerView) holder.getView(C0853R.C0855id.zhaopian_item_recycle_grid);
                recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 10);
                recyclerView.setLayoutManager(new GridLayoutManager(this.mContext, FVZhaoPianCameraFragment.this.horizontalItemNum));
                int unused = FVZhaoPianCameraFragment.this.mOperand = FVZhaoPianCameraFragment.this.horizontalItemNum;
                List<FilePhotoModel.DateBean.ContentBean> listaa = ((FilePhotoModel.DateBean) FVZhaoPianCameraFragment.this.listFileModel.get(pos)).getContent();
                List listItemPath = new ArrayList();
                listItemPath.clear();
                for (int s = 0; s < listaa.size(); s++) {
                    listItemPath.add(listaa.get(s).getPath());
                }
                final List<FilePhotoModel.DateBean.ContentBean> list = listaa;
                final int i = pos;
                BaseRVAdapter unused2 = FVZhaoPianCameraFragment.this.adapterInt = new BaseRVAdapter(this.mContext, listaa) {
                    public int getLayoutId(int viewType) {
                        return C0853R.layout.recycle_item_grid_item;
                    }

                    public void onBind(BaseViewHolder holder, final int position) {
                        String path;
                        ImageView itemImage = (ImageView) holder.getView(C0853R.C0855id.iv_recycle_item_image);
                        final LinearLayout iv_file_item_pho_ll = (LinearLayout) holder.getView(C0853R.C0855id.iv_file_item_pho_ll);
                        final ImageView iv_file_item_pho_gou = (ImageView) holder.getView(C0853R.C0855id.iv_file_item_pho_gou);
                        TextView item_image_yuanpian = (TextView) holder.getView(C0853R.C0855id.iv_recycle_item_image_yuanpian);
                        TextView item_image_bianji = (TextView) holder.getView(C0853R.C0855id.iv_recycle_item_image_bianji);
                        item_image_yuanpian.setVisibility(0);
                        item_image_yuanpian.setText(FVZhaoPianCameraFragment.this.getString(C0853R.string.file_edit_the_original_image));
                        item_image_bianji.setVisibility(8);
                        if (!((String) SPUtils.get(this.mContext, SharePrefConstant.SAVE_STORAGE_SD_CARD_PATH, IntentKey.FILE_PATH)).contains(Constants.FILENAME_SEQUENCE_SEPARATOR)) {
                            path = IntentKey.FILE_PATH + ((FilePhotoModel.DateBean.ContentBean) list.get(position)).getPath();
                        } else {
                            path = Util.getParentPath(this.mContext) + ((FilePhotoModel.DateBean.ContentBean) list.get(position)).getPath();
                        }
                        if (path.substring(path.indexOf(".") - 6, path.indexOf(".")).equals("bianji") || path.substring(path.indexOf(".") - 6, path.indexOf(".")).equals("mosaic") || path.substring(path.indexOf(".") - 4, path.indexOf(".")).equals("Edit")) {
                            item_image_bianji.setVisibility(0);
                            item_image_yuanpian.setVisibility(8);
                        }
                        if (FVZhaoPianCameraFragment.this.isListFileModelNotNull()) {
                            ((FilePhotoModel.DateBean) FVZhaoPianCameraFragment.this.listFileModel.get(FVZhaoPianCameraFragment.this.mListmodelPosition)).getContent().get(FVZhaoPianCameraFragment.this.mCurrentSelectPosition).setSelected(true);
                        }
                        if (CameraUtils.getCurrentPageIndex() == 2) {
                            itemImage.setSelected(((FilePhotoModel.DateBean.ContentBean) list.get(position)).isSelected());
                        }
                        ImageView photoView = itemImage;
                        FVZhaoPianCameraFragment.this.displayImageTarget(photoView, path, FVZhaoPianCameraFragment.this.getTarget(photoView, path, position));
                        if (FVZhaoPianCameraFragment.this.listSelectPath.contains(((FilePhotoModel.DateBean.ContentBean) list.get(position)).getPath())) {
                            iv_file_item_pho_ll.setVisibility(0);
                            iv_file_item_pho_gou.setVisibility(0);
                        } else {
                            iv_file_item_pho_ll.setVisibility(8);
                            iv_file_item_pho_gou.setVisibility(8);
                        }
                        itemImage.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                if (FVZhaoPianCameraFragment.this.onSelect) {
                                    List listItemPath = new ArrayList();
                                    listItemPath.clear();
                                    for (int s = 0; s < list.size(); s++) {
                                        listItemPath.add(((FilePhotoModel.DateBean.ContentBean) list.get(s)).getPath());
                                    }
                                    int aaa = 0;
                                    for (int s2 = 0; s2 < i; s2++) {
                                        aaa += ((FilePhotoModel.DateBean) FVZhaoPianCameraFragment.this.listFileModel.get(s2)).getContent().size();
                                    }
                                    C13071.this.mContext.getSharedPreferences("user", 0).edit().putString("camera", "0").commit();
                                    Intent intent = new Intent(C13071.this.mContext, LookPhotoActivity.class);
                                    intent.putStringArrayListExtra(IntentKey.CLASS_JSON, (ArrayList) FVZhaoPianCameraFragment.this.listItemPathLook);
                                    intent.putExtra(IntentKey.CLASS_POSION, position + aaa);
                                    C13071.this.mContext.startActivity(intent);
                                    return;
                                }
                                iv_file_item_pho_ll.setVisibility(0);
                                iv_file_item_pho_gou.setVisibility(0);
                                FVZhaoPianCameraFragment.this.listSelectPath.add(((FilePhotoModel.DateBean.ContentBean) list.get(position)).getPath());
                                Log.e("----------", "----------  listSelectPath  Size  333 ---" + FVZhaoPianCameraFragment.this.listSelectPath.size());
                                CameraUtils.setPhotoSelectNums(FVZhaoPianCameraFragment.this.listSelectPath.size());
                                if (FVZhaoPianCameraFragment.this.listSelectPath.size() == 1) {
                                    DataChangeNotification.getInstance().notifyDataChanged(IssueKey.VIDEO_PHOTO_CAMERA_DELETE_CHECK_HAVA);
                                }
                            }
                        });
                        iv_file_item_pho_ll.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                iv_file_item_pho_ll.setVisibility(8);
                                iv_file_item_pho_gou.setVisibility(8);
                                Iterator<String> sListIterator = FVZhaoPianCameraFragment.this.listSelectPath.iterator();
                                while (sListIterator.hasNext()) {
                                    if (sListIterator.next().equals(((FilePhotoModel.DateBean.ContentBean) list.get(position)).getPath())) {
                                        sListIterator.remove();
                                    }
                                }
                                CameraUtils.setPhotoSelectNums(FVZhaoPianCameraFragment.this.listSelectPath.size());
                                if (FVZhaoPianCameraFragment.this.listSelectPath.size() == 0) {
                                    DataChangeNotification.getInstance().notifyDataChanged(IssueKey.VIDEO_PHOTO_CAMERA_DELETE_CHECK_HAVA_NONE);
                                }
                                Log.e("----------", "----------  listSelectPath  Size  000 ---" + FVZhaoPianCameraFragment.this.listSelectPath.size());
                            }
                        });
                    }
                };
                recyclerView.setAdapter(FVZhaoPianCameraFragment.this.adapterInt);
            }
        };
        this.fragment_recycler_zhaopian.setAdapter(this.adapterOut);
        this.fragment_recycler_zhaopian.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == 0 || newState == 1) {
                    Log.i("KBein", "FVZhaoPianCameraFragment.onScrollStateChanged(): --->" + newState);
                    Glide.with(FVZhaoPianCameraFragment.this.mContext).resumeRequests();
                    return;
                }
                Log.i("KBein", "FVZhaoPianCameraFragment.onScrollStateChanged(): ===>" + newState);
                Glide.with(FVZhaoPianCameraFragment.this.mContext).pauseRequests();
            }
        });
    }

    public void onDataChanged(IssueKey issue, Object o) {
        if (!IssueKey.CAMERA_CHECK.equals(issue)) {
            if (IssueKey.CAMERA_CHECK_OK.equals(issue)) {
                this.onSelect = false;
            } else if (IssueKey.CAMERA_CHECK_LOOK.equals(issue)) {
                this.onSelectAll = false;
                this.onSelect = true;
                this.listSelectPath.clear();
                this.adapterOut.notifyDataSetChanged();
                CameraUtils.setPhotoSelectNums(this.listSelectPath.size());
                DataChangeNotification.getInstance().notifyDataChanged(IssueKey.VIDEO_PHOTO_CAMERA_DELETE_CHECK_HAVA_NONE);
            } else if (IssueKey.CAMERA_CHECK_ALL.equals(issue)) {
                this.onSelectAll = true;
                this.onSelect = false;
                this.listSelectPath.clear();
                this.listSelectPath.addAll(this.listPath);
                this.adapterOut.notifyDataSetChanged();
                CameraUtils.setPhotoSelectNums(this.listSelectPath.size());
                DataChangeNotification.getInstance().notifyDataChanged(IssueKey.VIDEO_PHOTO_CAMERA_DELETE_CHECK_HAVA);
            } else if (IssueKey.PHOTO_VIDEO_DEIETE.equals(issue)) {
                deleteData();
            }
        }
    }

    private void deleteData() {
        String basePath;
        Log.i("KBein", "FVZhaoPianCameraFragment.onDataChanged():--收到的删除消息--" + Thread.currentThread().getName());
        if (this.listSelectPath.size() != 0) {
            for (int a = 0; a < this.listSelectPath.size(); a++) {
                if (!((String) SPUtils.get(this.mContext, SharePrefConstant.SAVE_STORAGE_SD_CARD_PATH, IntentKey.FILE_PATH)).contains(Constants.FILENAME_SEQUENCE_SEPARATOR)) {
                    basePath = IntentKey.FILE_PATH;
                } else {
                    basePath = Util.getParentPath(this.mContext);
                }
                File file = new File(basePath + this.listSelectPath.get(a).toString());
                if (file != null) {
                    file.delete();
                }
                Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                this.mContext.getContentResolver().delete(uri, "_data='" + (basePath + this.listSelectPath.get(a).toString()) + "'", (String[]) null);
            }
            this.listSelectPath.clear();
            CameraUtils.setPhotoSelectNums(this.listSelectPath.size());
            Toast.makeText(this.mContext, C0853R.string.photo_delete_finish, 0).show();
            DataChangeNotification.getInstance().notifyDataChanged(IssueKey.VIDEO_PHOTO_CAMERA_DELETE_END);
        }
        upDateFiles();
    }

    public void onDestroy() {
        super.onDestroy();
        this.filePhotoModel = null;
        DataChangeNotification.getInstance().removeObserver((OnDataChangeObserver) this);
        EventBus.getDefault().unregister(this);
    }

    public void displayImageTarget(ImageView imageView, String url, BitmapImageViewTarget target) {
        Glide.get(imageView.getContext());
        Glide.with(imageView.getContext()).load(url).asBitmap().centerCrop().thumbnail(0.4f).diskCacheStrategy(DiskCacheStrategy.RESULT).into(target);
    }

    /* access modifiers changed from: private */
    public BitmapImageViewTarget getTarget(ImageView imageView, final String url, int position) {
        return new BitmapImageViewTarget(imageView) {
            /* access modifiers changed from: protected */
            public void setResource(Bitmap resource) {
                super.setResource(resource);
                LruCacheUtils.getInstance().addBitmapToMemoryCache(url, resource);
            }
        };
    }

    public void showProgress(final String msg) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                if (FVZhaoPianCameraFragment.this.mProgressDialog == null && FVZhaoPianCameraFragment.this.mContext != null) {
                    AlertDialog unused = FVZhaoPianCameraFragment.this.mProgressDialog = new LoadingView(FVZhaoPianCameraFragment.this.getActivity().getWindow().getContext());
                    FVZhaoPianCameraFragment.this.mProgressDialog.setCancelable(true);
                    FVZhaoPianCameraFragment.this.mProgressDialog.setCanceledOnTouchOutside(false);
                }
                FVZhaoPianCameraFragment.this.mProgressDialog.setMessage(msg);
                FVZhaoPianCameraFragment.this.mProgressDialog.show();
            }
        });
    }

    public void hideProgress() {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                if (FVZhaoPianCameraFragment.this.mProgressDialog != null) {
                    FVZhaoPianCameraFragment.this.mProgressDialog.dismiss();
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCome(Event event) {
        if (event != null && this.mIsFM210ButtonsEnable) {
            receiveEvent(event);
        }
    }

    /* access modifiers changed from: protected */
    public void receiveEvent(Event event) {
        if (isListFileModelNotNull()) {
            switch (event.getCode()) {
                case 64:
                    updateSelectorUp();
                    return;
                case 65:
                    updateSelectorDown();
                    return;
                case 66:
                    updateSelectorLeft();
                    return;
                case 67:
                    updateSelectorRight();
                    return;
                case 80:
                    openImage();
                    return;
                default:
                    return;
            }
        }
    }

    private void openImage() {
        List<FilePhotoModel.DateBean.ContentBean> listaa = this.listFileModel.get(this.mListmodelPosition).getContent();
        if (this.onSelect) {
            List listItemPath = new ArrayList();
            listItemPath.clear();
            for (int s = 0; s < listaa.size(); s++) {
                listItemPath.add(listaa.get(s).getPath());
            }
            int beforeCount = 0;
            for (int s2 = 0; s2 < this.mListmodelPosition; s2++) {
                beforeCount += this.listFileModel.get(s2).getContent().size();
            }
            this.mContext.getSharedPreferences("user", 0).edit().putString("camera", "0").commit();
            Intent intent = new Intent(this.mContext, LookPhotoActivity.class);
            intent.putStringArrayListExtra(IntentKey.CLASS_JSON, (ArrayList) this.listItemPathLook);
            intent.putExtra(IntentKey.CLASS_POSION, this.mCurrentSelectPosition + beforeCount);
            this.mContext.startActivity(intent);
        }
    }

    private void smoothScroll(int length) {
        this.fragment_recycler_zhaopian.smoothScrollBy(0, Util.dip2px(this.mContext, (float) length));
    }

    private void updateSelectorUp() {
        if (!(this.mCurrentSelectPosition / this.mOperand == 0)) {
            this.mCurrentSelectPosition -= this.mOperand;
            ViseLog.m1466e("ViltaX向上" + this.mCurrentSelectPosition);
            smoothScroll(-LENGTH_LINE);
            updateSelectList();
        } else if (this.mListmodelPosition > 0) {
            int lastItemSize = this.listFileModel.get(this.mListmodelPosition - 1).getContent().size();
            int currentPosition = this.mCurrentSelectPosition + 1;
            if (this.mOperand == 3) {
                if (lastItemSize % this.mOperand == 0) {
                    this.mCurrentSelectPosition = lastItemSize - (this.mOperand - this.mCurrentSelectPosition);
                } else if (lastItemSize % this.mOperand != 2) {
                    this.mCurrentSelectPosition = lastItemSize - 1;
                } else if (this.mCurrentSelectPosition == 0) {
                    this.mCurrentSelectPosition = lastItemSize - 2;
                } else {
                    this.mCurrentSelectPosition = lastItemSize - 1;
                }
            } else if (lastItemSize >= this.mOperand) {
                if (lastItemSize % this.mOperand == 0) {
                    this.mCurrentSelectPosition = (lastItemSize - this.mOperand) + this.mCurrentSelectPosition;
                } else if (lastItemSize % this.mOperand >= this.mCurrentSelectPosition) {
                    this.mCurrentSelectPosition = ((lastItemSize / this.mOperand) * this.mOperand) + this.mCurrentSelectPosition;
                } else {
                    this.mCurrentSelectPosition = lastItemSize - 1;
                }
            } else if (lastItemSize < currentPosition) {
                this.mCurrentSelectPosition = lastItemSize - 1;
            }
            ViseLog.m1466e("ViltaX选中上一天最后一张" + this.mCurrentSelectPosition);
            clearSelectStatus(this.listFileModel.get(this.mListmodelPosition).getContent());
            this.adapterOut.notifyItemChanged(this.mListmodelPosition);
            this.mListmodelPosition--;
            smoothScroll(-LENGTH_DAY);
            updateSelectList();
        }
    }

    private void updateSelectorDown() {
        boolean isLastLine;
        int itemSize = this.listFileModel.get(this.mListmodelPosition).getContent().size();
        int daySize = this.listFileModel.size();
        if (this.mCurrentSelectPosition / this.mOperand == (itemSize - 1) / this.mOperand) {
            isLastLine = true;
        } else {
            isLastLine = false;
        }
        if (isLastLine) {
            ViseLog.m1466e("ViltaX--updateSelectorDown----isLastLine--" + isLastLine);
            if (this.mListmodelPosition < this.listFileModel.size() - 1) {
                int nextItemSize = this.listFileModel.get(this.mListmodelPosition + 1).getContent().size();
                int currentItemSize = this.listFileModel.get(this.mListmodelPosition).getContent().size();
                int currentPosition = this.mCurrentSelectPosition + 1;
                if (this.mOperand == 3) {
                    if (nextItemSize >= 3) {
                        this.mCurrentSelectPosition %= this.mOperand;
                    } else if (nextItemSize == 2) {
                        if (this.mCurrentSelectPosition % this.mOperand == 2) {
                            this.mCurrentSelectPosition = 0;
                        } else {
                            this.mCurrentSelectPosition %= this.mOperand;
                        }
                    } else if (nextItemSize == 1) {
                        this.mCurrentSelectPosition = 0;
                    }
                } else if (nextItemSize >= this.mOperand) {
                    if (currentItemSize > this.mOperand) {
                        this.mCurrentSelectPosition %= this.mOperand;
                    }
                } else if (currentItemSize <= this.mOperand) {
                    if (currentPosition > nextItemSize) {
                        this.mCurrentSelectPosition = 0;
                    }
                } else if (currentPosition % this.mOperand == 0) {
                    if (currentPosition > nextItemSize) {
                        this.mCurrentSelectPosition = 0;
                    }
                } else if (currentPosition % this.mOperand > nextItemSize) {
                    this.mCurrentSelectPosition = 0;
                } else {
                    this.mCurrentSelectPosition -= (currentPosition / this.mOperand) * this.mOperand;
                }
                clearSelectStatus(this.listFileModel.get(this.mListmodelPosition).getContent());
                this.adapterOut.notifyItemChanged(this.mListmodelPosition);
                this.mListmodelPosition++;
                ViseLog.m1466e("ViltaX选中下一天对应张" + this.mCurrentSelectPosition);
                smoothScroll(LENGTH_DAY);
                updateSelectList();
            }
        } else if (this.mCurrentSelectPosition + this.mOperand <= itemSize - 1) {
            this.mCurrentSelectPosition += this.mOperand;
            ViseLog.m1466e("ViltaX向下" + this.mCurrentSelectPosition);
            smoothScroll(LENGTH_LINE);
            updateSelectList();
        } else if (this.mListmodelPosition < daySize - 1) {
            this.mCurrentSelectPosition = 0;
            clearSelectStatus(this.listFileModel.get(this.mListmodelPosition).getContent());
            this.adapterOut.notifyItemChanged(this.mListmodelPosition);
            this.mListmodelPosition++;
            ViseLog.m1466e("ViltaX选中下一天第一张" + this.mCurrentSelectPosition);
            smoothScroll(LENGTH_LINE + LENGTH_DAY);
            updateSelectList();
        }
    }

    private void updateSelectorLeft() {
        if (this.mListmodelPosition > 0 && this.mCurrentSelectPosition == 0) {
            clearSelectStatus(this.listFileModel.get(this.mListmodelPosition).getContent());
            this.adapterOut.notifyItemChanged(this.mListmodelPosition);
            this.mListmodelPosition--;
            this.mCurrentSelectPosition = this.listFileModel.get(this.mListmodelPosition).getContent().size() - 1;
            ViseLog.m1466e("ViltaX选中上一天最后一张" + this.mCurrentSelectPosition);
            updateSelectList();
            smoothScroll(-LENGTH_DAY);
        } else if (this.mCurrentSelectPosition > 0) {
            this.mCurrentSelectPosition--;
            ViseLog.m1466e("ViltaX向左" + this.mCurrentSelectPosition);
            updateSelectList();
            if ((this.mCurrentSelectPosition + 1) % this.mOperand == 0) {
                smoothScroll(-LENGTH_LINE);
            }
        }
    }

    private void updateSelectorRight() {
        int listModelSize = this.listFileModel.size();
        int itemSize = this.listFileModel.get(this.mListmodelPosition).getContent().size();
        if (this.mListmodelPosition < listModelSize && this.mCurrentSelectPosition < itemSize) {
            if (itemSize == 1) {
                if (this.mListmodelPosition < listModelSize - 1) {
                    clearSelectStatus(this.listFileModel.get(this.mListmodelPosition).getContent());
                    this.adapterOut.notifyItemChanged(this.mListmodelPosition);
                    this.mListmodelPosition++;
                    this.mCurrentSelectPosition = 0;
                    smoothScroll(LENGTH_DAY);
                    ViseLog.m1466e("ViltaX--正常向右--有一张照片，选中下一天第一个");
                }
            } else if (this.mCurrentSelectPosition < itemSize - 1) {
                this.mCurrentSelectPosition++;
                if (this.mCurrentSelectPosition % this.mOperand == 0) {
                    smoothScroll(LENGTH_LINE);
                }
            } else if (this.mCurrentSelectPosition == itemSize - 1 && this.mListmodelPosition < listModelSize - 1) {
                clearSelectStatus(this.listFileModel.get(this.mListmodelPosition).getContent());
                this.adapterOut.notifyItemChanged(this.mListmodelPosition);
                this.mListmodelPosition++;
                this.mCurrentSelectPosition = 0;
                smoothScroll(LENGTH_DAY);
                ViseLog.m1466e("ViltaX--正常向右--有多张照片，已到当天末尾,选中下一天第一个");
            }
            updateSelectList();
        }
    }

    private void clearSelectStatus(List<FilePhotoModel.DateBean.ContentBean> contents) {
        if (contents != null) {
            for (FilePhotoModel.DateBean.ContentBean tempContent : contents) {
                tempContent.setSelected(false);
            }
        }
    }

    private void updateSelectList() {
        this.filePhotoModel.getDate().clear();
        clearSelectStatus(this.listFileModel.get(this.mListmodelPosition).getContent());
        this.listFileModel.get(this.mListmodelPosition).getContent().get(this.mCurrentSelectPosition).setSelected(true);
        this.listFileModel.addAll(this.filePhotoModel.getDate());
        this.adapterOut.notifyItemChanged(this.mListmodelPosition);
    }

    /* access modifiers changed from: private */
    public void refreshAdapter() {
        Log.i("KBein", "FVZhaoPianCameraFragment.refreshAdapter():--listPath--");
        if (this.listPath.size() > 0) {
            CameraUtils.setPhotoNums(this.listPath.size());
            CameraPaths.CameraFiratPath(this.filePhotoModel.getDate().get(0).getContent().get(0).getPath());
            this.fragment_recycler_zhaopian.setVisibility(0);
            this.fragment_file_zhaopian_none.setVisibility(8);
        } else {
            CameraUtils.setPhotoNums(this.listPath.size());
            CameraPaths.CameraFiratPath((String) null);
            this.fragment_recycler_zhaopian.setVisibility(8);
            this.fragment_file_zhaopian_none.setVisibility(0);
        }
        this.listItemPathLook.clear();
        for (int a = 0; a < this.filePhotoModel.getDate().size(); a++) {
            List<FilePhotoModel.DateBean.ContentBean> listaa = this.listFileModel.get(a).getContent();
            for (int s = 0; s < listaa.size(); s++) {
                this.listItemPathLook.add(listaa.get(s).getPath());
            }
        }
        this.adapterOut.notifyDataSetChanged();
        DataChangeNotification.getInstance().notifyDataChanged(IssueKey.VIDEO_PHOTO_HAVE_OR_NONE);
        hideProgress();
    }
}
