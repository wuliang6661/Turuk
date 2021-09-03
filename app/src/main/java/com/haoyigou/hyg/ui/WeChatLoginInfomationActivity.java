package com.haoyigou.hyg.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.base.BaseActivity;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.entity.APPWeChatLoginBindMobileEntity;
import com.haoyigou.hyg.entity.StoreInfoEntity;
import com.haoyigou.hyg.entity.UserExperienceIdentifyCodeEntity;
import com.haoyigou.hyg.utils.LogUtils;
import com.haoyigou.hyg.utils.RegUtils;
import com.haoyigou.hyg.utils.SharedPreferencesUtils;
import com.haoyigou.hyg.utils.StringUtils;
import com.haoyigou.hyg.view.widget.TimeButton;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * 微信登录绑定手机号
 */
public class WeChatLoginInfomationActivity extends BaseActivity implements View.OnClickListener {

    private Button wechat_infomation_submit;
    private EditText wechat_infomation_etphone;
    private EditText wechat_information_etcode;
    private TimeButton wechat_login_timebutton;
    private EditText imageCodeEdit;   //图片验证码输入
    private ImageView imageCode;    //图片验证码显示
    /**
     * 当前Activity的弱引用，防止内存泄露
     **/
    private WeakReference<Activity> context = null;
    private ImageView setting_backto;
    private TextView tvHearder;

    private boolean isQuit;
    String codeStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_wechat_login);
        //将当前Activity压入栈
        context = new WeakReference<Activity>(this);
        mApplication.pushTask(context);
        initview();
        getImgVersition();
    }

    private void initview() {
        wechat_login_timebutton = (TimeButton) findViewById(R.id.wechat_login_timebutton);
        wechat_infomation_etphone = (EditText) findViewById(R.id.wechat_infomation_mobile);
        wechat_information_etcode = (EditText) findViewById(R.id.wechat_login_etcode);
        wechat_infomation_submit = (Button) findViewById(R.id.wechat_infomation_submit);
        setting_backto = (ImageView) findViewById(R.id.setting_backto);
        imageCode = (ImageView) findViewById(R.id.image_code);
        imageCodeEdit = (EditText) findViewById(R.id.image_code_edit);
        tvHearder = (TextView) findViewById(R.id.tvHearder);
        tvHearder.setText("绑定手机号");
        //退出
        setting_backto.setVisibility(View.GONE);
        setting_backto.setOnClickListener(this);
        wechat_infomation_submit.setOnClickListener(this);
        wechat_login_timebutton.setOnClickListener(this);
        imageCode.setOnClickListener(this);
    }

    /**
     * 获取数据
     */
    private void loadData() {
        Bundle bundle = getIntent().getExtras();
        String appopenid = bundle.getString("appopenid");
        String phone = wechat_infomation_etphone.getText().toString();
        String code = wechat_information_etcode.getText().toString();
//        Log.e("RwwwR", appopenid);
        APPWeChatLoginBindMobileEntity param = new APPWeChatLoginBindMobileEntity();
        param.setAppopenid(appopenid);
        param.setPhone(phone);
        param.setMesscode(code);
        HttpClient.wechatloginbindmobile(param, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Headers headers, String body) {
//                Log.e("getMobileCode1", "绑定手机号返回数据:" + body);
//                Log.e("ppp", headers.toString());
//                Log.e("body", body);
                JSONObject object = JSON.parseObject(body);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    loaduserinfo();

                } else if (HttpClient.UNLOGIN_CODE.equals(object.getString("status"))) {
                    String message = object.getString("message");
                    Toast.makeText(WeChatLoginInfomationActivity.this, message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {

            }
        }, WeChatLoginInfomationActivity.this);
    }

    /**
     * 获取合伙人信息
     */
    private void loaduserinfo() {
        StoreInfoEntity param = new StoreInfoEntity();
        HttpClient.userinfo(param, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String body) {
//                Log.e("getUSERINFORMATIOACode", "返回数据:" + body);
                JSONObject object = JSON.parseObject(body);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    String distributorId = object.getString("distributorId");
                    SharedPreferencesUtils.getInstance().putString("distributorId", distributorId);
                    Intent intent = new Intent(WeChatLoginInfomationActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else if (HttpClient.UNLOGIN_CODE.equals(object.getString("status"))) {

                }
            }

            @Override
            public void onFailure(Request request, IOException e) {

            }
        }, WeChatLoginInfomationActivity.this);


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
                    Glide.with(WeChatLoginInfomationActivity.this).load(HttpClient.HTTP_DOMAIN +
                            object.getString("path")).into(imageCode);
//                    codeStr = object.getString("code");
                }
            }
        }, WeChatLoginInfomationActivity.this);
    }


    /**
     * 获取验证码
     */
    private void loadDate() {
        UserExperienceIdentifyCodeEntity param = new UserExperienceIdentifyCodeEntity();
        param.setPhone(wechat_infomation_etphone.getText().toString());
//        Log.e("DDDd", wechat_infomation_etphone.getText().toString());
        param.setCodetype("2");
        param.setVerifycode(imageCodeEdit.getText().toString().trim());
        HttpClient.sendMobileCode(param, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String body) {
//                Log.e("getMobileCode", "返回数据:" + body);
                JSONObject object = JSON.parseObject(body);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                } else if (HttpClient.UNLOGIN_CODE.equals(object.getString("status"))) {

                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
            }
        }, WeChatLoginInfomationActivity.this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isQuit) {
                isQuit = true;
                Toast.makeText(this, R.string.exit_app, Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isQuit = false;
                    }
                }, 2000);
            } else {
                SharedPreferencesUtils.getInstance().remove("token");
                mApplication.exit();
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_backto:
                goToActivity(LoginActivity.class, true);
                break;
            case R.id.wechat_infomation_submit:   //提交
                if (wechat_infomation_etphone.length() == 0) {
                    Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (wechat_infomation_etphone.length() < 11) {
                    Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (!RegUtils.isMobileNumber(wechat_infomation_etphone.getText().toString())) {
//                    Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                if (wechat_information_etcode.length() == 0) {
                    Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                loadData();
                break;
            case R.id.wechat_login_timebutton:    //获取验证码
                if (wechat_infomation_etphone.length() == 0) {
                    Toast.makeText(this, "手机号不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
//                if (!RegUtils.isMobileNumber(wechat_infomation_etphone.getText().toString())) {
//                    Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                if (StringUtils.isEmpty(imageCodeEdit.getText().toString())) {
                    Toast.makeText(this, "图文验证码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (!codeStr.equalsIgnoreCase(imageCodeEdit.getText().toString())) {
//                    getImgVersition();
//                    Toast.makeText(this, "图文验证码错误", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                wechat_login_timebutton.startTimer();
                loadDate();
                break;
            case R.id.image_code:
                getImgVersition();
                break;
        }
    }
}
