package com.haoyigou.hyg.application;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.andrjhf.storage.encrypt.AES256SharedPreferences;
import com.haoyigou.hyg.config.GenerateTestUserSig;
import com.haoyigou.hyg.entity.UserBean;
import com.haoyigou.hyg.utils.OKHttpUpdateHttpService;
import com.haoyigou.hyg.utils.WindowUtils;
import com.meiqia.core.callback.OnInitCallback;
import com.meiqia.meiqiasdk.util.MQConfig;
import com.tencent.imsdk.TIMConnListener;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMGroupEventListener;
import com.tencent.imsdk.TIMGroupTipsElem;
import com.tencent.imsdk.TIMLogLevel;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMRefreshListener;
import com.tencent.imsdk.TIMSdkConfig;
import com.tencent.imsdk.TIMUserConfig;
import com.tencent.imsdk.TIMUserStatusListener;
import com.tencent.imsdk.session.SessionWrapper;
import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate.entity.UpdateError;
import com.xuexiang.xupdate.listener.OnUpdateFailureListener;
import com.xuexiang.xupdate.utils.UpdateUtils;
import com.yunfan.player.widget.PlayerAuthentication;

import java.util.List;

import androidx.multidex.MultiDex;
import cat.ereza.customactivityoncrash.CustomActivityOnCrash;

import static com.xuexiang.xupdate.entity.UpdateError.ERROR.CHECK_NO_NEW_VERSION;

/**
 * Created by kristain on 15/12/20.
 */
public class GlobalApplication extends MApplication {
    private Context context;

    public static UserBean user;
    public static int screen_height, screen_width;

    public static AES256SharedPreferences mAES256SharedPreferences;


    private static GlobalApplication app;
    private String b;
    private String c;
    private String d;
    private String e;
    private String f;
    private String g;

    public String getG() {
        return g;
    }

    public void setG(String g) {
        this.g = g;
    }

    public String getF() {
        return f;
    }

