package com.haoyigou.hyg.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andrjhf.storage.encrypt.AES256SharedPreferences;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.application.GlobalApplication;
import com.haoyigou.hyg.application.MApplication;
import com.haoyigou.hyg.base.BaseFragmentActivity;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.config.GenerateTestUserSig;
import com.haoyigou.hyg.config.MessageEvent;
import com.haoyigou.hyg.config.StateMessage;
import com.haoyigou.hyg.entity.Constants;
import com.haoyigou.hyg.entity.ShopCarBean;
import com.haoyigou.hyg.fragment.CommunityFragment;
import com.haoyigou.hyg.fragment.GroupFragment;
import com.haoyigou.hyg.fragment.NewGroupFragment;
import com.haoyigou.hyg.fragment.NewHomeFragment;
import com.haoyigou.hyg.fragment.NewTVShowFragment;
import com.haoyigou.hyg.fragment.PersonFragment;
import com.haoyigou.hyg.fragment.ShoppingCartFragment;
import com.haoyigou.hyg.service.AlarmService;
import com.haoyigou.hyg.utils.CustomUpdateParser;
import com.haoyigou.hyg.utils.DisplayUtil;
import com.haoyigou.hyg.utils.DisplayUtils;
import com.haoyigou.hyg.utils.OKHttpUpdateHttpService;
import com.haoyigou.hyg.utils.SharedPreferencesUtils;
import com.haoyigou.hyg.utils.UpdateManager;
import com.haoyigou.hyg.utils.Util;
import com.haoyigou.hyg.utils.WindowUtils;
import com.haoyigou.hyg.wxapi.WXUtils;
import com.luck.picture.lib.tools.SPUtils;
import com.meiqia.core.callback.OnInitCallback;
import com.meiqia.meiqiasdk.util.MQConfig;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import cn.jzvd.Jzvd;
import cn.sharesdk.framework.ShareSDK;
import me.leolin.shortcutbadger.ShortcutBadger;

import static android.net.ConnectivityManager.TYPE_MOBILE;
import static android.net.ConnectivityManager.TYPE_WIFI;
import static com.xuexiang.xupdate.entity.UpdateError.ERROR.CHECK_NO_NEW_VERSION;

/**
 * 包含4个fragment的主窗体
 */

public class MainActivity extends BaseFragmentActivity implements View.OnClickListener {


    @BindView(R.id.person_image)
    ImageView personImage;
    @BindView(R.id.person_text)
    TextView personText;
    @BindView(R.id.person_fragment)
    LinearLayout personFragment;
    @BindView(R.id.login_image)
    ImageView loginImage;
    @BindView(R.id.login_text)
    TextView loginText;
    @BindView(R.id.login_fragment)
    LinearLayout loginFragment;
    @BindView(R.id.home_image)
    ImageView homeImage;
    @BindView(R.id.home_text)
    TextView homeText;
    @BindView(R.id.home_fragment)
    LinearLayout homeFragment;
    @BindView(R.id.classify_image)
    ImageView classifyImage;
    @BindView(R.id.classify_text)
    TextView classifyText;
    @BindView(R.id.classify_fragment)
    RelativeLayout classifyFragment;
    @BindView(R.id.shopcar_image)
    ImageView shopcarImage;
    @BindView(R.id.shopcar_text)
    TextView shopcarText;
    @BindView(R.id.shopcar_fragment)
    LinearLayout shopcarFragment;
    @BindView(R.id.person_pre)
    ImageView personPre;
    @BindView(R.id.login_pre)
    ImageView loginPre;
    @BindView(R.id.home_pre)
    ImageView homePre;
    @BindView(R.id.classify_pre)
    ImageView classifyPre;
    @BindView(R.id.shopcar_pre)
    ImageView shopcarPre;
    @BindView(R.id.live_image)
    ImageView liveImage;
    @BindView(R.id.live_pre)
    ImageView livePre;
    @BindView(R.id.live_text)
    TextView liveText;
    @BindView(R.id.live_fragment)
    LinearLayout livFragment;
    @BindView(R.id.rlTeamActivity)
    RelativeLayout rlTeamActivity;//团购标签
    @BindView(R.id.txtTeamActivity)
    TextView txtTeamActivity;//团购文字
    @BindView(R.id.rlBottom)
    RelativeLayout rlBottom;

