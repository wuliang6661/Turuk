package com.haoyigou.hyg.view.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.ui.InvoiceActivity;
import com.haoyigou.hyg.ui.WebviewActivity;
import com.haoyigou.hyg.ui.personweb.PersonWebViewAct;
import com.haoyigou.hyg.utils.SharedPreferencesUtils;

public class JocellWebViewClient extends WebViewClient {
    static LoadingProgressDialog proDialog = null;
    Activity activity;
    boolean load_flag;
    private Context context;

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
//		view.loadUrl(url);
//		return false;
//		SimpleDateFormat sf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:sss");
        Log.e("log--", "shouldOverrideUrlLoading");
        if (url.startsWith("mailto:") || url.startsWith("geo:") || url.startsWith("tel:") || url.startsWith("smsto:")
                || url.startsWith("taobao:") //淘宝
                || url.startsWith("weixin://") //微信
                || url.startsWith("alipays://") //支付宝
                || url.startsWith("dianping://"))//大众点评
            {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            activity.startActivity(intent);
            return true;
        }
        if (url.contains("data:text/plain")){
            return true;
        }
        if (url.startsWith("http://www.fapiao.com/dzfp-web/pdf/download?")){
            new InvoiceActivity(activity,url).viewPdf();
            return true;
        }
        view.loadUrl(url);
        return true;
        //return false;
        //Log.d("System.out", " shouldOverrideUrlLoading url="+url);
//		Utils.writeLog( sf.format(new Date())+" shouldOverrideUrlLoading url="+url, "WebViewLogs.txt");
        //return super.shouldOverrideUrlLoading(view, url);
    }

    public JocellWebViewClient(Activity _activity) {
        activity = _activity;
    }

//	public BapWebViewClient(){
//		
//	}

    @Override
    public void onPageFinished(WebView view, String url) {
        try {
            view.loadUrl("javascript: setAccessType('app'); ");
            view.loadUrl("javascript:(function() { " +
                    "var videos = document.getElementsByTagName('audio');" +
                    " for(var i=0;i<videos.length;i++){videos[i].play();}})()");
//            stopProgressDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onPageFinished(view, url);

    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
//        startProgressDialog("加载中", activity);
        super.onPageStarted(view, url, favicon);
    }


    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {

        return super.shouldInterceptRequest(view, url);
    }

    @Override
    public void onTooManyRedirects(WebView view, Message cancelMsg,
                                   Message continueMsg) {

        super.onTooManyRedirects(view, cancelMsg, continueMsg);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode,
                                String description, String failingUrl) {
//        stopProgressDialog();
        super.onReceivedError(view, errorCode, description, failingUrl);
    }

    @Override
    public void onFormResubmission(WebView view, Message dontResend,
                                   Message resend) {

        super.onFormResubmission(view, dontResend, resend);
    }

    @Override
    public void doUpdateVisitedHistory(WebView view, String url,
                                       boolean isReload) {

        super.doUpdateVisitedHistory(view, url, isReload);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                   SslError error) {
//        stopProgressDialog();
        super.onReceivedSslError(view, handler, error);
    }

    @Override
    public void onReceivedHttpAuthRequest(WebView view,
                                          HttpAuthHandler handler, String host, String realm) {

        super.onReceivedHttpAuthRequest(view, handler, host, realm);
    }

    @Override
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {

        return super.shouldOverrideKeyEvent(view, event);
    }

    @Override
    public void onUnhandledKeyEvent(WebView view, KeyEvent event) {

        super.onUnhandledKeyEvent(view, event);
    }

    @Override
    public void onScaleChanged(WebView view, float oldScale, float newScale) {
        super.onScaleChanged(view, oldScale, newScale);
    }

    @Override
    public void onReceivedLoginRequest(WebView view, String realm,
                                       String account, String args) {
//		Log.i("System.out", "onReceivedLoginRequest");
//		SimpleDateFormat sf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:sss");
//		Utils.writeLog( sf.format(new Date())+" onReceivedLoginRequest", "WebViewLogs.txt");
        super.onReceivedLoginRequest(view, realm, account, args);
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

}
