package com.haoyigou.hyg.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.haoyigou.hyg.R;
import com.haoyigou.hyg.application.GlobalApplication;
import com.haoyigou.hyg.utils.AppManager;
import com.haoyigou.hyg.utils.LogUtils;
import com.haoyigou.hyg.utils.SharedPreferencesUtils;
import com.haoyigou.hyg.utils.WindowUtils;
import com.haoyigou.hyg.view.widget.LoadingProgressDialog;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * 所有Activity的父类，实现一些activity里公共的功能
 * Created by Administrator on 2016/10/24.
 */
public class BaseActivity extends FragmentActivity {

    public GlobalApplication mApplication;

    static LoadingProgressDialog proDialog = null;
    private boolean isSunce = true;   //状态栏是否需要改变颜色
    public String disId;
    private boolean needAlarm = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication = GlobalApplication.getInstance();
        AppManager.getAppManager().addActivity(this);
        LogUtils.init(this);
        WindowUtils.init(this);
//        setFlgsbg(R.color.title_bg);//通知栏所需颜色
        disId = SharedPreferencesUtils.getInstance().getString("distributorId", "1");
        setStatusBar();
    }


    public void setFlgsbg(int color) {
        if (!isSunce) return;
        // 修改状态栏颜色，4.4+生效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(color);//通知栏所需颜色
        }
    }

    /**
     * 浸入式状态栏实现同时取消5.0以上的阴影
     */
    protected void setStatusBar() {
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
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }
    /**
     * 设置状态栏是否需要改变颜色，默认改变
     *
     * @param isSunce
     */
    public void setIsSunce(boolean isSunce) {
        this.isSunce = isSunce;
    }


    /***
     * 传值跳转页面
     */
    public void goToActivity(Class<?> cls, Bundle bundle, boolean isFinish) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(bundle);
        startActivity(intent);
        if (isFinish)
            finish();
    }


    /**
     * 跳转activity
     */
    public void goToActivity(Class<?> cls, boolean isFinish) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
        if (isFinish)
            finish();
    }


    /***
     * 标题栏的返回监听
     */
    public void goBack() {
        LinearLayout back = (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 设置标题文字
     */
    public void setTitleText(String title) {
        TextView titleText = (TextView) findViewById(R.id.title_text);
        titleText.setText(title);
    }

    /**
     * 设置标题栏右边图标
     */
    public void setRightImage(int resource, View.OnClickListener listener) {
        ImageView rightImage = (ImageView) findViewById(R.id.right_image);
        rightImage.setVisibility(View.VISIBLE);
        rightImage.setImageResource(resource);
        rightImage.setOnClickListener(listener);
    }


    /**
     * 设置标题栏右边文字
     */
    public void setRightText(String text, View.OnClickListener listener) {
        TextView rightText = (TextView) findViewById(R.id.right_text);
        rightText.setVisibility(View.VISIBLE);
        rightText.setText(text);
        rightText.setOnClickListener(listener);
    }


    /***
     * 设置返回键是否显示,默认显示
     */
    public void setBackVistable(int vistable) {
        LinearLayout back = (LinearLayout) findViewById(R.id.back);
        back.setVisibility(vistable);
    }


    /***
     * 显示toast弹窗
     */
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    /**
     * 判断网络是否连接
     *
     * @return
     */
    public static boolean isNetworkAvailable() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) GlobalApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
        } catch (Exception e) {
            Log.v("Connectivity", e.getMessage());
        }
        return false;
    }

    public static void startProgressDialog(String progressMsg, Context mcontext) {
        if (proDialog == null) {
            proDialog = LoadingProgressDialog.createDialog(mcontext);
            proDialog.setMessage(progressMsg);
            proDialog.setCanceledOnTouchOutside(false);
        }
        proDialog.show();
    }


    public static void stopProgressDialog() {
        if (proDialog != null) {
            proDialog.dismiss();
            proDialog = null;
        }
    }

    @Override
    protected void onDestroy() {
        stopProgressDialog();
        AppManager.getAppManager().removeActivity(this);
        super.onDestroy();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        //判断程序进入后台是否是用户自身造成的（触摸返回键或HOME键），是则无需弹出警示。
//        if((keyCode==KeyEvent.KEYCODE_BACK) && event.getRepeatCount()==0){
//            if (proDialog != null && proDialog.isShowing()){
//                return false;
//            }
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    @Override
//    protected void onPause() {
//        //若程序进入后台不是用户自身造成的，则需要弹出警示
//        if(needAlarm) {
//            //弹出警示信息
//            Toast.makeText(getApplicationContext(), "您的界面被覆盖，请确认操作环境是否安全", Toast.LENGTH_SHORT).show();
//            //启动我们的AlarmService,用于给出覆盖了正常Activity的类名
//            Intent intent = new Intent(this, AlarmService.class);
//            startService(intent);
//        }
//        super.onPause();
//    }
}
