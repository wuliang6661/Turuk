package com.haoyigou.hyg.ui;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haoyigou.hyg.R;
import com.haoyigou.hyg.base.BaseActivity;
import com.haoyigou.hyg.common.http.HttpClient;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 活动页，h5
 */

public class WebActivity extends BaseActivity {

    JocellWebView jocellWebView;
    String url;
    @BindView(R.id.back)
    LinearLayout back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.headar_layout)
    LinearLayout headar_layout;

    private ValueCallback<Uri> mUploadMessage;

    private String orderNum;//订单编号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_h5);
        ButterKnife.bind(this);
        invotion();
        Intent intent = getIntent();
        orderNum = intent.getStringExtra("orderNum");
        if (orderNum == null) {
            if (getIntent().getStringExtra("exchangeUrl") != null) {
                url = getIntent().getStringExtra("exchangeUrl");
            } else {
                url = HttpClient.HTTP_DOMAIN + "/order/myAllGroupData?source=1";
            }
        } else {
            url = HttpClient.HTTP_DOMAIN + "/transitSet/payTransitIndex?ordernum=" + orderNum + "&fromapp=1";
        }
//        Log.e("URL", url);

        if (url.contains("/exchange/record/index")){
            headar_layout.setVisibility(View.VISIBLE);
        }else {
            headar_layout.setVisibility(View.GONE);
        }
        jocellWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                titleText.setText(view.getTitle());
            }
        });
        jocellWebView.load(url);
        jocellWebView.setmUploadMessage(mUploadMessage);
    }

    /**
     * 初始化布局
     */
    private void invotion() {
        setsStatusBar();
        jocellWebView = (JocellWebView) findViewById(R.id.jocellWebView);
    }

    /**
     * 浸入式状态栏实现同时取消5.0以上的阴影
     */
    protected void setsStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = getWindow().getDecorView();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        //修改字体颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//android6.0以后可以对状态栏文字颜色和图标进行修改
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    boolean isOnPause;

    /**
     * 当Activity执行onPause()时让WebView执行pause
     */
    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (jocellWebView != null) {
                jocellWebView.getClass().getMethod("onPause").invoke(jocellWebView, (Object[]) null);
                isOnPause = true;
                this.finish();
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
                if (jocellWebView != null) {
                    jocellWebView.getClass().getMethod("onResume").invoke(jocellWebView, (Object[]) null);
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
        if (jocellWebView != null) {
            jocellWebView.getSettings().setBuiltInZoomControls(true);
            jocellWebView.setVisibility(View.GONE);
            long delayTime = ViewConfiguration.getZoomControlsTimeout();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    WebActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            jocellWebView.destroy();
                            jocellWebView = null;
                        }
                    });
                }
            }, delayTime);
        }
        isOnPause = false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && jocellWebView.canGoBack()) {
            String back = getIntent().getExtras().getString("back");
            if (back == null) {
                if (url.contains("/exchange/record/index")){
                    finish();
                }else {
                    jocellWebView.goBack();
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        if (url.contains("/exchange/record/index")){
            finish();
        }
    }
}