    /***
     * 以下为显示的fragment
     */
    private Fragment personalFragment = new PersonFragment();//个人中心
    private NewHomeFragment shopFragment = new NewHomeFragment();//首页
    private Fragment cartFragment = new ShoppingCartFragment();//购物车
    private Fragment newGroupFragment = new NewGroupFragment();
    private Fragment fenlei = GroupFragment.newInstance("https://m.best1.com/groupbuy/productList?showplat=2&fromapp=1&appversion=10&parentLocation=110");//正式
//    private Fragment fenlei = GroupFragment.newInstance("https://ruiduo.happydoit.com/groupbuy/productList?showplat=2&fromapp=1&appversion=10&parentLocation=110");//测试
//    private Fragment liveFragment = new LiveAPPFragment();

    private Fragment newTVShowFragment = new NewTVShowFragment();//新改变加入直播的页面
//    private Fragment newTVShowFragment = new NewTVShowFragment();//老版TV直播界面
    private CommunityFragment communityFragment = new CommunityFragment();//社区
    private boolean isQuit;

    private static int position = -111111;   //默认从首页显示

    private IntentFilter intentFilter;
    private NetworkChangeReceiver networkChangeReceiver;
    private AlertView alertView;

    private MyReceiver receiver;
    public static boolean isForeground = false;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    //网络监听
    class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectionManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            if (connectionManager.getActiveNetworkInfo() != null) {
                NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isAvailable()) {
                    switch (networkInfo.getType()) {
                        case TYPE_MOBILE:
                            if (!MApplication.isFirstOpen){
                                Toast.makeText(context, "当前为移动网络", Toast.LENGTH_SHORT).show();
                                EventBus.getDefault().post("4G");//通知LiveAppFragment网络变化
                            }
                            break;
                        case TYPE_WIFI:
                            if (!MApplication.isFirstOpen) {
                                Toast.makeText(context, "当前为wifi网络", Toast.LENGTH_SHORT).show();
                                EventBus.getDefault().post("wifi");//通知LiveAppFragment网络变化
                            }
                            break;
                        default:
                            break;
                    }
                } else {
                    Toast.makeText(context, "网络已断开", Toast.LENGTH_SHORT).show();
                }
                MApplication.isFirstOpen = false;
            }else {
                Toast.makeText(context, "网络已断开", Toast.LENGTH_SHORT).show();
                MApplication.isFirstOpen = false;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        ButterKnife.bind(this);
        //网络变化监测
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, intentFilter);

        registerReserve();
        Constants.mainActivity = this;
        setListener();
        goToFragment(shopFragment);
        switchButtom(2);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = (int)(DisplayUtil.getMobileWidth(this) * 0.01);
        layoutParams.leftMargin = (int)(DisplayUtil.getMobileWidth(this) * 0.31);
        rlTeamActivity.setLayoutParams(layoutParams);

        receiver = new MyReceiver();
        IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);

        registerReceiver(receiver, homeFilter);
