package com.treasure.traveldiary.activity.diary_center;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.utils.LogUtil;
import com.treasure.traveldiary.utils.Tools;

import java.net.URLDecoder;

public class GameH5Activity extends BaseActivity implements View.OnClickListener {

    private WebView webView;
    private FrameLayout loading;
    private String assertPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_h5);
        initTitle();
        Tools.setTranslucentStatus(this);
        title.setText("小游戏");
        btn_back.setVisibility(View.VISIBLE);

        initFindId();
        getIntentReceiver();
        initClick();
        initWebView();
    }

    private void getIntentReceiver() {
        Intent intent = getIntent();
        if (!Tools.isNull(intent.getStringExtra("game_type"))) {
            if (intent.getStringExtra("game_type").equals("红还是绿")) {
                assertPath = "file:///android_asset/honghaishilv/index.html";
                title.setText("红还是绿");
            }
            if (intent.getStringExtra("game_type").equals("猴子接桃")) {
                assertPath = "file:///android_asset/monkey/index.html";
                title.setText("猴子接桃");
            }
        }
    }

    private void initFindId() {
        webView = (WebView) findViewById(R.id.game_h5_webView);
        loading = (FrameLayout) findViewById(R.id.loading_layout);
    }

    private void initClick() {
        btn_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                GameH5Activity.this.finish();
                break;
        }
    }

    private void initWebView() {
        //性能设置开启
        // 在条件满足时开启硬件加速
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.setAllowUniversalAccessFromFileURLs(true);
        }
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // 设置可以使用localStorage
        settings.setDomStorageEnabled(true);
        // 应用可以有数据库
        settings.setDatabaseEnabled(true);
        String databasePath = this.getApplicationContext()
                .getDir("database", Context.MODE_PRIVATE).getPath();
        settings.setDatabasePath(databasePath);
        // 应用可以有缓存
        settings.setAppCacheEnabled(true);
        String appCaceDir = this.getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
        settings.setAppCachePath(appCaceDir);

        webView.setWebChromeClient(new WebChromeClient());
        // Android中点击一个链接，默认是调用应用程序来启动，因此WebView需要代为处理这个动作 通过WebViewClient
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtil.e("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~url:"+url);
                if(url.contains("closegame")){
                        String aaa = url.split("result")[1].replace("=","");
                        try {
                            String result = URLDecoder.decode(aaa,"UTF-8");
                            new AlertDialog.Builder(GameH5Activity.this)
                                    .setTitle("提示")
                                    .setMessage(result)
                                    .setCancelable(false)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            GameH5Activity.this.finish();
                                        }
                                    })
                                    .show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }else {
                    view.loadUrl(url);
                }
                return true;
            }
        });
        webView.loadUrl(assertPath);

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
        webView.clearCache(true);
        webView.clearHistory();
        webView.clearFormData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }
}
