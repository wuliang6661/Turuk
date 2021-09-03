package com.haoyigou.hyg.ui.homeweb;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.haoyigou.hyg.R;
import com.haoyigou.hyg.base.BaseActivity;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.ui.JocellWebView;
import com.haoyigou.hyg.utils.MessageEvent;
import com.haoyigou.hyg.view.widget.LoadingProgressDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wuliang on 2016/11/23.
 * <p>
 * 从主页进入的H5页面
 */

public class HomeWebViewAct extends BaseActivity {

    @BindView(R.id.headar_layout)
    LinearLayout headarLayout;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.viewContent)
    View viewContent;
    @BindView(R.id.back)
    LinearLayout back;
    private JocellWebView webView;
    private String web_url;//网页url;
    static LoadingProgressDialog proDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_good);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        //将当前Activity压入栈
//        if (getIntent().getBooleanExtra("isTitle", false)) {
//            headarLayout.setVisibility(View.VISIBLE);
//            goBack();
//        }
        initview();
    }

    private void initview() {
        webView = (JocellWebView) findViewById(R.id.mygood_webview);
        String url = getIntent().getStringExtra("url");
        boolean isallAddress = getIntent().getBooleanExtra("all", false);
        if (isallAddress) {
            web_url = url;
        } else {
            web_url = HttpClient.HTTP_DOMAIN + url + "&fromapp=1";
        }
        if (web_url.contains("/exchange/index")) {
            headarLayout.setVisibility(View.VISIBLE);
            viewContent.setVisibility(View.GONE);
        } else {
            headarLayout.setVisibility(View.GONE);
            viewContent.setVisibility(View.VISIBLE);
        }
//        Log.e("log--", web_url);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 设置可以访问文件
        //  webSettings.setAllowFileAccess(true);
        // 设置可以支持缩放
        webSettings.setSupportZoom(true);
        webSettings.setTextZoom(100);
        // 设置默认缩放方式尺寸是far
        // webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        // 设置出现缩放工具
        webSettings.setBuiltInZoomControls(true);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                startProgressDialog("加载中", HomeWebViewAct.this);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                try {
                    view.loadUrl("javascript: setAccessType('app'); ");
                    view.loadUrl("javascript:(function() { " +
                            "var videos = document.getElementsByTagName('audio');" +
                            " for(var i=0;i<videos.length;i++){videos[i].play();}})()");
                    stopProgressDialog();
                    titleText.setText(view.getTitle());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("mailto:") || url.startsWith("geo:") || url.startsWith("tel:") || url.startsWith("smsto:")
                        || url.startsWith("taobao://") //淘宝
                        || url.startsWith("weixin://") //微信
                        || url.startsWith("alipays://") //支付宝
                        || url.startsWith("dianping://")//大众点评
                        || url.startsWith("tbopen://")//淘宝
                        || url.startsWith("openapp.jdmobile://")//淘宝
                        || url.startsWith("tmast://"))//淘宝
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    stopProgressDialog();
                    return true;
                }
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });
        //webSettings.setDefaultFontSize(16);
        //webView.addJavascriptInterface(new mobileJsMethod(),"mobilejs");
        webView.load(web_url);

    }

    boolean isOnPause;

    /**
     * 当Activity执行onPause()时让WebView执行pause
     */
    @Override
    protected void onPause() {
        super.onPause();
        proDialog = null;
        stopProgressDialog();
        try {
            if (webView != null) {
                webView.getClass().getMethod("onPause").invoke(webView, (Object[]) null);
                isOnPause = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 当Activity执行onResume()时让WebView执行resume
     */
    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (isOnPause) {
                if (webView != null) {
                    webView.getClass().getMethod("onResume").invoke(webView, (Object[]) null);
                }
                isOnPause = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 该处的处理尤为重要:
     * 应该在内置缩放控件消失以后,再执行mWebView.destroy()
     * 否则报错WindowLeaked
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        proDialog = null;
        stopProgressDialog();
        if (webView != null) {
            webView.getSettings().setBuiltInZoomControls(true);
            webView.setVisibility(View.GONE);
            long delayTime = ViewConfiguration.getZoomControlsTimeout();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    HomeWebViewAct.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            webView.destroy();
                            webView = null;
                        }
                    });
                }
            }, delayTime);
        }
        isOnPause = false;
        EventBus.getDefault().unregister(this);
    }

    public static void startProgressDialog(String progressMsg, Context mcontext) {
        stopProgressDialog();
        if (proDialog == null) {
            proDialog = LoadingProgressDialog.createDialog(mcontext);
            proDialog.setMessage(progressMsg);
        }
        proDialog.show();
        proDialog.setCanceledOnTouchOutside(false);
    }

    public static void stopProgressDialog() {
        if (proDialog != null && proDialog.isShowing()) {
            proDialog.dismiss();
            proDialog = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            String back = getIntent().getExtras().getString("back");
            if (back == null) {
                webView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent messageEvent) {
        if (messageEvent.getMessageType().equals("HomeWebViewAct")) {
            finish();
        }
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        if (webView.canGoBack()){
            webView.goBack();
        }else {
            finish();
        }
    }

}
