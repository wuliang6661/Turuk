package com.haoyigou.hyg.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.chinaums.pppay.unify.UnifyPayPlugin;
import com.chinaums.pppay.unify.UnifyPayRequest;
import com.haoyigou.hyg.application.GlobalApplication;
import com.haoyigou.hyg.application.MApplication;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.common.http.PersistentCookieStore;
import com.haoyigou.hyg.config.StateMessage;
import com.haoyigou.hyg.entity.Constants;
import com.haoyigou.hyg.entity.StoreInfoEntity;
import com.haoyigou.hyg.entity.WXPayBO;
import com.haoyigou.hyg.ui.personweb.PersonWebViewAct;
import com.haoyigou.hyg.utils.AppManager;
import com.haoyigou.hyg.utils.DownLoadImageUtils;
import com.haoyigou.hyg.utils.LogUtils;
import com.haoyigou.hyg.utils.PayResult;
import com.haoyigou.hyg.utils.SharedPreferencesUtils;
import com.haoyigou.hyg.utils.StringUtils;
import com.haoyigou.hyg.view.widget.JocellChromeClient;
import com.haoyigou.hyg.view.widget.JocellWebViewClient;
import com.haoyigou.hyg.view.widget.SharePopupWindow;
import com.haoyigou.hyg.view.widget.ShareWxPupWindow;
import com.haoyigou.hyg.wxapi.ImagePagerView;
import com.haoyigou.hyg.wxapi.WXUtils;
import com.haoyigou.hyg.wxapi.WxPayTask;
import com.meiqia.meiqiasdk.util.MQIntentBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 这个是施总自定义的一个webview 支付分享 安卓JS交互的方法在里面
 */

public class JocellWebView extends WebView {

    Context context;
    SharedPreferences sharedPreferences;
    SharePopupWindow popWindow = null;
    ShareWxPupWindow wxPupWindow = null;
    String shareUrl;
    String shareTitle;
    String shareLogo;
    String shareContent;
    private ValueCallback<Uri> mUploadMessage;

    private String loadUrl;


    public JocellWebView(Context context) {
        super(context);
        setting(context);
    }

