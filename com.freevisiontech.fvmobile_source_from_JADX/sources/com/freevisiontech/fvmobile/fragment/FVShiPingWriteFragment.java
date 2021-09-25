package com.freevisiontech.fvmobile.fragment;

import android.app.AlertDialog;
import android.content.Context;
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
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.activity.VideoEditingActivity;
import com.freevisiontech.fvmobile.base.BaseRVAdapter;
import com.freevisiontech.fvmobile.base.BaseViewHolder;
import com.freevisiontech.fvmobile.base.recyclerview.base.BaseRecyclerAdapter;
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
import java.util.Iterator;
import java.util.List;
import p010me.iwf.photopicker.widget.control.DataChangeNotification;
import p010me.iwf.photopicker.widget.control.IssueKey;
import p010me.iwf.photopicker.widget.control.OnDataChangeObserver;

public class FVShiPingWriteFragment extends Fragment implements View.OnClickListener, OnDataChangeObserver {
    private static final int REFRESH_ADAPTER = 7;
    private BaseRVAdapter adapterOut;
    /* access modifiers changed from: private */
    public FilePhotoModel filePhotoModel;
    @Bind({2131755769})
    LinearLayout file_paint_to_activity;
    @Bind({2131755509})
    LinearLayout fragment_file_shiping_create;
    @Bind({2131755772})
    RelativeLayout fragment_file_shiping_write_none;
    @Bind({2131755508})
    RecyclerView fragment_recycler_shiping;
    /* access modifiers changed from: private */
    public List<FilePhotoModel.DateBean> listFileModel;
    /* access modifiers changed from: private */
    public List<String> listPath;
    /* access modifiers changed from: private */
    public Context mContext;
    private AlertDialog mProgressDialog;
    /* access modifiers changed from: private */
    public Handler mUiHandler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 7:
                    FVShiPingWriteFragment.this.refreshAdapter();
                    return false;
                default:
                    return false;
            }
        }
    });
    private boolean onSelect = true;
    /* access modifiers changed from: private */
    public Boolean onSelectAll = false;
    private int phoneHeight;
    /* access modifiers changed from: private */
    public int phoneWidth;
    private BaseRecyclerAdapter recyclerAdapter;
    @Bind({2131755773})
    TextView video_edit_to_create;
    /* access modifiers changed from: private */
    public List videosPath;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(C0853R.layout.fragment_file_shi_ping_write, container, false);
        ButterKnife.bind((Object) this, view);
        this.mContext = getActivity();
        initData();
        initView();
        init();
        setListener();
        return view;
    }

    private void setListener() {
        this.fragment_file_shiping_create.setOnClickListener(this);
        this.fragment_recycler_shiping.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == 0 || newState == 1) {
                    Glide.with(FVShiPingWriteFragment.this.mContext).resumeRequests();
                } else {
                    Glide.with(FVShiPingWriteFragment.this.mContext).pauseRequests();
                }
            }
        });
    }

    private void init() {
        upDatePhotoMpdel();
    }

    private void initData() {
        this.file_paint_to_activity.setVisibility(8);
        this.file_paint_to_activity.setOnClickListener(this);
        this.videosPath = new ArrayList();
        this.listPath = new ArrayList();
        this.listFileModel = new ArrayList();
        this.fragment_file_shiping_create.setBackgroundResource(C0853R.color.white);
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

    private void upDatePhotoMpdel() {
        showProgress(getString(C0853R.string.file_show_pro_title));
        new Thread() {
            public void run() {
                super.run();
                FVShiPingWriteFragment.this.listPath.clear();
                FVShiPingWriteFragment.this.listPath.addAll(CameraPaths.getVideoPathFromSD(FVShiPingWriteFragment.this.mContext));
                Iterator<String> sListIterator = FVShiPingWriteFragment.this.listPath.iterator();
                while (sListIterator.hasNext()) {
                    String e = sListIterator.next();
                    if (e.substring(e.length() - 10, e.length() - 4).equals("bianji") || e.substring(e.length() - 10, e.length() - 4).equals("yidong") || e.substring(e.length() - 13, e.length() - 4).equals("TimeLapse") || e.substring(e.length() - 8, e.length() - 4).equals("Edit")) {
                        sListIterator.remove();
                    }
                }
                FilePhotoModel unused = FVShiPingWriteFragment.this.filePhotoModel = (FilePhotoModel) new Gson().fromJson(String.valueOf(GsonPhotoModelUtils.getPhotoModelGson(FVShiPingWriteFragment.this.listPath)), FilePhotoModel.class);
                FVShiPingWriteFragment.this.listFileModel.clear();
                FVShiPingWriteFragment.this.listFileModel.addAll(FVShiPingWriteFragment.this.filePhotoModel.getDate());
                FVShiPingWriteFragment.this.mUiHandler.sendEmptyMessage(7);
            }
        }.start();
    }

    /* access modifiers changed from: private */
    public void refreshAdapter() {
        if (this.listPath.size() > 0) {
            this.fragment_recycler_shiping.setVisibility(0);
            this.fragment_file_shiping_write_none.setVisibility(8);
        } else {
            this.fragment_recycler_shiping.setVisibility(8);
            this.fragment_file_shiping_write_none.setVisibility(0);
        }
        this.adapterOut.notifyDataSetChanged();
        hideProgress();
    }

    private void initView() {
        final String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        this.fragment_recycler_shiping.setLayoutManager(new LinearLayoutManager(this.mContext, 1, false));
        this.adapterOut = new BaseRVAdapter(this.mContext, this.listFileModel) {
            public int getLayoutId(int viewType) {
                return C0853R.layout.recycle_item_zhaopian;
            }

            public void onBind(BaseViewHolder holder, int position) {
                LinearLayout recycle_item_zhaopian_recycle = (LinearLayout) holder.getView(C0853R.C0855id.recycle_item_zhaopian_recycle);
                LinearLayout.LayoutParams lps = (LinearLayout.LayoutParams) recycle_item_zhaopian_recycle.getLayoutParams();
                lps.rightMargin = ((FVShiPingWriteFragment.this.phoneWidth - Util.dip2px(this.mContext, 300.0f)) - Util.dip2px(this.mContext, 4.0f)) - Util.dip2px(this.mContext, 37.0f);
                recycle_item_zhaopian_recycle.setLayoutParams(lps);
                View recycler_item_zhaopian_view_top = holder.getView(C0853R.C0855id.recycler_item_zhaopian_view_top);
                if (position == 0) {
                    recycler_item_zhaopian_view_top.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.transparent));
                }
                TextView item_date_text_view = (TextView) holder.getView(C0853R.C0855id.recycler_item_zhaopian_date);
                if (Util.isZh(this.mContext)) {
                    if (timeStamp.substring(0, 4).equals(((FilePhotoModel.DateBean) FVShiPingWriteFragment.this.listFileModel.get(position)).getTime().substring(0, 4))) {
                        item_date_text_view.setText(((FilePhotoModel.DateBean) FVShiPingWriteFragment.this.listFileModel.get(position)).getTime().substring(4, 6) + "月" + ((FilePhotoModel.DateBean) FVShiPingWriteFragment.this.listFileModel.get(position)).getTime().substring(6, ((FilePhotoModel.DateBean) FVShiPingWriteFragment.this.listFileModel.get(position)).getTime().length()) + "日");
                    } else {
                        item_date_text_view.setText(((FilePhotoModel.DateBean) FVShiPingWriteFragment.this.listFileModel.get(position)).getTime().substring(0, 4) + "年" + ((FilePhotoModel.DateBean) FVShiPingWriteFragment.this.listFileModel.get(position)).getTime().substring(4, 6) + "月" + ((FilePhotoModel.DateBean) FVShiPingWriteFragment.this.listFileModel.get(position)).getTime().substring(6, ((FilePhotoModel.DateBean) FVShiPingWriteFragment.this.listFileModel.get(position)).getTime().length()) + "日");
                    }
                } else if (timeStamp.substring(0, 4).equals(((FilePhotoModel.DateBean) FVShiPingWriteFragment.this.listFileModel.get(position)).getTime().substring(0, 4))) {
                    item_date_text_view.setText(Util.zhToEnglishMonth(Integer.valueOf(((FilePhotoModel.DateBean) FVShiPingWriteFragment.this.listFileModel.get(position)).getTime().substring(4, 6)).intValue(), this.mContext) + " " + ((FilePhotoModel.DateBean) FVShiPingWriteFragment.this.listFileModel.get(position)).getTime().substring(6, ((FilePhotoModel.DateBean) FVShiPingWriteFragment.this.listFileModel.get(position)).getTime().length()));
                } else {
                    item_date_text_view.setText(Util.zhToEnglishMonth(Integer.valueOf(((FilePhotoModel.DateBean) FVShiPingWriteFragment.this.listFileModel.get(position)).getTime().substring(4, 6)).intValue(), this.mContext) + " " + ((FilePhotoModel.DateBean) FVShiPingWriteFragment.this.listFileModel.get(position)).getTime().substring(6, ((FilePhotoModel.DateBean) FVShiPingWriteFragment.this.listFileModel.get(position)).getTime().length()) + "," + ((FilePhotoModel.DateBean) FVShiPingWriteFragment.this.listFileModel.get(position)).getTime().substring(0, 4));
                }
                RecyclerView recyclerView = (RecyclerView) holder.getView(C0853R.C0855id.zhaopian_item_recycle_grid);
                recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 10);
                recyclerView.setLayoutManager(new GridLayoutManager(this.mContext, 3));
                final List<FilePhotoModel.DateBean.ContentBean> listaa = ((FilePhotoModel.DateBean) FVShiPingWriteFragment.this.listFileModel.get(position)).getContent();
                recyclerView.setAdapter(new BaseRVAdapter(this.mContext, listaa) {
                    public int getLayoutId(int viewType) {
                        return C0853R.layout.recycle_item_grid_item;
                    }

                    public void onBind(BaseViewHolder holder, int position) {
                        final String path;
                        ImageView itemImage = (ImageView) holder.getView(C0853R.C0855id.iv_recycle_item_image);
                        final LinearLayout iv_file_item_pho_ll = (LinearLayout) holder.getView(C0853R.C0855id.iv_file_item_pho_ll);
                        final ImageView iv_file_item_pho_gou = (ImageView) holder.getView(C0853R.C0855id.iv_file_item_pho_gou);
                        TextView item_image_yuanpian = (TextView) holder.getView(C0853R.C0855id.iv_recycle_item_image_yuanpian);
                        TextView item_image_bianji = (TextView) holder.getView(C0853R.C0855id.iv_recycle_item_image_bianji);
                        item_image_yuanpian.setVisibility(0);
                        item_image_bianji.setVisibility(8);
                        if (!((String) SPUtils.get(this.mContext, SharePrefConstant.SAVE_STORAGE_SD_CARD_PATH, IntentKey.FILE_PATH)).contains(Constants.FILENAME_SEQUENCE_SEPARATOR)) {
                            path = IntentKey.FILE_PATH + ((FilePhotoModel.DateBean.ContentBean) listaa.get(position)).getPath();
                        } else {
                            path = Util.getParentPath(this.mContext) + ((FilePhotoModel.DateBean.ContentBean) listaa.get(position)).getPath();
                        }
                        if (FVShiPingWriteFragment.this.videosPath.contains(path)) {
                            iv_file_item_pho_ll.setVisibility(0);
                            iv_file_item_pho_gou.setVisibility(0);
                        } else {
                            iv_file_item_pho_ll.setVisibility(8);
                            iv_file_item_pho_gou.setVisibility(8);
                        }
                        if (path.substring(path.indexOf(".") - 6, path.indexOf(".")).equals("bianji") || path.substring(path.indexOf(".") - 4, path.indexOf(".")).equals("Edit")) {
                            item_image_bianji.setVisibility(0);
                            item_image_yuanpian.setVisibility(8);
                        }
                        ImageView photoView = itemImage;
                        FVShiPingWriteFragment.this.displayImageTarget(photoView, path, FVShiPingWriteFragment.this.getTarget(photoView, path, position));
                        if (FVShiPingWriteFragment.this.onSelectAll.booleanValue()) {
                            iv_file_item_pho_ll.setVisibility(0);
                            iv_file_item_pho_gou.setVisibility(0);
                        } else {
                            iv_file_item_pho_ll.setVisibility(8);
                            iv_file_item_pho_gou.setVisibility(8);
                        }
                        itemImage.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                iv_file_item_pho_ll.setVisibility(0);
                                iv_file_item_pho_gou.setVisibility(0);
                                FVShiPingWriteFragment.this.videosPath.add(path);
                                FVShiPingWriteFragment.this.video_edit_to_create.setAlpha(1.0f);
                            }
                        });
                        iv_file_item_pho_ll.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                iv_file_item_pho_ll.setVisibility(8);
                                iv_file_item_pho_gou.setVisibility(8);
                                Iterator<String> sListIterator = FVShiPingWriteFragment.this.videosPath.iterator();
                                while (sListIterator.hasNext()) {
                                    if (sListIterator.next().equals(path)) {
                                        sListIterator.remove();
                                    }
                                }
                                if (FVShiPingWriteFragment.this.videosPath.size() == 0) {
                                    FVShiPingWriteFragment.this.video_edit_to_create.setAlpha(0.2f);
                                } else {
                                    FVShiPingWriteFragment.this.video_edit_to_create.setAlpha(1.0f);
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
                    Toast.makeText(this.mContext, C0853R.string.file_select_video, 0).show();
                    return;
                } else {
                    startActivity(VideoEditingActivity.createIntent(this.mContext, this.videosPath));
                    return;
                }
            default:
                return;
        }
    }

    public void onDestroy() {
        super.onDestroy();
        this.filePhotoModel = null;
        DataChangeNotification.getInstance().removeObserver((OnDataChangeObserver) this);
    }

    public void onDataChanged(IssueKey issue, Object o) {
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
