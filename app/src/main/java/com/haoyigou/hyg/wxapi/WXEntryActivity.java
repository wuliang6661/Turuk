

package com.haoyigou.hyg.wxapi;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.haoyigou.hyg.application.GlobalApplication;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.config.StateMessage;
import com.haoyigou.hyg.entity.APPLoginEntity;
import com.haoyigou.hyg.entity.StoreInfoEntity;
import com.haoyigou.hyg.entity.UserBean;
import com.haoyigou.hyg.ui.LoginActivity;
import com.haoyigou.hyg.ui.MainActivity;
import com.haoyigou.hyg.ui.WeChatLoginInfomationActivity;
import com.haoyigou.hyg.utils.SharedPreferencesUtils;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Request;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import cn.sharesdk.wechat.utils.WXAppExtendObject;
import cn.sharesdk.wechat.utils.WXMediaMessage;
import cn.sharesdk.wechat.utils.WechatHandlerActivity;



public class WXEntryActivity extends WechatHandlerActivity implements IWXAPIEventHandler {
    public static String WX_APP_ID = "wx080d4ccaa7f3f9a0";
    public static IWXAPI api;
    private String JSESSIONID;
    String appopenid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, WX_APP_ID);
        api.handleIntent(getIntent(), this);
    }


    public void onGetMessageFromWXReq(WXMediaMessage msg) {
        if (msg != null) {
            Intent iLaunchMyself = getPackageManager().getLaunchIntentForPackage(getPackageName());
            startActivity(iLaunchMyself);
        }
    }


    public void onShowMessageFromWXReq(WXMediaMessage msg) {
        if (msg != null && msg.mediaObject != null
                && (msg.mediaObject instanceof WXAppExtendObject)) {
            WXAppExtendObject obj = (WXAppExtendObject) msg.mediaObject;
            Toast.makeText(this, obj.extInfo, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onReq(BaseReq req) {
        finish();
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp instanceof SendAuth.Resp) {
            SendAuth.Resp sresp = (SendAuth.Resp) resp;
            if (sresp.errCode == 0) {
                Toast.makeText(this, "微信登录成功", Toast.LENGTH_LONG).show();
                wechatlogin(sresp.code);
                StateMessage.IS_LOGIN = true;
            } else {
                Toast.makeText(this, "微信登录失败", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
        finish();
    }

    class BindTask extends AsyncTask<Map<String, String>, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Map<String, String>... arg0) {
            JSONObject jsons = null;
            Map map = arg0[0];
            APPLoginEntity param = new APPLoginEntity();
            param.setApptype(1);
            param.setCode(map.get("code").toString());
            Log.i("System.out", "wxcode " + map.get("code").toString());
            HttpClient.wechatlogin(param, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, String body) {
                    Log.e("getMobileCode", "返回数据:" + body);
                    String cookies = headers.get("Set-Cookie");
                    String cookie[] = cookies.split(";");
                    JSESSIONID = cookie[0].split("=")[1];
                    SharedPreferencesUtils.getInstance().putString("jsessionid", JSESSIONID);
                    com.alibaba.fastjson.JSONObject object = JSON.parseObject(body);
                    if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {


                    } else if (HttpClient.UNLOGIN_CODE.equals(object.getString("status"))) {

                    }
                }

                @Override
                public void onFailure(Request request, IOException e) {

                }
            }, WXEntryActivity.this);

            return jsons;
        }
    }

    private void wechatlogin(String map) {
        APPLoginEntity param = new APPLoginEntity();
        HttpClient.cookieStore.removeAll();
        param.setApptype(1);
        param.setCode(map);
        Log.i("System.out", "wxcode " + map.toString());
        HttpClient.wechatlogin(param, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, String body) {
                Log.e("getMobileCode", "返回数据:" + body);
                Log.e("TTT", headers.toString());
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(body);
                if (HttpClient.BINDCODE.equals(object.getString("status"))) {
                    String cookies = headers.get("Set-Cookie");
                    String cookie[] = cookies.split(";");
                    JSESSIONID = cookie[0].split("=")[1];
                    Log.e("QQQQQ", JSESSIONID);
                    SharedPreferencesUtils.getInstance().putString("jsessionid", JSESSIONID);
                    String token1 = object.getString("data");
                    SharedPreferencesUtils.getInstance().putString("token", token1);
                    appopenid = object.getString("appopenid");
                    String host = HttpClient.HTTP_DOMAIN;
                    Intent intent = new Intent(WXEntryActivity.this, WeChatLoginInfomationActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("appopenid", appopenid);
                    Log.e("RRR", appopenid);
                    Log.d("cookies0", HttpClient.cookieStore.getCookies().toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    String cookies = headers.get("Set-Cookie");
                    String cookie[] = cookies.split(";");
                    JSESSIONID = cookie[0].split("=")[1];
                    Log.e("QQQQQ", JSESSIONID);
                    SharedPreferencesUtils.getInstance().putString("jsessionid", JSESSIONID);
                    String token1 = object.getString("data");
                    SharedPreferencesUtils.getInstance().putString("token", token1);
                    loaduserinfo();
                } else if (HttpClient.UNSUCESS_CODE.equals(object.getString("status"))) {
                    Intent intent = new Intent(WXEntryActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Request request, IOException e) {

            }
        }, WXEntryActivity.this);
    }

//	class sleepThread extends Thread {
//		@Override
//		public void run() {
//			try {
//				sleep(1000);
//				handler.sendEmptyMessage(1);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			super.run();
//		}
//	}

//	android.os.Handler handler = new android.os.Handler() {
//		public void handleMessage(android.os.Message msg) {
//			if (msg.what == 1) {
//				Intent intent = new Intent(WXEntryActivity.this, WeChatLoginInfomationActivity.class);
//				Bundle bundle = new Bundle();
//				bundle.putString("appopenid", appopenid);
//				Log.e("RRR", appopenid);
//				intent.putExtras(bundle);
//				startActivity(intent);
//
//			}
//		}
//	};

    private void loaduserinfo() {
//		CookieSyncManager syncManager = CookieSyncManager.createInstance(WXEntryActivity.this.getApplicationContext());
//		CookieManager cookieManager = CookieManager.getInstance();
//		cookieManager.setAcceptCookie(true);
//		cookieManager.removeAllCookie();
//		syncManager.startSync();
//		String host = HttpClient.HTTP_DOMAIN;
//		cookieManager.setCookie(host, "JSESSIONID=" + JSESSIONID);
//		syncManager.sync();
        StoreInfoEntity param = new StoreInfoEntity();
        HttpClient.userinfo(param, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String body) {
                Log.e("getMobileCode", "返回数据:loaduserinfo" + body);
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(body);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    UserBean bean = JSON.parseObject(body, UserBean.class);
                    GlobalApplication.setUserBean(bean);
                    String shopname = object.getString("shopname");
                    Log.e("TAGGG+++", shopname);
                    String shopdesc = object.getString("shopdesc");
                    String headpic = object.getString("headerpic");
                    String income = object.getString("income");
                    Log.e("WWW", income);
                    String distributorId = object.getString("distributorId");
                    SharedPreferencesUtils.getInstance().putString("distributorId", distributorId);
                    Log.e("distributorId 0", distributorId);
                    Intent intent = new Intent(WXEntryActivity.this, MainActivity.class);
                    startActivity(intent);
                    Log.e("tiao", "tiao");
                    Log.e("HHHH", shopname);
                } else if (HttpClient.UNLOGIN_CODE.equals(object.getString("status"))) {
                    UserBean bean = JSON.parseObject(body, UserBean.class);
                    GlobalApplication.setUserBean(bean);
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {

            }
        }, WXEntryActivity.this);


    }
}




