package com.haoyigou.hyg.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import android.view.Window;
import android.view.WindowManager;

import com.haoyigou.hyg.application.GlobalApplication;
import com.haoyigou.hyg.utils.LogUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.lang.ref.WeakReference;

/**
 * Created by kristain on 15/12/19.
 */
public class BaseFragmentActivity extends BaseActivity {

    /***
     * 整个应用Applicaiton
     **/
    public static GlobalApplication mApplication = null;
    /**
     * 当前Activity的弱引用，防止内存泄露
     **/
    private WeakReference<Activity> context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication = GlobalApplication.getInstance();
        LogUtils.init(this);
        //将当前Activity压入栈
        context = new WeakReference<Activity>(this);
        mApplication.pushTask(context);
        keepFontSize();//使用字体不被系统影响
//        setFlgsbg(R.color.title_bg);//通知栏所需颜色
    }

    public void setFlgsbg(int color) {
        // 修改状态栏颜色，4.4+生效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(color);//通知栏所需颜色
        }
    }


    private void keepFontSize() {
        Resources res = getResources();
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 结束Activity从堆栈中移除
        mApplication.removeTask(context);
    }

    @TargetApi(19)
    protected void setTranslucentStatus() {
        Window window = getWindow();
        // Translucent status bar
        window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // Translucent navigation bar
        window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }
}