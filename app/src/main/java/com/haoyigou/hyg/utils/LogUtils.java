package com.haoyigou.hyg.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

/**
 * 日志工具类
 * Created by kristain on 15/12/17.
 */
public class LogUtils {

    private static final String TAG = "LogUtils";
    private static Context context;

    public static void init(Context con) {
        LogUtils.context = con;
    }

    /**
     * 上线后关闭log
     */
    private static final Boolean DEBUG = true;

    public static void d(String tag, String msg) {
        if (DEBUG) {
            tag = Thread.currentThread().getName() + ":" + tag;
            Log.d(TAG, tag + " : " + msg);
        }
    }

    public static void d(String tag, String msg, Throwable error) {
        if (DEBUG) {
            tag = Thread.currentThread().getName() + ":" + tag;
            Log.d(TAG, tag + " : " + msg, error);
        }
    }

    public static void i(String tag, String msg) {
        if (DEBUG) {
            tag = Thread.currentThread().getName() + ":" + tag;
            Log.i(TAG, tag + " : " + msg);
        }
    }

    public static void i(String tag, String msg, Throwable error) {
        if (DEBUG) {
            tag = Thread.currentThread().getName() + ":" + tag;
            Log.i(TAG, tag + " : " + msg, error);
        }
    }

    public static void w(String tag, String msg) {
        if (DEBUG) {
            tag = Thread.currentThread().getName() + ":" + tag;
            Log.w(TAG, tag + " : " + msg);
        }
    }

    public static void w(String tag, String msg, Throwable error) {
        if (DEBUG) {
            tag = Thread.currentThread().getName() + ":" + tag;
            Log.w(TAG, tag + " : " + msg, error);
        }
    }

    public static void e(String tag, String msg) {
        if (DEBUG) {
            tag = Thread.currentThread().getName() + ":" + tag;
            Log.e(TAG, tag + " : " + msg);
        }
    }

    public static void e(String tag, String msg, Throwable error) {
        if (DEBUG) {
            tag = Thread.currentThread().getName() + ":" + tag;
            Log.e(TAG, tag + " : " + msg, error);
        }
    }

    public static void showToast(String message) {
        if (context == null) {
//        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            return;
        }
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setTitle("提示");
        dialog.setMessage(message);
        dialog.setButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public static void showToast(String message, boolean isToast) {
        Toast.makeText(WindowUtils.getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
