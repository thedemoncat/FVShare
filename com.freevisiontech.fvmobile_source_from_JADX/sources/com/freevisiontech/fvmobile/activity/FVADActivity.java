package com.freevisiontech.fvmobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.p003v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.utility.Util;
import com.vise.log.ViseLog;

public class FVADActivity extends AppCompatActivity {
    @Bind({2131755560})
    WebView idWebview;
    @Bind({2131755249})
    ImageView imgBack;
    private String mAdUrl;
    private String mTitle;
    @Bind({2131756127})
    TextView tvCenterTitle;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0853R.layout.activity_web_view);
        getWindow().setFlags(1024, 1024);
        Util.hideBottomUIMenu(this);
        ButterKnife.bind((Activity) this);
        this.mTitle = getIntent().getStringExtra("adTitle");
        this.mAdUrl = getIntent().getStringExtra("adUrl");
        initView();
    }

    private void initView() {
        this.imgBack.setVisibility(0);
        this.tvCenterTitle.setVisibility(0);
        this.tvCenterTitle.setText(this.mTitle);
        this.idWebview.loadUrl(this.mAdUrl);
        ViseLog.m1466e("AdTest--mAdUrl--" + this.mAdUrl + "--mTitle--" + this.mTitle);
        WebSettings webSettings = this.idWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDisplayZoomControls(false);
        this.idWebview.setWebViewClient(new WebClient());
    }

    public class WebClient extends WebViewClient {
        public WebClient() {
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
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
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
    }

    @OnClick({2131755249})
    public void onViewClicked() {
        startActivity(new Intent(this, FVHomeActivity.class));
        finish();
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, FVHomeActivity.class));
        finish();
    }
}
