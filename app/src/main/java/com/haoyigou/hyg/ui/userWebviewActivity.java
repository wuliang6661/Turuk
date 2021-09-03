package com.haoyigou.hyg.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.LinearLayout;

import com.haoyigou.hyg.R;
import com.haoyigou.hyg.base.BaseActivity;
import com.haoyigou.hyg.common.http.HttpClient;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/23.
 */

public class userWebviewActivity extends BaseActivity {
    private WeakReference<Activity> context = null;
    private JocellWebView userwebview;
    private String web_url;
    @BindView(R.id.back)
    LinearLayout back;
    private String url;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_userweb);
        ButterKnife.bind(this);
        //将当前Activity压入栈
        context = new WeakReference<Activity>(this);
        mApplication.pushTask(context);
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setTitleText(title);
        initview();
    }
    private void initview() {
        userwebview = (JocellWebView) findViewById(R.id.user_webview);
        web_url = HttpClient.HTTP_DOMAIN + url + "?fromapp=1";
        WebSettings webSettings = userwebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 设置可以访问文件
        //  webSettings.setAllowFileAccess(true);
        // 设置可以支持缩放
        webSettings.setSupportZoom(true);
        webSettings.setTextZoom(100);
        webSettings.setTextSize(WebSettings.TextSize.LARGEST);

        // 设置默认缩放方式尺寸是far
        // webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        // 设置出现缩放工具
        webSettings.setBuiltInZoomControls(false);
//        webSettings.setDefaultFontSize(36);
        //webView.addJavascriptInterface(new mobileJsMethod(),"mobilejs");
//        Log.d("bugggggggggggggg", "initview: "+web_url);
        userwebview.load(web_url);
    }
}
