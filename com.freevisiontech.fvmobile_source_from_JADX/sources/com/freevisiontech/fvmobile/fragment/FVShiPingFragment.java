package com.freevisiontech.fvmobile.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.p001v4.app.Fragment;
import android.support.p001v4.widget.SwipeRefreshLayout;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.activity.FileWriteActivity;
import com.freevisiontech.fvmobile.activity.PlayVideoActivity;
import com.freevisiontech.fvmobile.base.BaseRVAdapter;
import com.freevisiontech.fvmobile.base.BaseViewHolder;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.freevisiontech.fvmobile.model.FilePhotoModel;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utility.SPUtils;
import com.freevisiontech.fvmobile.utility.SharePrefConstant;
import com.freevisiontech.fvmobile.utility.Util;
import com.freevisiontech.fvmobile.utils.CameraPaths;
import com.freevisiontech.fvmobile.utils.GsonPhotoModelUtils;
import com.freevisiontech.fvmobile.utils.LoadingView;
import com.freevisiontech.fvmobile.utils.LruCacheUtils;
import com.freevisiontech.fvmobile.widget.view.IntentKey;
import com.google.android.vending.expansion.downloader.Constants;
import com.google.gson.Gson;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import p010me.iwf.photopicker.widget.control.DataChangeNotification;
import p010me.iwf.photopicker.widget.control.IssueKey;
import p010me.iwf.photopicker.widget.control.OnDataChangeObserver;

