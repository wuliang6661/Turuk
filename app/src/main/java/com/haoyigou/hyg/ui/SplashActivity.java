package com.haoyigou.hyg.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import com.haoyigou.hyg.utils.BitmapUtils;
import com.haoyigou.hyg.utils.SharedPreferencesUtils;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMManager;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;

/***
 * App入口 ， 引导页
 */
public class SplashActivity extends BaseActivity {
    /***
     * 默认广告停留时间为3秒
     */
    private static int COUNTDOWN_TIME = 3;
    /**
     * 当前Activity的弱引用，防止内存泄露
     **/
    private WeakReference<Activity> context = null;
    private ImageView guideImage;
    /**
     * 广告倒计时显示
     */
    private TextView countDownText;
    private int count = 1;
    private Timer timer = null;
    private ViewPager pager;
    private GalleryPagerAdapter adapter;
    private String distributorId = "1";
    private String JSESSIONID;

    private boolean firstTimeUse = false;  //判断是否第一次进入app,默认为否

    /**
     * 引导页面图片
     */
    private int[] guide_images = {
            R.drawable.start_one,
            R.drawable.start_two,
            R.drawable.start_three,
            R.drawable.start_four
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setIsSunce(false);
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        setContentView(R.layout.act_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //将当前Activity压入栈
        context = new WeakReference<Activity>(this);
        mApplication.pushTask(context);
        invition();
        setListener();
        /**
         * 判断是否第一次进入APP
         */
        firstTimeUse = SharedPreferencesUtils.getInstance().getBoolean("first-time-use", true);
        if (firstTimeUse) {   //第一次进入，展示引导页
            initGuideGallery();
        }

        GotoMain();
//        }   //非第一次进入
    }


    /***
     * 初始化控件
     */
    private void invition() {
        guideImage = (ImageView) findViewById(R.id.guideImage);
        countDownText = (TextView) findViewById(R.id.countdown_text);
    }

    /***
     * 设置事件监听
     */
    private void setListener() {
        countDownText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToActivity(MainActivity.class, true);
                if (timer != null)
                    timer.cancel();
            }
        });
    }


    /***
     * 非第一次进入，则直接进入首页
     */
    private void GotoMain() {
        CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(SplashActivity.this.getApplicationContext());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeSessionCookie();
        cookieManager.removeAllCookie();
        cookieSyncMngr.stopSync();
        HttpClient.cookieStore.removeAll();
        tokenlogin();
    }

    /**
     * token登录
     */
    private void tokenlogin() {
        if (!isNetworkAvailable()) {
            goToActivity(MainActivity.class, true);
            Toast.makeText(this, "网络未连接!", Toast.LENGTH_SHORT);
        }
        APPLoginEntity param = new APPLoginEntity();
        param.setApptype(3);
        String token = SharedPreferencesUtils.getInstance().getString("token", "");
        if (token != null) {
            param.setToken(URLEncoder.encode(token));
        } else {
            param.setToken(null);
        }
        HttpClient.tokenlogin(param, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, String body) {
//                Log.e("tokenlogin--->", body);
                loaduserinfo();              //登陆请求完成 ，不管是否成功，都需获取公共信息
                //获取cookie
                String cookies = headers.get("Set-Cookie");
                if (cookies != null) {
                    String cookie[] = cookies.split(";");
                    JSESSIONID = cookie[0].split("=")[1];
                }
                SharedPreferencesUtils.getInstance().putString("jsessionid", JSESSIONID);
                if (body == null) return;
                JSONObject object = JSON.parseObject(body);
                if (!firstTimeUse) {
                    setDataToken(object);
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                goToActivity(MainActivity.class, true);
                Toast.makeText(SplashActivity.this, "登录信息失效!", Toast.LENGTH_SHORT);
            }
        }, SplashActivity.this);
    }

    /***
     * 判断是第一次进入还是后来进入的token登陆处理
     */
    private void setDataToken(JSONObject object) {
        String status = object.getString("status"); //登陆状态
        if (HttpClient.RET_SUCCESS_CODE.equals(status)) {  //登陆成功， 有广告数据，则开始加载广告
            StateMessage.IS_LOGIN = true;
            String havead = object.getString("havead");
            if (havead != null && havead.equals("1")) {
                String ImgUrl = object.getString("adlogo");
                String ImgListener = object.getString("adaddress");
                setAD_Img(ImgUrl, ImgListener);
            } else {
                goToActivity(MainActivity.class, true);
            }
            String token1 = object.getString("data");
            SharedPreferencesUtils.getInstance().putString("token", token1);
        }
        if (HttpClient.UNSUCESS_CODE.equals(status) || HttpClient.UNLOGIN_CODE.equals(status)) {  //登陆报错,404
            SharedPreferencesUtils.getInstance().putString("distributorId", "1");
            StateMessage.IS_LOGIN = false;
            goToActivity(MainActivity.class, true);
        }
        //否则失效了就重新登录
        //SharedPreferencesUtils.getInstance().putString("distributorId", "1");
        //StateMessage.IS_LOGIN = false;
        //goToActivity(MainActivity.class, true);
    }


    /**
     * 此处方法，用来开始展示广告的倒计时
     *
     * @param imgUrl
     * @param imgAddress
     */
    private void setAD_Img(String imgUrl, final String imgAddress) {
        Log.e("log--", "设置广告图片。。。");
        Picasso.with(SplashActivity.this).load(imgUrl)
                .into(guideImage);
        countDownText.setVisibility(View.VISIBLE);
        guideImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgAddress.equals("") || imgAddress.length() < 10) return;
                StateMessage.isGDMessahe = true;
                Intent intent = new Intent(SplashActivity.this, WebviewActivity.class);
                intent.putExtra("url", imgAddress);
                intent.putExtra("is_home", true);
                startActivity(intent);
                if (timer != null)
                    timer.cancel();
                finish();
            }
        });
        timer = new Timer();
        timer.schedule(task, 1000, 1000);
    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            handler.sendEmptyMessage(0);
        }
    };


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            COUNTDOWN_TIME--;
            if (COUNTDOWN_TIME <= 0) {
                timer.cancel();
                goToActivity(MainActivity.class, true);
            }
            countDownText.setText(COUNTDOWN_TIME + " 跳过");
        }
    };


    private void initGuideGallery() {
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setVisibility(View.VISIBLE);
        adapter = new GalleryPagerAdapter();
        pager.setAdapter(adapter);
    }

    public class GalleryPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return guide_images.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView item = new ImageView(SplashActivity.this);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            item.setLayoutParams(lp);
            item.setScaleType(ImageView.ScaleType.FIT_XY);
            //为了家载大图片出现oom,使用bitmap进行加载
            item.setImageBitmap(BitmapUtils.readBitMap(SplashActivity.this, guide_images[position]));
            //item.setImageResource(guide_images[position]);
            if (position == guide_images.length - 1) {
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferencesUtils.getInstance().putBoolean("first-time-use", false);
                        StateMessage.IS_LOGIN = false;
                        SplashActivity.this.goToActivity(MainActivity.class, true);
                    }
                });
            }
            container.addView(item);
            return item;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void loaduserinfo() {
        StoreInfoEntity param = new StoreInfoEntity();
        HttpClient.userinfo(param, new AsyncHttpResponseHandler() {
            public void onSuccess(String body) {
//                Log.e("getMobileCodeuser", "返回数据:" + body);
                JSONObject object = JSON.parseObject(body);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    distributorId = object.getString("distributorId");
                    if (distributorId == null || distributorId.trim().equals("")) {
                        distributorId = "1";
                    }
                    SharedPreferencesUtils.getInstance().putString("distributorId", distributorId);
                    UserBean bean = JSON.parseObject(body, UserBean.class);
                    SharedPreferencesUtils.getInstance().putString("userPhone", object.getString("phone"));
//                    Log.e("log--phone", object.getString("phone"));
                    GlobalApplication.setUserBean(bean);
                    JPushInterface.setAlias(getApplicationContext(),111,object.getString("phone"));
                    Set<String> tags = new HashSet<>();
                    tags.add(object.getString("phone"));
                    JPushInterface.setTags(getApplicationContext(),222,tags);
                } else if ("0".equals(object.getString("status"))) {
                    UserBean bean = JSON.parseObject(body, UserBean.class);
                    GlobalApplication.setUserBean(bean);
                    SharedPreferencesUtils.getInstance().putString("distributorId", "1");
                }

                // 登录腾讯IM,username 为用户名，userSig 为用户登录凭证
                String userSig = GenerateTestUserSig.genTestUserSig(object.getString("phone"));
                TIMManager.getInstance().login(object.getString("phone"), userSig, new TIMCallBack() {
                    @Override
                    public void onError(int code, String desc) {
                        //错误码 code 和错误描述 desc，可用于定位请求失败原因
                        //错误码 code 列表请参见错误码表
                        Log.d("腾讯IM登录", "错误代码" + code + "错误信息" + desc);
                    }

                    @Override
                    public void onSuccess() {
                        Log.d("腾讯IM登录", "成功");
                    }
                });

            }

            public void onFailure(Request request, IOException e) {
            }
        }, this);
    }
}
