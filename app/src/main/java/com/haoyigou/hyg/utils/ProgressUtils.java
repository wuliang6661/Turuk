package com.haoyigou.hyg.utils;

import android.content.Context;

import com.haoyigou.hyg.view.widget.LoadingProgressDialog;

/**
 * Created by wuliang on 2016/12/14.
 * <p>
 * 等待loding
 */

public class ProgressUtils {

    static LoadingProgressDialog proDialog = null;

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
}
