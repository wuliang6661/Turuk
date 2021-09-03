package com.haoyigou.hyg.view.floatview;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;

import com.haoyigou.hyg.R;
import com.haoyigou.hyg.ui.BaseFloatActivity;
import com.haoyigou.hyg.utils.DisplayUtils;
import com.haoyigou.hyg.utils.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;

/**
 * FloatWindowManager:管理悬浮窗视频播放
 * android.view.WindowManager$BadTokenException:
 * Unable to add window android.view.ViewRootImpl$W@123e0ab --
 * permission denied for this window 2003,type
 *
 * @author Nonolive-杜乾 Created on 2017/12/12-17:35.
 * E-mail:dusan.du@nonolive.com
 */

public class FloatWindowManager {
    public static final int FW_TYPE_ROOT_VIEW = 10;
    public static final int FW_TYPE_APP_DIALOG = 11;
    public static final int FW_TYPE_ALERT_WINDOW = 12;
    private int float_window_type = 0;
    private IFloatView floatView;
    private boolean isFloatWindowShowing = false;
    private FrameLayout contentView;
    private FloatViewParams floatViewParams;
    private WindowManager windowManager;
    private LastWindowInfo livePlayerWrapper;
    private BaseFloatActivity activity;
    private static int statusBarHeight = 0;

    private int currentPos = 0;

    public FloatWindowManager(int currentPos) {
        this.currentPos = currentPos;
        livePlayerWrapper = LastWindowInfo.getInstance();
    }

    /**
     * 显示悬浮窗口
     */
    public synchronized void showFloatWindow(BaseFloatActivity baseActivity, int floatWindowType,String liveUrl) {
        if (baseActivity == null) {
            return;
        }
        activity = baseActivity;
        Context mContext = baseActivity.getApplicationContext();
        showFloatWindow(baseActivity,mContext, floatWindowType,liveUrl);
    }

    public synchronized void showFloatWindow(Activity activity,Context context, int floatWindowType, String liveUrl) {
        if (context == null) {
            return;
        }
        float_window_type = floatWindowType;
        try {
            isFloatWindowShowing = true;
            initFloatWindow(activity,context,liveUrl);
        } catch (Exception e) {
            e.printStackTrace();
            isFloatWindowShowing = false;
        }
    }

    /**
     * 初始化悬浮窗
     */
    private void initFloatWindow(Activity activity,final Context mContext,String liveUrl) {
        if (mContext == null) {
            return;
        }
        floatViewParams = initFloatViewParams(mContext);
        if (float_window_type == FW_TYPE_ROOT_VIEW) {
            initCommonFloatView(mContext);
        } else {
            initSystemWindow(activity,mContext,liveUrl);
        }
        isFloatWindowShowing = true;
    }

