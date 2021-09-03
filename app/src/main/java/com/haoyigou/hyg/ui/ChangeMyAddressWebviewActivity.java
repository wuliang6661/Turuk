package com.haoyigou.hyg.ui;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;

import com.haoyigou.hyg.R;
import com.haoyigou.hyg.application.GlobalApplication;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.base.BaseActivity;
import com.haoyigou.hyg.utils.SharedPreferencesUtils;

import java.lang.ref.WeakReference;

/**
 * 改变地址网页链接
 */
public class ChangeMyAddressWebviewActivity extends BaseActivity {
    private JocellWebView webView;
    private String web_url;//网页url;
    private GlobalApplication mApplication = null;
    /**
     * 当前Activity的弱引用，防止内存泄露
     **/
    private WeakReference<Activity> context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_set_address);
        mApplication = GlobalApplication.getInstance();
        //将当前Activity压入栈
        context = new WeakReference<Activity>(this);
        mApplication.pushTask(context);
        initview();
    }

    private void initview() {
        webView = (JocellWebView) findViewById(R.id.myaddress_webview);
        web_url = HttpClient.HTTP_DOMAIN + HttpClient.MYADDRESS + "?distributorId=" + SharedPreferencesUtils.getInstance().getString("distributorId", null) + "&accessType=1";
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 设置可以访问文件
        //  webSettings.setAllowFileAccess(true);
        // 设置可以支持缩放
        webSettings.setSupportZoom(true);
        // 设置默认缩放方式尺寸是far
        // webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        // 设置出现缩放工具
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDefaultFontSize(16);
        //webView.addJavascriptInterface(new mobileJsMethod(),"mobilejs");
        webView.load(web_url);
    }
}
