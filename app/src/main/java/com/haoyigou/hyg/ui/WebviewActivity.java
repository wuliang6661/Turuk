package com.haoyigou.hyg.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.webkit.ValueCallback;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haoyigou.hyg.R;
import com.haoyigou.hyg.config.StateMessage;
import com.haoyigou.hyg.base.BaseActivity;

import java.util.Timer;
import java.util.TimerTask;


public class WebviewActivity extends BaseActivity {

    JocellWebView jocellWebView;
    String url;
    private LinearLayout backButton;
    private TextView tvHearder;
    private RelativeLayout header_layout;

    private ValueCallback<Uri> mUploadMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.h5);
        invotion();
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        if (intent.getBooleanExtra("is_home", false)) {
            StateMessage.isGDMessahe = true;
            tvHearder.setText("");
            header_layout.setVisibility(View.VISIBLE);
            header_layout.setOnTouchListener(new View.OnTouchListener() {    //将事件分发截止到本层
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToActivity(MainActivity.class, true);
                    jocellWebView.clearCache(true);
                }
            });
        }
        if (url != null) {
//            Log.e("URL", url);
        }

        jocellWebView.load(url);
        jocellWebView.setmUploadMessage(mUploadMessage);
    }

    /**
     * 初始化布局
     */
    private void invotion() {
        jocellWebView = (JocellWebView) findViewById(R.id.jocellWebView);
        header_layout = (RelativeLayout) findViewById(R.id.linear_setting);
        header_layout.setVisibility(View.GONE);
        tvHearder = (TextView) findViewById(R.id.tvHearder);
        backButton = (LinearLayout) findViewById(R.id.setting_backto);
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
                    WebviewActivity.this.runOnUiThread(new Runnable() {
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
            String  back = getIntent().getExtras().getString("back");
            if(back==null){
                jocellWebView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}