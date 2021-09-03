package com.haoyigou.hyg.config;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.hardware.Camera;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.haoyigou.hyg.application.MApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * 运行环境信息
 * Created by kristain on 15/12/17.
 */
public class  AppConfig {

    /***Log输出标识**/
    private static final String TAG = AppConfig.class.getSimpleName();

    /***屏幕显示材质**/
    private static final DisplayMetrics mDisplayMetrics = new DisplayMetrics();

    /**上下文**/
    private static final Context context = MApplication.gainContext();

    /**操作系统名称(GT-I9100G)***/
    public static final String MODEL_NUMBER = Build.MODEL;

    /**操作系统名称(I9100G)***/
    public static final String DISPLAY_NAME = Build.DISPLAY;

    /**操作系统版本(4.4)***/
    public static final String OS_VERSION = Build.VERSION.RELEASE;

    /**应用程序版本***/
    public static final String APP_VERSION = getVersionName();

    /***屏幕宽度**/
    public static final int SCREEN_WIDTH = getDisplayMetrics().widthPixels;

    /***屏幕高度**/
    public static final int SCREEN_HEIGHT = getDisplayMetrics().heightPixels;

    /***本机手机号码**/
   // public static final String PHONE_NUMBER = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();

    /***设备ID**/
//    public static final String DEVICE_ID = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();

    /***设备IMEI号码**/
   // public static final String IMEI = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getSimSerialNumber();

    /***设备IMSI号码**/
  //  public static final String IMSI = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();

    /***Activity之间数据传输数据对象Key**/
    public static final String ACTIVITY_DTO_KEY = "ACTIVITY_DTO_KEY";

    //wx0022d9b20fcaa97c  wxe471c2d47f8c30e4
    public static final String WX_APP_ID="wxe471c2d47f8c30e4";//
    public static final String AppSecret="ef69f73fa75eb8a628ae49f816327bc8";


    public static final String DB_NAME="xibao";
    public static final int DB_VERSION=1;


    /**获取系统显示材质***/
    public static DisplayMetrics getDisplayMetrics(){
        WindowManager windowMgr = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        windowMgr.getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics;
    }

    /**获取摄像头支持的分辨率***/
    public static List<Camera.Size> getSupportedPreviewSizes(Camera camera){
        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();
        return sizeList;
    }

    /**
     * 获取应用程序版本（versionName）
     * @return 当前应用的版本号
     */
    public static String getVersionName() {
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            Log.e(TAG, "获取应用程序版本失败，原因：" + e.getMessage());
            return "";
        }

        return info.versionName;
    }

    /**
     * 获取应用程序版本（versionName）
     * @return 当前应用的版本号
     */
    public static int getVersionCode() {
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            Log.e(TAG, "获取应用程序版本失败，原因："+e.getMessage());
            return -1;
        }

        return info.versionCode;
    }

    /**
     * 获取系统内核版本
     * @return
     */
    public static String getKernelVersion(){
        String strVersion= "";
        FileReader mFileReader = null;
        BufferedReader mBufferedReader = null;
        try {
            mFileReader = new FileReader("/proc/version");
            mBufferedReader = new BufferedReader(mFileReader, 8192);
            String str2 = mBufferedReader.readLine();
            strVersion = str2.split("\\s+")[2];//KernelVersion

        } catch (Exception e) {
            Log.e(TAG, "获取系统内核版本失败，原因："+e.getMessage());
        }finally{
            try {
                mBufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return strVersion;
    }


    /***
     * 获取MAC地址
     * @return
     */
    public static String getMacAddress(){
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if(wifiInfo.getMacAddress()!=null){
            return wifiInfo.getMacAddress();
        } else {
            return "";
        }
    }

    /**
     * 获取运行时间
     * @return 运行时间(单位/s)
     */
    public static long getRunTimes() {
        long ut = SystemClock.elapsedRealtime() / 1000;
        if (ut == 0) {
            ut = 1;
        }
        return ut;
    }

    /**
     * 判断是否为模拟器环境需要权限
     * <uses-permission android:name="android.permission.READ_PHONE_STATE" />
     * @param mContext 上下文
     * @return
     */
    public static boolean isEmulator(Context mContext) {
        TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceID = telephonyManager.getDeviceId();
        // 如果 运行的 是一个 模拟器
        return deviceID == null || deviceID.trim().length() == 0
                || deviceID.matches("0+");
    }

    /** * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) * * @param context */
    public static void cleanInternalCache(Context context) {
        deleteFilesByDirectory(context.getCacheDir());
    }

    /** * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases) * * @param context */
    public static void cleanDatabases(Context context) {
        deleteFilesByDirectory(new File("/data/data/"
                + context.getPackageName() + "/databases"));
    }

    /**
     * * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs) * * @param
     * context
     */
    public static void cleanSharedPreference(Context context) {
        deleteFilesByDirectory(new File("/data/data/"
                + context.getPackageName() + "/shared_prefs"));
    }

    /** * 按名字清除本应用数据库 * * @param context * @param dbName */
    public static void cleanDatabaseByName(Context context, String dbName) {
        context.deleteDatabase(dbName);
    }

    /** * 清除/data/data/com.xxx.xxx/files下的内容 * * @param context */
    public static void cleanFiles(Context context) {
        deleteFilesByDirectory(context.getFilesDir());
    }

    /**
     * * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache) * * @param
     * context
     */
    public static void cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            deleteFilesByDirectory(context.getExternalCacheDir());
        }
    }

    /** * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除 * * @param filePath */
    public static void cleanCustomCache(String filePath) {
        deleteFilesByDirectory(new File(filePath));
    }

    /** * 清除本应用所有的数据 * * @param context * @param filepath */
    public static void cleanApplicationData(Context context, String... filepath) {
        cleanInternalCache(context);
        cleanExternalCache(context);
        cleanDatabases(context);
        cleanSharedPreference(context);
        cleanFiles(context);
        for (String filePath : filepath) {
            cleanCustomCache(filePath);
        }
    }

    /** * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * * @param directory */
    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }
}