public class FVShiPingFragment extends Fragment implements View.OnClickListener, OnDataChangeObserver {
    private static final int REFRESH_ADAPTER = 7;
    private BaseRVAdapter adapterOut;
    /* access modifiers changed from: private */
    public FilePhotoModel filePhotoModel;
    private LinearLayout file_paint_to_activity;
    private RelativeLayout fragment_file_shiping_home_none;
    private RecyclerView fragment_recycler_shiping;
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
    private LoadingView mProgressDialog;
    /* access modifiers changed from: private */
    public Handler mUiHandler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 7:
                    FVShiPingFragment.this.refreshAdapter();
                    return false;
                default:
                    return false;
            }
        }
    });
    /* access modifiers changed from: private */
    public boolean onSelect = true;
    /* access modifiers changed from: private */
    public int phoneWidth;
    private SwipeRefreshLayout swipe_refresh;
    private View view;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(C0853R.layout.fragment_file_shi_ping_home, container, false);
        this.mContext = getActivity();
        initData();
        initView();
        init();
        setListener();
        return this.view;
    }

    private void setListener() {
        this.file_paint_to_activity.setVisibility(8);
        this.file_paint_to_activity.setOnClickListener(this);
        this.swipe_refresh.setColorSchemeResources(17170459, 17170452, 17170456, 17170454);
        this.swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                FVShiPingFragment.this.upDateFiles();
            }
        });
        this.fragment_recycler_shiping.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == 0 || newState == 1) {
                    Glide.with(FVShiPingFragment.this.mContext).resumeRequests();
                } else {
                    Glide.with(FVShiPingFragment.this.mContext).pauseRequests();
                }
            }
        });
    }

    private void initData() {
        this.listPath = new ArrayList();
        this.listFileModel = new ArrayList();
        this.listSelectPath = new ArrayList();
        this.listItemPathLook = new ArrayList();
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getRealMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        if (width > height) {
            this.phoneWidth = height;
        } else {
            this.phoneWidth = width;
        }
    }

    private void init() {
        DataChangeNotification.getInstance().addObserver(IssueKey.PHOTO_CHECK, this);
        DataChangeNotification.getInstance().addObserver(IssueKey.PHOTO_CHECK_OK, this);
        DataChangeNotification.getInstance().addObserver(IssueKey.PHOTO_CHECK_LOOK, this);
        DataChangeNotification.getInstance().addObserver(IssueKey.VIDEO_CHECK_ALL, this);
        DataChangeNotification.getInstance().addObserver(IssueKey.PHOTO_VIDEO_DEIETE, this);
        upDateFiles();
    }

    /* access modifiers changed from: private */
    public void upDateFiles() {
        showProgress(getString(C0853R.string.file_show_pro_title));
        new Thread(new Runnable() {
            public void run() {
                FVShiPingFragment.this.listPath.clear();
                FVShiPingFragment.this.listPath.addAll(CameraPaths.getVideoPathFromSD(FVShiPingFragment.this.mContext));
                Gson gson = new Gson();
                String json = String.valueOf(GsonPhotoModelUtils.getPhotoModelGson(FVShiPingFragment.this.listPath));
                Log.i("KBein", "FVShiPingFragment.run(): json --->" + json);
                FilePhotoModel unused = FVShiPingFragment.this.filePhotoModel = (FilePhotoModel) gson.fromJson(json, FilePhotoModel.class);
                FVShiPingFragment.this.listFileModel.clear();
                FVShiPingFragment.this.listFileModel.addAll(FVShiPingFragment.this.filePhotoModel.getDate());
                FVShiPingFragment.this.mUiHandler.sendEmptyMessage(7);
            }
        }).start();
    }

    private void initView() {
        final String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        this.fragment_recycler_shiping = (RecyclerView) this.view.findViewById(C0853R.C0855id.fragment_recycler_shiping);
        this.file_paint_to_activity = (LinearLayout) this.view.findViewById(C0853R.C0855id.file_paint_to_activity);
        this.swipe_refresh = (SwipeRefreshLayout) this.view.findViewById(C0853R.C0855id.swipe_refresh);
        this.fragment_file_shiping_home_none = (RelativeLayout) this.view.findViewById(C0853R.C0855id.fragment_file_shiping_home_none);
        this.fragment_recycler_shiping.setLayoutManager(new LinearLayoutManager(this.mContext, 1, false));
        this.adapterOut = new BaseRVAdapter(this.mContext, this.listFileModel) {
            public int getLayoutId(int viewType) {
                return C0853R.layout.recycle_item_zhaopian;
            }

            public void onBind(BaseViewHolder holder, int pos) {
                LinearLayout recycle_item_zhaopian_recycle = (LinearLayout) holder.getView(C0853R.C0855id.recycle_item_zhaopian_recycle);
                LinearLayout.LayoutParams lps = (LinearLayout.LayoutParams) recycle_item_zhaopian_recycle.getLayoutParams();
                lps.rightMargin = ((FVShiPingFragment.this.phoneWidth - Util.dip2px(this.mContext, 300.0f)) - Util.dip2px(this.mContext, 4.0f)) - Util.dip2px(this.mContext, 37.0f);
                recycle_item_zhaopian_recycle.setLayoutParams(lps);
                View recycler_item_zhaopian_view_top = holder.getView(C0853R.C0855id.recycler_item_zhaopian_view_top);
                if (pos == 0) {
                    recycler_item_zhaopian_view_top.setBackgroundColor(FVShiPingFragment.this.getResources().getColor(C0853R.color.transparent));
                }
                TextView item_date_text_view = (TextView) holder.getView(C0853R.C0855id.recycler_item_zhaopian_date);
                if (Util.isZh(this.mContext)) {
                    if (timeStamp.substring(0, 4).equals(((FilePhotoModel.DateBean) FVShiPingFragment.this.listFileModel.get(pos)).getTime().substring(0, 4))) {
                        item_date_text_view.setText(((FilePhotoModel.DateBean) FVShiPingFragment.this.listFileModel.get(pos)).getTime().substring(4, 6) + "???" + ((FilePhotoModel.DateBean) FVShiPingFragment.this.listFileModel.get(pos)).getTime().substring(6, ((FilePhotoModel.DateBean) FVShiPingFragment.this.listFileModel.get(pos)).getTime().length()) + "???");
                    } else {
                        item_date_text_view.setText(((FilePhotoModel.DateBean) FVShiPingFragment.this.listFileModel.get(pos)).getTime().substring(0, 4) + "???" + ((FilePhotoModel.DateBean) FVShiPingFragment.this.listFileModel.get(pos)).getTime().substring(4, 6) + "???" + ((FilePhotoModel.DateBean) FVShiPingFragment.this.listFileModel.get(pos)).getTime().substring(6, ((FilePhotoModel.DateBean) FVShiPingFragment.this.listFileModel.get(pos)).getTime().length()) + "???");
                    }
                } else if (timeStamp.substring(0, 4).equals(((FilePhotoModel.DateBean) FVShiPingFragment.this.listFileModel.get(pos)).getTime().substring(0, 4))) {
                    item_date_text_view.setText(Util.zhToEnglishMonth(Integer.valueOf(((FilePhotoModel.DateBean) FVShiPingFragment.this.listFileModel.get(pos)).getTime().substring(4, 6)).intValue(), this.mContext) + " " + ((FilePhotoModel.DateBean) FVShiPingFragment.this.listFileModel.get(pos)).getTime().substring(6, ((FilePhotoModel.DateBean) FVShiPingFragment.this.listFileModel.get(pos)).getTime().length()));
                } else {
                    item_date_text_view.setText(Util.zhToEnglishMonth(Integer.valueOf(((FilePhotoModel.DateBean) FVShiPingFragment.this.listFileModel.get(pos)).getTime().substring(4, 6)).intValue(), this.mContext) + " " + ((FilePhotoModel.DateBean) FVShiPingFragment.this.listFileModel.get(pos)).getTime().substring(6, ((FilePhotoModel.DateBean) FVShiPingFragment.this.listFileModel.get(pos)).getTime().length()) + "," + ((FilePhotoModel.DateBean) FVShiPingFragment.this.listFileModel.get(pos)).getTime().substring(0, 4));
                }
                RecyclerView recyclerView = (RecyclerView) holder.getView(C0853R.C0855id.zhaopian_item_recycle_grid);
                recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 10);
                recyclerView.setLayoutManager(new GridLayoutManager(this.mContext, 3));
                List<FilePhotoModel.DateBean.ContentBean> listaa = ((FilePhotoModel.DateBean) FVShiPingFragment.this.listFileModel.get(pos)).getContent();
                final List<FilePhotoModel.DateBean.ContentBean> list = listaa;
                final int i = pos;
                recyclerView.setAdapter(new BaseRVAdapter(this.mContext, listaa) {
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
                        item_image_bianji.setVisibility(8);
                        if (!((String) SPUtils.get(this.mContext, SharePrefConstant.SAVE_STORAGE_SD_CARD_PATH, IntentKey.FILE_PATH)).contains(Constants.FILENAME_SEQUENCE_SEPARATOR)) {
                            path = IntentKey.FILE_PATH + ((FilePhotoModel.DateBean.ContentBean) list.get(position)).getPath();
                        } else {
                            path = Util.getParentPath(this.mContext) + ((FilePhotoModel.DateBean.ContentBean) list.get(position)).getPath();
                        }
                        if (path.substring(path.indexOf(".") - 6, path.indexOf(".")).equals("bianji") || path.substring(path.indexOf(".") - 4, path.indexOf(".")).equals("Edit")) {
                            item_image_bianji.setVisibility(0);
                            item_image_yuanpian.setVisibility(8);
                        }
                        ImageView photoView = itemImage;
                        FVShiPingFragment.this.displayImageTarget(photoView, path, FVShiPingFragment.this.getTarget(photoView, path, position));
                        if (FVShiPingFragment.this.listSelectPath.contains(((FilePhotoModel.DateBean.ContentBean) list.get(position)).getPath())) {
                            iv_file_item_pho_ll.setVisibility(0);
                            iv_file_item_pho_gou.setVisibility(0);
                        } else {
                            iv_file_item_pho_ll.setVisibility(8);
                            iv_file_item_pho_gou.setVisibility(8);
                        }
                        itemImage.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                if (FVShiPingFragment.this.onSelect) {
                                    List listItemPath = new ArrayList();
                                    listItemPath.clear();
                                    for (int s = 0; s < list.size(); s++) {
                                        listItemPath.add(((FilePhotoModel.DateBean.ContentBean) list.get(s)).getPath());
                                    }
                                    int aaa = 0;
                                    for (int s2 = 0; s2 < i; s2++) {
                                        aaa += ((FilePhotoModel.DateBean) FVShiPingFragment.this.listFileModel.get(s2)).getContent().size();
                                    }
                                    C12731.this.mContext.getSharedPreferences("user", 0).edit().putString("camera", BleConstant.SHUTTER).commit();
                                    Intent intent = new Intent(C12731.this.mContext, PlayVideoActivity.class);
                                    intent.putStringArrayListExtra(IntentKey.CLASS_JSON, (ArrayList) FVShiPingFragment.this.listItemPathLook);
                                    intent.putExtra(IntentKey.CLASS_POSION, position + aaa);
                                    C12731.this.mContext.startActivity(intent);
                                    return;
                                }
                                iv_file_item_pho_ll.setVisibility(0);
                                iv_file_item_pho_gou.setVisibility(0);
                                FVShiPingFragment.this.listSelectPath.add(((FilePhotoModel.DateBean.ContentBean) list.get(position)).getPath());
                                CameraUtils.setVideoSelectNums(FVShiPingFragment.this.listSelectPath.size());
                                if (FVShiPingFragment.this.listSelectPath.size() == 1) {
                                    DataChangeNotification.getInstance().notifyDataChanged(IssueKey.VIDEO_PHOTO_CAMERA_DELETE_CHECK_HAVA);
                                }
                            }
                        });
                        iv_file_item_pho_ll.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                iv_file_item_pho_ll.setVisibility(8);
                                iv_file_item_pho_gou.setVisibility(8);
                                Iterator<String> sListIterator = FVShiPingFragment.this.listSelectPath.iterator();
                                while (sListIterator.hasNext()) {
                                    if (sListIterator.next().equals(((FilePhotoModel.DateBean.ContentBean) list.get(position)).getPath())) {
                                        sListIterator.remove();
                                    }
                                }
                                CameraUtils.setVideoSelectNums(FVShiPingFragment.this.listSelectPath.size());
                                if (FVShiPingFragment.this.listSelectPath.size() == 0) {
                                    DataChangeNotification.getInstance().notifyDataChanged(IssueKey.VIDEO_PHOTO_CAMERA_DELETE_CHECK_HAVA_NONE);
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
            case C0853R.C0855id.file_paint_to_activity:
                startActivity(FileWriteActivity.createIntent(this.mContext, "shiping"));
                return;
            default:
                return;
        }
    }

    public void onDataChanged(IssueKey issue, Object o) {
        if (!IssueKey.PHOTO_CHECK.equals(issue)) {
            if (IssueKey.PHOTO_CHECK_OK.equals(issue)) {
                this.onSelect = false;
            } else if (IssueKey.PHOTO_CHECK_LOOK.equals(issue)) {
                this.onSelect = true;
                this.listSelectPath.clear();
                this.adapterOut.notifyDataSetChanged();
                CameraUtils.setVideoSelectNums(this.listSelectPath.size());
                DataChangeNotification.getInstance().notifyDataChanged(IssueKey.VIDEO_PHOTO_CAMERA_DELETE_CHECK_HAVA_NONE);
            } else if (IssueKey.VIDEO_CHECK_ALL.equals(issue)) {
                this.onSelect = false;
                this.listSelectPath.clear();
                this.listSelectPath.addAll(this.listPath);
                this.adapterOut.notifyDataSetChanged();
                CameraUtils.setVideoSelectNums(this.listSelectPath.size());
                DataChangeNotification.getInstance().notifyDataChanged(IssueKey.VIDEO_PHOTO_CAMERA_DELETE_CHECK_HAVA);
            } else if (IssueKey.PHOTO_VIDEO_DEIETE.equals(issue)) {
                deleteData();
            }
        }
    }

    private void deleteData() {
        String basePath;
        Log.i("KBein", "FVShiPingFragment.run(): ?????? ---> " + Thread.currentThread().getName());
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
                Util.updataMediaStore(this.mContext, file);
            }
            this.listSelectPath.clear();
            CameraUtils.setVideoSelectNums(this.listSelectPath.size());
            Toast.makeText(this.mContext, C0853R.string.photo_delete_finish, 0).show();
            DataChangeNotification.getInstance().notifyDataChanged(IssueKey.VIDEO_PHOTO_DELETE_END);
        }
        upDateFiles();
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

    public void showProgress(String msg) {
        if (this.mProgressDialog == null) {
            this.mProgressDialog = new LoadingView(this.mContext);
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

    /* access modifiers changed from: private */
    public void refreshAdapter() {
        if (this.listPath.size() > 0) {
            CameraUtils.setPhotoNums(this.listPath.size());
            CameraPaths.CameraFiratPath(this.filePhotoModel.getDate().get(0).getContent().get(0).getPath());
            this.fragment_recycler_shiping.setVisibility(0);
            this.fragment_file_shiping_home_none.setVisibility(8);
        } else {
            CameraUtils.setPhotoNums(this.listPath.size());
            CameraPaths.CameraFiratPath((String) null);
            this.fragment_recycler_shiping.setVisibility(8);
            this.fragment_file_shiping_home_none.setVisibility(0);
        }
        this.listItemPathLook.clear();
        for (int a = 0; a < this.filePhotoModel.getDate().size(); a++) {
            List<FilePhotoModel.DateBean.ContentBean> listaa = this.listFileModel.get(a).getContent();
            for (int s = 0; s < listaa.size(); s++) {
                this.listItemPathLook.add(listaa.get(s).getPath());
            }
        }
        this.adapterOut.notifyDataSetChanged();
        this.swipe_refresh.setRefreshing(false);
        DataChangeNotification.getInstance().notifyDataChanged(IssueKey.VIDEO_PHOTO_HAVE_OR_NONE);
        hideProgress();
    }

    public void onDestroy() {
        super.onDestroy();
        DataChangeNotification.getInstance().removeObserver((OnDataChangeObserver) this);
    }
}
