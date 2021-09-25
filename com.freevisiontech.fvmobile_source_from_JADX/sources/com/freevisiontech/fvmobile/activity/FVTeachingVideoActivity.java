package com.freevisiontech.fvmobile.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.p003v7.app.AppCompatActivity;
import android.support.p003v7.widget.LinearLayoutManager;
import android.support.p003v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.adapter.TeachingVideoRvAdapter;
import com.freevisiontech.fvmobile.bean.TeachingVideo;
import com.freevisiontech.fvmobile.common.BleConstant;
import java.util.ArrayList;
import java.util.List;

public class FVTeachingVideoActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind({2131755249})
    ImageView mBackIv;
    private List<TeachingVideo> mData;
    private RecyclerView.LayoutManager mLayoutManager;
    @Bind({2131756129})
    ImageView mRightIv;
    @Bind({2131756128})
    TextView mRightTv;
    private TeachingVideoRvAdapter mRvAdaper;
    @Bind({2131756127})
    TextView mTitleTv;
    @Bind({2131755514})
    RecyclerView mVideosRv;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0853R.layout.activity_teaching_video);
        ButterKnife.bind((Activity) this);
        initData();
        initView();
    }

    private void initData() {
        this.mData = new ArrayList();
        this.mData.add(new TeachingVideo(getString(C0853R.string.product_presentation), getString(C0853R.string.product_presentation_url)));
        this.mData.add(new TeachingVideo(getString(C0853R.string.cradle_head_installation), getString(C0853R.string.cradle_head_installation_url)));
        this.mData.add(new TeachingVideo(getString(C0853R.string.connect_the_application), getString(C0853R.string.connect_the_application_url)));
        this.mData.add(new TeachingVideo(getString(C0853R.string.basic_operation), getString(C0853R.string.basic_operation_url)));
        this.mData.add(new TeachingVideo(getString(C0853R.string.mode_swapping), getString(C0853R.string.mode_swapping_url)));
        this.mData.add(new TeachingVideo(getString(C0853R.string.time_lapse_photography), getString(C0853R.string.time_lapse_photography_url)));
        this.mData.add(new TeachingVideo(getString(C0853R.string.motion_delay_photography), getString(C0853R.string.motion_delay_photography_url)));
        this.mData.add(new TeachingVideo(getString(C0853R.string.intelligent_tracking), getString(C0853R.string.intelligent_tracking_url)));
        this.mData.add(new TeachingVideo(getString(C0853R.string.mode_using_example), getString(C0853R.string.mode_using_example_url)));
    }

    private void initView() {
        this.mBackIv.setVisibility(0);
        this.mBackIv.setImageResource(C0853R.mipmap.fanhui);
        this.mTitleTv.setVisibility(0);
        this.mTitleTv.setText(BleConstant.FM_200_DISPLAY_NAME + getResources().getString(C0853R.string.teaching_video));
        this.mRightTv.setVisibility(8);
        this.mRightIv.setVisibility(8);
        this.mBackIv.setOnClickListener(this);
        this.mRvAdaper = new TeachingVideoRvAdapter(this.mData);
        this.mVideosRv.setAdapter(this.mRvAdaper);
        this.mLayoutManager = new LinearLayoutManager(this);
        this.mVideosRv.setLayoutManager(this.mLayoutManager);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0853R.C0855id.img_back:
                finish();
                return;
            default:
                return;
        }
    }
}