    public JocellWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setting(context);
    }

    public JocellWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setting(context);
    }


    public void setting(Context context) {
        this.context = context;
        getSettings().setJavaScriptEnabled(true);
        getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        getSettings().setSupportZoom(true);
        getSettings().setBuiltInZoomControls(true);
        getSettings().setUseWideViewPort(true);
        getSettings().setLoadWithOverviewMode(true);
        getSettings().setTextZoom(100);
//		webView.getSettings().setSupportMultipleWindows(true);
        setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        getSettings().setDomStorageEnabled(true);
        getSettings().setAppCacheMaxSize(1024 * 1024 * 2);
        String appCachePath = context.getApplicationContext().getCacheDir().getAbsolutePath();
        getSettings().setAppCachePath(appCachePath);
        getSettings().setAllowFileAccess(true);
        getSettings().setAppCacheEnabled(true);
        //webview在安卓5.0之前默认允许其加载混合网络协议内容
        // 在安卓5.0之后，默认不允许加载http与https混合内容，需要设置webview允许其加载混合网络协议内容
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        this.setWebChromeClient(new JocellChromeClient(mUploadMessage, context, (Activity) context));
        this.setWebViewClient(new JocellWebViewClient((Activity) context));//
        addJavascriptInterface(new mobileJsMethod(), "mobilejs");//
        sharedPreferences = context.getSharedPreferences("haoyigou", Context.MODE_PRIVATE);
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
                100,
                100);
        mLayoutParams.gravity = Gravity.CENTER;
        this.setLayoutParams(mLayoutParams);
        popWindow = new SharePopupWindow((Activity) context);
        wxPupWindow = new ShareWxPupWindow((Activity) context);
    }

    public void load(String url) {
        CookieSyncManager syncManager = CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeAllCookie();
        syncManager.startSync();
        String host = HttpClient.HTTP_DOMAIN;
        cookieManager.setCookie(host, "JSESSIONID=" + SharedPreferencesUtils.getInstance().getString("jsessionid", null));
        syncManager.sync();
        Map<String, String> extraHeaders = new HashMap();
        if (StateMessage.IS_LOGIN)
            extraHeaders.put("access-type", "android");
        OkHttpClient client = new OkHttpClient();
        client.setCookieHandler(new java.net.CookieManager(new PersistentCookieStore(context.getApplicationContext()), CookiePolicy.ACCEPT_ALL));
        if (SharedPreferencesUtils.getInstance().getString("jsessionid", null) != null && SharedPreferencesUtils.getInstance().getString("jsessionid", null).length() > 0) {
            extraHeaders.put("Set-Cookie", "JSESSIONID=" + SharedPreferencesUtils.getInstance().getString("jsessionid", null));
        }
        Constants.jocellWebView = this;
        int index = url.indexOf("?");
        if (index == -1) {
            url += "?afterUpdate=1";
        } else {
//            if (!url.contains("m.best1.com/distributor/productTab")){
                url += "&afterUpdate=1";
//            }
        }
//        Log.e("url", url);
        this.loadUrl = url.replace(" ", "");
//        if (!this.loadUrl.startsWith("https")) {
//            this.loadUrl = this.loadUrl.replace("http", "https");
//        }
        this.loadUrl(loadUrl, extraHeaders);
    }

    /* *
     * @author shichenwei
     *
     */
    class mobileJsMethod {
        @JavascriptInterface
        public void close() throws IllegalArgumentException, IllegalStateException, IOException {
            handler.sendEmptyMessage(1);
        }

        @JavascriptInterface
        public void appLogin() {
            handler.sendEmptyMessage(2);
        }

        /**
         * @param url 取消订单
         */
        @JavascriptInterface
        public void cancelorder(String url) {
//            Log.i("log--", url);
            Message message = new Message();
            Bundle data = new Bundle();
            data.putString("url", url);
            message.setData(data);
            message.what = 4;
            handler.sendMessage(message);
        }

        @JavascriptInterface
        public void newwindow(String url) {
            Message message = new Message();
            Bundle data = new Bundle();
            data.putString("url", url);
            message.setData(data);
            message.what = 7;
            handler.sendMessage(message);
        }

        @JavascriptInterface
        public void backcarthome(String url) {
            Message message = new Message();
            Bundle data = new Bundle();
            data.putString("url", url);
            message.setData(data);
            message.what = 11;
            handler.sendMessage(message);
        }

        @JavascriptInterface
        public void hiddenBack() {
            handler.sendEmptyMessage(5);
        }

        @JavascriptInterface
        public void appBusinessLoad() {
            handler.sendEmptyMessage(6);
        }

        @JavascriptInterface
        public void hideLoading() {
//            handler.sendEmptyMessage(8);
        }

        @JavascriptInterface
        public void home2() {
            handler.sendEmptyMessage(23);
        }

        @JavascriptInterface
        public void setShare(String title, String content, String logo, String url) {
            shareTitle = title;
            shareContent = content;
            shareLogo = logo;
            shareUrl = url;
            popWindow.setShareMessage(shareTitle, shareLogo, shareContent, shareUrl);
            handler.sendEmptyMessage(2);
        }

        /***
         * 带图文分享的分享调用方法
         */
        @JavascriptInterface
        public void setShare2(String title, String content, String logo, String url, String memo, String pics) {
//            Log.e("log--Title", title);
//            Log.e("log--content", content);
//            Log.e("log--logo", logo);
//            Log.e("log--url", url);
//            Log.e("log--memo", memo);
//            Log.e("log--pics", pics);
            popWindow.setImageAndTextShare(title, logo, content, url, memo, pics);
            handler.sendEmptyMessage(2);
        }


        /**
         * 只分享到微信、朋友圈的方法
         */
        @JavascriptInterface
        public void setShare3(String title, String content, String logo, String url) {
            shareTitle = title;
            shareContent = content;
            shareLogo = logo;
            shareUrl = url;
            wxPupWindow.setShareMessage(shareTitle, shareLogo, shareContent, shareUrl);
            handlerNew.sendEmptyMessage(0x22);
        }


        /***
         * 客服页面调用聊天页面回调
         */
        @JavascriptInterface
        public void mqkf(String logo, String str) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
            final HashMap<String, String> clientInfo = new HashMap<>();
            final String shopMessage;
            if (str.equals("undefined")) {
                Intent intent2 = new MQIntentBuilder(context)
                        .build();
                context.startActivity(intent2);
                return;
            }
//            Log.e("log--", "呼叫客服" + str);
            if (str.indexOf("&*") != -1) {
                String[] users = str.split("\\**");
                clientInfo.put("name", users[1]);
                clientInfo.put("tel", users[2]);
                clientInfo.put("comment", users[3]);
                shopMessage = users[0];
            } else {
                shopMessage = str;
                if (StringUtils.isEmpty(logo)) {
                    Intent intent2 = new MQIntentBuilder(context)
                            .setPreSendTextMessage(shopMessage)
                            .setClientInfo(clientInfo).build();
                    context.startActivity(intent2);
                    return;
                }
            }
            new DownLoadImageUtils().startDownLoading(logo, new DownLoadImageUtils.onDownLodingSource() {
                @Override
                public void onSource(String filePath) {
                    Intent intent2 = new MQIntentBuilder(context)
                            .setPreSendTextMessage(shopMessage)
                            .setPreSendImageMessage(new File(filePath))
                            .setClientInfo(clientInfo).build();
                    context.startActivity(intent2);
                }
            });
        }

        /**
         * 跳入登陆页面
         */
        @JavascriptInterface
        public void toLoginIndex() {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
        }


        @JavascriptInterface
        public void aliPay(String itemdata) {   //这里为直接支付
//            Log.i("log--", itemdata + "什么");
            Map<String, Object> params = new HashMap<>();
            params.put("strdata", itemdata);
            params.put("paytype", "2");
            params.put("source", "1");
            getAlipayOrder(HttpClient.ALIPAYADRESS, params);
        }

        @JavascriptInterface
        public void aliPay2() {
//            Log.e("log--", "aliPay2");
            Map<String, Object> params = new HashMap<>();
            getAlipayOrder(HttpClient.AlipayRuiduo, params);
        }

        @JavascriptInterface
        public void aliPay3(String ordernum) {    //这里为订单支付
//            Log.i("log--", ordernum + "时候");
            Map<String, Object> params = new HashMap<>();
            params.put("ordernum", ordernum);
            getAlipayOrder(HttpClient.AlipayOrder, params);
        }



        @JavascriptInterface
        public void wxPay(String itemdata) {
            Message msg = new Message();
            msg.what = 5;
            msg.obj = itemdata;
            handler.sendMessage(msg);
//            Log.i("log--", itemdata + "什么");
        }

        @JavascriptInterface
        public void wxPay2(String ordernum) {
            Message msg = new Message();
            msg.what = 12;
            msg.obj = ordernum;
            handler.sendMessage(msg);
        }

        @JavascriptInterface
        public void wxPay3(String ordernum) {
            Message msg = new Message();
            msg.what = 13;
            msg.obj = ordernum;
            handler.sendMessage(msg);
        }

        /**
         * 统一支付调用
         */
        @JavascriptInterface
        public void payWx(String ordernum, String payparam, String url) {
            StateMessage.isPinTuan = true;
            StateMessage.PinTuanUrl = url;
            StateMessage.pinOrderNum = ordernum;
            WXPayBO payBO = JSONObject.parseObject(payparam, WXPayBO.class);
            WXUtils.payWX(payBO);
        }

        @JavascriptInterface
        public void payAli(final String ordernum, final String payparam, final String url) {
            StateMessage.isPinTuan = true;
            StateMessage.PinTuanUrl = url;
            StateMessage.pinOrderNum = ordernum;
            Runnable payRunnable = new Runnable() {

                @Override
                public void run() {
                    PayTask alipay = new PayTask((Activity) context);
                    Map<String, String> result = alipay.payV2(payparam, true);

                    Message msg = new Message();
                    msg.what = 0x11;
                    msg.obj = ordernum;
                    handlerNew.sendMessage(msg);
                }
            };
            // 必须异步调用
            Thread payThread = new Thread(payRunnable);
            payThread.start();
        }


        @JavascriptInterface
        public void enchangePay(String itemdata) {//兑换商城支付  mode:3微信  4支付宝
            String regex = "[#:;|\\t]";
            String[] strings = itemdata.split(regex);
            List<String> data = new ArrayList<>(Arrays.asList(strings));

            Map<String, Object> params = new HashMap<>();
            params.put("ordernum", data.get(0));
            if (data.size() >= 2) {
                params.put("mode", data.get(1));
            }
            if (data.size() >= 3) {
                params.put("distributorId", data.get(2));
            }
            exchangePay(HttpClient.EXCHANGEPAY, params, data.get(1));
        }



        /***
         * 邀请码点击提交之后回调
         */
        @JavascriptInterface
        public void appYQMCloseWindow() {
            GlobalApplication.user.setInvitation("1000");  //随便给个数字，让邀请码判断消失
            handler.sendEmptyMessage(1);
        }


        /**
         * 任务中心的阅读链接 参数是合伙人ID
         *
         * @param url
         */
        @JavascriptInterface
        public void newwindow2(String url) {
            Message message = new Message();
            Bundle data = new Bundle();
            data.putString("url", url);
            message.setData(data);
            message.what = 18;
            handler.sendMessage(message);
        }
        @JavascriptInterface
        public void showTurn(String url) {
//            Log.e("log--", "turnProduct!"+url);
            Message message = new Message();
            Bundle data = new Bundle();
            data.putString("url", url);
            message.setData(data);
            message.what = 25;
            handler.sendMessage(message);
        }
        @JavascriptInterface
        public void toCharge() {
            handler.sendEmptyMessage(26);
        }
        /***
         * 跳转至购物车
         */
        @JavascriptInterface
        public void togwc() {
//            handler.sendEmptyMessage(1);
            StateMessage.isShopCarFinnish = true;
            StateMessage.url = loadUrl;
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
            EventBus.getDefault().post(new Boolean(false));
        }
        @JavascriptInterface
        public void home() {
            Log.e("log--", "js_home方法被调用!");
            handler.sendEmptyMessage(20);
        }
        @JavascriptInterface
        public void appCloseWindow() {
            handler.sendEmptyMessage(10);
        }

        @JavascriptInterface
        public void appMain() {
            handler.sendEmptyMessage(14);
        }

        @JavascriptInterface
        public void appMain2() {
            handler.sendEmptyMessage(15);
        }

        @JavascriptInterface
        public void closeMain2() {
            handler.sendEmptyMessage(16);
        }

        @JavascriptInterface
        public void closeMain() {
            handler.sendEmptyMessage(17);
        }




        @JavascriptInterface
        public void close2() {
            handler.sendEmptyMessage(21);
        }


        @JavascriptInterface
        public void fullpic(String current, String images) {
            ImagePagerView im = new ImagePagerView(context);
            im.init(Integer.parseInt(current) - 1, images);
            im.showAtLocation(JocellWebView.this, Gravity.BOTTOM, 0, 0);
        }

        @JavascriptInterface
        public void flcx(String url) {
            Message message = new Message();
            Bundle data = new Bundle();
            data.putString("url", url);
            message.setData(data);
            message.what = 19;
            handler.sendMessage(message);
        }

        @JavascriptInterface
        public void toOrderDetail(String ordernum) {
            Message message = new Message();
            message.what = 27;
            message.obj = ordernum;
            handler.sendMessage(message);
        }

        @JavascriptInterface
        public void groupTurn(String url) {
            Intent intent = new Intent(context, PersonWebViewAct.class);
            intent.putExtra("completeUrl","yes");
            intent.putExtra("url", url);
            context.startActivity(intent);
        }

        @JavascriptInterface
        public void gotoMini(String url) {
            IWXAPI api = WXAPIFactory.createWXAPI(context, WXUtils.WX_APP_ID);
            WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
            req.userName = "gh_ac15d3111f1f"; // 填小程序原始id
            req.path = url;//拉起小程序页面的可带参路径，不填默认拉起小程序首页
            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
            api.sendReq(req);
        }

    }


    @SuppressLint("HandlerLeak")
    Handler handlerNew = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x11:     //支付宝支付
                    PayResult payResult = new PayResult((String) msg.obj);
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
//                         /** 订单界面 */
                        String orderNum = (String) msg.obj;
                        /** 活动界面 */
                        Bundle bundle = new Bundle();
                        bundle.putString("orderNum", orderNum);
                        Intent intent = new Intent(context, WebActivity.class);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    } else {
                        LogUtils.showToast("亲，付款未成功，请稍后重试！", true);
                        StateMessage.isPinTuan = false;
                        String url = StateMessage.PinTuanUrl;
                        Intent intent = new Intent(context, WebviewActivity.class);
                        intent.putExtra("url", url);
                        context.startActivity(intent);
                        ((Activity) JocellWebView.this.context).finish();
                    }

                    break;
                case 0x22:    //只分享到微信、朋友圈
                    wxPupWindow.showAtLocation(JocellWebView.this, Gravity.BOTTOM, 0, 0);
                    break;
            }
        }
    };


    String resultStatus;//支付结果返回码
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                ((Activity) JocellWebView.this.context).finish();
            } else if (msg.what == 2) {
                popWindow.showAtLocation(JocellWebView.this, Gravity.BOTTOM, 0, 0);
            } else if (msg.what == 3) {    //支付宝付款完成
//                Bundle bundle = new Bundle();
//                bundle.putString("orderNum", ordernum);
//                Intent intent = new Intent(context, OrderMessageAct.class);
//                intent.putExtras(bundle);
//                context.startActivity(intent);
                PayResult payResult = new PayResult((String) msg.obj);
                String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                resultStatus = payResult.getResultStatus();
                // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                if (TextUtils.equals(resultStatus, "9000")) {
                    Intent intent = new Intent(context, WebActivity.class);
                    if (StateMessage.exchangeOrder){
                        Bundle bundle = new Bundle();
                        bundle.putString("exchangeUrl", HttpClient.HTTP_DOMAIN + "/exchange/record/index?distributorId=" + SharedPreferencesUtils.getInstance().getString("distributorId", ""));
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                        StateMessage.exchangeOrder = false;
                    }else {
                        Toast.makeText(context, "支付成功", Toast.LENGTH_SHORT).show();
                        Bundle bundle = new Bundle();

                        if (StateMessage.isPinTuan) {//拼团
                            bundle.putString("orderNum", null);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        } else {
                            bundle.putString("orderNum", ordernum);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        }
                    }
                } else {
                    // 判断resultStatus 为非"9000"则代表可能支付失败
                    // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                    if (StateMessage.exchangeOrder){
                        Bundle bundle = new Bundle();
                        bundle.putString("exchangeUrl", HttpClient.HTTP_DOMAIN + "/exchange/index");
                        Intent intent = new Intent(context, WebActivity.class);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                        StateMessage.exchangeOrder = false;
                    }else {
                        if (TextUtils.equals(resultStatus, "8000")) {
//                        Toast.makeText(context, "支付结果确认中", Toast.LENGTH_SHORT).show();
                            Bundle bundle = new Bundle();
                            bundle.putString("orderNum", ordernum);
                            Intent intent = new Intent(context, OrderMessageAct.class);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(context, "支付失败", Toast.LENGTH_SHORT).show();
                            Bundle bundle = new Bundle();
                            bundle.putString("orderNum", ordernum);
                            Intent intent = new Intent(context, OrderMessageAct.class);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                            ((Activity) JocellWebView.this.context).finish();
                        }
                    }
                }
            } else if (msg.what == 4) {
                Bundle data = msg.getData();
                String url = data.getString("url");
                Intent intent = new Intent(context, ApplicationDrawbackActivity.class);
                intent.putExtra("url", url);
                context.startActivity(intent);
            } else if (msg.what == 5) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("strdata", msg.obj.toString());
                map.put("paytype", "1");
                map.put("source", "1");
                WxPayTask task = new WxPayTask(context, 0);
                task.startProgressDialog("", context);
                task.execute(map);
            } else if (msg.what == 7) {
                Bundle data = msg.getData();
                String url = data.getString("url");
                Intent intent = new Intent(context, WebviewActivity.class);
                intent.putExtra("url", Constants.domain + url);
                context.startActivity(intent);
            } else if (msg.what == 10) {
                Bundle data = msg.getData();
                String url = data.getString("url");
                Intent intent = new Intent(context, WebviewActivity.class);
                intent.putExtra("url", Constants.domain + url);
                context.startActivity(intent);
            } else if (msg.what == 11) {
                Bundle data = msg.getData();
                String url = data.getString("url");
                Intent intent = new Intent(context, WebviewActivity.class);
                intent.putExtra("url", Constants.domain + url);
                context.startActivity(intent);
            } else if (msg.what == 12) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("ordernum", msg.obj.toString());
                map.put("distributorId", SharedPreferencesUtils.getInstance().getString("distributorId", null));
                WxPayTask task = new WxPayTask(context, 1);
                task.startProgressDialog("", context);
                task.execute(map);
            } else if (msg.what == 13) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("distributorId", SharedPreferencesUtils.getInstance().getString("distributorId", null));
                WxPayTask task = new WxPayTask(context, 2);
                task.startProgressDialog("", context);
                task.execute(map);
            } else if (msg.what == 14) {
                Bundle data = msg.getData();
                String url = data.getString("url");
                Intent intent = new Intent(context, WebviewActivity.class);
                intent.putExtra("url", HttpClient.HTTP_DOMAIN + url);
                context.startActivity(intent);
            } else if (msg.what == 16) {
                ((Activity) JocellWebView.this.context).finish();
                //  Constants.mainActivity.person();
            } else if (msg.what == 17) {   //通知个人中心刷新
                ((Activity) JocellWebView.this.context).finish();
                EventBus.getDefault().post("person_refresh");
            } else if (msg.what == 18) {
                Bundle data = msg.getData();
                String url = data.getString("url");
                Intent intent = new Intent(context, WebviewActivity.class);
                intent.putExtra("url", HttpClient.HTTP_DOMAIN + url + "?distributorId=" + SharedPreferencesUtils.getInstance().getString("distributorId", null));
                context.startActivity(intent);
            } else if (msg.what == 19) {
                Bundle data = msg.getData();
                String url = data.getString("url");
                Intent intent = new Intent(context, WebviewActivity.class);
                intent.putExtra("url", HttpClient.HTTP_DOMAIN + url);
                context.startActivity(intent);
            } else if (msg.what == 20) {
                if (StateMessage.isGDMessahe) {
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                    StateMessage.isGDMessahe = false;
                    ((Activity) JocellWebView.this.context).finish();
                } else {
                    EventBus.getDefault().post(new Boolean(true));
                    AppManager.getAppManager().goBackMain();
                }
            } else if (msg.what == 21) {
                if (StateMessage.isGDMessahe) {
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                    StateMessage.isGDMessahe = false;
                    ((Activity) JocellWebView.this.context).finish();
                } else {
                    EventBus.getDefault().post(new Boolean(true));
                    AppManager.getAppManager().goBackMain();
                }
            } else if (msg.what == 23) {
                loaduserinfo();
            }else if(msg.what == 25){
                Bundle data = msg.getData();
                String url = data.getString("url");
                Intent intent = new Intent(context, WebviewActivity.class);
                intent.putExtra("url", url);
                context.startActivity(intent);
            }
            else if(msg.what == 26){
                Intent intent = new Intent(context, VoucherCenterAct.class);
                context.startActivity(intent);
            }else if (msg.what == 27){
                Bundle bundle = new Bundle();
                bundle.putString("orderNum", msg.obj.toString());
                Intent intent = new Intent(context, OrderMessageAct.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }

        }
    };

    String ordernum;

    /***
     * 获取支付信息并调用支付宝开始付款
     *
     * @param
     */
    private void getAlipayOrder(String url, Map<String, Object> params) {
        HttpClient.post(url, params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(String content) {
                        super.onSuccess(content);
//                        Log.i("log--", content);
                        try {
                            org.json.JSONObject object = new org.json.JSONObject(content);
                            String status = object.optString("status");
                            ordernum = object.optString("ordernum");
                            MApplication.ordernum = ordernum;
                            if (status.equals("1")) {
                                String data = object.optString("data");
                                new AlipayThread(data).start();
                            } else if (status.equals("2")){//0元支付
                                Toast.makeText(context, "支付成功", Toast.LENGTH_SHORT).show();
                                Bundle bundle = new Bundle();
                                Intent intent = new Intent(context, WebActivity.class);
                                if (StateMessage.isPinTuan){//拼团
                                    bundle.putString("orderNum", null);
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);
                                }else {
                                    bundle.putString("orderNum", ordernum);
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);
                                }
                            }else {
//                                String disId = SharedPreferencesUtils.getInstance().getString("distributorId", "1");
//                                load(HttpClient.HTTP_DOMAIN + "/order/detail?distributorId=" + disId + "&ordernum=" + ordernum + "&source=1");
                                LogUtils.showToast(object.getString("data"), false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Request request, IOException e) {
                        super.onFailure(request, e);
                    }
                }, context
        );
    }


    /**
     * 银联支付
     * @param appPayRequest
     */
    private void payAliPayMiniPro(String appPayRequest){
        UnifyPayRequest msg = new UnifyPayRequest();
        msg.payChannel = UnifyPayRequest.CHANNEL_ALIPAY_MINI_PROGRAM;
        msg.payData = appPayRequest;
        UnifyPayPlugin.getInstance(context).sendPayRequest(msg);
    }


    /***
     *
     */
    private void exchangePay(String url, Map<String, Object> params,String orderType) {
        HttpClient.post(url, params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(String content) {
                        super.onSuccess(content);
                        try {
                            org.json.JSONObject object = new org.json.JSONObject(content);
                            String status = object.optString("status");
                            if (status.equals("1")) {
                                StateMessage.exchangeOrder = true;
                                if (orderType.equals("3")) {//微信
                                    WXPayBO payBO = JSON.parseObject(object.optString("data"), WXPayBO.class);
                                    WXUtils.payWX(payBO);
                                }else {
                                    String data = object.optString("data");
                                    new AlipayThread(data).start();
                                }
                            } else {
                                LogUtils.showToast(object.getString("data"), false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Request request, IOException e) {
                        super.onFailure(request, e);
                    }
                }, context
        );
    }


    class AlipayThread extends Thread {
        String itemdata;

        AlipayThread(String itemdata) {
            this.itemdata = itemdata;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            PayTask alipay = new PayTask((Activity) context);
            String result = alipay.pay(itemdata, true);
//            Log.i("log--", result);
            Message msg = new Message();
            msg.what = 3;
            msg.obj = result;
            handler.sendMessage(msg);
            super.run();
        }
    }

    public ValueCallback<Uri> getmUploadMessage() {
        return mUploadMessage;
    }

    public void setmUploadMessage(ValueCallback<Uri> mUploadMessage) {
        this.mUploadMessage = mUploadMessage;
    }


    private void loaduserinfo() {
        StoreInfoEntity param = new StoreInfoEntity();
        HttpClient.userinfo(param, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String body) {
//                Log.e("getMobileCode", "返回数据:" + body);
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(body);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    String distributorId = object.getString("distributorId");
                    SharedPreferencesUtils.getInstance().putString("distributorId", distributorId);
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("jocellWebView", "jocellWebView");
                    context.startActivity(intent);
                } else if (HttpClient.UNLOGIN_CODE.equals(object.getString("status"))) {

                }
            }

            @Override
            public void onFailure(Request request, IOException e) {

            }
        }, context);
    }
}