//        apkVerifyWithSHA(this,"");
        if (SPUtils.getInstance("code").getString("codeNo") == null ||
                !SPUtils.getInstance("code").getString("codeNo").equals(Util.getAppVersionName(this))){
            infoDialog();
        }else{
            ShareSDK.initSDK(MainActivity.this);
            initTIM();
            WXUtils.registerWX(MainActivity.this);
            initYunPlayer();
            JPushInterface.init(MainActivity.this);
            JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
            SPUtils.getInstance("code").put("codeNo", Util.getAppVersionName(MainActivity.this));
        }
    }

    /**
     * 注册各类服务
     */
    AlertView malertView;
    private void registerReserve() {
        EventBus.getDefault().register(this);
//        setFlgsbg(Color.TRANSPARENT);//通知栏所需颜色
        setmStatusBar(2);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
             malertView = new AlertView("注意", "需要获取访问您设备文件的权限以正常使用本应用以及后续安装更新", null,
                    new String[]{"确定", "取消"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onItemClick(Object o, int position) {
                    if (position == 0) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    }
                    malertView.dismiss();
                }
            });
            malertView.show();
        }
        //版本更新
        XUpdate.newBuild(this)
                .updateUrl(HttpClient.UPDATEVERSION)
                .updateParser(new CustomUpdateParser()) //设置自定义的版本更新解析器
                .update();
    }

    /**
     * 浸入式状态栏实现同时取消5.0以上的阴影
     */
    protected void setmStatusBar(int i) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = getWindow().getDecorView();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        //修改字体颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//android6.0以后可以对状态栏文字颜色和图标进行修改
            if (i==1) {//黑色状态栏文字
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }else {//白色状态栏文字
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean isHasInstallPermissionWithO(Context context){
        if (context == null){
            return false;
        }
        return context.getPackageManager().canRequestPackageInstalls();
    }

    /**
     * 开启设置安装未知来源应用权限界面
     * @param context
     */
    @RequiresApi (api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity(Context context) {
        if (context == null){
            return;
        }
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        ((Activity)context).startActivityForResult(intent,666);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode== 666 ){
           UpdateManager updateManager = new UpdateManager(MainActivity.this, "main");
           updateManager.checkUpdate();

        }else if (resultCode == 1){//TV直播界面由全屏切换为小屏显示的时候
            EventBus.getDefault().post(new MessageEvent("openVoice", "yes"));//发给LiveAppFragment
        }
    }

    public void showDialog(String str) {
        alertView = new AlertView("", "需要获取允许安装应用权限，是否去设置？", null,
                new String[]{"确定", "取消"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemClick(Object o, int position) {
                if (position == 0) {
                    startInstallPermissionSettingActivity(MainActivity.this);
                } else {
                    alertView.dismiss();
                }
            }
        });
        alertView.show();
    }

    /***
     * 设置底部菜单点击事件
     */
    private void setListener() {
        personFragment.setOnClickListener(this);
        loginFragment.setOnClickListener(this);
        homeFragment.setOnClickListener(this);
        classifyFragment.setOnClickListener(this);
        shopcarFragment.setOnClickListener(this);
        livFragment.setOnClickListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Boolean isMain) {   //是否进入首页
        if (isMain) {   //跳入首页
            goToFragment(shopFragment);
            switchButtom(2);
        } else {
            goToFragment(cartFragment);
            switchButtom(4);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String str) {
        if (str.equals("teamActivity")) {
           String lable = MApplication.teamActivity;
           if (lable != null && !lable.equals("null") && !lable.equals("")){
               rlTeamActivity.setVisibility(View.VISIBLE);
               txtTeamActivity.setText(lable);
           }else {
               rlTeamActivity.setVisibility(View.GONE);
           }
        }else if (str.equals("ShowTvFragment")){//显示直播Fragment
            MApplication.tvFragment = 1;
            MApplication.tvParentLocation = "105";
            Fragment newTVShowFragment = new NewTVShowFragment();
            goToFragment(newTVShowFragment);
//            switchButtom(5);
            setmStatusBar(1);
            Jzvd.releaseAllVideos();
        }else if (str.equals("ShowPinFragment")){//显示拼团Fragment
            goToFragment(newGroupFragment);
            switchButtom(3);
            setmStatusBar(2);
        }else if (str.equals("CloseTvFragment")){//关闭直播Fragment
            MApplication.tvFragment = 0;
            goToFragment(shopFragment);
//            switchButtom(2);
            setmStatusBar(1);
            rlBottom.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 添加fragment
     */
    @Override
    protected void onResume() {
        super.onResume();
        Context context = getApplicationContext();
        isForeground = true;
        if (StateMessage.IS_LOGIN) {    //判断是否登陆
            personFragment.setVisibility(View.VISIBLE);
            loginFragment.setVisibility(View.GONE);
            XGPushManager.registerPush(context, SharedPreferencesUtils.getInstance().getString("userPhone", "1"));
//            XGPushConfig.enableDebug(context, true);  //发布要删除
        } else {
            personFragment.setVisibility(View.GONE);
            loginFragment.setVisibility(View.VISIBLE);
            XGPushManager.registerPush(context);
//            XGPushConfig.enableDebug(context, true);
        }

        try {
            XGPushClickedResult click = XGPushManager.onActivityStarted(this);
            if (click != null) { // 判断是否来自信鸽的打开方式
                // 根据实际情况处理...
                Intent intent = new Intent(this, MessageBoxActivty.class);
                startActivity(intent);
            }
            StateMessage.badgeNum = 0;   //点击打开app时取消角标显示
//            ShortcutBadger.applyCount(this, 0);
            ShortcutBadger.removeCountOrThrow(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
//        XGPushManager.unregisterPush(this);
        unregisterReceiver(networkChangeReceiver);
        unregisterReceiver(receiver);
    }

    private Fragment mContent = null;

    /**
     * 修改显示的内容 不会重新加载
     **/
    public void goToFragment(Fragment to) {
        if (mContent != to) {
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            transaction.setCustomAnimations(R.anim.fade_in,R.anim.fade_out);
            if (!to.isAdded()) { // 先判断是否被add过
                if (mContent != null) {
                    transaction.hide(mContent).add(R.id.fragment_container, to).commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到Activity中
                }
                else {
                    transaction.add(R.id.fragment_container, to).commitAllowingStateLoss();
                }
            } else {
                if (mContent != null) {
                    transaction.hide(mContent).show(to).commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
                }
                else {
                    transaction.show(to).commitAllowingStateLoss();
                }
            }
            mContent = to;
        }
    }

    private boolean needAlarm = true;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断程序进入后台是否是用户自身造成的（触摸返回键或HOME键），是则无需弹出警示。
        if((keyCode==KeyEvent.KEYCODE_BACK || keyCode==KeyEvent.KEYCODE_HOME) && event.getRepeatCount()==0){
            needAlarm = false;
        }

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (Jzvd.backPress()) {
                return false;
            }
            if (isQuit == false) {
                if (MApplication.tvFragment == 1){//当前显示的是从首页点击更多进入的NewTvFragment,先关闭
                    goToFragment(shopFragment);
                    switchButtom(2);
                    setmStatusBar(1);
                    MApplication.tvFragment = 0;
                    rlBottom.setVisibility(View.VISIBLE);
                }else {
                    isQuit = true;
                    Toast.makeText(this, R.string.exit_app, Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isQuit = false;
                        }
                    }, 2000);
                }
            } else {
                mApplication.exit();
            }
        }
        return false;
    }

    @Override
    protected void onStop() {
        //若程序进入后台不是用户自身造成的，则需要弹出警示
        if(needAlarm) {
            //弹出警示信息
//            Toast.makeText(getApplicationContext(), "您的界面被覆盖，请确认操作环境是否安全", Toast.LENGTH_SHORT).show();
            //启动我们的AlarmService,用于给出覆盖了正常Activity的类名
            Intent intent = new Intent(this, AlarmService.class);
            startService(intent);
        }
        super.onStop();
    }

    @Override
    protected void onPause() {
        //若程序进入后台不是用户自身造成的，则需要弹出警示
        Jzvd.releaseAllVideos();
        isForeground = false;
        super.onPause();
    }

    private class MyReceiver extends BroadcastReceiver {

        private final String SYSTEM_DIALOG_REASON_KEY = "reason";
        private final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
        private final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);

                if (reason == null)
                    return;

                // Home键
                if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
//                    Toast.makeText(getApplicationContext(), "按了Home键", Toast.LENGTH_SHORT).show();
                    needAlarm = false;
                }

                // 最近任务列表键
                if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
//                    Toast.makeText(getApplicationContext(), "按了最近任务列表", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.person_fragment:
                goToFragment(personalFragment);
                switchButtom(0);
//                setMFlgsbg(getResources().getDrawable(R.drawable.tran_bg));//通知栏所需颜色
                setmStatusBar(2);
                Jzvd.releaseAllVideos();
                break;
            case R.id.login_fragment:
                switchButtom(1);
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra("addFinish", true);
                MainActivity.this.startActivity(intent);
                MainActivity.this.finish();
                Jzvd.releaseAllVideos();
                break;
            case R.id.home_fragment:
                goToFragment(shopFragment);
                switchButtom(2);
//                setFlgsbg(R.color.white);//通知栏所需颜色
                setmStatusBar(2);
                Jzvd.releaseAllVideos();
                break;
            case R.id.classify_fragment:
                goToFragment(newGroupFragment);
                switchButtom(3);
//                setFlgsbg(R.color.white);//通知栏所需颜色
                setmStatusBar(2);
                Jzvd.releaseAllVideos();
                break;
            case R.id.shopcar_fragment:
                goToFragment(cartFragment);
                switchButtom(4);
//                setMFlgsbg(getResources().getDrawable(R.drawable.change_color));//通知栏所需颜色
                setmStatusBar(2);
                Jzvd.releaseAllVideos();
                break;
            case R.id.live_fragment:
                MApplication.tvParentLocation = "105";
                goToFragment(communityFragment);
                switchButtom(5);
//                setMFlgsbg(getResources().getDrawable(R.drawable.change_color));//通知栏所需颜色
                setmStatusBar(2);
                Jzvd.releaseAllVideos();
                break;
        }
    }

    public void setMFlgsbg(Drawable drawable) {
        // 修改状态栏颜色，4.4+生效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintDrawable(drawable);//通知栏所需颜色
        }
    }
    /**
     * 切换底下显示
     *
     * @param position 0:为我的 1：登录 2:首页 3: 分类 4:购物车 5：直播
     */
    private void switchButtom(int position) {
        if (MainActivity.position == position) return;
        //Todo 切换页面暂停视频播放
//        shopFragment.setPause();
        if (position != 4) {   //非点击购物车
            StateMessage.isShopCarFinnish = false;
            StateMessage.url = "";
            EventBus.getDefault().post(new ShopCarBean());
        }
        if (position == 0) {
            setButton(personImage, personPre, personText, true);
        } else {
            setButton(personImage, personPre, personText, false);
        }
        if (position == 1) {
            setButton(loginImage, loginPre, loginText, true);
        } else {
            setButton(loginImage, loginPre, loginText, false);
        }
        if (position == 2) {
            setButton(homeImage, homePre, homeText, true);
        } else {
            setButton(homeImage, homePre, homeText, false);
        }
        if (position == 3) {
            setButton(classifyImage, classifyPre, classifyText, true);
        } else {
            setButton(classifyImage, classifyPre, classifyText, false);
        }
        if (position == 4) {
            setButton(shopcarImage, shopcarPre, shopcarText, true);
        } else {
            setButton(shopcarImage, shopcarPre, shopcarText, false);
        }

        if (position == 5) {
            setButton(liveImage, livePre, liveText, true);
        } else {
            setButton(liveImage, livePre, liveText,  false);
        }

        MainActivity.position = position;
    }

    private void setButton(ImageView image_pre, ImageView image_dis, TextView text, boolean ispre) {
        if (ispre) {
            image_pre.setVisibility(View.GONE);
            image_dis.setVisibility(View.VISIBLE);
            text.setTextColor(getResources().getColor(R.color.newMain));
        } else {
            image_pre.setVisibility(View.VISIBLE);
            image_dis.setVisibility(View.GONE);
            text.setTextColor(getResources().getColor(R.color.footer_text_color_normal));
        }
    }

    /**
     *  协议
     */
    private void infoDialog() {
        // 构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialog);
        final LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.information_dialog_layout, null);
        TextView cancle = v.findViewById(R.id.cancle);
        TextView sure = v.findViewById(R.id.sure);
        TextView txt = v.findViewById(R.id.txt2);

        // SpannableStringBuilder 用法
        SpannableStringBuilder spannableBuilder = new SpannableStringBuilder();
        spannableBuilder.append(txt.getText().toString());
        //设置部分文字点击事件
        ClickableSpan clickableSpan = new ClickableSpan() {//好易购用户协议
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(MainActivity.this, userWebviewActivity.class);
                intent.putExtra("url", "/userAgreement");
                intent.putExtra("title", "用户协议");
                startActivity(intent);
            }
            @Override
            public void updateDrawState(TextPaint ds) {
//                设定的是span超链接的文本颜色，而不是点击后的颜色
                ds.setColor(Color.parseColor("#009FFF"));
                ds.setUnderlineText(false);    //去除超链接的下划线
                ds.clearShadowLayer();//清除阴影
            }

        };
        spannableBuilder.setSpan(clickableSpan, 0, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        //设置部分文字点击事件
        ClickableSpan clickableSpan2 = new ClickableSpan() {//隐私声明
            @Override
            public void onClick(View widget) {
                Intent intent1 = new Intent(MainActivity.this, userWebviewActivity.class);
                intent1.putExtra("url", "/privacyNotice");
                intent1.putExtra("title", "隐私声明");
                startActivity(intent1);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
//                设定的是span超链接的文本颜色，而不是点击后的颜色
                ds.setColor(Color.parseColor("#009FFF"));
                ds.setUnderlineText(false);    //去除超链接的下划线
                ds.clearShadowLayer();//清除阴影
            }

        };
        spannableBuilder.setSpan(clickableSpan2, 13, 19, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        txt.setText(spannableBuilder);
        txt.setHighlightColor(getResources().getColor(android.R.color.transparent));//点击后的背景颜色，Android4.0以上默认是淡绿色，低版本的是黄色
        txt.setMovementMethod(LinkMovementMethod.getInstance());

        builder.setView(v);
        builder.setCancelable(true);
        final Dialog noticeDialog = builder.create();
        noticeDialog.getWindow().setGravity(Gravity.CENTER);
        noticeDialog.setCancelable(false);
        noticeDialog.show();

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noticeDialog.dismiss();
                mApplication.exit();
            }
        });

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noticeDialog.dismiss();
                ShareSDK.initSDK(MainActivity.this);
                initTIM();
                WXUtils.registerWX(MainActivity.this);
                initYunPlayer();
                JPushInterface.init(MainActivity.this);
                JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
                SPUtils.getInstance("code").put("codeNo", Util.getAppVersionName(MainActivity.this));
            }
        });

        WindowManager.LayoutParams layoutParams = noticeDialog.getWindow().getAttributes();
        layoutParams.width = (int)(DisplayUtils.getScreenWidth(MainActivity.this)*0.75);
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        noticeDialog.getWindow().setAttributes(layoutParams);
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
}