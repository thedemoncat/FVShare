package com.freevisiontech.fvmobile.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.utility.Util;
import p012tv.danmaku.ijk.media.player.IjkMediaMeta;

public class FVTeachingVideoSActivity extends Activity {
    @Bind({2131755249})
    ImageView imgBack;
    @Bind({2131756129})
    ImageView imgRight;
    boolean loadError = false;
    /* access modifiers changed from: private */
    public WebView mWebView;
    @Bind({2131756127})
    TextView tvCenterTitle;
    @Bind({2131756128})
    TextView tvRight;
    private String type;

    public static Intent creatIntent(Context context, String i) {
        Intent intent = new Intent(context, FVTeachingVideoSActivity.class);
        intent.putExtra(IjkMediaMeta.IJKM_KEY_TYPE, i);
        return intent;
    }

    private void parseIntent() {
        this.type = getIntent().getStringExtra(IjkMediaMeta.IJKM_KEY_TYPE);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0853R.layout.activity_web_view);
        ButterKnife.bind((Activity) this);
        parseIntent();
        initTitle();
        this.mWebView = (WebView) findViewById(C0853R.C0855id.id_webview);
        WebSettings webSettings = this.mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.setMixedContentMode(0);
        }
        if (!Util.isZh(this)) {
            this.mWebView.loadUrl("http://www.freevisiontech.com/oss/help/SelectModelEnglish.html");
        } else if (Util.isZhFanTi(this)) {
            this.mWebView.loadUrl("http://www.freevisiontech.com/oss/help/SelectModelFanti.html");
        } else {
            this.mWebView.loadUrl("http://www.freevisiontech.com/oss/help/SelectModel.html");
        }
        this.tvCenterTitle.setText("VILTA_MOBILE　" + getResources().getString(C0853R.string.teaching_video));
        this.mWebView.setWebViewClient(new WebClient());
    }

    private void initTitle() {
        this.imgBack.setVisibility(0);
        this.imgBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FVTeachingVideoSActivity.this.finish();
            }
        });
        this.tvCenterTitle.setVisibility(0);
        this.tvRight.setVisibility(8);
        this.imgRight.setVisibility(8);
    }

    public class WebClient extends WebViewClient {
        public WebClient() {
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (FVTeachingVideoSActivity.this.loadError) {
                FVTeachingVideoSActivity.this.mWebView.loadUrl("http://www.freevisiontech.com/oss/help/error.html");
            }
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            FVTeachingVideoSActivity.this.loadError = true;
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
