package com.haoyigou.hyg.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.haoyigou.hyg.R;
import com.haoyigou.hyg.base.BaseFragment;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.ui.JocellWebView;
import com.haoyigou.hyg.ui.OrderAllAct;
import com.haoyigou.hyg.ui.homeweb.HomeWebViewAct;
import com.haoyigou.hyg.ui.personweb.PersonWebViewAct;
import com.haoyigou.hyg.view.widget.LoadingProgressDialog;
import com.luck.picture.lib.tools.ScreenUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by witness on 2018/9/16.
 * 拼团
 */

public class GroupFragment extends BaseFragment {


    @BindView(R.id.mygood_webview)
    JocellWebView mygood_webview;
    @BindView(R.id.headLayout)
    LinearLayout headLayout;
    String url;
    static LoadingProgressDialog proDialog = null;

    public static GroupFragment newInstance(String url) {
        GroupFragment informationFragment = new GroupFragment();
        Bundle b = new Bundle();
        b.putString("url",url);
        informationFragment.setArguments(b);
        return informationFragment;
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            url = args.getString("url");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.group_fragment_layout, null,false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView(){
        setFlgsbg(R.color.mainBlue);
        setStatusBar();
        headLayout.post(() -> headLayout.setPadding(0, (int) ScreenUtils.getStatusBarHeight(getActivity()), 0, 0));
        WebSettings webSettings = mygood_webview.getSettings();
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
        mygood_webview.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                startProgressDialog("加载中", getActivity());
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                try {
                    view.loadUrl("javascript: setAccessType('app'); ");
                    view.loadUrl("javascript:(function() { " +
                            "var videos = document.getElementsByTagName('audio');" +
                            " for(var i=0;i<videos.length;i++){videos[i].play();}})()");
                    stopProgressDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);//当打开新的连接时,使用当前的webview,不使用系统其他浏览器
                return true;
            }
        });
        //webSettings.setDefaultFontSize(16);
        //webView.addJavascriptInterface(new mobileJsMethod(),"mobilejs");
        mygood_webview.load(url);
    }

    public void setFlgsbg(int color) {
        // 修改状态栏颜色，4.4+生效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(color);//通知栏所需颜色
        }
    }

    /**
     * 浸入式状态栏实现同时取消5.0以上的阴影
     */
    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = getActivity().getWindow().getDecorView();
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getActivity().getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        //修改字体颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//android6.0以后可以对状态栏文字颜色和图标进行修改
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

        boolean isOnPause;
    /**
     * 当Activity执行onPause()时让WebView执行pause
     */
    @Override
    public void onPause() {
        super.onPause();
        proDialog = null;
        stopProgressDialog();
        try {
            if (mygood_webview != null) {
                mygood_webview.getClass().getMethod("onPause").invoke(mygood_webview, (Object[]) null);
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
    public void onResume() {
        super.onResume();
        try {
            if (isOnPause) {
                if (mygood_webview != null) {
                    mygood_webview.getClass().getMethod("onResume").invoke(mygood_webview, (Object[]) null);
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
    public void onDestroy() {
        super.onDestroy();
        proDialog = null;
        stopProgressDialog();
        if (mygood_webview != null) {
            mygood_webview.getSettings().setBuiltInZoomControls(true);
            mygood_webview.setVisibility(View.GONE);
            long delayTime = ViewConfiguration.getZoomControlsTimeout();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mygood_webview.destroy();
                                mygood_webview = null;
                            }
                        });
                    }
                }
            }, delayTime);
        }
        isOnPause = false;
    }

}
