package com.haoyigou.hyg.ui.personweb;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haoyigou.hyg.R;
import com.haoyigou.hyg.base.BaseActivity;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.ui.JocellWebView;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wuliang on 2016/11/23.
 * 个人中心的所有H5页面由这里加载
 */

public class PersonWebViewAct extends BaseActivity {

    @BindView(R.id.headar_layout)
    LinearLayout headarLayout;
    @BindView(R.id.title_text)
    TextView title_text;
    @BindView(R.id.viewContent)
    View viewContent;
    private JocellWebView jocellWebView;
    private String web_url;//网页url;
    /**
     * 当前Activity的弱引用，防止内存泄露
     **/
    private WeakReference<Activity> context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_good);
        ButterKnife.bind(this);
        //将当前Activity压入栈
        context = new WeakReference<Activity>(this);
        mApplication.pushTask(context);
        initview();
        if (getIntent().getBooleanExtra("isTitle", false)) {
            headarLayout.setVisibility(View.VISIBLE);
            viewContent.setVisibility(View.GONE);
            goBack();
        }
        if (getIntent().getStringExtra("title") != null){
            headarLayout.setVisibility(View.VISIBLE);
            viewContent.setVisibility(View.GONE);
            title_text.setText(getIntent().getStringExtra("title"));
            goBack();
        }
    }

    private void initview() {
        viewContent.setVisibility(View.VISIBLE);
        jocellWebView = (JocellWebView) findViewById(R.id.mygood_webview);
        String url = getIntent().getStringExtra("url");
        if (getIntent().getStringExtra("completeUrl") != null && getIntent().getStringExtra("completeUrl").equals("yes")){
            web_url = url;
        }else {
            if (url.indexOf("?") == -1) {
                web_url = HttpClient.HTTP_DOMAIN + url + "?fromapp=1&appversion=1";
            } else {
                web_url = HttpClient.HTTP_DOMAIN + url + "&fromapp=1&appversion=1";
            }
        }
        WebSettings webSettings = jocellWebView.getSettings();
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
        //webSettings.setDefaultFontSize(16);
        //webView.addJavascriptInterface(new mobileJsMethod(),"mobilejs");
        jocellWebView.load(web_url);
    }

    /**
     * 设置表单数据值
     */
    public void setFileMessage(ValueCallback<Uri[]> filePathCallback) {
        this.mUploadCallbackAboveL = filePathCallback;
    }

    public void setFileMessag(ValueCallback<Uri> filePathCallback) {
        this.mUploadMessage = filePathCallback;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }


    private final static int FILECHOOSER_RESULTCODE = 1;// 表单的结果回调
    private Uri imageUri;
    private ValueCallback<Uri> mUploadMessage;// 表单的数据信息
    private ValueCallback<Uri[]> mUploadCallbackAboveL;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage && null == mUploadCallbackAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (mUploadCallbackAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (mUploadMessage != null) {
                Log.e("result", result + "");
                if (result == null) {
                    mUploadMessage.onReceiveValue(imageUri);
                    mUploadMessage = null;
                    Log.e("imageUri", imageUri + "");
                } else {
                    mUploadMessage.onReceiveValue(result);
                    mUploadMessage = null;
                }
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        if (requestCode != FILECHOOSER_RESULTCODE || mUploadCallbackAboveL == null) {
            return;
        }
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                results = new Uri[]{imageUri};
                return;
            } else {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        if (results != null) {
            mUploadCallbackAboveL.onReceiveValue(results);
            mUploadCallbackAboveL = null;
        } else {
            results = new Uri[]{imageUri};
            mUploadCallbackAboveL.onReceiveValue(results);
            mUploadCallbackAboveL = null;
        }
        return;
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
