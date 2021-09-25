package com.freevisiontech.fvmobile.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.freevisiontech.fvmobile.utility.Util;
import com.freevisiontech.fvmobile.utils.LocalResourceManager;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import p012tv.danmaku.ijk.media.player.IjkMediaMeta;

public class FVWebActivity extends Activity {
    @Bind({2131755249})
    ImageView imgBack;
    @Bind({2131756129})
    ImageView imgRight;
    private WebView mWebView;
    @Bind({2131756127})
    TextView tvCenterTitle;
    @Bind({2131756128})
    TextView tvRight;
    private String type;

    public static Intent creatIntent(Context context, String i) {
        Intent intent = new Intent(context, FVWebActivity.class);
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
        this.mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        new File(Environment.getExternalStorageDirectory().toString() + File.separator + "FollowSpeed.html");
        String data = null;
        try {
            data = getStringFromInputStream(LocalResourceManager.getLocalResourceStream(getApplicationContext(), getHtmlPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (data != null) {
            this.mWebView.loadData(data, "text/html;charset=UTF-8", "utf-8");
        } else {
            this.mWebView.loadData("", "text/html;charset=UTF-8", "utf-8");
        }
        this.mWebView.setWebViewClient(new WebClient());
    }

    private String getHtmlPath() {
        String path;
        String path2;
        if (BleConstant.SHUTTER.equals(this.type)) {
            if (Util.isZh(this)) {
                if (Util.isZhFanTi(this)) {
                    path2 = "DeadZoneFang.html";
                } else {
                    path2 = "DeadZone.html";
                }
            } else if (Util.isCs(this)) {
                path2 = "DeadZoneCs.html";
            } else if (Util.isDa(this)) {
                path2 = "DeadZoneDa.html";
            } else if (Util.isDe(this)) {
                path2 = "DeadZoneDe.html";
            } else if (Util.isFi(this)) {
                path2 = "DeadZoneFi.html";
            } else if (Util.isFr(this)) {
                path2 = "DeadZoneFr.html";
            } else if (Util.isHan(this)) {
                path2 = "DeadZoneHan.html";
            } else if (Util.isNo(this)) {
                path2 = "DeadZoneNo.html";
            } else if (Util.isSv(this)) {
                path2 = "DeadZoneSv.html";
            } else {
                path2 = "DeadZoneEng.html";
            }
            this.tvCenterTitle.setText(getString(C0853R.string.dead_band));
        } else {
            if (Util.isZh(this)) {
                if (Util.isZhFanTi(this)) {
                    path = "FollowSpeedFang.html";
                } else {
                    path = "FollowSpeed.html";
                }
            } else if (Util.isCs(this)) {
                path = "FollowSpeedCs.html";
            } else if (Util.isDa(this)) {
                path = "FollowSpeedDa.html";
            } else if (Util.isDe(this)) {
                path = "FollowSpeedDe.html";
            } else if (Util.isFi(this)) {
                path = "FollowSpeedFi.html";
            } else if (Util.isFr(this)) {
                path = "FollowSpeedFr.html";
            } else if (Util.isHan(this)) {
                path = "FollowSpeedHan.html";
            } else if (Util.isNo(this)) {
                path = "FollowSpeedNo.html";
            } else if (Util.isSv(this)) {
                path = "FollowSpeedSv.html";
            } else {
                path = "FollowSpeedEng.html";
            }
            this.tvCenterTitle.setText(getString(C0853R.string.follow_speed));
        }
        return path2;
    }

    private String getStringFromInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while (true) {
            int len = is.read(buffer);
            if (len == -1) {
                break;
            }
            baos.write(buffer, 0, len);
        }
        is.close();
        String html = baos.toString();
        String charset = "utf-8";
        if (html.contains("gbk") || html.contains("gb2312") || html.contains("GBK") || html.contains("GB2312")) {
            charset = "gbk";
        }
        String html2 = new String(baos.toByteArray(), charset);
        baos.close();
        return html2;
    }

    private void initTitle() {
        this.imgBack.setVisibility(0);
        this.imgBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FVWebActivity.this.finish();
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
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
