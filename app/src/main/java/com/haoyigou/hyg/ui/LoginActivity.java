package com.haoyigou.hyg.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.application.GlobalApplication;
import com.haoyigou.hyg.base.BaseActivity;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.config.GenerateTestUserSig;
import com.haoyigou.hyg.config.StateMessage;
import com.haoyigou.hyg.entity.APPLoginEntity;
import com.haoyigou.hyg.entity.StoreInfoEntity;
import com.haoyigou.hyg.entity.UserBean;
import com.haoyigou.hyg.entity.UserExperienceIdentifyCodeEntity;
import com.haoyigou.hyg.ui.personweb.PersonWebViewAct;
import com.haoyigou.hyg.utils.HttpUtils;
import com.haoyigou.hyg.utils.LogUtils;
import com.haoyigou.hyg.utils.RegUtils;
import com.haoyigou.hyg.utils.SharedPreferencesUtils;
import com.haoyigou.hyg.utils.StringUtils;
import com.haoyigou.hyg.view.widget.TimeButton;
import com.mob.tools.utils.UIHandler;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Request;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMManager;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

import static android.R.attr.action;


/**
 * 登录页面
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout go_back;  //返回
    private EditText login_etphone;//输入手机号
    private EditText login_etcode;//输入验证码
    private EditText imageCodeEdit;   //图片验证码输入
    private ImageView imageCode;    //图片验证码显示
    private Button login_button; //登录按钮
    private Button login_weixin;//微信登录按钮
    private TimeButton login_timebutton;//倒计时按钮
    private String JSESSIONID;
    private String distributorId = "";
    private CheckBox agree;
    /***
     * 整个应用Applicaiton
     **/
    /**
     * 当前Activity的弱引用，防止内存泄露
     **/
    private WeakReference<Activity> context = null;
    // 自己微信应用的 appId
    public static String WX_APP_ID = "wx080d4ccaa7f3f9a0";
    // 自己微信应用的 appSecret
    public static IWXAPI wxApi;

    String codeStr;
    private TextView usertext;
    private TextView usertext2;
    private Toast toast;
    private ProgressDialog progressDialog;
    private int MSG_ACTION_CCALLBACK=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setIsSunce(false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);
        //禁止录屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
        XGPushManager.unregisterPush(getApplicationContext());
        //将当前Activity压入栈
        context = new WeakReference<Activity>(this);
        mApplication.pushTask(context);
        wxApi = WXAPIFactory.createWXAPI(this, WX_APP_ID, true);
        wxApi.registerApp(WX_APP_ID);
        initview();
        initevent();
        getImgVersition();
        if (getIntent().hasExtra("addFinish")) {
            StateMessage.isLoginMethod = getIntent().getBooleanExtra("addFinish", false);
        }
        if (StateMessage.isLoginMethod) {
            go_back.setVisibility(View.VISIBLE);
            StateMessage.isLoginMethod = false;
        } else {
            go_back.setVisibility(View.INVISIBLE);
        }
    }

    private void initevent() {
        go_back.setOnClickListener(this);
        //点击发送验证码
        login_timebutton.setOnClickListener(this);
        //点击登录
        login_button.setOnClickListener(this);
        //微信登录
        login_weixin.setOnClickListener(this);
        imageCode.setOnClickListener(this);
        usertext.setOnClickListener(this);
        usertext2.setOnClickListener(this);
    }

    private void initview() {
        //weixin_login= (Button) findViewById(R.id.login_wenxin);
        login_etphone = (EditText) findViewById( R.id.login_etphone);
        login_etcode = (EditText) findViewById(R.id.login_etcode);
        login_button = (Button) findViewById(R.id.login_button);
        login_weixin = (Button) findViewById(R.id.login_wenxin);
        login_timebutton = (TimeButton) findViewById(R.id.login_timebutton);
        go_back = (LinearLayout) findViewById(R.id.finish_act);
        imageCode = (ImageView) findViewById(R.id.image_code);
        imageCodeEdit = (EditText) findViewById(R.id.image_code_edit);
        usertext = (TextView) findViewById(R.id.usertext);
        usertext2 = (TextView) findViewById(R.id.usertext2);
        toast = Toast.makeText(LoginActivity.this, "验证码错误", Toast.LENGTH_SHORT);
        agree = findViewById(R.id.agree);
    }

    /**
     * 获取图文验证码
     */
    private void getImgVersition() {
        HttpClient.getImageVersition(new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
//                LogUtils.e("wuliang", content);
                JSONObject object = JSON.parseObject(content);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    Glide.with(LoginActivity.this).load(HttpClient.HTTP_DOMAIN +
                            object.getString("path")).into(imageCode);
//                    codeStr = object.getString("code");
                }
            }
        }, LoginActivity.this);
    }

    /**
     * 验证码请求数据
     */

    private void loadDate() {
        UserExperienceIdentifyCodeEntity param = new UserExperienceIdentifyCodeEntity();
        param.setPhone(login_etphone.getText().toString());
//        Log.e("DDD", login_etphone.getText().toString());
        param.setCodetype("2");
        param.setVerifycode(imageCodeEdit.getText().toString().trim());
        HttpClient.sendMobileCode(param, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String body) {
//                Log.e("getMobileCode", "返回数据:" + body);
                JSONObject object = JSON.parseObject(body);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    Toast.makeText(LoginActivity.this, "验证码发送成功", Toast.LENGTH_SHORT).show();
                } else if (HttpClient.UNLOGIN_CODE.equals(object.getString("status"))) {
                    Toast.makeText(LoginActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {

            }
        }, LoginActivity.this);
    }

    /**
     * 登录请求数据
     */

    private void mobilelogin() {
        APPLoginEntity param = new APPLoginEntity();
        param.setApptype(2);
        param.setTel(login_etphone.getText().toString());
        param.setMobileCode(login_etcode.getText().toString());
        //每次重新登录的时候,都清理一下cookie
        cleanCookie();
        HttpClient.MobileLogin(param, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, String body) {
//                Log.e("getMobilePHONE", "返回数据:" + body);
                //获取cookie
                String cookies = headers.get("Set-Cookie");
                if (cookies != null) {
                    String cookie[] = cookies.split(";");
                    JSESSIONID = cookie[0].split("=")[1];
                }
                if (body == null) {
                    showToast("登陆出错！");
                    goToActivity(MainActivity.class, true);
                    loaduserinfo();
                }
                JSONObject object = JSON.parseObject(body);
                SharedPreferencesUtils.getInstance().putString("jsessionid", JSESSIONID);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    String token1 = object.getString("data");
                    SharedPreferencesUtils.getInstance().putString("token", token1);
                    SharedPreferencesUtils.getInstance().putString("userPhone", login_etphone.getText().toString());
                    loaduserinfo();
                } else if (HttpClient.UNLOGIN_CODE.equals(object.getString("status"))) {
                    login_button.setEnabled(true);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {

            }
        }, LoginActivity.this);
    }

    /**
     * 获取合伙人信息
     */
    private void loaduserinfo() {
        StoreInfoEntity param = new StoreInfoEntity();
        HttpClient.userinfo(param, new AsyncHttpResponseHandler() {
            public void onSuccess(String body) {
//                Log.e("getMobileCodeuser", "返回数据:" + body);
                JSONObject object = JSON.parseObject(body);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    StateMessage.IS_LOGIN = true;
                    distributorId = object.getString("distributorId");
                    SharedPreferencesUtils.getInstance().putString("distributorId", distributorId);
                    UserBean bean = JSON.parseObject(body, UserBean.class);
                    GlobalApplication.setUserBean(bean);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    JPushInterface.setAlias(getApplicationContext(),111,login_etphone.getText().toString());
                    Set<String> tags = new HashSet<>();
                    tags.add(login_etphone.getText().toString());
                    JPushInterface.setTags(getApplicationContext(),222,tags);
                    LoginActivity.this.finish();
                    // 登录腾讯IM,username 为用户名，userSig 为用户登录凭证
                    String userSig = GenerateTestUserSig.genTestUserSig(object.getString("phone"));
                    TIMManager.getInstance().login(object.getString("phone"), userSig, new TIMCallBack() {
                        @Override
                        public void onError(int code, String desc) {
                            //错误码 code 和错误描述 desc，可用于定位请求失败原因
                            //错误码 code 列表请参见错误码表
//                            Log.d("腾讯IM登录", "错误代码" + code + "错误信息" + desc);
                        }

                        @Override
                        public void onSuccess() {
                            Log.d("腾讯IM登录", "成功");
                        }
                    });



                } else if ("0".equals(object.getString("status"))) {
                    login_button.setEnabled(true);
                    UserBean bean = JSON.parseObject(body, UserBean.class);
                    GlobalApplication.setUserBean(bean);
                } else {
                    login_button.setEnabled(true);
                    Toast.makeText(LoginActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                }
            }

            public void onFailure(Request request, IOException e) {
            }
        }, this);
    }


    /***
     * QQ快捷登陆
     */
    public void login() {
        Tencent mTencent = Tencent.createInstance("1105612457", this.getApplicationContext());
        if (!mTencent.isSessionValid()) {
            mTencent.login(this, "get_user_info", new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    org.json.JSONObject object = (org.json.JSONObject) o;
                    Toast.makeText(LoginActivity.this, object.toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(UiError uiError) {
                }

                @Override
                public void onCancel() {
                }
            });
        }
    }

    //清除cookie
    public void cleanCookie() {
        HttpUtils._supermember_token = null;
        HttpUtils.islogin = false;
        HttpUtils.JSESSIONID = null;
        SharedPreferencesUtils.getInstance().remove("token");
        SharedPreferencesUtils.getInstance().remove("jsessionid");
        CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(LoginActivity.this);
        android.webkit.CookieManager cookieManager = android.webkit.CookieManager.getInstance();
        cookieManager.removeSessionCookie();
        cookieManager.removeAllCookie();
        cookieSyncMngr.stopSync();
        HttpClient.cookieStore.removeAll();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.finish_act:
                goToActivity(MainActivity.class, true);
                break;
            case R.id.login_timebutton:
                if (login_etphone.length() == 0) {
                    Toast.makeText(LoginActivity.this, "手机号不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                if (login_etphone.length() < 11) {
                    Toast.makeText(LoginActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (!RegUtils.isMobileNumber(login_etphone.getText().toString())) {
//                    Toast.makeText(LoginActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                if (StringUtils.isEmpty(imageCodeEdit.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "图文验证码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (!codeStr.equalsIgnoreCase(imageCodeEdit.getText().toString())) {
//                    getImgVersition();
//                    Toast.makeText(LoginActivity.this, "图文验证码错误", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                login_timebutton.startTimer();
                loadDate();
                break;
            case R.id.login_button:
                if (login_etphone.length() == 0) {
                    Toast.makeText(LoginActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (login_etphone.length() < 11) {
                    Toast.makeText(LoginActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (!RegUtils.isMobileNumber(login_etphone.getText().toString())) {
//                    Toast.makeText(LoginActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                if (login_etcode.length() == 0) {
                    Toast.makeText(LoginActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!agree.isChecked()){
                    Toast.makeText(LoginActivity.this, "请先阅读并同意用户协议与隐私政策", Toast.LENGTH_SHORT).show();
                    return;
                }
                login_button.setEnabled(false);
                toast.cancel();
                mobilelogin();
                break;
            case R.id.login_wenxin:
                IWXAPI msgApi = WXAPIFactory.createWXAPI(LoginActivity.this, null);
                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "supermember";
                msgApi.registerApp(WX_APP_ID);
                msgApi.sendReq(req);
//                LoginActivity.this.finish();

//                if (isWeixinAvilible(this)) {
//                    Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
//                    wechat.setPlatformActionListener(this);
//                    wechat.SSOSetting(false);
//                    authorize(wechat, 1);
//
//                }else {
//                    Toast.makeText(this, "您还没有安装微信，请先安装微信客户端",Toast.LENGTH_SHORT).show();
//                }
                break;
            case R.id.image_code:
                getImgVersition();
                break;
            case R.id.usertext:
                Intent intent = new Intent(LoginActivity.this,userWebviewActivity.class);
                intent.putExtra("url", "/userAgreement");
                intent.putExtra("title", "用户协议");
                startActivity(intent);
                break;
            case R.id.usertext2:
                Intent intent1 = new Intent(LoginActivity.this, userWebviewActivity.class);
                intent1.putExtra("url", "/privacyNotice");
                intent1.putExtra("title", "隐私声明");
                startActivity(intent1);
                break;

        }
    }

    public boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

}




