package com.haoyigou.hyg.wxapi;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.entity.Constants;
import com.haoyigou.hyg.ui.WebActivity;
import com.haoyigou.hyg.utils.HttpUtils;
import com.haoyigou.hyg.utils.LogUtils;
import com.haoyigou.hyg.view.widget.LoadingProgressDialog;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class WxPayTask extends AsyncTask<Map<String, Object>, Void, JSONObject> {

    LoadingProgressDialog proDialog = null;
    IWXAPI msgApi;
    private String status;
    private String reason;
    private String url;
    private Context mContext;


    public WxPayTask(Context context, int type) {
        msgApi = WXAPIFactory.createWXAPI(context, null);
        mContext = context;
        switch (type) {
            case 0:
                url = HttpClient.appwxpaysign_url;
                break;
            case 1:
                url = HttpClient.appwxpaysign_url1;
                break;
            case 2:
                url = HttpClient.appwxpaysign_url2;
                break;
        }
    }

    @Override
    protected JSONObject doInBackground(Map<String, Object>... params) {
//        String url = HttpClient.appwxpaysign_url;
        Log.e("EEURL", url);
        Map<String, Object> map = params[0];
        String content;
        JSONObject jsons = null;
        try {
            content = HttpUtils.send(url, map);
            Log.e("wuliang", content);
            jsons = new JSONObject(content);
            Log.e("orion", content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Log.e("FFFDD",jsons.toString());
        return jsons;

    }


    protected void onPostExecute(JSONObject result) {
        PayReq req = new PayReq();
        stopProgressDialog();
        try {
            if (result == null || result.equals("")) return;
            status = result.getString("status");
            if (status.equals("1")) {
                req.appId = result.getString("appid");
                Log.e("DDDFFF", req.appId.toString());
                req.partnerId = result.getString("partnerid");
                req.prepayId = result.getString("prepayid");
                req.nonceStr = result.getString("noncestr");
                req.timeStamp = result.getString("timestamp");
                req.packageValue = result.getString("package");
                req.sign = result.getString("sign");
                Constants.orderNum = result.getString("ordernum");
                Thread.sleep(1000);
                msgApi.registerApp(req.appId);
                msgApi.sendReq(req);
            } else if (status.equals("2")){
                Bundle bundle = new Bundle();
                bundle.putString("orderNum", result.getString("ordernum"));
                Intent intent = new Intent(mContext, WebActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }else {
                reason = result.optString("data");
                if (reason != null) {
                    LogUtils.showToast(reason.toString(), false);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void startProgressDialog(String progressMsg, Context mcontext) {
        if (proDialog == null) {
            proDialog = LoadingProgressDialog.createDialog(mcontext);
            proDialog.setMessage(progressMsg);
            proDialog.setCanceledOnTouchOutside(false);
        }
        proDialog.show();
    }

    public void stopProgressDialog() {
        if (proDialog != null) {
            proDialog.dismiss();
            proDialog = null;
        }
    }
}
