package com.haoyigou.hyg.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.application.MApplication;
import com.haoyigou.hyg.ui.homeweb.HomeWebViewAct;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.entity.PromotionsPopEntity;
import com.haoyigou.hyg.base.BaseActivity;
import com.haoyigou.hyg.utils.SharedPreferencesUtils;
import com.haoyigou.hyg.utils.StringUtils;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * 首页的一个优惠活动弹窗
 */

public class PromotionsPopActivity extends BaseActivity implements View.OnClickListener {

    private ImageView im_delete;
    private ImageView im_load;
    String businesslogo = "";
    String businessid = "";
    int hastab = 0;
    String tablogo = "";
    String tabId = "";
    String tabTitle;
    String taburl;
    String disid;

    private boolean isYOUHUI = true;   //判断是否显示的优惠券弹窗，默认为优惠弹窗，false为活动弹窗


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_discounts_pop);
        disid = SharedPreferencesUtils.getInstance().getString("distributorId", "1");
        initview();
        showPromotion();
    }

    /***
     * 显示优惠券弹窗
     */
    private void showPromotion() {
        String data = getIntent().getStringExtra("data");
        JSONObject object = JSON.parseObject(data);
        int hasGetDiscount = object.getInteger("hasGetDiscount");
        hastab = object.getInteger("hastab");
        im_delete.setVisibility(View.VISIBLE);
        im_load.setVisibility(View.VISIBLE);
        if (hasGetDiscount == 1) {
            businessid = object.getInteger("businessId").toString();
            businesslogo = object.getString("businesslogo");
            isYOUHUI = true;
            Picasso.with(PromotionsPopActivity.this).load(businesslogo).into(im_load);
        }
        if (hastab == 1) {
            tabTitle = object.getString("tabTitle");
            tabId = object.getString("tabId");
            tablogo = object.getString("tablogo");
            taburl = object.getString("taburl");
        }
        if (hasGetDiscount == 0 && hastab == 1) {
            isYOUHUI = false;
            Picasso.with(PromotionsPopActivity.this).load(tablogo).into(im_load);
        }
    }


    private void initview() {
        im_delete = (ImageView) findViewById(R.id.delete_btn);
        im_load = (ImageView) findViewById(R.id.load_image);
        im_delete.setOnClickListener(this);
        im_load.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.delete_btn:
                if (isYOUHUI) {
                    if (hastab == 1) {
                        isYOUHUI = false;
                        Picasso.with(PromotionsPopActivity.this).load(tablogo).into(im_load);
                    } else {
                        finish();
                    }
                } else {
                    finish();
                }
                break;
            case R.id.load_image:
                if (isYOUHUI) {
                    loadReason();
                } else {
                    if (!StringUtils.isEmpty(taburl)) {
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("all", true);
                        bundle.putString("url", taburl);
                        goToActivity(HomeWebViewAct.class, bundle, true);
                    } else {
                        MApplication.labelParentLocation = "100";
                        Bundle bundle = new Bundle();
                        bundle.putString("tabTitle", tabTitle);
                        bundle.putString("fromTabAc", "1");
                        bundle.putString("productTabId", tabId);
                        goToActivity(LabelActivity.class, bundle, true);
                    }
                }
                break;
        }
    }


    //判断返回的状态是否为1 跳到webview

    private void loadReason() {
        startProgressDialog("加载中",PromotionsPopActivity.this);
        PromotionsPopEntity param = new PromotionsPopEntity();
        param.setBusinessid(businessid);
        param.setSource("1");
        HttpClient.popaddress(param, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String body) {
//                Log.e("getReason", "返回数据:" + body);
                stopProgressDialog();
                JSONObject object = JSON.parseObject(body);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    //返回的状态为1的化 跳转到优惠卷
                    showYouhuiMessage();
                } else if (HttpClient.UNLOGIN_CODE.equals(object.getString("status"))) {
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                stopProgressDialog();
            }
        }, PromotionsPopActivity.this);
    }

    /***
     * 优惠卷弹窗点击之后的选项
     */
    private void showYouhuiMessage() {
        final Dialog dialog = new Dialog(PromotionsPopActivity.this, R.style.custom_dialog);
        View view = LayoutInflater.from(PromotionsPopActivity.this).inflate(R.layout.youhui_dialog_layout, null);
        TextView text = (TextView) view.findViewById(R.id.commit);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
//        alertDialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (isYOUHUI) {
                    if (hastab == 1) {
                        isYOUHUI = false;
                        Picasso.with(PromotionsPopActivity.this).load(tablogo).into(im_load);
                    } else {
                        finish();
                    }
                } else {
                    finish();
                }
            }
        });
        dialog.show();
    }
}
