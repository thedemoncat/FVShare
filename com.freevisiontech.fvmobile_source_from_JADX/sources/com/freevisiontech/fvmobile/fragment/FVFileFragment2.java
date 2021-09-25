package com.freevisiontech.fvmobile.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.p001v4.app.Fragment;
import android.support.p001v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.activity.FVHomeActivity;
import com.freevisiontech.fvmobile.activity.FileWriteActivity;
import com.freevisiontech.fvmobile.adapter.ActivityFileAdapter;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utility.SPUtils;
import com.freevisiontech.fvmobile.utility.SharePrefConstant;
import java.util.ArrayList;
import java.util.List;
import p010me.iwf.photopicker.widget.control.DataChangeNotification;
import p010me.iwf.photopicker.widget.control.IssueKey;
import p010me.iwf.photopicker.widget.control.OnDataChangeObserver;

public class FVFileFragment2 extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener, OnDataChangeObserver {
    public static final int SHIPING = 1;
    public static final int ZHAOPIAN = 0;
    private ViewPager fileViewPager;
    private LinearLayout file_paint_home_to_activity;
    private Context mContext;
    private Boolean paintShow = true;
    private TextView right_cancel;
    private TextView right_select;
    private int textColorCli;
    private int textColorNor;
    private List<TextView> textViews = new ArrayList();
    private TextView title_photo;
    private TextView title_video;
    private TextView tv_all_right;
    private RelativeLayout tv_all_right_cancel_select;
    private TextView tv_center_title;
    private View view;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(C0853R.layout.fragment_file_home, container, false);
        ininTitle();
        ininview();
        this.mContext = getActivity();
        DataChangeNotification.getInstance().addObserver(IssueKey.VIDEO_SELECT_CHECK, this);
        this.paintShow = true;
        DataChangeNotification.getInstance().addObserver(IssueKey.VIDEO_PHOTO_HAVE_OR_NONE, this);
        DataChangeNotification.getInstance().addObserver(IssueKey.VIDEO_PHOTO_DELETE_END, this);
        return this.view;
    }

    private void ininTitle() {
        this.tv_center_title = (TextView) this.view.findViewById(C0853R.C0855id.tv_file_center_title);
        this.tv_all_right = (TextView) this.view.findViewById(C0853R.C0855id.tv_all_right);
        this.right_cancel = (TextView) this.view.findViewById(C0853R.C0855id.tv_file_right_cancel);
        this.right_select = (TextView) this.view.findViewById(C0853R.C0855id.tv_file_right_select);
        this.tv_center_title.setText(C0853R.string.home_fragment_bottom_file);
        this.tv_center_title.getPaint().setFakeBoldText(true);
        this.tv_all_right.setOnClickListener(this);
        this.right_cancel.setOnClickListener(this);
        this.right_select.setOnClickListener(this);
        this.title_photo = (TextView) this.view.findViewById(C0853R.C0855id.frg_file_title_photo);
        this.title_video = (TextView) this.view.findViewById(C0853R.C0855id.frg_file_title_video);
        this.textViews.add(this.title_photo);
        this.textViews.add(this.title_video);
        this.textColorNor = getResources().getColor(C0853R.color.color_light_gray2);
        this.textColorCli = getResources().getColor(C0853R.color.color_black1);
        this.title_photo.setOnClickListener(this);
        this.title_video.setOnClickListener(this);
        this.file_paint_home_to_activity = (LinearLayout) this.view.findViewById(C0853R.C0855id.file_paint_home_to_activity);
        this.file_paint_home_to_activity.setOnClickListener(this);
        this.tv_all_right_cancel_select = (RelativeLayout) this.view.findViewById(C0853R.C0855id.tv_all_right_cancel_select);
        getActivity().getSharedPreferences("user", 0).edit().putString("camera", "0").commit();
    }

    private void ininview() {
        this.fileViewPager = (ViewPager) this.view.findViewById(C0853R.C0855id.file_view_pager);
        ActivityFileAdapter adapter = new ActivityFileAdapter(getActivity().getSupportFragmentManager());
        this.fileViewPager.setAdapter(adapter);
        this.fileViewPager.addOnPageChangeListener(this);
        if (((Boolean) SPUtils.get(getActivity(), SharePrefConstant.LAST_TAB_IMAGE_SELECTED, false)).booleanValue()) {
            this.fileViewPager.setCurrentItem(0);
            this.title_photo.setTextColor(this.textColorCli);
            this.title_video.setTextColor(this.textColorNor);
        } else {
            this.fileViewPager.setCurrentItem(1);
            this.title_photo.setTextColor(this.textColorNor);
            this.title_video.setTextColor(this.textColorCli);
        }
        adapter.notifyDataSetChanged();
    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0853R.C0855id.file_paint_home_to_activity:
                String look = getActivity().getSharedPreferences("user", 0).getString("camera", "");
                if (look.equals("0")) {
                    startActivity(FileWriteActivity.createIntent(this.mContext, "photo"));
                    return;
                } else if (look.equals(BleConstant.SHUTTER)) {
                    startActivity(FileWriteActivity.createIntent(this.mContext, "shiping"));
                    return;
                } else {
                    return;
                }
            case C0853R.C0855id.tv_all_right:
                String camera = getActivity().getSharedPreferences("user", 0).getString("camera", "");
                if (camera.equals("0")) {
                    DataChangeNotification.getInstance().notifyDataChanged(IssueKey.PHOTO_CHECK_ALL);
                    return;
                } else if (camera.equals(BleConstant.SHUTTER)) {
                    DataChangeNotification.getInstance().notifyDataChanged(IssueKey.VIDEO_CHECK_ALL);
                    return;
                } else {
                    return;
                }
            case C0853R.C0855id.tv_file_right_cancel:
                this.tv_all_right.setVisibility(8);
                this.right_cancel.setVisibility(8);
                this.right_select.setVisibility(0);
                ((FVHomeActivity) this.mContext).dateCancelChange();
                this.paintShow = true;
                this.file_paint_home_to_activity.setVisibility(0);
                DataChangeNotification.getInstance().notifyDataChanged(IssueKey.PHOTO_CHECK_LOOK);
                return;
            case C0853R.C0855id.tv_file_right_select:
                this.tv_all_right.setVisibility(0);
                this.right_cancel.setVisibility(0);
                this.right_select.setVisibility(8);
                ((FVHomeActivity) this.mContext).dateSelectChange();
                this.paintShow = false;
                this.file_paint_home_to_activity.setVisibility(8);
                DataChangeNotification.getInstance().notifyDataChanged(IssueKey.PHOTO_CHECK_OK);
                CameraUtils.setPhotoSelectNums(0);
                CameraUtils.setVideoSelectNums(0);
                return;
            case C0853R.C0855id.frg_file_title_photo:
                this.fileViewPager.setCurrentItem(0);
                getActivity().getSharedPreferences("user", 0).edit().putString("camera", "0").commit();
                SPUtils.put(getActivity(), SharePrefConstant.LAST_TAB_IMAGE_SELECTED, true);
                return;
            case C0853R.C0855id.frg_file_title_video:
                this.fileViewPager.setCurrentItem(1);
                getActivity().getSharedPreferences("user", 0).edit().putString("camera", BleConstant.SHUTTER).commit();
                SPUtils.put(getActivity(), SharePrefConstant.LAST_TAB_IMAGE_SELECTED, false);
                return;
            default:
                return;
        }
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                this.title_photo.setTextColor(this.textColorCli);
                this.title_video.setTextColor(this.textColorNor);
                getActivity().getSharedPreferences("user", 0).edit().putString("camera", "0").commit();
                SPUtils.put(getActivity(), SharePrefConstant.LAST_TAB_IMAGE_SELECTED, true);
                return;
            case 1:
                this.title_photo.setTextColor(this.textColorNor);
                this.title_video.setTextColor(this.textColorCli);
                getActivity().getSharedPreferences("user", 0).edit().putString("camera", BleConstant.SHUTTER).commit();
                SPUtils.put(getActivity(), SharePrefConstant.LAST_TAB_IMAGE_SELECTED, false);
                return;
            default:
                return;
        }
    }

    public void onPageScrollStateChanged(int state) {
    }

    public void onDataChanged(IssueKey issue, Object o) {
        if (issue.equals(IssueKey.VIDEO_SELECT_CHECK)) {
            this.fileViewPager.setCurrentItem(1);
            getActivity().getSharedPreferences("user", 0).edit().putString("camera", BleConstant.SHUTTER).commit();
        } else if (issue.equals(IssueKey.VIDEO_PHOTO_HAVE_OR_NONE)) {
            if (CameraUtils.getPhotoNums() == 0 && CameraUtils.getVideoNums() == 0) {
                this.file_paint_home_to_activity.setVisibility(8);
                this.tv_all_right.setVisibility(8);
                this.right_cancel.setVisibility(8);
                this.right_select.setVisibility(0);
                this.paintShow = true;
                this.tv_all_right_cancel_select.setVisibility(8);
                ((FVHomeActivity) this.mContext).dateCancelChange();
                DataChangeNotification.getInstance().notifyDataChanged(IssueKey.PHOTO_CHECK_LOOK);
                return;
            }
            if (this.paintShow.booleanValue()) {
                this.file_paint_home_to_activity.setVisibility(0);
            }
            this.tv_all_right_cancel_select.setVisibility(0);
        } else if (issue.equals(IssueKey.VIDEO_PHOTO_DELETE_END) && CameraUtils.getPhotoSelectNums() == 0 && CameraUtils.getVideoSelectNums() == 0) {
            this.tv_all_right.setVisibility(8);
            this.right_cancel.setVisibility(8);
            this.right_select.setVisibility(0);
            ((FVHomeActivity) this.mContext).dateCancelChange();
            this.paintShow = true;
            this.file_paint_home_to_activity.setVisibility(0);
            DataChangeNotification.getInstance().notifyDataChanged(IssueKey.PHOTO_CHECK_LOOK);
        }
    }
}
