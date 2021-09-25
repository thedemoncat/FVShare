package com.freevisiontech.fvmobile.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.p001v4.app.Fragment;
import android.support.p003v7.widget.GridLayoutManager;
import android.support.p003v7.widget.LinearLayoutManager;
import android.support.p003v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.activity.PhotoEditActivity;
import com.freevisiontech.fvmobile.base.BaseRVAdapter;
import com.freevisiontech.fvmobile.base.BaseViewHolder;
import com.freevisiontech.fvmobile.model.FilePhotoModel;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import p010me.iwf.photopicker.widget.control.DataChangeNotification;
import p010me.iwf.photopicker.widget.control.IssueKey;
import p010me.iwf.photopicker.widget.control.OnDataChangeObserver;

public class FVZhaoPianWriteFragment extends Fragment implements View.OnClickListener, OnDataChangeObserver {
    private static final int REFRESH_ADAPTER = 7;
    private BaseRVAdapter adapterOut;
    /* access modifiers changed from: private */
    public FilePhotoModel filePhotoModel;
    @Bind({2131755777})
    RelativeLayout fragment_file_zhaopian_write_none;
    @Bind({2131755774})
    RecyclerView fragment_recycler_zhaopian;
    /* access modifiers changed from: private */
    public List<FilePhotoModel.DateBean> listFileModel;
    /* access modifiers changed from: private */
    public List<String> listPath;
    /* access modifiers changed from: private */
    public Context mContext;
    private LoadingView mProgressDialog;
    /* access modifiers changed from: private */
    public Handler mUiHandler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 7:
                    FVZhaoPianWriteFragment.this.refreshAdapter();
                    return false;
                default:
                    return false;
            }
        }
    });
    /* access modifiers changed from: private */
    public boolean onSelect = true;
    /* access modifiers changed from: private */
    public Boolean onSelectAll = false;
    private int phoneHeight;
    /* access modifiers changed from: private */
    public int phoneWidth;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(C0853R.layout.fragment_file_zhao_pian_write, container, false);
        ButterKnife.bind((Object) this, view);
        this.mContext = getActivity();
        initData();
        initView();
        init();
        setListener();
        return view;
    }

    private void setListener() {
        this.fragment_recycler_zhaopian.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == 0 || newState == 1) {
                    Glide.with(FVZhaoPianWriteFragment.this.mContext).resumeRequests();
                } else {
                    Glide.with(FVZhaoPianWriteFragment.this.mContext).pauseRequests();
                }
            }
        });
    }

    private void init() {
        upDatePhotoMpdel();
    }

    private void initData() {
        this.listPath = new ArrayList();
        this.listFileModel = new ArrayList();
        DataChangeNotification.getInstance().addObserver(IssueKey.PHOTO_VIDEO_DEIETE, this);
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
            return;
        }
        this.phoneWidth = width;
        this.phoneHeight = height;
    }

    private void upDatePhotoMpdel() {
        showProgress(getString(C0853R.string.file_show_pro_title));
        new Thread() {
            public void run() {
                super.run();
                FVZhaoPianWriteFragment.this.listPath.clear();
                FVZhaoPianWriteFragment.this.listPath.addAll(CameraPaths.getImagePathFromSD(FVZhaoPianWriteFragment.this.mContext));
                FilePhotoModel unused = FVZhaoPianWriteFragment.this.filePhotoModel = (FilePhotoModel) new Gson().fromJson(String.valueOf(GsonPhotoModelUtils.getPhotoModelGson(FVZhaoPianWriteFragment.this.listPath)), FilePhotoModel.class);
                FVZhaoPianWriteFragment.this.listFileModel.clear();
                FVZhaoPianWriteFragment.this.listFileModel.addAll(FVZhaoPianWriteFragment.this.filePhotoModel.getDate());
                FVZhaoPianWriteFragment.this.mUiHandler.sendEmptyMessage(7);
            }
        }.start();
    }

    /* access modifiers changed from: private */
    public void refreshAdapter() {
        if (this.listPath.size() > 0) {
            this.fragment_recycler_zhaopian.setVisibility(0);
            this.fragment_file_zhaopian_write_none.setVisibility(8);
        } else {
            this.fragment_recycler_zhaopian.setVisibility(8);
            this.fragment_file_zhaopian_write_none.setVisibility(0);
        }
        this.adapterOut.notifyDataSetChanged();
        hideProgress();
    }

    private void initView() {
        final String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        this.fragment_recycler_zhaopian.setLayoutManager(new LinearLayoutManager(this.mContext, 1, false));
        this.adapterOut = new BaseRVAdapter(this.mContext, this.listFileModel) {
            public int getLayoutId(int viewType) {
                return C0853R.layout.recycle_item_zhaopian;
            }

            public void onBind(BaseViewHolder holder, int position) {
                LinearLayout recycle_item_zhaopian_recycle = (LinearLayout) holder.getView(C0853R.C0855id.recycle_item_zhaopian_recycle);
                LinearLayout.LayoutParams lps = (LinearLayout.LayoutParams) recycle_item_zhaopian_recycle.getLayoutParams();
                lps.rightMargin = ((FVZhaoPianWriteFragment.this.phoneWidth - Util.dip2px(this.mContext, 300.0f)) - Util.dip2px(this.mContext, 4.0f)) - Util.dip2px(this.mContext, 37.0f);
                recycle_item_zhaopian_recycle.setLayoutParams(lps);
                View recycler_item_zhaopian_view_top = holder.getView(C0853R.C0855id.recycler_item_zhaopian_view_top);
                if (position == 0) {
                    recycler_item_zhaopian_view_top.setBackgroundColor(FVZhaoPianWriteFragment.this.getResources().getColor(C0853R.color.transparent));
                }
                TextView item_date_text_view = (TextView) holder.getView(C0853R.C0855id.recycler_item_zhaopian_date);
                if (Util.isZh(this.mContext)) {
                    if (timeStamp.substring(0, 4).equals(((FilePhotoModel.DateBean) FVZhaoPianWriteFragment.this.listFileModel.get(position)).getTime().substring(0, 4))) {
                        item_date_text_view.setText(((FilePhotoModel.DateBean) FVZhaoPianWriteFragment.this.listFileModel.get(position)).getTime().substring(4, 6) + "月" + ((FilePhotoModel.DateBean) FVZhaoPianWriteFragment.this.listFileModel.get(position)).getTime().substring(6, ((FilePhotoModel.DateBean) FVZhaoPianWriteFragment.this.listFileModel.get(position)).getTime().length()) + "日");
                    } else {
                        item_date_text_view.setText(((FilePhotoModel.DateBean) FVZhaoPianWriteFragment.this.listFileModel.get(position)).getTime().substring(0, 4) + "年" + ((FilePhotoModel.DateBean) FVZhaoPianWriteFragment.this.listFileModel.get(position)).getTime().substring(4, 6) + "月" + ((FilePhotoModel.DateBean) FVZhaoPianWriteFragment.this.listFileModel.get(position)).getTime().substring(6, ((FilePhotoModel.DateBean) FVZhaoPianWriteFragment.this.listFileModel.get(position)).getTime().length()) + "日");
                    }
                } else if (timeStamp.substring(0, 4).equals(((FilePhotoModel.DateBean) FVZhaoPianWriteFragment.this.listFileModel.get(position)).getTime().substring(0, 4))) {
                    item_date_text_view.setText(Util.zhToEnglishMonth(Integer.valueOf(((FilePhotoModel.DateBean) FVZhaoPianWriteFragment.this.listFileModel.get(position)).getTime().substring(4, 6)).intValue(), this.mContext) + " " + ((FilePhotoModel.DateBean) FVZhaoPianWriteFragment.this.listFileModel.get(position)).getTime().substring(6, ((FilePhotoModel.DateBean) FVZhaoPianWriteFragment.this.listFileModel.get(position)).getTime().length()));
                } else {
                    item_date_text_view.setText(Util.zhToEnglishMonth(Integer.valueOf(((FilePhotoModel.DateBean) FVZhaoPianWriteFragment.this.listFileModel.get(position)).getTime().substring(4, 6)).intValue(), this.mContext) + " " + ((FilePhotoModel.DateBean) FVZhaoPianWriteFragment.this.listFileModel.get(position)).getTime().substring(6, ((FilePhotoModel.DateBean) FVZhaoPianWriteFragment.this.listFileModel.get(position)).getTime().length()) + "," + ((FilePhotoModel.DateBean) FVZhaoPianWriteFragment.this.listFileModel.get(position)).getTime().substring(0, 4));
                }
                RecyclerView recyclerView = (RecyclerView) holder.getView(C0853R.C0855id.zhaopian_item_recycle_grid);
                recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 10);
                recyclerView.setLayoutManager(new GridLayoutManager(this.mContext, 3));
                final List<FilePhotoModel.DateBean.ContentBean> listaa = ((FilePhotoModel.DateBean) FVZhaoPianWriteFragment.this.listFileModel.get(position)).getContent();
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
                        item_image_yuanpian.setText(FVZhaoPianWriteFragment.this.getString(C0853R.string.file_edit_the_original_image));
                        item_image_bianji.setVisibility(8);
                        if (!((String) SPUtils.get(this.mContext, SharePrefConstant.SAVE_STORAGE_SD_CARD_PATH, IntentKey.FILE_PATH)).contains(Constants.FILENAME_SEQUENCE_SEPARATOR)) {
                            path = IntentKey.FILE_PATH + ((FilePhotoModel.DateBean.ContentBean) listaa.get(position)).getPath();
                        } else {
                            path = Util.getParentPath(this.mContext) + ((FilePhotoModel.DateBean.ContentBean) listaa.get(position)).getPath();
                        }
                        if (path.substring(path.indexOf(".") - 6, path.indexOf(".")).equals("bianji") || path.substring(path.indexOf(".") - 6, path.indexOf(".")).equals("mosaic") || path.substring(path.indexOf(".") - 4, path.indexOf(".")).equals("Edit")) {
                            item_image_bianji.setVisibility(0);
                            item_image_yuanpian.setVisibility(8);
                        }
                        ImageView photoView = itemImage;
                        FVZhaoPianWriteFragment.this.displayImageTarget(photoView, path, FVZhaoPianWriteFragment.this.getTarget(photoView, path, position));
                        if (FVZhaoPianWriteFragment.this.onSelectAll.booleanValue()) {
                            iv_file_item_pho_ll.setVisibility(0);
                            iv_file_item_pho_gou.setVisibility(0);
                        } else {
                            iv_file_item_pho_ll.setVisibility(8);
                            iv_file_item_pho_gou.setVisibility(8);
                        }
                        itemImage.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                String basePath;
                                if (FVZhaoPianWriteFragment.this.onSelect) {
                                    List listItemPath = new ArrayList();
                                    listItemPath.clear();
                                    for (int s = 0; s < listaa.size(); s++) {
                                        listItemPath.add(((FilePhotoModel.DateBean.ContentBean) listaa.get(s)).getPath());
                                    }
                                    Intent intent = new Intent(C13281.this.mContext, PhotoEditActivity.class);
                                    if (!((String) SPUtils.get(C13281.this.mContext, SharePrefConstant.SAVE_STORAGE_SD_CARD_PATH, IntentKey.FILE_PATH)).contains(Constants.FILENAME_SEQUENCE_SEPARATOR)) {
                                        basePath = IntentKey.FILE_PATH;
                                    } else {
                                        basePath = Util.getParentPath(C13281.this.mContext);
                                    }
                                    intent.putExtra(IntentKey.PHOTOEDITPATH, basePath + ((FilePhotoModel.DateBean.ContentBean) listaa.get(position)).getPath());
                                    C13281.this.mContext.startActivity(intent);
                                    return;
                                }
                                iv_file_item_pho_ll.setVisibility(0);
                                iv_file_item_pho_gou.setVisibility(0);
                            }
                        });
                        iv_file_item_pho_ll.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                iv_file_item_pho_ll.setVisibility(8);
                                iv_file_item_pho_gou.setVisibility(8);
                            }
                        });
                    }
                });
            }
        };
        this.fragment_recycler_zhaopian.setAdapter(this.adapterOut);
    }

    public void onDestroy() {
        super.onDestroy();
        this.filePhotoModel = null;
        DataChangeNotification.getInstance().removeObserver((OnDataChangeObserver) this);
    }

    public void onClick(View v) {
        v.getId();
    }

    public static void MoveToPosition(LinearLayoutManager manager, int n) {
        manager.scrollToPositionWithOffset(n, 0);
        manager.setStackFromEnd(true);
    }

    public void showProgress(String msg) {
        if (this.mProgressDialog == null) {
            this.mProgressDialog = new LoadingView(getActivity().getWindow().getContext());
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

    public void onDataChanged(IssueKey issue, Object o) {
        showProgress(getString(C0853R.string.file_show_pro_title));
        upDatePhotoMpdel();
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
}