    /**
     * 直接在activity根布局添加悬浮窗
     *
     * @param mContext
     */
    private void initCommonFloatView(Context mContext) {
        if (activity == null || mContext == null) {
            return;
        }
        try {
            floatView = new FloatView(mContext, floatViewParams);
            View rootView = activity.getWindow().getDecorView().getRootView();
            contentView = rootView.findViewById(android.R.id.content);
            contentView.addView((View) floatView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 利用系统弹窗实现悬浮窗
     *
     * @param mContext
     */
    private void initSystemWindow(Activity activity,Context mContext,String liveUrl) {
        windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
        wmParams.packageName = mContext.getPackageName();
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_SCALED
                | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        if (float_window_type == FW_TYPE_APP_DIALOG) {
            //这个一定要activity running
            //wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG;//TYPE_TOAST
            //TYPE_TOAST targetSDK必须小于26
            wmParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        } else if (float_window_type == FW_TYPE_ALERT_WINDOW) {
            //需要权限
            if (Build.VERSION.SDK_INT >= 26) {
                wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            }
        }
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.gravity = Gravity.START | Gravity.TOP;

        wmParams.width = floatViewParams.width;
        wmParams.height = floatViewParams.height;
        wmParams.x = floatViewParams.x;
        wmParams.y = floatViewParams.y;
        wmParams.windowAnimations= android.R.style.Animation_Activity;

        floatView = new FloatWindowView(currentPos,activity,mContext, floatViewParams, wmParams,liveUrl);
        //监听关闭悬浮窗
        floatView.setFloatViewListener(new FloatViewListener() {
            @Override
            public void onClose() {
                floatView.closeSound();
                dismissFloatWindow();
            }

            @Override
            public void onClick() {

            }

            @Override
            public void onMoved() {

            }

            @Override
            public void onDragged() {

            }
        });
        try {
            windowManager.addView((View) floatView, wmParams);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ((FloatWindowView) floatView).setWindowType(float_window_type);
    }

    /**
     * 初始化窗口参数
     *
     * @param mContext
     * @return
     */
    private FloatViewParams initFloatViewParams(Context mContext) {
        FloatViewParams params = new FloatViewParams();
        int screenWidth = DisplayUtils.getScreenWidth(mContext);
        int screenHeight = DisplayUtils.getScreenHeight(mContext);
        int statusBarHeight = getStatusBarHeight(mContext);
        Log.d("dq", "screenWidth=" + screenWidth + ",screenHeight=" + screenHeight + ",statusBarHeight=" + statusBarHeight);
        //根据实际宽高和设计稿尺寸比例适应。
        int marginBottom = dip2px(mContext, 150);
        if (float_window_type == FW_TYPE_ROOT_VIEW) {
            marginBottom += statusBarHeight;
        }
        //设置窗口大小，已view、视频大小做调整
        int winWidth = LastWindowInfo.getInstance().getWidth();
        int winHeight = LastWindowInfo.getInstance().getHeight();
        int margin = dip2px(mContext, 15);
        int width = 0;
        if (winWidth <= winHeight) {
            //竖屏比例
            width = (int) (screenWidth * 1.0f * 1 / 3) + margin;
        } else {//横屏比例
            width = (int) (screenWidth * 1.0f / 2) + margin;
        }
        float ratio = 1.0f * winHeight / winWidth;
        int height = (int) (width * ratio);

        //如果上次的位置不为null，则用上次的位置
        FloatViewParams lastParams = livePlayerWrapper.getLastParams();
        if (lastParams != null) {
            params.width = lastParams.width;
            params.height = lastParams.height;
            params.x = lastParams.x;
            params.y = lastParams.y;
            params.contentWidth = lastParams.contentWidth;
        } else {
            params.width = width;
            params.height = height;
            params.x = screenWidth - width;
            params.y = screenHeight - height - marginBottom;
            params.contentWidth = width;
        }

        params.screenWidth = screenWidth;
        params.screenHeight = screenHeight;
        params.statusBarHeight = statusBarHeight;
        if (float_window_type == FW_TYPE_ROOT_VIEW) {
            initTitleBarHeight(params, statusBarHeight);
            //params.screenHeight = screenHeight - statusBarHeight;
        }
        params.viewMargin = margin;
        params.mMaxWidth = screenWidth / 2 + margin;
        params.mMinWidth = width;
        params.mRatio = ratio;
        return params;
    }

    /**
     * 应用内悬浮窗，边界设定，除去标题栏（96px？），状态栏高度 待优化有actionBar的情况下的移动边界问题
     */
    private void initTitleBarHeight(FloatViewParams params, int statusBarHeight) {
        int titleBarHeight = 0;
        if (activity != null) {
            int contentTop = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
            titleBarHeight = contentTop - statusBarHeight;
            titleBarHeight = titleBarHeight > 0 ? titleBarHeight : 0;
            params.titleBarHeight = titleBarHeight;
            Log.d("dq", "titleBarHeight=" + titleBarHeight);
        }
    }

    public IFloatView getFloatView() {
        return floatView;
    }

    /**
     * 隐藏悬浮视频窗口
     */
    public synchronized void dismissFloatWindow() {
        if (!isFloatWindowShowing) {
            return;
        }
        try {
            isFloatWindowShowing = false;
            if (floatView != null) {
                FloatViewParams floatViewParams = floatView.getParams();
                livePlayerWrapper.setLastParams(floatViewParams);
            }
            removeWindow();

            if (contentView != null && floatView != null) {
                contentView.removeView((View) floatView);
            }
            floatView = null;
            windowManager = null;
            contentView = null;
            activity = null;//防止activity泄漏
            floatView.closeSound();
        } catch (Exception e) {
        }
    }

    private void removeWindow() {
        if (windowManager != null && floatView != null) {
            windowManager.removeViewImmediate((View) floatView);
        }
    }

    public static int getStatusBarHeightByReflect(Context mContext) {
        //int sbHeight;
        if (statusBarHeight > 0) {
            return statusBarHeight;
        }
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int sbHeightId = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = mContext.getResources().getDimensionPixelSize(sbHeightId);
        } catch (Exception e1) {
            e1.printStackTrace();
            statusBarHeight = 0;
        }
        return statusBarHeight;
    }

    public static int getStatusBarHeight(Context mContext) {
        int statusBarHeight = getStatusBarHeightByReflect(mContext);
        if (statusBarHeight == 0) {
            statusBarHeight = dip2px(mContext, 30);
        }
        return statusBarHeight;
    }


    public static int dip2px(Context mContext, float dipValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}