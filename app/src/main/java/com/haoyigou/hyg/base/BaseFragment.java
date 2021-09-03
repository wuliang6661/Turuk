package com.haoyigou.hyg.base;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.haoyigou.hyg.application.GlobalApplication;
import com.haoyigou.hyg.utils.LogUtils;
import com.haoyigou.hyg.utils.SharedPreferencesUtils;
import com.haoyigou.hyg.view.widget.LoadingProgressDialog;

/**
 * Created by wuliang on 2016/11/25.
 * <p>
 * 所有fangment的父类
 */

public class BaseFragment extends Fragment {

    static LoadingProgressDialog proDialog = null;

    public String disId;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogUtils.init(getActivity());
        disId = SharedPreferencesUtils.getInstance().getString("distributorId", "1");
    }


    /**
     * 跳转activity
     */
    public void goToActivity(Class<?> cls, boolean isFinish) {
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
        if (isFinish)
            getActivity().finish();
    }


    /***
     * 传值跳转页面
     */
    public void goToActivity(Class<?> cls, Bundle bundle, boolean isFinish) {
        Intent intent = new Intent(getActivity(), cls);
        intent.putExtras(bundle);
        startActivity(intent);
        if (isFinish)
            getActivity().finish();
    }


    /***
     * 显示toast弹窗
     */
    public void showToast(String message) {
        if(message==null){

        }else {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }

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
    public void onDestroy() {
        super.onDestroy();
        stopProgressDialog();
    }
}
