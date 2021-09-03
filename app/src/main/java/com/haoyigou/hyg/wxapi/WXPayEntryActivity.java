package com.haoyigou.hyg.wxapi;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.config.StateMessage;
import com.haoyigou.hyg.entity.Constants;
import com.haoyigou.hyg.ui.OrderMessageAct;
import com.haoyigou.hyg.ui.VoucherRecordAct;
import com.haoyigou.hyg.ui.WebActivity;
import com.haoyigou.hyg.ui.WebviewActivity;
import com.haoyigou.hyg.utils.LogUtils;
import com.haoyigou.hyg.utils.SharedPreferencesUtils;
import com.haoyigou.hyg.view.widget.LoadingProgressDialog;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.WXPayEntryActivity";
    private IWXAPI api;
    static LoadingProgressDialog proDialog = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
        Log.e("EEDDCC", api.toString());
        stopProgressDialog();
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
        stopProgressDialog();
    }

    @Override
    public void onReq(BaseReq req) {

        //startProgressDialog("加载中",this);
    }

    @Override
    public void onResp(BaseResp resp) {
            if (StateMessage.isVoucher == 1) {    //从充值(话费)页面进入
                if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
                    if (resp.errCode == 0) {   //成功
                        LogUtils.showToast("话费充值成功，将在5-10分钟内到账，请您耐心等待。", false);
                        StateMessage.isVoucher = -1;
                        Intent intent = new Intent(this, VoucherRecordAct.class);
                        startActivity(intent);
                    } else{
                        LogUtils.showToast("亲，付款未成功，请稍后重试！", false);
                        //清空活动
                        StateMessage.payOrderNum = "";
                    }
                }
                finish();
            } else if (StateMessage.isVoucher == 2) {//从充值(流量)页面进入
                if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
                    if (resp.errCode == 0) {   //成功
                        LogUtils.showToast("流量充值成功，将在5-10分钟内到账，请您耐心等待。", false);
                        StateMessage.isVoucher = -1;
                        Intent intent = new Intent(this, VoucherRecordAct.class);
                        startActivity(intent);
                    } else {
                        LogUtils.showToast("亲，付款未成功，请稍后重试！", false);
                    }
                }
                finish();
            } else if (StateMessage.isPinTuan) {
//            StateMessage.isPinTuan = false;
//            String url = StateMessage.PinTuanUrl;
//            Intent intent = new Intent(this, WebviewActivity.class);
//            intent.putExtra("url", url);
//            startActivity(intent);
//            finish();
                if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
                    if (resp.errCode == 0) {   //成功
                        Bundle bundle = new Bundle();
                        bundle.putString("orderNum", null);
                        Intent intent = new Intent(this, WebActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        LogUtils.showToast("亲，付款未成功，请稍后重试！", false);
                        StateMessage.isPinTuan = false;
                        String url = StateMessage.PinTuanUrl;
                        Intent intent = new Intent(this, WebviewActivity.class);
                        intent.putExtra("url", url);
                        startActivity(intent);
                    }
                    finish();
                }
            } else if (StateMessage.exchangeOrder){//兑换商城
                if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
                    Intent intent = new Intent(this, WebActivity.class);
                    if (resp.errCode == 0) {   //成功
                        Bundle bundle = new Bundle();
                        bundle.putString("exchangeUrl", HttpClient.HTTP_DOMAIN + "/exchange/record/index?distributorId=" + SharedPreferencesUtils.getInstance().getString("distributorId", ""));

                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString("exchangeUrl", HttpClient.HTTP_DOMAIN + "/exchange/index");
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    StateMessage.exchangeOrder = false;
                    finish();
                }
            }else {
//            Bundle bundle = new Bundle();
//            bundle.putString("orderNum", Constants.orderNum);
//            Intent intent = new Intent(this, OrderMessageAct.class);
//            intent.putExtras(bundle);
//            startActivity(intent);
//            finish();

                if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
                    if (resp.errCode == 0) {   //成功
                        Bundle bundle = new Bundle();
                        bundle.putString("orderNum", Constants.orderNum);
                        Intent intent = new Intent(this, WebActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        LogUtils.showToast("亲，付款未成功，请稍后重试！", false);
                        Bundle bundle = new Bundle();
                        bundle.putString("orderNum", Constants.orderNum);
                        Intent intent = new Intent(this, OrderMessageAct.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    finish();
                }
            }
        }
    }