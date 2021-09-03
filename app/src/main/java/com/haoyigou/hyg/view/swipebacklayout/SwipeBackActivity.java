
package com.haoyigou.hyg.view.swipebacklayout;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import androidx.fragment.app.FragmentActivity;

import com.haoyigou.hyg.application.GlobalApplication;

import java.lang.ref.WeakReference;


public class SwipeBackActivity extends FragmentActivity implements SwipeBackActivityBase {
    private SwipeBackActivityHelper mHelper;

    /***整个应用Applicaiton**/
    private GlobalApplication mApplication = null;
    /**当前Activity的弱引用，防止内存泄露**/
    private WeakReference<Activity> context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取应用Application
        mApplication = GlobalApplication.getInstance();

        //将当前Activity压入栈
        context = new WeakReference<Activity>(this);
        mApplication.pushTask(context);

        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
        //使用字体不被系统影响
       // keepFontSize();
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            setTranslucentStatus(true);
//            SystemBarTintManager tintManager = new SystemBarTintManager(this);
//            tintManager.setStatusBarTintEnabled(true);
//            if("com.haoyigou.hyg.activity.LoginActivity".equals(this.getClass().getName())){
//                tintManager.setStatusBarTintResource(R.color.login_bar_bg);//通知栏所需颜色
//                // tintManager.setNavigationBarTintResource(R.color.login_nav_bg);
//            } if("com.drjing.xibao.module.news.activity.AdvertisementActivity".equals(this.getClass().getName())){
//                tintManager.setStatusBarTintResource(R.color.transparent);
//            }else{
//                tintManager.setStatusBarTintResource(R.color.color_statu_gray);//通知栏所需颜色
//
//            }
//        }

   }

    //使用字体不被系统影响
    private void keepFontSize(){
        Resources res = getResources();
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
 /*   @TargetApi(19)
    private void setTranslucentStatus() {
        Window window = getWindow();
        // Translucent status bar
        window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // Translucent navigation bar
        window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mApplication.removeTask(context);
    }
}
