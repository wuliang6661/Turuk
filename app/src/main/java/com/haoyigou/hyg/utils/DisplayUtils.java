package com.haoyigou.hyg.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.haoyigou.hyg.R;
import com.haoyigou.hyg.config.AppConfig;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.apache.http.client.HttpClient;

import java.lang.reflect.Method;

/**
 * 单位换算工具类
 *  px  ：像素 <br>
    in  ：英寸<br>
    mm  ：毫米<br>
    pt  ：磅，1/72 英寸<br>
    dp  ：一个基于density的抽象单位，如果一个160dpi的屏幕，1dp=1px<br>
    dip ：等同于dp<br>
    sp  ：同dp相似，但还会根据用户的字体大小偏好来缩放。<br>
    建议使用sp作为文本的单位，其它用dip<br>
    布局时尽量使用单位dip，少使用px <br>
 * Created by kristain on 15/12/17.
 *
 */
public class DisplayUtils {

    public static ImageLoader mImageLoader = ImageLoader.getInstance();

    /**
     * 获取当前分辨率下指定单位对应的像素大小（根据设备信息）
     * px,dip,sp -> px
     *
     * Paint.setTextSize()单位为px
     *
     * 代码摘自：TextView.setTextSize()
     *
     * @param unit  TypedValue.COMPLEX_UNIT_*
     * @param size
     * @return
     */
    public static float getRawSize(Context mContext, int unit, float size) {
        Resources r;
        if (mContext == null)
            r = Resources.getSystem();
        else
            r = mContext.getResources();

        return TypedValue.applyDimension(unit, size, r.getDisplayMetrics());
    }

    /**设备显示材质**/
    private static DisplayMetrics mDisplayMetrics = AppConfig.getDisplayMetrics();

    /**
     * sp转换px
     * @param spValue sp数值
     * @return px数值
     */
    public static int spTopx(float spValue) {
        return (int) (spValue * mDisplayMetrics.scaledDensity + 0.5f);
    }

    /**
     * px转换sp
     * @param pxValue px数值
     * @return sp数值
     */
    public static int pxTosp(float pxValue) {
        return (int) (pxValue / mDisplayMetrics.scaledDensity + 0.5f);
    }

    /**
     * dip转换px
     * @param dipValue dip数值
     * @return px数值
     */
    public static int dipTopx(int dipValue) {
        return (int) (dipValue * mDisplayMetrics.density + 0.5f);
    }

    /**
     * px转换dip
     * @param pxValue px数值
     * @return dip数值
     */
    public static int pxTodip(float pxValue) {
        return (int) (pxValue / mDisplayMetrics.density + 0.5f);
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param context 上下文
     * @param dpValue 尺寸dip
     * @return 像素值
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param context 上下文
     * @param pxValue 尺寸像素
     * @return DIP值
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 sp
     *
     * @param context 上下文
     * @param pxValue 尺寸像素
     * @return SP值
     */
    public static int px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 sp 的单位 转成为 px
     *
     * @param context 上下文
     * @param spValue SP值
     * @return 像素值
     */
    public static int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取dialog宽度
     *
     * @param activity Activity
     * @return Dialog宽度
     */
    public static int getDialogW(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = activity.getResources().getDisplayMetrics();
        int w = dm.widthPixels - 100;
        // int w = aty.getWindowManager().getDefaultDisplay().getWidth() - 100;
        return w;
    }

    /**
     * 获取屏幕宽度
     *
     * @param activity Activity
     * @return 屏幕宽度
     */
    public static int getScreenW(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = activity.getResources().getDisplayMetrics();
        int w = dm.widthPixels;
        // int w = aty.getWindowManager().getDefaultDisplay().getWidth();
        return w;
    }

    /**
     * 获取屏幕高度
     *
     * @param activity Activity
     * @return 屏幕高度
     */
    public static int getScreenH(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = activity.getResources().getDisplayMetrics();
        int h = dm.heightPixels;
        // int h = aty.getWindowManager().getDefaultDisplay().getHeight();
        return h;
    }

    /**
     * Toggle keyboard If the keyboard is visible,then hidden it,if it's
     * invisible,then show it
     *
     * @param context 上下文
     */
    public static void toggleKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

//    public static void displayLowQualityInImage(String url, ImageView view) {
//        if (view == null) {
//            return;
//        }
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .cacheInMemory(true)
//                .cacheOnDisk(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                .showImageOnLoading(R.drawable.image_show_default)
//                .build();
//        mImageLoader.displayImage(HttpClient.HTTP_UPLOAD_IMG_URL + url, view, options);
//    }

    public static int getScreenWidth(Context context){
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }


    //获取屏幕原始尺寸高度，包括虚拟功能键高度
    public static int getDpi(Context context){
        int dpi = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics",DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            dpi=displayMetrics.heightPixels;
        }catch(Exception e){
            e.printStackTrace();
        }
        return dpi;
    }

    /**
     * 获取 虚拟按键的高度
     * @param context
     * @return
     */
    public static  int getBottomStatusHeight(Context context){
        int totalHeight = getDpi(context);

        int contentHeight = getScreenHeight(context);

        return totalHeight  - contentHeight;
    }

    /**
     * 标题栏高度
     * @return
     */
    public static int getTitleHeight(Activity activity){
        return  activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context)
    {

        int statusHeight = -1;
        try
        {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return statusHeight;
    }


    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context)
    {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }
}
