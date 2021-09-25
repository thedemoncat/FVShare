package com.freevisiontech.fvmobile.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.p001v4.app.Fragment;
import android.support.p003v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.activity.FVAboutAppActivity;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.freevisiontech.fvmobile.utility.DataCleanManager;
import com.freevisiontech.fvmobile.utility.Util;
import com.freevisiontech.fvmobile.utils.BleUpgradeUtil;
import java.io.File;

public class FVSettingFragment extends Fragment {
    /* access modifiers changed from: private */
    public FirstSettingAdapter firstSettingAdapter;
    private int[] imagesone = {C0853R.mipmap.qingchuhuancun, C0853R.mipmap.teaching_video_icon, C0853R.mipmap.guanyu};
    @Bind({2131755249})
    ImageView img_back;
    @Bind({2131756129})
    ImageView img_right;
    @Bind({2131755794})
    ListView listViewFirst;
    private int[] stringone = {C0853R.string.clean_catch, C0853R.string.teaching_video, C0853R.string.about_app};
    @Bind({2131756127})
    TextView tv_center_title;
    @Bind({2131756128})
    TextView tv_right;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(C0853R.layout.fragment_setting_home, container, false);
        ButterKnife.bind((Object) this, view);
        initTitle();
        initView();
        return view;
    }

    private void initView() {
        this.firstSettingAdapter = new FirstSettingAdapter(getActivity(), this.stringone, this.imagesone);
        this.listViewFirst.setAdapter(this.firstSettingAdapter);
        this.listViewFirst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        FVSettingFragment.this.cleanDialog();
                        return;
                    case 1:
                        FVSettingFragment.openBrowser(FVSettingFragment.this.getActivity(), "");
                        return;
                    case 2:
                        FVSettingFragment.this.startActivity(new Intent(FVSettingFragment.this.getActivity(), FVAboutAppActivity.class));
                        return;
                    default:
                        return;
                }
            }
        });
    }

    public static void openBrowser(Context context, String url) {
        String url2;
        if (!Util.isZh(context)) {
            url2 = "http://www.freevisiontech.com:8080/oss/help/SelectModelEnglish.html";
        } else if (Util.isZhFanTi(context)) {
            url2 = "http://www.freevisiontech.com:8080/oss/help/SelectModelFanti.html";
        } else {
            url2 = "http://www.freevisiontech.com:8080/oss/help/SelectModel.html";
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setData(Uri.parse(url2));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            ComponentName resolveActivity = intent.resolveActivity(context.getPackageManager());
            context.startActivity(Intent.createChooser(intent, context.getString(C0853R.string.label_browser_select)));
            return;
        }
        Toast.makeText(context.getApplicationContext(), context.getString(C0853R.string.label_browser_down), 0).show();
    }

    private void initTitle() {
        this.img_back.setVisibility(8);
        this.tv_center_title.setVisibility(0);
        this.tv_center_title.setText(getResources().getString(C0853R.string.about));
        this.tv_right.setVisibility(8);
        this.img_right.setVisibility(8);
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private class FirstSettingAdapter extends BaseAdapter {
        private Context context;
        private int[] img;
        private int[] str;

        FirstSettingAdapter(Context context2, int[] string, int[] image) {
            this.context = context2;
            this.str = string;
            this.img = image;
        }

        public int getCount() {
            if (this.str == null) {
                return 0;
            }
            return this.str.length;
        }

        public String getItem(int position) {
            if (this.str == null) {
                return null;
            }
            return FVSettingFragment.this.getString(this.str[position]);
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView != null) {
                holder = (ViewHolder) convertView.getTag();
            } else {
                convertView = View.inflate(this.context, C0853R.layout.adapter_setting_mode_one, (ViewGroup) null);
                holder = new ViewHolder();
                convertView.setTag(holder);
                holder.txt_mode = (TextView) convertView.findViewById(C0853R.C0855id.txt_mode);
                holder.txt_info = (TextView) convertView.findViewById(C0853R.C0855id.txt_info);
                holder.img_scan = (ImageView) convertView.findViewById(C0853R.C0855id.img_scan);
            }
            holder.txt_mode.setText(FVSettingFragment.this.getString(this.str[position]));
            holder.img_scan.setImageResource(this.img[position]);
            if (position == 0) {
                try {
                    String cacheSize = DataCleanManager.getTotalCacheSize(FVSettingFragment.this.getActivity());
                    if (!Util.isEmpty(cacheSize)) {
                        holder.txt_info.setText(cacheSize);
                        holder.txt_info.setVisibility(0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                holder.txt_info.setVisibility(0);
            }
            return convertView;
        }

        class ViewHolder {
            ImageView img_scan;
            TextView txt_info;
            TextView txt_mode;

            ViewHolder() {
            }
        }
    }

    /* access modifiers changed from: private */
    public void cleanDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage((int) C0853R.string.label_whether_clean_cache);
        builder.setNegativeButton((int) C0853R.string.label_cancel, (DialogInterface.OnClickListener) null);
        builder.setPositiveButton((int) C0853R.string.label_sure, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                DataCleanManager.clearAllCache(FVSettingFragment.this.getActivity());
                FVSettingFragment.this.firstSettingAdapter.notifyDataSetChanged();
                FVSettingFragment.this.cleanAd();
            }
        });
        builder.show();
    }

    /* access modifiers changed from: private */
    public void cleanAd() {
        File adFile = new File(new BleUpgradeUtil().getFileDir() + BleConstant.AD_Dir + "ad.jpg");
        if (adFile.exists()) {
            adFile.delete();
        }
    }
}