    public void setF(String f) {
        this.f = f;
    }

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    private int a;

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }


    public static int currentVersionCode;
    public static String currentVersionName;


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
//        CustomActivityOnCrash.install(this);
        getVersionCode();
        getHeightAndWidth();
        initUpdate();
        initMQ();
        WindowUtils.init(this);
        mAES256SharedPreferences = new AES256SharedPreferences(getSharedPreferences("haoyigou", Context.MODE_PRIVATE));
    }

    private void initUpdate() {
        /** 版本更新 **/
        XUpdate.get()
                .debug(true)
                .isWifiOnly(false)                                               //默认设置只在wifi下检查版本更新
                .isGet(true)                                                    //默认设置使用get请求检查版本
                .isAutoMode(false)                                              //默认设置非自动模式，可根据具体使用配置
                .param("versionCode", UpdateUtils.getVersionCode(this))         //设置默认公共请求参数
                .param("appKey", getPackageName())
                .setOnUpdateFailureListener(new OnUpdateFailureListener() {     //设置版本更新出错的监听
                    @Override
                    public void onFailure(UpdateError error) {
                        if (error.getCode() != CHECK_NO_NEW_VERSION) {          //对不同错误进行处理
                            Toast.makeText(GlobalApplication.this,error.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .supportSilentInstall(true)                                     //设置是否支持静默安装，默认是true
                .setIUpdateHttpService(new OKHttpUpdateHttpService())           //这个必须设置！实现网络请求功能。
                .init(this);
    }


    /***
     * 注册美恰客服
     */
    private void initMQ() {
        //正式  0d00707be69e8db7e1122e81890c7fde
        //测试  6ca56a57b4bb55c85218f6dbac0b47a1

        MQConfig.init(this, "0d00707be69e8db7e1122e81890c7fde", new OnInitCallback() {
            @Override
            public void onSuccess(String clientId) {
                Log.e("log--", "init success");
            }

            @Override
            public void onFailure(int code, String message) {
                Log.e("log--", "int failure");
            }
        });
    }


    /**
     * 注册云帆直播
     */
    private void initYunPlayer() {
        PlayerAuthentication.AuthCallBack authCallBack =
                new PlayerAuthentication.AuthCallBack() {
                    @Override
                    public void onAuthenticateSuccess() {
                        Log.d("yunfan", "鉴权成功~！");
                    }

                    @Override
                    public void onAuthenticateError(int errorCode) {
                        Log.d("yunfan", "鉴权失败啦：" + errorCode);
                    }
                };
        PlayerAuthentication.getInstance().authenticate("2f10266ff5b3ae7d884beca9117e30edfd3492b9", authCallBack);
    }


    /**
     * 获取屏幕的高度和宽度
     */
    private void getHeightAndWidth() {
        // 要获取屏幕的宽和高等参数，首先需要声明一个DisplayMetrics对象，屏幕的宽高等属性存放在这个对象中
        DisplayMetrics DM = new DisplayMetrics();
        // 获取窗口管理器,获取当前的窗口,调用getDefaultDisplay()后，其将关于屏幕的一些信息写进DM对象中,最后通过getMetrics(DM)获取
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(DM);
        screen_width = manager.getDefaultDisplay().getWidth();
        screen_height = manager.getDefaultDisplay().getHeight();
    }


    public GlobalApplication() {
        app = this;
    }

    public static synchronized GlobalApplication getInstance() {
        if (app == null) {
            app = new GlobalApplication();
        }
        return app;

    }

    /***
     * 登陆成功时保存User信息，后面方便取用
     *
     * @param bean
     */
    public static void setUserBean(UserBean bean) {
        user = bean;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 退出APP时手动调用
     */
    @Override
    public void exit() {
        try {
            //关闭所有Activity
            //FloatViewService floatViewService = new FloatViewService();
//            Intent intent = new Intent(this, FloatViewService.class);
//            stopService(intent);
            removeAll();
            //退出进程
            System.exit(0);
        } catch (Exception e) {
        }
    }


    // 注册App异常崩溃处理器
    private void registerUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(AppException.getAppExceptionHandler());
    }

    /**
     * 初始化腾讯IM
     */
    private void initTIM(){
        //初始化 IM SDK 基本配置
        //判断是否是在主线程
        if (SessionWrapper.isMainProcess(getApplicationContext())) {
            TIMSdkConfig config = new TIMSdkConfig(GenerateTestUserSig.SDKAPPID)
                    .enableLogPrint(true)
                    .setLogLevel(TIMLogLevel.DEBUG)
                    .setLogPath(Environment.getExternalStorageDirectory().getPath() + "/justfortest/");

            //初始化 SDK
            TIMManager.getInstance().init(getApplicationContext(), config);
            //基本用户配置
            TIMUserConfig userConfig = new TIMUserConfig()
                    //设置用户状态变更事件监听器
                    .setUserStatusListener(new TIMUserStatusListener() {
                        @Override
                        public void onForceOffline() {
                            //被其他终端踢下线
                            Log.i("腾讯IM状态", "onForceOffline");
                        }

                        @Override
                        public void onUserSigExpired() {
                            //用户签名过期了，需要刷新 userSig 重新登录 IM SDK
                            Log.i("腾讯IM状态", "onUserSigExpired");
                        }
                    })
                    //设置连接状态事件监听器
                    .setConnectionListener(new TIMConnListener() {
                        @Override
                        public void onConnected() {
                            Log.i("腾讯IM状态", "onConnected");
                        }

                        @Override
                        public void onDisconnected(int code, String desc) {
                            Log.i("腾讯IM状态", "onDisconnected");
                        }

                        @Override
                        public void onWifiNeedAuth(String name) {
                            Log.i("腾讯IM状态", "onWifiNeedAuth");
                        }
                    })
                    //设置群组事件监听器
                    .setGroupEventListener(new TIMGroupEventListener() {
                        @Override
                        public void onGroupTipsEvent(TIMGroupTipsElem elem) {
                            Log.i("腾讯IM状态", "onGroupTipsEvent, type: " + elem.getTipsType());
                        }
                    })
                    //设置会话刷新监听器
                    .setRefreshListener(new TIMRefreshListener() {
                        @Override
                        public void onRefresh() {
                            Log.i("腾讯IM状态", "onRefresh");
                        }

                        @Override
                        public void onRefreshConversation(List<TIMConversation> conversations) {
                            Log.i("腾讯IM状态", "onRefreshConversation, conversation size: " + conversations.size());
                        }
                    });

            //禁用本地所有存储
            userConfig.disableStorage();
            //开启消息已读回执
            userConfig.enableReadReceipt(false);
            //将用户配置与通讯管理器进行绑定
            TIMManager.getInstance().setUserConfig(userConfig);
        }
    }


    /**
     * 获取软件版本号
     *
     * @return
     */
    private void getVersionCode() {
        PackageManager packageManager = this.getPackageManager();
        PackageInfo packInfo;
        try {
            packInfo = packageManager.getPackageInfo(this.getPackageName(), 0);
            currentVersionCode = packInfo.versionCode;
            currentVersionName = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
